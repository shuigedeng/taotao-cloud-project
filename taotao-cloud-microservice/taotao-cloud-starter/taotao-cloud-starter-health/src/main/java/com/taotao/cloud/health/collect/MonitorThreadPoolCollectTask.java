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


import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.Collector.Hook;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;

/**
 * MonitorThreadPoolCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:14:28
 */
public class MonitorThreadPoolCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;
	private Collector collector;

	public MonitorThreadPoolCollectTask(Collector collector, CollectTaskProperties properties) {
		this.collector = collector;
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getThreadPollTimeSpan();
	}

	@Override
	public String getDesc() {
		return "监控线程池采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.threadpool.info";
	}

	@Override
	public boolean getEnabled() {
		return properties.isThreadPollEnabled();
	}

	@Override
	protected Object getData() {
		try {
			//if (ContextUtil.getBean(
			//	ReflectionUtil.classForName("com.taotao.cloud.core.monitor.MonitorSystem"), false)
			//	== null) {
			//	return null;
			//}

			MonitorThreadPoolInfo info = new MonitorThreadPoolInfo();
			info.systemActiveCount =
				(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.active.count")
					.run();
			info.systemCorePoolSize =
				(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.core.poolSize")
					.run();
			info.systemPoolSizeLargest =
				(Integer) this.collector.call(
						"taotao.cloud.core.monitor.threadpool.poolSize.largest")
					.run();
			info.systemPoolSizeMax =
				(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.poolSize.max")
					.run();
			info.systemPoolSizeCount =
				(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.poolSize.count")
					.run();
			info.systemQueueSize =
				(Integer) this.collector.call("taotao.cloud.core.monitor.threadpool.queue.size")
					.run();
			info.systemTaskCount =
				(Long) this.collector.call("taotao.cloud.core.monitor.threadpool.task.count")
					.run();
			info.systemTaskCompleted =
				(Long) this.collector.call("taotao.cloud.core.monitor.threadpool.task.completed")
					.run();

			Hook hook = this.collector.hook("taotao.cloud.core.monitor.threadpool.hook");
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


	private static class MonitorThreadPoolInfo {

		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.active.count", desc = "系统线程池活动线程数")
		private Integer systemActiveCount;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.core.poolSize", desc = "系统线程池核心线程数")
		private Integer systemCorePoolSize;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.poolSize.largest", desc = "线程池历史最大线程数")
		private Integer systemPoolSizeLargest;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.poolSize.max", desc = "线程池最大线程数")
		private Integer systemPoolSizeMax;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.poolSize.count", desc = "线程池当前线程数")
		private Integer systemPoolSizeCount;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.queue.size", desc = "线程池当前排队等待任务数")
		private Integer systemQueueSize;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.count", desc = "线程池历史任务数")
		private Long systemTaskCount;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.completed", desc = "线程池已完成任务数")
		private Long systemTaskCompleted;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.error", desc = "线程池拦截上一次每秒出错次数")
		private Long systemTaskHookError;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.success", desc = "线程池拦截上一次每秒成功次数")
		private Long systemTaskHookSuccess;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.current", desc = "线程池拦截当前执行任务数")
		private Long systemTaskHookCurrent;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.list.detail", desc = "线程池拦截历史最大耗时任务列表")
		private String systemTaskHookList;
		@FieldReport(name = "taotao.cloud.health.collect.threadPool.system.task.hook.list.minute.detail", desc = "线程池拦截历史最大耗时任务列表(每分钟)")
		private String systemTaskHookListPerMinute;
	}
}
