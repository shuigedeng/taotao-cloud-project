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
package com.taotao.cloud.log.service.impl;

import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.log.model.RequestLog;
import com.taotao.cloud.log.service.IRequestLogService;
import com.taotao.cloud.redis.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 审计日志实现类-redis
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 11:18
 */
@Slf4j
public class RedisRequestLogServiceImpl implements IRequestLogService {

	private static final String SYS_LOG = "sys:log:request:";

	@Resource
	private RedisRepository redisRepository;

	@Override
	public void save(RequestLog requestLog) {
		String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())
			.format(Instant.now());
		Long index = redisRepository.leftPush(SYS_LOG + date, JsonUtil.toJSONString(requestLog));
		if (index > 0) {
			log.info("redis远程日志记录成功：{}", requestLog);
		} else {
			log.error("redis远程日志记录失败：{}", requestLog);
		}
	}
}
