/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.common.support.lock.DistributedLock;
import com.taotao.cloud.common.support.lock.ZLock;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * redisson分布式锁实现，基本锁功能的抽象实现
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:16:33
 */
public class RedissonDistributedLock implements DistributedLock {

	private final RedissonClient redisson;

	public RedissonDistributedLock(RedissonClient redisson) {
		this.redisson = redisson;
	}

	private ZLock getLock(String key, boolean isFair) {
		RLock lock;
		if (isFair) {
			lock = redisson.getFairLock(RedisConstant.LOCK_KEY_PREFIX + key);
		} else {
			lock = redisson.getLock(RedisConstant.LOCK_KEY_PREFIX + key);
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
