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
package com.taotao.cloud.log.annotation;

import java.lang.annotation.*;

/**
 * 系统操作记录
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/3 13:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RequestOperateLog {

	/**
	 * 功能描述
	 */
	String description() default "";

	/**
	 * 是否启用 操作日志
	 *
	 * @return 是否启用
	 */
	boolean enabled() default true;

	/**
	 * 描述
	 *
	 * @return {String}
	 */
	String value() default "";

	/**
	 * 是否拼接Controller类上的描述值
	 *
	 * @return 是否拼接Controller类上的描述值
	 */
	boolean controllerApiValue() default true;

	/**
	 * 记录执行参数
	 *
	 * @return 是否记录执行参数
	 */
	boolean request() default true;

	/**
	 * 当 request = false时， 方法报错记录请求参数
	 *
	 * @return 当 request = false时， 方法报错记录请求参数
	 */
	boolean requestByError() default true;

	/**
	 * 记录返回参数
	 *
	 * @return 是否记录返回参数
	 */
	boolean response() default true;
}
