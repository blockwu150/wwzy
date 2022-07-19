package com.enation.app.javashop.service.trade.complain.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.mapper.trade.complain.OrderComplainMapper;
import com.enation.app.javashop.model.errorcode.TradeErrorCode;
import com.enation.app.javashop.model.trade.complain.dos.OrderComplain;
import com.enation.app.javashop.model.trade.complain.dos.OrderComplainCommunication;
import com.enation.app.javashop.model.trade.complain.dto.ComplainDTO;
import com.enation.app.javashop.model.trade.complain.dto.ComplainQueryParam;
import com.enation.app.javashop.model.trade.complain.enums.ComplainQueryTagEnum;
import com.enation.app.javashop.model.trade.complain.enums.ComplainSkuStatusEnum;
import com.enation.app.javashop.model.trade.complain.enums.ComplainStatusEnum;
import com.enation.app.javashop.model.trade.complain.vo.ComplainFlow;
import com.enation.app.javashop.model.trade.complain.vo.OrderComplainVO;
import com.enation.app.javashop.model.trade.order.dto.OrderDetailDTO;
import com.enation.app.javashop.model.trade.order.dto.OrderSkuDTO;
import com.enation.app.javashop.model.trade.order.vo.OrderFlowNode;
import com.enation.app.javashop.service.trade.complain.OrderComplainCommunicationManager;
import com.enation.app.javashop.service.trade.complain.OrderComplainManager;
import com.enation.app.javashop.service.trade.order.OrderOperateManager;
import com.enation.app.javashop.service.trade.order.OrderQueryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 交易投诉表业务类
 *
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-27 16:48:27
 */
@Service
public class OrderComplainManagerImpl implements OrderComplainManager {

    @Autowired
    private OrderComplainMapper orderComplainMapper;
    @Autowired
    private OrderQueryManager orderQueryManager;

    @Autowired
    private OrderComplainCommunicationManager orderComplainCommunicationManager;

    @Autowired
    private OrderOperateManager orderOperateManager;

    /**
     * 查询交易投诉表列表
     * @param param 条件参数
     * @return WebPage 交易投诉表分页数据
     */
    @Override
    public WebPage list(ComplainQueryParam param) {

        QueryWrapper<OrderComplain> queryWrapper = new QueryWrapper<OrderComplain>()
                //如果会员id不为空，则查询会员条件
                .eq(param.getMemberId() != null, "member_id", param.getMemberId())
                //如果商家id不为空，则查询商家条件
                .eq(param.getSellerId() != null, "seller_id", param.getSellerId())
                //如果订单编号不为空，则查询订单编号
                .eq(!StringUtil.isEmpty(param.getOrderSn()), "order_sn", param.getOrderSn())
                //如果关键字不为空，则模糊匹配订单号，商品名称，内容
                .and(!StringUtil.isEmpty(param.getKeywords()), ew -> {
                    ew.like("order_sn", param.getKeywords()).or().like("goods_name", param.getKeywords())
                            .or().like("content", param.getKeywords())
                            .or().like("complain_topic", param.getKeywords());
                });

        // 按标签查询
        String tag = param.getTag();
        if (!StringUtil.isEmpty(tag)) {
            ComplainQueryTagEnum tagEnum = ComplainQueryTagEnum.valueOf(tag);
            switch (tagEnum) {

                case ALL:
                    break;
                //进行中
                case COMPLAINING:

                    queryWrapper.ne("status", ComplainStatusEnum.COMPLETE.name()).ne("status", ComplainStatusEnum.CANCEL.name());
                    break;
                //已完成
                case COMPLETE:
                    queryWrapper.eq("status", ComplainStatusEnum.COMPLETE.name());
                    break;
                //已撤销
                case CANCELED:
                    queryWrapper.eq("status", ComplainStatusEnum.CANCEL.name());
                    break;
                default: {
                    break;
                }
            }
        }

        //创建时间倒叙
        queryWrapper.orderByDesc("create_time");

        Page<OrderComplain> iPage = orderComplainMapper.selectPage(new Page<>(param.getPageNo(), param.getPageSize()), queryWrapper);

        return PageConvert.convert(iPage);
    }

    /**
     * 添加交易投诉表
     * @param complain 交易投诉表
     * @return OrderComplain 交易投诉表
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain add(ComplainDTO complain) {

        Buyer buyer = UserContext.getBuyer();
        String orderSn = complain.getOrderSn();
        //查询该交易投诉的订单
        OrderDetailDTO order = orderQueryManager.getModel(orderSn);
        //遍历该订单所有sku，找到该交易投诉对应的sku商品
        List<OrderSkuDTO> skuList = order.getOrderSkuList();
        OrderSkuDTO skuGoods = null;
        for (OrderSkuDTO sku : skuList) {
            if (sku.getSkuId().equals(complain.getSkuId())) {
                skuGoods = sku;
                break;
            }
        }
        if (skuGoods == null) {
            throw new ServiceException(TradeErrorCode.E472.code(), "您要投诉的商品不存在");
        }
        OrderComplain orderComplain = new OrderComplain();
        BeanUtil.copyProperties(complain, orderComplain);
        orderComplain.setCreateTime(DateUtil.getDateline());
        orderComplain.setStatus(ComplainStatusEnum.NEW.name());
        //商品信息
        orderComplain.setGoodsId(skuGoods.getGoodsId());
        orderComplain.setGoodsName(skuGoods.getName());
        orderComplain.setGoodsPrice(skuGoods.getPurchasePrice());
        orderComplain.setNum(skuGoods.getNum());
        orderComplain.setGoodsImage(skuGoods.getGoodsImage());
        //订单信息
        orderComplain.setOrderPrice(order.getOrderPrice());
        orderComplain.setOrderTime(order.getCreateTime());
        orderComplain.setShippingPrice(order.getShippingPrice());
        orderComplain.setShipNo(order.getShipNo());
        orderComplain.setShipMobile(order.getShipMobile());
        String addr = order.getShipProvince() + order.getShipCity() + order.getShipCounty();
        if (order.getShipTown() != null) {
            addr += order.getShipTown();
        }
        orderComplain.setShipAddr(addr + order.getShipAddr());
        orderComplain.setShipName(order.getShipName());
        //会员信息
        orderComplain.setMemberId(buyer.getUid());
        orderComplain.setMemberName(buyer.getUsername());
        //商家信息
        orderComplain.setSellerId(order.getSellerId());
        orderComplain.setSellerName(order.getSellerName());

        this.orderComplainMapper.insert(orderComplain);

        //更新订单商品的可投诉状态
        orderOperateManager.updateOrderItemsComplainStatus(order.getSn(), skuGoods.getSkuId(), orderComplain.getComplainId(), ComplainSkuStatusEnum.APPLYING);

        return orderComplain;
    }

    /**
     * 修改交易投诉表
     * @param orderComplain 交易投诉表
     * @param id 交易投诉表主键
     * @return OrderComplain 交易投诉表
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain edit(OrderComplain orderComplain, Long id) {

        orderComplain.setComplainId(id);
        this.orderComplainMapper.updateById(orderComplain);
        return orderComplain;
    }

    /**
     * 删除交易投诉表
     * @param id 交易投诉表主键
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Long id) {
        this.orderComplainMapper.deleteById(id);
    }

    /**
     * 获取交易投诉表
     * @param id 交易投诉表主键
     * @return OrderComplain  交易投诉表
     */
    @Override
    public OrderComplain getModel(Long id) {
        return this.orderComplainMapper.selectById(id);
    }

    /**
     * 撤销某个交易投诉
     * @param id 投诉id
     * @return 交易投诉表实体
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain cancel(Long id) {

        Buyer buyer = UserContext.getBuyer();
        OrderComplain model = this.getModel(id);
        if (model == null || !model.getMemberId().equals(buyer.getUid())) {
            throw new ServiceException(TradeErrorCode.E472.code(), "您无法操作该交易");
        }
        //设置交易投诉为已撤销状态
        model.setStatus(ComplainStatusEnum.CANCEL.name());
        this.orderComplainMapper.updateById(model);

        //更新订单商品的可投诉状态
        orderOperateManager.updateOrderItemsComplainStatus(model.getOrderSn(), model.getSkuId(), null, ComplainSkuStatusEnum.COMPLETE);

        return model;
    }

    /**
     * 审核并交由商家申诉
     * @param id 投诉id
     * @return 交易投诉表实体
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain auth(Long id) {

        //查询，并查看是否真实存在该交易
        OrderComplain model = this.checkModel(id);
        //设置交易投诉为待申诉状态
        model.setStatus(ComplainStatusEnum.WAIT_APPEAL.name());
        this.orderComplainMapper.updateById(model);

        return model;
    }

    /**
     * 管理员仲裁结束流程
     * @param id 投诉id
     * @param arbitrationResult 仲裁结果
     * @return 交易投诉表实体
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain complete(Long id, String arbitrationResult) {
        //查询，并查看是否真实存在该交易
        OrderComplain model = this.checkModel(id);
        //修改交易投诉为已完成状态
        model.setArbitrationResult(arbitrationResult);
        model.setStatus(ComplainStatusEnum.COMPLETE.name());
        this.orderComplainMapper.updateById(model);

        //更新订单商品的可投诉状态
        orderOperateManager.updateOrderItemsComplainStatus(model.getOrderSn(), model.getSkuId(), null, ComplainSkuStatusEnum.COMPLETE);

        return model;
    }

    /**
     * 商家申诉
     * @param id 投诉id
     * @param appealContent 申诉内容
     * @param images 图片
     * @return 交易投诉表实体
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain appeal(Long id, String appealContent, String[] images) {
        //查询，并查看是否真实存在该交易
        OrderComplain model = this.checkModel(id);
        //设置商家申诉内容
        model.setAppealContent(appealContent);
        //设置商家申诉上传的图片
        if (images != null) {
            model.setAppealImages(StringUtil.arrayToString(images, ","));
        }
        //设置商家申诉时间
        model.setAppealTime(DateUtil.getDateline());
        //设置投诉状态为对话中
        model.setStatus(ComplainStatusEnum.COMMUNICATION.name());
        this.orderComplainMapper.updateById(model);

        return model;
    }

    /**
     * 提交仲裁
     * @param id 投诉id
     * @return 交易投诉表实体
     */
    @Override
    @Transactional(value = "tradeTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderComplain arbitrate(Long id) {
        //查询，并查看是否真实存在该交易
        OrderComplain model = this.checkModel(id);
        //设置投诉状态为等待仲裁
        model.setStatus(ComplainStatusEnum.WAIT_ARBITRATION.name());
        this.orderComplainMapper.updateById(model);
        return model;
    }

    /**
     * 获取交易投诉及对话信息
     * @param id 投诉id
     * @return 交易投诉表实体
     */
    @Override
    public OrderComplainVO getModelAndCommunication(Long id) {
        //查询交易投诉对话列表
        List<OrderComplainCommunication> list = orderComplainCommunicationManager.list(id);
        //获取交易投诉信息
        OrderComplain orderComplain = this.getModel(id);

        OrderComplainVO res = new OrderComplainVO();
        BeanUtil.copyProperties(orderComplain, res);

        res.setCommunicationList(list);

        return res;
    }

    /**
     * 查询交易投诉的流程图
     * @param id 投诉id
     * @return 交易投诉流程图
     */
    @Override
    public List<OrderFlowNode> getComplainFlow(Long id) {

        OrderComplain model = this.getModel(id);
        // 交易投诉状态
        String status = model.getStatus();
        // 获取已撤销流程
        if (ComplainStatusEnum.CANCEL.name().equals(status)) {
            return ComplainFlow.getCancelFlow();
        }

        //获取交易投诉正常流程
        List<OrderFlowNode> flowList = ComplainFlow.getNormalFlow();

        //遍历流程，设置当前状态之前的流程为展示，之后的流程为灰色
        boolean isEnd = false;
        for (OrderFlowNode flow : flowList) {

            Integer showStatus = isEnd ? 0 : 1;
            flow.setShowStatus(showStatus);

            if (flow.getOrderStatus().equals(status)) {
                //当前状态往后的流程都是没走的，所以是灰色
                isEnd = true;
            }
        }

        return flowList;
    }

    /**
     * 查询，并查看是否真实存在该交易
     *
     * @param id
     * @return
     */
    private OrderComplain checkModel(Long id) {

        OrderComplain model = this.getModel(id);
        if (model == null) {
            throw new ServiceException(TradeErrorCode.E472.code(), "您无法操作该交易");
        }

        return model;
    }
}
