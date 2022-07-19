package com.enation.app.javashop.service.payment;

import java.util.Map;

/**
 * 退款接口
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-04-15
 */

public interface RefundManager {

    /**
     * 原路退回
     *
     * @param returnTradeNo 第三方订单号
     * @param refundSn      退款编号
     * @param refundPrice   退款金额
     * @return
     */
    Map originRefund(String returnTradeNo, String refundSn, Double refundPrice);


    /**
     * 查询退款状态
     *
     * @param returnTradeNo 第三方订单号
     * @param refundSn      退款编号
     * @return
     */
    String queryRefundStatus(String returnTradeNo, String refundSn);
}
