package com.enation.app.javashop.service.promotion.halfprice;

import com.enation.app.javashop.model.promotion.halfprice.dos.HalfPriceDO;
import com.enation.app.javashop.model.promotion.halfprice.vo.HalfPriceVO;
import com.enation.app.javashop.framework.database.WebPage;

/**
 * 第二件半价业务层
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-23 19:53:42
 */
public interface HalfPriceManager	{

	/**
	 * 查询第二件半价促销活动分页数据
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param keywords 查询关键字
	 * @return WebPage
	 */
	WebPage list(long page, long pageSize, String keywords);

	/**
	 * 添加第二件半价促销活动信息
	 * @param halfPrice 第二件半价促销活动信息
	 * @return HalfPrice 第二件半价促销活动信息
	 */
	HalfPriceDO add(HalfPriceVO halfPrice);

	/**
	 * 修改第二件半价促销活动信息
	 * @param halfPrice 第二件半价促销活动信息
	 * @param id 第二件半价促销活动主键ID
	 * @return HalfPrice 第二件半价促销活动信息
	 */
	HalfPriceDO edit(HalfPriceVO halfPrice, Long id);

	/**
	 * 删除第二件半价促销活动信息
	 * @param id 第二件半价促销活动主键ID
	 */
	void delete(Long id);

	/**
	 * 获取第二件半价促销活动信息
	 * @param id 第二件半价促销活动主键ID
	 * @return HalfPrice  第二件半价促销活动信息
	 */
	HalfPriceVO getFromDB(Long id);

	/**
	 * 验证操作权限<br/>
	 * 如有问题直接抛出权限异常
	 * @param id 第二件半价促销活动主键ID
	 */
	void verifyAuth(Long id);

}
