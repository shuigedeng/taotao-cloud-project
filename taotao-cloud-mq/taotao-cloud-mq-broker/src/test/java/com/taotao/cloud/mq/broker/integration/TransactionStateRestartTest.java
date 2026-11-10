package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.broker.BrokerConfig;
import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.broker.processor.PullMessageProcessor;
import com.taotao.cloud.mq.broker.transaction.TransactionCheckService;
import com.taotao.cloud.mq.broker.transaction.TransactionStateManager;
import com.taotao.cloud.mq.client.producer.TransactionCheckTask;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.PullResultPayload;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import com.taotao.cloud.mq.plugin.store.log.FlushDiskType;
import io.netty.channel.Channel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 「回查」这句话的两条承重边：待回查集合要能活过 broker 重启，超时不答的事务要真被判回滚。
 * <p>
 * 为什么这两条必须在同一个文件里：回查存在的理由是"producer 在提交之前挂了"，那件事天然跨进程、
 * 跨时间。事务表如果只在内存里，回查就在它自己的首要场景下失效。
 * <p>
 * 三个用例：
 * <ol>
 *   <li>{@link #pendingHalfTransactionSurvivesBrokerRestartAndCanBeCommitted()} —— 发半消息 →
 *       不提交 → 真 shutdown → 换全新 BrokerController（同一存储目录）→ 这条仍在待回查集合里，
 *       并且重启后对它提交，消费者读得回来。</li>
 *   <li>{@link #committedTransactionIsNotPutBackIntoThePendingSetAfterRestart()} —— 定论也要活过重启：
 *       已经提交过的事务不许在重启后变回"待回查"，存储上也不许因此多出第二份。</li>
 *   <li>{@link #checkServiceForcesRollbackOnceMaxCheckTimesIsReached()} —— "超时未确认 ⇒ 自动回滚"：
 *       每一轮回查都由测试当场驱动（{@code checkOnce()}），判据是回查计数与状态的值，
 *       没有任何一处用等待时间当结论；次数用尽之后消息永远不回投，且这个回滚结论也活过重启。</li>
 * </ol>
 * 请求全部走真 remoting 通道（发到 broker 的监听端口），这样"注册段接没接上"也在承重范围内。
 */
public class TransactionStateRestartTest {

    private static final String BROKER_NAME = "TxRestartBroker";
    private static final String CLUSTER_NAME = "TxRestartCluster";
    private static final String TOPIC = "TxRestartTopic";
    private static final int QUEUE_ID = 2;

    @TempDir
    File tempDir;

    private final List<BrokerController> launched = new ArrayList<BrokerController>();
    private final List<NettyRemotingClient> clients = new ArrayList<NettyRemotingClient>();
    private NettyRemotingClient wireClient;
    private String brokerAddr;

    @AfterEach
    public void tearDown() {
        for (NettyRemotingClient c : clients) {
            try {
                c.shutdown();
            } catch (Exception ignore) {
                // 收尸
            }
        }
        clients.clear();
        for (BrokerController b : launched) {
            try {
                b.shutdown();
            } catch (Exception ignore) {
                // 用例里已经关过的，忽略
            }
        }
        launched.clear();
        deleteRecursively(tempDir);
    }

    // ==================== 1. 待回查集合活过重启，且重启后仍能提交 ====================

    @Test
    @DisplayName("发半消息不提交 → 真重启 → 这条仍在待回查集合里，且能被提交并读回来")
    public void pendingHalfTransactionSurvivesBrokerRestartAndCanBeCommitted() throws Exception {
        BrokerController first = startBroker();
        String txId = "tx-survive-" + System.nanoTime();
        String msgId = "TX-SURVIVE-1";
        sendHalf(first, txId, msgId, "payload-that-must-survive-a-restart");

        // 前置事实：第一个进程里它就是待回查的（否则"重启后还在"这句话没有起点）
        TransactionStateManager firstManager = first.getTransactionMessageProcessor().getTransactionStateManager();
        assertNotNull(firstManager.getTransaction(txId), "重启前这条必须就在事务表里");
        assertEquals(0, countMsg(pullFrom(first, TOPIC, QUEUE_ID), msgId),
                "没提交之前消费端读不到才是半消息");

        first.shutdown();
        launched.remove(first);

        // ---- 全新实例（代表重启后的进程），同一个存储目录 ----
        BrokerController second = startBroker();
        TransactionStateManager manager = second.getTransactionMessageProcessor().getTransactionStateManager();
        List<TransactionCheckTask> pending = fetchPendingTasks(second);
        assertTrue(containsTask(pending, txId),
                "重启后这条事务必须还在待回查集合里（回查是给"
                        + "「producer 提交前就挂了」准备的，表在内存里就等于这句话不成立）。"
                        + "实际重启后取到的待回查清单: " + describeTasks(pending));
        TransactionStateManager.TransactionRecord recovered = manager.getTransaction(txId);
        assertEquals(TOPIC, recovered.getOriginalTopic(), "重建出来的原始 topic");
        assertEquals(QUEUE_ID, recovered.getOriginalQueueId(), "重建出来的原始队列（提交要回同一条队列）");

        // 重启后对这条提交：消费者读得回来，而且正好一份
        endTransaction(second, txId, "COMMIT");
        List<MessageExt> afterCommit = pullFrom(second, TOPIC, QUEUE_ID);
        assertEquals(1, countMsg(afterCommit, msgId),
                "重启之后补的提交必须把消息投到原始 topic, 实际: " + describe(afterCommit));
    }

    // ==================== 2. 定论也活过重启 ====================

    @Test
    @DisplayName("已经提交过的事务不会在重启后回到待回查集合，也不会多出第二份")
    public void committedTransactionIsNotPutBackIntoThePendingSetAfterRestart() throws Exception {
        BrokerController first = startBroker();
        String txId = "tx-committed-" + System.nanoTime();
        String msgId = "TX-COMMITTED-1";
        sendHalf(first, txId, msgId, "already-committed-before-restart");
        endTransaction(first, txId, "COMMIT");
        assertEquals(1, countMsg(pullFrom(first, TOPIC, QUEUE_ID), msgId), "提交后先要有一份（前置事实）");

        first.shutdown();
        launched.remove(first);

        BrokerController second = startBroker();
        List<TransactionCheckTask> pending = fetchPendingTasks(second);
        assertTrue(!containsTask(pending, txId),
                "已提交的事务被重启后又收回到待回查集合里了 ⇒ 后续回查会把这条再投一遍（双投的来源）"
                        + "，实际: " + describeTasks(pending));
        assertEquals(TransactionStateManager.TxState.COMMITTED,
                second.getTransactionMessageProcessor().getTransactionStateManager().getResolvedState(txId),
                "op 记录必须把这条事务的结论也带回来");
        assertEquals(1, countMsg(pullFrom(second, TOPIC, QUEUE_ID), msgId),
                "恢复过程本身不许投出第二份");
    }

    // ==================== 3. 超时未确认 ⇒ 自动回滚 ====================

    @Test
    @DisplayName("回查次数用尽：悬着不答的事务被自动回滚，且这个结论活过重启")
    public void checkServiceForcesRollbackOnceMaxCheckTimesIsReached() throws Exception {
        BrokerController broker = startBroker();
        String txId = "tx-timeout-" + System.nanoTime();
        String msgId = "TX-TIMEOUT-1";
        sendHalf(broker, txId, msgId, "nobody-ever-answers-this-one");

        TransactionStateManager manager = broker.getTransactionMessageProcessor().getTransactionStateManager();
        // 把生产的"每 60 秒才催一次"这道节流打开，好让测试当场驱动每一轮；
        // 判据是"第 N 轮之后状态是什么"，不是"睡了多久"。
        manager.setCheckIntervalMillis(0L);
        manager.setMaxCheckTimes(2);
        TransactionCheckService checkService = broker.getTransactionCheckService();
        assertNotNull(checkService, "broker 必须装配回查调度服务");
        assertTrue(checkService.isRunning(), "start() 之后回查服务必须在跑");

        checkService.checkOnce();
        assertEquals(1, manager.getTransaction(txId).getCheckTimes(), "第一轮记一次数");
        assertEquals(TransactionStateManager.TxState.CHECKING, manager.getTransaction(txId).getState(),
                "答不上来的事务必须留在待回查状态");
        assertEquals(0, countMsg(pullFrom(broker, TOPIC, QUEUE_ID), msgId), "回查本身不许投消息");

        checkService.checkOnce();
        assertEquals(2, manager.getTransaction(txId).getCheckTimes(), "第二轮记到上限");

        checkService.checkOnce();
        assertEquals(TransactionStateManager.TxState.ROLLBACKED, manager.getTransaction(txId).getState(),
                "次数用尽必须自动判回滚（这是「超时未确认就回滚」这句话的全部内容）");
        assertEquals(0, countMsg(pullFrom(broker, TOPIC, QUEUE_ID), msgId),
                "自动回滚之后原始 topic 上永远不该有这一条");
        assertTrue(!containsTask(fetchPendingTasks(broker), txId),
                "已经判回滚的事务不该再出现在待回查清单里");

        // 自动回滚也是定论：重启后不许回到待回查集合
        broker.shutdown();
        launched.remove(broker);
        BrokerController restarted = startBroker();
        assertTrue(!containsTask(fetchPendingTasks(restarted), txId),
                "超时回滚的结论没落盘 ⇒ 重启后这条又被回查一遍");
        assertEquals(TransactionStateManager.TxState.ROLLBACKED,
                restarted.getTransactionMessageProcessor().getTransactionStateManager()
                        .getResolvedState(txId), "回滚结论必须从盘上重放出来");
    }

    // ==================== 4. 回查的两条读路径都真在服务 ====================

    @Test
    @DisplayName("CHECK_TRANSACTION_STATE：不带 transactionId 回清单，带的回半消息本体")
    public void checkRequestServesBothThePendingListAndASingleHalfMessage() throws Exception {
        BrokerController broker = startBroker();
        String txId = "tx-check-" + System.nanoTime();
        String msgId = "TX-CHECK-1";
        String body = "the-half-message-itself-must-come-back";
        sendHalf(broker, txId, msgId, body);

        List<TransactionCheckTask> tasks = fetchPendingTasks(broker);
        TransactionCheckTask mine = null;
        for (TransactionCheckTask t : tasks) {
            if (txId.equals(t.getTransactionId())) {
                mine = t;
            }
        }
        assertNotNull(mine, "清单里必须能找到这一条, 实际: " + describeTasks(tasks));
        assertEquals(TOPIC, mine.getOriginalTopic(), "清单要带上原始 topic（客户端作答时要用）");
        assertEquals(msgId, mine.getMsgId(), "清单要能认出是哪条消息");

        // 单条模式：把半消息本体回给调用方，回查才有东西可判
        RemotingCommand one = RemotingCommand.createRequestCommand(RequestCode.CHECK_TRANSACTION_STATE);
        one.addExtField("transactionId", txId);
        RemotingCommand resp = askBroker(one);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(), "单条回查必须应答");
        MessageExt back = JsonCodec.decode(resp.getBody(), MessageExt.class);
        assertNotNull(back, "响应体必须是那条半消息");
        assertEquals(body, new String(back.getBody(), StandardCharsets.UTF_8), "回查带回的正文");
        assertEquals(msgId, back.getMsgId(), "回查带回的 msgId");
        assertNull(resp.getRemark(), "回查不该带错误");
    }

    // ==================== broker 生命周期（同一存储目录 = 重启） ====================

    private BrokerController startBroker() throws Exception {
        MessageStoreConfig msc = new MessageStoreConfig();
        String root = new File(tempDir, "broker").toString();
        msc.setStorePathRootDir(root);
        msc.setStorePathCommitLog(root + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        msc.setFlushDiskType(FlushDiskType.SYNC_FLUSH);

        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName(BROKER_NAME);
        bc.setBrokerClusterName(CLUSTER_NAME);
        bc.setNamesrvAddr("");

        NettyServerConfig nsc = new NettyServerConfig();
        int port = freePort();
        nsc.setListenPort(port);

        BrokerController broker = new BrokerController(bc, msc, nsc);
        assertTrue(broker.initialize(), "BrokerController.initialize() 必须成功（真 load）");
        broker.start();
        launched.add(broker);
        brokerAddr = "127.0.0.1:" + port;
        return broker;
    }

    // ==================== 走真 remoting 通道的事务请求 ====================

    private void sendHalf(BrokerController broker, String txId, String msgId, String body) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE_V2);
        req.addExtField("topic", TOPIC);
        req.addExtField("queueId", String.valueOf(QUEUE_ID));
        MessageExt ext = new MessageExt();
        ext.setTopic(TOPIC);
        ext.setMsgId(msgId);
        ext.setTags("TX");
        ext.setKeys(msgId);
        ext.setBody(body.getBytes(StandardCharsets.UTF_8));
        ext.setBornTimestamp(1700000000000L);
        ext.setTransactionId(txId);
        ext.putProperty("TRANSACTION_ID", txId);
        req.setBody(JsonCodec.encode(ext));

        RemotingCommand resp = askBroker(req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(),
                "半消息必须被事务处理器接走, code=" + resp.getCode() + " remark=" + resp.getRemark());
    }

    private void endTransaction(BrokerController broker, String txId, String state) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.END_TRANSACTION);
        req.addExtField("transactionId", txId);
        req.addExtField("originalTopic", TOPIC);
        req.addExtField("transactionState", state);
        RemotingCommand resp = askBroker(req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(),
                "二次确认 " + state + " 必须被 broker 接受, code=" + resp.getCode()
                        + " remark=" + resp.getRemark());
    }

    private List<TransactionCheckTask> fetchPendingTasks(BrokerController broker) throws Exception {
        RemotingCommand resp = askBroker(RemotingCommand.createRequestCommand(RequestCode.CHECK_TRANSACTION_STATE));
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(),
                "取待回查清单必须应答, remark=" + resp.getRemark());
        byte[] body = resp.getBody();
        if (body == null || body.length == 0) {
            return new ArrayList<TransactionCheckTask>();
        }
        List<TransactionCheckTask> tasks = JsonCodec.decodeList(body, TransactionCheckTask.class);
        return tasks == null ? new ArrayList<TransactionCheckTask>() : tasks;
    }

    private static boolean containsTask(List<TransactionCheckTask> tasks, String txId) {
        for (TransactionCheckTask t : tasks) {
            if (txId.equals(t.getTransactionId())) {
                return true;
            }
        }
        return false;
    }

    private static String describeTasks(List<TransactionCheckTask> tasks) {
        StringBuilder sb = new StringBuilder();
        for (TransactionCheckTask t : tasks) {
            sb.append(t.getTransactionId()).append('@').append(t.getOriginalTopic()).append('/')
                    .append(t.getOriginalQueueId()).append(" ");
        }
        return "[" + sb.toString().trim() + "]";
    }

    private RemotingCommand askBroker(RemotingCommand request) throws Exception {
        NettyRemotingClient client = sharedClient();
        Channel channel = client.getOrCreateChannel(brokerAddr);
        assertNotNull(channel, "连不上 broker: " + brokerAddr);
        RemotingCommand resp = client.invokeSync(channel, request, 10_000L);
        assertNotNull(resp, "RPC 没回应: code=" + request.getCode());
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

    private static int countMsg(List<MessageExt> messages, String msgId) {
        int n = 0;
        for (MessageExt m : messages) {
            if (msgId.equals(m.getMsgId())) {
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
            System.err.println("[tx-restart] leftover: " + file.getAbsolutePath());
        }
    }
}
