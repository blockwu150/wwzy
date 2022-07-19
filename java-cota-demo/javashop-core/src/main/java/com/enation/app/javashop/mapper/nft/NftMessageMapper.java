package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.Message;
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
public interface NftMessageMapper extends BaseMapper<Message> {
    @Select({"select se.status, s.*,m.nickname,m.mobile,m.face,IF(se.buyer_id=s.member_id,'t','f') as is_buyer  from es_nft_msg s\n" +
            "    inner join es_member m on s.member_id = m.member_id inner join es_nft_sell se on s.conversation_id=se.conversation_id ${ew.customSqlSegment}"})
    IPage<Map> pageMsg(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

}

