package com.enation.app.javashop.model.payment.dos;

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
 * 支付方式实体
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-04-16 15:01:49
 */
@TableName("es_payment_method")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PaymentMethodDO implements Serializable {

    private static final long serialVersionUID = 6216378920396390L;

    /**
     * 支付方式id
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(hidden = true)
    private Long methodId;
    /**
     * 支付方式名称
     */
    @ApiModelProperty(name = "method_name", value = "支付方式名称", required = false)
    private String methodName;
    /**
     * 支付插件名称
     */
    @ApiModelProperty(name = "plugin_id", value = "支付插件名称", required = false)
    private String pluginId;
    /**
     * pc是否可用
     */
    @ApiModelProperty(name = "pc_config", value = "pc是否可用", required = false)
    private String pcConfig;
    /**
     * wap是否可用
     */
    @ApiModelProperty(name = "wap_config", value = "wap是否可用", required = false)
    private String wapConfig;
    /**
     * app 原生是否可用
     */
    @ApiModelProperty(name = "app_native_config", value = "app 原生是否可用", required = false)
    private String appNativeConfig;
    /**
     * 支付方式图片
     */
    @ApiModelProperty(name = "image", value = "支付方式图片", required = false)
    private String image;
    /**
     * 是否支持原路退回
     */
    @ApiModelProperty(name = "is_retrace", value = "是否支持原路退回", required = false)
    private Integer isRetrace;
    /**
     * app RN是否可用
     */
    @ApiModelProperty(name = "app_react_config", value = "app RN是否可用", required = false)
    private String appReactConfig;
    /**
     * 小程序是否可用
     */
    @ApiModelProperty(name = "mini_config", value = "pc是否可用", required = false)
    private String miniConfig;

    @PrimaryKeyField
    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPcConfig() {
        return pcConfig;
    }

    public void setPcConfig(String pcConfig) {
        this.pcConfig = pcConfig;
    }

    public String getWapConfig() {
        return wapConfig;
    }

    public void setWapConfig(String wapConfig) {
        this.wapConfig = wapConfig;
    }

    public String getAppNativeConfig() {
        return appNativeConfig;
    }

    public void setAppNativeConfig(String appNativeConfig) {
        this.appNativeConfig = appNativeConfig;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getIsRetrace() {
        return isRetrace;
    }

    public void setIsRetrace(Integer isRetrace) {
        this.isRetrace = isRetrace;
    }

    public String getAppReactConfig() {
        return appReactConfig;
    }

    public void setAppReactConfig(String appReactConfig) {
        this.appReactConfig = appReactConfig;
    }

    public String getMiniConfig() {
        return miniConfig;
    }

    public void setMiniConfig(String miniConfig) {
        this.miniConfig = miniConfig;
    }

    @Override
    public String toString() {
        return "PaymentMethodDO{" +
                "methodId=" + methodId +
                ", methodName='" + methodName + '\'' +
                ", pluginId='" + pluginId + '\'' +
                ", pcConfig='" + pcConfig + '\'' +
                ", wapConfig='" + wapConfig + '\'' +
                ", appNativeConfig='" + appNativeConfig + '\'' +
                ", image='" + image + '\'' +
                ", isRetrace=" + isRetrace +
                ", appReactConfig='" + appReactConfig + '\'' +
                ", miniConfig='" + miniConfig + '\'' +
                '}';
    }

}
