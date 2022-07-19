package com.enation.app.javashop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.client.member.ShopCatClient;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.util.*;
import com.enation.app.javashop.mapper.goods.CategoryMapper;
import com.enation.app.javashop.mapper.goods.DraftGoodsMapper;
import com.enation.app.javashop.model.errorcode.GoodsErrorCode;
import com.enation.app.javashop.model.goods.dos.CategoryDO;
import com.enation.app.javashop.model.goods.dos.DraftGoodsDO;
import com.enation.app.javashop.model.goods.dos.GoodsDO;
import com.enation.app.javashop.model.goods.dos.GoodsGalleryDO;
import com.enation.app.javashop.model.goods.dto.GoodsDTO;
import com.enation.app.javashop.model.goods.vo.DraftGoodsVO;
import com.enation.app.javashop.model.goods.vo.GoodsMobileIntroVO;
import com.enation.app.javashop.model.goods.vo.GoodsSkuVO;
import com.enation.app.javashop.service.goods.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 草稿商品业务类
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-26 10:40:34
 */
@Service
public class DraftGoodsManagerImpl implements DraftGoodsManager {

	@Autowired
	private DraftGoodsParamsManager draftGoodsParamsManager;
	@Autowired
	private DraftGoodsSkuManager draftGoodsSkuManager;
	@Autowired
	private GoodsManager goodsManager;

	@Autowired
	private GoodsSkuManager goodsSkuManager;

	@Autowired
	private GoodsQueryManager goodsQueryManager;

	@Autowired
	private ShopCatClient shopCatClient;

	@Autowired
	private CategoryManager categoryManager;

	@Autowired
	private DraftGoodsMapper draftGoodsMapper;

	@Autowired
	private CategoryMapper categoryMapper;


	/**
	 * 查询草稿商品列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param keyword 关键字
	 * @param shopCatPath 店铺分组path
	 * @return WebPage
	 */
	@Override
	public WebPage list(long page, long pageSize, String keyword, String shopCatPath) {

		Seller seller = UserContext.getSeller();
		QueryWrapper<DraftGoodsDO> goodsWrapper = new QueryWrapper<>();
		//拼接商家id查询条件
		goodsWrapper.eq("seller_id",seller.getSellerId());
		//如果关键字不为空，拼接关键字查询条件
		if(!StringUtil.isEmpty(keyword)){

			goodsWrapper.and(wp->{
				wp.like("goods_name",keyword).or().like("sn",keyword);
			});
		}

		if(shopCatPath != null){
			//查询所有子店铺分组
			List<Map> catList = shopCatClient.getChildren(shopCatPath);
			if (!StringUtil.isNotEmpty(catList)) {
				throw new ServiceException(GoodsErrorCode.E301.code(), "店铺分组不存在");
			}
			List<String> catIds = catList.stream().map(c -> c.get("shop_cat_id").toString()).collect(Collectors.toList());
			//拼接店铺类目查询条件
			goodsWrapper.in("shop_cat_id",catIds);
		}
		goodsWrapper.orderByDesc("create_time");
		IPage webPage = this.draftGoodsMapper.selectMapsPage(new Page<>(page, pageSize), goodsWrapper);

		return PageConvert.convert(webPage);
	}

	/**
	 * 添加草稿商品
	 * @param goodsVO 草稿商品
	 * @return DraftGoods 草稿商品
	 */
	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public DraftGoodsDO add(GoodsDTO goodsVO) {

		Seller seller = UserContext.getSeller();
		// 没有规格给这个字段塞0
		goodsVO.setHaveSpec(0);

		DraftGoodsDO draftGoods = new DraftGoodsDO(goodsVO);
		// 商品状态 是否可用
		draftGoods.setCreateTime(DateUtil.getDateline());
		draftGoods.setQuantity(goodsVO.getQuantity());
		draftGoods.setSellerId(seller.getSellerId());
		// 相册
		List<GoodsGalleryDO> galleryList = goodsVO.getGoodsGalleryList();
		if (StringUtil.isNotEmpty(galleryList)) {
			List<String> list = new ArrayList<>();
			for (GoodsGalleryDO gallery : galleryList) {
				list.add(gallery.getOriginal());
			}
			draftGoods.setOriginal(JsonUtil.objectToJson(list));
		}

		draftGoods.setSellerName(seller.getSellerName());
		// 添加草稿箱商品
		this.draftGoodsMapper.insert(draftGoods);
		// 获取添加商品的商品ID
		Long draftGoodsId = draftGoods.getDraftGoodsId();
		// 添加商品参数
		if (StringUtil.isNotEmpty(goodsVO.getGoodsParamsList())) {
			draftGoodsParamsManager.addParams(goodsVO.getGoodsParamsList(), draftGoodsId);
		}

		return draftGoods;
	}

	/**
	 * 修改草稿商品
	 * @param goodsVO 草稿商品
	 * @param id 草稿商品主键
	 * @return DraftGoods 草稿商品
	 */
	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public DraftGoodsDO edit(GoodsDTO goodsVO, Long id) {

		//验证是否有权限操作
		Seller seller = UserContext.getSeller();
		DraftGoodsDO draftGoods = this.getModel(id);
		if(draftGoods == null || !draftGoods.getSellerId().equals(seller.getSellerId())){
			throw new ServiceException(GoodsErrorCode.E308.code(),"无权操作");
		}

		DraftGoodsDO goods = new DraftGoodsDO(goodsVO);
		goods.setQuantity(goodsVO.getQuantity());
		// 修改后的图片列表
		List<GoodsGalleryDO> galleryList = goodsVO.getGoodsGalleryList();
		List<String> listNew = new ArrayList<>();
		if (StringUtil.isNotEmpty(galleryList)) {
			for (GoodsGalleryDO gallery : galleryList) {
				listNew.add(gallery.getOriginal());
			}
		}

		goods.setOriginal(JsonUtil.objectToJson(listNew));
		goods.setDraftGoodsId(id);
		this.draftGoodsMapper.updateById(goods);
		// 处理参数信息
		// 添加商品参数
		if (StringUtil.isNotEmpty(goodsVO.getGoodsParamsList())) {
			this.draftGoodsParamsManager.addParams(goodsVO.getGoodsParamsList(), id);
		}

		return goods;
	}

	/**
	 * 删除草稿商品
	 * @param draftGoodsIds 草稿商品主键
	 */
	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public void delete(Long[] draftGoodsIds) {

		draftGoodsMapper.deleteBatchIds(Arrays.asList(draftGoodsIds));
	}

	/**
	 * 获取草稿商品
	 * @param id 草稿商品主键
	 * @return DraftGoods  草稿商品
	 */
	@Override
	public DraftGoodsDO getModel(Long id) {
		return this.draftGoodsMapper.selectById(id);
	}

	/**
	 * 获取草稿商品
	 * @param id 草稿商品主键
	 * @return DraftGoods  草稿商品
	 */
	@Override
	public DraftGoodsVO getVO(Long id) {

		DraftGoodsDO draftGoodsDO = this.getModel(id);
		DraftGoodsVO draftGoodsVO = new DraftGoodsVO();
		BeanUtil.copyProperties(draftGoodsDO,draftGoodsVO);

		//转换商品移动端详情数据
		if (StringUtil.notEmpty(draftGoodsVO.getMobileIntro())) {
			draftGoodsVO.setIntroList(JsonUtil.jsonToList(draftGoodsVO.getMobileIntro(), GoodsMobileIntroVO.class));
		}

        Map map = categoryManager.queryCatNameAndIs(draftGoodsVO.getCategoryId());
        draftGoodsVO.setCategoryName(map.get("catName").toString());
		//商品分类赋值
		Long categoryId = draftGoodsVO.getCategoryId();
		CategoryDO category = categoryManager.getModel(categoryId);

		QueryWrapper<CategoryDO> goodsWrapper = new QueryWrapper<>();
		//拼接分类id查询条件
		goodsWrapper.in("category_id",category.getCategoryPath().replace("|", ",") + "-1) ");
		List<CategoryDO> list = categoryMapper.selectList(goodsWrapper);

		String categoryName = "";
		Long[] categoryIds = new Long[3];
		int i = 0;
		if (StringUtil.isNotEmpty(list)) {
			for (CategoryDO c : list) {
				if ("".equals(categoryName)) {
					categoryName = " " + c.getName();
				} else {
					categoryName += ">" + c.getName()+" ";
				}
				categoryIds[i] = c.getCategoryId();
				i++;
			}
		}
		draftGoodsVO.setCategoryIds(categoryIds);

		return draftGoodsVO;
	}

	/**
	 * 草稿商品上架
	 * @param goodsVO 上架商品对象
	 * @param draftGoodsId 草稿商品id
	 * @return
	 */
	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public GoodsDO addMarket(GoodsDTO goodsVO, Long draftGoodsId) {
		Long[] goodsIds = new Long[]{ draftGoodsId };
		//先删除
		this.delete(goodsIds);
		//再添加
		goodsVO.setMarketEnable(1);
		GoodsDO goods = goodsManager.add( goodsVO);
		//同时需要存储sku信息
		List<GoodsSkuVO> skuList = draftGoodsSkuManager.getSkuList(draftGoodsId);
		goodsSkuManager.editSkus(skuList,goods.getGoodsId());
		return goods;
	}

}
