package com.taotao.cloud.mq.nameserver.kvconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * KVConfigManager 单元测试 — 验证内存表 CRUD + JSON 持久化往返。
 */
public class KVConfigManagerTest {

    @TempDir
    Path tempDir;

    private File configFile;
    private KVConfigManager manager;

    @BeforeEach
    public void setUp() throws Exception {
        configFile = tempDir.resolve("kvConfig.json").toFile();
        manager = new KVConfigManager(configFile.getAbsolutePath());
    }

    @Test
    public void testLoadWhenFileNotExists() {
        // 文件不存在, load 应当 noop 不抛异常
        assertFalse(configFile.exists());
        manager.load();
        assertTrue(manager.getAllNamespaces().isEmpty());
    }

    @Test
    public void testPutAndGetConfig() {
        manager.putConfig("NS_A", "k1", "v1");
        assertEquals("v1", manager.getConfig("NS_A", "k1"));
    }

    @Test
    public void testPutOverwritesValue() {
        manager.putConfig("NS_A", "k1", "v1");
        manager.putConfig("NS_A", "k1", "v2");
        assertEquals("v2", manager.getConfig("NS_A", "k1"));
    }

    @Test
    public void testGetConfigReturnsNullForUnknownKey() {
        assertNull(manager.getConfig("NS_A", "nope"));
    }

    @Test
    public void testGetConfigReturnsNullForUnknownNamespace() {
        assertNull(manager.getConfig("NS_NOT_EXIST", "k"));
    }

    @Test
    public void testDeleteConfig() {
        manager.putConfig("NS", "k1", "v1");
        manager.putConfig("NS", "k2", "v2");
        manager.deleteConfig("NS", "k1");
        assertNull(manager.getConfig("NS", "k1"));
        assertEquals("v2", manager.getConfig("NS", "k2"));
    }

    @Test
    public void testDeleteAllInNamespaceRemovesNamespace() {
        manager.putConfig("NS", "k1", "v1");
        manager.deleteConfig("NS", "k1");
        // 命名空间为空后, getConfigsByNamespace 返回空 map, getAllNamespaces 不含该 ns
        assertTrue(manager.getConfigsByNamespace("NS").isEmpty());
        assertFalse(manager.getAllNamespaces().contains("NS"));
    }

    @Test
    public void testDeleteNonExistentKeySafe() {
        manager.deleteConfig("NS_NOT_EXIST", "k");
        // 删不存在的 ns/key 不抛异常
        assertTrue(manager.getAllNamespaces().isEmpty());
    }

    @Test
    public void testGetConfigsByNamespace() {
        manager.putConfig("NS", "k1", "v1");
        manager.putConfig("NS", "k2", "v2");
        manager.putConfig("OTHER", "x", "y");

        java.util.Map<String, String> nsConfig = manager.getConfigsByNamespace("NS");
        assertEquals(2, nsConfig.size());
        assertEquals("v1", nsConfig.get("k1"));
        assertEquals("v2", nsConfig.get("k2"));
        // 修改返回的 map 不影响内部状态
        nsConfig.put("k1", "modified");
        assertEquals("v1", manager.getConfig("NS", "k1"));
    }

    @Test
    public void testGetConfigsByNamespaceEmptyForUnknown() {
        java.util.Map<String, String> nsConfig = manager.getConfigsByNamespace("UNKNOWN");
        assertNotNull(nsConfig);
        assertTrue(nsConfig.isEmpty());
    }

    @Test
    public void testGetAllConfigsSnapshot() {
        manager.putConfig("A", "k1", "v1");
        manager.putConfig("B", "k2", "v2");
        java.util.Map<String, java.util.Map<String, String>> all = manager.getAllConfigs();
        assertEquals(2, all.size());
        assertTrue(all.containsKey("A"));
        assertTrue(all.containsKey("B"));
    }

    @Test
    public void testGetAllNamespaces() {
        manager.putConfig("A", "x", "1");
        manager.putConfig("B", "y", "2");
        manager.putConfig("C", "z", "3");
        assertEquals(3, manager.getAllNamespaces().size());
    }

    @Test
    public void testClearAll() {
        manager.putConfig("A", "x", "1");
        manager.putConfig("B", "y", "2");
        manager.clearAll();
        assertTrue(manager.getAllNamespaces().isEmpty());
    }

    @Test
    public void testPersistAndReloadRoundTrip() {
        manager.putConfig("NS_A", "k1", "v1");
        manager.putConfig("NS_A", "k2", "v2");
        manager.putConfig("NS_B", "k3", "v3");
        manager.persist();
        assertTrue(configFile.exists());

        // 重新加载到新实例
        KVConfigManager loaded = new KVConfigManager(configFile.getAbsolutePath());
        loaded.load();
        assertEquals("v1", loaded.getConfig("NS_A", "k1"));
        assertEquals("v2", loaded.getConfig("NS_A", "k2"));
        assertEquals("v3", loaded.getConfig("NS_B", "k3"));
    }

    @Test
    public void testPersistCreatesParentDirectories() {
        File nested = tempDir.resolve("a/b/c/kvConfig.json").toFile();
        KVConfigManager m = new KVConfigManager(nested.getAbsolutePath());
        m.putConfig("NS", "k", "v");
        m.persist();
        assertTrue(nested.exists());
    }

    @Test
    public void testLoadInvalidJsonDoesNotThrow() throws Exception {
        java.nio.file.Files.write(configFile.toPath(),
                "not-a-json".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        // load 内部 log error 但不抛
        manager.load();
        // 加载失败后内存表保持空
        assertTrue(manager.getAllNamespaces().isEmpty());
    }
}
