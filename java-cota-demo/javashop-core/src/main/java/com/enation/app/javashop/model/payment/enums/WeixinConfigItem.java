package com.enation.app.javashop.model.payment.enums;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信客户端使用配置参数
 * @date 2018/4/11 17:05
 * @since v7.0.0
 */
public enum WeixinConfigItem {

	/**
	 * 商户号MCHID
	 */
	mchid("商户号MCHID"),
	/**
	 * APPID
	 */
	appid("APPID"),
	/**
	 * API密钥(key)
	 */
	key("API密钥(key)"),
	/**
	 * 应用密钥(AppSecret)
	 */
	app_secret("应用密钥(AppSecret)"),
	/**
	 * 证书路径
	 */
	p12_path("证书路径");

	private String text;

	WeixinConfigItem(String text) {
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
