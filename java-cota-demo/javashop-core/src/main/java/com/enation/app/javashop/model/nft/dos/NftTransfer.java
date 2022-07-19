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
 *nft转让
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

@TableName("es_nft_transfer")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftTransfer implements Serializable {

    private static final long serialVersionUID = -1037583750367443896L;
    public static final int TRANSFER_TYPE_FREE = 0;
    public static final int TRANSFER_TYPE_TRADE = 1;
    public static final int ASSET_CKB = 0;
    public static final int ASSET_NFT = 1;
    public static final int TRANSFER_STATUS_NEW = 0;
    public static final int TRANSFER_STATUS_TXED = 1;
    public static final int TRANSFER_STATUS_SUCCEED = 2;
    public static final int TRANSFER_STATUS_CANCELLED = 3;

    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name="transfer_id",value="NFT转让ID",hidden=true)
    private Long transferId;

    /**
     * 转让类型
     */
    @ApiModelProperty(name="type",value="转让类型",required=true)
    private int type;

    /**
     * 资产类型
     */
    @ApiModelProperty(name="asset",value="资产类型",required=true)
    private int asset;

    /**
     * 转出会员ID
     */
    @ApiModelProperty(name="member_id",value="转出会员ID",required=true)
    private Long memberId;

    /**
     * 转出地址
     */
    @ApiModelProperty(name="from_address",value="转出地址",required=true)
    private String fromAddress;

    /**
     * 转入地址
     */
    @ApiModelProperty(name="to_address",value="转入地址",required=true)
    private String toAddress;


    /**
     * 铸造指针
     */
    @ApiModelProperty(name = "token_index",value = "铸造指针")
    private String tokenIndex;

    /**
     * 收藏品CotaId
     */
    @ApiModelProperty(name = "cota_id",value = "收藏品CotaId")
    private String cotaId;

    /**
     * 金额
     */
    @ApiModelProperty(name="amount",value="金额",required=true)
    private int amount;

    /**
     * 交易ID
     */
    @ApiModelProperty(name="tx_id",value="txId",required=false)
    private Long txId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="create_time",value="创建时间",required=false)
    private Long createTime=System.currentTimeMillis()/1000;
    /**
     * 完成时间
     */
    @ApiModelProperty(name="complete_time",value="完成时间",required=false)
    private Long completeTime;

    /**
     * 交易状态
     */
    @ApiModelProperty(name="status",value="交易状态",required=true)
    private int status = TRANSFER_STATUS_NEW;

    public NftTransfer() {

    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAsset() {
        return asset;
    }

    public void setAsset(int asset) {
        this.asset = asset;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Long getTxId() {
        return txId;
    }

    public void setTxId(Long txId) {
        this.txId = txId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
