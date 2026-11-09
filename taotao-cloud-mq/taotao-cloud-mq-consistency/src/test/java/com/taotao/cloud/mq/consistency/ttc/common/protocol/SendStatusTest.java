package com.taotao.cloud.mq.consistency.ttc.common.protocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * SendStatus 单元测试 — 验证枚举值数量与命名稳定,避免对外协议意外变更。
 */
public class SendStatusTest {

    @Test
    public void testAllStatusesPresent() {
        SendStatus[] values = SendStatus.values();
        assertNotNull(values);
        // 8 个状态位, 不允许随意增减
        assertEquals(8, values.length);
    }

    @Test
    public void testValueOf() {
        assertEquals(SendStatus.SEND_OK, SendStatus.valueOf("SEND_OK"));
        assertEquals(SendStatus.FLUSH_DISK_TIMEOUT, SendStatus.valueOf("FLUSH_DISK_TIMEOUT"));
        assertEquals(SendStatus.FLUSH_SLAVE_TIMEOUT, SendStatus.valueOf("FLUSH_SLAVE_TIMEOUT"));
        assertEquals(SendStatus.SLAVE_NOT_AVAILABLE, SendStatus.valueOf("SLAVE_NOT_AVAILABLE"));
        assertEquals(SendStatus.SEND_FAILED, SendStatus.valueOf("SEND_FAILED"));
        assertEquals(SendStatus.NO_ROUTE, SendStatus.valueOf("NO_ROUTE"));
        assertEquals(SendStatus.MESSAGE_ILLEGAL, SendStatus.valueOf("MESSAGE_ILLEGAL"));
        assertEquals(SendStatus.UNKNOWN_ERROR, SendStatus.valueOf("UNKNOWN_ERROR"));
    }

    @Test
    public void testValueOfThrowsOnUnknown() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            SendStatus.valueOf("NOT_EXIST_STATUS");
        });
    }
}
