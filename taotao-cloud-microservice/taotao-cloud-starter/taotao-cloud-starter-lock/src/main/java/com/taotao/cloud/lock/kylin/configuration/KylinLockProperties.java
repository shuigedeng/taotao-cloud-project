package com.taotao.cloud.lock.kylin.configuration;

import com.taotao.cloud.lock.kylin.executor.LockExecutor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * kylin-lock 配置
 *
 * @author wangjinkui
 */
@RefreshScope
@ConfigurationProperties(prefix = "kylin.lock")
public class KylinLockProperties {

	/**
	 * 过期时间 单位：毫秒
	 */
	private long expire = 30000L;

	/**
	 * 获取锁超时时间 单位：毫秒
	 */
	private long acquireTimeout = 3000L;

	/**
	 * 获取锁失败时重试时间间隔 单位：毫秒
	 */
	private long retryInterval = 100L;

	/**
	 * 默认执行器，不设置默认取容器第一个(默认注入顺序，redisson>zookeeper)
	 */
	@SuppressWarnings("rawtypes")
	private Class<? extends LockExecutor> primaryExecutor;

	/**
	 * 锁key前缀
	 */
	private String lockKeyPrefix = "kylin-lock";

	//默认开启 redisson lock
	private Boolean redisson = true;


	public Boolean getRedisson() {
		return redisson;
	}

	public void setRedisson(Boolean redisson) {
		this.redisson = redisson;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public long getAcquireTimeout() {
		return acquireTimeout;
	}

	public void setAcquireTimeout(long acquireTimeout) {
		this.acquireTimeout = acquireTimeout;
	}

	public long getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(long retryInterval) {
		this.retryInterval = retryInterval;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends LockExecutor> getPrimaryExecutor() {
		return primaryExecutor;
	}

	@SuppressWarnings("rawtypes")
	public void setPrimaryExecutor(Class<? extends LockExecutor> primaryExecutor) {
		this.primaryExecutor = primaryExecutor;
	}

	public String getLockKeyPrefix() {
		return lockKeyPrefix;
	}

	public void setLockKeyPrefix(String lockKeyPrefix) {
		this.lockKeyPrefix = lockKeyPrefix;
	}

	@Override
	public String toString() {
		return "KylinLockProperties{" +
			"expire=" + expire +
			", acquireTimeout=" + acquireTimeout +
			", retryInterval=" + retryInterval +
			", primaryExecutor=" + primaryExecutor +
			", lockKeyPrefix='" + lockKeyPrefix + '\'' +
			", redisson=" + redisson +
			'}';
	}
}
