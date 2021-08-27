package com.taotao.cloud.health.collect;


import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.FieldReport;

/**
 * @author Huang Zhaoping
 */
public class ElkCollectTask extends AbstractCollectTask {

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.elk.timeSpan", 20);
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.elk.enabled", true);
	}

	@Override
	public String getDesc() {
		return "ELK性能采集";
	}

	@Override
	public String getName() {
		return "elk.info";
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

		@FieldReport(name = "elk.queue.size", desc = "ELK消息队列大小")
		private Integer queueSize;
		@FieldReport(name = "elk.consecutiveDropped", desc = "ELK消息连续丢弃数量")
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
