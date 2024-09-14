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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.stamp;

import com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.properties.JustAuthProperties;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import java.util.concurrent.TimeUnit;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>自定义JustAuth State Cache </p>
 *
 * @since : 2021/5/22 10:22
 */
public class JustAuthStateStampManager implements AuthStateCache, InitializingBean {

	private JustAuthProperties justAuthProperties;
	private RedisRepository redisRepository;

	public JustAuthStateStampManager(RedisRepository redisRepository,
		JustAuthProperties justAuthProperties) {
		this.redisRepository = redisRepository;
		//		super(redisRepository, AccessConstants.CACHE_NAME_TOKEN_JUSTAUTH);
		this.justAuthProperties = justAuthProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//		super.setExpire(justAuthProperties.getTimeout());
	}

	@Override
	public void cache(String key, String value) {
		this.redisRepository.set(key, value);
	}

	@Override
	public void cache(String key, String value, long expire) {
		this.redisRepository.setExpire(key, value, expire, TimeUnit.MILLISECONDS);
	}

	@Override
	public String get(String key) {
		Object o = redisRepository.get(key);
		if (o != null) {
			return (String) redisRepository.get(key);
		}
		return null;
	}

	@Override
	public boolean containsKey(String key) {
		Object o = redisRepository.get(key);
		return o == null;
	}
}
