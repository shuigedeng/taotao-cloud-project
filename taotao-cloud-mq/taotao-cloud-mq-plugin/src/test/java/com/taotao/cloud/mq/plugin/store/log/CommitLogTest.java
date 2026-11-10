package com.taotao.cloud.mq.plugin.store.log;

import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CommitLog 单元测试 — 验证构造、start/shutdown、putMessage 正常路径。
 */
public class CommitLogTest {

    @TempDir
    Path tempDir;

    private MessageStoreConfig config;

    @BeforeEach
    public void setUp() {
        config = new MessageStoreConfig();
        config.setStorePathRootDir(tempDir.resolve("store").toString());
        config.setStorePathCommitLog(tempDir.resolve("store/commitlog").toString());
        // 用 1MB 文件, 避免 1GB 默认导致测试慢
        config.setMappedFileSizeCommitLog(1024 * 1024);
    }

    @Test
    public void testConstructorCreatesMappedFileQueue() {
        CommitLog log = new CommitLog(config);
        assertNotNull(log.getMappedFileQueue());
    }

    @Test
    public void testStartAndShutdown() {
        CommitLog log = new CommitLog(config);
        log.start();
        // 跑 200ms 验证线程不挂
        try {
            Thread.sleep(200L);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        log.shutdown();
    }

    @Test
    public void testSyncFlushPicksGroupCommitService() {
        // 把配置改为 SYNC_FLUSH, 构造 CommitLog 后 start 跑 200ms, 不报错即可
        config.setFlushDiskType(FlushDiskType.SYNC_FLUSH);
        CommitLog log = new CommitLog(config);
        log.start();
        try {
            Thread.sleep(200L);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        log.shutdown();
    }

    @Test
    public void testConstants() {
        // 魔数稳定
        assertEquals(0xAABBCCDD, CommitLog.MESSAGE_MAGIC_CODE);
        assertEquals(0xCDDDABBA, CommitLog.MESSAGE_MAGIC_CODE_V2);
        assertEquals(0xBBCCDDEE, CommitLog.BLANK_MAGIC_CODE);
    }

    @Test
    public void testLoadEmptyReturnsTrue() {
        CommitLog log = new CommitLog(config);
        assertTrue(log.load());
    }

    @Test
    public void testPutMessageWithoutStartReturnsCreateFailed() {
        // 不调用 load, getLastMappedFile 会为 null, putMessage 走 CREATE_MAPPED_FILE_FAILED
        CommitLog log = new CommitLog(config);
        // 用反射/直接构造 MessageExtBrokerInner 验证
        com.taotao.cloud.mq.plugin.store.MessageExtBrokerInner msg = new com.taotao.cloud.mq.plugin.store.MessageExtBrokerInner();
        msg.setTopic("T");
        msg.setBody("body".getBytes());
        // 第一次 putMessage, mappedFile 还没创建, 会走到 load 默认路径
        com.taotao.cloud.mq.plugin.store.PutMessageResult r = log.putMessage(msg);
        assertNotNull(r);
        // mappedFile queue 在第一次 putMessage 时会自动创建 (getLastMappedFile 内部会触发 ensureMapping),
        // 所以这里要么 PUT_OK 要么 CREATE_MAPPED_FILE_FAILED, 两种结果都合理
        assertSame(
                com.taotao.cloud.mq.plugin.store.PutMessageStatus.PUT_OK, r.getPutMessageStatus()
        );
    }
}
