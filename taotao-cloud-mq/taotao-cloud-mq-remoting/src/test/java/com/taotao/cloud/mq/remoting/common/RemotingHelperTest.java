package com.taotao.cloud.mq.remoting.common;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RemotingHelper 单元测试
 */
public class RemotingHelperTest {

    @Test
    public void testString2SocketAddress() {
        InetSocketAddress addr = RemotingHelper.string2SocketAddress("10.0.0.1:10911");
        assertNotNull(addr);
        assertEquals("10.0.0.1", addr.getAddress().getHostAddress());
        assertEquals(10911, addr.getPort());
    }

    @Test
    public void testSocketAddress2String() {
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 8888);
        String s = RemotingHelper.socketAddress2String(addr);
        assertEquals("127.0.0.1:8888", s);
    }

    @Test
    public void testString2SocketAddressRoundTrip() {
        String original = "192.168.1.1:9090";
        InetSocketAddress addr = RemotingHelper.string2SocketAddress(original);
        String roundTrip = RemotingHelper.socketAddress2String(addr);
        assertEquals(original, roundTrip);
    }

    @Test
    public void testExceptionSimpleDescNull() {
        assertEquals("", RemotingHelper.exceptionSimpleDesc(null));
    }

    @Test
    public void testExceptionSimpleDescNoMessage() {
        Throwable t = new RuntimeException();
        String desc = RemotingHelper.exceptionSimpleDesc(t);
        assertTrue(desc.contains("java.lang.RuntimeException"));
    }

    @Test
    public void testExceptionSimpleDescWithMessage() {
        Throwable t = new IllegalStateException("bad state");
        String desc = RemotingHelper.exceptionSimpleDesc(t);
        assertTrue(desc.contains("IllegalStateException"));
        assertTrue(desc.contains("bad state"));
    }

    @Test
    public void testHandleExceptionNullSafe() {
        // 不抛
        RemotingHelper.handleException(null, "addr");
    }

    @Test
    public void testHandleExceptionWithReal() {
        // 不抛, 仅 log
        RemotingHelper.handleException(new RuntimeException("boom"), "1.2.3.4:10911");
    }

    @Test
    public void testParseChannelRemoteAddrNull() {
        assertEquals("", RemotingHelper.parseChannelRemoteAddr(null));
    }

    @Test
    public void testParseChannelLocalAddrNull() {
        assertEquals("", RemotingHelper.parseChannelLocalAddr(null));
    }

    @Test
    public void testIsPlatformDetectors() {
        // 不论测试在哪个平台, 都不应抛, 也至少有一个 false
        boolean linux = RemotingHelper.isLinuxPlatform();
        boolean windows = RemotingHelper.isWindowsPlatform();
        // 二者不应同时 true
        assertTrue(!(linux && windows));
    }
}
