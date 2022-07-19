package com.enation.app.javashop.model.system.dos;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.model.system.vo.UploaderVO;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.Gson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

/**
 * 存储方案实体
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-22 09:31:56
 */
@TableName("es_uploader")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UploaderDO implements Serializable {

	private static final long serialVersionUID = 3081675229152673L;

	/**储存id*/
	@TableId(type= IdType.ASSIGN_ID)
	@ApiModelProperty(hidden=true)
	private Long id;
	/**存储名称*/
	@NotEmpty(message="存储名称不能为空")
	@ApiModelProperty(name="name",value="存储名称",required=true)
	private String name;
	/**是否开启*/
	@ApiModelProperty(name="open",value="是否开启",required=false)
	private Integer open;
	/**存储配置*/
	@NotEmpty(message="存储配置不能为空")
	@ApiModelProperty(name="config",value="存储配置",required=true)
	private String config;
	/**存储插件id*/
	@NotEmpty(message="存储插件id不能为空")
	@ApiModelProperty(name="bean",value="存储插件id",required=true)
	private String bean;

	public UploaderDO(UploaderVO uploaderVO) {
		this.id = uploaderVO.getId();
		this.name = uploaderVO.getName();
		this.open = uploaderVO.getOpen();
		this.bean = uploaderVO.getBean();
		Gson gson = new Gson();
		this.config = gson.toJson(uploaderVO.getConfigItems());
	}
	public UploaderDO() {

	}

	@PrimaryKeyField
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

	public Integer getOpen() {
		return open;
	}
	public void setOpen(Integer open) {
		this.open = open;
	}

	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}

	public String getBean() {
		return bean;
	}
	public void setBean(String bean) {
		this.bean = bean;
	}
	@Override
	public String toString() {
		return "UploaderDO [id=" + id + ", name=" + name + ", open=" + open + ", config=" + config + ", bean=" + bean
				+ "]";
	}



}
