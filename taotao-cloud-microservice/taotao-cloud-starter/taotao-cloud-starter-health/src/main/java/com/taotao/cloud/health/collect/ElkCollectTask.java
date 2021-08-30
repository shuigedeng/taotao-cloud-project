package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.model.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;

/**
 * @author Huang Zhaoping
 */
public class ElkCollectTask extends AbstractCollectTask {

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
		return "ELK性能采集";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.elk.info";
	}

	@Override
	protected Object getData() {
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
	}

	private static class ElkInfo {

		@FieldReport(name = "taotao.cloud.health.collect.elk.queue.size", desc = "ELK消息队列大小")
		private Integer queueSize;
		@FieldReport(name = "taotao.cloud.health.collect.elk.consecutiveDropped", desc = "ELK消息连续丢弃数量")
		private Long consecutiveDropped;

		public ElkInfo() {
		}

		public ElkInfo(Integer queueSize, Long consecutiveDropped) {
			this.queueSize = queueSize;
			this.consecutiveDropped = consecutiveDropped;
		}

		public Integer getQueueSize() {
			return queueSize;
		}

		public void setQueueSize(Integer queueSize) {
			this.queueSize = queueSize;
		}

		public Long getConsecutiveDropped() {
			return consecutiveDropped;
		}

		public void setConsecutiveDropped(Long consecutiveDropped) {
			this.consecutiveDropped = consecutiveDropped;
		}
	}
}
