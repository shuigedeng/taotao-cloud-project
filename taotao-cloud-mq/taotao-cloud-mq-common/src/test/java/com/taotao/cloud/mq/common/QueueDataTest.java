package com.taotao.cloud.mq.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * QueueData 单元测试
 */
public class QueueDataTest {

    @Test
    public void testDefaultConstructor() {
        QueueData q = new QueueData();
        assertNull(q.getBrokerName());
        assertEquals(0, q.getReadQueueNums());
        assertEquals(0, q.getWriteQueueNums());
        assertEquals(0, q.getPerm());
    }

    @Test
    public void testFullConstructor() {
        QueueData q = new QueueData("BrokerA", 8, 4, TopicConfig.PERM_READ);
        assertEquals("BrokerA", q.getBrokerName());
        assertEquals(8, q.getReadQueueNums());
        assertEquals(4, q.getWriteQueueNums());
        assertEquals(TopicConfig.PERM_READ, q.getPerm());
    }

    @Test
    public void testSetters() {
        QueueData q = new QueueData();
        q.setBrokerName("B");
        q.setReadQueueNums(2);
        q.setWriteQueueNums(3);
        q.setPerm(TopicConfig.PERM_INHERIT);
        q.setTopicSysFlag(7);
        assertEquals("B", q.getBrokerName());
        assertEquals(2, q.getReadQueueNums());
        assertEquals(3, q.getWriteQueueNums());
        assertEquals(TopicConfig.PERM_INHERIT, q.getPerm());
        assertEquals(7, q.getTopicSysFlag());
    }

    @Test
    public void testToStringContainsFields() {
        QueueData q = new QueueData("B", 1, 2, 3);
        String s = q.toString();
        assertNotNull(s);
        assertTrue(s.contains("B"));
        assertTrue(s.contains("1"));
        assertTrue(s.contains("2"));
        assertTrue(s.contains("3"));
    }
}
