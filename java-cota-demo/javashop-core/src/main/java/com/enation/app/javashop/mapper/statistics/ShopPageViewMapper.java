package com.enation.app.javashop.mapper.statistics;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.statistics.dos.ShopPageView;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * 店铺访问量统计mapper
 * @author zs
 * @version v1.0
 * @since v7.2.2
 * 2020-08-03
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface ShopPageViewMapper extends BaseMapper<ShopPageView> {

}
