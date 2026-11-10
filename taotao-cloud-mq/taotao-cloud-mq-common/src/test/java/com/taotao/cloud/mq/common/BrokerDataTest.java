package com.taotao.cloud.mq.common;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BrokerData 单元测试
 */
public class BrokerDataTest {

    @Test
    public void testDefaultConstructor() {
        BrokerData data = new BrokerData();
        assertNull(data.getCluster());
        assertNull(data.getBrokerName());
        assertNotNull(data.getBrokerAddrs());
        assertEquals(0, data.getBrokerAddrs().size());
    }

    @Test
    public void testFullConstructor() {
        HashMap<Long, String> addrs = new HashMap<>();
        addrs.put(0L, "127.0.0.1:10911");
        BrokerData data = new BrokerData("DefaultCluster", "BrokerA", addrs);

        assertEquals("DefaultCluster", data.getCluster());
        assertEquals("BrokerA", data.getBrokerName());
        assertEquals("127.0.0.1:10911", data.selectBrokerAddr());
    }

    @Test
    public void testSelectBrokerAddrReturnsMaster() {
        BrokerData data = new BrokerData();
        data.putBrokerAddr(0L, "10.0.0.1:10911");
        data.putBrokerAddr(1L, "10.0.0.2:10911");
        assertEquals("10.0.0.1:10911", data.selectBrokerAddr());
    }

    @Test
    public void testSelectBrokerAddrReturnsNullWhenEmpty() {
        BrokerData data = new BrokerData();
        assertNull(data.selectBrokerAddr());
    }

    @Test
    public void testGetBrokerAddrById() {
        BrokerData data = new BrokerData();
        data.putBrokerAddr(1L, "10.0.0.2:10911");
        assertEquals("10.0.0.2:10911", data.getBrokerAddr(1L));
        assertNull(data.getBrokerAddr(2L));
    }

    @Test
    public void testPutBrokerAddrOverwrites() {
        BrokerData data = new BrokerData();
        data.putBrokerAddr(0L, "old-addr");
        data.putBrokerAddr(0L, "new-addr");
        assertEquals("new-addr", data.getBrokerAddr(0L));
    }

    @Test
    public void testSetters() {
        BrokerData data = new BrokerData();
        data.setCluster("C");
        data.setBrokerName("B");
        data.setBrokerAddrs(new HashMap<>());
        assertEquals("C", data.getCluster());
        assertEquals("B", data.getBrokerName());
        assertNotNull(data.getBrokerAddrs());
    }

    @Test
    public void testToStringContainsKeyFields() {
        BrokerData data = new BrokerData();
        data.setCluster("C1");
        data.setBrokerName("B1");
        String str = data.toString();
        assertNotNull(str);
        assertEquals(true, str.contains("C1"));
        assertEquals(true, str.contains("B1"));
    }
}
