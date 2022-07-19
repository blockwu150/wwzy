package com.enation.app.javashop.service.distribution.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.enation.app.javashop.mapper.distribution.DistributionGoodsMapper;
import com.enation.app.javashop.model.distribution.dos.DistributionGoods;
import com.enation.app.javashop.service.distribution.DistributionGoodsManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * DistributionGoodsManagerImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-14 上午12:39
 */
@Service
public class DistributionGoodsManagerImpl implements DistributionGoodsManager {

    @Autowired
    private DistributionGoodsMapper distributionGoodsMapper;

    /**
     * 设置分销商品提现设置
     *
     * @param distributionGoods 分销商品对象
     * @return
     */
    @Override
    @Transactional(value = "distributionTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public DistributionGoods edit(DistributionGoods distributionGoods) {
        //使用商品id查询某分销商品
        QueryWrapper<DistributionGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", distributionGoods.getGoodsId());
        DistributionGoods old = distributionGoodsMapper.selectOne(wrapper);
        //如果该分销商品不存在，则添加
        if (null == old) {
            distributionGoodsMapper.insert(distributionGoods);
            return distributionGoods;
        }
        //否则更新此分销商品
        UpdateWrapper<DistributionGoods> wrapperr = new UpdateWrapper<>();
        wrapperr.eq("id", old.getId());
        DistributionGoods distributionGood = new DistributionGoods();
        //修改一级提成金额
        distributionGood.setGrade1Rebate(old.getGrade1Rebate());
        //修改二级提成金额
        distributionGood.setGrade2Rebate(old.getGrade2Rebate());
        distributionGoodsMapper.update(distributionGoods, wrapperr);
        return distributionGoods;

    }

    /**
     * 删除
     *
     * @param goodsId
     */
    @Override
    public void delete(Long goodsId) {
        distributionGoodsMapper.deleteById(goodsId);
    }

    /**
     * 获取
     *
     * @param goodsId
     */
    @Override
    public DistributionGoods getModel(Long goodsId) {
        QueryWrapper<DistributionGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", goodsId);
        return distributionGoodsMapper.selectOne(wrapper);
    }
}
