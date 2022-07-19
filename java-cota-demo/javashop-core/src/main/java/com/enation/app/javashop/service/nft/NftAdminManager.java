package com.enation.app.javashop.service.nft;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.request.ThreadContextHolder;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.security.model.TokenConstant;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.member.MemberMapper;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.model.member.vo.MemberDepositeVO;
import com.enation.app.javashop.model.nft.dos.*;
import com.enation.app.javashop.model.nft.dto.BatchMintParam;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.nft.plugin.CotaNftPlugin;
import com.enation.app.javashop.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NftAdminManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
    private NftSellMapper nftSellMapper;
    @Autowired
    private NftMintMapper nftMintMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionParamMapper transactionParamMapper;
    @Autowired
    private NftCollectionMapper nftCollectionMapper;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private NftJobManager nftJobManager;
    @Autowired
    private CotaNftPlugin cotaNftPlugin;


//    private boolean doCollection() {
//        NftCollectionMapper nftCollectionMapper = (NftCollectionMapper) services.get("nftCollectionMapper");
//        NftAlbumMapper nftAlbumMapper = (NftAlbumMapper) services.get("nftAlbumMapper");
//
//        JSONObject a = (JSONObject)context.get("collection");
//        NftCollection collection = a.toBean(NftCollection.class);
//        collection.setStatus(NftCollection.STATUS_SUCCESS);
//        nftCollectionMapper.updateById(collection);
//        NftAlbum album = nftAlbumMapper.selectById(collection.getAlbumId());
//        if(album != null){
//            album.setNum(album.getNum() + 1);
//            nftAlbumMapper.updateById(album);
//        }
//        return true;
//    }

    public NftCollection define( Long albumId,String name,String description,String image,Integer num) {
        NftCollection c = new NftCollection();
        c.setAlbumId(albumId);
        c.setName(name);
        c.setDescription(description);
        c.setImage(image);
        c.setNum(num);
        NftConfig cfg = cfgMapper.selectById(1l);
        c.setIssuerId(cfg.getManagerId());
        nftJobManager.fakedefine(cfg.getManagerId(),c);
        return c;
    }

    public NftCollection preDefine( Long albumId,String name,String description,String image,Integer num,Double price) {
        NftCollection c = new NftCollection();
        c.setAlbumId(albumId);
        c.setName(name);
        c.setDescription(description);
        c.setImage(image);
        c.setNum(num);
        NftConfig cfg = cfgMapper.selectById(1l);
        c.setIssuerId(cfg.getManagerId());
//        nftJobManager.fakedefine(cfg.getManagerId(),c);
        c.setPrice(price);
        nftCollectionMapper.insert(c);
        return c;
    }

    public void gift(Long collectionId,Long memberId,Integer num) {
        NftCollection collection = nftCollectionMapper.selectById(collectionId);
        nftJobManager.fakemint(memberId,collection,num);
    }
    public void batchgift(String[] addresses, Long collectionId) {
        NftCollection collection = nftCollectionMapper.selectById(collectionId);
        nftJobManager.batchmint(addresses,collectionId);
    }
    public void batchmultygift(BatchMintParam[] params) {

        for(BatchMintParam param:params) {
            NftCollection c = nftCollectionMapper.selectById(param.getCollectionId());
            JSONObject obj = cotaNftPlugin.defineInfo(c.getCotaId());
            int beginIdx = Integer.valueOf((Integer) obj.get("issued"));
            int total = Integer.valueOf((Integer) obj.get("total"));
            if (total > 0 && beginIdx + param.getAddresses().length > total) {
                throw new ServiceException(MemberErrorCode.E110.code(), c.getCotaId() + "模版铸造已经达到上限！");
            }
        }
        for(BatchMintParam param:params) {
            NftCollection c = nftCollectionMapper.selectById(param.getCollectionId());
            NftMint mint = new NftMint();
            mint.setType(NftMint.MINT_TYPE_FREE);
            mint.setAlbumId(c.getAlbumId());
            mint.setCollectionId(c.getId());
            mint.setNum(param.getAddresses().length);
            mint.setMemberId(cfgMapper.selectById(1l).getManagerId());
            mint.setCreateTime(System.currentTimeMillis() / 1000);
            Transaction tx = new Transaction();
            tx.setType(Transaction.TX_TYPE_MINT);
            transactionMapper.insert(tx);
            TransactionParam tparam = new TransactionParam();
            tparam.setTransactionId(tx.getTransactionId());
            tparam.setType(TransactionParam.PARAM_TYPE_ONCE);
            tparam.setValue(c.getCotaId());
            transactionParamMapper.insert(tparam);
            for (int i = 0; i <  param.getAddresses().length; i++) {
                JSONObject mintObj = new JSONObject();
                mintObj.putOnce("tokenIndex", String.format("0x%08x", i));
                mintObj.putOnce("state", "0x00");
                mintObj.putOnce("characteristic", "0x0505050505050505050505050505050505050505");
                mintObj.putOnce("receiverAddress", param.getAddresses()[i]);
                tparam = new TransactionParam();
                tparam.setTransactionId(tx.getTransactionId());
                tparam.setType(TransactionParam.PARAM_TYPE_REPEATED);
                tparam.setValue(mintObj.toString());
                transactionParamMapper.insert(tparam);
            }
            mint.setTxId(tx.getTransactionId());
            nftMintMapper.insert(mint);
        }
    }

    public IPage<Map> getMembers(Long pageNo, Long pageSize,
                                 Long memberId,
                                 Boolean upOrDown,
                                 String ckbAddress,
                                 String nickname,
                                 String mobile){
        String token = ThreadContextHolder.getHttpRequest().getHeader(TokenConstant.HEADER_STRING);
        Page<Map> page = new Page<Map>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        if( cache.get(CachePrefix.TOKEN+"_"+ token+"_MEMBERID") != null
                && ("".equals(nickname) || nickname == null)
                && ("".equals(mobile) || mobile==null)
                && memberId==null){
            if(!pageNo.equals(cache.get(CachePrefix.TOKEN+"_"+ token+"_PAGENO") )
                    || !pageSize.equals(cache.get(CachePrefix.TOKEN+"_"+ token+"_PAGESIZE")) ){
                memberId= (Long) cache.get(CachePrefix.TOKEN+"_"+ token+"_MEMBERID");
                upOrDown = (Boolean) cache.get(CachePrefix.TOKEN+"_"+ token+"_UPORDOWN");
            }else{
                cache.vagueDel(CachePrefix.TOKEN+"_"+ token );
            }
        }
        if(memberId!=null && upOrDown != null) {
            cache.put(CachePrefix.TOKEN+"_"+ token +"_UPORDOWN",upOrDown,1000);
            cache.put(CachePrefix.TOKEN+"_"+ token+"_MEMBERID",memberId,1000);
            cache.put(CachePrefix.TOKEN+"_"+ token+"_PAGENO",pageNo,1000);
            cache.put(CachePrefix.TOKEN+"_"+ token+"_PAGESIZE",pageSize,1000);
            wrapper.eq( upOrDown != null && upOrDown == true, " m.member_id", memberMapper.selectById(memberId).getRefererId());
            wrapper.eq( upOrDown != null && upOrDown == false, " m.referer_id", memberId);
        }
        wrapper.like(ckbAddress!=null," n.cota_address",ckbAddress);
        wrapper.like(nickname!=null," m.nickname",nickname);
        wrapper.like(mobile!=null," m.mobile",mobile);
        nftMemberMapper.pageNftMember(page, wrapper);
        if(page.getRecords().size() == 0 && memberId!=null && upOrDown != null) {
            throw new ServiceException(MemberErrorCode.E110.code(), "没有上下级了!");
        }
        return  page;
    }

    public void transfer(Long memberId, String receiverAddress,String cotaId,String tokenIndex,String password) {
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
        if(!password.equals("99"))
            throw new ServiceException(MemberErrorCode.E110.code(), "转账密码错误!");
        nftJobManager.transfer(memberId,receiverAddress,cotaId,tokenIndex);
    }

}
