package com.enation.app.javashop.model.payment.vo;

import java.io.Serializable;

/**
 * 微信token
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-02-22 上午11:55
 */
public class WechatCgiAccessToken implements Serializable{
    /**
     * accesstoken
     */
    private String accessToken;
    /**
     * expires_in 有效时间
     */
    private Integer expires;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }
}
