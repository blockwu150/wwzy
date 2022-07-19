package com.enation.app.javashop.model.pagedata.enums;

/**
 * @author fk
 * @version v1.0
 * @Description: 文章分类类型
 * @date 2018/5/21 16:05
 * @since v7.0.0
 */
public enum ArticleCategoryType {

    /**
     * 帮助中心
     */
    HELP("帮助中心"),
    /**
     * 商城公告
     */
    NOTICE("商城公告"),
    /**
     * 固定位置
     */
    POSITION("固定位置"),
    /**
     * 商城促销
     */
    PROMOTION("商城促销"),
    /**
     * 其他
     */
    OTHER("其他");

    private String description;

    ArticleCategoryType(String description) {
        this.description = description;

    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }


}
