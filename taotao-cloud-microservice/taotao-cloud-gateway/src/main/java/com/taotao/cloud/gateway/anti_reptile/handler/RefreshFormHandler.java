package com.taotao.cloud.gateway.anti_reptile.handler;

import cn.hutool.http.HttpStatus;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.gateway.anti_reptile.ValidateFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class RefreshFormHandler implements HandlerFunction<ServerResponse> {

	@Autowired
	private ValidateFormService validateFormService;

	@Override
	public Mono<ServerResponse> handle(ServerRequest request) {
		try {
			String result = validateFormService.refresh(request);

			return ServerResponse
				.status(HttpStatus.HTTP_OK)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(Result.success(result));
		} catch (Exception e) {
			return ServerResponse
				.status(HttpStatus.HTTP_OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
		}
	}
}
