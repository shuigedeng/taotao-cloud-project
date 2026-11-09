package com.taotao.cloud.mq.consistency.ttc.client.consumer;

import com.taotao.cloud.mq.consistency.ttc.client.MQClientInstance;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.ConsumeConcurrentlyStatus;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.ConsumerOffsetRequests;
import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.common.filter.FilterType;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageModel;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.PullResultPayload;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.retry.BrokerBackedRetryTransport;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.retry.ConsumeRetryService;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.retry.DeadLetterQueue;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.retry.RetryPolicy;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingTimeoutException;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 推模式消费者（对标 RocketMQ DefaultMQPushConsumer）.
 * <p>
 * 内部启动定时拉取线程, 拉到的消息回调 {@link MessageListener}。
 * 支持 Broker 端消息过滤（Tag 标签过滤 / SQL92 属性过滤）。
 * 支持消费重试和死信队列。
 */
public class DefaultMQPushConsumer {

    private static final Logger log = LoggerFactory.getLogger(DefaultMQPushConsumer.class);

    private String consumerGroup;
    private String namesrvAddr;
    private NettyClientConfig nettyClientConfig;
    private String clientId;
    private long pullIntervalMillis = 1000;
    private int pullBatchSize = 32;
    private int maxReconsumeTimes = ConsumeRetryService.DEFAULT_MAX_RECONSUME_TIMES;
    private RetryPolicy retryPolicy = RetryPolicy.STEPPED;
    private long fixedRetryIntervalMillis = ConsumeRetryService.DEFAULT_ORDERLY_RETRY_INTERVAL_MILLIS;
    private long retryCheckIntervalMillis = ConsumeRetryService.DEFAULT_RETRY_CHECK_INTERVAL_MILLIS;
    private ConsumeRetryService.RetryDelayStrategy retryDelayStrategy;
    private ConsumeRetryService.RetryTransport retryTransport;
    private MessageModel messageModel = MessageModel.CLUSTERING;

    private MQClientInstance mqClientInstance;
    private final ConcurrentHashMap<String, MQClientInstance> instanceTable = new ConcurrentHashMap<>();
    /** 订阅表 (topic -> 订阅信息). */
    private final ConcurrentHashMap<String, SubscriptionData> subscriptionTable = new ConcurrentHashMap<>();
    /**
     * 队列位点的<b>本地缓存</b>，不再是位点的真相：真相在 broker 的 {@code ConsumerOffsetManager}
     * 里，每次 listener 返回成功都由 {@link #commitOffsetThrough} 写穿过去（跨重启恢复的读侧是
     * {@code PullMessageProcessor} 在请求不带 offset 时按 (topic, queueId, group) 回读）。
     * <p>
     * 表里<b>没有</b>某个队列 = "本进程还不知道该从哪儿读" ⇒ 首次拉取不发 offset 字段, 由 broker 按
     * 已提交位点决定起点（接线前这里是 {@code computeIfAbsent(mq, k -> 0L)}, 也就是每个新进程都从 0 重放）。
     */
    private final Map<MessageQueue, Long> offsetTable = new ConcurrentHashMap<>();
    /**
     * 位点提交失败的次数：提交失败只告警、不打断拉取循环（理由见 {@link #commitOffsetThrough}），
     * 而"只告警"这件事要让外面读得到，否则代价只剩一行日志。
     * 与 {@code DeadLetterQueue} 的两个计数同形：只服务观测，不参与任何业务路径。
     */
    private final AtomicLong offsetCommitFailures = new AtomicLong();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private ScheduledExecutorService pullExecutor;
    private MessageListener listener;
    private ConsumeRetryService consumeRetryService;

    public DefaultMQPushConsumer(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public void start() throws Exception {
        if (this.clientId == null) {
            this.clientId = "PUSH_" + consumerGroup + "_" + System.currentTimeMillis();
        }
        if (this.nettyClientConfig == null) {
            this.nettyClientConfig = new NettyClientConfig();
        }
        if (this.listener == null) {
            throw new IllegalStateException("MessageListener not set");
        }
        if (subscriptionTable.isEmpty()) {
            throw new IllegalStateException("No subscription registered. Call subscribe(topic, listener) first.");
        }
        this.mqClientInstance = getOrCreateInstance();

        // 初始化消费重试服务
        this.consumeRetryService = new ConsumeRetryService(consumerGroup, maxReconsumeTimes,
                retryPolicy, fixedRetryIntervalMillis, retryCheckIntervalMillis, retryDelayStrategy);
        this.consumeRetryService.setRetryCallback(this::doRetryConsume);
        installDurableRetryPaths();
        this.consumeRetryService.start();

        this.pullExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "PushConsumerPullThread");
            t.setDaemon(true);
            return t;
        });
        this.running.set(true);
        this.pullExecutor.scheduleWithFixedDelay(this::doPull, 500, pullIntervalMillis, TimeUnit.MILLISECONDS);
        log.info("DefaultMQPushConsumer started: group={} topics={}", consumerGroup, subscriptionTable.keySet());
    }

    /**
     * 把"重投副本"和"死信"这两条外投通路接到重试服务上。
     * <p>
     * 默认用走真 broker 的实现（{@code SEND_MESSAGE} + NameServer 路由）。调用方可以在
     * {@code start()} 之前换一个实现塞进来 —— 传一个总是返回 false 的实现就等于把这两条
     * 外投通路关掉，回到纯进程内那套行为。
     */
    private void installDurableRetryPaths() {
        ConsumeRetryService.RetryTransport transport = this.retryTransport;
        if (transport == null) {
            transport = new BrokerBackedRetryTransport(this.mqClientInstance);
            this.retryTransport = transport;
        }
        this.consumeRetryService.setRetryTransport(transport);
        if (transport instanceof DeadLetterQueue.DlqPublisher) {
            this.consumeRetryService.getDeadLetterQueue()
                    .setPublisher((DeadLetterQueue.DlqPublisher) transport);
        }
    }

    public void shutdown() {
        if (running.compareAndSet(true, false)) {
            if (pullExecutor != null) {
                pullExecutor.shutdownNow();
            }
            if (consumeRetryService != null) {
                consumeRetryService.shutdown();
            }
            for (MQClientInstance ins : instanceTable.values()) {
                ins.shutdown();
            }
            instanceTable.clear();
            offsetTable.clear();
            log.info("DefaultMQPushConsumer shutdown: group={}", consumerGroup);
        }
    }

    /**
     * 订阅 Topic（全订阅，不过滤）。
     *
     * @param topic    目标 Topic
     * @param listener 消息监听器
     */
    public void subscribe(String topic, MessageListener listener) {
        subscribe(topic, "*", FilterType.TAG, listener);
    }

    /**
     * 订阅 Topic（带 Tag 过滤）。
     *
     * @param topic    目标 Topic
     * @param tag      Tag 过滤表达式（如 "TagA"、"TagA||TagB"、"*"）
     * @param listener 消息监听器
     */
    public void subscribe(String topic, String tag, MessageListener listener) {
        subscribe(topic, tag, FilterType.TAG, listener);
    }

    /**
     * 订阅 Topic（带过滤类型和表达式）。
     *
     * @param topic           目标 Topic
     * @param filterExpression 过滤表达式
     * @param filterType      过滤类型（TAG / SQL92）
     * @param listener        消息监听器
     */
    public void subscribe(String topic, String filterExpression, FilterType filterType, MessageListener listener) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("topic is null or empty");
        }
        SubscriptionData data = new SubscriptionData(topic, filterExpression, filterType);
        subscriptionTable.put(topic, data);
        // 用户多次调用 subscribe 时, 最后一个 listener 覆盖之前的
        this.listener = listener;
        log.info("Subscription registered: topic={} filterType={} expression={}",
                topic, filterType, filterExpression);
    }

    /**
     * 单轮 pull 循环: 拉每个订阅 topic 的所有 queue, 把消息回调给 listener。
     * <p>
     * 广播模式下，每个实例独立拉取所有队列；集群模式下，通过 rebalance 分配队列。
     */
    private void doPull() {
        try {
            for (Map.Entry<String, SubscriptionData> entry : subscriptionTable.entrySet()) {
                String topic = entry.getKey();
                SubscriptionData subData = entry.getValue();
                TopicRouteData routeData = mqClientInstance.getTopicRouteData(topic);
                if (routeData == null || routeData.getQueueDatas() == null) {
                    continue;
                }

                // 广播模式：每个实例拉取所有队列
                // 集群模式：当前简化实现也拉取所有队列（完整实现需要 rebalance）
                Set<String> seenBrokers = new HashSet<>();
                for (BrokerData bd : routeData.getBrokerDatas()) {
                    if (!seenBrokers.add(bd.getBrokerName())) {
                        continue;
                    }
                    String brokerAddr = bd.selectBrokerAddr();
                    if (brokerAddr == null) {
                        continue;
                    }
                    int qNums = findWriteQueueNums(routeData, bd.getBrokerName());
                    for (int q = 0; q < qNums; q++) {
                        MessageQueue mq = new MessageQueue(topic, bd.getBrokerName(), q);
                        // null = 本地还不知道起点 ⇒ 请求里不带 offset, 让 broker 用已提交位点。
                        // 这里不再 computeIfAbsent(0L): 那等于每个新进程都把整个队列重放一遍。
                        Long offset = offsetTable.get(mq);
                        try {
                            pullAndDispatch(brokerAddr, mq, offset, subData);
                        } catch (Exception ex) {
                            log.warn("pull mq={} failed: {}", mq, ex.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("doPull loop failed", e);
        }
    }

    private int findWriteQueueNums(TopicRouteData routeData, String brokerName) {
        for (com.taotao.cloud.mq.consistency.ttc.common.QueueData qd : routeData.getQueueDatas()) {
            if (brokerName.equals(qd.getBrokerName())) {
                return qd.getWriteQueueNums();
            }
        }
        return 1;
    }

    private void pullAndDispatch(String brokerAddr, MessageQueue mq, Long offset, SubscriptionData subData)
            throws Exception {
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        request.addExtField("topic", mq.getTopic());
        request.addExtField("queueId", String.valueOf(mq.getQueueId()));
        if (offset != null) {
            request.addExtField("offset", String.valueOf(offset.longValue()));
        }
        request.addExtField("maxNum", String.valueOf(pullBatchSize));
        // 消费组必须随请求上去: 位点按 (topic, queueId, group) 存, 没有 group 就没有"按组恢复"的 key
        request.addExtField(ConsumerOffsetRequests.EXT_CONSUMER_GROUP, consumerGroup);

        // 发送过滤参数到 Broker 端
        if (subData != null && subData.getFilterExpression() != null
                && !subData.getFilterExpression().isEmpty()
                && !"*".equals(subData.getFilterExpression())) {
            request.addExtField("filterType", subData.getFilterType().name());
            request.addExtField("filterExpression", subData.getFilterExpression());
        }

        RemotingCommand response;
        try {
            response = mqClientInstance.invokeSync(brokerAddr, request, 3000);
        } catch (RemotingTimeoutException | RemotingSendRequestException ex) {
            log.warn("pull invoke failed: {}", ex.getMessage());
            return;
        }
        if (response == null || response.getCode() != RemotingSysResponseCode.SUCCESS) {
            return;
        }
        PullResultPayload payload = JsonCodec.decode(response.getBody(), PullResultPayload.class);
        if (payload == null || payload.getMessages() == null || payload.getMessages().isEmpty()) {
            // 空结果不等于"什么都没扫到"。带 Tag 过滤时, broker 会按 maxNum*3 取一窗,
            // 整窗都不匹配时它把 nextOffset 推到"扫到的位点"——那段消息按订阅语义本就不该
            // 投给我。不把它提交掉, 下一轮就会从同一位点取回同一窗、同样全被过滤,
            // 带过滤的消费者从此卡在这段前缀上: 无报错、无日志、就是收不到。
            // 只在 broker 真的推进过时才提交; offset == null (首拉, 走 broker 已提交位点)
            // 或 nextOffset 未前进时不提交, 避免无谓的写穿。
            if (payload != null && offset != null && payload.getNextOffset() > offset.longValue()) {
                commitOffsetThrough(brokerAddr, mq, payload.getNextOffset());
            }
            return;
        }

        // 客户端二次过滤（Tag 精确匹配）
        List<MessageExt> messages = payload.getMessages();
        if (subData != null && subData.getFilterType() == FilterType.TAG
                && subData.getFilterExpression() != null
                && !"*".equals(subData.getFilterExpression())) {
            messages = clientSideTagFilter(messages, subData.getFilterExpression());
        }

        if (messages.isEmpty()) {
            return;
        }

        // 回调
        MessageQueueContext ctx = new MessageQueueContext(mq);
        if (listener instanceof MessageListener.Concurrently) {
            ConsumeConcurrentlyStatus status = ((MessageListener.Concurrently) listener).consumeMessage(
                    messages.toArray(new MessageExt[0]), ctx);
            if (status == ConsumeConcurrentlyStatus.CONSUME_SUCCESS) {
                commitOffsetThrough(brokerAddr, mq, payload.getNextOffset());
            } else if (status == ConsumeConcurrentlyStatus.RECONSUME_LATER) {
                // 消费失败，将消息加入重试队列
                handleConsumeFailure(messages, brokerAddr, mq, payload.getNextOffset());
            }
        } else if (listener instanceof MessageListener.Orderly) {
            ConsumeOrderlyStatus status = ((MessageListener.Orderly) listener).consumeMessage(
                    messages.toArray(new MessageExt[0]), ctx);
            if (status == ConsumeOrderlyStatus.SUCCESS) {
                commitOffsetThrough(brokerAddr, mq, payload.getNextOffset());
            } else if (status == ConsumeOrderlyStatus.RECONSUME_LATER
                    || status == ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT) {
                // 消费失败，将消息加入重试队列
                handleConsumeFailure(messages, brokerAddr, mq, payload.getNextOffset());
            }
        }
    }

    /**
     * <b>提交点（写清楚并钉住）</b>：listener 对<b>这一批</b>明确回了成功之后，先更新本地缓存、
     * 再立刻把 {@code payload.getNextOffset()} 写穿到 broker。
     * <ul>
     *   <li>为什么是"这一批的 nextOffset"而不是"最后一条的 queueOffset"：{@code nextOffset} 是
     *       broker 给出的"下一条该读的位置"，与读侧 {@code queryOffset} 同一口径，差一就在这里出局。</li>
     *   <li>为什么失败这一路也要看情况提交：{@code RECONSUME_LATER} 交给重投时，只有"后继还留在
     *       这个进程里"才继续靠未确认来兜（位点停在上一批，消息被重拉）；后继一旦落到 broker 的存储里，
     *       原消息就必须确认掉，否则它会连着自己的副本被一遍遍重拉。细节见
     *       {@link #handleConsumeFailure}。两句话合起来才是 at-least-once：一条消息至少被投一次、
     *       但投出去的后继只有一份在推进它。</li>
     *   <li>提交失败只告警、不打断拉取循环：本地缓存已经推进，消息不会因此丢；
     *       真正的保护是"下一次成功提交会带上更靠后的位点"。告警记在日志里，笔数记在
     *       {@link #offsetCommitFailures} 里，外面用 {@link #getOffsetCommitFailures()} 读得到 ——
     *       一位提交没出去，光靠日志以外的读数也应该看得出来。</li>
     * </ul>
     */
    private void commitOffsetThrough(String brokerAddr, MessageQueue mq, long nextOffset) {
        offsetTable.put(mq, Long.valueOf(nextOffset));
        try {
            ConsumerOffsetRequests.sendOffsetCommit(mqClientInstance, brokerAddr, mq.getTopic(),
                    mq.getQueueId(), consumerGroup, nextOffset);
        } catch (Exception e) {
            offsetCommitFailures.incrementAndGet();
            log.warn("commit consumer offset failed: group={} mq={} offset={} err={}",
                    consumerGroup, mq, nextOffset, e.getMessage());
        }
    }

    /**
     * 处理消费失败：把这一批逐条交给重投服务，并且<b>只在整批都有了落到存储里的后继</b>
     * （重投副本进了 broker、或死信已经进了死信 Topic）时，才把这一批的位点确认掉。
     * <ul>
     *   <li>为什么要确认：副本已经在 broker 手里，下一次投递由"重新拉到那份副本"驱动。
     *       原消息再不确认，它就会被一遍遍重拉，而每被拉一次就多落一份副本 —— 第 N 级有
     *       2<sup>N</sup> 份，"最多 16 次"会直接退化成"投不完"。</li>
     *   <li>为什么整批都要看过：只凭一条有后继就推进整批的 nextOffset，会把这一批里别的消息一起跳过。</li>
     *   <li>为什么后继只在进程里时不确认：那时真相没有落盘，进程一停就没了。
     *       这条批的位点停在上一批 ⇒ 未确认的消息会被重新拉到，at-least-once 的既有语义照旧。</li>
     * </ul>
     */
    private void handleConsumeFailure(List<MessageExt> messages, String brokerAddr, MessageQueue mq,
                                      long nextOffset) {
        if (consumeRetryService == null) {
            return;
        }
        boolean allPersisted = true;
        for (MessageExt msg : messages) {
            int reconsumeTimes = msg.getReconsumeTimes();
            int outcome = consumeRetryService.addRetryMessageForOutcome(msg, reconsumeTimes);
            if (outcome != ConsumeRetryService.OUTCOME_RETRY_PERSISTED
                    && outcome != ConsumeRetryService.OUTCOME_DEAD_LETTER_PERSISTED) {
                allPersisted = false;
            }
        }
        if (allPersisted) {
            // 后继都落到 broker 手里了，这一批的活由后继接手：确认掉，让它不再被重拉。
            commitOffsetThrough(brokerAddr, mq, nextOffset);
        }
    }

    /**
     * 客户端 Tag 二次过滤（精确匹配）。
     * <p>
     * Broker 端可能基于 hashCode 预过滤，客户端需要再精确匹配一次。
     */
    private List<MessageExt> clientSideTagFilter(List<MessageExt> messages, String tagExpression) {
        if (tagExpression == null || tagExpression.isEmpty() || "*".equals(tagExpression)) {
            return messages;
        }
        String[] tags = tagExpression.split("\\|\\|");
        Set<String> tagSet = new HashSet<>();
        for (String tag : tags) {
            String trimmed = tag.trim();
            if (!trimmed.isEmpty()) {
                tagSet.add(trimmed);
            }
        }
        if (tagSet.isEmpty()) {
            return messages;
        }

        List<MessageExt> filtered = new java.util.ArrayList<>();
        for (MessageExt msg : messages) {
            String msgTag = msg.getTags();
            if (msgTag != null && tagSet.contains(msgTag)) {
                filtered.add(msg);
            }
        }
        return filtered;
    }

    public long getOffset(MessageQueue mq) {
        Long v = offsetTable.get(mq);
        return v == null ? 0L : v;
    }

    /**
     * 这个消费者实例上「listener 回了成功、但位点提交给 broker 这一步抛了」的笔数。
     * <p>
     * 它只服务观测：提交失败不阻断拉取循环（at-least-once 的理由见 {@link #commitOffsetThrough}），
     * 所以「有没有失败过」这件事在业务路径上是不可见的 —— 没有这个出口就只能靠翻日志。
     * 读它之前先明白另一件事：{@code 0} 只说明本实例没抛过，不代表 broker 上那位是新的；
     * 位点的真相在 broker 的 {@code ConsumerOffsetManager} 那一侧。
     *
     * @return 累计提交失败的笔数（单调不减, 关机也不清零）
     */
    public long getOffsetCommitFailures() {
        return offsetCommitFailures.get();
    }

    /**
     * 重试消费回调（由 ConsumeRetryService 调用）。
     */
    private boolean doRetryConsume(MessageExt message) {
        if (listener == null) {
            return false;
        }
        try {
            if (listener instanceof MessageListener.Concurrently) {
                ConsumeConcurrentlyStatus status = ((MessageListener.Concurrently) listener).consumeMessage(
                        new MessageExt[]{message}, new MessageQueueContext(new MessageQueue(
                                message.getTopic(), "", message.getQueueId())));
                return status == ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else if (listener instanceof MessageListener.Orderly) {
                ConsumeOrderlyStatus status = ((MessageListener.Orderly) listener).consumeMessage(
                        new MessageExt[]{message}, new MessageQueueContext(new MessageQueue(
                                message.getTopic(), "", message.getQueueId())));
                return status == ConsumeOrderlyStatus.SUCCESS;
            }
        } catch (Exception e) {
            log.error("Retry consume failed: topic={}, msgId={}", message.getTopic(), message.getMsgId(), e);
        }
        return false;
    }

    private MQClientInstance getOrCreateInstance() throws InterruptedException {
        if (mqClientInstance != null && mqClientInstance.getRemotingClient() != null) {
            return mqClientInstance;
        }
        String key = "PUSH@" + consumerGroup;
        MQClientInstance inst = instanceTable.get(key);
        if (inst == null) {
            inst = new MQClientInstance(clientId, namesrvAddr, nettyClientConfig);
            inst.start();
            instanceTable.put(key, inst);
        }
        return inst;
    }

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

    public long getPullIntervalMillis() {
        return pullIntervalMillis;
    }

    public void setPullIntervalMillis(long pullIntervalMillis) {
        this.pullIntervalMillis = pullIntervalMillis;
    }

    public int getPullBatchSize() {
        return pullBatchSize;
    }

    public void setPullBatchSize(int pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }

    public int getMaxReconsumeTimes() {
        return maxReconsumeTimes;
    }

    public void setMaxReconsumeTimes(int maxReconsumeTimes) {
        this.maxReconsumeTimes = maxReconsumeTimes;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public long getFixedRetryIntervalMillis() {
        return fixedRetryIntervalMillis;
    }

    public void setFixedRetryIntervalMillis(long fixedRetryIntervalMillis) {
        this.fixedRetryIntervalMillis = fixedRetryIntervalMillis;
    }

    public ConsumeRetryService getConsumeRetryService() {
        return consumeRetryService;
    }

    /**
     * 本消费组的死信 Topic 名（{@code %DLQ%{consumerGroup}}）。
     * <p>
     * "另起一个消费者订阅死信 Topic 做人工处理或告警"这句话里，订阅方要填的就是这个名字，
     * 所以它必须能从消费端问得出来，而不是只活在常量里。
     */
    public String getDeadLetterTopic() {
        String topic = consumeRetryService == null
                ? new DeadLetterQueue(consumerGroup).getDlqTopic()
                : consumeRetryService.getDeadLetterQueue().getDlqTopic();
        return topic;
    }

    /** 当前生效的外投通路（{@code start()} 之前可以替换）. */
    public ConsumeRetryService.RetryTransport getRetryTransport() {
        return retryTransport;
    }

    public void setRetryTransport(ConsumeRetryService.RetryTransport retryTransport) {
        this.retryTransport = retryTransport;
    }

    public long getRetryCheckIntervalMillis() {
        return retryCheckIntervalMillis;
    }

    public void setRetryCheckIntervalMillis(long retryCheckIntervalMillis) {
        this.retryCheckIntervalMillis = retryCheckIntervalMillis;
    }

    public void setRetryDelayStrategy(ConsumeRetryService.RetryDelayStrategy retryDelayStrategy) {
        this.retryDelayStrategy = retryDelayStrategy;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    /**
     * 订阅信息.
     * <p>
     * 包含 Topic、过滤类型和过滤表达式。
     */
    public static class SubscriptionData implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private final String topic;
        private final String subExpression;
        private final FilterType filterType;

        public SubscriptionData(String topic, String subExpression) {
            this(topic, subExpression, FilterType.TAG);
        }

        public SubscriptionData(String topic, String subExpression, FilterType filterType) {
            this.topic = topic;
            this.subExpression = subExpression;
            this.filterType = filterType != null ? filterType : FilterType.TAG;
        }

        public String getTopic() {
            return topic;
        }

        public String getSubExpression() {
            return subExpression;
        }

        public FilterType getFilterType() {
            return filterType;
        }

        public String getFilterExpression() {
            return subExpression;
        }
    }

    static {
        // 占位：保证 import 使用
        Collections.emptyList();
    }
}
