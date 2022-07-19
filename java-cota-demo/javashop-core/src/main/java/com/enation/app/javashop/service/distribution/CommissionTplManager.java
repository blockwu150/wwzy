package com.enation.app.javashop.service.distribution;

import com.enation.app.javashop.model.distribution.dos.CommissionTpl;
import com.enation.app.javashop.framework.database.WebPage;

/**
 * 提成模板manager接口
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 上午10:52
 */

public interface CommissionTplManager {


    /**
     * page
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @return page
     */
    WebPage page(long page, long pageSize);

    /**
     * 通过id获得CommissionTpl
     *
     * @param id 提成模版id
     * @return CommissionTpl
     */
    CommissionTpl getModel(long id);


    /**
     * 添加一个commissionTpl
     *
     * @param commissionTpl 模版
     * @return CommissionTplDO
     */
    CommissionTpl add(CommissionTpl commissionTpl);


    /**
     * 修改一个CommissionTpl
     *
     * @param commissionTpl 提成模版
     * @return CommissionTplDO
     */
    CommissionTpl edit(CommissionTpl commissionTpl);

    /**
     * 删除一个CommissionTpl
     * @param tplId 提成模版id
     */
    void delete(Long tplId);

    /**
     * 得到一个默认的模版
     *
     * @return DO
     */
    CommissionTpl getDefaultCommission();
}
