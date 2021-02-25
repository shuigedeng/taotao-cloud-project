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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * 自定义返回util
 *
 * @author dengtao
 * @date 2020/5/2 11:22
 * @since v1.0
 */
@UtilityClass
public class ResponseUtil {

	/**
	 * 通过流返回数据
	 *
	 * @param response   response
	 * @param msg        返回信息
	 * @param httpStatus 返回状态码
	 * @return void
	 * @author dengtao
	 * @date 2020/10/15 15:47
	 * @since v1.0
	 */
	public void writeResponse(HttpServletResponse response,
							  String msg,
							  int httpStatus) throws IOException {
		Result<String> result = Result.failed(null, httpStatus, msg);
		writeResponse(response, result);
	}

	/**
	 * 成功返回数据
	 *
	 * @param response response
	 * @param obj      数据对象
	 * @return void
	 * @author dengtao
	 * @date 2020/10/15 15:47
	 * @since v1.0
	 */
	public void success(HttpServletResponse response, @NonNull Object obj) throws IOException {
		Result<?> result = Result.succeed(obj);
		writeResponse(response, result);
	}

	/**
	 * 成功返回数据
	 *
	 * @param response response
	 * @param result   数据对象
	 * @return void
	 * @author dengtao
	 * @date 2020/10/15 15:47
	 * @since v1.0
	 */
	public void success(HttpServletResponse response, Result<?> result) throws IOException {
		writeResponse(response, result);
	}

	/**
	 * 成功返回数据
	 *
	 * @param response   response
	 * @param resultEnum 数据对象
	 * @return void
	 * @author dengtao
	 * @date 2020/10/15 15:48
	 * @since v1.0
	 */
	public void success(HttpServletResponse response, ResultEnum resultEnum) throws IOException {
		Result<String> result = Result.succeed(resultEnum);
		writeResponse(response, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param response response
	 * @param msg      数据
	 * @return void
	 * @author dengtao
	 * @date 2020/10/15 15:49
	 * @since v1.0
	 */
	public void failed(HttpServletResponse response, String msg) throws IOException {
		Result<String> result = Result.failed(msg);
		writeResponse(response, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param exchange   exchange
	 * @param httpStatus 状态码
	 * @param msg        数据
	 * @author dengtao
	 * @date 2020/10/15 15:51
	 * @since v1.0
	 */
	public void failed(HttpServletResponse exchange, int httpStatus, String msg) throws IOException {
		Result<String> result = Result.failed(httpStatus, msg);
		writeResponse(exchange, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param response response
	 * @param result   数据
	 * @return void
	 * @author dengtao
	 * @date 2020/10/15 15:49
	 * @since v1.0
	 */
	public void failed(HttpServletResponse response, Result<?> result) throws IOException {
		writeResponse(response, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param response   response
	 * @param resultEnum 数据
	 * @return void
	 * @author dengtao
	 * @date 2020/10/15 15:49
	 * @since v1.0
	 */
	public static void failed(HttpServletResponse response, ResultEnum resultEnum) throws IOException {
		Result<String> result = Result.failed(resultEnum);
		writeResponse(response, result);
	}

	/**
	 * 通过流返回数据
	 *
	 * @param response response
	 * @param result   数据
	 * @return void
	 * @author dengtao
	 * @date 2020/10/15 15:50
	 * @since v1.0
	 */
	private void writeResponse(HttpServletResponse response, Result<?> result) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.OK.value());
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		try (Writer writer = response.getWriter()) {
			writer.write(JsonUtil.toJSONString(result));
			writer.flush();
		}
	}

	/**
	 * webflux返回数据
	 *
	 * @param exchange   exchange
	 * @param httpStatus 状态
	 * @param msg        数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @date 2020/10/15 15:50
	 * @since v1.0
	 */
	public Mono<Void> writeResponse(ServerWebExchange exchange, int httpStatus, String msg) {
		Result<String> result = Result.failed(msg, httpStatus);
		return writeResponse(exchange, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param exchange exchange
	 * @param msg      数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @date 2020/10/15 15:50
	 * @since v1.0
	 */
	public Mono<Void> failed(ServerWebExchange exchange, String msg) {
		Result<String> result = Result.failed(msg);
		return writeResponse(exchange, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param exchange exchange
	 * @param result   数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @date 2020/10/15 15:50
	 * @since v1.0
	 */
	public Mono<Void> failed(ServerWebExchange exchange, Result<?> result) {
		return writeResponse(exchange, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param exchange   exchange
	 * @param httpStatus 状态码
	 * @param msg        数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @date 2020/10/15 15:51
	 * @since v1.0
	 */
	public Mono<Void> failed(ServerWebExchange exchange, int httpStatus, String msg) {
		Result<String> result = Result.failed(httpStatus, msg);
		return writeResponse(exchange, result);
	}

	/**
	 * 失败返回数据
	 *
	 * @param exchange   exchange
	 * @param resultEnum 状态码
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @date 2020/10/15 15:51
	 * @since v1.0
	 */
	public Mono<Void> failed(ServerWebExchange exchange, ResultEnum resultEnum) {
		Result<String> result = Result.failed(resultEnum);
		return writeResponse(exchange, result);
	}

	/**
	 * 通过流返回数据
	 *
	 * @param exchange exchange
	 * @param result   数据
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author dengtao
	 * @date 2020/10/15 15:52
	 * @since v1.0
	 */
	public Mono<Void> writeResponse(ServerWebExchange exchange, Result<?> result) {
		ServerHttpResponse response = exchange.getResponse();
		response.getHeaders().setAccessControlAllowCredentials(true);
		response.getHeaders().setAccessControlAllowOrigin("*");
		response.setStatusCode(HttpStatus.OK);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		DataBufferFactory dataBufferFactory = response.bufferFactory();
		DataBuffer buffer = dataBufferFactory.wrap(JsonUtil.toJSONString(result).getBytes(Charset.defaultCharset()));
		return response.writeWith(Mono.just(buffer)).doOnSuccess((error) -> DataBufferUtils.release(buffer));
	}
}
