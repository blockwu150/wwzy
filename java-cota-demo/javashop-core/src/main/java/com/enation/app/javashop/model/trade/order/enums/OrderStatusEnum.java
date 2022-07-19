package com.enation.app.javashop.model.trade.order.enums;

/**
 * 订单状态
 *
 * @author Snow
 * @version 1.0
 * @since v7.0.0
 * 2017年3月31日下午2:44:54
 */
public enum OrderStatusEnum {

    /**
     * 新订单
     */
    NEW("新订单"),

    /**
     * 出库失败
     */
    INTODB_ERROR("出库失败"),

    /**
     * 出库失败
     */
    BALANCE_ERROR("预存款抵扣失败"),

    /**
     * 已确认
     */
    CONFIRM("已确认"),

    /**
     * 已付款
     */
    PAID_OFF("已付款"),

    /**
     * 已成团
     */
    FORMED("已经成团"),

    /**
     * 已发货
     */
    SHIPPED("已发货"),

    /**
     * 已收货
     */
    ROG("已收货"),

    /**
     * 已完成
     */
    COMPLETE("已完成"),

    /**
     * 已取消
     */
    CANCELLED("已取消"),

    /**
     * 售后中
     */
    AFTER_SERVICE("售后中");


    private String description;

    OrderStatusEnum(String description) {
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }


}
