package com.enation.app.javashop.service.trade.order.impl;

import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.client.member.MemberAddressClient;
import com.enation.app.javashop.client.system.RegionsClient;
import com.enation.app.javashop.model.member.dos.MemberAddress;
import com.enation.app.javashop.model.member.dos.ReceiptHistory;
import com.enation.app.javashop.model.system.dos.Regions;
import com.enation.app.javashop.model.trade.order.enums.PaymentTypeEnum;
import com.enation.app.javashop.model.trade.order.vo.CheckoutParamVO;
import com.enation.app.javashop.service.trade.order.CheckoutParamManager;
import com.enation.app.javashop.model.trade.order.support.CheckoutParamName;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.security.model.Buyer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算参数 业务层实现类
 *
 * @author Snow create in 2018/4/8
 * @version v2.0
 * @since v7.0.0
 */
@Service
public class CheckoutParamManagerImpl implements CheckoutParamManager {

    @Autowired
    private Cache cache;

    @Autowired
    private MemberAddressClient memberAddressClient;

    @Autowired
    private RegionsClient regionsClient;


    /**
     * 获取订单的创建参数<br>
     * 如果没有设置过参数，则用默认
     * @return 结算参数
     */
    @Override
    public CheckoutParamVO getParam() {
        //由Reids缓存中读取出结算参数
        CheckoutParamVO param = this.read();

        //如果缓存中没有 new一个，并赋给默认值
        if (param == null) {

            param = new CheckoutParamVO();
            Buyer buyer = UserContext.getBuyer();

            MemberAddress address = this.memberAddressClient.getDefaultAddress(buyer.getUid());
            Long addrId = 0L;
            if (address != null) {
                addrId = address.getAddrId();
            }
            //默认配送地址
            param.setAddressId(addrId);

            //默认支付方式
            param.setPaymentType(PaymentTypeEnum.defaultType());

            //默认不需要发票
            ReceiptHistory receipt = new ReceiptHistory();
            param.setReceipt(receipt);

            //默认时间
            param.setReceiveTime("任意时间");

            //将结算参数写入缓存
            this.write(param);
        }

        return param;
    }

    /**
     * 设置收货地址id
     * @param addressId 收货地址id
     */
    @Override
    public void setAddressId(Long addressId) {
        //缓存中读取结算参数
        String key = getRedisKey();
        Map<String, Object> map = this.cache.getHash(key);
        //获取支付类型
        PaymentTypeEnum paymentType = (PaymentTypeEnum) map.get(CheckoutParamName.PAYMENT_TYPE);
        //验证一下是否支持货到付款
        this.checkCod(paymentType,addressId);

        this.cache.putHash(getRedisKey(), CheckoutParamName.ADDRESS_ID, addressId);
    }

    /**
     * 设置支付方式
     * @param paymentTypeEnum 支付方式{@link PaymentTypeEnum}
     */
    @Override
    public void setPaymentType(PaymentTypeEnum paymentTypeEnum) {
        this.cache.putHash(getRedisKey(), CheckoutParamName.PAYMENT_TYPE, paymentTypeEnum);
    }

    /**
     * 设置发票
     * @param receipt 发票信息 {@link  ReceiptHistory }
     */
    @Override
    public void setReceipt(ReceiptHistory receipt) {
        this.cache.putHash(getRedisKey(), CheckoutParamName.RECEIPT, receipt);
    }

    /**
     * 设置送货时间
     * @param receiveTime 送货时间
     */
    @Override
    public void setReceiveTime(String receiveTime) {
        this.cache.putHash(getRedisKey(), CheckoutParamName.RECEIVE_TIME, receiveTime);
    }

    /**
     * 设置订单备注
     * @param remark 订单备注
     */
    @Override
    public void setRemark(String remark) {
        this.cache.putHash(getRedisKey(), CheckoutParamName.REMARK, remark);
    }

    /**
     * 设置订单来源
     * @param clientType 客户端来源
     */
    @Override
    public void setClientType(String clientType) {
        this.cache.putHash(getRedisKey(), CheckoutParamName.CLIENT_TYPE, clientType);
    }

    /**
     * 取消发票
     */
    @Override
    public void deleteReceipt() {
        this.cache.putHash(getRedisKey(), CheckoutParamName.RECEIPT, null);
    }

    /**
     * 批量设置所有参数
     * @param paramVO 结算参数 {@link CheckoutParamVO}
     */
    @Override
    public void setAll(CheckoutParamVO paramVO) {
        this.write(paramVO);
    }

    /**
     * 检测是否支持货到付款
     * @param paymentTypeEnum 支付方式
     * @param addressId 会员收货地址id
     */
    @Override
    public void checkCod(PaymentTypeEnum paymentTypeEnum, Long addressId) {
        //如果支付方式不是货到付款，则不需要检测
        if(!PaymentTypeEnum.COD.equals(paymentTypeEnum)){
            return ;
        }

        //如果传入的会员收货地址id是null，则去缓存中读取会员收货地址
        if(addressId == null){
            CheckoutParamVO paramVO = this.getParam();
            addressId = paramVO.getAddressId();
        }

        //获取会员地址
        MemberAddress memberAddress = this.memberAddressClient.getModel(addressId);

        if(memberAddress == null){
            return;
        }

        List<Long> addIds = new ArrayList<>();
        addIds.add(memberAddress.getProvinceId());
        addIds.add(memberAddress.getCityId());
        addIds.add(memberAddress.getCountyId());
        addIds.add(memberAddress.getTownId());

        //判断会员收货地址所在省份，城市，县区，城镇是否支付货到付款，若有一个不支持则抛异常
        for (Long region: addIds) {
            Regions regions = this.regionsClient.getModel(region);
            if(regions == null){
                continue;
            }
            if(regions.getCod() == 0){
                throw new NoPermissionException("["+regions.getLocalName() + "]不支持货到付款");
            }
        }


    }


    /**
     * 读取结算参数缓存Key
     * @return 结算参数缓存Key+会员id
     */
    private String getRedisKey() {
        Buyer buyer = UserContext.getBuyer();
        return CachePrefix.CHECKOUT_PARAM_ID_PREFIX.getPrefix() + buyer.getUid();
    }


    /**
     * 将结算参数写入缓存
     * @param paramVO 结算参数
     */
    private void write(CheckoutParamVO paramVO) {
        String redisKey = getRedisKey();
        Map map = new HashMap<>(4);

        //设置收货地址
        if (paramVO.getAddressId() != null) {
            map.put(CheckoutParamName.ADDRESS_ID, paramVO.getAddressId());
        }
        //设置配送时间
        if (paramVO.getReceiveTime() != null) {
            map.put(CheckoutParamName.RECEIVE_TIME, paramVO.getReceiveTime());
        }
        //设置支付类型
        if (paramVO.getPaymentType() != null) {
            map.put(CheckoutParamName.PAYMENT_TYPE, paramVO.getPaymentType());
        }
        //设置发票
        if (paramVO.getReceipt() != null) {
            map.put(CheckoutParamName.RECEIPT, paramVO.getReceipt());
        }
        //设置备注
        if (paramVO.getRemark() != null) {
            map.put(CheckoutParamName.REMARK, paramVO.getRemark());
        }
        //设置客户端类型
        if (paramVO.getClientType() != null) {
            map.put(CheckoutParamName.CLIENT_TYPE, paramVO.getClientType());
        }

        this.cache.putAllHash(redisKey, map);
    }


    /**
     * 由Reids中读取出结算参数
     */
    private CheckoutParamVO read() {
        String key = getRedisKey();
        Map<String, Object> map = this.cache.getHash(key);

        //如果还没有存过则返回null
        if (map == null || map.isEmpty()) {
            return null;
        }

        //获取结算参数
        Long addressId = (Long) map.get(CheckoutParamName.ADDRESS_ID);
        PaymentTypeEnum paymentType = (PaymentTypeEnum) map.get(CheckoutParamName.PAYMENT_TYPE);
        ReceiptHistory receipt = (ReceiptHistory) map.get(CheckoutParamName.RECEIPT);
        String receiveTime = (String) map.get(CheckoutParamName.RECEIVE_TIME);
        String remark = (String) map.get(CheckoutParamName.REMARK);
        String clientType = (String) map.get(CheckoutParamName.CLIENT_TYPE);


        //将结算参数封装到CheckoutParamVO返回
        CheckoutParamVO param = new CheckoutParamVO();

        param.setAddressId(addressId);
        param.setReceipt(receipt);
        if (receiveTime == null) {
            receiveTime = "任意时间";
        }
        param.setReceiveTime(receiveTime);
        param.setRemark(remark);
        if (paymentType == null) {
            paymentType = PaymentTypeEnum.defaultType();
        }

        param.setPaymentType(paymentType);
        param.setClientType(clientType);
        return param;
    }

}
