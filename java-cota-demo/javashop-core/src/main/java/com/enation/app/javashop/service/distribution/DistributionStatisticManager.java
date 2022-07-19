package com.enation.app.javashop.service.distribution;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.statistics.vo.SimpleChart;


/**
 * 分销商统计
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/6/13 上午8:36
 */
public interface DistributionStatisticManager {


    /**
     * 营业额统计
     *
     * @param circle 搜索类型：YEAR/MONTH
     * @param memberId 会员id
     * @param year 年份
     * @param month 月份
     * @return 图表数据
     */
    SimpleChart getOrderMoney(String circle, Long memberId, Integer year, Integer month);

    /**
     * 利润
     *
     * @param circle 搜索类型：YEAR/MONTH
     * @param memberId 会员id
     * @param year 年份
     * @param month 月份
     * @return 图表数据
     */
    SimpleChart getPushMoney(String circle, Long memberId, Integer year, Integer month);

    /**
     * 订单数
     *
     * @param circle 搜索类型：YEAR/MONTH
     * @param memberId 会员id
     * @param year 年份
     * @param month 月份
     * @return 图表数据
     */
    SimpleChart getOrderCount(String circle, Long memberId, Integer year, Integer month);


    /**
     * 店铺统计
     * @param circle 搜索类型：YEAR/MONTH
     * @param year 年份
     * @param month 月份
     * @param pageSize 页码
     * @param pageNo 每页显示条数
     * @return 分页数据
     */
    WebPage getShopPush(String circle, Integer year, Integer month, Long pageSize, Long pageNo);

}
