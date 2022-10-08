package com.taotao.cloud.lock.kylin.aop;

import com.taotao.cloud.lock.kylin.annotation.KylinLock;
import com.taotao.cloud.lock.kylin.annotation.KylinLocks;
import com.taotao.cloud.lock.kylin.configuration.KylinLockProperties;
import com.taotao.cloud.lock.kylin.fail.LockFailureCallBack;
import com.taotao.cloud.lock.kylin.key.LockKeyBuilder;
import com.taotao.cloud.lock.kylin.model.LockInfo;
import com.taotao.cloud.lock.kylin.template.LockTemplate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 重复注解 ：实现多重复注解，多锁的功能 解析 KylinLocks 分布式锁aop处理器
 *
 * @author wangjinkui
 */
public class MultiLockInterceptor extends DefaultLockInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiLockInterceptor.class);

	public MultiLockInterceptor(LockTemplate lockTemplate,
		LockKeyBuilder lockKeyBuilder,
		LockFailureCallBack lockFailureCallBack,
		List<LockFailureCallBack> lockFailureCallBackList,
		KylinLockProperties lockProperties) {
		super(lockTemplate, lockKeyBuilder, lockFailureCallBack, lockFailureCallBackList,
			lockProperties);
	}

	/**
	 * 重复注解增强
	 *
	 * @param invocation 拦截器链
	 * @return 业务处理
	 * @throws Throwable 异常
	 */
	@Nullable
	@Override
	public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
		//验证class
		if (super.verifyAopClass(invocation)) {
			return invocation.proceed();
		}
		//获取注解
		KylinLocks kylinLocks = invocation.getMethod().getAnnotation(KylinLocks.class);
		List<LockInfo> lockInfoList = new ArrayList<>();
		try {
			KylinLock[] lockValues = kylinLocks.value();
			KylinLock nextLock = null;
			//循环加锁
			for (KylinLock kylinLock : lockValues) {
				nextLock = kylinLock;
				//加锁
				LockInfo lockInfo = super.lock(invocation, kylinLock);
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
			if (lockInfoList.size() == lockValues.length) {
				return invocation.proceed();
			}
			//失败
			return super.lockFailure(nextLock.lockFailure(), invocation.getMethod(),
				invocation.getArguments());
		} finally {
			if (lockInfoList.size() > 0) {
				lockInfoList.forEach(super::releaseLock);
			}
		}
	}
}
