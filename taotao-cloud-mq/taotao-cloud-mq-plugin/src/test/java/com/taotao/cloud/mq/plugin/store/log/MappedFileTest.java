package com.taotao.cloud.mq.plugin.store.log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MappedFile 单元测试
 */
public class MappedFileTest {

    @TempDir
    Path tempDir;

    private File testFile;
    private MappedFile mappedFile;

    @BeforeEach
    public void setUp() throws Exception {
        testFile = tempDir.resolve("test_mapped_file.dat").toFile();
    }

    @AfterEach
    public void tearDown() {
        if (mappedFile != null) {
            mappedFile.destroy(1000);
        }
    }

    @Test
    public void testConstructorWithFile() throws Exception {
        int fileSize = 1024 * 1024; // 1MB

        mappedFile = new MappedFile(testFile.getAbsolutePath(), fileSize);

        assertNotNull(mappedFile);
        assertEquals(fileSize, mappedFile.getFileSize());
        assertTrue(testFile.exists());
    }

    @Test
    public void testGetMappedByteBuffer() throws Exception {
        int fileSize = 1024 * 1024;
        mappedFile = new MappedFile(testFile.getAbsolutePath(), fileSize);

        MappedByteBuffer buffer = mappedFile.getMappedByteBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testFlush() throws Exception {
        int fileSize = 1024 * 1024;
        mappedFile = new MappedFile(testFile.getAbsolutePath(), fileSize);

        byte[] data = "Test data for flush".getBytes(StandardCharsets.UTF_8);
        // 走真正的追加路径（推进 wrotePosition），不再用 getMappedByteBuffer().put() 绕位点
        assertTrue(mappedFile.appendMessage(data), "appendMessage 应成功");

        // 刷盘前 flushedPosition 必须还是 0
        assertEquals(0, mappedFile.getFlushedPosition(), "未刷盘时 flushedPosition 应为 0");

        // 真刷盘：fileChannel.force(false) 成功才把 flushedPosition 推到 wrotePosition
        assertTrue(mappedFile.flush(0), "flush 应返回 true");
        assertEquals(data.length, mappedFile.getFlushedPosition(),
                "刷盘成功后 flushedPosition 应等于 wrotePosition");

        // 换一个句柄从盘上读回同样的字节，证明不是只改了内存计数
        RandomAccessFile raf = new RandomAccessFile(testFile, "r");
        try {
            byte[] onDisk = new byte[data.length];
            raf.readFully(onDisk);
            assertArrayEquals(data, onDisk);
        } finally {
            raf.close();
        }

        // 没有增量时 flush 是 no-op，且不会把位点弄错
        assertTrue(mappedFile.flush(0));
        assertEquals(data.length, mappedFile.getFlushedPosition());
    }

    @Test
    public void testIsAvailable() throws Exception {
        int fileSize = 1024 * 1024;
        mappedFile = new MappedFile(testFile.getAbsolutePath(), fileSize);

        assertTrue(mappedFile.isAvailable());
    }

    @Test
    public void testGetFileName() throws Exception {
        int fileSize = 1024 * 1024;
        mappedFile = new MappedFile(testFile.getAbsolutePath(), fileSize);

        assertNotNull(mappedFile.getFileName());
        assertTrue(mappedFile.getFileName().contains("test_mapped_file"));
    }
}
