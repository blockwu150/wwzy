package com.enation.app.javashop.model.goods.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.javashop.model.goods.dos.ParametersDO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 参数组vo
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月20日 下午4:33:21
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ParameterGroupVO implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 724427321881170297L;
	@ApiModelProperty("参数组关联的参数集合")
	private List<ParametersDO> params;
	@ApiModelProperty("参数组名称")
	private String groupName;
	@ApiModelProperty("参数组id")
	private Long groupId;

	public List<ParametersDO> getParams() {
		return params;
	}
	public void setParams(List<ParametersDO> params) {
		this.params = params;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	@Override
	public String toString() {
		return "ParameterGroupVO [params=" + params + ", groupName=" + groupName + ", groupId=" + groupId + "]";
	}

}
