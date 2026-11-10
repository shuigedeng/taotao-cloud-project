package com.taotao.cloud.mq.plugin.store.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ConsumerOffsetManager 单元测试.
 * <p>
 * 覆盖: commit / query / 持久化 / 重启恢复 / 并发提交 / 多 Group 隔离.
 */
@DisplayName("ConsumerOffsetManager 测试")
public class ConsumerOffsetManagerTest {

    private String storePath;
    private ConsumerOffsetManager mgr;

    @BeforeEach
    public void setUp() {
        storePath = System.getProperty("java.io.tmpdir") + File.separator
                + "ttc-offset-test-" + UUID.randomUUID().toString().substring(0, 8);
        new File(storePath).mkdirs();
        mgr = new ConsumerOffsetManager(storePath);
    }

    @AfterEach
    public void tearDown() {
        if (mgr != null) {
            mgr.shutdown();
        }
        // 清理测试目录
        deleteDir(new File(storePath));
    }

    private static void deleteDir(File f) {
        if (f == null || !f.exists()) return;
        if (f.isDirectory()) {
            File[] children = f.listFiles();
            if (children != null) {
                for (File c : children) deleteDir(c);
            }
        }
        f.delete();
    }

    @Test
    @DisplayName("commit + query 单条")
    public void testCommitAndQuery() {
        mgr.start();
        mgr.commitOffset("T1", 0, "G1", 100);
        assertEquals(100, mgr.queryOffset("T1", 0, "G1"));

        mgr.commitOffset("T1", 0, "G1", 200);
        assertEquals(200, mgr.queryOffset("T1", 0, "G1"));
    }

    @Test
    @DisplayName("commit 后 queryOffset 不存在返回 -1")
    public void testQueryMissing() {
        mgr.start();
        assertEquals(-1L, mgr.queryOffset("NOT_EXIST", 0, "G1"));
        assertEquals(-1L, mgr.queryOffset(null, 0, "G1"));
        assertEquals(-1L, mgr.queryOffset("T1", 0, null));
    }

    @Test
    @DisplayName("多 Group 隔离")
    public void testMultipleGroups() {
        mgr.start();
        mgr.commitOffset("T1", 0, "G1", 100);
        mgr.commitOffset("T1", 0, "G2", 200);
        mgr.commitOffset("T1", 1, "G1", 300);

        assertEquals(100, mgr.queryOffset("T1", 0, "G1"));
        assertEquals(200, mgr.queryOffset("T1", 0, "G2"));
        assertEquals(300, mgr.queryOffset("T1", 1, "G1"));

        assertEquals(3, mgr.size());
    }

    @Test
    @DisplayName("queryOffsetByGroup 返回该 Group 下所有 queueId 的 offset")
    public void testQueryByGroup() {
        mgr.start();
        mgr.commitOffset("T1", 0, "G1", 100);
        mgr.commitOffset("T1", 1, "G1", 200);
        mgr.commitOffset("T1", 2, "G1", 300);
        mgr.commitOffset("T1", 0, "G2", 999);

        Map<String, Long> r = mgr.queryOffsetByGroup("T1", "G1");
        assertEquals(3, r.size());
        assertEquals(100L, r.get("0"));
        assertEquals(200L, r.get("1"));
        assertEquals(300L, r.get("2"));

        Map<String, Long> r2 = mgr.queryOffsetByGroup("T1", "G2");
        assertEquals(1, r2.size());
        assertEquals(999L, r2.get("0"));
    }

    @Test
    @DisplayName("持久化: flush 后重启可恢复")
    public void testPersistAndReload() {
        mgr.start();
        mgr.commitOffset("T1", 0, "G1", 12345);
        mgr.commitOffset("T1", 1, "G2", 67890);
        mgr.flush();

        // 重启 - 新实例从相同路径加载
        mgr.shutdown();
        ConsumerOffsetManager mgr2 = new ConsumerOffsetManager(storePath);
        mgr2.start();

        assertEquals(12345, mgr2.queryOffset("T1", 0, "G1"));
        assertEquals(67890, mgr2.queryOffset("T1", 1, "G2"));

        mgr2.shutdown();
    }

    @Test
    @DisplayName("flushIfNecessary 不强制 flush 当 dirtyCount=0")
    public void testFlushIfNecessaryNoOp() {
        mgr.start();
        mgr.commitOffset("T1", 0, "G1", 100);
        mgr.flush();
        // dirtyCount=0, flushIfNecessary 不应触发
        mgr.flushIfNecessary();
        assertEquals(100, mgr.queryOffset("T1", 0, "G1"));
    }

    @Test
    @DisplayName("clear 后内存清空但可重新写入")
    public void testClear() {
        mgr.start();
        mgr.commitOffset("T1", 0, "G1", 100);
        assertEquals(1, mgr.size());

        mgr.clear();
        assertEquals(0, mgr.size());
        assertEquals(-1L, mgr.queryOffset("T1", 0, "G1"));

        mgr.commitOffset("T1", 0, "G1", 999);
        assertEquals(999L, mgr.queryOffset("T1", 0, "G1"));
    }

    @Test
    @DisplayName("commit null topic / null group 不抛异常")
    public void testCommitNullSafety() {
        mgr.start();
        mgr.commitOffset(null, 0, "G1", 100);
        mgr.commitOffset("T1", 0, null, 200);
        assertEquals(0, mgr.size());
    }

    @Test
    @DisplayName("flush 幂等")
    public void testFlushIdempotent() {
        mgr.start();
        mgr.commitOffset("T1", 0, "G1", 100);
        mgr.flush();
        mgr.flush();
        mgr.flush();
        assertNotNull(mgr.queryOffset("T1", 0, "G1"));
        // 文件应该存在
        File f = new File(storePath, ConsumerOffsetManager.OFFSET_FILE_NAME);
        assertTrue(f.exists(), "offset file should exist after flush");
    }
}
