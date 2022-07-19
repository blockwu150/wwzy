package com.enation.app.javashop.service.statistics.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.enation.app.javashop.client.goods.GoodsClient;
import com.enation.app.javashop.client.member.MemberCollectionGoodsClient;
import com.enation.app.javashop.mapper.statistics.GoodsDataMapper;
import com.enation.app.javashop.model.goods.vo.CacheGoods;
import com.enation.app.javashop.model.statistics.dto.GoodsData;
import com.enation.app.javashop.service.statistics.GoodsDataManager;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 商品数据收集
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/4 9:54
 */
@Service
public class GoodsDataManagerImpl implements GoodsDataManager {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GoodsDataMapper goodsDataMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private MemberCollectionGoodsClient memberCollectionGoodsClient;


    /**
     * 新增商品
     *
     * @param goodsIds 商品id
     */
    @Override
    public void addGoods(Long[] goodsIds) {
        for (Long goodsId : goodsIds) {
            //缓存中查询商品
            CacheGoods cacheGoods = goodsClient.getFromCache(goodsId);
            //转换为统计库商品数据
            GoodsData gd = new GoodsData(cacheGoods);
            //初始收藏数量为0
            gd.setFavoriteNum(0);
            //设置商品分类路径
            try {
                gd.setCategoryPath(goodsClient.getCategory(gd.getCategoryId()).getCategoryPath());
            } catch (Exception e) {
                this.logger.error("错误的商品分类id：" + gd.getCategoryId());
                gd.setCategoryPath("");
            }
            goodsDataMapper.insert(gd);
        }
    }

    /**
     * 私有方法，新增商品
     *
     * @param cacheGoods 新增商品
     */
    private void saveGoods(CacheGoods cacheGoods) {
        GoodsData gd = new GoodsData(cacheGoods);
        //设置收藏数量
        gd.setFavoriteNum(memberCollectionGoodsClient.getGoodsCollectCount(gd.getGoodsId()));
        //设置商品分类路径
        gd.setCategoryPath(goodsClient.getCategory(gd.getCategoryId()).getCategoryPath());
        goodsDataMapper.insert(gd);
    }

    /**
     * 修改商品
     *
     * @param goodsIds 商品id
     */
    @Override
    public void updateGoods(Long[] goodsIds) {

        for (Long goodsId : goodsIds) {
            CacheGoods cacheGoods = goodsClient.getFromCache(goodsId);
            //按商品id查询
            GoodsData gd = new QueryChainWrapper<>(goodsDataMapper)
                    .eq("goods_id", goodsId)
                    .one();
            if (gd == null) {
                this.saveGoods(cacheGoods);
            } else {
                gd = new GoodsData(cacheGoods, gd);
                //设置收藏数量
                gd.setFavoriteNum(memberCollectionGoodsClient.getGoodsCollectCount(gd.getGoodsId()));
                //设置商品分类路径
                gd.setCategoryPath(goodsClient.getCategory(gd.getCategoryId()).getCategoryPath());

                goodsDataMapper.updateById(gd);
            }
        }

    }

    /**
     * 删除商品
     *
     * @param goodsIds 商品id
     */
    @Override
    public void deleteGoods(Long[] goodsIds) {
        //按商品id批量删除
        QueryWrapper<GoodsData> delWrapper = new QueryWrapper<GoodsData>()
                .in("goods_id", goodsIds);

        goodsDataMapper.delete(delWrapper);
    }


    /**
     * 修改商品收藏数量
     * @param goodsData 商品统计数据
     */
    @Override
    public void updateCollection(GoodsData goodsData) {
        new UpdateChainWrapper<>(goodsDataMapper)
                //设置商品收藏数量
                .set("favorite_num", goodsData.getFavoriteNum())
                //按商品id修改
                .eq("goods_id", goodsData.getGoodsId())
                //提交修改
                .update();
    }

    /**
     * 获取商品
     * @param goodsId 商品id
     * @return 商品统计数据
     */
    @Override
    public GoodsData get(Long goodsId) {

        //按商品id查询
        return new QueryChainWrapper<>(goodsDataMapper)
                .eq("goods_id", goodsId)
                .one();
    }

    /**
     * 下架所有商品
     * @param sellerId 商家id
     */
    @Override
    public void underAllGoods(Long sellerId) {

        new UpdateChainWrapper<>(goodsDataMapper)
                //设置状态为下架
                .set("market_enable", 0)
                //按卖家id修改
                .eq("seller_id", sellerId)
                //提交修改
                .update();
    }
}
