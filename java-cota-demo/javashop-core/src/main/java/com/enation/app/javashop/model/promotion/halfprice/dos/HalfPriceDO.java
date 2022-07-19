package com.enation.app.javashop.model.promotion.halfprice.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 第二件半价实体
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-23 19:53:42
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@TableName("es_half_price")
public class HalfPriceDO implements Serializable {

    private static final long serialVersionUID = 6857594636337885L;

    /**第二件半价活动ID*/
    @ApiModelProperty(hidden=true)
    @TableId(type = IdType.ASSIGN_ID)
    private Long hpId;

    /**起始时间*/
    @Column(name = "start_time")
    @NotNull(message = "请填写活动起始时间")
    @Min(value = 0,message = "活动起始时间不正确")
    @ApiModelProperty(name="start_time",value="起始时间",required=true)
    private Long startTime;

    /**结束时间*/
    @Column(name = "end_time")
    @NotNull(message = "请填写活动截止时间")
    @Min(value = 0,message = "活动结束时间不正确")
    @ApiModelProperty(name="end_time",value="结束时间",required=true)
    private Long endTime;

    /**活动标题*/
    @Column(name = "title")
    @NotEmpty(message = "请填写活动标题")
    @ApiModelProperty(name="title",value="活动标题",required=true)
    private String title;

    /**商品参与方式*/
    @Column(name = "range_type")
    @NotNull(message = "请选择商品参与方式")
    @Min(value = 1,message = "商品参与方式值不正确")
    @Max(value = 2,message = "商品参与方式值不正确")
    @ApiModelProperty(name="range_type",value="商品参与方式,全部商品：1，部分商品：2",required=true)
    private Integer rangeType;

    /**是否停用 0.没有停用 1.停用*/
    @Column(name = "disabled")
    @ApiModelProperty(name="disabled",value="是否停用",required=false)
    private Integer disabled;

    /**活动说明*/
    @Column(name = "description")
    @ApiModelProperty(name="description",value="活动说明",required=false)
    private String description;

    /**商家id*/
    @Column(name = "seller_id")
    @ApiModelProperty(name="seller_id",value="商家id",required=false)
    private Long sellerId;

    @PrimaryKeyField
    public Long getHpId() {
        return hpId;
    }
    public void setHpId(Long hpId) {
        this.hpId = hpId;
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

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRangeType() {
        return rangeType;
    }
    public void setRangeType(Integer rangeType) {
        this.rangeType = rangeType;
    }

    public Integer getDisabled() {
        return disabled;
    }
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "HalfPriceDO{" +
                "hpId=" + hpId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", title='" + title + '\'' +
                ", rangeType=" + rangeType +
                ", disabled=" + disabled +
                ", description='" + description + '\'' +
                ", sellerId=" + sellerId +
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

        HalfPriceDO that = (HalfPriceDO) o;

        return new EqualsBuilder()
                .append(hpId, that.hpId)
                .append(startTime, that.startTime)
                .append(endTime, that.endTime)
                .append(title, that.title)
                .append(rangeType, that.rangeType)
                .append(disabled, that.disabled)
                .append(description, that.description)
                .append(sellerId, that.sellerId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(hpId)
                .append(startTime)
                .append(endTime)
                .append(title)
                .append(rangeType)
                .append(disabled)
                .append(description)
                .append(sellerId)
                .toHashCode();
    }
}
