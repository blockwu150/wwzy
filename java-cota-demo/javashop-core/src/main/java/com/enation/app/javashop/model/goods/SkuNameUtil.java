package com.enation.app.javashop.model.goods;

import com.enation.app.javashop.model.goods.vo.SpecValueVO;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;

import java.util.List;

/**
 * sku 名称生成工具
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-03-05
 */

public class SkuNameUtil {

    public  static  String createSkuName(String specs) {
        if (StringUtil.isEmpty(specs)) {
            return "";
        }
        List<SpecValueVO> specList  = JsonUtil.jsonToList(specs, SpecValueVO.class);

        StringBuffer skuName = new StringBuffer();
        specList.forEach(specValueVO -> {
            skuName.append("/");
            skuName.append(specValueVO.getSpecValue());
        });
        return skuName.toString().substring(1);
    }

}
