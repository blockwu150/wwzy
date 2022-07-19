package com.enation.app.javashop.service.promotion.seckill;

import com.enation.app.javashop.model.promotion.seckill.dto.SeckillAuditParam;
import com.enation.app.javashop.model.promotion.seckill.dto.SeckillQueryParam;
import com.enation.app.javashop.model.promotion.seckill.vo.SeckillGoodsVO;
import com.enation.app.javashop.model.promotion.seckill.vo.SeckillVO;
import com.enation.app.javashop.framework.database.WebPage;

/**
 * 限时抢购入库业务层
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-21 10:32:36
 */
public interface SeckillManager	{

	/**
	 * 查询限时抢购促销活动分页列表数据
	 * @param param 搜索参数
	 * @return WebPage
	 */
	WebPage list(SeckillQueryParam param);

	/**
	 * 新增限时抢购促销活动信息
	 * @param seckill 限时抢购促销活动信息
	 * @return Seckill 限时抢购促销活动信息
	 */
	SeckillVO add(SeckillVO seckill);

	/**
	* 修改限时抢购促销活动信息
	* @param seckill 限时抢购促销活动信息
	* @param id 限时抢购促销活动主键ID
	* @return Seckill 限时抢购促销活动信息
	*/
	SeckillVO edit(SeckillVO seckill, Long id);

	/**
	 * 删除限时抢购促销活动信息
	 * @param id 限时抢购促销活动主键ID
	 */
	void delete(Long id);

	/**
	 * 获取限时抢购促销活动信息
	 * @param id 限时抢购促销活动主键ID
	 * @return Seckill 限时抢购促销活动信息
	 */
	SeckillVO getModel(Long id);

	/**
	 * 根据商品ID读取限时秒杀的活动信息
	 * @param goodsId 商品ID
	 * @return
	 */
	SeckillGoodsVO getSeckillGoods(Long goodsId);

	/**
	 * 根据商品ID和商品skuID读取限时秒杀的活动信息
	 * @param goodsId 商品ID
	 * @param skuId 商品skuID
	 * @return
	 */
	SeckillGoodsVO getSeckillSku(Long goodsId, Long skuId);

	/**
	 * 批量审核参与活动的商品
	 * @param param 审核参数
	 */
	void batchAuditGoods(SeckillAuditParam param);

	/**
	 * 商家报名参与限时抢购活动
	 * @param sellerId 商家ID
	 * @param seckillId 限时抢购活动ID
	 */
	void sellerApply(Long sellerId, Long seckillId);

	/**
	 * 关闭某限时抢购活动
	 * @param id 限时抢购活动ID
	 */
    void close(Long id);
}
