package com.enation.app.javashop.service.payment.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.mapper.payment.PaymentMethodMapper;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.errorcode.PaymentErrorCode;
import com.enation.app.javashop.model.payment.dos.PaymentMethodDO;
import com.enation.app.javashop.model.payment.enums.ClientType;
import com.enation.app.javashop.model.payment.vo.ClientConfig;
import com.enation.app.javashop.model.payment.vo.PayConfigItem;
import com.enation.app.javashop.model.payment.vo.PaymentMethodVO;
import com.enation.app.javashop.model.payment.vo.PaymentPluginVO;
import com.enation.app.javashop.service.payment.PaymentMethodManager;
import com.enation.app.javashop.service.payment.PaymentPluginManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付方式表业务类
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-04-11 16:06:57
 */
@Service
public class PaymentMethodManagerImpl implements PaymentMethodManager {

    @Autowired
    private List<PaymentPluginManager> paymentPluginList;

    @Autowired
    private Cache cache;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Override
    public WebPage list(long page, long pageSize) {

        List<PaymentPluginVO> resultList = new ArrayList<>();

        //查询数据库中的支付方式
        List<PaymentMethodDO> list = new QueryChainWrapper<>(paymentMethodMapper).list();
        Map<String, PaymentMethodDO> map = new HashMap<>(list.size());

        for (PaymentMethodDO payment : list) {
            map.put(payment.getPluginId(), payment);
        }

        for (PaymentPluginManager plugin : paymentPluginList) {
            PaymentMethodDO payment = map.get(plugin.getPluginId());
            PaymentPluginVO result = null;

            //数据库中已经有支付方式
            if (payment != null) {
                result = new PaymentPluginVO(payment);
            } else {
                result = PaymentVoConverter.toValidatorPlatformVO(plugin);
            }

            resultList.add(result);
        }

        return new WebPage(page, Integer.valueOf(resultList.size()).longValue(), pageSize, resultList);
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PaymentMethodDO add(PaymentPluginVO paymentMethod, String paymentPluginId) {

        //删除库中的插件
        Map delMap = new HashMap();
        //拼接插件id删除条件
        delMap.put("plugin_id", paymentPluginId);
        paymentMethodMapper.deleteByMap(delMap);

        PaymentPluginManager paymentPlugin = findPlugin(paymentPluginId);
        if (paymentPlugin == null) {
            throw new ServiceException(PaymentErrorCode.E501.code(), "插件id不正确");
        }

        paymentMethod.setMethodName(paymentPlugin.getPluginName());
        paymentMethod.setPluginId(paymentPluginId);

        // 配置信息
        List<ClientConfig> clients = paymentMethod.getEnableClient();
        Map<String, ClientConfig> map = new HashMap(16);
        for (ClientConfig client : clients) {

            String keyStr = client.getKey();
            keyStr = keyStr.replace("amp;", "");
            // 区分客户端 pc_config&wap_config
            String[] keys = keyStr.split("&");

            for (String key : keys) {
                map.put(key, client);
            }

        }

        //为了防止传值格式错误，以自定义格式为准
        List<ClientConfig> needClients = paymentPlugin.definitionClientConfig();
        Map jsonMap = new HashMap(16);
        for (ClientConfig clientConfig : needClients) {
            String keyStr = clientConfig.getKey();
            // 区分客户端 pc_config&wap_config
            String[] keys = keyStr.split("&");
            for (String key : keys) {
                // 传值来的对象
                ClientConfig client = map.get(key);
                if (client == null) {
                    throw new ServiceException(PaymentErrorCode.E501.code(), "缺少" + clientConfig.getName() + "相关配置");
                }
                //未开启
                Integer open = client.getIsOpen();
                clientConfig.setIsOpen(client.getIsOpen());
                if (open == 0) {
                    //未开启则不保存配置参数
                    jsonMap.put(StringUtil.lowerUpperCaseColumn(key), JsonUtil.objectToJson(clientConfig));
                    continue;
                }
                //传值来的配置参数
                List<PayConfigItem> list = client.getConfigList();
                if (list == null) {
                    throw new ServiceException(PaymentErrorCode.E501.code(), clientConfig.getName() + "的配置不能为空");
                }
                // 循环成key value 格式
                Map<String, String> valueMap = new HashMap<>(list.size());
                for (PayConfigItem item : list) {
                    valueMap.put(item.getName(), item.getValue());
                }
                // 配置参数设置
                List<PayConfigItem> configList = clientConfig.getConfigList();
                for (PayConfigItem item : configList) {
                    String value = valueMap.get(item.getName());
                    if (StringUtil.isEmpty(value)) {
                        throw new ServiceException(PaymentErrorCode.E501.code(), clientConfig.getName() + "的" + item.getText() + "必填");
                    }
                    item.setValue(value);
                }
                clientConfig.setConfigList(configList);

                jsonMap.put(StringUtil.lowerUpperCaseColumn(key), JsonUtil.objectToJson(clientConfig));
            }

        }


        PaymentMethodDO payment = new PaymentMethodDO();
        //复制配置信息
        BeanUtil.copyPropertiesInclude(jsonMap, payment);

        BeanUtil.copyProperties(paymentMethod, payment);

        paymentMethodMapper.insert(payment);

        List<String> keys = new ArrayList<>();
        for (ClientType clientType : ClientType.values()) {
            keys.add(CachePrefix.PAYMENT_CONFIG.getPrefix() + clientType.getDbColumn() + paymentPluginId);
        }

        //删除缓存
        cache.multiDel(keys);

        return payment;
    }


    @Override
    public PaymentPluginVO getByPlugin(String pluginId) {

        PaymentMethodDO paymentMethod = this.getByPluginId(pluginId);

        if (paymentMethod == null) {

            PaymentPluginManager plugin = findPlugin(pluginId);
            if (plugin == null) {
                throw new ServiceException(PaymentErrorCode.E501.code(), "支付方式不存在");
            }
            PaymentPluginVO payment = PaymentVoConverter.toValidatorPlatformVO(plugin);
            payment.setEnableClient(plugin.definitionClientConfig());
            return payment;
        } else {

            Map<String, ClientConfig> map = new HashMap(16);

            String pcConfig = paymentMethod.getPcConfig();
            if (pcConfig != null) {
                ClientConfig config = JsonUtil.jsonToObject(pcConfig, ClientConfig.class);
                map.put(config.getKey(), config);
            }

            String wapConfig = paymentMethod.getWapConfig();
            if (wapConfig != null) {
                ClientConfig config = JsonUtil.jsonToObject(wapConfig, ClientConfig.class);
                map.put(config.getKey(), config);
            }

            String appReactConfig = paymentMethod.getAppReactConfig();
            if (appReactConfig != null) {
                ClientConfig config = JsonUtil.jsonToObject(appReactConfig, ClientConfig.class);
                map.put(config.getKey(), config);
            }

            String appNativeConfig = paymentMethod.getAppNativeConfig();
            if (appNativeConfig != null) {
                ClientConfig config = JsonUtil.jsonToObject(appNativeConfig, ClientConfig.class);
                map.put(config.getKey(), config);
            }

            String miniConfig = paymentMethod.getMiniConfig();
            if (miniConfig != null) {
                ClientConfig config = JsonUtil.jsonToObject(miniConfig, ClientConfig.class);
                map.put(config.getKey(), config);
            }

            PaymentPluginVO pluginVO = new PaymentPluginVO();
            BeanUtil.copyProperties(paymentMethod, pluginVO);
            // 配置信息
            List<ClientConfig> clientConfigs = new ArrayList<>();
            for (String key : map.keySet()) {
                clientConfigs.add(map.get(key));
            }
            pluginVO.setEnableClient(clientConfigs);

            return pluginVO;
        }

    }


    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PaymentMethodDO edit(PaymentMethodDO paymentMethod, Long id) {

        PaymentMethodDO method = paymentMethodMapper.selectById(id);
        if (method == null) {
            throw new ServiceException(PaymentErrorCode.E501.code(), "支付方式不存在");
        }

        paymentMethod.setMethodId(id);
        paymentMethodMapper.updateById(paymentMethod);

        List<String> keys = new ArrayList<>();
        for (ClientType clientType : ClientType.values()) {
            keys.add(CachePrefix.PAYMENT_CONFIG.getPrefix() + clientType.getDbColumn() + id);
        }

        //删除缓存
        cache.multiDel(keys);

        return paymentMethod;
    }

    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Long id) {
        paymentMethodMapper.deleteById(id);

        List<String> keys = new ArrayList<>();
        for (ClientType clientType : ClientType.values()) {
            keys.add(CachePrefix.PAYMENT_CONFIG.getPrefix() + clientType.getDbColumn() + id);
        }
        // 删除缓存
        cache.multiDel(keys);
    }

    @Override
    public PaymentMethodDO getByPluginId(String pluginId) {

        if (pluginId == null) {
            return null;
        }

        return new QueryChainWrapper<>(paymentMethodMapper)
                //拼接插件id查询条件
                .eq("plugin_id", pluginId)
                //查询单个对象
                .one();
    }

    @Override
    public List<PaymentMethodVO> queryMethodByClient(String clientType) {

        List<PaymentMethodVO> resList = new ArrayList<>();

        //查询所有然后循环判断
        List<Map<String,Object>> list = paymentMethodMapper.selectMaps(null);
        if (list != null) {
            String column = ClientType.valueOf(clientType).getDbColumn();
            for (Map map : list) {
                if(map.get(column) == null){
                    continue;
                }
                String config = map.get(column).toString();
                JSONObject json = JSONObject.fromObject(config);
                String open = json.get("is_open").toString();
                if ("1".equals(open)) {

                    PaymentMethodVO payment = new PaymentMethodVO();
                    payment.setPluginId(map.get("plugin_id").toString());
                    payment.setMethodName(map.get("method_name").toString());
                    payment.setIsRetrace(Integer.parseInt(map.get("is_retrace").toString()));
                    if (map.get("image") != null) {
                        payment.setImage(map.get("image").toString());
                    }

                    resList.add(payment);
                }
            }
        }
        return resList;
    }

    /**
     * 获取配置
     *
     * @param clientType 支付类型
     * @return
     */
    @Override
    public Map<String, String> getConfig(String clientType, String paymentMethodId) {
        cache.remove(CachePrefix.PAYMENT_CONFIG.getPrefix() + clientType + paymentMethodId);
        String config = (String) cache.get(CachePrefix.PAYMENT_CONFIG.getPrefix() + clientType + paymentMethodId);

        System.out.println(CachePrefix.PAYMENT_CONFIG.getPrefix() + clientType + paymentMethodId);
        if (config == null) {
            config = paymentMethodMapper.selectClientType(clientType, paymentMethodId);
            cache.put(CachePrefix.PAYMENT_CONFIG.getPrefix() + clientType + paymentMethodId, config);
        }

        if (StringUtil.isEmpty(config)) {
            return new HashMap<>(16);
        }

        Map map = JsonUtil.jsonToObject(config, Map.class);
        List<Map> list = (List<Map>) map.get("config_list");
        if (!"1".equals(map.get("is_open").toString())) {
            throw new ServiceException(PaymentErrorCode.E502.code(), "支付方式未开启");
        }

        Map<String, String> result = new HashMap<>(list.size());
        if (list != null) {
            for (Map item : list) {
                result.put(item.get("name").toString(), item.get("value").toString());
            }
        }
        return result;
    }


    /**
     * 查找支付插件
     *
     * @param pluginId
     * @return
     */
    private PaymentPluginManager findPlugin(String pluginId) {
        for (PaymentPluginManager plugin : paymentPluginList) {
            if (plugin.getPluginId().equals(pluginId)) {
                return plugin;
            }
        }
        return null;
    }
}
