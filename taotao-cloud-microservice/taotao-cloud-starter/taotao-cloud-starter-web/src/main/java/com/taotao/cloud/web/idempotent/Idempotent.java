/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.web.idempotent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Idempotent
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:16:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

	/**
	 * 关键key key是本次请求中参数的键， 重复请求的key取自header中的rid 用来标识这个请求的唯一性 拦截器中会使用key从请求参数中获取value
	 */
	String key() default "";

	/**
	 * 自定义key的前缀用来区分业务
	 */
	String perFix();

	/**
	 * 禁止重复提交的模式 默认是全部使用
	 */
	IdempotentTypeEnum ideTypeEnum() default IdempotentTypeEnum.ALL;
}
