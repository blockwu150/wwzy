package com.enation.app.javashop.service.promotion.minus;

import com.enation.app.javashop.model.promotion.minus.vo.MinusVO;
import com.enation.app.javashop.framework.database.WebPage;

/**
 * 单品立减接口
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月18日下午9:20:46
 *
 */
public interface MinusManager {

	/**
	 * 查询单品立减促销活动分页数据列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param keywords 查询关键字
	 * @return WebPage
	 */
	WebPage list(long page, long pageSize, String keywords);

	/**
	 * 添加单品立减促销活动信息
	 * @param minus 单品立减促销活动信息
	 * @return Minus 单品立减促销活动信息
	 */
	MinusVO add(MinusVO minus);

	/**
	 * 修改单品立减促销活动信息
	 * @param minus 单品立减促销活动信息
	 * @param id 单品立减促销活动主键ID
	 * @return Minus 单品立减促销活动信息
	 */
	MinusVO edit(MinusVO minus, Long id);

	/**
	 * 根据主键ID删除单品立减促销活动信息
	 * 1.根据活动id删除esMinus中的数据
	 * 2.调用promotionGoodsManager中的删除方法，删除esPromotionGoods表中的数据
	 * 3.删除Redis中的活动实例对象
	 * @param minusId 单品立减活动主键ID
	 */
	void delete(Long minusId);

	/**
	 * 根据主键ID获取单品立减促销活动信息
	 * @param minusId 单品立减活动主键ID
	 * @return MinusVO 单品立减促销活动信息
	 */
	MinusVO getFromDB(Long minusId);

	/**
	 * 验证操作权限<br/>
	 * 如有问题直接抛出权限异常
	 * @param minusId 单品立减活动主键ID
	 */
	void verifyAuth(Long minusId);

}
