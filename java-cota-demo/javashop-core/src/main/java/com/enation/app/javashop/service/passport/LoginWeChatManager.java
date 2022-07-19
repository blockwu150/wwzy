package com.enation.app.javashop.service.passport;


import com.enation.app.javashop.model.member.dto.WeChatMiniLoginDTO;
import com.enation.app.javashop.model.member.dto.WeChatUserDTO;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * 微信统一登陆服务
 * @author cs
 * @since v1.0
 * @version 7.2.2
 * 2020/09/24
 */
public interface LoginWeChatManager {


    /**
     * 获取授权登录的url
     *
     * @return
     */
    String getLoginUrl(String redirectUri);


    /**
     * 获取accesstoken
     *
     * @param code
     * @return
     */
    JSONObject getAccessToken(String code);


    /**
     * 获取unionid
     * @param code 微信h5授权返回的code
     * @return
     */
    Map wxWapLogin(String code, String uuid);

    /**
     * unipp app登陆
     * @param weChatUserDTO
     * @return
     */
    Map wxAppLogin(String uuid, WeChatUserDTO weChatUserDTO);

    /**
     * 小程序登陆
     * @param weChatMiniLoginDTO
     * @return
     */
    Map miniLogin(WeChatMiniLoginDTO weChatMiniLoginDTO);

    /**
     * 获取小程序的code
     * @param code
     * @return
     */
    String getMiniOpenid(String code);

    /**
     * 微信小程序绑定手机号
     * @param
     * @return
     */
    Map miniBindPhone(String encryptedData, String iv);
}
