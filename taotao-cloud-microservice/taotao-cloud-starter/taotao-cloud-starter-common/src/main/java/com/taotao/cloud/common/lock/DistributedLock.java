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
package com.taotao.cloud.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁顶级接口
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 09:12
 */
public interface DistributedLock {

	/**
	 * 获取锁，如果获取不成功则一直等待直到lock被获取
	 *
	 * @param key       锁的key
	 * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁； 如果leaseTime为-1，则保持锁定直到显式解锁
	 * @param unit      {@code leaseTime} 参数的时间单位
	 * @param isFair    是否公平锁
	 * @return com.taotao.cloud.core.lock.ZLock 锁对象
	 * @author shuigedeng
	 * @since 2021/2/25 16:46
	 */
	ZLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;

	default ZLock lock(String key, long leaseTime, TimeUnit unit) throws Exception {
		return this.lock(key, leaseTime, unit, false);
	}

	default ZLock lock(String key, boolean isFair) throws Exception {
		return this.lock(key, -1, null, isFair);
	}

	default ZLock lock(String key) throws Exception {
		return this.lock(key, -1, null, false);
	}

	/**
	 * 尝试获取锁，如果锁不可用则等待最多waitTime时间后放弃
	 *
	 * @param key       锁的key
	 * @param waitTime  获取锁的最大尝试时间(单位 {@code unit})
	 * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁； 如果leaseTime为-1，则保持锁定直到显式解锁
	 * @param unit      {@code waitTime} 和 {@code leaseTime} 参数的时间单位
	 * @return com.taotao.cloud.core.lock.ZLock 锁对象，如果获取锁失败则为null
	 * @author shuigedeng
	 * @since 2021/2/25 16:47
	 */
	ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair)
		throws Exception;

	default ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit)
		throws Exception {
		return this.tryLock(key, waitTime, leaseTime, unit, false);
	}

	default ZLock tryLock(String key, long waitTime, TimeUnit unit, boolean isFair)
		throws Exception {
		return this.tryLock(key, waitTime, -1, unit, isFair);
	}

	default ZLock tryLock(String key, long waitTime, TimeUnit unit) throws Exception {
		return this.tryLock(key, waitTime, -1, unit, false);
	}

	/**
	 * 释放锁
	 *
	 * @param lock 锁对象
	 * @author shuigedeng
	 * @since 2021/2/25 16:47
	 */
	void unlock(Object lock) throws Exception;

	/**
	 * 释放锁
	 *
	 * @param zLock 锁抽象对象
	 * @author shuigedeng
	 * @since 2021/2/25 16:47
	 */
	default void unlock(ZLock zLock) throws Exception {
		if (zLock != null) {
			this.unlock(zLock.getLock());
		}
	}
}
