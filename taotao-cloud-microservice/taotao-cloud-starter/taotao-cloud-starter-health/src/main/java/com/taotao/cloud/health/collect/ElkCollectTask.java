/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import com.taotao.cloud.common.utils.reflect.ReflectionUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.model.CollectInfo;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.Objects;

/**
 * ELK性能采集
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:38:09
 */
public class ElkCollectTask extends AbstractCollectTask {

	private final static String CLASS = "net.logstash.logback.appender.LogstashTcpSocketAppender";

	private static final String TASK_NAME = "taotao.cloud.health.collect.elk";

	private final CollectTaskProperties properties;

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
			ElkInfo info = new ElkInfo();
			Object appender = ContextUtil.getBean(ReflectionUtil.tryClassForName(CLASS), true);
			if (Objects.nonNull(appender)) {
				info.queueSize = ReflectionUtil.tryGetValue(appender, "getQueueSize");
				info.consecutiveDropped = ReflectionUtil.tryGetValue(appender, "consecutiveDroppedCount.get");
				info.getDroppedWarnFrequency = ReflectionUtil.tryGetValue(appender, "getDroppedWarnFrequency");
				info.getKeepAliveDuration = ReflectionUtil.tryGetValue(appender, "getKeepAliveDuration.getMilliseconds");
				info.getProducerType = ReflectionUtil.tryGetValue(appender, "getProducerType.name");
				info.getReconnectionDelay = ReflectionUtil.tryGetValue(appender, "getReconnectionDelay.getMilliseconds");
				info.getRingBufferSize = ReflectionUtil.tryGetValue(appender, "getRingBufferSize");
				info.getSecondaryConnectionTTL = ReflectionUtil.tryGetValue(appender, "getSecondaryConnectionTTL.getMilliseconds");
				info.getWriteTimeout = ReflectionUtil.tryGetValue(appender, "getWriteTimeout.getMilliseconds");
				info.getWriteBufferSize = ReflectionUtil.tryGetValue(appender, "getWriteBufferSize");
				return info;
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}

	private static class ElkInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".queue.size", desc = "ELK消息队列大小")
		private Integer queueSize = 0;
		@FieldReport(name = TASK_NAME + ".consecutive.dropped", desc = "ELK消息连续丢弃数量")
		private Long consecutiveDropped = 0L;
		@FieldReport(name = TASK_NAME + ".dropped.warn.frequency", desc = "ELK下降警告频率")
		private Integer getDroppedWarnFrequency = 0;
		@FieldReport(name = TASK_NAME + ".keep.alive.duration", desc = "ELK保持活动持续时间")
		private Long getKeepAliveDuration = 0L;
		@FieldReport(name = TASK_NAME + ".producer.type", desc = "ELK生产者类型")
		private String getProducerType = "";
		@FieldReport(name = TASK_NAME + ".reconnection.delay", desc = "ELK在与目标的连接失败后，在尝试重新连接到该目标之前等待的时间段")
		private Long getReconnectionDelay = 0L;
		@FieldReport(name = TASK_NAME + ".ring.buffer.size", desc = "ELK RingBuffer大小")
		private Integer getRingBufferSize = 0;
		@FieldReport(name = TASK_NAME + ".secondary.connection.ttL", desc = "ELK辅助连接 TTL大小")
		private Long getSecondaryConnectionTTL = 0L;
		@FieldReport(name = TASK_NAME + ".write.timeout", desc = "ELK在超时之前等待写入完成的时间段.并尝试重新连接到该目的地。")
		private Long getWriteTimeout = 0L;
		@FieldReport(name = TASK_NAME + ".write.buffer.size", desc = "ELK消写入缓冲区中可用的字节数")
		private Integer getWriteBufferSize = 0;
	}
}
