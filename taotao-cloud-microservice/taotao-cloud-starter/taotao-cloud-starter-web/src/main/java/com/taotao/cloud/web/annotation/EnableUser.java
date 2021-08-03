package com.taotao.cloud.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否启用自动获取用户信息注解 样例：public Object getUser(@EnableUser LoginUser user)
 *
 * @author shuigedeng
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableUser {

	/**
	 * 是否查询LoginUser对象所有信息，true则通过rpc接口查询
	 */
	boolean value() default false;
}
