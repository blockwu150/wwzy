package com.enation.app.javashop.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enation.app.javashop.framework.cache.MybatisRedisCache;
import com.enation.app.javashop.model.system.dos.MessageTemplateDO;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * 消息模版的Mapper
 * @author zhanghao
 * @version v1.0
 * @since v7.2.2
 * 2020/7/21
 */
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface MessageTemplateMapper extends BaseMapper<MessageTemplateDO> {
}
