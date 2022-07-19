package com.enation.app.javashop.service.distribution.impl;

import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.mapper.distribution.DistributionSellerBillMapper;
import com.enation.app.javashop.model.distribution.dos.DistributionSellerBillDO;
import com.enation.app.javashop.model.distribution.dos.DistributionOrderDO;
import com.enation.app.javashop.model.distribution.dto.DistributionSellerBillDTO;
import com.enation.app.javashop.service.distribution.DistributionSellerBillManager;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-09-05 下午4:12
 */
@Service
public class DistributionSellerBillManagerImpl implements DistributionSellerBillManager {

    @Autowired
    private DistributionSellerBillMapper distributionSellerBillMapper;

    /**
     * 新增记录
     *
     * @param distributionOrderDO
     */
    @Override
    public void add(DistributionOrderDO distributionOrderDO) {
        //创建分销商家结算单对象，相当于收款单
        DistributionSellerBillDO distributionSellerBillDO = new DistributionSellerBillDO();
        distributionSellerBillDO.setSellerId(distributionOrderDO.getSellerId());
        distributionSellerBillDO.setCreateTime(DateUtil.getDateline());
        //设置1级提成金额
        if(distributionOrderDO.getGrade1Rebate()==null){
            distributionOrderDO.setGrade1Rebate(0D);
        }
        //设置2级提成金额
        if(distributionOrderDO.getGrade2Rebate()==null){
            distributionOrderDO.setGrade2Rebate(0D);
        }
        //商品反现支出=1级提成金额+2级提成金额
        distributionSellerBillDO.setExpenditure(CurrencyUtil.add(distributionOrderDO.getGrade1Rebate(), distributionOrderDO.getGrade2Rebate()));
        distributionSellerBillDO.setReturnExpenditure(0D);
        distributionSellerBillDO.setOrderSn(distributionOrderDO.getOrderSn());
        //添加
        distributionSellerBillMapper.insert(distributionSellerBillDO);
    }

    /**
     * 新增退款记录
     *
     * @param distributionOrderDO
     */
    @Override
    public void addRefund(DistributionOrderDO distributionOrderDO) {
        //创建分销商家结算单对象，相当于退款单
        DistributionSellerBillDO distributionSellerBillDO = new DistributionSellerBillDO();
        distributionSellerBillDO.setSellerId(distributionOrderDO.getSellerId());
        distributionSellerBillDO.setCreateTime(DateUtil.getDateline());
        //设置1级退款金额
        if(distributionOrderDO.getGrade1SellbackPrice()==null){
            distributionOrderDO.setGrade1SellbackPrice(0D);
        }
        //设置2级退款金额
        if(distributionOrderDO.getGrade2SellbackPrice()==null){
            distributionOrderDO.setGrade2SellbackPrice(0D);
        }
        //商品反现退还=1级退款金额+2级退款金额
        distributionSellerBillDO.setReturnExpenditure(CurrencyUtil.add(distributionOrderDO.getGrade1SellbackPrice(), distributionOrderDO.getGrade2SellbackPrice()));
        distributionSellerBillDO.setExpenditure(0D);
        distributionSellerBillDO.setOrderSn(distributionOrderDO.getOrderSn());
        //添加
        distributionSellerBillMapper.insert(distributionSellerBillDO);
    }

    /**
     * 商家返现统计
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    @Override
    public List<DistributionSellerBillDTO> countSeller(Integer startTime, Integer endTime) {
        List<DistributionSellerBillDTO> list = distributionSellerBillMapper.queryCountSeller(startTime,endTime);
        return list;
    }

}
