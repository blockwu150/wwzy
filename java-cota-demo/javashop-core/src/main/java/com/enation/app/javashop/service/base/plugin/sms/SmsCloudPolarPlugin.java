package com.enation.app.javashop.service.base.plugin.sms;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.enation.app.javashop.framework.logs.Debugger;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.model.base.vo.ConfigItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SmsCloudPolarPlugin implements SmsPlatform  {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Debugger debugger;

    @Override
    public List<ConfigItem> definitionConfigItem() {
        List<ConfigItem> list = new ArrayList();

        ConfigItem accessKey = new ConfigItem();
        accessKey.setType("text");
        accessKey.setName("accessKey");
        accessKey.setText("API账号");

        ConfigItem accessSecret = new ConfigItem();
        accessSecret.setType("text");
        accessSecret.setName("accessSecret");
        accessSecret.setText("API秘钥");

        ConfigItem signCode = new ConfigItem();
        signCode.setType("text");
        signCode.setName("signCode");
        signCode.setText("短信签名");

        ConfigItem templateCode = new ConfigItem();
        templateCode.setType("text");
        templateCode.setName("templateCode");
        templateCode.setText("短信模版Code");

        ConfigItem classificationSecret = new ConfigItem();
        classificationSecret.setType("text");
        classificationSecret.setName("classificationSecret");
        classificationSecret.setText("套餐分类秘钥");

        list.add(accessKey);
        list.add(accessSecret);
        list.add(signCode);
        list.add(templateCode);
        list.add(classificationSecret);
        return list;
    }

    @Override
    public boolean onSend(String phone, String content, Map param) {
//        AuzZzNQu
        try {
            if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(content) || CollUtil.isEmpty(param)) {
                logger.error("发送短信参数异常,请检查phone={} ,content={} ,param={} ", phone, content, param);
                return false;
            }
            debugger.log("调起SmsCloudPolarPlugin", "参数为：", param.toString());
            String urls = "https://market.juncdt.com/smartmarket/msgService/sendMessage";



            JSONObject jsonBody = new JSONObject();
            jsonBody.put("accessKey", param.get("accessKey"));
            jsonBody.put("accessSecret", param.get("accessSecret"));
            jsonBody.put("classificationSecret", param.get("classificationSecret"));
            jsonBody.put("signCode", param.get("signCode"));
            jsonBody.put("templateCode", param.get("templateCode"));
            jsonBody.put("phone", phone);
            // 变量参数用map存
            Map<String, String> params = new HashMap<>();
            params.put("content", content);
            jsonBody.put("params", params);
            System.out.println(jsonBody.toString());

//            //API账号
//            param.put("accessKey", param.get("accessKey"));
//            //API秘钥
//            param.put("accessSecret", param.get("accessSecret"));
//            //短信签名
//            param.put("signCode", param.get("signCode"));
//            //短信模版Code
//            param.put("templateCode", param.get("templateCode"));
//            //套餐分类秘钥
//            param.put("classificationSecret", param.get("classificationSecret"));
//            //手机号
//            param.put("phone", phone);
//            //内容
//            param.put("params", "{\"content\":\"" + content + "\"}");
//            System.out.println("{\"content\":\"" + content + "\"}");
            debugger.log("向market.juncdt.com发出请求，请求url为：", urls);
            // 返回发送结果
            String result = HttpUtil.post(urls,jsonBody.toString());
            debugger.log("收到返回结果：", result);
            System.out.println(result);
            JSONObject jsonObject = JSONUtil.parseObj(result);
            JSONObject businessData= (JSONObject) jsonObject.get("BusinessData");
            if (!StringUtil.equals(businessData.get("code").toString(), "10000")) {
                logger.error("发送短信异常" + result);
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("发送短信异常", e);
        }
        return false;
    }

    @Override
    public String getPluginId() {
        return "SmsCloudPolarPlugin";
    }

    @Override
    public String getPluginName() {
        return "云极网关短信";
    }

    @Override
    public Integer getIsOpen() {
        return 1;
    }
}
