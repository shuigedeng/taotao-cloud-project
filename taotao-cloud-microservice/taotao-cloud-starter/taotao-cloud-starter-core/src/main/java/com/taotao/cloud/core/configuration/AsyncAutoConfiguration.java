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

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.decorator.ContextDecorator;
import com.taotao.cloud.core.properties.AsyncProperties;
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
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
@AutoConfiguration
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties({AsyncProperties.class})
@ConditionalOnProperty(prefix = AsyncProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AsyncAutoConfiguration implements AsyncConfigurer, InitializingBean {

	@Autowired
	private AsyncProperties asyncProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(AsyncAutoConfiguration.class, StarterName.CORE_STARTER);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> LogUtil
			.error(ex, "AsyncUncaughtExceptionHandler {} class: {} method: {} params: {}",
				asyncProperties.getThreadNamePrefix(),
				method.getDeclaringClass().getName(),
				method.getName(),
				params);
	}

	@Override
	@Bean(name = "asyncThreadPoolTaskExecutor")
	public AsyncThreadPoolTaskExecutor getAsyncExecutor() {
		AsyncThreadPoolTaskExecutor executor = new AsyncThreadPoolTaskExecutor();

		// 线程池名的前缀
		executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());
		// 核心线程数
		executor.setCorePoolSize(asyncProperties.getCorePoolSize());
		// 最大线程数
		executor.setMaxPoolSize(asyncProperties.getMaxPoolSiz());
		// 允许线程的空闲时间
		executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());
		// 任务队列容量（阻塞队列）
		executor.setQueueCapacity(asyncProperties.getQueueCapacity());
		// 是否允许核心线程超时
		executor.setAllowCoreThreadTimeOut(asyncProperties.isAllowCoreThreadTimeOut());
		// 应用关闭时-是否等待未完成任务继续执行，再继续销毁其他的Bean
		executor.setWaitForTasksToCompleteOnShutdown(asyncProperties.isWaitForTasksToCompleteOnShutdown());
		// 应用关闭时-继续等待时间（单位：秒）
		executor.setAwaitTerminationSeconds(asyncProperties.getAwaitTerminationSeconds());
		// ThreadFactory
		executor.setThreadFactory(new AsyncThreadPoolFactory(asyncProperties, executor));
		// 异步线程上下文装饰器
		executor.setTaskDecorator(new ContextDecorator(asyncProperties));
		/*
		 线程池拒绝策略
		 rejection-policy：当pool已经达到max size的时候，如何处理新任务
		 CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		 */
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		executor.initialize();

		return executor;
	}

	/**
	 * 对于异步任务时, 同样也能获取到 TraceId spring 的异步任务 @Async
	 */
	public static class AsyncTaskDecorator implements TaskDecorator {

		@Override
		public Runnable decorate(Runnable runnable) {
			try {
				RequestAttributes context = RequestContextHolder.currentRequestAttributes();
				Map<String, String> previous = MDC.getCopyOfContextMap();
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
		private final AsyncProperties asyncProperties;
		private final ThreadPoolTaskExecutor executor;

		public AsyncThreadPoolFactory(AsyncProperties asyncProperties,
			ThreadPoolTaskExecutor executor) {
			this.asyncProperties = asyncProperties;
			this.executor = executor;
			this.namePrefix = asyncProperties.getThreadNamePrefix() + "-pool-"
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
						asyncProperties));
			}

			t.setPriority(executor.getThreadPriority());
			t.setDaemon(executor.isDaemon());

			return t;
		}
	}

	public static class AsyncThreadPoolUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {

		private final Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler;
		private final AsyncProperties asyncProperties;

		public AsyncThreadPoolUncaughtExceptionHandler(
			Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler,
			AsyncProperties asyncProperties) {
			this.lastUncaughtExceptionHandler = lastUncaughtExceptionHandler;
			this.asyncProperties = asyncProperties;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			if (e != null) {
				LogUtil.error(e, "[警告] [{}] 捕获错误", asyncProperties.getThreadNamePrefix());
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
			super.execute(wrap(ttlRunnable));
		}

		@NotNull
		@Override
		public <T> Future<T> submit(@NotNull Callable<T> task) {
			Callable<T> ttlCallable = TtlCallable.get(task);
			showThreadPoolInfo("submit(Callable<T> task)");
			return super.submit(wrap(ttlCallable));
		}

		@NotNull
		@Override
		public Future<?> submit(@NotNull Runnable task) {
			Runnable ttlRunnable = TtlRunnable.get(task);
			showThreadPoolInfo("submit(Runnable task)");
			return super.submit(wrap(ttlRunnable));
		}

		@NotNull
		@Override
		public ListenableFuture<?> submitListenable(@NotNull Runnable task) {
			Runnable ttlRunnable = TtlRunnable.get(task);
			showThreadPoolInfo("submitListenable(Runnable task)");
			return super.submitListenable(wrap(ttlRunnable));
		}

		@NotNull
		@Override
		public <T> ListenableFuture<T> submitListenable(@NotNull Callable<T> task) {
			Callable<T> ttlCallable = TtlCallable.get(task);
			showThreadPoolInfo("submitListenable(Callable<T> task)");
			return super.submitListenable(wrap(ttlCallable));
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

		private static <T> Callable<T> wrap(final Callable<T> task) {
			Map<String, String> context = MDC.getCopyOfContextMap();

			return () -> {
				if (!context.isEmpty()) {
					MDC.setContextMap(context);
				}

				try {
					return task.call();
				} finally {
					if (!context.isEmpty()) {
						MDC.clear();
					}
				}
			};
		}

		private static Runnable wrap(final Runnable task) {
			Map<String, String> context = MDC.getCopyOfContextMap();

			return () -> {
				if (!context.isEmpty()) {
					MDC.setContextMap(context);
				}

				try {
					task.run();
				} finally {
					if (!context.isEmpty()) {
						MDC.clear();
					}
				}
			};
		}
	}
}
