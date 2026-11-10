package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.broker.BrokerConfig;
import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.client.consumer.ConsumeConcurrentlyStatus;
import com.taotao.cloud.mq.client.consumer.DefaultMQPushConsumer;
import com.taotao.cloud.mq.client.consumer.MessageListener;
import com.taotao.cloud.mq.client.consumer.MessageQueueContext;
import com.taotao.cloud.mq.client.consumer.retry.BrokerBackedRetryTransport;
import com.taotao.cloud.mq.client.consumer.retry.ConsumeRetryService;
import com.taotao.cloud.mq.client.consumer.retry.DeadLetterQueue;
import com.taotao.cloud.mq.common.BrokerData;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.nameserver.NameServerController;
import com.taotao.cloud.mq.nameserver.NamesrvConfig;
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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 「消费失败重投」与「次数用尽进死信」这两句话的端到端兑现：重投的副本经真 broker 落进存储、
 * 次数跟着消息活下来（换进程也接着数），次数用尽时消息真发到 {@code %DLQ%{consumerGroup}}，
 * 另起一个消费者能把它读回来。
 * <p>
 * 三个用例各自钉住一条边，缺一不可：
 * <ol>
 *   <li>{@link #retryCopySurvivesBrokerRestartAndContinuesFromTheNextAttempt()} —— 落盘这条边。
 *       重投副本必须真进到 CommitLog（判据是它被另一次 pull <b>从 broker 读回来</b>了，
 *       而不是在内存里转了个圈），并且换一个全新的 BrokerController（同一存储目录）之后
 *       仍然读得到自己那一级。</li>
 *   <li>{@link #doomedMessageLandsOnTheDeadLetterTopicWhereAnotherConsumerReadsIt()} —— 死信这条边。
 *       判据是「另一个消费者从死信 Topic 上读到了这一条」，不是「某个内存队列 size() 加了 1」。</li>
 *   <li>{@link #deadLetterWithoutRouteFallsBackInsteadOfVanishing()} —— 路由缺失时的证据。
 *       Topic 没在 NameServer 上注册过时外投通路量不到路由 ⇒ 消息必须退回进程内兜底，
 *       而不是「发出去了但其实没人收」。位点因此不提交 ⇒ 同一份副本必然被反复重拉；
 *       这一例的因果点是「同一份第 16 级副本被投到第三次」—— 快照记在监听器入口、
 *       记账发生在监听器返回之后（同一个 pull 线程的顺序调用），所以只有等再下一次投递可见，
 *       前两次的记账才必然已经落完。钉的是「同一条消息转入死信」只记一笔账、
 *       缓存里不许被它自己的副本挤出一第二份。</li>
 * </ol>
 * <b>口径说明</b>：
 * <ul>
 *   <li>「杀掉 consumer」= 真 {@code shutdown()} 之后再 {@code start()} 一个新实例（全新的
 *       {@code MQClientInstance}、全新的进程内状态），并把 broker 也换成同一存储目录上的新实例 ——
 *       重投的级数只能由消息自己带回来，不许由任何还活着的对象带回来。</li>
 *   <li>等异步一律是因果等待：每一次「回调落地」都是被测线程往阻塞队列里放的那一条快照，
 *       等待只给「这事根本没发生」一个有限的出路，判据是 msgId 与次数的值，不是时间。</li>
 *   <li>位点是 at-least-once 的（没提交成功就不前移），所以重启后低级别的副本会被再读一次；
 *       本文件不拿「读了几次」当判据，只拿「每一条读回来的副本带回来的级数等不等于它自己那一级」当判据。</li>
 *   <li>生产默认的重投次数（16）与检查周期（1s）不由本文件加速：这里注入的是间隔策略与检查周期，
 *       那两个生产默认值由 client 模块的契约用例单独钉住。</li>
 *   <li>存储路径 {@code storePathRootDir} 与 {@code storePathCommitLog} <b>都</b>显式设进
 *       {@code @TempDir}（只设一个会让 commitlog 逃逸到 {@code ~/store}，本仓踩过 2.0 GB 的账）。</li>
 * </ul>
 */
public class RetryDeadLetterE2ETest {

    /** 一次回调/一次读回的等待上限；只用于给出「这件事没发生」的可读失败，不承载任何数值判定. */
    private static final long BAIL_OUT_MILLIS = 90_000L;

    private static final String BROKER_NAME = "RetryDlqBroker";
    private static final String CLUSTER_NAME = "RetryDlqCluster";
    private static final int QUEUE_ID = 0;

    private static final String TOPIC_RETRY = "RetryRoundTripTopic";
    private static final String RETRY_GROUP = "retry-roundtrip-group";
    private static final String TOPIC_DOOMED = "DoomedTopic";
    private static final String DOOMED_GROUP = "doomed-group";
    private static final String DLQ_MONITOR_GROUP = "dlq-monitor";
    private static final String TOPIC_NO_ROUTE = "NeverRegisteredTopic";
    private static final String NOROUTE_GROUP = "noroute-group";

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

    // ==================== 1. 重投副本落盘 + 跨重启接着数 ====================

    @Test
    @DisplayName("重投副本经真 broker 落盘：换新进程（同一存储目录）之后仍按第 N+1 次继续，而不是从 0 开始")
    public void retryCopySurvivesBrokerRestartAndContinuesFromTheNextAttempt() throws Exception {
        startCluster();
        registerTopic(TOPIC_RETRY);

        Recorder first = new Recorder("第一轮");
        first.failForever();
        DefaultMQPushConsumer consumer = startConsumer(RETRY_GROUP, TOPIC_RETRY, first, 16);

        sendOne(TOPIC_RETRY, "RT-1", "rt-body");

        // ---- 第 0 级：producer 投进来的那一条，既不是重投也没带次数 ----
        Snapshot origin = first.awaitWithLevel(0, "原始消息第一次被投到");
        assertEquals("RT-1", origin.msgId, "第一条就是发进去的那一条");
        assertFalse(origin.isRetry,
                "★ 普通消息（没走过重投通路）读出来 isRetry() 必须是 false —— 只测 true 那一头等于没测");
        assertEquals(0, origin.reconsumeTimes, "没重投过的消息次数字段是 0");

        // ---- 第 1、2 级：这两条都是被 pull 从 broker 读回来的副本，不是内存里转的圈 ----
        Snapshot level1 = first.awaitWithLevel(1, "第一次重投的副本");
        assertEquals("RT-1#R1", level1.msgId, "副本的 msgId 要能认出它是第几级");
        assertTrue(level1.isRetry,
                "★ 重投出去的副本绕 broker 一圈回来之后, isRetry() 必须读得出 true");
        assertEquals("rt-body", level1.body, "副本的正文不许在落盘再读回来的路上变形");
        first.awaitWithLevel(2, "第二次重投的副本");

        // ---- 杀掉这个 consumer（进程内状态全没了），再把 broker 换成同一存储目录上的新实例 ----
        consumer.shutdown();
        launchedConsumers.remove(consumer);
        BrokerController alive = launchedBrokers.remove(launchedBrokers.size() - 1);
        alive.shutdown();
        startBrokerOnSameStore();
        awaitRegisteredBrokerAddr(TOPIC_RETRY);

        Recorder afterRestart = new Recorder("重启之后");
        afterRestart.failForever();
        DefaultMQPushConsumer restarted = startConsumer(RETRY_GROUP, TOPIC_RETRY, afterRestart, 16);
        try {
            // 全新的进程读到「这一条已经是第 2 次重投」—— 计数是消息带上来的，不是进程里的
            afterRestart.awaitWithLevel(2, "重启之后从存储里读回第 2 级副本");
            // 它又失败一次 ⇒ 下一跳必须是第 3 次，而不是回到 0
            afterRestart.awaitWithLevel(3, "重启之后按第 3 次继续重投");

            // ★ 承重断言：摘掉它，"副本穿存储/重启之后级数被重置回 0"这类回归就没人管了 ——
            // 每一条读回来的副本带回来的级数都必须等于它自己那一级
            int copies = 0;
            for (Snapshot s : afterRestart.all()) {
                int levelInId = levelEncodedIn(s.msgId);
                if (levelInId < 0) {
                    continue; // 原始消息本身（没被重投过）不参与这条判定
                }
                copies++;
                assertEquals(levelInId, s.reconsumeTimes,
                        "★ 副本 " + s.msgId + " 从 broker 读回来时带回来的次数必须等于它自己那一级 "
                                + levelInId + "，实测=" + s.reconsumeTimes
                                + " —— 读成 0 就说明次数没跟着消息进存储,「最多 16 次」会变成不封顶");
            }
            assertTrue(copies >= 2,
                    "重启之后至少要有两份带级数的副本被读回来, 实测=" + copies);
            assertTrue(afterRestart.maxLevelSeen() >= 3,
                    "重启之后的级数要能走到 3, 实测最高=" + afterRestart.maxLevelSeen());
        } finally {
            restarted.shutdown();
            launchedConsumers.remove(restarted);
        }
    }

    // ==================== 2. 次数用尽：消息真发到 %DLQ%{group}，另一个消费者读得回 ====================

    @Test
    @DisplayName("必死的消息重投到上限之后，另一个消费者从 %DLQ%{group} 上把它读回来，次数是 16 且原因可读")
    public void doomedMessageLandsOnTheDeadLetterTopicWhereAnotherConsumerReadsIt() throws Exception {
        startCluster();
        registerTopic(TOPIC_DOOMED);
        String dlqTopic = DeadLetterQueue.DLQ_TOPIC_PREFIX + DOOMED_GROUP;
        // 产品侧今天没有「client 自动建 topic」的入口（路由面那条账），死信 Topic 仍然要
        // 走既有的管理请求注册。这一步本身就是「订阅死信 Topic」这句话还差一环的证据。
        registerTopic(dlqTopic);
        awaitRegisteredBrokerAddr(dlqTopic);

        Recorder monitor = new Recorder("死信订阅方"); // 回成功：它只负责读回来
        DefaultMQPushConsumer watcher = startConsumer(DLQ_MONITOR_GROUP, dlqTopic, monitor, 16);

        Recorder doomed = new Recorder("必死的消费方");
        doomed.failForever();
        DefaultMQPushConsumer killer = startConsumer(DOOMED_GROUP, TOPIC_DOOMED, doomed, 16);
        try {
            assertEquals(dlqTopic, killer.getDeadLetterTopic(),
                    "消费端问得出自己那一份死信 Topic 的名字（订阅方要填的就是这个名）");

            sendOne(TOPIC_DOOMED, "DLQ-1", "doomed-body");

            Snapshot dead = monitor.awaitAny("死信 Topic 上读到那一条必死的消息");
            assertEquals("DLQ-1#DLQ", dead.msgId,
                    "★ 判据是「另一个消费者从死信 topic 上读到了这一条」, 实测 msgId=" + dead.msgId);
            assertEquals(16, dead.reconsumeTimes,
                    "★ 进死信那一条带回来的次数必须是 16（用尽）, 实测=" + dead.reconsumeTimes
                            + "；读成 0 就说明副本外投时次数没跟着进存储");
            assertTrue(dead.isRetry, "死信里那条也认得出自己是重投来的");
            assertEquals("doomed-body", dead.body, "正文必须还是那一条");
            assertEquals(TOPIC_DOOMED, dead.originalTopic,
                    "死信得说得出它原本死在哪个 topic 上（属性 RETRY_TOPIC）");
            assertNotNull(dead.reason, "转入死信的原因要随消息一起读得回来");
            assertTrue(dead.reason.contains("Exceeded max reconsume times: 16"),
                    "原因要点名是「次数用尽」, 实测=" + dead.reason);

            // 16 级确实一级不落地走过来了
            assertEquals(16, doomed.maxLevelSeen(),
                    "消费侧观察到的级数必须一路走到 16, 实测=" + doomed.maxLevelSeen());
            assertEquals(1, doomed.countAtLevel(0), "原始消息只投过一次, 级数序列不许掺水");

            // 内存那份只是缓存/统计：真发出去的那条不该再被它当第二真相留着。
            // 这一笔账是 putMessage 在 publish 返回之后才涨的，而上面那个因果点（另一个消费者从
            // 死信 topic 上读到了这条）落在 publish 里面 —— 读得到不等于账已经落完 ⇒ 直接等这个计数本身。
            DeadLetterQueue dlq = killer.getConsumeRetryService().getDeadLetterQueue();
            awaitTrue("消费端自己的死信记账落下（totalDeadLetters 到 1）", new Condition() {
                @Override
                public boolean holds() {
                    return dlq.getTotalDeadLetters() >= 1L;
                }
            });
            assertEquals(1, dlq.getTotalDeadLetters(), "统计计数记下这一条");
            assertEquals(0, dlq.size(),
                    "★ 已经落到死信 topic 上的那一条不该同时是进程内缓存的内容（否则两处真相）");

            // 死信之后不再往外重投：第 17 次不存在
            assertTrue(doomed.noLevelAbove(16),
                    "次数用尽之后不许再出现第 17 次, 实测最高=" + doomed.maxLevelSeen());
            assertEquals(1, monitor.totalSeen(),
                    "死信 topic 上这一条只该被读到一次, 多出来就是死信自己又被重投了一遍");
        } finally {
            killer.shutdown();
            launchedConsumers.remove(killer);
            watcher.shutdown();
            launchedConsumers.remove(watcher);
        }
    }

    // ==================== 3. 路由缺失时的证据：退回兜底而不是凭空消失 ====================

    @Test
    @DisplayName("死信 Topic 没注册（量不到路由）时：消息退回进程内兜底，且路由缺失这件事本身是可证的")
    public void deadLetterWithoutRouteFallsBackInsteadOfVanishing() throws Exception {
        startCluster();
        registerTopic(TOPIC_NO_ROUTE);
        String dlqTopic = DeadLetterQueue.DLQ_TOPIC_PREFIX + NOROUTE_GROUP;
        assertTrue(routeAbsent(dlqTopic),
                "前提：这个死信 topic 从没注册过 ⇒ NameServer 量不到它的路由"
                        + "（产品侧没有 client 自动建 topic 的入口，这一条就是那条账的证据）");

        Recorder doomed = new Recorder("没路由的那一轮");
        doomed.failForever();
        DefaultMQPushConsumer consumer = startConsumer(NOROUTE_GROUP, TOPIC_NO_ROUTE, doomed, 16);
        try {
            sendOne(TOPIC_NO_ROUTE, "NR-1", "noroute-body");
            // 重投副本回的是已注册的原始 topic, 所以 16 级照样一级不落地数上来
            Snapshot last = doomed.awaitWithLevel(16, "死信没路由时仍然要一级一级数到 16");
            assertTrue(last.isRetry, "走到用尽的这一条当然认得出自己是重投的");

            DeadLetterQueue dlq = consumer.getConsumeRetryService().getDeadLetterQueue();
            // 因果点：等「同一份第 16 级副本被投到第三次」这件事真的发生。
            // 这条通路的前提是死信 topic 量不到路由 ⇒ 外投必然失败 ⇒ 位点不提交 ⇒ 那一条必然被反复重拉
            // （既有的 at-least-once 语义）。等的是投递事件本身，不是「计数器涨起来了」那个瞬态读数。
            //
            // 为什么是第三次而不是第二次：投递快照是在监听器入口记下的（consumeMessage 的头一件事），
            // 而「转入死信」的记账发生在监听器返回之后（DefaultMQPushConsumer 的 pull 回调里
            // consumeMessage(...) → handleConsumeFailure(...) 是同一个 pull 线程上的顺序调用）。
            // 所以「第 N+1 条快照已经可见」才意味着「第 N 次的记账必然已经落完」；
            // 只等到第二次就断言，量的还是第二次记账抢不抢得到，那仍然是和 race 赛跑。
            doomed.awaitDeliveries(16, 3,
                    "死信没路由 ⇒ 位点不提交 ⇒ 同一份第 16 级副本必须被重拉到第三次");
            int redeliveries = doomed.countAtLevel(16);
            assertTrue(redeliveries >= 3,
                    "Recorder 的账要认得出「至少投过三次」, 实测=" + redeliveries);

            // 前两次重拉的记账此时都已经落完了 ⇒ 「同一条消息转入死信」若不幂等，
            // 记账与缓存必然已经被它自己的副本撑开。
            assertEquals(1, dlq.getTotalDeadLetters(),
                    "★ 同一条消息转入死信只记一笔账：第 16 级已经被投了 " + redeliveries
                            + " 次（前 " + (redeliveries - 1) + " 次的记账必然已落完），"
                            + "计数不许涨到 " + (redeliveries - 1) + "（实测=" + dlq.getTotalDeadLetters() + "）");
            assertEquals(1, dlq.size(),
                    "★ 发不出去的那一条必须留在进程内兜底（否则这条消息既没进 topic 也没人记得, 是凭空消失）,"
                            + " 且不许被它自己的副本挤掉；重投 " + redeliveries + " 次之后缓存里实测="
                            + dlq.size() + " 份");
            assertEquals(0, dlq.getTotalExpired(),
                    "同一条消息的自重复不许被记成「过期清理」, 实测 totalExpired=" + dlq.getTotalExpired());
            List<DeadLetterQueue.DeadLetterMessage> cached = dlq.pollAll();
            assertEquals(1, cached.size());
            assertEquals(16, cached.get(0).getReconsumeTimes());
            assertFalse(cached.get(0).isPublished(), "缓存里那条要标得出来「它没进死信 topic」");
            assertTrue(routeAbsent(dlqTopic), "收尾再看一次：全程没有任何东西替它建过路由");

        } finally {
            consumer.shutdown();
            launchedConsumers.remove(consumer);
        }
    }

    // ==================== 回调侧：把每一条落地当成因果事件记下来 ====================

    /** 一条回调落地的快照：级数、msgId、isRetry、正文、死信原因、原本的 topic. */
    private static final class Snapshot {
        final String msgId;
        final int reconsumeTimes;
        final boolean isRetry;
        final String body;
        final String reason;
        final String originalTopic;

        Snapshot(MessageExt msg) {
            this.msgId = msg.getMsgId();
            this.reconsumeTimes = msg.getReconsumeTimes();
            this.isRetry = msg.isRetry();
            this.body = msg.getBody() == null ? null : new String(msg.getBody(), StandardCharsets.UTF_8);
            this.reason = msg.getProperty(BrokerBackedRetryTransport.PROPERTY_DEAD_LETTER_REASON);
            this.originalTopic = msg.getProperty(ConsumeRetryService.PROPERTY_ORIGINAL_TOPIC);
        }
    }

    /** 一律回「失败」的监听器（订阅死信那一路可以留着回成功）。 */
    private static final class Recorder implements MessageListener.Concurrently {
        private final BlockingQueue<Snapshot> arrivals = new LinkedBlockingQueue<Snapshot>();
        private final List<Snapshot> all = new CopyOnWriteArrayList<Snapshot>();
        private final String who;
        private volatile boolean succeed = true;

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
            return succeed ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS
                    : ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        void failForever() {
            this.succeed = false;
        }

        /** 等到「某一条以第 level 级被投到」这件事发生；取出来的快照原样还回去, 等待不改变账. */
        Snapshot awaitWithLevel(int level, String what) {
            List<Snapshot> buffered = new ArrayList<Snapshot>();
            long deadline = System.currentTimeMillis() + BAIL_OUT_MILLIS;
            try {
                while (System.currentTimeMillis() < deadline) {
                    Snapshot s = arrivals.poll(200L, TimeUnit.MILLISECONDS);
                    if (s == null) {
                        continue;
                    }
                    buffered.add(s);
                    if (s.reconsumeTimes == level) {
                        arrivals.addAll(buffered);
                        return s;
                    }
                    if (s.reconsumeTimes > level) {
                        arrivals.addAll(buffered);
                        throw new AssertionError("等第 " + level + " 级时先看到了第 " + s.reconsumeTimes
                                + " 级（" + s.msgId + "）：级数是跳着走的, " + what);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            arrivals.addAll(buffered);
            throw new AssertionError("等满 " + BAIL_OUT_MILLIS + "ms 也没等到第 " + level
                    + " 级被投到：" + what + " ; 已经看到的=" + describe(all));
        }

        /** 等「第 level 级被投到第 wanted 次」这件事由投递线程自己写下：判据是 Recorder 记下的投递条数, 不是时间. */
        void awaitDeliveries(int level, int wanted, String what) {
            long deadline = System.currentTimeMillis() + BAIL_OUT_MILLIS;
            while (countAtLevel(level) < wanted) {
                if (System.currentTimeMillis() >= deadline) {
                    fail("等满 " + BAIL_OUT_MILLIS + "ms，第 " + level + " 级只被投到 "
                            + countAtLevel(level) + " 次（想要至少 " + wanted + "）：" + what
                            + " ; 已经看到的=" + describe(all));
                }
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    fail("等待被中断: " + what);
                }
            }
        }

        Snapshot awaitAny(String what) {
            try {
                Snapshot s = arrivals.poll(BAIL_OUT_MILLIS, TimeUnit.MILLISECONDS);
                assertNotNull(s, "一条都没读到：" + what);
                return s;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return fail("等待被中断: " + what);
            }
        }

        List<Snapshot> all() {
            return all;
        }

        int totalSeen() {
            return all.size();
        }

        int maxLevelSeen() {
            int max = -1;
            for (Snapshot s : all) {
                max = Math.max(max, s.reconsumeTimes);
            }
            return max;
        }

        int countAtLevel(int level) {
            int n = 0;
            for (Snapshot s : all) {
                if (s.reconsumeTimes == level) {
                    n++;
                }
            }
            return n;
        }

        boolean noLevelAbove(int ceiling) {
            for (Snapshot s : all) {
                if (s.reconsumeTimes > ceiling) {
                    return false;
                }
            }
            return true;
        }

        private String describe(List<Snapshot> list) {
            StringBuilder sb = new StringBuilder();
            for (Snapshot s : list) {
                sb.append(s.msgId).append('@').append(s.reconsumeTimes).append(' ');
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            return "Recorder{" + who + "}";
        }
    }

    /** 副本 msgId 里编的级数：{@code RT-1#R3 -> 3}；原始消息（没有 #R 后缀）返回 -1. */
    private static int levelEncodedIn(String msgId) {
        if (msgId == null) {
            return -1;
        }
        int at = msgId.lastIndexOf("#R");
        if (at < 0 || at + 2 >= msgId.length()) {
            return -1;
        }
        try {
            return Integer.parseInt(msgId.substring(at + 2));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

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
        String root = tempDir.resolve("broker").toString();
        msc.setStorePathRootDir(root);
        msc.setStorePathCommitLog(root + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        msc.setFlushDiskType(FlushDiskType.SYNC_FLUSH);

        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName(BROKER_NAME);
        bc.setBrokerClusterName(CLUSTER_NAME);
        bc.setNamesrvAddr(namesrvAddr);

        int port = freePort();
        NettyServerConfig nsc = new NettyServerConfig();
        nsc.setListenPort(port);

        BrokerController broker = new BrokerController(bc, msc, nsc);
        assertTrue(broker.initialize(), "BrokerController.initialize() 必须成功（真 load）");
        broker.start();
        launchedBrokers.add(broker);
        brokerAddr = "localhost:" + port;
    }

    /**
     * 起一个真 push consumer.
     *
     * @param maxReconsumeTimes 显式传生产的 16，让「最多 16 次」这句话本身在承重范围内
     */
    private DefaultMQPushConsumer startConsumer(String group, String topic, Recorder recorder,
                                                int maxReconsumeTimes) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setPullIntervalMillis(20L);
        consumer.setMaxReconsumeTimes(maxReconsumeTimes);
        // 快进的是「什么时候算到期」，不是生产默认值本身（那两个数由 client 侧的契约用例钉住）
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

    /** 这个 topic 在 NameServer 上量不出任何可连的 broker. */
    private boolean routeAbsent(String topic) throws Exception {
        return !hasBroker(fetchRoute(topic));
    }

    /** 等路由里出现「现在真正活着的那台 broker」：这是重启之后的同步点, 不是判据. */
    private void awaitRegisteredBrokerAddr(String topic) throws Exception {
        final String want = brokerAddr;
        awaitTrue("topic=" + topic + " 的路由里出现重启后的 broker 地址 " + want, new Condition() {
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
        inbound.setTags("retry-tag");
        inbound.setKeys(msgId);
        inbound.setBody(body.getBytes(StandardCharsets.UTF_8));
        inbound.setBornTimestamp(1700000000000L);

        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", topic);
        req.addExtField("queueId", String.valueOf(QUEUE_ID));
        req.addExtField("brokerName", BROKER_NAME);
        req.addExtField("msgId", msgId);
        req.setBody(JsonCodec.encode(inbound));

        RemotingCommand resp = askBroker(req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(), "发送必须被应答: " + msgId);
        SendResult result = JsonCodec.decode(resp.getBody(), SendResult.class);
        assertNotNull(result, "发送响应体必须能解出 SendResult");
        assertEquals(SendStatus.SEND_OK, result.getSendStatus(), "必须 SEND_OK: " + msgId);
    }

    private RemotingCommand askBroker(RemotingCommand request) throws Exception {
        final String topic = request.getExtField("topic");
        awaitTrue("发送前 topic=" + topic + " 要有路由", new Condition() {
            @Override
            public boolean holds() {
                try {
                    return hasBroker(fetchRoute(topic));
                } catch (Exception e) {
                    return false;
                }
            }
        });
        TopicRouteData route = fetchRoute(topic);
        String addr = route.getBrokerDatas().get(0).selectBrokerAddr();
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
            System.err.println("[retry-dlq-e2e] leftover: " + file.getAbsolutePath());
        }
    }
}
