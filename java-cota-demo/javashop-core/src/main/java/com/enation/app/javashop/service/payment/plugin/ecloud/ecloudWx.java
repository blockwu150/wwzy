package com.enation.app.javashop.service.payment.plugin.ecloud;

import org.springframework.stereotype.Service;

@Service
public class ecloudWx extends ecloudpayPlugin {
    @Override
    String getPayType() {
        return "WECHAT_JSAPI";
    }

    @Override
    public String getPluginId() {
        return "ecloudpayWxPlugin";
    }

    @Override
    public String getPluginName() {
        return "微信E付";
    }
}
