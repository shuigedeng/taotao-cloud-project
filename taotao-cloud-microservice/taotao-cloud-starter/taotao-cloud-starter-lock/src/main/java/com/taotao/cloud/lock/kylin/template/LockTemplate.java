package com.taotao.cloud.lock.kylin.template;

import com.taotao.cloud.lock.kylin.configuration.KylinLockProperties;
import com.taotao.cloud.lock.kylin.enums.LockType;
import com.taotao.cloud.lock.kylin.exception.LockException;
import com.taotao.cloud.lock.kylin.executor.LockExecutor;
import com.taotao.cloud.lock.kylin.model.LockInfo;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * 锁模板方法
 *
 * @author wangjinkui
 */
@SuppressWarnings("rawtypes")
public class LockTemplate implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(LockTemplate.class);

	private final Map<Class<? extends LockExecutor>, LockExecutor> executorMap = new LinkedHashMap<>();
	private final KylinLockProperties properties;
	private final List<LockExecutor> executors;
	private LockExecutor primaryExecutor;

	public LockTemplate(KylinLockProperties properties, List<LockExecutor> executors) {
		this.properties = properties;
		this.executors = executors;
	}

	/**
	 * 加锁 如果是不续期 则是取配置的过期时间 尝试获取锁超时时间 = -1
	 *
	 * @param key 锁标识
	 * @return 锁信息
	 */
	public LockInfo lock(String key) {
		return lock(key, LockType.REENTRANT);
	}


	/**
	 * 加锁 如果是不续期 则是取配置的过期时间 尝试获取锁超时时间 = -1
	 *
	 * @param key      锁标识
	 * @param lockType 锁类型
	 * @return 锁信息
	 */
	public LockInfo lock(String key, LockType lockType) {
		return lock(key, 0, -1, lockType);
	}

	/**
	 * 加锁 默认锁执行器
	 *
	 * @param key            锁标识
	 * @param expire         锁过期时间
	 * @param acquireTimeout 尝试获取锁超时时间(ms)
	 * @return 锁信息
	 */
	public LockInfo lock(String key, long expire, long acquireTimeout) {
		return lock(key, expire, acquireTimeout, null, LockType.REENTRANT, null);
	}


	/**
	 * 加锁 默认锁执行器
	 *
	 * @param key            锁key 同一个key只能被一个客户端持有
	 * @param expire         锁过期时间
	 * @param acquireTimeout 尝试获取锁超时时间(ms)
	 * @param lockType       锁类型
	 * @return 锁信息
	 */
	public LockInfo lock(String key, long expire, long acquireTimeout, LockType lockType) {
		return lock(key, expire, acquireTimeout, null, lockType, null);
	}

	/**
	 * @param key            锁key 同一个key只能被一个客户端持有
	 * @param expire         锁过期时间
	 * @param acquireTimeout 尝试获取锁超时时间(ms)
	 * @param executor       执行器
	 * @return 锁信息
	 */
	public LockInfo lock(String key, long expire, long acquireTimeout,
		Class<? extends LockExecutor> executor) {
		return lock(key, expire, acquireTimeout, executor, LockType.REENTRANT, null);
	}

	/**
	 * 加锁方法
	 *
	 * @param key            锁key 同一个key只能被一个客户端持有
	 * @param expire         过期时间(ms) 防止死锁
	 * @param acquireTimeout 尝试获取锁超时时间(ms)
	 * @param executor       执行器
	 * @param lockType       锁类型
	 * @param keySuffix      联锁后缀
	 * @return 加锁成功返回锁信息 失败返回null
	 */
	public LockInfo lock(String key, long expire, long acquireTimeout,
		Class<? extends LockExecutor> executor, LockType lockType, String[] keySuffix) {
		//默认重入锁
		lockType = null == lockType ? LockType.REENTRANT : lockType;
		acquireTimeout = acquireTimeout < 0 ? properties.getAcquireTimeout() : acquireTimeout;
		//锁重试间隔时间
		long retryInterval = properties.getRetryInterval();
		//锁执行器
		LockExecutor lockExecutor = obtainExecutor(executor);
		//renewal是否续期
		expire = !lockExecutor.renewal() && expire <= 0 ? properties.getExpire() : expire;
		int acquireCount = 0;

		long start = System.currentTimeMillis();

		try {
			do {
				acquireCount++;
				//获取锁
				Object lockInstance = lockExecutor.acquire(key, expire, acquireTimeout, lockType,
					keySuffix);
				if (null != lockInstance) {
					//锁信息
					return new LockInfo(key, expire, acquireTimeout, acquireCount, lockInstance,
						lockExecutor);
				}
				TimeUnit.MILLISECONDS.sleep(retryInterval);
			} while (System.currentTimeMillis() - start < acquireTimeout);
		} catch (InterruptedException e) {
			LOGGER.error("lock error", e);
			throw new LockException(e.getMessage());
		}
		return null;
	}

	/**
	 * 释放锁
	 *
	 * @param lockInfo 锁信息
	 * @return 是否释放锁成功
	 */
	@SuppressWarnings("unchecked")
	public boolean releaseLock(LockInfo lockInfo) {
		if (null == lockInfo) {
			return false;
		}
		return lockInfo.getLockExecutor().releaseLock(lockInfo.getLockInstance());
	}

	/**
	 * 获取锁的执行器
	 *
	 * @param clazz 执行器类
	 * @return 锁执行器
	 */
	protected LockExecutor obtainExecutor(Class<? extends LockExecutor> clazz) {
		if (null == clazz || clazz == LockExecutor.class) {
			return primaryExecutor;
		}
		final LockExecutor lockExecutor = executorMap.get(clazz);
		Assert.notNull(lockExecutor, String.format("can not get bean type of %s", clazz));
		return lockExecutor;
	}

	/**
	 * @param key      锁key 同一个key只能被一个客户端持有
	 * @param supplier 业务方法
	 * @param <T>      业务方法返回类型
	 * @return 业务方法返回值
	 */
	public <T> T execute(String key, Supplier<T> supplier) {
		return execute(key, LockType.REENTRANT, supplier);
	}


	/**
	 * @param key      锁key 同一个key只能被一个客户端持有
	 * @param lockType 锁类型
	 * @param supplier 业务方法
	 * @param <T>      业务方法返回类型
	 * @return 业务方法返回
	 */
	public <T> T execute(String key, LockType lockType, Supplier<T> supplier) {
		return execute(key, 0, -1, lockType, supplier);
	}

	/**
	 * @param key            锁key 同一个key只能被一个客户端持有
	 * @param expire         锁过期时间
	 * @param acquireTimeout 尝试获取锁超时时间
	 * @param supplier       业务方法
	 * @param <T>            业务方法返回类型
	 * @return 业务方法返回值
	 */
	public <T> T execute(String key, long expire, long acquireTimeout, Supplier<T> supplier) {
		return execute(key, expire, acquireTimeout, null, LockType.REENTRANT, supplier, null);
	}

	/**
	 * 编程式获取锁释放锁 默认锁执行器
	 *
	 * @param key            锁key 同一个key只能被一个客户端持有
	 * @param expire         锁过期时间
	 * @param acquireTimeout 尝试获取锁超时时间
	 * @param lockType       锁类型
	 * @param supplier       业务方法
	 * @param <T>            返回类型
	 * @return 业务方法返回
	 */
	public <T> T execute(String key, long expire, long acquireTimeout, LockType lockType,
		Supplier<T> supplier) {
		return execute(key, expire, acquireTimeout, null, lockType, supplier, null);
	}


	/**
	 * 编程式获取锁释放锁
	 *
	 * @param key            锁key 同一个key只能被一个客户端持有
	 * @param expire         锁过期时间
	 * @param acquireTimeout 尝试获取锁超时时间
	 * @param executor       执行器
	 * @param supplier       业务方法
	 * @param <T>            业务方法返回值类型
	 * @return 业务方法返回值
	 */
	public <T> T execute(String key, long expire, long acquireTimeout,
		Class<? extends LockExecutor> executor, Supplier<T> supplier) {
		return execute(key, expire, acquireTimeout, executor, LockType.REENTRANT, supplier, null);
	}

	/**
	 * 编程式获取锁释放锁
	 *
	 * @param key            锁key
	 * @param expire         锁过期时间
	 * @param acquireTimeout 尝试获取锁超时时间
	 * @param executor       执行器
	 * @param lockType       锁类型
	 * @param supplier       业务方法
	 * @param keySuffix      联锁 key 后缀
	 * @param <T>            业务方法返回值类型
	 * @return 业务方法返回值
	 */
	public <T> T execute(String key, long expire, long acquireTimeout,
		Class<? extends LockExecutor> executor, LockType lockType, Supplier<T> supplier,
		String[] keySuffix) {
		LockInfo lockInfo = null;
		try {
			//1、获取锁
			lockInfo = this.lock(key, expire, acquireTimeout, executor, lockType, keySuffix);
			if (null != lockInfo) {
				LOGGER.debug("acquire lock success, lockKey:{}", lockInfo.getLockKey());
				//2、执行业务
				return supplier.get();
			}
		} catch (Exception e) {
			LOGGER.error("acquire fail, lockKey:{} ", key);
			throw new LockException(e.getMessage());
		} finally {
			if (null != lockInfo) {
				//3、释放锁
				boolean releaseLock = this.releaseLock(lockInfo);
				if (releaseLock) {
					LOGGER.debug("release success, lockKey:{}", lockInfo.getLockKey());
				} else {
					LOGGER.error("release fail, lockKey:{}", lockInfo.getLockKey());
				}
			}
		}
		//获取锁失败，返回空。业务中用次判断
		return null;
	}

	/**
	 * 配置
	 *
	 * @throws Exception 异常
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(properties.getAcquireTimeout() >= 0, "tryTimeout must least 0");
		Assert.isTrue(properties.getExpire() >= -1, "expireTime must lease -1");
		Assert.isTrue(properties.getRetryInterval() >= 0, "retryInterval must more than 0");
		Assert.hasText(properties.getLockKeyPrefix(), "lock key prefix must be not blank");
		Assert.notEmpty(executors, "executors must have at least one");

		for (LockExecutor executor : executors) {
			executorMap.put(executor.getClass(), executor);
		}

		final Class<? extends LockExecutor> primaryExecutor = properties.getPrimaryExecutor();
		if (null == primaryExecutor) {
			this.primaryExecutor = executors.get(0);
		} else {
			this.primaryExecutor = executorMap.get(primaryExecutor);
			Assert.notNull(this.primaryExecutor, "primaryExecutor must be not null");
		}
	}

}
