package com.taotao.cloud.mq.consistency.ttc.broker.ha;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Master 端维护的 Slave 连接状态（对标 RocketMQ HAConnection 简化版）.
 * <p>
 * 每个 Slave 注册到 Master 后, Master 创建一个 HAConnectionState:
 * <ul>
 *   <li>slaveAddr: Slave 的 HA 客户端地址</li>
 *   <li>lastAckOffset: 最近一次 ack 的 CommitLog offset</li>
 *   <li>lastHeartbeatMs: 最近一次心跳时间</li>
 *   <li>pendingAckRequest: 等待中的 ack 请求 (GroupTransfer 语义)</li>
 * </ul>
 *
 * <p>心跳超时检测: heartbeat 超时则视为 Slave 掉线.
 *
 * <p><b>线程安全:</b> 所有字段用 volatile 或 AtomicLong, 单个 HAConnection 的请求处理在单一线程.
 */
public class HAConnectionState {

    private final String slaveAddr;

    /** 最近一次 ack 的 offset (从 HA_REPORT_OFFSET 上报). */
    private final AtomicLong lastAckOffset = new AtomicLong(-1L);

    /** 最近一次心跳时间 (毫秒). */
    private volatile long lastHeartbeatMs = System.currentTimeMillis();

    /** 创建时间. */
    private final long createTimeMs = System.currentTimeMillis();

    public HAConnectionState(String slaveAddr) {
        this.slaveAddr = slaveAddr;
    }

    public String getSlaveAddr() {
        return slaveAddr;
    }

    public long getLastAckOffset() {
        return lastAckOffset.get();
    }

    /**
     * 上报 ack offset (由 HA_REPORT_OFFSET 处理器调用).
     */
    public void updateAckOffset(long offset) {
        lastAckOffset.set(offset);
        lastHeartbeatMs = System.currentTimeMillis();
    }

    public long getLastHeartbeatMs() {
        return lastHeartbeatMs;
    }

    /**
     * 是否心跳超时 (默认 30s).
     * <p>约定: timeoutMs &lt;= 0 表示"永不过期" (超时检测禁用).
     */
    public boolean isExpired(long timeoutMs) {
        if (timeoutMs <= 0L) {
            return false;
        }
        return System.currentTimeMillis() - lastHeartbeatMs > timeoutMs;
    }

    public long getCreateTimeMs() {
        return createTimeMs;
    }

    @Override
    public String toString() {
        return "HAConnectionState{" +
                "slave='" + slaveAddr + '\'' +
                ", ackOffset=" + lastAckOffset.get() +
                ", lastHb=" + lastHeartbeatMs +
                '}';
    }
}
