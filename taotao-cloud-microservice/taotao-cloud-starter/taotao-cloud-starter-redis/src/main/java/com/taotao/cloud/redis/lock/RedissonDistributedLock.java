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
package com.taotao.cloud.redis.lock;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.common.lock.DistributedLock;
import com.taotao.cloud.common.lock.ZLock;
import com.taotao.cloud.redis.properties.RedisLockProperties;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * redisson分布式锁实现，基本锁功能的抽象实现
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/3 07:47
 */
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnProperty(prefix = RedisLockProperties.BASE_REDIS_LOCK_PREFIX,
	name = RedisLockProperties.ENABLED,
	havingValue = RedisLockProperties.TRUE)
public class RedissonDistributedLock implements DistributedLock {

	@Autowired
	private RedissonClient redisson;

	private ZLock getLock(String key, boolean isFair) {
		RLock lock;
		if (isFair) {
			lock = redisson.getFairLock(CommonConstant.LOCK_KEY_PREFIX + key);
		} else {
			lock = redisson.getLock(CommonConstant.LOCK_KEY_PREFIX + key);
		}
		return new ZLock(lock, this);
	}

	@Override
	public ZLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) {
		ZLock zLock = getLock(key, isFair);
		RLock lock = (RLock) zLock.getLock();
		lock.lock(leaseTime, unit);
		return zLock;
	}

	@Override
	public ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair)
		throws InterruptedException {
		ZLock zLock = getLock(key, isFair);
		RLock lock = (RLock) zLock.getLock();
		if (lock.tryLock(waitTime, leaseTime, unit)) {
			return zLock;
		}
		return null;
	}

	@Override
	public void unlock(Object lock) {
		if (lock != null) {
			if (lock instanceof RLock) {
				RLock rLock = (RLock) lock;
				if (rLock.isLocked()) {
					rLock.unlock();
				}
			} else {
				throw new LockException("requires RLock type");
			}
		}
	}
}
