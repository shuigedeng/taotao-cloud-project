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
package com.taotao.cloud.common.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 异步处理异常工具类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:23
 */
public class AsyncUtil {

	private final static String name = "异步处理异常工具";
	private static ExecutorService executorService = new ThreadPoolExecutor(1, 20,
		60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	private static Timer taskTimer = new Timer(name, true);

	/**
	 * 启动线程
	 *
	 * @param task
	 * @author dengtao
	 * @since 2021/6/22 17:39
	 */
	public static void execute(Runnable task) {
		execute(task, 0, 0, null);
	}

	/**
	 * 线程启动 出错后重试，最大重试maxRetryCount次，每次默认多延迟1秒执行，最大延迟5秒
	 *
	 * @param task
	 * @param maxRetryCount
	 * @author dengtao
	 * @since 2021/6/22 17:39
	 */
	public static void execute(Runnable task, int maxRetryCount) {
		execute(task, maxRetryCount, 0, null);
	}

	/**
	 * 出错后重试，最大重试maxRetryCount次，每次默认延迟1秒执行，每次增加1秒，最大延迟5秒，返回执行结果
	 *
	 * @param task
	 * @param maxRetryCount
	 * @param consumer
	 * @author dengtao
	 * @since 2021/6/22 17:39
	 */
	public static void execute(Runnable task, int maxRetryCount,
		BiConsumer<Boolean, Throwable> consumer) {
		execute(task, maxRetryCount, 0, consumer);
	}

	/**
	 * 出错后重试，最大重试maxRetryCount次，每次延迟delaySeconds秒执行,如果delaySeconds<=0 默认延迟1秒执行，每次增加1秒，最大延迟5秒，返回执行结果
	 *
	 * @param task
	 * @param maxRetryCount
	 * @param delaySeconds
	 * @param consumer
	 * @author dengtao
	 * @since 2021/6/22 17:39
	 */
	public static void execute(Runnable task, int maxRetryCount, int delaySeconds,
		BiConsumer<Boolean, Throwable> consumer) {
		new Executor(task, maxRetryCount, delaySeconds, consumer).execute();
	}

	/**
	 * 出错后重试，延迟firstDelaySeconds后开始重试，最大重试maxRetryCount次，每次延迟delaySeconds秒执行，如果delaySeconds<=0
	 * 默认延迟1秒执行，每次增加1秒，最大延迟5秒，返回执行结果
	 *
	 * @param task
	 * @param firstDelaySeconds
	 * @param maxRetryCount
	 * @param delaySeconds
	 * @param consumer
	 * @author dengtao
	 * @since 2021/6/22 17:40
	 */
	public static void executeDelay(Runnable task, int firstDelaySeconds, int maxRetryCount,
		int delaySeconds, BiConsumer<Boolean, Throwable> consumer) {
		Executor executor = new Executor(task, maxRetryCount, delaySeconds, consumer);
		if (firstDelaySeconds > 0) {
			LogUtil.warn(name, "延迟执行异常，将会在[" + firstDelaySeconds + "]秒后执行重试");
			taskTimer.schedule(new Task(executor), firstDelaySeconds * 1000L);
		} else {
			executor.execute();
		}
	}

	private static class Executor implements Runnable {

		private int[] retrySeconds = {1, 2, 3, 4, 5};
		private Runnable task;
		private int maxRetryCount;
		private int delaySeconds;
		private int retryAttempts;
		private BiConsumer<Boolean, Throwable> resultConsumer;

		public Executor(Runnable task, int maxRetryCount, int delaySeconds,
			BiConsumer<Boolean, Throwable> resultConsumer) {
			this.task = task;
			this.maxRetryCount = maxRetryCount;
			this.delaySeconds = delaySeconds;
			this.resultConsumer = resultConsumer;
		}

		public void execute() {
			executorService.execute(this);
		}

		@Override
		public void run() {
			Throwable exception = null;
			try {
				task.run();
			} catch (Throwable e) {
				if (retryAttempts++ < maxRetryCount) {
					int delay = delaySeconds > 0 ? delaySeconds
						: retrySeconds[Math.min(retryAttempts, retrySeconds.length) - 1];
					LogUtil.warn(name,
						"执行异常，将会在[" + delay + "]秒后进行第[" + retryAttempts + "]次重试，异常信息：" + e
							.getMessage(), e);
					taskTimer.schedule(new Task(this), delay * 1000L);
					return;
				}
				exception = e;
			}
			if (exception != null) {
				if (maxRetryCount > 0) {
					LogUtil.error(name,
						"执行异常，重试[" + maxRetryCount + "]后仍然失败，异常信息：" + exception.getMessage(),
						exception);
				} else {
					LogUtil.error(name, "执行异常，异常信息：" + exception.getMessage(),
						exception);
				}
			}
			if (resultConsumer != null) {
				try {
					resultConsumer.accept(exception == null, exception);
				} catch (Throwable e) {
					LogUtil.error(name, "处理结果回调异常", e);
				}
			}
		}
	}

	private static class Task extends TimerTask {

		private Executor task;

		public Task(Executor task) {
			this.task = task;
		}

		@Override
		public void run() {
			task.execute();
		}
	}
}
