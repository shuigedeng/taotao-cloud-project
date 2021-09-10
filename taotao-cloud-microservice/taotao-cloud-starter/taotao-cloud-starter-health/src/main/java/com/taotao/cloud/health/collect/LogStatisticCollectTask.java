package com.taotao.cloud.health.collect;

import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 收集日志记录情况：最近一分钟增长日志条数及错误日志条数
 *
 * @author Robin.Wang
 * @version 1.0.0
 * @date 2019-10-23
 */
public class LogStatisticCollectTask extends AbstractCollectTask {

	private CollectTaskProperties properties;
	private Collector collector;

	public LogStatisticCollectTask(Collector collector, CollectTaskProperties properties) {
		this.collector = collector;
		this.properties = properties;
	}

	@Override
	public int getTimeSpan() {
		return properties.getLogStatisticTimeSpan();
	}

	@Override
	public boolean getEnabled() {
		return properties.isLogStatisticEnabled();
	}

	@Override
	public String getDesc() {
		return "最近1分钟日志统计";
	}

	@Override
	public String getName() {
		return "taotao.cloud.health.collect.log.info";
	}

	@Override
	protected Object getData() {
		LogErrorInfo data = new LogErrorInfo();
		data.logerrorCount =
			this.collector.value("taotao.cloud.health.collect.log.error.count").get() == null ? 0
				: ((AtomicLong) (this.collector.value("taotao.cloud.health.collect.log.error.count")
					.get())).intValue();
		data.logIncreCount =
			this.collector.value("taotao.cloud.health.collect.log.incre.count").get() == null ? 0
				: ((AtomicLong) (this.collector.value("taotao.cloud.health.collect.log.incre.count")
					.get())).intValue();
		return data;
	}

	private static class LogErrorInfo {

		@FieldReport(name = "taotao.cloud.health.collect.log.error.count", desc = "最近1分钟错误日志数量")
		private Integer logerrorCount;
		@FieldReport(name = "taotao.cloud.health.collect.log.incre.count", desc = "最近1分钟日志条数增量")
		private Integer logIncreCount;

		public LogErrorInfo() {
		}

		public LogErrorInfo(Integer logerrorCount, Integer logIncreCount) {
			this.logerrorCount = logerrorCount;
			this.logIncreCount = logIncreCount;
		}

		public Integer getLogerrorCount() {
			return logerrorCount;
		}

		public void setLogerrorCount(Integer logerrorCount) {
			this.logerrorCount = logerrorCount;
		}

		public Integer getLogIncreCount() {
			return logIncreCount;
		}

		public void setLogIncreCount(Integer logIncreCount) {
			this.logIncreCount = logIncreCount;
		}
	}
}
