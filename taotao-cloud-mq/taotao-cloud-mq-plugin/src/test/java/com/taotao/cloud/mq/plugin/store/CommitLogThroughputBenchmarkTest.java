package com.taotao.cloud.mq.plugin.store;

import com.taotao.cloud.mq.common.testsupport.BenchResult;
import com.taotao.cloud.mq.plugin.store.log.CommitLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CommitLog 写入吞吐量测试 — 测量 putMessage 在不同消息大小下的吞吐。
 */
public class CommitLogThroughputBenchmarkTest {

    @TempDir
    Path tempDir;

    private CommitLog commitLog;

    @BeforeEach
    public void setUp() {
        MessageStoreConfig config = new MessageStoreConfig();
        config.setStorePathRootDir(tempDir.resolve("store").toString());
        config.setStorePathCommitLog(tempDir.resolve("store/commitlog").toString());
        config.setMappedFileSizeCommitLog(1024 * 1024); // 1MB, 节省空间
        commitLog = new CommitLog(config);
    }

    @Test
    void testPutMessage1KB() {
        int iterations = 500;
        int warmup = 50;
        BenchResult bench = BenchResult.runBest3("CommitLog", "putMessage-1KB", iterations, warmup, () -> {
            MessageExtBrokerInner msg = new MessageExtBrokerInner();
            msg.setTopic("PerfTopic");
            msg.setBody(new byte[1024]);
            msg.setQueueId(0);
            msg.setBornTimestamp(System.currentTimeMillis());
            msg.setPropertiesString("");
            commitLog.putMessage(msg);
        });
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
        bench.assertBelowThreshold(100_000.0, "CommitLog put 1KB");
    }

    @Test
    void testPutMessage16KB() {
        int iterations = 200;
        int warmup = 20;
        BenchResult bench = BenchResult.runBest3("CommitLog", "putMessage-16KB", iterations, warmup, () -> {
            MessageExtBrokerInner msg = new MessageExtBrokerInner();
            msg.setTopic("PerfTopic16K");
            msg.setBody(new byte[16 * 1024]);
            msg.setQueueId(0);
            msg.setBornTimestamp(System.currentTimeMillis());
            msg.setPropertiesString("");
            commitLog.putMessage(msg);
        });
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
    }

    @Test
    void testConcurrentPutMessage() {
        int threads = 4;
        int perThread = 100;
        int total = threads * perThread;
        AtomicInteger ok = new AtomicInteger(0);

        BenchResult bench = BenchResult.of("CommitLog", "concurrent-put-" + threads + "x" + perThread,
                total, 10, () -> {
                    for (int i = 0; i < perThread; i++) {
                        MessageExtBrokerInner msg = new MessageExtBrokerInner();
                        msg.setTopic("ConcTopic");
                        msg.setBody(new byte[256]);
                        msg.setQueueId(i % 4);
                        msg.setBornTimestamp(System.currentTimeMillis());
                        msg.setPropertiesString("");
                        PutMessageResult result = commitLog.putMessage(msg);
                        if (result.isOk()) {
                            ok.incrementAndGet();
                        }
                    }
                });
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
        assertTrue(ok.get() > 0, "至少部分并发写入应成功, ok=" + ok.get());
    }
}
