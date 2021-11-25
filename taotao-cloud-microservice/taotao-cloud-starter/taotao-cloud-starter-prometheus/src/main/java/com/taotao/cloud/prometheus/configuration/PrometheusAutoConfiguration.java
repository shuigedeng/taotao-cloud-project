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

package com.taotao.cloud.prometheus.configuration;

import com.taotao.cloud.prometheus.api.PrometheusApi;
import com.taotao.cloud.prometheus.api.ReactivePrometheusApi;
import com.taotao.cloud.prometheus.properties.PrometheusProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * prometheus http sd
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@Configuration
@ConditionalOnProperty(prefix = PrometheusProperties.PREFIX, name = "enabled", havingValue = "true")
public class PrometheusAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBean(DiscoveryClient.class)
	@ConditionalOnDiscoveryEnabled
	// @ConditionalOnBlockingDiscoveryEnabled
	@ConditionalOnProperty(value = "spring.cloud.discovery.blocking.enabled")
	public static class PrometheusApiConfiguration {

		@Bean
		public PrometheusApi prometheusApi(DiscoveryClient discoveryClient,
			ApplicationEventPublisher eventPublisher) {
			return new PrometheusApi(discoveryClient, eventPublisher);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBean(ReactiveDiscoveryClient.class)
	@ConditionalOnDiscoveryEnabled
	@ConditionalOnReactiveDiscoveryEnabled
	public static class ReactivePrometheusApiConfiguration {

		@Bean
		public ReactivePrometheusApi reactivePrometheusApi(ReactiveDiscoveryClient discoveryClient,
			ApplicationEventPublisher eventPublisher) {
			return new ReactivePrometheusApi(discoveryClient, eventPublisher);
		}

	}

}
