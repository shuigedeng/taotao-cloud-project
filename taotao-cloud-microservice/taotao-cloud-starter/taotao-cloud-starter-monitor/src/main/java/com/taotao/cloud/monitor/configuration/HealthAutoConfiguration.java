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
package com.taotao.cloud.monitor.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.configuration.CoreAutoConfiguration;
import com.taotao.cloud.core.configuration.MonitorAutoConfiguration;
import com.taotao.cloud.core.monitor.Monitor;
import com.taotao.cloud.monitor.collect.HealthCheckProvider;
import com.taotao.cloud.monitor.collect.HealthReportFilter;
import com.taotao.cloud.monitor.endpoint.MonitorEndPoint;
import com.taotao.cloud.monitor.properties.CollectTaskProperties;
import com.taotao.cloud.monitor.properties.MonitorProperties;
import com.taotao.cloud.monitor.strategy.WarnStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * HealthConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:22:15
 */
@AutoConfiguration(after = {CoreAutoConfiguration.class, WarnProviderAutoConfiguration.class, MonitorAutoConfiguration.class})
@EnableConfigurationProperties({MonitorProperties.class, CollectTaskProperties.class})
@ConditionalOnProperty(prefix = MonitorProperties.PREFIX, name = "enabled", havingValue = "true")
public class HealthAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(HealthAutoConfiguration.class, StarterName.MONITOR_STARTER);
	}

	@Bean(destroyMethod = "close")
	public HealthCheckProvider healthCheckProvider(
		WarnStrategy strategy,
		CollectTaskProperties collectTaskProperties,
		MonitorProperties monitorProperties,
		Monitor monitor) {
		return new HealthCheckProvider(
			collectTaskProperties,
			monitorProperties,
			strategy,
			monitor);
	}

	@Bean
	public MonitorEndPoint systemHealthEndPoint(HealthCheckProvider healthCheckProvider) {
		return new MonitorEndPoint(healthCheckProvider);
	}

	@Bean
	@ConditionalOnWebApplication(type = Type.SERVLET)
	public FilterRegistrationBean<HealthReportFilter> healthReportFilter() {
		FilterRegistrationBean<HealthReportFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
		filterRegistrationBean.setFilter(new HealthReportFilter());
		filterRegistrationBean.setName(HealthReportFilter.class.getName());
		filterRegistrationBean.addUrlPatterns("/health/report/*");
		return filterRegistrationBean;
	}

}
