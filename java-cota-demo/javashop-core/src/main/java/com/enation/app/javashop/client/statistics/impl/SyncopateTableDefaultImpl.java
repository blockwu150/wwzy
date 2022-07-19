package com.enation.app.javashop.client.statistics.impl;

import com.enation.app.javashop.client.statistics.SyncopateTableClient;
import com.enation.app.javashop.service.statistics.SyncopateTableManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 统计数据填充
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-22 上午8:41
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class SyncopateTableDefaultImpl implements SyncopateTableClient {
    @Autowired
    private SyncopateTableManager syncopateTableManager;

    /**
     * 每日填充数据
     */
    @Override
    public void everyDay() {
        syncopateTableManager.everyDay();
    }

}
