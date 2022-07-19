package com.enation.app.javashop.model.goods.vo;

import java.io.Serializable;

/**
 * 运费模板操作类型
 *
 * @author zh
 * @version v7.0
 * @date 2019/9/16 4:17 PM
 * @since v7.0
 */

public class ShipTemplateMsg implements Serializable {

    private static final long serialVersionUID = 973421070569515297L;
    /**
     * 运费模板id
     */
    private Long templateId;
    /**
     * 操作类型
     */
    private Integer operationType;

    public ShipTemplateMsg() {

    }

    public ShipTemplateMsg(Long templateId, Integer operationType) {
        this.operationType = operationType;
        this.templateId = templateId;
    }


    /**
     * 添加
     */
    public final static int ADD_OPERATION = 1;

    /**
     * 修改
     */
    public final static int UPDATE_OPERATION = 2;


    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}
