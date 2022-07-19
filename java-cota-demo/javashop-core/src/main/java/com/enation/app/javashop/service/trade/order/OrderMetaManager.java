package com.enation.app.javashop.service.trade.order;

import com.enation.app.javashop.model.promotion.fulldiscount.dos.FullDiscountGiftDO;
import com.enation.app.javashop.model.trade.order.dos.OrderMetaDO;
import com.enation.app.javashop.model.trade.order.enums.OrderMetaKeyEnum;

import java.util.List;

/**
 * 订单元信息
 * @author Snow create in 2018/6/27
 * @version v2.0
 * @since v7.0.0
 */
public interface OrderMetaManager {

    /**
     * 添加订单元
     * @param orderMetaDO 订单元实体
     */
    void add(OrderMetaDO orderMetaDO);

    /**
     * 读取订单元信息
     * @param orderSn 订单编号
     * @param metaKey 订单元Key
     * @return 订单元value
     */
    String getMetaValue(String orderSn,OrderMetaKeyEnum metaKey);

    /**
     * 读取订单元列表
     * @param orderSn 订单编号
     * @return 订单元列表
     */
    List<OrderMetaDO> list(String orderSn);

    /**
     * 修改订单元信息
     * @param orderSn 订单编号
     * @param metaKey 扩展-键
     * @param metaValue 订单元value
     */
    void updateMetaValue(String orderSn, OrderMetaKeyEnum metaKey, String metaValue);

    /**
     * 修改订单元售后状态
     * @param orderSn 订单编号
     * @param metaKey 扩展-键
     * @param status 订单元售后状态
     */
    void updateMetaStatus(String orderSn, OrderMetaKeyEnum metaKey, String status);

    /**
     * 获取一条订单元信息
     * @param orderSn 订单编号
     * @param metaKey 扩展-键
     * @return 订单元实体
     */
    OrderMetaDO getModel(String orderSn, OrderMetaKeyEnum metaKey);

    /**
     * 获取订单赠品信息集合
     * @param orderSn 订单编号
     * @param status 订单赠品的售后状态
     * @return 满优惠赠品实体列表
     */
    List<FullDiscountGiftDO> getGiftList(String orderSn, String status);
}
