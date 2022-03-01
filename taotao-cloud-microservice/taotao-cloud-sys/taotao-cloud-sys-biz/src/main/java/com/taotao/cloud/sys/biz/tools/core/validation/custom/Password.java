package com.taotao.cloud.sys.biz.tools.core.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.regex.Pattern;

@Documented
@Constraint(validatedBy = {PasswordValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "{sanri.webui.validator.constraints.password}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


    Strength strength() default Strength.LOW;

    /**
     * 强 ==> 密码长度大于等于8位数 包含大写字母[A-Z] + 小写字母[a-z] + 数字[0-9] + 非单词字符的特殊字符[标点符号，空格啥的这些] 结尾
     * 中 ==> 密码长度大于等于7位数 大写字母[A-Z] + 小写字母[a-z] 或者 大写字母[A-Z] + 数字[0-9] 或者 小写字母[a-z] + 数字[0-9] + 任意字符 结尾
     * 弱 ==> 大于等于6位 任何字符或者数字 (如果达不到这个条件就是弱，所以这里需要用false判断)
     */
    public enum Strength{
        LOW("(?=.{6,}).*"),
        MEDIUM("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$"),
        STRONG("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\\\W).*$");
        private Pattern value;

        Strength(String value) {
            this.value = Pattern.compile(value);
        }

        public Pattern getValue() {
            return value;
        }
    }
}
