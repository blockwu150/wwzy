package com.enation.app.javashop.service.passport;

import java.util.Map;

/**
 * 支付宝登陆相关接口
 * @author cs
 * @version v1.0
 * @since v7.2.2
 * 2020-10-30
 */
public interface LoginAlipayManager {


    /**
     * 获取wap登陆跳转地址
     * @param redirectUri
     * @return
     */
    String getLoginUrl(String redirectUri);


    /**
     * wap登陆
     * @param code
     * @param uuid
     * @return
     */
    Map wapLogin(String code, String uuid);
}
