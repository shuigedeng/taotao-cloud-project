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
package com.taotao.cloud.web.request.listener;

import com.taotao.cloud.web.request.event.RequestLoggerEvent;
import com.taotao.cloud.web.request.model.RequestLog;
import com.taotao.cloud.web.request.service.IRequestLoggerService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Objects;

/**
 * 注解形式的监听 异步监听日志事件
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/3 13:33
 */
public class RequestLoggerListener {

	private final List<IRequestLoggerService> requestLoggerService;

	public RequestLoggerListener(List<IRequestLoggerService> requestLoggerServices) {
		this.requestLoggerService = requestLoggerServices;
	}

	@Async
	@EventListener(RequestLoggerEvent.class)
	public void saveRequestLog(RequestLoggerEvent event) {
		RequestLog requestLog = (RequestLog) event.getSource();

		if (Objects.nonNull(requestLoggerService) && !requestLoggerService.isEmpty()) {
			requestLoggerService.forEach(service -> service.save(requestLog));
		}
	}
}
