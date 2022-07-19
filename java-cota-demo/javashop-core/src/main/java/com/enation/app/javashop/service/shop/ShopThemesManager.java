package com.enation.app.javashop.service.shop;

import com.enation.app.javashop.framework.database.WebPage;

import java.util.List;

import com.enation.app.javashop.model.shop.dos.ShopThemesDO;
import com.enation.app.javashop.model.shop.vo.ShopThemesVO;

/**
 * 店铺模版业务层
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-27 14:17:32
 */
public interface ShopThemesManager	{

	/**
	 * 查询店铺模版列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param type 模版类型
	 * @return WebPage
	 */
	WebPage list(long page, long pageSize, String type);
	/**
	 * 添加店铺模版
	 * @param shopThemes 店铺模版
	 * @return ShopThemes 店铺模版
	 */
	ShopThemesDO add(ShopThemesDO shopThemes);

	/**
	* 修改店铺模版
	* @param shopThemes 店铺模版
	* @param id 店铺模版主键
	* @return ShopThemes 店铺模版
	*/
	ShopThemesDO edit(ShopThemesDO shopThemes,Long id);

	/**
	 * 删除店铺模版
	 * @param id 店铺模版主键
	 */
	void delete(Long id);

	/**
	 * 获取店铺模版
	 * @param id 店铺模版主键
	 * @return ShopThemes  店铺模版
	 */
	ShopThemesDO getModel(Long id);

	/**
	 * 切换当前店铺模板
     * @param themesId 模板Id
     */
	void changeShopThemes(Long themesId);

	/**
	 * 获取当前默认模板
	 * @param type 模版类型 PC WAP
	 * @return 店铺模版
	 */
	ShopThemesDO getDefaultShopThemes(String type);
	/**
	 * 获取模版列表 不分页
	 * @param type 模版类型
	 * @return
	 */
	List<ShopThemesVO> list(String type);
	/**
	 * 获取店铺默认模版
	 * @param type 模版类型
	 * @return 店铺模版
	 */
	ShopThemesDO get(String type);
}
