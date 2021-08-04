/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.web.limit;

import com.aliyun.oss.common.utils.StringUtils;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * LimitAspect
 *
 * @RestController
 * public class LimiterController {
 * private static final AtomicInteger ATOMIC_INTEGER_1 = new AtomicInteger();
 * private static final AtomicInteger ATOMIC_INTEGER_2 = new AtomicInteger();
 * private static final AtomicInteger ATOMIC_INTEGER_3 = new AtomicInteger();
 *
 * Limit(key = "limitTest", period = 10, count = 3)
 * GetMapping("/limitTest1")
 * public int testLimiter1() {
 * 	return ATOMIC_INTEGER_1.incrementAndGet();
 * 	}
 *
 * GetMapping("/limitTest2")
 * public int testLimiter2() {
 * 	return ATOMIC_INTEGER_2.incrementAndGet();
 * 	}
 *
 * GetMapping("/limitTest3")
 * public int testLimiter3() {
 * 	return ATOMIC_INTEGER_3.incrementAndGet();
 * 	}
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/08/04 08:49
 */
@Aspect
public class LimitAspect {

	private static final String UNKNOWN = "unknown";

	private final RedisTemplate<String, Serializable> limitRedisTemplate;

	@Autowired
	public LimitAspect(RedisTemplate<String, Serializable> limitRedisTemplate) {
		this.limitRedisTemplate = limitRedisTemplate;
	}

	/**
	 * @param pjp
	 * @author xiaofu
	 * @description 切面
	 * @date 2020/4/8 13:04
	 */
	@Around("execution(public * *(..)) && @annotation(com.xiaofu.limit.api.Limit)")
	public Object interceptor(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		Limit limitAnnotation = method.getAnnotation(Limit.class);
		LimitType limitType = limitAnnotation.limitType();
		String name = limitAnnotation.name();
		String key;
		int limitPeriod = limitAnnotation.period();
		int limitCount = limitAnnotation.count();

		/**
		 * 根据限流类型获取不同的key ,如果不传我们会以方法名作为key
		 */
		switch (limitType) {
			case IP:
				key = getIpAddress();
				break;
			case CUSTOMER:
				key = limitAnnotation.key();
				break;
			default:
				key = StringUtils.upperCase(method.getName());
		}

		ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix(), key));
		try {
			String luaScript = buildLuaScript();
			RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
			Number count = limitRedisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
			if (count != null && count.intValue() <= limitCount) {
				return pjp.proceed();
			} else {
				throw new RuntimeException("You have been dragged into the blacklist");
			}
		} catch (Throwable e) {
			if (e instanceof RuntimeException) {
				throw new RuntimeException(e.getLocalizedMessage());
			}
			throw new RuntimeException("server exception");
		}
	}

	/**
	 * @author xiaofu
	 * @description 编写 redis Lua 限流脚本
	 * @date 2020/4/8 13:24
	 */
	public String buildLuaScript() {
		StringBuilder lua = new StringBuilder();
		lua.append("local c");
		lua.append("\nc = redis.call('get',KEYS[1])");
		// 调用不超过最大值，则直接返回
		lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
		lua.append("\nreturn c;");
		lua.append("\nend");
		// 执行计算器自加
		lua.append("\nc = redis.call('incr',KEYS[1])");
		lua.append("\nif tonumber(c) == 1 then");
		// 从第一次调用开始限流，设置对应键值的过期
		lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
		lua.append("\nend");
		lua.append("\nreturn c;");
		return lua.toString();
	}


	/**
	 * @author xiaofu
	 * @description 获取id地址
	 * @date 2020/4/8 13:24
	 */
	public String getIpAddress() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
