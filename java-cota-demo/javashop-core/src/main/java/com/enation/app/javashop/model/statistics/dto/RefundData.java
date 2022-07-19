package com.enation.app.javashop.model.statistics.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.model.aftersale.dos.RefundDO;
import com.enation.app.javashop.framework.database.annotation.Column;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * 退货数据
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/3/25 下午10:49
 */

@TableName("es_sss_refund_data")
public class RefundData implements Serializable {
    @ApiModelProperty(value = "主键id")
    @TableId(type= IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "商家id")
    @Column(name = "seller_id")
    private Long sellerId;

    @ApiModelProperty(value = "售后订单sn")
    @Column(name = "refund_sn")
    private String refundSn;

    @ApiModelProperty(value = "订单sn")
    @Column(name = "order_sn")
    private String orderSn;

    @ApiModelProperty(value = "退款金额")
    @Column(name = "refund_price")
    private Double refundPrice;

    @ApiModelProperty(value = "创建日期")
    @Column(name = "create_time")
    private Long createTime;


    public RefundData() {

    }

	public RefundData(RefundDO refund) {
        this.setCreateTime(refund.getCreateTime());
        this.setOrderSn(refund.getOrderSn());
        this.setSellerId(refund.getSellerId());
        this.setRefundPrice(refund.getRefundPrice());
        this.setRefundSn(refund.getSn());
    }

	public RefundData(Map refund) {
		this.setCreateTime(Long.parseLong(refund.get("create_time").toString()));
		this.setOrderSn(refund.get("order_sn").toString());
		this.setSellerId(Long.parseLong(refund.get("seller_id").toString()));
		this.setRefundPrice(Double.parseDouble(refund.get("refund_price").toString()));
		this.setRefundSn(refund.get("refund_sn").toString());
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getRefundSn() {
        return refundSn;
    }

    public void setRefundSn(String refundSn) {
        this.refundSn = refundSn;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Double getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(Double refundPrice) {
        this.refundPrice = refundPrice;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RefundData{" +
                " sellerId=" + sellerId +
                ", refundSn='" + refundSn + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", refundPrice=" + refundPrice +
                ", createTime=" + createTime +
                '}';
    }
}
