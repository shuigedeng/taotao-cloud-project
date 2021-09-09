package com.taotao.cloud.health.export;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.thread.ThreadPool;
import com.taotao.cloud.health.collect.HealthCheckProvider;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.ExportProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 11:01
 **/
public class ExportProvider {

	private boolean isClose = true;
	protected List<AbstractExport> exports = new ArrayList<>();

	public void registerCollectTask(AbstractExport export) {
		exports.add(export);
	}

	public void start() {
		isClose = false;

		if (ExportProperties.Default().isElkEnabled()) {
			registerCollectTask(new ElkExport());
		}

		ThreadPool.DEFAULT.submit("系统任务:ExportProvider采集上传任务", () -> {
			while (!ThreadPool.DEFAULT.isShutdown() && !isClose) {
				try {
					run();
				} catch (Exception e) {
					LogUtil.error(StarterNameConstant.HEALTH_STARTER, "run 循环上传报表出错", e);
				}

				try {
					Thread.sleep(ExportProperties.Default().getExportTimeSpan() * 1000L);
				} catch (Exception e) {
				}
			}
		});

		for (AbstractExport e : exports) {
			try {
				e.start();
			} catch (Exception ex) {
				LogUtil.error(StarterNameConstant.HEALTH_STARTER,
					e.getClass().getName() + "启动出错", ex);
			}
		}
	}

	public void run() {
		HealthCheckProvider healthProvider = ContextUtil.getBean(HealthCheckProvider.class, false);
		if (healthProvider != null) {
			Report report = healthProvider.getReport(false);
			for (AbstractExport e : exports) {
				e.run(report);
			}
		}
	}

	public void close() {
		isClose = true;
		for (AbstractExport e : exports) {
			try {
				e.close();
			} catch (Exception ex) {
				LogUtil.error(StarterNameConstant.HEALTH_STARTER,
					e.getClass().getName() + "关闭出错", ex);
			}
		}
	}
}
