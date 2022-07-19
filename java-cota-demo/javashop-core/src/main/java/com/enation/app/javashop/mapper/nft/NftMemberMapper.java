package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftMemberDO;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * NFT会员Mapper接口
 * @author ygg
 * @version v1.0
 * @since v7.2.2
 * 2022-04-11
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface NftMemberMapper extends BaseMapper<NftMemberDO> {
    @Select("select m.* from es_nft_member m ,es_member_route r where m.member_id = r.route_member_id and r.member_id = #{memberId} order by route_order asc ")
    List<NftMemberDO> getRoutes(Long memberId);
    @Select("select m.* from es_nft_member m ,es_member_route r where m.member_id = r.member_id and r.route_member_id = #{memberId}")
    List<NftMemberDO> getWholeTeam(Long memberId);
    @Select("select r.member_id,m.referer_id from es_member_route r inner join es_member m on r.member_id=m.member_id\n" +
            "where r.route_member_id=#{memberId};")
    List<Map<Long,Long>>  getTeamRefer(Long memberId);

    @Select({"select m.member_id,m.nickname,m.mobile,n.cota_address,n.is_registry \n" +
            "from es_member m inner join es_nft_member n on m.member_id=n.member_id \n" +
            "${ew.customSqlSegment}"})
    IPage<Map> pageNftMember(Page<Map> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select m.uname ,n.private_key,n.cota_address,n.is_registry\n" +
            "from es_member m inner join es_nft_member n on m.member_id=n.member_id\n" +
            "where m.member_id =#{memberId};")
    Map<String,Object>  getCipher(Long memberId);

    @Select("<script>select m.member_id from es_member m inner join (\n" +
            "select distinct referer_id from es_member\n" +
            "where referer_id is not null and member_id\n" +
            "in\n" +
            " <foreach collection=\"ids\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">" +
            "        #{item}" +
            " </foreach>" +
            "    ) c on m.member_id=c.referer_id  inner join es_member o on m.member_id=o.referer_id\n" +
            "inner join es_nft_member n on o.member_id=n.member_id and n.is_registry is true\n" +
            "group by m.member_id having count(1) % 3 =0;</script>")
    List<Long> everyThreeRegistry(@Param("ids") Set<Long> ids);


    @Select("select m.nickname,m.member_id,replace(m.mobile,substring(m.mobile,4,4),'***') as phone,n.is_registry\n" +
            "     from es_member m left join es_nft_member n on m.member_id = n.member_id\n" +
            "where m.referer_id = #{memberId};")
    Set<Map> referees(Long memberId);

}

