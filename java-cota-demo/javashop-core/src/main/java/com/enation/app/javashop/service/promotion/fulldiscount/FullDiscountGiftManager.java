package com.enation.app.javashop.service.promotion.fulldiscount;

import com.enation.app.javashop.model.goods.enums.QuantityType;
import com.enation.app.javashop.model.promotion.fulldiscount.dos.FullDiscountGiftDO;
import com.enation.app.javashop.framework.database.WebPage;

import java.util.List;

/**
 * 满优惠赠品业务层
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-30 17:34:46
 */
public interface FullDiscountGiftManager	{

	/**
	 * 查询赠品分页信息列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param keyWord 搜索关键字
	 * @return
	 */
	WebPage list(long page, long pageSize, String keyWord);

	/**
	 * 添加赠品信息
	 * @param fullDiscountGift 赠品信息
	 * @return FullDiscountGift 赠品信息
	 */
	FullDiscountGiftDO add(FullDiscountGiftDO fullDiscountGift);

	/**
	* 修改赠品信息
	* @param fullDiscountGift 赠品信息
	* @param id 赠品主键ID
	* @return FullDiscountGift 赠品信息
	*/
	FullDiscountGiftDO edit(FullDiscountGiftDO fullDiscountGift, Long id);

	/**
	 * 删除赠品信息
	 * @param id 赠品主键ID
	 */
	void delete(Long id);

	/**
	 * 根据赠品ID获取赠品信息
	 * @param id 赠品主键ID
	 * @return FullDiscountGift 赠品信息
	 */
	FullDiscountGiftDO getModel(Long id);

	/**
	 * 验证操作权限<br/>
	 * 如有问题直接抛出权限异常
	 * @param id 赠品主键ID
	 */
	void verifyAuth(Long id);

	/**
	 * 增加赠品库存
	 * @param giftDOList 赠品信息集合
	 * @return
	 */
	boolean addGiftQuantity(List<FullDiscountGiftDO> giftDOList);

	/**
	 * 增加赠品可用库存
	 * @param giftDOList 赠品信息集合
	 * @return
	 */
	boolean addGiftEnableQuantity(List<FullDiscountGiftDO> giftDOList);

	/**
	 * 减少赠品库存
	 * @param giftDOList 赠品信息集合
	 * @param type 库存类型 actual：实际库存，enable：可用库存
	 * @return
	 */
	boolean reduceGiftQuantity(List<FullDiscountGiftDO> giftDOList, QuantityType type);

	/**
	 * 获取商家所有赠品数据集合
	 * @return
	 */
	List<FullDiscountGiftDO> listAll();

	/**
	 * 验证当前赠品是否参与了满减满赠促销活动并且活动是否正在进行中
	 * @param giftId 赠品主键ID
	 */
	void verifyGift(Long giftId);
}
