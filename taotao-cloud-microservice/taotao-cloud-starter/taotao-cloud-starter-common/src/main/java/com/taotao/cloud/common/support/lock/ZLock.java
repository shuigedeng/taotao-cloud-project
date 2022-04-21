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

/**
 * 锁对象抽象
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:28:36
 */
public class ZLock implements AutoCloseable {

	/**
	 * lock
	 */
	private final Object lock;

	/**
	 * locker
	 */
	private final DistributedLock locker;

	public ZLock(Object lock, DistributedLock locker) {
		this.lock = lock;
		this.locker = locker;
	}

	@Override
	public void close() throws Exception {
		locker.unlock(lock);
	}

	public Object getLock() {
		return lock;
	}

	public DistributedLock getLocker() {
		return locker;
	}
}
