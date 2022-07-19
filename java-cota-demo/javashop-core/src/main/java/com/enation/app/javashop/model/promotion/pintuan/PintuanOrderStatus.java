package com.enation.app.javashop.model.promotion.pintuan;

/**
 * Created by kingapex on 2019-01-24.
 * 拼团订单状态
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-01-24
 */
public enum  PintuanOrderStatus {

    /**
     * 新订单
     */
    new_order,

    /**
     * 待成团
     */
    wait,

    /**
     * 已经支付
     */
    pay_off,

    /**
     * 已成团
     */
    formed,

    /**
     * 已取消
     */
    cancel
}
