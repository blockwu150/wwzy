package com.enation.app.javashop.service.nft;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.mapper.member.MemberMapper;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.model.nft.dos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class NftFragmentManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private NftFragmentMapper nftFragmentMapper;
    @Autowired
    private NftPointMapper nftPointMapper;
    @Autowired
    private NftMemberMapper nftMemberMapper;
    @Autowired
    private NftConfigMapper nftConfigMapper;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    private NftStateManager nftStateManager;
    @Autowired
    private NftJobManager nftJobManager;

    public IPage<Map> list(Long pageNo, Long pageSize) {
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<NftFragment>();
        return nftFragmentMapper.selectPage(page, wrapper);
    }

    public NftFragment addOrModified(Long id, String name, String thumbnail, String description, Integer mergeNum, Long collectionId) {
        NftFragment f = nftFragmentMapper.selectById(id);
        if (f == null) {
            f = new NftFragment();
            f.setId(id);
            f.setCreateTime(System.currentTimeMillis() / 1000);
            nftFragmentMapper.insert(f);
        }
        f.setName(name);
        f.setThumbnail(thumbnail);
        f.setDescription(description);
        f.setMergeNum(mergeNum);
        f.setCollectionId(collectionId);
        nftFragmentMapper.updateById(f);
        return f;
    }


    public void delete(Long[] ids) {
        //新建修改条件包装器
        QueryWrapper<NftPoint> wrapper = new QueryWrapper<>();
        wrapper.in("fragment_id", Arrays.asList(ids));

        List<NftPoint> ps = nftPointMapper.selectList(wrapper);
        if (ps.size() > 0)
            throw new ServiceException(MemberErrorCode.E110.code(), "不能删除，已经有用户取得碎片" + Arrays.asList(ids).toString());
        nftFragmentMapper.deleteBatchIds(Arrays.asList(ids));
    }
    public void genRandomPoint(Set<Long> ids) {
        List<Long> memberIds = nftMemberMapper.everyThreeRegistry(ids);
        for (Long id:memberIds
             ) {
            genRandomPoint(id);
        }
    }
    public NftPoint genRandomPoint(Long memberId) {

        List<NftFragment> fs = nftFragmentMapper.selectList(new QueryWrapper<>());
        if (fs.size() == 0) return null;
//        int fragPointCount = nftPointMapper.selectCount(new QueryWrapper<NftPoint>()
//                .eq("member_id", memberId)
//                .eq("fragment_id",fs.get(0).getId()));
//        if (fragPointCount > 0) return null;
        NftPoint p = new NftPoint();
        p.setCreateTime(System.currentTimeMillis() / 1000);
        p.setMemberId(memberId);
        p.setFragmentId(fs.get(0).getId());

//        int ran = ((Double)(Math.random() * 5)).intValue();
//        if (ran != 0)
            nftPointMapper.insert(p);
//        int childCount = memberMapper.selectCount(new QueryWrapper<Member>().eq("referer_id", memberId));
//        if (childCount >= 3 && ran != 0)
//            nftPointMapper.insert(p);

       return p;
    }

    public NftPoint genBuyPoint(Long memberId) {
        NftPoint p = new NftPoint();
        p.setCreateTime(System.currentTimeMillis() / 1000);
        p.setMemberId(memberId);
        List<NftFragment> fs = nftFragmentMapper.selectList(new QueryWrapper<>());
        if (fs.size() == 0) return null;
        p.setFragmentId(fs.get(1).getId());
        nftPointMapper.insert(p);
        return p;
    }

    public NftPoint nft2Point(Long memberId,String cotaId,String tokenIndex) {
        NftPoint p = new NftPoint();
        NftFragment f = nftFragmentMapper.selectOne(new QueryWrapper<NftFragment>().eq("changable_cota_id", cotaId));
        if(f == null) {
            throw new ServiceException(MemberErrorCode.E110.code(),"非碎片nft或碎片兑换未配置");
        }
        NftConfig cfg = nftConfigMapper.selectById(1l);
        NftMemberDO manager = nftMemberMapper.selectById(cfg.getChangableManagerId());
        NftMemberDO m = nftMemberMapper.selectById(memberId);

        Transaction tx = new Transaction();
        tx.setType(Transaction.TX_TYPE_NFT_TRANSFER);
        JSONObject obj = nftJobManager.transfer(memberId, manager.getCotaAddress(),cotaId,tokenIndex);
        tx.setTxHash(obj.get("txHash").toString());
        transactionMapper.insert(tx);
        p.setMemberId(memberId);
        p.setCreateTime(System.currentTimeMillis()/1000);
        p.setTxTime(System.currentTimeMillis()/1000);
        p.setTxId(tx.getTransactionId());
        p.setNft2point(true);
        p.setActive(NftPoint.STAUS_PROCESSING);
        p.setFragmentId(f.getId());
        nftPointMapper.insert(p);
        return p;
    }

    public synchronized String point2Nft(Long memberId,NftPoint p) {
        if(p.getMerged()) {
            throw new ServiceException(MemberErrorCode.E110.code(),"碎片已经合成不能兑换对应NFT");
        }
        if(p.getActive() != NftPoint.STAUS_SUCCESS) {
            throw new ServiceException(MemberErrorCode.E110.code(),"碎片处理中请等待完成");
        }
        NftFragment f = nftFragmentMapper.selectById(p.getFragmentId());
        String cotaId = f.getChangableCotaId();
        NftConfig cfg = nftConfigMapper.selectById(1l);
        NftMemberDO manager = nftMemberMapper.selectById(cfg.getChangableManagerId());
        NftMemberDO m = nftMemberMapper.selectById(memberId);
        JSONObject jsonObject = nftStateManager.getNfts(manager.getCotaAddress(), cotaId);
        if((int)jsonObject.get("total") == 0) {
            throw new ServiceException(MemberErrorCode.E110.code(),"没有可兑换nft请联系管理人员");
        }
        JSONArray array = jsonObject.getJSONArray("nfts");
        JSONObject jsonNft = (JSONObject) array.get(0);
        String tokenIndex = jsonNft.get("tokenIndex").toString();
        Transaction tx = new Transaction();
        tx.setType(Transaction.TX_TYPE_NFT_TRANSFER);
        JSONObject obj = nftJobManager.transfer(manager.getMemberId(), m.getCotaAddress(),cotaId,tokenIndex);
        tx.setTxHash(obj.get("txHash").toString());
        transactionMapper.insert(tx);
        p.setTxId(tx.getTransactionId());
        p.setNft2point(false);
        p.setActive(NftPoint.STAUS_PROCESSING);
        p.setTxTime(System.currentTimeMillis()/1000);
        nftPointMapper.updateById(p);
        return tx.getTxHash();
    }

    public JSONObject listFragmentNfts(Long memberId,long fragmentId) {
        NftMemberDO m = nftMemberMapper.selectById(memberId);
        NftFragment f = nftFragmentMapper.selectById(fragmentId);
        return nftStateManager.getNfts(m.getCotaAddress(), f.getChangableCotaId());
    }
}
