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
package com.taotao.cloud.logger.logging.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.taotao.cloud.common.utils.system.SystemUtils;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.logback.LogbackLoggingSystemProperties;

/**
 * 参考自 jhipster
 * <p>
 * Utility methods to add appenders to a {@link LoggerContext}.
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:42:03
 */
public class LoggingUtil {
	public static final String DEFAULT_LOG_DIR = "logs";
	public static final String LOG_FILE_ALL = "all.log";
	public static final String LOG_FILE_ERROR = "error.log";
	public static final String CONSOLE_APPENDER_NAME = "CONSOLE";
	public static final String FILE_APPENDER_NAME = "FILE";
	public static final String FILE_ERROR_APPENDER_NAME = "FILE_ERROR";
	public static final String DEFAULT_FILE_LOG_PATTERN = "${FILE_LOG_PATTERN:%d{${LOG_DATEFORMAT_PATTERN:yyyy-MM-dd HH:mm:ss.SSS}} ${LOG_LEVEL_PATTERN:%5p} ${PID:} --- [%t] %-40.40logger{39} : %m%n${LOG_EXCEPTION_CONVERSION_WORD:%wEx}}";

	/**
	 * detach appender
	 *
	 * @param name appender name
	 */
	public static void detachAppender(String name) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(name);
	}

	public static RollingPolicy rollingPolicy(LoggerContext context,
											  FileAppender<?> appender,
											  String logErrorFile) {
		final SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
		rollingPolicy.setContext(context);
		rollingPolicy.setCleanHistoryOnStart(SystemUtils.getPropToBool(LogbackLoggingSystemProperties.ROLLINGPOLICY_CLEAN_HISTORY_ON_START, false));
		rollingPolicy.setFileNamePattern(logErrorFile + ".%d{yyyy-MM-dd}.%i.gz");
		rollingPolicy.setMaxFileSize(FileSize.valueOf(SystemUtils.getProp(LogbackLoggingSystemProperties.ROLLINGPOLICY_MAX_FILE_SIZE, "10MB")));
		rollingPolicy.setMaxHistory(SystemUtils.getPropToInt(LogbackLoggingSystemProperties.ROLLINGPOLICY_MAX_HISTORY, 7));
		rollingPolicy.setTotalSizeCap(FileSize.valueOf(SystemUtils.getProp(LogbackLoggingSystemProperties.ROLLINGPOLICY_TOTAL_SIZE_CAP, "0")));
		rollingPolicy.setParent(appender);
		rollingPolicy.start();
		return rollingPolicy;
	}

}
