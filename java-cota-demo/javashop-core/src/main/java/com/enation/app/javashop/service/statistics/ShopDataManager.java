package com.enation.app.javashop.service.statistics;

import com.enation.app.javashop.model.statistics.dto.ShopData;

/**
 * 店铺收藏数据管理
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/8 下午4:11
 */

public interface ShopDataManager {

    /**
     * 修改店铺收藏数量
     * @param shopData 店铺统计实体
     */
    void updateCollection(ShopData shopData);

    /**
     * 修改店铺信息
     * @param shopData 店铺统计实体
     */
    void updateShopData(ShopData shopData);

    /**
     * 添加店铺数据
     * @param shopData 店铺统计实体
     */
    void add(ShopData shopData);

    /**
     * 获取商品数据
     * @param shopId 商家id
     * @return 店铺统计实体
     */
    ShopData get(Long shopId);


}
