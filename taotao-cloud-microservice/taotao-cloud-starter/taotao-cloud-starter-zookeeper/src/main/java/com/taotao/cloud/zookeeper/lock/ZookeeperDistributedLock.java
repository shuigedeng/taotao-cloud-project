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
package com.taotao.cloud.zookeeper.lock;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.common.support.lock.DistributedLock;
import com.taotao.cloud.common.support.lock.ZLock;
import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * zookeeper分布式锁实现
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:38:24
 */
public class ZookeeperDistributedLock implements DistributedLock {

	public static final String LOCK_KEY_PREFIX = "LOCK_KEY";

	private final CuratorFramework client;

	public ZookeeperDistributedLock(CuratorFramework client) {
		this.client = client;
	}

	private ZLock getLock(String key) {
		InterProcessMutex lock = new InterProcessMutex(client, getPath(key));
		return new ZLock(lock, this);
	}

	@Override
	public ZLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
		ZLock zLock = this.getLock(key);
		InterProcessMutex ipm = (InterProcessMutex) zLock.getLock();
		ipm.acquire();
		return zLock;
	}

	@Override
	public ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair)
		throws Exception {
		ZLock zLock = this.getLock(key);
		InterProcessMutex ipm = (InterProcessMutex) zLock.getLock();
		if (ipm.acquire(waitTime, unit)) {
			return zLock;
		}
		return null;
	}

	@Override
	public void unlock(Object lock) throws Exception {
		if (lock != null) {
			if (lock instanceof InterProcessMutex) {
				InterProcessMutex ipm = (InterProcessMutex) lock;
				if (ipm.isAcquiredInThisProcess()) {
					ipm.release();
				}
			} else {
				throw new LockException("requires InterProcessMutex type");
			}
		}
	}

	/**
	 * 锁路径
	 *
	 * @param key 锁路径key
	 * @return 锁路径
	 * @since 2021-09-07 20:52:58
	 */
	private String getPath(String key) {
		return CommonConstant.PATH_SPLIT + LOCK_KEY_PREFIX
			+ CommonConstant.PATH_SPLIT + key;
	}
}
