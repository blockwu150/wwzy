package com.enation.app.javashop.model.payment.enums;

/**
 * @author fk
 * @version v2.0
 * @Description: 银联在线客户端使用配置参数
 * @date 2018/4/11 17:05
 * @since v7.0.0
 */
public enum ChinapayConfigItem {

	/**
	 * 商户代码
	 */
	mer_id("商户代码"),
	/**
	 * 配置文件security.properties存放位置
	 */
	merchant_private_key("配置文件security.properties存放位置");

	private String text;

	ChinapayConfigItem(String text) {
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
