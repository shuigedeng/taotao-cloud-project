package com.taotao.cloud.health.collect;

import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 收集日志记录情况：最近一分钟增长日志条数及错误日志条数
 *
 * @version 2022.03
 * @date 2019-10-23
 */
public class LogStatisticCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.health.collect.logStatistic";

	private final CollectTaskProperties properties;

	public LogStatisticCollectTask(CollectTaskProperties properties) {
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
			LogErrorInfo info = new LogErrorInfo();
			Collector collector = Collector.getCollector();
			if (Objects.nonNull(collector)) {
				info.logerrorCount =
					collector.value("taotao.cloud.health.collect.log.error.count").get()
						== null
						? 0
						: ((AtomicLong) (collector.value(
								"taotao.cloud.health.collect.log.error.count")
							.get())).intValue();
				info.logIncreCount =
					collector.value("taotao.cloud.health.collect.log.incre.count").get()
						== null
						? 0
						: ((AtomicLong) (collector.value(
								"taotao.cloud.health.collect.log.incre.count")
							.get())).intValue();
				return info;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class LogErrorInfo implements CollectInfo {

		@FieldReport(name = TASK_NAME + ".error.count", desc = "最近1分钟错误日志数量")
		private Integer logerrorCount = 0;
		@FieldReport(name = TASK_NAME + ".incre.count", desc = "最近1分钟日志条数增量")
		private Integer logIncreCount = 0;
	}
}
