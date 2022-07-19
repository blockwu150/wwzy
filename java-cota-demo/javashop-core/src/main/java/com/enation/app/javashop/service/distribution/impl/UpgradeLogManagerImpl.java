package com.enation.app.javashop.service.distribution.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.client.member.MemberClient;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.distribution.UpgradeLogMapper;
import com.enation.app.javashop.model.distribution.dos.CommissionTpl;
import com.enation.app.javashop.model.distribution.dos.UpgradeLogDO;
import com.enation.app.javashop.model.distribution.enums.UpgradeTypeEnum;
import com.enation.app.javashop.service.distribution.CommissionTplManager;
import com.enation.app.javashop.service.distribution.DistributionManager;
import com.enation.app.javashop.service.distribution.UpgradeLogManager;
import com.enation.app.javashop.model.member.dos.Member;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 升级日志 实现
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 下午12:58
 */

@Component
public class UpgradeLogManagerImpl implements UpgradeLogManager {

    @Autowired
    private DistributionManager distributionManager;
    @Autowired
    private CommissionTplManager commissionTplManager;
    @Autowired
    private MemberClient memberClient;
    @Autowired
    private UpgradeLogMapper upgradeLogMapper;

    /**
     * 搜索
     *
     * @param page       分页
     * @param pageSize   分页每页数量
     * @param memberName 会员名
     * @return WebPage 分页数据
     */
    @Override
    public WebPage page(long page, long pageSize, String memberName) {
        QueryWrapper<UpgradeLogDO> wrapper = new QueryWrapper<>();
        //如果会员名称不为空，拼接查询条件
        wrapper.like(!StringUtil.isEmpty(memberName),"member_name",memberName);
        //按创建时间倒序查询
        wrapper.orderByDesc("create_time");
        IPage<UpgradeLogDO> iPage = upgradeLogMapper.selectPage(new Page<>(page,pageSize), wrapper);
        return PageConvert.convert(iPage);
    }

    /**
     * 新增一个模板升级日志
     * @param upgradeLog 模版升级日志
     * @return 模版升级日志
     */
    @Override
    public UpgradeLogDO add(UpgradeLogDO upgradeLog) {
        // 非空
        if (upgradeLog != null) {
            upgradeLogMapper.insert(upgradeLog);
        }
        return upgradeLog;
    }

    /**
     * 新增日志,一定要再修改之前【因为旧的模板id是根据用户id现查的】
     *
     * @param memberId    会员id
     * @param newTplId    新的模板id
     * @param upgradeType 模版操作类型
     */
    @Override
    public void addUpgradeLog(Long memberId, int newTplId, UpgradeTypeEnum upgradeType) {
        UpgradeLogDO upgradelog = new UpgradeLogDO();
        //获取会员信息
        Member member = this.memberClient.getModel(memberId);
        //根据会员id获得分销商的信息
        long oldTplId = this.distributionManager.getDistributorByMemberId(memberId).getCurrentTplId();
        CommissionTpl oldTpl = this.commissionTplManager.getModel(oldTplId);
        CommissionTpl newTpl = this.commissionTplManager.getModel(newTplId);

        //set数据
        upgradelog.setMemberId(memberId);
        if(member!=null) {
            upgradelog.setMemberName(member.getUname());
        }else{
            upgradelog.setMemberName("无名");
        }

        // 如果有 就记录
        if (oldTpl != null) {
            upgradelog.setOldTplId(oldTplId);
            upgradelog.setOldTplName(oldTpl.getTplName());
        }

        //设置相关数据
        upgradelog.setNewTplId(newTplId);
        upgradelog.setNewTplName(newTpl.getTplName());
        upgradelog.setType(upgradeType.getName());
        upgradelog.setCreateTime(DateUtil.getDateline());
        this.add(upgradelog);
    }
}
