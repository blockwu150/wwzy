package com.enation.app.javashop.model.member.validator;

/**
 * @author fk
 * @version v1.0
 * @Description: 商品评分
 * @date 2018/4/11 10:27
 * @since v7.0.0
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {GradeTypeValidator.class})
@Documented
@Target( {ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GradeType {

    String message() default "商品评分不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
