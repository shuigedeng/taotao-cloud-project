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
package com.taotao.cloud.monitor.export;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Context;
import cn.hutool.core.date.DateUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.monitor.model.Report;
import com.taotao.cloud.monitor.properties.ExportProperties;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.marker.MapEntriesAppendingMarker;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

/**
 * ElkExport
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:14:04
 */
public class ElkExport extends AbstractExport {

	private final ExportProperties exportProperties;
	private final LogstashTcpSocketAppender appender;

	public ElkExport(ExportProperties exportProperties,
		LogstashTcpSocketAppender appender) {
		this.exportProperties = exportProperties;
		this.appender = appender;
	}

	@Override
	public void start() {
		super.start();
		ILoggerFactory log = LoggerFactory.getILoggerFactory();
		if (log instanceof Context) {
			appender.setContext((Context) log);

			LogstashEncoder encoder = new LogstashEncoder();
			String appName =
				"Report-" + PropertyUtil.getPropertyCache(CommonConstant.SPRING_APP_NAME_KEY, "");
			encoder.setCustomFields("{\"appname\":\"" + appName + "\",\"appindex\":\"Report\"}");
			encoder.setEncoding("UTF-8");
			appender.setEncoder(encoder);
			appender.start();
		}
	}

	@Override
	public void run(Report report) {
		if (appender == null || !this.exportProperties.getElkEnabled()) {
			return;
		}

		Map<String, Object> map = new LinkedHashMap<>();
		report.eachReport((String field, Report.ReportItem reportItem) -> {
			if (reportItem != null && reportItem.getValue() instanceof Number) {
				map.put(field.replace(".", "_"), reportItem.getValue());
			}
			return reportItem;
		});

		LoggingEvent event = createLoggerEvent(map,
			"taotao cloud report:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		appender.doAppend(event);
	}

	private LoggingEvent createLoggerEvent(Map<String, Object> values, String message) {
		LoggingEvent loggingEvent = new LoggingEvent();
		loggingEvent.setTimeStamp(System.currentTimeMillis());
		loggingEvent.setLevel(Level.INFO);
		loggingEvent.setLoggerName("ReportLogger");
		loggingEvent.setMarker(new MapEntriesAppendingMarker(values));
		loggingEvent.setMessage(message);
		loggingEvent.setArgumentArray(new String[0]);
		loggingEvent.setThreadName(Thread.currentThread().getName());
		return loggingEvent;
	}

	@Override
	public void close() {
		super.close();
		if (appender != null) {
			appender.stop();
		}
	}
}
