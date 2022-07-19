package com.enation.app.javashop.model.trade.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 订单详细查询参数
 * @author Snow create in 2018/7/18
 * @version v2.0
 * @since v7.0.0
 */

@ApiIgnore
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderDetailQueryParam {

    @ApiModelProperty(value = "会员ID")
    private Long buyerId;

    @ApiModelProperty(value = "商家ID")
    private Long sellerId;


    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        OrderDetailQueryParam that = (OrderDetailQueryParam) o;

        return new EqualsBuilder()
                .append(buyerId, that.buyerId)
                .append(sellerId, that.sellerId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(buyerId)
                .append(sellerId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "OrderDetailQueryParam{" +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                '}';
    }
}
