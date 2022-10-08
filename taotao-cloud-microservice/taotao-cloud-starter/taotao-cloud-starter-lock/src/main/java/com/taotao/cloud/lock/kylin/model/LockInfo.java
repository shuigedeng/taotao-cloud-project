package com.taotao.cloud.lock.kylin.model;


import com.taotao.cloud.lock.kylin.executor.LockExecutor;

/**
 * 获取锁成功，返回对象 锁信息
 *
 * @author wangjinkui
 */
public class LockInfo {

	/**
	 * 锁名称
	 */
	private String lockKey;

	/**
	 * 过期时间
	 */
	private Long expire;

	/**
	 * 获取锁超时时间
	 */
	private Long acquireTimeout;

	/**
	 * 获取锁次数
	 */
	private int acquireCount;

	/**
	 * 锁实例
	 */
	private Object lockInstance;

	/**
	 * 锁执行器
	 */
	@SuppressWarnings("rawtypes")
	private LockExecutor lockExecutor;


	@SuppressWarnings("rawtypes")
	public LockInfo(String lockKey, Long expire, Long acquireTimeout, int acquireCount,
		Object lockInstance, LockExecutor lockExecutor) {
		this.lockKey = lockKey;
		this.expire = expire;
		this.acquireTimeout = acquireTimeout;
		this.acquireCount = acquireCount;
		this.lockInstance = lockInstance;
		this.lockExecutor = lockExecutor;
	}

	public String getLockKey() {
		return lockKey;
	}

	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public Long getAcquireTimeout() {
		return acquireTimeout;
	}

	public void setAcquireTimeout(Long acquireTimeout) {
		this.acquireTimeout = acquireTimeout;
	}

	public int getAcquireCount() {
		return acquireCount;
	}

	public void setAcquireCount(int acquireCount) {
		this.acquireCount = acquireCount;
	}

	public Object getLockInstance() {
		return lockInstance;
	}

	public void setLockInstance(Object lockInstance) {
		this.lockInstance = lockInstance;
	}

	@SuppressWarnings("rawtypes")
	public LockExecutor getLockExecutor() {
		return lockExecutor;
	}

	@SuppressWarnings("rawtypes")
	public void setLockExecutor(LockExecutor lockExecutor) {
		this.lockExecutor = lockExecutor;
	}

	@Override
	public String toString() {
		return "LockInfo{" +
			"lockKey='" + lockKey + '\'' +
			", expire=" + expire +
			", acquireTimeout=" + acquireTimeout +
			", acquireCount=" + acquireCount +
			", lockInstance=" + lockInstance +
			", lockExecutor=" + lockExecutor +
			'}';
	}
}
