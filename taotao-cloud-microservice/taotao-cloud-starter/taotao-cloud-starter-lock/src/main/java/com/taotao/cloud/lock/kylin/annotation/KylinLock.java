package com.taotao.cloud.lock.kylin.annotation;


import com.taotao.cloud.lock.kylin.enums.LockType;
import com.taotao.cloud.lock.kylin.executor.LockExecutor;
import com.taotao.cloud.lock.kylin.fail.LockFailureCallBack;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁注解
 *
 * @author wangjinkui
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Repeatable(KylinLocks.class)
public @interface KylinLock {

	/**
	 * 锁的key的一部分,为空则会使用 包名.类名.方法名
	 *
	 * @return 名称
	 */
	String name() default "";

	/**
	 * 锁的key一部分 支持 SpEL表达式 锁的key = prefix:name#keys
	 * prefix：{@link KylinLockProperties#getLockKeyPrefix()}
	 *
	 * @return KEY
	 */
	String[] keys() default "";

	/**
	 * @return 过期时间 单位：毫秒
	 * <pre>
	 *     过期时间一定是要长于业务的执行时间. 未设置则为默认时间30秒 默认值：{@link KylinLockProperties#getExpire()}
	 * </pre>
	 */
	long expire() default -1;

	/**
	 * @return 获取锁超时时间 单位：毫秒
	 * <pre>
	 *     结合业务,建议该时间不宜设置过长,特别在并发高的情况下. 未设置则为默认时间3秒 默认值：{@link KylinLockProperties#getAcquireTimeout()}
	 * </pre>
	 */
	long acquireTimeout() default -1;

	/**
	 * @return lock 执行器
	 */
	Class<? extends LockExecutor> executor() default LockExecutor.class;

	/**
	 * @return 锁类型 默认重入锁
	 */
	LockType lockType() default LockType.REENTRANT;

	/**
	 * redisson 红锁联锁 zk联锁 联红锁个数以及其中每个锁的后缀key 默认三个，原始key + 1,2,3 支持SpEL 表达式
	 *
	 * @return key后缀
	 */
	String[] keySuffix() default {"1", "2", "3"};

	/**
	 * 加锁失败回调
	 *
	 * @return 回调类
	 */
	Class<? extends LockFailureCallBack> lockFailure() default LockFailureCallBack.class;

}
