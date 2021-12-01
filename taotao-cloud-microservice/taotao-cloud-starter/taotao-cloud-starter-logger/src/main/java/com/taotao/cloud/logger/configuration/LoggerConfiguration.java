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
package com.taotao.cloud.logger.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.logger.aspect.RequestLoggerAspect;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.logger.listener.RequestLoggerListener;
import com.taotao.cloud.logger.properties.LoggerProperties;
import com.taotao.cloud.logger.properties.RequestLoggerProperties;
import com.taotao.cloud.logger.service.impl.KafkaRequestLoggerServiceImpl;
import com.taotao.cloud.logger.service.impl.LoggerRequestLoggerServiceImpl;
import com.taotao.cloud.logger.service.impl.RedisRequestLoggerServiceImpl;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Arrays;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties({RequestLoggerProperties.class, LoggerProperties.class})
@ConditionalOnProperty(prefix = LoggerProperties.PREFIX, name = "enabled", havingValue = "true")
public class LoggerConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(LoggerConfiguration.class, StarterName.LOG_STARTER);
	}

	/**
	 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/4/30 10:21
	 */
	@Configuration
	@ConditionalOnProperty(prefix = RequestLoggerProperties.PREFIX, name = "enabled", havingValue = "true")
	public static class RequestLoggerConfiguration implements InitializingBean {

		@Override
		public void afterPropertiesSet() throws Exception {
			LogUtil.started(RequestLoggerConfiguration.class, StarterName.LOG_STARTER);
		}

		@Autowired
		private RequestLoggerProperties properties;

		@Bean
		public RequestLoggerListener requestLogListener() {
			LogUtil.started(RequestLoggerListener.class, StarterName.LOG_STARTER);
			return new RequestLoggerListener();
		}

		@Bean
		public RequestLoggerAspect requestLogAspect() {
			LogUtil.started(RequestLoggerAspect.class, StarterName.LOG_STARTER);
			return new RequestLoggerAspect();
		}

		@Bean
		public LoggerRequestLoggerServiceImpl loggerRequestLogService() {
			LogUtil.started(LoggerRequestLoggerServiceImpl.class, StarterName.LOG_STARTER);
			if (determineLogType(RequestLoggerTypeEnum.LOGGER)) {
				return new LoggerRequestLoggerServiceImpl();
			}
			return null;
		}

		@Bean
		public RedisRequestLoggerServiceImpl redisRequestLogService(
			RedisRepository redisRepository) {
			LogUtil.started(RedisRequestLoggerServiceImpl.class, StarterName.LOG_STARTER);
			if (determineLogType(RequestLoggerTypeEnum.REDIS)) {
				return new RedisRequestLoggerServiceImpl(redisRepository);
			}
			return null;
		}

		@Bean
		public KafkaRequestLoggerServiceImpl kafkaRequestLogService(
			KafkaTemplate<String, String> kafkaTemplate) {
			LogUtil.started(KafkaRequestLoggerServiceImpl.class, StarterName.LOG_STARTER);
			if (determineLogType(RequestLoggerTypeEnum.KAFKA)) {
				return new KafkaRequestLoggerServiceImpl(kafkaTemplate);
			}
			return null;
		}

		private boolean determineLogType(RequestLoggerTypeEnum requestLoggerTypeEnum) {
			RequestLoggerTypeEnum[] types = properties.getTypes();
			assert types != null;
			return Arrays.stream(types)
				.anyMatch(type -> type.getCode() == requestLoggerTypeEnum.getCode());
		}
	}
}

