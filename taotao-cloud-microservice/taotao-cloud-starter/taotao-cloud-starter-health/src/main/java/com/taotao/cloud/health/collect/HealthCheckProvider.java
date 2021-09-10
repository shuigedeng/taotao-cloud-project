package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.http.HttpClientManager;
import com.taotao.cloud.core.model.Collector;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.health.model.EnumWarnType;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import com.taotao.cloud.health.properties.HealthProperties;
import com.taotao.cloud.health.strategy.DefaultWarnStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 18:47
 **/
public class HealthCheckProvider implements AutoCloseable {

	protected List<AbstractCollectTask> checkTasks = new ArrayList<>();
	protected DefaultWarnStrategy strategy;
	private boolean isclose;
	private CollectTaskProperties properties;
	private HealthProperties healthProperties;
	private MonitorThreadPool monitorThreadPool;
	private Collector collector;
	private DefaultHttpClient defaultHttpClient;

	public void registerCollectTask(AbstractCollectTask task) {
		checkTasks.add(task);
	}

	public HealthCheckProvider(
		DefaultWarnStrategy strategy,
		DefaultHttpClient defaultHttpClient,
		HttpClientManager httpClientManager,
		Collector collector,
		CollectTaskProperties properties,
		HealthProperties healthProperties,
		MonitorThreadPool monitorThreadPool) {
		this.strategy = strategy;
		this.collector = collector;
		this.isclose = false;
		this.properties = properties;
		this.healthProperties = healthProperties;
		this.monitorThreadPool = monitorThreadPool;
		this.defaultHttpClient = defaultHttpClient;

		registerCollectTask(new CpuCollectTask(properties));
		//registerCollectTask(new IOCollectTask(properties));
		registerCollectTask(new MemeryCollectTask(properties));
		registerCollectTask(new ThreadCollectTask(properties));
		registerCollectTask(new UnCatchExceptionCollectTask(properties));
		registerCollectTask(new MonitorThreadPoolCollectTask(collector, properties));
		//registerCollectTask(new BsfEurekaCollectTask());
		registerCollectTask(new MybatisCollectTask(collector, properties));
		registerCollectTask(new DataSourceCollectTask(properties));
		registerCollectTask(new TomcatCollectTask(properties));
		//registerCollectTask(new JedisCollectTask(properties));
		registerCollectTask(new NetworkCollectTask(properties));
		registerCollectTask(new XxlJobCollectTask(properties));
		//registerCollectTask(new FileCollectTask());
		//registerCollectTask(new RocketMQCollectTask());
		registerCollectTask(new HttpPoolCollectTask(httpClientManager,properties));
		//registerCollectTask(new CatCollectTask());
		//registerCollectTask(new ElasticSearchCollectTask());
		registerCollectTask(new ElkCollectTask(properties));
		registerCollectTask(new DoubtApiCollectTask(collector, properties));
		registerCollectTask(new LogStatisticCollectTask(collector,properties));

		this.monitorThreadPool.monitorSubmit("系统任务:HealthCheckProvider采集任务", () -> {
			while (!this.monitorThreadPool.monitorIsShutdown() && !isclose) {
				LogUtil.info(
					Thread.currentThread().getName() + " =========> 系统任务:HealthCheckProvider采集任务");

				try {
					Report report = getReport(false);
					String text = strategy.analyseText(report);
					if (StringUtil.isEmpty(text)) {
						return;
					}
					AbstractCollectTask.notifyMessage(EnumWarnType.ERROR, "健康检查", text);
				} catch (Exception e) {
					LogUtil.warn(StarterNameConstant.HEALTH_STARTER, "run 循环采集出错", e);
				}

				try {
					Thread.sleep(this.healthProperties.getHealthTimeSpan() * 1000L);
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		});
	}


	public Report getReport(boolean isAnalyse) {
		Report report = new Report().setDesc("健康检查报表").setName("taotao.cloud.health.report");
		for (AbstractCollectTask task : checkTasks) {
			if (task.getEnabled()) {
				try {
					Report report2 = task.getReport();
					if (report2 != null) {
						report.put(task.getName(),
							report2.setDesc(task.getDesc()).setName(task.getName()));
					}
				} catch (Exception e) {
					LogUtil.error(StarterNameConstant.HEALTH_STARTER,
						task.getName() + "采集获取报表出错", e);
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
		isclose = true;
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

	public boolean isIsclose() {
		return isclose;
	}

	public void setIsclose(boolean isclose) {
		this.isclose = isclose;
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

	public Collector getCollector() {
		return collector;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}
}
