package com.taotao.cloud.mq.consistency.ttc.common.protocol;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * PullResultPayload 单元测试
 */
public class PullResultPayloadTest {

    @Test
    public void testDefaultConstructor() {
        PullResultPayload p = new PullResultPayload();
        assertNull(p.getTopic());
        assertEquals(0, p.getQueueId());
        assertEquals(0L, p.getNextOffset());
        assertEquals(0L, p.getMinOffset());
        assertEquals(0L, p.getMaxOffset());
        assertNull(p.getMessages());
    }

    @Test
    public void testFullConstructor() {
        List<MessageExt> msgs = new ArrayList<>();
        msgs.add(new MessageExt());
        PullResultPayload p = new PullResultPayload("T", 1, 100L, 0L, 200L, msgs);
        assertEquals("T", p.getTopic());
        assertEquals(1, p.getQueueId());
        assertEquals(100L, p.getNextOffset());
        assertEquals(0L, p.getMinOffset());
        assertEquals(200L, p.getMaxOffset());
        assertEquals(1, p.getMessages().size());
    }

    @Test
    public void testSetters() {
        PullResultPayload p = new PullResultPayload();
        p.setTopic("X");
        p.setQueueId(2);
        p.setNextOffset(11L);
        p.setMinOffset(1L);
        p.setMaxOffset(99L);
        p.setMessages(new ArrayList<>());
        assertEquals("X", p.getTopic());
        assertEquals(2, p.getQueueId());
        assertEquals(11L, p.getNextOffset());
        assertEquals(1L, p.getMinOffset());
        assertEquals(99L, p.getMaxOffset());
        assertEquals(0, p.getMessages().size());
    }
}
