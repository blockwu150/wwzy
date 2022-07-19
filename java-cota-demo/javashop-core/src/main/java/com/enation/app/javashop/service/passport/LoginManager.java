package com.enation.app.javashop.service.passport;

import com.enation.app.javashop.model.member.dto.LoginUserDTO;
import com.enation.app.javashop.model.member.enums.ConnectTypeEnum;

import java.util.Map;

public interface LoginManager {

    /**
     * 根据UnionId登陆
     * @param loginUserDTO
     * @return
     */
    Map loginByUnionId(LoginUserDTO loginUserDTO);
}
