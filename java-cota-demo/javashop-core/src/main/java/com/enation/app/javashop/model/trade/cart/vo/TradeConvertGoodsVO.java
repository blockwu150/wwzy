package com.enation.app.javashop.model.trade.cart.vo;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 购物车—商品VO
 *
 * @author Snow
 * @version v2.0
 * @since v7.0.0
 * create in 2018/3/20
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
public class TradeConvertGoodsVO implements Serializable {

    @ApiModelProperty(value = "运费模板id")
    private Long templateId;

    @ApiModelProperty(value = "是否审核通过.0:未审核,1:通过,2:不通过")
    private Integer isAuth;

    @ApiModelProperty(name = "last_modify", value = "商品最后修改时间")
    private Long lastModify;


    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Long getLastModify() {
        return lastModify;
    }

    public void setLastModify(Long lastModify) {
        this.lastModify = lastModify;
    }

    @Override
    public String toString() {
        return "TradeConvertGoodsVO{" +
                "templateId=" + templateId +
                ", isAuth=" + isAuth +
                ", lastModify=" + lastModify +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        TradeConvertGoodsVO that = (TradeConvertGoodsVO) o;

        return new EqualsBuilder()
                .append(templateId, that.templateId)
                .append(isAuth, that.isAuth)
                .append(lastModify, that.lastModify)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(templateId)
                .append(isAuth)
                .append(lastModify)
                .toHashCode();
    }
}
