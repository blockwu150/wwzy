package com.enation.app.javashop.service.promotion.tool.support;

import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.framework.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 促销活动缓存
 * @author Snow
 * @since v6.4
 * @version v1.0
 * 2017年08月18日17:58:12
 */
public class PromotionCacheKeys {

	/**
	 * 读取积分换购redis key
	 * @param activityId
	 * @return
	 */
	public static final String getExchangeKey(Long activityId){
		String key = CachePrefix.STORE_ID_EXCHANGE_KEY.getPrefix()+ activityId;
		return key;
	}

	/**
	 * 读取满优惠redis key
	 * @param activityId
	 * @return
	 */
	public static final String getFullDiscountKey(Long activityId){
		String key = CachePrefix.STORE_ID_FULL_DISCOUNT_KEY.getPrefix()+ activityId;
		return key;
	}

	/**
	 * 读取团购活动redis key
	 * @param activityId
	 * @return
	 */
	public static final String getGroupbuyKey(Long activityId){
		String key = CachePrefix.STORE_ID_GROUP_BUY_KEY.getPrefix()+ activityId;
		return key;
	}

	/**
	 * 读取第二件半价活动redis key
	 * @param activityId
	 * @return
	 */
	public static final String getHalfPriceKey(Long activityId){
		String key = CachePrefix.STORE_ID_HALF_PRICE_KEY.getPrefix()+ activityId;
		return key;
	}

	/**
	 *
	 * 读取单品立减活动 redis key
	 * @param activityId
	 * @return
	 */
	public static final String getMinusKey(Long activityId){
		String key = CachePrefix.STORE_ID_MINUS_KEY.getPrefix()+ activityId;
		return key;
	}

	/**
	 * 读取限时抢购 redis key
	 * @param time 格式为(年月日):20171215   如果为空则默认查询当天
	 * @return
	 */
	public static final String getSeckillKey(String time){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		if(StringUtil.isEmpty(time)){
			time = dateFormat.format(new Date());
		}
		String key = CachePrefix.STORE_ID_SECKILL_KEY.getPrefix()+"_"+time;
		return key;
	}


}
