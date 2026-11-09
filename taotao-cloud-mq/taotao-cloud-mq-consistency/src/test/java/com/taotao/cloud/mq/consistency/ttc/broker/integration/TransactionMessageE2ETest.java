package com.taotao.cloud.mq.consistency.ttc.broker.integration;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerConfig;
import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.broker.processor.PullMessageProcessor;
import com.taotao.cloud.mq.consistency.ttc.broker.processor.TransactionMessageProcessor;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.ConsumeConcurrentlyStatus;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.DefaultMQPushConsumer;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.MessageListener;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.MessageQueueContext;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.retry.ConsumeRetryService;
import com.taotao.cloud.mq.consistency.ttc.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionListener;
import com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionMQProducer;
import com.taotao.cloud.mq.consistency.ttc.client.producer.TransactionState;
import com.taotao.cloud.mq.consistency.ttc.common.message.Message;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.PullResultPayload;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendStatus;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerController;
import com.taotao.cloud.mq.consistency.ttc.nameserver.NamesrvConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import com.taotao.cloud.mq.consistency.ttc.store.log.FlushDiskType;
import io.netty.channel.Channel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * 「事务消息（两阶段提交）」这句话在真请求路径上的兑现：
 * 半消息在第二阶段之前对消费端不可见，提交后消费者从<b>原始 topic</b> 真把那一条读回来，
 * 回滚后永远读不回来，而且同一条消息不许既以原始 topic 又以半消息 topic 各落一份。
 * <p>
 * 四条用例各钉一条边，缺一条就是那句话没兑现：
 * <ol>
 *   <li>{@link #committedTransactionIsReadBackByAConsumerOnTheOriginalTopic()} —— 提交后的再投递。
 *       判据是"一个真订阅了原始 topic 的 push consumer 读到了那条 msgId"，
 *       不是"某个内存表里状态改成了 COMMITTED"。</li>
 *   <li>{@link #halfMessageIsInvisibleUntilTheSecondPhaseAndAppearsExactlyOnceAfterIt()} —— 两阶段的
 *       "阶段"这件事本身：答 UNKNOWN 时原始 topic 上 0 条、半消息 topic 上正好 1 条；补一次 COMMIT
 *       之后原始 topic 上<b>正好 1 条</b>（"正好"这一半就是双投的照门）。</li>
 *   <li>{@link #rolledBackTransactionIsNeverReadable()} —— 回滚侧：回滚之后原始 topic 永远 0 条，
 *       哪怕事后又补一次 COMMIT 也不许投出来。</li>
 *   <li>{@link #ordinarySendIsNotDivertedByTheTransactionRegistration()} —— 把 201 绑给事务处理器
 *       之后，普通发送（200）一条都不许改道成半消息；同一次运行里带一个"半消息计数真的会涨"的
 *       阳性对照，证明这把尺看得见半消息那一路。</li>
 * </ol>
 * 等待一律因果：二次确认是同步 RPC，{@code sendMessageInTransaction} 返回那一刻 broker 的结论已经落完，
 * 所以"存储里有一条没有"是直接断言，不需要等；只有消费者那一路是异步的，等的是"某条 msgId 被投到"
 * 这个事件本身，时间只用来给"根本没发生"一个可读的失败。
 */
public class TransactionMessageE2ETest {

    /** 一次异步投递的等待上限；只用于给出「这件事没发生」的可读失败，不承载任何数值判定. */
    private static final long BAIL_OUT_MILLIS = 90_000L;

    private static final String BROKER_NAME = "TxMessageBroker";
    private static final String CLUSTER_NAME = "TxMessageCluster";
    private static final String TOPIC = "TxMessageTopic";
    private static final String GROUP = "tx-message-group";
    private static final int QUEUE_ID = 0;

    @TempDir
    Path tempDir;

    private NameServerController nameServer;
    private String namesrvAddr;
    private String brokerAddr;
    private final List<BrokerController> launchedBrokers = new ArrayList<BrokerController>();
    private final List<DefaultMQPushConsumer> launchedConsumers = new ArrayList<DefaultMQPushConsumer>();
    private final List<NettyRemotingClient> clients = new ArrayList<NettyRemotingClient>();
    private NettyRemotingClient wireClient;

    @AfterEach
    public void tearDown() {
        for (DefaultMQPushConsumer c : launchedConsumers) {
            try {
                c.shutdown();
            } catch (Exception ignore) {
                // 用例里已经关过的，收尸时忽略
            }
        }
        launchedConsumers.clear();
        for (NettyRemotingClient c : clients) {
            try {
                c.shutdown();
            } catch (Exception ignore) {
                // 同上
            }
        }
        clients.clear();
        for (BrokerController b : launchedBrokers) {
            try {
                b.shutdown();
            } catch (Exception ignore) {
                // 同上
            }
        }
        launchedBrokers.clear();
        if (nameServer != null) {
            nameServer.shutdown();
            nameServer = null;
        }
        deleteRecursively(tempDir.toFile());
    }

    // ==================== 1. 提交后消费者真读得回来（本支核心缺口） ====================

    @Test
    @DisplayName("提交后：真消费者从原始 topic 读到了那条被提交的消息")
    public void committedTransactionIsReadBackByAConsumerOnTheOriginalTopic() throws Exception {
        startCluster();
        registerTopic(TOPIC);
        BrokerController broker = lastBroker();

        // 消费者在提交之前就订阅好：否则"读到了"可能只是消费端自己的首次拉取赶上了
        Recorder recorder = new Recorder();
        startConsumer(GROUP, TOPIC, recorder);

        String body = "tx-committed-payload";
        SendResult sent = newProducer(TransactionState.COMMIT, null)
                .sendMessageInTransaction(message(body), null);
        assertEquals(SendStatus.SEND_OK, sent.getSendStatus(), "半消息必须被 broker 确认");
        String committedMsgId = sent.getMsgId();
        assertNotNull(committedMsgId, "半消息的确认里要带 msgId，否则这一路无从比对");

        // 判据一：真消费者读到了那一条（不是某个内存计数加了一）
        MessageExt arrived = recorder.awaitMsgId(committedMsgId,
                "提交之后消费者必须从原始 topic 读到那条消息");
        assertEquals(body, new String(arrived.getBody(), StandardCharsets.UTF_8), "消费者读到的正文");
        assertEquals(TOPIC, arrived.getTopic(), "消费者是从原始 topic 读到它的");

        // 判据二：原始 topic 上正好一条（双投会被这一条抓住）
        List<MessageExt> onOriginal = pullFrom(broker, TOPIC, QUEUE_ID);
        assertEquals(1, countWithMsgId(onOriginal, committedMsgId),
                "提交后原始 topic 上这条消息必须正好一份, 实际整个队列: " + describe(onOriginal));
    }

    // ==================== 2. 两阶段：阶段之前不可见，之后正好一份 ====================

    @Test
    @DisplayName("本地事务答 UNKNOWN 时消费端读不到；补一次 COMMIT 后正好一份（半消息不双投）")
    public void halfMessageIsInvisibleUntilTheSecondPhaseAndAppearsExactlyOnceAfterIt() throws Exception {
        startCluster();
        registerTopic(TOPIC);
        BrokerController broker = lastBroker();

        Message msg = message("tx-unknown-then-commit");
        TransactionMQProducer producer = newProducer(TransactionState.UNKNOWN, null);
        SendResult sent = producer.sendMessageInTransaction(msg, null);
        assertEquals(SendStatus.SEND_OK, sent.getSendStatus());
        String txId = msg.getProperty("TRANSACTION_ID");
        assertNotNull(txId, "事务 ID 必须跟着消息走到 broker（客户端不设它也认得）");

        // 第二阶段没结论 ⇒ 原始 topic 上必须一条都没有；半消息 topic 上必须正好一条
        assertEquals(0, countWithMsgId(pullFrom(broker, TOPIC, QUEUE_ID), sent.getMsgId()),
                "半消息阶段这条消息对消费端必须不可见, 原始 topic 上不该有任何一份");
        List<MessageExt> halves = pullFrom(broker, TransactionMessageProcessor.HALF_TOPIC,
                TransactionMessageProcessor.HALF_QUEUE_ID);
        assertEquals(1, countWithMsgId(halves, sent.getMsgId()),
                "半消息必须正好落一份在 TRANS_HALF_TOPIC（同一条消息不得既以原始 topic 又以 half topic 落两次）");

        // 补二次确认：同一条事务的结论现在才落地
        producer.endTransaction(txId, TOPIC, TransactionState.COMMIT);
        List<MessageExt> afterCommit = pullFrom(broker, TOPIC, QUEUE_ID);
        assertEquals(1, countWithMsgId(afterCommit, sent.getMsgId()),
                "补提交之后原始 topic 上必须正好一份, 实际: " + describe(afterCommit));
        // 幂等：再补一次同一结论，不许投出第二份
        producer.endTransaction(txId, TOPIC, TransactionState.COMMIT);
        assertEquals(1, countWithMsgId(pullFrom(broker, TOPIC, QUEUE_ID), sent.getMsgId()),
                "同一个事务重复提交必须幂等（第二份就是双投）");
    }

    // ==================== 3. 回滚侧 ====================

    @Test
    @DisplayName("回滚之后永远收不到：事后补一次 COMMIT 也不许把它投出来")
    public void rolledBackTransactionIsNeverReadable() throws Exception {
        startCluster();
        registerTopic(TOPIC);
        BrokerController broker = lastBroker();

        Recorder recorder = new Recorder();
        startConsumer(GROUP, TOPIC, recorder);

        Message msg = message("tx-rolled-back");
        TransactionMQProducer producer = newProducer(TransactionState.ROLLBACK, null);
        SendResult sent = producer.sendMessageInTransaction(msg, null);
        assertEquals(SendStatus.SEND_OK, sent.getSendStatus(), "半消息本身是送到的（回滚的是结论，不是发送）");
        String txId = msg.getProperty("TRANSACTION_ID");

        assertEquals(0, countWithMsgId(pullFrom(broker, TOPIC, QUEUE_ID), sent.getMsgId()),
                "回滚之后原始 topic 上一条都不许有");
        // 事后想反悔也不行：回滚已经定论
        producer.endTransaction(txId, TOPIC, TransactionState.COMMIT);
        assertEquals(0, countWithMsgId(pullFrom(broker, TOPIC, QUEUE_ID), sent.getMsgId()),
                "已回滚的事务被事后补提交时投出了消息 ⇒ 回滚这句话不成立");
        assertTrue(recorder.all.isEmpty(),
                "消费者一条都不该收到, 实际收到: " + recorder.describe(recorder.all));
    }

    // ==================== 4. 接线不改道：普通发送不会被当成半消息 ====================

    @Test
    @DisplayName("201 绑给事务处理器之后：普通发送一条都没被改道成半消息")
    public void ordinarySendIsNotDivertedByTheTransactionRegistration() throws Exception {
        startCluster();
        registerTopic(TOPIC);
        BrokerController broker = lastBroker();
        int halfBefore = pullFrom(broker, TransactionMessageProcessor.HALF_TOPIC,
                TransactionMessageProcessor.HALF_QUEUE_ID).size();

        DefaultMQProducer plain = new DefaultMQProducer("plain-group", new NettyClientConfig());
        plain.setNamesrvAddr(namesrvAddr);
        plain.start();
        try {
            Message msg = message("plain-send-not-a-half");
            SendResult sent = plain.send(msg);
            assertEquals(SendStatus.SEND_OK, sent.getSendStatus());
            assertEquals(1, countWithMsgId(pullFrom(broker, TOPIC, QUEUE_ID), sent.getMsgId()),
                    "普通发送必须落在原始 topic 上");
            List<MessageExt> halves = pullFrom(broker, TransactionMessageProcessor.HALF_TOPIC,
                    TransactionMessageProcessor.HALF_QUEUE_ID);
            assertEquals(halfBefore, halves.size(),
                    "普通发送一条都不许进半消息 topic（改道就是这条红）, 实际: " + describe(halves));
            assertEquals(0, countWithMsgId(halves, sent.getMsgId()), "普通发送的 msgId 不许出现在半消息 topic");

            // 同一把尺的阳性对照：真发一条 201，半消息计数必须涨 —— 证明上面那条 0 不是尺瞎
            RemotingCommand halfReq = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE_V2);
            MessageExt ext = new MessageExt();
            ext.setTopic(TOPIC);
            ext.setMsgId("TXRAW-1");
            ext.setBody("raw-201-half".getBytes(StandardCharsets.UTF_8));
            ext.setBornTimestamp(System.currentTimeMillis());
            ext.setTransactionId("tx-raw-201");
            halfReq.addExtField("topic", TOPIC);
            halfReq.addExtField("queueId", String.valueOf(QUEUE_ID));
            halfReq.setBody(JsonCodec.encode(ext));
            RemotingCommand halfResp = askBroker(halfReq);
            assertEquals(RemotingSysResponseCode.SUCCESS, halfResp.getCode(),
                    "201 必须由事务处理器接走并回 SEND_OK, remark=" + halfResp.getRemark());
            assertEquals(halfBefore + 1, pullFrom(broker, TransactionMessageProcessor.HALF_TOPIC,
                            TransactionMessageProcessor.HALF_QUEUE_ID).size(),
                    "阳性对照：真半消息必须让那个计数加一（上一条 0 才不是空跑）");
            assertEquals(0, countWithMsgId(pullFrom(broker, TOPIC, QUEUE_ID), "TXRAW-1"),
                    "半消息不许提前出现在原始 topic 上");
        } finally {
            plain.shutdown();
        }
    }

    // ==================== 5. 结构守卫：三个事务码真被接走了（含阳性对照） ====================

    @Test
    @DisplayName("机检：251/250/201 在 broker 上有处理器；未注册的码必须仍被判成不支持")
    public void transactionRequestCodesAreRoutedAndUnboundCodesStillAreNot() throws Exception {
        startCluster();

        // 三条事务码：今天的形状是 REQUEST_CODE_NOT_SUPPORTED，接上之后必须不再是那个码
        int[] bound = new int[] {RequestCode.END_TRANSACTION, RequestCode.CHECK_TRANSACTION_STATE,
                RequestCode.SEND_MESSAGE_V2};
        for (int code : bound) {
            RemotingCommand resp = askBroker(RemotingCommand.createRequestCommand(code));
            assertTrue(resp.getCode() != RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED,
                    "请求码 " + code + " 必须有处理器（今天实测是 REQUEST_CODE_NOT_SUPPORTED）, 实际 code="
                            + resp.getCode() + " remark=" + resp.getRemark());
        }

        // 阳性对照：这把尺必须能说出"不支持"，否则上面那三条 0 命中说明不了任何事。
        // SEND_BATCH_MESSAGE(202) 今天确实没有注册（见 registerProcessor 全量），它必须仍是 NOT_SUPPORTED。
        RemotingCommand unbound = askBroker(RemotingCommand.createRequestCommand(RequestCode.SEND_BATCH_MESSAGE));
        assertEquals(RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED, unbound.getCode(),
                "阳性对照失败：一个确实没注册的码也被判成支持 ⇒ 上面那三条断言是空跑");
    }

    // ==================== 集群与真链路 ====================

    private void startCluster() throws Exception {
        int nsPort = freePort();
        NamesrvConfig nsCfg = new NamesrvConfig();
        nsCfg.setKvConfigPath(tempDir.resolve("kvConfig.json").toString());
        NettyServerConfig nsNetty = new NettyServerConfig();
        nsNetty.setListenPort(nsPort);
        nameServer = new NameServerController(nsCfg, nsNetty);
        assertTrue(nameServer.initialize(), "NameServer.initialize() 必须成功");
        nameServer.start();
        namesrvAddr = "127.0.0.1:" + nsPort;
        startBrokerOnSameStore();
    }

    private void startBrokerOnSameStore() throws Exception {
        MessageStoreConfig msc = new MessageStoreConfig();
        String root = tempDir.resolve("broker").toString();
        msc.setStorePathRootDir(root);
        msc.setStorePathCommitLog(root + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        msc.setFlushDiskType(FlushDiskType.SYNC_FLUSH);

        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName(BROKER_NAME);
        bc.setBrokerClusterName(CLUSTER_NAME);
        bc.setNamesrvAddr(namesrvAddr);

        NettyServerConfig nsc = new NettyServerConfig();
        int port = freePort();
        nsc.setListenPort(port);

        BrokerController broker = new BrokerController(bc, msc, nsc);
        assertTrue(broker.initialize(), "BrokerController.initialize() 必须成功（真 load）");
        broker.start();
        launchedBrokers.add(broker);
        brokerAddr = "127.0.0.1:" + port;
    }

    private BrokerController lastBroker() {
        return launchedBrokers.get(launchedBrokers.size() - 1);
    }

    private TransactionMQProducer newProducer(TransactionState localState, TransactionState checkState)
            throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("tx-producer-" + System.nanoTime(),
                new NettyClientConfig());
        producer.setNamesrvAddr(namesrvAddr);
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public TransactionState executeLocalTransaction(Message half, Object arg) {
                return localState;
            }

            @Override
            public TransactionState checkLocalTransaction(Message half) {
                return checkState == null ? localState : checkState;
            }
        });
        producer.start();
        return producer;
    }

    private DefaultMQPushConsumer startConsumer(String group, String topic, Recorder recorder) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setPullIntervalMillis(20L);
        consumer.setRetryCheckIntervalMillis(5L);
        consumer.setRetryDelayStrategy(new ConsumeRetryService.RetryDelayStrategy() {
            @Override
            public long nextDelayMillis(int reconsumeTimes) {
                return 0L;
            }
        });
        consumer.subscribe(topic, recorder);
        consumer.start();
        launchedConsumers.add(consumer);
        return consumer;
    }

    private Message message(String body) {
        Message message = new Message();
        message.setTopic(TOPIC);
        message.setTags("TX");
        message.setKeys("tx-key");
        message.setBody(body.getBytes(StandardCharsets.UTF_8));
        return message;
    }

    /** 走真读路径（{@link PullMessageProcessor}）把某个队列读回来。 */
    private List<MessageExt> pullFrom(BrokerController broker, String topic, int queueId) throws Exception {
        PullMessageProcessor processor = new PullMessageProcessor(broker);
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        req.addExtField("topic", topic);
        req.addExtField("queueId", String.valueOf(queueId));
        req.addExtField("offset", "0");
        req.addExtField("maxNum", "1000");
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(),
                "读 topic=" + topic + "@" + queueId + " 必须应答, remark=" + resp.getRemark());
        PullResultPayload payload = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        if (payload == null || payload.getMessages() == null) {
            return new ArrayList<MessageExt>();
        }
        return payload.getMessages();
    }

    private static int countWithMsgId(List<MessageExt> messages, String msgId) {
        int n = 0;
        for (MessageExt m : messages) {
            if (msgId != null && msgId.equals(m.getMsgId())) {
                n++;
            }
        }
        return n;
    }

    private static String describe(List<MessageExt> messages) {
        StringBuilder sb = new StringBuilder();
        for (MessageExt m : messages) {
            sb.append(m.getTopic()).append('@').append(m.getQueueId()).append('/')
                    .append(m.getQueueOffset()).append("[").append(m.getMsgId()).append("] ");
        }
        return sb.toString();
    }

    // ==================== 走真 remoting 通道 ====================

    private void registerTopic(String topic) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
        req.addExtField("topic", topic);
        req.addExtField("readQueueNums", "1");
        req.addExtField("writeQueueNums", "1");
        RemotingCommand resp = askNameserver(req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(),
                "注册 topic " + topic + " 必须成功, remark=" + resp.getRemark());
        awaitTrue("broker 的注册要先进 nameserver 的路由表", new Condition() {
            @Override
            public boolean holds() {
                try {
                    RemotingCommand r = askNameserver(routeRequest(topic));
                    return r.getCode() == RemotingSysResponseCode.SUCCESS && r.getBody() != null;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    private static RemotingCommand routeRequest(String topic) {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTE_BY_TOPIC);
        req.addExtField("topic", topic);
        return req;
    }

    private RemotingCommand askBroker(RemotingCommand request) throws Exception {
        final NettyRemotingClient client = sharedClient();
        final Channel channel = client.getOrCreateChannel(brokerAddr);
        assertNotNull(channel, "到 broker " + brokerAddr + " 的通道必须建得起来");
        RemotingCommand resp = client.invokeSync(channel, request, 10_000L);
        assertNotNull(resp, "RPC 没回应: code=" + request.getCode());
        return resp;
    }

    private RemotingCommand askNameserver(RemotingCommand request) throws Exception {
        NettyRemotingClient client = sharedClient();
        Channel channel = client.getOrCreateChannel(namesrvAddr);
        assertNotNull(channel, "连不上 nameserver: " + namesrvAddr);
        RemotingCommand resp = client.invokeSync(channel, request, 10_000L);
        assertNotNull(resp, "nameserver 没回应: code=" + request.getCode());
        return resp;
    }

    private NettyRemotingClient sharedClient() {
        if (wireClient == null) {
            wireClient = new NettyRemotingClient(new NettyClientConfig());
            wireClient.start();
            clients.add(wireClient);
        }
        return wireClient;
    }

    // ==================== 因果等待 ====================

    private interface Condition {
        boolean holds();
    }

    /** 等一个由被测侧自己写下的读数（事件本身）；判据永远是值, 不是时间. */
    private static void awaitTrue(String what, Condition condition) {
        long deadline = System.currentTimeMillis() + BAIL_OUT_MILLIS;
        while (!condition.holds()) {
            if (System.currentTimeMillis() >= deadline) {
                fail("等满 " + BAIL_OUT_MILLIS + "ms 仍然不成立：" + what);
            }
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("等待被中断: " + what);
            }
        }
    }

    /** 记录每一条被投到的消息；判据取 msgId 与正文，不取时间。 */
    private static final class Recorder implements MessageListener.Concurrently {
        private final LinkedBlockingQueue<MessageExt> arrivals = new LinkedBlockingQueue<MessageExt>();
        private final List<MessageExt> all = new CopyOnWriteArrayList<MessageExt>();

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(MessageExt[] msgs, MessageQueueContext context) {
            for (MessageExt msg : msgs) {
                all.add(msg);
                arrivals.add(msg);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        /** 等到"这一条 msgId 被投到消费端"这件事发生。 */
        MessageExt awaitMsgId(String msgId, String what) {
            List<MessageExt> buffered = new ArrayList<MessageExt>();
            long deadline = System.currentTimeMillis() + BAIL_OUT_MILLIS;
            try {
                while (true) {
                    MessageExt head = arrivals.poll(50L, TimeUnit.MILLISECONDS);
                    if (head != null) {
                        if (msgId.equals(head.getMsgId())) {
                            arrivals.addAll(buffered);
                            return head;
                        }
                        buffered.add(head);
                    }
                    if (System.currentTimeMillis() >= deadline) {
                        arrivals.addAll(buffered);
                        fail("等满 " + BAIL_OUT_MILLIS + "ms 也没等到：" + what + "（要找 msgId=" + msgId
                                + ", 期间投到过的: " + describe(all) + "）");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("等待被中断: " + what);
                return null;
            }
        }

        String describe(List<MessageExt> list) {
            return TransactionMessageE2ETest.describe(list);
        }
    }

    private static int freePort() throws Exception {
        ServerSocket probe = new ServerSocket(0);
        try {
            return probe.getLocalPort();
        } finally {
            probe.close();
        }
    }

    private static void deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        if (!file.delete()) {
            System.err.println("[tx-e2e] leftover: " + file.getAbsolutePath());
        }
    }
}
