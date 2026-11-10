package com.taotao.cloud.mq.common.ha;

import com.taotao.cloud.mq.common.util.AtomicNumberSerializer;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 数据版本号（对标 RocketMQ DataVersion）.
 * <p>
 * Master 在每次更新 TopicConfig / ConsumerOffset / DelayOffset / SubscriptionGroup 时递增版本号.
 * Slave 通过对比本地 DataVersion 与 Master 的 DataVersion 决定是否需要同步, 实现增量同步.
 * <p>
 * 字段含义:
 * <ul>
 *   <li>timestamp: 版本变更时间戳 (毫秒), 用于时钟漂移检测</li>
 *   <li>counter:  版本号, 单调递增, 越大越新</li>
 * </ul>
 *
 * <p><b>线程安全:</b> counter 用 AtomicLong 保证并发安全.
 */
public class DataVersion implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private volatile long timestamp = System.currentTimeMillis();
    /** 标量进出：JsonUtil 的默认 POJO 分支动不了 final 的 AtomicLong（实测抛 IllegalArgumentException）。 */
    @JsonSerialize(using = AtomicNumberSerializer.AtomicNumberValueSerializer.class)
    @JsonDeserialize(using = AtomicNumberSerializer.AtomicNumberValueDeserializer.class)
    private final AtomicLong counter = new AtomicLong(0L);

    public DataVersion() {
    }

    /**
     * 标记一次版本变更: timestamp = now, counter + 1.
     */
    public void assignNewVersion() {
        this.timestamp = System.currentTimeMillis();
        this.counter.incrementAndGet();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public AtomicLong getCounter() {
        return counter;
    }

    public long getCounterValue() {
        return counter.get();
    }

    /**
     * 比较 self 是否比 other 更新 (counter 越大越新; counter 相等时 timestamp 越大越新).
     */
    public boolean isNewerThan(DataVersion other) {
        if (other == null) {
            return true;
        }
        if (this.counter.get() != other.counter.get()) {
            return this.counter.get() > other.counter.get();
        }
        return this.timestamp > other.timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataVersion)) return false;
        DataVersion that = (DataVersion) o;
        return timestamp == that.timestamp && counter.get() == that.counter.get();
    }

    @Override
    public int hashCode() {
        return (int) (timestamp ^ counter.get());
    }

    @Override
    public String toString() {
        return "DataVersion{ts=" + timestamp + ", counter=" + counter.get() + "}";
    }
}
