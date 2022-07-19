package com.enation.app.javashop.service.trade.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.trade.order.OrderLogMapper;
import com.enation.app.javashop.model.trade.order.dos.OrderLogDO;
import com.enation.app.javashop.service.trade.order.OrderLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单日志表业务类
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-05-16 12:01:34
 */
@Service
public class OrderLogManagerImpl implements OrderLogManager {

	@Autowired
	private OrderLogMapper orderLogMapper;

	/**
	 * 查询订单日志表列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return 订单日志分页数据
	 */
	@Override
	public WebPage list(long page, long pageSize){

		IPage<OrderLogDO> iPage = new QueryChainWrapper<>(orderLogMapper).page(new Page<>(page, pageSize));

		return PageConvert.convert(iPage);
	}

	/**
	 * 查询订单日志表列表
	 * @param orderSn 订单编号
	 * @return 订单日志列表
	 */
	@Override
	public List listAll(String orderSn) {

		List<OrderLogDO> list = new QueryChainWrapper<>(orderLogMapper)
				//按订单编号查询
				.eq("order_sn", orderSn)
				//列表查询
				.list();

		return list;
	}

	/**
	 * 添加订单日志表
	 * @param orderLog 订单日志表
	 * @return OrderLog 订单日志表
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public OrderLogDO add(OrderLogDO orderLog)	{
		orderLogMapper.insert(orderLog);
		return orderLog;
	}

	/**
	 * 修改订单日志表
	 * @param orderLog 订单日志表
	 * @param id 订单日志表主键
	 * @return OrderLog 订单日志表
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public OrderLogDO  edit(OrderLogDO	orderLog,Long id){
		orderLog.setLogId(id);
		orderLogMapper.updateById(orderLog);
		return orderLog;
	}

	/**
	 * 删除订单日志表
	 * @param id 订单日志表主键
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delete( Long id)	{
		orderLogMapper.deleteById(id);
	}

	/**
	 * 获取订单日志表
	 * @param id 订单日志表主键
	 * @return OrderLog  订单日志表
	 */
	@Override
	public	OrderLogDO getModel(Long id)	{
		return orderLogMapper.selectById(id);
	}
}
