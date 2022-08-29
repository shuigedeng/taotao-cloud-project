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
package com.taotao.cloud.core.monitor;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.model.Callable.Action1;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.configuration.MonitorAutoConfiguration.MonitorThreadPoolExecutor;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.ProcessExitEvent;
import com.taotao.cloud.core.model.Ref;
import com.taotao.cloud.core.properties.AsyncProperties;
import com.taotao.cloud.core.properties.MonitorThreadPoolProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自定义线程池及关键方法包装实现 装饰
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:46:36
 */
public class Monitor {

	public static final String TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY = "taotao.cloud.async.executor";
	public static final String TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_HOOK = "taotao.cloud.async.executor.hook";

	public static final String TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY = "taotao.cloud.monitor.executor";
	public static final String TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_HOOK = "taotao.cloud.monitor.executor.hook";

	private ThreadPoolExecutor monitorThreadPoolExecutor;
	private ThreadPoolTaskExecutor asyncThreadPoolExecutor;

	private MonitorThreadPoolProperties monitorThreadPoolProperties;
	private AsyncProperties asyncProperties;

	private Collector collector;

	public Monitor(
		Collector collector,
		AsyncProperties asyncProperties,
		AsyncThreadPoolTaskExecutor asyncThreadPoolExecutor,
		MonitorThreadPoolProperties monitorThreadPoolProperties,
		MonitorThreadPoolExecutor monitorThreadPoolExecutor) {

		this.collector = collector;

		// 核心线程池
		this.asyncThreadPoolExecutor = asyncThreadPoolExecutor;
		this.asyncProperties = asyncProperties;
		// 监控线程池
		this.monitorThreadPoolExecutor = monitorThreadPoolExecutor;
		this.monitorThreadPoolProperties = monitorThreadPoolProperties;

		if (Objects.nonNull(this.monitorThreadPoolExecutor)) {
			call(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY + ".active.count")
				.set(this.monitorThreadPoolExecutor::getActiveCount);
			call(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY + ".core.poolSize")
				.set(this.monitorThreadPoolExecutor::getCorePoolSize);
			call(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY + ".poolSize.largest")
				.set(this.monitorThreadPoolExecutor::getLargestPoolSize);
			call(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY + ".poolSize.max")
				.set(this.monitorThreadPoolExecutor::getMaximumPoolSize);
			call(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY + ".poolSize.count")
				.set(this.monitorThreadPoolExecutor::getPoolSize);
			call(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY + ".queue.size")
				.set(() -> this.monitorThreadPoolExecutor.getQueue().size());
			call(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY + ".task.count")
				.set(this.monitorThreadPoolExecutor::getTaskCount);
			call(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_CALL_KEY + ".task.completed")
				.set(this.monitorThreadPoolExecutor::getCompletedTaskCount);
		}

		if (Objects.nonNull(this.asyncThreadPoolExecutor)) {
			call(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY + ".active.count")
				.set(this.asyncThreadPoolExecutor::getActiveCount);
			call(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY + ".core.poolSize")
				.set(this.asyncThreadPoolExecutor::getCorePoolSize);
			call(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY + ".poolSize.largest")
				.set(() -> this.asyncThreadPoolExecutor.getThreadPoolExecutor()
					.getLargestPoolSize());
			call(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY + ".poolSize.max").set(
				() -> this.asyncThreadPoolExecutor.getThreadPoolExecutor().getMaximumPoolSize());
			call(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY + ".poolSize.count")
				.set(this.asyncThreadPoolExecutor::getPoolSize);
			call(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY + ".queue.size")
				.set(() -> this.asyncThreadPoolExecutor.getThreadPoolExecutor().getQueue().size());
			call(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY + ".task.count")
				.set(() -> this.asyncThreadPoolExecutor.getThreadPoolExecutor().getTaskCount());
			call(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_CALL_KEY + ".task.completed").set(
				() -> this.asyncThreadPoolExecutor.getThreadPoolExecutor().getCompletedTaskCount());
		}

		//JVM 停止或重启时，关闭线程池释
		ProcessExitEvent.register(() -> {
			try {
				monitorShutdown();
				asyncShutdown();
			} catch (Exception e) {
				LogUtils.error(e, "关闭SystemThreadPool时出错");
			}
		}, Integer.MAX_VALUE, false);
	}

	public Collector.Call call(String key) {
		return collector.call(key);
	}

	public Collector.Hook monitorHook() {
		return collector.hook(TAOTAO_CLOUD_COLLECTOR_MONITOR_EXECUTOR_HOOK);
	}

	public Collector.Hook asyncHook() {
		return collector.hook(TAOTAO_CLOUD_COLLECTOR_ASYNC_EXECUTOR_HOOK);
	}

	private void monitorThreadPoolCheckHealth() {
		if (monitorThreadPoolProperties.isCheckHealth()
			&& monitorThreadPoolExecutor.getMaximumPoolSize()
			<= monitorThreadPoolExecutor.getPoolSize()
			&& monitorThreadPoolExecutor.getQueue().size() > 0) {
			LogUtils.warn(
				"监控线程池已满 任务开始出现排队 请修改配置 [taotao.cloud.core.threadpool.monitor.maximumPoolSize] 当前活动线程数: {}"
				, monitorThreadPoolExecutor.getActiveCount());
		}
	}

	private void coreThreadPoolCheckHealth() {
		if (asyncProperties.isCheckHealth()
			&& asyncThreadPoolExecutor.getMaxPoolSize() <= asyncThreadPoolExecutor.getPoolSize()
			&& asyncThreadPoolExecutor.getThreadPoolExecutor().getQueue().size() > 0) {
			LogUtils.warn(
				"核心线程池已满 任务开始出现排队 请修改配置 [taotao.cloud.core.threadpool.async.threadPoolMaxSiz] 当前活动线程数: {}"
				, asyncThreadPoolExecutor.getActiveCount());
		}
	}

	public <T> Future<T> monitorSubmit(String taskName, Callable<T> task) {
		monitorThreadPoolCheckHealth();

		return monitorHook().run(taskName, () -> monitorThreadPoolExecutor.submit(task));
	}

	public <T> Future<T> asyncSubmit(String taskName, Callable<T> task) {
		if (Objects.isNull(asyncThreadPoolExecutor)) {
			LogUtils.warn("核心线程池未初始化");
			return null;
		}

		coreThreadPoolCheckHealth();
		return asyncHook().run(taskName, () -> asyncThreadPoolExecutor.submit(task));
	}

	public void monitorSubmit(String taskName, Runnable task) {
		monitorThreadPoolCheckHealth();
		monitorHook().run(taskName, () -> monitorThreadPoolExecutor.submit(task));
	}

	public Future<?> asyncSubmit(String taskName, Runnable task) {
		if (Objects.isNull(asyncThreadPoolExecutor)) {
			LogUtils.warn("核心线程池未初始化");
			return null;
		}

		coreThreadPoolCheckHealth();
		return asyncHook().run(taskName, () -> asyncThreadPoolExecutor.submit(task));
	}

	public boolean monitorIsShutdown() {
		return monitorThreadPoolExecutor.isShutdown();
	}

	public boolean coreIsShutdown() {
		if (Objects.isNull(asyncThreadPoolExecutor)) {
			LogUtils.warn("核心线程池未初始化");
			return true;
		}

		return asyncThreadPoolExecutor.getThreadPoolExecutor().isShutdown();
	}

	public void monitorShutdown() {
		monitorThreadPoolExecutor.shutdown();
	}

	public void asyncShutdown() {
		if (Objects.nonNull(asyncThreadPoolExecutor)) {
			asyncThreadPoolExecutor.shutdown();
		}
	}

	/**
	 * 任务拆分多个小任务分批并行处理，并行处理完一批再并行处理下一批。 在抛出错误的时候有问题，未修复，仅试验，不要用这个方法。
	 *
	 * @param taskName      taskName
	 * @param parallelCount parallelCount
	 * @param array         array
	 * @param action        action
	 * @since 2021-09-02 20:48:09
	 */
	public <T> void monitorParallelFor(String taskName, int parallelCount, List<T> array,
		final Action1<T> action) {
		monitorThreadPoolCheckHealth();

		monitorHook().run(taskName, () -> {
			int parallelCount2 = parallelCount;
			if (parallelCount2 > array.size()) {
				parallelCount2 = array.size();
			}

			//任务队列
			Queue<T> queueTasks = new LinkedList<>(array);

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
						} finally {
							latch.countDown();
						}
					});
					result.add(future);
				}

				try {
					latch.await();
				} catch (InterruptedException exp) {
					LogUtils.error(exp, "parallelFor 任务计数异常");
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
	 * @since 2021-09-02 20:48:25
	 */
	public <T> void monitorParallelFor2(String taskName, int parallelCount, Collection<T> array,
		final Action1<T> action) {
		monitorThreadPoolCheckHealth();
		monitorHook().run(taskName, () -> {
			int parallelCount2 = parallelCount;
			if (parallelCount2 > array.size()) {
				parallelCount2 = array.size();
			}
			//任务队列
			Queue<T> queueTasks = new LinkedList<>(array);

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
					LogUtils.error(exp, "parallelFor 任务计数异常");
				}
				if (!exceptionRef.isNull()) {
					throw new BaseException("parallelFor 并行执行出错", exceptionRef.getData());
				}
			}
			return 1;
		});
	}

	public ThreadPoolExecutor getMonitorThreadPoolExecutor() {
		return monitorThreadPoolExecutor;
	}

	public void setMonitorThreadPoolExecutor(
		ThreadPoolExecutor monitorThreadPoolExecutor) {
		this.monitorThreadPoolExecutor = monitorThreadPoolExecutor;
	}

	public ThreadPoolTaskExecutor getAsyncThreadPoolExecutor() {
		return asyncThreadPoolExecutor;
	}

	public void setAsyncThreadPoolExecutor(
		ThreadPoolTaskExecutor asyncThreadPoolExecutor) {
		this.asyncThreadPoolExecutor = asyncThreadPoolExecutor;
	}

	public MonitorThreadPoolProperties getMonitorThreadPoolProperties() {
		return monitorThreadPoolProperties;
	}

	public void setMonitorThreadPoolProperties(
		MonitorThreadPoolProperties monitorThreadPoolProperties) {
		this.monitorThreadPoolProperties = monitorThreadPoolProperties;
	}

	public AsyncProperties getAsyncThreadPoolProperties() {
		return asyncProperties;
	}

	public void setAsyncThreadPoolProperties(
		AsyncProperties asyncProperties) {
		this.asyncProperties = asyncProperties;
	}

	public Collector getCollector() {
		return collector;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}
}
