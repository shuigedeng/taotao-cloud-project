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
package com.taotao.cloud.monitor.collect.task;


import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.model.Collector.Hook;
import com.taotao.cloud.monitor.annotation.FieldReport;
import com.taotao.cloud.monitor.collect.AbstractCollectTask;
import com.taotao.cloud.monitor.collect.CollectInfo;
import com.taotao.cloud.monitor.properties.CollectTaskProperties;

import java.util.Objects;

/**
 * MybatisCollectTask
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 19:15:21
 */
public class MybatisCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.monitor.collect.mybatis";

	private final CollectTaskProperties properties;

	public MybatisCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getMybatisTimeSpan();
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
		return properties.isMybatisEnabled();
	}

	@Override
	protected CollectInfo getData() {
		try {
			SqlMybatisInfo info = new SqlMybatisInfo();

			Collector collector = Collector.getCollector();
			if (Objects.nonNull(collector)) {
				Hook hook = collector.hook("taotao.cloud.monitor.mybatis.sql.hook");
				info.hookCurrent = hook.getCurrent();
				info.hookError = hook.getLastErrorPerSecond();
				info.hookSuccess = hook.getLastSuccessPerSecond();
				info.hookList = hook.getMaxTimeSpanList().toText();
				info.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
				return info;
			}
		} catch (Exception e) {
			if(LogUtils.isErrorEnabled()){
				LogUtils.error(e);
			}
		}
		return null;
	}

	private static class SqlMybatisInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".sql.hook.error", desc = "mybatis 拦截上一次每秒出错次数")
		private Long hookError = 0L;
		@FieldReport(name = TASK_NAME + ".sql.hook.success", desc = "mybatis 拦截上一次每秒成功次数")
		private Long hookSuccess = 0L;
		@FieldReport(name = TASK_NAME + ".sql.hook.current", desc = "mybatis 拦截当前执行任务数")
		private Long hookCurrent = 0L;
		@FieldReport(name = TASK_NAME + ".sql.hook.list.detail", desc = "mybatis 拦截历史最大耗时任务列表")
		private String hookList = "";
		@FieldReport(name = TASK_NAME
			+ ".sql.hook.list.minute.detail", desc = "mybatis 拦截历史最大耗时任务列表(每分钟)")
		private String hookListPerMinute = "";

	}
}
