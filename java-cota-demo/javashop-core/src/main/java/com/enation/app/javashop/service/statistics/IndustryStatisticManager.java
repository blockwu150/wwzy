package com.enation.app.javashop.service.statistics;


import com.enation.app.javashop.model.base.SearchCriteria;
import com.enation.app.javashop.model.statistics.vo.SimpleChart;
import com.enation.app.javashop.framework.database.WebPage;

/**
 * 后台 行业分析
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/4/16 下午1:53
 */
public interface IndustryStatisticManager {


    /**
     * 按分类统计下单量
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getOrderQuantity(SearchCriteria searchCriteria);

    /**
     * 按分类统计下单商品数量
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getGoodsNum(SearchCriteria searchCriteria);

    /**
     * 按分类统计下单金额
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getOrderMoney(SearchCriteria searchCriteria);

    /**
     * 概括总览
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    WebPage getGeneralOverview(SearchCriteria searchCriteria);

}
