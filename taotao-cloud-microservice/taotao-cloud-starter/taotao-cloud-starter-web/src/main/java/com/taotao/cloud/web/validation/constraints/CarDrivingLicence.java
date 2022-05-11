package com.taotao.cloud.web.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证是否为驾驶证（别名：驾驶证档案编号、行驶证编号）<br>
 * <p>仅限：中国驾驶证档案编号</p>
 * <p>
 * 只支持以下一种格式：
 * <ul>
 * <li>12位数字字符串,eg:430101758218</li>
 * </ul>
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-11 10:13:35
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Constraint(validatedBy = { CarDrivingLicenceValidator.class })
@Repeatable(CarDrivingLicence.List.class)
public @interface CarDrivingLicence {
	
	/**
	 * 是否不允许为空 {@linkplain NotNull}
	 * @return 默认：true
	 */
	boolean notNull() default true;
	
	String message() default "不是一个合法的驾驶证格式";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

	/**
	 * Defines several {@code @Date} annotations on the same element.
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		CarDrivingLicence[] value();
	}

}
