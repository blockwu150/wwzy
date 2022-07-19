package com.enation.app.javashop.service.nft;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.client.member.DepositeClient;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.model.member.vo.MemberDepositeVO;
import com.enation.app.javashop.model.nft.dos.*;
import com.enation.app.javashop.service.member.DepositeManager;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.nft.plugin.CKBAccountUtil;
import com.enation.app.javashop.service.nft.plugin.CotaNftPlugin;
import com.enation.app.javashop.util.DESUtil;
import org.nervos.ckb.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.enation.app.javashop.service.nft.plugin.CKBAccountUtil.UnitCKB;

@Service
public class NftMemberManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    NftTransferMapper nftTransferMapper;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    private LockHashMapper lockHashMapper;
    @Autowired
    private ConversationMapper conversationMapper;
    @Autowired
    private NftSellMapper nftSellMapper;
    @Autowired
    private NftObjectMapper nftObjectMapper;
    @Autowired
    private UploadApplyMapper uploadApplyMapper;
    @Autowired
    private NftPointMapper nftPointMapper;
    @Autowired
    private NftConfigMapper nftConfigMapper;
    @Autowired
    private NftMemberMapper nftMemberMapper;
    @Autowired
    private NftAlbumMapper nftAlbumMapper;
    @Autowired
    private NftCollectionMapper nftCollectionMapper;
    @Autowired
    private NftFragmentMapper nftFragmentMapper;
    @Autowired
    private NftOrderMapper nftOrderMapper;
    @Autowired
    private NftFragmentManager nftFragmentManager;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private NftJobManager nftJobManager;
    @Autowired
    private NftStateManager nftStateManager;
    @Autowired
    private DepositeManager depositeManager;
    @Autowired
    private DepositeClient depositeClient;
    @Autowired
    private CotaNftPlugin cotaNftPlugin;



    private Buyer getCurrentMember(){
        //获取当前登录的用户信息
        Buyer buyer = UserContext.getBuyer();
        if (buyer == null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出登录");
        }
        return buyer;
    }
    public void add(Long memberId,String code) throws Exception {
        NftMemberDO m = new NftMemberDO();
        m.setMemberId(memberId);
        HashMap<String, String> map = cotaNftPlugin.newAccount();
//        m.setRemark(DESUtil.encrypt(code.toLowerCase(),map.get("memo")));
        m.setPrivateKey(DESUtil.encrypt(code.toLowerCase(),map.get("privateKey")));
        m.setCotaAddress(map.get("address"));
        logger.debug(m.getCotaAddress());
        logger.debug(m.getPrivateKey());
        logger.debug(m.getRemark());
//        if(m.getCotaAddress().length() > 50)
//            throw new ServiceException("110",m.getCotaAddress());
        nftMemberMapper.insert(m);
        logLockHash(m.getCotaAddress());
    }
    public void logLockHash(String address) {
        LockHash hash = new LockHash(address);
        if(lockHashMapper.selectById(hash.getLockHash()) == null) {
            lockHashMapper.insert(hash);
        };
    }

    public Map info() {
        Member m = memberManager.getModel(getCurrentMember().getUid());
        NftMemberDO nft = nftMemberMapper.selectById(m.getMemberId());
        Map ret = new HashMap();
        ret.put("nick_name",m.getNickname());
        ret.put("mobile",m.getMobile());
        ret.put("email",m.getEmail());
        ret.put("member_id",m.getMemberId());
        ret.put("hex_member_id",String.format("%016x", m.getMemberId()));
        ret.put("cota_address",nft.getCotaAddress());
        ret.put("is_registry",nft.getRegistry());
        return ret;
    }

    public Set<Map> getTeamInfo() {
        return nftMemberMapper.referees(getCurrentMember().getUid());
    }


    public String listNft(Integer pageNo, Integer pageSize, String cotaId) {
        String address = nftMemberMapper.selectById(getCurrentMember().getUid()).getCotaAddress();
        return cotaNftPlugin.getWithdrawCotaNfts(pageNo,pageSize,null,address);
    }

    public void define( String name,String description,String image,Integer num) {
        Long memberId = getCurrentMember().getUid();
        NftMemberDO nft = nftMemberMapper.selectById(memberId);
        if(nft.getRegistry() == false) {
            throw new ServiceException(MemberErrorCode.E110.code(),"需要先激活账户");
        }
        MemberDepositeVO deposite = depositeClient.getDepositeVO(memberId);
        Double total = deposite.getBalance();
        Map payload = nftStateManager.getDefinePayload(memberId);
        Integer price = (Integer) payload.get("price");
        if(total < price) {
            throw new ServiceException(MemberErrorCode.E110.code(),"余额不足");
        }

        NftCollection c = new NftCollection();
        c.setName(name);
        c.setDescription(description);
        c.setImage(image);
        c.setNum(num);
        c.setIssuerId(memberId);
//        nftCollectionMapper.insert(c);
//        nftJobManager.define(getCurrentMember().getUid(),c);
        nftJobManager.fakedefine(memberId,c);
        depositeManager.reduce(price.doubleValue() ,memberId,"定义 cotaId :" + c.getCotaId() + " 1份。");
        if(payload.get("state") != null) {
            nftStateManager.increaseDefine((NftState)payload.get("state"));
        }

    }



    public void mergeFragments( Long[] pointIds) {
        Long memberId = getCurrentMember().getUid();
        QueryWrapper<NftPoint> qw = new QueryWrapper<>();
        qw.in("id",pointIds);
        qw.eq("member_id",memberId);
        List<NftPoint> ps = nftPointMapper.selectList(qw);
        Map<Long, List<NftPoint>> mps = new HashMap<>();
        for(NftPoint p:ps) {
            if(p.getFragmentId() != null) {
                if( mps.get(p.getFragmentId())!= null) {
                    mps.get(p.getFragmentId()).add(p);
                } else {
                    List<NftPoint> s= new ArrayList<>();
                    s.add(p);
                    mps.put(p.getFragmentId(),s);
                }
            }
        }
        //藏品合成聚合标准
        Map<Long, Map<Long,Integer>> collectionFragments = new HashMap<>();
        List<NftFragment> frags = nftFragmentMapper.selectList(new QueryWrapper<>());
        for(NftFragment f:frags) {
            if(collectionFragments.get(f.getCollectionId())!= null) {
            }else {
                collectionFragments.put(f.getCollectionId(),new HashMap<Long,Integer>());
            }
            collectionFragments.get(f.getCollectionId()).put(f.getId(),f.getMergeNum());
        }
        //计算聚合数量
        for ( Map.Entry<Long, Map<Long,Integer>> entry :collectionFragments.entrySet()) {
            Integer num = null;
            for ( Map.Entry<Long,Integer> entryOfCol :entry.getValue().entrySet()) {
                if(mps.containsKey(entryOfCol.getKey()) && mps.get(entryOfCol.getKey()).size()>=entryOfCol.getValue()) {
                    if(num != null)
                        num = mps.get(entryOfCol.getKey()).size()/entryOfCol.getValue();
                    else{
                        num = 0;
                        num = Integer.min(num,mps.get(entryOfCol.getKey()).size()/entryOfCol.getValue());
                    }
                } else {
                    num =0;
                    break;
                }
            }
            if(num != null && num >0) {
                NftCollection c = nftCollectionMapper.selectById(entry.getKey());
                String txHash = nftJobManager.fakemint(memberId, c, num);
                for ( Map.Entry<Long,Integer> entryOfCol :entry.getValue().entrySet()) {
                    for(int i=0;i<entryOfCol.getValue()*num;i++) {
                        NftPoint p = mps.get(entryOfCol.getKey()).get(i);
                        p.setMerged(true);
                        nftPointMapper.updateById(p);
                    }
                }
            } else {
                throw new ServiceException(MemberErrorCode.E110.code(),"碎片组合不足合成失败");
            }
        }

//        for(Map.Entry<Long, List<NftPoint>> entry :mps.entrySet()) {
//            NftFragment f = nftFragmentMapper.selectById(entry.getKey());
//            int mergeNum = entry.getValue().size() / f.getMergeNum();
//            if(mergeNum > 0) {
//                NftCollection c = nftCollectionMapper.selectById(f.getCollectionId());
////                nftJobManager.mint(memberId,c,mergeNum);
//                nftJobManager.fakemint(memberId,c,mergeNum);
//                for(int i=0;i< mergeNum*f.getMergeNum();i++) {
//                    NftPoint p = entry.getValue().get(i);
//                    p.setMerged(true);
//                    nftPointMapper.updateById(p);
//                }
//            }
//        }
        
    }


    public void batchmint(String[] addresses, Long collectionId, String pwd) {
        long memberId = getCurrentMember().getUid();
        NftMemberDO nft = nftMemberMapper.selectById(memberId);
        if(nft.getRegistry() == false) {
            throw new ServiceException(MemberErrorCode.E110.code(),"需要先激活账户");
        }
        //检测支付密码是否正确
        this.depositeClient.checkPwd(memberId,pwd);
        NftCollection collection = nftCollectionMapper.selectById(collectionId);
        if(!collection.getIssuerId().equals( memberId)) {
            throw new ServiceException(MemberErrorCode.E110.code(),"只能分发本人定义的专辑");
        }
        nftJobManager.batchmint(addresses, collectionId);
    }

    public void buy(Long collectionId,Integer num,String password) {
        long memberId = getCurrentMember().getUid();
        NftConfig cfg = nftConfigMapper.selectById(1l);
        NftCollection collection = nftCollectionMapper.selectById(collectionId);
        if(collection.getAlbumId().equals(cfg.getOnlyOnceAlbumId())) {
            if(nftOrderMapper.selectCount(new QueryWrapper<NftOrder>()
                    .eq("member_id",memberId)
                    .eq("album_id",cfg.getOnlyOnceAlbumId()))>0) {
                throw new ServiceException(MemberErrorCode.E110.code(),"专辑每人限购一份");
            }
        }

        List<NftState> genesises = nftStateManager.getGenesisNft(memberId);
        NftAlbum album= nftAlbumMapper.selectById(collection.getAlbumId());
        if(album.getAlbumId() == 1536574062682177537l) {
            //奇点熊SR
            int buyed = nftOrderMapper.selectCount(
                    new QueryWrapper<NftOrder>()
                    .eq("album_id",album.getAlbumId())
                    .eq("member_id",memberId));
            if(genesises.size()<buyed + num) {
                throw new ServiceException(MemberErrorCode.E110.code(),"根据你所拥有的购买权限你只能买本专辑藏品"+(genesises.size()-buyed) + "个");
            }
        }
        if(genesises.size() >0 ) {
            if(System.currentTimeMillis()/1000 < album.getOpenTime()-60*30) {
                //1655616600
                throw new ServiceException(MemberErrorCode.E110.code(),"未到开售时间");
            }
        } else {
            if(System.currentTimeMillis()/1000 < album.getOpenTime()) {
                throw new ServiceException(MemberErrorCode.E110.code(),"未到开售时间");
            }
        }
        //检测支付密码是否正确
        this.depositeClient.checkPwd(memberId,password);
        MemberDepositeVO deposite = depositeClient.getDepositeVO(memberId);
            Double total = deposite.getBalance();
            if(total < num * collection.getPrice()) {
                throw new ServiceException(MemberErrorCode.E110.code(),"余额不足");
            }
            String txHash = nftJobManager.fakemint(getCurrentMember().getUid(),collection,num);
            nftJobManager.newOrder(getCurrentMember().getUid(),collection,num,txHash);
            depositeManager.reduce(num * collection.getPrice(),memberId,"购买 cotaId :" + collection.getCotaId() + " " + num + "份。txHash:" + txHash);
    }

    public void batchbuy(Long collectionId,Integer num,String password) {
        long memberId = getCurrentMember().getUid();
        NftConfig cfg = nftConfigMapper.selectById(1l);
        NftCollection collection = nftCollectionMapper.selectById(collectionId);
        if(cfg.getOnlyOnceAlbumId().equals(collection.getAlbumId())) {
            if(nftOrderMapper.selectCount(new QueryWrapper<NftOrder>()
                    .eq("member_id",memberId)
                    .eq("album_id",cfg.getOnlyOnceAlbumId()))>0) {
                throw new ServiceException(MemberErrorCode.E110.code(),"专辑每人限购一份");
            }
        }

        List<NftState> genesises = nftStateManager.getGenesisNft(memberId);
        NftAlbum album= nftAlbumMapper.selectById(collection.getAlbumId());
        if(album != null && album.getAlbumId() == 1536574062682177537l) {
            //奇点熊SR 1536574062682177537
            int buyed = nftOrderMapper.selectCount(
                    new QueryWrapper<NftOrder>()
                            .eq("album_id",album.getAlbumId())
                            .eq("member_id",memberId));
            if(genesises.size()<buyed + num) {
                throw new ServiceException(MemberErrorCode.E110.code(),"根据你所拥有的购买权限你只能买本专辑藏品"+(genesises.size()-buyed) + "个");
            }
        }
        if(album != null) {
            if(genesises.size() >0) {
                if(System.currentTimeMillis()/1000 < album.getOpenTime()-60*30) {
                    //1655616600
                    throw new ServiceException(MemberErrorCode.E110.code(),"未到开售时间");
                }
            } else {
                if(System.currentTimeMillis()/1000 < album.getOpenTime()) {
                    throw new ServiceException(MemberErrorCode.E110.code(),"未到开售时间");
                }
            }
        }
        //检测支付密码是否正确
        this.depositeClient.checkPwd(memberId,password);
        MemberDepositeVO deposite = depositeClient.getDepositeVO(memberId);
        Double total = deposite.getBalance();
        if(total < num * collection.getPrice()) {
            throw new ServiceException(MemberErrorCode.E110.code(),"余额不足");
        }
//        String txHash = nftJobManager.fakemint(getCurrentMember().getUid(),collection,num);
        nftJobManager.newOrder(getCurrentMember().getUid(),collection,num,NftMint.MINT_TYPE_BUY);
        depositeManager.reduce(num * collection.getPrice(),memberId,"购买 cotaId :" + collection.getCotaId() + " " + num + "份。");
    }

    public void withdrawal(String receiverAddress,int amount, String password) throws IOException {
        long memberId = getCurrentMember().getUid();
        //有买卖挂单处理中禁止体现
        int onBuying = nftSellMapper.selectCount(new QueryWrapper<NftSell>()
                .eq("buyer_id",memberId)
                .eq("status",NftSell.STAUS_PROCESSING));
        if(onBuying > 0) {
            throw new ServiceException(MemberErrorCode.E110.code(), "您的购买还在进行中，不能提取CKB!");
        }
        //检测支付密码是否正确
        this.depositeClient.checkPwd(memberId,password);
        Map<String, Object> cipher = nftMemberMapper.getCipher(memberId);
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String address = cipher.get("cota_address").toString();
        Set<String> privateKeys = new HashSet<>();
        privateKeys.add(privateKey);

        int withdrawalAmount = CKBAccountUtil.getBalance(address).intValue();
        if(withdrawalAmount - 215 < amount ) {
            throw new ServiceException(MemberErrorCode.E110.code(),"您的余额不足，215个ckb是运行Cota合约的最低保障，不能提取！");
        }
        //读取cotacell的签名
        NftConfig cfg = nftConfigMapper.selectById(1l);
        cipher = nftMemberMapper.getCipher(cfg.getManagerId());
        privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        privateKeys.add(privateKey);
        Map<String,Long> receivers = new HashMap<String,Long>();
        receivers.put(receiverAddress, Utils.ckbToShannon(amount));
        String txHash = CKBAccountUtil.sendCapacity(receivers, address, privateKeys);
        logger.debug("提现转账txHASH:" + txHash);
        Transaction tx = new Transaction();
        tx.setType(Transaction.TX_TYPE_CKB_TRANSFER);
        tx.setTxHash(txHash);
        transactionMapper.insert(tx);
        NftTransfer transfer = new NftTransfer();
        transfer.setType(NftTransfer.TRANSFER_TYPE_FREE);
        transfer.setAsset(NftTransfer.ASSET_CKB);
        transfer.setStatus(NftTransfer.TRANSFER_STATUS_TXED);
        transfer.setMemberId(memberId);
        transfer.setFromAddress(address);
        transfer.setToAddress(receiverAddress);
        transfer.setAmount(amount);
        transfer.setTxId(tx.getTransactionId());
        nftTransferMapper.insert(transfer);
    }
    public void transfer(String receiverAddress,String cotaId,String tokenIndex,String password) {
        long memberId = getCurrentMember().getUid();
        NftMemberDO nft = nftMemberMapper.selectById(memberId);
        if(nft.getRegistry() == false) {
            throw new ServiceException(MemberErrorCode.E110.code(),"需要先激活账户");
        }
        int sellCount = nftSellMapper.selectCount(new QueryWrapper<NftSell>().eq("cota_id",cotaId)
                .eq("token_index",tokenIndex)
                .notIn("status",NftSell.STAUS_CANCEL,NftSell.STAUS_COMPLETE));
        if(sellCount > 0) {
            throw new ServiceException(MemberErrorCode.E110.code(), "出售中,不得转让！");
        }
//        long receiverId = memberManager.getMemberByMobile(receiverPhone).getMemberId();
        //检测支付密码是否正确
        this.depositeClient.checkPwd(memberId,password);
        JSONObject obj = nftJobManager.transfer(memberId,receiverAddress,cotaId,tokenIndex);

        Transaction tx = new Transaction();
        tx.setType(Transaction.TX_TYPE_NFT_TRANSFER);
        tx.setTxHash(obj.get("txHash").toString());
        transactionMapper.insert(tx);
        NftTransfer transfer = new NftTransfer();
        transfer.setType(NftTransfer.TRANSFER_TYPE_FREE);
        transfer.setAsset(NftTransfer.ASSET_NFT);
        transfer.setStatus(NftTransfer.TRANSFER_STATUS_TXED);
        transfer.setMemberId(nft.getMemberId());
        transfer.setFromAddress(nft.getCotaAddress());
        transfer.setToAddress(receiverAddress);
        transfer.setCotaId(cotaId);
        transfer.setTokenIndex(tokenIndex);
        transfer.setTxId(tx.getTransactionId());
        nftTransferMapper.insert(transfer);
    }

    public IPage<Map> listPoint(Integer pageNo, Integer pageSize) {
        Page<Map> page = new Page<Map>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("p.member_id", getCurrentMember().getUid());
        wrapper.eq("p.merged", false);
        wrapper.eq("p.active", NftPoint.STAUS_SUCCESS);
        return  nftPointMapper.pagePoint(page, wrapper);
    }

    public void uploadCollection(Long collectionId,String headImage,String descriptImage,String certImage,Double price) {

        //检查collection ID 是否已经上架 或处于申请中
        NftCollection c = nftCollectionMapper.selectById(collectionId);
        if(c.getAlbumId() != null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "已经上架不能反复上架！");
        }
        QueryWrapper<UploadApply> qw = new QueryWrapper();
        qw.eq("collection_id",collectionId);
        qw.eq("status",UploadApply.STAUS_APPLY);
        List<UploadApply> applies = uploadApplyMapper.selectList(qw);
        if(applies.size() > 0) {
            throw new ServiceException(MemberErrorCode.E110.code(), "申请中不能重复申请！");
        }
        UploadApply uploadApply= new UploadApply();
        uploadApply.setCollectionId(collectionId);
        uploadApply.setHeadImage(headImage);
        uploadApply.setDescriptImage(descriptImage);
        uploadApply.setCertImage(certImage);
        uploadApply.setPrice(price);
        uploadApplyMapper.insert(uploadApply);
    }

    public String getCkbAmount() throws IOException {
        NftMemberDO nft = nftMemberMapper.selectById(getCurrentMember().getUid());
        return CKBAccountUtil.getBalance(nft.getCotaAddress().toString()).toString(10);
    }

    public void nft2Point(String cotaId, String tokenIndex) {
        nftFragmentManager.nft2Point(getCurrentMember().getUid(),cotaId,tokenIndex);
    }

    public void point2Nft(Long pointId) {
        nftFragmentManager.point2Nft(getCurrentMember().getUid(),nftPointMapper.selectById(pointId));
    }

    public JSONObject listFragmentNfts(Long fragmentId) {
        return nftFragmentManager.listFragmentNfts(getCurrentMember().getUid(),fragmentId);
    }
}
