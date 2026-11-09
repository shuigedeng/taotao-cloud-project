package com.taotao.cloud.mq.consistency.ttc.broker.processor;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.common.QueueData;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.ha.DataVersion;
import com.taotao.cloud.mq.consistency.ttc.store.config.TopicConfigManager;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Broker 端"管理"处理器（对标 RocketMQ AdminBrokerProcessor）.
 * <p>
 * 支持：
 * <ul>
 *   <li>CREATE_TOPIC — 创建 Topic（默认 4 读 4 写队列）</li>
 *   <li>VIEW_TOPIC — 查看 Topic 配置</li>
 *   <li>GET_ALL_TOPIC_CONFIG — 全量导出 (供 SlaveSynchronize 同步)</li>
 * </ul>
 */
public class AdminBrokerProcessor implements NettyRemotingAbstract.NettyRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(AdminBrokerProcessor.class);

    /**
     * Topic 配置的唯一持有者（内存表 + 它的盘上镜像都在里面）.
     * <p>
     * 本类不再自己持有 map：表与盘只能有一个持有者，否则"内存改了、文件没改"这种分叉就没有
     * 任何东西能挡住。正常路径上这个实例由 {@code BrokerController.initialize()} 建好并 start()
     * （即已经把盘上那份读回来）；只有绕过 initialize 直接构造本类时（单元测试）才现场建一个，
     * 落盘位置沿用同一份 {@code MessageStoreConfig} 的 rootDir。
     */
    private final TopicConfigManager topicConfigManager;

    /** TopicConfig 版本号 (每次 CREATE/UPDATE/DELETE 自增, 用于 Slave 增量同步). */
    private final DataVersion topicConfigDataVersion = new DataVersion();

    private final BrokerController brokerController;

    public AdminBrokerProcessor(BrokerController brokerController) {
        this.brokerController = brokerController;
        TopicConfigManager manager = brokerController.getTopicConfigManager();
        if (manager == null) {
            manager = new TopicConfigManager(
                    brokerController.getMessageStoreConfig().getStorePathRootDir());
            manager.start();
        }
        this.topicConfigManager = manager;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        int code = request.getCode();
        switch (code) {
            case RequestCode.CREATE_TOPIC:
            case RequestCode.UPDATE_AND_CREATE_TOPIC:
                return handleCreateTopic(request);
            case RequestCode.GET_ALL_TOPIC_LIST:
                return handleGetAllTopics(request);
            case RequestCode.DELETE_TOPIC:
                return handleDeleteTopic(request);
            case RequestCode.VIEW_MESSAGE_BY_KEY:
                return handleViewMessage(request);
            default:
                RemotingCommand response = RemotingCommand.createResponseCommand(
                        RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED);
                response.setOpaque(request.getOpaque());
                response.setRemark("admin code " + code + " not supported");
                return response;
        }
    }

    private RemotingCommand handleCreateTopic(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String topic = request.getExtField("topic");
        String readNStr = request.getExtField("readQueueNums");
        String writeNStr = request.getExtField("writeQueueNums");
        if (topic == null || topic.isEmpty()) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic is empty");
            return response;
        }

        int readN = readNStr == null ? TopicConfig.DEFAULT_READ_QUEUE_NUMS : Integer.parseInt(readNStr);
        int writeN = writeNStr == null ? TopicConfig.DEFAULT_WRITE_QUEUE_NUMS : Integer.parseInt(writeNStr);
        TopicConfig config = new TopicConfig(topic, readN, writeN, TopicConfig.PERM_READ_WRITE);
        topicConfigManager.putTopicConfig(config);
        topicConfigDataVersion.assignNewVersion();
        log.info("Topic created: {} read={} write={}", topic, readN, writeN);

        CreateTopicResult body = new CreateTopicResult();
        body.setTopic(topic);
        body.setQueueDataList(toQueueData(config));
        response.setBody(com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec.encode(body));
        return response;
    }

    private RemotingCommand handleDeleteTopic(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String topic = request.getExtField("topic");
        if (topic == null || topic.isEmpty()) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic is empty");
            return response;
        }

        // 只摘配置表，不动 CommitLog：存储层没有按 topic 物理清除的能力
        TopicConfig removed = topicConfigManager.deleteTopicConfig(topic);
        if (removed == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic not found: " + topic);
            return response;
        }
        topicConfigDataVersion.assignNewVersion();
        log.info("Topic deleted: {}", topic);
        return response;
    }

    private RemotingCommand handleViewMessage(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String topic = request.getExtField("topic");
        String queueIdStr = request.getExtField("queueId");
        String offsetStr = request.getExtField("queueOffset");
        String maxNumStr = request.getExtField("maxNum");
        if (topic == null || topic.isEmpty() || queueIdStr == null || offsetStr == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic/queueId/queueOffset required");
            return response;
        }

        int queueId;
        long queueOffset;
        int maxNum;
        try {
            queueId = Integer.parseInt(queueIdStr);
            queueOffset = Long.parseLong(offsetStr);
            maxNum = maxNumStr == null || maxNumStr.isEmpty() ? 32 : Integer.parseInt(maxNumStr);
        } catch (NumberFormatException e) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("queueId/queueOffset/maxNum must be numeric");
            return response;
        }

        com.taotao.cloud.mq.consistency.ttc.store.log.CommitLog commitLog = brokerController.getCommitLog();
        if (commitLog == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("commit log not available");
            return response;
        }

        List<com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt> messages =
                commitLog.pullMessage(topic, queueId, queueOffset, maxNum);

        MessageViewResult body = new MessageViewResult();
        body.setMessages(messages);
        response.setBody(com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec.encode(body));
        return response;
    }

    private RemotingCommand handleGetAllTopics(RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        TopicListResult body = new TopicListResult();
        body.setTopics(new ArrayList<>(topicConfigManager.getAllTopicConfigs().keySet()));
        response.setBody(com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec.encode(body));
        return response;
    }

    private List<QueueData> toQueueData(TopicConfig config) {
        List<QueueData> list = new ArrayList<>();
        QueueData qd = new QueueData();
        qd.setBrokerName(brokerController.getBrokerConfig().getBrokerName());
        qd.setReadQueueNums(config.getReadQueueNums());
        qd.setWriteQueueNums(config.getWriteQueueNums());
        qd.setPerm(config.getPerm());
        list.add(qd);
        return list;
    }

    public TopicConfig getTopicConfig(String topic) {
        return topicConfigManager.selectTopicConfig(topic);
    }

    /**
     * 全量 TopicConfig 快照 (供 BrokerOutAPI / SlaveSynchronize).
     */
    public Map<String, TopicConfig> getAllTopicConfigs() {
        return topicConfigManager.getAllTopicConfigs();
    }

    /**
     * 批量替换 TopicConfigTable 内容 (供 SlaveSynchronize 使用).
     * <p>
     * 用 incoming 完全替换本地表 (包括删除 incoming 中没有的条目).
     */
    public void replaceAllTopicConfigs(Map<String, TopicConfig> incoming) {
        topicConfigManager.replaceAllTopicConfigs(incoming);
        topicConfigDataVersion.assignNewVersion();
    }

    /**
     * topic 配置的后端（内存表 + 盘上镜像）, 供装配与关单时强制落盘.
     */
    public TopicConfigManager getTopicConfigManager() {
        return topicConfigManager;
    }

    /**
     * TopicConfig 版本号 (每次 CREATE/UPDATE/DELETE 自增).
     */
    public DataVersion getTopicConfigDataVersion() {
        return topicConfigDataVersion;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

    public static class CreateTopicResult implements Serializable {
        private static final long serialVersionUID = 1L;
        private String topic;
        private List<QueueData> queueDataList;

        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public List<QueueData> getQueueDataList() { return queueDataList; }
        public void setQueueDataList(List<QueueData> queueDataList) { this.queueDataList = queueDataList; }
    }

    public static class TopicListResult implements Serializable {
        private static final long serialVersionUID = 1L;
        private List<String> topics = new ArrayList<>();

        public List<String> getTopics() { return topics; }
        public void setTopics(List<String> topics) { this.topics = topics; }
    }

    public static class MessageViewResult implements Serializable {
        private static final long serialVersionUID = 1L;
        private List<com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt> messages = new ArrayList<>();

        public List<com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt> getMessages() { return messages; }
        public void setMessages(List<com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt> messages) {
            this.messages = messages;
        }
    }
}
