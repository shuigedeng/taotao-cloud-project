package com.taotao.cloud.iot.biz.communication.mqtt.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.maku.iot.communication.mqtt.factory.MqttMessageHandlerFactory;
import net.maku.iot.enums.DeviceTopicEnum;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * MQTT 配置类，用于设置和管理 MQTT 连接和消息处理。
 *
 * @author LSF maku_lsf@163.com
 */
@Data
@Slf4j
@Configuration
@IntegrationComponentScan
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttConfig {
    public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";
    public static final String INPUT_CHANNEL = "mqttInputChannel";

    // MQTT 用户名
    private String username;

    // MQTT 密码
    private String password;

    // MQTT 服务器 URL
    private String host;

    // 客户端 ID
    private String clientId;

    // 默认主题
    private String defaultTopic;

    // 处理 MQTT 消息的工厂
    @Resource
    private MqttMessageHandlerFactory mqttMessageHandlerFactory;

    @PostConstruct
    public void init() {
        log.info("MQTT 主机: {} 客户端ID: {} 默认主题：{}", this.host, this.clientId, this.defaultTopic);
    }

    /**
     * 配置并返回一个 MqttPahoClientFactory 实例，用于创建 MQTT 客户端连接。
     *
     * @return MqttPahoClientFactory
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        // 设置连接选项，包括服务器 URI、用户名和密码。
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{host});
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 创建一个用于发送 MQTT 消息的 MessageChannel。
     *
     * @return MessageChannel
     */
    @Bean(OUTBOUND_CHANNEL)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * 配置用于发送 MQTT 消息的 MessageHandler。
     *
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = OUTBOUND_CHANNEL)
    public MessageHandler mqttOutboundHandler() {
        // 使用 MqttPahoMessageHandler 创建一个新的 MQTT 客户端连接，用于发布消息。
        final MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId + "_pub", mqttClientFactory());
        handler.setDefaultQos(1);
        handler.setDefaultRetained(false);
        handler.setDefaultTopic(defaultTopic);
        handler.setAsync(true);
        return handler;
    }

    /**
     * 创建用于接收 MQTT 消息的 MessageChannel。
     *
     * @return MessageChannel
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * 配置  客户端，订阅的主题，
     * PROPERTY：设备属性上报主题，
     * COMMAND_RESPONSE：下发指令执行结果主题
     *
     * @return MqttPahoMessageDrivenChannelAdapter
     */
    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInboundAdapter() {
        final MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                clientId + "_sub",
                mqttClientFactory(), DeviceTopicEnum.PROPERTY.getWildcard(),
                DeviceTopicEnum.COMMAND_RESPONSE.getWildcard()
        );
        adapter.setCompletionTimeout(15000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * 通过通道获取数据并处理消息。
     *
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = INPUT_CHANNEL)
    public MessageHandler mqttMessageHandler() {
        return message -> {
            String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
            if (topic != null) {
                mqttMessageHandlerFactory.getHandlersForTopic(topic).forEach(handler -> {
                    if (log.isDebugEnabled()) {
                        log.debug("主题: {}, 消息内容: {}", topic, message.getPayload());
                    }
                    handler.handle(topic, message.getPayload().toString());
                });
            } else {
                log.warn("接收到主题为null的消息。");
            }
        };
    }
}
