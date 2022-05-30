package com.taotao.cloud.mongodb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * type表示查询类似，默认为equals attribute表示要查询的属性，默认为空串，如果为空则为字段名称
 *
 * @author shuigedeng
 * @version 2022.05
 * @link <a href="https://gitee.com/qwer.com/open-mongodb">...</a>
 * @since 2022-05-27 21:49:15
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryField {

	/**
	 * 类型
	 *
	 * @return {@link QueryType }
	 * @since 2022-05-27 21:48:43
	 */
	QueryType type() default QueryType.EQUALS;

	/**
	 * 属性
	 *
	 * @return {@link String }
	 * @since 2022-05-27 21:49:27
	 */
	String attribute() default "";
}
