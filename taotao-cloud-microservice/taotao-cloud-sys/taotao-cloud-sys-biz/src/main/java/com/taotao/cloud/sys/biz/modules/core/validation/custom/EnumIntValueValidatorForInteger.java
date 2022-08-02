package com.taotao.cloud.sys.biz.modules.core.validation.custom;

import org.apache.commons.lang3.ArrayUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumIntValueValidatorForInteger implements ConstraintValidator<EnumIntValue,Integer> {
    private int [] values;
    @Override
    public void initialize(EnumIntValue constraintAnnotation) {
        this.values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) {
            return true;
        }
        return ArrayUtils.contains(values,value);
    }
}
