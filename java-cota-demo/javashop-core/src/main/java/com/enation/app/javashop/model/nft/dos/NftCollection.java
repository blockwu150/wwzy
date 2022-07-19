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
 * @author ygg
 * @version v7.2.2
 * @Description Nft收藏
 * @ClassName NftCollection
 * @since v7.2.2 下午2:43 2022/4/20
 */
@TableName(value = "es_nft_collection")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftCollection implements Serializable {
    private static final long serialVersionUID = 3871264518313090318L;
    public static final int STATUS_APPLY=0;
    public static final int STATUS_SUCCESS=1;

    /**
     * 主键ID
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(hidden = true)
    private Long id;
    /**
     * 专辑Id
     */
    @ApiModelProperty(name = "album_id",value = "专辑Id")
    private Long albumId;
    /**
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
     * 收藏品图片
     */
    @ApiModelProperty(name = "image",value = "收藏品图片")
    private String image;
    /**
     * 数量
     */
    @ApiModelProperty(name = "num",value = "数量")
    private Integer num;

    /**
     * 价格
     */
    @ApiModelProperty(name = "price",value = "价格")
    private Double price;

    /**
     * 标记
     */
    @ApiModelProperty(name = "token_index",value = "铸造指针")
    private Integer tokenIndex;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status",value = "状态 0 等待上链 1 已生成")
    private Integer status=0;
    /**
     * 收藏品CotaId
     */
    @ApiModelProperty(name = "cota_id",value = "收藏品CotaId")
    private String cotaId;
    /**
     * 发行人Id
     */
    @ApiModelProperty(name = "issuer_id",value = "发行人Id")
    private Long issuerId;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "create_time",value = "创建时间")
    private Long createTime;

    public NftCollection() {
        createTime=System.currentTimeMillis()/1000;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTokenIndex() {
        return tokenIndex;
    }

    public void setTokenIndex(Integer tokenIndex) {
        this.tokenIndex = tokenIndex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCotaId() {
        return cotaId;
    }

    public void setCotaId(String cotaId) {
        this.cotaId = cotaId;
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
