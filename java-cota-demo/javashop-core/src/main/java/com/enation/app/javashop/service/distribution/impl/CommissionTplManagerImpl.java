package com.enation.app.javashop.service.distribution.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.distribution.CommissionTplMapper;
import com.enation.app.javashop.mapper.distribution.DistributionMapper;
import com.enation.app.javashop.model.distribution.dos.DistributionDO;
import com.enation.app.javashop.model.errorcode.DistributionErrorCode;
import com.enation.app.javashop.service.distribution.exception.DistributionException;
import com.enation.app.javashop.model.distribution.dos.CommissionTpl;
import com.enation.app.javashop.service.distribution.CommissionTplManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模版管理实现类
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 上午11:00
 */

@Service("commissionTplManager")
public class CommissionTplManagerImpl implements CommissionTplManager {

    @Autowired
    private CommissionTplMapper commissionTplMapper;
    @Autowired
    private DistributionMapper distributionMapper;

    /**
     * 通过id获得CommissionTpl
     *
     * @param tplId 提成模版id
     * @return CommissionTpl
     */
    @Override
    public CommissionTpl getModel(long tplId) {
        //查询某个模板
        return commissionTplMapper.selectById(tplId);
    }

    /**
     * 分页查询模板列表
     *
     * @param page     页码
     * @param pageSize 分页大小
     * @return page
     */
    @Override
    public WebPage page(long page, long pageSize) {
        QueryWrapper<CommissionTpl> wrapper = new QueryWrapper<>();
        IPage<CommissionTpl> iPage = commissionTplMapper.selectPage(new Page<>(page,pageSize), wrapper);
        return PageConvert.convert(iPage);
    }

    /**
     * 添加一个commissionTpl
     *
     * @param commissionTpl 模版
     * @return CommissionTplDO
     */
    @Override
    public CommissionTpl add(CommissionTpl commissionTpl) {
        //添加一个模板
        commissionTplMapper.insert(commissionTpl);
        Long id = commissionTpl.getId();
        // 如果是默认模板，则将其他的模板变成非默认
        if (commissionTpl.getIsDefault() == 1) {
            CommissionTpl commissionTpll = new CommissionTpl();
            UpdateWrapper<CommissionTpl> wrapper = new UpdateWrapper<>();
            //不是默认的模板都变成非默认
            wrapper.ne("id",id);
            commissionTpll.setIsDefault(0);
            commissionTplMapper.update(commissionTpll,wrapper);
        }
        return commissionTpl;
    }

    /**
     * 修改一个CommissionTpl
     *
     * @param commissionTpl 提成模版
     * @return CommissionTplDO
     */
    @Override
    public CommissionTpl edit(CommissionTpl commissionTpl) {
        //主键为条件更新模板信息
        commissionTplMapper.updateById(commissionTpl);
        //如果此模板是默认模板，则将其他的模板变成非默认
        if (commissionTpl.getIsDefault() == 1) {
            CommissionTpl commissionTpll = new CommissionTpl();
            UpdateWrapper<CommissionTpl> wrapper = new UpdateWrapper<>();
            wrapper.ne("id",commissionTpl.getId());
            commissionTpll.setIsDefault(0);
            commissionTplMapper.update(commissionTpll,wrapper);
        }
        return commissionTpl;
    }

    /**
     * 删除一个CommissionTpl
     * @param tplId 提成模版id
     */
    @Override
    public void delete(Long tplId) {

        CommissionTpl commissionTpl = this.getModel(tplId);
        //默认模板不能删除
        if (commissionTpl.getIsDefault() == 1) {
            throw new DistributionException(DistributionErrorCode.E1010.code(), DistributionErrorCode.E1010.des());
        }

        QueryWrapper<DistributionDO> wrapper = new QueryWrapper<>();
        wrapper.eq("current_tpl_id",tplId);
        //查询正在使用改模板的分销商数量，大于0，则不能删除
        if( distributionMapper.selectCount(wrapper)>0 ){
            throw new DistributionException(DistributionErrorCode.E1012.code(), DistributionErrorCode.E1012.des());
        }
        //删除模板
        commissionTplMapper.deleteById(tplId);
    }

    /**
     * 得到一个默认的模版
     *
     * @return DO
     */
    @Override
    public CommissionTpl getDefaultCommission() {
        QueryWrapper<CommissionTpl> wrapper = new QueryWrapper<>();
        //获取默认模板
        wrapper.eq("is_default", 1);
        return commissionTplMapper.selectOne(wrapper);
    }

}
