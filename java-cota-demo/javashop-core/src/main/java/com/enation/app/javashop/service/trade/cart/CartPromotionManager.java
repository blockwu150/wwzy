package com.enation.app.javashop.service.trade.cart;

import com.enation.app.javashop.model.member.dos.MemberCoupon;
import com.enation.app.javashop.model.trade.cart.vo.CartPromotionVo;
import com.enation.app.javashop.model.trade.cart.vo.CartVO;
import com.enation.app.javashop.model.trade.cart.vo.SelectedPromotionVo;

import java.util.List;

/**
 * 购物车优惠信息处理接口<br/>
 * 负责促销的使用、取消、读取。
 * 文档请参考：<br>
 * <a href="http://doc.javamall.com.cn/current/achitecture/jia-gou/ding-dan/cart-and-checkout.html" >购物车架构</a>
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/1
 */
public interface CartPromotionManager {

    /**
     * 由缓存中读取出用户选择的促销信息
     *
     * @return 用户选择的促销信息
     */
    SelectedPromotionVo getSelectedPromotion();



    /**
     * 使用一个促销活动
     *
     * @param sellerId 商家id
     * @param skuId 商品sku id
     * @param promotionVo 购物车中活动Vo
     */
    void usePromotion(Long sellerId, Long skuId, CartPromotionVo promotionVo);


    /**
     * 使用一个优惠券
     *
     * @param sellerId 商家id
     * @param mcId 优惠卷id
     * @param cartList 购物车集合
     * @param memberCoupon 会员优惠券
     */
    void useCoupon(Long sellerId, Long mcId, List<CartVO> cartList,MemberCoupon memberCoupon);

    /**
     * 检测一个优惠券
     *
     * @param sellerId 商家id
     * @param mcId 优惠卷id
     * @param cartList 购物车集合
     */
    MemberCoupon detectCoupon(Long sellerId, Long mcId, List<CartVO> cartList);

    /**
     * 删除一个店铺优惠券的使用
     *
     * @param sellerId 商家id
     */
    void deleteCoupon(Long sellerId);

    /**
     * 清除所有的优惠券
     */
    void cleanCoupon();

    /**
     * 批量删除sku对应的优惠活动
     *
     * @param skuids sku id数组
     */
    void delete(Long[] skuids);

    /**
     * 根据sku检查并清除无效的优惠活动
     *
     * @param skuId 商品sku id
     * @param promotionId 活动id
     * @param sellerId 商家id
     * @param promotionType 活动类型
     * @return
     */
    boolean checkPromotionInvalid(Long skuId, Long promotionId, Long sellerId, String promotionType);


    /**
     * 清空当前用户的所有优惠活动
     */
    void clean();

    /**
     * 检测并清除无效活动
     */
    void checkPromotionInvalid();
}
