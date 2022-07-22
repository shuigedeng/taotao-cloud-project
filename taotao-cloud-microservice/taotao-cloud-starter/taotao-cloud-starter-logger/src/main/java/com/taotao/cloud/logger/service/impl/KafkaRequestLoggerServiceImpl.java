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
package com.taotao.cloud.logger.service.impl;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.model.RequestLogger;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 审计日志实现类-Kafka
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/2 11:18
 */
public class KafkaRequestLoggerServiceImpl implements IRequestLoggerService {

	public static final String REQUEST_LOG_TOPIC = "request-log-";

	private static final long currentTime = System.currentTimeMillis();
	private static long lastSuccessTime = currentTime;
	private static long lastErrorTime = currentTime;

	private final AtomicLong sendSuccessNum = new AtomicLong(0L);
	private final AtomicLong sendErrorsNum = new AtomicLong(0L);

	private static final int THRESHOLD = 1000;

	@Value("${spring.application.name}")
	private String appName;

	private final KafkaTemplate<String, String> kafkaTemplate;

	public KafkaRequestLoggerServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public void save(RequestLogger requestLogger) {
		if (Objects.nonNull(kafkaTemplate)) {
			String request = JsonUtil.toJSONString(requestLogger);

			ListenableFuture<SendResult<String, String>> future = kafkaTemplate
				.send(REQUEST_LOG_TOPIC + appName, request);

			future.addCallback(new ListenableFutureCallback<>() {
				@Override
				public void onFailure(Throwable throwable) {
					long errorNum = sendErrorsNum.getAndIncrement();
					if (errorNum > 0 && errorNum % THRESHOLD == 0) {
						errorLog(errorNum, "请求日志发送远程记录失败");
					}
				}

				@Override
				public void onSuccess(SendResult<String, String> stringObjectSendResult) {
					long andIncrement = sendSuccessNum.getAndIncrement();
					if (andIncrement > 0 && andIncrement % THRESHOLD == 0) {
						successLog(andIncrement, "请求日志消息发送成功");
					}
				}
			});
		}
	}

	protected void successLog(long num, String msg) {
		long milliseconds = System.currentTimeMillis();

		long seconds = (milliseconds - currentTime) / 1000;
		long minute = seconds / 60;
		long hour = minute / 24;

		long lastSeconds = (milliseconds - lastSuccessTime) / 1000;
		long lastMinute = seconds / 60;
		long lastHour = minute / 24;

		LogUtil.info("KafkaRequestLogger [{}已达 {}条 共用时{}秒 {}分 {}小时, 最近一次用时{}秒 {}分 {}小时]", msg, num, seconds, minute, hour, lastSeconds, lastMinute, lastHour);
		lastSuccessTime = milliseconds;
	}

	protected void errorLog(long num, String msg) {
		long milliseconds = System.currentTimeMillis();

		long seconds = (milliseconds - currentTime) / 1000;
		long minute = seconds / 60;
		long hour = minute / 24;

		long lastSeconds = (milliseconds - lastErrorTime) / 1000;
		long lastMinute = seconds / 60;
		long lastHour = minute / 24;

		LogUtil.error("KafkaRequestLogger [{}已达 {}条 共用时{}秒 {}分 {}小时, 最近一次用时{}秒 {}分 {}小时]", msg, num, seconds, minute, hour, lastSeconds, lastMinute, lastHour);
		lastErrorTime = milliseconds;
	}
}

