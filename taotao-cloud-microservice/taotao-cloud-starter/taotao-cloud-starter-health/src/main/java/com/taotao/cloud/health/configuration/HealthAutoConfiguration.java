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
package com.taotao.cloud.health.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.configuration.CoreAutoConfiguration;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.common.model.PropertyCache;
import com.taotao.cloud.core.http.HttpClient;
import com.taotao.cloud.core.monitor.Monitor;
import com.taotao.cloud.health.collect.HealthCheckProvider;
import com.taotao.cloud.health.collect.HealthReportFilter;
import com.taotao.cloud.health.dump.DumpProvider;
import com.taotao.cloud.health.export.ExportProvider;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import com.taotao.cloud.health.properties.ExportProperties;
import com.taotao.cloud.health.properties.HealthProperties;
import com.taotao.cloud.health.properties.WarnProperties;
import com.taotao.cloud.health.strategy.DefaultWarnStrategy;
import com.taotao.cloud.health.strategy.Rule;
import com.taotao.cloud.health.strategy.WarnStrategy;
import com.taotao.cloud.health.strategy.WarnTemplate;
import com.taotao.cloud.health.warn.WarnProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * HealthConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:22:15
 */
@Configuration
@EnableConfigurationProperties({
	HealthProperties.class,
	CollectTaskProperties.class,
})
@AutoConfigureAfter({CoreAutoConfiguration.class})
@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "enabled", havingValue = "true")
public class HealthAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HealthAutoConfiguration.class, StarterName.HEALTH_STARTER);
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnBean
	public HealthCheckProvider getHealthCheckProvider(
		WarnStrategy strategy,
		HttpClient httpClient,
		CollectTaskProperties collectTaskProperties,
		HealthProperties healthProperties,
		Monitor monitor) {

		return new HealthCheckProvider(
			collectTaskProperties,
			healthProperties,
			strategy,
			httpClient,
			monitor);
	}

	@Bean
	public FilterRegistrationBean<HealthReportFilter> healthReportFilter() {
		FilterRegistrationBean<HealthReportFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
		filterRegistrationBean.setFilter(new HealthReportFilter());
		filterRegistrationBean.setName(HealthReportFilter.class.getName());
		filterRegistrationBean.addUrlPatterns("/health/report/*");
		return filterRegistrationBean;
	}

}
