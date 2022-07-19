package com.enation.app.javashop.service.statistics;

import com.enation.app.javashop.model.statistics.dos.GoodsPageView;
import com.enation.app.javashop.model.statistics.dos.ShopPageView;

import java.util.List;

/**
 * 访问次数manager
 *
 * @author liushuai
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/8/7 上午8:21
 */

public interface DisplayTimesManager {


    /**
     * 访问某地址
     *
     * @param url  访问的地址
     * @param uuid 客户唯一id
     */
    void view(String url, String uuid);


    /**
     * 立即整理现有的数据
     */
    void countNow();

    /**
     * 将统计好的店铺数据 写入数据库
     *
     * @param list 店铺统计数据
     */
    void countShop(List<ShopPageView> list);


    /**
     * 将统计好的商品数据 写入数据库
     *
     * @param list 商品统计数据
     */
    void countGoods(List<GoodsPageView> list);


}
