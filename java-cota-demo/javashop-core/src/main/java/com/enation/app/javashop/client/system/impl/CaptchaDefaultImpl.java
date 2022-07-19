package com.enation.app.javashop.client.system.impl;

import com.enation.app.javashop.service.base.service.CaptchaManager;
import com.enation.app.javashop.client.system.CaptchaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 验证码默认实现
 *
 * @author zh
 * @version v7.0
 * @date 18/7/31 上午10:51
 * @since v7.0
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class CaptchaDefaultImpl implements CaptchaClient {

    @Autowired
    private CaptchaManager captchaManager;

    @Override
    public boolean valid(String uuid, String code, String scene) {
        return this.captchaManager.valid(uuid, code, scene);
    }

    @Override
    public void deleteCode(String uuid, String code, String scene) {
        this.captchaManager.deleteCode(uuid, code, scene);
    }
}
