package com.enation.app.javashop.service.payment.plugin.ecloud;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.request.ThreadContextHolder;
import com.enation.app.javashop.framework.logs.Debugger;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.model.aftersale.enums.RefundStatusEnum;
import com.enation.app.javashop.model.payment.enums.ClientType;
import com.enation.app.javashop.model.payment.vo.ClientConfig;
import com.enation.app.javashop.model.payment.vo.PayBill;
import com.enation.app.javashop.model.payment.vo.PayConfigItem;
import com.enation.app.javashop.model.payment.vo.RefundBill;
import com.enation.app.javashop.model.trade.order.enums.TradeTypeEnum;
import com.enation.app.javashop.service.payment.AbstractPaymentPlugin;
import com.enation.app.javashop.service.payment.PaymentPluginManager;
//import com.enation.app.javashop.service.payment.plugin.fastpay.sdk.Des3Utils;
//import com.enation.app.javashop.service.payment.plugin.fastpay.sdk.KFRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author ygg
 * @version v2.0
 * @Description: 支付插件
 * @date 2021/4/12 10:25
 * @since v7.0.0
 */
public abstract class ecloudpayPlugin extends AbstractPaymentPlugin implements PaymentPluginManager {

    static RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Debugger debugger;
    @Autowired
    protected Cache cache;
    abstract String getPayType();
    @Override
    public List<ClientConfig> definitionClientConfig() {
        //声明返回类型
        List<ClientConfig> resultList = new ArrayList<>();
        //声明客户端配置
        ClientConfig config = new ClientConfig();
        //声明配置文件映射实体
        List<PayConfigItem> configList = new ArrayList<>();
        //配置文件映射实体赋值
        PayConfigItem item = new PayConfigItem();
        item.setName("URL");
        item.setText("URL");
        configList.add(item);

        item = new PayConfigItem();
        item.setName("appid");
        item.setText("商户的appid");
        configList.add(item);

        item = new PayConfigItem();
        item.setName("key");
        item.setText("商户的密钥");
        configList.add(item);

//        wp1653281758077
//        WbAN66KGBvIrtPOfe9JCvcZEslQv8tL9

    //客户端配置赋值
        config.setKey(ClientType.PC.getDbColumn() + "&" + ClientType.WAP.getDbColumn() + "&" + ClientType.NATIVE.getDbColumn() + "&" + ClientType.REACT.getDbColumn()
                + "&" + ClientType.MINI.getDbColumn());
        config.setConfigList(configList);
        config.setName("E云付");
        resultList.add(config);
        return resultList;
    }

    @Override
    public Map pay(PayBill bill) {
        Map result = new HashMap();
        Map<String, String> config = this.getConfig(bill.getClientType());
        String url = config.get("URL");
        String appid = config.get("appid");
        String key = config.get("key");

        try {
            String api = url + "order";
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("appid",appid);
            params.put("orderId",bill.getBillSn());
            params.put("amount",bill.getOrderPrice());
            params.put("payType",getPayType());
            params.put("product","下单");
            params.put("productDesc","购物");
            params.put("asynchUrl",this.getCallBackUrl(bill.getTradeType(), bill.getClientType()));
            params.put("ip","127.0.0.1");
            params.put("sign", sign(buildSignStr(params),key));
            String result1 = restTemplate.postForObject(api,params,String.class);
            result= JSONUtil.parseObj(result1);
            logger.debug(result1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onReturn(TradeTypeEnum tradeType) {

    }

    @Override
    public String onCallback(ClientType clientType) {
        debugger.log("进入义云付回调");
        Map<String, String> config = this.getConfig(clientType);
//        String url = config.get("URL");
//        String appid = config.get("appid");
//        String key = config.get("key");
        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        String msg = readBody(request);
        try {
//            logger.debug("解密前报文：" + msg);
//            String reqStr = Des3Utils.decrypt(Des3Utils.urlDecrypt(msg), secretKey);
            logger.debug("解密后报文：" + msg);
            JSONObject reqData = JSONObject.parseObject(msg);
            String billSn = reqData.getString("orderId");
            String returnTradeNo = reqData.getString("channelOrderId");
            double payPrice = StringUtil.toDouble(reqData.getString("amount"), 0d);
            logger.debug("外部订单号：" + returnTradeNo);
            logger.debug("支付订单号：" + billSn);
            logger.debug("交易金额：" + payPrice);
            /*这里建议做一下金额核对。*/
            this.paySuccess(billSn, returnTradeNo, payPrice);

//            if ("100".equals(reqData.getString("transStatus"))) {
//                /*交易成功*/
//
//                String billSn = reqData.getString("outOrderId");
//                String returnTradeNo = reqData.getString("orderCd");
//                double payPrice = StringUtil.toDouble(reqData.getString("transAmt"), 0d);
//
//                logger.debug("外部订单号：" + returnTradeNo);
//                logger.debug("支付订单号：" + billSn);
//                logger.debug("交易金额：" + payPrice);
//
//                /*这里建议做一下金额核对。*/
//                this.paySuccess(billSn, returnTradeNo, payPrice);
//                return Des3Utils.encrypt("{\"respCode\" : \"0000\", \"respMsg\" : \"成功\"}", secretKey);
//            } else if ("102".equals(reqData.getString("transStatus"))) {
//                /*交易失败*/
//            } else {
//                /*未知结果，这种情况不会有。*/
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*  一定要记得返回成功，否则，会继续通知，持续半小时。  */
//        try {
//            return Des3Utils.encrypt("{\"respCode\" : \"0000\", \"respMsg\" : \"成功\"}", secretKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
                    return "SUCCESS";
    }

    @Override
    public String onQuery(String billSn, Map config) {
        return null;
    }

    @Override
    public boolean onTradeRefund(RefundBill bill) {
//        try {
//            Map<String, String> config = bill.getConfigMap();
//            String orgCd = config.get("orgCd");
//            secretKey=config.get("secret");
//            debugger.log("基础参数为", config.toString());
//
//            JSONObject reqData = new JSONObject();
//            //交易码，退款固定：TRANS0107
//            reqData.put("trscode", "TRANS0107");
//            //商户编号
//            reqData.put("mchtCd", config.get("mchtCd"));
//            //原消费的外部订单号
//            reqData.put("oglOrderCd", bill.getReturnTradeNo());
//            //原消费的交易日期
//            reqData.put("oglOrdDate", bill.getReturnTradeNo().substring(0,8));
//            //退款金额（单位:元）
//            reqData.put("transAmt", bill.getRefundPrice());
//
//            /* 发送 */
//            String respStr = KFRequestUtil.req(reqUrl, "ORG", orgCd, secretKey, reqData.toJSONString());
//            JSONObject respData = JSONObject.parseObject(respStr);
//            if ("0000".equals(respData.getString("respCode"))) {
//                /* 交易正常 */
//                logger.debug(respData.getString("respMsg"));
//                if ("100".equals(respData.getString("transStatus"))) {
//                    /*交易成功*/
//                    return true;
//                } else if ("102".equals(respData.getString("transStatus"))) {
//                    /*交易失败*/
//                    cache.put(REFUND_ERROR_MESSAGE+"_"+bill.getRefundSn(),respStr );
//                    return false;
//                } else {
//                    /*交易状态未知，请调查询接口获取最终状态*/
//                }
//            } else {
//                /* 交易异常 */
//                logger.debug(respData.getString("respMsg"));
//                /*交易状态未知，请调查询接口获取最终状态*/
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return false;
    }

    @Override
    public String queryRefundStatus(RefundBill bill) {
//        try {
//            Map<String, String> config = bill.getConfigMap();
//            String orgCd = config.get("orgCd");
//            secretKey=config.get("secret");
//            debugger.log("基础参数为", config.toString());
//
//            JSONObject reqData = new JSONObject();
//            //交易码，查询固定：TRANS0102
//            reqData.put("trscode", "TRANS0102");
//            //商户编号
//            reqData.put("mchtCd", config.get("mchtCd"));
//            //原消费的外部订单号
//            reqData.put("oglOrdId", bill.getReturnTradeNo());
//            //原消费的交易日期
//            reqData.put("oglOrdDate", bill.getReturnTradeNo().substring(0,8));
//
//            /* 发送 */
//            String respStr = KFRequestUtil.req(reqUrl, "ORG", orgCd, secretKey, reqData.toJSONString());
//            JSONObject respData = JSONObject.parseObject(respStr);
//            if("0000".equals(respData.getString("respCode"))){
//                /* 交易正常 */
//                logger.debug(respData.getString("respMsg"));
//                if("100".equals(respData.getString("transStatus"))){
//                    /*交易成功*/
//                    return RefundStatusEnum.COMPLETED.value();
//                }else if("102".equals(respData.getString("transStatus"))){
//                    /*交易失败*/
//                    return RefundStatusEnum.REFUNDFAIL.value();
//                } else {
//                    /*交易状态未知，请调查询接口获取最终状态*/
//                }
//            } else {
//                /* 交易异常 */
//                logger.debug(respData.getString("respMsg"));
//                /*交易状态未知，请调查询接口获取最终状态*/
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        return RefundStatusEnum.REFUNDING.value();
    }

    @Override
    public Integer getIsRetrace() {
        return 1;
    }


    /**
     * 生成签名字符串
     * @param params
     * @return
     */
    public static String buildSignStr(Map<String,?> params) {
        if(params == null || params.size() == 0) {
            return "";
        }
        // 创建明文
        StringBuffer signStr = new StringBuffer();
        // 获取参数key
        List<String> keySet = new ArrayList<String>(params.keySet());
        // 对参数进行排序
        Collections.sort(keySet);
        // 生成明文
        for(String key:keySet) {
            try {
                signStr.append((key + "=" + URLDecoder.decode(params.get(key) + "","UTF-8") + "&"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("签名原串:" + signStr.toString().replaceAll("\\ ","+"));
        return signStr.toString().replaceAll("\\ ","+");
    }

    /**
     * 进行签名
     * @param signStr
     * @param key
     * @return
     */
    public static String sign(String signStr,String key) {
        try {
            return MD5.sign(signStr + "key=", key, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    static String readBody(HttpServletRequest request)  {
        String ret = null;
        if(request.getParameterMap().size()>0) {
            Set<String> a = request.getParameterMap().keySet();
            ret = a.iterator().next();
            return ret;
        }
        int len = request.getContentLength();
        byte[] buffer = new byte[len];
        ServletInputStream in = null;
        try {
            in = request.getInputStream();
            in.read(buffer,0,len);
            in.close();
            ret = new String(buffer,0,len,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
}

