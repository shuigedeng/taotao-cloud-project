package com.taotao.cloud.dubbo.validator;

import java.util.Arrays;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AllowedValueValidator implements ConstraintValidator<AllowedValue, Long> {
 
    private long[] allowedValues;
 
    @Override
    public void initialize(AllowedValue constraintAnnotation) {
        this.allowedValues = constraintAnnotation.value();
    }
 
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (allowedValues.length == 0) {
            return true;
        }
        return Arrays.stream(allowedValues).anyMatch(o -> Objects.equals(o, value));
    }
}
