package com.enation.app.javashop.service.passport;

import com.enation.app.javashop.model.member.dto.AppleIDUserDTO;

import java.util.Map;

/**
 * AppleID IOS 登陆服务
 * @author snow
 * @since v1.0
 * @version 7.2.2
 * 2020-12-16
 */
public interface LoginAppleIDManager {

    /**
     * IOS-APP 登录
     * @param uuid
     * @param appleIDUserDTO
     * @return
     */
    Map appleIDLogin(String uuid, AppleIDUserDTO appleIDUserDTO);



}
