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

package com.taotao.cloud.logger.logging.appender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.logging.config.MicaLoggingProperties;
import com.taotao.cloud.logger.logging.utils.LogStashUtil;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * LogStash 输出 json
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:43:10
 */
public class LoggingLogStashAppender implements ILoggingAppender {
	private static final String ASYNC_LOG_STASH_APPENDER_NAME = "ASYNC_LOG_STASH";
	private final MicaLoggingProperties properties;
	private final String customFieldsJson;

	public LoggingLogStashAppender(Environment environment,
								   MicaLoggingProperties properties) {
		this.properties = properties;
		// 1. 服务名和环境
		String appName = environment.getRequiredProperty(CommonConstant.SPRING_APP_NAME_KEY);
		String profile = environment.getRequiredProperty(CommonConstant.ACTIVE_PROFILES_PROPERTY);
		// 2. json 自定义字段
		Map<String, Object> customFields = new HashMap<>(4);
		customFields.put("appName", appName);
		customFields.put("profile", profile);
		// 3. 自定义配置的字段
		customFields.putAll(properties.getLogstash().getCustomFieldMap());
		this.customFieldsJson = JsonUtil.toJson(customFields);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		this.start(context);
	}

	@Override
	public void start(LoggerContext context) {
		LogUtil.info("LogStash logging start.");
		reload(context);
	}

	@Override
	public void reset(LoggerContext context) {
		LogUtil.info("LogStash logging reset.");
		reload(context);
	}

	private void reload(LoggerContext context) {
		MicaLoggingProperties.Logstash logStash = properties.getLogstash();
		if (logStash.isEnabled()) {
			addLogStashTcpSocketAppender(context, customFieldsJson, logStash);
		}
	}

	/**
	 * addLogstashTcpSocketAppender.
	 *
	 * @param context            a {@link LoggerContext} object.
	 * @param customFields       a {@link String} object.
	 * @param logStashProperties a {@link net.dreamlu.mica.logging.config.MicaLoggingProperties.Logstash} object.
	 */
	private static void addLogStashTcpSocketAppender(LoggerContext context,
													String customFields,
													MicaLoggingProperties.Logstash logStashProperties) {
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		final LogstashTcpSocketAppender logStashAppender = new LogstashTcpSocketAppender();
		logStashAppender.addDestination(logStashProperties.getDestinations());
		logStashAppender.setContext(context);
		logStashAppender.setEncoder(logstashEncoder(customFields));
		logStashAppender.setName(ASYNC_LOG_STASH_APPENDER_NAME);
		logStashAppender.setRingBufferSize(logStashProperties.getRingBufferSize());
		logStashAppender.start();
		// 先删除，再添加
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(ASYNC_LOG_STASH_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(logStashAppender);
	}

	private static LogstashEncoder logstashEncoder(String customFields) {
		final LogstashEncoder logstashEncoder = new LogstashEncoder();
		logstashEncoder.setThrowableConverter(LogStashUtil.throwableConverter());
		logstashEncoder.setCustomFields(customFields);
		return logstashEncoder;
	}

}
