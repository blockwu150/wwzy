package com.enation.app.javashop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.mapper.goods.DraftGoodsSkuMapper;
import com.enation.app.javashop.model.goods.dos.DraftGoodsSkuDO;
import com.enation.app.javashop.model.goods.vo.GoodsSkuVO;
import com.enation.app.javashop.service.goods.DraftGoodsSkuManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 草稿商品sku业务类
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-26 11:38:06
 */
@Service
public class DraftGoodsSkuManagerImpl implements DraftGoodsSkuManager{

	@Autowired
	private DraftGoodsSkuMapper draftGoodsSkuMapper;

    /**
     * 添加sku规格列表
     * @param skuList sku集合
     * @param draftGoodsId 草稿商品id
     */
	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public void add(List<GoodsSkuVO> skuList, Long draftGoodsId) {

		//先删除该草稿商品下的所有sku
		Map map = new HashMap<>();
		map.put("draft_goods_id",draftGoodsId);
		draftGoodsSkuMapper.deleteByMap(map);

		//再添加sku
		if(StringUtil.isNotEmpty(skuList)){
			for(GoodsSkuVO skuVO : skuList){
				// 将specValue 转成json放到specs
				skuVO.setSpecs(JsonUtil.objectToJson(skuVO.getSpecList()));
				skuVO.setGoodsId(draftGoodsId);
				// po vo转换
				DraftGoodsSkuDO draftGoodsSku = new DraftGoodsSkuDO(skuVO);
				this.draftGoodsSkuMapper.insert(draftGoodsSku);
			}
		}
	}

	/**
	 * 查询草稿箱的sku列表
	 * @param draftGoodsId 草稿商品id
	 * @return 商品sku列表
	 */
	@Override
	public List<GoodsSkuVO> getSkuList(Long draftGoodsId) {

		//查询该草稿商品下的所有sku
		QueryWrapper<DraftGoodsSkuDO> wrapper = new QueryWrapper<>();
		wrapper.eq("draft_goods_id",draftGoodsId);
		List<DraftGoodsSkuDO> list = this.draftGoodsSkuMapper.selectList(wrapper);

		//将DO封装为VO
		List<GoodsSkuVO> result = new ArrayList<>();
		for (DraftGoodsSkuDO sku : list) {
			GoodsSkuVO skuVo = new GoodsSkuVO(sku);
			result.add(skuVo);
		}
		return result;
	}

}
