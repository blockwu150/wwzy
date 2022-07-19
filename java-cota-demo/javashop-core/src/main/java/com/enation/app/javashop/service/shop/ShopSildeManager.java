package com.enation.app.javashop.service.shop;


import java.util.List;

import com.enation.app.javashop.model.shop.dos.ShopSildeDO;

/**
 * 店铺幻灯片业务层
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-28 18:50:58
 */
public interface ShopSildeManager	{

	/**
	 * 查询店铺幻灯片列表
	 * @param shopId 店铺ID
	 * @param clientType 客户端MOBLILE/PC
	 * @return
	 */
	List<ShopSildeDO> list(Long shopId,String clientType);
	/**
	 * 添加店铺幻灯片
	 * @param shopSilde 店铺幻灯片
	 * @return ShopSilde 店铺幻灯片
	 */
	ShopSildeDO add(ShopSildeDO shopSilde);

	/**
	 * 批量修改店铺幻灯片
	 * @param list 幻灯片
	 */
	void edit(List<ShopSildeDO> list);

	/**
	 * 删除店铺幻灯片
	 * @param id 店铺幻灯片主键
	 */
	void delete(Long id);

	/**
	 * 获取店铺幻灯片
	 * @param id 店铺幻灯片主键
	 * @return ShopSilde  店铺幻灯片
	 */
	ShopSildeDO getModel(Long id);

	/**
	* 修改店铺幻灯片
	* @param shopSilde 店铺幻灯片
	* @param id 店铺幻灯片主键
	* @return ShopSilde 店铺幻灯片
	*/
	ShopSildeDO edit(ShopSildeDO shopSilde, Long id);

}
