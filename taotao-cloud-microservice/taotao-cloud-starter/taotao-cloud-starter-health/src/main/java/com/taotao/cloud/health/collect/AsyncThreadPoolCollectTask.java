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
package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.Collector.Hook;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;

/**
 * MonitorThreadPoolCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:14:28
 */
public class AsyncThreadPoolCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.executor.async";

	private CollectTaskProperties properties;
	private Collector collector;

	public AsyncThreadPoolCollectTask(Collector collector, CollectTaskProperties properties) {
		this.collector = collector;
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getAsyncThreadTimeSpan();
	}

	@Override
	public String getDesc() {
		return this.getClass().getName();
	}

	@Override
	public String getName() {
		return TASK_NAME;
	}

	@Override
	public boolean getEnabled() {
		return properties.isAsyncThreadEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			AsyncThreadPoolProperties asyncThreadPoolProperties = ContextUtil.getBean(
				AsyncThreadPoolProperties.class, false);
			String threadNamePrefix = asyncThreadPoolProperties.getThreadNamePrefix();
			String asyncThreadName = threadNamePrefix.replace("-", ".");

			AsyncThreadPoolInfo info = new AsyncThreadPoolInfo();
			info.systemActiveCount =
				(Integer) this.collector.call(asyncThreadName + ".active.count").run();
			info.systemCorePoolSize =
				(Integer) this.collector.call(asyncThreadName + ".core.poolSize").run();
			info.systemPoolSizeLargest =
				(Integer) this.collector.call(asyncThreadName + ".poolSize.largest").run();
			info.systemPoolSizeMax =
				(Integer) this.collector.call(asyncThreadName + ".poolSize.max").run();
			info.systemPoolSizeCount =
				(Integer) this.collector.call(asyncThreadName + ".poolSize.count").run();
			info.systemQueueSize =
				(Integer) this.collector.call(asyncThreadName + ".queue.size").run();
			info.systemTaskCount =
				(Long) this.collector.call(asyncThreadName + ".task.count").run();
			info.systemTaskCompleted =
				(Long) this.collector.call(asyncThreadName + ".task.completed").run();

			Hook hook = this.collector.hook(asyncThreadName + ".hook");
			info.systemTaskHookCurrent = hook.getCurrent();
			info.systemTaskHookError = hook.getLastErrorPerSecond();
			info.systemTaskHookSuccess = hook.getLastSuccessPerSecond();
			info.systemTaskHookList = hook.getMaxTimeSpanList().toText();
			info.systemTaskHookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
			return info;
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}


	private static class AsyncThreadPoolInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".active.count", desc = "系统线程池活动线程数")
		private Integer systemActiveCount = 0;
		@FieldReport(name = TASK_NAME + ".core.poolSize", desc = "系统线程池核心线程数")
		private Integer systemCorePoolSize = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.largest", desc = "线程池历史最大线程数")
		private Integer systemPoolSizeLargest = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.max", desc = "线程池最大线程数")
		private Integer systemPoolSizeMax = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.count", desc = "线程池当前线程数")
		private Integer systemPoolSizeCount = 0;
		@FieldReport(name = TASK_NAME + ".queue.size", desc = "线程池当前排队等待任务数")
		private Integer systemQueueSize = 0;
		@FieldReport(name = TASK_NAME + ".task.count", desc = "线程池历史任务数")
		private Long systemTaskCount = 0L;
		@FieldReport(name = TASK_NAME + ".task.completed", desc = "线程池已完成任务数")
		private Long systemTaskCompleted = 0L;
		@FieldReport(name = TASK_NAME + ".task.hook.error", desc = "线程池拦截上一次每秒出错次数")
		private Long systemTaskHookError = 0L;
		@FieldReport(name = TASK_NAME + ".task.hook.success", desc = "线程池拦截上一次每秒成功次数")
		private Long systemTaskHookSuccess = 0L;
		@FieldReport(name = TASK_NAME + ".task.hook.current", desc = "线程池拦截当前执行任务数")
		private Long systemTaskHookCurrent = 0L;
		@FieldReport(name = TASK_NAME + ".task.hook.list.detail", desc = "线程池拦截历史最大耗时任务列表")
		private String systemTaskHookList = "";
		@FieldReport(name = TASK_NAME
			+ ".task.hook.list.minute.detail", desc = "线程池拦截历史最大耗时任务列表(每分钟)")
		private String systemTaskHookListPerMinute = "";
	}
}
