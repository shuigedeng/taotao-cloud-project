package com.taotao.cloud.mq.common;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁操作自动装配
 */
@ConditionalOnProperty(name = MessageQueueProperties.ENABLED, matchIfMissing = true)
@EnableConfigurationProperties(MessageQueueProperties.class)
@Configuration(proxyBeanMethods = false)
public class MessageQueueAutoConfiguration {

	public static final String AUTOWIRED_MESSAGE_QUEUE_FACTORY = "Autowired MessageQueueProviderFactory";

	private final MessageQueueProperties messageQueueProperties;

	public MessageQueueAutoConfiguration(MessageQueueProperties messageQueueProperties) {
		this.messageQueueProperties = messageQueueProperties;
	}

	@Bean
	public MessageQueueProviderFactory messageQueueProviderFactory() {
		LogUtils.debug(AUTOWIRED_MESSAGE_QUEUE_FACTORY);
		return new MessageQueueProviderFactory(messageQueueProperties.getType());
	}
}
