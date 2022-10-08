package com.taotao.cloud.lock.kylin.executor.zookeeper;

import com.taotao.cloud.lock.kylin.enums.LockType;
import com.taotao.cloud.lock.kylin.exception.LockException;
import com.taotao.cloud.lock.kylin.executor.AbstractLockExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * 支持读写锁、联锁、重入锁、不可重入锁 分布式锁zookeeper处理器
 *
 * @author wangjinkui
 */
public class ZookeeperLockExecutor extends AbstractLockExecutor<InterProcessLock> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperLockExecutor.class);
	//用于支持重入锁
	private static final ThreadLocal<LockData> LOCK_DATA_THREAD_LOCAL = new ThreadLocal<>();

	private final CuratorFramework curatorFramework;

	public ZookeeperLockExecutor(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}

	/**
	 * 加锁
	 *
	 * @param lockKey        锁标识
	 * @param expire         锁有效时间
	 * @param acquireTimeout 获取锁超时时间
	 * @param lockType       锁类型
	 * @param keySuffix      锁key后缀集合
	 * @return 锁实例
	 */
	@Override
	public InterProcessLock acquire(String lockKey, long expire, long acquireTimeout,
		LockType lockType, String[] keySuffix) {
		if (!CuratorFrameworkState.STARTED.equals(curatorFramework.getState())) {
			LOGGER.warn("instance must be started before calling this method");
			return null;
		}

		try {
			//加锁实例
			InterProcessLock lockInstance = getLockInstance(lockKey, lockType, keySuffix);

			if (lockInstance == null) {
				throw new LockException("lock instance is null");
			}

			final boolean locked = lockInstance.acquire(acquireTimeout, TimeUnit.MILLISECONDS);
			if (!locked) {
				removeReentrantLockData();
			}
			return obtainLockInstance(locked, lockInstance);
		} catch (LockException e) {
			throw e;
		} catch (Exception e) {
			removeReentrantLockData();
			LOGGER.error("zookeeper lock acquire error", e);
			return null;
		}
	}

	/**
	 * 释放锁
	 *
	 * @param lockInstance 锁实例
	 * @return 是否释放锁成功
	 */
	@Override
	public boolean releaseLock(InterProcessLock lockInstance) {
		try {
			if (lockInstance instanceof InterProcessMutex) {
				InterProcessMutex interProcessMutex = (InterProcessMutex) lockInstance;
				if (interProcessMutex.isOwnedByCurrentThread()) {
					interProcessMutex.release();
				}
				//移除重入锁
				removeReentrantLockData();
			} else {
				if (lockInstance.isAcquiredInThisProcess()) {
					lockInstance.release();
				}
			}
		} catch (Exception e) {
			LOGGER.error("zookeeper lock release error", e);
			return false;
		}
		return true;
	}


	/**
	 * 获取加锁实例
	 *
	 * @param lockKey   加锁key
	 * @param lockType  {@link LockType} 加锁类型 ： 重入锁、读写锁、联锁
	 * @param keySuffix 锁后缀集合
	 * @return 锁实例
	 */
	@Override
	public InterProcessLock getLockInstance(String lockKey, LockType lockType, String[] keySuffix) {
		//以”/“ 开始
		String nodePath = lockKey.startsWith("/") ? lockKey : "/" + lockKey;

		InterProcessLock lockInstance = null;

		switch (lockType) {
			case REENTRANT:
				lockInstance = this.getReentrantLockData(nodePath);
				break;
			case READ:
				lockInstance = new InterProcessReadWriteLock(curatorFramework, nodePath).readLock();
				break;
			case WRITE:
				lockInstance = new InterProcessReadWriteLock(curatorFramework,
					nodePath).writeLock();
				break;
			case MULTI:
				lockInstance = this.getMultiLock(nodePath, keySuffix);
				break;
			case SEMAPHORE:
				lockInstance = new InterProcessSemaphoreMutex(curatorFramework, nodePath);
				break;
			default:
				LOGGER.error("lockType is not support ,lockType:{}", lockType);
		}
		return lockInstance;
	}

	/**
	 * 移除重入锁
	 */
	private void removeReentrantLockData() {
		LockData lockData = LOCK_DATA_THREAD_LOCAL.get();
		if (null != lockData) {
			//减1
			int lockCount = lockData.lockCount.decrementAndGet();
			if (lockCount <= 0) {
				LOCK_DATA_THREAD_LOCAL.remove();
			}
		}
	}

	/**
	 * 重入锁对象
	 *
	 * @param nodePath 锁key
	 * @return 重入锁对象
	 */
	private InterProcessLock getReentrantLockData(String nodePath) {
		InterProcessLock lockInstance;
		LockData lockData = LOCK_DATA_THREAD_LOCAL.get();
		if (null == lockData) {
			lockInstance = new InterProcessMutex(curatorFramework, nodePath);
			lockData = new LockData(lockInstance);
			LOCK_DATA_THREAD_LOCAL.set(lockData);
		} else {
			lockInstance = lockData.interProcessLock;
			//加1
			lockData.lockCount.incrementAndGet();
		}
		return lockInstance;
	}

	/**
	 * 组织联锁
	 *
	 * @param nodePath
	 * @param keySuffix
	 * @return
	 */
	private InterProcessLock getMultiLock(String nodePath, String[] keySuffix) {
		keySuffix = super.defaultKeySuffix(keySuffix);

		List<InterProcessLock> list = new ArrayList<>(keySuffix.length);
		for (String suffix : keySuffix) {
			StringBuilder sb = new StringBuilder(nodePath);
			if (StringUtils.hasText(suffix)) {
				sb.append("/").append(suffix);
			}
			list.add(new InterProcessMutex(curatorFramework, sb.toString()));
		}

		return new InterProcessMultiLock(list);
	}

	/**
	 * 重入锁对象数据信息
	 */
	private static class LockData {

		//重入锁对象
		final InterProcessLock interProcessLock;
		//重入次数
		final AtomicInteger lockCount;

		private LockData(@NonNull InterProcessLock interProcessLock) {
			this.interProcessLock = interProcessLock;
			this.lockCount = new AtomicInteger(1);
		}
	}
}
