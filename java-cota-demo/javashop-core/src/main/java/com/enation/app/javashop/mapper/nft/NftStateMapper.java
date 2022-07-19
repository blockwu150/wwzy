package com.enation.app.javashop.mapper.nft;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.nft.dos.NftJob;
import com.enation.app.javashop.model.nft.dos.NftState;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * nft state Mapper接口
 * @author ygg
 * @version v1.0
 * @since v7.2.2
 * 2022-05-19
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface NftStateMapper extends BaseMapper<NftState> {

}

