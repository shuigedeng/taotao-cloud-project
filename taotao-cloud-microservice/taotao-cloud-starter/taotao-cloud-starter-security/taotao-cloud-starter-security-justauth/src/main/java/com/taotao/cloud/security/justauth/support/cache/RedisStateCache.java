/*
 * Copyright (c) 2019-2029, xkcoding & Yangkai.Shen & 沈扬凯 (237497819@qq.com & xkcoding.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.taotao.cloud.security.justauth.support.cache;

import com.taotao.cloud.security.justauth.properties.CacheProperties;
import java.util.concurrent.TimeUnit;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * Redis作为JustAuth的State的缓存
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019-08-02 15:10
 */
public class RedisStateCache implements AuthStateCache {

	private final RedisTemplate<String, String> redisTemplate;
	private final CacheProperties cacheProperties;

	public RedisStateCache(RedisTemplate<String, String> redisTemplate,
		CacheProperties cacheProperties) {
		this.redisTemplate = redisTemplate;
		this.cacheProperties = cacheProperties;
	}

	/**
	 * 存入缓存
	 *
	 * @param key   缓存key
	 * @param value 缓存内容
	 */
	@Override
	public void cache(String key, String value) {
		this.cache(key, value, cacheProperties.getTimeout().toMillis());
	}

	/**
	 * 存入缓存
	 *
	 * @param key     缓存key
	 * @param value   缓存内容
	 * @param timeout 指定缓存过期时间（毫秒）
	 */
	@Override
	public void cache(String key, String value, long timeout) {
		redisTemplate.opsForValue()
			.set(cacheProperties.getPrefix() + key, value, timeout, TimeUnit.MILLISECONDS);
	}

	/**
	 * 获取缓存内容
	 *
	 * @param key 缓存key
	 * @return 缓存内容
	 */
	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(cacheProperties.getPrefix() + key);
	}

	/**
	 * 是否存在key，如果对应key的value值已过期，也返回false
	 *
	 * @param key 缓存key
	 * @return true：存在key，并且value没过期；false：key不存在或者已过期
	 */
	@Override
	public boolean containsKey(String key) {
		Long expire = redisTemplate.getExpire(cacheProperties.getPrefix() + key,
			TimeUnit.MILLISECONDS);
		if (expire == null) {
			expire = 0L;
		}
		return expire > 0;
	}
}
