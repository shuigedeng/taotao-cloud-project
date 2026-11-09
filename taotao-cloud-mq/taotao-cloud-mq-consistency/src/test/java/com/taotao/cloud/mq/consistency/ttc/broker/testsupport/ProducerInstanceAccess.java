package com.taotao.cloud.mq.consistency.ttc.broker.testsupport;

import com.taotao.cloud.mq.consistency.ttc.client.MQClientInstance;
import com.taotao.cloud.mq.consistency.ttc.client.producer.DefaultMQProducer;

import java.lang.reflect.Field;

/**
 * 测试辅助 — 反射读取 {@link DefaultMQProducer} 内部持有的 MQClientInstance.
 * <p>
 * 仅供 z-mq-broker 测试模块使用，避免把 getter 暴露到生产 API。
 */
public final class ProducerInstanceAccess {

    private ProducerInstanceAccess() {
    }

    /**
     * 反射读取 producer.mqClientInstance 字段。如果未启动返回 null。
     */
    public static MQClientInstance peekInstance(DefaultMQProducer producer) {
        try {
            Field f = DefaultMQProducer.class.getDeclaredField("mqClientInstance");
            f.setAccessible(true);
            return (MQClientInstance) f.get(producer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("DefaultMQProducer.mqClientInstance field not accessible", e);
        }
    }
}
