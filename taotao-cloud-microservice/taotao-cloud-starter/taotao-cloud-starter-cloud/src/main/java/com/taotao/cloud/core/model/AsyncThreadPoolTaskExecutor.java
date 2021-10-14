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
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

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
	 * @author shuigedeng
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
