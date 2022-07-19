package com.enation.app.javashop.model.pagedata;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;


/**
 * 楼层实体
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-21 16:39:22
 */
@TableName("es_page")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PageData implements Serializable {

    private static final long serialVersionUID = 3806389481183972L;

    /**
     * 主键id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(hidden = true)
    private Long pageId;
    /**
     * 楼层名称
     */
    @ApiModelProperty(name = "page_name", value = "楼层名称", required = true)
    @NotEmpty(message = "名称不能为空")
    private String pageName;
    /**
     * 楼层数据
     */
    @ApiModelProperty(name = "page_data", value = "楼层数据", required = true)
    @NotEmpty(message = "页面数据不能为空")
    private String pageData;

    @ApiModelProperty(name = "page_type", value = "页面类型", hidden = true)
    private String pageType;

    @ApiModelProperty(name = "client_type", value = "客户端类型", hidden = true)
    private String clientType;

    @PrimaryKeyField
    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageData() {
        return pageData;
    }

    public void setPageData(String pageData) {
        this.pageData = pageData;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageData that = (PageData) o;
        if (pageId != null ? !pageId.equals(that.pageId) : that.pageId != null) {
            return false;
        }
        if (pageName != null ? !pageName.equals(that.pageName) : that.pageName != null) {
            return false;
        }
        if (pageData != null ? !pageData.equals(that.pageData) : that.pageData != null) {
            return false;
        }
        if (pageType != null ? !pageType.equals(that.pageType) : that.pageType != null) {
            return false;
        }
        return clientType != null ? clientType.equals(that.clientType) : that.clientType == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (pageId != null ? pageId.hashCode() : 0);
        result = 31 * result + (pageName != null ? pageName.hashCode() : 0);
        result = 31 * result + (pageData != null ? pageData.hashCode() : 0);
        result = 31 * result + (pageType != null ? pageType.hashCode() : 0);
        result = 31 * result + (clientType != null ? clientType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PageData{" +
                "pageId=" + pageId +
                ", pageName='" + pageName + '\'' +
                ", pageData='" + pageData + '\'' +
                ", pageType='" + pageType + '\'' +
                ", clientType='" + clientType + '\'' +
                '}';
    }


}
