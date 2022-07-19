package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftObject;
import com.enation.app.javashop.model.nft.dos.NftOrder;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * NFT订单Mapper接口
 * @author ygg
 * @version v1.0
 * @since v7.2.2
 * 2021-08-05
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface NftOrderMapper extends BaseMapper<NftOrder> {

}

