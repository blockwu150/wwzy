package com.enation.app.javashop.client.system;

import com.enation.app.javashop.model.base.SettingGroup;

/**
 * @author fk
 * @version v1.0
 * @Description: 系统设置
 * @date 2018/7/30 10:48
 * @since v7.0.0
 */
public interface SettingClient {

    /**
     * 系统参数配置
     *
     * @param group    系统设置的分组
     * @param settings 要保存的设置对象
     */
    void save(SettingGroup group, Object settings);

    /**
     * 获取配置
     *
     * @param group 分组名称
     * @return 存储对象
     */
    String get(SettingGroup group);

}
