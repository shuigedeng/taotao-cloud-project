/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.monitor.Monitor;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import com.taotao.cloud.core.properties.MonitorProperties;
import com.taotao.cloud.core.properties.MonitorThreadPoolProperties;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * MonitorAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:05:41
 */
@Configuration
@EnableConfigurationProperties({MonitorThreadPoolProperties.class, MonitorProperties.class})
@ConditionalOnProperty(prefix = MonitorProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class MonitorAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(MonitorAutoConfiguration.class, StarterName.CORE_STARTER);
	}

	@Bean
	@ConditionalOnMissingBean
	public MonitorThreadPoolExecutor monitorThreadPoolExecutor(
		MonitorThreadPoolProperties monitorThreadPoolProperties) {
		String monitorThreadName = monitorThreadPoolProperties.getThreadNamePrefix();
		MonitorThreadPoolExecutor monitorThreadPoolExecutor = new MonitorThreadPoolExecutor(
			monitorThreadPoolProperties.getCorePoolSize(),
			monitorThreadPoolProperties.getMaximumPoolSize(),
			monitorThreadPoolProperties.getKeepAliveTime(),
			TimeUnit.SECONDS,
			new SynchronousQueue<>(),
			new MonitorThreadPoolFactory(monitorThreadName));

		monitorThreadPoolExecutor.setNamePrefix(monitorThreadName);
		monitorThreadPoolExecutor.setRejectedExecutionHandler(
			new ThreadPoolExecutor.CallerRunsPolicy());
		return monitorThreadPoolExecutor;
	}

	@Bean
	@ConditionalOnBean
	public Monitor monitor(
		Collector collector,
		MonitorThreadPoolProperties monitorThreadPoolProperties,
		MonitorThreadPoolExecutor monitorThreadPoolExecutor,
		AsyncThreadPoolProperties asyncThreadPoolProperties,
		AsyncThreadPoolTaskExecutor asyncThreadPoolTaskExecutor) {

		return new Monitor(
			collector,
			asyncThreadPoolProperties,
			asyncThreadPoolTaskExecutor,
			monitorThreadPoolProperties,
			monitorThreadPoolExecutor
		);
	}

	/**
	 * 这是{@link ThreadPoolTaskExecutor}的一个简单替换，可搭配TransmittableThreadLocal实现父子线程之间的数据传递
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:02:27
	 */
	public static class MonitorThreadPoolExecutor extends ThreadPoolExecutor {

		private String namePrefix;

		public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			@NotNull TimeUnit unit,
			@NotNull BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			@NotNull TimeUnit unit,
			@NotNull BlockingQueue<Runnable> workQueue,
			@NotNull ThreadFactory threadFactory) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		}

		public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			@NotNull TimeUnit unit,
			@NotNull BlockingQueue<Runnable> workQueue,
			@NotNull RejectedExecutionHandler handler) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		}

		public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			@NotNull TimeUnit unit,
			@NotNull BlockingQueue<Runnable> workQueue,
			@NotNull ThreadFactory threadFactory,
			@NotNull RejectedExecutionHandler handler) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,
				handler);
		}

		@Override
		public void execute(@NotNull Runnable runnable) {
			Runnable ttlRunnable = TtlRunnable.get(runnable);
			showThreadPoolInfo("execute(Runnable task)");
			super.execute(ttlRunnable);
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

		/**
		 * 每次执行任务时输出当前线程池状态
		 *
		 * @param method method
		 * @since 2021-09-02 20:03:15
		 */
		private void showThreadPoolInfo(String method) {
			ThreadFactory threadFactory = this.getThreadFactory();
			MonitorThreadPoolFactory monitorThreadPoolFactory = null;
			if (threadFactory instanceof MonitorThreadPoolFactory) {
				monitorThreadPoolFactory = (MonitorThreadPoolFactory) threadFactory;
			}

			String threadNamePrefix = Objects.nonNull(monitorThreadPoolFactory) ?
				monitorThreadPoolFactory.getNamePrefix() : getNamePrefix();

			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			StackTraceElement stackTraceElement = stackTrace[stackTrace.length - 2];

			LogUtil.info(
				"className[{}] methodName[{}] lineNumber[{}] threadNamePrefix[{}] method[{}]  taskCount[{}] completedTaskCount[{}] activeCount[{}] queueSize[{}]",
				stackTraceElement.getClassName(),
				stackTraceElement.getMethodName(),
				stackTraceElement.getLineNumber(),
				threadNamePrefix,
				method,
				this.getTaskCount(),
				this.getCompletedTaskCount(),
				this.getActiveCount(),
				this.getQueue().size());
		}

		public String getNamePrefix() {
			return namePrefix;
		}

		public void setNamePrefix(String namePrefix) {
			this.namePrefix = namePrefix;
		}
	}

	/**
	 * CoreThreadPoolFactory
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:48:39
	 */
	public static class MonitorThreadPoolFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		public MonitorThreadPoolFactory(String namePrefix) {
			this.group = Thread.currentThread().getThreadGroup();

			this.namePrefix = namePrefix + "-pool-" + poolNumber.getAndIncrement();
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r,
				namePrefix + "-thread-" + threadNumber.getAndIncrement(),
				0);

			UncaughtExceptionHandler handler = t.getUncaughtExceptionHandler();
			if (!(handler instanceof MonitorThreadPoolUncaughtExceptionHandler)) {
				t.setUncaughtExceptionHandler(
					new MonitorThreadPoolUncaughtExceptionHandler(handler));
			}

			//后台线程模式
			t.setDaemon(true);

			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}

			return t;
		}

		public ThreadGroup getGroup() {
			return group;
		}

		public AtomicInteger getThreadNumber() {
			return threadNumber;
		}

		public String getNamePrefix() {
			return namePrefix;
		}
	}

	/**
	 * DefaultThreadPoolUncaughtExceptionHandler
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:48:50
	 */
	public static class MonitorThreadPoolUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {

		private Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler;

		public MonitorThreadPoolUncaughtExceptionHandler(
			Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler) {
			this.lastUncaughtExceptionHandler = lastUncaughtExceptionHandler;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			if (e != null) {
				LogUtil.error(e, "[警告] [taotao-cloud-monitor-executor] 未捕获错误");
			}

			if (lastUncaughtExceptionHandler != null) {
				lastUncaughtExceptionHandler.uncaughtException(t, e);
			}
		}
	}
}
