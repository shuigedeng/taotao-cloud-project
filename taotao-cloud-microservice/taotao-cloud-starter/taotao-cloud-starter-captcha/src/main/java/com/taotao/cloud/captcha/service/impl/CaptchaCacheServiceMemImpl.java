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

import com.taotao.cloud.captcha.service.CaptchaCacheService;
import com.taotao.cloud.captcha.util.CacheUtil;

/**
 * 对于分布式部署的应用，我们建议应用自己实现CaptchaCacheService， 比如用Redis，参考service/spring-boot代码示例。
 * 如果应用是单点的，也没有使用redis，那默认使用内存。 内存缓存只适合单节点部署的应用，否则验证码生产与验证在节点之间信息不同步，导致失败
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:50
 */
public class CaptchaCacheServiceMemImpl implements CaptchaCacheService {

	@Override
	public void set(String key, String value, long expiresInSeconds) {
		CacheUtil.set(key, value, expiresInSeconds);
	}

	@Override
	public boolean exists(String key) {
		return CacheUtil.exists(key);
	}

	@Override
	public void delete(String key) {
		CacheUtil.delete(key);
	}

	@Override
	public String get(String key) {
		return CacheUtil.get(key);
	}

	@Override
	public Long increment(String key, long val) {
		Long ret = Long.valueOf(CacheUtil.get(key)) + val;
		CacheUtil.set(key, ret + "", 0);
		return ret;
	}

	@Override
	public String type() {
		return "local";
	}
}
