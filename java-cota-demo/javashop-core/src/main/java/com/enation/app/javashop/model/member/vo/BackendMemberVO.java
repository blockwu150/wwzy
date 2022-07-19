package com.enation.app.javashop.model.member.vo;


import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 后台首页会员vo
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-30 14:27:48
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BackendMemberVO implements Serializable {


    /**
     * 昵称
     */
    @ApiModelProperty(name = "nickname", value = "昵称", required = false)
    private String nickname;
    /**
     * 邮箱
     */
    @ApiModelProperty(name = "email", value = "邮箱", required = false)
    private String email;
    /**
     * 会员注册时间
     */
    @ApiModelProperty(name = "createTime", value = "会员注册时间", required = false)
    private Long createTime;

    /**
     * 手机号码
     */
    @ApiModelProperty(name = "mobile", value = "手机号码", required = true)
    private String mobile;


    @ApiModelProperty(name = "uname", value = "会员用户名", required = true)
    private String uname;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "BackendMemberVO{" +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                ", mobile='" + mobile + '\'' +
                ", uname='" + uname + '\'' +
                '}';
    }
}