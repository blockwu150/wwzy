package com.enation.app.javashop.service.payment.plugin.ecloud;

import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.util.*;

public class Demo {


//    http://testpay.eyunpay.com/
//appid:wp1583831144030
//key:fk6Qp9Rh6uh06eBp5042lUYvu9cOwG0v



//    wp1653281758077
//    WbAN66KGBvIrtPOfe9JCvcZEslQv8tL9
//    http://payapi.eyunpay.com
//    static String URL = "http://testpay.eyunpay.com/";
//    static String appid = "wp1583831144030";
//    static String key = "fk6Qp9Rh6uh06eBp5042lUYvu9cOwG0v";

    static String URL = "http://payapi.eyunpay.com/open/api/pay/";
    static String appid = "wp1653281758077";
    static String key = "WbAN66KGBvIrtPOfe9JCvcZEslQv8tL9";


    static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args){
        order("100");
        //query("订单号");
//        refund("订单号","退款金额");
    }

    /**
     * 下单接口
     * @param amount
     */
    public static void order(String amount){
        String api = URL + "order";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid",appid);
        params.put("orderId","1653522127815");
        params.put("amount",amount);
        params.put("payType","WECHAT_JSAPI");
        params.put("product","TEST");
        params.put("productDesc","TEST");
        params.put("asynchUrl","http://api.nft.wwzy.club/payment/callback/ORDER/fastpayPlugin/NATIVE");
        params.put("ip","127.0.0.1");
        params.put("sign", sign(buildSignStr(params),key));
        String result = restTemplate.postForObject(api,params,String.class);
        System.out.println(result);
    }

    /**
     * 查询接口
     * @param order
     */
    public static void query(String order){
        String api = URL + "query";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid",appid);
        params.put("orderId",order);
        params.put("sign", sign(buildSignStr(params),key));
        String result = restTemplate.postForObject(api,params,String.class);
        System.out.println(result);
    }

    /**
     * 退款接口
     * @param order
     */
    public static void refund(String order,String amount){
        String api = URL + "refund";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("appid",appid);
        params.put("orderId",order);
        params.put("refundAmount",amount);
        params.put("sign", sign(buildSignStr(params),key));
        String result = restTemplate.postForObject(api,params,String.class);
        System.out.println(result);
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
}