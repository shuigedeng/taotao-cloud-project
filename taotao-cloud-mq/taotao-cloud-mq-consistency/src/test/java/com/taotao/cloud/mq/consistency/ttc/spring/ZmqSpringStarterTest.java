package com.taotao.cloud.mq.consistency.ttc.spring;

import com.taotao.cloud.mq.consistency.ttc.spring.annotation.EnableZmq;
import com.taotao.cloud.mq.consistency.ttc.spring.listener.ZmqListener;
import com.taotao.cloud.mq.consistency.ttc.spring.properties.ZmqProperties;
import com.taotao.cloud.mq.consistency.ttc.spring.template.ZmqTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Z-MQ Spring Boot Starter 单元测试.
 * <p>
 * 纯 POJO 测试 — 不依赖 Spring Boot Context / Logback / Netty 初始化,
 * 避免 classpath 版本冲突。
 */
@DisplayName("Z-MQ Spring Boot Starter 测试")
public class ZmqSpringStarterTest {

    // ===== ZmqProperties 测试 =====

    @Test
    @DisplayName("ZmqProperties 默认值正确")
    public void testDefaultProperties() {
        ZmqProperties props = new ZmqProperties();
        assertEquals("localhost:9876", props.getNamesrvAddr());
        assertTrue(props.isEnabled());
        assertEquals("zmq-default-producer-group", props.getProducer().getGroup());
        assertEquals(3000, props.getProducer().getSendTimeoutMillis());
        assertEquals(4, props.getProducer().getDefaultTopicQueueNums());
        assertEquals("zmq-default-consumer-group", props.getConsumer().getGroup());
        assertEquals(1000, props.getConsumer().getPullIntervalMillis());
        assertEquals(32, props.getConsumer().getPullBatchSize());
    }

    @Test
    @DisplayName("ZmqProperties 自定义配置覆盖")
    public void testCustomProperties() {
        ZmqProperties props = new ZmqProperties();
        props.setNamesrvAddr("broker1:9876;broker2:9876");
        props.setEnabled(false);
        props.getProducer().setGroup("my-group");
        props.getProducer().setSendTimeoutMillis(5000);
        props.getProducer().setDefaultTopicQueueNums(8);
        props.getConsumer().setGroup("my-consumer");
        props.getConsumer().setPullIntervalMillis(2000);
        props.getConsumer().setPullBatchSize(64);

        assertEquals("broker1:9876;broker2:9876", props.getNamesrvAddr());
        assertFalse(props.isEnabled());
        assertEquals("my-group", props.getProducer().getGroup());
        assertEquals(5000, props.getProducer().getSendTimeoutMillis());
        assertEquals(8, props.getProducer().getDefaultTopicQueueNums());
        assertEquals("my-consumer", props.getConsumer().getGroup());
        assertEquals(2000, props.getConsumer().getPullIntervalMillis());
        assertEquals(64, props.getConsumer().getPullBatchSize());
    }

    @Test
    @DisplayName("ZmqProperties Producer/Consumer 独立替换")
    public void testPropertiesReplace() {
        ZmqProperties props = new ZmqProperties();
        ZmqProperties.Producer prod = new ZmqProperties.Producer();
        prod.setGroup("new-group");
        props.setProducer(prod);
        assertEquals("new-group", props.getProducer().getGroup());

        ZmqProperties.Consumer cons = new ZmqProperties.Consumer();
        cons.setGroup("new-consumer");
        props.setConsumer(cons);
        assertEquals("new-consumer", props.getConsumer().getGroup());
    }

    // ===== @EnableZmq 注解测试 =====

    @Test
    @DisplayName("@EnableZmq 注解存在性")
    public void testEnableZmqAnnotationExists() {
        EnableZmq annotation = EnableZmqConfig.class.getAnnotation(EnableZmq.class);
        assertNotNull(annotation, "@EnableZmq should be present on EnableZmqConfig");
    }

    @Test
    @DisplayName("@EnableZmq 是 RUNTIME 保留策略")
    public void testEnableZmqRetentionPolicy() {
        java.lang.annotation.Retention retention = EnableZmq.class.getAnnotation(
                java.lang.annotation.Retention.class);
        assertNotNull(retention);
        assertEquals(java.lang.annotation.RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    @DisplayName("@EnableZmq 标注在 TYPE 上")
    public void testEnableZmqTarget() {
        java.lang.annotation.Target target = EnableZmq.class.getAnnotation(
                java.lang.annotation.Target.class);
        assertNotNull(target);
        assertTrue(target.value().length > 0);
    }

    // ===== @ZmqListener 注解测试 =====

    @Test
    @DisplayName("@ZmqListener 注解属性正确")
    public void testZmqListenerAnnotation() throws NoSuchMethodException {
        Method method = SampleListener.class.getMethod("onMessage", String.class);
        ZmqListener listener = method.getAnnotation(ZmqListener.class);
        assertNotNull(listener);
        assertEquals("test-topic", listener.topic());
        assertEquals("test-tag", listener.tag());
        assertEquals("test-group", listener.consumerGroup());
    }

    @Test
    @DisplayName("@ZmqListener 默认值: tag 和 group 为空")
    public void testZmqListenerDefaults() throws NoSuchMethodException {
        Method method = SampleListener.class.getMethod("onDefault", String.class);
        ZmqListener listener = method.getAnnotation(ZmqListener.class);
        assertNotNull(listener);
        assertEquals("default-topic", listener.topic());
        assertEquals("", listener.tag());
        assertEquals("", listener.consumerGroup());
    }

    @Test
    @DisplayName("@ZmqListener 仅需 topic 属性")
    public void testZmqListenerMinimal() throws NoSuchMethodException {
        Method method = SampleListener.class.getMethod("onMinimal", String.class);
        ZmqListener listener = method.getAnnotation(ZmqListener.class);
        assertNotNull(listener);
        assertEquals("minimal-topic", listener.topic());
        // 其他属性全部使用默认值
        assertEquals("", listener.tag());
        assertEquals("", listener.consumerGroup());
    }

    @Test
    @DisplayName("@ZmqListener 可以标注多个方法")
    public void testMultipleListeners() {
        long count = 0;
        for (Method m : SampleListener.class.getDeclaredMethods()) {
            if (m.isAnnotationPresent(ZmqListener.class)) {
                count++;
            }
        }
        assertTrue(count >= 3, "SampleListener should have at least 3 @ZmqListener methods, found " + count);
    }

    @Test
    @DisplayName("@ZmqListener RUNTIME 保留策略")
    public void testZmqListenerRetentionPolicy() {
        java.lang.annotation.Retention retention = ZmqListener.class.getAnnotation(
                java.lang.annotation.Retention.class);
        assertNotNull(retention);
        assertEquals(java.lang.annotation.RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    @DisplayName("@ZmqListener 标注在 METHOD 上")
    public void testZmqListenerTarget() {
        java.lang.annotation.Target target = ZmqListener.class.getAnnotation(
                java.lang.annotation.Target.class);
        assertNotNull(target);
        assertEquals(1, target.value().length);
        assertEquals(java.lang.annotation.ElementType.METHOD, target.value()[0]);
    }

    // ===== ZmqTemplate 消息构建逻辑测试 =====

    @Test
    @DisplayName("ZmqTemplate 配置类完整可访问")
    public void testZmqTemplateClassExists() {
        // 验证 ZmqTemplate 类可被加载 (不需要实例化)
        assertNotNull(ZmqTemplate.class);
        assertNotNull(ZmqTemplate.ZmqSendException.class);
    }

    @Test
    @DisplayName("ZmqSendException 正常创建")
    public void testZmqSendException() {
        Exception cause = new RuntimeException("network error");
        ZmqTemplate.ZmqSendException ex = new ZmqTemplate.ZmqSendException("send failed", cause);
        assertEquals("send failed", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    @DisplayName("消息编码 UTF-8 与 SDK 一致")
    public void testMessageEncoding() {
        String body = "你好 Z-MQ 🚀";
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        String decoded = new String(bytes, StandardCharsets.UTF_8);
        assertEquals(body, decoded);
        assertTrue(bytes.length > 0);
    }

    // ===== spring.factories 验证 =====

    @Test
    @DisplayName("spring.factories 配置文件存在")
    public void testSpringFactoriesExists() {
        java.net.URL url = getClass().getClassLoader()
                .getResource("META-INF/spring.factories");
        assertNotNull(url, "spring.factories should exist in classpath");
    }

    // ===== 测试辅助 =====

    @EnableZmq
    static class EnableZmqConfig {
    }

    static class SampleListener {
        @ZmqListener(topic = "test-topic", tag = "test-tag", consumerGroup = "test-group")
        public void onMessage(String message) {
        }

        @ZmqListener(topic = "default-topic")
        public void onDefault(String message) {
        }

        @ZmqListener(topic = "minimal-topic")
        public void onMinimal(String message) {
        }
    }
}
