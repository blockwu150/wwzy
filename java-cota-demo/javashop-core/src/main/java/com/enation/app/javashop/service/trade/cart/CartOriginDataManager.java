package com.enation.app.javashop.service.trade.cart;

import com.enation.app.javashop.model.trade.cart.enums.CheckedWay;
import com.enation.app.javashop.model.trade.cart.vo.CartSkuOriginVo;

import java.util.List;

/**
 * 购物车原始数据业务类<br/>
 * 负责对购物车原始数据{@link CartSkuOriginVo}在缓存中的读写
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/11
 */
public interface CartOriginDataManager {


    /**
     * 由缓存中读取数据
     * @param checkedWay 表明调用该方法是CART,BUY_NOW还是TRADE,确定调用不同的购物缓存数据
     * @return 列表
     */
    List<CartSkuOriginVo> read(CheckedWay checkedWay);

    /**
     * 向缓存中写入数据
     * @param skuId 要写入的sku id
     * @param num 要加入购物车的数量
     * @param activityId 要参加的活动
     * @param promotionType 要参加的活动类型
     * @return 购物车原始数据
     */
    CartSkuOriginVo add(Long skuId, int num, Long activityId, String promotionType);

    /**
     * 向缓存中写入数据
     * @param skuId 要写入的sku id
     * @param num 要加入购物车的数量
     * @param activityId 要参加的活动
     * @return 购物车原始数据
     */
    CartSkuOriginVo addBuyNow(Long skuId, int num, Long activityId, String promotionType);

    /**
     * 立即购买
     * @param skuId 商品sku id
     * @param num 购买数量
     * @param activityId 活动id
     * @param promotionType 活动类型
     */
    void buy(Long skuId, Integer num, Long activityId, String promotionType);

    /**
     * 更新数量
     * @param skuId 要更新的sku id
     * @param num 要更新的数量
     */
    CartSkuOriginVo updateNum(Long skuId, int num);


    /**
     * 更新选中状态
     * @param skuId 要更新的sku id
     * @param checked 选中状态 1选中 0未选中
     * @return 购物车原始数据
     */
    CartSkuOriginVo checked(long skuId, int checked);


    /**
     * 更新某个店铺的所有商品的选中状态
     * @param sellerId 商家id
     * @param checked 选中状态 1选中 0未选中
     */
    void checkedSeller(long sellerId,int checked);


    /**
     * 更新全部的选中状态
     * @param checked 选中状态 1选中 0未选中
     * @param way 更新源，BUY_NOW,立即购买,CART,购物车
     *
     */
    void checkedAll(int checked, CheckedWay way);


    /**
     * 批量删除
     * @param skuIds skuid数组
     * @param way 删除源，BUY_NOW,立即购买,CART,购物车
     */
    void delete(Long[] skuIds, CheckedWay way);

    /**
     * 清空购物车
     */
    void clean();

    /**
     * 清除掉已经选中的商品
     * @param way 清除缓存源，BUY_BOW,立即购买，CART,购物车
     */
    void cleanChecked(CheckedWay way);


}
