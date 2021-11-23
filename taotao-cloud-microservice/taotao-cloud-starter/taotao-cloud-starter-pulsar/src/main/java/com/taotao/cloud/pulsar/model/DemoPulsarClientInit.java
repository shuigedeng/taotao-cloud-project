package com.taotao.cloud.pulsar.model;

import org.apache.pulsar.client.api.PulsarClient;


//初始化 Client--demo 级别
public class DemoPulsarClientInit {

    private static final DemoPulsarClientInit INSTANCE = new DemoPulsarClientInit();

    private PulsarClient pulsarClient;

    public static DemoPulsarClientInit getInstance() {
        return INSTANCE;
    }

    public void init() throws Exception {
        pulsarClient = PulsarClient.builder()
                .serviceUrl(PulsarConstant.SERVICE_HTTP_URL)
                .build();
    }

    public PulsarClient getPulsarClient() {
        return pulsarClient;
    }
}
