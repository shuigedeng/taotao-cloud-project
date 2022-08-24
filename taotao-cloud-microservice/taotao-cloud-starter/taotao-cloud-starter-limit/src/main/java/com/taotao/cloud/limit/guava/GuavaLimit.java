package com.taotao.cloud.limit.guava;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 番石榴限制
 * 直接将注解打在控制层的方法上，需要配合Spring框架的Controller注解使用
 * <pre class="code">
 *
 * &#064;GuavaLimit(token  = 20, message = "无法访问！")
 *     public List list(@RequestParam final Integer type) {
 *         final KoneOrderType orderType = KoneOrderType.builder().type(type).build();
 *         return mOrderTypeMapper.list(orderType);
 *     }
 * </pre>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-08-08 10:36:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GuavaLimit {
	/**
	 * 每秒访问的次数
	 * 默认可以访问20次
	 */
	double token() default 20;

	/**
	 * 被限流拦截返回客户端的消息
	 */
	String message() default "无法访问！";
}
