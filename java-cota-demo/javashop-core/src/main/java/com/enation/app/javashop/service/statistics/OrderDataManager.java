package com.enation.app.javashop.service.statistics;

import com.enation.app.javashop.model.trade.order.dos.OrderDO;

/**
 * 订单信息收集manager
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/8 上午8:22
 */

public interface OrderDataManager {


    /**
     * 订单新增
     *
     * @param order 订单数据
     */
    void put(OrderDO order);

    /**
     * 订单修改
     *
     * @param order 订单数据
     */
    void change(OrderDO order);

}
