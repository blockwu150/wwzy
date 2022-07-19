package com.enation.app.javashop.service.payment;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.payment.dos.BankVoucherDO;

import java.util.List;

/**
 * 收款单业务层
 * @author xlp
 * @version v2.0
 * @since v7.0.0
 * 2018-07-18 10:39:51
 */
public interface BankVoucherManager {

	/**
	 * 查询收款单列表
	 * @param buyerId 查询参数
	 * @return WebPage
	 */
	WebPage list(Long pageNo, Long pageSize,Long  buyerId,Integer isPay,Long createTime);
	WebPage list(Long pageNo, Long pageSize,Long  buyerId,String mobile,Integer isPay,Long createTime);

	/**
	 * 添加凭证
	 * @param  bankVoucher 凭证
	 * @return PayLog 收款单
	 */
	BankVoucherDO add(BankVoucherDO bankVoucher);

	/**
	* 修改凭证
	* @param bankVoucher 收款单
	* @param id 收款单主键
	* @return PayLog 收款单
	*/
	BankVoucherDO edit(BankVoucherDO bankVoucher, Long id);

	/**
	 * 删除凭证
	 * @param id 收款单主键
	 */
	void delete(Long id);

	/**
	 * 获取凭证
	 * @param id 凭证主键
	 * @return BankVoucherDO  凭证
	 */
	BankVoucherDO getModel(Long id);


	/**
	 * 返回不分页的数据
	 * @param
	 * @return
	 */
	List<BankVoucherDO> exportExcel(Long buyerId,Integer isPay,Long createTime);
}
