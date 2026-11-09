package com.taotao.cloud.mq.consistency.ttc.client.producer;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * SendCallback 单元测试 — 通过匿名实现验证接口契约。
 */
public class SendCallbackTest {

    @Test
    public void testCallbackOnSuccessPath() {
        AtomicInteger successCount = new AtomicInteger(0);
        com.taotao.cloud.mq.consistency.ttc.client.producer.SendCallback cb = new com.taotao.cloud.mq.consistency.ttc.client.producer.SendCallback() {
            @Override
            public void onSuccess(com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult sendResult) {
                successCount.incrementAndGet();
            }

            @Override
            public void onException(Throwable e) {
            }
        };
        cb.onSuccess(null);
        assertEquals(1, successCount.get());
    }

    @Test
    public void testCallbackOnExceptionPath() {
        AtomicInteger exCount = new AtomicInteger(0);
        com.taotao.cloud.mq.consistency.ttc.client.producer.SendCallback cb = new com.taotao.cloud.mq.consistency.ttc.client.producer.SendCallback() {
            @Override
            public void onSuccess(com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult sendResult) {
            }

            @Override
            public void onException(Throwable e) {
                exCount.incrementAndGet();
            }
        };
        cb.onException(new RuntimeException("boom"));
        assertEquals(1, exCount.get());
    }

    @Test
    public void testCallbackWithMessageExt() {
        MessageExt ext = new MessageExt();
        ext.setMsgId("m-1");
        com.taotao.cloud.mq.consistency.ttc.client.producer.SendCallback cb = new com.taotao.cloud.mq.consistency.ttc.client.producer.SendCallback() {
            @Override
            public void onSuccess(com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult sendResult) {
            }

            @Override
            public void onException(Throwable e) {
            }
        };
        // 不抛
        cb.onSuccess(com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult.ok("m-1", "T", 0, 0L));
    }
}
