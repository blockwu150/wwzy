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

@TableName("es_nft_transaction_param")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransactionParam implements Serializable {

    public static final int PARAM_TYPE_ONCE= 0;
    public static final int PARAM_TYPE_REPEATED = 1;
    private static final long serialVersionUID = 8107229341336099356L;

    @TableId(type= IdType.INPUT)
    @ApiModelProperty(name="transaction_id",value="交易ID",hidden=true)
    private Long transactionId;

    /**
     * 参数类型
     */
    @ApiModelProperty(name="type",value="参数类型",required=true)
    private int type;

    /**
     * 参数值
     */
    @ApiModelProperty(name="value",value="参数值",required=true)
    private String value;


    public TransactionParam() {

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
