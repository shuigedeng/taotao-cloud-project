package com.taotao.cloud.sys.biz.api.controller.tools.core.validation.custom;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumIntValueValidatorForString implements ConstraintValidator<EnumIntValue, String> {
    private int[] values;

    @Override
    public void initialize(EnumIntValue constraintAnnotation) {
        this.values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        if (!NumberUtils.isCreatable(value)) {
            return false;
        }
        int intValue = NumberUtils.toInt(value);
        return ArrayUtils.contains(values, intValue);
    }
}
