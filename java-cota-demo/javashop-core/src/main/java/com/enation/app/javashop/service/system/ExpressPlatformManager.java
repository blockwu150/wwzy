package com.enation.app.javashop.service.system;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.system.dos.ExpressPlatformDO;
import com.enation.app.javashop.model.system.vo.ExpressDetailVO;
import com.enation.app.javashop.model.system.vo.ExpressPlatformVO;

/**
 * 快递平台业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-11 14:42:50
 */
public interface ExpressPlatformManager {

    /**
     * 查询快递平台列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return WebPage
     */
    WebPage list(long page, long pageSize);

    /**
     * 添加快递平台
     *
     * @param expressPlatformVO 快递平台
     * @return expressPlatformVO 快递平台
     */
    ExpressPlatformVO add(ExpressPlatformVO expressPlatformVO);

    /**
     * 修改快递平台
     *
     * @param expressPlatformVO 快递平台
     * @return ExpressPlatformDO 快递平台
     */
    ExpressPlatformVO edit(ExpressPlatformVO expressPlatformVO);

    /**
     * 根据beanid获取快递平台
     *
     * @param bean beanid
     * @return
     */
    ExpressPlatformDO getExpressPlatform(String bean);

    /**
     * 根据快递平台的beanid 获取快递平台的配置项
     *
     * @param bean 快递平台beanid
     * @return 快递平台
     */
    ExpressPlatformVO getExoressConfig(String bean);

    /**
     * 开启某个快递平台
     *
     * @param bean
     */
    void open(String bean);

    /**
     * 查询物流信息
     *
     * @param id 物流公司id
     * @param nu  物流单号
     * @return 物流详细
     */
    ExpressDetailVO getExpressDetail(Long id, String nu);

    /**
     * 获取当前系统开启的快递平台
     *
     * @return 快递平台VO
     */
    ExpressPlatformVO getOpen();
}
