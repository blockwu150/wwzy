package com.enation.app.javashop.service.statistics.impl;

import com.enation.app.javashop.client.goods.GoodsClient;
import com.enation.app.javashop.client.member.MemberAskClient;
import com.enation.app.javashop.client.trade.OrderClient;
import com.enation.app.javashop.model.statistics.vo.ShopDashboardVO;
import com.enation.app.javashop.service.statistics.DashboardStatisticManager;
import com.enation.app.javashop.model.trade.order.vo.OrderStatusNumVO;
import com.enation.app.javashop.framework.context.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 仪表盘业务实现类
 *
 * @author mengyuanming
 * @version 2.0
 * @since 7.0
 * 2018/6/25 10:41
 */
@Service
public class DashboardStatisticManagerImpl implements DashboardStatisticManager {

    @Autowired
    private MemberAskClient memberAskClient;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private GoodsClient goodsClient;

    /**
     * 获取仪表盘数据
     *
     * @return 商家中心数据
     */
    @Override
    public ShopDashboardVO getShopData() {

        // 获取商家id
        long sellerId = UserContext.getSeller().getSellerId();

        // 返回值
        ShopDashboardVO shopDashboardVO = new ShopDashboardVO();

        //商品总数
        Integer num = goodsClient.queryGoodsCountByParam(1, sellerId);

        // 出售中的商品数量
        String marketGoods = null == num ? "0" : num.toString();
        shopDashboardVO.setMarketGoods(null == marketGoods ? "0" : marketGoods);

        // 仓库待上架的商品数量
        num = goodsClient.queryGoodsCountByParam(0, sellerId);


        String pendingGoods = null == num ? "0" : num.toString();
        shopDashboardVO.setPendingGoods(null == pendingGoods ? "0" : pendingGoods);

        // 待处理买家咨询数量
        String pendingMemberAsk = this.memberAskClient.getNoReplyCount(sellerId).toString();
        shopDashboardVO.setPendingMemberAsk(null == pendingMemberAsk ? "0" : pendingMemberAsk);

        OrderStatusNumVO orderStatusNumVO = this.orderClient.getOrderStatusNum(null, sellerId);
        // 所有订单数量
        shopDashboardVO.setAllOrdersNum(null == orderStatusNumVO.getAllNum() ? "0" : orderStatusNumVO.getAllNum().toString());
        // 待付款订单数量
        shopDashboardVO.setWaitPayOrderNum(null == orderStatusNumVO.getWaitPayNum() ? "0" : orderStatusNumVO.getWaitPayNum().toString());
        // 待发货订单数量
        shopDashboardVO.setWaitShipOrderNum(null == orderStatusNumVO.getWaitShipNum() ? "0" : orderStatusNumVO.getWaitShipNum().toString());
        // 待收货订单数量
        shopDashboardVO.setWaitDeliveryOrderNum(null == orderStatusNumVO.getWaitRogNum() ? "0" : orderStatusNumVO.getWaitRogNum().toString());
        // 待处理申请售后订单数量
        shopDashboardVO.setAftersaleOrderNum(null == orderStatusNumVO.getRefundNum() ? "0" : orderStatusNumVO.getRefundNum().toString());

        return shopDashboardVO;
    }

}
