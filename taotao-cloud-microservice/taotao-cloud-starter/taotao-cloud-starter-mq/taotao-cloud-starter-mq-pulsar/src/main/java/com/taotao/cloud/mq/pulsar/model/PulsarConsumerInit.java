package com.taotao.cloud.mq.pulsar.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.pulsar.client.api.Consumer;

public class PulsarConsumerInit {

	private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(
		"pulsar-consumer-init").build();

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
		threadFactory);

	private final String topic;

	private volatile Consumer<byte[]> consumer;

	public PulsarConsumerInit(String topic) {
		this.topic = topic;
	}

	public void init() {
		executorService.scheduleWithFixedDelay(this::initWithRetry, 0, 10, TimeUnit.SECONDS);
	}

	private void initWithRetry() {
		try {
			final DemoPulsarClientInit instance = DemoPulsarClientInit.getInstance();
			consumer = instance.getPulsarClient().newConsumer().topic(topic)
				.messageListener(new DummyMessageListener<>()).subscribe();
			executorService.shutdown();
		} catch (Exception e) {
			LogUtils.error("init pulsar producer error, exception is ", e);
		}
	}

	public Consumer<byte[]> getConsumer() {
		return consumer;
	}
}
