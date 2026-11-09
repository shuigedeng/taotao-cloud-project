package com.taotao.cloud.mq.consistency.ttc.client.consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Consumer 状态枚举单元测试
 */
public class ConsumerStatusTest {

    @Test
    public void testConsumeConcurrentlyStatus() {
        assertEquals(2, ConsumeConcurrentlyStatus.values().length);
        assertNotNull(ConsumeConcurrentlyStatus.valueOf("CONSUME_SUCCESS"));
        assertNotNull(ConsumeConcurrentlyStatus.valueOf("RECONSUME_LATER"));
        assertNotEquals(ConsumeConcurrentlyStatus.CONSUME_SUCCESS, ConsumeConcurrentlyStatus.RECONSUME_LATER);
    }

    @Test
    public void testConsumeOrderlyStatus() {
        assertEquals(3, ConsumeOrderlyStatus.values().length);
        assertNotNull(ConsumeOrderlyStatus.valueOf("SUCCESS"));
        assertNotNull(ConsumeOrderlyStatus.valueOf("RECONSUME_LATER"));
        assertNotNull(ConsumeOrderlyStatus.valueOf("SUSPEND_CURRENT_QUEUE_A_MOMENT"));
    }
}
