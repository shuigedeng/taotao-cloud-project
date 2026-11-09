package com.taotao.cloud.mq.consistency.ttc.spring.template;

import com.taotao.cloud.mq.consistency.ttc.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.consistency.ttc.common.message.Message;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Z-MQ 消息发送模板 (类似 RabbitTemplate / JmsTemplate).
 * <p>
 * 提供简洁的消息发送 API, 屏蔽底层 Producer 生命周期管理:
 * <pre>
 * {@literal @}Autowired
 * private ZmqTemplate zmqTemplate;
 *
 * // 发送字符串
 * SendResult result = zmqTemplate.syncSend("my-topic", "hello");
 *
 * // 发送带 tag 和 key
 * SendResult result = zmqTemplate.syncSend("my-topic", "order-created", "ORDER_001", body);
 *
 * // 发送带属性
 * SendResult result = zmqTemplate.syncSend("my-topic", "tag", "key", body, properties);
 * </pre>
 */
public class ZmqTemplate {

    private static final Logger log = LoggerFactory.getLogger(ZmqTemplate.class);

    private final DefaultMQProducer producer;
    private final boolean ownProducer;

    /**
     * 使用外部 Producer 构造 (由 AutoConfiguration 创建).
     */
    public ZmqTemplate(DefaultMQProducer producer) {
        this.producer = producer;
        this.ownProducer = false;
    }

    /**
     * 自动创建 Producer (独立使用模式).
     */
    public ZmqTemplate(String namesrvAddr, String producerGroup) {
        this.producer = new DefaultMQProducer(producerGroup);
        this.producer.setNamesrvAddr(namesrvAddr);
        this.ownProducer = true;
        try {
            this.producer.start();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start ZMQ producer", e);
        }
    }

    // ===== 同步发送 =====

    /**
     * 同步发送消息 (topic + body).
     *
     * @param topic 目标 Topic
     * @param body  消息体 (String)
     * @return 发送结果
     */
    public SendResult syncSend(String topic, String body) {
        return syncSend(topic, null, null, body);
    }

    /**
     * 同步发送消息 (topic + tag + body).
     */
    public SendResult syncSend(String topic, String tag, String body) {
        return syncSend(topic, tag, null, body);
    }

    /**
     * 同步发送消息 (topic + tag + key + body).
     */
    public SendResult syncSend(String topic, String tag, String key, String body) {
        Message msg = buildMessage(topic, tag, key, body);
        return doSend(msg);
    }

    /**
     * 同步发送消息 (带自定义属性).
     */
    public SendResult syncSend(String topic, String tag, String key, String body,
                               Map<String, String> properties) {
        Message msg = buildMessage(topic, tag, key, body);
        if (properties != null) {
            msg.setProperties(properties);
        }
        return doSend(msg);
    }

    /**
     * 同步发送原始 Message 对象.
     */
    public SendResult syncSend(Message message) {
        return doSend(message);
    }

    // ===== Oneway 发送 =====

    /**
     * 单向发送 (不关心结果, 适合日志/监控等场景).
     */
    public void sendOneway(String topic, String body) {
        sendOneway(topic, null, null, body);
    }

    /**
     * 单向发送 (带 tag + key).
     */
    public void sendOneway(String topic, String tag, String key, String body) {
        Message msg = buildMessage(topic, tag, key, body);
        try {
            producer.sendOneway(msg);
        } catch (Exception e) {
            log.error("sendOneway failed: topic={}", topic, e);
        }
    }

    // ===== 内部方法 =====

    private SendResult doSend(Message msg) {
        try {
            return producer.send(msg);
        } catch (Exception e) {
            log.error("send failed: topic={}", msg.getTopic(), e);
            throw new ZmqSendException("Failed to send message to " + msg.getTopic(), e);
        }
    }

    private Message buildMessage(String topic, String tag, String key, String body) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("topic is null or empty");
        }
        if (body == null) {
            throw new IllegalArgumentException("body is null");
        }
        Message msg = new Message();
        msg.setTopic(topic);
        msg.setTags(tag);
        msg.setKeys(key);
        msg.setBody(body.getBytes(StandardCharsets.UTF_8));
        return msg;
    }

    @PreDestroy
    public void destroy() {
        if (ownProducer && producer != null) {
            producer.shutdown();
            log.info("ZmqTemplate producer shutdown");
        }
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    /**
     * ZMQ 发送异常.
     */
    public static class ZmqSendException extends RuntimeException {
        public ZmqSendException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
