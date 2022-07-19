package com.enation.app.javashop.model.trade.order.vo;

import com.enation.app.javashop.model.trade.order.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.model.trade.order.enums.OrderStatusEnum;
import com.enation.app.javashop.model.trade.order.enums.OrderTypeEnum;
import com.enation.app.javashop.model.trade.order.enums.PaymentTypeEnum;

import java.util.List;

/**
 * 订单流程对象，用于定义订单流程图
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-03-11
 */

public class OrderFlow {

    /**
     * 款到发货流程
     */
    private static List<OrderFlowNode> PAY_FIRST_FLOW;

    /**
     * 货到付款流程
     */
    private static List<OrderFlowNode> COD_FLOW;

    /**
     * 拼团流程
     */
    private static List<OrderFlowNode> PINTUAN_FLOW;

    /**
     * 取消流程（付款前取消订单）
     */
    private static List<OrderFlowNode> CANCEL_FLOW;

    /**
     * 出库失败流程
     */
    private static List<OrderFlowNode> INTODB_ERROR_FLOW;

    /**
     * 取消流程（付款后收货前取消订单）
     */
    private static List<OrderFlowNode> CANCEL_ORDER_FLOW;

    static {
        initFlow();
    }

    /**
     * 初始化
     */
    private static void initFlow() {

        //款到发货的流程
        PAY_FIRST_FLOW = new MyList()
                .add(OrderStatusEnum.NEW)
                .add(OrderStatusEnum.CONFIRM)
                .add(OrderStatusEnum.PAID_OFF)
                .add(OrderStatusEnum.SHIPPED)
                .add(OrderStatusEnum.ROG)
                .add(OrderStatusEnum.COMPLETE)
                .getList();

        //货到付款的流程
        COD_FLOW = new MyList()
                .add(OrderStatusEnum.NEW)
                .add(OrderStatusEnum.CONFIRM)
                .add(OrderStatusEnum.SHIPPED)
                .add(OrderStatusEnum.ROG)
                .add(OrderStatusEnum.PAID_OFF)
                .add(OrderStatusEnum.COMPLETE)
                .getList();


        //拼团流程
        PINTUAN_FLOW = new MyList()
                .add(OrderStatusEnum.NEW)
                .add(OrderStatusEnum.CONFIRM)
                .add(OrderStatusEnum.PAID_OFF)
                .add(OrderStatusEnum.FORMED)
                .add(OrderStatusEnum.SHIPPED)
                .add(OrderStatusEnum.ROG)
                .add(OrderStatusEnum.COMPLETE)
                .getList();


        //取消订单的流程（付款前取消订单）
        CANCEL_FLOW = new MyList()
                .add(OrderStatusEnum.NEW)
                .add(OrderStatusEnum.CONFIRM)
                .add(OrderStatusEnum.CANCELLED)
                .getList();

        //出库失败的流程
        INTODB_ERROR_FLOW = new MyList()
                .add(OrderStatusEnum.NEW)
                .add(OrderStatusEnum.INTODB_ERROR)
                .getList();

        //取消订单的流程（付款后收货前取消订单）
        CANCEL_ORDER_FLOW = new MyList()
                .add(OrderServiceStatusEnum.NEW)
                .add(OrderServiceStatusEnum.CONFIRM)
                .add(OrderServiceStatusEnum.PAID_OFF)
                .add(OrderServiceStatusEnum.APPLY)
                .add(OrderServiceStatusEnum.PASS)
                .add(OrderServiceStatusEnum.COMPLETE)
                .getList();
    }


    /**
     * 获取相应的订单流程
     * @param orderType 订单类型
     * @param paymentType 付款类型
     * @return
     */
    public static List<OrderFlowNode> getFlow( String orderType,  String paymentType){
        //拼团类型的订单直接返回拼团流程
        if (orderType.equals(OrderTypeEnum.PINTUAN.name())) {
            return PINTUAN_FLOW;
        }


        //在线支付的是 款到发货流程
        if (PaymentTypeEnum.ONLINE.name().equals(paymentType)) {
            return PAY_FIRST_FLOW;
        }

        //货到付款流程
        if (PaymentTypeEnum.COD.name().equals(paymentType)) {
            return  COD_FLOW;
        }

        return null;
    }

    /**
     * 获取取消流程（付款前取消订单）
     * @return
     */
    public static List<OrderFlowNode> getCancelFlow() {
        return CANCEL_FLOW;
    }

    /**
     * 获取出库失败流程
     * @return
     */
    public static List<OrderFlowNode> getIntodbErrorFlow() {
        return INTODB_ERROR_FLOW;
    }

    /**
     * 获取取消流程（付款后收货前取消订单）
     * @return
     */
    public static List<OrderFlowNode> getCancelOrderFlow() {
        return CANCEL_ORDER_FLOW;
    }

}
