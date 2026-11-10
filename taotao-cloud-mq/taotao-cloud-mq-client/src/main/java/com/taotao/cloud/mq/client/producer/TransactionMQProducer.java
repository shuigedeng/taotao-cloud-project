package com.taotao.cloud.mq.client.producer;

import com.taotao.cloud.mq.client.MQClientInstance;
import com.taotao.cloud.mq.common.enums.TransactionCheckTask;
import com.taotao.cloud.mq.common.enums.TransactionState;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.remoting.exception.RemotingTimeoutException;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事务消息生产者（对标 RocketMQ TransactionMQProducer）.
 * <p>
 * 支持两阶段事务消息：
 * <ol>
 *   <li>发送 Half 消息到 Broker（消息暂不可见）</li>
 *   <li>执行本地事务</li>
 *   <li>根据本地事务结果发送 Commit 或 Rollback</li>
 * </ol>
 * <p>
 * 使用示例：
 * <pre>
 * TransactionMQProducer producer = new TransactionMQProducer("tx-group");
 * producer.setNamesrvAddr("localhost:9876");
 * producer.setTransactionListener(new MyTransactionListener());
 * producer.start();
 *
 * Message msg = new Message("topic", "tag", "key", body);
 * producer.sendMessageInTransaction(msg, null);
 * </pre>
 */
public class TransactionMQProducer extends DefaultMQProducer {

    private static final Logger log = LoggerFactory.getLogger(TransactionMQProducer.class);

    private TransactionListener transactionListener;
    private long checkMaxTimes = 15;
    private long checkIntervalMillis = 60_000L;

    /** 本地事务执行状态缓存: transactionId -> TransactionState */
    private final ConcurrentHashMap<String, TransactionState> localTransactionStateTable = new ConcurrentHashMap<>();

    /** Half 消息缓存: transactionId -> Message */
    private final ConcurrentHashMap<String, Message> halfMessageTable = new ConcurrentHashMap<>();

    /** 半消息落在哪台 broker 上: transactionId -> brokerAddr（二次确认必须发回同一台） */
    private final ConcurrentHashMap<String, String> halfBrokerAddrTable = new ConcurrentHashMap<>();

    public TransactionMQProducer(String producerGroup) {
        super(producerGroup);
    }

    public TransactionMQProducer(String producerGroup, NettyClientConfig nettyClientConfig) {
        super(producerGroup, nettyClientConfig);
    }

    /**
     * 发送事务消息（两阶段）。
     * <p>
     * prepare 阶段走 {@link RequestCode#SEND_MESSAGE_V2}（broker 侧由事务处理器落成半消息），
     * 业务 topic 全程不改：半消息落到哪个队列由 broker 从请求里读到并记住，提交时原样回投。
     *
     * @param message 待发送的消息
     * @param arg     传递给 TransactionListener.executeLocalTransaction 的参数
     * @return 发送结果
     * @throws Exception 发送异常；二次确认没送到时也抛（半消息已经在 broker 上，调用方可以用
     *                   {@link #endTransaction} 补确认）
     */
    public SendResult sendMessageInTransaction(Message message, Object arg) throws Exception {
        if (transactionListener == null) {
            throw new IllegalStateException("TransactionListener not set");
        }

        // 生成事务 ID
        String transactionId = UUID.randomUUID().toString();
        message.putProperty("TRANSACTION_ID", transactionId);
        message.setTransactionId(transactionId);

        // 1. 发送 Half 消息到 Broker（业务 topic 保持原样，只换请求码）
        String originalTopic = message.getTopic();
        PreparedSend half = prepareMessageSend(message, RequestCode.SEND_MESSAGE_V2);
        String halfBrokerAddr = brokerAddrOf(half);

        SendResult sendResult = executePreparedSend(half);
        if (sendResult == null || sendResult.getSendStatus() != com.taotao.cloud.mq.common.protocol.SendStatus.SEND_OK) {
            log.error("Send half message failed: transactionId={}", transactionId);
            return sendResult;
        }

        // 2. 缓存 Half 消息
        halfMessageTable.put(transactionId, message);
        halfBrokerAddrTable.put(transactionId, halfBrokerAddr);

        // 3. 执行本地事务
        TransactionState localState;
        try {
            localState = transactionListener.executeLocalTransaction(message, arg);
        } catch (Exception e) {
            log.error("Execute local transaction failed: transactionId={}", transactionId, e);
            localState = TransactionState.UNKNOWN;
        }
        if (localState == null) {
            // listener 返回 null 不是"提交"，按未决处理，留给回查
            localState = TransactionState.UNKNOWN;
        }

        // 4. 缓存本地事务状态
        localTransactionStateTable.put(transactionId, localState);

        // 5. 发送二次确认到 Broker
        sendTransactionCommit(transactionId, originalTopic, localState, halfBrokerAddr);

        return sendResult;
    }

    /**
     * 事后补二次确认：{@link #sendMessageInTransaction} 在"半消息已落住、确认没送到"时把决定权交回
     * 调用方，调用方随时可以用同一个 transactionId 重发确认（broker 侧确认是幂等的）。
     */
    public void endTransaction(String transactionId, String originalTopic, TransactionState state)
            throws Exception {
        String brokerAddr = halfBrokerAddrTable.get(transactionId);
        if (brokerAddr == null) {
            brokerAddr = selectBrokerAddr();
        }
        sendTransactionCommit(transactionId, originalTopic, state, brokerAddr);
    }

    /**
     * 发送事务二次确认（Commit/Rollback）到 Broker。
     * <p>
     * 失败一律抛出去：这一条确认没落地就意味着"半消息会一直挂在待回查集合里，最终被自动回滚"，
     * 调用方必须知道这件事，不能只留一行 warn。
     */
    private void sendTransactionCommit(String transactionId, String originalTopic, TransactionState state,
                                       String brokerAddr) throws Exception {
        MQClientInstance instance = mqClientInstance;
        if (instance == null) {
            throw new IllegalStateException("MQClientInstance not initialized, transaction commit not sent:"
                    + " transactionId=" + transactionId);
        }
        if (brokerAddr == null) {
            throw new RemotingSendRequestException("No broker addr for transaction commit: transactionId="
                    + transactionId);
        }

        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.END_TRANSACTION);
        request.addExtField("transactionId", transactionId);
        request.addExtField("originalTopic", originalTopic);
        request.addExtField("transactionState", state.name());

        RemotingCommand response;
        try {
            response = instance.invokeSync(brokerAddr, request, 3000);
        } catch (RemotingTimeoutException | RemotingSendRequestException e) {
            throw new RemotingSendRequestException("Send transaction commit failed: transactionId="
                    + transactionId + " brokerAddr=" + brokerAddr + " cause=" + e.getMessage());
        }
        if (response == null) {
            throw new RemotingSendRequestException("No response for transaction commit: transactionId="
                    + transactionId + " brokerAddr=" + brokerAddr);
        }
        if (response.getCode() != RemotingSysResponseCode.SUCCESS) {
            throw new RemotingSendRequestException("Broker rejected transaction commit: transactionId="
                    + transactionId + " code=" + response.getCode() + " remark=" + response.getRemark());
        }
        log.info("Transaction commit sent: transactionId={} state={} brokerAddr={}", transactionId, state, brokerAddr);
    }

    /**
     * 主动拉一次待回查事务并按本地事务状态作答（回查在生产者这一侧驱动）。
     * <p>
     * broker 结构上没有到 producer 的通道（client 侧不监听任何端口），所以"回查"能兑现的形状是
     * 生产者主动来取：请求 {@link RequestCode#CHECK_TRANSACTION_STATE} 不带 transactionId 时，
     * broker 回一批待回查快照；本方法对每一条问 {@link TransactionListener#checkLocalTransaction}，
     * 拿到确定状态就把二次确认发回去。
     *
     * @return 本轮实际作答（发出二次确认）的事务条数
     */
    public int checkPendingTransactions() throws Exception {
        return checkPendingTransactions(null);
    }

    /**
     * @param brokerAddrHint 明确指定问哪台 broker；不传时用"本进程写过半消息的那台"，
     *                       再退回按 {@code TRANS_HALF_TOPIC} 的路由查（这条 topic 没有在 nameserver
     *                       建过配置时查不到，所以 hint 这一档是必须的，不是可选的便利参数）。
     */
    public int checkPendingTransactions(String brokerAddrHint) throws Exception {
        MQClientInstance instance = mqClientInstance;
        if (instance == null) {
            throw new IllegalStateException("producer not started");
        }
        String brokerAddr = brokerAddrHint;
        if (brokerAddr == null && !halfBrokerAddrTable.isEmpty()) {
            brokerAddr = halfBrokerAddrTable.values().iterator().next();
        }
        if (brokerAddr == null) {
            brokerAddr = selectBrokerAddr();
        }
        if (brokerAddr == null) {
            throw new RemotingSendRequestException("No broker addr for transaction check");
        }

        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.CHECK_TRANSACTION_STATE);
        RemotingCommand response = instance.invokeSync(brokerAddr, request, 3000);
        if (response == null) {
            throw new RemotingSendRequestException("No response for transaction check, brokerAddr=" + brokerAddr);
        }
        if (response.getCode() != RemotingSysResponseCode.SUCCESS) {
            throw new RemotingSendRequestException("Broker rejected transaction check: code="
                    + response.getCode() + " remark=" + response.getRemark());
        }
        byte[] body = response.getBody();
        if (body == null || body.length == 0) {
            return 0;
        }
        List<TransactionCheckTask> tasks = JsonCodec.decodeList(body, TransactionCheckTask.class);
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }
        int answered = 0;
        for (TransactionCheckTask task : tasks) {
            if (task == null || task.getTransactionId() == null) {
                continue;
            }
            String txId = task.getTransactionId();
            TransactionState state = checkTransaction(txId);
            if (state == TransactionState.UNKNOWN) {
                // 本地事务还没结论：不作答，留给下一轮回查（broker 侧到次数会自动回滚）
                log.info("Transaction check still UNKNOWN, leave it pending: transactionId={}", txId);
                continue;
            }
            endTransaction(txId, task.getOriginalTopic(), state);
            answered++;
        }
        return answered;
    }

    /**
     * 处理 Broker 回查（由 MQClientInstance 调用）。
     *
     * @param transactionId 事务 ID
     * @return 事务状态
     */
    public TransactionState checkTransaction(String transactionId) {
        // 先检查本地缓存
        TransactionState cachedState = localTransactionStateTable.get(transactionId);
        if (cachedState != null && cachedState != TransactionState.UNKNOWN) {
            return cachedState;
        }

        // 调用 listener 回查
        Message halfMessage = halfMessageTable.get(transactionId);
        if (halfMessage == null) {
            log.warn("Half message not found for check: transactionId={}", transactionId);
            return TransactionState.ROLLBACK;
        }

        try {
            TransactionState state = transactionListener.checkLocalTransaction(halfMessage);
            if (state != null && state != TransactionState.UNKNOWN) {
                localTransactionStateTable.put(transactionId, state);
                return state;
            }
            return TransactionState.UNKNOWN;
        } catch (Exception e) {
            log.error("Check local transaction failed: transactionId={}", transactionId, e);
            return TransactionState.UNKNOWN;
        }
    }

    /**
     * 清理已完成的事务缓存。
     */
    public void clearTransactionCache(String transactionId) {
        localTransactionStateTable.remove(transactionId);
        halfMessageTable.remove(transactionId);
        halfBrokerAddrTable.remove(transactionId);
    }

    private String selectBrokerAddr() {
        // 简单实现：从 NameServer 获取 Broker 地址
        if (mqClientInstance != null) {
            try {
                com.taotao.cloud.mq.common.TopicRouteData routeData = mqClientInstance.getTopicRouteData("TRANS_HALF_TOPIC");
                if (routeData != null && routeData.getBrokerDatas() != null && !routeData.getBrokerDatas().isEmpty()) {
                    return routeData.getBrokerDatas().get(0).selectBrokerAddr();
                }
            } catch (Exception e) {
                log.debug("Get broker addr failed: {}", e.getMessage());
            }
        }
        return null;
    }

    // ==================== Getters and Setters ====================

    public TransactionListener getTransactionListener() {
        return transactionListener;
    }

    public void setTransactionListener(TransactionListener transactionListener) {
        this.transactionListener = transactionListener;
    }

    public long getCheckMaxTimes() {
        return checkMaxTimes;
    }

    public void setCheckMaxTimes(long checkMaxTimes) {
        this.checkMaxTimes = checkMaxTimes;
    }

    public long getCheckIntervalMillis() {
        return checkIntervalMillis;
    }

    public void setCheckIntervalMillis(long checkIntervalMillis) {
        this.checkIntervalMillis = checkIntervalMillis;
    }
}
