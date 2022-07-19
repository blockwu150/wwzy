package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftAlbum;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 商城配置Mapper接口
 * @author ygg
 * @version v1.0
 * @since v7.2.2
 * 2021-08-05
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface NftAlbumMapper extends BaseMapper<NftAlbum> {

    @Select({"select a.*,m.face as issuer_face,m.nickname as issuer_name from es_nft_album a inner join es_member m on a.issuer_id = m.member_id ${ew.customSqlSegment}"})
    IPage<Map> pageNftAlbum(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

//    @Select({"select l.id,l.member_id ,m.nickname,m.mobile,case l.asset when 2 then '金豆' when 3 then '矿镐' when 4 then '金砂' when 5 then '锁仓' end  as asset,l.remark,l.amount,l.create_time\n" +
//            "from es_game_ledger l inner join es_member m on m.member_id=l.member_id ${ew.customSqlSegment}"})
//    IPage<Map> pageGameLedger(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

}

