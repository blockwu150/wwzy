package com.enation.app.javashop.model.nft.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.elasticsearch.script.Script;

import java.io.Serializable;

/**
 * @author ygg
 * @version v7.2.2
 * @Description Nft积分(碎片)
 * @ClassName NftPoint
 * @since v7.2.2 下午2:43 2020/8/5
 */
@TableName(value = "es_nft_point")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftPoint implements Serializable {

    private static final long serialVersionUID = -2540486202242524948L;

    public static final int STAUS_PROCESSING = 0;
    public static final int STAUS_SUCCESS = 1;
    public static final int STAUS_CANCEL = 2;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(name = "id", value = "id")
    private Long Id;
    /**
     * 会员Id
     */
    @ApiModelProperty(name = "member_id", value = "会员Id")
    private Long memberId;

    /**
     * 碎片Id
     */
    @ApiModelProperty(name = "fragment_id", value = "碎片Id")
    private Long fragmentId;

    /**
     * 合成状态
     */
    @ApiModelProperty(name = "merged", value = "合成状态")
    private Boolean merged = false;

    /**
     * 激活状态
     */
    @ApiModelProperty(name = "active", value = "激活状态")
    private int active = STAUS_PROCESSING;

    /**
     * 交易Id
     */
    @ApiModelProperty(name = "tx_id", value = "交易Id")
    private Long txId ;

    /**
     * 交易时间
     */
    @ApiModelProperty(name = "tx_time", value = "交易时间")
    private Long txTime;

    /**
     * 兑换方向
     */
    @ApiModelProperty(name = "is_nft2point", value = "兑换方向")
    private Boolean isNft2point;




    /**
     * 创建时间
     */
    @ApiModelProperty(name = "create_time", value = "创建时间")
    private Long createTime;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(Long fragmentId) {
        this.fragmentId = fragmentId;
    }

    public Boolean getMerged() {
        return merged;
    }

    public void setMerged(Boolean merged) {
        this.merged = merged;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Long getTxId() {
        return txId;
    }

    public void setTxId(Long txId) {
        this.txId = txId;
    }

    public Long getTxTime() {
        return txTime;
    }

    public void setTxTime(Long txTime) {
        this.txTime = txTime;
    }

    public Boolean getNft2point() {
        return isNft2point;
    }

    public void setNft2point(Boolean nft2point) {
        isNft2point = nft2point;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public NftPoint() {
    }
}
