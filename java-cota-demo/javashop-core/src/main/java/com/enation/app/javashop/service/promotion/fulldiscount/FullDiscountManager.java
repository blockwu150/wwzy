package com.enation.app.javashop.service.promotion.fulldiscount;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.promotion.fulldiscount.vo.FullDiscountVO;

/**
 * 满优惠活动业务层
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 10:42:06
 */
public interface FullDiscountManager	{

	/**
	 * 查询满减满赠促销活动信息分页数据集合
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param keywords 查询关键字
	 * @return WebPage
	 */
	WebPage list(long page, long pageSize, String keywords);

	/**
	 * 添加满减满赠促销活动信息
	 * @param fullDiscount 满减满赠促销活动信息
	 * @return FullDiscount 满减满赠促销活动信息
	 */
	FullDiscountVO add(FullDiscountVO fullDiscount);

	/**
	* 修改满减满赠促销活动信息
	* @param fullDiscount 满减满赠促销活动信息
	* @param id 满减满赠促销活动主键ID
	* @return FullDiscount 满减满赠促销活动信息
	*/
	FullDiscountVO edit(FullDiscountVO fullDiscount, Long id);

	/**
	 * 删除满减满赠促销活动信息
	 * @param id 满减满赠促销活动主键ID
	 */
	void delete(Long id);

	/**
	 * 从数据库获取满减满赠促销活动信息
	 * @param fdId 满减满赠促销活动主键ID
	 * @return 满减满赠促销活动信息
	 */
	FullDiscountVO getModel(Long fdId);

	/**
	 * 验证操作权限<br/>
	 * 如有问题直接抛出权限异常
	 * @param id 满减满赠促销活动主键ID
	 */
	void verifyAuth(Long id);



}
