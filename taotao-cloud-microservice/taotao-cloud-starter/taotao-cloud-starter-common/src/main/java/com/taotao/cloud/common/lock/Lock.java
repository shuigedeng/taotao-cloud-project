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
package com.taotao.cloud.common.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 锁
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 09:12
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

	/**
	 * 锁的key
	 */
	String key();

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

	/**
	 * 是否公平锁
	 */
	boolean isFair() default false;
}
