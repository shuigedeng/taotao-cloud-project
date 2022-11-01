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
package com.taotao.cloud.gateway.springcloud.filter.global;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.common.IdGeneratorUtils;
import com.taotao.cloud.common.utils.servlet.TraceUtils;
import com.taotao.cloud.gateway.springcloud.properties.FilterProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 生成日志链路追踪id
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:13
 */
@Component
@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "trace", havingValue = "true", matchIfMissing = true)
public class TraceLogFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String traceId = IdGeneratorUtils.getIdStr();
		TraceUtils.setMdcTraceId(traceId);
		ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
			.headers(h -> h.add(CommonConstant.TAOTAO_CLOUD_TRACE_HEADER, traceId))
			.build();

		ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
		return chain.filter(build);
	}

	@Override
	public int getOrder() {
		return 1;
	}
}

