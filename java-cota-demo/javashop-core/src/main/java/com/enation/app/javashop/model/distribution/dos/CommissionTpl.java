package com.enation.app.javashop.model.distribution.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 提成模版
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/17 下午3:24
 */
@TableName("es_commission_tpl")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommissionTpl implements Serializable {


    /**
     * 主键
     */
    @TableId(type= IdType.ASSIGN_ID)
    @ApiModelProperty(hidden = true)
    private Long id;

    /**
     * 模板名称
     */
    @NotEmpty(message = "模版名称不能为空")
    @ApiModelProperty(name="tpl_name",value = "模版名称", required = false)
    private String tplName;

    /**
     * 模板说明
     */
    @ApiModelProperty(name="tpl_describe",value = "模版描述", required = false)
    private String tplDescribe;


    /**
     * 与消费者相差1级 时返利金额
     */
    @NotNull(message = "相差1级返利金额 不能为空")
    @ApiModelProperty(value = "相差1级返利金额 百分比值：填写1 则为百分之1", required = false)
    private Double grade1;

    /**
     * 与消费者相差2级 时返利金额
     */
    @NotNull(message = "相差2级返利金额 不能为空")
    @ApiModelProperty(value = "相差2级返利金额 百分比值：填写1 则为百分之1", required = false)
    private Double grade2;


    /**
     * 是否默认 1 默认，0 非默认
     */
    @NotNull(message = "默认参数不能为空")
    @ApiModelProperty(name="is_default",value = "是否默认：1默认，0非默认", required = false)
    private Integer isDefault;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTplDescribe() {
        return tplDescribe;
    }

    public void setTplDescribe(String tplDescribe) {
        this.tplDescribe = tplDescribe;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public Double getGrade1() {
        return grade1;
    }

    public void setGrade1(Double grade1) {
        this.grade1 = grade1;
    }

    public Double getGrade2() {
        return grade2;
    }

    public void setGrade2(Double grade2) {
        this.grade2 = grade2;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommissionTpl that = (CommissionTpl) o;
        if (tplName != null ? !tplName.equals(that.tplName) : that.tplName != null) {
            return false;
        }
        if (tplDescribe != null ? !tplDescribe.equals(that.tplDescribe) : that.tplDescribe != null) {
            return false;
        }
        if (grade1 != null ? !grade1.equals(that.grade1) : that.grade1 != null) {
            return false;
        }
        if (grade2 != null ? !grade2.equals(that.grade2) : that.grade2 != null) {
            return false;
        }
        return isDefault != null ? isDefault.equals(that.isDefault) : that.isDefault == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (tplName != null ? tplName.hashCode() : 0);
        result = 31 * result + (tplDescribe != null ? tplDescribe.hashCode() : 0);
        result = 31 * result + (grade1 != null ? grade1.hashCode() : 0);
        result = 31 * result + (grade2 != null ? grade2.hashCode() : 0);
        result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CommissionTpl{" +
                " tplName='" + tplName + '\'' +
                ", tplDescribe='" + tplDescribe + '\'' +
                ", grade1=" + grade1 +
                ", grade2=" + grade2 +
                ", isDefault=" + isDefault +
                '}';
    }
}
