package com.enation.app.javashop.model.goods.vo;

import com.enation.app.javashop.model.goods.enums.QuantityType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 商品库存vo
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月7日 上午11:23:16
 */
public class GoodsQuantityVO implements Cloneable{



	private Long goodsId;

	private Long skuId;

	private Integer quantity;

	private  QuantityType quantityType;

	public GoodsQuantityVO() {}


	public GoodsQuantityVO(Long goodsId, Long skuId, Integer quantity ) {
		super();
		this.goodsId = goodsId;
		this.skuId = skuId;
		this.quantity = quantity;

	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Object object = super.clone();
		return object;
	}


	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getQuantity() {

		if ( quantity==null){
			return 0;
		}

		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public QuantityType getQuantityType() {
		return quantityType;
	}

	public void setQuantityType(QuantityType quantityType) {
		this.quantityType = quantityType;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		GoodsQuantityVO that = (GoodsQuantityVO) o;

		return new EqualsBuilder()
				.append(goodsId, that.goodsId)
				.append(skuId, that.skuId)
				.append(quantity, that.quantity)
				.append(quantityType, that.quantityType)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(goodsId)
				.append(skuId)
				.append(quantity)
				.append(quantityType)
				.toHashCode();
	}

	@Override
	public String toString() {
		return "GoodsQuantityVO{" +
				"goodsId=" + goodsId +
				", skuId=" + skuId +
				", quantity=" + quantity +
				", quantityType=" + quantityType +
				'}';
	}


}

