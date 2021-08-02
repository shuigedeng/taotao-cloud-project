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
package com.taotao.cloud.gateway.configuration;

import cn.hutool.http.HttpStatus;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.CaptchaUtil;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.gateway.properties.ApiProperties;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.wf.captcha.ArithmeticCaptcha;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 特殊路由配置信息
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 22:11
 */
@Configuration
public class RouterFunctionConfiguration {

	private static final String FALLBACK = "/fallback";
	private static final String CODE = "/code";

	@Bean
	public RouterFunction<ServerResponse> routerFunction(
		HystrixFallbackHandler hystrixFallbackHandler,
		ImageCodeHandler imageCodeWebHandler,
		FaviconHandler faviconHandler,
		ApiProperties apiProperties) {
		return RouterFunctions
			.route(RequestPredicates.path(FALLBACK)
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler)
			.andRoute(RequestPredicates.GET(apiProperties.getBaseUri() + CODE)
				.and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), imageCodeWebHandler)
			.andRoute(RequestPredicates.GET("/favicon.ico")
				.and(RequestPredicates.accept(MediaType.IMAGE_PNG)), faviconHandler);
	}

	/**
	 * Hystrix 降级处理
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/4/29 22:11
	 */
	@Component
	public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {

		private static final int DEFAULT_PORT = 9700;

		@Override
		public Mono<ServerResponse> handle(ServerRequest serverRequest) {
			Optional<Object> originalUris = serverRequest
				.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
			Optional<InetSocketAddress> socketAddress = serverRequest.remoteAddress();

			originalUris.ifPresent(originalUri -> LogUtil
				.error("网关执行请求:{0}失败,请求主机: {1},请求数据:{2} hystrix服务降级处理", null,
					originalUri,
					socketAddress.orElse(new InetSocketAddress(DEFAULT_PORT)).getHostString(),
					buildMessage(serverRequest)));

			return ServerResponse
				.status(HttpStatus.HTTP_OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
		}

		private String buildMessage(ServerRequest request) {
			StringBuilder message = new StringBuilder("[");
			message.append(request.methodName());
			message.append(" ");
			message.append(request.uri());
			MultiValueMap<String, String> params = request.queryParams();
			Map<String, String> map = params.toSingleValueMap();
			if (map.size() > 0) {
				message.append(" 请求参数: ");
				String serialize = JsonUtil.toJSONString(message);
				message.append(serialize);
			}
			Object requestBody = request.exchange()
				.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
			if (Objects.nonNull(requestBody)) {
				message.append(" 请求body: ");
				message.append(requestBody.toString());
			}
			message.append("]");
			return message.toString();
		}
	}


	/**
	 * 图形验证码处理器
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/4/29 22:11
	 */
	@Component
	public class ImageCodeHandler implements HandlerFunction<ServerResponse> {

		private static final String PARAM_T = "t";
		private final RedisRepository redisRepository;

		public ImageCodeHandler(RedisRepository redisRepository) {
			this.redisRepository = redisRepository;
		}

		@Override
		public Mono<ServerResponse> handle(ServerRequest request) {
			try {
				ArithmeticCaptcha captcha = CaptchaUtil.getArithmeticCaptcha();
				String text = captcha.text();
				LogUtil.info(text);
				MultiValueMap<String, String> params = request.queryParams();
				String t = params.getFirst(PARAM_T);
				redisRepository
					.setExpire(RedisConstant.TAOTAO_CLOUD_CAPTCHA_KEY + t, text.toLowerCase(), 120);

				return ServerResponse
					.status(HttpStatus.HTTP_OK)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(Result.success(captcha.toBase64()));
			} catch (Exception e) {
				return ServerResponse
					.status(HttpStatus.HTTP_OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
			}
		}
	}

	/**
	 * 图形验证码处理器
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/4/29 22:11
	 */
	@Component
	public class FaviconHandler implements HandlerFunction<ServerResponse> {

		@Override
		public Mono<ServerResponse> handle(ServerRequest request) {
			try {
				ClassPathResource classPathResource = new ClassPathResource("favicon.ico");
				InputStream inputStream = classPathResource.getInputStream();

				byte[] bytes = IOUtils.toByteArray(inputStream);

				return ServerResponse
					.status(HttpStatus.HTTP_OK)
					.contentType(MediaType.IMAGE_PNG)
					.bodyValue(bytes);
			} catch (Exception e) {
				return ServerResponse
					.status(HttpStatus.HTTP_OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
			}
		}
	}
}
