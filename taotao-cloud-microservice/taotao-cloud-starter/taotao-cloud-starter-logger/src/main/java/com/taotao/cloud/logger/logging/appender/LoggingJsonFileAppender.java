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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.CharPool;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.logging.config.MicaLoggingProperties;
import com.taotao.cloud.logger.logging.utils.LogStashUtil;
import com.taotao.cloud.logger.logging.utils.LoggingUtil;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LoggingSystemProperties;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * json 日志输出，json 日志只输出 all.log
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:43:06
 */
public class LoggingJsonFileAppender implements ILoggingAppender {
	private final MicaLoggingProperties properties;
	private final String logAllFile;
	private final String customFieldsJson;

	public LoggingJsonFileAppender(Environment environment,
								   MicaLoggingProperties properties) {
		this.properties = properties;
		// 1. 服务名和环境和日志目录
		String appName = environment.getRequiredProperty(CommonConstant.SPRING_APP_NAME_KEY);
		String profile = environment.getRequiredProperty(CommonConstant.ACTIVE_PROFILES_PROPERTY);
		// 2. 文件日志格式
		String fileLogPattern = environment.resolvePlaceholders(LoggingUtil.DEFAULT_FILE_LOG_PATTERN);
		System.setProperty(LoggingSystemProperties.FILE_LOG_PATTERN, fileLogPattern);
		// 3. 生成日志文件的文件
		String logDir = environment.getProperty("logging.file.path", LoggingUtil.DEFAULT_LOG_DIR);
		this.logAllFile = logDir + CharPool.SLASH + appName + CharPool.SLASH + LoggingUtil.LOG_FILE_ALL;
		// 4. json 自定义字段
		Map<String, Object> customFields = new HashMap<>(4);
		customFields.put("appName", appName);
		customFields.put("profile", profile);
		this.customFieldsJson = JsonUtils.toJson(customFields);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		this.start(context);
	}

	@Override
	public void start(LoggerContext context) {
		LogUtils.info("JsonFile logging start.");
		reload(context);
	}

	@Override
	public void reset(LoggerContext context) {
		LogUtils.info("JsonFile logging start.");
		reload(context);
	}

	private void reload(LoggerContext context) {
		MicaLoggingProperties.Files files = properties.getFiles();
		if (files.isEnabled() && files.isUseJsonFormat()) {
			addAllFileAppender(context, logAllFile, customFieldsJson);
		}
	}

	/**
	 * <p>addJsonConsoleAppender.</p>
	 *
	 * @param context      a {@link LoggerContext} object.
	 * @param customFields a {@link String} object.
	 */
	private static void addAllFileAppender(LoggerContext context,
										   String logFile,
										   String customFields) {
		// More documentation is available at: https://github.com/logstash/logstash-logback-encoder
		final RollingFileAppender<ILoggingEvent> allFileAppender = new RollingFileAppender<>();
		allFileAppender.setContext(context);
		allFileAppender.setEncoder(compositeJsonEncoder(context, customFields));
		allFileAppender.setName(LoggingUtil.FILE_APPENDER_NAME);
		allFileAppender.setFile(logFile);
		allFileAppender.setRollingPolicy(LoggingUtil.rollingPolicy(context, allFileAppender, logFile));
		allFileAppender.start();
		// 先删除，再添加
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(LoggingUtil.FILE_APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(allFileAppender);
	}

	private static LoggingEventCompositeJsonEncoder compositeJsonEncoder(LoggerContext context,
																		 String customFields) {
		final LoggingEventCompositeJsonEncoder compositeJsonEncoder = new LoggingEventCompositeJsonEncoder();
		compositeJsonEncoder.setContext(context);
		compositeJsonEncoder.setProviders(LogStashUtil.jsonProviders(context, customFields));
		compositeJsonEncoder.start();
		return compositeJsonEncoder;
	}

}
