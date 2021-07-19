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
package com.taotao.cloud.gateway.filter.global;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.gateway.loadBalancer.CustomGrayLoadBalancer;
import java.net.URI;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 蓝绿发布<br>
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 22:13
 */
@Component
public class GrayFilter implements GlobalFilter, Ordered {

	private static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;
	private final LoadBalancerClientFactory clientFactory;
	private final LoadBalancerProperties properties;

	public GrayFilter(LoadBalancerClientFactory clientFactory,
		LoadBalancerProperties properties) {
		this.clientFactory = clientFactory;
		this.properties = properties;
	}

	@Override
	public int getOrder() {
		return LOAD_BALANCER_CLIENT_FILTER_ORDER;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
		String schemePrefix = exchange
			.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);

		if (url != null && ("grayLb".equals(url.getScheme()) || "grayLb".equals(schemePrefix))) {
			ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);
			LogUtil.info(
				ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);

			return this.choose(exchange).doOnNext((response) -> {
				if (!response.hasServer()) {
					throw NotFoundException
						.create(false, "Unable to find instance for " + url.getHost());
				} else {
					URI uri = exchange.getRequest().getURI();
					String overrideScheme = null;
					if (schemePrefix != null) {
						overrideScheme = url.getScheme();
					}

					DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(
						response.getServer(), overrideScheme);
					URI requestUrl = this.reconstructURI(serviceInstance, uri);
					LogUtil.info("LoadBalancerClientFilter url chosen: " + requestUrl);

					exchange.getAttributes()
						.put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
				}
			}).then(chain.filter(exchange));
		} else {
			return chain.filter(exchange);
		}
	}

	protected URI reconstructURI(ServiceInstance serviceInstance, URI original) {
		return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
	}

	private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
		URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
		assert uri != null;
		CustomGrayLoadBalancer loadBalancer = new CustomGrayLoadBalancer(
			clientFactory.getLazyProvider(uri.getHost(), ServiceInstanceListSupplier.class),
			uri.getHost());
		return loadBalancer.choose(this.createRequest(exchange));
	}

	private Request createRequest(ServerWebExchange exchange) {
		HttpHeaders headers = exchange.getRequest().getHeaders();
		return new DefaultRequest<>(headers);
	}
}
