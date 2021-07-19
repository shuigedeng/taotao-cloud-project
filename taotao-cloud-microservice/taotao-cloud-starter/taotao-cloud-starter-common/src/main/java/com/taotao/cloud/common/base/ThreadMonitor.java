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

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程监控
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/6/22 17:14
 **/
public class ThreadMonitor {

	private ThreadPoolExecutor threadPoolExecutor;
	private String name;

	//="bsf.threadPool.system";
	public ThreadMonitor(String name, ThreadPoolExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
		this.name = name;
		Collector.Default.call(name + ".active.count").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getActiveCount());
		Collector.Default.call(name + ".core.poolSize").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getCorePoolSize());
		Collector.Default.call(name + ".poolSize.largest")
			.set(() -> threadPoolExecutor == null ? 0 : threadPoolExecutor.getLargestPoolSize());
		Collector.Default.call(name + ".poolSize.max").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getMaximumPoolSize());
		Collector.Default.call(name + ".poolSize.count").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getPoolSize());
		Collector.Default.call(name + ".queue.size").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getQueue().size());
		Collector.Default.call(name + ".task.count").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getTaskCount());
		Collector.Default.call(name + ".task.completed").set(() ->
			threadPoolExecutor == null ? 0 : threadPoolExecutor.getCompletedTaskCount());
	}

	public Collector.Hook hook() {
		return Collector.Default.hook(name + ".hook");
	}
}
