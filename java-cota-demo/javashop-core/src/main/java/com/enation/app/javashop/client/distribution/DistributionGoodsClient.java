package com.enation.app.javashop.client.distribution;

import com.enation.app.javashop.model.distribution.dos.DistributionGoods;

/**
 * 分销商品客户端
 * @author liushuai
 * @version v1.0
 * @since v7.0
 * 2018/8/14 下午1:31
 * @Description:
 *
 */
public interface DistributionGoodsClient {


    /**
     * 修改分销商品提现设置
     * @param distributionGoods
     * @return
     */
    DistributionGoods edit(DistributionGoods distributionGoods);

    /**
     * 获取分销设置
     * @param goodsId
     * @return
     */
    DistributionGoods getModel(Long goodsId);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

}
