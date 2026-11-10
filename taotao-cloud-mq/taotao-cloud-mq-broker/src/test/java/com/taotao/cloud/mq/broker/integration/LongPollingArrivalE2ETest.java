package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.broker.longpoll.PullRequestHoldService;
import com.taotao.cloud.mq.broker.processor.PullMessageProcessor;
import com.taotao.cloud.mq.client.consumer.DefaultMQPullConsumer;
import com.taotao.cloud.mq.client.producer.DefaultMQProducer;
import com.taotao.cloud.mq.common.MessageQueue;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.integration.support.ClusterTestHelper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * W2c 验收用例：把"Pull 长轮询 / 消息到达毫秒级响应"这句广告真的接上。
 * <p>
 * 形状（工单 §3.4）：起 broker → 对一个空队列发起<b>带挂起预算</b>的 pull（后台线程）→
 * <b>因果等到它真挂上了</b>（锚 = {@link PullRequestHoldService#hasSuspended}）→ 发 1 条 →
 * 该 pull 直接返回这条消息。
 * <p>
 * <b>三条断言各管一支反证</b>（顺序有讲究，先跑的是承重的）：
 * <ol>
 *   <li>{@code FOUND + 恰好那条消息} —— CP-A 的靶子。把 processor 里"挂起再重读"摘成立即返回空
 *       ⇒ 这条红，而且红在"pull 拿到了那条消息"上，不红在超时/异常上。</li>
 *   <li>{@code suspendWakeup == "message"} —— CP-B 的靶子。§3.1 规定"超时也要重读一次"，
 *       所以只断言 (1) 的话，把到达侧 notify 摘掉后 pull 会挂到到期、照样读到那条消息而假绿；
 *       醒来的<em>原因</em>才是到达侧唤醒的唯一可观测证据（摘掉唤醒点 ⇒ 变成 "timeout" ⇒ 红在这条）。</li>
 *   <li>{@code 用例期间它确实挂起过} —— 防"其实没真挂起"让上面两条一起假绿。</li>
 * </ol>
 * <p>
 * <b>不用挂钟阈值当判据</b>：本类所有等待都是"等到要断言的东西出现"（hold 表里出现登记 /
 * pull 带着结果回来）。bail-out 只把"没等到"报成失败，不会因为"到点了"而放行。
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LongPollingArrivalE2ETest {

    private static final Logger log = LoggerFactory.getLogger(LongPollingArrivalE2ETest.class);

    /** 到达侧用例给 broker 的挂起预算 (毫秒)：够长，用例不靠"卡到超时"通过。 */
    private static final long ARRIVAL_HOLD_BUDGET_MILLIS = 5_000L;

    /**
     * "没人叫我 ⇒ 只能到点自己醒"那条用例的预算：刻意取小，让"预算 + 一个扫描周期"远小于
     * 客户端按 §2.3 算出来的 RPC 超时，避免在负载重的机器上把这一腿读成客户端超时。
     */
    private static final long TIMEOUT_ONLY_HOLD_BUDGET_MILLIS = 500L;

    /** 因果等待的重查节奏（只是别把 CPU 转满，不是判据）。 */
    private static final long SUSPEND_POLL_INTERVAL_MILLIS = 10L;
    /** 挂起登记迟迟不出现就报失败的上限（bail-out，不是"等这么久就算过"）。 */
    private static final int MAX_SUSPEND_POLLS = 2_000;

    /** pull 线程最晚该带着结果回来的兜底线（远大于预算，只用于把"挂死"报成失败而不是挂住整个套件）。 */
    private static final long FUTURE_BAIL_OUT_SECONDS = 60L;

    private ClusterTestHelper helper;
    private DefaultMQProducer producer;
    private String brokerName;

    @BeforeAll
    public void startCluster() throws Exception {
        helper = new ClusterTestHelper();
        helper.startCluster();
        brokerName = helper.getBroker().getBrokerConfig().getBrokerName();
        producer = new DefaultMQProducer("W2C_LP_PRODUCER_" + UUID.randomUUID().toString().substring(0, 6));
        producer.setNamesrvAddr(helper.getNamesrvAddr());
        producer.start();
    }

    @AfterAll
    public void stopCluster() {
        if (producer != null) {
            try { producer.shutdown(); } catch (Exception ignore) { }
        }
        if (helper != null) {
            helper.shutdownCluster();
        }
    }

    // ==================== §3.4 主用例 ====================

    @Test
    @DisplayName("W2c: 带挂起预算的 pull 真挂在 hold 表上, 消息一到就把它叫醒并带回那条消息")
    public void suspendedPullIsWokenByMessageArrival() throws Exception {
        String topic = uniqueTopic("W2C_ARRIVAL");
        helper.createTopic(topic, 1, 1); // 单写队列 ⇒ producer 只能落到 queueId 0
        PullRequestHoldService hold = helper.getBroker().getPullRequestHoldService();
        assertNotNull(hold, "BrokerController 必须装配 PullRequestHoldService");

        DefaultMQPullConsumer consumer = newPullConsumer();
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            final MessageQueue mq = new MessageQueue(topic, brokerName, 0);
            Future<DefaultMQPullConsumer.PullResult> pull = pool.submit(
                    () -> consumer.pull(mq, 0L, 16, ARRIVAL_HOLD_BUDGET_MILLIS));

            boolean suspended = awaitSuspendedOrReportAbsent(topic, 0);
            String body = "w2c-body-" + topic;
            assertEquals(SendStatus.SEND_OK, sendOne(topic, body).getSendStatus(),
                    "发送必须成功, 否则到达侧根本没有可唤醒的东西");

            DefaultMQPullConsumer.PullResult result = pull.get(FUTURE_BAIL_OUT_SECONDS, TimeUnit.SECONDS);

            // ---- 断言 1 (CP-A 靶子): 这条挂起的 pull 直接返回了那条消息 ----
            assertNotNull(result, "pull 必须有结果");
            assertEquals(1, result.getMsgFoundList().size(),
                    "挂起的 pull 必须被到达的消息叫醒并带回那条消息（实际拿到 " + result.getMsgFoundList().size()
                            + " 条, status=" + result.getStatus() + ", suspendWakeup="
                            + result.getSuspendWakeup() + "）");
            assertEquals(DefaultMQPullConsumer.PullStatus.FOUND, result.getStatus(),
                    "带挂起预算的 pull 拿到消息时状态必须是 FOUND");
            assertEquals(body, new String(result.getMsgFoundList().get(0).getBody(), StandardCharsets.UTF_8),
                    "带回的必须是刚写进去的那一条");
            assertEquals(1L, result.getNextOffset(), "nextOffset 必须推进到那条消息之后");

            // ---- 断言 2 (CP-B 靶子): 醒的原因是"消息到达", 不是"挂到超时顺便读到" ----
            assertEquals(PullMessageProcessor.WAKEUP_BY_MESSAGE, result.getSuspendWakeup(),
                    "suspendWakeup 必须是 message：§3.1 规定超时后也重读一次, 所以只有这个字段能证明"
                            + "是到达侧的 notifyMessageArrived 放开了这条 pull（摘掉唤醒点它会变成 timeout）");

            // ---- 断言 3: 用例期间它确实挂起过, 否则上面两条是在测短轮询 ----
            assertTrue(suspended, "pull 从未登记进 hold 表 ⇒ 本用例没在测长轮询（topic=" + topic + "）");
            assertFalse(hold.hasSuspended(topic, 0), "返回之后该队列上不该再留着挂起登记（登记必须被摘干净）");
            assertEquals(0, hold.totalHoldCount(), "hold 表必须清空, 不许漏条目");
        } finally {
            pool.shutdownNow();
            consumer.shutdown();
        }
    }

    // ==================== 队列定向 + §2.3 不变式的动态腿 ====================

    @Test
    @DisplayName("W2c: 别的队列到消息不算唤醒; 到点自己醒也必须带回答复, 而不是让客户端超时")
    public void suspendedPullIsNotWokenByOtherQueueAndStillAnswersOnTimeout() throws Exception {
        String topic = uniqueTopic("W2C_TARGET");
        helper.createTopic(topic, 8, 1); // 写队列只有 0, 但允许对 queueId 7 发起 pull（它永远是空的）
        PullRequestHoldService hold = helper.getBroker().getPullRequestHoldService();

        DefaultMQPullConsumer consumer = newPullConsumer();
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            final MessageQueue untouched = new MessageQueue(topic, brokerName, 7);
            Future<DefaultMQPullConsumer.PullResult> pull = pool.submit(
                    () -> consumer.pull(untouched, 0L, 16, TIMEOUT_ONLY_HOLD_BUDGET_MILLIS));

            assertTrue(awaitSuspendedOrReportAbsent(topic, 7), "queueId 7 上的 pull 必须真挂起");
            // 同 topic 的 queueId 0 到一条消息 —— hold 表按 topic@queueId 分组, 不该放开 queueId 7
            assertEquals(SendStatus.SEND_OK, sendOne(topic, "w2c-target-" + topic).getSendStatus(),
                    "queueId 0 的发送必须成功");

            DefaultMQPullConsumer.PullResult result = pull.get(FUTURE_BAIL_OUT_SECONDS, TimeUnit.SECONDS);

            // 唤醒点解错队列（按 topic 一把唤醒 / 从 body 反解出错的 queueId）会红在这里
            assertEquals(PullMessageProcessor.WAKEUP_BY_TIMEOUT, result.getSuspendWakeup(),
                    "queueId 7 从来没写过消息, 它只能因超时而醒; 变成 message 说明唤醒点把别的队列的"
                            + "到达算到了它头上");
            assertEquals(0, result.getMsgFoundList().size(), "空队列到点返回必须是空结果");
            assertEquals(DefaultMQPullConsumer.PullStatus.NO_NEW_MSG, result.getStatus(),
                    "到点自己醒也要拿到真响应, 不能是错误状态");
            // §2.3 的动态腿: 上面两条能成立, 就说明"broker 挂起时长(=请求预算) 严格小于客户端 RPC 超时"
            // —— 反过来 (工单里那对 3000ms 客户端 / 15000ms 默认挂起) 这一腿只会以异常收场。
            assertEquals(0, hold.totalHoldCount(), "到点返回后 hold 表必须清空");
        } finally {
            pool.shutdownNow();
            consumer.shutdown();
        }
    }

    // ==================== 不带预算 ⇒ 逐字不变 ====================

    @Test
    @DisplayName("W2c: 不带挂起预算的 pull 不进 hold 表, 也不抢长轮询的位置")
    public void pullWithoutBudgetNeverSuspend() throws Exception {
        String topic = uniqueTopic("W2C_NO_BUDGET");
        helper.createTopic(topic, 1, 1);
        PullRequestHoldService hold = helper.getBroker().getPullRequestHoldService();

        DefaultMQPullConsumer consumer = newPullConsumer();
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            final MessageQueue mq = new MessageQueue(topic, brokerName, 0);
            // 先放一条带预算的 pull 进 hold 表, 让"登记数没变"成为可观测的读数
            Future<DefaultMQPullConsumer.PullResult> held = pool.submit(
                    () -> consumer.pull(mq, 0L, 16, ARRIVAL_HOLD_BUDGET_MILLIS));
            assertTrue(awaitSuspendedOrReportAbsent(topic, 0), "作为对照的长轮询 pull 必须挂起");
            assertEquals(1, hold.totalHoldCount(), "对照: 此刻 hold 表上只有那一条长轮询");

            DefaultMQPullConsumer.PullResult shortPoll = consumer.pull(mq, 0L, 16);
            assertEquals(DefaultMQPullConsumer.PullStatus.NO_NEW_MSG, shortPoll.getStatus(),
                    "不带预算: 空队列立刻返回 NO_NEW_MSG");
            assertNull(shortPoll.getSuspendWakeup(),
                    "不带预算: broker 不该走挂起分支（响应里出现 suspendWakeup 就说明短轮询被拖进了长轮询语义）");
            assertEquals(1, hold.totalHoldCount(),
                    "不带预算的 pull 不许往 hold 表里加登记（变成 2 说明它也被挂起了）");
            assertEquals(1, hold.queueKeyCount(), "也不许新开一个 key");

            // 收尾: 给那条对照 pull 一条真消息, 让它"因到达而醒"而不是被中断 ——
            // notifyMessageArrived 是同步摘登记的, 所以 sendOne 返回后读数就已经是 0。
            sendOne(topic, "w2c-drain-" + topic);
            DefaultMQPullConsumer.PullResult drained = held.get(FUTURE_BAIL_OUT_SECONDS, TimeUnit.SECONDS);
            assertEquals(PullMessageProcessor.WAKEUP_BY_MESSAGE, drained.getSuspendWakeup(),
                    "对照 pull 也该被那条消息叫醒");
            assertEquals(0, hold.totalHoldCount(), "唤醒之后 hold 表必须已经清空（不许漏登记）");
        } finally {
            pool.shutdownNow();
            consumer.shutdown();
        }
    }

    // ==================== 工具 ====================

    /**
     * 因果等待 hold 表出现该队列的登记。
     *
     * @return true=真的挂上了; false=到 bail-out 上限仍没挂上（调用方必须把 false 当成失败报出来，
     *         本类由"确实挂起过"那条断言负责红）
     */
    private boolean awaitSuspendedOrReportAbsent(String topic, int queueId) throws InterruptedException {
        PullRequestHoldService hold = helper.getBroker().getPullRequestHoldService();
        for (int polls = 0; polls < MAX_SUSPEND_POLLS; polls++) {
            if (hold.hasSuspended(topic, queueId)) {
                return true;
            }
            Thread.sleep(SUSPEND_POLL_INTERVAL_MILLIS);
        }
        log.warn("hold 表始终没出现 topic={} queueId={} 的登记 (totalHoldCount={}, queueKeyCount={})",
                topic, queueId, hold.totalHoldCount(), hold.queueKeyCount());
        return false;
    }

    private DefaultMQPullConsumer newPullConsumer() throws Exception {
        DefaultMQPullConsumer consumer =
                new DefaultMQPullConsumer("W2C_LP_CONSUMER_" + UUID.randomUUID().toString().substring(0, 6));
        consumer.setNamesrvAddr(helper.getNamesrvAddr());
        consumer.start();
        return consumer;
    }

    private SendResult sendOne(String topic, String body) throws Exception {
        return producer.send(new Message(topic, body.getBytes(StandardCharsets.UTF_8)));
    }

    private static String uniqueTopic(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
