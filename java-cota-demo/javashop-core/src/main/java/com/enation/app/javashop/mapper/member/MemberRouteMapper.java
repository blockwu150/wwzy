package com.enation.app.javashop.mapper.member;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.member.dos.MemberRoute;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会员路由Mapper接口
 * @author yugg
 * @version v1.0
 * @since v7.2.2
 * 2021-08-06
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface MemberRouteMapper extends BaseMapper<MemberRoute> {
    @Select("select route_member_id from es_member_route where member_id = #{mId} order by route_order asc ")
    List<Long> getRouteIds(Long mId);
}
