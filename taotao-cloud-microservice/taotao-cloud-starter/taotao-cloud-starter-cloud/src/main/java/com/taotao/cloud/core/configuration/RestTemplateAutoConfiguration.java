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
package com.taotao.cloud.core.configuration;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.http.HttpClientManager;
import com.taotao.cloud.core.properties.HttpClientProperties;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:48
 */
@Configuration
public class RestTemplateAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RestTemplateAutoConfiguration.class, StarterNameConstant.CLOUD_STARTER);
	}

	/**
	 * 设置请求连接超时时间（毫秒）
	 */
	private static final int CONNECTION_REQUEST_TIMEOUT = 30 * 1000;

	/**
	 * 设置连接超时时间（毫秒）
	 */
	private static final int CONNECT_TIMEOUT = 90 * 1000;

	/**
	 * 设置读取超时时间（毫秒）
	 */
	private static final int READ_TIMEOUT = 90 * 1000;

	@Bean
	public HttpClientManager httpClientManager() {
		return new HttpClientManager();
	}

	@Bean(destroyMethod = "close")
	public DefaultHttpClient getDefaultHttpClient(
		HttpClientProperties httpClientProperties,
		HttpClientManager httpClientManager) {
		LogUtil.started(DefaultHttpClient.class, StarterNameConstant.CLOUD_STARTER);
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpClientProperties,
			httpClientManager);
		return httpClientManager.register("taotao.cloud.core.httpclient", defaultHttpClient);
	}

	@Bean
	public HttpClient httpClient(DefaultHttpClient defaultHttpClient) {
		LogUtil.started(HttpClient.class, StarterNameConstant.CLOUD_STARTER);
		return defaultHttpClient.getClient();
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(HttpClient httpClient) {
		LogUtil.started(RestTemplate.class, StarterNameConstant.CLOUD_STARTER);

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
		factory.setConnectTimeout(CONNECT_TIMEOUT);
		factory.setReadTimeout(READ_TIMEOUT);
		factory.setHttpClient(httpClient);

		RestTemplate restTemplate = new RestTemplate(factory);
		// 将自定义的 ClientHttpRequestInterceptor 添加到 RestTemplate 中，可添加多个
		//restTemplate.setInterceptors(Collections.singletonList(new GrayHttpRequestInterceptor()));
		return restTemplate;
	}
}
