package com.taotao.cloud.mq.client.producer;

import com.taotao.cloud.mq.client.MQClientInstance;
import com.taotao.cloud.mq.common.BrokerData;
import com.taotao.cloud.mq.common.MessageQueue;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.remoting.exception.RemotingTimeoutException;
import com.taotao.cloud.mq.remoting.netty.*;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 默认 MQ 生产者（对标 RocketMQ DefaultMQProducer）.
 * <p>
 * 一个 producerGroup 共享一个 DefaultMQProducer 实例。
 * 内部委托给 MQClientInstance 完成与 NameServer / Broker 的通信。
 */
public class DefaultMQProducer {

    private static final Logger log = LoggerFactory.getLogger(DefaultMQProducer.class);

    private String producerGroup;
    private String namesrvAddr;
    private NettyClientConfig nettyClientConfig;
    private String clientId;
    private long sendMsgTimeoutMillis = 3000;
    private int defaultTopicQueueNums = 4;

    protected MQClientInstance mqClientInstance;
    private final ConcurrentHashMap<String, MQClientInstance> instanceTable = new ConcurrentHashMap<>();
    private final AtomicLong msgIdGenerator = new AtomicLong(0);

    public DefaultMQProducer(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public DefaultMQProducer(String producerGroup, NettyClientConfig nettyClientConfig) {
        this.producerGroup = producerGroup;
        this.nettyClientConfig = nettyClientConfig;
    }

    public void start() throws Exception {
        if (this.clientId == null) {
            this.clientId = "PRODUCER_" + producerGroup + "_" + System.currentTimeMillis();
        }
        if (this.nettyClientConfig == null) {
            this.nettyClientConfig = new NettyClientConfig();
        }
        MQClientInstance instance = getOrCreateInstance();
        // 通过 MQClientInstance 引用保持
        this.mqClientInstance = instance;
        log.info("DefaultMQProducer started: group={}, namesrv={}", producerGroup, namesrvAddr);
    }

    public void shutdown() {
        // 简化：每个 Producer 共享一份 instance，但生产端通常应保留
        for (MQClientInstance ins : instanceTable.values()) {
            ins.shutdown();
        }
        instanceTable.clear();
        log.info("DefaultMQProducer shutdown: group={}", producerGroup);
    }

    /**
     * 同步发送：阻塞等待结果，失败抛出异常.
     */
    public SendResult send(Message message) throws Exception {
        return sendWithRequestCode(message, RequestCode.SEND_MESSAGE);
    }

    /**
     * 按指定的请求码同步发送. 普通发送走 {@link RequestCode#SEND_MESSAGE},
     * 事务的 prepare 阶段走 {@link RequestCode#SEND_MESSAGE_V2} —— 两者共用同一套
     * "路由 → 选队列 → 找 master 地址 → 编码" 的组包口径, 只差那一个码, 所以只有码是参数.
     * <p>
     * 路由与队列始终按 {@code message.getTopic()} 这条【业务 topic】来选: 调用方不许为了
     * "让它落到别的 topic" 而就地改掉消息自己的 topic, 那是把业务 topic 丢掉的第一步
     * (broker 侧回投时就没有原始 topic 可用了).
     */
    protected SendResult sendWithRequestCode(Message message, int requestCode) throws Exception {
        return executePreparedSend(prepareMessageSend(message, requestCode));
    }

    /**
     * 只做组包 (含前置校验), 不发; 调用方拿到 {@link PreparedSend} 后就知道这条消息真正落到了
     * 哪台 broker —— 事务的二次确认必须发往【写下半消息的那一台】, 而不是另查一次路由.
     */
    PreparedSend prepareMessageSend(Message message, int requestCode) throws Exception {
        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }
        if (mqClientInstance == null) {
            throw new IllegalStateException("producer not started");
        }
        validateMessage(message);
        return prepareSend(getOrCreateInstance(), message, requestCode);
    }

    /** 把一条已组好的请求同步发出去并翻译成 SendResult. */
    SendResult executePreparedSend(PreparedSend prepared) throws Exception {
        MQClientInstance instance = getOrCreateInstance();

        // 同步 RPC
        RemotingCommand response = instance.invokeSync(prepared.brokerAddr, prepared.request, sendMsgTimeoutMillis);

        SendResult result = toSendResult(response, prepared);
        if (result.getMsgId() == null) {
            result.setMsgId(prepared.request.getExtField("msgId"));
        }
        return result;
    }

    /** 这条请求实际发往的 broker 地址 (二次确认要发回同一台). */
    String brokerAddrOf(PreparedSend prepared) {
        return prepared.brokerAddr;
    }

    /**
     * 单向发送：不关心结果，也不等结果.
     * <p>
     * 真走 {@code invokeOneway}：请求被标成 oneway 后 broker 端不会写响应（
     * {@code NettyRemotingAbstract.processRequestCommand} 对 {@code isOnewayRPC()} 直接跳过响应），
     * 所以这里绝不能再用 invokeSync —— 那等于每次 oneway 都阻塞满 sendMsgTimeoutMillis
     * 再抛 RemotingTimeoutException。oneway 的许可是即借即还的，不占响应表位。
     */
    public void sendOneway(Message message) throws Exception {
        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }
        if (mqClientInstance == null) {
            throw new IllegalStateException("producer not started");
        }
        validateMessage(message);
        MQClientInstance instance = getOrCreateInstance();

        PreparedSend prepared = prepareSend(instance, message);
        instance.invokeOneway(prepared.brokerAddr, prepared.request, sendMsgTimeoutMillis);
    }

    /**
     * 异步发送：SendCallback 回调结果.
     * <p>
     * 真异步：RPC 走 {@code invokeAsync}，onSuccess/onException 由 remoting 层的
     * callbackExecutor 线程执行，不在调用线程上。RPC 还没发出去就失败（未 start / 无路由 /
     * 消息非法 / 流控）时，异常同样投递到 callbackExecutor 上报给 callback，
     * 保证"回调永远在别的线程、绝不当场同步执行"这一条对外语义。
     */
    public void send(Message message, SendCallback sendCallback) throws Exception {
        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }
        if (sendCallback == null) {
            // 没有回调就没有异步可言：退回同步语义，异常直接抛给调用方
            send(message);
            return;
        }
        MQClientInstance instance;
        PreparedSend prepared;
        try {
            if (mqClientInstance == null) {
                throw new IllegalStateException("producer not started");
            }
            validateMessage(message);
            instance = getOrCreateInstance();
            prepared = prepareSend(instance, message);
        } catch (Exception preFlightFailure) {
            dispatchToCallbackExecutor(sendCallback, preFlightFailure);
            return;
        }

        final SendCallback callback = sendCallback;
        final PreparedSend finalPrepared = prepared;
        instance.invokeAsync(prepared.brokerAddr, prepared.request, sendMsgTimeoutMillis,
                new NettyRemotingAbstract.InvokeCallback() {
                    @Override
                    public void operationComplete(ResponseFuture responseFuture) {
                        Throwable cause = responseFuture.getCause();
                        if (cause == null && responseFuture.getResponseCommand() == null) {
                            // 没响应也没异常 ⇒ 只能是被超时扫描摘掉
                            cause = new RemotingTimeoutException(
                                    "no response for async send, opaque=" + responseFuture.getOpaque());
                        }
                        if (cause != null && responseFuture.getResponseCommand() == null) {
                            callback.onException(cause);
                            return;
                        }
                        try {
                            callback.onSuccess(toSendResult(responseFuture.getResponseCommand(), finalPrepared));
                        } catch (Throwable e) {
                            callback.onException(e);
                        }
                    }
                });
    }

    /**
     * 把一个尚未发出的失败交给 callback：投递到 remoting 的 callbackExecutor，
     * 池不可用（客户端已关闭等）时才退回当前线程，保证"至少交付一次"。
     */
    private void dispatchToCallbackExecutor(final SendCallback callback, final Throwable failure) {
        ExecutorService callbackExecutor = null;
        try {
            MQClientInstance instance = getOrCreateInstance();
            NettyRemotingClient client = instance.getRemotingClient();
            if (client != null) {
                callbackExecutor = client.getCallbackExecutor();
            }
        } catch (Exception e) {
            log.warn("cannot resolve callbackExecutor for async send failure", e);
        }

        if (callbackExecutor == null || callbackExecutor.isShutdown()) {
            callback.onException(failure);
            return;
        }
        final Throwable toReport = failure;
        try {
            callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    callback.onException(toReport);
                }
            });
        } catch (RejectedExecutionException e) {
            log.warn("callbackExecutor rejected pre-flight failure, deliver it here", e);
            callback.onException(failure);
        }
    }

    /**
     * 组一条发送请求：路由 → 选队列 → 找 master 地址 → 编码消息体.
     */
    private PreparedSend prepareSend(MQClientInstance instance, Message message) throws Exception {
        return prepareSend(instance, message, RequestCode.SEND_MESSAGE);
    }

    private PreparedSend prepareSend(MQClientInstance instance, Message message, int requestCode) throws Exception {
        TopicRouteData routeData = instance.getTopicRouteData(message.getTopic());
        if (routeData == null || routeData.getQueueDatas() == null || routeData.getQueueDatas().isEmpty()) {
            throw new RemotingSendRequestException("No route for topic: " + message.getTopic());
        }
        MessageQueue mq = instance.selectOneMessageQueue(message.getTopic(), routeData);
        if (mq == null) {
            throw new RemotingSendRequestException("No writable queue for topic: " + message.getTopic());
        }
        String brokerAddr = lookupBrokerMasterAddr(routeData, mq.getBrokerName());
        if (brokerAddr == null) {
            throw new RemotingSendRequestException("No master addr for broker: " + mq.getBrokerName());
        }
        return new PreparedSend(mq, brokerAddr, buildSendRequest(message, mq, requestCode));
    }

    RemotingCommand buildSendRequest(Message message, MessageQueue mq) {
        return buildSendRequest(message, mq, RequestCode.SEND_MESSAGE);
    }

    RemotingCommand buildSendRequest(Message message, MessageQueue mq, int requestCode) {
        MessageExt inner = buildMessageExt(message, mq);
        RemotingCommand request = RemotingCommand.createRequestCommand(requestCode);
        request.addExtField("msgId", inner.getMsgId());
        request.addExtField("topic", message.getTopic());
        request.addExtField("queueId", String.valueOf(mq.getQueueId()));
        request.addExtField("brokerName", mq.getBrokerName());
        request.setBody(JsonCodec.encode(inner));
        return request;
    }

    /**
     * 把 broker 响应翻译成 SendResult（同步与异步共用一套口径）.
     * <p>
     * <b>response == null 一律抛，不许就地造码。</b>刷盘超时这个码的语义是"消息已经写进存储、
     * 只是同步刷盘没在时限内落住盘"，这件事只有 store 侧知道（CommitLog 产出对应的
     * PutMessageStatus，broker 的 SendMessageProcessor 才把它写进响应体）。client 在根本没拿到
     * 响应时对此一无所知：旧实现凭空写一个刷盘超时的码，等于把"可能根本没送到"洗成
     * "送到了但没落盘"。抛 {@link RemotingSendRequestException} 才是"我不知道结果"的正确表达，
     * 消息里带上 topic / brokerName / queueId / brokerAddr / opaque 供调用人定位这条请求。
     * <p>
     * 注意别把这段和"异步侧无响应"搞混：{@code NettyRemotingAbstract.invokeSyncImpl} 拿到空响应时
     * 自己就抛（该文件里 {@code return null} 是 0 命中），所以 null 只代表 remoting 契约被违反
     * （自定义 NettyRemotingClient 实现、或测试替身），不代表 broker 报了超时。
     */
    private SendResult toSendResult(RemotingCommand response, PreparedSend prepared)
            throws RemotingSendRequestException {
        if (response == null) {
            throw noResponseFromBroker(prepared.messageQueue, prepared.brokerAddr, prepared.request);
        }
        SendResult result = new SendResult();
        result.setSendStatus(SendStatus.SEND_OK);
        MessageQueue mq = prepared.messageQueue;
        if (response.getCode() != RemotingSysResponseCode.SUCCESS) {
            result.setSendStatus(SendStatus.SEND_FAILED);
            result.setErrorMsg(response.getRemark());
            return result;
        }
        if (response.getBody() != null && response.getBody().length > 0) {
            try {
                SendResult remote = JsonCodec.decode(response.getBody(), SendResult.class);
                if (remote != null) {
                    if (remote.getMessageQueue() == null) {
                        remote.setMessageQueue(mq);
                    }
                    return remote;
                }
            } catch (Exception e) {
                log.warn("decode send result failed, use local", e);
            }
        }
        result.setMessageQueue(mq);
        return result;
    }

    /**
     * "没拿到响应"的唯一表达：抛，并且把足以定位这条请求的信息带进消息。
     * <p>
     * 包装形状照 {@code MQClientInstance.invokeAndTranslateException} 已有的那一条
     * （{@code RemotingSendRequestException} + 人可读的 addr），不自创异常类型。
     */
    private static RemotingSendRequestException noResponseFromBroker(MessageQueue mq, String brokerAddr,
                                                                    RemotingCommand request) {
        StringBuilder msg = new StringBuilder("no response command from broker; send outcome is unknown");
        msg.append(", topic=").append(mq == null ? "<unknown>" : mq.getTopic());
        msg.append(", brokerName=").append(mq == null ? "<unknown>" : mq.getBrokerName());
        msg.append(", queueId=").append(mq == null ? "<unknown>" : String.valueOf(mq.getQueueId()));
        msg.append(", brokerAddr=").append(brokerAddr == null ? "<unknown>" : brokerAddr);
        msg.append(", opaque=").append(request == null ? "<unknown>" : String.valueOf(request.getOpaque()));
        // 只抛：client 侧没有任何信息来源可以支撑一个"刷盘"结论
        return new RemotingSendRequestException(msg.toString());
    }

    /** 一次发送的预备结果：选中的队列、目标地址与已编码的请求. */
    static class PreparedSend {
        final MessageQueue messageQueue;
        final String brokerAddr;
        final RemotingCommand request;

        PreparedSend(MessageQueue messageQueue, String brokerAddr, RemotingCommand request) {
            this.messageQueue = messageQueue;
            this.brokerAddr = brokerAddr;
            this.request = request;
        }
    }

    /**
     * 按指定队列发送（保证顺序）.
     */
    public SendResult send(Message message, MessageQueue mq) throws Exception {
        if (mq == null) {
            return send(message);
        }
        if (mqClientInstance == null) {
            throw new IllegalStateException("producer not started");
        }
        MQClientInstance instance = getOrCreateInstance();
        TopicRouteData routeData = instance.getTopicRouteData(message.getTopic());
        if (routeData == null) {
            throw new RemotingSendRequestException("No route for topic: " + message.getTopic());
        }
        String brokerAddr = lookupBrokerMasterAddr(routeData, mq.getBrokerName());
        if (brokerAddr == null) {
            throw new RemotingSendRequestException("No master addr for broker: " + mq.getBrokerName());
        }

        MessageExt inner = buildMessageExt(message, mq);
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        request.addExtField("topic", message.getTopic());
        request.addExtField("queueId", String.valueOf(mq.getQueueId()));
        request.addExtField("brokerName", mq.getBrokerName());
        request.setBody(JsonCodec.encode(inner));

        RemotingCommand response = instance.invokeSync(brokerAddr, request, sendMsgTimeoutMillis);
        if (response == null) {
            // 与 toSendResult 同一条口径：没响应就抛，绝不就地造一个只有 broker 才知道的码
            throw noResponseFromBroker(mq, brokerAddr, request);
        }
        SendResult result = new SendResult();
        if (response.getCode() != RemotingSysResponseCode.SUCCESS) {
            result.setSendStatus(SendStatus.SEND_FAILED);
            result.setErrorMsg(response.getRemark());
            return result;
        }
        result.setSendStatus(SendStatus.SEND_OK);
        return result;
    }

    private MQClientInstance getOrCreateInstance() throws InterruptedException {
        if (mqClientInstance != null && mqClientInstance.getRemotingClient() != null) {
            return mqClientInstance;
        }
        String key = "PRODUCER@" + producerGroup;
        MQClientInstance inst = instanceTable.get(key);
        if (inst == null) {
            inst = new MQClientInstance(clientId, namesrvAddr, nettyClientConfig);
            inst.start();
            instanceTable.put(key, inst);
        }
        return inst;
    }

    private MessageExt buildMessageExt(Message message, MessageQueue mq) {
        MessageExt ext = new MessageExt();
        ext.setTopic(message.getTopic());
        ext.setTags(message.getTags());
        ext.setKeys(message.getKeys());
        ext.setBody(message.getBody());
        ext.setFlag(message.getFlag());
        ext.setProperties(message.getProperties());
        ext.setBornTimestamp(System.currentTimeMillis());
        ext.setQueueId(mq.getQueueId());
        ext.setQueueOffset(0L);
        ext.setMsgId(generateMsgId(message.getTopic()));
        return ext;
    }

    private String generateMsgId(String topic) {
        return clientId + "-" + topic + "-" + msgIdGenerator.incrementAndGet();
    }

    private String lookupBrokerMasterAddr(TopicRouteData routeData, String brokerName) {
        if (routeData == null || routeData.getBrokerDatas() == null) {
            return null;
        }

        for (BrokerData bd : routeData.getBrokerDatas()) {
            if (bd.getBrokerName().equals(brokerName)) {
                return bd.selectBrokerAddr();
            }
        }
        return null;
    }

    private void validateMessage(Message message) {
        if (message.getTopic() == null || message.getTopic().isEmpty()) {
            throw new IllegalArgumentException("topic is null or empty");
        }
        if (message.getBody() == null) {
            throw new IllegalArgumentException("body is null");
        }
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public long getSendMsgTimeoutMillis() {
        return sendMsgTimeoutMillis;
    }

    public void setSendMsgTimeoutMillis(long sendMsgTimeoutMillis) {
        this.sendMsgTimeoutMillis = sendMsgTimeoutMillis;
    }

    public int getDefaultTopicQueueNums() {
        return defaultTopicQueueNums;
    }

    public void setDefaultTopicQueueNums(int defaultTopicQueueNums) {
        this.defaultTopicQueueNums = defaultTopicQueueNums;
    }

    public NettyClientConfig getNettyClientConfig() {
        return nettyClientConfig;
    }

    public void setNettyClientConfig(NettyClientConfig nettyClientConfig) {
        this.nettyClientConfig = nettyClientConfig;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /** P 占位.
     */
    static class P implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}
