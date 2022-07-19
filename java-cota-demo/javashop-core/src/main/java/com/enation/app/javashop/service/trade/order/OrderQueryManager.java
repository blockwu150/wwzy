package com.enation.app.javashop.service.trade.order;

import com.enation.app.javashop.model.trade.order.dos.OrderDO;
import com.enation.app.javashop.model.trade.order.dos.OrderItemsDO;
import com.enation.app.javashop.model.trade.order.dto.OrderDetailQueryParam;
import com.enation.app.javashop.model.trade.order.dto.OrderQueryParam;
import com.enation.app.javashop.model.trade.order.vo.*;
import com.enation.app.javashop.model.trade.order.dto.OrderDetailDTO;
import com.enation.app.javashop.framework.database.WebPage;

import java.util.List;
import java.util.Map;

/**
 * 订单相关
 *
 * @author Snow create in 2018/5/14
 * @version v2.0
 * @since v7.0.0
 */
public interface OrderQueryManager {


    /**
     * 查询订单表列表
     *
     * @param paramDTO 参数对象
     * @return WebPage
     */
    WebPage list(OrderQueryParam paramDTO);

    /**
     * 按条件导出订单数据
     *
     * @param paramDTO 参数对象
     * @return WebPage
     */
    List<OrderLineVO> export(OrderQueryParam paramDTO);

    /**
     * 读取一个订单详细<br/>
     *
     * @param orderSn    订单编号 必传
     * @param queryParam 参数
     * @return 订单详情实体
     */
    OrderDetailVO getModel(String orderSn, OrderDetailQueryParam queryParam);

    /**
     * 根据订单ID读取一个订单详细
     *
     * @param orderId 订单id
     * @return 订单实体
     */
    OrderDO getModel(Long orderId);

    /**
     * 根据订单编号读取一个订单详细
     * @param orderSn 订单编号
     * @return 订单实体
     */
    OrderDO getOrder(String orderSn);

    /**
     * 读取订单状态的订单数
     *
     * @param memberId 会员id
     * @param sellerId 商家id
     * @return 订单各个状态的订单数
     */
    OrderStatusNumVO getOrderStatusNum(Long memberId, Long sellerId);


    /**
     * 获取某订单的订单项
     *
     * @param orderSn 订单编号
     * @return 订单项集合
     */
    List<OrderItemsDO> orderItems(String orderSn);

    /**
     * 根据订单sn读取，订单的流程
     *
     * @param orderSn 订单编号
     * @return 订单流程
     */
    List<OrderFlowNode> getOrderFlow(String orderSn);


    /**
     * 读取会员所有的订单数量
     *
     * @param memberId 会员id
     * @return 订单数量
     */
    Integer getOrderNumByMemberId(Long memberId);


    /**
     * 读取会员(评论状态)订单数量
     *
     * @param memberId 会员id
     * @param commentStatus 评论状态
     * @return 订单数量
     */
    Integer getOrderCommentNumByMemberId(Long memberId, String commentStatus);


    /**
     * 读取订单列表根据交易编号——系统内部使用 OrderClient
     *
     * @param tradeSn 交易编号
     * @return 订单信息DTO集合
     */
    List<OrderDetailDTO> getOrderByTradeSn(String tradeSn);


    /**
     * 读取订单列表根据交易编号
     *
     * @param tradeSn 交易编号
     * @param memberId 会员id
     * @return 订单明细集合
     */
    List<OrderDetailVO> getOrderByTradeSn(String tradeSn, Long memberId);

    /**
     * 查询一个订单的详细
     *
     * @param orderSn 订单编号
     * @return 订单详情
     */
    OrderDetailDTO getModel(String orderSn);

    /**
     * 获取订单可退款总额
     *
     * @param orderSn 订单编号
     * @return 订单可退款总额
     */
    double getOrderRefundPrice(String orderSn);

    /**
     * 获取几个月之内购买过相关商品的订单数据
     * 获取的订单数据只限已完成和已收货并且未删除的订单
     *
     * @param goodsId  商品id
     * @param memberId 会员id
     * @param month    月数 例：2代表2个月之内
     * @return 订单集合
     */
    List<OrderDO> listOrderByGoods(Long goodsId, Long memberId, Integer month);


    /**
     * 根据订单orderId查询发货单相关信息
     *
     * @param orderId 订单id
     * @return 发货单信息
     */
    InvoiceVO getInvoice(Long orderId);

    /**
     * 根据订单orderId查询货物相关信息
     *
     * @param orderSn 交易订单id
     * @return 货物信息
     */
    List<Map> getItemsPromotionTypeandNum(String orderSn);

    /**
     * 根据订单号集合，查询订单集合
     * @param orderSnList 订单号集合
     * @return 订单集合
     */
    List<OrderDO> queryOrderList(List<String> orderSnList);
}
