package com.enation.app.javashop.client.distribution.impl;

import com.enation.app.javashop.client.distribution.CommissionTplClient;
import com.enation.app.javashop.model.distribution.dos.CommissionTpl;
import com.enation.app.javashop.service.distribution.CommissionTplManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * CommissionTplClientDefaultImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午2:19
 */

@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class CommissionTplClientDefaultImpl  implements CommissionTplClient{
    @Autowired
    private CommissionTplManager commissionTplManager;
    /**
     * 获取默认模版
     *
     * @return
     */
    @Override
    public CommissionTpl getDefaultCommission() {
        return commissionTplManager.getDefaultCommission();
    }
}
