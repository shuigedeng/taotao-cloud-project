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
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.feign.annotation.ConditionalOnFeignUseOkHttp;
import com.taotao.cloud.feign.okhttp.OkHttpResponseInterceptor;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

/**
 * <p>Description: OkHttp 自动配置 </p>
 * <p>
 * 1. 默认让 Feign 使用 OkHttp 作为 HttpClient。所以直接使用 Feign 的配置来对 OkHttp 进行配置。 2. 如果存在
 * `feign.okhttp.enabled` 配置， 同时其值为 `true`，就会自动配置 OkHttp。 3. 在此处配置 OkHttp，也是为了共用 OkHttp 的配置，让其可以同时支持
 * RestTemplate
 * <p>
 * {@code org.springframework.cloud.openfeign.loadbalancer.OkHttpFeignLoadBalancerConfiguration}
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-02 09:31:33
 */
@AutoConfiguration(before = FeignLoadBalancerAutoConfiguration.class)
@ConditionalOnFeignUseOkHttp
public class OkHttpAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(OkHttpAutoConfiguration.class, StarterName.FEIGN_STARTER);
	}

	private okhttp3.OkHttpClient okHttpClient;

	@PostConstruct
	public void postConstruct() {
		LogUtils.started(OkHttpAutoConfiguration.class, StarterName.FEIGN_STARTER);
	}

	@Bean
	@ConditionalOnMissingBean(ConnectionPool.class)
	public ConnectionPool connectionPool(FeignHttpClientProperties feignHttpClientProperties,
		OkHttpClientConnectionPoolFactory connectionPoolFactory) {
		int maxTotalConnections = feignHttpClientProperties.getMaxConnections();
		long timeToLive = feignHttpClientProperties.getTimeToLive();
		TimeUnit ttlUnit = feignHttpClientProperties.getTimeToLiveUnit();
		return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
	}

	@Bean
	public okhttp3.OkHttpClient okHttpClient(OkHttpClientFactory okHttpClientFactory,
		ConnectionPool connectionPool, FeignClientProperties feignClientProperties,
		FeignHttpClientProperties feignHttpClientProperties) {
		FeignClientProperties.FeignClientConfiguration defaultConfig = feignClientProperties.getConfig()
			.get("default");
		int readTimeout = 5000;
		if (Objects.nonNull(defaultConfig)) {
			readTimeout = defaultConfig.getReadTimeout();
		}

		int connectTimeout = feignHttpClientProperties.getConnectionTimeout();
		boolean disableSslValidation = feignHttpClientProperties.isDisableSslValidation();
		boolean followRedirects = feignHttpClientProperties.isFollowRedirects();

		this.okHttpClient = okHttpClientFactory.createBuilder(disableSslValidation)
			.readTimeout(readTimeout, TimeUnit.MILLISECONDS)
			.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
			.followRedirects(followRedirects)
			.connectionPool(connectionPool)
			.addInterceptor(new OkHttpResponseInterceptor())
			.build();

		return this.okHttpClient;
	}

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory(okhttp3.OkHttpClient okHttpClient) {
		OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
		LogUtils.info("Bean [Client Http Request Factory for OkHttp] Auto Configure.");
		return factory;
	}

	@PreDestroy
	public void destroy() {
		if (this.okHttpClient != null) {
			this.okHttpClient.dispatcher().executorService().shutdown();
			this.okHttpClient.connectionPool().evictAll();
		}
	}
}
