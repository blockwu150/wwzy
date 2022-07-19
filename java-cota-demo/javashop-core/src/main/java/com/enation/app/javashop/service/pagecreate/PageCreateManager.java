package com.enation.app.javashop.service.pagecreate;
/**
 * 
 * 静态页生成接口
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月1日 上午11:47:00
 */
public interface PageCreateManager {
	/**
	 * 开始生成静态页
	 * @param choosePages
	 * @return
	 */
	boolean startCreate(String[] choosePages) ;
}
