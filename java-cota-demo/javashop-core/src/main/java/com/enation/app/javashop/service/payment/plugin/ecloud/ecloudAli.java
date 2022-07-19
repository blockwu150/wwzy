package com.enation.app.javashop.service.payment.plugin.ecloud;

import org.springframework.stereotype.Service;

@Service
public class ecloudAli extends ecloudpayPlugin {
    @Override
    String getPayType() {
        return "ALI_JSAPI";
    }

    @Override
    public String getPluginId() {
        return "ecloudpayAliPlugin";
    }

    @Override
    public String getPluginName() {
        return "支付宝E付";
    }
}
