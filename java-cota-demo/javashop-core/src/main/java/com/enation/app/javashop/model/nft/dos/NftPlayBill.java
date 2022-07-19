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
 * @Description 海报
 * @ClassName NftCollection
 * @since v7.2.2 下午2:43 2022/4/20
 */
@TableName(value = "es_nft_play_bill")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftPlayBill implements Serializable {


    private static final long serialVersionUID = -7946778435673042014L;
    /**
     * 专辑Id
     */
    @TableId(type= IdType.INPUT)
    @ApiModelProperty(name = "collection_id",value = "收藏品Id")
    private Long collectionId;

    /**
     * 头部图片
     */
    @ApiModelProperty(name = "head_image",value = "头部图片")
    private String headImage;
    /**
     * 介绍图片
     */
    @ApiModelProperty(name = "descript_image",value = "介绍图片")
    private String descriptImage;
    /**
     * 证书图片
     */
    @ApiModelProperty(name = "cert_image",value = "证书图片")
    private String certImage;

    public NftPlayBill() {
    }

    public NftPlayBill(Long collectionId, String headImage, String descriptImage, String certImage) {
        this.collectionId = collectionId;
        this.headImage = headImage;
        this.descriptImage = descriptImage;
        this.certImage = certImage;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getDescriptImage() {
        return descriptImage;
    }

    public void setDescriptImage(String descriptImage) {
        this.descriptImage = descriptImage;
    }

    public String getCertImage() {
        return certImage;
    }

    public void setCertImage(String certImage) {
        this.certImage = certImage;
    }
}
