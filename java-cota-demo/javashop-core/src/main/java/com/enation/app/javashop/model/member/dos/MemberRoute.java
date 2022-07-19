package com.enation.app.javashop.model.member.dos;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author ygg
 * @version v7.2.2
 * @Description 会员路由DO
 * @ClassName MemberRouteDO
 * @since v7.2.2 下午2:43 2021/1/11
 */
@TableName(value = "es_member_route")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberRoute implements Serializable {


    private static final long serialVersionUID = -7680352684899897909L;
    /**
     * 会员ID
     */
    @ApiModelProperty(name = "member_id",value = "会员id")
    private Long memberId;

    /**
     * 路由会员ID
     */
    @ApiModelProperty(name = "route_member_id",value = "路由会员ID")
    private Long routeMemberId;


    /**
     * 路由次序
     */
    @ApiModelProperty(name = "route_order",value = "路由次序")
    private Long routeOrder;


    public MemberRoute(){

    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getRouteMemberId() {
        return routeMemberId;
    }

    public void setRouteMemberId(Long routeMemberId) {
        this.routeMemberId = routeMemberId;
    }

    public Long getRouteOrder() {
        return routeOrder;
    }

    public void setRouteOrder(Long routeOrder) {
        this.routeOrder = routeOrder;
    }

    @Override
    public String toString() {
        return "RouteDO{" +
//                "id=" + id +
                " memberId=" + memberId + '\'' +
                ", routeOrder='" + routeOrder + '\'' +
                ", routeMemberId='" + routeMemberId + '}';
//                ", unionType='" + unionType + '\'' +
//                ", unboundTime=" + unboundTime +
//                '}';
    }
}


