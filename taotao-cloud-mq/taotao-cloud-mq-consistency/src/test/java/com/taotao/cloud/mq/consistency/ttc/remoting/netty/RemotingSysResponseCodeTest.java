package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RemotingSysResponseCode 单元测试 — 验证响应码枚举稳定。
 */
public class RemotingSysResponseCodeTest {

    @Test
    public void testKeyCodesStable() {
        // 关键响应码值固定, 协议改动需要同步更新
        assertEquals(0, RemotingSysResponseCode.SUCCESS);
        assertEquals(1, RemotingSysResponseCode.SYSTEM_ERROR);
        assertEquals(3, RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED);
    }
}
