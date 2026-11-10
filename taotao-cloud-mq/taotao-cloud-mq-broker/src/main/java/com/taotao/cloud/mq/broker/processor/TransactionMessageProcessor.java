package com.taotao.cloud.mq.broker.processor;

import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.broker.longpoll.PullRequestHoldService;
import com.taotao.cloud.mq.broker.transaction.TransactionCheckService;
import com.taotao.cloud.mq.broker.transaction.TransactionStateManager;
import com.taotao.cloud.mq.broker.transaction.TransactionStateRecovery;
import com.taotao.cloud.mq.common.enums.TransactionCheckTask;
import com.taotao.cloud.mq.common.enums.TransactionState;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.plugin.store.AppendMessageResult;
import com.taotao.cloud.mq.plugin.store.MessageExtBrokerInner;
import com.taotao.cloud.mq.plugin.store.PutMessageResult;
import com.taotao.cloud.mq.plugin.store.log.CommitLog;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 事务消息处理器（对标 RocketMQ TransactionMessageProcessor）.
 * <p>
 * 处理以下请求：
 * <ul>
 *   <li>{@link RequestCode#SEND_MESSAGE_V2} — 发送 Half 消息</li>
 *   <li>{@link RequestCode#END_TRANSACTION} — 事务二次确认（Commit/Rollback）</li>
 *   <li>{@link RequestCode#CHECK_TRANSACTION_STATE} — 事务回查</li>
 * </ul>
 */
public class TransactionMessageProcessor implements NettyRemotingAbstract.NettyRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(TransactionMessageProcessor.class);

    /** 半消息统一落在这条内部 topic 的 0 号队列上（见 {@link #HALF_QUEUE_ID} 的说明）。 */
    public static final String HALF_TOPIC = "TRANS_HALF_TOPIC";
    /** 已定论事务的 op 记录（提交/回滚各一条），重启时用它把半消息集合里的已定论条目摘掉。 */
    public static final String OP_TOPIC = "TRANS_OP_HALF_TOPIC";
    /**
     * 半消息与 op 记录都只写 0 号队列。理由两条：
     * 半消息不是给消费者读的（它对消费端不可见），落在哪条队列没有对外语义；
     * 而"重启后重建待回查集合"要能把这条 topic 一次扫干净 —— 队列号发散的代价是恢复时
     * 得先知道当初用了哪些队列，而那份信息在 {@code InMemoryQueueIndex} 里根本枚举不出来。
     * 业务队列号原样记在属性 {@link #PROP_ORIGINAL_QUEUE_ID} 里，提交回投时用它，顺序不丢。
     */
    public static final int HALF_QUEUE_ID = 0;

    public static final String PROP_TRANSACTION_ID = "TRANSACTION_ID";
    public static final String PROP_ORIGINAL_TOPIC = "ORIGINAL_TOPIC";
    public static final String PROP_ORIGINAL_QUEUE_ID = "ORIGINAL_QUEUE_ID";
    public static final String PROP_TRANSACTION_STATE = "TRANSACTION_STATE";

    private final BrokerController brokerController;
    private final TransactionStateManager transactionStateManager;

    public TransactionMessageProcessor(BrokerController brokerController) {
        this.brokerController = brokerController;
        this.transactionStateManager = new TransactionStateManager();
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        int code = request.getCode();
        switch (code) {
            case RequestCode.SEND_MESSAGE_V2:
                return processHalfMessage(ctx, request);
            case RequestCode.END_TRANSACTION:
                return processEndTransaction(ctx, request);
            case RequestCode.CHECK_TRANSACTION_STATE:
                return processCheckTransaction(ctx, request);
            default:
                RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR);
                response.setRemark("Unsupported request code: " + code);
                return response;
        }
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

    /**
     * 处理 Half 消息发送：业务 topic 原样留在属性里，存储上只落 {@link #HALF_TOPIC} 那一份。
     */
    private RemotingCommand processHalfMessage(ChannelHandlerContext ctx, RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String topic = request.getExtField("topic");
        String transactionId = request.getExtField("transactionId");
        String queueIdStr = request.getExtField("queueId");

        // 解码消息
        MessageExt inbound = decodeMessage(request);
        if (inbound == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("decode body failed");
            return response;
        }
        if (transactionId == null) {
            // 事务 ID 的两个真来源：消息自己的字段，或随消息一起编码进 properties 的那一份。
            // 请求扩展字段只是给"手工拼请求"的调用方留的口子，不能当成唯一入口。
            transactionId = inbound.getTransactionId();
            if (transactionId == null) {
                transactionId = inbound.getProperty(PROP_TRANSACTION_ID);
            }
        }
        if (topic == null) {
            topic = inbound.getTopic();
        }
        if (transactionId == null || topic == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("topic or transactionId missing");
            return response;
        }

        int originalQueueId = queueIdStr != null ? Integer.parseInt(queueIdStr) : 0;

        // 业务 topic 就是消息自己的 topic：半消息路径不许在到达 broker 之前把它改掉，
        // 所以这里读的是 inbound.getTopic()，而不是某个内部改写后的值。
        String originalTopic = inbound.getTopic();
        transactionStateManager.putHalfMessage(transactionId, originalTopic, originalQueueId, inbound);

        // 将 Half 消息写入 TRANS_HALF_TOPIC
        CommitLog commitLog = brokerController.getCommitLog();
        if (commitLog == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("commitLog not initialized");
            return response;
        }

        MessageExtBrokerInner inner = toInner(inbound, HALF_TOPIC, HALF_QUEUE_ID);
        inner.putProperty(PROP_TRANSACTION_ID, transactionId);
        inner.putProperty(PROP_ORIGINAL_TOPIC, originalTopic);
        inner.putProperty(PROP_ORIGINAL_QUEUE_ID, String.valueOf(originalQueueId));

        PutMessageResult result = commitLog.putMessage(inner);

        SendResult sendResult = new SendResult();
        if (result.isOk()) {
            sendResult.setSendStatus(SendStatus.SEND_OK);
            sendResult.setMsgId(inner.getMsgId());
            sendResult.setTopic(topic);
            sendResult.setQueueId(originalQueueId);
            AppendMessageResult amr = result.getAppendMessageResult();
            if (amr != null) {
                sendResult.setQueueOffset(inner.getQueueOffset());
            }
            // 半消息对消费端不可见，所以这里不唤醒长轮询：叫醒它的那一步在提交回投时。
        } else {
            // 半消息没落住 ⇒ 这条事务不存在。状态表里那一条必须撤掉，否则它会悬在待回查集合里，
            // 等着被回查到一个根本不存在的消息。
            transactionStateManager.rollbackTransaction(transactionId);
            sendResult.setSendStatus(SendStatus.SEND_FAILED);
            sendResult.setErrorMsg(result.getPutMessageStatus().name());
        }

        response.setBody(JsonCodec.encode(sendResult));
        return response;
    }

    /**
     * 处理事务二次确认（Commit/Rollback）。
     * <p>
     * COMMIT 的兑现是两件事，缺一件都不叫提交：
     * <ol>
     *   <li>把半消息按【原始 topic + 原始队列】重新投进存储，让消费者读得回来；</li>
     *   <li>落一条 op 记录，让这条事务在 broker 重启后不再回到待回查集合。</li>
     * </ol>
     * 回滚相反：只落 op 记录，消息永远不回投。
     */
    private RemotingCommand processEndTransaction(ChannelHandlerContext ctx, RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String transactionId = request.getExtField("transactionId");
        String stateStr = request.getExtField("transactionState");
        String originalTopic = request.getExtField("originalTopic");

        if (transactionId == null || stateStr == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("transactionId or state missing");
            return response;
        }

        TransactionState state;
        try {
            state = TransactionState.valueOf(stateStr);
        } catch (IllegalArgumentException e) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("unknown transaction state: " + stateStr);
            return response;
        }

        TransactionStateManager.ResolveOutcome outcome = resolveTransaction(transactionId, state);
        if (originalTopic != null) {
            TransactionStateManager.TransactionRecord record =
                    transactionStateManager.getTransaction(transactionId);
            if (record != null && !originalTopic.equals(record.getOriginalTopic())) {
                log.warn("originalTopic in request disagrees with the half record: transactionId={}"
                        + " request={} record={}", transactionId, originalTopic, record.getOriginalTopic());
            }
        }

        if (outcome == TransactionStateManager.ResolveOutcome.NOT_FOUND) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("transaction not found: transactionId=" + transactionId);
            return response;
        }
        if (outcome == TransactionStateManager.ResolveOutcome.DELIVERY_FAILED) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("commit rejected: re-delivery to original topic failed: transactionId="
                    + transactionId);
            return response;
        }
        response.setBody(JsonCodec.encode(outcome.name()));
        return response;
    }

    /**
     * 一个事务结论的全部副作用，只有这一处：状态改写 → （提交时）把消息按原始 topic + 原始队列回投 →
     * 落一条定论记录。二次确认请求与 broker 侧回查驱动走的都是这一个方法。
     * <p>
     * 幂等靠状态表里的 resolved 表：同一条事务第二次收到 COMMIT 不会投出第二份，
     * 收到 ROLLBACK 也不会把已提交的消息再动一遍。
     */
    public TransactionStateManager.ResolveOutcome resolveTransaction(
            String transactionId, TransactionState state) {
        switch (state) {
            case COMMIT: {
                TransactionStateManager.ResolveOutcome outcome =
                        transactionStateManager.commitTransaction(transactionId);
                if (outcome != TransactionStateManager.ResolveOutcome.COMMITTED) {
                    return outcome;
                }
                TransactionStateManager.TransactionRecord record =
                        transactionStateManager.getTransaction(transactionId);
                if (!reDeliverToOriginalTopic(record)) {
                    // 回投没落住 —— "提交过了"是个兑现不了的事实，撤掉结论让调用方重发（见该方法的说明）
                    transactionStateManager.forgetResolveForRetry(transactionId);
                    return TransactionStateManager.ResolveOutcome.DELIVERY_FAILED;
                }
                appendResolveRecord(transactionId, TransactionStateManager.TxState.COMMITTED);
                log.info("Transaction committed and re-delivered: transactionId={} topic={} queueId={}",
                        transactionId, record.getOriginalTopic(), record.getOriginalQueueId());
                return TransactionStateManager.ResolveOutcome.COMMITTED;
            }
            case ROLLBACK: {
                TransactionStateManager.ResolveOutcome outcome = transactionStateManager.rollbackTransaction(transactionId)
                        ? TransactionStateManager.ResolveOutcome.ROLLBACKED
                        : settledOutcome(transactionId);
                if (outcome == TransactionStateManager.ResolveOutcome.ROLLBACKED) {
                    appendResolveRecord(transactionId, TransactionStateManager.TxState.ROLLBACKED);
                    log.info("Transaction rolled back: transactionId={}", transactionId);
                }
                return outcome;
            }
            case UNKNOWN:
            default: {
                // 客户端答"还不知道"：这条继续悬在待回查集合里。这不是错误（响应 SUCCESS），
                // 也不是没找到 —— 区分开才有意义：调用方据此知道半消息确实还在 broker 上等结论。
                TransactionStateManager.TransactionRecord record =
                        transactionStateManager.getTransaction(transactionId);
                TransactionStateManager.ResolveOutcome outcome =
                        (record != null && record.isPending())
                                ? TransactionStateManager.ResolveOutcome.STILL_PENDING
                                : settledOutcome(transactionId);
                log.info("Transaction state unknown: transactionId={} outcome={}", transactionId, outcome);
                return outcome;
            }
        }
    }

    /** 已经定论过的事务，把结论翻译成 outcome（供 ROLLBACK/UNKNOWN 这条分支复用）。 */
    private TransactionStateManager.ResolveOutcome settledOutcome(String transactionId) {
        TransactionStateManager.TxState settled = transactionStateManager.getResolvedState(transactionId);
        if (settled == TransactionStateManager.TxState.COMMITTED) {
            return TransactionStateManager.ResolveOutcome.ALREADY_COMMITTED;
        }
        if (settled == TransactionStateManager.TxState.ROLLBACKED) {
            return TransactionStateManager.ResolveOutcome.ALREADY_ROLLBACKED;
        }
        return TransactionStateManager.ResolveOutcome.NOT_FOUND;
    }

    /**
     * 提交后的再投递：按原始 topic + 原始队列把这条消息真写进存储，让消费者读得回来。
     *
     * @return 是否落住
     */
    private boolean reDeliverToOriginalTopic(TransactionStateManager.TransactionRecord record) {
        CommitLog commitLog = brokerController.getCommitLog();
        Message half = record.getHalfMessage();
        if (commitLog == null || half == null) {
            return false;
        }
        MessageExtBrokerInner inner = toInner(half, record.getOriginalTopic(), record.getOriginalQueueId());
        PutMessageResult result = commitLog.putMessage(inner);
        boolean ok = result.isOk() || isAppendedToStore(result);
        if (ok) {
            // 与发送路径同一口径唤醒长轮询：消息已经能被 pull 读到，挂在原队列上的 pull 要立刻重读。
            PullRequestHoldService holdService =
                    brokerController.getPullRequestHoldService();
            if (holdService != null) {
                holdService.notifyMessageArrived(record.getOriginalTopic(), record.getOriginalQueueId());
            }
        }
        return ok;
    }

    /** 落一条 op 记录：这条事务已经定论，重启后重建待回查集合时要把它摘掉。 */
    private void appendResolveRecord(String transactionId, TransactionStateManager.TxState settled) {
        CommitLog commitLog = brokerController.getCommitLog();
        if (commitLog == null) {
            log.error("cannot append transaction op record without commitLog: transactionId={}", transactionId);
            return;
        }
        MessageExtBrokerInner inner = new MessageExtBrokerInner();
        inner.setTopic(OP_TOPIC);
        inner.setQueueId(HALF_QUEUE_ID);
        inner.setMsgId("OP-" + transactionId);
        inner.setBody(transactionId.getBytes(StandardCharsets.UTF_8));
        inner.setBornTimestamp(System.currentTimeMillis());
        inner.putProperty(PROP_TRANSACTION_ID, transactionId);
        inner.putProperty(PROP_TRANSACTION_STATE, settled.name());
        PutMessageResult result = commitLog.putMessage(inner);
        if (!result.isOk()) {
            log.error("transaction op record not stored: transactionId={} state={} status={}",
                    transactionId, settled, result.getPutMessageStatus());
        }
    }

    /**
     * 处理事务回查。
     * <p>
     * 两种模式，按请求里有没有 {@code transactionId} 分流：
     * <ul>
     *   <li><b>带 transactionId</b> = broker 侧回查驱动：回这条的半消息，并把回查计数推一格；
     *       次数用完直接判回滚（"超时未确认 ⇒ 自动回滚"这条语义就落在这里和
     *       {@link TransactionCheckService}）。</li>
     *   <li><b>不带 transactionId</b> = 生产者主动来取待回查清单（broker 结构上打不到 producer，
     *       所以"回查"的驱动方向只能是 producer → broker）。只读不计数：一次拉取不等于一次催办。</li>
     * </ul>
     */
    private RemotingCommand processCheckTransaction(ChannelHandlerContext ctx, RemotingCommand request) {
        RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        response.setOpaque(request.getOpaque());

        String transactionId = request.getExtField("transactionId");
        if (transactionId == null) {
            // 取待回查清单
            List<TransactionCheckTask> tasks = new ArrayList<>();
            for (TransactionStateManager.TransactionRecord record :
                    transactionStateManager.listAllPendingTransactions()) {
                tasks.add(new TransactionCheckTask(record.getTransactionId(), record.getOriginalTopic(),
                        record.getOriginalQueueId(), msgIdOf(record.getHalfMessage()),
                        record.getHalfMessage(), record.getCheckTimes(), record.getCreateTimestamp()));
            }
            response.setBody(JsonCodec.encode(tasks));
            return response;
        }

        TransactionStateManager.TransactionRecord record = transactionStateManager.getTransaction(transactionId);
        if (record == null) {
            response.setCode(RemotingSysResponseCode.SYSTEM_ERROR);
            response.setRemark("Transaction not found: " + transactionId);
            return response;
        }

        // 已经定论过的不必再回查，直接把结论回给调用方
        TransactionStateManager.TxState settled = transactionStateManager.getResolvedState(transactionId);
        if (settled != null) {
            response.setBody(JsonCodec.encode(settled.name()));
            return response;
        }

        // 检查回查次数
        if (record.getCheckTimes() >= transactionStateManager.getMaxCheckTimes()) {
            // 超过最大回查次数，自动回滚（回滚的全部副作用与二次确认那条路径共用一个出口）
            TransactionStateManager.ResolveOutcome forced =
                    resolveTransaction(transactionId, TransactionState.ROLLBACK);
            log.warn("Transaction max check times reached, forced to {}: transactionId={}",
                    forced, transactionId);
            response.setBody(JsonCodec.encode("ROLLBACK"));
            return response;
        }

        // 更新回查状态
        transactionStateManager.markChecked(transactionId);

        // 返回 Half 消息给客户端进行回查
        response.setBody(JsonCodec.encode(record.getHalfMessage()));
        return response;
    }

    /**
     * broker 重启后从存储重建待回查集合（实现在
     * {@link TransactionStateRecovery}）。
     */
    public TransactionStateManager getTransactionStateManager() {
        return transactionStateManager;
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

    /** msgId 只在 MessageExt 上有；裸 Message 没有，此时回 null 而不是编一个。 */
    private static String msgIdOf(Message msg) {
        return msg instanceof MessageExt ? ((MessageExt) msg).getMsgId() : null;
    }

    /** 把一条消息按指定 topic/queueId 复制成"要写进存储"的那一份。 */
    private MessageExtBrokerInner toInner(Message src, String topic, int queueId) {
        MessageExtBrokerInner dst = new MessageExtBrokerInner();
        dst.setTopic(topic);
        dst.setTags(src.getTags());
        dst.setKeys(src.getKeys());
        dst.setBody(src.getBody());
        dst.setFlag(src.getFlag());
        dst.setProperties(src.getProperties() == null
                ? new java.util.HashMap<String, String>() : new java.util.HashMap<String, String>(src.getProperties()));
        dst.setQueueId(queueId);
        // msgId / bornTimestamp / 重投次数是 MessageExt 上的字段（Message 没有）：半消息两条来源
        // （请求里解出来的、从存储读回来的）都是 MessageExt，走这一支原样带走；
        // 只有手工构造的裸 Message 才落到另一支，那时出生时间就是"现在"。
        if (src instanceof MessageExt) {
            MessageExt ext = (MessageExt) src;
            dst.setBornTimestamp(ext.getBornTimestamp());
            dst.setQueueOffset(ext.getQueueOffset());
            dst.setMsgId(ext.getMsgId());
            dst.setReconsumeTimes(ext.getReconsumeTimes());
            dst.setPreparedTransactionOffset(ext.getPreparedTransactionOffset());
        } else {
            dst.setBornTimestamp(System.currentTimeMillis());
        }
        StringBuilder props = new StringBuilder();
        for (Map.Entry<String, String> e : dst.getProperties().entrySet()) {
            props.append(e.getKey()).append("=").append(e.getValue()).append("\n");
        }
        dst.setPropertiesString(props.toString());
        return dst;
    }

    /** 记录是否已经 append 进 CommitLog（与"同步刷盘是否超时"是两件事），口径同 SendMessageProcessor。 */
    private static boolean isAppendedToStore(PutMessageResult result) {
        AppendMessageResult amr = result.getAppendMessageResult();
        return amr != null && amr.getStatus() == AppendMessageResult.AppendMessageStatus.PUT_OK;
    }
}
