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

package com.taotao.cloud.redis.ratelimiter;


import com.taotao.cloud.common.support.function.CheckedSupplier;
import com.taotao.cloud.common.utils.exception.ExceptionUtils;

import java.util.concurrent.TimeUnit;

/**
 * RateLimiter 限流 Client
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
public interface RateLimiterClient {

	/**
	 * 服务是否被限流
	 *
	 * @param key 自定义的key，请保证唯一
	 * @param max 支持的最大请求
	 * @param ttl 时间,单位默认为秒（seconds）
	 * @return boolean
	 * @since 2022-04-27 17:45:23
	 */
	default boolean isAllowed(String key, long max, long ttl) {
		return this.isAllowed(key, max, ttl, TimeUnit.SECONDS);
	}

	/**
	 * 服务是否被限流
	 *
	 * @param key      自定义的key，请保证唯一
	 * @param max      支持的最大请求
	 * @param ttl      时间
	 * @param timeUnit 时间单位
	 * @return boolean
	 * @since 2022-04-27 17:45:23
	 */
	boolean isAllowed(String key, long max, long ttl, TimeUnit timeUnit);

	/**
	 * 服务限流，被限制时抛出 RateLimiterException 异常，需要自行处理异常
	 *
	 * @param key      自定义的key，请保证唯一
	 * @param max      支持的最大请求
	 * @param ttl      时间
	 * @param supplier Supplier 函数式
	 * @return {@link T }
	 * @since 2022-04-27 17:45:23
	 */
	default <T> T allow(String key, long max, long ttl, CheckedSupplier<T> supplier) {
		return allow(key, max, ttl, TimeUnit.SECONDS, supplier);
	}

	/**
	 * 服务限流，被限制时抛出 RateLimiterException 异常，需要自行处理异常
	 *
	 * @param key      自定义的key，请保证唯一
	 * @param max      支持的最大请求
	 * @param ttl      时间
	 * @param timeUnit 时间单位
	 * @param supplier Supplier 函数式
	 * @return {@link T }
	 * @since 2022-04-27 17:45:23
	 */
	default <T> T allow(String key, long max, long ttl, TimeUnit timeUnit,
		CheckedSupplier<T> supplier) {
		boolean isAllowed = this.isAllowed(key, max, ttl, timeUnit);
		if (isAllowed) {
			try {
				return supplier.get();
			} catch (Throwable e) {
				throw ExceptionUtils.unchecked(e);
			}
		}
		throw new RateLimiterException(key, max, ttl, timeUnit);
	}
}
