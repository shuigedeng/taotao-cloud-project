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
 * ExportProvider
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:15:21
 */
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

	/**
	 * registerCollectTask
	 *
	 * @param export export
	 * @author shuigedeng
	 * @since 2021-09-10 17:15:27
	 */
	public void registerCollectTask(AbstractExport export) {
		this.exports.add(export);
	}

	/**
	 * start
	 *
	 * @author shuigedeng
	 * @since 2021-09-10 17:15:34
	 */
	public void start() {
		this.isClose = false;

		if (this.exportProperties.getElkEnabled()) {
			LogstashTcpSocketAppender logstashTcpSocketAppender = ContextUtil.getBean(
				LogstashTcpSocketAppender.class,
				false);
			if (Objects.nonNull(logstashTcpSocketAppender)) {
				registerCollectTask(
					new ElkExport(this.exportProperties, logstashTcpSocketAppender));
			}
		}

		this.monitorThreadPool.monitorSubmit("系统任务: ExportProvider 采集上传任务", () -> {
			while (!this.monitorThreadPool.monitorIsShutdown() && !isClose) {
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

	/**
	 * run
	 *
	 * @author shuigedeng
	 * @since 2021-09-10 17:16:38
	 */
	public void run() {
		if (this.healthCheckProvider != null) {
			Report report = healthCheckProvider.getReport(false);
			for (AbstractExport e : exports) {
				e.run(report);
			}
		}
	}

	/**
	 * close
	 *
	 * @author shuigedeng
	 * @since 2021-09-10 17:16:35
	 */
	public void close() {
		this.isClose = true;
		for (AbstractExport e : this.exports) {
			try {
				e.close();
			} catch (Exception ex) {
				LogUtil.error(ex, StarterNameConstant.HEALTH_STARTER,
					e.getClass().getName() + "关闭出错");
			}
		}
	}
}
