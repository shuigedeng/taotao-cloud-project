package com.taotao.cloud.web.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ResponseErrorCode 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 09:25:53
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseErrorCode {

	String value();
}
