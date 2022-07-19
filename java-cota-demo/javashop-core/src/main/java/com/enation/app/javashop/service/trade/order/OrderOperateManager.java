package com.enation.app.javashop.service.trade.order;

import com.enation.app.javashop.model.trade.cart.dos.OrderPermission;
import com.enation.app.javashop.model.trade.complain.enums.ComplainSkuStatusEnum;
import com.enation.app.javashop.model.trade.order.dos.OrderDO;
import com.enation.app.javashop.model.trade.order.enums.CommentStatusEnum;
import com.enation.app.javashop.model.trade.order.enums.OrderOperateEnum;
import com.enation.app.javashop.model.trade.order.enums.OrderServiceStatusEnum;
import com.enation.app.javashop.model.trade.order.enums.OrderStatusEnum;
import com.enation.app.javashop.model.trade.order.vo.*;

/**
 * 订单流程操作
 *
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
public interface OrderOperateManager {

    /**
     * 确认订单
     *
     * @param confirmVO  订单确认vo
     * @param permission 需要检测的订单权限
     */
    void confirm(ConfirmVO confirmVO, OrderPermission permission);

    /**
     * 为某订单付款<br/>
     *
     * @param orderSn    订单号
     * @param payPrice   本次付款金额
     * @param returnTradeNo 支付方式返回的交易号
     * @param permission 需要检测的订单权限
     * @param permission 权限 {@link OrderPermission}
     * @return
     * @throws IllegalArgumentException 下列情形之一抛出此异常:
     *                                  <li>order_sn(订单id)为null</li>
     * @throws IllegalStateException    如果订单支付状态为已支付
     */
    OrderDO payOrder(String orderSn, Double payPrice, String returnTradeNo, OrderPermission permission);


    /**
     * 发货
     *
     * @param deliveryVO 运单</br>
     * @param permission 需要检测的订单权限
     */
    void ship(DeliveryVO deliveryVO, OrderPermission permission);


    /**
     * 订单收货
     *
     * @param rogVO      收货VO
     * @param permission 需要检测的订单权限
     */
    void rog(RogVO rogVO, OrderPermission permission);


    /**
     * 订单取消
     *
     * @param cancelVO   取消vo
     * @param permission 需要检测的订单权限
     */
    void cancel(CancelVO cancelVO, OrderPermission permission);

    /**
     * 订单完成
     *
     * @param completeVO 订单完成vo
     * @param permission 需要检测的订单权限
     */
    void complete(CompleteVO completeVO, OrderPermission permission);

    /**
     * 更新订单的售后状态
     *
     * @param orderSn 订单编号
     * @param serviceStatus 售后状态
     */
    void updateServiceStatus(String orderSn, OrderServiceStatusEnum serviceStatus);


    /**
     * 修改收货人信息
     *
     * @param orderConsignee 收货信息
     * @return 收货信息
     */
    OrderConsigneeVO updateOrderConsignee(OrderConsigneeVO orderConsignee);

    /**
     * 修改订单价格
     *
     * @param orderSn 订单编号
     * @param orderPrice 订单价格
     */
    void updateOrderPrice(String orderSn, Double orderPrice);

    /**
     * 更新订单的评论状态
     *
     * @param orderSn 订单编号
     * @param commentStatus 评论状态
     */
    void updateCommentStatus(String orderSn, CommentStatusEnum commentStatus);

    /**
     * 更新货物列表json
     *
     * @param itemsJson 货物列表json
     * @param orderSn 订单编号
     */
    void updateItemJson(String itemsJson, String orderSn);

    /**
     * 更新订单的订单状态
     *
     * @param orderSn 订单编号
     * @param orderStatus 订单状态
     * @return 更新结果
     */
    boolean updateOrderStatus(String orderSn, OrderStatusEnum orderStatus);

    /**
     * 更新交易状态
     *
     * @param sn          交易编号
     * @param orderStatus 状态
     * @return 更新结果
     */
    boolean updateTradeStatus(String sn, OrderStatusEnum orderStatus);

    /**
     * 执行操作
     *
     * @param orderSn      订单编号
     * @param permission   权限
     * @param orderOperate 要执行什么操作
     * @param paramVO      参数对象
     */
    void executeOperate(String orderSn, OrderPermission permission, OrderOperateEnum orderOperate, Object paramVO);

    /**
     * 更新订单项可退款金额
     * @param order 订单明细
     */
    void updateItemRefundPrice(OrderDetailVO order);

    /**
     * 更新订单的店铺名称
     * @param shopId 店铺id
     * @param shopName 店铺名称
     */
    void editOrderShopName(Long shopId, String shopName);


    /**
     * 更新订单项评论状态
     * @param orderSn 订单编号
     * @param goodsId 订单项商品id
     * @param commentStatus 评论状态
     */
    void updateItemsCommentStatus(String orderSn,Long goodsId,CommentStatusEnum commentStatus);

    /**
     * 更新订单商品的交易投诉状态
     * @param orderSn 订单编号
     * @param skuId 商品skuid
     * @param complainId 交易投诉id
     * @param status 投诉状态
     */
    void updateOrderItemsComplainStatus(String orderSn, Long skuId, Long complainId, ComplainSkuStatusEnum status);

}
