package com.enation.app.javashop.mapper.distribution;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.distribution.dos.WithdrawSettingDO;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * 提现设置的Mapper
 * @author zhanghao
 * @version v1.0
 * @since v7.2.2
 * 2020/8/5
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface WithdrawSettingMapper extends BaseMapper<WithdrawSettingDO> {
}
