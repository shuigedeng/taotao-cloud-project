package com.taotao.cloud.core.utils;

import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.core.model.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;


public class WebfluxResponseUtil {
	/**
	 * webflux的response返回json对象
	 */
	public static <T> Mono<Void> responseWriter(ServerWebExchange exchange, Integer code, T data, String type, String msg) {
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
		DataBuffer buffer = dataBufferFactory.wrap(JsonUtil.toJSONString(result).getBytes(Charset.defaultCharset()));
		return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
			DataBufferUtils.release(buffer);
		});
	}
}
