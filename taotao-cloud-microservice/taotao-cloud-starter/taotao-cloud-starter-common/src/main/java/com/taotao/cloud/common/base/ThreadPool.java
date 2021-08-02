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
package com.taotao.cloud.common.base;

import com.taotao.cloud.common.base.Callable.Action1;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.LogUtil;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池及关键方法包装实现 装饰
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/6/22 17:14
 **/
public class ThreadPool {

	static {
		initSystem();
	}

	public static void initSystem() {
		System = new ThreadPool("taotao.cloud.threadPool.system",
			ThreadPoolProperties.getThreadPoolMinSize(),
			ThreadPoolProperties.getThreadPoolMaxSize());
	}

	public static ThreadPool System;
	private ThreadPoolExecutor threadPool;

	private boolean checkHealth = true;

	public boolean isCheckHealth() {
		return checkHealth;
	}

	public void setCheckHealth(boolean checkHealth) {
		this.checkHealth = checkHealth;
	}

	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	private ThreadMonitor threadMonitor;
	private String name;

	public ThreadMonitor getThreadMonitor() {
		return threadMonitor;
	}

	public String getName() {
		return name;
	}

	public ThreadPool(String name, int threadPoolMinSize, int threadPoolMaxSize) {
		this.name = name;
		threadPool = new ThreadPoolExecutor(threadPoolMinSize, threadPoolMaxSize,
			60L, TimeUnit.SECONDS,
			new SynchronousQueue<>(), new SystemThreadPoolFactory());
		threadMonitor = new ThreadMonitor(this.name, threadPool);
	}

	private void checkHealth() {
		if (checkHealth && threadPool.getMaximumPoolSize() <= threadPool.getPoolSize()
			&& threadPool.getQueue().size() > 0) {
			LogUtil.warn(
				"线程池已满,任务开始出现排队,请考虑设置taotao.cloud.threadpool.max,当前:" + threadPool.getMaximumPoolSize());
		}
	}

	public <T> Future<T> submit(String taskName, Callable<T> task) {
		checkHealth();
		return threadMonitor.hook().run(taskName, () -> threadPool.submit(task));
	}

	public Future<?> submit(String taskName, Runnable task) {
		checkHealth();
		return threadMonitor.hook().run(taskName, () -> threadPool.submit(task));
	}

	public boolean isShutdown() {
		return threadPool.isShutdown();
	}

	public void shutdown() {
		threadPool.shutdown();
	}


	static {
		//JVM 停止或重启时，关闭线程池释
		ProcessExitEvent.register(() -> {
			try {
				System.shutdown();
			} catch (Exception e) {
				LogUtil
					.error("关闭SystemThreadPool时出错", e);
			}
		}, Integer.MAX_VALUE, false);

	}

	/**
	 * 任务拆分多个小任务分批并行处理，并行处理完一批再并行处理下一批。 在抛出错误的时候有问题，未修复，仅试验，不要用这个方法。
	 *
	 * @param taskName
	 * @param parallelCount
	 * @param array
	 * @param action
	 * @param <T>
	 */
	public <T> void parallelFor(String taskName, int parallelCount, List<T> array,
		final Action1<T> action) {
		checkHealth();
		threadMonitor.hook().run(taskName, () -> {
			int parallelCount2 = parallelCount;
			if (parallelCount2 > array.size()) {
				parallelCount2 = array.size();
			}
			//任务队列
			Queue<T> queueTasks = new LinkedList();
			queueTasks.addAll(array);

			while (queueTasks.size() > 0) {
				//运行任务列表
				final List<T> runningTasks = new ArrayList<>(parallelCount2);
				T task;
				for (int i = 0; i < parallelCount2; i++) {
					if ((task = queueTasks.poll()) != null) {
						runningTasks.add(task);
					} else {
						break;
					}
				}

				final CountDownLatch latch = new CountDownLatch(runningTasks.size());
				List<Future<?>> result = new ArrayList<>(parallelCount2);
				for (T obj : runningTasks) {
					Future<?> future = threadPool.submit(() -> {
						try {
							action.invoke(obj);
						} catch (Exception exp) {
							throw exp;
						} finally {
							latch.countDown();
						}
					});
					result.add(future);
				}

				try {
					latch.await();
				} catch (InterruptedException exp) {
					LogUtil
						.error("parallelFor 任务计数异常", exp);
				}
				for (Future<?> f : result) {
					try {
						f.get();
					} catch (Exception exp) {
						throw new BaseException("parallelFor并行执行出错", exp);
					}
				}
			}
			return 1;
		});

	}


	/**
	 * 任务使用固定并行大小处理任务,直到所有任务处理完毕。
	 *
	 * @param taskName
	 * @param parallelCount
	 * @param array
	 * @param action
	 * @param <T>
	 */
	public <T> void parallelFor2(String taskName, int parallelCount, Collection<T> array,
		final Action1<T> action) {
		checkHealth();
		threadMonitor.hook().run(taskName, () -> {
			int parallelCount2 = parallelCount;
			if (parallelCount2 > array.size()) {
				parallelCount2 = array.size();
			}
			//任务队列
			Queue<T> queueTasks = new LinkedList();
			queueTasks.addAll(array);

			if (queueTasks.size() > 0) {
				final CountDownLatch latch = new CountDownLatch(parallelCount2);
				Object lock = new Object();
				Ref<Exception> exceptionRef = new Ref<>(null);
				for (int i = 0; i < parallelCount2; i++) {
					threadPool.submit(() -> {
						while (true) {
							T task;
							synchronized (lock) {
								task = queueTasks.poll();
							}
							if (task != null && exceptionRef.isNull()) {
								try {
									action.invoke(task);
								} catch (Exception exp) {
									latch.countDown();
									exceptionRef.setData(exp);
									break;
								}
							} else {
								latch.countDown();
								break;
							}
						}
						return;
					});
				}

				try {
					latch.await();
				} catch (InterruptedException exp) {
					LogUtil
						.error("parallelFor 任务计数异常", exp);
				}
				if (!exceptionRef.isNull()) {
					throw new BaseException("parallelFor 并行执行出错", exceptionRef.getData());
				}
			}
			return 1;
		});
	}

	static class SystemThreadPoolFactory implements ThreadFactory {

		private ThreadFactory factory = Executors.defaultThreadFactory();

		@Override
		public Thread newThread(Runnable r) {
			Thread t = factory.newThread(r);
			t.setName("bsf-threadPool-" + t.getName());
			UncaughtExceptionHandler handler = t.getUncaughtExceptionHandler();
			if (!(handler instanceof DefaultThreadPoolUncaughtExceptionHandler)) {
				t.setUncaughtExceptionHandler(
					new DefaultThreadPoolUncaughtExceptionHandler(handler));
			}
			//后台线程模式
			t.setDaemon(true);
			return t;
		}
	}


	public static class DefaultThreadPoolUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {

		private Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler = null;

		public DefaultThreadPoolUncaughtExceptionHandler(
			Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler) {
			this.lastUncaughtExceptionHandler = lastUncaughtExceptionHandler;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			try {
				if (e != null) {
					LogUtil.error("[警告] TheadPool未捕获错误", e);
				}
			} catch (Exception e2) {
				LogUtil.error("[警告] TheadPool未捕获错误", e2);
			}
			if (lastUncaughtExceptionHandler != null) {
				lastUncaughtExceptionHandler.uncaughtException(t, e);
			}
		}
	}
}
