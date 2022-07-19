package com.enation.app.javashop.client.distribution.impl;

import com.enation.app.javashop.client.distribution.DistributionClient;
import com.enation.app.javashop.model.distribution.dos.DistributionDO;
import com.enation.app.javashop.service.distribution.DistributionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * DistributionGoodsClientDefaultImpl
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-08-14 下午1:32
 */
@Service
@ConditionalOnProperty(value = "javashop.product", havingValue = "stand")
public class DistributionClientDefaultImpl implements DistributionClient {

    @Autowired
    private DistributionManager distributionManager;

    /**
     * 新增分销商
     *
     * @param distributor
     * @return
     */
    @Override
    public DistributionDO add(DistributionDO distributor) {
        return distributionManager.add(distributor);
    }

    /**
     * 更新Distributor信息
     *
     * @param distributor
     * @return
     */
    @Override
    public DistributionDO edit(DistributionDO distributor) {
        return distributionManager.edit(distributor);
    }

    /**
     * 获取分销商
     *
     * @param memberId
     * @return
     */
    @Override
    public DistributionDO getDistributorByMemberId(Long memberId) {
        return distributionManager.getDistributorByMemberId(memberId);
    }

    /**
     * 根据会员id设置其上级分销商（两级）
     *
     * @param memberId 会员id
     * @param parentId 上级会员的id
     * @return 设置结果， trun=成功 false=失败
     */
    @Override
    public boolean setParentDistributorId(Long memberId, Long parentId) {
        return distributionManager.setParentDistributorId(memberId, parentId);
    }
}
