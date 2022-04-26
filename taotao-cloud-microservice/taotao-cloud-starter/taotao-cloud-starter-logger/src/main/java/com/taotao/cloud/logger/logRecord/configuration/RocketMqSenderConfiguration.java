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
package com.taotao.cloud.logger.logRecord.configuration;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.logRecord.constants.LogConstants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


/**
 * 火箭mq发送器配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:44:40
 */
@Configuration
@ConditionalOnProperty(name = "log-record.data-pipeline", havingValue = LogConstants.DataPipeline.ROCKET_MQ)
@EnableConfigurationProperties({LogRecordProperties.class})
public class RocketMqSenderConfiguration {

	/**
	 * namesrv addr
	 */
	private String namesrvAddr;
	/**
	 * 组名称
	 */
	private String groupName;
	/**
	 * 最大消息大小
	 */
	private int maxMessageSize;
	/**
	 * 味精发送超时
	 */
	private int sendMsgTimeout;
	/**
	 * 重试发送失败时候
	 */
	private int retryTimesWhenSendFailed;
	/**
	 * 主题
	 */
	private String topic;

	/**
	 * 属性
	 */
	@Autowired
    private LogRecordProperties properties;

	/**
	 * 兔子mq配置
	 *
	 * @since 2022-04-26 14:44:40
	 */
	@PostConstruct
    public void rabbitMqConfig() {
        this.namesrvAddr = properties.getRocketMqProperties().getNamesrvAddr();
        this.groupName = properties.getRocketMqProperties().getGroupName();
        this.maxMessageSize = properties.getRocketMqProperties().getMaxMessageSize();
        this.sendMsgTimeout = properties.getRocketMqProperties().getSendMsgTimeout();
        this.retryTimesWhenSendFailed = properties.getRocketMqProperties().getRetryTimesWhenSendFailed();
        this.topic = properties.getRocketMqProperties().getTopic();
        LogUtil.info("LogRecord RocketMqSenderConfiguration namesrvAddr [{}] groupName [{}] maxMessageSize [{}] sendMsgTimeout [{}] retryTimesWhenSendFailed [{}] topic [{}]",
                namesrvAddr, groupName, maxMessageSize, sendMsgTimeout, retryTimesWhenSendFailed, topic);
    }

	/**
	 * 默认mq生产商
	 *
	 * @return {@link DefaultMQProducer }
	 * @since 2022-04-26 14:44:40
	 */
	@Bean
    public DefaultMQProducer defaultMqProducer() throws RuntimeException {
        DefaultMQProducer producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setCreateTopicKey(this.topic);
        // 如果需要同一个 jvm 中不同的 producer 往不同的 mq 集群发送消息，需要设置不同的 instanceName
        //producer.setInstanceName(instanceName);
        // 如果发送消息的最大限制 默认为0
        producer.setMaxMessageSize(this.maxMessageSize);
        // 如果发送消息超时时间 默认为3000
        producer.setSendMsgTimeout(this.sendMsgTimeout);
        // 如果发送消息失败，设置重试次数，默认为2
        producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        try {
            producer.start();
	        LogUtil.info("LogRecord RocketMq producer is started");
        } catch (MQClientException e) {
	        LogUtil.error("LogRecord failed to start RocketMq producer", e);
            throw new RuntimeException(e);
        }
        return producer;
    }
}
