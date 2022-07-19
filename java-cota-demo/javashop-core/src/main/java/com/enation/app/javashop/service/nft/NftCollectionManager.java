package com.enation.app.javashop.service.nft;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.client.member.DepositeClient;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.mapper.nft.NftAlbumMapper;
import com.enation.app.javashop.mapper.nft.NftCollectionMapper;
import com.enation.app.javashop.mapper.nft.NftConfigMapper;
import com.enation.app.javashop.mapper.nft.NftPlayBillMapper;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.nft.dos.NftAlbum;
import com.enation.app.javashop.model.nft.dos.NftCollection;
import com.enation.app.javashop.model.nft.dos.NftPlayBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NftCollectionManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NftPlayBillMapper nftPlayBillMapper;
    @Autowired
    private NftCollectionMapper nftCollectionMapper;
    @Autowired
    private NftAlbumMapper nftAlbumMapper;
    @Autowired
    private NftConfigMapper nftConfigMapper;

    public IPage<Map> collections(Long pageNo, Long pageSize, Long albumId,Long issuerId,Boolean priceOrder) {
        Page<Map> page = new Page<Map>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq(albumId!= null ,"c.album_id", albumId);
        wrapper.eq(issuerId!= null ,"c.issuer_id", issuerId);
        wrapper.eq("c.status", NftCollection.STATUS_SUCCESS);
        if(priceOrder != null) {
            if(Boolean.TRUE.equals(priceOrder)) {
                wrapper.orderByAsc("c.price");
            } else {
                wrapper.orderByDesc("c.price");
            }
        } else {
            wrapper.orderByDesc("c.id");
        }
        return  nftCollectionMapper.pageCollection(page, wrapper);
    }

    synchronized public void delete(Long[] ids) {
        //新建修改条件包装器
        QueryWrapper<NftCollection> wrapper = new QueryWrapper<>();
        wrapper.in("id", Arrays.asList(ids));
        List<NftCollection> cs = nftCollectionMapper.selectList(wrapper);
        Map<Long, Integer> albumIdDelNums = new HashMap();

        for (NftCollection c : cs) {
            if (c.getAlbumId() != null) {
                if (albumIdDelNums.get(c.getAlbumId()) == null) {
                    albumIdDelNums.put(c.getAlbumId(), 1);
                } else {
                    albumIdDelNums.put(c.getAlbumId(), albumIdDelNums.get(c.getAlbumId()) + 1);
                }
            }
        }
        for (Map.Entry<Long, Integer> entry : albumIdDelNums.entrySet()) {
            NftAlbum album = nftAlbumMapper.selectById(entry.getKey());
            album.setNum(album.getNum() - entry.getValue());
            nftAlbumMapper.updateById(album);
        }
        nftCollectionMapper.deleteBatchIds(Arrays.asList(ids));

    }


    public void setPrice(Long collectionId, Double price) {

        NftCollection c = nftCollectionMapper.selectById(collectionId);
        c.setPrice(price);
        if(c.getAlbumId()==null) {
            c.setAlbumId(nftConfigMapper.selectById(1l).getCustomAlbumId());
        }
        nftCollectionMapper.updateById(c);
    }
    public void setPlayBill( NftPlayBill playBill) {
        if(nftPlayBillMapper.selectById(playBill.getCollectionId())==null) {
            nftPlayBillMapper.insert(playBill);
        }else {
            nftPlayBillMapper.updateById(playBill);
        }
    }

    public NftCollection getCollection(String cotaId, String tokenIndex) {
        return nftCollectionMapper.selectOne(new QueryWrapper<NftCollection>()
                .eq("cota_id",cotaId));
//        eq("token_index",new BigInteger(tokenIndex.substring(2),16).intValue())
    }
}
