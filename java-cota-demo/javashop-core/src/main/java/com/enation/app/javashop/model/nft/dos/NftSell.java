package com.enation.app.javashop.model.nft.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 *nft销售单
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

@TableName("es_nft_sell")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftSell implements Serializable {
    private static final long serialVersionUID = -7223870428455952620L;
    public static final int STAUS_OPEN = 0;
    public static final int STAUS_PROCESSING = 1;
    public static final int STAUS_BUYER_PAID = 2;
    public static final int STAUS_AUDIT = 3;
    public static final int STAUS_CANCEL = 4;
    public static final int STAUS_SELLER_RECEIVE_MONEY = 5;
    public static final int STAUS_COMPLETE = 9;

    public static final int PROCESS_SECONDS = 15*60;


    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name="id",value="NFT销售单ID",hidden=true)
    private Long id;
//    /**
//     * nft o Id
//     */
//    @ApiModelProperty(name="object_id",value="NFT对象ID",required=true)
//    private Long objectId;

    /**
     * 卖家Id
     */
    @ApiModelProperty(name="seller_id",value="销售会员ID",required=true)
    private Long sellerId;

    /**
     * 买家Id
     */
    @ApiModelProperty(name="buyer_id",value="购买会员ID",required=true)
    private Long buyerId;


    /**
     * 价格
     */
    @ApiModelProperty(name="price",value="价格",required=false)
    private Double price;


    /**
     * 备注
     */
    @ApiModelProperty(name="ramark",value="备注",required=false)
    private String remark;


    /**
     * 销售单状态
     */
    @ApiModelProperty(name="status",value="销售单状态 0 OPEN 1 PROCESSING 2 BUYER_PAID 3 AUDIT 4 CANCELL 5 RECEIVEMONEY 9 COMPLETE",required=false)
    private Integer status = STAUS_OPEN;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="create_time",value="创建时间",required=false)
    private Long createTime=System.currentTimeMillis()/1000;

    /**
     * 启动时间
     */
    @ApiModelProperty(name="process_time",value="启动时间",required=false)
    private Long processTime ;

    /**
     * 交易对话
     */
    @ApiModelProperty(name="conversation_id",value="当前交易对话",required=false)
    private Long conversationId;

    /**
     * 标记
     */
    @ApiModelProperty(name = "token_index",value = "铸造指针")
    private String tokenIndex;

    /**
     * 收藏品CotaId
     */
    @ApiModelProperty(name = "cota_id",value = "收藏品CotaId")
    private String cotaId;
    /**
     * 收藏品图片
     */
    @ApiModelProperty(name = "image",value = "收藏品图片")
    private String image;
    /*
     * 收藏品名称
     */
    @ApiModelProperty(name = "name",value = "收藏品名称")
    private String name;
    /**
     * 收藏品描述
     */
    @ApiModelProperty(name = "description",value = "收藏品描述")
    private String description;

    /**
     * 流通费用
     */
    @ApiModelProperty(name = "fee",value = "流通费用")
    private Integer fee;

    /**
     * 消费NFT状态
     */
    @ApiModelProperty(name = "state_id",value = "消费NFT状态")
    private String stateId;

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
     * 交易ID
     */
    @ApiModelProperty(name="ckb_tx_id",value="ckbTxId",required=false)
    private Long ckbTxId;

    /**
     * 交易ID
     */
    @ApiModelProperty(name="nft_tx_id",value="nftTxId",required=false)
    private Long nftTxId;



    public NftSell() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

//    public Long getObjectId() {
//        return objectId;
//    }
//
//    public void setObjectId(Long objectId) {
//        this.objectId = objectId;
//    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
    }

    public String getTokenIndex() {
        return tokenIndex;
    }

    public void setTokenIndex(String tokenIndex) {
        this.tokenIndex = tokenIndex;
    }

    public String getCotaId() {
        return cotaId;
    }

    public void setCotaId(String cotaId) {
        this.cotaId = cotaId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
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

    public Long getCkbTxId() {
        return ckbTxId;
    }

    public void setCkbTxId(Long ckbTxId) {
        this.ckbTxId = ckbTxId;
    }

    public Long getNftTxId() {
        return nftTxId;
    }

    public void setNftTxId(Long nftTxId) {
        this.nftTxId = nftTxId;
    }
}
