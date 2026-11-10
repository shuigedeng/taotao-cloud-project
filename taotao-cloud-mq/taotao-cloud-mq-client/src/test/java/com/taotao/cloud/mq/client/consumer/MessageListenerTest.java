package com.taotao.cloud.mq.client.consumer;

import com.taotao.cloud.mq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * MessageListener 接口实现样例测试 — 验证两种 listener 子接口都可被实现。
 */
public class MessageListenerTest {

    @Test
    public void testConcurrentlyListenerCanBeImplemented() {
        MessageListener.Concurrently listener = new MessageListener.Concurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(MessageExt[] msgs, MessageQueueContext mq) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        };
        assertNotNull(listener);
    }

    @Test
    public void testOrderlyListenerCanBeImplemented() {
        MessageListener.Orderly listener = new MessageListener.Orderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(MessageExt[] msgs, MessageQueueContext mq) {
                return ConsumeOrderlyStatus.SUCCESS;
            }
        };
        assertNotNull(listener);
    }
}
