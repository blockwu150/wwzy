package com.enation.app.javashop.service.payment.impl;

import com.enation.app.javashop.model.payment.enums.ClientType;
import com.enation.app.javashop.service.payment.PaymentMethodManager;
import com.enation.app.javashop.service.payment.plugin.weixin.WeixinUtil;
import com.enation.app.javashop.service.payment.WechatSmallchangeManager;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * WechatSmallchangeServiceImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2019-06-11 下午4:26
 */
@Service
public class WechatSmallchangeManagerImpl implements WechatSmallchangeManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaymentMethodManager paymentMethodManager;
    /**
     * 支付url
     */
    private String PAY_BASE_URL = "https://api.mch.weixin.qq.com";

    /**
     * /**
     * <pre>
     * 企业付款业务是基于微信支付商户平台的资金管理能力，为了协助商户方便地实现企业向个人付款，针对部分有开发能力的商户，提供通过API完成企业付款的功能。
     * 比如目前的保险行业向客户退保、给付、理赔。
     * 企业付款将使用商户的可用余额，需确保可用余额充足。查看可用余额、充值、提现请登录商户平台“资金管理”https://pay.weixin.qq.com/进行操作。
     * 注意：与商户微信支付收款资金并非同一账户，需要单独充值。
     * 文档详见:https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
     * 接口链接：https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers
     * </pre>
     * <p>
     * 自动发送零钱红包
     *
     * @param openId
     * @param price
     */
    @Override
    public boolean autoSend(String openId, Double price, String ip, String sn) {

        try {

            Map<String, String> returnParams = null;
            //2019-06-12 参数2 见 com.enation.app.javashop.service.payment.plugin.weixin.WeixinPuginConfig.getPluginId()
            Map<String, String> config = paymentMethodManager.getConfig(ClientType.WAP.getDbColumn(), "weixinPayPlugin");
            String appId = config.get("appid");
            String mchId = config.get("mchid");
            String key = config.get("key");
            String p12 = config.get("p12_path");


            File file = new File(p12);
            if (!file.exists()) {
                return false;
            }

            Map<String, String> params = new TreeMap<>();
            params.put("mch_appid", appId);
            params.put("mchid", mchId);
            //随机字符串，不长于32位
            params.put("nonce_str", StringUtil.getRandStr(10));
            params.put("openid", openId);
            params.put("partner_trade_no", sn);
            params.put("check_name", "NO_CHECK");
            params.put("amount", CurrencyUtil.toFen(price));
            params.put("desc", "佣金提现");
            params.put("spbill_create_ip", ip);

            String sign = WeixinUtil.createSign(params, key);
            //签名
            params.put("sign", sign);
            String url = PAY_BASE_URL + "/mmpaymkttransfers/promotion/transfers";

            String xml = null;
            try {
                xml = WeixinUtil.mapToXml(params);
            } catch (Exception e) {
                this.logger.error("不规范的参数:" + JsonUtil.objectToJson(params));
                throw new ServiceException("不规范的参数", e.getMessage());
            }
            logger.debug("url:" + url);
            logger.debug("params:" + xml);
            Document resultDoc = WeixinUtil.verifyCertPost(url, xml, mchId, p12);
            returnParams = WeixinUtil.xmlToMap(resultDoc);
            logger.debug("WechatMpService.entPay result:" + JsonUtil.objectToJson(returnParams));

            //创建操作成功的值
            String successValue = "SUCCESS";

            //此字段是通信标识，非交易标识，返回值为SUCCESS/FAIL，交易是否成功需要查看result_code来判断
            String returnCode = returnParams.get("return_code");
            logger.debug("return_code值为：" + returnCode);
            //SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况。如果状态为FAIL，请务必关注错误代码（err_code字段），通过查询查询接口确认此次付款的结果。
            String resultCode = returnParams.get("result_code");
            logger.debug("result_code值为：" + resultCode);

            //当returnCode和resultCode值都为SUCCESS时，才能证明付款成功
            if (successValue.equals(returnCode) && successValue.equals(resultCode)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            this.logger.error("零钱接口异常", e);
            return false;
        }


    }

}
