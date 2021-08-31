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
package com.taotao.cloud.elasticsearch.component;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.elasticsearch.properties.ElasticsearchProperties;
import com.taotao.cloud.elasticsearch.properties.RestClientPoolProperties;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * es配置类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/3 06:47
 */
@ConditionalOnProperty(prefix = ElasticsearchProperties.PREFIX, name = "enabled", havingValue = "true")
public class ElasticsearchComponent implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ElasticsearchComponent.class, StarterName.ELASTICSEARCH_STARTER);
	}

	@Bean
	public RestClientBuilderCustomizer restClientBuilderCustomizer(
		RestClientPoolProperties poolProperties,
		ElasticsearchRestClientProperties restProperties) {
		return (builder) -> {
			setRequestConfig(builder, poolProperties);
			setHttpClientConfig(builder, poolProperties, restProperties);
		};
	}

	/**
	 * 异步httpclient连接延时配置
	 *
	 * @param builder        builder
	 * @param poolProperties poolProperties
	 * @author shuigedeng
	 * @since 2021/2/26 08:53
	 */
	private void setRequestConfig(RestClientBuilder builder,
		RestClientPoolProperties poolProperties) {
		builder.setRequestConfigCallback(requestConfigBuilder -> {
			requestConfigBuilder
				.setConnectTimeout(poolProperties.getConnectTimeOut())
				.setSocketTimeout(poolProperties.getSocketTimeOut())
				.setConnectionRequestTimeout(poolProperties.getConnectionRequestTimeOut());
			return requestConfigBuilder;
		});
	}

	/**
	 * 异步httpclient连接延时配置
	 *
	 * @param builder        builder
	 * @param poolProperties poolProperties
	 * @author shuigedeng
	 * @since 2021/2/26 08:53
	 */
	private void setHttpClientConfig(RestClientBuilder builder,
		RestClientPoolProperties poolProperties,
		ElasticsearchRestClientProperties restProperties) {
		builder.setHttpClientConfigCallback(httpClientBuilder -> {
			httpClientBuilder
				.setMaxConnTotal(poolProperties.getMaxConnectNum())
				.setMaxConnPerRoute(poolProperties.getMaxConnectPerRoute());

			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(restProperties::getUsername).to(username -> {
				CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider
					.setCredentials(AuthScope.ANY,
						new UsernamePasswordCredentials(username, restProperties.getPassword()));
				httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
			});
			return httpClientBuilder;
		});
	}

	@Bean
	@ConditionalOnMissingBean
	public ElasticsearchRestTemplate elasticsearchRestTemplate(
		RestHighLevelClient restHighLevelClient) {
		return new ElasticsearchRestTemplate(restHighLevelClient);
	}
}
