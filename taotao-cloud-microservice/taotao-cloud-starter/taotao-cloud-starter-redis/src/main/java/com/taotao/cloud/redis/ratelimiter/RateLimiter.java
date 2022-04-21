/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.redis.ratelimiter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式 限流注解，默认速率为 600/ms
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RateLimiter {

	/**
	 * 限流的 key 支持，必须：请保持唯一性
	 *
	 * @return key
	 */
	String value();

	/**
	 * 限流的参数，可选，支持 spring el # 读取方法参数和 @ 读取 spring bean
	 *
	 * @return param
	 */
	String param() default "";

	/**
	 * 支持的最大请求，默认: 100
	 *
	 * @return 请求数
	 */
	long max() default 100L;

	/**
	 * 持续时间，默认: 3600
	 *
	 * @return 持续时间
	 */
	long ttl() default 1L;

	/**
	 * 时间单位，默认为分
	 *
	 * @return TimeUnit
	 */
	TimeUnit timeUnit() default TimeUnit.MINUTES;
}
