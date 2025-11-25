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

package com.taotao.cloud.gateway.filter.gateway;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.filter.gateway.RequestTimeGatewayFilterFactory.Config;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * RequestTimeGatewayFilterFactory
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/08/09 21:25
 */
@Component
public class RequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<Config> {

	private static final String START_TIME = "StartTime";
	private static final String ENABLED = "enabled";

	@Override
	public List<String> shortcutFieldOrder() {
		return Collections.singletonList(ENABLED);
	}


	public RequestTimeGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply( Config config ) {
		return ( exchange, chain ) -> {
			if (!config.isEnabled()) {
				return chain.filter(exchange);
			}
			exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
			return chain.filter(exchange)
				.then(
					Mono.fromRunnable(
						() -> {
							Long startTime = exchange.getAttribute(START_TIME);
							if (Objects.nonNull(startTime)) {
								ServerHttpRequest request = exchange.getRequest();
								StringBuilder sb =
									new StringBuilder(request.getURI().getRawPath())
										.append(" 请求时间: ")
										.append(
											System.currentTimeMillis()
												- startTime)
										.append("ms");
								sb.append(" 请求参数: ").append(request.getQueryParams());
								LogUtils.info(sb.toString());
							}
						}));
		};
	}

	public static class Config {

		/**
		 * 控制是否开启统计
		 */
		private boolean enabled;

		public Config() {
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled( boolean enabled ) {
			this.enabled = enabled;
		}
	}
}
