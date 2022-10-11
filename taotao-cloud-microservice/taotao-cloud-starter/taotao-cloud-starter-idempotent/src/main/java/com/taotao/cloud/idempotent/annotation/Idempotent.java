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
package com.taotao.cloud.idempotent.annotation;

import com.taotao.cloud.idempotent.enums.IdempotentTypeEnum;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

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

	/**
	 * 获取锁的最大尝试时间(单位 {@code unit}) 该值大于0则使用 locker.tryLock 方法加锁，否则使用 locker.lock 方法
	 */
	long waitTime() default 0;

	/**
	 * 加锁的时间(单位 {@code unit})，超过这个时间后锁便自动解锁； 如果leaseTime为-1，则保持锁定直到显式解锁
	 */
	long leaseTime() default -1;

	/**
	 * 参数的时间单位
	 */
	TimeUnit unit() default TimeUnit.SECONDS;
}
