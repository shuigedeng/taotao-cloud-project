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
package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.core.http.HttpClient;
import com.taotao.cloud.core.monitor.Monitor;
import com.taotao.cloud.health.enums.WarnTypeEnum;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import com.taotao.cloud.health.properties.HealthProperties;
import com.taotao.cloud.health.strategy.DefaultWarnStrategy;
import com.taotao.cloud.health.strategy.WarnStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * HealthCheckProvider
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:42:25
 */
public class HealthCheckProvider implements AutoCloseable {

	private List<AbstractCollectTask> checkTasks = new ArrayList<>();
	private boolean close;

	private Monitor monitor;
	private WarnStrategy strategy;
	private HealthProperties healthProperties;
	private CollectTaskProperties collectTaskProperties;

	public void registerCollectTask(AbstractCollectTask task) {
		checkTasks.add(task);
	}

	public HealthCheckProvider(
		CollectTaskProperties collectTaskProperties,
		HealthProperties healthProperties,
		WarnStrategy strategy,
		Monitor monitor) {
		this.strategy = strategy;
		this.close = false;
		this.collectTaskProperties = collectTaskProperties;
		this.healthProperties = healthProperties;
		this.monitor = monitor;

		registerCollectTask(new CpuCollectTask(collectTaskProperties));
		//registerCollectTask(new IOCollectTask(collectTaskProperties));
		registerCollectTask(new MemoryCollectTask(collectTaskProperties));
		registerCollectTask(new ThreadCollectTask(collectTaskProperties));
		registerCollectTask(new UnCatchExceptionCollectTask(collectTaskProperties));
		registerCollectTask(new MonitorThreadPoolCollectTask(collectTaskProperties));
		registerCollectTask(new AsyncThreadPoolCollectTask(collectTaskProperties));
		//registerCollectTask(new BsfEurekaCollectTask());
		registerCollectTask(new MybatisCollectTask(collectTaskProperties));
		registerCollectTask(new DataSourceCollectTask(collectTaskProperties));
		registerCollectTask(new WebServerCollectTask(collectTaskProperties));
		//registerCollectTask(new JedisCollectTask(collectTaskProperties));
		registerCollectTask(new NetworkCollectTask(collectTaskProperties));
		registerCollectTask(new XxlJobCollectTask(collectTaskProperties));
		//registerCollectTask(new FileCollectTask());
		//registerCollectTask(new RocketMQCollectTask());
		registerCollectTask(new HttpPoolCollectTask(collectTaskProperties));
		//registerCollectTask(new CatCollectTask());
		//registerCollectTask(new ElasticSearchCollectTask());
		registerCollectTask(new ElkCollectTask(collectTaskProperties));
		registerCollectTask(new DoubtApiCollectTask(collectTaskProperties));
		registerCollectTask(new LogStatisticCollectTask(collectTaskProperties));
		registerCollectTask(new NacosCollectTask(collectTaskProperties));

		monitor.monitorSubmit("系统任务: HealthCheckProvider 采集任务", () -> {
			while (!monitor.monitorIsShutdown() && !close) {
				try {
					Report report = getReport(false);
					String text = strategy.analyseText(report);
					if (StringUtil.isEmpty(text)) {
						return;
					}

					AbstractCollectTask.notifyMessage(WarnTypeEnum.ERROR, "健康检查", text);
				} catch (Exception e) {
					LogUtil.warn(StarterName.HEALTH_STARTER, "run 循环采集出错", e);
				}

				try {
					Thread.sleep(healthProperties.getTimeSpan() * 1000L);
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		});
	}


	/**
	 * getReport
	 *
	 * @param isAnalyse isAnalyse
	 * @return {@link com.taotao.cloud.health.model.Report }
	 * @author shuigedeng
	 * @since 2021-09-10 17:42:55
	 */
	public Report getReport(boolean isAnalyse) {
		Report report = new Report()
			.setDesc("健康检查报表")
			.setName("taotao.cloud.health.report");

		for (AbstractCollectTask task : checkTasks) {
			if (task.getEnabled()) {
				try {
					Report report2 = task.getReport();
					if (report2 != null) {
						report.put(task.getName(), report2.setDesc(task.getDesc()).setName(task.getName()));
					}
				} catch (Exception e) {
					LogUtil.error(e,
						StarterName.HEALTH_STARTER + task.getName() + "采集获取报表出错");
				}
			}
		}

		if (isAnalyse) {
			report = strategy.analyse(report);
		}

		return report;
	}

	@Override
	public void close() {
		close = true;
		for (AbstractCollectTask task : checkTasks) {
			try {
				task.close();
			} catch (Exception exp) {
				LogUtil.warn(StarterName.HEALTH_STARTER, "close资源释放出错",
					exp);
			}
		}
	}


	public List<AbstractCollectTask> getCheckTasks() {
		return checkTasks;
	}

	public void setCheckTasks(List<AbstractCollectTask> checkTasks) {
		this.checkTasks = checkTasks;
	}

	public WarnStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(DefaultWarnStrategy strategy) {
		this.strategy = strategy;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	public HealthProperties getHealthProperties() {
		return healthProperties;
	}

	public void setHealthProperties(HealthProperties healthProperties) {
		this.healthProperties = healthProperties;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public void setStrategy(WarnStrategy strategy) {
		this.strategy = strategy;
	}

	public CollectTaskProperties getCollectTaskProperties() {
		return collectTaskProperties;
	}

	public void setCollectTaskProperties(
		CollectTaskProperties collectTaskProperties) {
		this.collectTaskProperties = collectTaskProperties;
	}
}
