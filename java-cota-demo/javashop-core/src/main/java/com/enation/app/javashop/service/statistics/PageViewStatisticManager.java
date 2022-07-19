package com.enation.app.javashop.service.statistics;

import com.enation.app.javashop.model.base.SearchCriteria;
import com.enation.app.javashop.model.statistics.vo.SimpleChart;

/**
 * 商家中心与平台后台，流量分析
 * 
 * @author mengyuanming
 * @version 2.0
 * @since 7.0 
 * 2018年3月19日上午8:36:28
 */
public interface PageViewStatisticManager {

	/**
	 * 平台后台 查询店铺流量
	 * 
	 * @param searchCriteria，流量参数类
	 * @return 按时间分组的访问量数据
	 */
	SimpleChart countShop(SearchCriteria searchCriteria);

	/**
	 * 平台后台 查询商品访问量
	 * 
	 * @param searchCriteria，流量参数类
	 * @return 访问流量前30的商品名及流量数据
	 */
	SimpleChart countGoods(SearchCriteria searchCriteria);

}
