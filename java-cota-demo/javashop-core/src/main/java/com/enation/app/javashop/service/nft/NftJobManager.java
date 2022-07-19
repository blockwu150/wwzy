package com.enation.app.javashop.service.nft;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.rabbitmq.MessageSender;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.nft.dos.*;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.nft.plugin.CotaNftPlugin;
import com.enation.app.javashop.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NftJobManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    NftSellMapper nftSellMapper;
    @Autowired
    LockHashMapper lockHashMapper;
    @Autowired
    NftMintMapper nftMintMapper;
    @Autowired
    NftOrderMapper nftOrderMapper;
    @Autowired
    NftObjectMapper nftObjectMapper;
    @Autowired
    private NftMemberMapper nftMemberMapper;
    @Autowired
    private NftAlbumMapper nftAlbumMapper;
    @Autowired
    private NftCollectionMapper nftCollectionMapper;
    @Autowired
    private NftConfigMapper nftConfigMapper;
    @Autowired
    private NftJobMapper nftJobMapper;
    @Autowired
    private NftActionMapper nftActionMapper;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    TransactionParamMapper transactionParamMapper;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private CotaNftPlugin cotaNftPlugin;

    public void define(Long memberId, NftCollection collection) {
        //context memberId long 会员Id
        //context collection NftCollection  收藏品对象
        //context -- cotaNftPlugin nft插件
        //context -- nftCollectionMapper 收藏品mapper
//        NftConfig cfg = nftConfigMapper.selectById(1l);
//        Map<String, Object> cipher = nftMemberMapper.getCipher(cfg.getManagerId());
        Map context = new HashMap();
        context.put("memberId", memberId);
        context.put("collection", collection);
        NftJob define = new NftJob(memberId, NftJob.JOB_DEFINE);
        Long[] ids = new Long[]{memberId};
        initJob(define, context, ids);


    }

    public void mint(Long receiverId, NftCollection collection, Integer num) {
        //context isRegistry boolean 是否注册
        //context chargeKey 充值键
        //context chargPwd 充值密码
        //context chargAddress 充值地址
        //context privateKey String  转出私钥
        //context pwd String 密码
        //context address String  地址
        //context toAddress String  提取地址
        //context toPrivateKey String  提取私钥
        //context toPwd String  提取码
        //context collection NftCollection  收藏品对象
        //context num Integer 数量
        //context -- cotaNftPlugin nft插件
        //context -- nftCollectionMapper 收藏品mapper
        NftConfig cfg = nftConfigMapper.selectById(1l);
        Map<String, Object> cipher = nftMemberMapper.getCipher(cfg.getManagerId());
        Map context = new HashMap();

        context.put("memberId", collection.getIssuerId());
        context.put("receiverId", receiverId);
        context.put("collection", collection);
        context.put("num", num);

        NftJob mint = new NftJob(collection.getIssuerId(), NftJob.JOB_MINT);
        Long[] ids = new Long[]{collection.getIssuerId(), receiverId};
        initJob(mint, context, ids);

//        initJob(mint,context);
//        mint.setContext(JSONUtil.parseObj(context).toString());
//        nftJobMapper.insert(mint);
//        List<NftAction> actions = mint.genActions(context);
//        mint.setActions(actions);
//        Map services = getServices();
//        try {
//            mint.process(services);
//        }catch (Exception e){
//            e.printStackTrace();
//        } finally {
//            nftJobMapper.updateById(mint);
//            for(NftAction action: actions) {
//                nftActionMapper.insert(action);
//            }
//        }
    }

    public void withdrawal(Long memberId, Long receiverId, String cotaId, String tokenIndex) {
        //context memberId long 出让人Id
        //context receiverId long 受让人Id
        //context cotaId string cotaId
        //context tokenIndex string 16进制 tokenIndex

        Map context = new HashMap();
        context.put("memberId", memberId);
        context.put("receiverId", receiverId);
        context.put("cotaId", cotaId);
        context.put("tokenIndex", tokenIndex);

        NftJob withdraw = new NftJob(memberId, NftJob.JOB_WITHDRAWAL);
        Long[] ids = new Long[]{memberId, receiverId};
        initJob(withdraw, context, ids);
    }

    public void registry(Long memberId) {
        Map context = new HashMap();
        context.put("memberId", memberId);
        NftJob registry = new NftJob(memberId, NftJob.JOB_REGISTRY);
        initJob(registry, context, null);
    }

    private void initJob(NftJob job, Map context, Long[] ids) {
        job.setContext(JSONUtil.parseObj(context).toString());
        nftJobMapper.insert(job);
        List<NftAction> actions = job.genActions(context);
        job.setActions(actions);
        //附加金额检查充值action和registy action
        if (context.get("ids") != null && ids.length > 0) {


        }
        job.getActions().get(0).setStatus(NftAction.STATUS_DOING);
        Map services = getServices();
        try {
            job.process(services);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nftJobMapper.updateById(job);
            for (NftAction action : actions) {
                nftActionMapper.insert(action);
            }
        }
    }

    public void cronJobs() {
        QueryWrapper<NftJob> qw = new QueryWrapper<NftJob>();
        qw.isNull("complete_time");
        qw.eq("status", NftJob.STATUS_READY);
        List<NftJob> jobs = nftJobMapper.selectList(qw);
        //运行状态
        UpdateWrapper<NftJob> uw = new UpdateWrapper<>();
        uw.isNull("complete_time");
        uw.eq("status", NftJob.STATUS_READY);
        uw.setSql("status = 1");
        nftJobMapper.update(null, uw);

        for (NftJob job : jobs) {
            QueryWrapper<NftAction> qwa = new QueryWrapper<NftAction>();
            qwa.eq("job_id", job.getJobId());
            qwa.ne("status", NftAction.STATUS_DONE);
            List<NftAction> actions = nftActionMapper.selectList(qwa);
            job.setActions(actions);
            processJob(job);
        }

    }

    private void processJob(NftJob job) {
        try {
            job.process(getServices());
        } catch (Exception e) {
            e.printStackTrace();
            job.setStatus(NftJob.STATUS_EXCEPTION);
            String msg = e.getMessage();
            if (msg.length() > 500) {
                msg = msg.substring(0, 500);
            }
            job.setException(msg);
        } finally {
            nftJobMapper.updateById(job);
            for (NftAction action : job.getActions()) {
                nftActionMapper.updateById(action);
            }
        }
    }

    private Map getServices() {
        Map services = new HashMap();
        services.put("cotaNftPlugin", cotaNftPlugin);
        services.put("nftCollectionMapper", nftCollectionMapper);
        services.put("nftAlbumMapper", nftAlbumMapper);
        services.put("nftJobMapper", nftJobMapper);
        services.put("nftMemberMapper", nftMemberMapper);
        return services;
    }


    public void initAccount() {
        List<NftMemberDO> accounts = nftMemberMapper.selectList(new QueryWrapper<>());
        for (NftMemberDO n : accounts) {
            Map<String, Object> cipher = nftMemberMapper.getCipher(n.getMemberId());

            String address = cipher.get("cota_address").toString();
            ;
            String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
            String ret = cotaNftPlugin.toAddress(privateKey);
            if (ret != null && ret.length() > 0) {
                n.setCotaAddress(ret);
                nftMemberMapper.updateById(n);
            }


        }
    }

    public IPage<Map> list(Long pageNo, Long pageSize, Long memberId, Integer status) {
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq(memberId != null, "member_id", memberId);
        wrapper.eq(status != null, "status", status);
        wrapper.orderByDesc("job_id");
        return nftJobMapper.selectPage(page, wrapper);
    }

    public void fakedefine(Long memberId, NftCollection c) {
        Map cipher = nftMemberMapper.getCipher(memberId);
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String address = cipher.get("cota_address").toString();
        JSONObject obj = cotaNftPlugin.define(address, privateKey, c.getName(), c.getDescription(), c.getImage(), c.getNum());
        c.setCotaId(obj.get("cotaId").toString());
        c.setStatus(NftCollection.STATUS_SUCCESS);
        nftCollectionMapper.insert(c);
        NftAlbum album = nftAlbumMapper.selectById(c.getAlbumId());
        if (album != null) {
            album.setNum(album.getNum() + 1);
            nftAlbumMapper.updateById(album);
        }
    }
    public void processDefine(Long memberId, NftCollection c) {
        Map cipher = nftMemberMapper.getCipher(memberId);
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String address = cipher.get("cota_address").toString();
        JSONObject obj = cotaNftPlugin.define(address, privateKey, c.getName(), c.getDescription(), c.getImage(), c.getNum());
        c.setCotaId(obj.get("cotaId").toString());
        c.setStatus(NftCollection.STATUS_SUCCESS);
        nftCollectionMapper.updateById(c);
        NftAlbum album = nftAlbumMapper.selectById(c.getAlbumId());
        if (album != null) {
            album.setNum(album.getNum() + 1);
            nftAlbumMapper.updateById(album);
        }

    }


    public String fakemint(Long memberId, NftCollection c, int mergeNum) {
        String toAddress = nftMemberMapper.selectById(memberId).getCotaAddress();
        return fakemint(toAddress, c, mergeNum);
    }

    public String fakemint(String toAddress, NftCollection c, int mergeNum) {
        if (!c.getStatus().equals(NftCollection.STATUS_SUCCESS)) {
            throw new ServiceException(MemberErrorCode.E110.code(), "模版未上链！");
        }
        JSONObject obj = cotaNftPlugin.defineInfo(c.getCotaId());
        int beginIdx = Integer.valueOf((Integer) obj.get("issued"));
        int total = Integer.valueOf((Integer) obj.get("total"));
        if (total > 0 && beginIdx + mergeNum > total) {
            throw new ServiceException(MemberErrorCode.E110.code(), "模版铸造已经达到上限！");
        }
//        String toAddress = nftMemberMapper.selectById(memberId).getCotaAddress();
        JSONObject mintJson = new JSONObject();
        mintJson.putOnce("cotaId",c.getCotaId());
        JSONArray mints = new JSONArray();
        for (int i = beginIdx; i < beginIdx + mergeNum; i++) {
            JSONObject mintObj = new JSONObject();
            mintObj.putOnce("tokenIndex", String.format("0x%08x", i));
            mintObj.putOnce("state", "0x00");
            mintObj.putOnce("characteristic", "0x0505050505050505050505050505050505050505");
            mintObj.putOnce("receiverAddress", toAddress);
            mints.add(mintObj);
        }
        mintJson.putOnce("mints", mints);
        Map cipher = nftMemberMapper.getCipher(c.getIssuerId());
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String address = cipher.get("cota_address").toString();

        JSONObject ret = cotaNftPlugin.mint(address, privateKey, mintJson);
        ret.get("txHash").toString();

        c.setTokenIndex(beginIdx + mergeNum);
        nftCollectionMapper.updateById(c);
        return ret.get("txHash").toString();
    }

    public void batchmint(String[] addresses, Long collectionId) {
        NftCollection c = nftCollectionMapper.selectById(collectionId);
        if (!c.getStatus().equals(NftCollection.STATUS_SUCCESS)) {
            throw new ServiceException(MemberErrorCode.E110.code(), "模版未上链！");
        }
        JSONObject obj = cotaNftPlugin.defineInfo(c.getCotaId());
        int beginIdx = Integer.valueOf((Integer) obj.get("issued"));
        int total = Integer.valueOf((Integer) obj.get("total"));
        if (total > 0 && beginIdx + addresses.length > total) {
            throw new ServiceException(MemberErrorCode.E110.code(), "模版铸造已经达到上限！");
        }
//        String toAddress = nftMemberMapper.selectById(memberId).getCotaAddress();
        JSONObject mintJson = new JSONObject();
        mintJson.putOnce("cotaId",c.getCotaId());
        JSONArray mints = new JSONArray();
        for (int i = 0; i <  + addresses.length; i++) {
            JSONObject mintObj = new JSONObject();
            mintObj.putOnce("tokenIndex", String.format("0x%08x",beginIdx + i));
            mintObj.putOnce("state", "0x00");
            mintObj.putOnce("characteristic", "0x0505050505050505050505050505050505050505");
            mintObj.putOnce("receiverAddress", addresses[i]);
            mints.add(mintObj);
        }
        mintJson.putOnce("mints", mints);
        Map cipher = nftMemberMapper.getCipher(c.getIssuerId());
        String privateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String address = cipher.get("cota_address").toString();

        JSONObject ret = cotaNftPlugin.mint(address, privateKey, mintJson);
        ret.get("txHash").toString();
        c.setTokenIndex(beginIdx + addresses.length);
        nftCollectionMapper.updateById(c);
    }


    public void newOrder(Long memberId, NftCollection collection, Integer num, String txHash) {
        NftOrder order = new NftOrder();
        order.setAlbumId(collection.getAlbumId());
        order.setMemberId(memberId);
        order.setNum(num);
        order.setCollectionId(collection.getId());
        order.setPrice(collection.getPrice());
        order.setAmount(num * order.getPrice());
        order.setCreateTime(System.currentTimeMillis() / 1000);
        order.setRemark("txHash:" + txHash);
        order.setTxHash(txHash);
        nftOrderMapper.insert(order);
        this.messageSender.send(new MqMessage(AmqpExchange.NFT_ORDER_CREATE, AmqpExchange.NFT_ORDER_CREATE + "_ROUTING", order));
    }
    public void newOrder(Long memberId, NftCollection c, Integer num,Integer mintType) {
        JSONObject obj = cotaNftPlugin.defineInfo(c.getCotaId());
        int beginIdx = Integer.valueOf((Integer) obj.get("issued"));
        int total = Integer.valueOf((Integer) obj.get("total"));
        if (total > 0 && beginIdx + num > total) {
            throw new ServiceException(MemberErrorCode.E110.code(), "模版铸造已经达到上限！");
        }
        NftOrder order = new NftOrder();
        order.setAlbumId(c.getAlbumId());
        order.setMemberId(memberId);
        order.setNum(num);
        order.setCollectionId(c.getId());
        order.setPrice(c.getPrice());
        order.setAmount(num * order.getPrice());
        order.setCreateTime(System.currentTimeMillis() / 1000);
        NftMint mint = new NftMint();
        mint.setType(mintType);
        mint.setAlbumId(c.getAlbumId());
        mint.setCollectionId(c.getId());
        mint.setNum(num);
        mint.setMemberId(memberId);
        mint.setCreateTime(System.currentTimeMillis() / 1000);
        Transaction tx = new Transaction();
        tx.setType(Transaction.TX_TYPE_MINT);
        transactionMapper.insert(tx);
        TransactionParam param = new TransactionParam();
        param.setTransactionId(tx.getTransactionId());
        param.setType(TransactionParam.PARAM_TYPE_ONCE);
        param.setValue(c.getCotaId());
        transactionParamMapper.insert(param);
        String address = nftMemberMapper.selectById(memberId).getCotaAddress();
        for (int i = 0; i <  num; i++) {
            JSONObject mintObj = new JSONObject();
            mintObj.putOnce("tokenIndex", String.format("0x%08x",beginIdx + i));
            mintObj.putOnce("state", "0x00");
            mintObj.putOnce("characteristic", "0x0505050505050505050505050505050505050505");
            mintObj.putOnce("receiverAddress", address);
            param = new TransactionParam();
            param.setTransactionId(tx.getTransactionId());
            param.setType(TransactionParam.PARAM_TYPE_REPEATED);
            param.setValue(mintObj.toString());
            transactionParamMapper.insert(param);
        }
        mint.setTxId(tx.getTransactionId());
        nftMintMapper.insert(mint);
        order.setMintId(mint.getMintId());
        nftOrderMapper.insert(order);
    }


    public void fakewithdrawal(long memberId, long receiverId, String cotaId, String tokenIndex) {
        withdrawal(memberId, receiverId, cotaId, tokenIndex);
    }

    public JSONObject transfer(long memberId, String address, String cotaId, String tokenIndex) {

        Map cipher = nftMemberMapper.getCipher(memberId);
        String fromPrivateKey = DESUtil.decrypt(cipher.get("uname").toString(), cipher.get("private_key").toString());
        String fromAddress = cipher.get("cota_address").toString();
        return cotaNftPlugin.transfer(checkLockHash(memberId,fromAddress,cotaId,tokenIndex),address,fromAddress,fromPrivateKey,cotaId,tokenIndex);

    }

    public String checkLockHash(Long memberId,String address,String cotaId, String tokenIndex) {
        JSONObject senderInfo = cotaNftPlugin.getNftSenderInfo(address, cotaId, tokenIndex);
        String lockHash = senderInfo.get("senderLockHash").toString();
        LockHash withdrawLockHash = lockHashMapper.selectById(lockHash);

        if(withdrawLockHash == null) {
            Map msg = new HashMap();
            msg.put("memberId",memberId);
            msg.put("url","https://explorer.nervos.org/address/"+lockHash);
            this.messageSender.send(new MqMessage(AmqpExchange.NFT_OUTER_ADDRESS_REMIND, AmqpExchange.NFT_OUTER_ADDRESS_REMIND + "_ROUTING", msg));
            throw new ServiceException(MemberErrorCode.E110.code(), "当前NFT的上一次持有地址未知，打开区块链浏览器 https://explorer.nervos.org/address/"+lockHash+ " 并补充地址!");
        }
        return withdrawLockHash.getAddress();
    }


}
