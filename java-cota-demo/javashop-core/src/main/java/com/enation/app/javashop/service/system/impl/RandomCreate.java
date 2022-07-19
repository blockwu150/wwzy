package com.enation.app.javashop.service.system.impl;

import com.enation.app.javashop.model.base.SettingGroup;
import com.enation.app.javashop.service.base.service.SettingManager;
import com.enation.app.javashop.model.system.vo.SiteSetting;
import com.enation.app.javashop.framework.context.ApplicationContextHolder;
import com.enation.app.javashop.framework.util.JsonUtil;

/**
 * 随机验证码生成
 *
 * @author zh
 * @version v7.0
 * @date 18/4/24 下午8:06
 * @since v7.0
 */

public class RandomCreate {

    public static String getRandomCode() {
        // 随机生成的动态码
        String dynamicCode = "" + (int) ((Math.random() * 9 + 1) * 100000);
        //如果是测试模式，验证码为1111
        SettingManager settingManager = (SettingManager) ApplicationContextHolder.getBean("settingManagerImpl");
        String siteSettingJson = settingManager.get(SettingGroup.SITE);

        SiteSetting setting = JsonUtil.jsonToObject(siteSettingJson,SiteSetting.class);
        if (setting == null || setting.getTestMode() == null || setting.getTestMode().equals(1)) {
            dynamicCode = "1111";
        }
        return dynamicCode;
    }

}
