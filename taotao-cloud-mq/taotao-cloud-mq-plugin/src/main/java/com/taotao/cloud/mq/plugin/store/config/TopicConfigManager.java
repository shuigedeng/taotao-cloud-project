package com.taotao.cloud.mq.plugin.store.config;

import com.taotao.cloud.mq.common.TopicConfig;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.plugin.store.config.ConsumerOffsetManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Topic 配置管理器（与 {@link ConsumerOffsetManager} 同形状）.
 * <p>
 * Topic 表是控制面数据：条数量级是个位数到几百，写频率极低（建/改 topic、slave 从主全量覆盖），
 * 读频率极高（每条 pull、每次心跳上报）。所以这里用"内存表 + 每次变更立刻整份原子落盘"：
 * <ul>
 *   <li>内存读写 — O(1) ConcurrentHashMap，读侧不加锁</li>
 *   <li>变更即落盘 — 写入口在一次写锁内完成"改表 + 重写文件"，两者之间不存在可观察的中间态；
 *       调用方拿到返回时，这条配置已经在盘上了（不靠周期任务，因此也不需要等）</li>
 *   <li>启动加载 — {@link #start()} 从 {@value #TOPIC_CONFIG_FILE_NAME} 重建内存表，
 *       必须发生在 broker 开始收请求之前</li>
 *   <li>原子写 — 先写 .tmp 再 {@code Files.move(..., REPLACE_EXISTING)}，崩溃时不会留半截文件</li>
 * </ul>
 * 文件格式：每行一条 {@code TopicConfig} 的 JSON（与 {@code consumer_offset.json} 同口径，
 * 一行坏了一行跳过，不牵连同文件其余条目）.
 *
 * <p><b>线程安全:</b> 所有公开方法均线程安全。表本身是 ConcurrentMap，写入口用
 * {@link ReentrantReadWriteLock} 串行化，保证"表"与"盘上的那一行"成对变化。
 */
public class TopicConfigManager {

    private static final Logger log = LoggerFactory.getLogger(TopicConfigManager.class);

    /** 持久化文件名. */
    public static final String TOPIC_CONFIG_FILE_NAME = "topic_config.json";

    private final String storePath;

    /** 唯一的 topic 配置表：这张表的镜像就是 {@link #TOPIC_CONFIG_FILE_NAME}. */
    private final ConcurrentMap<String, TopicConfig> topicConfigTable =
            new ConcurrentHashMap<String, TopicConfig>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private volatile boolean started;

    public TopicConfigManager(String storePathRootDir) {
        this.storePath = storePathRootDir;
    }

    /**
     * 启动: 把盘上的 topic 表读回内存.
     * <p>
     * 幂等。文件不存在（首次启动）时得到一张空表，这不是错误。
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
            log.info("TopicConfigManager started, storePath={}, loaded={}", storePath, topicConfigTable.size());
        } catch (Exception e) {
            log.error("TopicConfigManager start failed", e);
        }
    }

    /**
     * 建 / 改一条 Topic 配置，并在同一次写锁里落盘.
     *
     * @return 落进表里的那条配置（与传入同一实例，便于调用方回读）
     */
    public TopicConfig putTopicConfig(TopicConfig config) {
        if (config == null || config.getTopicName() == null || config.getTopicName().isEmpty()) {
            return null;
        }
        lock.writeLock().lock();
        try {
            topicConfigTable.put(config.getTopicName(), config);
            persist();
            return config;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 删一条 Topic 配置，并落盘。
     * <p>
     * 对标 RocketMQ deleteTopic 的语义：只摘配置表与路由，不去动 CommitLog 里已落盘的消息
     * —— 存储层没有按 topic 物理清除的能力，硬删会让 offset 与索引同时失真。
     *
     * @return 之前存在的那条配置；本来就没有则返回 null（调用方据此区分「删掉了」与「本来就没有」）
     */
    public TopicConfig deleteTopicConfig(String topic) {
        if (topic == null || topic.isEmpty()) {
            return null;
        }
        lock.writeLock().lock();
        try {
            TopicConfig removed = topicConfigTable.remove(topic);
            if (removed != null) {
                persist();
            }
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 用 incoming <b>完全替换</b>本地表（包括删掉 incoming 里没有的条目），并落盘.
     * <p>
     * 主从同步走的就是这条路：覆盖之后 slave 本地的盘上看到的也必须是这一份，
     * 否则内存与文件会分叉，重启后读到的是上一次同步的那一份。
     */
    public void replaceAllTopicConfigs(Map<String, TopicConfig> incoming) {
        lock.writeLock().lock();
        try {
            topicConfigTable.clear();
            if (incoming != null) {
                for (Map.Entry<String, TopicConfig> entry : incoming.entrySet()) {
                    TopicConfig config = entry.getValue();
                    if (config != null && config.getTopicName() == null) {
                        config.setTopicName(entry.getKey());
                    }
                    if (config != null && config.getTopicName() != null && !config.getTopicName().isEmpty()) {
                        topicConfigTable.put(config.getTopicName(), config);
                    }
                }
            }
            persist();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 按 topic 名查一条配置，不存在返回 null.
     */
    public TopicConfig selectTopicConfig(String topic) {
        if (topic == null) {
            return null;
        }
        return topicConfigTable.get(topic);
    }

    /**
     * 当前表的快照（读侧不暴露内部 map）.
     */
    public Map<String, TopicConfig> getAllTopicConfigs() {
        return new HashMap<String, TopicConfig>(topicConfigTable);
    }

    /**
     * 内存中记录的条数.
     */
    public int size() {
        return topicConfigTable.size();
    }

    /** 测试/运维用: 清空 topic 表并落盘. */
    public void clear() {
        lock.writeLock().lock();
        try {
            topicConfigTable.clear();
            persist();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 强制落盘（变更入口内部已经落过, 这里是关机时的兜底）。
     */
    public void flush() {
        lock.writeLock().lock();
        try {
            persist();
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
            log.info("TopicConfigManager shutdown, total topics={}", topicConfigTable.size());
        } catch (Exception e) {
            log.warn("TopicConfigManager shutdown flush error", e);
        }
    }

    // ============== 内部方法 ==============

    private void load() {
        Path file = Paths.get(storePath, TOPIC_CONFIG_FILE_NAME);
        if (!Files.exists(file)) {
            return;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.warn("load topic configs failed: {}", e.getMessage());
            return;
        }
        int loaded = 0;
        int skipped = 0;
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            TopicConfig config = parseLine(line);
            if (config == null || config.getTopicName() == null || config.getTopicName().isEmpty()) {
                skipped++;
                continue;
            }
            topicConfigTable.put(config.getTopicName(), config);
            loaded++;
        }
        log.info("loaded {} topic configs from {} (skipped {} bad lines)", loaded, file, skipped);
    }

    private static TopicConfig parseLine(String line) {
        try {
            return JsonCodec.fromJson(line.trim(), TopicConfig.class);
        } catch (Exception ignore) {
            // 跳过损坏行
            return null;
        }
    }

    /**
     * 整份重写：写到 .tmp 再原子替换，避免崩溃时留下半截文件.
     * <p>
     * 必须在写锁里调用（表在此刻不会变）。
     */
    private void persist() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, TopicConfig> entry : topicConfigTable.entrySet()) {
            TopicConfig config = entry.getValue();
            if (config == null) {
                continue;
            }
            if (config.getTopicName() == null) {
                config.setTopicName(entry.getKey());
            }
            sb.append(JsonCodec.toJson(config)).append('\n');
        }
        Path file = Paths.get(storePath, TOPIC_CONFIG_FILE_NAME);
        try {
            Path tmp = Paths.get(storePath, TOPIC_CONFIG_FILE_NAME + ".tmp");
            Files.write(tmp, sb.toString().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            try {
                Files.deleteIfExists(file);
            } catch (IOException ignored) {
            }
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("persist topic configs failed: {}", e.getMessage());
        }
    }
}
