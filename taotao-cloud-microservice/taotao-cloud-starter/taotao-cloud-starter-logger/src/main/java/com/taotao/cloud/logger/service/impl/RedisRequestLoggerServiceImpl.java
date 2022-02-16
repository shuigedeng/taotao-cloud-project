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

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.logger.model.RequestLogger;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 审计日志实现类-redis
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 11:18
 */
public class RedisRequestLoggerServiceImpl implements IRequestLoggerService {

	private final RedisRepository redisRepository;

	public RedisRequestLoggerServiceImpl(RedisRepository redisRepository) {
		this.redisRepository = redisRepository;
	}

	@Override
	public void save(RequestLogger requestLogger) {
		String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())
			.format(Instant.now());

		if (Objects.nonNull(redisRepository)) {
			redisRepository.send(RedisConstant.REQUEST_LOG_TOPIC, JsonUtil.toJSONString(requestLogger));

			Long index = redisRepository.leftPush(RedisConstant.REQUEST_LOG + date,
				JsonUtil.toJSONString(requestLogger));
			if (index > 0) {
				//LogUtil.info("redis远程日志记录成功：{}", requestLog);
			} else {
				LogUtil.error("redis远程日志记录失败：{}", requestLogger);
			}
		}
	}
}
