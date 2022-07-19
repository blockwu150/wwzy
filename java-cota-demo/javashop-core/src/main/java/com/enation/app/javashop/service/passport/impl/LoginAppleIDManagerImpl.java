package com.enation.app.javashop.service.passport.impl;

import com.enation.app.javashop.model.member.dto.AppleIDUserDTO;
import com.enation.app.javashop.model.member.dto.LoginUserDTO;
import com.enation.app.javashop.model.member.enums.ConnectTypeEnum;
import com.enation.app.javashop.service.member.ConnectManager;
import com.enation.app.javashop.service.passport.LoginAppleIDManager;
import com.enation.app.javashop.service.passport.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * AppleID登陆相关接口
 * @author snow
 * @version v1.0
 * @since v7.2.2
 * 2020-12-17
 */
@Service
public class LoginAppleIDManagerImpl implements LoginAppleIDManager {

    @Autowired
    private ConnectManager connectManager;

    @Autowired
    private LoginManager loginManager;

    @Override
    public Map appleIDLogin(String uuid, AppleIDUserDTO appleIDUserDTO) {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUuid(uuid);
        loginUserDTO.setTokenOutTime(null);
        loginUserDTO.setRefreshTokenOutTime(null);
        loginUserDTO.setUnionType(ConnectTypeEnum.ALIPAY);
        loginUserDTO.setUnionid(appleIDUserDTO.getOpenid());
        return loginManager.loginByUnionId(loginUserDTO);
    }
}
