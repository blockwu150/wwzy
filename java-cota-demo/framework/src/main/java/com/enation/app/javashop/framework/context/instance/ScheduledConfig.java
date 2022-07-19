package com.enation.app.javashop.framework.context.instance;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

/**
 * 定时任务线程池配置
 * @author liuyulei
 * @version 1.0
 * @since 7.2.2
 * 2020/11/18 0018  14:30
 */
@Service
public class ScheduledConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));

    }
}
