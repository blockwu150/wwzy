package com.enation.app.javashop.service.base.service;

import com.enation.app.javashop.model.base.SceneType;
import com.enation.app.javashop.model.base.vo.SmsSendVO;

/**
 * 手机短信接口
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2018年3月19日 下午4:02:40
 */
public interface SmsManager {
    /**
     * 发送手机短信
     *
     * @param smsSendVO
     */
    void send(SmsSendVO smsSendVO);

    /**
     * 验证手机验证码
     *
     * @param scene  业务场景
     * @param mobile 手机号码
     * @param code   手机验证码
     * @return 是否通过校验 true通过，false不通过
     */
    boolean valid(String scene, String mobile, String code);

    /**
     * 获取验证通过的手机号
     * @param scene
     * @param mobile
     * @return
     */
    String validMobile(String scene,String mobile);

    /**
     * 在缓存中记录验证码
     *
     * @param scene  业务场景
     * @param mobile 手机号码
     * @param code   手机验证码
     */
    void record(String scene, String mobile, String code);

    /**
     * 发送(发送手机短信)消息
     *
     * @param byName    操作，替换内容
     * @param mobile    手机号码
     * @param sceneType 操作类型
     */
    void sendSmsMessage(String byName, String mobile, SceneType sceneType);

}
