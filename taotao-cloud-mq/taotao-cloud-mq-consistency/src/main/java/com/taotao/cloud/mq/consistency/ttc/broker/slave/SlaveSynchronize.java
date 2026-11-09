package com.taotao.cloud.mq.consistency.ttc.broker.slave;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.broker.processor.AdminBrokerProcessor;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.ha.ConsumerOffsetSerializeWrapper;
import com.taotao.cloud.mq.consistency.ttc.common.ha.DataVersion;
import com.taotao.cloud.mq.consistency.ttc.common.ha.TopicConfigSerializeWrapper;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.config.ConsumerOffsetManager;
import io.netty.channel.Channel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * * Slave 同步服务（对标 RocketMQ SlaveSynchronize）.
 * <p>
 * 当本 Broker 配置为 Slave (brokerId != 0) 时启动, 周期性从 Master 拉取元数据:
 * <ul>
 *   <li>TopicConfig — Topic 路由配置 (createTopic/updateTopic/deleteTopic)</li>
 *   <li>ConsumerOffset — Consumer 消费进度</li>
 *   <li>DelayOffset — 延迟消息投递进度</li>
 *   <li>SubscriptionGroup — 订阅关系 (MVP 暂未实现)</li>
 * </ul>
 *
 * <p>同步策略:
 * <ul>
 *   <li>每次调用 {@link #syncAll()} 全量同步 TopicConfig / ConsumerOffset / DelayOffset</li>
 *   <li>增量判断由 DataVersion 控制: Master 版本 > 本地版本时全量覆盖, 否则跳过</li>
 *   <li>同步失败不影响业务; 下次 syncAll 重试</li>
 * </ul>
 *
 * <p><b>线程安全:</b> 所有方法从 ConcurrentMap 读, 不阻塞业务线程.
 *
 * @see <a href="https://github.com/apache/rocketmq/blob/develop/broker/src/main/java/org/apache/rocketmq/broker/slave/SlaveSynchronize.java">RocketMQ SlaveSynchronize</a>
 */
public class SlaveSynchronize {

    private static final Logger log = LoggerFactory.getLogger(SlaveSynchronize.class);

    /** 单次同步超时 (毫秒). */
    private static final long SYNC_TIMEOUT_MS = 5_000L;

    private final BrokerController brokerController;
    private volatile String masterAddr;

    /**
     * 缓存 NettyRemotingClient, 避免每次 syncAll 都新建一个未启动的客户端 (导致连接失败).
     */
    private NettyRemotingClient nettyRemotingClient;

    /** 本地 TopicConfig 版本号 (Master 同步下来后比对). */
    private final DataVersion localTopicConfigVersion = new DataVersion();
    /** 本地 ConsumerOffset 版本号. */
    private final DataVersion localConsumerOffsetVersion = new DataVersion();
    /** 本地 DelayOffset 版本号. */
    private final DataVersion localDelayOffsetVersion = new DataVersion();

    public SlaveSynchronize(BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    public String getMasterAddr() {
        return masterAddr;
    }

    public void setMasterAddr(String masterAddr) {
        if (masterAddr == null || masterAddr.equals(this.masterAddr)) {
            return;
        }
        log.info("Update master address from {} to {}", this.masterAddr, masterAddr);
        this.masterAddr = masterAddr;
    }

    /**
     * 启动同步服务 (BrokerController.start() 时调用, 注册 Netty 客户端).
     */
    public void start() {
        try {
            this.nettyRemotingClient = new NettyRemotingClient(new NettyClientConfig());
            this.nettyRemotingClient.start();
            log.info("SlaveSynchronize NettyRemotingClient started");
        } catch (Exception e) {
            log.error("SlaveSynchronize start failed", e);
        }
    }

    /**
     * 关闭同步服务.
     */
    public void shutdown() {
        if (nettyRemotingClient != null) {
            try {
                nettyRemotingClient.shutdown();
            } catch (Exception e) {
                log.warn("shutdown NettyRemotingClient failed", e);
            }
            nettyRemotingClient = null;
        }
    }

    /**
     * 全量同步所有元数据 (TopicConfig + ConsumerOffset + DelayOffset).
     * <p>
     * 在 Slave 启动时调用一次, 之后可由 BrokerController 定时调用保持最新.
     */
    public void syncAll() {
        if (masterAddr == null || masterAddr.isEmpty()) {
            log.warn("slave master addr not configured, skip sync");
            return;
        }
        try {
            syncTopicConfig();
        } catch (Exception e) {
            log.warn("syncTopicConfig failed", e);
        }
        try {
            syncConsumerOffset();
        } catch (Exception e) {
            log.warn("syncConsumerOffset failed", e);
        }
        try {
            syncDelayOffset();
        } catch (Exception e) {
            log.warn("syncDelayOffset failed", e);
        }
    }

    private void syncTopicConfig() throws Exception {
        NettyRemotingClient client = ensureClient();
        Channel ch = client.getOrCreateChannel(masterAddr);
        if (ch == null) {
            log.warn("syncTopicConfig: no channel for master {}", masterAddr);
            return;
        }

        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_TOPIC_CONFIG);
        RemotingCommand response = client.invokeSync(ch, request, SYNC_TIMEOUT_MS);
        if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS) {
            log.warn("syncTopicConfig: bad response from master {}", masterAddr);
            return;
        }
        if (response.getBody() == null || response.getBody().length == 0) {
            return;
        }

        TopicConfigSerializeWrapper wrapper;
        try {
            wrapper = JsonCodec.decode(response.getBody(), TopicConfigSerializeWrapper.class);
        } catch (Exception e) {
            log.warn("syncTopicConfig: decode failed", e);
            return;
        }
        if (wrapper == null || wrapper.getDataVersion() == null) {
            return;
        }

        // 增量判断: 本地版本 >= Master 版本时跳过
        if (!wrapper.getDataVersion().isNewerThan(localTopicConfigVersion)) {
            log.debug("syncTopicConfig: local version is up-to-date, skip");
            return;
        }

        // 全量替换本地 topicConfigTable
        AdminBrokerProcessor admin = brokerController.getAdminBrokerProcessor();
        if (admin != null) {
            Map<String, TopicConfig> incoming = wrapper.getTopicConfigTable();
            admin.replaceAllTopicConfigs(incoming);
            log.info("syncTopicConfig: applied {} topic configs from master, new version={}",
                    incoming.size(), wrapper.getDataVersion());
        }
        localTopicConfigVersion.setTimestamp(wrapper.getDataVersion().getTimestamp());
        localTopicConfigVersion.getCounter().set(wrapper.getDataVersion().getCounterValue());
    }

    private void syncConsumerOffset() throws Exception {
        NettyRemotingClient client = ensureClient();
        Channel ch = client.getOrCreateChannel(masterAddr);
        if (ch == null) return;

        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_CONSUMER_OFFSET);
        RemotingCommand response = client.invokeSync(ch, request, SYNC_TIMEOUT_MS);
        if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS
                || response.getBody() == null || response.getBody().length == 0) {
            return;
        }
        ConsumerOffsetSerializeWrapper wrapper;
        try {
            wrapper = JsonCodec.decode(response.getBody(), ConsumerOffsetSerializeWrapper.class);
        } catch (Exception e) {
            log.warn("syncConsumerOffset: decode failed", e);
            return;
        }
        if (wrapper == null || wrapper.getDataVersion() == null
                || !wrapper.getDataVersion().isNewerThan(localConsumerOffsetVersion)) {
            return;
        }

        // 写入 ConsumerOffsetManager
        ConsumerOffsetManager mgr = brokerController.getConsumerOffsetManager();
        if (mgr != null) {
            // MVP 简化: Slave 上 consumer offset 与 Master 共享 (强一致)
            // 生产场景应区分: 消费进度从 Master 同步, 但 Slave 不允许 commit
            Map<String, Map<Integer, Long>> offsetTable = wrapper.getOffsetTable();
            for (Map.Entry<String, Map<Integer, Long>> topicEntry : offsetTable.entrySet()) {
                String topic = topicEntry.getKey();
                for (Map.Entry<Integer, Long> queueEntry : topicEntry.getValue().entrySet()) {
                    int queueId = queueEntry.getKey();
                    long offset = queueEntry.getValue();
                    // 使用 __SYNC_GROUP__ 标记来源是同步
                    mgr.commitOffset(topic, queueId, "__SYNC_GROUP__", offset);
                }
            }
            log.info("syncConsumerOffset: applied {} topic-queue offsets, version={}",
                    offsetTable.size(), wrapper.getDataVersion());
        }
        localConsumerOffsetVersion.setTimestamp(wrapper.getDataVersion().getTimestamp());
        localConsumerOffsetVersion.getCounter().set(wrapper.getDataVersion().getCounterValue());
    }

    private void syncDelayOffset() throws Exception {
        NettyRemotingClient client = ensureClient();
        Channel ch = client.getOrCreateChannel(masterAddr);
        if (ch == null) return;

        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_DELAY_OFFSET);
        RemotingCommand response = client.invokeSync(ch, request, SYNC_TIMEOUT_MS);
        if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS
                || response.getBody() == null || response.getBody().length == 0) {
            return;
        }
        ConsumerOffsetSerializeWrapper wrapper;
        try {
            wrapper = JsonCodec.decode(response.getBody(), ConsumerOffsetSerializeWrapper.class);
        } catch (Exception e) {
            log.warn("syncDelayOffset: decode failed", e);
            return;
        }
        if (wrapper == null || wrapper.getDataVersion() == null
                || !wrapper.getDataVersion().isNewerThan(localDelayOffsetVersion)) {
            return;
        }

        // MVP: 延迟 offset 与 consumer offset 共享存储, 仅记录版本号
        log.info("syncDelayOffset: version={}", wrapper.getDataVersion());
        localDelayOffsetVersion.setTimestamp(wrapper.getDataVersion().getTimestamp());
        localDelayOffsetVersion.getCounter().set(wrapper.getDataVersion().getCounterValue());
    }

    private NettyRemotingClient ensureClient() {
        // 使用缓存的 client (启动时已 start), 避免每次新建未初始化的客户端
        if (nettyRemotingClient == null) {
            log.warn("SlaveSynchronize NettyRemotingClient not started");
            return null;
        }
        return nettyRemotingClient;
    }

    // ============== 测试/监控 ==============

    public DataVersion getLocalTopicConfigVersion() {
        return localTopicConfigVersion;
    }

    public DataVersion getLocalConsumerOffsetVersion() {
        return localConsumerOffsetVersion;
    }

    public DataVersion getLocalDelayOffsetVersion() {
        return localDelayOffsetVersion;
    }
}
