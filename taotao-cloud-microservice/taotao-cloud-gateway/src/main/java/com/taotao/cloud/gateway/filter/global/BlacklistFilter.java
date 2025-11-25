/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.gateway.filter.global;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.TraceUtils;
import com.taotao.cloud.gateway.properties.FilterProperties;
import com.taotao.cloud.gateway.service.ISafeRuleService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 第一执行 黑名单过滤器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-08 13:19:32
 */
@Component
@ConditionalOnProperty(
	prefix = FilterProperties.PREFIX,
	name = "blacklist",
	havingValue = "true",
	matchIfMissing = true)
public class BlacklistFilter implements GlobalFilter, Ordered {

	private final ISafeRuleService safeRuleService;

	public BlacklistFilter( ISafeRuleService safeRuleService ) {
		this.safeRuleService = safeRuleService;
	}

	@Override
	public Mono<Void> filter( ServerWebExchange exchange, GatewayFilterChain chain ) {

		// SkywalkingUtil.putTidIntoMdc(exchange);

		// 是否开启黑名单
		// 从redis里查询黑名单是否存在
		LogUtils.info("进入黑名单模式");

		// 检查黑名单
		Mono<Void> result = safeRuleService.filterBlackList(exchange);
		if (result != null) {
			return result;
		}

		// 增加CORS
		// 解决前端登录跨域的问题
		ServerHttpRequest request = exchange.getRequest();
		if (CorsUtils.isCorsRequest(request)) {
			ServerHttpResponse response = exchange.getResponse();
			HttpHeaders headers = response.getHeaders();
			headers.add("Access-Control-Allow-Origin", "*");
			headers.add("Access-Control-Allow-Methods", "*");
			headers.add("Access-Control-Max-Age", "3600");
			headers.add("Access-Control-Allow-Headers", "*");
			if (request.getMethod() == HttpMethod.OPTIONS) {
				response.setStatusCode(HttpStatus.OK);
				return Mono.empty();
			}
		}

		return chain.filter(exchange)
			.then(
				Mono.fromRunnable(
					() -> {
						// SkywalkingUtil.putTidIntoMdc(exchange);

						LogUtils.info("Response BlacklistFilter 最终最终返回数据");

						TraceUtils.removeTraceId();
						LocaleContextHolder.resetLocaleContext();
					}));
	}


	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}


	public void aa( String aa, String bb ) {

	}
}
