package com.enation.app.javashop.client.statistics.impl;

import com.enation.app.javashop.client.statistics.OrderDataClient;
import com.enation.app.javashop.service.statistics.OrderDataManager;
import com.enation.app.javashop.model.trade.order.dos.OrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * OrderDataClientDefaultImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午2:41
 */

@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class OrderDataClientDefaultImpl implements OrderDataClient {

    @Autowired
    private OrderDataManager orderDataManager;
    /**
     * 订单新增
     *
     * @param order
     */
    @Override
    public void put(OrderDO order) {
        orderDataManager.put(order);
    }

    /**
     * 订单修改
     *
     * @param order
     */
    @Override
    public void change(OrderDO order) {
        orderDataManager.change(order);
    }
}
