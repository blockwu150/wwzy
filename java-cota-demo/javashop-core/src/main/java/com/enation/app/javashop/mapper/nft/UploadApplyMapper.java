package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftPlayBill;
import com.enation.app.javashop.model.nft.dos.UploadApply;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 上架Mapper接口
 * @author ygg
 * @version v1.0
 * @since v7.2.2
 * 2021-08-05
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface UploadApplyMapper extends BaseMapper<UploadApply> {




    @Select({"select u.*,m.nickname,m.mobile,c.name as collection_name, c.description as collection_description,c.image as collection_image from es_nft_upload_apply u inner join es_nft_collection c on u.collection_id=c.id\n" +
            "inner join es_member m on m.member_id=c.issuer_id" +
            " ${ew.customSqlSegment}"})
    IPage<Map> pageUploadApply(Page<Map> page, @Param(Constants.WRAPPER) Wrapper wrapper);


}

