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

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.logger.event.RequestLoggerEvent;
import com.taotao.cloud.logger.model.RequestLogger;
import com.taotao.cloud.logger.service.impl.KafkaRequestLoggerServiceImpl;
import com.taotao.cloud.logger.service.impl.LoggerRequestLoggerServiceImpl;
import com.taotao.cloud.logger.service.impl.RedisRequestLoggerServiceImpl;
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

	@Async
	@EventListener(RequestLoggerEvent.class)
	public void saveRequestLog(RequestLoggerEvent event) {
		RequestLogger requestLogger = (RequestLogger) event.getSource();
		KafkaRequestLoggerServiceImpl kafkaRequestLogService = ContextUtil
			.getBean(KafkaRequestLoggerServiceImpl.class, true);
		if (null != kafkaRequestLogService) {
			kafkaRequestLogService.save(requestLogger);
		}

		LoggerRequestLoggerServiceImpl loggerRequestLogService = ContextUtil
			.getBean(LoggerRequestLoggerServiceImpl.class, true);
		if (null != loggerRequestLogService) {
			loggerRequestLogService.save(requestLogger);
		}

		RedisRequestLoggerServiceImpl redisRequestLogService = ContextUtil
			.getBean(RedisRequestLoggerServiceImpl.class, true);
		if (null != redisRequestLogService) {
			redisRequestLogService.save(requestLogger);
		}

		//sysLogService.save(requestLog);
	}
}
