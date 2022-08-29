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


import ch.qos.logback.core.util.Duration;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.reflect.ReflectionUtils;
import com.taotao.cloud.monitor.annotation.FieldReport;
import com.taotao.cloud.monitor.collect.AbstractCollectTask;
import com.taotao.cloud.monitor.collect.CollectInfo;
import com.taotao.cloud.monitor.properties.CollectTaskProperties;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.appender.destination.PreferPrimaryDestinationConnectionStrategy;

import java.util.Objects;

/**
 * ELK性能采集
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:38:09
 */
public class ElkCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.monitor.collect.elk";

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
			LogstashTcpSocketAppender appender = ContextUtils.getBean(LogstashTcpSocketAppender.class, true);
			if (Objects.nonNull(appender)) {
				info.ringBufferSize = appender.getRingBufferSize();
				info.consecutiveDropped = ReflectionUtils.tryGetValue(appender, "consecutiveDroppedCount.get");
				info.droppedWarnFrequency = appender.getDroppedWarnFrequency();
				info.keepAliveDuration = appender.getKeepAliveDuration().getMilliseconds();
				info.producerType = appender.getProducerType().name();
				info.reconnectionDelay = appender.getReconnectionDelay().getMilliseconds();

				Duration duration = appender.getConnectionStrategy() instanceof PreferPrimaryDestinationConnectionStrategy ? ((PreferPrimaryDestinationConnectionStrategy) appender.getConnectionStrategy()).getSecondaryConnectionTTL() : null;
				if(Objects.nonNull(duration)){
					info.secondaryConnectionTtl = duration.getMilliseconds();
				}

				info.writeTimeout = appender.getWriteTimeout().getMilliseconds();
				info.writeBufferSize = appender.getWriteBufferSize();
				return info;
			}
		} catch (Exception e) {
			if(LogUtils.isErrorEnabled()){
				LogUtils.error(e);
			}
		}
		return null;
	}

	private static class ElkInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".ring.buffer.size", desc = "ELK消息队列大小")
		private Integer ringBufferSize = 0;
		@FieldReport(name = TASK_NAME + ".consecutive.dropped.count", desc = "ELK消息连续丢弃数量")
		private Long consecutiveDropped = 0L;
		@FieldReport(name = TASK_NAME + ".dropped.warn.frequency", desc = "ELK下降警告频率")
		private Integer droppedWarnFrequency = 0;
		@FieldReport(name = TASK_NAME + ".keep.alive.duration", desc = "ELK保持活动持续时间")
		private Long keepAliveDuration = 0L;
		@FieldReport(name = TASK_NAME + ".producer.type", desc = "ELK生产者类型")
		private String producerType = "";
		@FieldReport(name = TASK_NAME + ".reconnection.delay", desc = "ELK在与目标的连接失败后，在尝试重新连接到该目标之前等待的时间段")
		private Long reconnectionDelay = 0L;
		@FieldReport(name = TASK_NAME + ".secondary.connection.ttl", desc = "ELK辅助连接 TTL大小")
		private Long secondaryConnectionTtl = 0L;
		@FieldReport(name = TASK_NAME + ".write.timeout", desc = "ELK在超时之前等待写入完成的时间段.并尝试重新连接到该目的地。")
		private Long writeTimeout = 0L;
		@FieldReport(name = TASK_NAME + ".write.buffer.size", desc = "ELK消写入缓冲区中可用的字节数")
		private Integer writeBufferSize = 0;
	}
}
