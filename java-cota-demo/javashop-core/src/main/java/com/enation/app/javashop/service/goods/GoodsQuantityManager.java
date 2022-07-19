package com.enation.app.javashop.service.goods;

import com.enation.app.javashop.model.goods.vo.GoodsQuantityVO;

import java.util.List;
import java.util.Map;


/**
 * 商品库存接口
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午11:47:29
 *
 * @version 3.0
 * 统一为一个接口（更新接口）<br/>
 * 内部实现为redis +lua 保证原子性 -- by kingapex 2019-01-17
 */
public interface GoodsQuantityManager {



	/**
	 * 库存更新接口
 	 * @param goodsQuantityList 要更新的库存vo List
	 * @return 如果更新成功返回真，否则返回假
	 */
	Boolean updateSkuQuantity(List<GoodsQuantityVO> goodsQuantityList );

	/**
	 * 同步数据库数据
	 */
	void syncDataBase();


	/**
	 * 为某个sku 填充库存cache<br/>
	 * 库存数量由数据库中获取<br/>
	 * 一般用于缓存被击穿的情况
	 * @param skuId skuId
	 * @return 可用库存和实际库存
	 */
	Map<String,Integer> fillCacheFromDB(Long skuId);


}
