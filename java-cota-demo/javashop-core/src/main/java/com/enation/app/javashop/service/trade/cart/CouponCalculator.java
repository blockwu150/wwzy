package com.enation.app.javashop.service.trade.cart;

import com.enation.app.javashop.model.member.dos.MemberCoupon;
import com.enation.app.javashop.model.trade.cart.vo.CartSkuVO;

/**
 * 优惠券计算器
 *
 * @author fk
 * @version v2.0
 * @since v7.1.5
 * 2019-09-17 23:19:39
 */
public interface CouponCalculator {

    /**
     * 计算商符合条件商品的总金额
     * @param coupon 会员优惠券
     * @param sku 购物车中的产品
     * @return 符合条件商品的总金额
     */
    Double calculate(MemberCoupon coupon, CartSkuVO sku);

}
