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

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.model.DatePattern;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.model.RequestLogger;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import com.taotao.cloud.redis.repository.RedisRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 审计日志实现类-redis
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/2 11:18
 */
public class RedisRequestLoggerServiceImpl implements IRequestLoggerService {

	private final RedisRepository redisRepository;
	private static final int THRESHOLD = 1000;

	private final AtomicLong sendSuccessNum = new AtomicLong(0L);
	private final AtomicLong sendErrorsNum = new AtomicLong(0L);

	public RedisRequestLoggerServiceImpl(RedisRepository redisRepository) {
		this.redisRepository = redisRepository;
	}

	@Override
	public void save(RequestLogger requestLogger) {
		String date = DateUtil.format(LocalDate.now(), DatePattern.COLON_DATE_PATTERN);

		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.REQUEST_LOG_TOPIC, requestLogger);

			Long index = redisRepository.leftPush(RedisConstant.REQUEST_LOG + date, requestLogger);
			if (index > 0) {
				long andIncrement = sendSuccessNum.getAndIncrement();
				if (andIncrement > 0 && andIncrement % THRESHOLD == 0) {
					LogUtil.info("RedisRequestLogger 远程日志记录成功：成功条数：{}", andIncrement);
				}
			} else {
				long andIncrement = sendErrorsNum.getAndIncrement();
				if (andIncrement > 0 && andIncrement % THRESHOLD == 0) {
					LogUtil.error("RedisRequestLogger 远程日志记录失败：失败条数：{}", andIncrement);
				}
			}
		}
	}
}
