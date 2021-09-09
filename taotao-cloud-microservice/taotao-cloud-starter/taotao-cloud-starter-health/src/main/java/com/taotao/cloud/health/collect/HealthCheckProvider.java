package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.thread.ThreadPool;
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
	protected DefaultWarnStrategy strategy = DefaultWarnStrategy.Default;
	private boolean isclose;
	private CollectTaskProperties properties;

	public void registerCollectTask(AbstractCollectTask task) {
		checkTasks.add(task);
	}

	public HealthCheckProvider(CollectTaskProperties properties) {
		isclose = false;
		this.properties = properties;

		registerCollectTask(new CpuCollectTask(properties));
		//registerCollectTask(new IOCollectTask(properties));
		registerCollectTask(new MemeryCollectTask(properties));
		registerCollectTask(new ThreadCollectTask(properties));
		registerCollectTask(new UnCatchExceptionCollectTask(properties));
		registerCollectTask(new ThreadPoolSystemCollectTask(properties));
		//registerCollectTask(new BsfEurekaCollectTask());
		registerCollectTask(new MybatisCollectTask(properties));
		registerCollectTask(new DataSourceCollectTask(properties));
		registerCollectTask(new TomcatCollectTask(properties));
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

		ThreadPool.DEFAULT.submit("系统任务:HealthCheckProvider采集任务", () -> {
			while (!ThreadPool.DEFAULT.isShutdown() && !isclose) {
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
					Thread.sleep(HealthProperties.Default().getHealthTimeSpan() * 1000);
				} catch (Exception e) {
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
}
