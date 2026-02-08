/*
 * Copyright 2013-present the original author or authors.
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

package com.taotao.cloud.gateway.filter;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.taotao.boot.common.utils.log.LogUtils;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.servlet.ServletException;
import org.jspecify.annotations.Nullable;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.gateway.server.mvc.common.Configurable;
import org.springframework.cloud.gateway.server.mvc.common.HttpStatusHolder;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
/**
 * @author 决定“我管了之后要做什么手脚”（改请求、加头、鉴权、限流、改响应……）
 * @date 2025/12/15
 * @description 定义包含所有自定义filter的函数式过滤器类,供filtersupplier类使用暴露到项目中
 * 一个类只能定义一个静态方法，spring cloud gateway只注册一个静态方法
 *
 * 提示：定义的过滤器类按一个类一个静态方法，多增加几个静态方法spring也只会加载第一个静态方法，静态方法一定要加@Shotcut注解，不然启动报错，报错原因分析及解决，将在另一篇关于gateway疑难杂症解决的文章中展开。
 * 过滤器定义（SampleHandlerFilterFunctions.java）:一个类一个静态过滤器方法（方法上一定要加上@Shortcut注解），多加了也没用spring只会加载第一个静态方法
 */
public abstract class RequestTimeFilterFunctions {

	private RequestTimeFilterFunctions() {
	}

	public static class RequestTimeFilterSupplier extends SimpleFilterSupplier {
		public RequestTimeFilterSupplier() {
			super(RequestTimeFilterFunctions.class);
		}
	}

	@Shortcut(ENABLED)
	public static HandlerFilterFunction<ServerResponse, ServerResponse> requestTime(boolean enabled) {
		return requestTime(config -> config.setEnabled(enabled));
	}

	public static HandlerFilterFunction<ServerResponse, ServerResponse> requestTime(
		Consumer<RequestTimeFilterFunctions.Config> configConsumer) {
		RequestTimeFilterFunctions.Config config = new RequestTimeFilterFunctions.Config();
		configConsumer.accept(config);
		return requestTime(config);
	}

	private static final String START_TIME = "StartTime";
	private static final String ENABLED = "enabled";

	@Shortcut
	@Configurable
	public static HandlerFilterFunction<ServerResponse, ServerResponse> requestTime(Config config) {

		return (request, next) -> {
			if (!config.isEnabled()) {
				return next.handle(request);
			}

//			log.error("==========进入自定义filter处理逻辑，请求头：{},响应头{}===========", reqHeaderName, repHeaderName);
//			ServerRequest modified = ServerRequest.from(request)
//				.header(reqHeaderName, "request header for filter").build();
//			ServerResponse response = next.handle(modified);
//			response.headers().add(repHeaderName, "response header for filter");
//			return response;

			MvcUtils.putAttribute(request, START_TIME, System.currentTimeMillis());

			ServerResponse serverResponse = next.handle(request);

			Long startTime  = MvcUtils.getAttribute(request, START_TIME);

			StringBuilder sb =
				new StringBuilder(request.servletRequest().getRequestURI())
					.append(" 请求时间: ")
					.append(System.currentTimeMillis() - startTime)
					.append("ms");
			sb.append(" 请求参数: ").append(request.servletRequest().getQueryString());
			LogUtils.info(sb.toString());

			return serverResponse;

		};
	}

	public static class Config {
		/**
		 * 控制是否开启统计
		 */
		private boolean enabled;

		public Config() {}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

}
