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
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;

/**
 * ELK性能采集
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:38:09
 */
public class ElkCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.elk";

	private CollectTaskProperties properties;

	public ElkCollectTask(CollectTaskProperties properties) {
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getElkTimeSpan();
	}

	@Override
	public boolean getEnabled() {
		return properties.isElkEnabled();
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
	protected CollectInfo getData() {
		try {
			Object appender = ContextUtil.getBean(ReflectionUtil.tryClassForName(
				"net.logstash.logback.appender.LogstashTcpSocketAppender"), false);
			if (appender == null) {
				return null;
			}

			ElkInfo data = new ElkInfo();
			data.queueSize = ReflectionUtil.tryGetValue(appender, "getQueueSize");
			data.consecutiveDropped = ReflectionUtil.tryGetValue(appender,
				"consecutiveDroppedCount.get");
			return data;
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}

	private static class ElkInfo implements CollectInfo{

		@FieldReport(name = TASK_NAME + ".queue.size", desc = "ELK消息队列大小")
		private Integer queueSize;
		@FieldReport(name = TASK_NAME + ".consecutiveDropped", desc = "ELK消息连续丢弃数量")
		private Long consecutiveDropped;
	}
}
