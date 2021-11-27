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
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.CoreThreadPoolFactory;
import com.taotao.cloud.core.launch.StartedEventListener;
import com.taotao.cloud.core.model.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.common.model.PropertyCache;
import com.taotao.cloud.common.model.Pubsub;
import com.taotao.cloud.core.monitor.MonitorSystem;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.properties.MonitorThreadPoolProperties;
import com.taotao.cloud.core.runner.CoreApplicationRunner;
import com.taotao.cloud.core.runner.CoreCommandLineRunner;
import com.taotao.cloud.common.utils.PropertyUtil;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * CoreConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:05:41
 */
@Configuration
public class CoreAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CoreAutoConfiguration.class, StarterNameConstant.CLOUD_STARTER);
	}

	@Autowired
	private AsyncThreadPoolProperties asyncThreadPoolProperties;

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

	@Bean
	public AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor(){
		LogUtil.started(ThreadPoolTaskExecutor.class, StarterNameConstant.CLOUD_STARTER);

		AsyncThreadPoolTaskExecutor executor = new AsyncThreadPoolTaskExecutor();
		executor.setCorePoolSize(asyncThreadPoolProperties.getCorePoolSize());
		executor.setMaxPoolSize(asyncThreadPoolProperties.getMaxPoolSiz());
		executor.setQueueCapacity(asyncThreadPoolProperties.getQueueCapacity());
		executor.setKeepAliveSeconds(asyncThreadPoolProperties.getKeepAliveSeconds());
		executor.setThreadNamePrefix(asyncThreadPoolProperties.getThreadNamePrefix());

		executor.setThreadFactory(new CoreThreadPoolFactory(asyncThreadPoolProperties, executor));

		/*
		 rejection-policy：当pool已经达到max size的时候，如何处理新任务
		 CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		 */
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;
	}

	@Bean(destroyMethod = "monitorShutdown")
	public MonitorThreadPool coreMonitorThreadPool(
		Collector collector,
		MonitorThreadPoolProperties monitorThreadPoolProperties,
		AsyncThreadPoolProperties asyncThreadPoolProperties,
		AsyncThreadPoolTaskExecutor coreThreadPoolTaskExecutor) {
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

	@Bean
	public StartedEventListener startedEventListener(){
		return new StartedEventListener();
	}

	@Configuration
	public static class CoreFunction implements Function<String, String>{

		@Override
		public String apply(String s) {
			return s;
		}
	}
}
