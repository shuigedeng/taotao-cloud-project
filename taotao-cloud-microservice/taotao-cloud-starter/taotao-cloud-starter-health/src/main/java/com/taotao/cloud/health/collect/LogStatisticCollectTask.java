package com.taotao.cloud.health.collect;

import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.model.CollectInfo;
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

	private static final String TASK_NAME = "taotao.cloud.health.collect.logStatistic";

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
		return this.getClass().getName();
	}

	@Override
	public String getName() {
		return TASK_NAME;
	}

	@Override
	protected CollectInfo getData() {
		try {
			LogErrorInfo data = new LogErrorInfo();
			data.logerrorCount =
				this.collector.value("taotao.cloud.health.collect.log.error.count").get() == null
					? 0
					: ((AtomicLong) (this.collector.value(
							"taotao.cloud.health.collect.log.error.count")
						.get())).intValue();
			data.logIncreCount =
				this.collector.value("taotao.cloud.health.collect.log.incre.count").get() == null
					? 0
					: ((AtomicLong) (this.collector.value(
							"taotao.cloud.health.collect.log.incre.count")
						.get())).intValue();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class LogErrorInfo implements CollectInfo{

		@FieldReport(name = TASK_NAME + ".error.count", desc = "最近1分钟错误日志数量")
		private Integer logerrorCount  = 0;
		@FieldReport(name = TASK_NAME + ".incre.count", desc = "最近1分钟日志条数增量")
		private Integer logIncreCount = 0;
	}
}
