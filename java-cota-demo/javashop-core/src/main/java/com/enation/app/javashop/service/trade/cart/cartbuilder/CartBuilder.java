package com.enation.app.javashop.service.trade.cart.cartbuilder;

import com.enation.app.javashop.model.trade.cart.enums.CheckedWay;
import com.enation.app.javashop.model.trade.cart.vo.CartView;
import com.enation.app.javashop.service.trade.cart.cartbuilder.impl.CartSkuFilter;

/**
 * 购物车构建器<br/>
 * 他的目标是要生产出一个{@link CartView}
 * 文档请参考：<br>
 * <a href="http://doc.javamall.com.cn/current/achitecture/jia-gou/ding-dan/cart-and-checkout.html" >购物车架构</a>
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/10
 */
public interface CartBuilder {

    /**
     * 渲染购物车的sku信息
     *
     * @param way 表明调用该方法是CART,BUY_NOW还是TRADE,确定调用不同的购物缓存数据
     * @return
     */
    CartBuilder renderSku(CheckedWay way);

    /**
     * 带过滤器的渲染sku
     *
     * @param filter
     * @param way 表明调用该方法是CART,BUY_NOW还是TRADE,确定调用不同的购物缓存数据
     * @return
     */
    CartBuilder renderSku(CartSkuFilter filter, CheckedWay way);

    /**
     * 渲染促销信息
     * @return builder
     */
    CartBuilder renderPromotion();



    /**
     * 计算购物车价格
     * @param includeCoupon
     * @return
     */
    CartBuilder countPrice(Boolean includeCoupon);


    /**
     * 计算运费
     *
     * @return builder
     */
    CartBuilder countShipPrice();


    /**
     * 计算优惠券费用
     *
     * @return builder
     */
    CartBuilder renderCoupon();

    /**
     * 检测数据正确性
     *
     * @return
     */
    CartBuilder checkData();

    /**
     * 构建购物车视图
     *
     * @return
     */
    CartView build();


}
