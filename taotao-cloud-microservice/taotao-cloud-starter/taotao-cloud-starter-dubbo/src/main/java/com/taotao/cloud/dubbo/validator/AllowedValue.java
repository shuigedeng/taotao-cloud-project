package com.taotao.cloud.dubbo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 允许值
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:22:09
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { })
// @Constraint(validatedBy = {AllowedValueValidator.class}) 注2
public @interface AllowedValue {

	/**
	 * 消息
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:22:10
	 */
	String message() default "参数值不在合法范围内";

	/**
	 * 组
	 *
	 * @return {@link Class }<{@link ? }>{@link [] }
	 * @since 2022-04-27 17:22:10
	 */
	Class<?>[] groups() default { };

	/**
	 * 有效载荷
	 *
	 * @return {@link Class }<{@link ? } {@link extends } {@link Payload }>{@link [] }
	 * @since 2022-04-27 17:22:10
	 */
	Class<? extends Payload>[] payload() default { };

	/**
	 * 价值
	 *
	 * @return {@link long[] }
	 * @since 2022-04-27 17:22:10
	 */
	long[] value() default {};
 
}
