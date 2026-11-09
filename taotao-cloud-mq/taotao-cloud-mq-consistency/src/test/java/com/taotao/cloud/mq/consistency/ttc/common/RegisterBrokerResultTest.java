package com.taotao.cloud.mq.consistency.ttc.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RegisterBrokerResult 单元测试
 */
public class RegisterBrokerResultTest {

    @Test
    public void testDefaultConstructor() {
        RegisterBrokerResult r = new RegisterBrokerResult();
        assertTrue(r.getChangedTopicSet().isEmpty());
    }

    @Test
    public void testSetters() {
        RegisterBrokerResult r = new RegisterBrokerResult();
        r.setHaServerAddr("ha-addr");
        r.setMasterAddr("master-addr");
        r.getChangedTopicSet().add("TopicA");
        assertEquals("ha-addr", r.getHaServerAddr());
        assertEquals("master-addr", r.getMasterAddr());
        assertTrue(r.getChangedTopicSet().contains("TopicA"));
    }

    @Test
    public void testReplaceChangedTopicSet() {
        RegisterBrokerResult r = new RegisterBrokerResult();
        java.util.HashSet<String> set = new java.util.HashSet<>();
        set.add("X");
        set.add("Y");
        r.setChangedTopicSet(set);
        assertEquals(2, r.getChangedTopicSet().size());
    }

    @Test
    public void testToString() {
        RegisterBrokerResult r = new RegisterBrokerResult();
        r.setMasterAddr("m");
        String s = r.toString();
        assertTrue(s.contains("m"));
    }
}
