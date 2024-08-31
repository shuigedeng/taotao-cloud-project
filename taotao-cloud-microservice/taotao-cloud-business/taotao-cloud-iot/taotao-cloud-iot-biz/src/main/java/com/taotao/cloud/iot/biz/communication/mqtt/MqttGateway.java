package com.taotao.cloud.iot.biz.communication.mqtt;

import jakarta.annotation.Resource;
import com.taotao.cloud.iot.biz.communication.mqtt.config.MqttConfig;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * MQTT网关
 *
 * @author 
 */
@Component
@MessagingGateway(defaultRequestChannel = MqttConfig.OUTBOUND_CHANNEL)
public class MqttGateway {
    @Resource
    private MqttConfig mqttConfig;

    public void sendToMqtt(String payload) {
        mqttConfig.mqttOutboundHandler().handleMessage(MessageBuilder.withPayload(payload).build());
    }

    public void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload) {
        mqttConfig.mqttOutboundHandler().handleMessage(MessageBuilder.withPayload(payload).setHeader(MqttHeaders.TOPIC, topic).build());
    }

    public void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload) {
        mqttConfig.mqttOutboundHandler().handleMessage(MessageBuilder.withPayload(payload).setHeader(MqttHeaders.TOPIC, topic).setHeader(MqttHeaders.QOS, qos).build());
    }

    public void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.RETAINED) boolean retained, String payload) {
        mqttConfig.mqttOutboundHandler().handleMessage(MessageBuilder.withPayload(payload).setHeader(MqttHeaders.TOPIC, topic).setHeader(MqttHeaders.RETAINED, retained).build());
    }
}
