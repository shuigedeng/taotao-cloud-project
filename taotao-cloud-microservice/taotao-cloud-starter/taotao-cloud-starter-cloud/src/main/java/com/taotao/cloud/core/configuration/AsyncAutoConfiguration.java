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

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步任务配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@ConditionalOnProperty(prefix = AsyncThreadPoolProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AsyncAutoConfiguration implements AsyncConfigurer, InitializingBean {

	@Autowired
	private AsyncThreadPoolProperties asyncThreadPoolProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(AsyncAutoConfiguration.class, StarterNameConstant.CLOUD_STARTER);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> LogUtil
			.error(ex, "AsyncUncaughtExceptionHandler {} class: {} method: {} params: {}",
				asyncThreadPoolProperties.getThreadNamePrefix(),
				method.getDeclaringClass().getName(),
				method.getName(),
				params);
	}

	@Override
	@Bean("taskExecutor")
	public AsyncThreadPoolTaskExecutor getAsyncExecutor() {
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

	public static class CoreThreadPoolFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;
		private final AsyncThreadPoolProperties asyncThreadPoolProperties;
		private final ThreadPoolTaskExecutor executor;

		public CoreThreadPoolFactory(AsyncThreadPoolProperties asyncThreadPoolProperties,
			ThreadPoolTaskExecutor executor) {
			this.asyncThreadPoolProperties = asyncThreadPoolProperties;
			this.executor = executor;
			this.namePrefix = asyncThreadPoolProperties.getThreadNamePrefix() + "-pool-"
				+ poolNumber.getAndIncrement();
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(executor.getThreadGroup(), r,
				namePrefix + "-thread-" + threadNumber.getAndIncrement(),
				0);

			UncaughtExceptionHandler handler = t.getUncaughtExceptionHandler();
			if (!(handler instanceof CoreThreadPoolUncaughtExceptionHandler)) {
				t.setUncaughtExceptionHandler(
					new CoreThreadPoolUncaughtExceptionHandler(handler, asyncThreadPoolProperties));
			}

			t.setPriority(executor.getThreadPriority());
			t.setDaemon(executor.isDaemon());

			return t;
		}
	}

	public static class CoreThreadPoolUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {

		private final Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler;
		private final AsyncThreadPoolProperties asyncThreadPoolProperties;

		public CoreThreadPoolUncaughtExceptionHandler(
			Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler,
			AsyncThreadPoolProperties asyncThreadPoolProperties) {
			this.lastUncaughtExceptionHandler = lastUncaughtExceptionHandler;
			this.asyncThreadPoolProperties = asyncThreadPoolProperties;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			if (e != null) {
				LogUtil.error(e, "[警告] [{}] 捕获错误", asyncThreadPoolProperties.getThreadNamePrefix());
			}

			if (lastUncaughtExceptionHandler != null) {
				lastUncaughtExceptionHandler.uncaughtException(t, e);
			}
		}
	}
}
