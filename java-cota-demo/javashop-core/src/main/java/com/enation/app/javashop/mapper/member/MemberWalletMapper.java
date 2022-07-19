package com.enation.app.javashop.mapper.member;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.member.dos.MemberWalletDO;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

/**
 * 会员钱包Mapper接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.2.2
 * 2020-07-23
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface MemberWalletMapper extends BaseMapper<MemberWalletDO> {

}
