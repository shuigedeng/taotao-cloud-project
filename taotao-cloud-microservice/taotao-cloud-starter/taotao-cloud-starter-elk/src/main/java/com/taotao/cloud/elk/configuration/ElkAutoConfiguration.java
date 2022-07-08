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
package com.taotao.cloud.elk.configuration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.elk.filter.LogStatisticsFilter;
import com.taotao.cloud.elk.properties.ElkHealthLogStatisticProperties;
import com.taotao.cloud.elk.properties.ElkProperties;
import com.taotao.cloud.elk.properties.ElkWebAspectProperties;
import com.taotao.cloud.elk.properties.ElkWebProperties;
import javax.annotation.Resource;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ElkConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/3 10:43
 */
@AutoConfiguration
@EnableConfigurationProperties({
	ElkProperties.class,
	ElkWebAspectProperties.class,
	ElkHealthLogStatisticProperties.class,
	ElkWebProperties.class
})
@ConditionalOnProperty(prefix = ElkProperties.PREFIX, name = "enabled", havingValue = "true")
public class ElkAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ElkAutoConfiguration.class, StarterName.ELK_STARTER);
	}

	@Resource
	private ElkProperties elkProperties;

	@Resource
	private LogStatisticsFilter logStatisticsFilter;

	@Bean(initMethod = "start", destroyMethod = "stop")
	public LogstashTcpSocketAppender logstashTcpSocketAppender() {
		LogstashTcpSocketAppender appender = new LogstashTcpSocketAppender();
		String[] destinations = elkProperties.getDestinations();
		if (elkProperties.getDestinations() == null || elkProperties.getDestinations().length == 0) {
			throw new BaseException("未设置elk地址");
		}

		for (String destination : destinations) {
			appender.addDestination(destination);
		}
		appender.setEncoder(createEncoder());

		ILoggerFactory factory = LoggerFactory.getILoggerFactory();
		if (factory instanceof LoggerContext context) {
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
	@ConditionalOnProperty(prefix = ElkHealthLogStatisticProperties.PREFIX, name = "enabled", havingValue = "true")
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
