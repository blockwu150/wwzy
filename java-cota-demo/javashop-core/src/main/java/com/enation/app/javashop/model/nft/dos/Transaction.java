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
 *nft交易
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

@TableName("es_nft_transaction")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Transaction implements Serializable {
    private static final long serialVersionUID = 2611248547065646648L;
    public static final int TX_TYPE_MINT = 0;
    public static final int TX_TYPE_NFT_TRANSFER = 1;
    public static final int TX_TYPE_CKB_TRANSFER = 2;
    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name="transaction_id",value="交易ID",hidden=true)
    private Long transactionId;

    /**
     * 交易类型
     */
    @ApiModelProperty(name="type",value="交易类型",required=true)
    private int type;


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

    public Transaction() {

    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
