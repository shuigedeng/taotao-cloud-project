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
package com.taotao.cloud.captcha.service.impl;

import com.taotao.cloud.captcha.model.CaptchaException;
import com.taotao.cloud.captcha.service.CaptchaCacheService;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Objects;

/**
 * CaptchaCacheServiceRedisImpl 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:41:47
 */
public class CaptchaCacheServiceRedisImpl implements CaptchaCacheService {

	private final RedisRepository redisRepository;

	public CaptchaCacheServiceRedisImpl() {
		RedisRepository bean = ContextUtil.getBean(RedisRepository.class, true);
		if (Objects.isNull(bean)) {
			throw new CaptchaException("redis 未初始化");
		}
		this.redisRepository = bean;
	}

	@Override
	public void set(String key, String value, long expiresInSeconds) {
		redisRepository.set(key, value, expiresInSeconds);
	}

	@Override
	public boolean exists(String key) {
		return redisRepository.exists(key);
	}

	@Override
	public void delete(String key) {
		redisRepository.del(key);
	}

	@Override
	public String get(String key) {
		Object o = redisRepository.get(key);
		if (Objects.nonNull(o)) {
			return redisRepository.get(key).toString();
		}
		return null;
	}

	@Override
	public Long increment(String key, long val) {
		String s = get(key);
		if (StringUtil.isEmpty(s)) {
			return null;
		}
		Long ret = Long.parseLong(s) + val;
		set(key, ret + "", 0);
		return ret;
	}

	@Override
	public String type() {
		return "redis";
	}
}
