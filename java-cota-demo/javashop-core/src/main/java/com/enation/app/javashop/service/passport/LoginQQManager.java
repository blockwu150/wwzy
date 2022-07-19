package com.enation.app.javashop.service.passport;


import com.enation.app.javashop.model.member.dto.QQUserDTO;
import com.enation.app.javashop.model.member.dto.WeChatMiniLoginDTO;
import com.enation.app.javashop.model.member.dto.WeChatUserDTO;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * QQ统一登陆服务
 * @author cs
 * @since v1.0
 * @version 7.2.2
 * 2020/09/24
 */
public interface LoginQQManager {





    /**
     * 获取unionid
     * @param accessToken QQh5授权返回的code
     * @return
     */
    Map qqWapLogin(String accessToken, String uuid);

    /**
     * QQ app登陆
     * @param qqUserDTO
     * @return
     */
    Map qqAppLogin(String uuid, QQUserDTO qqUserDTO);

    /**
     * 获取wap端appid
     * @return
     */
    String getAppid();
}
