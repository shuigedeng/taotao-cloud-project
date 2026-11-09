package com.taotao.cloud.mq.consistency.ttc.broker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BrokerConfig 单元测试
 */
public class BrokerConfigTest {

    @Test
    public void testDefaults() {
        BrokerConfig c = new BrokerConfig();
        assertNotNull(c);
        // brokerName 来自 System.getProperty("brokerName", "DEFAULT_BROKER")
        assertEquals("DEFAULT_BROKER", c.getBrokerName());
        // brokerClusterName 来自 System.getProperty("brokerClusterName", "DEFAULT_CLUSTER")
        assertEquals("DEFAULT_CLUSTER", c.getBrokerClusterName());
        // namesrvAddr 来自 System.getenv, 可能为 null
        assertEquals(16, c.getSendMessageThreadPoolNums());
        assertEquals(16, c.getPullMessageThreadPoolNums());
        assertEquals(4, c.getAdminBrokerThreadPoolNums());
    }

    @Test
    public void testSetters() {
        BrokerConfig c = new BrokerConfig();
        c.setNamesrvAddr("localhost:9876");
        c.setBrokerName("MyBroker");
        c.setBrokerClusterName("MyCluster");
        c.setSendMessageThreadPoolNums(32);
        c.setPullMessageThreadPoolNums(8);
        c.setAdminBrokerThreadPoolNums(2);
        assertEquals("localhost:9876", c.getNamesrvAddr());
        assertEquals("MyBroker", c.getBrokerName());
        assertEquals("MyCluster", c.getBrokerClusterName());
        assertEquals(32, c.getSendMessageThreadPoolNums());
        assertEquals(8, c.getPullMessageThreadPoolNums());
        assertEquals(2, c.getAdminBrokerThreadPoolNums());
    }

    @Test
    public void testBrokerNameFromSystemProperty() {
        // 通过 -DbrokerName=... 注入, 应当被读取
        System.setProperty("brokerName", "SysPropBroker");
        try {
            BrokerConfig c = new BrokerConfig();
            assertEquals("SysPropBroker", c.getBrokerName());
        } finally {
            System.clearProperty("brokerName");
        }
    }

    @Test
    public void testBrokerClusterFromSystemProperty() {
        System.setProperty("brokerClusterName", "SysPropCluster");
        try {
            BrokerConfig c = new BrokerConfig();
            assertEquals("SysPropCluster", c.getBrokerClusterName());
        } finally {
            System.clearProperty("brokerClusterName");
        }
    }

    @Test
    public void testNameServerAddrMayBeNull() {
        // NAMESRV_ADDR env var 通常不设置, namesrvAddr 应为 null
        BrokerConfig c = new BrokerConfig();
        // 不抛
        c.setNamesrvAddr(null);
        assertTrue(c.getNamesrvAddr() == null);
    }
}
