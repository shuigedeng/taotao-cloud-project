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
package com.taotao.cloud.limit.ext;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableList;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.limit.annotation.Limit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.lang.reflect.Method;

/**
 * LimitAspect
 *
 * <pre class="code">
 * &#064;RestController
 * public class LimiterController {
 *   private static final AtomicInteger ATOMIC_INTEGER_1 = new AtomicInteger();
 *   private static final AtomicInteger ATOMIC_INTEGER_2 = new AtomicInteger();
 *   private static final AtomicInteger ATOMIC_INTEGER_3 = new AtomicInteger();
 *
 * &#064;Limit(key = "limitTest", period = 10, count = 3)
 * GetMapping("/limitTest1")
 * public int testLimiter1() {
 * 	return ATOMIC_INTEGER_1.incrementAndGet();
 *    }
 *
 * GetMapping("/limitTest2")
 * public int testLimiter2() {
 * 	return ATOMIC_INTEGER_2.incrementAndGet();
 *    }
 *
 * GetMapping("/limitTest3")
 * public int testLimiter3() {
 * 	return ATOMIC_INTEGER_3.incrementAndGet();
 *    }
 * </pre>
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:05:48
 */
@Aspect
public class LimitAspect {

	/**
	 * UNKNOWN
	 */
	private static final String UNKNOWN = "unknown";

	/**
	 * redisRepository
	 */
	private final RedisRepository redisRepository;

	public LimitAspect(RedisRepository redisRepository) {
		this.redisRepository = redisRepository;
	}

	@Around(value = "@annotation(limit)")
	public Object around(ProceedingJoinPoint pjp, Limit limit) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();

		LimitType limitType = limit.limitType();
		String name = limit.name();
		int limitPeriod = limit.period();
		int limitCount = limit.count();

		//根据限流类型获取不同的key ,如果不传我们会以方法名作为key
		String key = switch (limitType) {
			case IP -> RequestUtils.getHttpServletRequestIpAddress();
			case CUSTOMER -> StrUtil.isBlank(limit.key())
				? org.apache.commons.lang.StringUtils.upperCase(method.getName())
				: limit.key();
		};

		ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limit.prefix(), key));

		try {
			String luaScript = buildLuaScript();
			RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
			Number count = redisRepository.getRedisTemplate()
				.execute(redisScript, keys, limitCount, limitPeriod);
			if (count != null && count.intValue() <= limitCount) {
				return pjp.proceed();
			} else {
				throw new LimitException(ResultEnum.BLACKLIST);
			}
		} catch (Throwable e) {
			LogUtils.error(e);
			throw new LimitException(ResultEnum.ERROR);
		}
	}

	/**
	 * redis Lua 限流脚本
	 *
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 22:06:58
	 */
	public String buildLuaScript() {
		return """
			local c
			c = redis.call('get',KEYS[1])
						
			-- 调用不超过最大值，则直接返回
			if c and tonumber(c) > tonumber(ARGV[1]) then
				return c;
			end
						
			-- 执行计算器自加
			c = redis.call('incr',KEYS[1])
			if tonumber(c) == 1 then
			-- 从第一次调用开始限流，设置对应键值的过期
				redis.call('expire',KEYS[1],ARGV[2])
			end
						
			return c;
			""";
	}
}
