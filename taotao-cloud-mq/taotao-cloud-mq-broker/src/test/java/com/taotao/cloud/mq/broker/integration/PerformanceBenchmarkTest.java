package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.common.BrokerData;
import com.taotao.cloud.mq.common.MessageQueue;
import com.taotao.cloud.mq.common.QueueData;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.common.testsupport.BenchResult;
import com.taotao.cloud.mq.common.util.JsonCodec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 性能基准测试 — 测量核心路径的吞吐量与延迟, 不引入 JMH。
 * <p>
 * 每个测试输出到 console, 并写到 target/perf-reports/benchmark.txt。
 * baseline 目标: 序列化 < 10us/op, 路由查询 < 1ms/op, 并发写 < 100us/op。
 */
public class PerformanceBenchmarkTest {

    @TempDir
    Path tempDir;

    @Test
    void testJsonCodecEncodeDecode1KB() {
        SendResult original = SendResult.ok("msg-001", "BenchmarkTopic", 1, 100L);
        original.setErrorMsg("benchmark test message with some content to simulate real payload");
        original.setMessageQueue(new MessageQueue("BenchmarkTopic", "BrokerA", 1));
        byte[] encoded = JsonCodec.encode(original);

        BenchResult encode = BenchResult.runBest3("JsonCodec", "encode-1KB", 100_000, 5_000,
                () -> JsonCodec.encode(original));
        encode.printToConsole();
        encode.writeToFile(tempDir.resolve("benchmark.txt"));
        encode.assertBelowThreshold(10.0, "JsonCodec encode 1KB");

        BenchResult decode = BenchResult.runBest3("JsonCodec", "decode-1KB", 100_000, 5_000,
                () -> JsonCodec.decode(encoded, SendResult.class));
        decode.printToConsole();
        decode.writeToFile(tempDir.resolve("benchmark.txt"));
        decode.assertBelowThreshold(10.0, "JsonCodec decode 1KB");
    }

    @Test
    void testJsonCodecEncodeDecode64KB() {
        SendResult original = new SendResult(SendStatus.SEND_OK);
        StringBuilder sb = new StringBuilder(64 * 1024);
        for (int i = 0; i < 64 * 1024; i++) sb.append('x');
        original.setErrorMsg(sb.toString());
        original.setMsgId("big-msg");
        original.setTopic("BigTopic");
        byte[] encoded = JsonCodec.encode(original);

        // 64KB 测试内存压力大, 单次运行 + 更少预热
        BenchResult encode = BenchResult.of("JsonCodec", "encode-64KB", 10_000, 500,
                () -> JsonCodec.encode(original));
        encode.printToConsole();
        encode.writeToFile(tempDir.resolve("benchmark.txt"));

        BenchResult decode = BenchResult.of("JsonCodec", "decode-64KB", 10_000, 500,
                () -> JsonCodec.decode(encoded, SendResult.class));
        decode.printToConsole();
        decode.writeToFile(tempDir.resolve("benchmark.txt"));
        decode.assertBelowThreshold(500.0, "JsonCodec decode 64KB");
    }

    @Test
    void testJsonCodecEncodeDecode1MB() {
        SendResult original = new SendResult(SendStatus.SEND_OK);
        StringBuilder sb = new StringBuilder(1024 * 1024);
        for (int i = 0; i < 1024 * 1024; i++) sb.append('y');
        original.setErrorMsg(sb.toString());
        original.setMsgId("1mb-msg");
        original.setTopic("BigTopic1MB");
        byte[] encoded = JsonCodec.encode(original);

        // 1MB 测试用单次运行, 避免 OOM (runBest3 会保留 3 份 1MB 对象)
        BenchResult encode = BenchResult.of("JsonCodec", "encode-1MB", 500, 50,
                () -> JsonCodec.encode(original));
        encode.printToConsole();
        encode.writeToFile(tempDir.resolve("benchmark.txt"));

        BenchResult decode = BenchResult.of("JsonCodec", "decode-1MB", 500, 50,
                () -> JsonCodec.decode(encoded, SendResult.class));
        decode.printToConsole();
        decode.writeToFile(tempDir.resolve("benchmark.txt"));
        decode.assertBelowThreshold(5_000.0, "JsonCodec decode 1MB");
    }

    @Test
    void testMessageQueueEqualsHashCodePerf() {
        MessageQueue[] queues = new MessageQueue[1000];
        for (int i = 0; i < 1000; i++) {
            queues[i] = new MessageQueue("Topic" + i, "Broker" + (i % 10), i % 4);
        }

        BenchResult bench = BenchResult.runBest3("MessageQueue", "equals-1000", 100_000, 5_000, () -> {
            for (MessageQueue a : queues) {
                for (MessageQueue b : queues) {
                    a.equals(b);
                }
            }
        });
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
    }

    @Test
    void testTopicRouteDataSerialization() {
        TopicRouteData route = new TopicRouteData();
        route.setOrder(true);
        for (int i = 0; i < 4; i++) {
            route.getQueueDatas().add(new QueueData("Broker" + i, 4, 4, 3));
            BrokerData bd = new BrokerData("Cluster", "Broker" + i, new HashMap<>());
            bd.putBrokerAddr(0L, "10.0.0." + i + ":10911");
            route.getBrokerDatas().add(bd);
        }

        byte[] encoded = JsonCodec.encode(route);
        BenchResult encode = BenchResult.runBest3("TopicRouteData", "encode", 50_000, 3_000,
                () -> JsonCodec.encode(route));
        encode.printToConsole();
        encode.writeToFile(tempDir.resolve("benchmark.txt"));

        BenchResult decode = BenchResult.runBest3("TopicRouteData", "decode", 50_000, 3_000,
                () -> JsonCodec.decode(encoded, TopicRouteData.class));
        decode.printToConsole();
        decode.writeToFile(tempDir.resolve("benchmark.txt"));
        decode.assertBelowThreshold(20.0, "TopicRouteData decode");
    }

    @Test
    void testConcurrentMessageCreation() throws Exception {
        int threads = 4;
        int perThread = 10_000;
        AtomicInteger total = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(threads);
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        long start = System.nanoTime();
        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try {
                    for (int i = 0; i < perThread; i++) {
                        Message m = new Message("T", ("body-" + i).getBytes(StandardCharsets.UTF_8));
                        m.putProperty("k", "v");
                        total.incrementAndGet();
                    }
                } finally {
                    done.countDown();
                }
            });
        }
        done.await(30, TimeUnit.SECONDS);
        pool.shutdownNow();
        long elapsed = System.nanoTime() - start;

        double opsPerSec = (double) total.get() / (elapsed / 1_000_000_000.0);
        double usPerOp = (elapsed / 1_000.0) / total.get();
        System.out.printf("[Message] concurrent-create: %,d msgs, %.3f us/op, %.0f msgs/s%n",
                total.get(), usPerOp, opsPerSec);
        assertTrue(opsPerSec > 100_000, "消息创建应 > 100K ops/s, 实际=" + opsPerSec);
    }
}
