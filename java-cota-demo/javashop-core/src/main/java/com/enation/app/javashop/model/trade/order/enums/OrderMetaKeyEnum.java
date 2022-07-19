package com.enation.app.javashop.model.trade.order.enums;

/**
 * 订单元Key
 *
 * @author Snow create in 2018/7/5
 * @version v2.0
 * @since v7.0.0
 */
public enum OrderMetaKeyEnum {

    /** 使用的积分 */
    POINT("使用的积分"),

    /** 赠送的积分 */
    GIFT_POINT("赠送的积分"),

    /** 赠送的优惠券 */
    COUPON("赠送的优惠券"),

    /** 优惠券抵扣金额 */
    COUPON_PRICE("优惠券抵扣金额"),

    /** 满减金额 */
    FULL_MINUS("满减金额"),

    /** 赠品*/
    GIFT("赠品"),

    CASH_BACK("返现")

    ;

    private String description;

    OrderMetaKeyEnum(String description){
        this.description=description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description(){
        return this.description;
    }

    public String value(){
        return this.name();
    }

}
