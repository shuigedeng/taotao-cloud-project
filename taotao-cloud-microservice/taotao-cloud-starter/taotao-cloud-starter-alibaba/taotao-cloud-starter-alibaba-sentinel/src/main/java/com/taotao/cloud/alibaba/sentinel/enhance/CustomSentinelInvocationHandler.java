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
package com.taotao.cloud.alibaba.sentinel.enhance;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.taotao.cloud.common.model.Result;
import feign.Feign;
import feign.InvocationHandlerFactory.MethodHandler;
import feign.MethodMetadata;
import feign.Target;
import feign.Util;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>Description: 复制原有代码，扩展支持统一 fallback 工厂 </p>
 */
public class CustomSentinelInvocationHandler implements InvocationHandler {

	private static final Logger log = LoggerFactory.getLogger(
		CustomSentinelInvocationHandler.class);

	private final Target<?> target;

	private final Map<Method, MethodHandler> dispatch;

	private FallbackFactory fallbackFactory;

	private Map<Method, Method> fallbackMethodMap;

	CustomSentinelInvocationHandler(Target<?> target, Map<Method, MethodHandler> dispatch,
		FallbackFactory fallbackFactory) {
		this.target = Util.checkNotNull(target, "target");
		this.dispatch = Util.checkNotNull(dispatch, "dispatch");
		this.fallbackFactory = fallbackFactory;
		this.fallbackMethodMap = toFallbackMethod(dispatch);
	}

	CustomSentinelInvocationHandler(Target<?> target, Map<Method, MethodHandler> dispatch) {
		this.target = Util.checkNotNull(target, "target");
		this.dispatch = Util.checkNotNull(dispatch, "dispatch");
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args)
		throws Throwable {
		if ("equals".equals(method.getName())) {
			try {
				Object otherHandler = args.length > 0 && args[0] != null
					? Proxy.getInvocationHandler(args[0])
					: null;
				return equals(otherHandler);
			} catch (IllegalArgumentException e) {
				return false;
			}
		} else if ("hashCode".equals(method.getName())) {
			return hashCode();
		} else if ("toString".equals(method.getName())) {
			return toString();
		}

		Object result;
		MethodHandler methodHandler = this.dispatch.get(method);
		// only handle by HardCodedTarget
		if (target instanceof Target.HardCodedTarget) {
			Target.HardCodedTarget hardCodedTarget = (Target.HardCodedTarget) target;
			MethodMetadata methodMetadata = SentinelContractHolder.METADATA_MAP
				.get(hardCodedTarget.type().getName()
					+ Feign.configKey(hardCodedTarget.type(), method));
			// resource default is HttpMethod:protocol://url
			if (methodMetadata == null) {
				result = methodHandler.invoke(args);
			} else {
				String resourceName = methodMetadata.template().method().toUpperCase()
					+ ":" + hardCodedTarget.url() + methodMetadata.template().path();
				Entry entry = null;
				try {
					ContextUtil.enter(resourceName);
					entry = SphU.entry(resourceName, EntryType.OUT, 1, args);
					result = methodHandler.invoke(args);
				} catch (Throwable ex) {
					// fallback handle
					if (!BlockException.isBlockException(ex)) {
						Tracer.traceEntry(ex, entry);
					}
					if (fallbackFactory != null) {
						try {
							return fallbackMethodMap.get(method)
								.invoke(fallbackFactory.create(ex), args);
						} catch (IllegalAccessException e) {
							// shouldn't happen as method is public due to being an
							// interface
							throw new AssertionError(e);
						} catch (InvocationTargetException e) {
							throw new AssertionError(e.getCause());
						}
					} else {
						// throw exception if fallbackFactory is null
						// 若是R类型 执行自动降级返回R
						if (Result.class == method.getReturnType()) {
							log.error(" Feign service call exception", ex);
							return Result.fail(ex.getLocalizedMessage());
						} else {
							throw ex;
						}
					}
				} finally {
					if (entry != null) {
						entry.exit(1, args);
					}
					ContextUtil.exit();
				}
			}
		} else {
			// other target type using default strategy
			result = methodHandler.invoke(args);
		}

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CustomSentinelInvocationHandler) {
			CustomSentinelInvocationHandler other = (CustomSentinelInvocationHandler) obj;
			return target.equals(other.target);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return target.hashCode();
	}

	@Override
	public String toString() {
		return target.toString();
	}

	static Map<Method, Method> toFallbackMethod(Map<Method, MethodHandler> dispatch) {
		Map<Method, Method> result = new LinkedHashMap<>();
		for (Method method : dispatch.keySet()) {
			method.setAccessible(true);
			result.put(method, method);
		}
		return result;
	}
}
