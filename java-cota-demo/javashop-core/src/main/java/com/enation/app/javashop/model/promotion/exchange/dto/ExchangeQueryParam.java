package com.enation.app.javashop.model.promotion.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 积分商品搜索参数
 * @author Snow create in 2018/5/29
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel
public class ExchangeQueryParam {


    @ApiModelProperty(name="name",value="商品名称",required=false)
    private String name;

    @ApiModelProperty(name="cat_id",value="积分分类ID",required=false)
    private Long catId;

    @ApiModelProperty(name="sn",value="商品编号",required=false)
    private String sn;

    @ApiModelProperty(name="seller_name",value="店铺名称",required=false)
    private String sellerName;

    @ApiModelProperty(name="page_no",value="第几页",required=false)
    private Long pageNo;

    @ApiModelProperty(name="page_size",value="每页条数",required=false)
    private Long pageSize;

    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        ExchangeQueryParam that = (ExchangeQueryParam) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(catId, that.catId)
                .append(sn, that.sn)
                .append(sellerName, that.sellerName)
                .append(pageNo, that.pageNo)
                .append(pageSize, that.pageSize)
                .append(startTime, that.startTime)
                .append(endTime, that.endTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(catId)
                .append(sn)
                .append(sellerName)
                .append(pageNo)
                .append(pageSize)
                .append(startTime)
                .append(endTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ExchangeQueryParam{" +
                "name='" + name + '\'' +
                ", catId=" + catId +
                ", sn='" + sn + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
