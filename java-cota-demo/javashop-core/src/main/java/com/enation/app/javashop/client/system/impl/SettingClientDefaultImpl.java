package com.enation.app.javashop.client.system.impl;

import com.enation.app.javashop.model.base.SettingGroup;
import com.enation.app.javashop.service.base.service.SettingManager;
import com.enation.app.javashop.client.system.SettingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author fk
 * @version v1.0
 * @Description: 系统设置
 * @date 2018/7/30 10:49
 * @since v7.0.0
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class SettingClientDefaultImpl implements SettingClient {

    @Autowired
    private SettingManager settingManager;

    @Override
    public void save(SettingGroup group, Object settings) {

        this.settingManager.save(group,settings);

    }

    @Override
    public String get(SettingGroup group) {
        return this.settingManager.get(group);
    }
}
