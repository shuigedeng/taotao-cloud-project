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
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.properties.AsyncProperties;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 异步任务配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties({AsyncProperties.class, AsyncThreadPoolProperties.class})
@ConditionalOnProperty(prefix = AsyncProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AsyncAutoConfiguration implements AsyncConfigurer, InitializingBean {

	@Autowired
	private AsyncThreadPoolProperties asyncThreadPoolProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(AsyncAutoConfiguration.class, StarterName.CORE_STARTER);
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
	public AsyncThreadPoolTaskExecutor getAsyncExecutor() {
		//AsyncThreadPoolTaskExecutor taskExecutor = ContextUtil.getBean(
		//	AsyncThreadPoolTaskExecutor.class, true);
		//if (Objects.nonNull(taskExecutor)) {
		//	return taskExecutor;
		//}
		//
		//return null;

		AsyncThreadPoolTaskExecutor executor = new AsyncThreadPoolTaskExecutor();
		executor.setCorePoolSize(asyncThreadPoolProperties.getCorePoolSize());
		executor.setMaxPoolSize(asyncThreadPoolProperties.getMaxPoolSiz());
		executor.setQueueCapacity(asyncThreadPoolProperties.getQueueCapacity());
		executor.setKeepAliveSeconds(asyncThreadPoolProperties.getKeepAliveSeconds());
		executor.setThreadNamePrefix(asyncThreadPoolProperties.getThreadNamePrefix());

		executor.setThreadFactory(new AsyncThreadPoolFactory(asyncThreadPoolProperties, executor));
		executor.setTaskDecorator(new AsyncTaskDecorator());

		/*
		 rejection-policy：当pool已经达到max size的时候，如何处理新任务
		 CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		 */
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;
	}

	/**
	 * 对于异步任务时, 同样也能获取到 TraceId
	 * spring 的异步任务 @Async
	 */
	public static class AsyncTaskDecorator implements TaskDecorator {
		@Override
		public Runnable decorate(Runnable runnable) {
			try {
				RequestAttributes context = RequestContextHolder.currentRequestAttributes();
				Map<String,String> previous = MDC.getCopyOfContextMap();
				return () -> {
					try {
						RequestContextHolder.setRequestAttributes(context);

						MDC.setContextMap(previous);

						runnable.run();
					} finally {
						RequestContextHolder.resetRequestAttributes();
						MDC.clear();
					}
				};
			} catch (IllegalStateException e) {
				return runnable;
			}
		}
	}

	public static class AsyncThreadPoolFactory implements ThreadFactory {

		private final AtomicInteger poolNumber = new AtomicInteger(1);
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;
		private final AsyncThreadPoolProperties asyncThreadPoolProperties;
		private final ThreadPoolTaskExecutor executor;

		public AsyncThreadPoolFactory(AsyncThreadPoolProperties asyncThreadPoolProperties,
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
			if (!(handler instanceof AsyncThreadPoolUncaughtExceptionHandler)) {
				t.setUncaughtExceptionHandler(
					new AsyncThreadPoolUncaughtExceptionHandler(handler,
						asyncThreadPoolProperties));
			}

			t.setPriority(executor.getThreadPriority());
			t.setDaemon(executor.isDaemon());

			return t;
		}
	}

	public static class AsyncThreadPoolUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {

		private final Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler;
		private final AsyncThreadPoolProperties asyncThreadPoolProperties;

		public AsyncThreadPoolUncaughtExceptionHandler(
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

	public static class AsyncThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

		@Override
		public void execute(@NotNull Runnable runnable) {
			Runnable ttlRunnable = TtlRunnable.get(runnable);
			showThreadPoolInfo("execute(Runnable task)");
			super.execute(ttlRunnable);
		}

		@Override
		public void execute(@NotNull Runnable task, long startTimeout) {
			showThreadPoolInfo("execute(Runnable task, long startTimeout)");
			super.execute(task, startTimeout);
		}

		@Override
		public <T> Future<T> submit(@NotNull Callable<T> task) {
			Callable<T> ttlCallable = TtlCallable.get(task);
			showThreadPoolInfo("submit(Callable<T> task)");
			return super.submit(ttlCallable);
		}

		@Override
		public Future<?> submit(@NotNull Runnable task) {
			Runnable ttlRunnable = TtlRunnable.get(task);
			showThreadPoolInfo("submit(Runnable task)");
			return super.submit(ttlRunnable);
		}

		@Override
		public ListenableFuture<?> submitListenable(@NotNull Runnable task) {
			Runnable ttlRunnable = TtlRunnable.get(task);
			showThreadPoolInfo("submitListenable(Runnable task)");
			return super.submitListenable(ttlRunnable);
		}

		@Override
		public <T> ListenableFuture<T> submitListenable(@NotNull Callable<T> task) {
			Callable<T> ttlCallable = TtlCallable.get(task);
			showThreadPoolInfo("submitListenable(Callable<T> task)");
			return super.submitListenable(ttlCallable);
		}

		/**
		 * 每次执行任务时输出当前线程池状态
		 *
		 * @param method method
		 * @since 2021-09-02 20:03:15
		 */
		private void showThreadPoolInfo(String method) {
			ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			StackTraceElement stackTraceElement = stackTrace[stackTrace.length - 2];

			LogUtil.info(
				"className[{}] methodName[{}] lineNumber[{}] threadNamePrefix[{}] method[{}]  taskCount[{}] completedTaskCount[{}] activeCount[{}] queueSize[{}]",
				stackTraceElement.getClassName(),
				stackTraceElement.getMethodName(),
				stackTraceElement.getLineNumber(),
				this.getThreadNamePrefix(),
				method,
				threadPoolExecutor.getTaskCount(),
				threadPoolExecutor.getCompletedTaskCount(),
				threadPoolExecutor.getActiveCount(),
				threadPoolExecutor.getQueue().size());
		}
	}
}
