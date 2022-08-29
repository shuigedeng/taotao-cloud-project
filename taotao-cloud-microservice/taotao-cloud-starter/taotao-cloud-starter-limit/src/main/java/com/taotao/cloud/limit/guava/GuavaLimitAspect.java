package com.taotao.cloud.limit.guava;

import com.google.common.util.concurrent.RateLimiter;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.log.LogUtils;
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

    @Around("@annotation(com.taotao.cloud.limit.guava.GuavaLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		GuavaLimit requestCurrentLimitingAnnotation = methodSignature.getMethod().getAnnotation(GuavaLimit.class);
        RateLimiter rateLimiter = rateLimiterMap.get(methodSignature.toString());

        if (rateLimiter==null) {
            rateLimiter = RateLimiter.create(requestCurrentLimitingAnnotation.token());
            rateLimiterMap.put(methodSignature.toString(),rateLimiter);
        }

		// 开始限流
        if (!rateLimiter.tryAcquire()) {
            return requestCurrentLimitingAnnotation.message();
        }

		try {
			return joinPoint.proceed();
		} catch (Throwable e) {
			LogUtils.error(e);
			throw new GuavaLimitException(ResultEnum.ERROR);
		}
    }

}
