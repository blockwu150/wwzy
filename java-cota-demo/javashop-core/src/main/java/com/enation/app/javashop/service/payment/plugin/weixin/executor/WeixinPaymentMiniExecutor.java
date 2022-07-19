package com.enation.app.javashop.service.payment.plugin.weixin.executor;

import com.enation.app.javashop.model.payment.vo.PayBill;
import com.enation.app.javashop.service.payment.plugin.weixin.WeixinPuginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信wap端
 * @date 2018/4/1810:12
 * @since v7.0.0
 */
@Service
public class WeixinPaymentMiniExecutor extends WeixinPuginConfig {

    @Autowired
    private WeixinPaymentJsapiExecutor weixinPaymentJsapiExecutor;

    /**
     * 支付
     *
     * @param bill
     * @return
     */
    public Map onPay(PayBill bill) {
        return weixinPaymentJsapiExecutor.onPay(bill);
    }


}
