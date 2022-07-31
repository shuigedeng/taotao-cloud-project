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

import com.taotao.cloud.logger.logging.config.MicaLoggingProperties;
import com.taotao.cloud.logger.logging.utils.LoggingUtil;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 项目启动事件通知
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:42:19
 */
public class LoggingStartedEventListener {
	private final MicaLoggingProperties properties;

	public LoggingStartedEventListener(MicaLoggingProperties properties) {
		this.properties = properties;
	}

	@Async
	@Order
	@EventListener(WebServerInitializedEvent.class)
	public void afterStart() {
		// 1. 关闭控制台
		MicaLoggingProperties.Console console = properties.getConsole();
		if (console.isCloseAfterStart()) {
			LoggingUtil.detachAppender(LoggingUtil.CONSOLE_APPENDER_NAME);
		}
	}
}
