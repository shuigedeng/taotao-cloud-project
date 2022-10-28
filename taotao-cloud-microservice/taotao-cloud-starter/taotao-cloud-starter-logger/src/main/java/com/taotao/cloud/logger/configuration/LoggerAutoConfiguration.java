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
package com.taotao.cloud.logger.configuration;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.annotation.ConditionalOnRequestLoggerType;
import com.taotao.cloud.logger.aspect.RequestLoggerAspect;
import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import com.taotao.cloud.logger.listener.RequestLoggerListener;
import com.taotao.cloud.logger.properties.LoggerProperties;
import com.taotao.cloud.logger.properties.RequestLoggerProperties;
import com.taotao.cloud.logger.service.IRequestLoggerService;
import com.taotao.cloud.logger.service.impl.KafkaRequestLoggerServiceImpl;
import com.taotao.cloud.logger.service.impl.LoggerRequestLoggerServiceImpl;
import com.taotao.cloud.logger.service.impl.RedisRequestLoggerServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:21
 */
@AutoConfiguration
@EnableConfigurationProperties({RequestLoggerProperties.class, LoggerProperties.class})
@ConditionalOnProperty(prefix = LoggerProperties.PREFIX, name = "enabled", havingValue = "true")
public class LoggerAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(LoggerAutoConfiguration.class, StarterName.LOG_STARTER);
	}

	/**
	 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
	 *
	 * @author shuigedeng
	 * @version 2022.03
	 * @since 2020/4/30 10:21
	 */
	@Configuration
	@ConditionalOnProperty(prefix = RequestLoggerProperties.PREFIX, name = "enabled", havingValue = "true")
	public static class RequestLoggerConfiguration implements InitializingBean {

		@Override
		public void afterPropertiesSet() throws Exception {
			LogUtils.started(RequestLoggerConfiguration.class, StarterName.LOG_STARTER);
		}

		@Bean
		@ConditionalOnRequestLoggerType(logType = RequestLoggerTypeEnum.LOGGER)
		public IRequestLoggerService loggerRequestLoggerService() {
			return new LoggerRequestLoggerServiceImpl();
		}

		@Bean
		@ConditionalOnClass(RedisRepository.class)
		@ConditionalOnBean(RedisRepository.class)
		@ConditionalOnRequestLoggerType(logType = RequestLoggerTypeEnum.REDIS)
		public IRequestLoggerService redisRequestLoggerService(RedisRepository redisRepository) {
			return new RedisRequestLoggerServiceImpl(redisRepository);
		}

		@Bean
		@ConditionalOnClass(KafkaTemplate.class)
		@ConditionalOnBean(KafkaTemplate.class)
		@ConditionalOnRequestLoggerType(logType = RequestLoggerTypeEnum.KAFKA)
		public IRequestLoggerService kafkaRequestLoggerService(KafkaTemplate<String, String> kafkaTemplate) {
			return new KafkaRequestLoggerServiceImpl(kafkaTemplate);
		}

		@Bean
		public RequestLoggerListener requestLoggerListener(List<IRequestLoggerService> requestLoggerServices) {
			return new RequestLoggerListener(requestLoggerServices);
		}

		@Bean
		public RequestLoggerAspect requestLoggerAspect() {
			return new RequestLoggerAspect();
		}

		// @Bean
		// public List<IRequestLoggerService> requestLoggerServices() {
		// 	List<IRequestLoggerService> requestLoggerServices = new ArrayList<>();
		// 	RequestLoggerTypeEnum[] types = properties.getTypes();
		// 	for (RequestLoggerTypeEnum type : types) {
		// 		if (RequestLoggerTypeEnum.LOGGER.equals(type)) {
		// 			requestLoggerServices.add(new LoggerRequestLoggerServiceImpl());
		// 		}
		// 		if (RequestLoggerTypeEnum.REDIS.equals(type)) {
		// 			requestLoggerServices.add(new RedisRequestLoggerServiceImpl(redisRepository));
		// 		}
		// 		if (RequestLoggerTypeEnum.KAFKA.equals(type)) {
		// 			requestLoggerServices.add(new KafkaRequestLoggerServiceImpl(kafkaTemplate));
		// 		}
		// 	}
		//
		// 	return requestLoggerServices;
		// }
	}


}

