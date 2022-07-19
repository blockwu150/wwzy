package com.enation.app.javashop.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.app.javashop.mapper.goods.GoodsParamsMapper;
import com.enation.app.javashop.mapper.goods.ParameterGroupMapper;
import com.enation.app.javashop.mapper.goods.ParametersMapper;
import com.enation.app.javashop.model.goods.dos.GoodsParamsDO;
import com.enation.app.javashop.model.goods.dos.ParameterGroupDO;
import com.enation.app.javashop.model.goods.vo.GoodsParamsGroupVO;
import com.enation.app.javashop.model.goods.vo.GoodsParamsVO;
import com.enation.app.javashop.service.goods.GoodsParamsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品参数
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月21日 下午5:30:11
 */
@Service
public class GoodsParamsManagerImpl implements GoodsParamsManager {

    @Autowired
    private GoodsParamsMapper goodsParamsMapper;
    @Autowired
    private ParameterGroupMapper parameterGroupMapper;
    @Autowired
    private ParametersMapper parametersMapper;

    /**
     * 添加商品关联的参数
     *
     * @param goodsId   商品id
     * @param paramList 参数集合
     */
    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addParams(List<GoodsParamsDO> paramList, Long goodsId) {

        //先删除该商品所有参数
        this.goodsParamsMapper.delete(new QueryWrapper<GoodsParamsDO>()
                .eq("goods_id",goodsId));

        //再添加商品参数
        if (paramList != null) {
            for (GoodsParamsDO param : paramList) {
                param.setGoodsId(goodsId);
                this.goodsParamsMapper.insert(param);
            }
        }
    }

    /**
     * 修改商品查询分类和商品关联的参数
     *
     * @param categoryId 分类id
     * @param goodsId 商品id
     * @return 商品参数集合
     */
    @Override
    public List<GoodsParamsGroupVO> queryGoodsParams(Long categoryId, Long goodsId) {

        //查询参数组
        List<ParameterGroupDO> groupList = parameterGroupMapper.selectList(new QueryWrapper<ParameterGroupDO>()
                .eq("category_id",categoryId));

        //查询商品关联的参数及参数值
        List<GoodsParamsVO> paramList = this.goodsParamsMapper.queryGoodsParamsValue(categoryId,goodsId);

        //拼装返回值
        List<GoodsParamsGroupVO> resList = this.convertParamList(groupList, paramList);

        return resList;
    }

    /**
     * 拼装返回值
     *
     * @param paramList 商品参数集合
     * @return 商品参数集合
     */
    private List<GoodsParamsGroupVO> convertParamList(List<ParameterGroupDO> groupList, List<GoodsParamsVO> paramList) {
        //根据组id分组
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
        //将参数DO封装为VO
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

    /**
     * 添加商品查询分类和商品关联的参数
     *
     * @param categoryId 分类id
     * @return 商品参数集合
     */
    @Override
    public List<GoodsParamsGroupVO> queryGoodsParams(Long categoryId) {
        //查询参数组
        List<ParameterGroupDO> groupList = parameterGroupMapper.selectList(new QueryWrapper<ParameterGroupDO>()
                .eq("category_id",categoryId));

        //查询分类关联的参数
        List<GoodsParamsVO> paramList = this.parametersMapper.queryParams(categoryId);

        //拼装返回值
        List<GoodsParamsGroupVO> resList = this.convertParamList(groupList, paramList);

        return resList;
    }
}
