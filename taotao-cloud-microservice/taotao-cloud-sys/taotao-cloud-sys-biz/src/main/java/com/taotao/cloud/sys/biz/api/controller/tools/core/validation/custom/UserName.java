package com.taotao.cloud.sys.biz.api.controller.tools.core.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UserNameValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserName {
    String message() default "{sanri.webui.validator.constraints.username}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean chinese() default false;
}
