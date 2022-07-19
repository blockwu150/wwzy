package com.enation.app.javashop.service.trade.order;

import com.enation.app.javashop.model.member.vo.SalesVO;
import com.enation.app.javashop.framework.database.WebPage;

/**
 * 会员销售记录
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018/6/29 上午9:31
 * @Description:
 *
 */
public interface MemberSalesManager {


    /**
     * 商品销售记录
     * @param pageSize 每天显示数量
     * @param pageNo 当前页码
     * @param goodsId 商品id
     * @return 销售记录分页数据
     */
    WebPage<SalesVO> list(Long pageSize, Long pageNo, Long goodsId);


}
