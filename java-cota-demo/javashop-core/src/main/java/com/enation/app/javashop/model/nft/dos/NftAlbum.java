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
 * @Description Nft专辑
 * @ClassName NftAlbum
 * @since v7.2.2 下午2:43 2022/4/20
 */
@TableName(value = "es_nft_album")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftAlbum implements Serializable {
    private static final long serialVersionUID = -4697373859161852233L;
    private static final Integer TAG_DEFAULT=0;
    private static final Integer TAG_HOT=1;

    /**
     * 主键ID
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(name = "album_id", value = "id")
    private Long albumId;
    /**
     * 专辑名称
     */
    @ApiModelProperty(name = "name",value = "专辑名称")
    private String name;
    /**
     * 专辑封面
     */
    @ApiModelProperty(name = "face",value = "专辑封面")
    private String face;
    /**
     * 专辑描述
     */
    @ApiModelProperty(name = "description",value = "专辑描述")
    private String description;

    /**
     * 数量
     */
    @ApiModelProperty(name = "num",value = "数量")
    private Integer num;

    /**
     *发行者
     */
    @ApiModelProperty(name = "issuer_id",value = "发行者Id")
    private Long issuerId;

    /**
     * 开放时间
     */
    @ApiModelProperty(name = "open_time",value = "开放时间")
    private Long openTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "create_time",value = "创建时间")
    private Long createTime;

    /**
     * 数量
     */
    @ApiModelProperty(name = "tag",value = "标签")
    private Integer tag=0;

    public NftAlbum() {
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

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long creatTime) {
        this.createTime = creatTime;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }
}
