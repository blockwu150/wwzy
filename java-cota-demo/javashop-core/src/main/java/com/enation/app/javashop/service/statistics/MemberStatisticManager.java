package com.enation.app.javashop.service.statistics;


import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.base.SearchCriteria;
import com.enation.app.javashop.model.statistics.vo.SimpleChart;

/**
 * 会员相关统计
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/4/16 下午1:54
 */

public interface MemberStatisticManager {

    /**
     * 获取新增会员数量
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getIncreaseMember(SearchCriteria searchCriteria);

    /**
     * 获取新增会员数量 表格
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    WebPage getIncreaseMemberPage(SearchCriteria searchCriteria);

    /**
     * 获取会员下单量
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getMemberOrderQuantity(SearchCriteria searchCriteria);

    /**
     * 获取会员下单量 表格
     *
     * @param searchCriteria 查询条件
     * @return 分页数据
     */
    WebPage getMemberOrderQuantityPage(SearchCriteria searchCriteria);

    /**
     * 获取下单商品数量 表格
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getMemberGoodsNum(SearchCriteria searchCriteria);

    /**
     * 获取下单商品数量 表格
     *
     * @param searchCriteria 查询条件
     * @return 分页数据
     */
    WebPage getMemberGoodsNumPage(SearchCriteria searchCriteria);

    /**
     * 获取下单总金额
     *
     * @param searchCriteria 查询条件
     * @return 图表数据
     */
    SimpleChart getMemberMoney(SearchCriteria searchCriteria);

    /**
     * 获取下单总金额 表格
     *
     * @param searchCriteria 查询条件
     * @return 分页数据
     */
    WebPage getMemberMoneyPage(SearchCriteria searchCriteria);


}
