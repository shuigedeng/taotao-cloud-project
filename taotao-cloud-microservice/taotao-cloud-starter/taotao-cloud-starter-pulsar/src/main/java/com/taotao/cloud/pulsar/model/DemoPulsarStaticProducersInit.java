package com.taotao.cloud.pulsar.model;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.pulsar.client.api.Producer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 多个生产者一个线程，适用于生产者数目较多的场景
 */
public class DemoPulsarStaticProducersInit {

    private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("pulsar-producers-init").build();

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, threadFactory);

    private final Map<String, Producer<byte[]>> producerMap = new ConcurrentHashMap<>();

    private int initIndex = 0;

    private final List<String> topics;

    public DemoPulsarStaticProducersInit(List<String> topics) {
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
                final Producer<byte[]> producer = instance.getPulsarClient().newProducer().topic(topics.get(initIndex)).create();
                producerMap.put(topics.get(initIndex), producer);
            } catch (Exception e) {
                LogUtil.error("init pulsar producer error, exception is {}", e);
                break;
            }
        }
    }

    public Producer<byte[]> getProducers(String topic) {
        return producerMap.get(topic);
    }

}
