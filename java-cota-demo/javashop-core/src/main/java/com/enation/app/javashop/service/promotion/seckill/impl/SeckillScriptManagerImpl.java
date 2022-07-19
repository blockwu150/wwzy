package com.enation.app.javashop.service.promotion.seckill.impl;

import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.promotion.seckill.dos.SeckillApplyDO;
import com.enation.app.javashop.service.promotion.seckill.SeckillScriptManager;
import com.enation.app.javashop.model.promotion.tool.enums.PromotionTypeEnum;
import com.enation.app.javashop.model.promotion.tool.vo.PromotionScriptVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.ScriptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 限时抢购促销活动脚本业务接口实现
 * @author duanmingyu
 * @version v1.0
 * @since v7.2.0
 * 2020-02-19
 */
@SuppressWarnings("Duplicates")
@Service
public class SeckillScriptManagerImpl implements SeckillScriptManager {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Cache cache;

    /**
     * 创建参与限时抢购促销活动商品的脚本数据信息
     * @param promotionId 限时抢购促销活动ID
     * @param goodsList 参与限时抢购促销活动的商品集合
     */
    @Override
    public void createCacheScript(Long promotionId, List<SeckillApplyDO> goodsList) {
        //如果参与促销活动的商品集合不为空并且集合长度不等于0
        if (goodsList != null && goodsList.size() != 0) {
            //批量放入缓存的数据集合
            Map<String, List<PromotionScriptVO>> cacheMap = new HashMap<>();

            //循环skuID集合，将脚本放入缓存中
            for (SeckillApplyDO goods : goodsList) {

                //缓存key
                String cacheKey = CachePrefix.SKU_PROMOTION.getPrefix() + goods.getSkuId();

                //获取商品开始的时刻
                String[] time = this.getTime(goods.getTimeLine(), goods.getStartDay());

                //获取拼团活动脚本信息
                PromotionScriptVO scriptVO = new PromotionScriptVO();

                //渲染并读取限时抢购促销活动脚本信息
                String script = renderScript(time[0], time[1], goods.getPrice());

                scriptVO.setPromotionScript(script);
                scriptVO.setPromotionId(promotionId);
                scriptVO.setPromotionType(PromotionTypeEnum.SECKILL.name());
                scriptVO.setIsGrouped(false);
                scriptVO.setPromotionName("限时抢购");
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
     * 删除商品存放在缓存中的限时抢购促销活动相关的脚本数据信息
     * @param promotionId 限时抢购促销活动ID
     * @param goodsList 参与限时抢购促销活动的商品集合
     */
    @Override
    public void deleteCacheScript(Long promotionId, List<SeckillApplyDO> goodsList) {
        //如果参与促销活动的商品集合不为空并且集合长度不等于0
        if (goodsList != null && goodsList.size() != 0) {
            //需要批量更新的缓存数据集合
            Map<String, List<PromotionScriptVO>> updateCacheMap = new HashMap<>();

            //需要批量删除的缓存key集合
            List<String> delKeyList = new ArrayList<>();

            for (SeckillApplyDO goods : goodsList) {
                //缓存key
                String cacheKey = CachePrefix.SKU_PROMOTION.getPrefix() + goods.getSkuId();

                //从缓存中读取促销脚本缓存
                List<PromotionScriptVO> scriptCacheList = (List<PromotionScriptVO>) cache.get(cacheKey);

                if (scriptCacheList != null && scriptCacheList.size() != 0) {
                    //循环促销脚本缓存数据集合
                    for (PromotionScriptVO script : scriptCacheList) {
                        //如果脚本数据的促销活动信息与当前修改的促销活动信息一致，那么就将此信息删除
                        if (PromotionTypeEnum.SECKILL.name().equals(script.getPromotionType())
                                && script.getPromotionId().intValue() == promotionId.intValue()) {
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
     * 获取限时抢购开始和结束时间
     * @param timeLine 开始时刻
     * @param startDay 活动开始当天的起始时间
     * @return
     */
    private String[] getTime(Integer timeLine, Long startDay) {
        String date = DateUtil.toString(startDay, "yyyy-MM-dd");
        Long startTime = DateUtil.getDateline(date + " " + timeLine + ":00:00", "yyyy-MM-dd HH:mm:ss");
        Long endTime = DateUtil.getDateline(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        String[] time = new String[2];
        time[0] = startTime.toString();
        time[1] = endTime.toString();
        return time;
    }

    /**
     * 渲染并读取限时抢购促销活动脚本信息
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

        logger.debug("生成限时抢购促销活动脚本：" + script);

        return script;
    }
}
