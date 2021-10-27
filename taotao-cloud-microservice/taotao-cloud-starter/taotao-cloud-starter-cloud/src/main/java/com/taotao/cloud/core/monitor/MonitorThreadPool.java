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
package com.taotao.cloud.core.monitor;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.AsyncThreadPoolExecutor;
import com.taotao.cloud.core.model.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.model.Callable.Action1;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.ProcessExitEvent;
import com.taotao.cloud.core.model.Ref;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import com.taotao.cloud.core.properties.MonitorThreadPoolProperties;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自定义线程池及关键方法包装实现 装饰
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:46:36
 */
public class MonitorThreadPool {

	/**
	 * monitorThreadPoolExecutor
	 */
	private final ThreadPoolExecutor monitorThreadPoolExecutor;
	/**
	 * coreThreadPoolExecutor
	 */
	private final ThreadPoolTaskExecutor coreThreadPoolExecutor;
	/**
	 * monitorCheckHealth
	 */
	private boolean monitorCheckHealth = true;
	/**
	 * coreCheckHealth
	 */
	private boolean coreCheckHealth = true;
	/**
	 * monitorSystem
	 */
	private MonitorSystem monitorSystem;
	/**
	 * monitorThreadName
	 */
	private String monitorThreadName;
	/**
	 * collector
	 */
	private Collector collector;

	public MonitorThreadPool(
		Collector collector,
		MonitorThreadPoolProperties monitorThreadPoolProperties,
		AsyncThreadPoolProperties asyncThreadPoolProperties,
		AsyncThreadPoolTaskExecutor coreThreadPoolTaskExecutor) {
		this.collector = collector;

		// 监控线程池
		this.monitorThreadName = monitorThreadPoolProperties.getThreadNamePrefix();
		AsyncThreadPoolExecutor monitorThreadPoolExecutor = new AsyncThreadPoolExecutor(
			monitorThreadPoolProperties.getCorePoolSize(),
			monitorThreadPoolProperties.getMaximumPoolSize(),
			monitorThreadPoolProperties.getKeepAliveTime(),
			TimeUnit.SECONDS,
			new SynchronousQueue<>(),
			new MonitorThreadPoolFactory(this.monitorThreadName));

		monitorThreadPoolExecutor.setNamePrefix(this.monitorThreadName);
		this.monitorThreadPoolExecutor = monitorThreadPoolExecutor;

		this.monitorThreadPoolExecutor.setRejectedExecutionHandler(
			new ThreadPoolExecutor.CallerRunsPolicy());

		// 核心线程池
		this.coreThreadPoolExecutor = coreThreadPoolTaskExecutor;

		this.monitorSystem = new MonitorSystem(
			this.collector,
			this.monitorThreadName,
			asyncThreadPoolProperties.getThreadNamePrefix(),
			monitorThreadPoolExecutor,
			coreThreadPoolTaskExecutor);

		//JVM 停止或重启时，关闭线程池释
		ProcessExitEvent.register(() -> {
			try {
				this.monitorShutdown();
				this.coreShutdown();
			} catch (Exception e) {
				LogUtil.error(e, "关闭SystemThreadPool时出错");
			}
		}, Integer.MAX_VALUE, false);
	}

	/**
	 * monitorThreadPoolCheckHealth
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:26
	 */
	private void monitorThreadPoolCheckHealth() {
		if (monitorCheckHealth
			&& monitorThreadPoolExecutor.getMaximumPoolSize()
			<= monitorThreadPoolExecutor.getPoolSize()
			&& monitorThreadPoolExecutor.getQueue().size() > 0) {
			LogUtil.warn(
				"监控线程池已满 任务开始出现排队 请修改配置 [taotao.cloud.core.threadpool.monitor.maximumPoolSize] 当前活动线程数: {}"
				, monitorThreadPoolExecutor.getActiveCount());
		}
	}

	/**
	 * monitorThreadPoolCheckHealth
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:26
	 */
	private void coreThreadPoolCheckHealth() {
		if (coreCheckHealth
			&& coreThreadPoolExecutor.getMaxPoolSize() <= coreThreadPoolExecutor.getPoolSize()
			&& coreThreadPoolExecutor.getThreadPoolExecutor().getQueue().size() > 0) {
			LogUtil.warn(
				"核心线程池已满 任务开始出现排队 请修改配置 [taotao.cloud.core.threadpool.async.threadPoolMaxSiz] 当前活动线程数: {}"
				, coreThreadPoolExecutor.getActiveCount());
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
	public <T> Future<T> monitorSubmit(String taskName, Callable<T> task) {
		monitorThreadPoolCheckHealth();
		return monitorSystem.monitorHook()
			.run(taskName, () -> monitorThreadPoolExecutor.submit(task));
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
	public <T> Future<T> coreSubmit(String taskName, Callable<T> task) {
		if (Objects.isNull(coreThreadPoolExecutor)) {
			LogUtil.warn("核心线程池未初始化");
			return null;
		}

		coreThreadPoolCheckHealth();
		return monitorSystem.coreHook()
			.run(taskName, () -> coreThreadPoolExecutor.submit(task));
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
	public Future<?> monitorSubmit(String taskName, Runnable task) {
		monitorThreadPoolCheckHealth();
		return monitorSystem.monitorHook()
			.run(taskName, () -> monitorThreadPoolExecutor.submit(task));
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
	public Future<?> coreSubmit(String taskName, Runnable task) {
		if (Objects.isNull(coreThreadPoolExecutor)) {
			LogUtil.warn("核心线程池未初始化");
			return null;
		}
		coreThreadPoolCheckHealth();
		return monitorSystem.coreHook()
			.run(taskName, () -> coreThreadPoolExecutor.submit(task));
	}

	/**
	 * isShutdown
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:50
	 */
	public boolean monitorIsShutdown() {
		return monitorThreadPoolExecutor.isShutdown();
	}

	/**
	 * isShutdown
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:50
	 */
	public boolean coreIsShutdown() {
		if (Objects.isNull(coreThreadPoolExecutor)) {
			LogUtil.warn("核心线程池未初始化");
			return true;
		}

		return coreThreadPoolExecutor.getThreadPoolExecutor().isShutdown();
	}

	/**
	 * shutdown
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:52
	 */
	public void monitorShutdown() {
		monitorThreadPoolExecutor.shutdown();
	}


	/**
	 * shutdown
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:47:52
	 */
	public void coreShutdown() {
		if (Objects.nonNull(coreThreadPoolExecutor)) {
			coreThreadPoolExecutor.shutdown();
		}
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
	public <T> void monitorParallelFor(String taskName, int parallelCount, List<T> array,
		final Action1<T> action) {
		monitorThreadPoolCheckHealth();

		monitorSystem.monitorHook().run(taskName, () -> {
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
					Future<?> future = monitorThreadPoolExecutor.submit(() -> {
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
	public <T> void monitorParallelFor2(String taskName, int parallelCount, Collection<T> array,
		final Action1<T> action) {
		monitorThreadPoolCheckHealth();
		monitorSystem.monitorHook().run(taskName, () -> {
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
					monitorThreadPoolExecutor.submit(() -> {
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

	public ThreadPoolExecutor getMonitorThreadPoolExecutor() {
		return monitorThreadPoolExecutor;
	}

	public ThreadPoolTaskExecutor getCoreThreadPoolExecutor() {
		return coreThreadPoolExecutor;
	}

	public boolean isMonitorCheckHealth() {
		return monitorCheckHealth;
	}

	public void setMonitorCheckHealth(boolean monitorCheckHealth) {
		this.monitorCheckHealth = monitorCheckHealth;
	}

	public boolean isCoreCheckHealth() {
		return coreCheckHealth;
	}

	public void setCoreCheckHealth(boolean coreCheckHealth) {
		this.coreCheckHealth = coreCheckHealth;
	}

	public MonitorSystem getMonitorSystem() {
		return monitorSystem;
	}

	public void setMonitorSystem(MonitorSystem monitorSystem) {
		this.monitorSystem = monitorSystem;
	}

	public String getMonitorThreadName() {
		return monitorThreadName;
	}

	public void setMonitorThreadName(String monitorThreadName) {
		this.monitorThreadName = monitorThreadName;
	}

	public Collector getCollector() {
		return collector;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}
}
