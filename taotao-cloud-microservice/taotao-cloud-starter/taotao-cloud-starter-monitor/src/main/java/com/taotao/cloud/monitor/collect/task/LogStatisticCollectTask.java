package com.taotao.cloud.monitor.collect.task;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.monitor.annotation.FieldReport;
import com.taotao.cloud.monitor.collect.AbstractCollectTask;
import com.taotao.cloud.monitor.collect.CollectInfo;
import com.taotao.cloud.monitor.properties.CollectTaskProperties;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 收集日志记录情况：最近一分钟增长日志条数及错误日志条数
 *
 * @author dengtao
 * @version 2022.03
 */
public class LogStatisticCollectTask extends AbstractCollectTask {

	private static final String TASK_NAME = "taotao.cloud.monitor.collect.logStatistic";

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
					collector.value("taotao.cloud.monitor.collect.log.error.count").get()
						== null
						? 0
						: ((AtomicLong) (collector.value(
								"taotao.cloud.monitor.collect.log.error.count")
							.get())).intValue();
				info.logIncreCount =
					collector.value("taotao.cloud.monitor.collect.log.incre.count").get()
						== null
						? 0
						: ((AtomicLong) (collector.value(
								"taotao.cloud.monitor.collect.log.incre.count")
							.get())).intValue();
				return info;
			}
		} catch (Exception e) {
			if(LogUtils.isErrorEnabled()){
				LogUtils.error(e);
			}
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
