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
package com.taotao.cloud.mq.rabbitmq.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.mq.rabbitmq.producer.FastBuildRabbitMqProducer;
import com.taotao.cloud.mq.rabbitmq.properties.RabbitMQProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * RabbitMQ配置类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/28 17:17
 */
@AutoConfiguration
@ConditionalOnClass(FastBuildRabbitMqProducer.class)
@EnableConfigurationProperties(RabbitMQProperties.class)
@ConditionalOnProperty(prefix = RabbitMQProperties.PREFIX, name = "enabled", havingValue = "true")
public class RabbitMQAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(RabbitMQAutoConfiguration.class, StarterName.MQ_RABBITMQ_STARTER);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		LogUtils.started(RabbitTemplate.class, StarterName.MQ_RABBITMQ_STARTER);
		return new RabbitTemplate(connectionFactory);
	}

	@Bean
	@ConditionalOnMissingBean
	public ConnectionFactory connectionFactory(RabbitMQProperties rabbitMqProperties) {
		LogUtils.started(ConnectionFactory.class, StarterName.MQ_RABBITMQ_STARTER);
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(rabbitMqProperties.getAddresses());
		connectionFactory.setUsername(rabbitMqProperties.getUsername());
		connectionFactory.setPassword(rabbitMqProperties.getPassword());
		connectionFactory.setVirtualHost(rabbitMqProperties.getVirtualHost());
		connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
		return connectionFactory;
	}

	@Bean
	@ConditionalOnMissingBean
	public FastBuildRabbitMqProducer fastRabbitMqProducer(ConnectionFactory connectionFactory) {
		LogUtils.started(FastBuildRabbitMqProducer.class, StarterName.MQ_RABBITMQ_STARTER);
		return new FastBuildRabbitMqProducer(connectionFactory);
	}
}
