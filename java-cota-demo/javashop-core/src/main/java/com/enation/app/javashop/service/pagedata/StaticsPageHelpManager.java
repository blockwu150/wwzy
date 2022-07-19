package com.enation.app.javashop.service.pagedata;

import java.util.List;

/**
 * 静态页面帮助页面
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/7/17 下午3:27
 * @Description:
 *
 */
public interface StaticsPageHelpManager {

    /**
     * 获取帮助页面总数
     * @return
     */
    Integer count();


    /**
     * 分页获取帮助
     * @param page
     * @param pageSize
     * @return
     */
    List helpList(Long page,Long pageSize);


}
