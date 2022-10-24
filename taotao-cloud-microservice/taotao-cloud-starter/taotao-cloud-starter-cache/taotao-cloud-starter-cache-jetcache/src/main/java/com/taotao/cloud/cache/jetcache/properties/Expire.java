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
package com.taotao.cloud.cache.jetcache.properties;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 自定义二级缓存过期时间通用属性
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-25 08:53:22
 */
public class Expire {

	/**
	 * 统一缓存时长，默认：1
	 */
	private Long duration = 1L;

	/**
	 * 统一缓存时长单位，默认：小时。
	 */
	private TimeUnit unit = TimeUnit.HOURS;

	/**
	 * Redis缓存TTL设置，默认：1小时，单位小时
	 * <p>
	 * 使用Duration类型，配置参数形式如下： "?ns" //纳秒 "?us" //微秒 "?ms" //毫秒 "?s" //秒 "?m" //分 "?h" //小时 "?d" //天
	 */
	private Duration ttl;

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	public Duration getTtl() {
		if (ObjectUtils.isEmpty(this.ttl)) {
			this.ttl = convertToDuration(this.duration, this.unit);
		}
		return ttl;
	}

	private Duration convertToDuration(Long duration, TimeUnit timeUnit) {
		switch (timeUnit) {
			case DAYS:
				return Duration.ofDays(duration);
			case HOURS:
				return Duration.ofHours(duration);
			case SECONDS:
				return Duration.ofSeconds(duration);
			default:
				return Duration.ofMinutes(duration);
		}
	}
}
