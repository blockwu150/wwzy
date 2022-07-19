package com.enation.app.javashop.model.payment.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;


/**
 *银行转账凭证实体
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-04-16 17:28:07
 *
 * update
 * @author: liuyulei
 * @create: 2019/12/27 10:08
 * @version:3.0
 * @since:7.1.4
 */

@TableName("es_payment_bank_voucher")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BankVoucherDO implements Serializable {
    private static final long serialVersionUID = -1774822559762596945L;
    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(hidden=true)
    private Long id;

    /**
     * 会员Id
     */
    @ApiModelProperty(name="buyer_id",value="会员Id",required=true)
    private Long buyerId;

    /**
     * 开户行
     */
    @ApiModelProperty(name="bank",value="开户行",required=false)
    private String bank;
    /**
     * 账号
     */
    @ApiModelProperty(name="account",value="账号",required=false)
    private String account;
    /**
     * 开户姓名
     */
    @ApiModelProperty(name="account_name",value="开户姓名",required=false)
    private String accountName;
    /**
     * 汇款人姓名
     */
    @ApiModelProperty(name="pay_name",value="汇款人姓名",required=false)
    private String payName;


    /**
     * 凭证
     */
    @ApiModelProperty(name="voucher",value="凭证",required=true)
    private String voucher;
    /**
     * 流水凭证号
     */
    @ApiModelProperty(name="voucer_no",value="流水凭证号",required=false)
    private String voucherNo;

    /**
     * 是否已支付
     */
    @ApiModelProperty(name="is_pay",value="是否已支付",required=false)
    private Integer isPay = 0;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="create_time",value="创建时间",required=false)
    private Long createTime;

    /**
     * 金额
     */
    @ApiModelProperty(name="amount",value="金额",required=false)
    private Double amount;


    public BankVoucherDO() {
    }

    @PrimaryKeyField
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentBillDO{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankVoucherDO that = (BankVoucherDO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(isPay)
                .toHashCode();
    }
}