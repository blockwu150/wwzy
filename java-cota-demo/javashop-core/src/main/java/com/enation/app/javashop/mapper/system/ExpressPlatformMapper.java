package com.enation.app.javashop.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.system.dos.ExpressPlatformDO;
import org.apache.ibatis.annotations.CacheNamespace;


/**
 * 快递平台的Mapper
 * @author zhanghao
 * @version v1.0
 * @since v7.2.2
 * 2020/7/21
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface ExpressPlatformMapper extends BaseMapper<ExpressPlatformDO> {
}
