package com.enation.app.javashop.model.nft.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 *上架藏品模版
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-04-16 17:28:07
 *
 * update
 * @author: liuyulei
 * @create: 2022/05/03 10:08
 * @version:3.0
 * @since:7.1.4
 */

@TableName("es_nft_order")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftOrder implements Serializable {

    private static final long serialVersionUID = 4002852694891354672L;

    public static final int ORDER_STAUS_NEW = 0;
    public static final int ORDER_STAUS_PAID = 1;
    public static final int ORDER_STAUS_DELIVERY = 2;
    public static final int ORDER_STAUS_CANCELL = 3;
    public static final int ORDER_STAUS_REFUND = 4;
    public static final int ORDER_STAUS_COMPLETE = 9;

    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name="order_id",value="NFT订单ID",hidden=true)
    private Long orderId;

    /**
     * 会员Id
     */
    @ApiModelProperty(name="member_id",value="会员ID",required=true)
    private Long memberId;

    /**
     * 专辑ID
     */
    @ApiModelProperty(name="album_id",value="专辑ID",required=true)
    private Long albumId;

    /**
     * 收藏品模版ID
     */
    @ApiModelProperty(name="collection_id",value="收藏品模版ID",required=true)
    private Long collectionId;

    /**
     * 价格
     */
    @ApiModelProperty(name="price",value="价格",required=false)
    private Double price;
    /**
     * 价格
     */
    @ApiModelProperty(name="num",value="数量",required=false)
    private Integer num;
    /**
     * 价格
     */
    @ApiModelProperty(name="amount",value="金额",required=false)
    private Double amount;

    /**
     * 备注
     */
    @ApiModelProperty(name="ramark",value="备注",required=false)
    private String remark;

    /**
     * 交易哈希码
     */
    @ApiModelProperty(name="tx_hash",value="txHash",required=false)
    private String txHash;
    /**
     * 交易哈希状态
     */
    @ApiModelProperty(name="tx_hash_status",value="txHashState",required=false)
    private String txHashStatus;

    /**
     * 订单状态
     */
    @ApiModelProperty(name="status",value="订单状态 0 NEW 1 PAID 2 DELIVERY 3 CANCELL 4 REFUND 9 COMPLETE",required=false)
    private Integer status = ORDER_STAUS_NEW;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="create_time",value="创建时间",required=false)
    private Long createTime=System.currentTimeMillis()/1000;
    /**
     * 分发ID
     */
    @ApiModelProperty(name="mint_id",value="分发ID",required=false)
    private Long mintId;

    public NftOrder() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getTxHashStatus() {
        return txHashStatus;
    }

    public void setTxHashStatus(String txHashStatus) {
        this.txHashStatus = txHashStatus;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getMintId() {
        return mintId;
    }

    public void setMintId(Long mintId) {
        this.mintId = mintId;
    }
}
