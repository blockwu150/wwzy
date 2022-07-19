package com.enation.app.javashop.service.promotion.coupon;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.promotion.coupon.dos.CouponDO;
import com.enation.app.javashop.model.promotion.coupon.dto.CouponParams;
import com.enation.app.javashop.model.promotion.coupon.vo.GoodsCouponVO;

import java.util.List;

/**
 * 优惠券业务层
 *
 * @author Snow
 * @version v2.0
 * @since v7.0.0
 * 2018-04-17 23:19:39
 */
public interface CouponManager {

    /**
     * 查询优惠券分页数据列表
     * @param params 查询参数
     * @return WebPage
     */
    WebPage list(CouponParams params);

    /**
     * 读取商品可用优惠券
     * @param goodsId 商品ID
     * @return
     */
    List<GoodsCouponVO> getListByGoods(Long goodsId);

    /**
     * 读取商家可用优惠券
     * @param sellerId 商家ID
     * @return
     */
    List<CouponDO> getList(Long sellerId);

    /**
     * 添加优惠券信息
     * @param coupon 优惠券信息
     * @return Coupon 优惠券信息
     */
    CouponDO add(CouponDO coupon);

    /**
     * 修改优惠券信息
     * @param coupon 优惠券信息
     * @param id     优惠券主键ID
     * @return Coupon 优惠券信息
     */
    CouponDO edit(CouponDO coupon, Long id);

    /**
     * 删除优惠券信息
     * @param id 优惠券主键ID
     */
    void delete(Long id);

    /**
     * 获取优惠券信息
     * @param id 优惠券主键ID
     * @return Coupon 优惠券信息
     */
    CouponDO getModel(Long id);

    /**
     * 验证操作权限<br/>
     * 如有问题直接抛出权限异常
     * @param id 优惠券主键ID
     */
    void verifyAuth(Long id);

    /**
     * 增加优惠券已使用数量
     * @param id 优惠券主键ID
     */
    void addUsedNum(Long id);

    /**
     * 增加优惠券被领取数量
     * @param couponId 优惠券主键ID
     */
    void addReceivedNum(Long couponId);

    /**
     * 查询商家所有优惠券分页数据列表
     * @param page     页码
     * @param pageSize 每页数量
     * @param sellerId 商家id
     * @return WebPage
     */
    WebPage all(long page, long pageSize, Long sellerId);

    /**
     * 修改优惠券所属的店铺名称
     * 当商家修改店铺名称时执行的操作
     * @param shopId   店铺id
     * @param shopName 店铺名称
     */
    void editCouponShopName(Long shopId, String shopName);

    /**
     * 根据失效状态获取优惠券数据集合
     * @param status 失效状态 0：全部，1：有效，2：失效
     * @return
     */
    List<CouponDO> getByStatus(Integer status);
}
