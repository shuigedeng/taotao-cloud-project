package com.taotao.cloud.web.mvc.constraints;

import com.taotao.cloud.web.mvc.validator.LimitedValueValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {LimitedValueValidator.class})
public @interface LimitedValue {

	//默认错误消息
	String message() default "必须为指定值";

	boolean allowNullValue() default false;

	String[] strValues() default {};

	int[] intValues() default {};

	//分组
	Class<?>[] groups() default {};

	//负载
	Class<? extends Payload>[] payload() default {};
}
