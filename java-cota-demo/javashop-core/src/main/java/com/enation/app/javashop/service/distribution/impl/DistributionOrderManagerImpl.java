package com.enation.app.javashop.service.distribution.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.util.*;
import com.enation.app.javashop.mapper.distribution.DistributionMapper;
import com.enation.app.javashop.mapper.distribution.DistributionOrderMapper;
import com.enation.app.javashop.model.base.SettingGroup;
import com.enation.app.javashop.client.system.SettingClient;
import com.enation.app.javashop.client.trade.OrderClient;
import com.enation.app.javashop.model.errorcode.DistributionErrorCode;
import com.enation.app.javashop.service.distribution.exception.DistributionException;
import com.enation.app.javashop.model.distribution.dos.*;
import com.enation.app.javashop.model.distribution.dto.DistributionRefundDTO;
import com.enation.app.javashop.model.distribution.vo.DistributionOrderVO;
import com.enation.app.javashop.model.distribution.vo.DistributionSellbackOrderVO;
import com.enation.app.javashop.service.distribution.*;
import com.enation.app.javashop.model.trade.order.dos.OrderDO;
import com.enation.app.javashop.model.trade.order.dos.OrderItemsDO;
import com.enation.app.javashop.model.trade.order.enums.OrderTypeEnum;
import com.enation.app.javashop.model.trade.order.enums.ShipStatusEnum;
import com.enation.app.javashop.model.trade.order.vo.OrderDetailVO;
import com.google.gson.Gson;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分销订单Manager 实现
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 下午12:05
 */

@Component
public class DistributionOrderManagerImpl implements DistributionOrderManager {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommissionTplManager commissionTplManager;
    @Autowired
    private DistributionManager distributionManager;
    @Autowired
    private DistributionSellerBillManager distributionSellerBillManager;
    @Autowired
    private DistributionGoodsManager distributionGoodsManager;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private SettingClient settingClient;
    @Autowired
    private DistributionOrderManager distributionOrderManager;
    @Autowired
    private BillMemberManager billMemberManager;
    @Autowired
    private DistributionOrderMapper distributionOrderMapper;
    @Autowired
    private DistributionMapper distributionMapper;

    /**
     * 根据sn获得分销商订单详情
     *
     * @param orderSn 订单编号
     * @return FxOrderDO
     */
    @Override
    public DistributionOrderDO getModelByOrderSn(String orderSn) {
        // 如果订单orderSn有效,则作为order_sn的查询条件
        if (!StringUtil.isEmpty(orderSn)) {
            QueryWrapper<DistributionOrderDO> wrapper = new QueryWrapper<>();
            wrapper.eq("order_sn", orderSn);
            DistributionOrderDO distributionOrderDO = distributionOrderMapper.selectOne(wrapper);
            return distributionOrderDO;
        }
        return null;
    }

    /**
     * 根据id获得分销商订单详情
     *
     * @param orderId 订单id
     * @return FxOrderDO
     */
    @Override
    public DistributionOrderDO getModel(Long orderId) {
        // 如果订单orderId有效，则作为order_id的查询条件
        if (orderId != null) {
            QueryWrapper<DistributionOrderDO> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id", orderId);
            DistributionOrderDO distributionOrderDO = distributionOrderMapper.selectOne(wrapper);
            return distributionOrderDO;
        }
        return null;
    }

    /**
     * 保存一条数据
     * @param distributionOrderDO 分销订单
     * @return
     */
    @Override
    public DistributionOrderDO add(DistributionOrderDO distributionOrderDO) {
        distributionOrderDO.setGrade1SellbackPrice(0D);
        distributionOrderDO.setGrade2SellbackPrice(0D);

        //添加一个分销订单
        distributionOrderMapper.insert(distributionOrderDO);

        return distributionOrderDO;

    }

    /**
     * 通过订单id，计算出各个级别的返利金额并保存到数据库
     *
     * @param orderSn 订单编号
     * @return 计算结果 true 成功， false 失败
     */
    @Override
    public boolean calCommission(String orderSn) {

        //如果orderSn为空，则返回false
        if(orderSn == null){
            return false;
        }

        DistributionOrderDO model = this.getModelByOrderSn(orderSn);
        //获取订单价格
        double price = model.getOrderPrice();

        // 我的一级分销商
        Long lv1MemberId = model.getMemberIdLv1();
        // 我的二级分销商
        Long lv2MemberId = model.getMemberIdLv2();
        // 计算一级分销和二级分销的返利金额
        calRebate(model, lv1MemberId, lv2MemberId);

        // 2.保存到分销商冻结金额
        if (lv1MemberId != null && lv1MemberId != 0) {
            this.distributionManager.addFrozenCommission(model.getGrade1Rebate(), lv1MemberId);
            this.distributionManager.addTotalPrice(price, model.getGrade1Rebate(), lv1MemberId);
        }

        if (lv2MemberId != null && lv2MemberId != 0) {
            this.distributionManager.addFrozenCommission(model.getGrade2Rebate(), lv2MemberId);
            this.distributionManager.addTotalPrice(price, model.getGrade2Rebate(), lv2MemberId);
        }
        // 3.保存订单
        distributionOrderMapper.updateById(model);
        return true;

    }

    /**
     * 计算退款时需要退的返利金额
     *
     * @param orderSn 订单sn
     * @param price    退款金额
     * @return 计算结果 true 成功， false 失败
     */
    @Override
    public boolean calReturnCommission(String orderSn, double price) {

        // 退款可为0 如为0 则需要退的返利金额也为0
        if (price == 0) {
            return true;
        }
        // 如果订单id有效
        if (orderSn == null) {
            return false;
        }
        OrderDetailVO orderDetailVO = orderClient.getOrderVO(orderSn);
        //订单金额为0，则不计算
        if (orderDetailVO.getGoodsPrice() == 0) {
            return true;
        }
        //未发货状态，不计算
        if (ShipStatusEnum.SHIP_NO.equals(orderDetailVO.getShipStatus())) {
            return true;
        }

        //可以返还的比例 = 订单申请退款金额/订单金额

        Double calReturnPercentage = 0.0;
        Double oprice = CurrencyUtil.add(orderDetailVO.getNeedPayMoney(), orderDetailVO.getBalance());
        //可以返还的比例 = 订单申请退款金额/订单金额
        if (oprice > 0.0) {
            calReturnPercentage = CurrencyUtil.div(CurrencyUtil.mul(price, 100), oprice);
        }

        DistributionOrderDO distributionOrder = this.getModelByOrderSn(orderSn);

        Double lv1ReturnPrice = 0D;
        Double lv2ReturnPrice = 0D;

        // 1.获取各个级别的memberid
        Long lv1MemberId = distributionOrder.getMemberIdLv1();
        Long lv2MemberId = distributionOrder.getMemberIdLv2();

        DistributionRefundDTO distributionRefundDTO = new DistributionRefundDTO();
        distributionRefundDTO.setMemberIdLv1(lv1MemberId);
        distributionRefundDTO.setMemberIdLv2(lv2MemberId);

        if (lv1MemberId != null && lv1MemberId != 0) {
            //付值，最初的返现金额
            lv1ReturnPrice += distributionOrder.getGrade1SellbackPrice() == null ? 0D : distributionOrder.getGrade1SellbackPrice();
            //最初的返现金额+这回返现金额=总返现金额
            lv1ReturnPrice +=
                    CurrencyUtil.div(
                            CurrencyUtil.mul(
                                    distributionOrder.getGrade1Rebate()
                                    , calReturnPercentage),
                            100);
            distributionOrder.setGrade1SellbackPrice(lv1ReturnPrice);


            distributionRefundDTO.setRefundLv1(distributionOrder.getGrade1Rebate() * calReturnPercentage / 100);
        }
        if (lv2MemberId != null && lv2MemberId != 0) {
            lv2ReturnPrice += distributionOrder.getGrade2SellbackPrice() == null ? 0D : distributionOrder.getGrade2SellbackPrice();
            lv2ReturnPrice +=
                    CurrencyUtil.div(
                            CurrencyUtil.mul(
                                    distributionOrder.getGrade2Rebate()
                                    , calReturnPercentage),
                            100);


            distributionOrder.setGrade2SellbackPrice(lv2ReturnPrice);


            distributionRefundDTO.setRefundLv2(distributionOrder.getGrade2Rebate() * calReturnPercentage / 100);

        }


        if (distributionOrder.getReturnMoney() == null) {

            distributionOrder.setReturnMoney(0D);
        }
        distributionRefundDTO.setRefundMoney(price);
        distributionOrder.setReturnMoney(CurrencyUtil.add(distributionOrder.getReturnMoney(), price));

        // 4.保存订单
        distributionOrderMapper.updateById(distributionOrder);

        // 如果id不为0（有效id）
        if (lv1MemberId != null) {
            UpdateWrapper wrapper = new UpdateWrapper();
            this.distributionManager.subTotalPrice(distributionRefundDTO.getRefundMoney(), distributionRefundDTO.getRefundLv1(), distributionRefundDTO.getMemberIdLv1());
            wrapper.eq("member_id", lv1MemberId);
            //退款时需要退还的返利金额
            wrapper.setSql("can_rebate = can_rebate -" + distributionRefundDTO.getRefundLv1());
            distributionMapper.update(null, wrapper);
        }
        // 有效id 则2级有效
        if (lv2MemberId != null) {
            UpdateWrapper wrapper = new UpdateWrapper();
            this.distributionManager.subTotalPrice(distributionRefundDTO.getRefundMoney(), distributionRefundDTO.getRefundLv2(), distributionRefundDTO.getMemberIdLv2());
            wrapper.eq("member_id", lv2MemberId);
            //退款时需要退还的返利金额
            wrapper.setSql("can_rebate = can_rebate -" + distributionRefundDTO.getRefundLv2());
            distributionMapper.update(null, wrapper);
        }

        //结算单相关处理
        billMemberManager.returnShop(distributionOrderManager.getModelByOrderSn(orderSn), distributionRefundDTO);

        //与商家结算相关判定
        distributionSellerBillManager.addRefund(distributionOrder);


        return true;

    }

    /**
     * 通过订单id，把各个级别的返利金额增加到分销商冻结金额中
     *
     * @param orderSn 订单sn
     * @return 操作结果 true 成功， false 失败
     */
    @Override
    public boolean addDistributorFreeze(String orderSn) {
        // 如果是一个正确的id
        if (orderSn != null) {
            DistributionOrderDO distributionOrder = this.getModelByOrderSn(orderSn);

            // 1.获取各个级别的memberid
            Long lv1MemberId = distributionOrder.getMemberIdLv1();
            Long lv2MemberId = distributionOrder.getMemberIdLv2();

            // 2.获取各个级别的返利金额
            Double lv1Commission = distributionOrder.getGrade1Rebate();
            Double lv2Commission = distributionOrder.getGrade2Rebate();

            // 3.增加到冻结金额中
            UpdateWrapper wrapper = new UpdateWrapper();
            if (lv1MemberId != null && lv1MemberId != 0) {
                wrapper.eq("member_id", lv1MemberId);
                //增加到冻结金额中
                wrapper.setSql("frozen_price = frozen_price +" + lv1Commission);
                distributionMapper.update(null, wrapper);
            }
            if (lv2MemberId != null && lv2MemberId != 0) {
                wrapper.eq("member_id", lv2MemberId);
                //增加到冻结金额中
                wrapper.setSql("frozen_price = frozen_price +" + lv2Commission);
                distributionMapper.update(null, wrapper);
            }
            return true;
        }
        return false;
    }

    /**
     * 根据购买人增加上级人员订单数量
     *
     * @param buyMemberId 购买人会员id
     */
    @Override
    public void addOrderNum(Long buyMemberId) {
        DistributionDO buyDistributor = this.distributionManager.getDistributorByMemberId(buyMemberId);

        // 上级订单数量
        Long lv1MemberId = buyDistributor.getMemberIdLv1();
        Long lv2MemberId = buyDistributor.getMemberIdLv2();

        // 如果存在上级
        if (null != lv1MemberId) {
            distributionMapper.update(null, new UpdateWrapper<DistributionDO>()
                    .setSql("order_num = order_num + 1")
                    .eq("member_id", lv1MemberId));
        }
        // 如果存在2级
        if (lv2MemberId != null) {
            distributionMapper.update(null, new UpdateWrapper<DistributionDO>()
                    .setSql("order_num = order_num + 1")
                    .eq("member_id", lv2MemberId));
        }
    }

    /**
     * 根据价格 算出lv1 lv2的返利金额
     *
     * @param distributionOrderDO do
     * @param lv1MemberId         lv1会员id
     * @param lv2MemberId         lv2会员id
     *                            Map集合 key： lv1_rebate=lv1返利金额 lv2_rebate=lv2返利金额
     */
    private void calRebate(DistributionOrderDO distributionOrderDO, Long lv1MemberId, Long lv2MemberId) {

        //查询系统设置
        String json = settingClient.get(SettingGroup.DISTRIBUTION);
        DistributionSetting distributionSetting = JsonUtil.jsonToObject(json, DistributionSetting.class);

        // 1.获取各个级别的分销商
        DistributionDO lv1Distributor = this.distributionManager.getDistributorByMemberId(lv1MemberId);
        DistributionDO lv2Distributor = this.distributionManager.getDistributorByMemberId(lv2MemberId);

        //如果商品模式开启，则优先按照商品进行计算
        if (distributionSetting.getGoodsModel() == 1) {
            List<OrderItemsDO> orderItemsDOS = orderClient.orderItems(distributionOrderDO.getOrderSn());
            //商品返现计算
            this.goodsRebate(distributionOrderDO, orderItemsDOS, lv1Distributor, lv2Distributor);

        } else {
            // 模板返现计算
            this.tplRebate(distributionOrderDO, lv1Distributor, lv2Distributor);
        }
        //增加商家结算支出
        distributionSellerBillManager.add(distributionOrderDO);

    }

    /**
     * 模版返现
     *
     * @param lv1Distributor
     * @param lv2Distributor
     */
    private void tplRebate(DistributionOrderDO distributionOrderDO, DistributionDO lv1Distributor, DistributionDO lv2Distributor) {

        Map<String, Double> map = new HashMap<String, Double>(16);

        double lv1Commission = 0;
        double lv2Commission = 0;

        // 2.获取各个级别分销商的模板对象
        // 如果有这个级别的分销商才计算
        if (lv1Distributor != null) {
            CommissionTpl lv1CommissionTpl = this.commissionTplManager
                    .getModel(lv1Distributor.getCurrentTplId());
            double lv1CommissionRatio = lv1CommissionTpl.getGrade1();

            lv1Commission = CurrencyUtil.div(CurrencyUtil.mul(lv1CommissionRatio, distributionOrderDO.getOrderPrice()), 100);
            distributionOrderDO.setLv1Point(lv1CommissionRatio);
        }

        // 如果有这个级别的分销商才计算
        if (lv2Distributor != null) {

            CommissionTpl lv2CommissionTpl = this.commissionTplManager
                    .getModel(lv2Distributor.getCurrentTplId());
            double lv2CommissionRatio = lv2CommissionTpl.getGrade2();
            lv2Commission = CurrencyUtil.div(CurrencyUtil.mul(lv2CommissionRatio, distributionOrderDO.getOrderPrice()), 100);
            distributionOrderDO.setLv2Point(lv2CommissionRatio);
        }
        distributionOrderDO.setGrade1Rebate(lv1Commission);
        distributionOrderDO.setGrade2Rebate(lv2Commission);
    }


    /**
     * 商品返现
     *
     * @param orderItemsDOS
     * @param lv1Distributor
     * @param lv2Distributor
     * @return
     */
    private void goodsRebate(DistributionOrderDO distributionOrderDO, List<OrderItemsDO> orderItemsDOS, DistributionDO lv1Distributor, DistributionDO lv2Distributor) {
        //计算出商品返现的金额，并且记录商品返现单件返现金额
        Map<Long, Double> grade1 = new HashMap<>(16);
        Map<Long, Double> grade2 = new HashMap<>(16);
        Map<Long, Integer> num = new HashMap<Long, Integer>(16);
        List<DistributionGoods> dgs = new ArrayList<>();
        for (OrderItemsDO orderItemsDO : orderItemsDOS) {
            DistributionGoods distributionGoods = distributionGoodsManager.getModel(orderItemsDO.getGoodsId());
            //如果没有设置商品返现，则设置一个默认0返现的商品
            if (distributionGoods == null) {
                distributionGoods = new DistributionGoods();
                distributionGoods.setGoodsId(orderItemsDO.getGoodsId());
                distributionGoods.setGrade1Rebate(0);
                distributionGoods.setGrade2Rebate(0);
                distributionGoods.setId(0L);
            }
            dgs.add(distributionGoods);
            if (distributionGoods != null) {
                grade1.put(orderItemsDO.getProductId(), distributionGoods.getGrade1Rebate());
                grade2.put(orderItemsDO.getProductId(), distributionGoods.getGrade2Rebate());
                num.put(orderItemsDO.getProductId(), orderItemsDO.getNum());
            }
        }
        //根据商品返现
        double lv1Commission = 0;
        double lv2Commission = 0;
        for (Long productId : grade1.keySet()) {
            if (lv1Distributor != null) {
                lv1Commission = CurrencyUtil.add(lv1Commission, CurrencyUtil.mul(grade1.get(productId), num.get(productId)));
            }
            if (lv2Distributor != null) {
                lv2Commission = CurrencyUtil.add(lv2Commission, CurrencyUtil.mul(grade2.get(productId), num.get(productId)));
            }
        }
        Map<String, Double> result = new HashMap<>(16);
        result.put("lv1_rebate", lv1Commission);
        result.put("lv2_rebate", lv2Commission);
        distributionOrderDO.setGrade1Rebate(lv1Commission);
        distributionOrderDO.setGrade2Rebate(lv2Commission);
        Gson gson = new Gson();
        distributionOrderDO.setGoodsRebate(gson.toJson(dgs));
    }

    /**
     * 结算单订单查询
     *
     * @param pageSize  每页数量
     * @param page 第几页
     * @param memberId 会员id
     * @param billId 账单id
     * @return
     */
    @Override
    public WebPage<DistributionOrderVO> pageDistributionOrder(Long pageSize, Long page, Long memberId, Long billId) {

        if (memberId == null || billId == null) {
            throw new DistributionException(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des());
        }

        IPage<DistributionOrderDO> iPage = distributionOrderMapper.selectPageDo(new Page<>(page, pageSize), memberId, billId);
        WebPage<DistributionOrderDO> data = PageConvert.convert(iPage);

        // DistributionOrderDO转换DistributionOrderVO
        List<DistributionOrderVO> list = new ArrayList<>();
        for (DistributionOrderDO ddo : data.getData()) {
            list.add(new DistributionOrderVO(ddo, memberId));
        }

        WebPage<DistributionOrderVO> result = new WebPage<>(data.getPageNo(), data.getDataTotal(), data.getPageSize(), list);
        return result;
    }

    /**
     * 分销商退货订单分页
     * @param pageSize 每页数量
     * @param page 第几页
     * @param memberId 会员id
     * @param billId 账单id
     * @return
     */
    @Override
    public WebPage<DistributionSellbackOrderVO> pageSellBackOrder(Long pageSize, Long page, Long memberId, Long billId) {

        if ((memberId == null) || (billId == null)) {
            throw new DistributionException(DistributionErrorCode.E1011.code(), DistributionErrorCode.E1011.des());
        }

        IPage<DistributionOrderDO> iPage = distributionOrderMapper.selectPageDoo(new Page<>(page, pageSize), memberId, billId);
        WebPage<DistributionOrderDO> data = PageConvert.convert(iPage);

        // DistributionOrderDO转换DistributionSellbackOrderVO
        List<DistributionSellbackOrderVO> list = new ArrayList<>();
        for (DistributionOrderDO ddo : data.getData()) {
            list.add(new DistributionSellbackOrderVO(ddo, memberId));
        }

        WebPage<DistributionSellbackOrderVO> result = new WebPage<>(data.getPageNo(), data.getDataTotal(), data.getPageSize(), list);
        return result;

    }


    /**
     * 确认收款事件
     *
     * @param order
     */
    @Transactional(value = "distributionTransactionManager", rollbackFor = Exception.class)
    @Override
    public void confirm(OrderDO order) {

        //换货订单或者补发商品订单不产生返利金额
        if (OrderTypeEnum.CHANGE.name().equals(order.getOrderType())
                || OrderTypeEnum.SUPPLY_AGAIN.name().equals(order.getOrderType())) {
            return;
        }

        //计算返利金额
        try {
            Long buyMemberId = order.getMemberId();
            DistributionDO distributor = this.distributionManager.getDistributorByMemberId(buyMemberId);

            // 新增分销关联订单
            DistributionOrderDO distributionOrderDO = new DistributionOrderDO();
            distributionOrderDO.setMemberIdLv1(distributor.getMemberIdLv1());
            distributionOrderDO.setMemberIdLv2(distributor.getMemberIdLv2());
            distributionOrderDO.setOrderId(order.getOrderId());
            distributionOrderDO.setBuyerMemberId(buyMemberId);
            distributionOrderDO.setBuyerMemberName(distributor.getMemberName());
            distributionOrderDO.setOrderSn(order.getSn());
            // 解锁周期
            String setting = settingClient.get(SettingGroup.DISTRIBUTION);

            DistributionSetting ds = JsonUtil.jsonToObject(setting, DistributionSetting.class);

            distributionOrderDO.setSettleCycle((ds.getCycle() * 3600 * 24) + new Long(DateUtil.getDateline()).intValue());
            double orderPrice = 0.0;
            if (order.getNeedPayMoney() != null && order.getNeedPayMoney() > 0.0) {
                orderPrice = order.getNeedPayMoney();
            }
            if (order.getBalance() != null && order.getBalance() > 0.0) {
                orderPrice = CurrencyUtil.add(orderPrice, order.getBalance());
            }
            distributionOrderDO.setOrderPrice(orderPrice);
            distributionOrderDO.setCreateTime(order.getCreateTime());
            distributionOrderDO.setSellerId(order.getSellerId());
            this.distributionOrderManager.add(distributionOrderDO);
            this.logger.info("订单【" + order.getSn() + "】支付金额【" + order.getNeedPayMoney() + "】");

            // 调用增加订单数量
            this.distributionOrderManager.addOrderNum(order.getMemberId());

            // 调用计算返利金额方法
            this.distributionOrderManager.calCommission(order.getSn());
            //计算业绩
            billMemberManager.buyShop(this.distributionOrderManager.getModelByOrderSn(order.getSn()));

        } catch (RuntimeException e) {
            this.logger.error("分销提现计算异常", e);
        }

    }


}
