package com.taotao.cloud.message.biz.austin.support.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka工具类
 */
@Component

public class KafkaUtils {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 发送kafka消息
     *
     * @param topicName
     * @param jsonMessage
     */
    public void send(String topicName, String jsonMessage) {
        kafkaTemplate.send(topicName, jsonMessage);
    }

}
