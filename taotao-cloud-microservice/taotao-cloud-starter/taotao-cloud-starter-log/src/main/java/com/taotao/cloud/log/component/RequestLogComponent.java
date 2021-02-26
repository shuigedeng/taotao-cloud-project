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
package com.taotao.cloud.log.component;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.annotation.EnableTaoTaoCloudAsync;
import com.taotao.cloud.log.aspect.RequestLogAspect;
import com.taotao.cloud.log.listener.RequestLogListener;
import com.taotao.cloud.log.properties.RequestLogProperties;
import com.taotao.cloud.log.service.impl.KafkaRequestLogServiceImpl;
import com.taotao.cloud.log.service.impl.LoggerRequestLogServiceImpl;
import com.taotao.cloud.log.service.impl.RedisRequestLogServiceImpl;
import com.taotao.cloud.redis.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:21
 */
@Slf4j
@EnableTaoTaoCloudAsync
public class RequestLogComponent implements InitializingBean {

	@Autowired
	RequestLogProperties requestLogProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.info(StarterNameConstant.TAOTAO_CLOUD_LOG_STARTER + "{0}", "日志模块已启动");
	}

	@Bean
	public RequestLogListener sysLogListener() {
		return new RequestLogListener();
	}

	@Bean
	public RequestLogAspect sysLogAspect(ApplicationEventPublisher publisher) {
		return new RequestLogAspect(publisher);
	}

	@Bean
	//@ConditionalOnProperty(prefix = "taotao.cloud.log", name = "type", havingValue = "logger", matchIfMissing = true)
	public LoggerRequestLogServiceImpl loggerSysLogService() {
		if (determineLogType()) {
			if (determineLogType("logger")) {
				return new LoggerRequestLogServiceImpl();
			}
		}
		return null;
	}


	@Bean
	//@ConditionalOnProperty(prefix = "taotao.cloud.log", name = "type", havingValue = "redis")
	@ConditionalOnBean(value = {RedisRepository.class})
	public RedisRequestLogServiceImpl redisSysLogService() {
		if (determineLogType()) {
			if (determineLogType("redis")) {
				return new RedisRequestLogServiceImpl();
			}
		}
		return null;
	}

	@Bean
	//@ConditionalOnProperty(prefix = "taotao.cloud.log", name = "type", havingValue = "kafka")
	@ConditionalOnClass({KafkaTemplate.class})
	public KafkaRequestLogServiceImpl kafkaSysLogService() {
		if (determineLogType()) {
			if (determineLogType("kafka")) {
				return new KafkaRequestLogServiceImpl();
			}
		}
		return null;
	}

	private boolean determineLogType() {
		String[] types = requestLogProperties.getTypes();
		return types.length != 0;
	}

	private boolean determineLogType(String type) {
		String[] types = requestLogProperties.getTypes();
		for (String s : types) {
			if (type.equals(s)) {
				return true;
			}
		}
		return false;
	}
}

