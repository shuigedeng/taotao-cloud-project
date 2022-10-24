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

package com.taotao.cloud.cache.redis.ratelimiter;


import java.util.concurrent.TimeUnit;

/**
 * 限流异常
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
public class RateLimiterException extends RuntimeException {

	private final String key;
	private final long max;
	private final long ttl;
	private final TimeUnit timeUnit;

	public RateLimiterException(String key, long max, long ttl, TimeUnit timeUnit) {
		super(String.format("您的访问次数已超限：%s，速率：%d/%ds", key, max, timeUnit.toSeconds(ttl)));
		this.key = key;
		this.max = max;
		this.ttl = ttl;
		this.timeUnit = timeUnit;
	}

	public String getKey() {
		return key;
	}

	public long getMax() {
		return max;
	}

	public long getTtl() {
		return ttl;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
