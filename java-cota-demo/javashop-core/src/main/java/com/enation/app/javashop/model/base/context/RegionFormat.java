package com.enation.app.javashop.model.base.context;

import java.lang.annotation.*;

/**
 * Created by kingapex on 2018/5/2.
 * 地区注解
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/5/2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface RegionFormat {

}
