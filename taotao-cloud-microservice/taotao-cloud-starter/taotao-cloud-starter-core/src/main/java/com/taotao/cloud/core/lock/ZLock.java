package com.taotao.cloud.core.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 锁对象抽象
 */
@AllArgsConstructor
public class ZLock implements AutoCloseable {
	@Getter
	private final Object lock;

	private final DistributedLock locker;

	@Override
	public void close() throws Exception {
		locker.unlock(lock);
	}
}
