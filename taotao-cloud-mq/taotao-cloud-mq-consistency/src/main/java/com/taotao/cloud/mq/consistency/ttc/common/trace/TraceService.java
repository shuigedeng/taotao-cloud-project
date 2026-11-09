package com.taotao.cloud.mq.consistency.ttc.common.trace;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息轨迹服务（对标 RocketMQ TraceService）.
 * <p>
 * 收集和管理消息全链路轨迹信息：
 * <ul>
 *   <li>Producer 发送轨迹</li>
 *   <li>Broker 存储轨迹</li>
 *   <li>Consumer 消费轨迹</li>
 * </ul>
 * <p>
 * 轨迹数据异步批量写入，减少对主流程的影响。
 */
public class TraceService {

    private static final Logger log = LoggerFactory.getLogger(TraceService.class);

    /** 默认批量写入大小 */
    private static final int DEFAULT_BATCH_SIZE = 1024;

    /** 默认刷新间隔（毫秒） */
    private static final long DEFAULT_FLUSH_INTERVAL_MILLIS = 5000L;

    /** 轨迹数据队列 */
    private final ConcurrentLinkedQueue<TraceBean> traceQueue = new ConcurrentLinkedQueue<>();

    /** 统计计数器 */
    private final AtomicLong totalTraces = new AtomicLong(0);
    private final AtomicLong droppedTraces = new AtomicLong(0);

    /** 批量写入大小 */
    private int batchSize = DEFAULT_BATCH_SIZE;

    /** 刷新间隔 */
    private long flushIntervalMillis = DEFAULT_FLUSH_INTERVAL_MILLIS;

    /** 轨迹监听器 */
    private TraceListener traceListener;

    /** 调度器 */
    private ScheduledExecutorService scheduler;

    /** 是否运行中 */
    private volatile boolean running = false;

    /**
     * 轨迹监听器接口。
     */
    public interface TraceListener {
        /**
         * 批量处理轨迹数据。
         *
         * @param traces 轨迹列表
         */
        void onTraceBatch(List<TraceBean> traces);
    }

    public TraceService() {
    }

    public TraceService(int batchSize, long flushIntervalMillis) {
        this.batchSize = batchSize;
        this.flushIntervalMillis = flushIntervalMillis;
    }

    /**
     * 启动轨迹服务。
     */
    public void start() {
        if (running) {
            return;
        }
        this.running = true;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "TraceServiceThread");
            t.setDaemon(true);
            return t;
        });
        this.scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                flush();
            }
        }, flushIntervalMillis, flushIntervalMillis, TimeUnit.MILLISECONDS);
        log.info("TraceService started: batchSize={}, flushInterval={}ms", batchSize, flushIntervalMillis);
    }

    /**
     * 停止轨迹服务。
     */
    public void shutdown() {
        this.running = false;
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        // 最后一次刷新
        flush();
        log.info("TraceService shutdown: totalTraces={}, droppedTraces={}",
                totalTraces.get(), droppedTraces.get());
    }

    /**
     * 设置轨迹监听器。
     */
    public void setTraceListener(TraceListener traceListener) {
        this.traceListener = traceListener;
    }

    /**
     * 记录轨迹数据。
     *
     * @param trace 轨迹数据
     */
    public void recordTrace(TraceBean trace) {
        if (!running) {
            return;
        }
        totalTraces.incrementAndGet();
        traceQueue.offer(trace);

        // 队列过大时丢弃
        if (traceQueue.size() > batchSize * 2) {
            droppedTraces.incrementAndGet();
        }
    }

    /**
     * 批量刷新轨迹数据。
     */
    private void flush() {
        if (traceQueue.isEmpty() || traceListener == null) {
            return;
        }

        List<TraceBean> batch = new ArrayList<>();
        int count = 0;
        TraceBean trace;
        while (count < batchSize && (trace = traceQueue.poll()) != null) {
            batch.add(trace);
            count++;
        }

        if (!batch.isEmpty()) {
            try {
                traceListener.onTraceBatch(batch);
            } catch (Exception e) {
                log.error("Trace batch write failed: size={}", batch.size(), e);
            }
        }
    }

    /**
     * 获取队列中的轨迹数量。
     */
    public int getQueueSize() {
        return traceQueue.size();
    }

    /**
     * 获取总轨迹数。
     */
    public long getTotalTraces() {
        return totalTraces.get();
    }

    /**
     * 获取丢弃的轨迹数。
     */
    public long getDroppedTraces() {
        return droppedTraces.get();
    }

    /**
     * 是否正在运行。
     */
    public boolean isRunning() {
        return running;
    }

    // ==================== 便捷方法 ====================

    /**
     * 记录 Producer 发送轨迹。
     */
    public void recordProducerSend(String topic, String msgId, String keys, String tags,
                                   String brokerAddr, String producerAddr,
                                   String producerGroup, String sendStatus, long costTime) {
        TraceBean trace = new TraceBean(TraceType.ProducerSend, topic, msgId);
        trace.setKeys(keys);
        trace.setTags(tags);
        trace.setBrokerAddr(brokerAddr);
        trace.setProducerAddr(producerAddr);
        trace.setProducerGroup(producerGroup);
        trace.setSendStatus(sendStatus);
        trace.setCostTime(costTime);
        recordTrace(trace);
    }

    /**
     * 记录 Consumer 消费轨迹。
     */
    public void recordConsumerConsume(String topic, String msgId, String keys,
                                     String consumerAddr, String consumerGroup,
                                     String consumeStatus, long costTime) {
        TraceBean trace = new TraceBean(TraceType.ConsumerConsume, topic, msgId);
        trace.setKeys(keys);
        trace.setConsumerAddr(consumerAddr);
        trace.setConsumerGroup(consumerGroup);
        trace.setConsumeStatus(consumeStatus);
        trace.setCostTime(costTime);
        recordTrace(trace);
    }

    /**
     * 记录消费失败轨迹。
     */
    public void recordConsumeFailed(String topic, String msgId, String errorMsg) {
        TraceBean trace = new TraceBean(TraceType.ConsumerConsumeFail, topic, msgId);
        trace.setErrorMsg(errorMsg);
        trace.setConsumeStatus("FAIL");
        recordTrace(trace);
    }
}
