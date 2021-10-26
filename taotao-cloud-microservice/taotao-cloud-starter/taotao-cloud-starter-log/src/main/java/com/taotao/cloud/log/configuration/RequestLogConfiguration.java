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
package com.taotao.cloud.log.configuration;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.log.aspect.RequestLogAspect;
import com.taotao.cloud.log.enums.LogTypeEnum;
import com.taotao.cloud.log.listener.RequestLogListener;
import com.taotao.cloud.log.properties.RequestLogProperties;
import com.taotao.cloud.log.service.impl.KafkaRequestLogServiceImpl;
import com.taotao.cloud.log.service.impl.LoggerRequestLogServiceImpl;
import com.taotao.cloud.log.service.impl.RedisRequestLogServiceImpl;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:21
 */
@Configuration
@ConditionalOnProperty(prefix = RequestLogProperties.PREFIX, name = "enabled", havingValue = "true")
public class RequestLogConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RequestLogConfiguration.class, StarterNameConstant.LOG_STARTER);
	}

	@Autowired
	private RequestLogProperties properties;

	@Bean
	public RequestLogListener sysLogListener() {
		LogUtil.started(RequestLogListener.class, StarterNameConstant.LOG_STARTER);
		return new RequestLogListener();
	}

	@Bean
	public RequestLogAspect sysLogAspect() {
		LogUtil.started(RequestLogAspect.class, StarterNameConstant.LOG_STARTER);
		return new RequestLogAspect();
	}

	@Bean
	public LoggerRequestLogServiceImpl loggerSysLogService() {
		LogUtil.started(LoggerRequestLogServiceImpl.class, StarterNameConstant.LOG_STARTER);
		if (determineLogType(LogTypeEnum.LOGGER)) {
			return new LoggerRequestLogServiceImpl();
		}
		return null;
	}

	@Bean
	public RedisRequestLogServiceImpl redisSysLogService(RedisRepository redisRepository) {
		LogUtil.started(RedisRequestLogServiceImpl.class, StarterNameConstant.LOG_STARTER);
		if (determineLogType(LogTypeEnum.REDIS)) {
			return new RedisRequestLogServiceImpl(redisRepository);
		}
		return null;
	}

	@Bean
	public KafkaRequestLogServiceImpl kafkaSysLogService(KafkaTemplate<String, String> kafkaTemplate) {
		LogUtil.started(KafkaRequestLogServiceImpl.class, StarterNameConstant.LOG_STARTER);
		if (determineLogType(LogTypeEnum.KAFKA)) {
			return new KafkaRequestLogServiceImpl(kafkaTemplate);
		}
		return null;
	}

	private boolean determineLogType(LogTypeEnum logTypeEnum) {
		LogTypeEnum[] types = properties.getTypes();
		assert types != null;
		return Arrays.stream(types)
			.anyMatch(type -> type.getCode() == logTypeEnum.getCode());
	}
}

