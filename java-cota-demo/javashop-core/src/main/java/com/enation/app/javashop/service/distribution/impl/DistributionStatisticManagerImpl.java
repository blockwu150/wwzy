package com.enation.app.javashop.service.distribution.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.distribution.DistributionOrderMapper;
import com.enation.app.javashop.model.base.SearchCriteria;
import com.enation.app.javashop.model.distribution.vo.SellerPushVO;
import com.enation.app.javashop.service.distribution.DistributionStatisticManager;
import com.enation.app.javashop.model.statistics.enums.QueryDateType;
import com.enation.app.javashop.model.statistics.vo.ChartSeries;
import com.enation.app.javashop.model.statistics.vo.SimpleChart;
import com.enation.app.javashop.util.DataDisplayUtil;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 分销商统计
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-13 上午8:37
 */
@Service
public class DistributionStatisticManagerImpl implements DistributionStatisticManager {

    @Autowired
    private DistributionOrderMapper distributionOrderMapper;

    /**
     * 营业额统计
     *
     * @param circle 搜索类型：YEAR/MONTH
     * @param memberId 会员id
     * @param year 年份
     * @param month 月份
     * @return 图表数据
     */
    @Override
    public SimpleChart getOrderMoney(String circle, Long memberId, Integer year, Integer month) {

        //封装查询条件
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMonth(month);
        searchCriteria.setYear(year);
        searchCriteria.setCycleType(circle);

        searchCriteria = new SearchCriteria(searchCriteria);

        //获取开始时间和结束时间
        long[] timesTramp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);

        //获取查询结果的横坐标数值，按年分则是12，按月份则是月份的天数
        Integer resultSize = DataDisplayUtil.getResultSize(searchCriteria);

        //sql转换时间的格式化表达式，%m表示按照月份，%d表示按照天
        String circleWhere = "";
        if (Objects.equals(searchCriteria.getCycleType(), QueryDateType.YEAR.name())) {
            circleWhere = "%m";
        } else {
            circleWhere = "%d";
        }

        List<Map<String, Object>> list = distributionOrderMapper.querySumOrderPrice(circleWhere, timesTramp[0], timesTramp[1], memberId);

        //封装图表数据
        String[] xAxis = new String[resultSize],
                data = new String[resultSize];

        for (int i = 0; i < resultSize; i++) {

            data[i] = 0 + "";
            for (Map<String, Object> map : list) {
                try {
                    if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                        data[i] = map.get("order_price").toString();
                    }
                } catch (NullPointerException e) {
                }
            }
            xAxis[i] = i + 1 + "";
        }

        ChartSeries chartSeries = new ChartSeries("订单金额统计", data, new String[0]);

        SimpleChart simpleChart = new SimpleChart(chartSeries, xAxis, new String[0]);

        return simpleChart;
    }

    /**
     * 利润
     *
     * @param circle 搜索类型：YEAR/MONTH
     * @param memberId 会员id
     * @param year 年份
     * @param month 月份
     * @return 图表数据
     */
    @Override
    public SimpleChart getPushMoney(String circle, Long memberId, Integer year, Integer month) {

        //封装查询条件
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMonth(month);
        searchCriteria.setYear(year);
        searchCriteria.setCycleType(circle);

        searchCriteria = new SearchCriteria(searchCriteria);

        //获取开始时间和结束时间
        long[] timesTramp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);

        Integer resultSize = DataDisplayUtil.getResultSize(searchCriteria);

        //日期格式化类型
        String circleWhere = "";
        if (Objects.equals(searchCriteria.getCycleType(), QueryDateType.YEAR.name())) {
            circleWhere = "%m";
        } else {
            circleWhere = "%d";
        }

        //查询一级分销商营业额数据
        List<Map<String, Object>> list = distributionOrderMapper.querySumGrade1Rebate(circleWhere, timesTramp[0], timesTramp[1], memberId);
        //查询二级分销商营业额数据
        List<Map<String, Object>> list2 = distributionOrderMapper.querySumGrade2Rebate(circleWhere, timesTramp[0], timesTramp[1], memberId);

        List<Map<String, Object>> result = new ArrayList<>();

        //封装统计所需数据格式
        for (int i = 0; i < resultSize; i++) {
            double finalRebate = 0;
            for (Map<String, Object> map : list) {
                try {
                    if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                        finalRebate = CurrencyUtil.add(finalRebate, Double.parseDouble(map.get("grade_rebate").toString()));
                    }
                } catch (NullPointerException e) {
                }
            }
            for (Map<String, Object> map : list2) {
                try {
                    if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                        finalRebate = CurrencyUtil.add(finalRebate, Double.parseDouble(map.get("grade_rebate").toString()));
                    }
                } catch (NullPointerException e) {
                }
            }
            Map<String, Object> map = new HashMap<>(16);
            map.put("date", i + 1);
            map.put("grade_rebate", finalRebate);
            result.add(map);
        }

        //封装图表数据
        String[] xAxis = new String[resultSize],
                data = new String[resultSize];

        for (int i = 0; i < resultSize; i++) {

            data[i] = 0 + "";
            for (Map<String, Object> map : result) {
                if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                    data[i] = map.get("grade_rebate").toString();
                }
            }
            xAxis[i] = i + 1 + "";
        }

        ChartSeries chartSeries = new ChartSeries("订单提成统计", data, new String[0]);

        SimpleChart simpleChart = new SimpleChart(chartSeries, xAxis, new String[0]);
        return simpleChart;
    }

    /**
     * 订单数
     *
     * @param circle 搜索类型：YEAR/MONTH
     * @param memberId 会员id
     * @param year 年份
     * @param month 月份
     * @return 图表数据
     */
    @Override
    public SimpleChart getOrderCount(String circle, Long memberId, Integer year, Integer month) {
        //封装查询条件
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMonth(month);
        searchCriteria.setYear(year);
        searchCriteria.setCycleType(circle);

        searchCriteria = new SearchCriteria(searchCriteria);

        //获取开始时间和结束时间
        long[] timesTramp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);

        Integer resultSize = DataDisplayUtil.getResultSize(searchCriteria);

        //日期格式化类型
        String circleWhere = "";
        if (Objects.equals(searchCriteria.getCycleType(), QueryDateType.YEAR.name())) {
            circleWhere = "%m";
        } else {
            circleWhere = "%d";
        }

        List<Map<String, Object>> list = distributionOrderMapper.queryCount(circleWhere, timesTramp[0], timesTramp[1], memberId);

        //封装统计图表数据
        String[] xAxis = new String[resultSize],
                data = new String[resultSize];

        for (int i = 0; i < resultSize; i++) {

            data[i] = 0 + "";
            for (Map<String, Object> map : list) {
                try {
                    if (Integer.parseInt(map.get("date").toString()) == (i + 1)) {
                        data[i] = map.get("count").toString();
                    }
                } catch (NullPointerException e) {
                }
            }
            xAxis[i] = i + 1 + "";
        }

        ChartSeries chartSeries = new ChartSeries("订单数量统计", data, new String[0]);

        SimpleChart simpleChart = new SimpleChart(chartSeries, xAxis, new String[0]);

        return simpleChart;
    }

    /**
     * 店铺统计
     * @param circle 搜索类型：YEAR/MONTH
     * @param year 年份
     * @param month 月份
     * @param pageSize 页码
     * @param pageNo 每页显示条数
     * @return 分页数据
     */
    @Override
    public WebPage getShopPush(String circle, Integer year, Integer month, Long pageSize, Long pageNo) {
        //封装查询条件
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMonth(month);
        searchCriteria.setYear(year);
        searchCriteria.setCycleType(circle);

        searchCriteria = new SearchCriteria(searchCriteria);
        //获取开始时间和结束时间
        long[] timesTramp = DataDisplayUtil.getStartTimeAndEndTime(searchCriteria);

        IPage<SellerPushVO> iPage = distributionOrderMapper.queryGradeRebateForPage(new Page<>(pageNo, pageSize),timesTramp[0], timesTramp[1]);
        return PageConvert.convert(iPage);
    }
}
