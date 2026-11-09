package com.taotao.cloud.mq.consistency.ttc.broker.processor;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendStatus;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.AppendMessageResult;
import com.taotao.cloud.mq.consistency.ttc.store.MessageExtBrokerInner;
import com.taotao.cloud.mq.consistency.ttc.store.PutMessageResult;
import com.taotao.cloud.mq.consistency.ttc.store.PutMessageStatus;
import com.taotao.cloud.mq.consistency.ttc.store.log.CommitLog;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;

/**
 * Broker 端"发送消息"处理器（对标 RocketMQ SendMessageProcessor）.
 * <p>
 * 接收 SEND_MESSAGE 请求 → 解码 MessageExt → 写入 CommitLog → 返回 SendResult。
 */
public class SendMessageProcessor implements NettyRemotingAbstract.NettyRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(SendMessageProcessor.class);

    private final BrokerController brokerController;

    public SendMessageProcessor(BrokerController brokerController) {
        this.brokerController = brokerController;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        TopicLogHelper.incSendCount();
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        // 处理 oneway
        if (request.isOnewayRPC()) {
            log.debug("sendMessage oneway received, ignore response");
            return null;
        }

        String topic = request.getExtField("topic");
        String queueIdStr = request.getExtField("queueId");
        String brokerName = request.getExtField("brokerName");
        if (topic == null || queueIdStr == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic or queueId missing");
            return response;
        }

        int queueId = Integer.parseInt(queueIdStr);
        MessageExt inbound = decodeMessage(request);
        if (inbound == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("decode body failed");
            return response;
        }

        CommitLog commitLog = brokerController.getCommitLog();
        if (commitLog == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("commitLog not initialized");
            return response;
        }

        // 构造 MessageExtBrokerInner
        MessageExtBrokerInner inner = new MessageExtBrokerInner();
        copyMessageExtFields(inbound, inner);
        inner.setQueueId(queueId);

        PutMessageResult result = commitLog.putMessage(inner);
        // 长轮询到达侧唤醒: 消息一旦真进了存储 (可被 pull 读到), 就把 (topic, queueId) 报给
        // PullRequestHoldService, 让挂在该队列上的 pull 立刻重读。
        //
        // 为什么落在这里而不是 CommitLog 的 haCallback 上: 那个钩子只给 commitLog 的物理位点和
        // body 字节, 而 hold 表按 topic@queueId 分组 (PullRequestHoldService#key), 从 body 反解
        // topic/queueId 既脆弱又要在测试里额外证明"解错队列会被发现"。本方法手里就有确定的
        // topic + queueId (queueId 是 inner.setQueueId(...) 之后 CommitLog 用来建索引的那一个),
        // 唤醒点落在知道 topic/queueId 的那一层才是对的。
        //
        // FLUSH_DISK_TIMEOUT 也要唤醒: 那条分支下记录已经 append 进存储 (后续 force/关机刷盘会落住),
        // pull 已经能读到它 —— 不唤醒就等于"消息到了但没人被叫醒"。
        if (result.isOk() || isAppendedToStore(result)) {
            notifyLongPollingArrived(topic, queueId);
        }
        // 构造 SendResult
        SendResult sendResult = new SendResult();
        AppendMessageResult amr = result.getAppendMessageResult();
        if (result.isOk()) {
            sendResult.setSendStatus(SendStatus.SEND_OK);
            if (amr != null) {
                sendResult.setMsgId(inner.getMsgId());
                sendResult.setTopic(topic);
                sendResult.setQueueId(queueId);
                // amr.getWroteOffset() 是 commitLog 的物理位点；队列位点由 CommitLog 分配后写回 inner
                sendResult.setQueueOffset(inner.getQueueOffset());
            }
        } else if (result.getPutMessageStatus() == PutMessageStatus.FLUSH_DISK_TIMEOUT
                && amr != null && amr.getStatus() == AppendMessageResult.AppendMessageStatus.PUT_OK) {
            // 同步刷盘超时会带合法的 AppendMessageResult：记录已经进存储（后续 force / 关机刷盘会落住），
            // 但"已落盘"没有兑现。这里必须把 FLUSH_DISK_TIMEOUT 原样塞进响应体的 SendResult ——
            // 客户端 toSendResult() 在 code==SUCCESS 时直接采用响应体里的 SendStatus，
            // 所以这条状态码就这样传到生产者（工单 T2：不许"广告了但没接线"）。
            sendResult.setSendStatus(SendStatus.FLUSH_DISK_TIMEOUT);
            sendResult.setMsgId(inner.getMsgId());
            sendResult.setTopic(topic);
            sendResult.setQueueId(queueId);
            sendResult.setQueueOffset(inner.getQueueOffset());
            sendResult.setErrorMsg(PutMessageStatus.FLUSH_DISK_TIMEOUT.name());
            log.error("sync flush timeout, respond FLUSH_DISK_TIMEOUT to producer, topic={} queueId={} msgId={}",
                    topic, queueId, inner.getMsgId());
        } else {
            sendResult.setSendStatus(SendStatus.SEND_FAILED);
            sendResult.setErrorMsg(result.getPutMessageStatus().name());
        }
        // 响应码
        response.setCode(RemotingSysResponseCode.SUCCESS);
        response.setBody(JsonCodec.encode(sendResult));
        TopicLogHelper.incSendSuccess(result.isOk());
        return response;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

    /**
     * 记录是否已经 append 进 CommitLog (与"同步刷盘是否超时"是两件事).
     * PUT_OK 之后 pull 就能从存储读到这条消息。
     */
    private static boolean isAppendedToStore(PutMessageResult result) {
        AppendMessageResult amr = result.getAppendMessageResult();
        return amr != null && amr.getStatus() == AppendMessageResult.AppendMessageStatus.PUT_OK;
    }

    /**
     * 把"该队列有新消息"报给长轮询服务; 服务没装配时安静跳过 (不影响发送响应本身).
     */
    private void notifyLongPollingArrived(String topic, int queueId) {
        com.taotao.cloud.mq.consistency.ttc.broker.longpoll.PullRequestHoldService holdService =
                brokerController.getPullRequestHoldService();
        if (holdService != null) {
            holdService.notifyMessageArrived(topic, queueId);
        }
    }

    private MessageExt decodeMessage(RemotingCommand request) {
        if (request.getBody() == null || request.getBody().length == 0) {
            return null;
        }
        try {
            return JsonCodec.decode(request.getBody(), MessageExt.class);
        } catch (Exception e) {
            log.error("decode message body failed", e);
            return null;
        }
    }

    private void copyMessageExtFields(MessageExt src, MessageExtBrokerInner dst) {
        dst.setTopic(src.getTopic());
        dst.setTags(src.getTags());
        dst.setKeys(src.getKeys());
        dst.setBody(src.getBody());
        dst.setFlag(src.getFlag());
        dst.setProperties(src.getProperties());
        dst.setBornTimestamp(src.getBornTimestamp());
        dst.setQueueId(src.getQueueId());
        dst.setQueueOffset(src.getQueueOffset());
        dst.setMsgId(src.getMsgId());
        // 重投次数必须跟着进存储：编解码那一列（reconsumeTimes）读回来才有人认得它。
        // 漏掉这一行的话，绕 broker 一圈的重投副本一律以"第 0 次"回到消费端，
        // "最多重投 N 次"就变成了不封顶。
        dst.setReconsumeTimes(src.getReconsumeTimes());
        // 序列化属性为字符串，便于写入 CommitLog
        StringBuilder props = new StringBuilder();
        if (src.getProperties() != null) {
            for (java.util.Map.Entry<String, String> e : src.getProperties().entrySet()) {
                props.append(e.getKey()).append("=").append(e.getValue()).append("\n");
            }
        }
        dst.setPropertiesString(props.toString());
    }

    /** 简易统计（避免日志噪音） */
    static class TopicLogHelper {
        static final java.util.concurrent.atomic.AtomicLong SEND_COUNT = new java.util.concurrent.atomic.AtomicLong();
        static final java.util.concurrent.atomic.AtomicLong SEND_SUCCESS = new java.util.concurrent.atomic.AtomicLong();

        static void incSendCount() {
            SEND_COUNT.incrementAndGet();
        }

        static void incSendSuccess(boolean ok) {
            if (ok) { SEND_SUCCESS.incrementAndGet(); }

        }

        public static long getSendCount() { return SEND_COUNT.get(); }
        public static long getSendSuccess() { return SEND_SUCCESS.get(); }
    }
}
