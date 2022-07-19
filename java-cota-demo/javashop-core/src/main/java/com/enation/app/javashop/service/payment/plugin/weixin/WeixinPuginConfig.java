package com.enation.app.javashop.service.payment.plugin.weixin;

import com.enation.app.javashop.model.base.SettingGroup;
import com.enation.app.javashop.client.system.SettingClient;
import com.enation.app.javashop.model.payment.vo.PayBill;
import com.enation.app.javashop.service.payment.AbstractPaymentPlugin;
import com.enation.app.javashop.model.system.vo.SiteSetting;
import com.enation.app.javashop.framework.context.request.ThreadContextHolder;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author fk
 * @version v2.0
 * @Description: 微信相关config
 * @date 2018/4/1810:14
 * @since v7.0.0
 */
public class WeixinPuginConfig extends AbstractPaymentPlugin {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String OPENID_SESSION_KEY = "weixin_openid";
    public static final String UNIONID_SESSION_KEY = "weixin_unionid";
    public static final String QR_URL_PREFIX = "weixin://wxpay/bizpayurl[?]pr=";
    public static final String CACHE_KEY_PREFIX = "{pay}_";

    @Autowired
    private SettingClient settingClient;


    @Override
    protected String getPluginId() {

        return "weixinPayPlugin";
    }


    /**
     * 组装参数生成预付订单
     *
     * @param bill
     * @param params
     * @return 微信返回信息和支付参数
     */
    protected Map<String, String> createUnifiedOrder(PayBill bill, Map<String, String> params) {

        Map<String, String> map = this.getConfig(bill.getClientType());
        WeixinPayConfig config = new WeixinPayConfig();
        config.setAppId(map.get("appid"));
        config.setMchId(map.get("mchid"));
        config.setKey(map.get("key"));

        params.put("appid", config.getAppId());
        params.put("mch_id", config.getMchId());
        params.put("nonce_str", StringUtil.getRandStr(10));
        params.put("body", getSiteName() + "-" + bill.getSubSn());
        params.put("out_trade_no", bill.getBillSn());
        // 应付转为分
        Double money = bill.getOrderPrice();
        if (money != null) {
            params.put("total_fee", CurrencyUtil.toFen(money));
        }
        params.put("notify_url", this.getCallBackUrl(bill.getTradeType(), bill.getClientType()));
        logger.info("微信回调地址：" + this.getCallBackUrl(bill.getTradeType(), bill.getClientType()));

        String sign = WeixinUtil.createSign(params, config.getKey());
        params.put("sign", sign);
        try {
            String xml = WeixinUtil.mapToXml(params);

            logger.debug("微信支付请求参数如下：");
            logger.debug(xml);

            Document resultDoc = WeixinUtil.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);



            //Element rootEl = resultDoc.getRootElement();

            Map resultMap = WeixinUtil.xmlToMap(resultDoc);
            xml = WeixinUtil.mapToXml(resultMap);

            logger.debug("微信返回值为：");
            logger.debug(xml);

            resultMap.putAll(map);

            return resultMap;
        } catch (Exception e) {
            this.logger.error("生成参数失败", e);
        }

        return null;
    }

    /**
     * 获取ip
     *
     * @return
     */
    protected String getIpAddress() {
        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtil.isEmpty(ip)) {
            return request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Cdn-Src-Ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取站点名称
     * @return
     */
    protected String getSiteName() {
        String siteSettingJson = this.settingClient.get(SettingGroup.SITE);

        SiteSetting siteSetting = JsonUtil.jsonToObject(siteSettingJson, SiteSetting.class);
        return siteSetting.getSiteName();
    }
}
