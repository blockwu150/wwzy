package com.enation.app.javashop.service.trade.order.impl;

import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.enation.app.javashop.mapper.trade.order.TradeMapper;
import com.enation.app.javashop.service.trade.order.TradePriceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易价格业务
 * @author Snow create in 2018/3/22
 * @version v2.0
 * @since v7.0.0
 */

@Service
public class TradePriceManagerImpl implements TradePriceManager {


    @Autowired
    private TradeMapper tradeMapper;


    /**
     * 未付款的订单，商家修改订单金额，同时修改交易价格
     * @param tradeSn		交易编号
     * @param tradePrice    交易价格
     * @param discountPrice 优惠的金额
     */
    @Override
    @Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void updatePrice(String tradeSn, Double tradePrice, Double discountPrice) {

        new UpdateChainWrapper<>(tradeMapper)
                //设置总价格
                .set("total_price", tradePrice)
                //设置优惠的金额
                .set("discount_price", discountPrice)
                //按订单编号修改
                .eq("trade_sn", tradeSn)
                //提交修改
                .update();
    }


}
