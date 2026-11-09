package com.taotao.cloud.mq.consistency.ttc.store.log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MappedFileQueue 单元测试
 */
public class MappedFileQueueTest {

    @TempDir
    Path tempDir;

    private File storeDir;
    private MappedFileQueue mappedFileQueue;

    @BeforeEach
    public void setUp() {
        storeDir = tempDir.resolve("store").toFile();
        storeDir.mkdirs();
    }

    @AfterEach
    public void tearDown() {
        if (mappedFileQueue != null) {
            // Cleanup if needed
        }
    }

    @Test
    public void testConstructor() {
        String storePath = storeDir.getAbsolutePath();
        int mappedFileSize = 1024 * 1024; // 1MB

        mappedFileQueue = new MappedFileQueue(storePath, mappedFileSize);

        assertNotNull(mappedFileQueue);
        assertEquals(mappedFileSize, mappedFileQueue.getMappedFileSize());
    }

    @Test
    public void testGetMappedFileSize() {
        String storePath = storeDir.getAbsolutePath();
        int mappedFileSize = 1024 * 1024;

        mappedFileQueue = new MappedFileQueue(storePath, mappedFileSize);

        assertEquals(mappedFileSize, mappedFileQueue.getMappedFileSize());
    }

    @Test
    public void testGetStorePath() {
        String storePath = storeDir.getAbsolutePath();
        int mappedFileSize = 1024 * 1024;

        mappedFileQueue = new MappedFileQueue(storePath, mappedFileSize);

        assertEquals(storePath, mappedFileQueue.getStorePath());
    }

    @Test
    public void testGetMappedFiles() {
        String storePath = storeDir.getAbsolutePath();
        int mappedFileSize = 1024 * 1024;

        mappedFileQueue = new MappedFileQueue(storePath, mappedFileSize);

        List<MappedFile> files = mappedFileQueue.getMappedFiles();

        assertNotNull(files);
        assertTrue(files.isEmpty());
    }

    @Test
    public void testGetFlushedWhere() {
        String storePath = storeDir.getAbsolutePath();
        int mappedFileSize = 1024 * 1024;

        mappedFileQueue = new MappedFileQueue(storePath, mappedFileSize);

        assertEquals(0, mappedFileQueue.getFlushedWhere());
    }

    @Test
    public void testSetFlushedWhere() {
        String storePath = storeDir.getAbsolutePath();
        int mappedFileSize = 1024 * 1024;

        mappedFileQueue = new MappedFileQueue(storePath, mappedFileSize);

        mappedFileQueue.setFlushedWhere(1024);
        assertEquals(1024, mappedFileQueue.getFlushedWhere());
    }

    @Test
    public void testGetCommittedWhere() {
        String storePath = storeDir.getAbsolutePath();
        int mappedFileSize = 1024 * 1024;

        mappedFileQueue = new MappedFileQueue(storePath, mappedFileSize);

        assertEquals(0, mappedFileQueue.getCommittedWhere());
    }

    @Test
    public void testSetCommittedWhere() {
        String storePath = storeDir.getAbsolutePath();
        int mappedFileSize = 1024 * 1024;

        mappedFileQueue = new MappedFileQueue(storePath, mappedFileSize);

        mappedFileQueue.setCommittedWhere(2048);
        assertEquals(2048, mappedFileQueue.getCommittedWhere());
    }
}
