package com.enation.app.javashop.model.member.dto;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Arrays;

/**
 * 会员优惠券查询参数
 *
 * @author Snow create in 2018/6/12
 * @version v2.0
 * @since v7.0.0
 */
public class MemberCouponQueryParam {

    @ApiModelProperty(value = "页码", name = "page_no")
    private Long pageNo;

    @ApiModelProperty(value = "条数", name = "page_size")
    private Long pageSize;

    @ApiModelProperty(value = "优惠券状态 0为全部，1为未使用，2为已使用，3为已过期",allowableValues = "0,1,2")
    private Integer status;

    /**
     * 此项为订单结算页，根据订单金额读取会员可用的优惠券
     */
    @ApiModelProperty(value = "订单总金额", name = "order_price")
    private Double orderPrice;

    @ApiModelProperty(value = "商家ID集合")
    private Long[] sellerIds;


    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long[] getSellerIds() {
        return sellerIds;
    }

    public void setSellerIds(Long[] sellerIds) {
        this.sellerIds = sellerIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        MemberCouponQueryParam param = (MemberCouponQueryParam) o;

        return new EqualsBuilder()
                .append(pageNo, param.pageNo)
                .append(pageSize, param.pageSize)
                .append(status, param.status)
                .append(orderPrice, param.orderPrice)
                .append(sellerIds, param.sellerIds)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(pageNo)
                .append(pageSize)
                .append(status)
                .append(orderPrice)
                .append(sellerIds)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "MemberCouponQueryParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", status=" + status +
                ", orderPrice=" + orderPrice +
                ", sellerIds=" + Arrays.toString(sellerIds) +
                '}';
    }
}
