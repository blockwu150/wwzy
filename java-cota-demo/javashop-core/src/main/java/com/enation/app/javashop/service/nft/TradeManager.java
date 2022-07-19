package com.enation.app.javashop.service.nft;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.client.member.DepositeClient;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.rabbitmq.MessageSender;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.vo.MemberDepositeVO;
import com.enation.app.javashop.model.nft.dos.*;
import com.enation.app.javashop.service.member.DepositeManager;
import com.enation.app.javashop.service.nft.plugin.CKBAccountUtil;
import com.enation.app.javashop.util.DESUtil;
//import org.nervos.ckb.indexer.Receiver;
import org.nervos.ckb.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.enation.app.javashop.service.nft.plugin.CKBAccountUtil.UnitCKB;

@Service
public class TradeManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private NftConfigMapper nftConfigMapper;
    @Autowired
    private NftTransferMapper nftTransferMapper;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    private NftMessageMapper nftMessageMapper;
    @Autowired
    private ConversationMapper conversationMapper;
    @Autowired
    private NftSellMapper nftSellMapper;
//    @Autowired
//    private NftObjectMapper nftObjectMapper;
    @Autowired
    private NftMemberMapper nftMemberMapper;
    @Autowired
    private NftJobManager nftJobManager;
    @Autowired
    private DepositeClient depositeClient;
    @Autowired
    private DepositeManager depositeManager;
    @Autowired
    private NftStateManager nftStateManager;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private Cache cache;



    private Buyer getCurrentMember(){
        //获取当前登录的用户信息
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }
        return buyer;
    }

    public void onSell(String cotaId,String tokenIndex,String name,String image,String description ,Double price) {
        Long memberId = getCurrentMember().getUid();
        //30分钟内有转账NFT操作的不得出售
        int onTransferCount = nftTransferMapper.selectCount(new QueryWrapper<NftTransfer>().eq("cota_id",cotaId)
                .eq("token_index",tokenIndex)
                .gt("create_time",System.currentTimeMillis()/1000 - 30*60));
        if(onTransferCount > 0) {
            throw new ServiceException(MemberErrorCode.E110.code(), "此NFT30分钟内有转账操作，不能出售!");
        }
        //无创世高合不能流通
        if(nftStateManager.getGenesisNft(memberId).size() + nftStateManager.getCompNft(memberId).size() ==0) {
            throw new ServiceException(MemberErrorCode.E110.code(),"拥有创世高合NFT才能流通！");
        }
        MemberDepositeVO deposite = depositeClient.getDepositeVO(memberId);
        Double total = deposite.getBalance();
        Map payload = nftStateManager.getSellPayload(memberId);
        Integer fee = (Integer) payload.get("price");
        if(total < fee) {
            throw new ServiceException(MemberErrorCode.E110.code(),"余额不足");
        }

        NftMemberDO n = nftMemberMapper.selectById(memberId);
        if(n.getRegistry() == false) {
            throw new ServiceException(MemberErrorCode.E110.code(), "非激活用户请先激活！");
        }
        String address = n.getCotaAddress();
        nftJobManager.checkLockHash(memberId,address,cotaId,tokenIndex);
        int sellCount = nftSellMapper.selectCount(new QueryWrapper<NftSell>().eq("cota_id",cotaId)
                .eq("token_index",tokenIndex)
                .notIn("status",NftSell.STAUS_CANCEL,NftSell.STAUS_COMPLETE));
        if(sellCount > 0) {
            throw new ServiceException(MemberErrorCode.E110.code(), "出售中,不得反复上架销售！");
        }
        

        NftSell ns = new NftSell();
        ns.setSellerId(memberId);
        ns.setCotaId(cotaId);
        ns.setTokenIndex(tokenIndex);
        ns.setName(name);
        ns.setImage(image);
        ns.setDescription(description);
        ns.setPrice(price);
        if(fee > 0) {
            ns.setFee(fee);
        }
        if(payload.get("state") != null) {
            NftState state = (NftState) payload.get("state");
            ns.setStateId(state.getNftKey());
            nftStateManager.increaseSell(state);
        }
        nftSellMapper.insert(ns);
        depositeManager.reduce(fee.doubleValue() ,memberId,"流通 cotaId :" + cotaId + " tokenIndex:" + tokenIndex);
    }

    public synchronized void matchSell(Long  sellId,String pwd) throws IOException {
        NftSell s = nftSellMapper.selectById(sellId);

        if (!s.getStatus().equals(NftSell.STAUS_OPEN)){
            throw new ServiceException(MemberErrorCode.E110.code(), "状态已变，请重新选择！");
        }
        Long memberId = getCurrentMember().getUid();
        if (s.getSellerId().equals(memberId)){
            throw new ServiceException(MemberErrorCode.E110.code(), "不能买自己的挂卖的NFT！");
        }
        //检测支付密码是否正确
        this.depositeClient.checkPwd(memberId,pwd);

        Map<String, Object> cipher = nftMemberMapper.getCipher(memberId);
        String buyerPrivateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String buyerAddress = cipher.get("cota_address").toString();

        cipher = nftMemberMapper.getCipher(s.getSellerId());
        String sellerPrivateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String sellerAddress = cipher.get("cota_address").toString();
        nftJobManager.checkLockHash(s.getSellerId(),sellerAddress,s.getCotaId(),s.getTokenIndex());
        int buyerAmount = CKBAccountUtil.getBalance(buyerAddress).intValue();
        if(buyerAmount - 215 < s.getPrice() ) {
            throw new ServiceException(MemberErrorCode.E110.code(),"您的余额不足，215个ckb是运行Cota合约的最低保障，不能提取！");
        }
        //读取cotacell的签名
        NftConfig cfg = nftConfigMapper.selectById(1l);
        cipher = nftMemberMapper.getCipher(cfg.getManagerId());
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        Set<String> privateKeys = new HashSet<>();
        privateKeys.add(privateKey);
        privateKeys.add(buyerPrivateKey);

        Map<String,Long> receivers = new HashMap<>();
        receivers.put(sellerAddress, Utils.ckbToShannon(s.getPrice()));
        String txHash = CKBAccountUtil.sendCapacity(receivers, buyerAddress, privateKeys);
        logger.debug("购买转账txHASH:" + txHash);
        Transaction tx = new Transaction();
        tx.setType(Transaction.TX_TYPE_CKB_TRANSFER);
        tx.setTxHash(txHash);
        transactionMapper.insert(tx);
        Transaction tx1 = new Transaction();
        tx1.setType(Transaction.TX_TYPE_NFT_TRANSFER);
        transactionMapper.insert(tx1);
        s.setCkbTxId(tx.getTransactionId());
        s.setNftTxId(tx1.getTransactionId());
        s.setBuyerId(memberId);
        s.setStatus(NftSell.STAUS_PROCESSING);
        nftSellMapper.updateById(s);
        JSONObject obj = nftJobManager.transfer(s.getSellerId(),buyerAddress,s.getCotaId(),s.getTokenIndex());
        tx1.setTxHash(obj.get("txHash").toString());
        transactionMapper.updateById(tx1);
        nftSellMapper.updateById(s);
    }

//    public synchronized void matchSell(Long  sellId) {
//        NftSell s = nftSellMapper.selectById(sellId);
//        if (!s.getStatus().equals(NftSell.STAUS_OPEN)){
//            throw new ServiceException(MemberErrorCode.E110.code(), "状态已变，请重新选择！");
//        }
//        Long memberId = getCurrentMember().getUid();
//        if (s.getSellerId().equals(memberId)){
//            throw new ServiceException(MemberErrorCode.E110.code(), "不能买自己的挂卖的NFT！");
//        }
//        Conversation c = new Conversation();
//        conversationMapper.insert(c);
//        s.setConversationId(c.getId());
//        s.setBuyerId(memberId);
//        s.setStatus(NftSell.STAUS_PROCESSING);
//        nftSellMapper.updateById(s);
//        this.messageSender.send(new MqMessage(AmqpExchange.NFT_BUYER_IN, AmqpExchange.NFT_BUYER_IN + "_ROUTING", s));
//    }


    public void pay(Long  sellId) {
        Long memberId = getCurrentMember().getUid();
        NftSell s = nftSellMapper.selectById(sellId);
        if(s.getStatus() !=NftSell.STAUS_PROCESSING )
            throw new ServiceException(MemberErrorCode.E110.code(), "非处理状态，不能支付！");
        s.setStatus(NftSell.STAUS_BUYER_PAID);
        nftSellMapper.updateById(s);
    }
    public void receiveMoney(Long  sellId) {
        Long memberId = getCurrentMember().getUid();
        NftSell s = nftSellMapper.selectById(sellId);
        if(s.getStatus() !=NftSell.STAUS_BUYER_PAID )
            throw new ServiceException(MemberErrorCode.E110.code(), "买家还未支付，不能收款！");
        s.setStatus(NftSell.STAUS_SELLER_RECEIVE_MONEY);
        nftSellMapper.updateById(s);
    }
    public void cancel(Long  sellId) {
//        Long memberId = getCurrentMember().getUid();
//        NftSell s = nftSellMapper.selectById(sellId);
//        if(s.getConversationId() != null ) {
//            if(System.currentTimeMillis()/1000 -conversationMapper.selectById(s.getConversationId()).getCreateTime() < 1800 ){
//                if(!s.getStatus().equals(NftSell.STAUS_OPEN) && s.getSellerId().equals(memberId) ) {
//                    throw new ServiceException(MemberErrorCode.E110.code(), "卖家不能取消！");
//                }
//            };
//        }
//        if(s.getStatus().equals(NftSell.STAUS_CANCEL)){
//            throw new ServiceException(MemberErrorCode.E110.code(), "已经是取消状态！");
//        }
//        if(s.getStatus().equals(NftSell.STAUS_BUYER_PAID)) {
//            s.setStatus(NftSell.STAUS_AUDIT);
//        }else if(s.getStatus().equals(NftSell.STAUS_OPEN)) {
//            s.setStatus(NftSell.STAUS_CANCEL);
//            //退款及撤销创世高合使用次数
//            if(s.getFee() >0) {
//                depositeManager.increase(s.getFee().doubleValue() ,s.getSellerId(),"撤销流通 cotaId :" + s.getCotaId() + " tokenIndex:" + s.getTokenIndex() );
//            }
//            if(s.getStateId() != null) {
//                nftStateManager.reduceSell(s.getStateId());
//            }
//        }else {
//            s.setStatus(NftSell.STAUS_OPEN);
//            s.setConversationId(null);
//        }
//        nftSellMapper.updateById(s);

        NftSell s = nftSellMapper.selectById(sellId);
        if(s.getStatus().equals(NftSell.STAUS_OPEN)) {
            s.setStatus(NftSell.STAUS_CANCEL);
            //退款及撤销创世高合使用次数
            if(s.getFee() >0) {
                depositeManager.increase(s.getFee().doubleValue() ,s.getSellerId(),"撤销流通 cotaId :" + s.getCotaId() + " tokenIndex:" + s.getTokenIndex() );
            }
            if(s.getStateId() != null) {
                nftStateManager.reduceSell(s.getStateId());
            }
            nftSellMapper.updateById(s);
        }

    }
    public IPage<Map> listMySells(Integer pageNo, Integer pageSize) {
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Long memberId = getCurrentMember().getUid();
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq(memberId!=null,"seller_id",memberId);
        wrapper.orderByDesc("sell_id");
        return nftSellMapper.pageSell(page, wrapper);
    }
    public IPage<Map> listMyBuys(Integer pageNo, Integer pageSize) {
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        Long memberId = getCurrentMember().getUid();
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq(memberId!=null,"buyer_id",memberId);
        wrapper.orderByDesc("sell_id");
        return nftSellMapper.pageSell(page, wrapper);
    }

    public IPage<Map> listMarket(Integer pageNo, Integer pageSize) {
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("status",NftSell.STAUS_OPEN);
        wrapper.orderByDesc("sell_id");
        return nftSellMapper.pageSell(page, wrapper);
    }

    public IPage<Map> listMsgs(Integer pageNo, Integer pageSize,Long sellId) {
        NftSell sell = nftSellMapper.selectById(sellId);
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
//        Long memberId = getCurrentMember().getUid();
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("s.conversation_id",sell.getConversationId());
//        wrapper.orderByDesc("sell_id");
        return nftMessageMapper.pageMsg(page, wrapper);
    }
    public NftSell sellInfo(Long sellId) {
        NftSell sell = nftSellMapper.selectById(sellId);
       return sell;
    }

    public void sendMsg(Long sellId,Integer type,String content) {
        NftSell sell = nftSellMapper.selectById(sellId);
        Long conversationId = sell.getConversationId();
        Long memberId = getCurrentMember().getUid();
        Message msg = new Message(memberId,conversationId,type,content);
        nftMessageMapper.insert(msg);
    }

    public void cronOnChain() {
        Boolean isDoing = (Boolean) cache.get(CachePrefix.TRADE+"_IS_NFT_SELLS_ON_CHAIN");
        if(isDoing == null || isDoing == false) {
            cache.put(CachePrefix.TRADE+"_IS_NFT_SELLS_ON_CHAIN",Boolean.TRUE,1000);
            List<NftSell> sells = nftSellMapper.selectList(new QueryWrapper<NftSell>().eq("status", NftSell.STAUS_SELLER_RECEIVE_MONEY));
            for (NftSell sell:sells) {
                onChain(sell);
            }
        }
        cache.vagueDel(CachePrefix.TRADE+"_IS_NFT_SELLS_ON_CHAIN" );
    }
    public String onChain(NftSell sell) {
        String ret = null;
        String address = nftMemberMapper.selectById(sell.getBuyerId()).getCotaAddress();
        int retries = 3;
        while (retries > 0) {
            try {
                JSONObject obj = nftJobManager.transfer(sell.getSellerId(), address, sell.getCotaId(), sell.getTokenIndex());
                ret = obj.get("txHash").toString();
                sell.setStatus(NftSell.STAUS_COMPLETE);
                sell.setTxHash(ret);
                nftSellMapper.updateById(sell);
                retries=0;
            } catch (Exception e) {
                e.printStackTrace();
                retries --;
            }
        }
        return ret;
    }


}
