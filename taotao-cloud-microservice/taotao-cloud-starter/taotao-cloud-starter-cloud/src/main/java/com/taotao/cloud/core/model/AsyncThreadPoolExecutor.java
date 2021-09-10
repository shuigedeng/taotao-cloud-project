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
package com.taotao.cloud.core.model;

/**
 * AsyncThreadPoolTaskExecutor
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/09/08 17:35
 */

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.monitor.MonitorThreadPool.MonitorThreadPoolFactory;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 这是{@link ThreadPoolTaskExecutor}的一个简单替换，可搭配TransmittableThreadLocal实现父子线程之间的数据传递
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:02:27
 */
public class AsyncThreadPoolExecutor extends ThreadPoolExecutor {

	private static final long serialVersionUID = -5887035957049288777L;

	private String namePrefix;

	public AsyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
		@NotNull TimeUnit unit,
		@NotNull BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	public AsyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
		@NotNull TimeUnit unit,
		@NotNull BlockingQueue<Runnable> workQueue,
		@NotNull ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public AsyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
		@NotNull TimeUnit unit,
		@NotNull BlockingQueue<Runnable> workQueue,
		@NotNull RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public AsyncThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
		@NotNull TimeUnit unit,
		@NotNull BlockingQueue<Runnable> workQueue,
		@NotNull ThreadFactory threadFactory,
		@NotNull RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,
			handler);
	}

	@Override
	public void execute(Runnable runnable) {
		Runnable ttlRunnable = TtlRunnable.get(runnable);
		showThreadPoolInfo("execute(Runnable task)");
		super.execute(ttlRunnable);
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

	/**
	 * 每次执行任务时输出当前线程池状态
	 *
	 * @param method method
	 * @author shuigedeng
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

		LogUtil.info(
			"threadNamePrefix[{}], method[{}], taskCount[{}], completedTaskCount[{}], activeCount[{}], queueSize[{}]",
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
