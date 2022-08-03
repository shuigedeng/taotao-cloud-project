package com.taotao.cloud.sys.biz.api.controller.tools.core.validation.custom;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumStringValueValidator implements ConstraintValidator<EnumStringValue,String> {
    private String [] values;
    @Override
    public void initialize(EnumStringValue constraintAnnotation) {
        this.values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(value)) {
            return true;
        }
        return ArrayUtils.contains(values,value);
    }
}
