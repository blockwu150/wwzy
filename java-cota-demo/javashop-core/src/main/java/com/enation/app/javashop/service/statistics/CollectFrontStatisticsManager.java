package com.enation.app.javashop.service.statistics;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.statistics.vo.SimpleChart;

/**
* 商家中心，商品收藏统计
*
* @author mengyuanming
* @version 2.0
* @since 7.0
* 2018年4月20日下午4:23:58
*/
public interface CollectFrontStatisticsManager {

	/**
	 * 商品收藏图表数据
	 *
	 * @param sellerId，商家id
	 * @return SimpleChart，简单图表数据
	 */
	SimpleChart getChart(Long sellerId);

	/**
	 * 商品收藏列表数据
	 * @param pageNo，页码
	 * @param pageSize，页面数据量
	 * @param sellerId，商家id
	 * @return WebPage，分页数据
	 */
	WebPage getPage(Long pageNo, Long pageSize, Long sellerId);

}
