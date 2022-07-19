package com.enation.app.javashop.service.trade.cart.impl;

import com.enation.app.javashop.model.trade.cart.enums.CartType;
import com.enation.app.javashop.model.trade.cart.enums.CheckedWay;
import com.enation.app.javashop.model.trade.cart.vo.CartSkuVO;
import com.enation.app.javashop.model.trade.cart.vo.CartVO;
import com.enation.app.javashop.model.trade.cart.vo.CartView;
import com.enation.app.javashop.service.trade.cart.CartReadManager;
import com.enation.app.javashop.service.trade.cart.cartbuilder.*;
import com.enation.app.javashop.service.trade.cart.cartbuilder.impl.DefaultCartBuilder;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车只读操作业务类
 *
 * @author Snow create in 2018/3/21
 * @version v2.0 by kingapex
 * 此处通过建造者模式来完成，具体架构文档请参考：
 * http://doc.javamall.com.cn/current/achitecture/jia-gou/ding-dan/cart-and-checkout.html
 * @since v7.0.0
 */
@Service
public class CartReadManagerImpl implements CartReadManager {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 购物车促销渲染器
     */
    @Autowired
    private CartPromotionRenderer cartPromotionRenderer;

    /**
     * 购物车价格计算器
     */
    @Autowired
    private CartPriceCalculator cartPriceCalculator;

    /**
     * 购物车sku数据渲染器
     */
    @Autowired
    @Qualifier(value = "cartSkuRendererImpl")
    private CartSkuRenderer cartSkuRenderer;

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


    /**
     * 读取购物车数据，并计算优惠和价格
     * @param way 获取方式
     * @return 购物车构建器最终要构建的数据
     */
    @Override
    public CartView getCartListAndCountPrice(CheckedWay way) {

        //调用CartView生产流程线进行生产
        CartBuilder cartBuilder = new DefaultCartBuilder(CartType.CART, cartSkuRenderer, cartPromotionRenderer, cartPriceCalculator, checkDataRebderer);

        /**
         * 生产流程：渲染sku->校验sku是否有效->渲染促销规则->计算价格->生成成品
         * 生产流程说明 ： 校验sku是否有效必须放在渲染促销规则之前，如果参加满减活动的商品失效，那么满减活动无需渲染
         * update by liuyulei 2019-05-17
         */
        CartView cartView = cartBuilder.renderSku(way).checkData().renderPromotion().countPrice(false).build();
        List<CartVO> itemList = cartView.getCartList();
        processChecked(itemList);
        return cartView;
    }


    /**
     * 处理购物车的选中情况
     * 根据每个商品的选中情况来 设置店铺是否全选
     *
     * @param itemList
     */
    private void processChecked(List<CartVO> itemList) {
        for (CartVO cartVO : itemList) {
            //设置默认为店铺商品全选
            cartVO.setChecked(1);
            cartVO.setInvalid(0);

            //如果购物车有一个有效的商品
            Boolean notInvalid = false;

            for (CartSkuVO skuVO : cartVO.getSkuList()) {
                // 如果商品没有选中 并且他不是一个有效的商品
                if (skuVO.getChecked() == 0 && skuVO.getInvalid() == 0) {
                    cartVO.setChecked(0);
                }
                if (skuVO.getInvalid() == 0) {
                    notInvalid = true;
                }
            }

            //如果 所有商品都无效 && 购物车状态为以选中
            if (!notInvalid && cartVO.getChecked() == 1) {
                cartVO.setInvalid(1);
            }
            this.logger.info("购物车选中处理结果===》：");
            this.logger.info(cartVO.toString());
        }
    }

    /**
     * 由缓存中取出已勾选的购物列表<br>
     * @param way 获取方式
     * @return 购物车构建器最终要构建的数据
     */
    @Override
    public CartView getCheckedItems(CheckedWay way) {

        //调用CartView生产流程线进行生产
        CartBuilder cartBuilder = new DefaultCartBuilder(CartType.CHECKOUT, cartSkuRenderer, cartPromotionRenderer, cartPriceCalculator, cartCouponRenderer, cartShipPriceCalculator,checkDataRebderer);

        /**
         * 生产流程：渲染sku->校验sku是否有效->渲染促销规则(计算优惠券）->计算运费->计算价格 -> 渲染优惠券 ->生成成品
         * 生产流程说明 ： 校验sku是否有效必须放在渲染促销规则之前，如果参加满减活动的商品失效，那么满减活动无需渲染
         * update by liuyulei 2019-05-17
         */
        CartView cartView = cartBuilder.renderSku(way).checkData().renderPromotion().countShipPrice().countPrice(true).renderCoupon().build();
        List<CartVO> cartList = cartView.getCartList();


        //无效的购物车清
        List<CartVO> invalidCart = new ArrayList<>();

        for (CartVO cart : cartList) {
            List<CartSkuVO> skuList = cart.getSkuList();
            List<CartSkuVO> newSkuList = new ArrayList<CartSkuVO>();

            for (CartSkuVO skuVO : skuList) {
                //只将选中的压入
                if (skuVO.getChecked() == 1) {
                    newSkuList.add(skuVO);
                }
            }
            cart.setSkuList(newSkuList);

            if (newSkuList.size() == 0) {
                invalidCart.add(cart);
            }
        }
        //去除没有可以购买商品的购物车
        for (CartVO cart : invalidCart) {
            cartList.remove(cart);
        }

        return cartView;

    }


}
