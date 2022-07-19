package com.enation.app.javashop.service.system;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.system.dos.SmsPlatformDO;
import com.enation.app.javashop.model.system.vo.SmsPlatformVO;

/**
 * 短信网关表业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-23 11:31:05
 */
public interface SmsPlatformManager {

    /**
     * 查询短信网关表列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return WebPage
     */
    WebPage list(long page, long pageSize);

    /**
     * 添加短信网关表
     *
     * @param smsPlatform 短信网关vo
     * @return
     */
    SmsPlatformVO add(SmsPlatformVO smsPlatform);

    /**
     * 添加短信网关表
     *
     * @param smsPlatform 短信网关vo
     * @return
     */
    SmsPlatformVO edit(SmsPlatformVO smsPlatform);


    /**
     * 获取短信网关表
     *
     * @param id 短信网关表主键
     * @return Platform  短信网关表
     */
    SmsPlatformDO getModel(Long id);


    /**
     * 根据beanid获取短信网关
     *
     * @param bean beanid
     * @return
     */
    SmsPlatformDO getSmsPlateform(String bean);

    /**
     * 启用网关
     *
     * @param bean 短信网关beanid
     */
    void openPlatform(String bean);

    /**
     * 根据短信网关的beanid 获取短信网关的配置项
     *
     * @param bean 短信网关 beanid
     * @return 短信网关VO
     */
    SmsPlatformVO getConfig(String bean);

    /**
     * 获取开启的短信网关
     *
     * @return 短信网关VO
     */
    SmsPlatformVO getOpen();

}
