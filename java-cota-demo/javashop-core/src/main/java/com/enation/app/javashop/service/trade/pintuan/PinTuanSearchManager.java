package com.enation.app.javashop.service.trade.pintuan;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.promotion.pintuan.PinTuanGoodsVO;
import com.enation.app.javashop.model.promotion.pintuan.PtGoodsDoc;

import java.util.List;

/**
 * Created by kingapex on 2019-01-21.
 * 拼团搜索业务接口
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-01-21
 */
public interface PinTuanSearchManager {

    /**
     * 搜索拼团商品
     * @param categoryId 商品分类id
     * @param pageNo 页码
     * @param pageSize 每页显示条数
     * @return 拼团商品集合
     */
    List<PtGoodsDoc> search(Long categoryId, Integer pageNo, Integer pageSize);


    /**
     * 向es写入索引
     * @param goodsDoc 拼团商品
     */
    void addIndex(PtGoodsDoc goodsDoc );

    /**
     * 向es写入索引
     * @param pintuanGoods 拼团商品
     * @return  是否生成成功
     */
    boolean addIndex(PinTuanGoodsVO pintuanGoods);

    /**
     * 删除一个sku的索引
     * @param skuId 商品sku id
     */
    void delIndex(Long skuId);


    /**
     * 删除某个商品的所有的索引
     * @param goodsId 商品id
     */
    void deleteByGoodsId(Long goodsId);


    /**
     * 删除某个拼团的所有索引
     * @param pinTuanId 拼团id
     */
    void deleteByPintuanId(Long pinTuanId);

    /**
     * 根据拼团id同步es中的拼团商品<br/>
     * 当拼团活动商品发生变化时调用此方法
     * @param pinTuanId 拼团活动id
     */
    void syncIndexByPinTuanId(Long pinTuanId);

    /**
     * 根据商品id同步es中的拼团商品<br>
     * @param goodsId 商品id
     */
    void syncIndexByGoodsId(Long goodsId);

    /**
     * 查询分页的拼团商品
     * @param categoryId 商品分类id
     * @param pageNo 页码
     * @param pageSize 每页显示条数
     * @return 拼团商品分页数据
     */
    WebPage searchPage(Long categoryId, Integer pageNo, Integer pageSize);
}
