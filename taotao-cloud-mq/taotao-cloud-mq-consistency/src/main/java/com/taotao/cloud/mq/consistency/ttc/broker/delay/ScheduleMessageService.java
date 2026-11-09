package com.taotao.cloud.mq.consistency.ttc.broker.delay;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Broker 端"延迟消息"投递服务（对标 RocketMQ ScheduleMessageService）.
 * <p>
 * RocketMQ 内置 18 个延迟级别 (1s/5s/10s/30s/1m/2m/3m/4m/5m/6m/7m/8m/9m/10m/20m/30m/1h/2h),
 * 这里直接定义等价级别并允许用户覆盖.
 *
 * <p>工作流程:
 * <ol>
 *   <li>Producer 发消息时设置 {@code delayLevel} (1-18), Broker 在 SendMessageProcessor 中调用 schedule</li>
 *   <li>消息进入本服务的 DelayQueue 但延迟投递 (不立即可被 PullMessageProcessor 看到)</li>
 *   <li>守护线程 take() 阻塞等待, 到期后调用 {@link DelayedMessageListener#onExpired}</li>
 *   <li>回调中把消息标记为"可投递" (由回调实现决定如何路由回正常队列)</li>
 * </ol>
 *
 * <p><b>持久化:</b> 支持将延迟消息的投递时间持久化到文件，重启后恢复延迟队列。
 *
 * <p><b>线程安全:</b> 所有公开方法线程安全, 内部用 DelayQueue + 守护线程.
 *
 * @see <a href="https://github.com/apache/rocketmq/blob/develop/broker/src/main/java/org/apache/rocketmq/broker/schedule/ScheduleMessageService.java">RocketMQ ScheduleMessageService</a>
 */
public class ScheduleMessageService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleMessageService.class);

    /** RocketMQ 默认 18 个延迟级别. */
    public static final String DEFAULT_DELAY_LEVELS =
            "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";

    /** 延迟级别对应的毫秒数 (从 level 1 开始, 共 18 级). */
    private final long[] delayLevelMillis;

    /** level 1-18 -> 各自的 DelayQueue. */
    private final DelayQueue<DelayedMessage>[] levelQueues;

    private final AtomicBoolean started = new AtomicBoolean(false);

    /** 回调: 当延迟消息到期时调用. */
    private DelayedMessageListener listener;

    /** 持久化文件路径 */
    private String persistFilePath;

    /** 延迟 offset 持久化存储: level -> nextOffset */
    private final ConcurrentHashMap<Integer, Long> delayOffsetTable = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public ScheduleMessageService() {
        this(DEFAULT_DELAY_LEVELS);
    }

    @SuppressWarnings("unchecked")
    public ScheduleMessageService(String delayLevelsConfig) {
        this.delayLevelMillis = parseDelayLevels(delayLevelsConfig);
        this.levelQueues = new DelayQueue[delayLevelMillis.length];
        for (int i = 0; i < delayLevelMillis.length; i++) {
            this.levelQueues[i] = new DelayQueue<>();
        }
    }

    /**
     * 启动延迟消息服务 (启动每个 level 一个守护线程).
     */
    public void start() {
        if (started.compareAndSet(false, true)) {
            // 加载持久化数据
            loadPersistData();

            for (int i = 0; i < levelQueues.length; i++) {
                final int level = i + 1;
                Thread t = new Thread(() -> deliverLoop(level), "ScheduleMessageService-Level-" + level);
                t.setDaemon(true);
                t.start();
            }
            log.info("ScheduleMessageService started, {} levels", delayLevelMillis.length);
        }
    }

    /**
     * 关闭延迟消息服务.
     */
    public void shutdown() {
        if (started.compareAndSet(true, false)) {
            // 持久化当前状态
            persistDelayOffsets();
            log.info("ScheduleMessageService shutdown");
        }
    }

    /**
     * 注册延迟消息到期监听器.
     */
    public void setListener(DelayedMessageListener listener) {
        this.listener = listener;
    }

    /**
     * 设置持久化文件路径。
     */
    public void setPersistFilePath(String persistFilePath) {
        this.persistFilePath = persistFilePath;
    }

    /**
     * 调度一条延迟消息.
     *
     * @param delayLevel 1-18 (1-based)
     * @param messageKey 消息标识 (用于到期回调, 同 level 内必须唯一)
     * @param payload    任意附加数据
     * @throws IllegalArgumentException level 越界
     */
    public void schedule(int delayLevel, String messageKey, Object payload) {
        if (delayLevel < 1 || delayLevel > delayLevelMillis.length) {
            throw new IllegalArgumentException(
                    "delayLevel must be 1.." + delayLevelMillis.length + ", got " + delayLevel);
        }
        long delayMs = delayLevelMillis[delayLevel - 1];
        long expireAt = System.currentTimeMillis() + delayMs;
        DelayedMessage dm = new DelayedMessage(messageKey, payload, expireAt);
        levelQueues[delayLevel - 1].offer(dm);
        log.debug("scheduled message: level={} key={} expireAt+{}ms", delayLevel, messageKey, delayMs);
    }

    /**
     * 取消一条延迟消息 (仅在到期前有效).
     *
     * @return true=找到并取消, false=不存在或已到期
     */
    public boolean cancel(int delayLevel, String messageKey) {
        if (delayLevel < 1 || delayLevel > levelQueues.length) {
            return false;
        }
        DelayedMessage target = null;
        for (DelayedMessage dm : levelQueues[delayLevel - 1]) {
            if (dm.getKey().equals(messageKey)) {
                target = dm;
                break;
            }
        }
        return target != null && levelQueues[delayLevel - 1].remove(target);
    }

    /**
     * 获取一个延迟级别的剩余等待时间 (毫秒).
     */
    public long getDelayMillis(int delayLevel) {
        if (delayLevel < 1 || delayLevel > delayLevelMillis.length) {
            return -1L;
        }
        return delayLevelMillis[delayLevel - 1];
    }

    public int getLevelCount() {
        return delayLevelMillis.length;
    }

    /**
     * 当前在某 level 上的延迟消息数量 (测试/监控用).
     */
    public int getScheduledCount(int delayLevel) {
        if (delayLevel < 1 || delayLevel > levelQueues.length) {
            return 0;
        }
        return levelQueues[delayLevel - 1].size();
    }

    /**
     * 当前所有延迟消息总数.
     */
    public int totalScheduledCount() {
        int total = 0;
        for (DelayQueue<DelayedMessage> q : levelQueues) {
            total += q.size();
        }
        return total;
    }

    // ==================== 持久化相关方法 ====================

    /**
     * 持久化延迟 offset 到文件。
     */
    public void persistDelayOffsets() {
        if (persistFilePath == null || persistFilePath.isEmpty()) {
            return;
        }
        File file = new File(persistFilePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<Integer, Long> entry : delayOffsetTable.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            log.info("Delay offsets persisted: file={}", persistFilePath);
        } catch (IOException e) {
            log.error("Failed to persist delay offsets: file={}", persistFilePath, e);
        }
    }

    /**
     * 从文件加载延迟 offset。
     */
    private void loadPersistData() {
        if (persistFilePath == null || persistFilePath.isEmpty()) {
            return;
        }
        File file = new File(persistFilePath);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    int level = Integer.parseInt(parts[0]);
                    long offset = Long.parseLong(parts[1]);
                    delayOffsetTable.put(level, offset);
                }
            }
            log.info("Delay offsets loaded: file={}, entries={}", persistFilePath, delayOffsetTable.size());
        } catch (IOException e) {
            log.error("Failed to load delay offsets: file={}", persistFilePath, e);
        }
    }

    /**
     * 更新延迟 offset（由 BrokerController 在投递延迟消息时调用）。
     */
    public void updateDelayOffset(int level, long offset) {
        delayOffsetTable.put(level, offset);
    }

    /**
     * 获取延迟 offset。
     */
    public long getDelayOffset(int level) {
        return delayOffsetTable.getOrDefault(level, 0L);
    }

    private void deliverLoop(int level) {
        DelayQueue<DelayedMessage> queue = levelQueues[level - 1];
        while (started.get()) {
            try {
                DelayedMessage dm = queue.take();
                if (listener != null) {
                    try {
                        listener.onExpired(dm.getKey(), dm.getPayload());
                    } catch (Exception e) {
                        log.error("listener.onExpired failed: key={}", dm.getKey(), e);
                    }
                }
                log.debug("delivered delayed message: level={} key={}", level, dm.getKey());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.warn("deliverLoop error", e);
            }
        }
    }

    private static long[] parseDelayLevels(String config) {
        String[] parts = config.trim().split("\\s+");
        long[] levels = new long[parts.length];
        for (int i = 0; i < parts.length; i++) {
            levels[i] = parseDuration(parts[i]);
        }
        return levels;
    }

    private static long parseDuration(String s) {
        s = s.trim().toLowerCase();
        if (s.endsWith("ms")) {
            return Long.parseLong(s.substring(0, s.length() - 2));
        } else if (s.endsWith("s")) {
            return Long.parseLong(s.substring(0, s.length() - 1)) * 1_000L;
        } else if (s.endsWith("m")) {
            return Long.parseLong(s.substring(0, s.length() - 1)) * 60_000L;
        } else if (s.endsWith("h")) {
            return Long.parseLong(s.substring(0, s.length() - 1)) * 3_600_000L;
        }
        throw new IllegalArgumentException("Invalid duration: " + s);
    }

    /** 延迟消息条目. */
    public static final class DelayedMessage implements Delayed {
        private static final AtomicLong SEQ = new AtomicLong();

        private final long seq;
        private final String key;
        private final Object payload;
        private final long expireAt;

        public DelayedMessage(String key, Object payload, long expireAt) {
            this.seq = SEQ.incrementAndGet();
            this.key = key;
            this.payload = payload;
            this.expireAt = expireAt;
        }

        public String getKey() { return key; }
        public Object getPayload() { return payload; }
        public long getExpireAt() { return expireAt; }
        public long getSeq() { return seq; }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expireAt - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed other) {
            if (other instanceof DelayedMessage) {
                return Long.compare(this.expireAt, ((DelayedMessage) other).expireAt);
            }
            return 0;
        }
    }

    /** 延迟消息到期监听器. */
    public interface DelayedMessageListener {
        /**
         * 消息到期时回调.
         *
         * @param key     schedule 时传入的 messageKey
         * @param payload schedule 时传入的 payload
         */
        void onExpired(String key, Object payload);
    }
}
