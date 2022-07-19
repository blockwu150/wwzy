package com.enation.app.javashop.model.promotion.tool.dto;

import com.enation.app.javashop.framework.database.annotation.Column;
import io.swagger.annotations.ApiModelProperty;

/**
 * 促销信息DTO
 * @author Snow create in 2018/4/16
 * @version v2.0
 * @since v7.0.0
 */
public class PromotionDetailDTO {

    /**活动开始时间*/
    @Column(name = "start_time")
    @ApiModelProperty(name="start_time",value="活动开始时间",required=false)
    private Long startTime;

    /**活动结束时间*/
    @Column(name = "end_time")
    @ApiModelProperty(name="end_time",value="活动结束时间",required=false)
    private Long endTime;

    /**活动id*/
    @Column(name = "activity_id")
    @ApiModelProperty(name="activity_id",value="活动id",required=false)
    private Long activityId;

    /**促销工具类型*/
    @Column(name = "promotion_type")
    @ApiModelProperty(name="promotion_type",value="促销工具类型",required=false)
    private String promotionType;

    /**活动标题*/
    @Column(name = "title")
    @ApiModelProperty(name="title",value="活动标题",required=false)
    private String title;


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

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PromotionDetailDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", activityId=" + activityId +
                ", promotionType='" + promotionType + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
