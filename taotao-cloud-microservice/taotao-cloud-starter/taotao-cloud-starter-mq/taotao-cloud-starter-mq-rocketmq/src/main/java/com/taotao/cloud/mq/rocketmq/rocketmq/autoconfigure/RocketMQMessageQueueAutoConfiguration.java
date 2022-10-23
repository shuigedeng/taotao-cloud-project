package com.taotao.cloud.mq.rocketmq.rocketmq.autoconfigure;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.mq.common.MessageQueueAutoConfiguration;
import com.taotao.cloud.mq.common.MessageQueueConsumer;
import com.taotao.cloud.mq.common.MessageQueueProperties;
import com.taotao.cloud.mq.common.MessageQueueProvider;
import com.taotao.cloud.mq.common.MessageQueueProviderFactory;
import com.taotao.cloud.mq.rocketmq.properties.RocketmqProperties;
import com.taotao.cloud.mq.rocketmq.rocketmq.core.RocketMQConsumer;
import com.taotao.cloud.mq.rocketmq.rocketmq.core.RocketMQProvider;
import com.taotao.cloud.mq.rocketmq.rocketmq.env.FixedRocketMQConsumerProperties;
import com.taotao.cloud.mq.rocketmq.rocketmq.env.FixedRocketMQProducerProperties;
import java.util.List;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ 自动配置
 */
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@ConditionalOnBean(RocketMQProperties.class)
@ConditionalOnClass(RocketMQTemplate.class)
@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties({FixedRocketMQProducerProperties.class,
	FixedRocketMQConsumerProperties.class})
@Configuration(proxyBeanMethods = false)
public class RocketMQMessageQueueAutoConfiguration {

	public static final String ENABLED = "rocketmq.enabled";

	public static final String TYPE = "ROCKETMQ";

	public static final String BEAN_CONSUMER = "rocketMQConsumer";

	public static final String BEAN_PROVIDER = "rocketMQProvider";

	public static final String AUTOWIRED_ROCKET_MQ_CONSUMER = "Autowired RocketMQConsumer";

	public static final String AUTOWIRED_ROCKET_MQ_PROVIDER = "Autowired RocketMQProvider";

	@Bean(BEAN_CONSUMER)
	public RocketMQConsumer rocketMQConsumer(MessageQueueProperties messageQueueProperties,
		RocketMQProperties rocketMQProperties,
		FixedRocketMQConsumerProperties fixedRocketMQConsumerProperties,
		ObjectProvider<List<MessageQueueConsumer>> messageListeners) {
		LogUtils.debug(AUTOWIRED_ROCKET_MQ_CONSUMER);
		return new RocketMQConsumer(messageQueueProperties, rocketMQProperties,
			fixedRocketMQConsumerProperties,
			messageListeners.getIfAvailable());
	}

	@Bean(BEAN_PROVIDER)
	public MessageQueueProvider messageQueueProvider(RocketMQTemplate rocketMQTemplate,
		FixedRocketMQProducerProperties fixedRocketMQProducerProperties) {
		LogUtils.debug(AUTOWIRED_ROCKET_MQ_PROVIDER);
		MessageQueueProviderFactory.addBean(TYPE, BEAN_PROVIDER);
		return new RocketMQProvider(rocketMQTemplate, fixedRocketMQProducerProperties);
	}
}
