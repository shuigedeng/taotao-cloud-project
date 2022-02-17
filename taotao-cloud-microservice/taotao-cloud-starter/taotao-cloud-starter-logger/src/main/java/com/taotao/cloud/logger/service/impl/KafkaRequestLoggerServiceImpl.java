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
package com.taotao.cloud.logger.service.impl;

import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.logger.model.RequestLogger;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * 审计日志实现类-Kafka
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 11:18
 */
public class KafkaRequestLoggerServiceImpl implements IRequestLoggerService {

	public static final String REQUEST_LOG_TOPIC = "request-log-";

	@Value("${spring.application.name}")
	private String appName;

	private final KafkaTemplate<String, String> kafkaTemplate;

	public KafkaRequestLoggerServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public void save(RequestLogger requestLogger) {
		if (Objects.isNull(kafkaTemplate)) {
			String request = JsonUtil.toJSONString(requestLogger);

			ListenableFuture<SendResult<String, String>> future = kafkaTemplate
				.send(REQUEST_LOG_TOPIC + appName, request);

			future.addCallback(new ListenableFutureCallback<>() {
				@Override
				public void onFailure(Throwable throwable) {
					LogUtil.error("远程日志记录失败：{}", throwable);
				}

				@Override
				public void onSuccess(SendResult<String, String> stringObjectSendResult) {
					//log.info("远程日志记录成功：{}", requestLog);
				}
			});
		}
	}
}

