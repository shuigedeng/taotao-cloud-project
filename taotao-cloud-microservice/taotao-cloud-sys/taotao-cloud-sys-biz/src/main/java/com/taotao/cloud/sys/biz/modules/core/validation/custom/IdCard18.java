package com.taotao.cloud.sys.biz.modules.core.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {IdCard18Validator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdCard18 {
    String message() default "{sanri.webui.validator.constraints.idcard}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
