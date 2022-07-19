package com.enation.app.javashop.service.trade.order.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.enation.app.javashop.mapper.trade.order.TransactionRecordMapper;
import com.enation.app.javashop.model.trade.order.dos.TransactionRecord;
import com.enation.app.javashop.service.trade.order.TransactionRecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 交易记录表业务类
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-05-25 15:37:56
 */
@Service
public class TransactionRecordManagerImpl implements TransactionRecordManager {

	@Autowired
	private TransactionRecordMapper transactionRecordMapper;

	/**
	 * 查询交易记录表列表
	 * @param orderSn 订单编号
	 * @return 交易记录表列表
	 */
	@Override
	public List listAll(String orderSn){

		List list = new QueryChainWrapper<>(transactionRecordMapper)
				//按订单编号查询
				.eq("order_sn", orderSn)
				//列表查询
				.list();

		return list;
	}

	/**
	 * 添加交易记录表
	 * @param transactionRecord 交易记录表
	 * @return TransactionRecord 交易记录表
	 */
	@Override
	@Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	TransactionRecord  add(TransactionRecord	transactionRecord)	{
		transactionRecordMapper.insert(transactionRecord);
		return transactionRecord;
	}

	/**
	 * 修改交易记录表
	 * @param transactionRecord 交易记录表
	 * @param id 交易记录表主键
	 * @return TransactionRecord 交易记录表
	 */
	@Override
	@Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	TransactionRecord  edit(TransactionRecord	transactionRecord,Long id){
		transactionRecord.setRecordId(id);
		transactionRecordMapper.updateById(transactionRecord);
		return transactionRecord;
	}


	/**
	 * 删除交易记录表
	 * @param id 交易记录表主键
	 */
	@Override
	@Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete( Long id)	{
		transactionRecordMapper.deleteById(id);
	}

	/**
	 * 获取交易记录表
	 * @param id 交易记录表主键
	 * @return TransactionRecord  交易记录表
	 */
	@Override
	public	TransactionRecord getModel(Long id)	{
		return transactionRecordMapper.selectById(id);
	}
}
