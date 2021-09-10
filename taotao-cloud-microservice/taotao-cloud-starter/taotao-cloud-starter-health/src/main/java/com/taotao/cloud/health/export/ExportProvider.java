package com.taotao.cloud.health.export;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.health.collect.HealthCheckProvider;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.ExportProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.logstash.logback.appender.LogstashTcpSocketAppender;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 11:01
 **/
public class ExportProvider {

	private boolean isClose = true;
	private MonitorThreadPool monitorThreadPool;
	private ExportProperties exportProperties;
	private HealthCheckProvider healthCheckProvider;
	protected List<AbstractExport> exports = new ArrayList<>();

	public ExportProvider(MonitorThreadPool monitorThreadPool,
		ExportProperties exportProperties,
		HealthCheckProvider healthCheckProvider) {
		this.monitorThreadPool = monitorThreadPool;
		this.exportProperties = exportProperties;
		this.healthCheckProvider = healthCheckProvider;
	}

	public void registerCollectTask(AbstractExport export) {
		this.exports.add(export);
	}

	public void start() {
		this.isClose = false;

		if (this.exportProperties.isElkEnabled()) {
			LogstashTcpSocketAppender logstashTcpSocketAppender = ContextUtil.getBean(
				LogstashTcpSocketAppender.class,
				false);
			if (Objects.nonNull(logstashTcpSocketAppender)) {
				registerCollectTask(
					new ElkExport(this.exportProperties, logstashTcpSocketAppender));
			}
		}

		this.monitorThreadPool.monitorSubmit("系统任务:ExportProvider采集上传任务", () -> {
			while (!this.monitorThreadPool.monitorIsShutdown() && !isClose) {
				LogUtil.info(
					Thread.currentThread().getName() + " =========> 系统任务:ExportProvider采集上传任务");

				try {
					run();
				} catch (Exception e) {
					LogUtil.error(StarterNameConstant.HEALTH_STARTER, "run 循环上传报表出错", e);
				}

				try {
					Thread.sleep(this.exportProperties.getExportTimeSpan() * 1000L);
				} catch (Exception e) {
					LogUtil.error(e);
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
		if (this.healthCheckProvider != null) {
			Report report = healthCheckProvider.getReport(false);
			for (AbstractExport e : exports) {
				e.run(report);
			}
		}
	}

	public void close() {
		this.isClose = true;
		for (AbstractExport e : this.exports) {
			try {
				e.close();
			} catch (Exception ex) {
				LogUtil.error(StarterNameConstant.HEALTH_STARTER,
					e.getClass().getName() + "关闭出错", ex);
			}
		}
	}
}
