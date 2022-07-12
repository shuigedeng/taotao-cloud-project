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


package com.taotao.cloud.jetcache.configuration;

import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.support.DefaultMetricsManager;
import com.taotao.cloud.jetcache.metrics.JetCacheMonitorManager;
import com.taotao.cloud.jetcache.properties.JetCacheMetricsProperties;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * jetcache metrics 配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-03 09:50:38
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = JetCacheMetricsProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(JetCacheMetricsProperties.class)
public class JetCacheMetricsAutoConfiguration {

	@Bean
	@ConditionalOnBean
	public JetCacheMonitorManager jetCacheMonitorManager(JetCacheMetricsProperties properties,
														 GlobalCacheConfig globalCacheConfig,
														 MeterRegistry meterRegistry) {
		DefaultMetricsManager defaultMetricsManager;
		if (properties.isEnabledStatInfoLogger()) {
			defaultMetricsManager = new DefaultMetricsManager(globalCacheConfig.getStatIntervalMinutes(), TimeUnit.MINUTES, properties.isVerboseLog());
		} else {
			defaultMetricsManager = null;
		}
		return new JetCacheMonitorManager(defaultMetricsManager, meterRegistry);
	}

}
