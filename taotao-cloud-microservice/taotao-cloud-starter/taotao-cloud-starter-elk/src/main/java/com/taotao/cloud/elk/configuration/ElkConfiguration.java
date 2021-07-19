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
package com.taotao.cloud.elk.configuration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.elk.component.LogStatisticsFilter;
import com.taotao.cloud.elk.constant.ElkConstant;
import com.taotao.cloud.elk.properties.ElkProperties;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * ElkConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/3 10:43
 */
@Slf4j
@ConditionalOnProperty(
	prefix = ElkConstant.BASE_ELK_PREFIX,
	name = ElkConstant.ENABLED,
	havingValue = ElkConstant.TRUE
)
public class ElkConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info(
			"[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_ELK_STARTER + "]" + "elk模块已启动");
	}

	@Resource
	private ElkProperties elkProperties;

	@Resource
	private LogStatisticsFilter logStatisticsFilter;

	@Bean(initMethod = "start", destroyMethod = "stop")
	public LogstashTcpSocketAppender logstashTcpSocketAppender() {
		LogstashTcpSocketAppender appender = new LogstashTcpSocketAppender();
		String[] destinations = elkProperties.getDestinations();
		if (elkProperties.getDestinations() == null
			|| elkProperties.getDestinations().length == 0) {
			throw new BaseException("未设置elk地址");
		}

		for (String destination : destinations) {
			appender.addDestination(destination);
		}
		appender.setEncoder(createEncoder());

		ILoggerFactory factory = LoggerFactory.getILoggerFactory();
		if (factory instanceof LoggerContext) {
			LoggerContext context = ((LoggerContext) factory);
			appender.setContext(context);
			context.getLogger("ROOT").addAppender(appender);
		}

		if (logStatisticsFilter != null) {
			//增加错误日志统计拦截
			appender.addFilter(logStatisticsFilter);
		}

		return appender;
	}

	@Bean
	@ConditionalOnProperty(
		prefix = ElkConstant.BASE_ELK_LOG_STATISTIC_PREFIX,
		name = ElkConstant.ENABLED,
		havingValue = ElkConstant.TRUE
	)
	LogStatisticsFilter getLogStatisticsFilter() {
		return new LogStatisticsFilter();
	}

	private Encoder<ILoggingEvent> createEncoder() {
		LogstashEncoder encoder = new LogstashEncoder();
		String appName = elkProperties.getAppName();
		if (StrUtil.isBlank(appName)) {
			appName = elkProperties.getSpringAppName();
		}

		if (StrUtil.isBlank(appName)) {
			throw new BaseException("缺少appName配置");
		}
		encoder.setCustomFields("{\"appname\":\"" + appName + "\",\"appindex\":\"applog\"}");
		encoder.setEncoding("UTF-8");

		return encoder;
	}
}
