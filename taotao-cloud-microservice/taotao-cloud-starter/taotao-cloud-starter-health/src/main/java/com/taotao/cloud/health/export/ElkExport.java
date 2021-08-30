package com.taotao.cloud.health.export;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Context;
import cn.hutool.core.date.DateUtil;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.utils.PropertyUtil;
import com.taotao.cloud.health.model.Report;
import com.taotao.cloud.health.properties.ExportProperties;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.marker.MapEntriesAppendingMarker;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

/**
 * @author: chejiangyi
 * @version: 2019-08-13 19:48
 **/
public class ElkExport extends AbstractExport {

	private LogstashTcpSocketAppender appender;


	@Override
	public void start() {
		super.start();
		ILoggerFactory log = LoggerFactory.getILoggerFactory();
		if (log instanceof Context) {
			appender = new LogstashTcpSocketAppender();
			appender.setContext((Context) log);
			String[] destinations = ExportProperties.Default().getElkDestinations();
			if (destinations == null || destinations.length == 0) {
				return;
			}

			for (String destination : destinations) {
				appender.addDestination(destination);
			}

			LogstashEncoder encoder = new LogstashEncoder();
			String appname =
				"Report-" + PropertyUtil.getPropertyCache(CoreProperties.SpringApplicationName,
					"");
			encoder.setCustomFields("{\"appname\":\"" + appname + "\",\"appindex\":\"Report\"}");
			encoder.setEncoding("UTF-8");
			appender.setEncoder(encoder);
			appender.start();
		}
	}

	@Override
	public void run(Report report) {
		if (appender == null || !ExportProperties.Default().isElkEnabled()) {
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
			"taotao cloud Report:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
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
