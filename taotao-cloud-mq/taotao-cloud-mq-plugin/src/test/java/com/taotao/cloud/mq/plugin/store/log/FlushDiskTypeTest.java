package com.taotao.cloud.mq.plugin.store.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * FlushDiskType 单元测试
 */
public class FlushDiskTypeTest {

    @Test
    public void testEnumCount() {
        assertEquals(2, FlushDiskType.values().length);
    }

    @Test
    public void testValueOf() {
        assertEquals(FlushDiskType.SYNC_FLUSH, FlushDiskType.valueOf("SYNC_FLUSH"));
        assertEquals(FlushDiskType.ASYNC_FLUSH, FlushDiskType.valueOf("ASYNC_FLUSH"));
    }

    @Test
    public void testNotEqual() {
        assertNotEquals(FlushDiskType.SYNC_FLUSH, FlushDiskType.ASYNC_FLUSH);
    }
}
