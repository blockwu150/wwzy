package com.enation.app.javashop.model.trade.order.vo;
import	java.io.Serializable;

import com.enation.app.javashop.model.goods.vo.GoodsSkuVO;
import com.enation.app.javashop.model.goods.vo.SpecValueVO;
import com.enation.app.javashop.model.trade.cart.vo.CartPromotionVo;
import com.enation.app.javashop.model.trade.cart.vo.CartSkuVO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * 订单商品项
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel( description = "订单商品项")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderSkuVO implements Serializable {


    private static final long serialVersionUID = 6568329356458655688L;
    @ApiModelProperty(value = "卖家id")
    private Long sellerId;

    @ApiModelProperty(value = "卖家姓名")
    private String sellerName;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "产品id")
    private Long skuId;

    @ApiModelProperty(value = "产品sn")
    private String skuSn;

    @ApiModelProperty(value = "商品所属的分类id")
    private Long catId;

    @ApiModelProperty(value = "购买数量")
    private Integer num;

    @ApiModelProperty(value = "商品重量")
    private Double goodsWeight;

    @ApiModelProperty(value = "商品原价")
    private Double originalPrice;

    @ApiModelProperty(value = "购买时的成交价")
    private Double purchasePrice;

    @ApiModelProperty(value = "小计")
    private Double subtotal;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    /** 商品规格列表*/
    @ApiModelProperty(value = "规格列表")
    private List<SpecValueVO> specList;

    /** 积分换购活动中，购买这个商品需要消费的积分 */
    @ApiModelProperty(value = "使用积分")
    private Integer point;

    @ApiModelProperty(value = "快照ID")
    private Long snapshotId;

    @ApiModelProperty(value = "售后状态")
    private String serviceStatus;

    @ApiModelProperty(value = "投诉状态，投诉中，可投诉，不可投诉")
    private String complainStatus;

    @ApiModelProperty(value = "交易投诉id")
    private Long complainId;

    @ApiModelProperty(value = "已参与的单品活动工具列表")
    private List<CartPromotionVo> singleList;

    @ApiModelProperty(value = "已参与的组合活动工具列表")
    private List<CartPromotionVo> groupList;

    @ApiModelProperty(value = "商品操作允许情况" )
    private GoodsOperateAllowable goodsOperateAllowableVO;

    @ApiModelProperty(value = "此商品需要提示给顾客的优惠标签")
    private List<String> promotionTags;

    @ApiModelProperty(value = "此商品的优惠类型")
    private String promotionType;

    @ApiModelProperty(value = "优惠数量")
    private Integer purchaseNum;

    /** 此字段在订单退款时使用，针对的是商家手动修改订单价格的情况 */
    @ApiModelProperty(value = "实际支付小计")
    private Double actualPayTotal;

    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(Integer purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(Double goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public List<SpecValueVO> getSpecList() {
        return specList;
    }

    public void setSpecList(List<SpecValueVO> specList) {
        this.specList = specList;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public List<CartPromotionVo> getSingleList() {
        return singleList;
    }

    public void setSingleList(List<CartPromotionVo> singleList) {
        this.singleList = singleList;
    }

    public List<CartPromotionVo> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<CartPromotionVo> groupList) {
        this.groupList = groupList;
    }

    public GoodsOperateAllowable getGoodsOperateAllowableVO() {
        return goodsOperateAllowableVO;
    }

    public List<String> getPromotionTags() {
        return promotionTags;
    }

    public void setPromotionTags(List<String> promotionTags) {
        this.promotionTags = promotionTags;
    }

    public void setGoodsOperateAllowableVO(GoodsOperateAllowable goodsOperateAllowableVO) {
        this.goodsOperateAllowableVO = goodsOperateAllowableVO;
    }

    public Double getActualPayTotal() {
        return actualPayTotal;
    }

    public void setActualPayTotal(Double actualPayTotal) {
        this.actualPayTotal = actualPayTotal;
    }

    public String getComplainStatus() {
        return complainStatus;
    }

    public void setComplainStatus(String complainStatus) {
        this.complainStatus = complainStatus;
    }

    public Long getComplainId() {
        return complainId;
    }

    public void setComplainId(Long complainId) {
        this.complainId = complainId;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        OrderSkuVO skuVO = (OrderSkuVO) o;

        return new EqualsBuilder()
                .append(sellerId, skuVO.sellerId)
                .append(sellerName, skuVO.sellerName)
                .append(goodsId, skuVO.goodsId)
                .append(skuId, skuVO.skuId)
                .append(skuSn, skuVO.skuSn)
                .append(catId, skuVO.catId)
                .append(num, skuVO.num)
                .append(goodsWeight, skuVO.goodsWeight)
                .append(originalPrice, skuVO.originalPrice)
                .append(purchasePrice, skuVO.purchasePrice)
                .append(subtotal, skuVO.subtotal)
                .append(name, skuVO.name)
                .append(goodsImage, skuVO.goodsImage)
                .append(specList, skuVO.specList)
                .append(point, skuVO.point)
                .append(snapshotId, skuVO.snapshotId)
                .append(serviceStatus, skuVO.serviceStatus)
                .append(singleList, skuVO.singleList)
                .append(groupList, skuVO.groupList)
                .append(goodsOperateAllowableVO, skuVO.goodsOperateAllowableVO)
                .append(actualPayTotal, skuVO.actualPayTotal)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sellerId)
                .append(sellerName)
                .append(goodsId)
                .append(skuId)
                .append(skuSn)
                .append(catId)
                .append(num)
                .append(goodsWeight)
                .append(originalPrice)
                .append(purchasePrice)
                .append(subtotal)
                .append(name)
                .append(goodsImage)
                .append(specList)
                .append(point)
                .append(snapshotId)
                .append(serviceStatus)
                .append(singleList)
                .append(groupList)
                .append(goodsOperateAllowableVO)
                .append(actualPayTotal)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "OrderSkuVO{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", goodsId=" + goodsId +
                ", skuId=" + skuId +
                ", skuSn='" + skuSn + '\'' +
                ", catId=" + catId +
                ", num=" + num +
                ", goodsWeight=" + goodsWeight +
                ", originalPrice=" + originalPrice +
                ", purchasePrice=" + purchasePrice +
                ", subtotal=" + subtotal +
                ", name='" + name + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                ", specList=" + specList +
                ", point=" + point +
                ", snapshotId=" + snapshotId +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", singleList=" + singleList +
                ", groupList=" + groupList +
                ", goodsOperateAllowableVO=" + goodsOperateAllowableVO +
                ", promotionTags=" + promotionTags +
                ", actualPayTotal=" + actualPayTotal +
                '}';
    }

    public OrderSkuVO() {
    }

    public OrderSkuVO(GoodsSkuVO goodsSkuVO) {
        this.sellerId = goodsSkuVO.getSellerId();
        this.catId = goodsSkuVO.getCategoryId();
        this.goodsId = goodsSkuVO.getGoodsId();
        this.goodsImage = goodsSkuVO.getThumbnail();
        this.goodsWeight = goodsSkuVO.getWeight();
        this.name = goodsSkuVO.getGoodsName();
        this.sellerName = goodsSkuVO.getSellerName();
        this.skuSn = goodsSkuVO.getSn();
        this.originalPrice = goodsSkuVO.getPrice();
    }

    public OrderSkuVO(CartSkuVO cartSkuVO) {
        this.sellerId = cartSkuVO.getSellerId();
        this.catId = cartSkuVO.getCatId();
        this.goodsId = cartSkuVO.getGoodsId();
        this.goodsImage = cartSkuVO.getGoodsImage();
        this.goodsWeight = cartSkuVO.getGoodsWeight();
        this.name = cartSkuVO.getName();
        this.sellerName = cartSkuVO.getSellerName();
        this.skuSn = cartSkuVO.getSkuSn();
        this.num = cartSkuVO.getNum();
        this.purchaseNum = cartSkuVO.getPurchaseNum();
        this.actualPayTotal = cartSkuVO.getSubtotal();
        this.point = cartSkuVO.getPoint();
        this.subtotal = cartSkuVO.getSubtotal();
    }
}
