package com.taotao.cloud.lock.kylin.aop;

import com.taotao.cloud.lock.kylin.annotation.KylinLock;
import com.taotao.cloud.lock.kylin.enums.LockType;
import com.taotao.cloud.lock.kylin.fail.LockFailureCallBack;
import com.taotao.cloud.lock.kylin.key.LockKeyBuilder;
import com.taotao.cloud.lock.kylin.model.LockInfo;
import com.taotao.cloud.lock.kylin.spring.boot.autoconfigure.KylinLockProperties;
import com.taotao.cloud.lock.kylin.template.LockTemplate;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 分布式锁aop处理器 环绕通知
 *
 * @author wangjinkui
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LockAspect implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(LockAspect.class);
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
	public LockAspect(LockTemplate lockTemplate,
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
	 * @param joinPoint 方法执行切点
	 * @return 业务方法返回
	 * @throws Throwable 异常
	 */
	@Around("@annotation(com.wjk.kylin.lock.annotation.KylinLock) || @annotation(com.wjk.kylin.lock.annotation.KylinLocks)")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		List<LockInfo> lockInfoList = new ArrayList<>();
		try {
			//获取方法
			Method method = this.getMethod(joinPoint);
			Object[] args = joinPoint.getArgs();
			//获取注解
			KylinLock[] locks = method.getAnnotationsByType(KylinLock.class);

			KylinLock nextLock = null;
			//循环加锁
			for (KylinLock kylinLock : locks) {
				nextLock = kylinLock;
				//加锁
				LockInfo lockInfo = this.lock(method, args, kylinLock);
				//加锁成功
				if (null != lockInfo) {
					LOGGER.debug("acquire lock success, lockKey:{}", lockInfo.getLockKey());
					lockInfoList.add(lockInfo);
				} else {
					//只要有一个失败，则跳出
					break;
				}
			}

			//全部加锁成功，才算成功
			if (lockInfoList.size() == locks.length) {
				return joinPoint.proceed();
			}
			//失败
			return this.lockFailure(nextLock.lockFailure(), method, args);
		} finally {
			if (lockInfoList.size() > 0) {
				lockInfoList.forEach(this::releaseLock);
			}
		}
	}

	/**
	 * 加锁
	 *
	 * @param method    加锁方法
	 * @param args      加锁方法参数
	 * @param kylinLock 锁注解
	 * @return 锁信息
	 * @throws Throwable 异常
	 */
	protected LockInfo lock(@Nonnull Method method, Object[] args, KylinLock kylinLock)
		throws Throwable {
		//组装锁key
		final String lockKey = getLockKey(method, args, kylinLock);
		//联锁、红锁后缀key
		final String[] kysSuffix = getKeySuffix(method, args, kylinLock);

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
	 * @param method    加锁方法
	 * @param args      加锁方法参数
	 * @param kylinLock 锁注解
	 * @return 解析联锁、红锁 key后缀的值
	 */
	private String[] getKeySuffix(@Nonnull Method method, Object[] args, KylinLock kylinLock) {
		String[] keySuffix = kylinLock.keySuffix();
		if (Objects.equals(kylinLock.lockType(), LockType.MULTI)
			|| Objects.equals(kylinLock.lockType(), LockType.RED)) {
			lockKeyBuilder.buildKeySuffix(method, args, keySuffix);
		}
		return keySuffix;
	}

	/**
	 * 组装 锁 key
	 *
	 * @param method    加锁方法
	 * @param args      加锁方法参数
	 * @param kylinLock 锁注解
	 * @return 锁key
	 */
	private String getLockKey(@Nonnull Method method, Object[] args, KylinLock kylinLock) {
		String prefix = lockProperties.getLockKeyPrefix() + ":";
		prefix += StringUtils.hasText(kylinLock.name()) ? kylinLock.name() :
			method.getDeclaringClass().getName() + "." + method.getName();

		String buildKey = lockKeyBuilder.buildKey(method, args, kylinLock.keys());
		return StringUtils.hasText(buildKey) ? (prefix + "#" + buildKey) : prefix;
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

	/**
	 * 获取执行方法
	 *
	 * @param joinPoint 方法执行切点
	 * @return
	 */
	protected Method getMethod(@Nonnull ProceedingJoinPoint joinPoint) {
		Method method = null;

		try {
			Signature signature = joinPoint.getSignature();
			MethodSignature ms = (MethodSignature) signature;
			Object target = joinPoint.getTarget();
			method = target.getClass().getMethod(ms.getName(), ms.getParameterTypes());
		} catch (NoSuchMethodException e) {
			LOGGER.error("acquire lock get method error", e);
		}

		return method;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(lockFailureCallBackList, "lockFailureCallBackList must have at least one");
		for (LockFailureCallBack lockFailureCallBack : lockFailureCallBackList) {
			lockFailureCallBackMap.put(lockFailureCallBack.getClass(), lockFailureCallBack);
		}
	}
}
