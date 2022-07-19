package com.enation.app.javashop.client.system;

import com.enation.app.javashop.model.system.dos.Message;

/**
 * @author fk
 * @version v2.0
 * @Description: 站内消息
 * @date 2018/8/14 10:14
 * @since v7.0.0
 */
public interface MessageClient {

    /**
     * 通过id查询站内消息
     *
     * @param id 消息id
     * @return 站内消息对象
     */
    Message get(Long id);

}
