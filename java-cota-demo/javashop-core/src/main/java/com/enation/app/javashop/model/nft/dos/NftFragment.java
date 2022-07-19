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
 * @Description Nft碎片配置
 * @ClassName NftFragment
 * @since v7.2.2 下午2:43 2022/4/19
 */
@TableName(value = "es_nft_fragment")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NftFragment implements Serializable{

    private static final long serialVersionUID = -1516351770979713868L;
    /**
     * 碎片ID
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(name = "id",value = "id")
    private Long id;
    /**
     * 碎片名称
     */
    @ApiModelProperty(name = "name",value = "碎片名称")
    private String name;

    /**
     * 碎片图
     */
    @ApiModelProperty(name = "thumbnail",value = "碎片图")
    private String thumbnail;
    /**
     * 碎片描述
     */
    @ApiModelProperty(name = "description",value = "碎片描述")
    private String description;

    /**
     * 合并数
     */
    @ApiModelProperty(name = "merge_num",value = "合并数")
    private Integer mergeNum = 0;

    /**
     * 收藏品Id
     */
    @ApiModelProperty(name = "collection_id",value = "收藏品Id")
    private Long collectionId;

    /**
     * 可兑换NFT cotaId
     */
    @ApiModelProperty(name = "changable_cota_id",value = "可兑换cotaId")
    private String changableCotaId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "create_time",value = "创建时间")
    private Long createTime;

    public NftFragment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getMergeNum() {
        return mergeNum;
    }

    public void setMergeNum(Integer mergeNum) {
        this.mergeNum = mergeNum;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getChangableCotaId() {
        return changableCotaId;
    }

    public void setChangableCotaId(String changableCotaId) {
        this.changableCotaId = changableCotaId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
