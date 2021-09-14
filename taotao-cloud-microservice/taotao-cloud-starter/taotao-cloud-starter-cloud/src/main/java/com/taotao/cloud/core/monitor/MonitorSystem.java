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

import com.taotao.cloud.core.model.Collector;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ThreadMonitor
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:46:01
 */
public class MonitorSystem {

	/**
	 * monitorThreadPoolExecutor
	 */
	private ThreadPoolExecutor monitorThreadPoolExecutor;
	/**
	 * coreThreadPoolExecutor
	 */
	private ThreadPoolTaskExecutor coreThreadPoolExecutor;
	/**
	 * monitorThreadName
	 */
	private final String monitorThreadName;
	/**
	 * coreThreadName
	 */
	private final String coreThreadName;
	/**
	 * collector
	 */
	private Collector collector;

	public MonitorSystem(
		Collector collector,
		String monitorThreadName,
		String coreThreadName,
		ThreadPoolExecutor monitorThreadPoolExecutor,
		ThreadPoolTaskExecutor coreThreadPoolExecutor) {
		this.collector = collector;
		this.monitorThreadPoolExecutor = monitorThreadPoolExecutor;
		this.monitorThreadName = monitorThreadName.replace("-", ".");

		this.coreThreadPoolExecutor = coreThreadPoolExecutor;
		this.coreThreadName = coreThreadName.replace("-", ".");

		if (Objects.nonNull(this.monitorThreadPoolExecutor)) {
			collector.call(this.monitorThreadName + ".active.count").set(this.monitorThreadPoolExecutor::getActiveCount);
			collector.call(this.monitorThreadName + ".core.poolSize").set(this.monitorThreadPoolExecutor::getCorePoolSize);
			collector.call(this.monitorThreadName + ".poolSize.largest").set(this.monitorThreadPoolExecutor::getLargestPoolSize);
			collector.call(this.monitorThreadName + ".poolSize.max").set(this.monitorThreadPoolExecutor::getMaximumPoolSize);
			collector.call(this.monitorThreadName + ".poolSize.count").set(this.monitorThreadPoolExecutor::getPoolSize);
			collector.call(this.monitorThreadName + ".queue.size").set(() -> this.monitorThreadPoolExecutor.getQueue().size());
			collector.call(this.monitorThreadName + ".task.count").set(this.monitorThreadPoolExecutor::getTaskCount);
			collector.call(this.monitorThreadName + ".task.completed").set(this.monitorThreadPoolExecutor::getCompletedTaskCount);
		}

		if (Objects.nonNull(this.coreThreadPoolExecutor)) {
			collector.call(this.coreThreadName + ".active.count").set(this.coreThreadPoolExecutor::getActiveCount);
			collector.call(this.coreThreadName + ".core.poolSize").set(this.coreThreadPoolExecutor::getCorePoolSize);
			collector.call(this.coreThreadName + ".poolSize.largest").set(() -> this.coreThreadPoolExecutor.getThreadPoolExecutor().getLargestPoolSize());
			collector.call(this.coreThreadName + ".poolSize.max").set(() -> this.coreThreadPoolExecutor.getThreadPoolExecutor().getMaximumPoolSize());
			collector.call(this.coreThreadName + ".poolSize.count").set(this.coreThreadPoolExecutor::getPoolSize);
			collector.call(this.coreThreadName + ".queue.size").set(() -> this.coreThreadPoolExecutor.getThreadPoolExecutor().getQueue().size());
			collector.call(this.coreThreadName + ".task.count").set(() -> this.coreThreadPoolExecutor.getThreadPoolExecutor().getTaskCount());
			collector.call(this.coreThreadName + ".task.completed").set(() -> this.coreThreadPoolExecutor.getThreadPoolExecutor().getCompletedTaskCount());
		}
	}

	/**
	 * hook
	 *
	 * @return {@link com.taotao.cloud.core.model.Collector.Hook }
	 * @author shuigedeng
	 * @since 2021-09-02 20:46:19
	 */
	public Collector.Hook monitorHook() {
		return collector.hook(monitorThreadName + ".hook");
	}

	/**
	 * hook
	 *
	 * @return {@link com.taotao.cloud.core.model.Collector.Hook }
	 * @author shuigedeng
	 * @since 2021-09-02 20:46:19
	 */
	public Collector.Hook coreHook() {
		return collector.hook(coreThreadName + ".hook");
	}

	public ThreadPoolExecutor getMonitorThreadPoolExecutor() {
		return monitorThreadPoolExecutor;
	}

	public void setMonitorThreadPoolExecutor(ThreadPoolExecutor monitorThreadPoolExecutor) {
		this.monitorThreadPoolExecutor = monitorThreadPoolExecutor;
	}

	public ThreadPoolTaskExecutor getCoreThreadPoolExecutor() {
		return coreThreadPoolExecutor;
	}

	public void setCoreThreadPoolExecutor(
		ThreadPoolTaskExecutor coreThreadPoolExecutor) {
		this.coreThreadPoolExecutor = coreThreadPoolExecutor;
	}

	public String getMonitorThreadName() {
		return monitorThreadName;
	}

	public String getCoreThreadName() {
		return coreThreadName;
	}

	public Collector getCollector() {
		return collector;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}
}
