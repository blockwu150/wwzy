package com.enation.app.javashop.service.promotion.groupbuy;

import com.enation.app.javashop.model.promotion.groupbuy.dos.GroupbuyQuantityLog;

import java.util.List;

/**
 * 团购商品库存日志表业务层
 * @author xlp
 * @version v1.0
 * @since v7.0.0
 * 2018-07-09 15:32:29
 */
public interface GroupbuyQuantityLogManager	{

	/**
	 * 还原团购库存
	 * @param orderSn 订单编号
	 * @return List<GroupbuyQuantityLog> 团购商品库存操作日志信息集合
	 */
	List<GroupbuyQuantityLog> rollbackReduce(String orderSn);

	/**
	 * 添加团购商品库存日志表
	 * @param groupbuyQuantityLog 团购商品库存日志表
	 * @return GroupbuyQuantityLog 团购商品库存操作日志信息
	 */
	GroupbuyQuantityLog add(GroupbuyQuantityLog groupbuyQuantityLog);



}
