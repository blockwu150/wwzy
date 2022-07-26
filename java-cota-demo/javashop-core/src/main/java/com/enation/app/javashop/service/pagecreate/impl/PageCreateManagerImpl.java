package com.enation.app.javashop.service.pagecreate.impl;

import com.enation.app.javashop.model.base.rabbitmq.AmqpExchange;
import com.enation.app.javashop.service.pagecreate.PageCreateManager;
import com.enation.app.javashop.model.system.vo.ProgressEnum;
import com.enation.app.javashop.model.system.vo.TaskProgress;
import com.enation.app.javashop.model.system.vo.TaskProgressConstant;
import com.enation.app.javashop.model.util.progress.ProgressManager;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.rabbitmq.MessageSender;
import com.enation.app.javashop.framework.rabbitmq.MqMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 静态页生成实现
 *
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月1日 上午11:51:09
 */
@Component
public class PageCreateManagerImpl implements PageCreateManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ProgressManager progressManager;

    @Override
    public boolean startCreate(String[] choosePages) {

        TaskProgress taskProgress =  progressManager.getProgress(TaskProgressConstant.PAGE_CREATE);
        if ( taskProgress!= null) {
            //如果任务已经完成，返回可以执行
            return taskProgress.getTaskStatus().equals(ProgressEnum.SUCCESS.getStatus());
        }
        this.sendPageCreateMessage(choosePages);
        return true;
    }


    /**
     * 发送页面生成消息
     *
     * @param choosePages 要发送的对象 要生成的页面
     */
    public void sendPageCreateMessage(String[] choosePages) {
        try {
            this.messageSender.send(new MqMessage(AmqpExchange.PAGE_CREATE, AmqpExchange.PAGE_CREATE+"_ROUTING", choosePages));
        } catch (Exception e) {
            logger.error("发送页面生成消息异常",e);
        }
    }


}
