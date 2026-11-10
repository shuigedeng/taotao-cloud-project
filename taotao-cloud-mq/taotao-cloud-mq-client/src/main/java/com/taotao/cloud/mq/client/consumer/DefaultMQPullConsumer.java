package com.taotao.cloud.mq.client.consumer;

import com.taotao.cloud.mq.client.MQClientInstance;
import com.taotao.cloud.mq.common.BrokerData;
import com.taotao.cloud.mq.common.MessageQueue;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.PullResultPayload;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拉模式消费者（对标 RocketMQ DefaultMQPullConsumer）.
 * <p>
 * 用户自行调用 {@link #pull(MessageQueue, long, int)} 拉取消息，自行管理 offset。
 * 适合"应用驱动"场景 (例如按需拉取、批处理、流控)。
 */
public class DefaultMQPullConsumer {

    private static final Logger log = LoggerFactory.getLogger(DefaultMQPullConsumer.class);

    /**
     * 挂起预算在 pull 请求里的字段名 —— 与 Broker 侧
     * {@code PullMessageProcessor.EXT_SUSPEND_TIMEOUT_MILLIS} 是同一个线上契约
     * （client 不依赖 broker, 所以这里只能是字面量; 两侧由 LongPollingWiringGuardTest 钉住同一个值）。
     */
    public static final String SUSPEND_TIMEOUT_FIELD = "suspendTimeoutMillis";

    /** 不带挂起预算时的 pull RPC 超时（接线前写死的 3000ms，逐字保留）。 */
    public static final long PULL_RPC_TIMEOUT_MILLIS = 3000L;

    /**
     * 响应里"这次 pull 因何而醒"的字段名 —— 与 Broker 侧
     * {@code PullMessageProcessor.EXT_SUSPEND_WAKEUP} 同一个线上契约。
     */
    public static final String SUSPEND_WAKEUP_FIELD = "suspendWakeup";

    /**
     * 带挂起预算时，RPC 超时在预算之上再留的余量。
     * <p>
     * 为什么必须有这块余量：Broker 那边挂起到期由 PullRequestHoldService 的
     * 扫描线程兑现（扫描周期 1s），真实释放时刻最晚是"预算 + 一个扫描周期"，再算上响应写回与
     * 网络回程。客户端若只比预算多等一点点，就会出现"Broker 正要返回、客户端已经超时"，
     * 而 pull 超时在客户端被读成 CONNECTION_LOST/异常 ⇒ 接了长轮询反而比不接更坏。
     */
    public static final long SUSPEND_RPC_MARGIN_MILLIS = 3000L;

    /**
     * pull 这一次 RPC 允许的超时：结构上保证"客户端一定比 Broker 醒得晚"。
     * <p>
     * 不变式（被机检钉住，不留在注释里）: 对任意预算 b ≥ 0 都有
     * {@code pullRpcTimeoutMillis(b) > b}，且 Broker 侧实际挂起时长 {@code <= b}。
     *
     * @param suspendBudgetMillis 本次 pull 给 Broker 的挂起预算, {@code <= 0} 表示不挂起
     * @return RPC 超时（毫秒）
     */
    public static long pullRpcTimeoutMillis(long suspendBudgetMillis) {
        if (suspendBudgetMillis <= 0) {
            return PULL_RPC_TIMEOUT_MILLIS;
        }
        return suspendBudgetMillis + SUSPEND_RPC_MARGIN_MILLIS;
    }

    private String consumerGroup;
    private String namesrvAddr;
    private NettyClientConfig nettyClientConfig;
    private String clientId;

    private MQClientInstance mqClientInstance;
    private final ConcurrentHashMap<String, MQClientInstance> instanceTable = new ConcurrentHashMap<>();
    /**
     * 队列位点的<b>本地缓存</b>：这里的数不再是位点的真相（真相在 broker 的
     * {@code ConsumerOffsetManager}，按 {@code (topic, queueId, group)} 存盘、跨重启恢复）。
     * <p>
     * 写这张表只有两条路：{@link #updateOffset(MessageQueue, long)}（用户自己记）与
     * {@link #commitOffset(MessageQueue, long)}（写穿到 broker）。只走前一条 ⇒ 位点出不了进程,
     * 这正是本支要修的"广告了但没接线"。
     */
    private final Map<MessageQueue, Long> offsetTable = new ConcurrentHashMap<>();

    public DefaultMQPullConsumer(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public void start() throws Exception {
        if (this.clientId == null) {
            this.clientId = "PULL_" + consumerGroup + "_" + System.currentTimeMillis();
        }
        if (this.nettyClientConfig == null) {
            this.nettyClientConfig = new NettyClientConfig();
        }
        this.mqClientInstance = getOrCreateInstance();
        log.info("DefaultMQPullConsumer started: group={} namesrv={}", consumerGroup, namesrvAddr);
    }

    public void shutdown() {
        for (MQClientInstance ins : instanceTable.values()) {
            ins.shutdown();
        }
        instanceTable.clear();
        offsetTable.clear();
        log.info("DefaultMQPullConsumer shutdown: group={}", consumerGroup);
    }

    /**
     * 同步拉取（短轮询：读不到就立刻返回）。
     *
     * @param mq         队列
     * @param offset     起始 offset
     * @param maxNums    最多拉多少条
     * @return 拉取结果（包含 nextOffset 与消息列表）
     */
    public PullResult pull(MessageQueue mq, long offset, int maxNums) throws Exception {
        return pull(mq, offset, maxNums, 0L);
    }

    /**
     * 同步拉取，允许 Broker 端长轮询。
     *
     * @param mq                  队列
     * @param offset              起始 offset
     * @param maxNums             最多拉多少条
     * @param suspendBudgetMillis 允许 Broker 在"这个 offset 读不到消息"时挂起多久（毫秒）;
     *                            {@code <= 0} 即短轮询, 请求与响应形状与三参版本逐字相同
     * @return 拉取结果（包含 nextOffset 与消息列表）
     */
    public PullResult pull(MessageQueue mq, long offset, int maxNums, long suspendBudgetMillis) throws Exception {
        return pullInternal(mq, Long.valueOf(offset), maxNums, suspendBudgetMillis);
    }

    /**
     * 同步拉取，<b>不带 offset</b>：起始位点由 broker 按 {@code (topic, queueId, 本消费组)} 的
     * 已提交位点决定 —— 这才是"消费位点持久化, 跨重启恢复"那句广告的读侧。
     * <p>
     * 本组从没提交过位点时 broker 回落到 0（与接线前同一形状，不会抛）。
     *
     * @param mq      队列
     * @param maxNums 最多拉多少条
     * @return 拉取结果（{@code nextOffset} 即下一次该提交的位点）
     */
    public PullResult pullFromCommittedOffset(MessageQueue mq, int maxNums) throws Exception {
        return pullInternal(mq, null, maxNums, 0L);
    }

    /**
     * 同上，允许 Broker 端长轮询。
     *
     * @param mq                  队列
     * @param maxNums             最多拉多少条
     * @param suspendBudgetMillis 允许 Broker 挂起多久（毫秒）; {@code <= 0} 即短轮询
     * @return 拉取结果
     */
    public PullResult pullFromCommittedOffset(MessageQueue mq, int maxNums, long suspendBudgetMillis)
            throws Exception {
        return pullInternal(mq, null, maxNums, suspendBudgetMillis);
    }

    /**
     * @param offset 起始位点; {@code null} = <b>不发 offset 字段</b>, 让 broker 用已提交位点。
     *               之所以用"字段缺席"而不是"发 -1"：-1 在请求里是一个合法-looking 的位点数,
     *               一旦被 {@code Long.parseLong} 吃下去就会变成一个谁都没想要的起点。
     */
    private PullResult pullInternal(MessageQueue mq, Long offset, int maxNums, long suspendBudgetMillis)
            throws Exception {
        if (mq == null) {
            throw new IllegalArgumentException("MessageQueue is null");
        }
        if (mqClientInstance == null) {
            throw new IllegalStateException("consumer not started");
        }

        TopicRouteData routeData = mqClientInstance.getTopicRouteData(mq.getTopic());
        if (routeData == null) {
            throw new RemotingSendRequestException("No route for topic: " + mq.getTopic());
        }
        String brokerAddr = lookupBrokerAddr(routeData, mq.getBrokerName());
        if (brokerAddr == null) {
            throw new RemotingSendRequestException("No broker addr for: " + mq.getBrokerName());
        }

        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        request.addExtField("topic", mq.getTopic());
        request.addExtField("queueId", String.valueOf(mq.getQueueId()));
        if (offset != null) {
            request.addExtField("offset", String.valueOf(offset.longValue()));
        }
        request.addExtField("maxNum", String.valueOf(maxNums));
        // 消费组: broker 侧按 (topic, queueId, group) 解析已提交位点用; 显式带 offset 时它只是备查
        request.addExtField(ConsumerOffsetRequests.EXT_CONSUMER_GROUP, consumerGroup);
        if (suspendBudgetMillis > 0) {
            // 只带预算的请求才会挂起
            request.addExtField(SUSPEND_TIMEOUT_FIELD, String.valueOf(suspendBudgetMillis));
        }

        RemotingCommand response = mqClientInstance.invokeSync(brokerAddr, request,
                pullRpcTimeoutMillis(suspendBudgetMillis));
        // 失败路径上没有 broker 给的位点可用, 只能把请求里那个数（不带 offset 时是 -1, 表示"未知"）回给调用方
        long fallbackOffset = offset == null ? -1L : offset.longValue();
        if (response == null) {
            return new PullResult(PullStatus.CONNECTION_LOST, mq, fallbackOffset,
                    java.util.Collections.emptyList());
        }
        if (response.getCode() != RemotingSysResponseCode.SUCCESS) {
            return new PullResult(PullStatus.SYSTEM_ERROR, mq, fallbackOffset,
                    java.util.Collections.emptyList());
        }
        PullResultPayload payload = JsonCodec.decode(response.getBody(), PullResultPayload.class);
        if (payload == null) {
            return new PullResult(PullStatus.NO_MATCHED_MSG, mq, fallbackOffset,
                    java.util.Collections.emptyList());
        }
        long nextOffset = payload.getNextOffset();
        List<MessageExt> msgs = payload.getMessages();
        PullStatus status = msgs == null || msgs.isEmpty()
                ? PullStatus.NO_NEW_MSG
                : PullStatus.FOUND;
        return new PullResult(status, mq, nextOffset, msgs == null ? java.util.Collections.emptyList() : msgs,
                response.getExtField(SUSPEND_WAKEUP_FIELD));
    }

    /**
     * <b>显式提交消费位点</b>：把 {@code (topic, queueId, 本消费组) -> offset} 写给 broker，
     * 由 broker 的 {@code ConsumerOffsetManager} 落盘（周期 flush + 关机 flush），从而跨重启恢复。
     * <p>
     * 提交的是"下次该从哪儿开始读"，即已消费的最后一条的 queueOffset + 1 —— 与
     * {@code PullResult.getNextOffset()} 同一个口径。
     * <p>
     * broker 没答应就抛（不静默）：提交失败的后果是重启后整队重放，比当场响更糟。
     *
     * @param mq     队列
     * @param offset 下次消费的起始位点
     * @return broker 侧记下的位点（响应里回读的那个值）
     */
    public long commitOffset(MessageQueue mq, long offset) throws Exception {
        if (mq == null) {
            throw new IllegalArgumentException("MessageQueue is null");
        }
        if (mqClientInstance == null) {
            throw new IllegalStateException("consumer not started");
        }
        return commitOffsetToBroker(resolveBrokerAddr(mq), mq, offset);
    }

    /**
     * 同一个提交，但直接指定 broker 地址（不走路由）。
     * <p>
     * 存在的理由：单 broker / 运维脚本 / 测试手里往往只有 {@code host:port}，没有 nameserver 也不该
     * 因此丢位点。{@link #commitOffset(MessageQueue, long)} 就是解析完地址后调它。
     */
    public long commitOffsetToBroker(String brokerAddr, MessageQueue mq, long offset) throws Exception {
        if (mqClientInstance == null) {
            throw new IllegalStateException("consumer not started");
        }
        long stored = ConsumerOffsetRequests.sendOffsetCommit(mqClientInstance, brokerAddr,
                mq.getTopic(), mq.getQueueId(), consumerGroup, offset);
        offsetTable.put(mq, Long.valueOf(stored));
        return stored;
    }

    private String resolveBrokerAddr(MessageQueue mq) throws RemotingSendRequestException {
        TopicRouteData routeData = mqClientInstance.getTopicRouteData(mq.getTopic());
        if (routeData == null) {
            throw new RemotingSendRequestException("No route for topic: " + mq.getTopic());
        }
        String brokerAddr = lookupBrokerAddr(routeData, mq.getBrokerName());
        if (brokerAddr == null) {
            throw new RemotingSendRequestException("No broker addr for: " + mq.getBrokerName());
        }
        return brokerAddr;
    }

    /**
     * 取本地缓存里的位点（<b>不是</b> broker 上的真相；没提交过就一直是一次本地读）。
     * <p>
     * 想要"重启后接着上次的位置读"，用 {@link #pullFromCommittedOffset(MessageQueue, int)}：
     * 那条路把 key 交给 broker 去解，进程内这张表不再是唯一真相。
     */
    public long getOffset(MessageQueue mq) {
        Long off = offsetTable.get(mq);
        return off == null ? 0L : off;
    }

    /**
     * 只改本地缓存，<b>不碰 broker</b>。要让它出得了这个进程，必须再调
     * {@link #commitOffset(MessageQueue, long)}（或直接用 {@link #pullFromCommittedOffset(MessageQueue, int)}
     * 让 broker 决定起点）。
     */
    public void updateOffset(MessageQueue mq, long offset) {
        offsetTable.put(mq, offset);
    }

    private String lookupBrokerAddr(TopicRouteData routeData, String brokerName) {
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

    private MQClientInstance getOrCreateInstance() throws InterruptedException {
        if (mqClientInstance != null && mqClientInstance.getRemotingClient() != null) {
            return mqClientInstance;
        }
        String key = "PULL@" + consumerGroup;
        MQClientInstance inst = instanceTable.get(key);
        if (inst == null) {
            inst = new MQClientInstance(clientId, namesrvAddr, nettyClientConfig);
            inst.start();
            instanceTable.put(key, inst);
        }
        return inst;
    }

    // ===== Getters / Setters =====

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
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

    /**
     * 拉取状态（对标 RocketMQ PullStatus）.
     */
    public enum PullStatus {
        /** 找到消息 */
        FOUND,
        /** 没有新消息 */
        NO_NEW_MSG,
        /** 没有匹配的消息 (offset 已越界) */
        NO_MATCHED_MSG,
        /** 系统错误 */
        SYSTEM_ERROR,
        /** 连接丢失 */
        CONNECTION_LOST
    }

    /**
     * 拉取结果封装。
     */
    public static class PullResult {
        private final PullStatus status;
        private final MessageQueue messageQueue;
        private final long nextOffset;
        private final List<MessageExt> msgFoundList;
        /**
         * Broker 这次为什么放开挂起的 pull（长轮询专用）。
         * <p>
         * null = 这次请求没挂起（不带预算的正常形状）；"message" 才代表"消息到达把这条 pull 叫醒了"，
         * 取值见 {@code PullMessageProcessor.EXT_SUSPEND_WAKEUP} 的说明。
         */
        private final String suspendWakeup;

        public PullResult(PullStatus status, MessageQueue mq, long nextOffset, List<MessageExt> msgs) {
            this(status, mq, nextOffset, msgs, null);
        }

        public PullResult(PullStatus status, MessageQueue mq, long nextOffset, List<MessageExt> msgs,
                          String suspendWakeup) {
            this.status = status;
            this.messageQueue = mq;
            this.nextOffset = nextOffset;
            this.msgFoundList = msgs;
            this.suspendWakeup = suspendWakeup;
        }

        public PullStatus getStatus() {
            return status;
        }

        public MessageQueue getMessageQueue() {
            return messageQueue;
        }

        public long getNextOffset() {
            return nextOffset;
        }

        public List<MessageExt> getMsgFoundList() {
            return msgFoundList;
        }

        /**
         * @return Broker 回写的"因何而醒"标记, null 表示本次没有挂起
         */
        public String getSuspendWakeup() {
            return suspendWakeup;
        }
    }
}
