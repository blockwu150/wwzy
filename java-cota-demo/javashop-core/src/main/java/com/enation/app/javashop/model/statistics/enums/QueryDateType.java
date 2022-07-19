package com.enation.app.javashop.model.statistics.enums;

/**
 * 枚举：搜索日期类型
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/3/28 下午1:40
 */
public enum QueryDateType {

    // 月份
    MONTH("月份"),
    // 年份
    YEAR("年份");

    private String query;

    QueryDateType(String query){
        this.query=query;
    }

    public String description(){
        return this.query;
    }

    public String value(){
        return this.name();
    }

}