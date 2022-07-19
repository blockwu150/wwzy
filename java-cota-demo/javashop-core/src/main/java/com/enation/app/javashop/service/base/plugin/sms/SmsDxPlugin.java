package com.enation.app.javashop.service.base.plugin.sms;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.enation.app.javashop.framework.logs.Debugger;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.model.base.vo.ConfigItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SmsDxPlugin implements SmsPlatform  {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Debugger debugger;

    @Override
    public List<ConfigItem> definitionConfigItem() {
        List<ConfigItem> list = new ArrayList();


        ConfigItem name = new ConfigItem();
        name.setType("text");
        name.setName("account");
        name.setText("客户账号");

        ConfigItem password = new ConfigItem();
        password.setType("text");
        password.setName("password");
        password.setText("接口密码");

        ConfigItem id = new ConfigItem();
        id.setType("text");
        id.setName("id");
        id.setText("企业ID");

        ConfigItem trumpet = new ConfigItem();
        trumpet.setType("text");
        trumpet.setName("extno");
        trumpet.setText("扩展号");

        list.add(id);
        list.add(name);
        list.add(password);
        list.add(trumpet);

        return list;
    }

    @Override
    public boolean onSend(String phone, String content, Map param) {
        try {
            if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(content) || CollUtil.isEmpty(param)) {
                logger.error("发送短信参数异常,请检查phone={} ,content={} ,param={} ", phone, content, param);
                return false;
            }
            debugger.log("调起SmsDxPlugin", "参数为：", param.toString());
            String urls = "http://81.70.134.239/smsJson.aspx";
            //用户
            param.put("action", "send");
            param.remove("id");
            //手机号
            param.put("mobile", phone);
            //内容
            param.put("content", "【未物】"+content);
            debugger.log("向81.70.134.239发出请求，请求url为：", urls);
            // 返回发送结果
            String result = HttpUtil.post(urls,param);
            debugger.log("收到返回结果：", result);
            JSONObject jsonObject = JSONUtil.parseObj(result);
            if (!StringUtil.equals(jsonObject.get("returnstatus").toString(), "Faild")) {
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
        return "smsDxPlugin";
    }

    @Override
    public String getPluginName() {
        return "兜信网关短信";
    }

    @Override
    public Integer getIsOpen() {
        return 1;
    }
}
