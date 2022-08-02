package com.taotao.cloud.sys.biz.modules.core.validation.custom;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UserNameValidator implements ConstraintValidator<UserName,String> {
    // 字母，数字，_- 4 到 16 位
    Pattern userNamePatternEn = Pattern.compile("^[a-zA-Z0-9_-]{4,16}$");
    // 中文,字母，数字，_- 4 到 16 位
    Pattern userNamePatternCn = Pattern.compile("^[a-zA-Z0-9_-\\u4E00-\\u9FA5]{4,16}$");

    private boolean chinese;

    @Override
    public void initialize(UserName constraintAnnotation) {
        this.chinese = constraintAnnotation.chinese();
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(userName)){
            return true;
        }
        if(chinese){
            return userNamePatternCn.matcher(userName).find();
        }
        return userNamePatternEn.matcher(userName).find();
    }
}
