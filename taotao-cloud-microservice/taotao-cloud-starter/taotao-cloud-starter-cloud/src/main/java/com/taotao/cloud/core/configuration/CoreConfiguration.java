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

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.PropertyCache;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.runner.CoreApplicationRunner;
import com.taotao.cloud.core.runner.CoreCommandLineRunner;
import com.taotao.cloud.core.thread.ThreadMonitor;
import com.taotao.cloud.core.thread.ThreadPool;
import com.taotao.cloud.core.utils.PropertyUtil;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * CoreConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/04/02 10:25
 */
public class CoreConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.info(CoreConfiguration.class, StarterName.CLOUD_STARTER, " CoreConfiguration 模块已启动");
	}

	@Bean(value = "meterRegistryCustomizer")
	MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
		return meterRegistry -> meterRegistry
			.config()
			.commonTags("application", PropertyUtil.getProperty(SpringApplicationName));
	}

	@Bean(initMethod = "shutdown")
	@Lazy
	public ThreadPool getSystemThreadPool() {
		if (ThreadPool.DEFAULT == null || ThreadPool.DEFAULT.isShutdown()) {
			ThreadPool.initSystem();
		}
		return ThreadPool.DEFAULT;
	}

	@Bean
	@ConditionalOnBean(ThreadPool.class)
	@Lazy
	public ThreadMonitor getSystemThreadPoolMonitor() {
		return ThreadPool.DEFAULT.getThreadMonitor();
	}

	@Bean
	@Lazy
	public PropertyCache getPropertyCache() {
		PropertyCache.DEFAULT.clear();
		return PropertyCache.DEFAULT;
	}

	@Bean
	public CoreApplicationRunner coreApplicationRunner() {
		return new CoreApplicationRunner();
	}

	@Bean
	public CoreCommandLineRunner coreCommandLineRunner() {
		return new CoreCommandLineRunner();
	}
}
