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
package com.taotao.cloud.web.configuration;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.properties.AsyncTaskProperties;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 默认异步任务配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 09:12
 */
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class AsyncTaskConfiguration implements AsyncConfigurer {

	private final AsyncTaskProperties asyncTaskProperties;

	public AsyncTaskConfiguration(
		AsyncTaskProperties asyncTaskProperties) {
		this.asyncTaskProperties = asyncTaskProperties;
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new AsyncThreadPoolTaskExecutor();
		executor.setCorePoolSize(asyncTaskProperties.getCorePoolSize());
		executor.setMaxPoolSize(asyncTaskProperties.getMaxPoolSiz());
		executor.setQueueCapacity(asyncTaskProperties.getQueueCapacity());
		executor.setThreadNamePrefix(asyncTaskProperties.getThreadNamePrefix());

		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> LogUtil
			.error("class#method: " + method.getDeclaringClass().getName() + "#" + method
				.getName(), ex);
	}

	/**
	 * 这是{@link ThreadPoolTaskExecutor}的一个简单替换，可搭配TransmittableThreadLocal实现父子线程之间的数据传递
	 *
	 * @author shuigedeng
	 * @since 2019/8/14
	 */
	public class AsyncThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

		private static final long serialVersionUID = -5887035957049288777L;

		@Override
		public void execute(Runnable runnable) {
			Runnable ttlRunnable = TtlRunnable.get(runnable);
			super.execute(ttlRunnable);
		}

		@Override
		public <T> Future<T> submit(Callable<T> task) {
			Callable ttlCallable = TtlCallable.get(task);
			return super.submit(ttlCallable);
		}

		@Override
		public Future<?> submit(Runnable task) {
			Runnable ttlRunnable = TtlRunnable.get(task);
			return super.submit(ttlRunnable);
		}

		@Override
		public ListenableFuture<?> submitListenable(Runnable task) {
			Runnable ttlRunnable = TtlRunnable.get(task);
			return super.submitListenable(ttlRunnable);
		}

		@Override
		public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
			Callable ttlCallable = TtlCallable.get(task);
			return super.submitListenable(ttlCallable);
		}
	}
}
