package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.base.Collector;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.FieldReport;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 收集日志记录情况：最近一分钟增长日志条数及错误日志条数
 *
 * @author Robin.Wang
 * @version 1.0.0
 * @date 2019-10-23
 */
public class LogStatisticCollectTask extends AbstractCollectTask {

	@Override
	public int getTimeSpan() {
		return PropertyUtil.getPropertyCache("bsf.health.log.statistic.timeSpan", 20);
	}

	@Override
	public boolean getEnabled() {
		return PropertyUtil.getPropertyCache("bsf.health.log.statistic.enabled", true);
	}

	@Override
	public String getDesc() {
		return "最近1分钟日志统计";
	}

	@Override
	public String getName() {
		return "log.info";
	}

	@Override
	protected Object getData() {
		LogErrorInfo data = new LogErrorInfo();
		data.logerrorCount = Collector.Default.value("bsf.health.log.error.count").get() == null ? 0
			: ((AtomicLong) (Collector.Default.value("bsf.health.log.error.count")
				.get())).intValue();
		data.logIncreCount = Collector.Default.value("bsf.health.log.incre.count").get() == null ? 0
			: ((AtomicLong) (Collector.Default.value("bsf.health.log.incre.count")
				.get())).intValue();
		return data;
	}

	private static class LogErrorInfo {

		@FieldReport(name = "log.error.count", desc = "最近1分钟错误日志数量")
		private Integer logerrorCount;
		@FieldReport(name = "log.incre.count", desc = "最近1分钟日志条数增量")
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
