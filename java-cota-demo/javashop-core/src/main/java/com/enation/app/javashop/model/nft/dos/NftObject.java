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

@TableName("es_nft_object")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftObject implements Serializable {

    private static final long serialVersionUID = -8632392168152264195L;
    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name="id",value="ID",hidden=true)
    private Long id;

    /**
     * 会员Id
     */
    @ApiModelProperty(name="member_id",value="会员Id",required=true)
    private Long memberId;

    /**
     * 收藏品模版ID
     */
    @ApiModelProperty(name="collection_id",value="收藏品模版ID",required=true)
    private Long collectionId;

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
     * 发行人Id
     */
    @ApiModelProperty(name = "issuer_id",value = "发行人Id")
    private Long issuerId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="create_time",value="创建时间",required=false)
    private Long createTime=System.currentTimeMillis()/1000;

    public NftObject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
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

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
