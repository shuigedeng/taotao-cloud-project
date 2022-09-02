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
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.properties.AsyncProperties;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.Objects;
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
@AutoConfiguration(after = AsyncThreadPoolAutoConfiguration.class)
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties({AsyncProperties.class})
@ConditionalOnProperty(prefix = AsyncProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AsyncAutoConfiguration implements AsyncConfigurer, InitializingBean {

	@Autowired
	private AsyncProperties asyncProperties;
	@Autowired
	private AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(AsyncAutoConfiguration.class, StarterName.CORE_STARTER);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> LogUtils
			.error(ex, "AsyncUncaughtExceptionHandler {} class: {} method: {} params: {}",
				asyncProperties.getThreadNamePrefix(),
				method.getDeclaringClass().getName(),
				method.getName(),
				params);
	}

	@Override
	public AsyncThreadPoolTaskExecutor getAsyncExecutor() {
		return asyncThreadPoolTaskExecutor;
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
				LogUtils.error(e, "[警告] [{}] 捕获错误", asyncProperties.getThreadNamePrefix());
			}

			if (lastUncaughtExceptionHandler != null) {
				lastUncaughtExceptionHandler.uncaughtException(t, e);
			}
		}
	}

	/**
	 * 异步线程池任务执行人
	 *
	 * @author shuigedeng
	 * @version 2022.06
	 * @since 2022-07-29 21:59:40
	 */
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

			LogUtils.info(
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
				if (Objects.nonNull(context) && !context.isEmpty()) {
					MDC.setContextMap(context);
				}

				try {
					task.run();
				} finally {
					if (Objects.nonNull(context) && !context.isEmpty()) {
						MDC.clear();
					}
				}
			};
		}
	}
}
