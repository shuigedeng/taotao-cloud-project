package com.taotao.cloud.mq.spring.autoconfigure;

import com.taotao.cloud.mq.client.consumer.ConsumeConcurrentlyStatus;
import com.taotao.cloud.mq.client.consumer.DefaultMQPushConsumer;
import com.taotao.cloud.mq.client.consumer.MessageListener;
import com.taotao.cloud.mq.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.spring.listener.TtcMqListener;
import com.taotao.cloud.mq.spring.properties.TtcMqProperties;
import com.taotao.cloud.mq.spring.template.TtcMqTemplate;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import jakarta.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ttc-MQ Spring Boot 自动配置.
 * <p>
 * 自动完成:
 * <ul>
 *   <li>创建 DefaultMQProducer + TtcMqTemplate Bean</li>
 *   <li>扫描 @TtcMqListener 注解方法, 创建 DefaultMQPushConsumer 订阅</li>
 *   <li>管理所有 Producer / Consumer 的生命周期</li>
 * </ul>
 */
@Configuration
@ConditionalOnClass(DefaultMQProducer.class)
@ConditionalOnProperty(prefix = "ttc.mq", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(TtcMqProperties.class)
public class TtcMqAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TtcMqAutoConfiguration.class);

    @Autowired
    private TtcMqProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    /** 所有创建的 Consumer (用于 shutdown). */
    private final List<DefaultMQPushConsumer> managedConsumers = new ArrayList<>();

    /** Consumer 实例缓存: group → Consumer (同一 group 共享一个 Consumer). */
    private final ConcurrentHashMap<String, DefaultMQPushConsumer> consumerCache = new ConcurrentHashMap<>();

    /** 已启动的 Consumer group. */
    private final java.util.Set<String> startedGroups = java.util.Collections.synchronizedSet(new java.util.HashSet<>());

    // ===== Producer =====

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public DefaultMQProducer ttcMqProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(properties.getProducer().getGroup());
        producer.setNamesrvAddr(properties.getNamesrvAddr());
        producer.setSendMsgTimeoutMillis(properties.getProducer().getSendTimeoutMillis());
        producer.setDefaultTopicQueueNums(properties.getProducer().getDefaultTopicQueueNums());
        producer.setClientId("SPRING_" + properties.getProducer().getGroup() + "_" + System.currentTimeMillis());
        log.info("ttc Producer configured: group={}, namesrv={}",
                properties.getProducer().getGroup(), properties.getNamesrvAddr());
        return producer;
    }

    @Bean
    @ConditionalOnMissingBean
    public TtcMqTemplate ttcMqTemplate(DefaultMQProducer producer) {
        return new TtcMqTemplate(producer);
    }

    // ===== Consumer: @TtcMqListener 扫描 =====

    @PostConstruct
    public void registerListeners() {
        if (!properties.isEnabled()) {
            return;
        }

        // 扫描所有 Bean 中带 @TtcMqListener 的方法
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Class<?> clazz = bean.getClass();
            // 处理 CGLIB 代理
            if (clazz.getName().contains("$")) {
                clazz = clazz.getSuperclass();
            }

            for (Method method : clazz.getDeclaredMethods()) {
                TtcMqListener listener = method.getAnnotation(TtcMqListener.class);
                if (listener != null) {
                    registerListenerBean(bean, method, listener);
                }
            }
        }
    }

    private void registerListenerBean(Object bean, Method method, TtcMqListener listener) {
        String topic = listener.topic();
        String group = StringUtils.hasText(listener.consumerGroup())
                ? listener.consumerGroup()
                : properties.getConsumer().getGroup();

        DefaultMQPushConsumer consumer = consumerCache.computeIfAbsent(group, g -> {
            try {
                DefaultMQPushConsumer c = new DefaultMQPushConsumer(g);
                c.setNamesrvAddr(properties.getNamesrvAddr());
                c.setPullIntervalMillis(properties.getConsumer().getPullIntervalMillis());
                c.setPullBatchSize(properties.getConsumer().getPullBatchSize());
                c.setClientId("SPRING_" + g + "_" + System.currentTimeMillis());
                return c;
            } catch (Exception e) {
                throw new IllegalStateException("Failed to create consumer for group: " + g, e);
            }
        });

        // 注册监听: 使用 method 反射回调
        method.setAccessible(true);
        consumer.subscribe(topic, new MessageListener.Concurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(MessageExt[] msgs, com.taotao.cloud.mq.client.consumer.MessageQueueContext ctx) {
                for (MessageExt msg : msgs) {
                    try {
                        Class<?>[] paramTypes = method.getParameterTypes();
                        Object[] args;
                        if (paramTypes.length == 0) {
                            args = new Object[]{};
                        } else if (paramTypes[0] == String.class) {
                            args = new Object[]{new String(msg.getBody(), StandardCharsets.UTF_8)};
                        } else if (paramTypes[0] == byte[].class) {
                            args = new Object[]{msg.getBody()};
                        } else if (paramTypes[0] == MessageExt.class) {
                            args = new Object[]{msg};
                        } else {
                            args = new Object[]{new String(msg.getBody(), StandardCharsets.UTF_8)};
                        }
                        method.invoke(bean, args);
                    } catch (Exception e) {
                        log.error("TtcMqListener invoke failed: topic={} method={}", topic, method.getName(), e);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        managedConsumers.add(consumer);
        log.info("TtcMqListener registered: topic={} group={} method={}.{}",
                topic, group, bean.getClass().getSimpleName(), method.getName());

        // 确保 consumer 已启动 (每个 group 只启动一次)
        if (startedGroups.add(group)) {
            try {
                consumer.start();
            } catch (Exception e) {
                log.error("Failed to start consumer for group: {}", group, e);
            }
        }
    }

    @PreDestroy
    public void shutdownConsumers() {
        for (DefaultMQPushConsumer consumer : managedConsumers) {
            try {
                consumer.shutdown();
            } catch (Exception e) {
                log.warn("Consumer shutdown failed", e);
            }
        }
        log.info("All ttc managed consumers shutdown (count={})", managedConsumers.size());
    }
}
