package com.taotao.cloud.mq.consistency.ttc.broker.outapi;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.broker.processor.AdminBrokerProcessor;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.ha.ConsumerOffsetSerializeWrapper;
import com.taotao.cloud.mq.consistency.ttc.common.ha.DataVersion;
import com.taotao.cloud.mq.consistency.ttc.common.ha.TopicConfigSerializeWrapper;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.config.ConsumerOffsetManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Broker OutAPI 处理器（对标 RocketMQ BrokerOutAPI）.
 * <p>
 * Master Broker 对外暴露的"Broker 之间 RPC 接口", Slave 启动时通过这些接口拉取元数据.
 * <p>
 * 主要接口:
 * <ul>
 *   <li>{@code GET_ALL_TOPIC_CONFIG}: 返回所有 Topic 配置 (含版本号)</li>
 *   <li>{@code GET_ALL_CONSUMER_OFFSET}: 返回所有 ConsumerOffset (含版本号)</li>
 *   <li>{@code GET_ALL_DELAY_OFFSET}: 返回所有延迟消息 offset (简化: 由 Topic 自身 offset 推导)</li>
 *   <li>{@code GET_ALL_SUBSCRIPTION_GROUP}: 返回订阅组配置 (MVP 暂返回空)</li>
 *   <li>{@code QUERY_DATA_VERSION}: 返回 Master 数据版本号 (用于增量同步判断)</li>
 * </ul>
 *
 * <p><b>线程安全:</b> 所有方法从 topicConfigTable / offsetTable 读, 这些是 ConcurrentMap.
 *
 * @see com.taotao.cloud.mq.consistency.ttc.broker.slave.SlaveSynchronize
 * @see <a href="https://github.com/apache/rocketmq/blob/develop/broker/src/main/java/org/apache/rocketmq/broker/processor/BrokerSendProcessor.java">RocketMQ BrokerSendProcessor (BrokerOutAPI 实现)</a>
 */
public class BrokerOutAPI implements NettyRemotingAbstract.NettyRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(BrokerOutAPI.class);

    private final BrokerController brokerController;

    public BrokerOutAPI(BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        int code = request.getCode();
        switch (code) {
            case RequestCode.GET_ALL_TOPIC_CONFIG:
                return handleGetAllTopicConfig(request);
            case RequestCode.GET_ALL_CONSUMER_OFFSET:
                return handleGetAllConsumerOffset(request);
            case RequestCode.GET_ALL_DELAY_OFFSET:
                return handleGetAllDelayOffset(request);
            case RequestCode.GET_ALL_SUBSCRIPTION_GROUP:
                return handleGetAllSubscriptionGroup(request);
            case RequestCode.QUERY_DATA_VERSION:
                return handleQueryDataVersion(request);
            default:
                RemotingCommand resp = RemotingCommand.createResponseCommand(
                        RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED);
                resp.setOpaque(request.getOpaque());
                resp.setRemark("BrokerOutAPI code " + code + " not supported");
                return resp;
        }
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

    // ============== 处理方法 ==============

    private RemotingCommand handleGetAllTopicConfig(RemotingCommand request) {
        TopicConfigSerializeWrapper wrapper = new TopicConfigSerializeWrapper();
        AdminBrokerProcessor admin = brokerController.getAdminBrokerProcessor();
        if (admin != null) {
            Map<String, TopicConfig> all = admin.getAllTopicConfigs();
            wrapper.setTopicConfigTable(new java.util.concurrent.ConcurrentHashMap<>(all));
            wrapper.setDataVersion(admin.getTopicConfigDataVersion());
        }
        return success(request, wrapper);
    }

    private RemotingCommand handleGetAllConsumerOffset(RemotingCommand request) {
        ConsumerOffsetSerializeWrapper wrapper = new ConsumerOffsetSerializeWrapper();
        ConsumerOffsetManager mgr = brokerController.getConsumerOffsetManager();
        if (mgr != null) {
            wrapper.setOffsetTable(collectOffsetTable(mgr));
            wrapper.setDataVersion(brokerController.getConsumerOffsetDataVersion());
        }
        return success(request, wrapper);
    }

    /**
     * MVP: 延迟消息 offset 与 Topic 自身 offset 共享 (无 SCHEDULE_TOPIC_XXXX 隔离队列).
     * 返回与 GET_ALL_CONSUMER_OFFSET 一致即可.
     */
    private RemotingCommand handleGetAllDelayOffset(RemotingCommand request) {
        ConsumerOffsetSerializeWrapper wrapper = new ConsumerOffsetSerializeWrapper();
        ConsumerOffsetManager mgr = brokerController.getConsumerOffsetManager();
        if (mgr != null) {
            wrapper.setOffsetTable(collectOffsetTable(mgr));
            wrapper.setDataVersion(brokerController.getDelayOffsetDataVersion());
        }
        return success(request, wrapper);
    }

    /**
     * MVP: 订阅组配置暂未单独存储, 返回空 wrapper + 默认版本号.
     */
    private RemotingCommand handleGetAllSubscriptionGroup(RemotingCommand request) {
        // 直接返回空 wrapper; 真实实现应返回 SubscriptionGroupWrapper
        return success(request, new com.taotao.cloud.mq.consistency.ttc.common.ha.ConsumerOffsetSerializeWrapper());
    }

    private RemotingCommand handleQueryDataVersion(RemotingCommand request) {
        Map<String, DataVersion> versions = new HashMap<>();
        AdminBrokerProcessor admin = brokerController.getAdminBrokerProcessor();
        if (admin != null) {
            versions.put("topicConfig", admin.getTopicConfigDataVersion());
        }
        versions.put("consumerOffset", brokerController.getConsumerOffsetDataVersion());
        versions.put("delayOffset", brokerController.getDelayOffsetDataVersion());
        return success(request, versions);
    }

    // ============== 工具 ==============

    private RemotingCommand success(RemotingCommand request, Object body) {
        RemotingCommand resp = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        resp.setOpaque(request.getOpaque());
        if (body != null) {
            try {
                resp.setBody(JsonCodec.encode(body));
            } catch (Exception e) {
                log.error("encode outapi response failed", e);
                resp.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
                resp.setRemark("encode failed: " + e.getMessage());
            }
        }
        return resp;
    }

    /**
     * 从 ConsumerOffsetManager 的内存表聚合成 (topic -> queueId -> maxOffset).
     */
    private Map<String, Map<Integer, Long>> collectOffsetTable(ConsumerOffsetManager mgr) {
        Map<String, Map<Integer, Long>> result = new HashMap<>();
        for (String topic : mgr.getAllKnownTopics()) {
            Map<Integer, Long> perQueue = new HashMap<>();
            // 简化: 遍历可能的 queueId (MVP 用 0-15)
            for (int q = 0; q < 16; q++) {
                Map<String, Long> perGroup = mgr.queryOffsetByGroupAndQueue(topic, q);
                if (!perGroup.isEmpty()) {
                    long max = perGroup.values().stream().mapToLong(Long::longValue).max().orElse(0L);
                    perQueue.put(q, max);
                }
            }
            if (!perQueue.isEmpty()) {
                result.put(topic, perQueue);
            }
        }
        return result;
    }
}
