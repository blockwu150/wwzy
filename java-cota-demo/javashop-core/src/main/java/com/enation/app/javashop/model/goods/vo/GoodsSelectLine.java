package com.enation.app.javashop.model.goods.vo;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品选择器使用对象
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月28日 下午4:28:32
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsSelectLine {

	@ApiModelProperty(value="商品id")
	@Column(name= "goods_id")
	private Long goodsId;

	@ApiModelProperty(value="skuid")
	@Column(name= "sku_id")
	private Long skuId;

	@ApiModelProperty(value="规格信息")
	private String specs;

	@ApiModelProperty(value="商品名称")
	@Column(name= "goods_name")
	private String  goodsName;

	@ApiModelProperty(value="商品编号")
	private String sn;

	@ApiModelProperty(value="商品缩略图")
	private String thumbnail;

	@ApiModelProperty(value="商品大图")
	private String big;

	@ApiModelProperty(value="商品价格")
	private Double price ;

	@ApiModelProperty(value="库存")
	private Integer quantity;

	@ApiModelProperty(value="可用库存")
	private Integer enableQuantity;

	@ApiModelProperty(value="购买数量")
	@Column(name= "buy_count")
	private Integer buyCount;

	@ApiModelProperty(value="评论数量")
	@Column(name= "comment_num")
	private Integer commentNum;

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getEnableQuantity() {
		return enableQuantity;
	}

	public void setEnableQuantity(Integer enableQuantity) {
		this.enableQuantity = enableQuantity;
	}

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	public String getBig() {
		return big;
	}

	public void setBig(String big) {
		this.big = big;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}
}
