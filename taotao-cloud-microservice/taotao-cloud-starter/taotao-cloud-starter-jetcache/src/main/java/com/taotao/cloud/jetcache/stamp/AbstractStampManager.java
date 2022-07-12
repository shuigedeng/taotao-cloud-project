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

package com.taotao.cloud.jetcache.stamp;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ObjectUtils;

/**
 * <p>Description: 抽象Stamp管理 </p>
 *
 * @param <K> 签章缓存对应Key值的类型。
 * @param <V> 签章缓存存储数据，对应的具体存储值的类型，
 * @author : gengwei.zheng
 * @date : 2021/8/23 11:51
 */
public abstract class AbstractStampManager<K, V> implements StampManager<K, V> {

	/**
	 * 指定数据存储缓存
	 *
	 * @return {@link Cache}
	 */
	protected abstract Cache<K, V> getCache();

	private static final Duration DEFAULT_EXPIRE = Duration.ofMinutes(1);

	private Duration expire;

	@Override
	public Duration getExpire() {
		if (ObjectUtils.isEmpty(this.expire) || this.expire.equals(Duration.ZERO)) {
			return DEFAULT_EXPIRE;
		} else {
			return this.expire;
		}
	}

	public void setExpire(Duration expire) {
		this.expire = expire;
	}

	@Override
	public boolean check(K key, V value) {
		if (ObjectUtils.isEmpty(value)) {
			throw new RuntimeException("Parameter Stamp value is null");
		}

		V storedStamp = this.get(key);
		if (ObjectUtils.isEmpty(storedStamp)) {
			throw new RuntimeException("Stamp is invalid!");
		}

		if (ObjectUtils.notEqual(storedStamp, value)) {
			throw new RuntimeException("Stamp is mismathch!");
		}

		return true;
	}

	@Override
	public V get(K key) {
		return this.getCache().get(key);
	}

	@Override
	public void delete(K key) {
		boolean result = this.getCache().remove(key);
		if (!result) {
			throw new RuntimeException("Delete Stamp From Storage Failed");
		}
	}

	@Override
	public void put(K key, V value, long expireAfterWrite, TimeUnit timeUnit) {
		this.getCache().put(key, value, expireAfterWrite, timeUnit);
	}

	@Override
	public AutoReleaseLock lock(K key, long expire, TimeUnit timeUnit) {
		return this.getCache().tryLock(key, expire, timeUnit);
	}

	@Override
	public boolean lockAndRun(K key, long expire, TimeUnit timeUnit, Runnable action) {
		return this.getCache().tryLockAndRun(key, expire, timeUnit, action);
	}
}
