package com.enation.app.javashop.model.payment.enums;

/**
 * 登陆超时时间枚举
 *
 * @author cs
 * @version v1.0
 * @Description:
 * @since v7.2
 * 2020/11/2
 */
public enum LoginOutTimeEnmu {
    /**
     * APP端Token过期时间
     * 60*60*24*90
     */
    TOKEN_APP(7776000L),

    /**
     * APP端refreshToken过期时间
     * 60*60*24*90+60
     */
    REFRESH_TOKEN_APP(7776060L),
    /**
     * 小程序端Token过期时间
     * 60*60*24*2
     */
    TOKEN_MINI(172800L),
    /**
     * 小程序端refreshToken过期时间
     * 60*60*24*2+60
     */
    REFRESH_TOKEN_MINI(172860L);
    private Long text;

    LoginOutTimeEnmu(Long text){
        this.text = text;
    }

    public Long getText() {
        return text;
    }

}
