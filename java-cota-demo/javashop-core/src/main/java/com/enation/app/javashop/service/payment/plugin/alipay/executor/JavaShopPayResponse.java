package com.enation.app.javashop.service.payment.plugin.alipay.executor;
/**
 * javashop支付相应对象
 *
 * @author zh
 * @version v7.0
 * @date 18/7/19 下午4:47
 * @since v7.0
 */

import com.alipay.api.AlipayResponse;
import com.enation.app.javashop.model.payment.vo.Form;

public class JavaShopPayResponse extends AlipayResponse {
    /**
     * 组织好数据结构的表单信息
     */
    private Form form;


    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return "JavaShopPayResponse{" +
                "form=" + form +
                '}';
    }
}
