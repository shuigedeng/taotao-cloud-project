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
package com.taotao.cloud.web.idempotent;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.common.support.lock.DistributedLock;
import com.taotao.cloud.common.support.lock.ZLock;
import com.taotao.cloud.common.utils.aop.AopUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 注解执行器 处理重复请求 和串行指定条件的请求
 * <p>
 * 两种模式的拦截
 * <p>
 * 1.rid 是针对每一次请求的
 * </p>
 * 2.key+val 是针对相同参数请求
 * </p>
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:00:21
 */
@Aspect
public class IdempotentAspect {

	/**
	 * PER_FIX_KEY
	 */
	private final ThreadLocal<String> PER_FIX_KEY = new ThreadLocal<>();

	private final ThreadLocal<ZLock> ZLOCK_CONTEXT = new ThreadLocal<>();

	/**
	 * 配置注解后 默认开启
	 */
	private final boolean enable = true;

	/**
	 * request请求头中的key
	 */
	private final static String HEADER_RID_KEY = "RID";

	/**
	 * redis中锁的key前缀
	 */
	private static final String REDIS_KEY_PREFIX = "RID:";

	/**
	 * 锁等待时长
	 */
	private static final int LOCK_WAIT_TIME = 10;

	private final DistributedLock distributedLock;

	public IdempotentAspect(DistributedLock distributedLock) {
		this.distributedLock = distributedLock;
	}

	@Pointcut("@annotation(com.taotao.cloud.web.idempotent.Idempotent)")
	public void watchIde() {

	}

	@Before("watchIde()")
	public void doBefore(JoinPoint joinPoint) throws Exception {
		Idempotent idempotent = AopUtil.getAnnotation(joinPoint, Idempotent.class);

		if (enable && null != idempotent) {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (null == attributes) {
				throw new IdempotentException("请求数据为空");
			}
			HttpServletRequest request = attributes.getRequest();

			//1.判断模式
			if (idempotent.ideTypeEnum() == IdempotentTypeEnum.ALL || idempotent.ideTypeEnum() == IdempotentTypeEnum.RID) {
				//2.1.通过rid模式判断是否属于重复提交
				String rid = request.getHeader(HEADER_RID_KEY);

				try {
					if (StringUtils.isNotBlank(rid)) {
						ZLock result = distributedLock.tryLock(
							REDIS_KEY_PREFIX + rid,
							LOCK_WAIT_TIME,
							TimeUnit.MILLISECONDS);
						if (Objects.isNull(result)) {
							throw new IdempotentException("命中RID重复请求");
						}
						LogUtil.debug("msg1=当前请求已成功记录,且标记为0未处理,,{}={}", HEADER_RID_KEY, rid);
						ZLOCK_CONTEXT.set(result);
					} else {
						LogUtil.warn("msg1=header没有rid,防重复提交功能失效,,remoteHost={}" + request.getRemoteHost());
					}
				} catch (Exception e) {
					LogUtil.error("获取redis锁发生异常", e);
					throw e;
				}
			}

			if (idempotent.ideTypeEnum() == IdempotentTypeEnum.ALL || idempotent.ideTypeEnum() == IdempotentTypeEnum.KEY) {
				//2.2.通过自定义key模式判断是否属于重复提交
				String key = idempotent.key();
				if (StringUtils.isNotBlank(key)) {
					String val = "";
					Object[] paramValues = joinPoint.getArgs();
					String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
					//获取自定义key的value
					for (int i = 0; i < paramNames.length; i++) {
						String params = JSON.toJSONString(paramValues[i]);
						if (params.startsWith("{")) {
							//如果是对象
							//通过key获取value
							JSONObject jsonObject = JSON.parseObject(params);
							val = jsonObject.getString(key);
						} else if (key.equals(paramNames[i])) {
							//如果是单个k=v
							val = params;
						} else {
							//如果自定义的key,在请求参数中没有此参数,说明非法请求
							LogUtil.warn("自定义的key,在请求参数中没有此参数,防重复提交功能失效");
						}
					}

					//判断重复提交的条件
					String perFix = idempotent.perFix();
					if (StringUtils.isNotBlank(val)) {
						perFix = perFix + ":" + val;

						try {
							ZLock result = distributedLock.tryLock(perFix, LOCK_WAIT_TIME,
								TimeUnit.MILLISECONDS);
							if (!Objects.nonNull(result)) {
								String targetName = joinPoint.getTarget().getClass().getName();
								String methodName = joinPoint.getSignature().getName();
								LogUtil.error("不允许重复执行,,key={},,targetName={},,methodName={}", perFix, targetName, methodName);
								throw new IdempotentException("不允许重复提交");
							}
							//存储在当前线程
							PER_FIX_KEY.set(perFix);
							ZLOCK_CONTEXT.set(result);
							LogUtil.info("msg1=当前请求已成功锁定:{}", perFix);
						} catch (Exception e) {
							LogUtil.error("获取redis锁发生异常", e);
							throw e;
						}
					} else {
						LogUtil.warn("自定义的key,在请求参数中value为空,防重复提交功能失效");
					}
				}
			}
		}
	}

	@After("watchIde()")
	public void doAfter(JoinPoint joinPoint) throws Throwable {
		try {
			Idempotent idempotent = AopUtil.getAnnotation(joinPoint, Idempotent.class);
			if (enable && null != idempotent) {
				if (idempotent.ideTypeEnum() == IdempotentTypeEnum.ALL || idempotent.ideTypeEnum() == IdempotentTypeEnum.RID) {
					ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					HttpServletRequest request = attributes.getRequest();
					String rid = request.getHeader(HEADER_RID_KEY);
					if (StringUtils.isNotBlank(rid)) {
						try {
							distributedLock.unlock(ZLOCK_CONTEXT.get());
							// redisService.unLock(REDIS_KEY_PREFIX + rid);
							LogUtil.info("msg1=当前请求已成功处理,,rid={}", rid);

							PER_FIX_KEY.remove();
							ZLOCK_CONTEXT.remove();
						} catch (Exception e) {
							LogUtil.error("释放redis锁异常", e);
						}
					}
				}

				if (idempotent.ideTypeEnum() == IdempotentTypeEnum.ALL
					|| idempotent.ideTypeEnum() == IdempotentTypeEnum.KEY) {
					// 自定义key
					String key = idempotent.key();
					if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(PER_FIX_KEY.get())) {
						try {
							distributedLock.unlock(ZLOCK_CONTEXT.get());
							//redisService.unLock(PER_FIX_KEY.get());
							LogUtil.info("msg1=当前请求已成功释放,,key={}", PER_FIX_KEY.get());

							PER_FIX_KEY.remove();
							ZLOCK_CONTEXT.remove();
						} catch (Exception e) {
							LogUtil.error("释放redis锁异常", e);
						}
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
	}
}
