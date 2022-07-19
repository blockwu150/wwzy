package com.enation.app.javashop.model.member.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 微博信任登录参数项枚举类
 * @ClassName WeiboConnectConfigItemEnum
 * @since v7.0 下午6:12 2018/6/28
 */
public enum WeiboConnectConfigItemEnum {
    /**
     *  app_key
     */
    app_key("App Key"),
    /**
     * app_secret
     */
    app_secret("App Secret");

    private String text;

    WeiboConnectConfigItemEnum(String text) {
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
