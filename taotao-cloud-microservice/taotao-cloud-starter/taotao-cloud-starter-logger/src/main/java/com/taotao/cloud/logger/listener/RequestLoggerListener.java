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
package com.taotao.cloud.logger.listener;

import com.taotao.cloud.logger.event.RequestLoggerEvent;
import com.taotao.cloud.logger.model.RequestLogger;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import java.util.List;
import java.util.Objects;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 注解形式的监听 异步监听日志事件
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/3 13:33
 */
public class RequestLoggerListener {

	private List<IRequestLoggerService> requestLoggerService;

	public RequestLoggerListener(List<IRequestLoggerService> requestLoggerServices) {
		this.requestLoggerService = requestLoggerServices;
	}

	@Async
	@EventListener(RequestLoggerEvent.class)
	public void saveRequestLog(RequestLoggerEvent event) {
		RequestLogger requestLogger = (RequestLogger) event.getSource();

		if (Objects.nonNull(requestLoggerService) && requestLoggerService.size() > 0) {
			requestLoggerService.forEach(service -> {
				service.save(requestLogger);
			});
		}
	}
}
