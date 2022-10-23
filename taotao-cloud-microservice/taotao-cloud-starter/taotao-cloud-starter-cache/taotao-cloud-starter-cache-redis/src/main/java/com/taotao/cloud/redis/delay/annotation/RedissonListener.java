package com.taotao.cloud.redis.delay.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RedissonListener
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:23:47
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonListener {

	String[] queues();

	String errorHandler() default "";

	String isolationStrategy() default "";

	String messageConverter() default "";

	/**
	 * concurrency of the consumer num
	 *
	 * @return listener count
	 */
	int concurrency() default 1;

	/**
	 * poll a list data from redis each time if grater than 1
	 *
	 * @return max fetch count
	 */
	int maxFetch() default 1;

}
