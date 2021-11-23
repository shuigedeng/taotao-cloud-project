package com.taotao.cloud.pulsar.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.pulsar.client.api.Producer;

/**
 * 静态 producer，指不会随着业务的变化进行 producer 的启动或关闭。那么就在微服务启动完成、client 初始化完成之后，初始化 producer，样例如下
 *
 * <p>一个生产者一个线程，适用于生产者数目较少的场景</p>
 */
public class DemoPulsarStaticProducerInit {

	private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(
		"pulsar-producer-init").build();

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
		threadFactory);

	private final String topic;

	private volatile Producer<byte[]> producer;

	public DemoPulsarStaticProducerInit(String topic) {
		this.topic = topic;
	}

	public void init() {
		executorService.scheduleWithFixedDelay(this::initWithRetry, 0, 10, TimeUnit.SECONDS);
	}

	private void initWithRetry() {
		try {
			final DemoPulsarClientInit instance = DemoPulsarClientInit.getInstance();

			producer = instance.getPulsarClient().newProducer().topic(topic).create();

			executorService.shutdown();
		} catch (Exception e) {
			LogUtil.error("init pulsar producer error, exception is {}", e);
		}
	}

	public Producer<byte[]> getProducer() {
		return producer;
	}

}
