package com.enation.app.javashop.service.passport;

import com.enation.app.javashop.model.member.dto.LoginAppDTO;

import java.util.Map;

/**
 * 新浪微博登陆相关接口
 * @author cs
 * @version v1.0
 * @since v7.2.2
 * 2020-10-30
 */
public interface LoginWeiboManager {


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
     * @param redirectUri
     * @return
     */
    Map wapLogin(String code, String uuid, String redirectUri);

    /**
     * app登陆
     * @param loginAppDTO
     * @return
     */
    Map appLogin(LoginAppDTO loginAppDTO);
}
