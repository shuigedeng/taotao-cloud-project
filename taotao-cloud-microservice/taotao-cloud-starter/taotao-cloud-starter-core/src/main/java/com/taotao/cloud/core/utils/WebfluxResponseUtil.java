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
package com.taotao.cloud.core.utils;

import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.core.model.Result;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * WebfluxResponseUtil
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 11:22
 */
@UtilityClass
public class WebfluxResponseUtil {

	public static <T> Mono<Void> responseWriter(ServerWebExchange exchange, Integer code, T data,
		String type, String msg) {
		Result<T> result = Result.of(code, data, type, msg);
		return responseWrite(exchange, result);
	}

	public static Mono<Void> responseFailed(ServerWebExchange exchange, String msg) {
		Result<String> result = Result.failed(msg);
		return responseWrite(exchange, result);
	}

	public static <T> Mono<Void> responseSuccess(ServerWebExchange exchange, String msg) {
		Result<String> result = Result.failed(msg);
		return responseWrite(exchange, result);
	}

	public static <T> Mono<Void> responseWrite(ServerWebExchange exchange, Result<T> result) {
		ServerHttpResponse response = exchange.getResponse();
		response.getHeaders().setAccessControlAllowCredentials(true);
		response.getHeaders().setAccessControlAllowOrigin("*");
		response.setStatusCode(HttpStatus.OK);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		DataBufferFactory dataBufferFactory = response.bufferFactory();
		DataBuffer buffer = dataBufferFactory
			.wrap(JsonUtil.toJSONString(result).getBytes(Charset.defaultCharset()));
		return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
			DataBufferUtils.release(buffer);
		});
	}
}
