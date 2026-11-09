package com.taotao.cloud.mq.consistency.ttc.store;

import com.taotao.cloud.mq.consistency.ttc.store.log.FlushDiskType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * MessageStoreConfig 单元测试
 */
public class MessageStoreConfigTest {

    @Test
    public void testDefaultValues() {
        MessageStoreConfig c = new MessageStoreConfig();
        // 默认 rootDir 形如 /Users/<user>/store
        assertNotNull(c.getStorePathRootDir());
        assertNotNull(c.getStorePathCommitLog());
        assertEquals(1024 * 1024 * 1024, c.getMappedFileSizeCommitLog());
        assertEquals(FlushDiskType.ASYNC_FLUSH, c.getFlushDiskType());
        assertEquals(5000, c.getSyncFlushTimeout());
    }

    @Test
    public void testSetters() {
        MessageStoreConfig c = new MessageStoreConfig();
        c.setStorePathRootDir("/tmp/zmq/store");
        c.setStorePathCommitLog("/tmp/zmq/store/commitlog");
        c.setMappedFileSizeCommitLog(64 * 1024 * 1024);
        c.setFlushDiskType(FlushDiskType.SYNC_FLUSH);
        c.setSyncFlushTimeout(3000);
        assertEquals("/tmp/zmq/store", c.getStorePathRootDir());
        assertEquals("/tmp/zmq/store/commitlog", c.getStorePathCommitLog());
        assertEquals(64 * 1024 * 1024, c.getMappedFileSizeCommitLog());
        assertEquals(FlushDiskType.SYNC_FLUSH, c.getFlushDiskType());
        assertEquals(3000, c.getSyncFlushTimeout());
    }
}
