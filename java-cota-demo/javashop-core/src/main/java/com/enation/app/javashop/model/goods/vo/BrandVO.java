package com.enation.app.javashop.model.goods.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 品牌vo
 * 
 * @author fk
 * @version v1.0
 * @since v7.0 2018年3月16日 下午4:44:55
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BrandVO {

	@ApiModelProperty(value="品牌名称")
	private String name;

	@ApiModelProperty(value="品牌图标")
	private String logo;

	@ApiModelProperty(value="品牌id")
	private Long brandId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	@Override
	public String toString() {
		return "BrandVO{" +
				"name='" + name + '\'' +
				", logo='" + logo + '\'' +
				", brandId=" + brandId +
				'}';
	}
}
