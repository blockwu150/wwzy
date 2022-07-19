package com.enation.app.javashop.model.member.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 支付宝信任登录参数项枚举类
 * @ClassName AlipayConnectConfigItemEnum
 * @since v7.0 下午6:13 2018/6/28
 */
public enum AlipayConnectConfigItemEnum {
    /**
     * appId
     */
    app_id("app_id"),
    /**
     * 商户私钥
     */
    private_key("商户私钥"),
    /**
     * 支付宝公钥
     */
    public_key("支付宝公钥");

    private String text;

    AlipayConnectConfigItemEnum(String text) {
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
