package com.taotao.cloud.lock.kylin.aop;

import com.taotao.cloud.lock.kylin.annotation.KylinLock;
import com.taotao.cloud.lock.kylin.configuration.KylinLockProperties;
import com.taotao.cloud.lock.kylin.enums.LockType;
import com.taotao.cloud.lock.kylin.fail.LockFailureCallBack;
import com.taotao.cloud.lock.kylin.key.LockKeyBuilder;
import com.taotao.cloud.lock.kylin.model.LockInfo;
import com.taotao.cloud.lock.kylin.template.LockTemplate;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * 解析注解 KylinLock 默认 分布式锁aop处理器 方法拦截器,它是一个接口,用于Spring AOP编程中的动态代理.实现该接口可以对需要增强的方法进行增强.
 *
 * @author wangjinkui
 */
public class DefaultLockInterceptor implements LockInterceptor, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLockInterceptor.class);
	private final Map<Class<? extends LockFailureCallBack>, LockFailureCallBack> lockFailureCallBackMap = new LinkedHashMap<>();

	protected final LockTemplate lockTemplate;
	protected final LockKeyBuilder lockKeyBuilder;
	protected final LockFailureCallBack lockFailureCallBack;
	protected final List<LockFailureCallBack> lockFailureCallBackList;
	protected final KylinLockProperties lockProperties;

	/**
	 * @param lockTemplate            锁模板
	 * @param lockKeyBuilder          锁key组装器
	 * @param lockFailureCallBack     默认锁失败回调
	 * @param lockFailureCallBackList 锁失败回调类
	 * @param lockProperties          配置信息
	 */
	public DefaultLockInterceptor(LockTemplate lockTemplate,
		LockKeyBuilder lockKeyBuilder,
		LockFailureCallBack lockFailureCallBack,
		List<LockFailureCallBack> lockFailureCallBackList,
		KylinLockProperties lockProperties) {
		this.lockTemplate = lockTemplate;
		this.lockKeyBuilder = lockKeyBuilder;
		this.lockFailureCallBack = lockFailureCallBack;
		this.lockFailureCallBackList = lockFailureCallBackList;
		this.lockProperties = lockProperties;
	}

	/**
	 * 增强
	 *
	 * @param invocation 拦截器链
	 * @return 业务返回
	 * @throws Throwable 异常
	 */
	@Nullable
	@Override
	public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
		//1.验证class
		if (verifyAopClass(invocation)) {
			return invocation.proceed();
		}

		//2.获取注解
		KylinLock kylinLock = invocation.getMethod().getAnnotation(KylinLock.class);
		LockInfo lockInfo = null;
		try {
			//加锁
			lockInfo = lock(invocation, kylinLock);

			//加锁成功
			if (null != lockInfo) {
				LOGGER.debug("acquire lock success, lockKey:{}", lockInfo.getLockKey());
				//执行原始方法
				return invocation.proceed();
			}
			//加锁失败
			return this.lockFailure(kylinLock.lockFailure(), invocation.getMethod(),
				invocation.getArguments());
		} finally {
			//解锁
			releaseLock(lockInfo);
		}
	}

	/**
	 * 加锁
	 *
	 * @param invocation 拦截器链
	 * @param kylinLock  锁注解
	 * @return 锁信息
	 * @throws Throwable 异常
	 */
	protected LockInfo lock(@Nonnull MethodInvocation invocation, KylinLock kylinLock)
		throws Throwable {
		//组装锁key
		final String lockKey = getLockKey(invocation, kylinLock);
		//联锁、红锁后缀key
		final String[] kysSuffix = getKeySuffix(invocation, kylinLock);

		//加锁
		return lockTemplate.lock(lockKey, kylinLock.expire(), kylinLock.acquireTimeout(),
			kylinLock.executor(), kylinLock.lockType(), kysSuffix);
	}

	/**
	 * 加锁失败 执行失败回调方法 方法级
	 *
	 * @param clazz  失败回调类
	 * @param method 加锁方法
	 * @param args   加锁方法参数
	 * @return 失败回调方法返回值
	 * @throws Throwable 异常
	 */
	protected Object lockFailure(Class<? extends LockFailureCallBack> clazz, Method method,
		Object[] args) throws Throwable {
		//执行接口默认失败方法
		if (null == clazz || clazz == LockFailureCallBack.class) {
			lockFailureCallBack.callBack(method, args);
			return null;
		}

		//执行具体的失败方法
		LockFailureCallBack lockFailureCallBack = this.getLockFailureCallBack(clazz);
		//相同的方法名、参数类型列表。
		try {
			//获取、执行失败回调的方法
			return clazz.getMethod(method.getName(), method.getParameterTypes())
				.invoke(lockFailureCallBack, args);
		} catch (InvocationTargetException e) {
			if (null == e.getCause()) {
				throw e;
			}
			//加锁失败的方法所抛出的异常引起 InvocationTargetException
			//抛出原异常
			throw e.getCause();
		}
	}

	/**
	 * 解锁
	 *
	 * @param lockInfo 锁信息
	 */
	protected void releaseLock(LockInfo lockInfo) {
		if (null != lockInfo) {
			final boolean releaseLock = lockTemplate.releaseLock(lockInfo);
			if (releaseLock) {
				LOGGER.debug("release lock success, lockKey:{}", lockInfo.getLockKey());
			} else {
				LOGGER.error("release lock fail, lockKey:{}", lockInfo.getLockKey());
			}
		}
	}

	/**
	 * 联锁、红锁 key后缀支持SpEL表达式
	 *
	 * @param invocation 拦截器链
	 * @param kylinLock  锁注解
	 * @return 解析联锁、红锁 key后缀的值
	 */
	private String[] getKeySuffix(@Nonnull MethodInvocation invocation, KylinLock kylinLock) {
		String[] keySuffix = kylinLock.keySuffix();
		if (Objects.equals(kylinLock.lockType(), LockType.MULTI)
			|| Objects.equals(kylinLock.lockType(), LockType.RED)) {
			lockKeyBuilder.buildKeySuffix(invocation, keySuffix);
		}
		return keySuffix;
	}

	/**
	 * 组装 锁 key
	 *
	 * @param invocation 拦截器链
	 * @param kylinLock  锁注解
	 * @return 锁key
	 */
	private String getLockKey(@Nonnull MethodInvocation invocation, KylinLock kylinLock) {
		String prefix = lockProperties.getLockKeyPrefix() + ":";
		prefix += StringUtils.hasText(kylinLock.name()) ? kylinLock.name() :
			invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod()
				.getName();

		String buildKey = lockKeyBuilder.buildKey(invocation, kylinLock.keys());
		return StringUtils.hasText(buildKey) ? (prefix + "#" + buildKey) : prefix;
	}

	/**
	 * 验证class
	 *
	 * @param invocation 拦截器链
	 * @return 验证是否通过
	 */
	protected boolean verifyAopClass(@Nonnull MethodInvocation invocation) {
		//fix 使用其他aop组件时,aop切了两次.
		//获取一个代理对象的最终对象类型
		//https://blog.csdn.net/wolfcode_cn/article/details/80660478
		Class<?> cls = AopProxyUtils.ultimateTargetClass(
			Objects.requireNonNull(invocation.getThis()));
		return !cls.equals(invocation.getThis().getClass());
	}


	/**
	 * 获取锁的失败回调
	 *
	 * @param clazz 失败回调类
	 * @return 锁失败回调
	 */
	protected LockFailureCallBack getLockFailureCallBack(
		Class<? extends LockFailureCallBack> clazz) {
		final LockFailureCallBack lockFailureCallBack = lockFailureCallBackMap.get(clazz);
		Assert.notNull(lockFailureCallBack, String.format("can not get bean type of %s", clazz));
		return lockFailureCallBack;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(lockFailureCallBackList, "lockFailureCallBackList must have at least one");
		for (LockFailureCallBack lockFailureCallBack : lockFailureCallBackList) {
			lockFailureCallBackMap.put(lockFailureCallBack.getClass(), lockFailureCallBack);
		}
	}
}
