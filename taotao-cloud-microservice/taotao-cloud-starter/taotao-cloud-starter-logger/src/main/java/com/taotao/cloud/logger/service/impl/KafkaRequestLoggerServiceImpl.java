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

import com.google.common.base.Stopwatch;
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
import java.util.concurrent.TimeUnit;
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

	private final Stopwatch currentStopwatch = Stopwatch.createStarted();
	private final Stopwatch lastSuccessStopwatch = Stopwatch.createStarted();
	private final Stopwatch lastErrorStopwatch = Stopwatch.createStarted();

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
						errorLog(errorNum);
					}
				}

				@Override
				public void onSuccess(SendResult<String, String> stringObjectSendResult) {
					long andIncrement = sendSuccessNum.getAndIncrement();
					if (andIncrement > 0 && andIncrement % THRESHOLD == 0) {
						successLog(andIncrement);
					}
				}
			});
		}
	}

	protected void successLog(long num) {
		long hour = currentStopwatch.elapsed(TimeUnit.HOURS);
		long minute = currentStopwatch.elapsed(TimeUnit.MINUTES);
		long seconds = currentStopwatch.elapsed(TimeUnit.SECONDS);

		long lastSeconds = lastSuccessStopwatch.elapsed(TimeUnit.SECONDS);
		long lastMinute = lastSuccessStopwatch.elapsed(TimeUnit.MINUTES);
		long lastHour = lastSuccessStopwatch.elapsed(TimeUnit.HOURS);

		LogUtil.info("KafkaRequestLogger [{}已达 {}条 共用时{}秒 {}分 {}小时, 最近一次用时{}秒 {}分 {}小时]", "请求日志消息发送成功", num, seconds, minute, hour, lastSeconds, lastMinute, lastHour);
		lastSuccessStopwatch.reset().start();
	}

	protected void errorLog(long num) {
		long hour = currentStopwatch.elapsed(TimeUnit.HOURS);
		long minute = currentStopwatch.elapsed(TimeUnit.MINUTES);
		long seconds = currentStopwatch.elapsed(TimeUnit.SECONDS);

		long lastSeconds = lastErrorStopwatch.elapsed(TimeUnit.SECONDS);
		long lastMinute = lastErrorStopwatch.elapsed(TimeUnit.MINUTES);
		long lastHour = lastErrorStopwatch.elapsed(TimeUnit.HOURS);

		LogUtil.error("KafkaRequestLogger [{}已达 {}条 共用时{}秒 {}分 {}小时, 最近一次用时{}秒 {}分 {}小时]", "请求日志发送远程记录失败", num, seconds, minute, hour, lastSeconds, lastMinute, lastHour);
		lastErrorStopwatch.reset().start();
	}
}

