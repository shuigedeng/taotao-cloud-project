package com.taotao.cloud.mq.consistency.ttc.broker.integration;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerConfig;
import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.ConsumeConcurrentlyStatus;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.DefaultMQPullConsumer;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.DefaultMQPushConsumer;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.MessageListener;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.MessageQueueContext;
import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
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
import com.taotao.cloud.mq.consistency.ttc.store.config.ConsumerOffsetManager;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * 「push 消费者在 listener 回成功之后把位点写穿给 broker」这句话的行为兑现：提交方是
 * {@link DefaultMQPushConsumer} 本体（真 {@code start()}、真拉取线程、真 RPC），判据是
 * {@link ConsumerOffsetManager#queryOffset(String, int, String)} 那个<b>broker 侧的读数</b>。
 * <p>
 * 为什么判据必须落在 broker 的读数上：push 侧的提交是「先写进程内缓存、再发 RPC、RPC 失败只记一行
 * 告警」，所以 {@code DefaultMQPushConsumer.getOffset(mq)} 在提交根本没出去的时候照样是那个"看起来
 * 已经提交过"的数 —— 拿它当尺，这条边断了也量不出来。每一处断言写的都是 broker 的读数，本地缓存
 * 只出现在失败信息里做对照。
 * <p>
 * 四例各钉一条边：
 * <ol>
 *   <li>{@link #pushConsumerSuccessCommitsTheOffsetIntoTheBroker()} —— 提交这条边真的落到了
 *       broker 的三元组表里（5 条消费完 ⇒ broker 侧那位 == 5），而且只投一遍。</li>
 *   <li>{@link #pushCommittedOffsetSurvivesRealRestartAndResumesTheTail()} —— 提交出去的那位
 *       真跨得过进程：真 {@code shutdown()}（关机 flush）→ 同一存储目录换一个全新的
 *       {@link BrokerController} → 任何一次投递之前那位就必须已经从盘上读回来，
 *       新的 push 消费者只读得到尾巴（前 4 条不重放）。</li>
 *   <li>{@link #realPullCommitApiMovesTheReadStartOfTheSameGroup()} —— client 侧那对显式入口
 *       （{@code commitOffset} / {@code pullFromCommittedOffset}）走真 API 复现同一套语义，
 *       并且与 push 那两例<b>不共用任何断言</b>：摘掉 push 的提交边时这一例必须仍然绿。</li>
 *   <li>{@link #rejectedCommitsAreCountedWithoutBreakingThePullLoop()} —— 提交<b>失败</b>这一头同样
 *       要有读数：①失败笔数从 0 涨成正数 ②拉取循环照旧把下一条批投进来 ③broker 侧那位一步没前移。
 *       三条一起断才叫"静默的代价有了出口"，只断①是假的。</li>
 * </ol>
 * <b>口径</b>：
 * <ul>
 *   <li>没有 sleep 阈值当判据。等异步一律等在被断言的那个读数本身上（broker 侧那位、
 *       或监听器自己写下的投递快照），超时只给"这事根本没发生"一个可读的出路。</li>
 *   <li>端口一律现问（{@code freePort()}），不写死 —— 写死端口会被内核的临时端口段抢走。</li>
 *   <li>存储路径 {@code storePathRootDir} 与 {@code storePathCommitLog} <b>都</b>显式设进
 *       {@code @TempDir}（只设一个会让 commitlog 逃逸到 {@code ~/store}，本仓踩过 2.0 GB 的账）。</li>
 * </ul>
 */
public class PushOffsetCommitE2ETest {

    /** 一次读数/一次投递的等待上限；只用于给出「这件事根本没发生」的可读失败，不承载任何数值判定. */
    private static final long BAIL_OUT_MILLIS = 90_000L;

    private static final String BROKER_NAME = "W2hOffsetBroker";
    private static final String CLUSTER_NAME = "W2hOffsetCluster";
    private static final int QUEUE_ID = 0;

    private static final String TOPIC_PUSH = "PushCommitTopic";
    private static final String PUSH_GROUP = "push-commit-group";
    private static final int PUSH_TOTAL = 5;

    private static final String TOPIC_TAIL = "PushRestartTailTopic";
    private static final String TAIL_GROUP = "push-restart-group";
    private static final int TAIL_FIRST_ROUND = 4;
    private static final int TAIL_TOTAL = 10;

    private static final String TOPIC_PULL = "PullCommitApiTopic";
    private static final String PULL_GROUP = "pull-commit-api-group";
    private static final int PULL_TOTAL = 10;
    private static final int PULL_COMMITTED = 4;

    private static final String TOPIC_REJECTED = "RejectedCommitTopic";
    /** 尺自检用的 topic（正经消费组在上面提交得动）—— 与 {@link #TOPIC_REJECTED} 分开, 免得往被断言的队列里塞消息. */
    private static final String TOPIC_CONTROL = "RejectedCommitControlTopic";
    /**
     * 这一路专门用来把提交打到<b>必然失败</b>：broker 的提交口对空消费组显式回
     * {@code SYSTEM_ERROR("consumerGroup missing")}，而拉取路径上空消费组只是"按组恢复无从下手、
     * 回落到 0"，不是错误。于是"提交全抛、消费照常"这个形状不需要关停 broker 就能稳定复现。
     */
    private static final String BROKEN_GROUP = "";
    private static final String CONTROL_GROUP = "w2hControlGroup";
    private static final int REJECT_FIRST_ROUND = 3;
    private static final int REJECT_TOTAL = 6;

    @TempDir
    Path tempDir;

    private NameServerController nameServer;
    private String namesrvAddr;
    /** 现在这台 broker 绑上的端口（{@code NettyServerConfig} 没有 getter, 广告地址是 {@code localhost:端口}）. */
    private int brokerPort;
    private final List<BrokerController> launchedBrokers = new ArrayList<BrokerController>();
    private final List<DefaultMQPushConsumer> launchedPushConsumers = new ArrayList<DefaultMQPushConsumer>();
    private final List<DefaultMQPullConsumer> launchedPullConsumers = new ArrayList<DefaultMQPullConsumer>();
    private final List<NettyRemotingClient> clients = new ArrayList<NettyRemotingClient>();
    private NettyRemotingClient wireClient;

    @AfterEach
    public void tearDown() {
        for (DefaultMQPushConsumer c : launchedPushConsumers) {
            try {
                c.shutdown();
            } catch (Exception ignore) {
                // 用例里已经关过的，收尸时忽略
            }
        }
        launchedPushConsumers.clear();
        for (DefaultMQPullConsumer c : launchedPullConsumers) {
            try {
                c.shutdown();
            } catch (Exception ignore) {
                // 同上
            }
        }
        launchedPullConsumers.clear();
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

    // ==================== 1. push 半边：listener 回成功 ⇒ broker 侧那位真的被写了 ====================

    @Test
    @DisplayName("push: listener 回 CONSUME_SUCCESS 之后，broker 侧 (topic, queueId, group) 那位必须是消费完的那个数")
    public void pushConsumerSuccessCommitsTheOffsetIntoTheBroker() throws Exception {
        startCluster();
        registerTopic(TOPIC_PUSH);
        awaitRegisteredBrokerAddr(TOPIC_PUSH);

        for (int i = 0; i < PUSH_TOTAL; i++) {
            sendOne(TOPIC_PUSH, "PUSH-" + i, "push-body-" + i);
        }

        Recorder received = new Recorder("回成功的消费方");
        DefaultMQPushConsumer consumer = startPushConsumer(PUSH_GROUP, TOPIC_PUSH, received);
        MessageQueue mq = new MessageQueue(TOPIC_PUSH, BROKER_NAME, QUEUE_ID);

        // ★ 等在被断言的那个读数本身上：broker 侧那位变成 5
        awaitTrue("broker 侧 " + keyDesc(TOPIC_PUSH, PUSH_GROUP) + " 变成 " + PUSH_TOTAL,
                offsetBecomes(TOPIC_PUSH, PUSH_GROUP, PUSH_TOTAL));
        assertEquals(PUSH_TOTAL, brokerOffset(TOPIC_PUSH, PUSH_GROUP),
                "★ 判据（broker 的读数）: 5 条都回成功之后, broker 记下的那位必须是 5 —— 实测 "
                        + describeBoth(TOPIC_PUSH, PUSH_GROUP, consumer, mq)
                        + "；停在 0/−1 就说明提交根本没出进程");

        // 投过来的就是那 5 条，一条不多一条不少（位点前移之后不再重放）
        assertEquals(PUSH_TOTAL, received.totalSeen(),
                "5 条应当恰好被投 5 次（多出来的就是位点没生效、同一条被反复重拉）, 实测="
                        + received.describe());
        for (int i = 0; i < received.totalSeen(); i++) {
            Snapshot s = received.all().get(i);
            assertEquals("PUSH-" + i, s.msgId, "第 " + i + " 次投递的 msgId, 实测=" + s.describe());
            assertEquals((long) i, s.queueOffset, "第 " + i + " 次投递带回来的队列位点必须逐条对上");
        }
        assertEquals(PUSH_TOTAL, received.countTopicOnly(TOPIC_PUSH),
                "投回来的消息都得是这个 topic 上的, 实测=" + received.describe());

        consumer.shutdown();
        launchedPushConsumers.remove(consumer);
    }

    // ==================== 2. push 提交的那位跨得过真重启，尾巴由 push 自己接着读 ====================

    @Test
    @DisplayName("push: 提交 → 真 shutdown → 同一存储目录换全新 BrokerController → 新 push 消费者只读得到尾巴（前 4 条不重放）")
    public void pushCommittedOffsetSurvivesRealRestartAndResumesTheTail() throws Exception {
        startCluster();
        registerTopic(TOPIC_TAIL);
        awaitRegisteredBrokerAddr(TOPIC_TAIL);

        // ---- 第一轮：队列里只有前 TAIL_FIRST_ROUND 条 ----
        for (int i = 0; i < TAIL_FIRST_ROUND; i++) {
            sendOne(TOPIC_TAIL, "TAIL-" + i, "tail-body-" + i);
        }

        Recorder firstRound = new Recorder("第一轮");
        DefaultMQPushConsumer firstConsumer = startPushConsumer(TAIL_GROUP, TOPIC_TAIL, firstRound);
        awaitTrue("第一轮 broker 侧 " + keyDesc(TOPIC_TAIL, TAIL_GROUP) + " 变成 " + TAIL_FIRST_ROUND,
                offsetBecomes(TOPIC_TAIL, TAIL_GROUP, TAIL_FIRST_ROUND));
        assertEquals(TAIL_FIRST_ROUND, brokerOffset(TOPIC_TAIL, TAIL_GROUP),
                "第一轮提交完之后 broker 侧那位必须是 " + TAIL_FIRST_ROUND + ", 实测="
                        + brokerOffset(TOPIC_TAIL, TAIL_GROUP));
        assertEquals(TAIL_FIRST_ROUND, firstRound.totalSeen(),
                "第一轮只该投到前 " + TAIL_FIRST_ROUND + " 条, 实测=" + firstRound.describe());

        // ---- 提交方下线：位点只能靠 broker 那一侧活着 ----
        firstConsumer.shutdown();
        launchedPushConsumers.remove(firstConsumer);

        // ---- 没有消费者在读了，尾巴这 6 条先进存储 ----
        for (int i = TAIL_FIRST_ROUND; i < TAIL_TOTAL; i++) {
            sendOne(TOPIC_TAIL, "TAIL-" + i, "tail-body-" + i);
        }
        assertEquals(TAIL_FIRST_ROUND, brokerOffset(TOPIC_TAIL, TAIL_GROUP),
                "没有消费者在提交 ⇒ 新进的 6 条不许自己推动那位（那位仍然是 " + TAIL_FIRST_ROUND + "）, 实测="
                        + brokerOffset(TOPIC_TAIL, TAIL_GROUP));

        // ---- 真关机：BrokerController.shutdown() → ConsumerOffsetManager.shutdown() → flush() ----
        BrokerController goingDown = launchedBrokers.get(launchedBrokers.size() - 1);
        goingDown.shutdown();
        launchedBrokers.remove(goingDown);
        File offsetFile = new File(storeRoot(), ConsumerOffsetManager.OFFSET_FILE_NAME);
        assertTrue(offsetFile.isFile() && offsetFile.length() > 0,
                "干净关机之后位点文件必须在（不在就说明 push 提交的那位从没离开过进程）: "
                        + offsetFile.getAbsolutePath());

        // ---- 重启：全新 BrokerController 实例、同一存储目录 ----
        startBrokerOnSameStore();
        BrokerController restarted = launchedBrokers.get(launchedBrokers.size() - 1);
        assertEquals(TAIL_FIRST_ROUND,
                restarted.getConsumerOffsetManager().queryOffset(TOPIC_TAIL, QUEUE_ID, TAIL_GROUP),
                "重启之后、任何一次投递之前，broker 侧那位就必须已经从盘上读回来了（这才是「持久化」那一半）, 实测="
                        + restarted.getConsumerOffsetManager().queryOffset(TOPIC_TAIL, QUEUE_ID, TAIL_GROUP));
        awaitRegisteredBrokerAddr(TOPIC_TAIL);

        // ---- 续读：换一个全新的 push 消费者（进程内缓存是空的），提交方仍然是 push ----
        Recorder afterRestart = new Recorder("重启之后");
        DefaultMQPushConsumer resumed = startPushConsumer(TAIL_GROUP, TOPIC_TAIL, afterRestart);
        try {
            awaitTrue("重启之后 broker 侧 " + keyDesc(TOPIC_TAIL, TAIL_GROUP) + " 变成 " + TAIL_TOTAL,
                    offsetBecomes(TOPIC_TAIL, TAIL_GROUP, TAIL_TOTAL));
            assertEquals(TAIL_TOTAL, brokerOffset(TOPIC_TAIL, TAIL_GROUP),
                    "★ 尾巴读完并再提交一次之后, broker 侧那位必须是 " + TAIL_TOTAL + ", 实测="
                            + brokerOffset(TOPIC_TAIL, TAIL_GROUP));

            assertEquals(TAIL_TOTAL - TAIL_FIRST_ROUND, afterRestart.totalSeen(),
                    "★ 重启之后只该读到尾巴 6 条（读到 10 条就是位点没跨过来、整队重放）, 实测="
                            + afterRestart.describe());
            for (int i = 0; i < afterRestart.totalSeen(); i++) {
                Snapshot s = afterRestart.all().get(i);
                assertEquals("TAIL-" + (TAIL_FIRST_ROUND + i), s.msgId,
                        "★ 重启后第一条必须是第 " + (TAIL_FIRST_ROUND + 1) + " 条（msgId 逐字对上）, "
                                + "第 " + i + " 个实测=" + s.describe());
                assertEquals((long) TAIL_FIRST_ROUND + i, s.queueOffset,
                        "尾巴的队列位点必须从 " + TAIL_FIRST_ROUND + " 起连续, index=" + i + " 实测=" + s.queueOffset);
            }
        } finally {
            resumed.shutdown();
            launchedPushConsumers.remove(resumed);
        }
    }

    // ==================== 3. pull 半边：真 client API 走一遍已钉过的语义 ====================

    @Test
    @DisplayName("pull: 真 commitOffset(...) + pullFromCommittedOffset(...) 走一遍——提交 4 ⇒ 不带 offset 拉到的第一条是第 5 条")
    public void realPullCommitApiMovesTheReadStartOfTheSameGroup() throws Exception {
        startCluster();
        registerTopic(TOPIC_PULL);
        awaitRegisteredBrokerAddr(TOPIC_PULL);

        for (int i = 0; i < PULL_TOTAL; i++) {
            sendOne(TOPIC_PULL, "API-" + i, "api-body-" + i);
        }

        DefaultMQPullConsumer consumer = startPullConsumer(PULL_GROUP);
        MessageQueue mq = new MessageQueue(TOPIC_PULL, BROKER_NAME, QUEUE_ID);
        try {
            // ---- 前置对照：本组还没提交过 ⇒ 不带 offset 的拉取只能从 0 起 ----
            DefaultMQPullConsumer.PullResult before = consumer.pullFromCommittedOffset(mq, 32);
            assertEquals(PULL_TOTAL, before.getMsgFoundList().size(),
                    "没提交过位点时不带 offset 必须读到全部 10 条, 实测="
                            + brokerOffset(TOPIC_PULL, PULL_GROUP));
            assertEquals("API-0", before.getMsgFoundList().get(0).getMsgId(), "起点必须是第 1 条");
            assertEquals(0L, before.getMsgFoundList().get(0).getQueueOffset());

            // ---- 提交侧：真 API（不是手搓 RemotingCommand），返回值必须是 broker 回读上来的那位 ----
            long stored = consumer.commitOffset(mq, PULL_COMMITTED);
            assertEquals(PULL_COMMITTED, stored,
                    "commitOffset 必须把 broker 回读上来的数还给调用方（把自己传进去的数原样吐回来不算）");
            assertEquals(PULL_COMMITTED, brokerOffset(TOPIC_PULL, PULL_GROUP),
                    "★ 判据（broker 的读数）: 真 API 提交的那位必须写进 broker 的三元组表, 实测="
                            + brokerOffset(TOPIC_PULL, PULL_GROUP));
            // 这一条是 pull 侧的习惯（缓存写的是 broker 回读上来的 stored），只对 pull 成立；
            // push 侧的缓存在 RPC 之前就写了，拿同一个数当尺量 push 会量出一份假绿。
            assertEquals(PULL_COMMITTED, consumer.getOffset(mq),
                    "pull 侧本地缓存记的必须是 broker 回读上来的那个值, 实测=" + consumer.getOffset(mq));

            // ---- 读取侧：不带 offset ⇒ 起点由 broker 按已提交位点决定 ----
            DefaultMQPullConsumer.PullResult tail = consumer.pullFromCommittedOffset(mq, 32);
            List<MessageExt> msgs = tail.getMsgFoundList();
            assertEquals(PULL_TOTAL - PULL_COMMITTED, msgs.size(),
                    "从已提交位点起读，剩下的必须是 6 条（读到 10 条 = 起点还是 0）, 实测=" + msgs.size());
            assertEquals("API-" + PULL_COMMITTED, msgs.get(0).getMsgId(),
                    "★ 不带 offset 拉到的第一条必须是第 " + (PULL_COMMITTED + 1) + " 条, 实测="
                            + msgs.get(0).getMsgId());
            assertEquals((long) PULL_COMMITTED, msgs.get(0).getQueueOffset());
            for (int i = 0; i < msgs.size(); i++) {
                assertEquals((long) PULL_COMMITTED + i, msgs.get(i).getQueueOffset(),
                        "尾巴必须连续, index=" + i);
                assertEquals("API-" + (PULL_COMMITTED + i), msgs.get(i).getMsgId(),
                        "msgId 逐条对上, index=" + i);
            }
            assertEquals(PULL_TOTAL, tail.getNextOffset(), "读完尾巴之后该提交的位置");

            // ---- 那位真的在驱动起点，不是一次性摆设：提交到队尾之后就读不到了 ----
            assertEquals(PULL_TOTAL, consumer.commitOffset(mq, tail.getNextOffset()), "第二次提交必须被记下");
            assertEquals(0, consumer.pullFromCommittedOffset(mq, 32).getMsgFoundList().size(),
                    "提交到队尾之后再不带 offset 拉，应当读不到消息");
        } finally {
            consumer.shutdown();
            launchedPullConsumers.remove(consumer);
        }
    }

    // ==================== 4. 提交失败不许是静默的：计数读得到、循环没断、那位没前移 ====================

    @Test
    @DisplayName("提交被 broker 拒掉时：失败笔数读得到（≥1）、拉取循环照旧投下一条批、broker 侧那位一步没前移")
    public void rejectedCommitsAreCountedWithoutBreakingThePullLoop() throws Exception {
        startCluster();
        registerTopic(TOPIC_REJECTED);
        awaitRegisteredBrokerAddr(TOPIC_REJECTED);

        // ---- 尺自检（阳性对照）：同一台 broker、同一套三元组 key，正经消费组提交就得落地。
        // 没有这一步，下面那条 −1 也可以被"queryOffset 压根读不到东西"解释。
        // 对照走的是<b>另一个 topic</b>：它只要证明那把尺读得到东西就够了，
        // 不许往被断言的那个队列里塞消息（塞了就把"投了几条"这笔账搅浑）。
        registerTopic(TOPIC_CONTROL);
        awaitRegisteredBrokerAddr(TOPIC_CONTROL);
        DefaultMQPullConsumer control = startPullConsumer(CONTROL_GROUP);
        MessageQueue controlMq = new MessageQueue(TOPIC_REJECTED, BROKER_NAME, QUEUE_ID);
        MessageQueue controlProbeMq = new MessageQueue(TOPIC_CONTROL, BROKER_NAME, QUEUE_ID);
        try {
            assertEquals(1L, control.commitOffset(controlProbeMq, 1L),
                    "对照: 正经消费组的提交必须当场被 broker 记下（分不清是那把尺坏还是这条路坏, 在这儿分开）");
            assertEquals(1L, brokerOffset(TOPIC_CONTROL, CONTROL_GROUP),
                    "对照: broker 侧那位真的前移了, 实测=" + brokerOffset(TOPIC_CONTROL, CONTROL_GROUP));
        } finally {
            control.shutdown();
            launchedPullConsumers.remove(control);
        }
        assertEquals(-1L, brokerOffset(TOPIC_REJECTED, BROKEN_GROUP),
                "动手之前先看清: 坏的那一格 (…," + BROKEN_GROUP + ") 在 broker 上本来就是空的");

        Recorder received = new Recorder("提交被拒的那一路");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(BROKEN_GROUP);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setPullIntervalMillis(20L);
        consumer.subscribe(TOPIC_REJECTED, received);
        launchedPushConsumers.add(consumer);
        assertEquals(0L, consumer.getOffsetCommitFailures(),
                "起点: 一位都没提交过 ⇒ 失败笔数必须是 0（下面那条『变正』才是从 0 涨上去的）, 实测="
                        + consumer.getOffsetCommitFailures());

        // 第一批先全部进存储，再起消费者 ⇒ 「一批 3 条」是这一步自己定的形状，不是撞运气
        for (int i = 0; i < REJECT_FIRST_ROUND; i++) {
            sendOne(TOPIC_REJECTED, "REJ-" + i, "rej-body-" + i);
        }
        consumer.start();

        try {
            // ① 等在被断言的那个读数本身上：失败笔数自己变正（不是"等投递变多再回头断计数"）
            awaitTrue("提交失败计数从 0 变正", counterAbove(consumer, 0L));
            long afterFirstRound = consumer.getOffsetCommitFailures();
            assertTrue(afterFirstRound >= 1L,
                    "① 提交失败要读得到: 第一批回成功而 broker 一封都没收下 ⇒ 计数至少 1, 实测="
                            + afterFirstRound);
            // ③ broker 侧那位没有前进
            assertEquals(-1L, brokerOffset(TOPIC_REJECTED, BROKEN_GROUP),
                    "③ 提交全被拒 ⇒ broker 侧那一格必须还是空的; 实测="
                            + brokerOffset(TOPIC_REJECTED, BROKEN_GROUP)
                            + " （进程内缓存那位=" + consumer.getOffset(controlMq) + ", 它不是判据）");

            // 前提先立住: 第一批 3 条确实投到了（监听器自己写下的快照, 不是推算）
            received.awaitArrival(REJECT_FIRST_ROUND, "第一批得先投进来");
            assertEquals(REJECT_FIRST_ROUND, received.totalSeen(),
                    "第一批就该是 3 条（这一例里位点提交全被拒、本地缓存才是唯一在走的尺）, 实测="
                            + received.describe());

            // ② 拉取循环不许被打断: 再来一批, 监听器照样收得到（提交失败只记账, 不掐循环）
            for (int i = REJECT_FIRST_ROUND; i < REJECT_TOTAL; i++) {
                sendOne(TOPIC_REJECTED, "REJ-" + i, "rej-body-" + i);
            }
            received.awaitArrival(REJECT_TOTAL, "提交失败之后, 第二批还得投得进来");
            // 第二批的提交同样被拒 ⇒ 同一本账接着涨（等的还是计数本身）
            awaitTrue("第二批的提交失败也要记进同一本账", counterAbove(consumer, afterFirstRound));
            assertTrue(consumer.getOffsetCommitFailures() > afterFirstRound,
                    "② 计数要跟着第二批继续涨（拉取循环没断的直接证据）: 第一批后=" + afterFirstRound
                            + " 现在=" + consumer.getOffsetCommitFailures());
            assertEquals(REJECT_TOTAL, received.totalSeen(),
                    "② 两批共 " + REJECT_TOTAL + " 条都投到了, 实测=" + received.describe());

            // ③ 收尾再看一次: 全程那一格一步没前移
            assertEquals(-1L, brokerOffset(TOPIC_REJECTED, BROKEN_GROUP),
                    "③ 投了 " + received.totalSeen() + " 条、失败记了 " + consumer.getOffsetCommitFailures()
                            + " 笔之后, broker 侧那一格仍然必须是空的; 实测="
                            + brokerOffset(TOPIC_REJECTED, BROKEN_GROUP));
            assertEquals(1L, brokerOffset(TOPIC_CONTROL, CONTROL_GROUP),
                    "★ 位点是按 (topic, queueId, group) 存的 ⇒ 坏的那一路不许碰到对照那一格, 实测="
                            + brokerOffset(TOPIC_CONTROL, CONTROL_GROUP));
            // 这一条不是判据, 是把"为什么不能拿本地缓存当尺"写在同一个读数上：
            // 提交一笔都没出去, 进程内缓存那位照样是前移过的。
            assertEquals((long) REJECT_TOTAL, consumer.getOffset(controlMq),
                    "对照用: 本地缓存在 RPC 之前就写了 ⇒ 它前移而 broker 没动（所以它不能当判据）, 实测="
                            + consumer.getOffset(controlMq));
        } finally {
            consumer.shutdown();
            launchedPushConsumers.remove(consumer);
        }
    }

    // ==================== 尺与被测两侧的读数 ====================

    /** broker 侧那位（三元组 key 的真相）；本文件所有承重断言都落在这个数上. */
    private long brokerOffset(String topic, String group) {
        BrokerController alive = launchedBrokers.get(launchedBrokers.size() - 1);
        return alive.getConsumerOffsetManager().queryOffset(topic, QUEUE_ID, group);
    }

    private String keyDesc(String topic, String group) {
        return "(" + topic + ", " + QUEUE_ID + ", " + group + ")";
    }

    /** 失败信息里同时给出两处读数：broker 的真相 vs 进程内缓存 —— 只有前者是判据. */
    private String describeBoth(String topic, String group, DefaultMQPushConsumer consumer, MessageQueue mq) {
        return "broker 侧=" + brokerOffset(topic, group) + " 进程内缓存(不是判据)=" + consumer.getOffset(mq);
    }

    private interface Condition {
        boolean holds();
    }

    /** 等某个 broker 侧读数变成期望值：轮询的就是被断言的那个数本身. */
    private Condition offsetBecomes(final String topic, final String group, final long expected) {
        return new Condition() {
            @Override
            public boolean holds() {
                return brokerOffset(topic, group) == expected;
            }
        };
    }

    /** 等 push 消费者自己的失败计数涨过 wanted：轮的同样是被断言的那个数本身，不是别的事件. */
    private Condition counterAbove(final DefaultMQPushConsumer consumer, final long wanted) {
        return new Condition() {
            @Override
            public boolean holds() {
                return consumer.getOffsetCommitFailures() > wanted;
            }
        };
    }

    /** 等一个由被测侧自己写下的读数（事件本身）；判据永远是值，不是时间. */
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

    /** 在同一个存储目录上起（或再起）一个全新的 BrokerController —— 这就是「重启」那一步. */
    private void startBrokerOnSameStore() throws Exception {
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(storeRoot());
        msc.setStorePathCommitLog(storeRoot() + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        msc.setFlushDiskType(FlushDiskType.SYNC_FLUSH);

        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName(BROKER_NAME);
        bc.setBrokerClusterName(CLUSTER_NAME);
        bc.setNamesrvAddr(namesrvAddr);

        int port = freePort();
        NettyServerConfig nsc = new NettyServerConfig();
        nsc.setListenPort(port);
        this.brokerPort = port;

        BrokerController broker = new BrokerController(bc, msc, nsc);
        assertTrue(broker.initialize(), "BrokerController.initialize() 必须成功（真 load）");
        broker.start();
        launchedBrokers.add(broker);
    }

    private String storeRoot() {
        return tempDir.resolve("broker").toString();
    }

    private DefaultMQPushConsumer startPushConsumer(String group, String topic,
                                                    MessageListener listener) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setPullIntervalMillis(20L);
        consumer.subscribe(topic, listener);
        consumer.start();
        launchedPushConsumers.add(consumer);
        return consumer;
    }

    private DefaultMQPullConsumer startPullConsumer(String group) throws Exception {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(group);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.start();
        launchedPullConsumers.add(consumer);
        return consumer;
    }

    /** 既有的管理请求码：向 NameServer 注册一个 topic（产品侧没有 client 自动建 topic 的入口）. */
    private void registerTopic(String topic) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
        req.addExtField("topic", topic);
        req.addExtField("readQueueNums", "1");
        req.addExtField("writeQueueNums", "1");
        RemotingCommand resp = askNameserver(req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(),
                "注册 topic " + topic + " 必须成功, remark=" + resp.getRemark());
    }

    private TopicRouteData fetchRoute(String topic) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTE_BY_TOPIC);
        req.addExtField("topic", topic);
        RemotingCommand resp = askNameserver(req);
        if (resp.getCode() != RemotingSysResponseCode.SUCCESS || resp.getBody() == null
                || resp.getBody().length == 0) {
            return null;
        }
        return JsonCodec.decode(resp.getBody(), TopicRouteData.class);
    }

    private static boolean hasBroker(TopicRouteData route) {
        return route != null && route.getBrokerDatas() != null && !route.getBrokerDatas().isEmpty();
    }

    /** 等路由里出现「现在真正活着的那台 broker」：这是重启之后的同步点，不是判据. */
    private void awaitRegisteredBrokerAddr(final String topic) throws Exception {
        final String want = "localhost:" + brokerPort;
        awaitTrue("topic=" + topic + " 的路由里出现现在这台 broker 的地址 " + want, new Condition() {
            @Override
            public boolean holds() {
                try {
                    TopicRouteData route = fetchRoute(topic);
                    if (!hasBroker(route)) {
                        return false;
                    }
                    for (BrokerData bd : route.getBrokerDatas()) {
                        if (want.equals(bd.selectBrokerAddr())) {
                            return true;
                        }
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    private void sendOne(String topic, String msgId, String body) throws Exception {
        MessageExt inbound = new MessageExt();
        inbound.setTopic(topic);
        inbound.setMsgId(msgId);
        inbound.setTags("w2h-tag");
        inbound.setKeys(msgId);
        inbound.setBody(body.getBytes(StandardCharsets.UTF_8));
        inbound.setBornTimestamp(1700000000000L);

        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", topic);
        req.addExtField("queueId", String.valueOf(QUEUE_ID));
        req.addExtField("brokerName", BROKER_NAME);
        req.addExtField("msgId", msgId);
        req.setBody(JsonCodec.encode(inbound));

        RemotingCommand resp = askBroker(req, topic);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(), "发送必须被应答: " + msgId);
        SendResult result = JsonCodec.decode(resp.getBody(), SendResult.class);
        assertNotNull(result, "发送响应体必须能解出 SendResult");
        assertEquals(SendStatus.SEND_OK, result.getSendStatus(), "必须 SEND_OK: " + msgId);
    }

    private RemotingCommand askBroker(RemotingCommand request, String topic) throws Exception {
        awaitTrue("发送前 topic=" + topic + " 要有可连的 broker", new Condition() {
            @Override
            public boolean holds() {
                try {
                    return hasBroker(fetchRoute(topic));
                } catch (Exception e) {
                    return false;
                }
            }
        });
        String addr = fetchRoute(topic).getBrokerDatas().get(0).selectBrokerAddr();
        assertNotNull(addr, "路由里没有可连的 broker 地址");
        NettyRemotingClient client = sharedClient();
        Channel channel = client.getOrCreateChannel(addr);
        assertNotNull(channel, "连不上 broker: " + addr);
        RemotingCommand resp = client.invokeSync(channel, request, 5000L);
        assertNotNull(resp, "RPC 没回应: code=" + request.getCode());
        return resp;
    }

    private RemotingCommand askNameserver(RemotingCommand request) throws Exception {
        NettyRemotingClient client = sharedClient();
        Channel channel = client.getOrCreateChannel(namesrvAddr);
        assertNotNull(channel, "连不上 nameserver: " + namesrvAddr);
        RemotingCommand resp = client.invokeSync(channel, request, 5000L);
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

    /** 现问一个当前空闲的端口，不占固定端口（写死的端口会被内核临时端口段抢走）. */
    private static int freePort() throws Exception {
        ServerSocket probe = new ServerSocket(0);
        try {
            return probe.getLocalPort();
        } finally {
            probe.close();
        }
    }

    // ==================== 回调侧：把每一次投递当成因果事件记下来 ====================

    /** 一次投递的快照：msgId 与它从存储里带回来的队列位点. */
    private static final class Snapshot {
        final String msgId;
        final long queueOffset;
        final String topic;

        Snapshot(MessageExt msg) {
            this.msgId = msg.getMsgId();
            this.queueOffset = msg.getQueueOffset();
            this.topic = msg.getTopic();
        }

        String describe() {
            return msgId + "@offset=" + queueOffset + " topic=" + topic;
        }
    }

    /** 一律回「成功」的监听器：它每被投一次就记一条快照，等待等的是这些快照本身. */
    private static final class Recorder implements MessageListener.Concurrently {
        private final List<Snapshot> all = new CopyOnWriteArrayList<Snapshot>();
        private final BlockingQueue<Snapshot> arrivals = new LinkedBlockingQueue<Snapshot>();
        private final String who;

        Recorder(String who) {
            this.who = who;
        }

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(MessageExt[] msgs, MessageQueueContext context) {
            for (MessageExt msg : msgs) {
                Snapshot s = new Snapshot(msg);
                all.add(s);
                arrivals.add(s);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        /** 等「第 wanted 条投递」这件事由投递线程自己写下. */
        void awaitArrival(int wanted, String what) {
            long deadline = System.currentTimeMillis() + BAIL_OUT_MILLIS;
            while (all.size() < wanted) {
                if (System.currentTimeMillis() >= deadline) {
                    fail("等满 " + BAIL_OUT_MILLIS + "ms 只投到 " + all.size() + " 条（想要至少 "
                            + wanted + "）：" + what + " ; 已经看到的=" + describe());
                }
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    fail("等待被中断: " + what);
                }
            }
        }

        List<Snapshot> all() {
            return all;
        }

        int totalSeen() {
            return all.size();
        }

        int countTopicOnly(String topic) {
            int n = 0;
            for (Snapshot s : all) {
                if (topic.equals(s.topic)) {
                    n++;
                }
            }
            return n;
        }

        String describe() {
            StringBuilder sb = new StringBuilder();
            for (Snapshot s : all) {
                sb.append(s.describe()).append("; ");
            }
            return "Recorder{" + who + "} => " + sb;
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
            System.err.println("[w2h-push-offset-e2e] leftover: " + file.getAbsolutePath());
        }
    }
}
