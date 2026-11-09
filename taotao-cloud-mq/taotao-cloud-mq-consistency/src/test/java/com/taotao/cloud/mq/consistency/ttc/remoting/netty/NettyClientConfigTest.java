package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * NettyClientConfig 单元测试
 */
public class NettyClientConfigTest {

    @Test
    public void testDefaults() {
        NettyClientConfig c = new NettyClientConfig();
        assertEquals(4, c.getClientWorkerThreads());
        assertEquals(4, c.getClientCallbackExecutorThreads());
        assertEquals(1, c.getClientSelectorThreads());
        assertEquals(65535, c.getClientAsyncSemaphoreValue());
        assertEquals(65535, c.getClientOnewaySemaphoreValue());
        assertEquals(3000, c.getConnectTimeoutMillis());
        assertEquals(120, c.getClientChannelMaxIdleTimeSeconds());
        assertEquals(65535, c.getClientSocketSndBufSize());
        assertEquals(65535, c.getClientSocketRcvBufSize());
    }

    @Test
    public void testSetters() {
        NettyClientConfig c = new NettyClientConfig();
        c.setClientWorkerThreads(8);
        c.setClientCallbackExecutorThreads(16);
        c.setClientSelectorThreads(2);
        c.setClientAsyncSemaphoreValue(1024);
        c.setClientOnewaySemaphoreValue(2048);
        c.setConnectTimeoutMillis(5000);
        c.setClientChannelMaxIdleTimeSeconds(30);
        c.setClientSocketSndBufSize(16384);
        c.setClientSocketRcvBufSize(16384);
        assertEquals(8, c.getClientWorkerThreads());
        assertEquals(16, c.getClientCallbackExecutorThreads());
        assertEquals(2, c.getClientSelectorThreads());
        assertEquals(1024, c.getClientAsyncSemaphoreValue());
        assertEquals(2048, c.getClientOnewaySemaphoreValue());
        assertEquals(5000, c.getConnectTimeoutMillis());
        assertEquals(30, c.getClientChannelMaxIdleTimeSeconds());
        assertEquals(16384, c.getClientSocketSndBufSize());
        assertEquals(16384, c.getClientSocketRcvBufSize());
    }
}
