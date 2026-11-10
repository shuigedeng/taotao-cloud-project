package com.taotao.cloud.mq.plugin.store.log;

import com.taotao.cloud.mq.plugin.store.log.CommitLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 进程内 Queue <b>位点</b>索引 — 只持有 {@code queueOffset -> (commitLogOffset, size)}，不持有消息体。
 * <p>
 * W1 之前这里是 {@code ConcurrentMap<Long, MessageExt>}，整条消息（含 body）常驻堆里：
 * 读路径根本不碰盘，堆随消息量单调增长。现在读消息必须走
 * {@link CommitLog#pullMessage(String, int, long, int)} → {@code MappedFile.selectMappedBuffer} → 解码。
 * <p>
 * 内存占用：每条位点约 48B（Entry + skip-list 节点），100 万条约 50MB。可配置上限（按条目数淘汰）。
 *
 * <p><b>线程安全:</b> 所有公开方法均线程安全, 内部状态用 ConcurrentHashMap + AtomicLong。
 */
public class InMemoryQueueIndex {

    /** 一条位点：消息在 CommitLog 里的物理位置与长度。 */
    public static final class Entry {
        private final long queueOffset;
        private final long commitLogOffset;
        private final int size;

        Entry(long queueOffset, long commitLogOffset, int size) {
            this.queueOffset = queueOffset;
            this.commitLogOffset = commitLogOffset;
            this.size = size;
        }

        public long getQueueOffset() {
            return queueOffset;
        }

        public long getCommitLogOffset() {
            return commitLogOffset;
        }

        /** 记录在 CommitLog 中的物理长度（字节）。 */
        public int getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "Entry{queueOffset=" + queueOffset + ", commitLogOffset=" + commitLogOffset
                    + ", size=" + size + '}';
        }
    }

    /**
     * 单个 (topic, queueId) 的位点表。
     * <p>
     * {@code byOffset} 用跳表层保证按 queueOffset 有序遍历（老实现的 HashMap 表 + 逐 offset 试探
     * 在淘汰后会出现空洞）；{@code nextOffset} 是该队列的下一个待分配 queueOffset。
     */
    public static final class QueueEntry {
        private final String topic;
        private final int queueId;
        private final ConcurrentMap<Long, Entry> byOffset = new ConcurrentSkipListMap<>();
        private final AtomicLong nextOffset = new AtomicLong(0L);

        QueueEntry(String topic, int queueId) {
            this.topic = topic;
            this.queueId = queueId;
        }

        public String getTopic() {
            return topic;
        }

        public int getQueueId() {
            return queueId;
        }

        public long getNextOffset() {
            return nextOffset.get();
        }

        public int getSize() {
            return byOffset.size();
        }
    }

    private final ConcurrentMap<String, QueueEntry> entries = new ConcurrentHashMap<>();
    /** 每个 (topic, queueId) 最多保留多少条位点, 0 表示不限制。 */
    private final int maxPerQueue;

    public InMemoryQueueIndex() {
        this(0);
    }

    public InMemoryQueueIndex(int maxPerQueue) {
        this.maxPerQueue = maxPerQueue;
    }

    private static String key(String topic, int queueId) {
        return topic + "@" + queueId;
    }

    public QueueEntry getOrCreateEntry(String topic, int queueId) {
        return entries.computeIfAbsent(key(topic, queueId), k -> new QueueEntry(topic, queueId));
    }

    /**
     * 分配该队列的下一个 queueOffset（单调递增，与物理写入顺序在同一把锁里配对）。
     */
    public long allocateOffset(String topic, int queueId) {
        return getOrCreateEntry(topic, queueId).nextOffset.getAndIncrement();
    }

    /**
     * 登记一条位点。{@code queueOffset} 必须是 {@link #allocateOffset} 给出的值，
     * 或者是恢复扫描从盘上读到的值（后者会把 nextOffset 抬到 {@code queueOffset + 1}）。
     *
     * @return 登记的条目
     */
    public Entry appendEntry(String topic, int queueId, long queueOffset, long commitLogOffset, int size) {
        QueueEntry entry = getOrCreateEntry(topic, queueId);
        Entry value = new Entry(queueOffset, commitLogOffset, size);
        entry.byOffset.put(queueOffset, value);
        long current = entry.nextOffset.get();
        while (queueOffset >= current) {
            if (entry.nextOffset.compareAndSet(current, queueOffset + 1)) {
                break;
            }
            current = entry.nextOffset.get();
        }
        // 按条目数淘汰最早的位点（不再是"整条消息留在堆里"）
        if (maxPerQueue > 0 && entry.byOffset.size() > maxPerQueue) {
            long floor = entry.nextOffset.get() - maxPerQueue;
            entry.byOffset.keySet().removeIf(off -> off < floor);
        }
        return value;
    }

    /**
     * 查询从指定 offset 开始的最多 maxNum 条位点。
     *
     * @return 列表按 queueOffset 升序排列; 若 offset 超出范围返回空列表
     */
    public List<Entry> queryEntries(String topic, int queueId, long offset, int maxNum) {
        QueueEntry entry = entries.get(key(topic, queueId));
        if (entry == null || offset >= entry.nextOffset.get() || maxNum <= 0) {
            return new ArrayList<>();
        }
        List<Entry> result = new ArrayList<>();
        // skipList 的 tailMap 天然按 offset 升序
        for (Map.Entry<Long, Entry> e : ((ConcurrentSkipListMap<Long, Entry>) asSkipList(entry.byOffset)).tailMap(offset).entrySet()) {
            if (result.size() >= maxNum) {
                break;
            }
            result.add(e.getValue());
        }
        return result;
    }

    private static ConcurrentSkipListMap<Long, Entry> asSkipList(ConcurrentMap<Long, Entry> map) {
        return (ConcurrentSkipListMap<Long, Entry>) map;
    }

    /** 获取队列当前最大 offset (新消息会分配这个值)。 */
    public long getMaxOffset(String topic, int queueId) {
        QueueEntry entry = entries.get(key(topic, queueId));
        return entry == null ? 0L : entry.nextOffset.get();
    }

    /** 队列中已索引的位点数量。 */
    public int getSize(String topic, int queueId) {
        QueueEntry entry = entries.get(key(topic, queueId));
        return entry == null ? 0 : entry.byOffset.size();
    }

    /** 清空所有索引 (用于测试隔离)。 */
    public void clear() {
        entries.clear();
    }

    /** 总队列数 (topic × queueId)。 */
    public int getQueueCount() {
        return entries.size();
    }
}
