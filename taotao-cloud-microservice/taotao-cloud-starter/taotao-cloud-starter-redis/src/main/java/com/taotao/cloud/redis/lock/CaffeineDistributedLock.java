package com.taotao.cloud.redis.lock;


import com.taotao.cloud.common.lock.DistributedLock;
import com.taotao.cloud.common.lock.ZLock;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁 只能用redis实现
 * 写这个类的目的，只是为了防止代码启动报错
 *
 * @author zuihou
 * @date 2019/08/07
 */
public class CaffeineDistributedLock implements DistributedLock {

	@Override
	public ZLock lock(String key, long leaseTime,
		TimeUnit unit, boolean isFair) throws Exception {
		return null;
	}

	@Override
	public ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair)
		throws Exception {
		return null;
	}

	@Override
	public void unlock(Object lock) throws Exception {

	}
}
