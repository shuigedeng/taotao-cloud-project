package com.taotao.cloud.sys.biz.modules.core.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EnumIntValueValidatorForString.class,EnumIntValueValidatorForInteger.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumIntValue {
    // 这里提示消息不能写那个 value 变量，在源码中有数组转换失败 ParameterTermResolver interpolate 方法 30 行
    String message() default "{sanri.webui.validator.constraints.enum.int}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int [] value() default {};
}
