package com.taotao.cloud.mq.consistency.ttc.client.consumer.retry;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * 「消费失败 → 重投 → 次数单调递增 → 用尽转死信」这条通路今天的行为契约。
 * <p>
 * 这一组用例的目的不是证明某个新写的东西能用，而是把<b>已经在跑</b>的那条通路钉成可断言的事实：
 * 它现在绿，以后有人改坏了才会红。判据全部是因果的 ——
 * 每一次「到期重投」的因果点就是回调线程往队列里放的那一条快照，「外投发生没发生」的因果点就是
 * 发布通路被叫到的那一次；而死信自己的计数与缓存是 {@link DeadLetterQueue#putMessage} 在 publish
 * <b>返回之后</b>才写的（写它的是调度线程），所以那两个读数是直接等它们本身，不拿外投事件当它们的替身。
 * 等待本身只给「这件事根本没发生」一个有限的出路，不承担任何数值判定。
 * 没有任何用例用 sleep 或挂钟阈值当结论。
 * <p>
 * 为了在合理的时间内跑完 16 级重投，这里注入的是「到点即投 + 每 5ms 扫一次」这套参数。
 * 生产默认值由 {@link #productionDefaultsAreStillOneSecondTickAndSixteenAttempts()} 单独钉住：
 * 想快可以注入，但生产那两套数不许跟着测试改。
 */
public class ConsumeRetryServiceContractTest {

    /** 一次回调预算的等待上限；只用于「事件根本没发生」时给出可读的失败，不参与任何数值判定. */
    private static final long BAIL_OUT_MILLIS = 30_000L;

    private static final String GROUP = "contract-group";
    private static final String TOPIC = "contract-topic";

    /** 一次回调落地时记下的快照：必须在回调那一刻读，否则读到的是这条消息最后的样子。 */
    private static final class Delivery {
        final int reconsumeTimes;
        final boolean isRetry;
        final String msgId;
        final String body;

        Delivery(MessageExt msg) {
            this.reconsumeTimes = msg.getReconsumeTimes();
            this.isRetry = msg.isRetry();
            this.msgId = msg.getMsgId();
            this.body = new String(msg.getBody(), StandardCharsets.UTF_8);
        }
    }

    /** 记录每一条落地的回调；默认一律回「失败」（＝还要再来一跳）。 */
    private static final class RecordingCallback implements ConsumeRetryService.RetryCallback {
        private final BlockingQueue<Delivery> deliveries = new LinkedBlockingQueue<Delivery>();
        private final boolean succeeds;

        RecordingCallback() {
            this(false);
        }

        RecordingCallback(boolean succeeds) {
            this.succeeds = succeeds;
        }

        @Override
        public boolean retryConsume(MessageExt message) {
            deliveries.add(new Delivery(message));
            return succeeds;
        }

        Delivery take(String what) {
            Delivery d = null;
            try {
                d = deliveries.poll(BAIL_OUT_MILLIS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assertNotNull(d, "到期之后再也没有回调过（等满 " + BAIL_OUT_MILLIS + "ms）：" + what);
            return d;
        }

        int pending() {
            return deliveries.size();
        }
    }

    /**
     * 记录「外投被叫到」这一次：它是「死信有没有往 Topic 发」的因果点。
     * <p>
     * 它<b>不是</b>死信计数与进程内缓存的因果点 —— 那两样是 publish 返回之后才写的，
     * 拿它当那两个读数的替身就是「等 A 断言 B」。
     */
    private static final class RecordingPublisher implements DeadLetterQueue.DlqPublisher {
        private final BlockingQueue<String> events = new LinkedBlockingQueue<String>();
        private final List<String> topics = new ArrayList<String>();
        private final List<Integer> times = new ArrayList<Integer>();
        private final List<String> reasons = new ArrayList<String>();
        private final boolean accepts;

        RecordingPublisher(boolean accepts) {
            this.accepts = accepts;
        }

        @Override
        public boolean publish(MessageExt message, int reconsumeTimes, String dlqTopic, String reason) {
            topics.add(dlqTopic);
            times.add(Integer.valueOf(reconsumeTimes));
            reasons.add(reason);
            events.add(message.getMsgId());
            return accepts;
        }

        void awaitOne(String what) {
            String id = null;
            try {
                id = events.poll(BAIL_OUT_MILLIS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assertNotNull(id, "始终没有转入死信（等满 " + BAIL_OUT_MILLIS + "ms）：" + what);
        }
    }

    /**
     * 等「死信记账落下」这件事本身，而不是等它的邻居。
     * <p>
     * {@link DeadLetterQueue#putMessage} 的次序是「先外投，publish 返回之后才涨计数、才进缓存」，
     * 而调度线程跑完这一趟就走了，后面不再有任何事件可等 —— 所以断言那两个值之前必须直接等它们，
     * 否则读到的就是「还没写完」的中间状态（慢机器上是 0，快机器上碰巧是 1）。
     */
    private static void awaitDeadLetterBooked(DeadLetterQueue dlq, long wanted, String what) {
        long deadline = System.currentTimeMillis() + BAIL_OUT_MILLIS;
        while (dlq.getTotalDeadLetters() < wanted || dlq.size() < wanted) {
            if (System.currentTimeMillis() >= deadline) {
                fail("等满 " + BAIL_OUT_MILLIS + "ms 死信记账仍没落下：" + what
                        + "（实测 totalDeadLetters=" + dlq.getTotalDeadLetters()
                        + ", size=" + dlq.size() + "）");
            }
            try {
                Thread.sleep(5L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("等待被中断: " + what);
            }
        }
    }

    private static MessageExt message(String msgId) {
        MessageExt msg = new MessageExt();
        msg.setTopic(TOPIC);
        msg.setMsgId(msgId);
        msg.setBody(("payload-" + msgId).getBytes(StandardCharsets.UTF_8));
        msg.setBornTimestamp(1700000000000L);
        return msg;
    }

    /** 快进版服务：到点即投、5ms 扫一次；次数上限保持生产口径. */
    private static ConsumeRetryService fastService(String group) {
        return new ConsumeRetryService(group,
                ConsumeRetryService.DEFAULT_MAX_RECONSUME_TIMES, RetryPolicy.STEPPED, 0L,
                5L, new ConsumeRetryService.RetryDelayStrategy() {
                    @Override
                    public long nextDelayMillis(int reconsumeTimes) {
                        return 0L;
                    }
                });
    }

    // ==================== 1. 失败进重投队列，并当场把次数记在字段上 ====================

    @Test
    @DisplayName("消费失败：消息进重投队列，重投次数记在字段上并带出「这条是重投的」标记")
    public void failedConsumptionIsQueuedWithTheAttemptNumberOnTheField() {
        ConsumeRetryService svc = fastService(GROUP);
        MessageExt msg = message("M-QUEUE-1");
        assertFalse(msg.isRetry(), "刚拉回来的普通消息不该被认成重投消息");
        assertEquals(0, msg.getReconsumeTimes(), "没重投过的消息次数字段是 0");

        assertTrue(svc.addRetryMessage(msg, 0), "第一次失败应当被排进重投队列");

        assertEquals(1, svc.getRetryMessageCount(TOPIC), "重投队列里就该有这一条");
        assertEquals(1, svc.getTotalRetryMessageCount());
        assertEquals(1, msg.getReconsumeTimes(),
                "★ 次数的真相是字段：排进重投的那一刻就该从 0 变成 1");
        assertTrue(msg.isRetry(),
                "★ 要往外重投的消息必须认得出自己是重投的（读侧就是 MessageExt#isRetry）");
        assertEquals(TOPIC, msg.getProperty(ConsumeRetryService.PROPERTY_ORIGINAL_TOPIC),
                "属性表里留的是「这条原本属于哪个 topic」，不是次数");
        assertNull(msg.getProperty(ConsumeRetryService.PROPERTY_RECONSUME_TIMES),
                "次数不许同时记在属性里 —— 两条通路各记一份，绕 broker 一圈之后必然对不上");
    }

    // ==================== 2. 到期真的再回调一次，且次数单调递增 ====================

    @Test
    @DisplayName("到期真的再回调一次，且每次回调带出去的次数严格 +1，一路到第 16 次")
    public void dueMessagesAreRedeliveredOnceEachAndTheAttemptNumberOnlyGoesUp() {
        ConsumeRetryService svc = fastService(GROUP);
        RecordingCallback callback = new RecordingCallback();
        svc.setRetryCallback(callback);
        svc.start();
        try {
            MessageExt msg = message("M-MONO-1");
            assertTrue(svc.addRetryMessage(msg, 0));

            List<Integer> observed = new ArrayList<Integer>();
            for (int expected = 1; expected <= 16; expected++) {
                Delivery d = callback.take("第 " + expected + " 次重投");
                assertEquals(expected, d.reconsumeTimes,
                        "★ 第 " + expected + " 次回调带出去的次数必须是 " + expected + "，实测="
                                + d.reconsumeTimes + "（次数不单调 = 计数在某一跳被重置了）");
                assertTrue(d.isRetry, "重投出去的每一条都要能读出 isRetry()==true, 次数=" + expected);
                assertEquals("M-MONO-1", d.msgId, "重投的始终是这一条消息, 次数=" + expected);
                assertEquals("payload-M-MONO-1", d.body, "正文不许在重投中变形, 次数=" + expected);
                observed.add(Integer.valueOf(d.reconsumeTimes));
            }
            assertEquals(16, observed.size());
            for (int i = 1; i < observed.size(); i++) {
                assertEquals(observed.get(i - 1).intValue() + 1, observed.get(i).intValue(),
                        "序列必须逐个 +1, index=" + i);
            }
        } finally {
            svc.shutdown();
        }
    }

    // ==================== 3. 第 16 次之后进死信，且不再回调 ====================

    @Test
    @DisplayName("第 16 次仍然失败：转入死信队列，并且再没有任何东西可被投出去")
    public void afterTheSixteenthFailureTheMessageGoesToTheDeadLetterQueueAndNothingIsScheduled() {
        ConsumeRetryService svc = fastService(GROUP);
        RecordingCallback callback = new RecordingCallback();
        svc.setRetryCallback(callback);
        // accepts=false：这一跳量的是「发不出去时进程内那份还兜着」；publish 被叫到是
        // 「往死信 Topic 外投过」的因果点，但死信账上那一笔要另外等（见 awaitDeadLetterBooked）
        RecordingPublisher publisher = new RecordingPublisher(false);
        svc.getDeadLetterQueue().setPublisher(publisher);
        svc.start();
        try {
            MessageExt msg = message("M-DLQ-1");
            assertTrue(svc.addRetryMessage(msg, 0));

            for (int expected = 1; expected <= 16; expected++) {
                Delivery d = callback.take("第 " + expected + " 次重投");
                assertEquals(expected, d.reconsumeTimes);
            }

            publisher.awaitOne("第 16 次失败之后应当转入死信");
            assertEquals(1, publisher.topics.size(), "只许转一次死信");
            assertEquals("%DLQ%" + GROUP, publisher.topics.get(0),
                    "★ 死信 Topic 的口径是 %DLQ%{consumerGroup}");
            assertEquals(16, publisher.times.get(0).intValue(),
                    "★ 进死信那条的次数必须是 16，实测=" + publisher.times.get(0));
            assertTrue(publisher.reasons.get(0).contains("16"),
                    "转入死信的原因要读得出来, 实际=" + publisher.reasons.get(0));

            DeadLetterQueue dlq = svc.getDeadLetterQueue();
            awaitDeadLetterBooked(dlq, 1, "第 16 次失败的那一条要在死信账上落一笔");
            assertEquals(1, dlq.getTotalDeadLetters(), "死信计数记下这一条");
            assertEquals(1, dlq.size(), "这条没发出去, 所以进程内那份还在兜着");

            List<DeadLetterQueue.DeadLetterMessage> drained = dlq.pollAll();
            assertEquals(1, drained.size(), "死信要取得出来才谈得上人工处理");
            assertEquals(16, drained.get(0).getReconsumeTimes());
            assertEquals("M-DLQ-1", drained.get(0).getMessage().getMsgId());
            assertEquals("payload-M-DLQ-1",
                    new String(drained.get(0).getMessage().getBody(), StandardCharsets.UTF_8));
            assertTrue(drained.get(0).getMessage().isRetry(), "死信里那条也带着「我是重投来的」这个事实");

            // 「不再回调」的因果证明：唯一的投递来源是重投队列，而它已经空了
            assertEquals(0, svc.getTotalRetryMessageCount(),
                    "★ 次数用尽之后不许还有东西排在重投队列里（那就是第 17 次）");
            assertEquals(0, callback.pending(), "回调一侧已经没有第 17 条");
        } finally {
            svc.shutdown();
        }
    }

    // ==================== 3b. 上面那个等待为什么必须存在：记账落在 publish 返回之后 ====================

    @Test
    @DisplayName("外投被叫到的那一刻死信账上还是空的：计数与缓存在 publish 返回之后才写")
    public void theDeadLetterAccountingIsWrittenAfterThePublishCallReturns() {
        DeadLetterQueue dlq = new DeadLetterQueue(GROUP);
        final List<Long> countersInsidePublish = new ArrayList<Long>();
        final List<Integer> sizesInsidePublish = new ArrayList<Integer>();
        dlq.setPublisher(new DeadLetterQueue.DlqPublisher() {
            @Override
            public boolean publish(MessageExt message, int reconsumeTimes, String dlqTopic, String reason) {
                countersInsidePublish.add(Long.valueOf(dlq.getTotalDeadLetters()));
                sizesInsidePublish.add(Integer.valueOf(dlq.size()));
                return false;
            }
        });

        // 同步调用，不经调度线程：这里量的就是 putMessage 自己内部的那一次先后
        assertFalse(dlq.putMessage(message("M-ORDER-1"), 16,
                "Exceeded max reconsume times: " + ConsumeRetryService.DEFAULT_MAX_RECONSUME_TIMES));

        assertEquals(1, countersInsidePublish.size(), "外投就该被叫到一次");
        assertEquals(0L, countersInsidePublish.get(0).longValue(),
                "★ publish 被叫到时账上还是空的 —— 「等外投事件」结构上等不到这一笔记账, "
                        + "这就是 awaitDeadLetterBooked 必须存在的理由");
        assertEquals(0, sizesInsidePublish.get(0).intValue(), "同理, 缓存里那一刻也还没东西");
        assertEquals(1L, dlq.getTotalDeadLetters(), "publish 返回之后这一笔才落到账上");
        assertEquals(1, dlq.size(), "以及缓存里");
    }

    // ==================== 4. 没设置外投通路时，行为与进程内那套逐字相同 ====================

    @Test
    @DisplayName("没接外投通路：仍然按进程内定时重投，且这一跳成功之后不再排下一次")
    public void withoutATransportTheInProcessScheduleStillRedelivers() {
        ConsumeRetryService svc = fastService(GROUP);
        // 这里用"回成功"的回调：一旦回失败, 调度线程会立刻排下一跳,
        // "队列已空"就不再是那次投递的结论而变成和时间赛跑的读数了。
        RecordingCallback callback = new RecordingCallback(true);
        svc.setRetryCallback(callback);
        MessageExt msg = message("M-INPROC-1");
        assertTrue(svc.addRetryMessage(msg, 0));
        // 排队状态在未启动调度线程时量：一启动就有 5ms 的 tick 与断言赛跑, 那是在和时间赛跑而不是在验契约
        assertEquals(1, svc.getRetryMessageCount(TOPIC), "没通路时这条消息就排在进程内");
        svc.start();
        try {
            Delivery d = callback.take("进程内那一跳");
            assertEquals(1, d.reconsumeTimes);
            assertEquals("payload-M-INPROC-1", d.body, "回调拿到的还是那一条消息的正文");
            assertEquals(0, svc.getRetryMessageCount(TOPIC), "投出去之后就要从待投队列里摘掉");
            assertEquals(0, svc.getTotalRetryMessageCount(), "回成功之后不许再排下一次");
            assertEquals(0, callback.pending(), "只回调这一次");
        } finally {
            svc.shutdown();
        }
    }

    // ==================== 5. 外投成功就不再排进程内那一跳（一条消息只许有一条通路） ====================

    @Test
    @DisplayName("外投通路收下副本时：不再排进程内定时投，避免同一条消息投两遍")
    public void persistedCopySuppressesTheInProcessAttempt() {
        ConsumeRetryService svc = fastService(GROUP);
        RecordingCallback callback = new RecordingCallback();
        svc.setRetryCallback(callback);
        final List<Integer> sent = new ArrayList<Integer>();
        final List<String> sentTopics = new ArrayList<String>();
        svc.setRetryTransport(new ConsumeRetryService.RetryTransport() {
            @Override
            public boolean sendRetryCopy(MessageExt message, int reconsumeTimes) {
                sent.add(Integer.valueOf(reconsumeTimes));
                sentTopics.add(message.getTopic());
                assertEquals(reconsumeTimes, message.getReconsumeTimes(),
                        "外投之前次数必须已经写在字段上：副本带的就是它自己那一级的数");
                assertTrue(message.isRetry(), "外投的副本必须带着重投标记");
                return true;
            }
        });
        svc.start();
        try {
            MessageExt msg = message("M-DUR-1");
            assertTrue(svc.addRetryMessage(msg, 0));
            assertEquals(1, sent.size(), "第一次失败就该有一份副本外投");
            assertEquals(TOPIC, sentTopics.get(0), "重投副本回到原 topic, 消费者下一轮才拉得到");
            assertEquals(0, svc.getTotalRetryMessageCount(),
                    "★ 副本已经在 broker 手里, 进程内不许再排一次（否则同一条消息两条通路, 级数翻倍）");
            assertEquals(0, callback.pending(), "通路接上之后, 进程内那套调度一步都不许走");

            assertTrue(svc.addRetryMessage(msg, 15), "第 16 级还在上限之内");
            assertEquals(2, sent.size());
            assertEquals(16, sent.get(1).intValue(), "第 16 级副本带出去的次数是 16");

            assertFalse(svc.addRetryMessage(msg, 16), "16 次用尽: 没有下一次了");
            assertEquals(2, sent.size(), "次数用尽之后不许再往外投重投副本（那是第 17 次）");
        } finally {
            svc.shutdown();
        }
    }

    @Test
    @DisplayName("外投通路投不出去时：退回进程内那一跳，消息不许凭空消失")
    public void failedTransportFallsBackToTheInProcessAttempt() {
        ConsumeRetryService svc = fastService(GROUP);
        RecordingCallback callback = new RecordingCallback();
        svc.setRetryCallback(callback);
        svc.setRetryTransport(new ConsumeRetryService.RetryTransport() {
            @Override
            public boolean sendRetryCopy(MessageExt message, int reconsumeTimes) {
                return false; // 没有路由 / broker 拒收 —— 通路失败
            }
        });
        svc.start();
        try {
            MessageExt msg = message("M-FALLBACK-1");
            assertTrue(svc.addRetryMessage(msg, 0));
            Delivery d = callback.take("回退那一跳");
            assertEquals(1, d.reconsumeTimes,
                    "★ 外投失败退回进程内之后, 计数不许被重置成 0（那就是「最多 16 次」变成不封顶的入口）");
        } finally {
            svc.shutdown();
        }
    }

    // ==================== 6. 生产默认值不许因为「测试想快」而漂移 ====================

    @Test
    @DisplayName("生产默认值钉住：1 秒扫一次、最多 16 次、阶梯间隔 10s→2h、固定间隔下限 1s")
    public void productionDefaultsAreStillOneSecondTickAndSixteenAttempts() {
        assertEquals(16, ConsumeRetryService.DEFAULT_MAX_RECONSUME_TIMES,
                "广告侧写的是「最多 16 次」，这条常量就是那句广告的兑现口径");
        assertEquals(1000L, ConsumeRetryService.DEFAULT_RETRY_CHECK_INTERVAL_MILLIS,
                "到期检查周期是 1 秒");
        assertEquals(30_000L, ConsumeRetryService.DEFAULT_ORDERLY_RETRY_INTERVAL_MILLIS);

        ConsumeRetryService plain = new ConsumeRetryService("plain-group");
        assertEquals(16, plain.getMaxReconsumeTimes(), "单参构造出来的服务必须吃生产默认值");
        assertEquals(1000L, plain.getRetryCheckIntervalMillis(), "默认检查周期不许被测试参数带跑");
        assertNotNull(plain.getDelayStrategy());
        assertEquals(10_000L, plain.getDelayStrategy().nextDelayMillis(1),
                "默认策略的第一跳仍然是 10 秒");

        ConsumeRetryService explicit = new ConsumeRetryService("g", 3, RetryPolicy.FIXED, 7L,
                25L, null);
        assertEquals(3, explicit.getMaxReconsumeTimes());
        assertEquals(25L, explicit.getRetryCheckIntervalMillis());
        assertNotNull(explicit.getDelayStrategy(), "策略传 null 时要回落到生产那两套数值, 不能是空");
        assertEquals(1000L, explicit.getDelayStrategy().nextDelayMillis(4),
                "FIXED 策略仍然守住「最小 1 秒」那个 floor：配 7ms 也还是 1000ms");

        assertEquals(10_000L, RetryPolicy.getSteppedInterval(1), "第 1 次重投等 10 秒");
        assertEquals(30_000L, RetryPolicy.getSteppedInterval(2));
        assertEquals(7_200_000L, RetryPolicy.getSteppedInterval(16), "第 16 次重投等 2 小时");
        assertEquals(7_200_000L, RetryPolicy.getSteppedInterval(99), "越界以后停在最后一档");
        assertEquals(1000L, RetryPolicy.getFixedInterval(0L), "固定间隔有 1 秒的 floor");
    }

    // ==================== 7. 死信缓存只是缓存：配了发布通路就别再当唯一真相 ====================

    @Test
    @DisplayName("配了死信发布通路：死信发到 %DLQ%{group}，进程内那份不再是唯一持有者")
    public void deadLettersArePublishedToTheDlqTopicWhenAPublisherIsWired() {
        ConsumeRetryService svc = fastService(GROUP);
        RecordingPublisher publisher = new RecordingPublisher(true);
        DeadLetterQueue dlq = svc.getDeadLetterQueue();
        dlq.setPublisher(publisher);

        MessageExt msg = message("M-PUBLISH-1");
        msg.setReconsumeTimes(16);
        assertFalse(svc.addRetryMessage(msg, 16), "16 次已经用尽");

        assertEquals(1, publisher.topics.size(), "★ 死信必须真往死信 Topic 发一次");
        assertEquals("%DLQ%" + GROUP, publisher.topics.get(0));
        assertEquals(16, publisher.times.get(0).intValue());
        assertTrue(publisher.reasons.get(0).contains("Exceeded max reconsume times"));
        assertEquals(1, dlq.getTotalDeadLetters(), "统计计数照记");
        assertEquals(0, dlq.size(),
                "★ 已经发到 topic 上的那一条不该再被进程内那份当第二真相留着");
        assertTrue(msg.isRetry(), "转死信的消息同样认得出自己是重投来的");
    }

    @Test
    @DisplayName("发布失败时死信仍留在进程内兜底，不许静默丢掉")
    public void unpublishableDeadLettersStayInProcessAsFallback() {
        ConsumeRetryService svc = fastService(GROUP);
        RecordingPublisher publisher = new RecordingPublisher(false);
        DeadLetterQueue dlq = svc.getDeadLetterQueue();
        dlq.setPublisher(publisher);

        MessageExt msg = message("M-NOROUTE-1");
        svc.addRetryMessage(msg, 16);
        assertEquals(1, dlq.size(), "发不出去的那一条必须还在进程内, 至少有告警可捞");
        assertEquals(1, dlq.getTotalDeadLetters());
        List<DeadLetterQueue.DeadLetterMessage> drained = dlq.pollAll();
        assertEquals(1, drained.size());
        assertFalse(drained.get(0).isPublished(),
                "缓存里这一条要标得出来「它没进 topic」, 否则统计会把缓存当真相");
        assertEquals(0, dlq.size(), "pollAll 取走之后缓存就空了 —— 它是缓存, 不是队列的真相");
    }
}
