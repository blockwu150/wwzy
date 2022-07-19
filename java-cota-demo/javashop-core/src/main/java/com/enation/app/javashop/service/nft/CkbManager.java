package com.enation.app.javashop.service.nft;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.enation.app.javashop.client.member.DepositeClient;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.rabbitmq.MessageSender;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import com.enation.app.javashop.mapper.member.MemberMapper;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.base.message.NftMintsMsg;
import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.vo.MemberDepositeVO;
import com.enation.app.javashop.model.nft.dos.*;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.nft.plugin.CKBAccountUtil;
import com.enation.app.javashop.service.nft.plugin.CotaNftPlugin;
import com.enation.app.javashop.util.DESUtil;

import org.nervos.ckb.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static com.enation.app.javashop.service.nft.plugin.CKBAccountUtil.UnitCKB;

@Service
public class CkbManager {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private Cache cache;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private NftConfigMapper cfgMapper;
    @Autowired
    private NftMemberMapper nftMemberMapper;
    @Autowired
    private NftAlbumMapper nftAlbumMapper;
    @Autowired
    private NftCollectionMapper nftCollectionMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionParamMapper transactionParamMapper;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private NftFragmentManager nftFragmentManager;
    @Autowired
    private NftJobManager nftJobManager;
    @Autowired
    private CotaNftPlugin cotaNftPlugin;
    @Autowired
    private DepositeClient depositeClient;
    @Autowired
    private NftStateManager nftStateManager;
    @Autowired
    private TradeManager tradeManager;
    @Autowired
    private NftOrderMapper nftOrderMapper;
    @Autowired
    private NftSellMapper nftSellMapper;
    @Autowired
    private NftMintMapper nftMintMapper;


    public void cronCharge() throws IOException {
        Boolean isDoing = (Boolean) cache.get(CachePrefix.TRADE + "_IS_NFT_INIT_CHARGE");
        if (isDoing == null || isDoing == false) {
            cache.put(CachePrefix.TRADE + "_IS_NFT_INIT_CHARGE", Boolean.TRUE, 1000);

            NftConfig cfg = cfgMapper.selectById(1l);
            Map<String, Object> cipher = nftMemberMapper.getCipher(cfg.getManagerId());
            //            Map<String,Object> cipher = nftMemberMapper.getCipher(m.getMemberId());
            String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
            String address = cipher.get("cota_address").toString();

            BigInteger faucetAmt = CKBAccountUtil.getBalance(cipher.get("cota_address").toString());
            int receiverAmt = 0;
            Map<String,Long> receivers = new HashMap<String,Long>();
            List<NftMemberDO> nftmembers = nftMemberMapper.selectList(new QueryWrapper<NftMemberDO>()
                    .eq("is_registry", false)
                    .eq("is_charged", false));
            List<NftMemberDO> receiverBag = new LinkedList<>();
            for (NftMemberDO m : nftmembers) {
//                BigInteger amt = CKBAccountUtil.getBalance(m.getCotaAddress());
//            if(BigInteger.ZERO.equals(amt)) {
                MemberDepositeVO deposite = depositeClient.getDepositeVO(m.getMemberId());
                Double total = deposite.getBalance();
                if (total >= cfg.getInitDeposite()) {
                    receivers.put(m.getCotaAddress(), Utils.ckbToShannon(cfg.getInitCkbAmt()));
                    receiverAmt = receiverAmt + cfg.getInitCkbAmt();
                    receiverBag.add(m);
                }
//            }
            }

            if (receiverAmt > faucetAmt.intValue() - 61) {
                throw new ServiceException(MemberErrorCode.E110.code(), "管理资金不足,需要" + receiverAmt + ",拥有" + faucetAmt);
            }
            if (receivers.size() > 0) {
                String txHash = CKBAccountUtil.sendCapacity(receivers, address, privateKey);
                logger.debug("初始化充值txHASH:" + txHash);
                for (NftMemberDO m : receiverBag) {
                    m.setCharged(true);
                    nftMemberMapper.updateById(m);
                    //扣能量点
                    if (nftStateManager.getGenesisNft(m.getMemberId()).size() == 0)
                        depositeClient.reduce(cfg.getInitDeposite().doubleValue(), m.getMemberId(), "激活账户");
                }
            }
        }
        cache.vagueDel(CachePrefix.TRADE + "_IS_NFT_INIT_CHARGE");
    }

    public void cronRegisty() throws IOException {

        Boolean isDoing = (Boolean) cache.get(CachePrefix.TRADE + "_IS_NFT_REGISTY");
        if (isDoing == null || isDoing == false) {
            cache.put(CachePrefix.TRADE + "_IS_NFT_REGISTY", Boolean.TRUE, 1000);
            NftConfig cfg = cfgMapper.selectById(1l);
            Map<String, Object> cipher = nftMemberMapper.getCipher(cfg.getManagerId());
            String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
            String address = cipher.get("cota_address").toString();
            List<NftMemberDO> nftmembers = nftMemberMapper.selectList(new QueryWrapper<NftMemberDO>()
                    .eq("is_registry", false)
                    .eq("is_charged", true));
            List<NftMemberDO> affacts = new LinkedList<NftMemberDO>();
            String unregs = "";
            for (NftMemberDO m : nftmembers) {
                BigInteger amt = CKBAccountUtil.getBalance(m.getCotaAddress());
                if (amt.intValue() > 61) {
                    affacts.add(m);
                    unregs = unregs + m.getCotaAddress() + ",";
                }
            }
            if (unregs.length() > 0) {
                unregs = unregs.substring(0, unregs.length() - 1);
                int retries = 1;
                while (retries > 0) {
                    try {
                        JSONObject obj = cotaNftPlugin.registry(address, privateKey, unregs);
                        logger.debug("初始化注册txHASH:" + obj.get("txHash"));
                        Set<Long> ids = new HashSet<>();
                        for (NftMemberDO m : affacts) {
                            m.setRegistry(true);
                            nftMemberMapper.updateById(m);
                            ids.add(m.getMemberId());
                        }
                        if (ids.size() > 0)
                            nftFragmentManager.genRandomPoint(ids);
                        retries = 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                        retries--;
                    }
                }
            }

        }
        cache.vagueDel(CachePrefix.TRADE + "_IS_NFT_REGISTY");
    }

    public void cronCheckTxHash() throws IOException {
        Boolean isDoing = (Boolean) cache.get(CachePrefix.TRADE + "_IS_NFT_CHECKTX");
        if (isDoing == null || isDoing == false) {
            cache.put(CachePrefix.TRADE + "_IS_NFT_CHECKTX", Boolean.TRUE, 1000);


            List<NftOrder> orders = nftOrderMapper.selectList(new QueryWrapper<NftOrder>().isNotNull("tx_hash").isNull("tx_hash_status"));
            for (NftOrder order : orders
            ) {
                order.setTxHashStatus(CKBAccountUtil.getTxStatus(order.getTxHash()).toString());
                nftOrderMapper.updateById(order);
            }

            List<NftSell> sells = nftSellMapper.selectList(new QueryWrapper<NftSell>().isNotNull("tx_hash").isNull("tx_hash_status"));
            for (NftSell sell : sells
            ) {
                sell.setTxHashStatus(CKBAccountUtil.getTxStatus(sell.getTxHash()).toString());
                nftSellMapper.updateById(sell);
            }
            List<Transaction> txs = transactionMapper.selectList(new QueryWrapper<Transaction>().isNotNull("tx_hash")
                    .isNull("tx_hash_status").or()
                    .eq("tx_hash_status", "PENDING").or().eq("tx_hash_status", "UNKNOWN"));
            for (Transaction tx : txs
            ) {
                tx.setTxHashStatus(CKBAccountUtil.getTxStatus(tx.getTxHash()).name());
                transactionMapper.updateById(tx);
                if ("REJECTED".equals(tx.getTxHashStatus())) {
                    if (tx.getType() == Transaction.TX_TYPE_MINT) {
                        NftMint mint = nftMintMapper.selectOne(new QueryWrapper<NftMint>().eq("tx_id", tx.getTransactionId()));
                        mint.setStatus(NftMint.MINT_STATUS_CANCELLED);
                        nftMintMapper.updateById(mint);
                        this.messageSender.send(new MqMessage(AmqpExchange.NFT_MINT_CANCELLED, AmqpExchange.NFT_MINT_CANCELLED + "_ROUTING", mint));
                    }
                } else if ("COMMITTED".equals(tx.getTxHashStatus())) {
                    if (tx.getType() == Transaction.TX_TYPE_MINT) {
                        NftMint mint = nftMintMapper.selectOne(new QueryWrapper<NftMint>().eq("tx_id", tx.getTransactionId()));
                        mint.setStatus(NftMint.MINT_STATUS_SUCCEED);
                        nftMintMapper.updateById(mint);
                        if (NftMint.MINT_TYPE_BUY == mint.getType()) {
                            NftOrder order = nftOrderMapper.selectOne(new QueryWrapper<NftOrder>().eq("mint_id", mint.getMintId()));
                            order.setStatus(NftOrder.ORDER_STAUS_COMPLETE);
                            nftOrderMapper.updateById(order);
                            this.messageSender.send(new MqMessage(AmqpExchange.NFT_ORDER_CREATE, AmqpExchange.NFT_ORDER_CREATE + "_ROUTING", order));
                        }
                    }
                } else if ("UNKNOWN".equals(tx.getTxHashStatus())) {
                    if (tx.getType() == Transaction.TX_TYPE_MINT) {
                        NftMint mint = nftMintMapper.selectOne(new QueryWrapper<NftMint>().eq("tx_id", tx.getTransactionId()));
                        if (System.currentTimeMillis() / 1000 - mint.getGroupId() > 30 * 60) {
                            mint.setStatus(NftMint.MINT_STATUS_CANCELLED);
                            nftMintMapper.updateById(mint);
                            this.messageSender.send(new MqMessage(AmqpExchange.NFT_MINT_CANCELLED, AmqpExchange.NFT_MINT_CANCELLED + "_ROUTING", mint));
                            tx.setTxHashStatus("REJECTED");
                            transactionMapper.updateById(tx);
                        }
                    }
                }
            }
        }
        cache.vagueDel(CachePrefix.TRADE + "_IS_NFT_CHECKTX");
    }

    public void cronSendRejectTx() throws IOException {
        Boolean isDoing = (Boolean) cache.get(CachePrefix.TRADE + "_IS_NFT_SEND_REJECT");
        if (isDoing == null || isDoing == false) {
            cache.put(CachePrefix.TRADE + "_IS_NFT_SEND_REJECT", Boolean.TRUE, 1000);
            List<NftOrder> orders = nftOrderMapper.selectList(new QueryWrapper<NftOrder>().isNotNull("tx_hash").eq("tx_hash_status", "rejected"));
            for (NftOrder order : orders
            ) {
                try {
                    NftMemberDO n = nftMemberMapper.selectById(order.getMemberId());
                    NftCollection c = nftCollectionMapper.selectById(order.getCollectionId());
                    String txHash = nftJobManager.fakemint(n.getCotaAddress(), c, order.getNum());
                    order.setTxHash(txHash);
                    order.setTxHashStatus(null);
                    nftOrderMapper.updateById(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            List<NftSell> sells = nftSellMapper.selectList(new QueryWrapper<NftSell>().isNotNull("tx_hash").eq("tx_hash_status", "rejected"));
            for (NftSell sell : sells
            ) {
                sell.setTxHash(null);
                sell.setTxHashStatus(null);
                tradeManager.onChain(sell);
            }
        }
        cache.vagueDel(CachePrefix.TRADE + "_IS_NFT_SEND_REJECT");
    }

//    public void debuggenMintTx() {
//        Boolean isDoing = (Boolean) cache.get(CachePrefix.TRADE + "_IS_NFT_MINT_GEN");
//        if (isDoing == null || isDoing == false) {
//            cache.put(CachePrefix.TRADE + "_IS_NFT_MINT_GEN", Boolean.TRUE, 300);
////            //cotaId 分别 mintTx
////            List<Transaction> txs = transactionMapper.selectList(new QueryWrapper<Transaction>()
////                    .eq("type", Transaction.TX_TYPE_MINT));
//            //按collectionId 分开mint
//            List<NftMint> mints = nftMintMapper.selectList(new QueryWrapper<NftMint>()
//                    .eq("group_id",1656133505));
//            Map<Long, Set<NftMint>> c2m = new HashMap<>();
//            for (NftMint mint : mints) {
//                if (c2m.containsKey(mint.getCollectionId()))
//                    c2m.get(mint.getCollectionId()).add(mint);
//                else {
//                    c2m.put(mint.getCollectionId(), new HashSet<NftMint>());
//                    c2m.get(mint.getCollectionId()).add(mint);
//                }
//            }
//            for (Map.Entry<Long, Set<NftMint>> entry : c2m.entrySet()) {
//                NftCollection c = nftCollectionMapper.selectById(entry.getKey());
//                JSONObject obj = cotaNftPlugin.defineInfo(c.getCotaId());
//                int beginIdx = Integer.valueOf((Integer) obj.get("issued"));
//                int total = Integer.valueOf((Integer) obj.get("total"));
//                checkMintQuantity(entry.getValue(), total, beginIdx);
//                sendGroupMintTx(entry.getValue(), total, beginIdx, c);
////                this.messageSender.send(new MqMessage(AmqpExchange.NFT_MINT_SEND, AmqpExchange.NFT_MINT_SEND + "_ROUTING", new NftMintsMsg(entry.getKey(), entry.getValue())));
//            }
//
//        }
//        cache.vagueDel(CachePrefix.TRADE + "_IS_NFT_MINT_GEN");
//    }


    public void genMintTx() {
        Boolean isDoing = (Boolean) cache.get(CachePrefix.TRADE + "_IS_NFT_MINT_GEN");
        if (isDoing == null || isDoing == false) {
            cache.put(CachePrefix.TRADE + "_IS_NFT_MINT_GEN", Boolean.TRUE, 300);
//            //cotaId 分别 mintTx
//            List<Transaction> txs = transactionMapper.selectList(new QueryWrapper<Transaction>()
//                    .eq("type", Transaction.TX_TYPE_MINT));
            //按collectionId 分开mint
            List<NftMint> mints = nftMintMapper.selectList(new QueryWrapper<NftMint>()
                    .eq("status", NftMint.MINT_STATUS_NEW)
                    .isNull("group_id"));
            Map<Long, Set<NftMint>> c2m = new HashMap<>();
            for (NftMint mint : mints) {
                if (c2m.containsKey(mint.getCollectionId()))
                    c2m.get(mint.getCollectionId()).add(mint);
                else {
                    c2m.put(mint.getCollectionId(), new HashSet<NftMint>());
                    c2m.get(mint.getCollectionId()).add(mint);
                }
            }
            for (Map.Entry<Long, Set<NftMint>> entry : c2m.entrySet()) {
                NftCollection c = nftCollectionMapper.selectById(entry.getKey());
                JSONObject obj = cotaNftPlugin.defineInfo(c.getCotaId());
                int beginIdx = Integer.valueOf((Integer) obj.get("issued"));
                int total = Integer.valueOf((Integer) obj.get("total"));
                checkMintQuantity(entry.getValue(), total, beginIdx);
                sendGroupMintTx(entry.getValue(), total, beginIdx, c);
//                this.messageSender.send(new MqMessage(AmqpExchange.NFT_MINT_SEND, AmqpExchange.NFT_MINT_SEND + "_ROUTING", new NftMintsMsg(entry.getKey(), entry.getValue())));
            }

        }
        cache.vagueDel(CachePrefix.TRADE + "_IS_NFT_MINT_GEN");
    }

    private void sendGroupMintTx(Set<NftMint> mints, int total, int beginIdx, NftCollection c) {
        JSONArray mintArray = new JSONArray();
//        List<Long> ids = new LinkedList<>();
        Map<Long, Set<NftMint>> g2m = new HashMap<>();
        for (NftMint mint : mints) {
//            if (mint.getStatus() == NftMint.MINT_STATUS_TXED) {
//                ids.add(mint.getTxId());
                if (g2m.containsKey(mint.getGroupId()))
                    g2m.get(mint.getGroupId()).add(mint);
                else {
                    g2m.put(mint.getGroupId(), new HashSet<NftMint>());
                    g2m.get(mint.getGroupId()).add(mint);
                }
//            }
        }
        for (Map.Entry<Long, Set<NftMint>> entry : g2m.entrySet()) {
            try {
                sendMintTx(entry.getValue(), total, beginIdx, c);
                beginIdx = c.getTokenIndex();
                Thread.sleep(120000);
            } catch (Exception e) {
                logger.debug(e.getMessage());
                for (NftMint mint : entry.getValue()) {
                    //删除交易
                    transactionMapper.deleteById(mint.getTxId());
                    transactionParamMapper.delete(
                            new UpdateWrapper<TransactionParam>()
                                    .eq("transaction_id", mint.getTxId()));
                    mint.setTxId(null);
                    mint.setStatus(NftMint.MINT_STATUS_CANCELLED);
                    nftMintMapper.updateById(mint);
                    this.messageSender.send(new MqMessage(AmqpExchange.NFT_MINT_CANCELLED, AmqpExchange.NFT_MINT_CANCELLED + "_ROUTING", mint));
                }

            }
        }
    }

    private void sendMintTx(Set<NftMint> mints, int total, int beginIdx, NftCollection c) {
        JSONObject obj = cotaNftPlugin.defineInfo(c.getCotaId());
        beginIdx = Integer.valueOf((Integer) obj.get("issued"));
        JSONArray mintArray = new JSONArray();
        List<Long> ids = new LinkedList<>();

        for (NftMint mint : mints) {
            ids.add(mint.getTxId());
        }
        //reindex
        List<TransactionParam> params = transactionParamMapper.selectList(new QueryWrapper<TransactionParam>()
                .in("transaction_id", ids)
                .eq("type", TransactionParam.PARAM_TYPE_REPEATED));
        for (TransactionParam param : params) {
            JSONObject mintObj = JSONUtil.parseObj(param.getValue());
            mintObj.replace("tokenIndex", String.format("0x%08x", beginIdx++));
            mintArray.add(mintObj);
        }
        //send tx
        JSONObject mintJson = new JSONObject();
        mintJson.putOnce("cotaId", c.getCotaId());
        mintJson.putOnce("mints", mintArray);
        Map cipher = nftMemberMapper.getCipher(c.getIssuerId());
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String address = cipher.get("cota_address").toString();
        JSONObject ret = cotaNftPlugin.mint(address, privateKey, mintJson);
        ret.get("txHash").toString();
        c.setTokenIndex(beginIdx);
        nftCollectionMapper.updateById(c);

        LambdaUpdateWrapper<Transaction> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(Transaction::getTransactionId, ids)
                .setSql("tx_hash=\'" + ret.get("txHash").toString() + "\'");
        transactionMapper.update(null, lambdaUpdateWrapper);

        LambdaUpdateWrapper<NftMint> lambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper1.in(NftMint::getTxId, ids)
                .setSql("status=" + NftMint.MINT_STATUS_TXED);
        nftMintMapper.update(null, lambdaUpdateWrapper1);
    }

    private void checkMintQuantity(Set<NftMint> mints, int total, int beginIdx) {
        long groupId = System.currentTimeMillis() / 1000;
        int idx = 0;

        int limitNum = total - beginIdx;
        for (NftMint mint : mints) {
            if (mint.getNum() > limitNum && total > 0) {
                mint.setStatus(NftMint.MINT_STATUS_CANCELLED);
                this.messageSender.send(new MqMessage(AmqpExchange.NFT_MINT_CANCELLED, AmqpExchange.NFT_MINT_CANCELLED + "_ROUTING", mint));
                if (mint.getTxId() != null) {
                    //删除交易
                    transactionMapper.deleteById(mint.getTxId());
                    transactionParamMapper.delete(
                            new UpdateWrapper<TransactionParam>()
                                    .eq("transaction_id", mint.getTxId()));
                    mint.setTxId(null);
                }
            } else {
                idx = idx + mint.getNum();
                mint.setGroupId(groupId + idx / 60);
            }
            nftMintMapper.updateById(mint);
            limitNum = limitNum - mint.getNum();
        }

    }
}

