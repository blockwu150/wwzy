package com.enation.app.javashop.service.distribution.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.client.member.ConnectClient;
import com.enation.app.javashop.client.payment.WechatSmallchangeClient;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.sncreator.SnCreator;
import com.enation.app.javashop.framework.util.*;
import com.enation.app.javashop.mapper.distribution.DistributionMapper;
import com.enation.app.javashop.mapper.distribution.WithdrawApplyMapper;
import com.enation.app.javashop.mapper.distribution.WithdrawSettingMapper;
import com.enation.app.javashop.model.base.SubCode;
import com.enation.app.javashop.model.distribution.dos.DistributionDO;
import com.enation.app.javashop.model.distribution.dos.WithdrawApplyDO;
import com.enation.app.javashop.model.distribution.dos.WithdrawSettingDO;
import com.enation.app.javashop.model.distribution.enums.WithdrawStatusEnum;
import com.enation.app.javashop.model.distribution.vo.BankParamsVO;
import com.enation.app.javashop.model.distribution.vo.WithdrawApplyVO;
import com.enation.app.javashop.model.distribution.vo.WithdrawAuditPaidVO;
import com.enation.app.javashop.model.errorcode.DistributionErrorCode;
import com.enation.app.javashop.model.member.dos.ConnectDO;
import com.enation.app.javashop.model.member.enums.ConnectTypeEnum;
import com.enation.app.javashop.service.distribution.DistributionManager;
import com.enation.app.javashop.service.distribution.WithdrawManager;
import com.enation.app.javashop.service.distribution.exception.DistributionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 提现设置实现
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 下午12:57
 */

@Service
public class WithdrawManagerImpl implements WithdrawManager {

    @Autowired
    private DistributionManager distributionManager;
    @Autowired
    private ConnectClient connectClient;
    @Autowired
    private WechatSmallchangeClient wechatSmallchangeClient;
    @Autowired
    SnCreator snCreator;

    @Autowired
    private WithdrawApplyMapper withdrawApplyMapper;
    @Autowired
    private WithdrawSettingMapper withdrawSettingMapper;
    @Autowired
    private DistributionMapper distributionMapper;


    /**
     * 根据ID提现申请详细记录
     *
     * @param id 提现申请id
     * @return 提现申请记录
     */
    @Override
    public WithdrawApplyDO getModel(Long id) {
        QueryWrapper<WithdrawApplyDO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        return withdrawApplyMapper.selectOne(wrapper);
    }

    /**
     * 根据member_id查询提现记录
     * @param memberId 会员id
     * @param pageNo 页码
     * @param pageSize 每页显示条数
     * @return 分页数据
     */
    @Override
    public WebPage<WithdrawApplyVO> pageWithdrawApply(Long memberId, Long pageNo, Long pageSize) {
        IPage<WithdrawApplyDO> iPage = withdrawApplyMapper.pageWithdrawApply(new Page<>(pageNo, pageSize), memberId);
        WebPage<WithdrawApplyVO> result = this.convertPage(iPage);
        return result;
    }

    /**
     * 保存提现设置
     * @param bankParams 提现参数
     */
    @Override
    public void saveWithdrawWay(BankParamsVO bankParams) {
        //查询该用户提现设置
        Long userId = UserContext.getBuyer().getUid();
        QueryWrapper<WithdrawSettingDO> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", userId);
        WithdrawSettingDO withdrawSetting = withdrawSettingMapper.selectOne(wrapper);

        //如果提现设置存在，则修改，否则添加
        if (withdrawSetting != null) {
            withdrawSetting.setMemberId(userId);
            withdrawSetting.setParam(JsonUtil.objectToJson(bankParams));
            withdrawSettingMapper.updateById(withdrawSetting);
//            Map where = new HashMap(16);
//            where.put("id", withdrawSetting.getId());
//            this.daoSupport.update("es_withdraw_setting", withdrawSetting, where);
        } else {
            withdrawSetting = new WithdrawSettingDO();
            withdrawSetting.setMemberId(userId);
            withdrawSetting.setParam(JsonUtil.objectToJson(bankParams));
            withdrawSettingMapper.insert(withdrawSetting);
        }
    }

    /**
     * 申请提现
     *
     * @param memberId    会员id
     * @param applyMoney  申请金额
     * @param applyRemark 备注
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void applyWithdraw(Long memberId, Double applyMoney, String applyRemark) {
        //设置提现申请参数
        WithdrawApplyDO apply = new WithdrawApplyDO();
        apply.setApplyTime(DateUtil.getDateline());
        apply.setApplyMoney(applyMoney);
        apply.setApplyRemark(applyRemark);
        apply.setStatus(WithdrawStatusEnum.APPLY.name());
        apply.setMemberId(memberId);
        apply.setMemberName(UserContext.getBuyer().getUsername());
        apply.setSn("" + snCreator.create(SubCode.PAY_BILL));
        apply.setIp(IPUtil.getIpAdrress());
        withdrawApplyMapper.insert(apply);
        // 修改可提现金额
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("member_id", memberId);
        wrapper.setSql("can_rebate=can_rebate-" + applyMoney);
        wrapper.setSql("withdraw_frozen_price=withdraw_frozen_price+" + applyMoney);
        distributionMapper.update(null, wrapper);
    }

    /**
     * 批量审核提现申请
     *
     * @param withdrawAuditPaidVO 提现申请审核参数实体
     * @param auditResult 审核结果 {@link WithdrawStatusEnum}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchAuditing(WithdrawAuditPaidVO withdrawAuditPaidVO, String auditResult) {
        //判断是否选择了提现申请
        if (withdrawAuditPaidVO.getApplyIds() == null || withdrawAuditPaidVO.getApplyIds().length == 0) {
            throw new DistributionException(DistributionErrorCode.E1000.code(), "请选择要审核的提现申请");
        }

        //判断审核状态值是否正确
        if (StringUtil.isEmpty(auditResult) || (!WithdrawStatusEnum.VIA_AUDITING.value().equals(auditResult) && !WithdrawStatusEnum.FAIL_AUDITING.value().equals(auditResult))) {
            throw new DistributionException(DistributionErrorCode.E1005.code(), "审核状态值不正确");
        }

        for (Long applyId : withdrawAuditPaidVO.getApplyIds()) {
            WithdrawApplyDO wdo = this.getModel(applyId);
            //判断提现申请是否存在
            if (wdo == null) {
                throw new DistributionException(DistributionErrorCode.E1004.code(), "ID为" + wdo.getId() + "的提现申请不存在");
            }

            //除状态为申请中的提现申请，其它状态的提现申请都不允许审核
            if (!WithdrawStatusEnum.APPLY.value().equals(wdo.getStatus())) {
                throw new DistributionException(DistributionErrorCode.E1002.code(), "ID为" + wdo.getId() + "的提现申请已经审核，不能重复审核");
            }

            //判断审核状态值是否正确
            if (StringUtil.isEmpty(auditResult) || (!WithdrawStatusEnum.VIA_AUDITING.value().equals(auditResult) && !WithdrawStatusEnum.FAIL_AUDITING.value().equals(auditResult))) {
                throw new DistributionException(DistributionErrorCode.E1005.code(), "审核状态值不正确");
            }

            //更改提现申请审核状态数据
            WithdrawApplyDO withdrawApplyDo = new WithdrawApplyDO();
            UpdateWrapper<WithdrawApplyDO> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", applyId);
            withdrawApplyDo.setStatus(auditResult);
            withdrawApplyDo.setInspectTime(DateUtil.getDateline());
            withdrawApplyDo.setInspectRemark(withdrawAuditPaidVO.getRemark());
            withdrawApplyMapper.update(withdrawApplyDo, wrapper);

            //如果审核未通过，要将提现的金额返还
            if (WithdrawStatusEnum.FAIL_AUDITING.name().equals(auditResult)) {
                // 获取分销商信息
                DistributionDO distributionDO = this.distributionManager.getDistributorByMemberId(wdo.getMemberId());

                double rebate = CurrencyUtil.add(distributionDO.getCanRebate(), wdo.getApplyMoney());
                double frozen = CurrencyUtil.sub(distributionDO.getWithdrawFrozenPrice(), wdo.getApplyMoney());

                UpdateWrapper<DistributionDO> wrapperr = new UpdateWrapper<>();
                //根据会员id修改
                wrapperr.eq("member_id", wdo.getMemberId());
                //修改可提现金额
                wrapperr.set("can_rebate", rebate);
                //修改冻结金额
                wrapperr.set("withdraw_frozen_price", frozen);
                distributionMapper.update(null, wrapperr);
            } else {
                autoSend(applyId);
            }
        }
    }

    /**
     * 批量设置已转账
     * @param withdrawAuditPaidVO 提现申请审核参数实体
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchAccountPaid(WithdrawAuditPaidVO withdrawAuditPaidVO) {
        //判断是否选择了提现申请
        if (withdrawAuditPaidVO.getApplyIds() == null || withdrawAuditPaidVO.getApplyIds().length == 0) {
            throw new DistributionException(DistributionErrorCode.E1000.code(), "请选择要设置为已转账的提现申请");
        }

        for (Long applyId : withdrawAuditPaidVO.getApplyIds()) {
            WithdrawApplyDO wdo = this.getModel(applyId);
            //判断提现申请是否存在
            if (wdo == null) {
                throw new DistributionException(DistributionErrorCode.E1004.code(), "ID为" + wdo.getId() + "的提现申请不存在");
            }
            //除状态为审核通过的提现申请，其它状态的提现申请都不允许设置已转账
            if (!WithdrawStatusEnum.VIA_AUDITING.value().equals(wdo.getStatus())) {
                throw new DistributionException(DistributionErrorCode.E1002.code(), "ID为" + wdo.getId() + "的提现申请审核未通过，不能设置已转账");
            }

            WithdrawApplyDO withdrawApplyDo = new WithdrawApplyDO();
            UpdateWrapper<WithdrawApplyDO> wrapper = new UpdateWrapper<>();
            //根据提现申请id修改
            wrapper.eq("id", applyId);
            //修改提现状态为已转账
            withdrawApplyDo.setStatus(WithdrawStatusEnum.TRANSFER_ACCOUNTS.name());
            //设置转账时间为当前时间
            withdrawApplyDo.setTransferTime(DateUtil.getDateline());
            //设置转账备注
            withdrawApplyDo.setTransferRemark(withdrawAuditPaidVO.getRemark());
            withdrawApplyMapper.update(withdrawApplyDo, wrapper);
        }
    }

    /**
     * 获取提现设置
     *
     * @param memberId 会员id
     * @return 提现参数
     */
    @Override
    public BankParamsVO getWithdrawSetting(Long memberId) {
        //查询该会员提现设置
        QueryWrapper<WithdrawSettingDO> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        WithdrawSettingDO withdrawSetting = withdrawSettingMapper.selectOne(wrapper);
        if (withdrawSetting == null) {
            return new BankParamsVO();
        }
        //将json字符串转为vo返回
        return JsonUtil.jsonToObject(withdrawSetting.getParam(), BankParamsVO.class);
    }

    /**
     * 分页会员提现查询
     *
     * @param pageNo 页码
     * @param pageSize 每页显示条数
     * @param map 查询参数
     * @return 分页数据
     */
    @Override
    public WebPage<WithdrawApplyVO> pageApply(Long pageNo, Long pageSize, Map<String, String> map) {
        QueryWrapper<WithdrawApplyDO> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtil.isEmpty(map.get("uname")), "member_name", map.get("uname"));
        wrapper.gt(!StringUtil.isEmpty(map.get("start_time")), "apply_time", map.get("start_time"));
        wrapper.lt(!StringUtil.isEmpty(map.get("end_time")), "apply_time", map.get("end_time"));
        wrapper.eq(!StringUtil.isEmpty(map.get("status")), "status", map.get("status"));
        wrapper.orderByDesc("id");

        IPage<WithdrawApplyDO> iPage = withdrawApplyMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        WebPage<WithdrawApplyVO> result = this.convertPage(iPage);

        return result;
    }

    /**
     * 导出提现申请
     * @param map 筛选参数
     * @return 提现申请列表
     */
    @Override
    public List<WithdrawApplyDO> exportApply(Map<String, String> map) {

        QueryWrapper<WithdrawApplyDO> wrapper = new QueryWrapper<>();
        //如果会员名称不为空，拼接会员名称查询条件
        wrapper.like(!StringUtil.isEmpty(map.get("uname")), "member_name", map.get("uname"));
        //如果开始时间不为空，拼接提现时间查询条件
        wrapper.gt(!StringUtil.isEmpty(map.get("start_time")), "apply_time", map.get("start_time"));
        //如果结束时间不为空，拼接结束时间查询条件
        wrapper.lt(!StringUtil.isEmpty(map.get("end_time")), "apply_time", map.get("end_time"));
        //如果提现状态不为空，拼接体现状态查询条件
        wrapper.eq(!StringUtil.isEmpty(map.get("status")), "status", map.get("status"));
        //按提现时间倒序查询
        wrapper.orderByDesc("apply_time");
        List<WithdrawApplyDO> applyList = withdrawApplyMapper.selectList(wrapper);

        return applyList;
    }

    /**
     * 分页会员提现查询
     *
     * @param memberId 会员id
     * @return 可提现金额
     */
    @Override
    public Double getRebate(Long memberId) {

        QueryWrapper<DistributionDO> wrapper = new QueryWrapper<>();
        wrapper.select("can_rebate");
        wrapper.eq("member_id", memberId);
        Double rebate = distributionMapper.selectOne(wrapper).getCanRebate();
        return rebate <= 0 ? 0 : rebate;
    }

    /**
     * 转换page
     *
     * @param page
     * @return
     */
    private WebPage convertPage(IPage<WithdrawApplyDO> page) {
        List<WithdrawApplyVO> vos = new ArrayList<>();
        //将DO转换为VO
        for (WithdrawApplyDO withdrawApplyDO : page.getRecords()) {
            WithdrawApplyVO applyVO = new WithdrawApplyVO(withdrawApplyDO);
            BankParamsVO paramsVO = this.getWithdrawSetting(withdrawApplyDO.getMemberId());
            applyVO.setBankParamsVO(paramsVO);
            vos.add(applyVO);
        }
        WebPage result = new WebPage(page.getPages(), page.getTotal(), page.getSize(), vos);
        return result;
    }

    /**
     * 自动发送红包
     *
     * @param applyId 提现申请id
     */
    private void autoSend(Long applyId) {
        //查询提现申请
        WithdrawApplyDO withdrawApplyDO = this.getModel(applyId);
        //查询联合登录对象
        ConnectDO connectDO = connectClient.getConnect(withdrawApplyDO.getMemberId(), ConnectTypeEnum.WECHAT_OPENID.value());
        //如果查不到，则不进行操作
        if (connectDO == null || StringUtil.isEmpty(connectDO.getUnionId())) {
            return;
        }
        //自动发送零钱红包
        boolean success = wechatSmallchangeClient.autoSend(connectDO.getUnionId(), withdrawApplyDO.getApplyMoney(), withdrawApplyDO.getIp(), withdrawApplyDO.getSn());
        if (success) {
            WithdrawApplyDO withdrawApplyDo = new WithdrawApplyDO();
            UpdateWrapper<WithdrawApplyDO> wrapper = new UpdateWrapper<>();
            //根据提现id修改
            wrapper.eq("id", applyId);
            //修改提现状态为已转账
            withdrawApplyDo.setStatus(WithdrawStatusEnum.TRANSFER_ACCOUNTS.name());
            //修改转账时间
            withdrawApplyDo.setTransferTime(DateUtil.getDateline());
            //设置转账备注
            withdrawApplyDo.setTransferRemark("零钱转账");
            withdrawApplyMapper.update(withdrawApplyDo, wrapper);
        }
    }
}
