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


import com.taotao.cloud.core.model.Collector;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ThreadMonitor
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:46:01
 */
public class ThreadMonitor {

	/**
	 * threadPoolExecutor
	 */
	private ThreadPoolExecutor threadPoolExecutor;

	/**
	 * name
	 */
	private final String name;

	public ThreadMonitor(String name, ThreadPoolExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
		this.name = name;

		Collector.DEFAULT.call(name + ".active.count").set(() ->
			threadPoolExecutor == null ? 0 : this.threadPoolExecutor.getActiveCount());
		Collector.DEFAULT.call(name + ".core.poolSize").set(() ->
			threadPoolExecutor == null ? 0 : this.threadPoolExecutor.getCorePoolSize());
		Collector.DEFAULT.call(name + ".poolSize.largest").set(() ->
			threadPoolExecutor == null ? 0 : this.threadPoolExecutor.getLargestPoolSize());
		Collector.DEFAULT.call(name + ".poolSize.max").set(() ->
			threadPoolExecutor == null ? 0 : this.threadPoolExecutor.getMaximumPoolSize());
		Collector.DEFAULT.call(name + ".poolSize.count").set(() ->
			threadPoolExecutor == null ? 0 : this.threadPoolExecutor.getPoolSize());
		Collector.DEFAULT.call(name + ".queue.size").set(() ->
			threadPoolExecutor == null ? 0 : this.threadPoolExecutor.getQueue().size());
		Collector.DEFAULT.call(name + ".task.count").set(() ->
			threadPoolExecutor == null ? 0 : this.threadPoolExecutor.getTaskCount());
		Collector.DEFAULT.call(name + ".task.completed").set(() ->
			threadPoolExecutor == null ? 0 : this.threadPoolExecutor.getCompletedTaskCount());
	}

	/**
	 * hook
	 *
	 * @return {@link com.taotao.cloud.core.model.Collector.Hook }
	 * @author shuigedeng
	 * @since 2021-09-02 20:46:19
	 */
	public Collector.Hook hook() {
		return Collector.DEFAULT.hook(name + ".hook");
	}
}
