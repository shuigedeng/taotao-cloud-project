package com.taotao.cloud.web.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ResponseErrorProperty
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 08:58:22
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseErrorProperty {

	String value() default "";

	boolean includeIfNull() default false;
}
