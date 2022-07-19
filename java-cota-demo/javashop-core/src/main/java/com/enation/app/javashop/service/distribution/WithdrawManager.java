package com.enation.app.javashop.service.distribution;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.distribution.dos.WithdrawApplyDO;
import com.enation.app.javashop.model.distribution.enums.WithdrawStatusEnum;
import com.enation.app.javashop.model.distribution.vo.BankParamsVO;
import com.enation.app.javashop.model.distribution.vo.WithdrawApplyVO;
import com.enation.app.javashop.model.distribution.vo.WithdrawAuditPaidVO;

import java.util.List;
import java.util.Map;


/**
 * 提现接口
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 下午1:18
 */
public interface WithdrawManager {

    /**
     * 根据ID提现申请详细记录
     *
     * @param id 提现申请id
     * @return 提现申请记录
     */
    WithdrawApplyDO getModel(Long id);

    /**
     * 申请提现
     *
     * @param memberId    会员id
     * @param applyMoney  申请金额
     * @param applyRemark 备注
     */
    void applyWithdraw(Long memberId, Double applyMoney, String applyRemark);

    /**
     * 批量审核提现申请
     *
     * @param withdrawAuditPaidVO 提现申请审核参数实体
     * @param auditResult 审核结果 {@link WithdrawStatusEnum}
     */
    void batchAuditing(WithdrawAuditPaidVO withdrawAuditPaidVO, String auditResult);

    /**
     * 批量设置已转账
     * @param withdrawAuditPaidVO 提现申请审核参数实体
     */
    void batchAccountPaid(WithdrawAuditPaidVO withdrawAuditPaidVO);

    /**
     * 根据member_id查询提现记录
     * @param memeberId 会员id
     * @param pageNo 页码
     * @param pageSize 每页显示条数
     * @return 分页数据
     */
    WebPage<WithdrawApplyVO> pageWithdrawApply(Long memeberId, Long pageNo, Long pageSize);

    /**
     * 保存提现设置
     * @param bankParams 提现参数
     */
    void saveWithdrawWay(BankParamsVO bankParams);

    /**
     * 获取提现设置
     *
     * @param memberId 会员id
     * @return 提现参数
     */
    BankParamsVO getWithdrawSetting(Long memberId);

    /**
     * 分页会员提现查询
     *
     * @param pageNo 页码
     * @param pageSize 每页显示条数
     * @param map 查询参数
     * @return 分页数据
     */
    WebPage<WithdrawApplyVO> pageApply(Long pageNo, Long pageSize, Map<String, String> map);

    /**
     * 导出提现申请
     * @param map 筛选参数
     * @return 提现申请列表
     */
    List<WithdrawApplyDO> exportApply(Map<String, String> map);

    /**
     * 分页会员提现查询
     *
     * @param memberId 会员id
     * @return 可提现金额
     */
    Double getRebate(Long memberId);

}
