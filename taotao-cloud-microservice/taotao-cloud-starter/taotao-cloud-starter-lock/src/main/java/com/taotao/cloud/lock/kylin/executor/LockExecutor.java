package com.taotao.cloud.lock.kylin.executor;


import com.taotao.cloud.lock.kylin.enums.LockType;

/**
 * 分布式锁核心处理器
 *
 * @author wangjinkui
 */
public interface LockExecutor<T> {

	/**
	 * 续期，目前只有redisson支持，且expire参数为-1才会续期
	 *
	 * @return 是否续期
	 */
	default boolean renewal() {
		return false;
	}

	/**
	 * 加锁
	 *
	 * @param lockKey        锁标识
	 * @param expire         锁有效时间
	 * @param acquireTimeout 获取锁超时时间
	 * @param lockType       锁类型
	 * @param keySuffix      联锁、红锁key后缀
	 * @return 锁信息
	 */
	T acquire(String lockKey, long expire, long acquireTimeout, LockType lockType,
		String[] keySuffix);

	/**
	 * 解锁
	 *
	 * @param lockInstance 锁实例
	 * @return 是否释放成功
	 */
	boolean releaseLock(T lockInstance);

	/**
	 * 获取加锁实例
	 *
	 * @param lockKey   加锁key
	 * @param lockType  {@link LockType} 加锁类型
	 * @param keySuffix 联锁、红锁key后缀
	 * @return 锁实例
	 */
	T getLockInstance(String lockKey, LockType lockType, String[] keySuffix);
}
