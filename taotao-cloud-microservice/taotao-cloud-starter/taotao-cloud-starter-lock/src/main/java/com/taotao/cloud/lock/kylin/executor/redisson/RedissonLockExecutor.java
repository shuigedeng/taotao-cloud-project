package com.taotao.cloud.lock.kylin.executor.redisson;

import com.taotao.cloud.lock.kylin.enums.LockType;
import com.taotao.cloud.lock.kylin.exception.LockException;
import com.taotao.cloud.lock.kylin.executor.AbstractLockExecutor;
import java.util.concurrent.TimeUnit;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * redisson 重入锁、读写锁、联锁、红锁、公平锁
 *
 * @author wangjinkui
 */
public class RedissonLockExecutor extends AbstractLockExecutor<RLock> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedissonLockExecutor.class);

	private final RedissonClient redissonClient;

	public RedissonLockExecutor(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
		LOGGER.debug("kylin-lock init redisson lock executor.");
	}

	/**
	 * 是否续期
	 *
	 * @return 是否续期
	 */
	@Override
	public boolean renewal() {
		return true;
	}

	/**
	 * 加锁
	 *
	 * @param lockKey        锁标识
	 * @param expire         锁有效时间
	 * @param acquireTimeout 获取锁超时时间
	 * @param lockType       锁类型
	 * @param keySuffix      联锁、红锁key后缀
	 * @return 锁实例
	 */
	@Override
	public RLock acquire(String lockKey, long expire, long acquireTimeout, LockType lockType,
		String[] keySuffix) {
		try {
			//
			RLock lockInstance = getLockInstance(lockKey, lockType, keySuffix);

			if (null == lockInstance) {
				throw new LockException("lock instance is null");
			}

			final boolean locked = lockInstance.tryLock(acquireTimeout, expire,
				TimeUnit.MILLISECONDS);
			return obtainLockInstance(locked, lockInstance);
		} catch (LockException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("redisson lock acquire error", e);
			return null;
		}
	}

	/**
	 * 释放锁 正常解锁
	 *
	 * @param lockInstance 锁实例
	 * @return 是否释放锁成功
	 */
	@Override
	public boolean releaseLock(RLock lockInstance) {
		//是否当前线程加锁
		try {
			if (lockInstance instanceof RedissonMultiLock) {
				lockInstance.unlock();
				return true;
			} else {
				if (lockInstance.isLocked() && lockInstance.isHeldByCurrentThread()) {
					lockInstance.unlock();
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("redisson lock release error", e);
			return false;
		}
		return false;
	}


	/**
	 * 获取加锁实例
	 *
	 * @param lockKey   加锁key
	 * @param lockType  {@link LockType} 加锁类型 ： 重入锁、读写锁、联锁
	 * @param keySuffix 联锁、红锁key后缀
	 * @return 锁实例
	 */
	@Override
	public RLock getLockInstance(String lockKey, LockType lockType, String[] keySuffix) {
		RLock lockInstance = null;
		switch (lockType) {
			case REENTRANT:
				lockInstance = redissonClient.getLock(lockKey);
				break;
			case READ:
				lockInstance = redissonClient.getReadWriteLock(lockKey).readLock();
				break;
			case WRITE:
				lockInstance = redissonClient.getReadWriteLock(lockKey).writeLock();
				break;
			case MULTI:
				lockInstance = this.getMultiLock(lockKey, keySuffix);
				break;
			case RED:
				lockInstance = this.getRedLock(lockKey, keySuffix);
				break;
			case FAIR:
				lockInstance = redissonClient.getFairLock(lockKey);
				break;
			default:
				LOGGER.error("lockType is not support ,lockType:{}", lockType);
		}
		return lockInstance;
	}

	/**
	 * 红锁
	 *
	 * @param lockKey   锁标识
	 * @param keySuffix 锁key后缀集合
	 * @return 红锁
	 */
	private RLock getRedLock(String lockKey, String[] keySuffix) {
		RLock[] locks = this.getRLockArray(lockKey, keySuffix);
		return new RedissonRedLock(locks);
	}

	/**
	 * 组织联锁
	 *
	 * @param lockKey   锁key
	 * @param keySuffix 锁key后缀集合
	 * @return 联锁
	 */
	private RLock getMultiLock(String lockKey, String[] keySuffix) {
		RLock[] locks = this.getRLockArray(lockKey, keySuffix);
		return new RedissonMultiLock(locks);
	}

	/**
	 * 获取锁数组 用于红锁、联锁 使用可重入锁
	 *
	 * @param lockKey   锁标识
	 * @param keySuffix 锁标识后缀
	 * @return 锁实例集合
	 */
	private RLock[] getRLockArray(String lockKey, String[] keySuffix) {
		keySuffix = super.defaultKeySuffix(keySuffix);
		RLock[] locks = new RLock[keySuffix.length];
		for (int i = 0; i < keySuffix.length; i++) {
			String suffix = keySuffix[i];
			StringBuilder sb = new StringBuilder(lockKey);
			if (StringUtils.hasText(suffix)) {
				sb.append(":").append(suffix);
			}
			locks[i] = redissonClient.getLock(sb.toString());
		}
		return locks;
	}
}
