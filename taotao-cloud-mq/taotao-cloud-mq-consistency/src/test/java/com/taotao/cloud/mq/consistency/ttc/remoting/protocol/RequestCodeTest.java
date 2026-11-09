package com.taotao.cloud.mq.consistency.ttc.remoting.protocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RequestCode 单元测试 — 验证协议码分类与判定函数。
 */
public class RequestCodeTest {

    @Test
    public void testNameserverRequestCodes() {
        // NameServer 内部: 1-99 (不含 99, 实现是 < 99)
        assertTrue(RequestCode.isNameServerRequest(1));
        assertTrue(RequestCode.isNameServerRequest(50));
        assertTrue(RequestCode.isNameServerRequest(98));
        assertFalse(RequestCode.isNameServerRequest(0));
        assertFalse(RequestCode.isNameServerRequest(99));
        assertFalse(RequestCode.isNameServerRequest(100));
    }

    @Test
    public void testBrokerRequestCodes() {
        // Broker: 100-499
        assertTrue(RequestCode.isBrokerRequest(100));
        assertTrue(RequestCode.isBrokerRequest(200));
        assertTrue(RequestCode.isBrokerRequest(311));
        assertTrue(RequestCode.isBrokerRequest(499));
        assertFalse(RequestCode.isBrokerRequest(99));
        assertFalse(RequestCode.isBrokerRequest(500));
    }

    @Test
    public void testIsNameServerRequestFalseForHighCodes() {
        assertFalse(RequestCode.isNameServerRequest(500));
        assertFalse(RequestCode.isNameServerRequest(Integer.MAX_VALUE));
    }

    @Test
    public void testSpecificRequestCodes() {
        // 验证几个关键码值稳定, 协议变更需要同步
        assertEquals(1, RequestCode.REGISTER_BROKER);
        assertEquals(2, RequestCode.UNREGISTER_BROKER);
        assertEquals(5, RequestCode.UPDATE_AND_CREATE_TOPIC);
        assertEquals(8, RequestCode.GET_ALL_TOPIC_LIST);
        assertEquals(100, RequestCode.BROKER_HEARTBEAT);
        assertEquals(200, RequestCode.SEND_MESSAGE);
        assertEquals(210, RequestCode.PULL_MESSAGE);
        assertEquals(311, RequestCode.GET_ROUTE_BY_TOPIC);
    }
}
