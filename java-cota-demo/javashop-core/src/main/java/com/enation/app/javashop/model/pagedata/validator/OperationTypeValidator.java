package com.enation.app.javashop.model.pagedata.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author fk
 * @version v2.0
 * @Description: OperationType 验证
 * @date 2018/4/3 11:44
 * @since v7.0.0
 */
public class OperationTypeValidator implements ConstraintValidator<OperationType, String> {

    private final String[] ALL_STATUS = {"URL", "GOODS","KEYWORD","SHOP","CATEGORY","TOPIC","NONE"};

    @Override
    public void initialize(OperationType status) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(ALL_STATUS).contains(value);
    }
}
