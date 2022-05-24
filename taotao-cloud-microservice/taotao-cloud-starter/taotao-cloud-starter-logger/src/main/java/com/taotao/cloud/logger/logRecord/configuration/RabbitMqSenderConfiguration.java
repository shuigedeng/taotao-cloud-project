///*
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.logger.logRecord.configuration;
//
//import com.taotao.cloud.common.utils.log.LogUtil;
//import com.taotao.cloud.logger.logRecord.constants.LogConstants;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//
//
///**
// * 兔子mq发送器配置
// *
// * @author shuigedeng
// * @version 2022.04
// * @since 2022-04-26 14:44:47
// */
//@AutoConfiguration
//@ConditionalOnProperty(name = "log-record.data-pipeline", havingValue = LogConstants.DataPipeline.RABBIT_MQ)
//@EnableConfigurationProperties({LogRecordProperties.class})
//public class RabbitMqSenderConfiguration {
//
//	/**
//	 * 兔子主机
//	 */
//	private String rabbitHost;
//	/**
//	 * 兔子港口
//	 */
//	private int rabbitPort;
//	/**
//	 * 交换
//	 */
//	private String exchange;
//	/**
//	 * 队列
//	 */
//	private String queue;
//	/**
//	 * 路由关键
//	 */
//	private String routingKey;
//	/**
//	 * 用户名
//	 */
//	private String username;
//	/**
//	 * 密码
//	 */
//	private String password;
//
//	/**
//	 * 属性
//	 */
//	@Autowired
//    private LogRecordProperties properties;
//
//	/**
//	 * 兔子mq配置
//	 *
//	 * @since 2022-04-26 14:44:47
//	 */
//	@PostConstruct
//    public void rabbitMqConfig() {
//        this.rabbitHost = properties.getRabbitMqProperties().getHost();
//        this.rabbitPort = properties.getRabbitMqProperties().getPort();
//        this.queue = properties.getRabbitMqProperties().getQueueName();
//        this.routingKey = properties.getRabbitMqProperties().getRoutingKey();
//        this.exchange= properties.getRabbitMqProperties().getExchangeName();
//        this.username= properties.getRabbitMqProperties().getUsername();
//        this.password= properties.getRabbitMqProperties().getPassword();
//        LogUtil.info("LogRecord RabbitMqSenderConfiguration host [{}] port [{}] exchange [{}] queue [{}] routingKey [{}]",
//                rabbitHost, rabbitPort, exchange, queue, routingKey);
//    }
//
//	/**
//	 * 兔子连接工厂
//	 *
//	 * @return {@link ConnectionFactory }
//	 * @since 2022-04-26 14:44:47
//	 */
//	@Bean
//    ConnectionFactory rabbitConnectionFactory() {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitHost, rabbitPort);
//        cachingConnectionFactory.setUsername(username);
//        cachingConnectionFactory.setPassword(password);
//        return cachingConnectionFactory;
//    }
//
//	/**
//	 * 土里土气交换
//	 *
//	 * @return {@link DirectExchange }
//	 * @since 2022-04-26 14:44:47
//	 */
//	@Bean
//    DirectExchange rubeExchange() {
//        return new DirectExchange(exchange, true, false);
//    }
//
//	/**
//	 * 土里土气队列
//	 *
//	 * @return {@link Queue }
//	 * @since 2022-04-26 14:44:47
//	 */
//	@Bean
//    public Queue rubeQueue() {
//        return new Queue(queue, true);
//    }
//
//	/**
//	 * 土包子交换绑定
//	 *
//	 * @param rubeExchange 土里土气交换
//	 * @param rubeQueue    土里土气队列
//	 * @return {@link Binding }
//	 * @since 2022-04-26 14:44:47
//	 */
//	@Bean
//    Binding rubeExchangeBinding(DirectExchange rubeExchange, Queue rubeQueue) {
//        return BindingBuilder.bind(rubeQueue).to(rubeExchange).with(routingKey);
//    }
//
//	/**
//	 * 土包子交换模板
//	 *
//	 * @param rabbitConnectionFactory 兔子连接工厂
//	 * @return {@link RabbitTemplate }
//	 * @since 2022-04-26 14:44:47
//	 */
//	@Bean
//    public RabbitTemplate rubeExchangeTemplate(ConnectionFactory rabbitConnectionFactory) {
//        RabbitTemplate r = new RabbitTemplate(rabbitConnectionFactory);
//        r.setExchange(exchange);
//        r.setRoutingKey(routingKey);
//        r.setConnectionFactory(rabbitConnectionFactory);
//        return r;
//    }
//}
