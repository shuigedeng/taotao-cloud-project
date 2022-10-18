package com.taotao.cloud.kafka.kafka.autoconfigure;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.core.mq.MessageQueueAutoConfiguration;
import com.taotao.cloud.core.mq.MessageQueueConsumer;
import com.taotao.cloud.core.mq.MessageQueueProperties;
import com.taotao.cloud.core.mq.MessageQueueProvider;
import com.taotao.cloud.core.mq.MessageQueueProviderFactory;
import com.taotao.cloud.kafka.kafka.core.KafkaConsumer;
import com.taotao.cloud.kafka.kafka.core.KafkaProvider;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Kafka 自动配置
 */
@AutoConfigureAfter(MessageQueueAutoConfiguration.class)
@ConditionalOnBean(KafkaProperties.class)
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnProperty(prefix = com.taotao.cloud.kafka.properties.KafkaProperties.PREFIX, name = "enabled", havingValue = "true")
@Configuration(proxyBeanMethods = false)
public class KafkaMessageQueueAutoConfiguration {

	public static final String TYPE = "KAFKA";

	public static final String BEAN_CONSUMER = "kafkaConsumer";

	public static final String BEAN_PROVIDER = "kafkaProvider";

	public static final String AUTOWIRED_KAKFA_CONSUMER = "Autowired KafkaConsumer";

	public static final String AUTOWIRED_KAFKA_PROVIDER = "Autowired KafkaProvider";

	@Bean(BEAN_CONSUMER)
	public KafkaConsumer kafkaConsumer(MessageQueueProperties messageQueueProperties,
		KafkaProperties kafkaProperties,
		ObjectProvider<List<MessageQueueConsumer>> messageListeners,
		ObjectProvider<ConsumerFactory<String, String>> consumerFactory,
		AsyncThreadPoolTaskExecutor taskExecutor) {
		LogUtils.debug(AUTOWIRED_KAKFA_CONSUMER);
		return new KafkaConsumer(messageQueueProperties, kafkaProperties,
			messageListeners.getIfAvailable(),
			consumerFactory.getIfAvailable(), taskExecutor);
	}

	@Bean(BEAN_PROVIDER)
	public MessageQueueProvider messageQueueProvider(KafkaTemplate<String, String> kafkaTemplate) {
		LogUtils.debug(AUTOWIRED_KAFKA_PROVIDER);
		MessageQueueProviderFactory.addBean(TYPE, BEAN_PROVIDER);
		return new KafkaProvider(kafkaTemplate);
	}
}
