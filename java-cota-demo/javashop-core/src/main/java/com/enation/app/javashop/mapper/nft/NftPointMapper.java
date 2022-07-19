package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftAction;
import com.enation.app.javashop.model.nft.dos.NftPoint;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 动作Mapper接口
 * @author ygg
 * @version v1.0
 * @since v7.2.2
 * 2021-08-05
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface NftPointMapper extends BaseMapper<NftPoint> {
    @Select({"select p.id,f.name,f.thumbnail,p.merged,p.create_time from es_nft_point p \n" +
            "    inner join es_nft_fragment f on f.id = p.fragment_id" +
            " ${ew.customSqlSegment}"})
    IPage<Map> pagePoint(Page<Map> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}

