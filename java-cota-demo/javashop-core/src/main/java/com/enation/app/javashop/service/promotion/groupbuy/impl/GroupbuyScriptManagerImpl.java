package com.enation.app.javashop.service.promotion.groupbuy.impl;

import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.promotion.groupbuy.dos.GroupbuyActiveDO;
import com.enation.app.javashop.service.promotion.groupbuy.GroupbuyActiveManager;
import com.enation.app.javashop.service.promotion.groupbuy.GroupbuyScriptManager;
import com.enation.app.javashop.model.promotion.tool.dos.PromotionGoodsDO;
import com.enation.app.javashop.model.promotion.tool.enums.PromotionTypeEnum;
import com.enation.app.javashop.model.promotion.tool.vo.PromotionScriptVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.ScriptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 团购促销活动脚本业务接口
 * @author duanmingyu
 * @version v1.0
 * @since v7.2.0
 * 2020-02-18
 */
@SuppressWarnings("Duplicates")
@Service
public class GroupbuyScriptManagerImpl implements GroupbuyScriptManager {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Cache cache;

    @Autowired
    private GroupbuyActiveManager groupbuyActiveManager;

    /**
     * 创建参与团购促销活动商品的脚本数据信息
     * @param promotionId 团购促销活动ID
     * @param goodsList 参与团购促销活动的商品集合
     */
    @Override
    public void createCacheScript(Long promotionId, List<PromotionGoodsDO> goodsList) {
        //如果参与团购促销活动的商品集合不为空并且集合长度不为0
        if (goodsList != null && goodsList.size() != 0) {
            //获取团购活动详细信息
            GroupbuyActiveDO groupbuyActiveDO = this.groupbuyActiveManager.getModel(promotionId);

            //批量放入缓存的数据集合
            Map<String, List<PromotionScriptVO>> cacheMap = new HashMap<>();

            //循环参与团购活动的商品集合，将脚本放入缓存中
            for (PromotionGoodsDO goods : goodsList) {

                //缓存key
                String cacheKey = CachePrefix.SKU_PROMOTION.getPrefix() + goods.getSkuId();

                //获取拼团活动脚本信息
                PromotionScriptVO scriptVO = new PromotionScriptVO();

                //渲染并读取团购促销活动脚本信息
                String script = renderScript(groupbuyActiveDO.getStartTime().toString(), groupbuyActiveDO.getEndTime().toString(), goods.getPrice());

                scriptVO.setPromotionScript(script);
                scriptVO.setPromotionId(promotionId);
                scriptVO.setPromotionType(PromotionTypeEnum.GROUPBUY.name());
                scriptVO.setIsGrouped(false);
                scriptVO.setPromotionName("团购");
                scriptVO.setSkuId(goods.getSkuId());

                //从缓存中读取脚本信息
                List<PromotionScriptVO> scriptList = (List<PromotionScriptVO>) cache.get(cacheKey);
                if (scriptList == null) {
                    scriptList = new ArrayList<>();
                }

                scriptList.add(scriptVO);

                cacheMap.put(cacheKey, scriptList);
            }

            //将sku促销脚本数据批量放入缓存中
            cache.multiSet(cacheMap);
        }
    }

    /**
     * 删除商品存放在缓存中的团购促销活动相关的脚本数据信息
     * @param promotionId 团购促销活动ID
     * @param goodsList 参与团购促销活动的商品集合
     */
    @Override
    public void deleteCacheScript(Long promotionId, List<PromotionGoodsDO> goodsList) {
        //如果参与团购促销活动的商品集合不为空并且集合长度不为0
        if (goodsList != null && goodsList.size() != 0) {
            //需要批量更新的缓存数据集合
            Map<String, List<PromotionScriptVO>> updateCacheMap = new HashMap<>();

            //需要批量删除的缓存key集合
            List<String> delKeyList = new ArrayList<>();

            for (PromotionGoodsDO goods : goodsList) {
                //缓存key
                String cacheKey = CachePrefix.SKU_PROMOTION.getPrefix() + goods.getSkuId();

                //从缓存中读取促销脚本缓存
                List<PromotionScriptVO> scriptCacheList = (List<PromotionScriptVO>) cache.get(cacheKey);

                if (scriptCacheList != null && scriptCacheList.size() != 0) {
                    //循环促销脚本缓存数据集合
                    for (PromotionScriptVO script : scriptCacheList) {
                        //如果脚本数据的促销活动信息与当前修改的促销活动信息一致，那么就将此信息删除
                        if (PromotionTypeEnum.GROUPBUY.name().equals(script.getPromotionType())
                                && script.getPromotionId().equals(promotionId)) {
                            scriptCacheList.remove(script);
                            break;
                        }
                    }

                    if (scriptCacheList.size() == 0) {
                        delKeyList.add(cacheKey);
                    } else {
                        updateCacheMap.put(cacheKey, scriptCacheList);
                    }
                }
            }

            cache.multiDel(delKeyList);
            cache.multiSet(updateCacheMap);
        }
    }

    /**
     * 渲染并读取团购促销活动脚本信息
     * @param startTime 活动开始时间
     * @param endTime 活动结束时间
     * @param price 限时抢购商品价格
     * @return
     */
    private String renderScript(String startTime, String endTime, Double price) {
        Map<String, Object> model = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("price", price);

        model.put("promotionActive", params);

        String path = "single_promotion.ftl";
        String script = ScriptUtil.renderScript(path, model);

        logger.debug("生成团购促销活动脚本：" + script);

        return script;
    }
}
