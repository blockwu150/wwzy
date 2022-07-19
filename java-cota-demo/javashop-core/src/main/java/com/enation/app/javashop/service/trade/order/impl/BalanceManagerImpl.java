package com.enation.app.javashop.service.trade.order.impl;

import com.enation.app.javashop.client.member.DepositeClient;
import com.enation.app.javashop.model.member.dos.MemberWalletDO;
import com.enation.app.javashop.model.trade.order.enums.TradeTypeEnum;
import com.enation.app.javashop.service.trade.order.plugin.PaymentServicePlugin;
import com.enation.app.javashop.model.trade.order.vo.BalancePayVO;
import com.enation.app.javashop.service.trade.order.BalanceManager;
import com.enation.app.javashop.service.trade.order.TradeQueryManager;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 预存款抵扣业务类
 * @author: liuyulei
 * @create: 2020-01-01 11:55
 * @version:1.0
 * @since:7.1.4
 **/
@Service
public class BalanceManagerImpl implements BalanceManager {


    @Autowired
    private TradeQueryManager tradeQueryManager;

    @Autowired
    private DepositeClient depositeClient;

    @Autowired
    private List<PaymentServicePlugin> paymentServicePlugin;


    /**
     * 预存款抵扣方法
     * @param sn   交易编号/订单编号
     * @param memberId 会员id
     * @param tradeType 交易类型
     * @param password  交易密码
     * @return 预存款支付参数
     */
    @Override
    public BalancePayVO balancePay(String sn,Long memberId, String tradeType, String password) {

        //检测订单、交易是否属于当前登录会员
        this.tradeQueryManager.checkIsOwner(sn,memberId);

        //检测支付密码是否正确
        this.depositeClient.checkPwd(memberId,password);

        //在支付子业务插件中，找到对应业务插件
        PaymentServicePlugin plugin = this.findServicePlugin(tradeType);

        //获取订单待支付金额
        Double needPay = plugin.getPrice(sn);

        //获取会员预存款信息
        MemberWalletDO walletDO = this.depositeClient.getModel(memberId);

        //判断预存款余额与订单待支付金额
        Double diffPrice = CurrencyUtil.sub(needPay,walletDO.getPreDeposite());
        Double balance = 0D;
        if(diffPrice >= 0 ){
            //此时预存款不足，无法完全抵扣所有订单支付基恩
            balance = walletDO.getPreDeposite();
            needPay = diffPrice;

        }else{
            //此时订单支付金额为0
            balance = needPay;
            needPay = 0D;

        }

        BalancePayVO payVO = new BalancePayVO();
        payVO.setSn(sn);
        payVO.setBalance(balance);
        payVO.setNeedPay(needPay);

        //预存款支付，修改订单待支付金额
        plugin.balancePay(payVO,memberId);

        return payVO;

    }



    /**
     * 在支付子业务插件中  找到对应业务插件
     * @param tradeType 交易类型{@link TradeTypeEnum}
     * @return
     */
    private PaymentServicePlugin findServicePlugin(String tradeType) {
        for (PaymentServicePlugin plugin:paymentServicePlugin){
            if (tradeType.equals(plugin.getServiceType())) {
                return plugin;
            }
        }
        return null;
    }
}
