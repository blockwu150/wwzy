package com.enation.app.javashop.model.base.rabbitmq;

/**
 * AMQP消息定义
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4
 * 2017-08-17 18：00
 */
public class AmqpExchange {


    /**
     * TEST
     */
    public final static String TEST_EXCHANGE = "TEST_EXCHANGE_FANOUT";


    /**
     * PC首页变化消息
     */
    public final static String PC_INDEX_CHANGE = "PC_INDEX_CHANGE";

    /**
     * 移动端首页变化消息
     */
    public final static String MOBILE_INDEX_CHANGE = "MOBILE_INDEX_CHANGE";

    /**
     * 商品变化消息
     */
    public final static String GOODS_CHANGE = "GOODS_CHANGE";

    /**
     * 商品sku变化消息
     */
    public final static String GOODS_SKU_CHANGE = "GOODS_SKU_CHANGE";

    /**
     * 商品优先级变化消息
     */
    public final static String GOODS_PRIORITY_CHANGE = "GOODS_PRIORITY_CHANGE";

    /**
     * 商品变化消息附带原因
     */
    public final static String GOODS_CHANGE_REASON = "GOODS_CHANGE_REASON";

    /**
     * 帮助变化消息
     */
    public final static String HELP_CHANGE = "HELP_CHANGE";


    /**
     * 页面生成消息
     */
    public final static String PAGE_CREATE = "PAGE_CREATE";

    /**
     * 索引生成消息
     */
    public final static String INDEX_CREATE = "INDEX_CREATE";

    /**
     * 订单创建消息
     * 没有入库
     */
    public final static String ORDER_CREATE = "ORDER_CREATE";

    /**
     * 入库失败消息
     * 入库失败
     */
    public final static String ORDER_INTODB_ERROR = "ORDER_INTODB_ERROR";

    /**
     * 订单状态变化消息
     * 带入库的
     */
    public final static String ORDER_STATUS_CHANGE = "ORDER_STATUS_CHANGE";

    /**
     * 会员登录消息
     */
    public final static String MEMEBER_LOGIN = "MEMEBER_LOGIN";


    /**
     * 会员注册消息
     */
    public final static String MEMEBER_REGISTER = "MEMEBER_REGISTER";

    /**
     * 店铺变更消息
     */
    public final static String SHOP_CHANGE_REGISTER = "SHOP_CHANGE_REGISTER";
    /**
     * 分类变更消息
     */
    public final static String GOODS_CATEGORY_CHANGE = "GOODS_CATEGORY_CHANGE";

    /**
     * 售后状态改变消息
     */
    public final static String REFUND_STATUS_CHANGE = "REFUND_STATUS_CHANGE";

    /**
     * 发送站内信息
     */
    public final static String MEMBER_MESSAGE = "MEMBER_MESSAGE";

    /**
     * 发送手机短信消息
     */
    public final static String SEND_MESSAGE = "SEND_MESSAGE";

    /**
     * 邮件发送消息
     */
    public final static String EMAIL_SEND_MESSAGE = "EMAIL_SEND_MESSAGE";

    /**
     * 商品评论消息
     */
    public final static String GOODS_COMMENT_COMPLETE = "GOODS_COMMENT_COMPLETE";
    /**
     * 网上支付
     */
    public final static String ONLINE_PAY = "ONLINE_PAY";

    /**
     * 完善个人资料
     */
    public final static String MEMBER_INFO_COMPLETE = "MEMBER_INFO_COMPLETE";

    /**
     * 站点导航栏变化消息
     */
    public final static String SITE_NAVIGATION_CHANGE = "SITE_NAVIGATION_CHANGE";


    /**
     * 商品收藏
     */
    public final static String GOODS_COLLECTION_CHANGE = "GOODS_COLLECTION_CHANGE";


    /**
     * 店铺收藏
     */
    public final static String SELLER_COLLECTION_CHANGE = "SELLER_COLLECTION_CHANGE";


    /**
     * 店铺关闭
     */
    public final static String CLOSE_STORE = "CLOSE_STORE";

    /**
     * 店铺信息发生改变
     */
    public final static String SHOP_CHANGE = "SHOP_CHANGE";


    /**
     * 店铺浏览统计
     */
    public final static String SHOP_VIEW_COUNT = "SHOP_VIEW_COUNT";

    /**
     * 商品浏览统计
     */
    public final static String GOODS_VIEW_COUNT = "GOODS_VIEW_COUNT";

    /**
     * 会员资料改变
     */
    public final static String MEMBER_INFO_CHANGE = "MEMBER_INFO_CHANGE";

    /**
     * 会员历史足迹
     */
    public final static String MEMBER_HISTORY = "MEMBER_HISTORY";

    /**
     * 搜索关键字消息
     */
    public final static String SEARCH_KEYWORDS = "SEARCH_KEYWORDS";

    /**
     * 提示词变更
     */
    public final static String GOODS_WORDS_CHANGE = "GOODS_WORDS_CHANGE";


    /**
     * 拼团成功消息
     */
    public final static String PINTUAN_SUCCESS = "PINTUAN_SUCCESS";

    /**
     * 运费模板变化消息
     */
    public final static String SHIP_TEMPLATE_CHANGE = "SHIP_TEMPLATE_CHANGE";
    /**
     * 会员商品咨询消息
     */
    public final static String MEMBER_GOODS_ASK = "MEMBER_GOODS_ASK";

    /**
     * 会员商品咨询回复消息
     */
    public final static String MEMBER_GOODS_ASK_REPLY = "MEMBER_GOODS_ASK_REPLY";

    /**
     * 商家创建换货或补发商品售后服务新订单消息
     */
    public final static String AS_SELLER_CREATE_ORDER = "AS_SELLER_CREATE_ORDER";

    /**
     * 售后服务单状态改变消息
     */
    public final static String AS_STATUS_CHANGE = "AS_STATUS_CHANGE";

    /**
     * 支付账单变化消息
     */
    public final static String PAYMENT_BILL_CHANGE = "PAYMENT_BILL_CHANGE";

    /**
     * NFT订单创建消息
     */
    public final static String NFT_ORDER_CREATE = "NFT_ORDER_CREATE";
    /**
     * NFT订单取消消息
     */
    public final static String NFT_ORDER_CANCELLED = "NFT_ORDER_CANCELLED";
    /**
     * NFT分发上链消息
     */
    public final static String NFT_MINT_SEND = "NFT_MINT_SEND";

    /**
     * NFT订单取消消息
     */
    public final static String NFT_MINT_CANCELLED = "NFT_MINT_CANCELLED";


    /**
     * NFT交易上链
     */
    public final static String NFT_TRADE_ON_CHAIN = "NFT_TRADE_ON_CHAIN";

    /**
     * NFT交易完成
     */
    public final static String NFT_TRADE_COMPLETE = "NFT_TRADE_COMPLETE";
    /**
     * NFT外部地址提醒
     */
    public final static String NFT_OUTER_ADDRESS_REMIND = "NFT_OUTER_ADDRESS_REMIND";
    /**
     * NFT买家进场
     */
    public final static String NFT_BUYER_IN = "NFT_BUYER_IN";

}
