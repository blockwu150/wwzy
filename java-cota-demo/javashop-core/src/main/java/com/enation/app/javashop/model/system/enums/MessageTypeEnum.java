package com.enation.app.javashop.model.system.enums;

/**
 * @author zjp
 * @version v7.0
 * @Description 消息类型枚举类
 * @ClassName MessageTypeEnum
 * @since v7.0 下午4:49 2018/7/5
 */
public enum MessageTypeEnum {
    //店铺
    SHOP("店铺"),
    //会员
    MEMBER("会员"),
    //其他
    OTHER("其他");

    private String description;

    MessageTypeEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
