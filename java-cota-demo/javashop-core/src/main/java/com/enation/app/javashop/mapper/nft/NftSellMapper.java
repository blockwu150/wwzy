package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftAlbum;
import com.enation.app.javashop.model.nft.dos.NftSell;
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
public interface NftSellMapper extends BaseMapper<NftSell> {
    @Select({
            "select s.id as sell_id,s.seller_id, s.buyer_id, s.price,s.remark,s.status,s.tx_hash,s.tx_hash_status, s.conversation_id,\n" +
                    "s.create_time ,s.token_index,s.cota_id,s.image,s.name,s.description,m.nickname as s_nickname,m.mobile as s_mobile,m.face as s_face,bm.nickname as b_nickname,bm.mobile as b_mobile,bm.face as b_face\n" +
                    "from es_nft_sell s inner join es_member m on s.seller_id = m.member_id\n" +
                    "left join es_member bm on s.buyer_id = bm.member_id ${ew.customSqlSegment}"})
    IPage<Map> pageSell(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

}

