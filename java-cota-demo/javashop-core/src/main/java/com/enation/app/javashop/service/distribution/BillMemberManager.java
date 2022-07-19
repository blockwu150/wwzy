package com.enation.app.javashop.service.distribution;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.distribution.dos.BillMemberDO;
import com.enation.app.javashop.model.distribution.dos.DistributionOrderDO;
import com.enation.app.javashop.model.distribution.dto.DistributionRefundDTO;
import com.enation.app.javashop.model.distribution.vo.BillMemberVO;
import com.enation.app.javashop.model.distribution.vo.DistributionOrderVO;
import com.enation.app.javashop.model.distribution.vo.DistributionSellbackOrderVO;

import java.util.List;

/**
 * 用户结算单
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 上午8:51
 */

public interface BillMemberManager {

    /**
     * 获取某个会员的下线业绩
     *
     * @param memberId 会员id
     * @param billId   账单id
     * @return
     */
    List<BillMemberVO> allDown(Long memberId, Long billId);


    /**
     * 获取结算单
     *
     * @param totalSn 单号
     * @param memberId 会员id
     * @return
     */
    BillMemberDO getBillByTotalSn(String totalSn, Long memberId);

    /**
     * 分页获取会员历史业绩
     *
     * @param memberId 会员id
     * @param pageNo 第几页
     * @param pageSize 每页数量
     * @return
     */
    WebPage<BillMemberVO> getAllByMemberId(Long memberId, Long pageNo, Long pageSize);

    /**
     * 新增一个结算单
     *
     * @param billMember 会员对象
     * @return
     */
    BillMemberDO add(BillMemberDO billMember);

    /**
     * 查询一个总结算单的会员结算单
     *
     * @param page     第几页
     * @param pageSize 每页数量
     * @param id       id
     * @param uname    会员名称
     * @return page
     */
    WebPage<BillMemberVO> page(Long page, Long pageSize, Long id, String uname);


    /**
     * 获取分销商结算单
     *
     * @param billId 总结算单id
     * @return do
     */
    BillMemberDO getBillMember(Long billId);


    /**
     * 购买商品产生的结算
     *
     * @param order 分销订单对象
     */
    void buyShop(DistributionOrderDO order);

    /**
     * 退货商品产生的结算
     *
     * @param order                 分销订单对象
     * @param distributionRefundDTO 分销退款单对象
     */
    void returnShop(DistributionOrderDO order, DistributionRefundDTO distributionRefundDTO);

    /**
     * 获取分销账单
     *
     * @param page     第几页
     * @param pageSize 每页数量
     * @param id       id
     * @param memberId 会员id
     * @return page
     */
    WebPage<DistributionOrderVO> listOrder(Long page, Long pageSize, Long id, Long memberId);


    /**
     * 获取分销退款单
     *
     * @param page     第几页
     * @param pageSize 每页数量
     * @param id       id
     * @param memberId 会员id
     * @return page
     */
    WebPage<DistributionSellbackOrderVO> listSellback(Long page, Long pageSize, Long id, Long memberId);

    /**
     * 获取当前分销商当月结算单
     *
     * @param memberId 会员id
     * @return model
     */
    BillMemberVO getCurrentBillMember(Long memberId, Long billId);

    /**
     * 获取指定sn的分销商结算单
     *
     * @param memberId 会员id
     * @param sn       单号
     * @return do
     */
    BillMemberDO getHistoryBillMember(Long memberId, String sn);


    /**
     * 结算单会员分页查询
     *
     * @param pageNo 第几页
     * @param pageSize 每页数量
     * @param memberId 会员id
     * @return page
     */
    WebPage<BillMemberVO> billMemberPage(Long pageNo, Long pageSize, Long memberId);

}
