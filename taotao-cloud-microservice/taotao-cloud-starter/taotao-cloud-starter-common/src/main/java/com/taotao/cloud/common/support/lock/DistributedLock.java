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
package com.taotao.cloud.common.support.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁顶级接口
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:25:06
 */
public interface DistributedLock {

	/**
	 * 获取锁，如果获取不成功则一直等待直到lock被获取
	 *
	 * @param key       锁key
	 * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁； 如果leaseTime为-1，则保持锁定直到显式解锁
	 * @param unit      参数的时间单位
	 * @param isFair    是否公平锁
	 * @return ZLock对象
	 * @since 2021-09-02 20:25:19
	 */
	ZLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;

	/**
	 * 加锁
	 *
	 * @param key       锁key
	 * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁； 如果leaseTime为-1，则保持锁定直到显式解锁
	 * @param unit      参数的时间单位
	 * @return ZLock对象
	 * @since 2021-09-02 20:25:36
	 */
	default ZLock lock(String key, long leaseTime, TimeUnit unit) throws Exception {
		return this.lock(key, leaseTime, unit, false);
	}

	/**
	 * 加锁
	 *
	 * @param key    锁key
	 * @param isFair 是否是公平嗦
	 * @return ZLock对象
	 * @since 2021-09-02 20:25:39
	 */
	default ZLock lock(String key, boolean isFair) throws Exception {
		return this.lock(key, -1, null, isFair);
	}

	/**
	 * 加锁
	 *
	 * @param key 锁key
	 * @return ZLock对象
	 * @since 2021-09-02 20:25:46
	 */
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
	 * @return ZLock对象
	 * @since 2021-09-02 20:26:06
	 */
	ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair)
		throws Exception;

	/**
	 * 尝试获取锁
	 *
	 * @param key       锁key
	 * @param waitTime  获取锁的最大尝试时间
	 * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁； 如果leaseTime为-1，则保持锁定直到显式解锁
	 * @param unit      参数的时间单位
	 * @return ZLock对象
	 * @since 2021-09-02 20:26:22
	 */
	default ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit)
		throws Exception {
		return this.tryLock(key, waitTime, leaseTime, unit, false);
	}

	/**
	 * 尝试获取锁
	 *
	 * @param key      锁key
	 * @param waitTime 获取锁的最大尝试时间
	 * @param unit     时间单位
	 * @param isFair   是否是公平嗦
	 * @return ZLock对象
	 * @since 2021-09-02 20:26:25
	 */
	default ZLock tryLock(String key, long waitTime, TimeUnit unit, boolean isFair)
		throws Exception {
		return this.tryLock(key, waitTime, -1, unit, isFair);
	}

	/**
	 * 尝试获取锁
	 *
	 * @param key      锁key
	 * @param waitTime 获取锁的最大尝试时间
	 * @param unit     时间单位
	 * @return ZLock对象
	 * @since 2021-09-02 20:26:27
	 */
	default ZLock tryLock(String key, long waitTime, TimeUnit unit) throws Exception {
		return this.tryLock(key, waitTime, -1, unit, false);
	}

	/**
	 * 释放锁
	 *
	 * @param lock 锁对象
	 * @since 2021-09-02 20:26:33
	 */
	void unlock(Object lock) throws Exception;

	/**
	 * 释放锁
	 *
	 * @param zLock 锁抽象对象
	 * @since 2021-09-02 20:26:42
	 */
	default void unlock(ZLock zLock) throws Exception {
		if (zLock != null) {
			this.unlock(zLock.getLock());
		}
	}
}
