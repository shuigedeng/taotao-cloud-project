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
package com.taotao.cloud.logger.logging.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.ContextAwareBase;

import com.taotao.cloud.logger.logging.appender.ILoggingAppender;
import java.util.List;

/**
 * Logback configuration is achieved by configuration file and API. When configuration file change
 * is detected, the configuration is reset. This listener ensures that the programmatic
 * configuration is also re-applied after reset.
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:42:27
 */
public class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {
	private final List<ILoggingAppender> appenderList;

	public LogbackLoggerContextListener(List<ILoggingAppender> appenderList) {
		this.appenderList = appenderList;
	}

	public LogbackLoggerContextListener(ContextAware declaredOrigin, List<ILoggingAppender> appenderList) {
		super(declaredOrigin);
		this.appenderList = appenderList;
	}

	@Override
	public boolean isResetResistant() {
		return true;
	}

	@Override
	public void onStart(LoggerContext context) {
		for (ILoggingAppender appender : appenderList) {
			appender.start(context);
		}
	}

	@Override
	public void onReset(LoggerContext context) {
		for (ILoggingAppender appender : appenderList) {
			appender.reset(context);
		}
	}

	@Override
	public void onStop(LoggerContext context) {
		// Nothing to do.
	}

	@Override
	public void onLevelChange(Logger logger, Level level) {
		// Nothing to do.
	}
}
