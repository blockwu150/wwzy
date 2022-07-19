package com.enation.app.javashop.model.goods.vo;

import java.util.List;

import com.enation.app.javashop.model.goods.dos.SpecValuesDO;
import com.enation.app.javashop.model.goods.dos.SpecificationDO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 规格vo
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月20日 上午11:28:48
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SpecificationVO extends SpecificationDO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6722899699412983854L;
	/**
	 * 规格值
	 */
	@ApiModelProperty("规格值")
	private List<SpecValuesDO> valueList;

	public List<SpecValuesDO> getValueList() {
		return valueList;
	}

	public void setValueList(List<SpecValuesDO> valueList) {
		this.valueList = valueList;
	}

	@Override
	public String toString() {
		return "SpecificationVO [valueList=" + valueList + "]";
	}
	
	
}
