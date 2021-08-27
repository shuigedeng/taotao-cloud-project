package com.taotao.cloud.health.collect;

import com.taotao.cloud.common.base.ThreadPool;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.health.base.AbstractCollectTask;
import com.taotao.cloud.health.base.EnumWarnType;
import com.taotao.cloud.health.base.Report;
import com.taotao.cloud.health.config.HealthProperties;
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
	private boolean isclose = false;

	public void registerCollectTask(AbstractCollectTask task) {
		checkTasks.add(task);
	}


	public HealthCheckProvider() {
		isclose = false;
		registerCollectTask(new CpuCollectTask());
		registerCollectTask(new IOCollectTask());
		registerCollectTask(new MemeryCollectTask());
		registerCollectTask(new ThreadCollectTask());
		registerCollectTask(new UnCatchExceptionCollectTask());
		registerCollectTask(new BsfThreadPoolSystemCollectTask());
		registerCollectTask(new BsfEurekaCollectTask());
		registerCollectTask(new MybatisCollectTask());
		registerCollectTask(new DataSourceCollectTask());
		registerCollectTask(new TomcatCollectTask());
		registerCollectTask(new JedisCollectTask());
		registerCollectTask(new NetworkCollectTask());
		registerCollectTask(new XxlJobCollectTask());
		registerCollectTask(new FileCollectTask());
		registerCollectTask(new RocketMQCollectTask());
		registerCollectTask(new HttpPoolCollectTask());
		registerCollectTask(new CatCollectTask());
		registerCollectTask(new ElasticSearchCollectTask());
		registerCollectTask(new ElkCollectTask());
		registerCollectTask(new DoubtApiCollectTask());
		registerCollectTask(new LogStatisticCollectTask());

		ThreadPool.System.submit("bsf系统任务:HealthCheckProvider采集任务", () -> {
			while (!ThreadPool.System.isShutdown() && !isclose) {
				try {
					run();
				} catch (Exception e) {
					LogUtil.warn(HealthProperties.Project, "run 循环采集出错", e);
				}
				try {
					Thread.sleep(HealthProperties.Default().getBsfHealthTimeSpan() * 1000);
				} catch (Exception e) {
				}
			}
		});
	}


	public Report getReport(boolean isAnalyse) {
		Report report = new Report().setDesc("健康检查报表").setName("bsf.health.report");
		for (AbstractCollectTask task : checkTasks) {
			if (task.getEnabled()) {
				try {
					Report report2 = task.getReport();
					if (report2 != null) {
						report.put(task.getName(),
							report2.setDesc(task.getDesc()).setName(task.getName()));
					}
				} catch (Exception e) {
					LogUtil.error(HealthProperties.Project,
						task.getName() + "采集获取报表出错", e);
				}

			}
		}
		if (isAnalyse) {
			report = strategy.analyse(report);
		}
		return report;
	}

	public void run() {
		Report report = getReport(false);
		String text = strategy.analyseText(report);
		if (StringUtil.isEmpty(text)) {
			return;
		}
		AbstractCollectTask.notifyMessage(EnumWarnType.ERROR, "bsf健康检查", text);

	}

	@Override
	public void close() {
		isclose = true;
		for (AbstractCollectTask task : checkTasks) {
			try {
				task.close();
			} catch (Exception exp) {
				LogUtil.warn(HealthProperties.Project, "close资源释放出错",
					exp);
			}
		}
	}
}
