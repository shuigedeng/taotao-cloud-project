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
package com.taotao.cloud.sentinel.configuration;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.cloud.sentinel.feign.SentinelInvocationHandler;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import java.lang.annotation.Target;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.MethodMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 限流、熔断统一处理类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/13 17:32
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class SentinelAutoConfiguration {

	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "feign.sentinel.enabled")
	public Feign.Builder feignSentinelBuilder() {
		return MateFeignSentinel.builder();
	}


	@Bean
	@ConditionalOnClass(HttpServletRequest.class)
	public BlockExceptionHandler blockExceptionHandler() {
		return (request, response, e) -> {
			LogUtil.error("WebmvcHandler Sentinel调用失败: {0}", e);
			Result<String> result = Result.fail(e.getMessage());
			ResponseUtil.fail(response, result);
		};
	}

	@Bean
	@ConditionalOnClass(ServerResponse.class)
	public BlockRequestHandler blockRequestHandler() {
		return (exchange, e) -> {
			LogUtil.error("WebfluxHandler Sentinel调用失败: {0}", e);
			return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(Result.fail(e.getMessage())));
		};
	}

	/**
	 * 重写{@link SentinelFeign} 解决Feign与Sentinel冲突的问题
	 * 这个问题在Hoxton SR10版本出现
	 *
	 * @author pangu
	 */
	public static final class MateFeignSentinel {

		private MateFeignSentinel() {

		}

		public static MateFeignSentinel.Builder builder() {
			return new MateFeignSentinel.Builder();
		}

		public static final class Builder extends Feign.Builder implements ApplicationContextAware {

			private Contract contract = new Contract.Default();

			private ApplicationContext applicationContext;

			private FeignContext feignContext;

			@Override
			public Feign.Builder invocationHandlerFactory(
				InvocationHandlerFactory invocationHandlerFactory) {
				throw new UnsupportedOperationException();
			}

			@Override
			public MateFeignSentinel.Builder contract(Contract contract) {
				this.contract = contract;
				return this;
			}

			@Override
			public Feign build() {
				super.invocationHandlerFactory(new InvocationHandlerFactory() {
					@Override
					public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
						// 查找 FeignClient 上的 降级策略
						FeignClient feignClient = AnnotationUtils.findAnnotation(target.type(), FeignClient.class);
						Class fallback = feignClient.fallback();
						Class fallbackFactory = feignClient.fallbackFactory();

						String beanName = feignClient.contextId();
						if (!StringUtils.hasText(beanName)) {
							beanName = feignClient.name();
						}

						Object fallbackInstance;
						FallbackFactory fallbackFactoryInstance;
						// check fallback and fallbackFactory properties
						if (void.class != fallback) {
							fallbackInstance = getFromContext(beanName, "fallback", fallback, target.type());
							return new MateSentinelInvocationHandler(target, dispatch,
								new FallbackFactory.Default(fallbackInstance));
						}
						if (void.class != fallbackFactory) {
							fallbackFactoryInstance = (FallbackFactory) getFromContext(beanName, "fallbackFactory",
								fallbackFactory, FallbackFactory.class);
							return new MateSentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
						}
						return new MateSentinelInvocationHandler(target, dispatch);
					}

					private Object getFromContext(String name, String type, Class fallbackType, Class targetType) {
						Object fallbackInstance = feignContext.getInstance(name, fallbackType);
						if (fallbackInstance == null) {
							throw new IllegalStateException(String.format(
								"No %s instance of type %s found for feign client %s", type, fallbackType, name));
						}

						if (!targetType.isAssignableFrom(fallbackType)) {
							throw new IllegalStateException(String.format(
								"Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
								type, fallbackType, targetType, name));
						}
						return fallbackInstance;
					}
				});

				super.contract(new SentinelContractHolder(contract));
				return super.build();
			}

			@Override
			public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
				this.applicationContext = applicationContext;
				feignContext = this.applicationContext.getBean(FeignContext.class);
			}

		}

	}

	/**
	 * 重写{@link SentinelInvocationHandler} 支持自动降级
	 *
	 * @author pangu
	 */
	@Slf4j
	public class MateSentinelInvocationHandler implements InvocationHandler {

		public static final String EQUALS = "equals";

		public static final String HASH_CODE = "hashCode";

		public static final String TO_STRING = "toString";

		private final Target<?> target;

		private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;

		private FallbackFactory fallbackFactory;

		private Map<Method, Method> fallbackMethodMap;

		MateSentinelInvocationHandler(Target<?> target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch,
			FallbackFactory fallbackFactory) {
			this.target = checkNotNull(target, "target");
			this.dispatch = checkNotNull(dispatch, "dispatch");
			this.fallbackFactory = fallbackFactory;
			this.fallbackMethodMap = toFallbackMethod(dispatch);
		}

		MateSentinelInvocationHandler(Target<?> target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
			this.target = checkNotNull(target, "target");
			this.dispatch = checkNotNull(dispatch, "dispatch");
		}

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			if (EQUALS.equals(method.getName())) {
				try {
					Object otherHandler = args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
					return equals(otherHandler);
				} catch (IllegalArgumentException e) {
					return false;
				}
			} else if (HASH_CODE.equals(method.getName())) {
				return hashCode();
			} else if (TO_STRING.equals(method.getName())) {
				return toString();
			}

			Object result;
			InvocationHandlerFactory.MethodHandler methodHandler = this.dispatch.get(method);
			// only handle by HardCodedTarget
			if (target instanceof Target.HardCodedTarget) {
				Target.HardCodedTarget hardCodedTarget = (Target.HardCodedTarget) target;
				MethodMetadata methodMetadata = SentinelContractHolder.METADATA_MAP
					.get(hardCodedTarget.type().getName() + Feign.configKey(hardCodedTarget.type(), method));
				// resource default is HttpMethod:protocol://url
				if (methodMetadata == null) {
					result = methodHandler.invoke(args);
				} else {
					String resourceName = methodMetadata.template().method().toUpperCase() + ":" + hardCodedTarget.url()
						+ methodMetadata.template().path();
					Entry entry = null;
					try {
						ContextUtil.enter(resourceName);
						entry = SphU.entry(resourceName, EntryType.OUT, 1, args);
						result = methodHandler.invoke(args);
					} catch (Throwable ex) {
						// fallback handle
						if (!BlockException.isBlockException(ex)) {
							Tracer.trace(ex);
						}
						if (fallbackFactory != null) {
							try {
								Object fallbackResult = fallbackMethodMap.get(method).invoke(fallbackFactory.create(ex),
									args);
								return fallbackResult;
							} catch (IllegalAccessException e) {
								// shouldn't happen as method is public due to being an
								// interface
								throw new AssertionError(e);
							} catch (InvocationTargetException e) {
								throw new AssertionError(e.getCause());
							}
						} else {
							// 若是R类型 执行自动降级返回R
							if (Result.class == method.getReturnType()) {
								log.error("feign 服务间调用异常", ex);
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
			if (obj instanceof SentinelInvocationHandler) {
				MateSentinelInvocationHandler other = (MateSentinelInvocationHandler) obj;
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

		static Map<Method, Method> toFallbackMethod(Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
			Map<Method, Method> result = new LinkedHashMap<>();
			for (Method method : dispatch.keySet()) {
				method.setAccessible(true);
				result.put(method, method);
			}
			return result;
		}
	}

}
