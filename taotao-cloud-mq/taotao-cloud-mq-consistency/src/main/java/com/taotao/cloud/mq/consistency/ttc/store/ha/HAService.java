package com.taotao.cloud.mq.consistency.ttc.store.ha;

/**
 * HA 服务接口（对标 RocketMQ HAService）.
 * <p>
 * 提供 Master-Slave 之间 CommitLog 同步能力.
 * <p>
 * 工作原理:
 * <ol>
 *   <li>Slave 启动后通过 {@link #start()} 建立到 Master 的 HA 客户端 (HAClient)</li>
 *   <li>Slave 周期性上报本地 offset (Master 据此判断同步状态)</li>
 *   <li>Master 收到 putMessage 后, 异步推送给所有已注册的 Slave (HAConnection)</li>
 *   <li>Slave 收到推送后写入本地 CommitLog + ConsumerOffset</li>
 *   <li>Master 的 GroupTransferService 等待至少一个 Slave ack 后, 才返回 send success</li>
 * </ol>
 *
 * <p><b>线程安全:</b> 由具体实现保证.
 *
 * @see <a href="https://github.com/apache/rocketmq/blob/develop/store/src/main/java/org/apache/rocketmq/store/ha/DefaultHAService.java">RocketMQ DefaultHAService</a>
 */
public interface HAService {

    /**
     * 启动 HA 服务.
     * <p>
     * Master 模式: 启动 Accept 线程, 接受 Slave 连接
     * Slave 模式: 启动 HAClient 线程, 连接 Master
     */
    void start() throws Exception;

    /**
     * 关闭 HA 服务.
     */
    void shutdown();

    /**
     * 当前是否 Slave 模式.
     */
    boolean isSlave();

    /**
     * 通知 HA 服务有新消息写入 (仅 Master 调用).
     * <p>
     * Master CommitLog.putMessage 成功后调用, HA 服务会异步推送给所有 Slave.
     *
     * @param commitLogOffset 新消息的 CommitLog 物理偏移量
     * @param bodyBytes       写入的消息字节 (用于复制到 Slave)
     */
    void notifyMessageArrived(long commitLogOffset, byte[] bodyBytes);

    /**
     * 等待 Slave ack 到指定 offset (Master 调用).
     * <p>
     * 实现 GroupCommit 语义: 等至少一个 Slave ack 后才返回.
     *
     * @param commitLogOffset 期望 Slave ack 的 CommitLog offset
     * @param timeoutMillis   等待超时 (毫秒)
     * @return true=至少一个 Slave 已 ack; false=超时
     */
    boolean waitForSlaveAck(long commitLogOffset, long timeoutMillis);

    /**
     * 上报本地 offset (Slave 调用, 由 HAClient 周期性触发).
     *
     * @param maxOffset Slave 本地最大 CommitLog offset
     */
    void reportSlaveOffset(long maxOffset);

    /**
     * 当前 Slave ack 进度 (Master 视角).
     */
    long getSlaveAckOffset();

    /**
     * 当前 Slave 连接数 (Master 视角, Slave 视角返回 0).
     */
    int getConnectionCount();
}
