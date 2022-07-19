package com.enation.app.javashop.model.member.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 微信信任登录参数组枚举类
 * @ClassName 微信ConnectConfigGroupEnum
 * @since v7.0 下午8:05 2018/6/28
 */
public enum WechatConnectConfigGroupEnum {
    /**
     * PC网页端参数
     */
    pc("PC网页端参数"),
    /**
     * 微信网页端参数
     */
    wechat("H5参数"),
    /**
     * 小程序参数
     */
    miniprogram("小程序参数");


    private String text;

    WechatConnectConfigGroupEnum(String text) {
        this.text = text;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String value() {
        return this.name();
    }
}
