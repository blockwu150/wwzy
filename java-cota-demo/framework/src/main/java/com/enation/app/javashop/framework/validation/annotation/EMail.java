package com.enation.app.javashop.framework.validation.annotation;

import com.enation.app.javashop.framework.validation.impl.EMailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 手机号码校验注解
 *
 * @author zh
 * @version v7.0
 * @date 18/5/9 下午3:04
 * @since v7.0
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EMailValidator.class})
public @interface EMail {

    String regexp() default "";

    String message() default "邮箱格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
