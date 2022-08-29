/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.dingtalk.constant.DingerConstant;
import com.taotao.cloud.dingtalk.properties.HttpClientProperties;
import com.taotao.cloud.dingtalk.support.DingerHttpClient;
import com.taotao.cloud.dingtalk.support.DingerHttpTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Dinger默认Http客户端配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:16:45
 */
@AutoConfiguration
@EnableConfigurationProperties({HttpClientProperties.class})
@ConditionalOnProperty(prefix = HttpClientProperties.PREFIX, value = "enabled", havingValue = "true")
public class HttpClientAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(HttpClientAutoConfiguration.class, StarterName.DINGTALK_STARTER);
	}

	@Bean(name = "dingerClientHttpRequestFactory")
	public ClientHttpRequestFactory dingerClientHttpRequestFactory(
		HttpClientProperties httpClientProperties) {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout((int) httpClientProperties.getReadTimeout().toMillis());
		factory.setConnectTimeout((int) httpClientProperties.getConnectTimeout().toMillis());
		return factory;
	}

	@Bean(name = DingerConstant.DINGER_REST_TEMPLATE)
	public RestTemplate restTemplate(ClientHttpRequestFactory dingerClientHttpRequestFactory) {
		return new RestTemplate(dingerClientHttpRequestFactory);
	}

	@Bean
	public DingerHttpClient dingerHttpClient(
		@Autowired @Qualifier(DingerConstant.DINGER_REST_TEMPLATE) RestTemplate restTemplate) {
		return new DingerHttpTemplate(restTemplate);
	}
}
