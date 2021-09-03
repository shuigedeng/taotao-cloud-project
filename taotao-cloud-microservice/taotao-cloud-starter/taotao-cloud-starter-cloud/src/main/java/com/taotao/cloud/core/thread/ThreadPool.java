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
package com.taotao.cloud.core.thread;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Callable.Action1;
import com.taotao.cloud.core.model.ProcessExitEvent;
import com.taotao.cloud.core.model.Ref;
import com.taotao.cloud.core.properties.ThreadPoolProperties;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自定义线程池及关键方法包装实现 装饰
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:46:36
 */
public class ThreadPool {

	/**
	 * DEFAULT
	 */
	public static ThreadPool DEFAULT;
	/**
	 * threadPoolExecutor
	 */
	private ThreadPoolExecutor threadPoolExecutor;
	/**
	 * checkHealth
	 */
	private boolean checkHealth = true;
	/**
	 * threadMonitor
	 */
	private ThreadMonitor threadMonitor;
	/**
	 * name
	 */
	private String name;

	static {
		initSystem();

		//JVM 停止或重启时，关闭线程池释
		ProcessExitEvent.register(() -> {
			try {
				DEFAULT.shutdown();
			} catch (Exception e) {
				LogUtil.error(e, "关闭SystemThreadPool时出错");
			}
		}, Integer.MAX_VALUE, false);
	}

	/**
	 * initSystem
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:46:54
	 */
	public static void initSystem() {
		ThreadPoolProperties threadPoolProperties = ContextUtil.getBean(ThreadPoolProperties.class,
				true);
		DEFAULT = new ThreadPool("taotao.cloud.core.threadPool",
				threadPoolProperties.getThreadPoolMinSize(),
				threadPoolProperties.getThreadPoolMaxSiz());
	}

	public ThreadPool(String name, int threadPoolMinSize, int threadPoolMaxSize) {
		this.name = name;

		ThreadPoolTaskExecutor threadPoolTaskExecutor = ContextUtil.getBean(
				ThreadPoolTaskExecutor.class, true);
		if (Objects.isNull(threadPoolTaskExecutor)) {
			threadPoolExecutor = new ThreadPoolExecutor(
					threadPoolMinSize,
					threadPoolMaxSize,
					60L,
					TimeUnit.SECONDS,
					new SynchronousQueue<>(),
					new CoreThreadPoolFactory());
		} else {
			threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
		}

		threadMonitor = new ThreadMonitor(this.name, threadPoolExecutor);
	}

	/**
	 * checkHealth
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:26
	 */
	private void checkHealth() {
		if (checkHealth
				&& threadPoolExecutor.getMaximumPoolSize() <= threadPoolExecutor.getPoolSize()
				&& threadPoolExecutor.getQueue().size() > 0) {
			LogUtil.warn(
					"线程池已满,任务开始出现排队,taotao.cloud.core.threadpool.threadPoolMaxSiz,当前: {}"
					, threadPoolExecutor.getMaximumPoolSize());
		}
	}

	/**
	 * submit
	 *
	 * @param taskName taskName
	 * @param task     task
	 * @param <T>      T
	 * @return {@link java.util.concurrent.Future }
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:31
	 */
	public <T> Future<T> submit(String taskName, Callable<T> task) {
		checkHealth();
		return threadMonitor.hook().run(taskName, () -> threadPoolExecutor.submit(task));
	}

	/**
	 * submit
	 *
	 * @param taskName taskName
	 * @param task     task
	 * @return {@link java.util.concurrent.Future }
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:41
	 */
	public Future<?> submit(String taskName, Runnable task) {
		checkHealth();
		return threadMonitor.hook().run(taskName, () -> threadPoolExecutor.submit(task));
	}

	/**
	 * isShutdown
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:50
	 */
	public boolean isShutdown() {
		return threadPoolExecutor.isShutdown();
	}

	/**
	 * shutdown
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:52
	 */
	public void shutdown() {
		threadPoolExecutor.shutdown();
	}


	/**
	 * 任务拆分多个小任务分批并行处理，并行处理完一批再并行处理下一批。 在抛出错误的时候有问题，未修复，仅试验，不要用这个方法。
	 *
	 * @param taskName      taskName
	 * @param parallelCount parallelCount
	 * @param array         array
	 * @param action        action
	 * @param <T>           T
	 * @author shuigedeng
	 * @since 2021-09-02 20:48:09
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
			Queue<T> queueTasks = new LinkedList<>();
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
					Future<?> future = threadPoolExecutor.submit(() -> {
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
					LogUtil.error(exp, "parallelFor 任务计数异常");
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
	 * @param taskName      taskName
	 * @param parallelCount parallelCount
	 * @param array         array
	 * @param action        action
	 * @param <T>           T
	 * @author shuigedeng
	 * @since 2021-09-02 20:48:25
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
					threadPoolExecutor.submit(() -> {
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
					});
				}

				try {
					latch.await();
				} catch (InterruptedException exp) {
					LogUtil.error(exp, "parallelFor 任务计数异常");
				}
				if (!exceptionRef.isNull()) {
					throw new BaseException("parallelFor 并行执行出错", exceptionRef.getData());
				}
			}
			return 1;
		});
	}

	/**
	 * CoreThreadPoolFactory
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:48:39
	 */
	public static class CoreThreadPoolFactory implements ThreadFactory {

		/**
		 * factory
		 */
		private final ThreadFactory factory = Executors.defaultThreadFactory();

		@Override
		public Thread newThread(Runnable r) {
			Thread t = factory.newThread(r);
			t.setName("taotao-cloud-core-thread-" + t.getName());

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

	/**
	 * DefaultThreadPoolUncaughtExceptionHandler
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:48:50
	 */
	public static class DefaultThreadPoolUncaughtExceptionHandler implements
			Thread.UncaughtExceptionHandler {

		private Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler;

		public DefaultThreadPoolUncaughtExceptionHandler(
				Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler) {
			this.lastUncaughtExceptionHandler = lastUncaughtExceptionHandler;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			if (e != null) {
				LogUtil.error(e, "[警告] TheadPool 未捕获错误");
			}

			if (lastUncaughtExceptionHandler != null) {
				lastUncaughtExceptionHandler.uncaughtException(t, e);
			}
		}
	}

	public boolean isCheckHealth() {
		return checkHealth;
	}

	public void setCheckHealth(boolean checkHealth) {
		this.checkHealth = checkHealth;
	}

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	public ThreadMonitor getThreadMonitor() {
		return threadMonitor;
	}

	public String getName() {
		return name;
	}
}
