package com.enation.app.javashop.model.nft.dos;

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

@TableName("es_nft_upload_apply")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UploadApply implements Serializable {

    private static final long serialVersionUID = -1433968021564701964L;

    public static final int STAUS_APPLY = 0;
    public static final int STAUS_UPLOADED = 1;
    public static final int STAUS_CANCELLED = 2;

    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(hidden=true)
    private Long id;

    /**
     * 会员Id
     */
    @ApiModelProperty(name="collection_id",value="收藏品模版ID",required=true)
    private Long collectionId;

    /**
     * 开户行
     */
    @ApiModelProperty(name="head_image",value="海报头部宣传画",required=false)
    private String headImage;
    /**
     * 账号
     */
    @ApiModelProperty(name="descript_image",value="海报宣传画",required=false)
    private String descriptImage;
    /**
     * 开户姓名
     */
    @ApiModelProperty(name="cert_image",value="收藏品证书",required=false)
    private String certImage;

    /**
     * 价格
     */
    @ApiModelProperty(name="price",value="价格",required=false)
    private Double price;

    /**
     * 是否已上架
     */
    @ApiModelProperty(name="status",value="是否已上架 0 apply 1 uploaded 2 cancel",required=false)
    private Integer status = STAUS_APPLY;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="create_time",value="创建时间",required=false)
    private Long createTime=System.currentTimeMillis()/1000;

    public UploadApply() {
    }

    @PrimaryKeyField
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
