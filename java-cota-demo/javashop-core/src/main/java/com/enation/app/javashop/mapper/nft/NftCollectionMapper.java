package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftCollection;
import com.enation.app.javashop.model.nft.dos.NftConfig;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 收藏品Mapper接口
 * @author ygg
 * @version v1.0
 * @since v7.2.2
 * 2021-08-05
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface NftCollectionMapper extends BaseMapper<NftCollection> {

    @Select({"select * from es_nft_collection c \n" +
            "    left join es_nft_play_bill e on c.id = e.collection_id" +
            " ${ew.customSqlSegment}"})
    IPage<Map> pageCollection(Page<Map> page, @Param(Constants.WRAPPER) Wrapper wrapper);


}

