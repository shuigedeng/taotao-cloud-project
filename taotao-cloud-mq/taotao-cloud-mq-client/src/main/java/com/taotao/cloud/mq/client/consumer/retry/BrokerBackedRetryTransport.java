package com.taotao.cloud.mq.client.consumer.retry;

import com.taotao.cloud.mq.client.MQClientInstance;
import com.taotao.cloud.mq.common.BrokerData;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 走真 broker 的重投/死信通路.
 * <p>
 * 两件事都只使用已有的请求码：
 * <ul>
 *   <li>重投副本 → 发回<b>原 Topic</b>（{@code SEND_MESSAGE}），消费者下一轮 pull 就会把它读回来，
 *       读回来时 {@link MessageExt#getReconsumeTimes()} 是这份副本带出去的次数 —— 掉电不丢就成立在这里：
 *       次数的载体是消息自己，不是消费进程里的某张表。</li>
 *   <li>死信 → 发到 {@code %DLQ%{consumerGroup}} 这个 Topic（同样是 {@code SEND_MESSAGE}），
 *       这样"另起一个消费者订阅死信 Topic 做人工处理或告警"才有东西可订阅。</li>
 * </ul>
 * 路由按消费端既有的口径向 NameServer 取（和 pull 用的是同一套），取不到路由就返回 false：
 * 调用方据此退回进程内那条既有通路，而不是绕过 NameServer 直连某个 broker 地址。
 * <p>
 * 每一次外投都换一个新的 msgId（{@code 原msgId#R1}、{@code 原msgId#DLQ}），这样在存储里
 * "第 N 次重投的那一份"是可定位、可数得出来的；原始消息的 msgId 留在
 * {@link #PROPERTY_SOURCE_MSG_ID} 这个属性里，不丢可追溯性。
 */
public class BrokerBackedRetryTransport implements ConsumeRetryService.RetryTransport, DeadLetterQueue.DlqPublisher {

    private static final Logger log = LoggerFactory.getLogger(BrokerBackedRetryTransport.class);

    /** 外投副本上记录"这份是从哪条消息派生出来的" */
    public static final String PROPERTY_SOURCE_MSG_ID = "SOURCE_MSG_ID";

    /** 死信副本上记录转入死信的原因（订阅死信 Topic 的一侧要能读到它） */
    public static final String PROPERTY_DEAD_LETTER_REASON = "DLQ_REASON";

    private final MQClientInstance mqClientInstance;
    private final long sendTimeoutMillis;

    public BrokerBackedRetryTransport(MQClientInstance mqClientInstance) {
        this(mqClientInstance, 3000L);
    }

    public BrokerBackedRetryTransport(MQClientInstance mqClientInstance, long sendTimeoutMillis) {
        this.mqClientInstance = mqClientInstance;
        this.sendTimeoutMillis = sendTimeoutMillis;
    }

    @Override
    public boolean sendRetryCopy(MessageExt message, int reconsumeTimes) {
        String copyId = baseMsgIdOf(message) + "#R" + reconsumeTimes;
        return sendCopy(message, message.getTopic(), copyId, reconsumeTimes, null);
    }

    @Override
    public boolean publish(MessageExt message, int reconsumeTimes, String dlqTopic, String reason) {
        if (dlqTopic == null || dlqTopic.isEmpty()) {
            return false;
        }
        String copyId = baseMsgIdOf(message) + "#DLQ";
        return sendCopy(message, dlqTopic, copyId, reconsumeTimes, reason);
    }

    /**
     * 把这条消息按 {@code targetTopic} 再投一份。
     *
     * @param reason 非 null 时作为 {@link #PROPERTY_DEAD_LETTER_REASON} 一起带走（死信用）
     * @return broker 是否真的收下了这份副本
     */
    private boolean sendCopy(MessageExt message, String targetTopic, String copyMsgId,
                             int reconsumeTimes, String reason) {
        if (mqClientInstance == null || targetTopic == null || targetTopic.isEmpty()) {
            return false;
        }
        TopicRouteData routeData = mqClientInstance.getTopicRouteData(targetTopic);
        if (routeData == null || routeData.getBrokerDatas() == null || routeData.getBrokerDatas().isEmpty()) {
            log.warn("No route for topic={}, retry copy stays in process: msgId={}", targetTopic, copyMsgId);
            return false;
        }
        String brokerAddr = null;
        String brokerName = null;
        for (BrokerData bd : routeData.getBrokerDatas()) {
            String addr = bd.selectBrokerAddr();
            if (addr != null) {
                brokerAddr = addr;
                brokerName = bd.getBrokerName();
                break;
            }
        }
        if (brokerAddr == null) {
            return false;
        }
        int queueId = pickQueueId(routeData, brokerName, copyMsgId);

        MessageExt copy = copyForSend(message, targetTopic, copyMsgId, reconsumeTimes, reason);
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        request.addExtField("msgId", copyMsgId);
        request.addExtField("topic", targetTopic);
        request.addExtField("queueId", String.valueOf(queueId));
        request.addExtField("brokerName", brokerName);
        request.setBody(JsonCodec.encode(copy));

        try {
            RemotingCommand response = mqClientInstance.invokeSync(brokerAddr, request, sendTimeoutMillis);
            if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS) {
                log.warn("Send rejected: topic={} msgId={} code={} remark={}", targetTopic, copyMsgId,
                        response == null ? "null" : String.valueOf(response.getCode()),
                        response == null ? "" : response.getRemark());
                return false;
            }
            SendResult result = response.getBody() == null ? null
                    : JsonCodec.decode(response.getBody(), SendResult.class);
            if (result == null || result.getSendStatus() != SendStatus.SEND_OK) {
                log.warn("Send not OK: topic={} msgId={} status={}", targetTopic, copyMsgId,
                        result == null ? "null" : String.valueOf(result.getSendStatus()));
                return false;
            }
            return true;
        } catch (Exception e) {
            log.warn("Send threw: topic={} msgId={} err={}", targetTopic, copyMsgId, e.getMessage());
            return false;
        }
    }

    /** 写队列数里按 msgId 散一个出来：同一份副本每次落同一个队列，便于按队列复查。 */
    private int pickQueueId(TopicRouteData routeData, String brokerName, String copyMsgId) {
        int writeQueueNums = 1;
        if (routeData.getQueueDatas() != null) {
            for (com.taotao.cloud.mq.common.QueueData qd : routeData.getQueueDatas()) {
                if (qd.getBrokerName() != null && qd.getBrokerName().equals(brokerName)) {
                    writeQueueNums = Math.max(1, qd.getWriteQueueNums());
                    break;
                }
            }
        }
        return Math.abs(copyMsgId.hashCode() % writeQueueNums);
    }

    /**
     * 造这一份要往外投的副本：正文、tag、key、属性都跟着原消息走，
     * 次数记在字段上（那是唯一一条能穿过存储的通路），标记由重试服务打好的属性带过来。
     */
    private static MessageExt copyForSend(MessageExt src, String targetTopic, String copyMsgId,
                                          int reconsumeTimes, String reason) {
        MessageExt copy = new MessageExt();
        copy.setTopic(targetTopic);
        copy.setTags(src.getTags());
        copy.setKeys(src.getKeys());
        copy.setFlag(src.getFlag());
        copy.setBody(src.getBody());
        copy.setBornTimestamp(src.getBornTimestamp());
        copy.setMsgId(copyMsgId);
        copy.setReconsumeTimes(reconsumeTimes);
        copy.putProperty(PROPERTY_SOURCE_MSG_ID, baseMsgIdOf(src));
        Map<String, String> props = src.getProperties();
        if (props != null) {
            for (Map.Entry<String, String> e : props.entrySet()) {
                if (e.getKey() != null && e.getValue() != null
                        && !PROPERTY_SOURCE_MSG_ID.equals(e.getKey())) {
                    copy.putProperty(e.getKey(), e.getValue());
                }
            }
        }
        if (reason != null) {
            copy.putProperty(PROPERTY_DEAD_LETTER_REASON, reason);
        }
        return copy;
    }

    /**
     * 原始消息的 msgId：副本自己也会绕回来被消费一次，所以要顺着
     * {@link #PROPERTY_SOURCE_MSG_ID} 往上追回第一个不带这个属性的那一条，避免
     * {@code id#R1#R2#R3} 这样越长越肥。
     */
    private static String baseMsgIdOf(MessageExt msg) {
        String source = msg.getProperty(PROPERTY_SOURCE_MSG_ID);
        String id = source != null && !source.isEmpty() ? source : msg.getMsgId();
        if (id == null || id.isEmpty()) {
            return "NO-ID";
        }
        return id;
    }
}
