package com.taotao.cloud.sys.biz.api.controller.tools.core.validation.custom;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password,String> {
    private Password.Strength strength;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.strength = constraintAnnotation.strength();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(password)){
            return true;
        }
        return strength.getValue().matcher(password).find();
    }


}
