package com.enation.app.javashop.service.base.plugin.sms;

import java.util.List;
import java.util.Map;

import com.enation.app.javashop.model.base.vo.ConfigItem;

/**
 * 短信发送
 *
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2018年3月23日 下午3:07:05
 */
public interface SmsPlatform {
    /**
     * 配置各个存储方案的参数
     *
     * @return 参数列表
     */
    List<ConfigItem> definitionConfigItem();

    /**
     * 发送短信事件
     *
     * @param phone   手机号
     * @param content 发送内容
     * @param param   其它参数
     * @return
     */
    boolean onSend(String phone, String content, Map param);


    /**
     * 获取插件ID
     *
     * @return 插件beanId
     */
    String getPluginId();

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    String getPluginName();

    /**
     * 短信网关是否开启
     *
     * @return 0 不开启  1 开启
     */
    Integer getIsOpen();
}
