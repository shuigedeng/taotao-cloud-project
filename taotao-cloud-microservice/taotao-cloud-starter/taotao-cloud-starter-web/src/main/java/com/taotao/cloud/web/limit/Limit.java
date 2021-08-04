/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.web.limit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;

/**
 * Limit
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2021/08/04 08:46
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

	/**
	 * 名字
	 */
	String name() default "";

	/**
	 * key
	 */
	String key() default "";

	/**
	 * Key的前缀
	 */
	String prefix() default "";

	/**
	 * 给定的时间范围 单位(秒)
	 */
	int period();

	/**
	 * 一定时间内最多访问次数
	 */
	int count();

	/**
	 * 限流的类型(用户自定义key 或者 请求ip)
	 */
	LimitType limitType() default LimitType.CUSTOMER;
}
