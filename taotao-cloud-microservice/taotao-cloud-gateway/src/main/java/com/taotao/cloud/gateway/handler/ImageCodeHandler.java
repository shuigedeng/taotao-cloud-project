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
package com.taotao.cloud.gateway.handler;

import cn.hutool.http.HttpStatus;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.CaptchaUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 图形验证码处理器
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 22:11
 */
@Component
@AllArgsConstructor
public class ImageCodeHandler implements HandlerFunction<ServerResponse> {

	private static final String PARAM_T = "t";
	private final RedisRepository redisRepository;

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
