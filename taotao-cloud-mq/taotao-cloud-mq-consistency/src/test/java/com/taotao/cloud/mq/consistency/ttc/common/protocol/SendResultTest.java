package com.taotao.cloud.mq.consistency.ttc.common.protocol;

import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SendResult 单元测试
 */
public class SendResultTest {

    @Test
    public void testDefaultConstructorSetsOk() {
        SendResult r = new SendResult();
        assertEquals(SendStatus.SEND_OK, r.getSendStatus());
    }

    @Test
    public void testStatusConstructor() {
        SendResult r = new SendResult(SendStatus.SEND_FAILED);
        assertEquals(SendStatus.SEND_FAILED, r.getSendStatus());
    }

    @Test
    public void testOkFactory() {
        SendResult r = SendResult.ok("MSG_ID_001", "TopicA", 1, 100L);
        assertEquals("MSG_ID_001", r.getMsgId());
        assertEquals("TopicA", r.getTopic());
        assertEquals(1, r.getQueueId());
        assertEquals(100L, r.getQueueOffset());
        assertEquals(SendStatus.SEND_OK, r.getSendStatus());
    }

    @Test
    public void testSetters() {
        SendResult r = new SendResult();
        r.setMsgId("m1");
        r.setTopic("t");
        r.setQueueId(2);
        r.setQueueOffset(10L);
        r.setSendStatus(SendStatus.NO_ROUTE);
        r.setErrorMsg("no route");
        r.setMessageQueue(new MessageQueue("t", "b", 2));
        assertEquals("m1", r.getMsgId());
        assertEquals("t", r.getTopic());
        assertEquals(2, r.getQueueId());
        assertEquals(10L, r.getQueueOffset());
        assertEquals(SendStatus.NO_ROUTE, r.getSendStatus());
        assertEquals("no route", r.getErrorMsg());
        assertNotNull(r.getMessageQueue());
    }

    @Test
    public void testToString() {
        SendResult r = SendResult.ok("m", "T", 0, 1L);
        String s = r.toString();
        assertNotNull(s);
        assertTrue(s.contains("T"));
        assertTrue(s.contains("SEND_OK"));
    }
}
