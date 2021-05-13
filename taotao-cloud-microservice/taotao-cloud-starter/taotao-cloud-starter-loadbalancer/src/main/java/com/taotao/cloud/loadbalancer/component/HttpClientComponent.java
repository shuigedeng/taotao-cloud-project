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
package com.taotao.cloud.loadbalancer.component;

import com.taotao.cloud.loadbalancer.properties.RestTemplateProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * RestTemplateAutoConfiguration
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/6/15 11:31
 */
public class HttpClientComponent {

	/**
	 * 使用连接池的 httpclient
	 */
	@Bean
	@Order(500)
	public HttpClient httpClient(RestTemplateProperties restTemplateProperties) {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder
			.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.getSocketFactory())
			.register("https", SSLConnectionSocketFactory.getSocketFactory())
			.build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
			registry);
		// 最大链接数
		connectionManager.setMaxTotal(restTemplateProperties.getMaxTotal());
		// 同路由并发数20
		connectionManager.setDefaultMaxPerRoute(restTemplateProperties.getMaxPerRoute());

		RequestConfig requestConfig = RequestConfig.custom()
			// 读超时
			.setSocketTimeout(restTemplateProperties.getReadTimeout())
			// 链接超时
			.setConnectTimeout(restTemplateProperties.getConnectTimeout())
			// 链接不够用的等待时间
			.setConnectionRequestTimeout(restTemplateProperties.getReadTimeout())
			.build();

		return HttpClientBuilder.create()
			.setDefaultRequestConfig(requestConfig)
			.setConnectionManager(connectionManager)
			.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
			.build();
	}
}
