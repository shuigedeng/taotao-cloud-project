/*
 * Copyright ©2015-2021 Jaemon. All Rights Reserved.
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
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.dingtalk.constant.DingerConstant;
import com.taotao.cloud.dingtalk.exception.ConfigurationException;
import com.taotao.cloud.dingtalk.model.DingerConfigurerAdapter;
import com.taotao.cloud.dingtalk.model.DingerManagerBuilder;
import com.taotao.cloud.dingtalk.model.DingerRobot;
import com.taotao.cloud.dingtalk.properties.DingerProperties;
import com.taotao.cloud.dingtalk.properties.HttpClientProperties;
import com.taotao.cloud.dingtalk.properties.ThreadPoolProperties;
import com.taotao.cloud.dingtalk.session.DingerSessionFactory;
import com.taotao.cloud.dingtalk.session.SessionConfiguration;
import com.taotao.cloud.dingtalk.spring.DingerSessionFactoryBean;
import com.taotao.cloud.dingtalk.support.CustomMessage;
import com.taotao.cloud.dingtalk.support.DingerAsyncCallback;
import com.taotao.cloud.dingtalk.support.DingerExceptionCallback;
import com.taotao.cloud.dingtalk.support.DingerHttpClient;
import com.taotao.cloud.dingtalk.support.DingerIdGenerator;
import com.taotao.cloud.dingtalk.support.DingerSignAlgorithm;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * DingerAutoConfiguration
 *
 * @since 1.2
 */
@Configuration
@EnableConfigurationProperties({
	ThreadPoolProperties.class,
	HttpClientProperties.class,
	DingerProperties.class})
@AutoConfigureAfter({BeanConfiguration.class,
	HttpClientConfiguration.class,
	ThreadPoolConfiguration.class})
@ConditionalOnProperty(prefix = DingerProperties.PREFIX, value = "enabled", havingValue = "true")
public class DingtalkConfiguration implements InitializingBean {

	private final DingerProperties properties;
	private final ResourceLoader resourceLoader;

	public DingtalkConfiguration(DingerProperties dingerProperties, ResourceLoader resourceLoader) {
		this.properties = dingerProperties;
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(DingtalkConfiguration.class, StarterName.DINGTALK_STARTER);

		checkConfigFileExists();
	}

	@Bean
	@ConditionalOnMissingBean(DingerConfigurerAdapter.class)
	public DingerConfigurerAdapter dingerConfigurerAdapter() {
		return new DingerConfigurerAdapter();
	}

	@Bean
	@ConditionalOnBean
	public DingerManagerBuilder dingerManagerBuilder(
		@Qualifier(DingerConstant.DINGER_REST_TEMPLATE) RestTemplate restTemplate,
		DingerExceptionCallback dingerExceptionCallback,
		@Qualifier(DingerConstant.TEXT_MESSAGE) CustomMessage textMessage,
		@Qualifier(DingerConstant.MARKDOWN_MESSAGE) CustomMessage markDownMessage,
		DingerSignAlgorithm dingerSignAlgorithm,
		DingerIdGenerator dingerIdGenerator,
		@Qualifier(DingerConstant.DINGER_EXECUTOR) Executor dingTalkExecutor,
		DingerAsyncCallback dingerAsyncCallback,
		DingerHttpClient dingerHttpClient) {

		return new DingerManagerBuilder(restTemplate, dingerExceptionCallback, textMessage
			, markDownMessage, dingerSignAlgorithm, dingerIdGenerator, dingTalkExecutor,
			dingerAsyncCallback, dingerHttpClient);
	}

	@Bean
	public DingerRobot dingerSender(DingerConfigurerAdapter dingerConfigurerAdapter,
		DingerManagerBuilder dingerManagerBuilder) {
		try {
			dingerConfigurerAdapter.configure(dingerManagerBuilder);
		} catch (Exception ex) {
			throw new ConfigurationException(ex);
		}
		return new DingerRobot(properties, dingerManagerBuilder);
	}

	@Bean
	@ConditionalOnMissingBean
	public DingerSessionFactory dingerSessionFactory(DingerRobot dingerRobot) throws Exception {
		DingerSessionFactoryBean factory = new DingerSessionFactoryBean();
		factory.setConfiguration(SessionConfiguration.of(properties, dingerRobot));
		return factory.getObject();
	}

	private void checkConfigFileExists() {
		if (StringUtils.hasText(this.properties.getDingerLocations())) {
			Resource resource = this.resourceLoader.getResource(
				this.properties.getDingerLocations());

			Assert.state(resource.exists(), "Cannot find config location: " + resource
				+ " (please add config file or check your Dinger configuration)");
		}
	}
}
