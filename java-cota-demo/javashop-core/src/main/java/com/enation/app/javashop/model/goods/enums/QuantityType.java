package com.enation.app.javashop.model.goods.enums;

/**
 * Created by kingapex on 2019-01-17.
 * 库存数类型
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2019-01-17
 */
public enum  QuantityType {

    /**
     * 实际的库存，包含了待发货的
     */
    actual,

    /**
     * 可以售的库存，不包含待发货的
     */
    enable

}
