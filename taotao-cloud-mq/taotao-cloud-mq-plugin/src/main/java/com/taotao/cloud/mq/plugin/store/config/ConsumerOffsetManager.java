package com.taotao.cloud.mq.plugin.store.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Consumer 位点管理器（对标 RocketMQ ConsumerOffsetManager）.
 * <p>
 * 负责按 (topic, queueId, group) 三元组持久化消费者位点, 支持:
 * <ul>
 *   <li>内存读写 — O(1) ConcurrentHashMap</li>
 *   <li>周期持久化 — 5s 一次, 失败重试, 避免每次提交都写盘</li>
 *   <li>启动加载 — 从 offset.json 重建内存表</li>
 *   <li>读多写少 — ReentrantReadWriteLock 保护</li>
 * </ul>
 * <p>
 * 持久化格式 (JSON, 每行一条):
 * <pre>
 * {"topic":"T","queueId":0,"group":"G","offset":12345,"timestamp":1700000000}
 * </pre>
 *
 * <p><b>线程安全:</b> 所有公开方法均线程安全. 内部状态用 ConcurrentHashMap + ReentrantReadWriteLock。
 */
public class ConsumerOffsetManager {

    private static final Logger log = LoggerFactory.getLogger(ConsumerOffsetManager.class);

    /** 持久化文件名 */
    public static final String OFFSET_FILE_NAME = "consumer_offset.json";

    /** 写入防抖间隔 (毫秒) */
    private static final long FLUSH_INTERVAL_MS = 5_000L;

    private final String storePath;

    /** 内存表: key = topic + "@" + queueId + "@" + group -> offset */
    private final ConcurrentMap<String, OffsetEntry> offsetTable = new ConcurrentHashMap<>();

    /** 写时是否需要 flush (脏标记) */
    private final AtomicLong dirtyCount = new AtomicLong(0);

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /** 上次 flush 时间 (ms) */
    private volatile long lastFlushTime;

    private volatile boolean started;

    public ConsumerOffsetManager(String storePathRootDir) {
        this.storePath = storePathRootDir;
        this.lastFlushTime = System.currentTimeMillis();
    }

    /**
     * 启动: 加载持久化文件.
     */
    public void start() {
        if (started) {
            return;
        }
        try {
            File dir = new File(storePath);
            if (!dir.exists() && !dir.mkdirs()) {
                log.warn("create storePath failed: {}", storePath);
            }
            load();
            started = true;
            log.info("ConsumerOffsetManager started, storePath={}, loaded={}", storePath, offsetTable.size());
        } catch (Exception e) {
            log.error("ConsumerOffsetManager start failed", e);
        }
    }

    /**
     * 提交一个 Consumer Group 的 Queue 位点.
     *
     * @param topic   Topic 名称
     * @param queueId 队列 ID
     * @param group   Consumer Group
     * @param offset  已消费的最大位点 + 1 (即下次消费的起始 offset)
     */
    public void commitOffset(String topic, int queueId, String group, long offset) {
        if (topic == null || group == null) {
            return;
        }
        String key = makeKey(topic, queueId, group);
        OffsetEntry entry = new OffsetEntry(topic, queueId, group, offset, System.currentTimeMillis());
        offsetTable.put(key, entry);
        dirtyCount.incrementAndGet();
    }

    /**
     * 查询 Consumer Group 在某 Queue 上的位点. 不存在时返回 -1.
     */
    public long queryOffset(String topic, int queueId, String group) {
        if (topic == null || group == null) {
            return -1L;
        }
        OffsetEntry e = offsetTable.get(makeKey(topic, queueId, group));
        return e == null ? -1L : e.getOffset();
    }

    /**
     * 查询 Consumer Group 在某 Topic 下所有 Queue 的位点.
     *
     * @return key=queueId (String), value=offset; 无记录返回空 Map
     */
    public java.util.Map<String, Long> queryOffsetByGroup(String topic, String group) {
        java.util.Map<String, Long> result = new java.util.HashMap<>();
        if (topic == null || group == null) {
            return result;
        }
        String prefix = topic + "@";
        for (java.util.Map.Entry<String, OffsetEntry> kv : offsetTable.entrySet()) {
            OffsetEntry v = kv.getValue();
            if (v.getGroup().equals(group) && v.getTopic().equals(topic)) {
                result.put(String.valueOf(v.getQueueId()), v.getOffset());
            }
        }
        return result;
    }

    /**
     * 周期 flush 检查 (由 BrokerController 定时调用, 5s 一次).
     * <p>
     * 仅当距上次 flush 超过 {@link #FLUSH_INTERVAL_MS} 且存在脏数据时才真正落盘.
     */
    public void flushIfNecessary() {
        if (dirtyCount.get() == 0) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastFlushTime < FLUSH_INTERVAL_MS) {
            return;
        }
        flush();
    }

    /**
     * 强制刷盘 (Broker 关闭时调用).
     */
    public void flush() {
        if (dirtyCount.get() == 0) {
            return;
        }
        lock.writeLock().lock();
        try {
            if (dirtyCount.get() == 0) {
                return;
            }
            persist();
            dirtyCount.set(0);
            lastFlushTime = System.currentTimeMillis();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 关闭: flush 后清理状态.
     */
    public void shutdown() {
        try {
            flush();
            log.info("ConsumerOffsetManager shutdown, total entries={}", offsetTable.size());
        } catch (Exception e) {
            log.warn("ConsumerOffsetManager shutdown flush error", e);
        }
    }

    /**
     * 内存中记录的条数.
     */
    public int size() {
        return offsetTable.size();
    }

    /** 测试/运维用: 清空所有位点. */
    public void clear() {
        lock.writeLock().lock();
        try {
            offsetTable.clear();
            dirtyCount.set(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 列出当前已知的所有 Topic (用于 SlaveSynchronize 增量同步遍历).
     */
    public java.util.Set<String> getAllKnownTopics() {
        java.util.Set<String> topics = new java.util.HashSet<>();
        for (OffsetEntry e : offsetTable.values()) {
            if (e.getTopic() != null) {
                topics.add(e.getTopic());
            }
        }
        return topics;
    }

    /**
     * 按 (topic, queueId) 聚合所有 ConsumerGroup 的最大 offset.
     * <p>
     * 用于 SlaveSynchronize 全量同步: key=group(String), value=offset.
     */
    public java.util.Map<String, Long> queryOffsetByGroupAndQueue(String topic, int queueId) {
        java.util.Map<String, Long> result = new java.util.HashMap<>();
        if (topic == null) return result;
        for (java.util.Map.Entry<String, OffsetEntry> kv : offsetTable.entrySet()) {
            OffsetEntry v = kv.getValue();
            if (v.getTopic().equals(topic) && v.getQueueId() == queueId) {
                // 同 (topic, queueId) 多 group 取最大
                Long cur = result.get(v.getGroup());
                if (cur == null || v.getOffset() > cur) {
                    result.put(v.getGroup(), v.getOffset());
                }
            }
        }
        return result;
    }

    // ============== 内部方法 ==============

    private static String makeKey(String topic, int queueId, String group) {
        return topic + "@" + queueId + "@" + group;
    }

    private void load() {
        Path file = Paths.get(storePath, OFFSET_FILE_NAME);
        if (!Files.exists(file)) {
            return;
        }
        try {
            long count = Files.lines(file, StandardCharsets.UTF_8)
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseLine)
                    .filter(e -> e != null)
                    .peek(e -> offsetTable.put(makeKey(e.getTopic(), e.getQueueId(), e.getGroup()), e))
                    .count();
            log.info("loaded {} consumer offsets from {}", count, file);
        } catch (IOException e) {
            log.warn("load consumer offsets failed: {}", e.getMessage());
        }
    }

    private OffsetEntry parseLine(String line) {
        try {
            // 极简 JSON 解析: {"topic":"T","queueId":0,"group":"G","offset":12345,"timestamp":1700000000}
            String s = line.trim();
            if (s.startsWith("{") && s.endsWith("}")) {
                String body = s.substring(1, s.length() - 1);
                String topic = extract(body, "topic");
                String group = extract(body, "group");
                int queueId = Integer.parseInt(extract(body, "queueId"));
                long offset = Long.parseLong(extract(body, "offset"));
                long ts = Long.parseLong(extract(body, "timestamp"));
                return new OffsetEntry(topic, queueId, group, offset, ts);
            }
        } catch (Exception ignore) {
            // 跳过损坏行
        }
        return null;
    }

    private static String extract(String body, String key) {
        String pattern = "\"" + key + "\":";
        int start = body.indexOf(pattern);
        if (start < 0) {
            return "";
        }
        start += pattern.length();
        // 跳过空白
        while (start < body.length() && Character.isWhitespace(body.charAt(start))) {
            start++;
        }
        if (start >= body.length()) {
            return "";
        }
        char c = body.charAt(start);
        if (c == '"') {
            // 字符串值
            int end = body.indexOf('"', start + 1);
            return end < 0 ? "" : body.substring(start + 1, end);
        }
        // 数值
        int end = start;
        while (end < body.length() && "0123456789-".indexOf(body.charAt(end)) >= 0) {
            end++;
        }
        return body.substring(start, end);
    }

    private void persist() {
        Path file = Paths.get(storePath, OFFSET_FILE_NAME);
        StringBuilder sb = new StringBuilder();
        for (OffsetEntry e : offsetTable.values()) {
            sb.append("{\"topic\":\"").append(e.getTopic())
                    .append("\",\"queueId\":").append(e.getQueueId())
                    .append(",\"group\":\"").append(e.getGroup())
                    .append("\",\"offset\":").append(e.getOffset())
                    .append(",\"timestamp\":").append(e.getTimestamp())
                    .append("}\n");
        }
        try {
            // 原子写: 写到 .tmp 再 rename, 避免崩溃时半截文件
            Path tmp = Paths.get(storePath, OFFSET_FILE_NAME + ".tmp");
            Files.write(tmp, sb.toString().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            try {
                Files.deleteIfExists(file);
            } catch (IOException ignored) {
            }
            Files.move(tmp, file, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("persist consumer offsets failed: {}", e.getMessage());
        }
    }

    /** 位点条目 (不可变). */
    public static final class OffsetEntry {
        private final String topic;
        private final int queueId;
        private final String group;
        private final long offset;
        private final long timestamp;

        public OffsetEntry(String topic, int queueId, String group, long offset, long timestamp) {
            this.topic = topic;
            this.queueId = queueId;
            this.group = group;
            this.offset = offset;
            this.timestamp = timestamp;
        }

        public String getTopic() { return topic; }
        public int getQueueId() { return queueId; }
        public String getGroup() { return group; }
        public long getOffset() { return offset; }
        public long getTimestamp() { return timestamp; }
    }
}
