package com.enation.app.javashop.framework.validation.impl;

import com.enation.app.javashop.framework.validation.annotation.EMail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码校验实现
 *
 * @author zh
 * @version v7.0
 * @date 18/5/9 下午3:08
 * @since v7.0
 */
public class EMailValidator implements ConstraintValidator<EMail, String> {

    private static Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Matcher m = pattern.matcher(value);
        return m.matches();
    }

    @Override
    public void initialize(EMail constraintAnnotation) {

    }
}
