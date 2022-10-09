package com.taotao.cloud.lock.kylin.executor;

/**
 * 抽象 锁 执行器
 *
 * @author wangjinkui
 */
public abstract class AbstractLockExecutor<T> implements LockExecutor<T> {

	private static final String[] KEY_SUFFIX = {"1", "2", "3"};

	/**
	 * 获取锁的实例
	 *
	 * @param locked       是否加锁成功
	 * @param lockInstance 锁实例
	 * @return 锁实例
	 */
	protected T obtainLockInstance(boolean locked, T lockInstance) {
		return locked ? lockInstance : null;
	}

	/**
	 * 默认联锁红锁key后缀
	 *
	 * @param keySuffix 后缀集合
	 * @return 锁后缀集合
	 */
	protected String[] defaultKeySuffix(String[] keySuffix) {
		if (null == keySuffix || keySuffix.length == 0) {
			keySuffix = AbstractLockExecutor.KEY_SUFFIX;
		}
		return keySuffix;
	}
}
