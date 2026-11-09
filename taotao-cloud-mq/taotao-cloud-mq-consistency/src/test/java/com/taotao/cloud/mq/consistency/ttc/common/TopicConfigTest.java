package com.taotao.cloud.mq.consistency.ttc.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TopicConfig 单元测试
 */
public class TopicConfigTest {

    @Test
    public void testDefaultConstructor() {
        TopicConfig c = new TopicConfig();
        assertNull(c.getTopicName());
        assertEquals(TopicConfig.DEFAULT_READ_QUEUE_NUMS, c.getReadQueueNums());
        assertEquals(TopicConfig.DEFAULT_WRITE_QUEUE_NUMS, c.getWriteQueueNums());
        assertEquals(TopicConfig.PERM_READ_WRITE, c.getPerm());
        assertFalse(c.isOrder());
        assertFalse(c.isUnit());
        assertEquals(0, c.getTopicSysFlag());
    }

    @Test
    public void testNameOnlyConstructor() {
        TopicConfig c = new TopicConfig("TopicX");
        assertEquals("TopicX", c.getTopicName());
        // 默认值依然有效
        assertEquals(TopicConfig.DEFAULT_READ_QUEUE_NUMS, c.getReadQueueNums());
    }

    @Test
    public void testFullConstructor() {
        TopicConfig c = new TopicConfig("T", 8, 4, TopicConfig.PERM_READ);
        assertEquals("T", c.getTopicName());
        assertEquals(8, c.getReadQueueNums());
        assertEquals(4, c.getWriteQueueNums());
        assertEquals(TopicConfig.PERM_READ, c.getPerm());
    }

    @Test
    public void testSetters() {
        TopicConfig c = new TopicConfig();
        c.setTopicName("T");
        c.setReadQueueNums(2);
        c.setWriteQueueNums(2);
        c.setPerm(TopicConfig.PERM_WRITE);
        c.setOrder(true);
        c.setUnit(true);
        c.setTopicSysFlag(99);
        assertEquals("T", c.getTopicName());
        assertEquals(2, c.getReadQueueNums());
        assertEquals(2, c.getWriteQueueNums());
        assertEquals(TopicConfig.PERM_WRITE, c.getPerm());
        assertTrue(c.isOrder());
        assertTrue(c.isUnit());
        assertEquals(99, c.getTopicSysFlag());
    }

    @Test
    public void testBuildClusterName() {
        assertEquals("DefaultCluster", TopicConfig.buildClusterName("anyBrokerName"));
    }

    @Test
    public void testPermFlagsComposition() {
        // PERM_READ_WRITE = READ | WRITE = 3
        assertEquals(3, TopicConfig.PERM_READ_WRITE);
        assertEquals(1, TopicConfig.PERM_READ);
        assertEquals(2, TopicConfig.PERM_WRITE);
        assertEquals(0, TopicConfig.PERM_INHERIT);
    }

    @Test
    public void testToStringContainsKeyFields() {
        TopicConfig c = new TopicConfig("T", 2, 2, TopicConfig.PERM_READ_WRITE);
        String s = c.toString();
        assertNotNull(s);
        assertTrue(s.contains("T"));
        assertTrue(s.contains("perm=3"));
    }
}
