package com.enation.app.javashop.model.trade.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 货运单
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel(description = "货运单")
public class DeliveryVO {

    @ApiModelProperty(value = "订单编号" )
    private String orderSn;

    @ApiModelProperty(value = "货运单号" )
    private String deliveryNo;

    @ApiModelProperty(value = "物流公司" )
    private Long logiId;

    @ApiModelProperty(value = "物流公司名称" )
    private String logiName;

    @ApiModelProperty(hidden=true )
    private String operator;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public Long getLogiId() {
        return logiId;
    }

    public void setLogiId(Long logiId) {
        this.logiId = logiId;
    }

    public String getLogiName() {
        return logiName;
    }

    public void setLogiName(String logiName) {
        this.logiName = logiName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "DeliveryVO{" +
                "orderSn='" + orderSn + '\'' +
                ", deliveryNo='" + deliveryNo + '\'' +
                ", logiId=" + logiId +
                ", logiName='" + logiName + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        DeliveryVO that = (DeliveryVO) o;

        return new EqualsBuilder()
                .append(orderSn, that.orderSn)
                .append(deliveryNo, that.deliveryNo)
                .append(logiId, that.logiId)
                .append(logiName, that.logiName)
                .append(operator, that.operator)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(orderSn)
                .append(deliveryNo)
                .append(logiId)
                .append(logiName)
                .append(operator)
                .toHashCode();
    }
}
