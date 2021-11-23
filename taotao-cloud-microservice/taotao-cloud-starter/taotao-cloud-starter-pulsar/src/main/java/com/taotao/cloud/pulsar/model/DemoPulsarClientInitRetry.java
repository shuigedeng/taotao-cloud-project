package com.taotao.cloud.pulsar.model;

import com.taotao.cloud.common.utils.LogUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.pulsar.client.api.PulsarClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//初始化 Client--可上线级别
public class DemoPulsarClientInitRetry {

    private static final DemoPulsarClientInitRetry INSTANCE = new DemoPulsarClientInitRetry();

    private volatile PulsarClient pulsarClient;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new DefaultThreadFactory("pulsar-cli-init"));

    public static DemoPulsarClientInitRetry getInstance() {
        return INSTANCE;
    }

    public void init() {
        executorService.scheduleWithFixedDelay(this::initWithRetry, 0, 10, TimeUnit.SECONDS);
    }

    private void initWithRetry() {
        try {
            pulsarClient = PulsarClient.builder()
                    .serviceUrl(PulsarConstant.SERVICE_HTTP_URL)
                    .build();

	        LogUtil.info("pulsar client init success");
            this.executorService.shutdown();
        } catch (Exception e) {
	        LogUtil.error("init pulsar error, exception is ", e);
        }
    }

    public PulsarClient getPulsarClient() {
        return pulsarClient;
    }

}
