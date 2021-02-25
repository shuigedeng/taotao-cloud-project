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
package com.taotao.cloud.gateway.swagger;

import cn.hutool.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.Optional;

/**
 * <br>
 *
 * @author dengtao
 * @since 2020/4/29 22:10
 * @version 1.0.0
 */
@Component
public class SwaggerUiHandler implements HandlerFunction<ServerResponse> {

	@Autowired(required = false)
	private UiConfiguration uiConfiguration;

	/**
	 * Handle the given request.
	 *
	 * @param request the request to handlerø
	 * @return the response
	 */
	@Override
	public Mono<ServerResponse> handle(ServerRequest request) {
		return ServerResponse.status(HttpStatus.HTTP_OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(
				Optional.ofNullable(uiConfiguration)
					.orElse(UiConfigurationBuilder.builder().build())));
	}
}
