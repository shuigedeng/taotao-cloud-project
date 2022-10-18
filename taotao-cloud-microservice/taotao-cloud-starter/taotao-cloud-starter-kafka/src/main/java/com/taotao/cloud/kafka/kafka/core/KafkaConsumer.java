package com.taotao.cloud.kafka.kafka.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.mq.Message;
import com.taotao.cloud.core.mq.MessageQueueConsumer;
import com.taotao.cloud.core.mq.MessageQueueListener;
import com.taotao.cloud.core.mq.MessageQueueProperties;
import com.taotao.cloud.kafka.kafka.autoconfigure.KafkaMessageQueueAutoConfiguration;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * RocketMQ 消费者
 */
public class KafkaConsumer implements InitializingBean, DisposableBean {

	private static final String KAFKA_CONSUMER_PROCESSOR_CONSUME_ERROR = "KafkaConsumerProcessor consume error: {}";

	public static final String INITIALIZING_KAFKA_CONSUMER = "Initializing KafkaConsumer";

	public static final String DESTROY_KAFKA_CONSUMER = "Destroy KafkaConsumer";

	public static final String CREATE_CONSUMER_FROM_CONSUMER_FACTORY_GROUP_TOPIC = "Create consumer from consumerFactory, group: {}, topic: {}";

	private final List<Consumer<String, String>> consumers = Lists.newArrayList();

	private final MessageQueueProperties messageQueueProperties;

	private final KafkaProperties kafkaProperties;

	private final List<MessageQueueConsumer> messageQueueConsumers;

	private final ConsumerFactory<String, String> consumerFactory;

	private final TaskExecutor taskExecutor;

	public KafkaConsumer(MessageQueueProperties messageQueueProperties,
						 KafkaProperties kafkaProperties, List<MessageQueueConsumer> messageQueueConsumers,
						 ConsumerFactory<String, String> consumerFactory, TaskExecutor taskExecutor) {
		this.messageQueueProperties = messageQueueProperties;
		this.kafkaProperties = kafkaProperties;
		this.messageQueueConsumers = messageQueueConsumers;
		this.consumerFactory = consumerFactory;
		this.taskExecutor = taskExecutor;
	}


	@SuppressWarnings("InfiniteLoopStatement")
	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.debug(INITIALIZING_KAFKA_CONSUMER);
		if (CollectionUtils.isEmpty(messageQueueConsumers)) {
			return;
		}
		for (MessageQueueConsumer messageQueueConsumer : messageQueueConsumers) {
			Consumer<String, String> consumer = createConsumer(messageQueueConsumer);
			if (consumer == null) {
				continue;
			}
			consumers.add(consumer);
			taskExecutor.execute(() -> {
				while (true) {
					try {
						ConsumerRecords<String, String> consumerRecords =
							consumer.poll(kafkaProperties.getConsumer().getFetchMaxWait());
						if (consumerRecords == null || consumerRecords.isEmpty()) {
							continue;
						}
						int maxPollRecords = kafkaProperties.getConsumer().getMaxPollRecords();
						Map<TopicPartition, OffsetAndMetadata> offsets = Maps.newHashMapWithExpectedSize(
							maxPollRecords);
						List<Message> messages = Lists.newArrayListWithCapacity(
							consumerRecords.count());
						consumerRecords.forEach(record -> {
							offsets.put(new TopicPartition(record.topic(), record.partition()),
								new OffsetAndMetadata(record.offset() + 1));

							Message message = new Message();
							message.setTopic(record.topic());
							message.setPartition(record.partition());
							message.setKey(record.key());
							message.setBody(record.value());
							messages.add(message);
						});
						messageQueueConsumer.consume(messages, () -> consumer.commitSync(offsets));
					} catch (Exception e) {
						LogUtils.error(KAFKA_CONSUMER_PROCESSOR_CONSUME_ERROR, e.getMessage(), e);
					}
				}
			});
		}
	}

	@Override
	public void destroy() {
		LogUtils.debug(DESTROY_KAFKA_CONSUMER);
		consumers.forEach(Consumer::unsubscribe);
	}

	private Consumer<String, String> createConsumer(MessageQueueConsumer messageQueueConsumer) {
		Class<? extends MessageQueueConsumer> clazz = messageQueueConsumer.getClass();
		MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);

		if (StringUtils.isNotBlank(annotation.type()) &&
			!messageQueueProperties.getType().equalsIgnoreCase(annotation.type())) {
			return null;
		}

		if (StringUtils.isBlank(annotation.type()) &&
			!KafkaMessageQueueAutoConfiguration.TYPE.equalsIgnoreCase(
				messageQueueProperties.getType())) {
			return null;
		}

		String topic = annotation.topic();

		String group = null;
		if (StringUtils.isNotBlank(annotation.group())) {
			group = annotation.group();
		} else if (StringUtils.isNotBlank(kafkaProperties.getConsumer().getGroupId())) {
			group = kafkaProperties.getConsumer().getGroupId() + "_" + topic;
		}

		Consumer<String, String> consumer = consumerFactory.createConsumer(group,
			kafkaProperties.getClientId());
		consumer.subscribe(Collections.singleton(topic));

		LogUtils.debug(CREATE_CONSUMER_FROM_CONSUMER_FACTORY_GROUP_TOPIC, group, topic);
		return consumer;
	}
}
