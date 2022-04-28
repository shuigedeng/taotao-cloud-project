package com.taotao.cloud.dubbo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

/**
 * 允许价值验证器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:22:16
 */
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
