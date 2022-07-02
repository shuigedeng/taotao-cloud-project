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

package com.taotao.cloud.feign.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.feign.annotation.ConditionalOnFeignUseHttpClient;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * <p>Description: HttpClient 自动配置 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-02 09:30:46
 */
@AutoConfiguration
@ConditionalOnFeignUseHttpClient
public class HttpClientAutoConfiguration {

	@PostConstruct
	public void postConstruct() {
		LogUtil.started(HttpClientAutoConfiguration.class, StarterName.FEIGN_STARTER);
	}

	private final Timer connectionManagerTimer = new Timer(
		"FeignApacheHttpClientConfiguration.connectionManagerTimer", true);

	@Autowired(required = false)
	private RegistryBuilder registryBuilder;

	private CloseableHttpClient httpClient;

	@Bean
	@ConditionalOnMissingBean(HttpClientConnectionManager.class)
	public HttpClientConnectionManager connectionManager(
		ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
		FeignHttpClientProperties feignHttpClientProperties) {

		final HttpClientConnectionManager connectionManager = connectionManagerFactory.newConnectionManager(
			feignHttpClientProperties.isDisableSslValidation(),
			feignHttpClientProperties.getMaxConnections(),
			feignHttpClientProperties.getMaxConnectionsPerRoute(),
			feignHttpClientProperties.getTimeToLive(),
			feignHttpClientProperties.getTimeToLiveUnit(), this.registryBuilder);
		this.connectionManagerTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				connectionManager.closeExpiredConnections();
			}
		}, 30000, feignHttpClientProperties.getConnectionTimerRepeat());
		return connectionManager;
	}

	@Bean
	public CloseableHttpClient httpClient(ApacheHttpClientFactory httpClientFactory,
		HttpClientConnectionManager httpClientConnectionManager,
		FeignHttpClientProperties feignHttpClientProperties) {
		RequestConfig defaultRequestConfig = RequestConfig.custom()
			.setConnectTimeout(feignHttpClientProperties.getConnectionTimeout())
			.setRedirectsEnabled(feignHttpClientProperties.isFollowRedirects()).build();
		this.httpClient = httpClientFactory.createBuilder()
			.setConnectionManager(httpClientConnectionManager)
			.setDefaultRequestConfig(defaultRequestConfig).build();
		return this.httpClient;
	}

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory(CloseableHttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
			httpClient);
		LogUtil.info("Bean [Client Http Request Factory for HttpClient] Auto Configure.");
		return httpComponentsClientHttpRequestFactory;
	}

	@PreDestroy
	public void destroy() {
		this.connectionManagerTimer.cancel();
		if (this.httpClient != null) {
			try {
				this.httpClient.close();
			} catch (IOException e) {
				LogUtil.info(" Could not correctly close httpClient.");
			}
		}
	}
}
