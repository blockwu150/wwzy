package com.enation.app.javashop.service.payment.plugin.ecloud;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

public class MD5 {
	/**
	 *签名字符串
	 * @param text 需要签名的字符串
	 * @param key 密钥
	 * @param input_charset 编码格式
	 * @return 签名结果
	 */
	public static String sign(String text,String key,String input_charset){
		text = text+key;
		return DigestUtils.md5Hex(getContentBytes(text,input_charset));
	}
	

	/**
	 * 把字符串转换成byte数组
	 * @param content 需要转换的字符串
	 * @param charset 编码格式
	 * @return 返回目标字符串转换成的字节数组
	 */
	private static byte[] getContentBytes(String content,String charset){
		if(null==charset || "".equals(charset)){
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			 throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}    
   
	/**
	 * 验证签名
	 * @param text 需要签名的字符串
	 * @param sign  比较标准
	 * @param key 密钥
	 * @param input_charset 编码格式
	 * @return 返回验证结果
	 */
	public static boolean verify(String text,String sign,String key,String input_charset){
		text = text + key;
		String mySign = DigestUtils.md5Hex(getContentBytes(text,input_charset));
		if(sign.equals(mySign))
			return true;
		return false;
	}
}
