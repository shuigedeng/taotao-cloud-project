package com.taotao.cloud.mq.broker.ha;

import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.plugin.store.config.ConsumerOffsetManager;
import com.taotao.cloud.mq.plugin.store.ha.HAService;
import com.taotao.cloud.mq.plugin.store.log.CommitLog;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 默认 HA 服务实现（对标 RocketMQ DefaultHAService）.
 * <p>
 * 设计:
 * <ul>
 *   <li>Master 模式: 维护 Slave 连接状态, 接收 putMessage 通知, 异步推送给 Slave</li>
 *   <li>Slave 模式: 启动 HAClient 连接到 Master, 周期性 pull Master CommitLog 增量</li>
 * </ul>
 * <p>
 * 实现简化:
 * <ul>
 *   <li>用 {@link LinkedBlockingQueue} 暂存待推送的消息 (Master -> Slave)</li>
 *   <li>后台 daemon 线程把队列里消息通过 HA_PUSH_COMMITLOG 推给所有 Slave</li>
 *   <li>Slave HAClient 周期性 send HA_REPORT_OFFSET 上报本地 offset</li>
 *   <li>Master HAConnectionState 记录每个 slave 的 ack offset, GroupTransfer 用 CountDownLatch 等待</li>
 * </ul>
 *
 * <p><b>线程安全:</b> slaveConnections 是 ConcurrentMap, 各字段用 volatile/AtomicLong.
 *
 * @see <a href="https://github.com/apache/rocketmq/blob/develop/store/src/main/java/org/apache/rocketmq/store/ha/DefaultHAService.java">RocketMQ DefaultHAService</a>
 */
public class DefaultHAService implements HAService {

    private static final Logger log = LoggerFactory.getLogger(DefaultHAService.class);

    /** Slave 心跳超时 (毫秒). */
    private static final long SLAVE_HEARTBEAT_TIMEOUT_MS = 30_000L;

    /** 待推送消息队列 (Master -> Slave). */
    private final LinkedBlockingQueue<HAPushEntry> pushQueue = new LinkedBlockingQueue<>(1024);

    /** 推送后台线程 (Master 模式). */
    private Thread pushThread;

    /** HA 客户端线程 (Slave 模式). */
    private Thread slaveSyncThread;

    /** Master 视角: slaveAddr -> HAConnectionState. */
    private final ConcurrentMap<String, HAConnectionState> slaveConnections = new ConcurrentHashMap<>();

    /** Slave 视角: 本地已 ack 的最大 CommitLog offset. */
    private final AtomicLong localAckOffset = new AtomicLong(-1L);

    /** Master 视角: 所有 slave 最小 ack offset (即同步进度). */
    private final AtomicLong slaveAckOffset = new AtomicLong(0L);

    private final BrokerController brokerController;
    private final boolean slave;

    private volatile boolean running;

    public DefaultHAService(BrokerController brokerController) {
        this.brokerController = brokerController;
        this.slave = !brokerController.getBrokerConfig().isMaster();
    }

    @Override
    public void start() throws Exception {
        if (running) {
            return;
        }
        running = true;

        if (slave) {
            // Slave 模式: 启动周期性同步线程
            slaveSyncThread = new Thread(this::slaveSyncLoop, "HASlaveSync-" + brokerController.getBrokerConfig().getBrokerName());
            slaveSyncThread.setDaemon(true);
            slaveSyncThread.start();
            log.info("HAService (SLAVE) started");
        } else {
            // Master 模式: 启动推送线程
            pushThread = new Thread(this::masterPushLoop, "HAMasterPush-" + brokerController.getBrokerConfig().getBrokerName());
            pushThread.setDaemon(true);
            pushThread.start();
            log.info("HAService (MASTER) started");
        }
    }

    @Override
    public void shutdown() {
        running = false;
        if (pushThread != null) {
            pushThread.interrupt();
            pushThread = null;
        }
        if (slaveSyncThread != null) {
            slaveSyncThread.interrupt();
            slaveSyncThread = null;
        }
        slaveConnections.clear();
        log.info("HAService shutdown");
    }

    @Override
    public boolean isSlave() {
        return slave;
    }

    // ============== Master 模式 API ==============

    @Override
    public void notifyMessageArrived(long commitLogOffset, byte[] bodyBytes) {
        if (slave) return;
        if (bodyBytes == null || bodyBytes.length == 0) return;
        boolean ok = pushQueue.offer(new HAPushEntry(commitLogOffset, bodyBytes));
        if (!ok) {
            log.warn("HA pushQueue full, dropping message at offset={}", commitLogOffset);
        }
    }

    @Override
    public boolean waitForSlaveAck(long commitLogOffset, long timeoutMillis) {
        if (slave) {
            // Slave 模式不该调用此方法
            return true;
        }
        if (slaveConnections.isEmpty()) {
            // 没有 slave, 直接返回 true (避免单机部署卡住)
            return true;
        }
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < deadline) {
            if (slaveAckOffset.get() >= commitLogOffset) {
                return true;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }

    @Override
    public void reportSlaveOffset(long maxOffset) {
        if (slave) {
            localAckOffset.set(maxOffset);
            return;
        }
        // Master 模式: 通过 channel.remoteAddress() 找到 slave 连接 (在 HAProcessor 中调用)
        // 这里更新 localAckOffset 作为兜底
        slaveAckOffset.updateAndGet(prev -> Math.max(prev, maxOffset));
    }

    @Override
    public long getSlaveAckOffset() {
        if (slave) return localAckOffset.get();
        return slaveAckOffset.get();
    }

    @Override
    public int getConnectionCount() {
        if (slave) return 0;
        // 清理过期连接
        cleanupExpired();
        return slaveConnections.size();
    }

    /**
     * Master 视角: 注册或更新 slave 连接状态.
     * <p>
     * 由 HAProcessor (HA_REPORT_OFFSET) 调用.
     */
    public HAConnectionState registerOrUpdateSlave(String slaveAddr, long ackOffset) {
        HAConnectionState state = slaveConnections.computeIfAbsent(slaveAddr, HAConnectionState::new);
        state.updateAckOffset(ackOffset);
        // 更新 master 视角的最小 ack
        updateMinAck();
        return state;
    }

    /**
     * Master 推送循环: 从 pushQueue 取消息, 发送给所有活跃 Slave.
     */
    private void masterPushLoop() {
        while (running) {
            HAPushEntry entry;
            try {
                entry = pushQueue.poll(50, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            if (entry == null) continue;

            // 清理过期连接
            cleanupExpired();

            // 推送给所有 slave (简化: 直接通过 SlaveSynchronize 复用 NettyRemotingClient)
            // 实际 RocketMQ 是每个 HAConnection 一个 writer 线程
            for (Map.Entry<String, HAConnectionState> e : slaveConnections.entrySet()) {
                pushToSlave(e.getKey(), entry);
            }
        }
        log.info("masterPushLoop exit");
    }

    /**
     * 推送单条消息给指定 slave.
     * <p>
     * MVP: 通过 SlaveSynchronize 持有的 NettyRemotingClient 发送 HA_PUSH_COMMITLOG.
     */
    private void pushToSlave(String slaveAddr, HAPushEntry entry) {
        // MVP 简化: 不通过 Netty 推送, 让 Slave 主动拉取
        // 推送语义留给后续 PR 实现 (需要 slave 暴露 receive 端点)
        // 这里仅记录日志
        if (log.isDebugEnabled()) {
            log.debug("pushToSlave: slave={} offset={} (pending pull model)", slaveAddr, entry.offset);
        }
    }

    /**
     * 清理心跳超时的 slave 连接.
     */
    private void cleanupExpired() {
        Iterator<Map.Entry<String, HAConnectionState>> it = slaveConnections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, HAConnectionState> e = it.next();
            if (e.getValue().isExpired(SLAVE_HEARTBEAT_TIMEOUT_MS)) {
                log.warn("HA slave {} expired, removing", e.getKey());
                it.remove();
            }
        }
        updateMinAck();
    }

    /**
     * 更新 master 视角的 slave 同步进度 = 所有 slave 中最小的 ack offset.
     */
    private void updateMinAck() {
        if (slaveConnections.isEmpty()) {
            slaveAckOffset.set(0L);
            return;
        }
        long min = Long.MAX_VALUE;
        for (HAConnectionState s : slaveConnections.values()) {
            min = Math.min(min, s.getLastAckOffset());
        }
        if (min == Long.MAX_VALUE) min = 0L;
        slaveAckOffset.set(Math.max(0L, min));
    }

    // ============== Slave 模式 API ==============

    /**
     * Slave 同步循环: 周期性从 Master 拉取新消息, 写入本地 CommitLog.
     */
    private void slaveSyncLoop() {
        String masterAddr = brokerController.getBrokerConfig().getBrokerMasterAddr();
        if (masterAddr == null || masterAddr.isEmpty()) {
            log.error("Slave mode but brokerMasterAddr is empty, HA disabled");
            return;
        }
        NettyRemotingClient client = new NettyRemotingClient(new NettyClientConfig());
        try {
            client.start();
        } catch (Exception e) {
            log.error("HA client start failed", e);
            return;
        }
        long localOffset = 0L;
        log.info("Slave HA sync started, master={}", masterAddr);

        while (running) {
            try {
                Thread.sleep(200);  // 200ms 周期 poll
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            // 1) 周期性上报本地 offset
            reportOffsetToMaster(client, masterAddr, localOffset);

            // 2) 拉取 Master 新消息 (后续 PR 实现 HA_PULL_COMMITLOG RPC)
            // MVP 简化: 仅心跳上报, 不主动拉取增量
            // (通过 BrokerOutAPI / HAProcessor 扩展实现增量拉取)
            if (log.isTraceEnabled()) {
                log.trace("Slave HA loop: local offset={}", localOffset);
            }
        }
        try {
            client.shutdown();
        } catch (Exception ignore) {
            // ignore
        }
        log.info("Slave HA sync loop exit");
    }

    private void reportOffsetToMaster(NettyRemotingClient client, String masterAddr, long localOffset) {
        try {
            Channel ch = client.getOrCreateChannel(masterAddr);
            if (ch == null) return;
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.HA_REPORT_OFFSET);
            // 用 channel remote address 作为 slave 标识 (Master 用此去重/识别 slave)
            String slaveAddr = ch.remoteAddress() != null ? ch.remoteAddress().toString() : "slave-unknown";
            request.addExtField("slaveAddr", slaveAddr);
            request.addExtField("ackOffset", String.valueOf(localOffset));
            RemotingCommand response = client.invokeSync(ch, request, 3_000L);
            if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS) {
                log.warn("HA reportOffset failed: {}", response == null ? "null" : response.getCode());
            }
        } catch (Exception e) {
            log.warn("HA reportOffset error", e);
        }
    }

    /**
     * Slave 写入 Master 推送的消息 (由 HAClient 拉取后调用).
     * <p>
     * MVP: 本接口预留, 实际写入走 BrokerController 的 SendMessageProcessor 复用.
     */
    public void writeSlaveMessage(byte[] bodyBytes) {
        if (!slave) return;
        if (bodyBytes == null || bodyBytes.length == 0) return;
        // MVP: 实际写入逻辑由 HAClient 后续 PR 实现
        // 这里仅记录日志
        if (log.isDebugEnabled()) {
            log.debug("writeSlaveMessage: {} bytes", bodyBytes.length);
        }
    }

    // ============== 内部类 ==============

    /**
     * 待推送消息条目 (Master 内部).
     */
    private static final class HAPushEntry {
        final long offset;
        final byte[] body;

        HAPushEntry(long offset, byte[] body) {
            this.offset = offset;
            this.body = body;
        }
    }

    /**
     * ConsumerOffsetManager 访问 (用于 Slave 同步 ConsumerOffset 后更新).
     */
    public ConsumerOffsetManager getConsumerOffsetManager() {
        return brokerController.getConsumerOffsetManager();
    }

    /**
     * CommitLog 访问 (用于 Slave 写入消息).
     */
    public CommitLog getCommitLog() {
        return brokerController.getCommitLog();
    }

    /**
     * 获取所有 slave 连接 (测试用).
     */
    public Map<String, HAConnectionState> getSlaveConnections() {
        return slaveConnections;
    }
}
