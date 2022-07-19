package com.enation.app.javashop.model.pagecreate;

/**
 * 场景枚举
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-05-02 下午3:40
 */
public enum PageCreateEnum {

    //PAGE CREATE ENUM
    INDEX("首页"), GOODS("商品页"), HELP("帮助页面") , SHOP("店铺首页");

    String scene;

    PageCreateEnum(String scene) {
        this.scene = scene;
    }

    public String getScene() {
        return scene;
    }

}
