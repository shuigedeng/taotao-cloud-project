package com.taotao.cloud.health.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: chejiangyi
 * @version: 2019-07-28 09:36
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldReport {

	/**
	 * 唯一名称
	 *
	 * @return
	 */
	String name() default "";

	/**
	 * 描述
	 *
	 * @return
	 */
	String desc() default "";
}
