package com.taotao.cloud.sys.biz.modules.core.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EnumStringValueValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumStringValue {
    String message() default "{sanri.webui.validator.constraints.enum}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String [] value();
}
