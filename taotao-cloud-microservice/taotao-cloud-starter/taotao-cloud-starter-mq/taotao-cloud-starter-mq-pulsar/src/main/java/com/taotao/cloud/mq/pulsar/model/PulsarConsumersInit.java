package com.taotao.cloud.mq.pulsar.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.pulsar.client.api.Consumer;

public class PulsarConsumersInit {

	private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(
		"pulsar-consumers-init").build();

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
		threadFactory);

	private final Map<String, Consumer<byte[]>> consumerMap = new ConcurrentHashMap<>();

	private int initIndex = 0;

	private final List<String> topics;

	public PulsarConsumersInit(List<String> topics) {
		this.topics = topics;
	}

	public void init() {
		executorService.scheduleWithFixedDelay(this::initWithRetry, 0, 10, TimeUnit.SECONDS);
	}

	private void initWithRetry() {
		if (initIndex == topics.size()) {
			executorService.shutdown();
			return;
		}
		for (; initIndex < topics.size(); initIndex++) {
			try {
				final DemoPulsarClientInit instance = DemoPulsarClientInit.getInstance();
				final Consumer<byte[]> consumer = instance.getPulsarClient().newConsumer()
					.topic(topics.get(initIndex)).messageListener(new DummyMessageListener<>())
					.subscribe();
				consumerMap.put(topics.get(initIndex), consumer);
			} catch (Exception e) {
				LogUtils.error("init pulsar producer error, exception is ", e);
				break;
			}
		}
	}

	public Consumer<byte[]> getConsumer(String topic) {
		return consumerMap.get(topic);
	}
}
