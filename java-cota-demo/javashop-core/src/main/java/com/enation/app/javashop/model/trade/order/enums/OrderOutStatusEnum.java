package com.enation.app.javashop.model.trade.order.enums;

/**
 * 订单出库状态枚举
 *
 * @author Snow create in 2018/7/10
 * @version v2.0
 * @since v7.0.0
 */
public enum OrderOutStatusEnum {

    /** 等待出库 */
    WAIT("等待出库"),

    /** 出库成功 */
    SUCCESS("出库成功"),

    /** 出库失败 */
    FAIL("出库失败");


    private String description;

    OrderOutStatusEnum(String description){
        this.description = description;
    }

    public String description(){
        return this.description;
    }



}
