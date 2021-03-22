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

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.core.model.Result;
import lombok.NonNull;
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


	/**
	 * webflux成功返回数据
	 *
	 * @param exchange exchange
	 * @param data     数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @since 2020/10/15 15:50
	 */
	public Mono<Void> success(ServerWebExchange exchange, Object data) {
		Result<Object> result = Result.success(data);
		return writeResponse(exchange, result);
	}

	/**
	 * webflux失败返回数据
	 *
	 * @param exchange exchange
	 * @param data     数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @since 2020/10/15 15:50
	 */
	public Mono<Void> fail(ServerWebExchange exchange, Object data) {
		Result<Object> result = Result.fail(data);
		return writeResponse(exchange, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param exchange exchange
	 * @param result   数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @since 2020/10/15 15:50
	 */
	public Mono<Void> result(ServerWebExchange exchange, Result<?> result) {
		return writeResponse(exchange, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param exchange   exchange
	 * @param resultEnum 状态码
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @since 2020/10/15 15:51
	 */
	public Mono<Void> fail(ServerWebExchange exchange, ResultEnum resultEnum) {
		Result<String> result = Result.fail(resultEnum);
		return writeResponse(exchange, result);
	}

	/**
	 * 通过流返回数据
	 *
	 * @param exchange exchange
	 * @param result   数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @since 2020/10/15 15:52
	 */
	public Mono<Void> writeResponse(ServerWebExchange exchange, Result<?> result) {
		ServerHttpResponse response = exchange.getResponse();
		response.getHeaders().setAccessControlAllowCredentials(true);
		response.getHeaders().setAccessControlAllowOrigin("*");
		response.setStatusCode(HttpStatus.OK);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		DataBufferFactory dataBufferFactory = response.bufferFactory();
		DataBuffer buffer = dataBufferFactory
			.wrap(JsonUtil.toJSONString(result).getBytes(Charset.defaultCharset()));
		return response.writeWith(Mono.just(buffer))
			.doOnSuccess((error) -> DataBufferUtils.release(buffer));
	}
}
