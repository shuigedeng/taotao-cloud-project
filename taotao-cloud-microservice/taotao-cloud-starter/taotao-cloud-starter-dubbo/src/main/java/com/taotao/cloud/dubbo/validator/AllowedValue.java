package com.taotao.cloud.dubbo.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { })
// @Constraint(validatedBy = {AllowedValueValidator.class}) 注2
public @interface AllowedValue {
 
    String message() default "参数值不在合法范围内";
 
    Class<?>[] groups() default { };
 
    Class<? extends Payload>[] payload() default { };
 
    long[] value() default {};
 
}
