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
import com.taotao.cloud.common.model.PropertyCache;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.health.collect.HealthCheckProvider;
import com.taotao.cloud.health.dump.DumpProvider;
import com.taotao.cloud.health.export.ExportProvider;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import com.taotao.cloud.health.properties.ExportProperties;
import com.taotao.cloud.health.properties.HealthProperties;
import com.taotao.cloud.health.properties.WarnProperties;
import com.taotao.cloud.health.strategy.DefaultWarnStrategy;
import com.taotao.cloud.health.strategy.Rule;
import com.taotao.cloud.health.strategy.WarnTemplate;
import com.taotao.cloud.health.warn.WarnProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HealthConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:22:15
 */
@Configuration
@EnableConfigurationProperties({
	ExportProperties.class,
	HealthProperties.class,
	WarnProperties.class,
	CollectTaskProperties.class,
})
@AutoConfigureAfter({CoreAutoConfiguration.class})
@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "enabled", havingValue = "true")
public class HealthAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HealthAutoConfiguration.class, StarterNameConstant.HEALTH_STARTER);
	}

	@Bean
	public DefaultWarnStrategy defaultWarnStrategy(PropertyCache propertyCache) {
		WarnTemplate warnTemplate = new WarnTemplate()
			.register("", "参数:{name}({desc}),命中规则:{rule},当前值：{value}");
		return new DefaultWarnStrategy(warnTemplate, new Rule.RulesAnalyzer(propertyCache));
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "warn", havingValue = "true")
	public WarnProvider getWarnProvider(WarnProperties warnProperties, MonitorThreadPool monitorThreadPool) {
		LogUtil.started(WarnProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new WarnProvider(warnProperties, monitorThreadPool);
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "check", havingValue = "true")
	public HealthCheckProvider getHealthCheckProvider(
		DefaultWarnStrategy strategy,
		DefaultHttpClient defaultHttpClient,
		CollectTaskProperties collectTaskProperties,
		HealthProperties healthProperties,
		MonitorThreadPool monitorThreadPool) {
		LogUtil.started(HealthCheckProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new HealthCheckProvider(
			strategy,
			defaultHttpClient,
			collectTaskProperties,
			healthProperties,
			monitorThreadPool);
	}

	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "export", havingValue = "true")
	public ExportProvider getExportProvider(
		MonitorThreadPool monitorThreadPool,
		ExportProperties exportProperties,
		HealthCheckProvider healthCheckProvider) {
		LogUtil.started(ExportProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new ExportProvider(monitorThreadPool, exportProperties, healthCheckProvider);
	}

	@Bean
	@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "dump", havingValue = "true")
	public DumpProvider dumpProvider() {
		LogUtil.started(DumpProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new DumpProvider();
	}

}
