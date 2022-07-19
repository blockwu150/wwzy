package com.enation.app.javashop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.app.javashop.mapper.goods.DraftGoodsParamsMapper;
import com.enation.app.javashop.mapper.goods.ParameterGroupMapper;
import com.enation.app.javashop.model.goods.dos.DraftGoodsParamsDO;
import com.enation.app.javashop.model.goods.dos.GoodsParamsDO;
import com.enation.app.javashop.model.goods.dos.ParameterGroupDO;
import com.enation.app.javashop.model.goods.vo.GoodsParamsGroupVO;
import com.enation.app.javashop.model.goods.vo.GoodsParamsVO;
import com.enation.app.javashop.service.goods.DraftGoodsParamsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 草稿商品参数表业务类
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-26 11:31:20
 */
@Service
public class DraftGoodsParamsManagerImpl implements DraftGoodsParamsManager{

	@Autowired
	private DraftGoodsParamsMapper draftGoodsParamsMapper;

	@Autowired
	private ParameterGroupMapper parameterGroupMapper;


    /**
     * 添加草稿商品的参数集合
     * @param goodsParamsList 商品参数集合
     * @param draftGoodsId 草稿id
     */
	@Override
	@Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addParams(List<GoodsParamsDO> goodsParamsList, Long draftGoodsId) {

		//先删除该草稿商品下的参数
		QueryWrapper<DraftGoodsParamsDO> wrapper = new QueryWrapper<>();
		wrapper.eq("draft_goods_id",draftGoodsId);
		draftGoodsParamsMapper.delete(wrapper);
		//再添加商品参数
		for (GoodsParamsDO param : goodsParamsList){
			DraftGoodsParamsDO draftGoodsParams = new DraftGoodsParamsDO(param);
			draftGoodsParams.setDraftGoodsId(draftGoodsId);
			this.draftGoodsParamsMapper.insert(draftGoodsParams);
		}

	}


	/**
	 * 查询分类关联的参数，同时返回已经添加的值
	 * @param categoryId 分类id
	 * @param draftGoodsId 草稿id
	 * @return 商品参数列表
	 */
	@Override
	public List<GoodsParamsGroupVO> getParamByCatAndDraft(Long categoryId, Long draftGoodsId) {

		//查询参数组
		QueryWrapper<ParameterGroupDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("category_id",categoryId);
		List<ParameterGroupDO> groupList = this.parameterGroupMapper.selectList(queryWrapper);

		//查询草稿商品关联的参数及参数值
		List<GoodsParamsVO> paramList = draftGoodsParamsMapper.queryDraftGoodsParamsValue(categoryId,draftGoodsId);

		//拼装返回值
		List<GoodsParamsGroupVO> resList = this.convertParamList(groupList, paramList);

		return resList;
	}

	/**
	 * 拼装返回值
	 *
	 * @param paramList 商品参数列表
	 * @return 商品参数列表
	 */
	private List<GoodsParamsGroupVO> convertParamList(List<ParameterGroupDO> groupList, List<GoodsParamsVO> paramList) {
		//按参数组id分组
		Map<Long, List<GoodsParamsVO>> map = new HashMap<>(16);
		for (GoodsParamsVO param : paramList) {
			if (map.get(param.getGroupId()) != null) {
				map.get(param.getGroupId()).add(param);
			} else {
				List<GoodsParamsVO> list = new ArrayList<>();
				list.add(param);
				map.put(param.getGroupId(), list);
			}
		}

		//将DO封装为VO
		List<GoodsParamsGroupVO> resList = new ArrayList<>();
		for (ParameterGroupDO group : groupList) {
			GoodsParamsGroupVO list = new GoodsParamsGroupVO();
			list.setGroupName(group.getGroupName());
			list.setGroupId(group.getGroupId());
			list.setParams(map.get(group.getGroupId()));
			resList.add(list);
		}
		return resList;
	}

}
