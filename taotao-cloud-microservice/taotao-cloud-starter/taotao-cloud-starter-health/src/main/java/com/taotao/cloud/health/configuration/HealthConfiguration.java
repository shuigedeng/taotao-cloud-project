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

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.configuration.CoreAutoConfiguration;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.PropertyCache;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import com.taotao.cloud.core.properties.MonitorThreadPoolProperties;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.collect.HealthCheckProvider;
import com.taotao.cloud.health.dump.DumpProvider;
import com.taotao.cloud.health.export.ExportProvider;
import com.taotao.cloud.health.filter.DumpFilter;
import com.taotao.cloud.health.filter.HealthReportFilter;
import com.taotao.cloud.health.filter.PingFilter;
import com.taotao.cloud.health.interceptor.DoubtApiInterceptor;
import com.taotao.cloud.health.properties.CheckProperties;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import com.taotao.cloud.health.properties.DoubtApiProperties;
import com.taotao.cloud.health.properties.DumpProperties;
import com.taotao.cloud.health.properties.ExportProperties;
import com.taotao.cloud.health.properties.HealthProperties;
import com.taotao.cloud.health.properties.PingProperties;
import com.taotao.cloud.health.properties.ReportProperties;
import com.taotao.cloud.health.properties.WarnProperties;
import com.taotao.cloud.health.strategy.DefaultWarnStrategy;
import com.taotao.cloud.health.strategy.Rule;
import com.taotao.cloud.health.strategy.WarnTemplate;
import com.taotao.cloud.health.warn.WarnProvider;
import java.util.Objects;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * HealthConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:22:15
 */
@Configuration
@AutoConfigureAfter({CoreAutoConfiguration.class})
@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "enabled", havingValue = "true")
public class HealthConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HealthConfiguration.class, StarterNameConstant.HEALTH_STARTER);
	}

	@Bean
	public WarnTemplate warnTemplate() {
		return new WarnTemplate()
			.register("", "参数:{name}({desc}),命中规则:{rule},当前值：{value}");
	}

	@Bean
	@ConditionalOnBean(PropertyCache.class)
	public DefaultWarnStrategy defaultWarnStrategy(
		WarnTemplate warnTemplate,
		PropertyCache propertyCache) {
		return new DefaultWarnStrategy(warnTemplate, new Rule.RulesAnalyzer(propertyCache));
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(prefix = WarnProperties.PREFIX, name = "enabled", havingValue = "true")
	public WarnProvider getWarnProvider(WarnProperties warnProperties,
		MonitorThreadPool monitorThreadPool) {
		LogUtil.started(WarnProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new WarnProvider(warnProperties, monitorThreadPool);
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnBean(MonitorThreadPool.class)
	@ConditionalOnProperty(prefix = CheckProperties.PREFIX, name = "enabled", havingValue = "true")
	public HealthCheckProvider getHealthCheckProvider(
		DefaultWarnStrategy strategy,
		DefaultHttpClient defaultHttpClient,
		Collector collector,
		CollectTaskProperties collectTaskProperties,
		HealthProperties healthProperties,
		MonitorThreadPool monitorThreadPool) {
		LogUtil.started(HealthCheckProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new HealthCheckProvider(
			strategy,
			defaultHttpClient,
			collector,
			collectTaskProperties,
			healthProperties,
			monitorThreadPool);
	}

	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnBean(HealthCheckProvider.class)
	@ConditionalOnProperty(prefix = ExportProperties.PREFIX, name = "enabled", havingValue = "true")
	public ExportProvider getExportProvider(
		MonitorThreadPool monitorThreadPool,
		ExportProperties exportProperties,
		HealthCheckProvider healthCheckProvider) {
		LogUtil.started(ExportProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new ExportProvider(monitorThreadPool, exportProperties, healthCheckProvider);
	}

	@Bean
	@ConditionalOnProperty(prefix = DumpProperties.PREFIX, name = "enabled", havingValue = "true")
	public DumpProvider dumpProvider() {
		LogUtil.started(DumpProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new DumpProvider();
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnProperty(prefix = ReportProperties.PREFIX, name = "enabled", havingValue = "true")
	public FilterRegistrationBean healthReportFilter() {
		LogUtil.started(HealthReportFilter.class, StarterNameConstant.HEALTH_STARTER);
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		filterRegistrationBean.setFilter(new HealthReportFilter());
		filterRegistrationBean.setName("taotao-cloud-report-filter");
		filterRegistrationBean.addUrlPatterns("/health/report/*");
		LogUtil.info(
			"health报表注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/");
		return filterRegistrationBean;
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnProperty(prefix = DumpProperties.PREFIX, name = "enabled", havingValue = "true")
	public FilterRegistrationBean dumpFilter() {
		LogUtil.started(DumpFilter.class, StarterNameConstant.HEALTH_STARTER);

		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		filterRegistrationBean.setFilter(new DumpFilter());
		filterRegistrationBean.setName("taotao-cloud-dump-filter");
		filterRegistrationBean.addUrlPatterns("/health/dump/*");
		LogUtil.info(
			"health dump注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/dump/");
		return filterRegistrationBean;
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnProperty(prefix = PingProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean pingFilter() {
		LogUtil.started(PingFilter.class, StarterNameConstant.HEALTH_STARTER);

		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		filterRegistrationBean.setFilter(new PingFilter());
		filterRegistrationBean.setName("taotao-cloud-ping-filter");
		filterRegistrationBean.addUrlPatterns("/health/ping/");
		LogUtil.info(
			"health ping注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/ping/");
		return filterRegistrationBean;
	}

	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnProperty(prefix = DoubtApiProperties.PREFIX, name = "enabled", havingValue = "true")
	public static class DoubtApiConfiguration implements WebMvcConfigurer {

		private DoubtApiProperties properties;
		private Collector collector;

		public DoubtApiConfiguration(Collector collector, DoubtApiProperties properties) {
			this.collector = collector;
			this.properties = properties;
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new DoubtApiInterceptor(collector, properties))
				.addPathPatterns("/**")
				.excludePathPatterns("/actuator/**");
		}
	}
}
