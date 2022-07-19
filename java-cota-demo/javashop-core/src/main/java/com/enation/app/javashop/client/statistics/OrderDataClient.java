package com.enation.app.javashop.client.statistics;

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

public interface OrderDataClient {


    /**
     * 订单新增
     *
     * @param order
     */
    void put(OrderDO order);

    /**
     * 订单修改
     *
     * @param order
     */
    void change(OrderDO order);

}
