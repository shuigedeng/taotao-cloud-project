package com.taotao.cloud.mq.consistency.ttc.store.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * InMemoryQueueIndex 单元测试 — 验证 (topic, queueId) <b>位点</b>索引的单调递增与并发安全。
 * <p>
 * W1 之后索引只持有 {@code {queueOffset, commitLogOffset, size}}，不再持有整条 MessageExt
 * （消息体必须从 CommitLog 盘上读，见 CommitLogRecoveryTest）。因此原来断言"索引能取回 body"的
 * 用例改成断言位点三元组。顺序 / 上限 / 队列隔离 / 清空这些语义一条没丢。
 */
public class InMemoryQueueIndexTest {

    private InMemoryQueueIndex index;

    @BeforeEach
    public void setUp() {
        index = new InMemoryQueueIndex();
    }

    /** 写一条位点：物理偏移用可预测的 10000+i，长度用 64+i，便于逐字段断言。 */
    private InMemoryQueueIndex.Entry appendOne(InMemoryQueueIndex target, String topic, int queueId, long i) {
        long queueOffset = target.allocateOffset(topic, queueId);
        assertEquals(i, queueOffset, "allocateOffset 应返回第 i 个队列位点");
        return target.appendEntry(topic, queueId, queueOffset, 10000L + i, (int) (64 + i));
    }

    @Test
    public void testAppendAssignsMonotonicOffsets() {
        String topic = "T" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 100; i++) {
            InMemoryQueueIndex.Entry entry = appendOne(index, topic, 0, i);
            assertEquals(i, entry.getQueueOffset(),
                    "第 " + i + " 条消息 offset 应等于 " + i);
        }
        assertEquals(100, index.getSize(topic, 0));
        assertEquals(100L, index.getMaxOffset(topic, 0));
    }

    @Test
    public void testQueryReturnsEntriesInOrder() {
        String topic = "T" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 10; i++) {
            appendOne(index, topic, 0, i);
        }

        List<InMemoryQueueIndex.Entry> all = index.queryEntries(topic, 0, 0, 100);
        assertEquals(10, all.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(i, all.get(i).getQueueOffset());
            // 位点索引不再持消息体，能取回的是物理偏移与长度
            assertEquals(10000L + i, all.get(i).getCommitLogOffset());
            assertEquals(64 + i, all.get(i).getSize());
        }
    }

    @Test
    public void testQueryFromOffset() {
        String topic = "T" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 10; i++) {
            appendOne(index, topic, 0, i);
        }
        List<InMemoryQueueIndex.Entry> partial = index.queryEntries(topic, 0, 5, 100);
        assertEquals(5, partial.size());
        assertEquals(5L, partial.get(0).getQueueOffset());
        assertEquals(9L, partial.get(4).getQueueOffset());
    }

    @Test
    public void testQueryRespectsMaxNum() {
        String topic = "T" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 20; i++) {
            appendOne(index, topic, 0, i);
        }
        List<InMemoryQueueIndex.Entry> three = index.queryEntries(topic, 0, 0, 3);
        assertEquals(3, three.size());
        assertEquals(0L, three.get(0).getQueueOffset());
        assertEquals(2L, three.get(2).getQueueOffset());
    }

    @Test
    public void testQueryBeyondMaxReturnsEmpty() {
        String topic = "T" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 5; i++) {
            appendOne(index, topic, 0, i);
        }
        List<InMemoryQueueIndex.Entry> beyond = index.queryEntries(topic, 0, 100, 10);
        assertTrue(beyond.isEmpty());
    }

    @Test
    public void testDifferentQueuesHaveIndependentOffsets() {
        String topic = "T" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 5; i++) {
            appendOne(index, topic, 0, i);
        }
        for (int i = 0; i < 5; i++) {
            appendOne(index, topic, 1, i);
        }
        assertEquals(5, index.getSize(topic, 0));
        assertEquals(5, index.getSize(topic, 1));
        assertEquals(5L, index.getMaxOffset(topic, 0));
        assertEquals(5L, index.getMaxOffset(topic, 1));

        List<InMemoryQueueIndex.Entry> q0 = index.queryEntries(topic, 0, 0, 10);
        List<InMemoryQueueIndex.Entry> q1 = index.queryEntries(topic, 1, 0, 10);
        assertEquals(5, q0.size());
        assertEquals(5, q1.size());
        assertEquals(0L, q0.get(0).getQueueOffset());
        assertEquals(0L, q1.get(0).getQueueOffset());
        // 队列之间物理位点互不串台
        assertEquals(10000L, q0.get(0).getCommitLogOffset());
        assertEquals(10000L, q1.get(0).getCommitLogOffset());
    }

    @Test
    public void testDifferentTopicsHaveIndependentOffsets() {
        String topicA = "A" + UUID.randomUUID().toString().substring(0, 6);
        String topicB = "B" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 3; i++) {
            appendOne(index, topicA, 0, i);
            appendOne(index, topicB, 0, i);
        }
        assertEquals(3, index.getSize(topicA, 0));
        assertEquals(3, index.getSize(topicB, 0));
    }

    @Test
    public void testEmptyTopicQueryReturnsEmpty() {
        List<InMemoryQueueIndex.Entry> empty = index.queryEntries("nonexistent", 0, 0, 10);
        assertTrue(empty.isEmpty());
        assertEquals(0L, index.getMaxOffset("nonexistent", 0));
    }

    @Test
    public void testConcurrentAppendMonotonic() throws Exception {
        String topic = "CC" + UUID.randomUUID().toString().substring(0, 6);
        int threads = 10;
        int perThread = 100;
        int total = threads * perThread;
        CountDownLatch done = new CountDownLatch(total);
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                for (int i = 0; i < perThread; i++) {
                    long queueOffset = index.allocateOffset(topic, 0);
                    index.appendEntry(topic, 0, queueOffset, 500L + queueOffset, 32);
                    done.countDown();
                }
            });
        }
        assertTrue(done.await(10, TimeUnit.SECONDS));
        pool.shutdown();

        // 所有 offset 应当从 0 到 total-1 都出现, 无重复
        assertEquals(total, index.getMaxOffset(topic, 0));
        List<InMemoryQueueIndex.Entry> all = index.queryEntries(topic, 0, 0, total * 2);
        assertEquals(total, all.size(), "并发写入后应能拉到全部 " + total + " 条位点");
        Set<Long> seen = new HashSet<>();
        long prevOffset = -1;
        for (InMemoryQueueIndex.Entry e : all) {
            assertTrue(e.getQueueOffset() > prevOffset,
                    "offset 应单调递增: " + prevOffset + " -> " + e.getQueueOffset());
            assertTrue(seen.add(e.getQueueOffset()), "queueOffset 不应重复: " + e.getQueueOffset());
            assertEquals(500L + e.getQueueOffset(), e.getCommitLogOffset(),
                    "位点里的物理偏移应与分配时写入的一致");
            prevOffset = e.getQueueOffset();
        }
        assertEquals(total, seen.size());
    }

    @Test
    public void testClear() {
        String topic = "T";
        appendOne(index, topic, 0, 0);
        appendOne(index, topic, 0, 1);
        assertEquals(2, index.getSize(topic, 0));
        index.clear();
        assertEquals(0, index.getSize(topic, 0));
        assertTrue(index.queryEntries(topic, 0, 0, 10).isEmpty());
        assertEquals(0L, index.getMaxOffset(topic, 0), "clear 后队列条目应整体消失");
    }

    @Test
    public void testQueueCount() {
        String topic = "T" + UUID.randomUUID().toString().substring(0, 6);
        appendOne(index, topic, 0, 0);
        appendOne(index, topic, 1, 0);
        appendOne(index, topic, 2, 0);
        assertEquals(3, index.getQueueCount());
    }

    @Test
    public void testMaxPerQueueEviction() {
        InMemoryQueueIndex bounded = new InMemoryQueueIndex(3);
        String topic = "T" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 10; i++) {
            appendOne(bounded, topic, 0, i);
        }
        // maxPerQueue=3 时只保留最后 3 条 (offset 7, 8, 9)
        assertEquals(10L, bounded.getMaxOffset(topic, 0));
        // size 受 maxPerQueue 限制
        assertTrue(bounded.getSize(topic, 0) <= 3, "位点条数应受 maxPerQueue 限制");
        List<InMemoryQueueIndex.Entry> tail = bounded.queryEntries(topic, 0, 0, 100);
        assertEquals(3, tail.size());
        assertEquals(7L, tail.get(0).getQueueOffset(), "被淘汰的应是最早的位点");
        assertEquals(9L, tail.get(2).getQueueOffset());
    }

    @Test
    public void testAllocateWithoutEntryCreatesQueue() {
        String topic = "NEW" + UUID.randomUUID().toString().substring(0, 6);
        assertEquals(0L, index.getMaxOffset(topic, 7));
        assertEquals(0L, index.allocateOffset(topic, 7));
        assertEquals(1L, index.getMaxOffset(topic, 7), "分配即推进 nextOffset，即使还没落位点");
        InMemoryQueueIndex.QueueEntry entry = index.getOrCreateEntry(topic, 7);
        assertNotNull(entry);
        assertEquals(7, entry.getQueueId());
        assertEquals(topic, entry.getTopic());
        assertEquals(1L, entry.getNextOffset());
        assertEquals(0, entry.getSize());
    }

    @Test
    public void testRestoreEntryRaisesCounter() {
        String topic = "R" + UUID.randomUUID().toString().substring(0, 6);
        // 恢复路径：位点带着盘上的 queueOffset 回来，计数器必须抬到 max+1
        index.appendEntry(topic, 0, 42L, 9000L, 128);
        assertEquals(43L, index.getMaxOffset(topic, 0));
        List<InMemoryQueueIndex.Entry> entries = index.queryEntries(topic, 0, 40, 10);
        assertEquals(1, entries.size());
        assertEquals(42L, entries.get(0).getQueueOffset());
        assertEquals(9000L, entries.get(0).getCommitLogOffset());
        assertEquals(128, entries.get(0).getSize());
    }
}
