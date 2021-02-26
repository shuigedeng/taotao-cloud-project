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
package com.taotao.cloud.rabbitmq.component;

import com.taotao.cloud.rabbitmq.producer.FastBuildRabbitMqProducer;
import com.taotao.cloud.rabbitmq.properties.RabbitMQProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/28 17:17
 */
@Configuration
@ConditionalOnClass(FastBuildRabbitMqProducer.class)
@ConditionalOnProperty(prefix = "taotao.cloud.rabbitmq", name = "enabled", havingValue = "true")
public class RabbitMqComponent {

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

	@Bean
	@ConditionalOnMissingBean
	public ConnectionFactory connectionFactory(RabbitMQProperties rabbitMqProperties) {
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
	@ConditionalOnProperty(prefix = "taotao.cloud.rabbitmq", value = "enabled", havingValue = "true")
	public FastBuildRabbitMqProducer fastRabbitMqProducer(ConnectionFactory connectionFactory) {
		return new FastBuildRabbitMqProducer(connectionFactory);
	}
}
