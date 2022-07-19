package com.enation.app.javashop.service.trade.pintuan.impl;

import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.client.goods.GoodsClient;
import com.enation.app.javashop.model.goods.vo.GoodsSkuVO;
import com.enation.app.javashop.service.trade.pintuan.PintuanCartManager;
import com.enation.app.javashop.model.errorcode.TradeErrorCode;
import com.enation.app.javashop.model.trade.cart.enums.CartType;
import com.enation.app.javashop.model.trade.cart.enums.CheckedWay;
import com.enation.app.javashop.model.trade.cart.vo.CartSkuOriginVo;
import com.enation.app.javashop.model.trade.cart.vo.CartView;
import com.enation.app.javashop.service.trade.cart.cartbuilder.*;
import com.enation.app.javashop.service.trade.cart.cartbuilder.impl.DefaultCartBuilder;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by kingapex on 2019-01-23.
 * 拼团购物车业务类实现
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-01-23
 */
@Service
public class PintuanCartManagerImpl implements PintuanCartManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 购物车促销渲染器
     */
    @Autowired
    @Qualifier(value = "cartPromotionRendererImpl")
    private CartPromotionRenderer cartPromotionRuleRenderer;

    /**
     * 购物车价格计算器
     */
    @Autowired
    @Qualifier(value = "pintuanCartPriceCalculatorImpl")
    private CartPriceCalculator cartPriceCalculator;

    /**
     * 拼团购物车sku数据渲染器
     */
    @Autowired
    @Qualifier(value = "pintuanCartSkuRenderer")
    private CartSkuRenderer pintuanCartSkuRenderer;

    /**
     * 数据校验
     */
    @Autowired
    private CheckDataRebderer checkDataRebderer;

    /**
     * 购物车优惠券渲染器
     */
    @Autowired
    private CartCouponRenderer cartCouponRenderer;

    /**
     * 购物车运费价格计算器
     */
    @Autowired
    private CartShipPriceCalculator cartShipPriceCalculator;


    @Autowired
    private GoodsClient goodsClient;


    @Autowired
    private Cache cache;


    /**
     * 获取拼团购物车
     * @return 购物车视图
     */
    @Override
    public CartView getCart() {
        //调用CartView生产流程线进行生产
        CartBuilder cartBuilder = new DefaultCartBuilder(CartType.PINTUAN, pintuanCartSkuRenderer, cartPromotionRuleRenderer, cartPriceCalculator, cartCouponRenderer, cartShipPriceCalculator, checkDataRebderer);

        //生产流程为：渲染sku->渲染促销规则(计算优惠券）->计算运费->计算价格 -> 渲染优惠券 ->生成成品
        CartView cartView = cartBuilder.renderSku(CheckedWay.CART).countShipPrice().countPrice(false).checkData().build();
        logger.debug("cartView:"+cartView);
        return cartView;
    }

    /**
     * 将一个拼团的sku加入到购物车中
     * @param skuId 商品sku id
     * @param num 加入的数量
     * @return 购物车原始数据
     */
    @SuppressWarnings("Duplicates")
    @Override
    public CartSkuOriginVo addSku(Long skuId, Integer num) {
        CartSkuOriginVo skuVo = new CartSkuOriginVo();
        GoodsSkuVO sku = this.goodsClient.getSkuFromCache(skuId);
        if (sku == null) {
            throw new ServiceException(TradeErrorCode.E451.code(), "商品已失效，请刷新购物车");
        }
        if(num<=0){
            throw new ServiceException(TradeErrorCode.E451.code(), "商品数量不能小于等于0。");
        }

        //读取sku的可用库存
        Integer enableQuantity = sku.getEnableQuantity();
        if (enableQuantity <= 0) {
            throw new ServiceException(TradeErrorCode.E451.code(), "商品库存已不足，不能购买。");
        }

        BeanUtils.copyProperties(sku, skuVo);
        skuVo.setNum(num);
        skuVo.setChecked(1);

        String originKey = this.getOriginKey();

        cache.put(originKey, skuVo);
        logger.debug("将拼团商品加入缓存：" + skuVo);

        return skuVo;
    }

    /**
     * 读取当前会员购物车原始数据key
     *
     * @return
     */
    @SuppressWarnings("Duplicates")
    protected String getOriginKey() {

        String cacheKey = "";
        //如果会员登录了，则要以会员id为key
        Buyer buyer = UserContext.getBuyer();
        if (buyer != null) {
            cacheKey = CachePrefix.CART_SKU_PREFIX.getPrefix() + buyer.getUid();
        }

        return cacheKey;
    }
}
