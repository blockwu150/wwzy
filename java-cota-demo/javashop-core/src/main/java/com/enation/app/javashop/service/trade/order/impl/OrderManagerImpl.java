package com.enation.app.javashop.service.trade.order.impl;

import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.enation.app.javashop.mapper.trade.order.OrderMapper;
import com.enation.app.javashop.model.trade.order.dos.OrderDO;
import com.enation.app.javashop.model.trade.order.vo.*;
import com.enation.app.javashop.service.trade.order.OrderManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  订单操作实现
 * @author Snow create in 2018/5/21
 * @version v2.0
 * @since v7.0.0
 */
@Service
public class OrderManagerImpl implements OrderManager {

    @Autowired
    private OrderMapper orderMapper;


    /**
     * 修改订单信息
     * @param orderDO 订单实体
     * @return 订单明细实体
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public OrderDetailVO update(OrderDO orderDO) {

        //修改订单
        new UpdateChainWrapper<>(orderMapper)
                .eq("sn", orderDO.getSn())
                .eq("member_id", orderDO.getMemberId())
                .update(orderDO);

        //将orderDO数据拷贝到orderDetailVO中
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtils.copyProperties(orderDO,orderDetailVO);

        return orderDetailVO;
    }


}
