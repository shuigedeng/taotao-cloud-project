package com.taotao.cloud.iot.biz.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.mqtt.inbound.AbstractMqttMessageDrivenChannelAdapter;
import org.springframework.stereotype.Component;

/**
 * Mqtt工具类
 *
 * @author LSF maku_lsf@163.com
 */
@Component
@RequiredArgsConstructor
public class MqttUtils {
    private final AbstractMqttMessageDrivenChannelAdapter mqttMessageAdapter;

    /**
     * 动态订阅主题，默认使用qos 1
     *
     * @param topics
     */
    public void addTopic(String... topics) {
        mqttMessageAdapter.addTopic(topics);
    }

    /**
     * 动态订阅主题
     *
     * @param topic
     * @param qos
     */
    public void addTopic(String topic, int qos) {
        mqttMessageAdapter.addTopic(topic, qos);
    }

    /**
     * 动态取消主题订阅
     *
     * @param topics
     */
    public void removeTopic(String... topics) {
        mqttMessageAdapter.removeTopic(topics);
    }
}
