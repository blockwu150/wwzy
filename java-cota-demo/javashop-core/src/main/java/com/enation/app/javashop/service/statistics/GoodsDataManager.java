package com.enation.app.javashop.service.statistics;

import com.enation.app.javashop.model.statistics.dto.GoodsData;

/**
 * 商品收集manager
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/8 下午4:11
 */

public interface GoodsDataManager {
    /**
     * 新增商品
     *
     * @param goodsIds 商品id
     */
    void addGoods(Long[] goodsIds);

    /**
     * 修改商品
     *
     * @param goodsIds 商品id
     */
    void updateGoods(Long[] goodsIds);

    /**
     * 删除商品
     *
     * @param goodsIds 商品id
     */
    void deleteGoods(Long[] goodsIds);

    /**
     * 修改商品收藏数量
     * @param goodsData 商品统计数据
     */
    void updateCollection(GoodsData goodsData);


    /**
     * 获取商品
     * @param goodsId 商品id
     * @return 商品统计数据
     */
    GoodsData get(Long goodsId);

    /**
     * 下架所有商品
     * @param sellerId 商家id
     */
    void underAllGoods(Long sellerId);

}
