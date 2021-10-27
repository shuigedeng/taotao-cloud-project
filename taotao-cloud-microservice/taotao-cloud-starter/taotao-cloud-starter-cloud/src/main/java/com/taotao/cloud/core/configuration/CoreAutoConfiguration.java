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
package com.taotao.cloud.core.configuration;

import static com.taotao.cloud.core.properties.CoreProperties.SpringApplicationName;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.PropertyCache;
import com.taotao.cloud.core.model.Pubsub;
import com.taotao.cloud.core.monitor.MonitorSystem;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.properties.MonitorThreadPoolProperties;
import com.taotao.cloud.core.runner.CoreApplicationRunner;
import com.taotao.cloud.core.runner.CoreCommandLineRunner;
import com.taotao.cloud.core.utils.PropertyUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import java.util.function.Function;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

/**
 * CoreConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:05:41
 */
@Configuration
@AutoConfigureAfter(AsyncAutoConfiguration.class)
public class CoreAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CoreAutoConfiguration.class, StarterNameConstant.CLOUD_STARTER);
	}

	@Bean(value = "meterRegistryCustomizer")
	MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
		LogUtil.started(MeterRegistryCustomizer.class, StarterNameConstant.CLOUD_STARTER);

		return meterRegistry -> meterRegistry
			.config()
			.commonTags("application", PropertyUtil.getProperty(SpringApplicationName));
	}

	@Bean
	public Pubsub pubsub() {
		LogUtil.started(Pubsub.class, StarterNameConstant.CLOUD_STARTER);
		return new Pubsub();
	}

	@Bean
	public Collector collector(CoreProperties coreProperties) {
		LogUtil.started(Collector.class, StarterNameConstant.CLOUD_STARTER);
		return new Collector(coreProperties);
	}

	@Bean
	public PropertyCache propertyCache(Pubsub pubsub) {
		LogUtil.started(PropertyCache.class, StarterNameConstant.CLOUD_STARTER);
		return new PropertyCache(pubsub);
	}

	@Bean(destroyMethod = "monitorShutdown")
	@ConditionalOnMissingBean(MonitorThreadPool.class)
	public MonitorThreadPool coreMonitorThreadPool(
		Collector collector,
		MonitorThreadPoolProperties monitorThreadPoolProperties,
		AsyncThreadPoolProperties asyncThreadPoolProperties,
		@Qualifier("taskExecutor") AsyncThreadPoolTaskExecutor coreThreadPoolTaskExecutor) {
		LogUtil.started(MonitorThreadPool.class, StarterNameConstant.CLOUD_STARTER);
		return new MonitorThreadPool(
			collector,
			monitorThreadPoolProperties,
			asyncThreadPoolProperties,
			coreThreadPoolTaskExecutor
		);
	}

	@Bean
	public MonitorSystem monitorThread(MonitorThreadPool monitorThreadPool) {
		LogUtil.started(MonitorSystem.class, StarterNameConstant.CLOUD_STARTER);
		return monitorThreadPool.getMonitorSystem();
	}

	@Bean
	public CoreApplicationRunner coreApplicationRunner() {
		LogUtil.started(CoreApplicationRunner.class, StarterNameConstant.CLOUD_STARTER);
		return new CoreApplicationRunner();
	}

	@Bean
	public CoreCommandLineRunner coreCommandLineRunner(PropertyCache propertyCache,
		CoreProperties coreProperties) {
		LogUtil.started(CoreCommandLineRunner.class, StarterNameConstant.CLOUD_STARTER);
		return new CoreCommandLineRunner(propertyCache, coreProperties);
	}

	@Configuration
	public static class CoreFunction implements Function<String, String>{

		@Override
		public String apply(String s) {
			return s;
		}
	}
}
