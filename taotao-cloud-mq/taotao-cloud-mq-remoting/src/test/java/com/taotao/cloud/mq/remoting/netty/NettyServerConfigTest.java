package com.taotao.cloud.mq.remoting.netty;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * NettyServerConfig 单元测试
 */
public class NettyServerConfigTest {

    @Test
    public void testDefaults() {
        NettyServerConfig c = new NettyServerConfig();
        assertEquals(8888, c.getListenPort());
        assertEquals(1, c.getServerBossThreads());
        assertEquals(8, c.getServerWorkerThreads());
        assertEquals(3, c.getServerSelectorThreads());
        assertEquals(4, c.getServerCallbackExecutorThreads());
        assertEquals(64, c.getServerAsyncSemaphoreValue());
        assertEquals(256, c.getServerOnewaySemaphoreValue());
        assertEquals(65535, c.getServerSocketSndBufSize());
        assertEquals(65535, c.getServerSocketRcvBufSize());
        assertEquals(1024, c.getServerSocketBacklog());
        assertEquals(120, c.getServerChannelMaxIdleTimeSeconds());
        assertEquals(false, c.isUseEpollNativeSelector());
    }

    @Test
    public void testSetters() {
        NettyServerConfig c = new NettyServerConfig();
        c.setListenPort(12345);
        c.setServerBossThreads(2);
        c.setServerWorkerThreads(16);
        c.setServerSelectorThreads(4);
        c.setServerCallbackExecutorThreads(8);
        c.setServerAsyncSemaphoreValue(128);
        c.setServerOnewaySemaphoreValue(512);
        c.setServerSocketSndBufSize(32768);
        c.setServerSocketRcvBufSize(32768);
        c.setServerSocketBacklog(2048);
        c.setServerChannelMaxIdleTimeSeconds(60);
        c.setUseEpollNativeSelector(true);
        assertEquals(12345, c.getListenPort());
        assertEquals(2, c.getServerBossThreads());
        assertEquals(16, c.getServerWorkerThreads());
        assertEquals(4, c.getServerSelectorThreads());
        assertEquals(8, c.getServerCallbackExecutorThreads());
        assertEquals(128, c.getServerAsyncSemaphoreValue());
        assertEquals(512, c.getServerOnewaySemaphoreValue());
        assertEquals(32768, c.getServerSocketSndBufSize());
        assertEquals(32768, c.getServerSocketRcvBufSize());
        assertEquals(2048, c.getServerSocketBacklog());
        assertEquals(60, c.getServerChannelMaxIdleTimeSeconds());
        assertEquals(true, c.isUseEpollNativeSelector());
    }

    @Test
    public void testToStringContainsKeyFields() {
        NettyServerConfig c = new NettyServerConfig();
        c.setListenPort(19999);
        String s = c.toString();
        assertEquals(true, s.contains("19999"));
        assertEquals(true, s.contains("listenPort"));
    }
}
