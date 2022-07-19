package com.enation.app.javashop.service.payment.plugin.weixin.executor;

import com.enation.app.javashop.framework.context.request.ThreadContextHolder;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.model.base.DomainHelper;
import com.enation.app.javashop.model.payment.enums.ClientType;
import com.enation.app.javashop.model.payment.vo.PayBill;
import com.enation.app.javashop.model.trade.order.enums.TradeTypeEnum;
import com.enation.app.javashop.service.payment.plugin.weixin.WeixinPuginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信wap端
 * @date 2018/4/1810:12
 * @since v7.0.0
 */
@Service
public class WeixinPaymentWapExecutor extends WeixinPuginConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String H5 = "H5";

    @Autowired
    private DomainHelper domainHelper;

    /**
     * 支付
     *
     * @param bill
     * @return
     */
    public Map onPay(PayBill bill) {

        Map<String, String> params = new TreeMap<>();
        Map<String, String> result = new TreeMap<>();
        params.put("spbill_create_ip", getIpAddress());
        params.put("trade_type", "MWEB");

        Map<String, String> map = super.createUnifiedOrder(bill, params);
        // 返回结果
        String resultCode = map.get("result_code");
        if (SUCCESS.equals(resultCode)) {
            String codeUrl = map.get("mweb_url");
            String redirect_url=  getPayWapSuccessUrl(bill.getTradeType().name(), bill.getSubSn(),bill.getPluginId());
            result.put("gateway_url", codeUrl + "&redirect_url=" +redirect_url);
            return result;
        }else{
            logger.error("生成微信预付订单出错");
        }

        return null;

    }


    /**
     * 获取支付成功调取页面
     *
     * @param tradeType
     * @return
     */
    private String getPayWapSuccessUrl(String tradeType, String subSn,String pluginId) {

        //uniapp-h5 支付成功结果页
        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        String client = request.getHeader("clientType");

        String redirectUri = "";
        if (ClientType.UNIAPP.name().equals(client)) {
            if (TradeTypeEnum.RECHARGE.name().equals(tradeType)){
                redirectUri = "/mine-module/account-balance?";
            }else{
                redirectUri = "/pages/cashier/cashier?";
            }
        } else {
            //非uniapp的会跳页面
            redirectUri = "/checkout/cashier?";
        }

       StringBuffer url = new StringBuffer(domainHelper.getMobileDomain());

        url.append(redirectUri);

        if (TradeTypeEnum.TRADE.name().equals(tradeType)) {
            url.append("trade_sn=" + subSn);
        } else {
            url.append("order_sn=" + subSn);
        }

        url.append("&is_callback=yes&default_plugin_id="+pluginId);
        String result = "";
        try {
            result = URLEncoder.encode(url.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
