package com.enation.app.javashop.framework.rabbitmq;

/**
 * 发送的消息对象
 *
 * @author fk
 * @version v7.2.0
 * @since v7.2.0
 * 2020-06-15 21:50:52
 */
public class MqMessage {

    private String exchange;

    private String routingKey;

    private Object message;


    public MqMessage(String exchange, String routingKey, Object message) {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.message = message;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
