package com.enation.app.javashop.model.member.enums;

/**
 * @author fk
 * @version v1.0
 * @Description: 评论评分枚举
 * @date 2018/5/3 11:12
 * @since v7.0.0
 */
public enum CommentGrade {

    /**
     * 好评
     */
    good("好评"),
    /**
     * 中评
     */
    neutral("中评"),
    /**
     * 差评
     */
    bad("差评");

    private String description;

    CommentGrade(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
    public String value(){
        return this.name();
    }
}
