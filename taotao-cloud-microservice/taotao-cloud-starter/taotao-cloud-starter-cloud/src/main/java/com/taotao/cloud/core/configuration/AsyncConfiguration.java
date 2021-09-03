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

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.properties.AsyncProperties;
import com.taotao.cloud.core.thread.ThreadPool;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * 异步任务配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfiguration implements AsyncConfigurer, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(AsyncConfiguration.class, StarterName.CLOUD_STARTER);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> LogUtil
			.error("class#method: " + method.getDeclaringClass().getName() + "#" + method
				.getName(), ex);
	}

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor(AsyncProperties asyncProperties) {
		LogUtil.started(ThreadPoolTaskExecutor.class, StarterName.CLOUD_STARTER);

		ThreadPoolTaskExecutor executor = new AsyncThreadPoolTaskExecutor();
		executor.setCorePoolSize(asyncProperties.getCorePoolSize());
		executor.setMaxPoolSize(asyncProperties.getMaxPoolSiz());
		executor.setQueueCapacity(asyncProperties.getQueueCapacity());
		executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());

		executor.setThreadFactory(new ThreadPool.CoreThreadPoolFactory());
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;
	}

	/**
	 * 这是{@link ThreadPoolTaskExecutor}的一个简单替换，可搭配TransmittableThreadLocal实现父子线程之间的数据传递
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:02:27
	 */
	public class AsyncThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

		private static final long serialVersionUID = -5887035957049288777L;

		@Override
		public void execute(Runnable runnable) {
			Runnable ttlRunnable = TtlRunnable.get(runnable);
			showThreadPoolInfo("execute(Runnable task)");
			super.execute(ttlRunnable);
		}

		@Override
		public void execute(Runnable task, long startTimeout) {
			showThreadPoolInfo("execute(Runnable task, long startTimeout)");
			super.execute(task, startTimeout);
		}

		@Override
		public <T> Future<T> submit(Callable<T> task) {
			Callable ttlCallable = TtlCallable.get(task);
			showThreadPoolInfo("submit(Callable<T> task)");
			return super.submit(ttlCallable);
		}

		@Override
		public Future<?> submit(Runnable task) {
			Runnable ttlRunnable = TtlRunnable.get(task);
			showThreadPoolInfo("submit(Runnable task)");
			return super.submit(ttlRunnable);
		}

		@Override
		public ListenableFuture<?> submitListenable(Runnable task) {
			Runnable ttlRunnable = TtlRunnable.get(task);
			showThreadPoolInfo("submitListenable(Runnable task)");
			return super.submitListenable(ttlRunnable);
		}

		@Override
		public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
			Callable ttlCallable = TtlCallable.get(task);
			showThreadPoolInfo("submitListenable(Callable<T> task)");
			return super.submitListenable(ttlCallable);
		}

		/**
		 * 每次执行任务时输出当前线程池状态
		 *
		 * @param method method
		 * @author shuigedeng
		 * @since 2021-09-02 20:03:15
		 */
		private void showThreadPoolInfo(String method) {
			ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
			LogUtil.info(
				"threadNamePrefix[{}], method[{}], taskCount[{}], completedTaskCount[{}], activeCount[{}], queueSize[{}]",
				this.getThreadNamePrefix(),
				method,
				threadPoolExecutor.getTaskCount(),
				threadPoolExecutor.getCompletedTaskCount(),
				threadPoolExecutor.getActiveCount(),
				threadPoolExecutor.getQueue().size());
		}
	}
}
