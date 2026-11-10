package com.taotao.cloud.mq.plugin.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MessageExtBrokerInner 单元测试
 */
public class MessageExtBrokerInnerTest {

    @Test
    public void testDefaultConstructor() {
        MessageExtBrokerInner m = new MessageExtBrokerInner();
        assertNotNull(m);
        assertNull(m.getPropertiesString());
        assertEquals(0L, m.getStoreTimestamp());
        assertEquals(0L, m.getCommitLogOffset());
        assertEquals(0, m.getBodyCRC());
    }

    @Test
    public void testSetters() {
        MessageExtBrokerInner m = new MessageExtBrokerInner();
        m.setPropertiesString("k=v");
        m.setStoreTimestamp(123L);
        m.setCommitLogOffset(456L);
        m.setBodyCRC(789);
        assertEquals("k=v", m.getPropertiesString());
        assertEquals(123L, m.getStoreTimestamp());
        assertEquals(456L, m.getCommitLogOffset());
        assertEquals(789, m.getBodyCRC());
    }

    @Test
    public void testInheritsMessageExtFields() {
        MessageExtBrokerInner m = new MessageExtBrokerInner();
        m.setTopic("TopicA");
        m.setBody("body".getBytes());
        m.setQueueId(2);
        m.setQueueOffset(100L);
        assertEquals("TopicA", m.getTopic());
        assertEquals("body", new String(m.getBody()));
        assertEquals(2, m.getQueueId());
        assertEquals(100L, m.getQueueOffset());
    }
}
