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
package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.model.Result;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义返回util
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 11:22
 */
@UtilityClass
public class ResponseUtil {

	/**
	 * 成功返回数据
	 *
	 * @param response response
	 * @param data     数据对象
	 * @author dengtao
	 * @since 2020/10/15 15:47
	 */
	public void success(HttpServletResponse response, Object data) throws IOException {
		Result<?> result = Result.success(data);
		writeResponse(response, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param response response
	 * @param data     数据对象
	 * @author dengtao
	 * @since 2020/10/15 15:47
	 */
	public void fail(HttpServletResponse response, Object data) throws IOException {
		Result<?> result = Result.fail(data);
		writeResponse(response, result);
	}

	/**
	 * 成功返回数据
	 *
	 * @param response response
	 * @param result   数据对象
	 * @author dengtao
	 * @since 2020/10/15 15:47
	 */
	public void result(HttpServletResponse response, Result<?> result) throws IOException {
		writeResponse(response, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param response   response
	 * @param resultEnum 数据对象
	 * @author dengtao
	 * @since 2020/10/15 15:48
	 */
	public void fail(HttpServletResponse response, ResultEnum resultEnum) throws IOException {
		Result<String> result = Result.fail(resultEnum);
		writeResponse(response, result);
	}

	/**
	 * 通过流返回数据
	 *
	 * @param response response
	 * @param result   数据
	 * @author dengtao
	 * @since 2020/10/15 15:50
	 */
	private void writeResponse(HttpServletResponse response, Result<?> result) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.OK.value());
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		try (Writer writer = response.getWriter()) {
			writer.write(JsonUtil.toJSONString(result));
			writer.flush();
		}
	}


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
