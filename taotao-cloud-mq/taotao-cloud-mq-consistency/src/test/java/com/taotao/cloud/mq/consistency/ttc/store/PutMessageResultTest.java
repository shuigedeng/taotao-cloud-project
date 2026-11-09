package com.taotao.cloud.mq.consistency.ttc.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PutMessageResult + PutMessageStatus 单元测试
 */
public class PutMessageResultTest {

    @Test
    public void testPutOkIsOk() {
        AppendMessageResult amr = new AppendMessageResult(
                AppendMessageResult.AppendMessageStatus.PUT_OK, 0L, 10, 0L);
        PutMessageResult r = new PutMessageResult(PutMessageStatus.PUT_OK, amr);
        assertTrue(r.isOk());
        assertEquals(PutMessageStatus.PUT_OK, r.getPutMessageStatus());
        assertNotNull(r.getAppendMessageResult());
    }

    @Test
    public void testPutFailedIsNotOk() {
        PutMessageResult r = new PutMessageResult(PutMessageStatus.MESSAGE_ILLEGAL, null);
        assertFalse(r.isOk());
        assertEquals(PutMessageStatus.MESSAGE_ILLEGAL, r.getPutMessageStatus());
        assertNull(r.getAppendMessageResult());
    }

    @Test
    public void testAllStatusEnumsPresent() {
        // 8 个状态位,不允许随意增减
        assertEquals(8, PutMessageStatus.values().length);
    }

    @Test
    public void testSetPutMessageStatusNoop() {
        // setPutMessageStatus 显式标记为 deprecated 兼容方法, 不修改字段
        PutMessageResult r = new PutMessageResult(PutMessageStatus.PUT_OK, null);
        r.setPutMessageStatus(PutMessageStatus.MESSAGE_ILLEGAL);
        assertEquals(PutMessageStatus.PUT_OK, r.getPutMessageStatus());
    }

    @Test
    public void testAppendMessageResultFields() {
        AppendMessageResult amr = new AppendMessageResult(
                AppendMessageResult.AppendMessageStatus.PUT_OK, 1024L, 256, 1700000000000L);
        assertEquals(AppendMessageResult.AppendMessageStatus.PUT_OK, amr.getStatus());
        assertEquals(1024L, amr.getWroteOffset());
        assertEquals(256, amr.getWroteBytes());
        assertEquals(256L, amr.getWroteBytesLong());
        assertEquals(1700000000000L, amr.getStoreTimestamp());
    }

    @Test
    public void testAppendMessageStatusEnumsPresent() {
        assertEquals(4, AppendMessageResult.AppendMessageStatus.values().length);
    }
}
