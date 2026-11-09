package com.taotao.cloud.mq.consistency.ttc.common.message;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MessageExt 单元测试
 */
public class MessageExtTest {

    @Test
    public void testDefaultConstructor() {
        MessageExt messageExt = new MessageExt();
        assertEquals(0, messageExt.getQueueId());
        assertEquals(0, messageExt.getStoreSize());
        assertEquals(0, messageExt.getQueueOffset());
        assertEquals(0, messageExt.getSysFlag());
        assertEquals(0, messageExt.getBornTimestamp());
        assertEquals(0, messageExt.getStoreTimestamp());
        assertEquals(0, messageExt.getCommitLogOffset());
        assertEquals(0, messageExt.getBodyCRC());
        assertEquals(0, messageExt.getReconsumeTimes());
        assertEquals(0, messageExt.getPreparedTransactionOffset());
        assertNull(messageExt.getMsgId());
        assertNull(messageExt.getBornHost());
        assertNull(messageExt.getStoreHost());
    }

    @Test
    public void testInheritanceFromMessage() {
        MessageExt messageExt = new MessageExt();

        // Test inherited properties from Message
        messageExt.setTopic("TestTopic");
        assertEquals("TestTopic", messageExt.getTopic());

        byte[] body = "TestBody".getBytes(StandardCharsets.UTF_8);
        messageExt.setBody(body);
        assertArrayEquals(body, messageExt.getBody());

        messageExt.setTags("TagA||TagB");
        assertEquals("TagA||TagB", messageExt.getTags());

        messageExt.setKeys("Key001");
        assertEquals("Key001", messageExt.getKeys());
    }

    @Test
    public void testQueueId() {
        MessageExt messageExt = new MessageExt();

        messageExt.setQueueId(5);
        assertEquals(5, messageExt.getQueueId());

        messageExt.setQueueId(0);
        assertEquals(0, messageExt.getQueueId());
    }

    @Test
    public void testStoreSize() {
        MessageExt messageExt = new MessageExt();

        messageExt.setStoreSize(1024);
        assertEquals(1024, messageExt.getStoreSize());
    }

    @Test
    public void testQueueOffset() {
        MessageExt messageExt = new MessageExt();

        messageExt.setQueueOffset(100);
        assertEquals(100, messageExt.getQueueOffset());
    }

    @Test
    public void testSysFlag() {
        MessageExt messageExt = new MessageExt();

        messageExt.setSysFlag(1);
        assertEquals(1, messageExt.getSysFlag());
    }

    @Test
    public void testBornTimestamp() {
        MessageExt messageExt = new MessageExt();

        long currentTime = System.currentTimeMillis();
        messageExt.setBornTimestamp(currentTime);
        assertEquals(currentTime, messageExt.getBornTimestamp());
    }

    @Test
    public void testBornHost() {
        MessageExt messageExt = new MessageExt();

        InetSocketAddress address = new InetSocketAddress("192.168.1.100", 10911);
        messageExt.setBornHost(address);
        assertEquals(address, messageExt.getBornHost());
    }

    @Test
    public void testStoreTimestamp() {
        MessageExt messageExt = new MessageExt();

        long currentTime = System.currentTimeMillis();
        messageExt.setStoreTimestamp(currentTime);
        assertEquals(currentTime, messageExt.getStoreTimestamp());
    }

    @Test
    public void testStoreHost() {
        MessageExt messageExt = new MessageExt();

        InetSocketAddress address = new InetSocketAddress("192.168.1.200", 10909);
        messageExt.setStoreHost(address);
        assertEquals(address, messageExt.getStoreHost());
    }

    @Test
    public void testMsgId() {
        MessageExt messageExt = new MessageExt();

        String msgId = "0A00000100004A1200000000000001";
        messageExt.setMsgId(msgId);
        assertEquals(msgId, messageExt.getMsgId());
    }

    @Test
    public void testCommitLogOffset() {
        MessageExt messageExt = new MessageExt();

        messageExt.setCommitLogOffset(2048);
        assertEquals(2048, messageExt.getCommitLogOffset());
    }

    @Test
    public void testBodyCRC() {
        MessageExt messageExt = new MessageExt();

        messageExt.setBodyCRC(123456789);
        assertEquals(123456789, messageExt.getBodyCRC());
    }

    @Test
    public void testReconsumeTimes() {
        MessageExt messageExt = new MessageExt();

        messageExt.setReconsumeTimes(3);
        assertEquals(3, messageExt.getReconsumeTimes());
    }

    @Test
    public void testPreparedTransactionOffset() {
        MessageExt messageExt = new MessageExt();

        messageExt.setPreparedTransactionOffset(4096);
        assertEquals(4096, messageExt.getPreparedTransactionOffset());
    }

    @Test
    public void testToString() {
        MessageExt messageExt = new MessageExt();
        messageExt.setTopic("TestTopic");
        messageExt.setQueueId(1);
        messageExt.setQueueOffset(100);
        messageExt.setMsgId("TestMsgId");

        String str = messageExt.toString();

        assertNotNull(str);
        assertTrue(str.contains("TestTopic"));
        assertTrue(str.contains("queueId=1"));
        assertTrue(str.contains("queueOffset=100"));
    }
}
