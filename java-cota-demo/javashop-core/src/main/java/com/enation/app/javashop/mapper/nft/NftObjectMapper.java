package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftJob;
import com.enation.app.javashop.model.nft.dos.NftObject;
import com.enation.app.javashop.model.nft.dos.NftOrder;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * NFT对象Mapper接口
 * @author ygg
 * @version v1.0
 * @since v7.2.2
 * 2021-08-05
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface NftObjectMapper extends BaseMapper<NftObject> {
    @Select({"select * from es_nft_object where cota_id =#{cotaId} and token_index=#{tokenIndex}"})
    NftObject getNftBycti(String cotaId,String tokenIndex);

}

