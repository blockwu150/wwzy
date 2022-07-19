package com.enation.app.javashop.model.member.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 微博信任登录参数组枚举类
 * @ClassName WeiboConnectConfigGroupEnum
 * @since v7.0 下午8:05 2018/6/28
 */
public enum WeiboConnectConfigGroupEnum {
    /**
     * 网页端参数 （PC，WAP，微信网页端）
     */
    pc("网页端参数 （PC，H5）");


    private String text;

    WeiboConnectConfigGroupEnum(String text) {
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
