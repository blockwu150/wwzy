package com.enation.app.javashop.client.system;

import java.util.List;

/**
 * @author fk
 * @version v2.0
 * @Description: 帮助中心
 * @date 2018/8/14 10:38
 * @since v7.0.0
 */
public interface StaticsPageHelpClient {

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
    List helpList(Long page, Long pageSize);

}
