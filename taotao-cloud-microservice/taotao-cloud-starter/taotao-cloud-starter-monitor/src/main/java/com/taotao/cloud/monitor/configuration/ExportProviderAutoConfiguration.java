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
import com.taotao.cloud.core.monitor.Monitor;
import com.taotao.cloud.monitor.collect.HealthCheckProvider;
import com.taotao.cloud.monitor.export.ExportProvider;
import com.taotao.cloud.monitor.properties.ExportProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * HealthConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:22:15
 */
@AutoConfiguration
@EnableConfigurationProperties(ExportProperties.class)
@ConditionalOnProperty(prefix = ExportProperties.PREFIX, name = "enabled", havingValue = "true")
public class ExportProviderAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(ExportProviderAutoConfiguration.class, StarterName.MONITOR_STARTER);
	}

	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnBean
	public ExportProvider getExportProvider(
		Monitor monitor,
		ExportProperties exportProperties,
		HealthCheckProvider healthCheckProvider) {
		return new ExportProvider(monitor, exportProperties, healthCheckProvider);
	}


}
