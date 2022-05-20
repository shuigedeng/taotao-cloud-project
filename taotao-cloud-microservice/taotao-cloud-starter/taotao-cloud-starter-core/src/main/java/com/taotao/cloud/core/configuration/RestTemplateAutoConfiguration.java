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
package com.taotao.cloud.core.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.http.HttpClientManager;
import com.taotao.cloud.core.properties.HttpClientProperties;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/8/24 23:48
 */
@AutoConfiguration
@EnableConfigurationProperties({HttpClientProperties.class})
@ConditionalOnProperty(prefix = HttpClientProperties.PREFIX, name = "enabled", havingValue = "true")
public class RestTemplateAutoConfiguration implements InitializingBean {

	private static final String DEFAULT_HTTP_CLIENT_ID = "taotao.cloud.core.httpclient";

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RestTemplateAutoConfiguration.class, StarterName.CORE_STARTER);
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

	@Bean
	public HttpClient httpClient(HttpClientProperties httpClientProperties,
		HttpClientManager httpClientManager) {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpClientProperties,
			httpClientManager);

		return httpClientManager.register(DEFAULT_HTTP_CLIENT_ID, defaultHttpClient).getClient();
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(HttpClient httpClient) {
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
