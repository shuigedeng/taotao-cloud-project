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
package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.Collector.Hook;
import com.taotao.cloud.core.properties.AsyncThreadPoolProperties;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.Objects;

/**
 * MonitorThreadPoolCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:14:28
 */
public class AsyncThreadPoolCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.executor.async";

	private final CollectTaskProperties properties;

	public AsyncThreadPoolCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getAsyncThreadTimeSpan();
	}

	@Override
	public String getDesc() {
		return getClass().getName();
	}

	@Override
	public String getName() {
		return TASK_NAME;
	}

	@Override
	public boolean getEnabled() {
		return properties.getAsyncThreadEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			Collector collector = Collector.getCollector();
			AsyncThreadPoolProperties asyncThreadPoolProperties = ContextUtil.getBean(
				AsyncThreadPoolProperties.class, true);

			if (Objects.nonNull(collector) && Objects.nonNull(asyncThreadPoolProperties)) {

				String asyncThreadName = asyncThreadPoolProperties.
					getThreadNamePrefix().replace("-", ".");

				AsyncExecutorCollectInfo info = new AsyncExecutorCollectInfo();
				info.asyncExecutorActiveCount =
					(Integer) collector.call(asyncThreadName + ".active.count").run();
				info.asyncExecutorCorePoolSize =
					(Integer) collector.call(asyncThreadName + ".core.poolSize").run();
				info.asyncExecutorPoolSizeLargest =
					(Integer) collector.call(asyncThreadName + ".poolSize.largest").run();
				info.asyncExecutorPoolSizeMax =
					(Integer) collector.call(asyncThreadName + ".poolSize.max").run();
				info.asyncExecutorPoolSizeCount =
					(Integer) collector.call(asyncThreadName + ".poolSize.count").run();
				info.asyncExecutorQueueSize =
					(Integer) collector.call(asyncThreadName + ".queue.size").run();
				info.asyncExecutorTaskCount =
					(Long) collector.call(asyncThreadName + ".task.count").run();
				info.asyncExecutorTaskCompleted =
					(Long) collector.call(asyncThreadName + ".task.completed").run();

				Hook hook = collector.hook(asyncThreadName + ".hook");
				info.asyncExecutorTaskHookCurrent = hook.getCurrent();
				info.asyncExecutorTaskHookError = hook.getLastErrorPerSecond();
				info.asyncExecutorTaskHookSuccess = hook.getLastSuccessPerSecond();
				info.asyncExecutorTaskHookList = hook.getMaxTimeSpanList().toText();
				info.asyncExecutorTaskHookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
				return info;
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}

	private static class AsyncExecutorCollectInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".active.count", desc = "异步核心线程池活动线程数")
		private Integer asyncExecutorActiveCount = 0;
		@FieldReport(name = TASK_NAME + ".core.poolSize", desc = "异步核心线程池核心线程数")
		private Integer asyncExecutorCorePoolSize = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.largest", desc = "异步核心线程池历史最大线程数")
		private Integer asyncExecutorPoolSizeLargest = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.max", desc = "异步核心线程池最大线程数")
		private Integer asyncExecutorPoolSizeMax = 0;
		@FieldReport(name = TASK_NAME + ".poolSize.count", desc = "异步核心线程池当前线程数")
		private Integer asyncExecutorPoolSizeCount = 0;
		@FieldReport(name = TASK_NAME + ".queue.size", desc = "异步核心线程池当前排队等待任务数")
		private Integer asyncExecutorQueueSize = 0;
		@FieldReport(name = TASK_NAME + ".task.count", desc = "异步核心线程池历史任务数")
		private Long asyncExecutorTaskCount = 0L;
		@FieldReport(name = TASK_NAME + ".task.completed", desc = "异步核心线程池已完成任务数")
		private Long asyncExecutorTaskCompleted = 0L;
		@FieldReport(name = TASK_NAME + ".task.hook.error", desc = "异步核心线程池拦截上一次每秒出错次数")
		private Long asyncExecutorTaskHookError = 0L;
		@FieldReport(name = TASK_NAME + ".task.hook.success", desc = "异步核心线程池拦截上一次每秒成功次数")
		private Long asyncExecutorTaskHookSuccess = 0L;
		@FieldReport(name = TASK_NAME + ".task.hook.current", desc = "异步核心线程池拦截当前执行任务数")
		private Long asyncExecutorTaskHookCurrent = 0L;
		@FieldReport(name = TASK_NAME + ".task.hook.list.detail", desc = "异步核心线程池拦截历史最大耗时任务列表")
		private String asyncExecutorTaskHookList = "";
		@FieldReport(name = TASK_NAME + ".task.hook.list.minute.detail", desc = "异步核心线程池拦截历史最大耗时任务列表(每分钟)")
		private String asyncExecutorTaskHookListPerMinute = "";
	}
}
