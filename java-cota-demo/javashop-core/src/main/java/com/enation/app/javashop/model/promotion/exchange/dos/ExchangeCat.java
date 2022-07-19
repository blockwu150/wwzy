package com.enation.app.javashop.model.promotion.exchange.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 积分兑换分类实体
 * @author Snow
 * @version v7.0.0
 * @since v7.0.0
 * 2018-05-29 16:56:22
 */
@TableName(value = "es_exchange_cat")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ExchangeCat implements Serializable {

    private static final long serialVersionUID = 8629683606901701L;

    /**分类id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(hidden=true)
    private Long categoryId;
    /**分类名称*/
    @ApiModelProperty(name="name",value="分类名称",required=false)
    private String name;
    /**父分类*/
    @ApiModelProperty(name="parent_id",value="父分类",required=false)
    private Long parentId;
    /**分类id路径*/
    @ApiModelProperty(name="category_path",value="分类id路径",required=false)
    private String categoryPath;
    /**商品数量*/
    @ApiModelProperty(name="goods_count",value="商品数量",required=false)
    private Integer goodsCount;
    /**分类排序*/
    @ApiModelProperty(name="category_order",value="分类排序",required=false)
    private Integer categoryOrder;
    /**是否在页面上显示*/
    @ApiModelProperty(name="list_show",value="是否在页面上显示",required=false,example = "1为显示，0为不显示")
    private Integer listShow;
    /**分类图片*/
    @ApiModelProperty(name="image",value="分类图片",required=false)
    private String image;

    @PrimaryKeyField
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCategoryPath() {
        return categoryPath;
    }
    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Integer getCategoryOrder() {
        return categoryOrder;
    }
    public void setCategoryOrder(Integer categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    public Integer getListShow() {
        return listShow;
    }
    public void setListShow(Integer listShow) {
        this.listShow = listShow;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


	@Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ExchangeCat that = (ExchangeCat) o;
        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) {return false;}
        if (name != null ? !name.equals(that.name) : that.name != null) {return false;}
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) {return false;}
        if (categoryPath != null ? !categoryPath.equals(that.categoryPath) : that.categoryPath != null) {return false;}
        if (goodsCount != null ? !goodsCount.equals(that.goodsCount) : that.goodsCount != null) {return false;}
        if (categoryOrder != null ? !categoryOrder.equals(that.categoryOrder) : that.categoryOrder != null) {return false;}
        if (listShow != null ? !listShow.equals(that.listShow) : that.listShow != null) {return false;}
        return image != null ? image.equals(that.image) : that.image == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (categoryPath != null ? categoryPath.hashCode() : 0);
        result = 31 * result + (goodsCount != null ? goodsCount.hashCode() : 0);
        result = 31 * result + (categoryOrder != null ? categoryOrder.hashCode() : 0);
        result = 31 * result + (listShow != null ? listShow.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExchangeCat{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", categoryPath='" + categoryPath + '\'' +
                ", goodsCount=" + goodsCount +
                ", categoryOrder=" + categoryOrder +
                ", listShow=" + listShow +
                ", image='" + image + '\'' +
                '}';
    }


}
