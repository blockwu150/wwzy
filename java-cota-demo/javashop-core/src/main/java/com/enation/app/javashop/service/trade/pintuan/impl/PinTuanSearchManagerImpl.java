package com.enation.app.javashop.service.trade.pintuan.impl;

import com.enation.app.javashop.client.goods.GoodsClient;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.elasticsearch.EsConfig;
import com.enation.app.javashop.framework.elasticsearch.EsSettings;
import com.enation.app.javashop.framework.logs.Debugger;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.HexUtils;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.mapper.trade.pintuan.PintuanGoodsMapper;
import com.enation.app.javashop.model.goods.SkuNameUtil;
import com.enation.app.javashop.model.goods.dos.CategoryDO;
import com.enation.app.javashop.model.goods.vo.CacheGoods;
import com.enation.app.javashop.model.goods.vo.GoodsSkuVO;
import com.enation.app.javashop.model.promotion.pintuan.PinTuanGoodsVO;
import com.enation.app.javashop.model.promotion.pintuan.PtGoodsDoc;
import com.enation.app.javashop.service.trade.pintuan.PinTuanSearchManager;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingapex on 2019-01-21.
 * 拼团搜索业务实现者
 *
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-01-21
 */

@Service
public class PinTuanSearchManagerImpl implements PinTuanSearchManager {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private EsConfig esConfig;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private PintuanGoodsMapper pintuanGoodsMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PinTuanSearchManagerImpl() {
    }

    public PinTuanSearchManagerImpl(ElasticsearchTemplate _elasticsearchTemplate, EsConfig _esConfig) {
        this.elasticsearchTemplate = _elasticsearchTemplate;
        this.esConfig = _esConfig;

    }

    /**
     * 搜索拼团商品
     * @param categoryId 商品分类id
     * @param pageNo 页码
     * @param pageSize 每页显示条数
     * @return 拼团商品集合
     */
    @SuppressWarnings("Duplicates")
    @Override
    public List<PtGoodsDoc> search(Long categoryId, Integer pageNo, Integer pageSize) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 分类检索
        if (categoryId != null) {

            CategoryDO category = goodsClient.getCategory(categoryId);
            if (category != null) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("categoryPath", HexUtils.encode(category.getCategoryPath()) + "*"));
            }

        }
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        String indexName = esConfig.getIndexName() + "_" + EsSettings.PINTUAN_INDEX_NAME;
        searchQuery.withIndices(indexName).withTypes(EsSettings.PINTUAN_TYPE_NAME).withPageable(pageable).withQuery(boolQueryBuilder);

        List<PtGoodsDoc> resultlist = elasticsearchTemplate.queryForList(searchQuery.build(), PtGoodsDoc.class);
        //新的结果集合
        List<PtGoodsDoc> newResultlist = new ArrayList<>();
        for (PtGoodsDoc goodsDoc : resultlist) {
            GoodsSkuVO skuVO = goodsClient.getSkuFromCache(goodsDoc.getSkuId());
            if (skuVO != null) {
                if (skuVO.getEnableQuantity() > 0) {
                    goodsDoc.setIsEnableSale(true);
                } else {
                    goodsDoc.setIsEnableSale(false);
                }
                newResultlist.add(goodsDoc);
            }
        }
        return newResultlist;
    }

    /**
     * 向es写入索引
     * @param goodsDoc 拼团商品
     */
    @Override
    public void addIndex(PtGoodsDoc goodsDoc) {

        //对cat path特殊处理
        CategoryDO categoryDO = goodsClient.getCategory(goodsDoc.getCategoryId());
        goodsDoc.setCategoryPath(HexUtils.encode(categoryDO.getCategoryPath()));

        IndexQuery indexQuery = new IndexQuery();
        String indexName = esConfig.getIndexName() + "_" + EsSettings.PINTUAN_INDEX_NAME;
        indexQuery.setIndexName(indexName);
        indexQuery.setType("pintuan_goods");
        indexQuery.setId(goodsDoc.getSkuId().toString());
        indexQuery.setObject(goodsDoc);

        elasticsearchTemplate.index(indexQuery);
        logger.debug("将拼团商品将拼团商品ID[" + goodsDoc.getGoodsId() + "] " + goodsDoc + " 写入索引");
    }

    @Autowired
    private Debugger debugger;

    /**
     * 向es写入索引
     * @param pintuanGoods 拼团商品
     * @return  是否生成成功
     */
    @Override
    public boolean addIndex(PinTuanGoodsVO pintuanGoods) {

        String goodsName = pintuanGoods.getGoodsName() + " " + SkuNameUtil.createSkuName(pintuanGoods.getSpecs());

        try {

            CacheGoods cacheGoods = goodsClient.getFromCache(pintuanGoods.getGoodsId());
            PtGoodsDoc ptGoodsDoc = new PtGoodsDoc();
            ptGoodsDoc.setCategoryId(cacheGoods.getCategoryId());
            ptGoodsDoc.setThumbnail(pintuanGoods.getThumbnail());
            ptGoodsDoc.setSalesPrice(pintuanGoods.getSalesPrice());
            ptGoodsDoc.setOriginPrice(pintuanGoods.getPrice());
            ptGoodsDoc.setGoodsName(goodsName);
            ptGoodsDoc.setBuyCount(pintuanGoods.getSoldQuantity());
            ptGoodsDoc.setGoodsId(pintuanGoods.getGoodsId());
            ptGoodsDoc.setSkuId(pintuanGoods.getSkuId());
            ptGoodsDoc.setPinTuanId(pintuanGoods.getPintuanId());

            this.addIndex(ptGoodsDoc);

            return true;
        } catch (Exception e) {
            logger.error("为拼团商品[" + goodsName + "]生成索引报错", e);
            debugger.log("为拼团商品[" + goodsName + "]生成索引报错", StringUtil.getStackTrace(e));
            return false;
        }


    }

    /**
     * 删除一个sku的索引
     * @param skuId 商品sku id
     */
    @Override
    public void delIndex(Long skuId) {
        String indexName = esConfig.getIndexName() + "_" + EsSettings.PINTUAN_INDEX_NAME;

        elasticsearchTemplate.delete(indexName, EsSettings.PINTUAN_TYPE_NAME, "" + skuId);

        logger.debug("将拼团商品ID[" + skuId + "]删除索引");
    }

    /**
     * 删除某个商品的所有的索引
     * @param goodsId 商品id
     */
    @Override
    public void deleteByGoodsId(Long goodsId) {
        DeleteQuery dq = dq();
        dq.setQuery(QueryBuilders.termQuery("goodsId", goodsId));

        elasticsearchTemplate.delete(dq);
    }

    private DeleteQuery dq() {
        DeleteQuery dq = new DeleteQuery();
        String indexName = esConfig.getIndexName() + "_" + EsSettings.PINTUAN_INDEX_NAME;
        dq.setIndex(indexName);
        dq.setType(EsSettings.PINTUAN_TYPE_NAME);
        return dq;
    }

    /**
     * 删除某个拼团的所有索引
     * @param pinTuanId 拼团id
     */
    @Override
    public void deleteByPintuanId(Long pinTuanId) {
        DeleteQuery dq = dq();

        dq.setQuery(QueryBuilders.termQuery("pinTuanId", pinTuanId));

        elasticsearchTemplate.delete(dq);
    }

    /**
     * 根据拼团id同步es中的拼团商品<br/>
     * 当拼团活动商品发生变化时调用此方法
     * @param pinTuanId 拼团活动id
     */
    @Override
    public void syncIndexByPinTuanId(Long pinTuanId) {
        //查询出数据库中的拼团商品
        List<PinTuanGoodsVO> dbList = this.pintuanGoodsMapper.selectPinTuanGoodsVOList(pinTuanId);

        //查询出索引出的商品
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        bqb.must(QueryBuilders.termQuery("pinTuanId", pinTuanId));

        List<PtGoodsDoc> esList = queryList(bqb);

        //同步数据
        sync(dbList, esList);

    }

    /**
     * 根据商品id同步es中的拼团商品<br>
     * @param goodsId 商品id
     */
    @Override
    public void syncIndexByGoodsId(Long goodsId) {

        CacheGoods goods = goodsClient.getFromCache(goodsId);
        List<GoodsSkuVO> skuList = goods.getSkuList();

        Long now = DateUtil.getDateline();

        //查询出数据库中的拼团商品
        List<PinTuanGoodsVO> dbTempList = this.pintuanGoodsMapper.selectPintuanGoodsByGoodsId(goodsId, now, now);

        List<PinTuanGoodsVO> dbList = new ArrayList<>();

        for (PinTuanGoodsVO ptGoods : dbTempList) {
            if (findSku(ptGoods.getSkuId(), skuList)) {
                dbList.add(ptGoods);
            }
        }


        //根据商品id查询出索引中的商品
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        bqb.must(QueryBuilders.termQuery("goodsId", goodsId));

        List<PtGoodsDoc> esList = queryList(bqb);
        //同步数据
        sync(dbList, esList);

    }

    /**
     * 查询分页的拼团商品
     * @param categoryId 商品分类id
     * @param pageNo 页码
     * @param pageSize 每页显示条数
     * @return 拼团商品分页数据
     */
    @Override
    public WebPage searchPage(Long categoryId, Integer pageNo, Integer pageSize) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 分类检索
        if (categoryId != null) {

            CategoryDO category = goodsClient.getCategory(categoryId);
            if (category != null) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("categoryPath", HexUtils.encode(category.getCategoryPath()) + "*"));
            }

        }
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        String indexName = esConfig.getIndexName() + "_" + EsSettings.PINTUAN_INDEX_NAME;
        searchQuery.withIndices(indexName).withTypes(EsSettings.PINTUAN_TYPE_NAME).withPageable(pageable).withQuery(boolQueryBuilder);

        AggregatedPage<PtGoodsDoc> resultlist = elasticsearchTemplate.queryForPage(searchQuery.build(), PtGoodsDoc.class);


        for (PtGoodsDoc goodsDoc : resultlist.getContent()) {
            GoodsSkuVO skuVO = goodsClient.getSkuFromCache(goodsDoc.getSkuId());
            if (skuVO == null) {
                System.out.println(goodsDoc);
            }
        }


        return new WebPage(pageNo.longValue(), resultlist.getTotalElements(),pageSize.longValue(),  resultlist.getContent());
    }


    private boolean findSku(Long skuId, List<GoodsSkuVO> skuList) {
        for (GoodsSkuVO sku : skuList) {
            if (sku.getSkuId().equals(skuId)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 由es中查询拼团sku列表
     *
     * @param bqb BoolQueryBuilder
     * @return 拼团商品列表
     */
    private List<PtGoodsDoc> queryList(BoolQueryBuilder bqb) {

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        String indexName = esConfig.getIndexName() + "_" + EsSettings.PINTUAN_INDEX_NAME;

        searchQuery.withIndices(indexName).withTypes(EsSettings.PINTUAN_TYPE_NAME).withQuery(bqb);

        List<PtGoodsDoc> esList = elasticsearchTemplate.queryForList(searchQuery.build(), PtGoodsDoc.class);

        return esList;
    }

    /**
     * 对比数据库和es中的两个集合，以数据库的为准，同步es中的数据
     *
     * @param dbList 数据库集合
     * @param esList es中的集合
     */
    private void sync(List<PinTuanGoodsVO> dbList, List<PtGoodsDoc> esList) {

        //按数据库中的来循环
        dbList.forEach(dbGoods -> {

            PtGoodsDoc goodsDoc = findFromList(esList, dbGoods.getSkuId());

            //在索引中没找到说明新增了
            if (goodsDoc == null) {
                this.addIndex(dbGoods);

            } else {

                //看看售价变没变
                Double salesPrice = goodsDoc.getSalesPrice();
                Double dbPrice = dbGoods.getSalesPrice();

                //价格发生变化了
                if (!salesPrice.equals(dbPrice)) {
                    goodsDoc.setSalesPrice(dbPrice);
                    IndexQuery indexQuery = new IndexQueryBuilder().withId("" + goodsDoc.getSkuId()).withObject(goodsDoc).build();
                    elasticsearchTemplate.index(indexQuery);
                }
            }

        });

        //遍历查看删除了的商品
        esList.forEach(goodsDoc -> {
            boolean result = findFormList(dbList, goodsDoc.getSkuId());

            //没找到，说明删除了
            if (!result) {
                delIndex(goodsDoc.getSkuId());
            }

        });


    }

    /**
     * 查询 数据库的列表中没有
     *
     * @param dbList 拼团商品集合
     * @param skuId 商品sku id
     * @return true：有 false：没有
     */
    private boolean findFormList(List<PinTuanGoodsVO> dbList, Long skuId) {
        for (PinTuanGoodsVO goodsVO : dbList) {
            if (goodsVO.getSkuId().equals(skuId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找索引列表中有没有
     *
     * @param list 拼团商品集合
     * @param skuId 商品sku id
     * @return 拼团商品
     */
    private PtGoodsDoc findFromList(List<PtGoodsDoc> list, Long skuId) {
        for (PtGoodsDoc goodsDoc : list) {
            if (goodsDoc.getSkuId().equals(skuId)) {
                return goodsDoc;
            }
        }

        return null;
    }
}
