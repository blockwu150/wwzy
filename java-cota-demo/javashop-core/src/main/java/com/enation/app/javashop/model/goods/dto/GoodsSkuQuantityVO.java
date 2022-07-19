package com.enation.app.javashop.model.goods.dto;

import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 商品sku库存
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月23日 上午11:26:49
 */
@ApiIgnore
public class GoodsSkuQuantityVO implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 2050291085109943997L;

	/**
	 * 库存数量
	 */
	@ApiModelProperty(name="quantity_count",value="库存数量")
	@Max(value = 99999999,message = "库存数量不正确")
	@Min(value = 0,message = "库存数量不正确")
	private Integer quantityCount;

	/**
	 * skuid
	 */
	@ApiModelProperty(name="sku_id",value="sku值")
	private Long skuId;

	public Integer getQuantityCount() {
		return quantityCount;
	}

	public void setQuantityCount(Integer quantityCount) {
		this.quantityCount = quantityCount;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	@Override
	public String toString() {
		return "GoodsSkuQuantityVO [quantityCount=" + quantityCount + ", skuId=" + skuId + "]";
	}

}
