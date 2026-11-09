package com.taotao.cloud.mq.consistency.ttc.client.consumer;

import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * MessageQueueContext 单元测试
 */
public class MessageQueueContextTest {

    @Test
    public void testConstructorWithQueue() {
        MessageQueue mq = new MessageQueue("T", "B", 1);
        MessageQueueContext ctx = new MessageQueueContext(mq);
        assertNotNull(ctx);
        assertEquals(mq, ctx.getMessageQueue());
    }

    @Test
    public void testMessageQueueFieldIsFinal() {
        // messageQueue 是 final, 只能通过构造器设置
        MessageQueue mq = new MessageQueue("T", "B", 2);
        MessageQueueContext ctx = new MessageQueueContext(mq);
        // 验证 getter 返回的与构造器传入相同
        assertEquals(mq, ctx.getMessageQueue());
    }
}
