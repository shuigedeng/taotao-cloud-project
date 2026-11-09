package com.taotao.cloud.mq.consistency.ttc.nameserver.processor;

import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.QueueData;
import com.taotao.cloud.mq.consistency.ttc.common.RegisterBrokerResult;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerController;
import com.taotao.cloud.mq.consistency.ttc.remoting.common.RemotingHelper;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * NameServer 默认请求处理器（对标 RocketMQ DefaultRequestProcessor）.
 * <p>
 * 负责路由 / 注册 / KV 等所有 NameServer 侧协议命令的统一处理。
 */
public class DefaultRequestProcessor implements NettyRemotingAbstract.NettyRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(DefaultRequestProcessor.class);

    private final NameServerController nameServerController;

    public DefaultRequestProcessor(NameServerController nameServerController) {
        this.nameServerController = nameServerController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        if (ctx != null) {
            log.debug("recv request: {} from {}", request.getCode(), RemotingHelper.parseChannelRemoteAddr(ctx.channel()));
        }
        switch (request.getCode()) {
            case RequestCode.REGISTER_BROKER:
                return handleRegisterBroker(ctx, request);
            case RequestCode.UNREGISTER_BROKER:
                return handleUnregisterBroker(request);
            case RequestCode.GET_ROUTEINFO_BY_TOPIC:
                return handleGetRouteInfoByTopic(request);
            case RequestCode.GET_ROUTE_BY_TOPIC:
                return handleGetRouteInfoByTopic(request);
            case RequestCode.GET_BROKER_CLUSTER_INFO:
                return handleGetBrokerClusterInfo(request);
            case RequestCode.GET_ALL_TOPIC_LIST:
                return handleGetAllTopicList(request);
            case RequestCode.UPDATE_AND_CREATE_TOPIC:
                return handleUpdateAndCreateTopic(request);
            default:
                RemotingCommand response = RemotingCommand.createResponseCommand(
                        RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED);
                response.setOpaque(request.getOpaque());
                response.setRemark("code " + request.getCode() + " not supported");
                return response;
        }
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

    /**
     * Broker 注册。
     */
    private RemotingCommand handleRegisterBroker(ChannelHandlerContext ctx, RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String clusterName = request.getExtField("clusterName");
        String brokerAddr = request.getExtField("brokerAddr");
        String brokerName = request.getExtField("brokerName");
        String brokerIdStr = request.getExtField("brokerId");
        String haServerAddr = request.getExtField("haServerAddr");
        if (clusterName == null || brokerAddr == null || brokerName == null || brokerIdStr == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("clusterName/brokerAddr/brokerName/brokerId required");
            return response;
        }

        long brokerId = Long.parseLong(brokerIdStr);

        // 解析 TopicConfig 列表 (Body 为 List<TopicConfig> JSON)
        List<TopicConfig> topicConfigList = null;
        if (request.getBody() != null && request.getBody().length > 0) {
            try {
                topicConfigList = JsonCodec.decodeList(request.getBody(), TopicConfig.class);
            } catch (Exception e) {
                log.warn("decode topicConfigList failed", e);
            }
        }

        RegisterBrokerResult result = nameServerController.getRouteInfoManager().registerBroker(
                clusterName, brokerAddr, brokerName, brokerId, haServerAddr,
                ctx == null ? null : ctx.channel());

        // 把 TopicConfig 注册进 topicQueueTable。注册报文是全量快照，所以先清掉这个 broker
        // 名下的旧条目 —— 否则它删掉的 topic 会永远留在路由表里（控制台里是一条 0 队列的幽灵）
        nameServerController.getRouteInfoManager().unregisterTopicsOfBroker(brokerName);
        if (topicConfigList != null) {
            for (TopicConfig tc : topicConfigList) {
                nameServerController.getRouteInfoManager().registerTopic(brokerName, tc);
            }
        }

        response.setBody(JsonCodec.encode(result));
        log.info("Broker registered: cluster={} brokerName={} brokerAddr={} brokerId={}",
                clusterName, brokerName, brokerAddr, brokerId);
        return response;
    }

    /**
     * Broker 注销。
     */
    private RemotingCommand handleUnregisterBroker(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String clusterName = request.getExtField("clusterName");
        String brokerAddr = request.getExtField("brokerAddr");
        String brokerName = request.getExtField("brokerName");
        String brokerIdStr = request.getExtField("brokerId");
        if (clusterName == null || brokerAddr == null || brokerName == null || brokerIdStr == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("clusterName/brokerAddr/brokerName/brokerId required");
            return response;
        }

        nameServerController.getRouteInfoManager().unregisterBroker(
                clusterName, brokerAddr, brokerName, Long.parseLong(brokerIdStr));
        return response;
    }

    /**
     * 查询某 Topic 的路由。
     */
    private RemotingCommand handleGetRouteInfoByTopic(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String topic = request.getExtField("topic");
        if (topic == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic is required");
            return response;
        }
        TopicRouteData data = nameServerController.getRouteInfoManager().pickupTopicRouteData(topic);
        if (data == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic " + topic + " not exist");
            return response;
        }
        response.setBody(JsonCodec.encode(data));
        return response;
    }

    /**
     * 查询集群所有 Broker 信息。
     */
    private RemotingCommand handleGetBrokerClusterInfo(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        ClusterInfo info = new ClusterInfo();
        info.setBrokerAddrTable(new java.util.HashMap<>(
                nameServerController.getRouteInfoManager().getBrokerAddrTable()));
        info.setClusterAddrTable(new java.util.HashMap<>(
                nameServerController.getRouteInfoManager().getClusterAddrTable()));
        response.setBody(JsonCodec.encode(info));
        return response;
    }

    /**
     * 查询所有 Topic 列表。
     */
    private RemotingCommand handleGetAllTopicList(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        TopicList body = new TopicList();
        body.setTopics(new ArrayList<>(nameServerController.getRouteInfoManager().getTopicQueueTable().keySet()));
        response.setBody(JsonCodec.encode(body));
        return response;
    }

    /**
     * 处理 UPDATE_AND_CREATE_TOPIC — 在 NameServer 侧建立 topicQueueTable 中的 Topic 入口.
     * <p>
     * 这里不分配 QueueData 的 brokerName (集群模式下需要再向所有 Broker 同步), MVP 简化
     * 为 "topicQueueTable 收到 topic → 所有已注册 broker 都覆盖一份 QueueData"。
     */
    private RemotingCommand handleUpdateAndCreateTopic(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String topic = request.getExtField("topic");
        String readNStr = request.getExtField("readQueueNums");
        String writeNStr = request.getExtField("writeQueueNums");
        if (topic == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic is required");
            return response;
        }
        int readN = readNStr == null ? 4 : Integer.parseInt(readNStr);
        int writeN = writeNStr == null ? 4 : Integer.parseInt(writeNStr);
        TopicConfig config = new TopicConfig(topic, readN, writeN, TopicConfig.PERM_READ_WRITE);

        // 把 Topic 关联到所有已注册 broker（简化: 同集群全共享）
        java.util.Collection<BrokerData> brokers = nameServerController.getRouteInfoManager().getBrokerAddrTable().values();
        if (brokers.isEmpty()) {
            // NameServer 尚未收到 Broker 注册, 仍然记录 topic 但不绑 broker
            log.warn("UPDATE_AND_CREATE_TOPIC {} but no broker registered yet", topic);
        }
        for (BrokerData bd : brokers) {
            nameServerController.getRouteInfoManager().registerTopic(bd.getBrokerName(), config);
        }
        log.info("UPDATE_AND_CREATE_TOPIC: topic={} readN={} writeN={} brokers={}", topic, readN, writeN, brokers.size());
        return response;
    }

    /**
     * 集群信息返回体。
     */
    public static class ClusterInfo implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private java.util.HashMap<String, BrokerData> brokerAddrTable;
        private java.util.HashMap<String, java.util.Set<String>> clusterAddrTable;

        public java.util.HashMap<String, BrokerData> getBrokerAddrTable() { return brokerAddrTable; }
        public void setBrokerAddrTable(java.util.HashMap<String, BrokerData> brokerAddrTable) { this.brokerAddrTable = brokerAddrTable; }
        public java.util.HashMap<String, java.util.Set<String>> getClusterAddrTable() { return clusterAddrTable; }
        public void setClusterAddrTable(java.util.HashMap<String, java.util.Set<String>> clusterAddrTable) { this.clusterAddrTable = clusterAddrTable; }
    }

    public static class TopicList implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private List<String> topics;

        public List<String> getTopics() { return topics; }
        public void setTopics(List<String> topics) { this.topics = topics; }
    }
}
