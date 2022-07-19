package com.enation.app.javashop.model.support.validator.validator;

import com.enation.app.javashop.model.support.validator.annotation.SortType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author fk
 * @version v2.0
 * @Description: SortType 验证
 * @date 2018/4/3 11:44
 * @since v7.0.0
 */
public class SortTypeValidator implements ConstraintValidator<SortType, String> {

    private final String[] ALL_STATUS = {"up", "down"};

    @Override
    public void initialize(SortType status) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(ALL_STATUS).contains(value);
    }
}
