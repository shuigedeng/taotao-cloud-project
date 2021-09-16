/*
 * Copyright 2002-2021 the original author or authors.
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

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.health.enums.WarnTypeEnum;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import com.taotao.cloud.health.properties.HealthProperties;
import com.taotao.cloud.health.strategy.DefaultWarnStrategy;
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

	protected List<AbstractCollectTask> checkTasks = new ArrayList<>();
	protected DefaultWarnStrategy strategy;
	private boolean close;
	private CollectTaskProperties properties;
	private HealthProperties healthProperties;
	private MonitorThreadPool monitorThreadPool;
	private DefaultHttpClient defaultHttpClient;

	public void registerCollectTask(AbstractCollectTask task) {
		checkTasks.add(task);
	}

	public HealthCheckProvider(
		DefaultWarnStrategy strategy,
		DefaultHttpClient defaultHttpClient,
		CollectTaskProperties properties,
		HealthProperties healthProperties,
		MonitorThreadPool monitorThreadPool) {
		this.strategy = strategy;
		this.close = false;
		this.properties = properties;
		this.healthProperties = healthProperties;
		this.monitorThreadPool = monitorThreadPool;
		this.defaultHttpClient = defaultHttpClient;

		registerCollectTask(new CpuCollectTask(properties));
		//registerCollectTask(new IOCollectTask(properties));
		registerCollectTask(new MemoryCollectTask(properties));
		registerCollectTask(new ThreadCollectTask(properties));
		registerCollectTask(new UnCatchExceptionCollectTask(properties));
		registerCollectTask(new MonitorThreadPoolCollectTask(properties));
		registerCollectTask(new AsyncThreadPoolCollectTask(properties));
		//registerCollectTask(new BsfEurekaCollectTask());
		registerCollectTask(new MybatisCollectTask(properties));
		registerCollectTask(new DataSourceCollectTask(properties));
		registerCollectTask(new WebServerCollectTask(properties));
		//registerCollectTask(new JedisCollectTask(properties));
		registerCollectTask(new NetworkCollectTask(properties));
		registerCollectTask(new XxlJobCollectTask(properties));
		//registerCollectTask(new FileCollectTask());
		//registerCollectTask(new RocketMQCollectTask());
		registerCollectTask(new HttpPoolCollectTask(properties));
		//registerCollectTask(new CatCollectTask());
		//registerCollectTask(new ElasticSearchCollectTask());
		registerCollectTask(new ElkCollectTask(properties));
		registerCollectTask(new DoubtApiCollectTask(properties));
		registerCollectTask(new LogStatisticCollectTask(properties));
		registerCollectTask(new NacosCollectTask(properties));

		monitorThreadPool.monitorSubmit("系统任务: HealthCheckProvider 采集任务", () -> {
			while (!monitorThreadPool.monitorIsShutdown() && !close) {
				try {
					Report report = getReport(false);
					String text = strategy.analyseText(report);
					if (StringUtil.isEmpty(text)) {
						return;
					}

					AbstractCollectTask.notifyMessage(WarnTypeEnum.ERROR, "健康检查", text);
				} catch (Exception e) {
					LogUtil.warn(StarterNameConstant.HEALTH_STARTER, "run 循环采集出错", e);
				}

				try {
					Thread.sleep(healthProperties.getHealthTimeSpan() * 1000L);
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
						report.put(task.getName(),
							report2.setDesc(task.getDesc())
								.setName(task.getName()));
					}
				} catch (Exception e) {
					LogUtil.error(e,
						StarterNameConstant.HEALTH_STARTER + task.getName() + "采集获取报表出错");
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
				LogUtil.warn(StarterNameConstant.HEALTH_STARTER, "close资源释放出错",
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

	public DefaultWarnStrategy getStrategy() {
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

	public CollectTaskProperties getProperties() {
		return properties;
	}

	public void setProperties(CollectTaskProperties properties) {
		this.properties = properties;
	}

	public HealthProperties getHealthProperties() {
		return healthProperties;
	}

	public void setHealthProperties(HealthProperties healthProperties) {
		this.healthProperties = healthProperties;
	}

	public MonitorThreadPool getMonitorThreadPool() {
		return monitorThreadPool;
	}

	public void setMonitorThreadPool(MonitorThreadPool monitorThreadPool) {
		this.monitorThreadPool = monitorThreadPool;
	}

	public DefaultHttpClient getDefaultHttpClient() {
		return defaultHttpClient;
	}

	public void setDefaultHttpClient(DefaultHttpClient defaultHttpClient) {
		this.defaultHttpClient = defaultHttpClient;
	}
}
