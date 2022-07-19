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
 *nft分发
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

@TableName("es_nft_mint")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftMint implements Serializable {

    private static final long serialVersionUID = 371046349357594122L;
    public static final int MINT_TYPE_FREE = 0;
    public static final int MINT_TYPE_BUY = 1;
    public static final int MINT_STATUS_NEW = 0;
    public static final int MINT_STATUS_TXED = 1;
    public static final int MINT_STATUS_SUCCEED = 2;
    public static final int MINT_STATUS_CANCELLED = 3;

    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name="mint_id",value="NFT分发ID",hidden=true)
    private Long mintId;

    /**
     * 分组ID
     */
    @ApiModelProperty(name="group_id",value="分组ID",required=false)
    private Long groupId;

    /**
     * 分发类型
     */
    @ApiModelProperty(name="type",value="分发类型",required=true)
    private int type;

    /**
     * 会员Id
     */
    @ApiModelProperty(name="member_id",value="会员ID",required=true)
    private Long memberId;

    /**
     * 专辑ID
     */
    @ApiModelProperty(name="album_id",value="专辑ID",required=false)
    private Long albumId;

    /**
     * 收藏品模版ID
     */
    @ApiModelProperty(name="collection_id",value="收藏品模版ID",required=true)
    private Long collectionId;

    /**
     * 数量
     */
    @ApiModelProperty(name="num",value="数量",required=false)
    private Integer num;
    /**
     * 备注
     */
    @ApiModelProperty(name="ramark",value="备注",required=false)
    private String remark;

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
     * 分发状态
     */
    @ApiModelProperty(name="status",value="分发状态",required=true)
    private int status = MINT_STATUS_NEW;

    public NftMint() {
    }

    public Long getMintId() {
        return mintId;
    }

    public void setMintId(Long mintId) {
        this.mintId = mintId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
