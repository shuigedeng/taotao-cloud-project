/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.support.lock;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.exception.LockException;
import com.taotao.cloud.common.utils.aop.AopUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 分布式锁切面
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:27:36
 */
@Aspect
public class LockAspectUtil extends AopUtil {

	@Autowired(required = false)
	private DistributedLock distributedLock;

	/**
	 * 用于SpEL表达式解析.
	 */
	private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

	/**
	 * 用于获取方法参数定义名字.
	 */
	private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

	@Around("@within(lock) || @annotation(lock)")
	public Object aroundLock(ProceedingJoinPoint point, Lock lock)
			throws Throwable {
		if (lock == null) {
			lock = point.getTarget().getClass().getDeclaredAnnotation(Lock.class);
		}
		String lockKey = lock.key();

		if (distributedLock == null) {
			throw new LockException("DistributedLock is null");
		}

		if (StrUtil.isEmpty(lockKey)) {
			throw new LockException("lockKey is null");
		}

		if (lockKey.contains("#")) {
			MethodSignature methodSignature = (MethodSignature) point.getSignature();
			Object[] args = point.getArgs();
			lockKey = getValBySpEL(lockKey, methodSignature, args);
		}

		ZLock lockObj = null;
		try {
			//加锁
			if (lock.waitTime() > 0) {
				lockObj = distributedLock.tryLock(lockKey,
						lock.waitTime(),
						lock.leaseTime(),
						lock.unit(),
						lock.isFair());
			} else {
				lockObj = distributedLock.tryLock(lockKey,
						lock.leaseTime(),
						lock.unit(),
						lock.isFair());
			}

			if (lockObj != null) {
				LogUtil.info("获取Redis分布式锁[成功]，加锁完成，开始执行业务逻辑...");
				return point.proceed();
			} else {
				LogUtil.error("获取分布式锁[失败]");
				throw new LockException("锁等待超时");
			}
		} finally {
			distributedLock.unlock(lockObj);
		}
	}

	/**
	 * 解析spEL表达式
	 *
	 * @param spEL            spEL
	 * @param methodSignature 方法签名
	 * @param args            参数
	 * @return 表达式值
	 * @since 2021-09-02 20:28:11
	 */
	private String getValBySpEL(String spEL, MethodSignature methodSignature, Object[] args) {
		//获取方法形参名数组
		String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
		if (paramNames != null && paramNames.length > 0) {
			Expression expression = spelExpressionParser.parseExpression(spEL);
			// spring的表达式上下文对象
			EvaluationContext context = new StandardEvaluationContext();
			// 给上下文赋值
			for (int i = 0; i < args.length; i++) {
				context.setVariable(paramNames[i], args[i]);
			}
			return Objects.requireNonNull(expression.getValue(context)).toString();
		}
		return null;
	}
}
