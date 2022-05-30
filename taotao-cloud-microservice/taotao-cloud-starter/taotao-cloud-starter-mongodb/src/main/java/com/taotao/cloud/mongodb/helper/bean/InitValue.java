package com.taotao.cloud.mongodb.helper.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * InitValue
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-27 21:50:24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface InitValue {

	/**
	 * value
	 *
	 * @return {@link String }
	 * @since 2022-05-27 21:50:24
	 */
	String value();

}
