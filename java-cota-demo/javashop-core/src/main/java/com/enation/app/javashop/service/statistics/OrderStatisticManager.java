package com.enation.app.javashop.service.statistics;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.base.SearchCriteria;
import com.enation.app.javashop.model.statistics.vo.MapChartData;
import com.enation.app.javashop.model.statistics.vo.MultipleChart;
import com.enation.app.javashop.model.statistics.vo.SalesTotal;
import com.enation.app.javashop.model.statistics.vo.SimpleChart;

/**
 * 订单相关统计
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/4/16 下午1:53
 */

public interface OrderStatisticManager {

    /**
     * 获取订单下单金额
     *
     * @param searchCriteria 查询条件
     * @param orderStatus 订单状态
     * @return 图表数据
     */
    MultipleChart getOrderMoney(SearchCriteria searchCriteria, String orderStatus);

    /**
     * 获取订单下单金额
     *
     * @param searchCriteria 搜索参数
     * @param orderStatus    订单状态
     * @param pageNo         页码
     * @param pageSize       分页大小
     * @return 分页数据
     */
    WebPage getOrderPage(SearchCriteria searchCriteria, String orderStatus, Long pageNo, Long pageSize);

    /**
     * 获取订单下单量
     *
     * @param searchCriteria 搜索参数
     * @param orderStatus    订单状态
     * @return 图表数据
     */
    MultipleChart getOrderNum(SearchCriteria searchCriteria, String orderStatus);


    /**
     * 获取销售收入统计
     *
     * @param searchCriteria 查询条件
     * @param pageNo         页码
     * @param pageSize       分页大小
     * @return 分页数据
     */
    WebPage getSalesMoney(SearchCriteria searchCriteria, Long pageNo, Long pageSize);

    /**
     * 获取销售收入退款 统计
     *
     * @param searchCriteria  查询条件
     * @param pageNo         页码
     * @param pageSize       分页大小
     * @return 分页数据
     */
    WebPage getAfterSalesMoney(SearchCriteria searchCriteria, Long pageNo, Long pageSize);

    /**
     * 销售收入总览
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SalesTotal getSalesMoneyTotal(SearchCriteria searchCriteria);

    /**
     * 按区域分析下单会员量
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    MapChartData getOrderRegionMember(SearchCriteria searchCriteria);

    /**
     * 按区域分析下单数
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    MapChartData getOrderRegionNum(SearchCriteria searchCriteria);

    /**
     * 按区域分析下单金额
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    MapChartData getOrderRegionMoney(SearchCriteria searchCriteria);

    /**
     * 获取区域分析表格
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    WebPage getOrderRegionForm(SearchCriteria searchCriteria);

    /**
     * 客单价分布
     *
     * @param searchCriteria 查询条件
     * @param prices 价格区间
     * @return 图表数据
     */
    SimpleChart getUnitPrice(SearchCriteria searchCriteria, Integer[] prices);

    /**
     * 购买频次分析
     *
     * @return 分页数据
     */
    WebPage getUnitNum();

    /**
     * 购买时段分析
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getUnitTime(SearchCriteria searchCriteria);

    /**
     * 退款统计
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getReturnMoney(SearchCriteria searchCriteria);


}
