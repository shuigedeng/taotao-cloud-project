package com.taotao.cloud.mq.spring.listener;

import java.lang.annotation.*;

/**
 * ttc-MQ 消息监听注解 (类似 @RabbitListener / @KafkaListener).
 * <p>
 * 标注在方法上, 该方法会在收到指定 Topic 的消息时自动调用:
 * <pre>
 * {@literal @}Component
 * public class OrderListener {
 *
 *     {@literal @}TtcMqListener(topic = "order-events")
 *     public void onOrderCreated(String message) {
 *         System.out.println("Received: " + message);
 *     }
 *
 *     {@literal @}TtcMqListener(topic = "payment-events", tag = "pay-success", consumerGroup = "payment-group")
 *     public void onPaymentSuccess(String message) {
 *         System.out.println("Payment success: " + message);
 *     }
 * }
 * </pre>
 *
 * <p>方法参数支持:
 * <ul>
 *   <li>{@code String} — 自动反序列化消息 body 为字符串</li>
 *   <li>{@code byte[]} — 原始字节</li>
 *   <li>{@code com.taotao.cloud.mq.common.message.MessageExt} — 完整消息对象</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TtcMqListener {

    /**
     * 监听的 Topic (必填).
     */
    String topic();

    /**
     * 消息 Tag 过滤 (可选, 默认订阅所有 tag).
     */
    String tag() default "";

    /**
     * 消费者组名 (可选, 默认使用配置文件中的 group).
     */
    String consumerGroup() default "";
}
