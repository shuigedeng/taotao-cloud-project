package com.taotao.cloud.mq.consistency.ttc.common;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TopicRouteData 单元测试
 */
public class TopicRouteDataTest {

    @Test
    public void testDefaultConstructor() {
        TopicRouteData r = new TopicRouteData();
        assertNotNull(r.getQueueDatas());
        assertNotNull(r.getBrokerDatas());
        assertNotNull(r.getFilterServerOuterMap());
        assertEquals(0, r.getQueueDatas().size());
        assertEquals(0, r.getBrokerDatas().size());
        assertEquals(0, r.getOrderTopicConf());
        assertFalse(r.isOrder());
    }

    @Test
    public void testSetters() {
        TopicRouteData r = new TopicRouteData();
        r.setOrderTopicConf(1);
        r.setOrder(true);
        r.setQueueDatas(new ArrayList<>());
        r.setBrokerDatas(new ArrayList<>());
        r.setFilterServerOuterMap(new HashMap<>());
        assertEquals(1, r.getOrderTopicConf());
        assertTrue(r.isOrder());
        assertNotNull(r.getQueueDatas());
        assertNotNull(r.getBrokerDatas());
        assertNotNull(r.getFilterServerOuterMap());
    }

    @Test
    public void testAddQueueAndBrokerData() {
        TopicRouteData r = new TopicRouteData();
        r.getQueueDatas().add(new QueueData("B", 4, 4, TopicConfig.PERM_READ_WRITE));
        r.getBrokerDatas().add(new BrokerData("C", "B", new HashMap<>()));
        assertEquals(1, r.getQueueDatas().size());
        assertEquals(1, r.getBrokerDatas().size());
    }

    @Test
    public void testFilterServerMapAdd() {
        TopicRouteData r = new TopicRouteData();
        r.getFilterServerOuterMap().put("k1", "v1");
        assertEquals("v1", r.getFilterServerOuterMap().get("k1"));
    }

    @Test
    public void testToStringContainsKeyFields() {
        TopicRouteData r = new TopicRouteData();
        r.setOrder(true);
        String s = r.toString();
        assertNotNull(s);
        assertTrue(s.contains("order=true"));
    }
}
