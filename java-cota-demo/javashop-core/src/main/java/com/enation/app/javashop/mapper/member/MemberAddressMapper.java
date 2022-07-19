package com.enation.app.javashop.mapper.member;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.member.dos.MemberAddress;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * 会员收货地址Mapper接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.2.2
 * 2020-07-23
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface MemberAddressMapper extends BaseMapper<MemberAddress> {

}
