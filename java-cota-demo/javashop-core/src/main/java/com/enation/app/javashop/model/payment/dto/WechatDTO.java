package com.enation.app.javashop.model.payment.dto;

import com.enation.app.javashop.model.member.vo.Auth2Token;
import java.io.Serializable;

/**
 * WechatDTO
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-06-18 11:28 AM
 */
public class WechatDTO implements Serializable {

    /**
     * 需要跳转地址
     */
    private String redirectUrl;


    /**
     * 是否需要重定向，前端跳转重新获取accesstoken（包含openid）
     */
    private boolean needRedirect;


    /**
     * 存储用户信息与微信之间沟通的token
     */
    private Auth2Token auth2Token;


    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean isNeedRedirect() {
        return needRedirect;
    }

    public void setNeedRedirect(boolean needRedirect) {
        this.needRedirect = needRedirect;
    }

    public Auth2Token getAuth2Token() {
        return auth2Token;
    }

    public void setAuth2Token(Auth2Token auth2Token) {
        this.auth2Token = auth2Token;
    }
}
