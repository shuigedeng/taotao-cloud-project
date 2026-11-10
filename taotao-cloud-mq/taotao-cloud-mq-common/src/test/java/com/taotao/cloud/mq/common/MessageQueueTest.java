package com.taotao.cloud.mq.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MessageQueue 单元测试
 */
public class MessageQueueTest {

    @Test
    public void testDefaultConstructor() {
        MessageQueue mq = new MessageQueue();
        assertNotNull(mq);
    }

    @Test
    public void testFullConstructor() {
        MessageQueue mq = new MessageQueue("TopicA", "BrokerA", 0);
        assertEquals("TopicA", mq.getTopic());
        assertEquals("BrokerA", mq.getBrokerName());
        assertEquals(0, mq.getQueueId());
    }

    @Test
    public void testSetters() {
        MessageQueue mq = new MessageQueue();
        mq.setTopic("T");
        mq.setBrokerName("B");
        mq.setQueueId(3);
        assertEquals("T", mq.getTopic());
        assertEquals("B", mq.getBrokerName());
        assertEquals(3, mq.getQueueId());
    }

    @Test
    public void testEqualsAndHashCodeSame() {
        MessageQueue mq1 = new MessageQueue("T", "B", 1);
        MessageQueue mq2 = new MessageQueue("T", "B", 1);
        assertEquals(mq1, mq2);
        assertEquals(mq1.hashCode(), mq2.hashCode());
    }

    @Test
    public void testEqualsReflexive() {
        MessageQueue mq = new MessageQueue("T", "B", 1);
        assertEquals(mq, mq);
    }

    @Test
    public void testEqualsNullAndOtherType() {
        MessageQueue mq = new MessageQueue("T", "B", 1);
        assertNotEquals(mq, null);
        assertNotEquals(mq, "string");
    }

    @Test
    public void testNotEqualsWhenTopicDiffers() {
        MessageQueue mq1 = new MessageQueue("T1", "B", 1);
        MessageQueue mq2 = new MessageQueue("T2", "B", 1);
        assertNotEquals(mq1, mq2);
    }

    @Test
    public void testNotEqualsWhenBrokerNameDiffers() {
        MessageQueue mq1 = new MessageQueue("T", "B1", 1);
        MessageQueue mq2 = new MessageQueue("T", "B2", 1);
        assertNotEquals(mq1, mq2);
    }

    @Test
    public void testNotEqualsWhenQueueIdDiffers() {
        MessageQueue mq1 = new MessageQueue("T", "B", 1);
        MessageQueue mq2 = new MessageQueue("T", "B", 2);
        assertNotEquals(mq1, mq2);
    }

    @Test
    public void testEqualsWithNullFields() {
        MessageQueue mq1 = new MessageQueue();
        MessageQueue mq2 = new MessageQueue();
        assertEquals(mq1, mq2);
    }

    @Test
    public void testToStringContainsAllFields() {
        MessageQueue mq = new MessageQueue("TopicA", "BrokerA", 2);
        String s = mq.toString();
        assertNotNull(s);
        assertTrue(s.contains("TopicA"));
        assertTrue(s.contains("BrokerA"));
        assertTrue(s.contains("2"));
    }
}
