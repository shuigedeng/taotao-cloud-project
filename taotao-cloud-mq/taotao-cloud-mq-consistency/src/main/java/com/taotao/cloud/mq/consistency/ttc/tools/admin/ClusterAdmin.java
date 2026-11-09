package com.taotao.cloud.mq.consistency.ttc.tools.admin;

import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.common.ha.TopicConfigSerializeWrapper;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import io.netty.channel.Channel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 集群管理 RPC 客户端（对标 mqadmin ClusterAdmin）.
 * <p>
 * 通过 Netty RPC 与 NameServer / Broker 通信，执行运维操作:
 * <ul>
 *   <li>listTopics — 查询所有 Topic</li>
 *   <li>topicRoute — 查询 Topic 路由</li>
 *   <li>listBrokers — 查询所有 Broker</li>
 *   <li>topicConfig — 查询 Broker 上的 Topic 配置</li>
 *   <li>createTopic — 创建 Topic</li>
 * </ul>
 */
public class ClusterAdmin {
    /** BrokerData 没带 cluster 字段时的分组名，避免出现 null 键。 */
    private static final String UNKNOWN_CLUSTER = "UNKNOWN";


    private static final Logger log = LoggerFactory.getLogger(ClusterAdmin.class);

    private final String namesrvAddr;
    private NettyRemotingClient namesrvClient;

    public ClusterAdmin(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public void start() {
        this.namesrvClient = new NettyRemotingClient(new NettyClientConfig());
        this.namesrvClient.start();
    }

    public void shutdown() {
        if (namesrvClient != null) {
            namesrvClient.shutdown();
        }
    }

    // ===== NameServer 操作 =====

    /**
     * 查询所有 Topic 列表.
     */
    public List<String> listTopics() {
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_TOPIC_LIST);
        RemotingCommand response = invokeNamesrv(request);
        if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS) {
            return Collections.emptyList();
        }
        if (response.getBody() == null || response.getBody().length == 0) {
            return Collections.emptyList();
        }
        try {
            TopicListResult result = JsonCodec.decode(response.getBody(), TopicListResult.class);
            return result != null && result.getTopics() != null ? result.getTopics() : Collections.emptyList();
        } catch (Exception e) {
            log.warn("listTopics decode failed", e);
            return Collections.emptyList();
        }
    }

    /**
     * 查询 Topic 路由.
     */
    public TopicRouteData topicRoute(String topic) {
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTEINFO_BY_TOPIC);
        request.addExtField("topic", topic);
        RemotingCommand response = invokeNamesrv(request);
        if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS) {
            return null;
        }
        if (response.getBody() == null || response.getBody().length == 0) {
            return null;
        }
        try {
            return JsonCodec.decode(response.getBody(), TopicRouteData.class);
        } catch (Exception e) {
            log.warn("topicRoute decode failed: {}", topic, e);
            return null;
        }
    }

    /**
     * 查询所有 Broker 集群信息，按 cluster 分组.
     * <p>
     * NameServer 实际回的 body 形状是 {@code ClusterInfo{brokerAddrTable, clusterAddrTable}}，
     * 即 brokerName → BrokerData，**不是** clusterName → List&lt;BrokerData&gt;。
     * 按后者去解会静默得到空 map（键名对不上，任何异常都不抛），调用方看到的就是
     * "集群里一个 broker 都没有"。
     */
    public Map<String, List<BrokerData>> listBrokers() {
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_BROKER_CLUSTER_INFO);
        RemotingCommand response = invokeNamesrv(request);
        if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS) {
            return Collections.emptyMap();
        }
        if (response.getBody() == null || response.getBody().length == 0) {
            return Collections.emptyMap();
        }
        try {
            ClusterInfoHolder info = JsonCodec.decode(response.getBody(), ClusterInfoHolder.class);
            if (info == null || info.getBrokerAddrTable() == null || info.getBrokerAddrTable().isEmpty()) {
                return Collections.emptyMap();
            }
            Map<String, List<BrokerData>> grouped = new LinkedHashMap<>();
            for (BrokerData data : info.getBrokerAddrTable().values()) {
                if (data == null) {
                    continue;
                }
                String cluster = data.getCluster() != null ? data.getCluster() : UNKNOWN_CLUSTER;
                List<BrokerData> bucket = grouped.get(cluster);
                if (bucket == null) {
                    bucket = new ArrayList<>();
                    grouped.put(cluster, bucket);
                }
                bucket.add(data);
            }
            return grouped;
        } catch (Exception e) {
            log.warn("listBrokers decode failed", e);
            return Collections.emptyMap();
        }
    }

    /**
     * 创建 Topic (通过 Broker 端口).
     */
    public boolean createTopic(String brokerAddr, String topic, int readQueueNums, int writeQueueNums) {
        NettyRemotingClient brokerClient = new NettyRemotingClient(new NettyClientConfig());
        brokerClient.start();
        try {
            Channel ch = brokerClient.getOrCreateChannel(brokerAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
            request.addExtField("topic", topic);
            request.addExtField("readQueueNums", String.valueOf(readQueueNums));
            request.addExtField("writeQueueNums", String.valueOf(writeQueueNums));
            RemotingCommand response = brokerClient.invokeSync(ch, request, 5000);
            return response != null && response.getCode() == RemotingSysResponseCode.SUCCESS;
        } catch (Exception e) {
            log.error("createTopic failed: topic={} broker={}", topic, brokerAddr, e);
            return false;
        } finally {
            brokerClient.shutdown();
        }
    }

    /**
     * 删除 Topic (通过 Broker 端口).
     * <p>
     * 只摘 broker 上的配置表，不动已落盘的消息 —— 存储层没有按 topic 物理清除的能力。
     */
    public boolean deleteTopic(String brokerAddr, String topic) {
        NettyRemotingClient brokerClient = new NettyRemotingClient(new NettyClientConfig());
        brokerClient.start();
        try {
            Channel ch = brokerClient.getOrCreateChannel(brokerAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.DELETE_TOPIC);
            request.addExtField("topic", topic);
            RemotingCommand response = brokerClient.invokeSync(ch, request, 5000);
            return response != null && response.getCode() == RemotingSysResponseCode.SUCCESS;
        } catch (Exception e) {
            log.error("deleteTopic failed: topic={} broker={}", topic, brokerAddr, e);
            return false;
        } finally {
            brokerClient.shutdown();
        }
    }

    /**
     * 按 topic/queue/offset 读取一段消息 (通过 Broker 端口).
     */
    public List<MessageExt> viewMessage(String brokerAddr, String topic, int queueId,
                                        long queueOffset, int maxNum) {
        NettyRemotingClient brokerClient = new NettyRemotingClient(new NettyClientConfig());
        brokerClient.start();
        try {
            Channel ch = brokerClient.getOrCreateChannel(brokerAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.VIEW_MESSAGE_BY_KEY);
            request.addExtField("topic", topic);
            request.addExtField("queueId", String.valueOf(queueId));
            request.addExtField("queueOffset", String.valueOf(queueOffset));
            request.addExtField("maxNum", String.valueOf(maxNum));
            RemotingCommand response = brokerClient.invokeSync(ch, request, 5000);
            if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS
                    || response.getBody() == null || response.getBody().length == 0) {
                return Collections.emptyList();
            }
            MessageViewResult result = JsonCodec.decode(response.getBody(), MessageViewResult.class);
            return result != null && result.getMessages() != null
                    ? result.getMessages() : Collections.emptyList();
        } catch (Exception e) {
            log.error("viewMessage failed: topic={} broker={}", topic, brokerAddr, e);
            return Collections.emptyList();
        } finally {
            brokerClient.shutdown();
        }
    }

    /**
     * 查询 broker 上的订阅组列表 (通过 Broker 端口).
     * <p>
     * 走 GET_ALL_SUBSCRIPTION_GROUP —— 该命令在 broker 侧当前返回空表（MVP 未实现订阅组存储），
     * 这里如实把空结果透传上去，不伪造条目。
     */
    public Map<String, Object> listConsumerGroups(String brokerAddr) {
        NettyRemotingClient brokerClient = new NettyRemotingClient(new NettyClientConfig());
        brokerClient.start();
        try {
            Channel ch = brokerClient.getOrCreateChannel(brokerAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_SUBSCRIPTION_GROUP);
            RemotingCommand response = brokerClient.invokeSync(ch, request, 5000);
            if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS
                    || response.getBody() == null || response.getBody().length == 0) {
                return Collections.emptyMap();
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> result = JsonCodec.decode(response.getBody(), Map.class);
            return result != null ? result : Collections.emptyMap();
        } catch (Exception e) {
            log.error("listConsumerGroups failed: broker={}", brokerAddr, e);
            return Collections.emptyMap();
        } finally {
            brokerClient.shutdown();
        }
    }

    /**
     * 查询 broker 上某个 topic 的配置 (通过 Broker 端口).
     * <p>
     * 走 {@code GET_ALL_TOPIC_CONFIG}（该命令返回全表）后在客户端取单条 ——
     * broker 侧没有单查命令，另开一条码只为省一次反序列化并不划算。
     */
    public TopicConfig topicConfig(String brokerAddr, String topic) {
        Map<String, TopicConfig> all = allTopicConfigs(brokerAddr);
        return all == null ? null : all.get(topic);
    }

    /** broker 上的全量 topic 配置 (通过 Broker 端口)。 */
    public Map<String, TopicConfig> allTopicConfigs(String brokerAddr) {
        NettyRemotingClient brokerClient = new NettyRemotingClient(new NettyClientConfig());
        brokerClient.start();
        try {
            Channel ch = brokerClient.getOrCreateChannel(brokerAddr);
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_TOPIC_CONFIG);
            RemotingCommand response = brokerClient.invokeSync(ch, request, 5000);
            if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS
                    || response.getBody() == null || response.getBody().length == 0) {
                return Collections.emptyMap();
            }
            TopicConfigSerializeWrapper wrapper =
                    JsonCodec.decode(response.getBody(), TopicConfigSerializeWrapper.class);
            if (wrapper == null || wrapper.getTopicConfigTable() == null) {
                return Collections.emptyMap();
            }
            return wrapper.getTopicConfigTable();
        } catch (Exception e) {
            log.error("allTopicConfigs failed: broker={}", brokerAddr, e);
            return Collections.emptyMap();
        } finally {
            brokerClient.shutdown();
        }
    }

    // ===== 内部方法 =====

    /**
     * NameServer 是否真的连得上.
     * <p>
     * 配了几个地址不等于有几个可用节点：listBrokers 连不上时同样返回空 map，
     * 调用方分不出"集群里没 broker"和"压根没连上 NameServer"。
     * 控制台要把这个差别如实显示出来，所以单独探一次。
     */
    public boolean namesrvReachable() {
        RemotingCommand response = invokeNamesrv(
                RemotingCommand.createRequestCommand(RequestCode.GET_BROKER_CLUSTER_INFO));
        return response != null && response.getCode() == RemotingSysResponseCode.SUCCESS;
    }

    private RemotingCommand invokeNamesrv(RemotingCommand request) {
        if (namesrvClient == null) {
            log.warn("namesrvClient not started");
            return null;
        }
        try {
            String first = namesrvAddr.split(";")[0].trim();
            Channel ch = namesrvClient.getOrCreateChannel(first);
            if (ch == null) {
                log.warn("cannot connect to namesrv: {}", first);
                return null;
            }
            return namesrvClient.invokeSync(ch, request, 5000);
        } catch (Exception e) {
            log.error("invokeNamesrv failed: code={}", request.getCode(), e);
            return null;
        }
    }

    /** Topic 列表结果 (用于 JSON 反序列化). */
    public static class TopicListResult {
        private List<String> topics;

        public List<String> getTopics() {
            return topics;
        }

        public void setTopics(List<String> topics) {
            this.topics = topics;
        }
    }

    /**
     * 码 4 的返回体，镜像 NameServer 的 ClusterInfo。
     * <p>
     * 这里不复用 nameserver 模块的类型：z-mq-tools 是客户端侧依赖，不该反向依赖服务端模块。
     */
    public static class ClusterInfoHolder {
        private Map<String, BrokerData> brokerAddrTable;
        private Map<String, List<String>> clusterAddrTable;

        public Map<String, BrokerData> getBrokerAddrTable() {
            return brokerAddrTable;
        }

        public void setBrokerAddrTable(Map<String, BrokerData> brokerAddrTable) {
            this.brokerAddrTable = brokerAddrTable;
        }

        public Map<String, List<String>> getClusterAddrTable() {
            return clusterAddrTable;
        }

        public void setClusterAddrTable(Map<String, List<String>> clusterAddrTable) {
            this.clusterAddrTable = clusterAddrTable;
        }
    }

    /** 消息查询结果 (与 broker 侧 AdminBrokerProcessor.MessageViewResult 同构). */
    public static class MessageViewResult {
        private List<MessageExt> messages;

        public List<MessageExt> getMessages() {
            return messages;
        }

        public void setMessages(List<MessageExt> messages) {
            this.messages = messages;
        }
    }
}
