package com.taotao.cloud.limit.guava;

import com.google.common.util.concurrent.RateLimiter;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.limit.annotation.GuavaLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Guava限制aop
 *
 * @author shuigedeng
 * @version 2022.08
 * @since 2022-08-08 10:33:24
 */
@Aspect
public class GuavaLimitAspect {

	private final ConcurrentHashMap<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

	@Around(value = "@annotation(guavaLimit)")
	public Object around(ProceedingJoinPoint joinPoint, GuavaLimit guavaLimit) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		RateLimiter rateLimiter = rateLimiterMap.get(methodSignature.toString());

		if (rateLimiter == null) {
			rateLimiter = RateLimiter.create(guavaLimit.token());
			rateLimiterMap.put(methodSignature.toString(), rateLimiter);
		}

		// 开始限流
		if (!rateLimiter.tryAcquire()) {
			return guavaLimit.message();
		}

		try {
			return joinPoint.proceed();
		} catch (Throwable e) {
			LogUtils.error(e);
			throw new GuavaLimitException(ResultEnum.ERROR);
		}
	}

}
