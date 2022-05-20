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
package com.taotao.cloud.core.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.model.PropertyCache;
import com.taotao.cloud.common.model.Pubsub;
import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.launch.StartedEventListener;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.runner.CoreApplicationRunner;
import com.taotao.cloud.core.runner.CoreCommandLineRunner;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

import static com.taotao.cloud.core.properties.CoreProperties.SpringApplicationName;

/**
 * CoreConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:05:41
 */
@AutoConfiguration
@EnableConfigurationProperties({CoreProperties.class})
@ConditionalOnProperty(prefix = CoreProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class CoreAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CoreAutoConfiguration.class, StarterName.CORE_STARTER);
	}

	@Bean(value = "meterRegistryCustomizer")
	MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
		return meterRegistry -> meterRegistry
			.config()
			.commonTags("application", PropertyUtil.getProperty(SpringApplicationName));
	}

	@Bean
	public Collector collector(CoreProperties coreProperties) {
		return new Collector(coreProperties);
	}

	@Bean
	public PropertyCache propertyCache() {
		return new PropertyCache(new Pubsub<>());
	}

	@Bean
	public CoreApplicationRunner coreApplicationRunner() {
		return new CoreApplicationRunner();
	}

	@Bean
	@ConditionalOnBean(PropertyCache.class)
	public CoreCommandLineRunner coreCommandLineRunner(PropertyCache propertyCache,
		CoreProperties coreProperties) {
		return new CoreCommandLineRunner(propertyCache, coreProperties);
	}

	@Bean
	public StartedEventListener startedEventListener() {
		return new StartedEventListener();
	}

	@AutoConfiguration
	public static class CoreFunction implements Function<String, String> {

		@Override
		public String apply(String s) {
			return s;
		}
	}
}
