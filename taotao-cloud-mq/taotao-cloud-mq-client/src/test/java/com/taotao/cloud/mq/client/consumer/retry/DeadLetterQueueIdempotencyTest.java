package com.taotao.cloud.mq.client.consumer.retry;

import com.taotao.cloud.mq.common.message.MessageExt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 「同一条消息转入死信」这句话的幂等契约（不起进程、不发网络，只量死信容器自己）。
 * <p>
 * 为什么要单独钉这一组：死信 Topic 没有路由时外投必然失败 ⇒ 位点不提交 ⇒ 同一份消息会被一遍遍重拉，
 * 而每一次重拉都会再进一次 {@link DeadLetterQueue#putMessage}。记账如果不认得「这条已经转过了」，
 * 缓存里就会堆同一份副本的 N 个分身，并且第 N+1 个分身会把最早那一份「必须兜着」的条目挤出去 ——
 * 那句「发不出去的那一条留在进程内兜底」就在重拉下不成立了，统计读数同时开始说谎。
 * <p>
 * 三条边界一起钉：
 * <ol>
 *   <li>判重只作用于**发不出去**那一路的记账；每一次重拉仍然要真去试一次外投。</li>
 *   <li>publish 成功那一路**不许**被幂等挡：宁可死信 Topic 上多一条，也不许「以为发过了」就不发。</li>
 *   <li>判重用的结构必须有界：它不许随运行时间无界增长，超出容量时要看得出来它把最老的键忘了。</li>
 * </ol>
 * 判据全部是「调了几次外投」「缓存里有几份」「计数涨到几」这类可数的值，没有任何 sleep 或挂钟阈值。
 */
public class DeadLetterQueueIdempotencyTest {

    private static final String GROUP = "idempotency-group";
    private static final String TOPIC = "idempotency-topic";
    private static final String REASON = "Exceeded max reconsume times: 16";

    /** 每一次「转入死信」都被记下来：这条记录就是「外投被叫到」这件事的可读证据. */
    private static final class CountingPublisher implements DeadLetterQueue.DlqPublisher {
        private final List<String> msgIds = new ArrayList<String>();
        private final List<Integer> times = new ArrayList<Integer>();
        private final boolean accepts;

        CountingPublisher(boolean accepts) {
            this.accepts = accepts;
        }

        @Override
        public boolean publish(MessageExt message, int reconsumeTimes, String dlqTopic, String reason) {
            msgIds.add(message.getMsgId());
            times.add(Integer.valueOf(reconsumeTimes));
            assertEquals("%DLQ%" + GROUP, dlqTopic, "死信 Topic 的口径始终是 %DLQ%{consumerGroup}");
            assertEquals(REASON, reason, "转入死信的原因要原样带到外投这一侧");
            return accepts;
        }

        int calls() {
            return msgIds.size();
        }
    }

    private static MessageExt message(String msgId) {
        return messageIn(TOPIC, msgId);
    }

    private static MessageExt messageIn(String topic, String msgId) {
        MessageExt msg = new MessageExt();
        msg.setTopic(topic);
        msg.setMsgId(msgId);
        msg.setKeys(msgId);
        msg.setBody(("payload-" + msgId).getBytes(StandardCharsets.UTF_8));
        msg.setReconsumeTimes(16);
        return msg;
    }

    @Test
    @DisplayName("同一条副本被重拉四次：外投每次真试，记账与缓存只认第一笔")
    public void repeatedRedeliveryOfTheSameCopyIsBookedOnlyOnce() {
        DeadLetterQueue dlq = new DeadLetterQueue(GROUP);
        CountingPublisher publisher = new CountingPublisher(false);
        dlq.setPublisher(publisher);

        MessageExt msg = message("M-REDELIVERY-1");
        for (int i = 1; i <= 4; i++) {
            assertFalse(dlq.putMessage(msg, 16, REASON),
                    "第 " + i + " 次重拉仍然没进死信 Topic, 返回值必须是 false（调用方据此不提交位点）");
        }

        assertEquals(4, publisher.calls(),
                "★ 幂等只许作用在记账上：每一次重拉都还是要真去试一次外投, 实测只试了 "
                        + publisher.calls() + " 次");
        assertEquals(1, dlq.size(), "缓存里同一份副本只该有一份");
        assertEquals(1, dlq.getTotalDeadLetters(), "同一条消息转入死信只记一笔账");
        assertEquals(0, dlq.getTotalExpired(), "自重复不许被记成「过期清理」");

        List<DeadLetterQueue.DeadLetterMessage> cached = dlq.pollAll();
        assertEquals(1, cached.size());
        assertEquals(16, cached.get(0).getReconsumeTimes());
        assertEquals("M-REDELIVERY-1", cached.get(0).getMessage().getMsgId());
        assertFalse(cached.get(0).isPublished(), "缓存里那条要标得出来「它没进死信 Topic」");
    }

    @Test
    @DisplayName("判重键的粒度：换了 msgId、换了一级、换了 Topic 都算新的一条, 各自记账")
    public void differentCopiesEachGetTheirOwnEntry() {
        DeadLetterQueue dlq = new DeadLetterQueue(GROUP);
        CountingPublisher publisher = new CountingPublisher(false);
        dlq.setPublisher(publisher);

        MessageExt a = message("M-A");
        MessageExt otherTopicSameId = messageIn("other-topic", "M-A");
        MessageExt b = message("M-B");

        assertCached(dlq.putMessage(a, 16, REASON), "第一次转入");
        assertCached(dlq.putMessage(a, 15, REASON), "同一条的另一级");
        assertCached(dlq.putMessage(otherTopicSameId, 16, REASON), "同 id 但换了 Topic");
        assertCached(dlq.putMessage(b, 16, REASON), "另一条消息");
        assertEquals(4, dlq.size(), "四条各不相同的转入都要各留一份");
        assertEquals(4, dlq.getTotalDeadLetters());

        // 同样的四条再来一轮：一条都不许多记
        assertCached(dlq.putMessage(a, 16, REASON), "第二轮 16 级");
        assertCached(dlq.putMessage(a, 15, REASON), "第二轮 15 级");
        assertCached(dlq.putMessage(otherTopicSameId, 16, REASON), "第二轮换 Topic");
        assertCached(dlq.putMessage(b, 16, REASON), "第二轮另一条");
        assertEquals(4, dlq.size(), "第二轮重拉不许把缓存变成 8 份");
        assertEquals(4, dlq.getTotalDeadLetters(), "第二轮重拉不许把统计涨到 8");
        assertEquals(8, publisher.calls(), "但外投仍然一次都没少试");
    }

    /** 这里的 publisher 一律回「没发出去」：每一次转入都必须回 false, 逐条钉住. */
    private static void assertCached(boolean value, String what) {
        assertFalse(value, what + "：没接成功的外投通路时, 这一次转入该回 false（调用方据此不提交位点）");
    }

    @Test
    @DisplayName("publish 成功那一路不许被幂等挡：同一份副本被重拉时宁可多发, 不许「以为发过了」就不发")
    public void publishedDeadLettersAreNeverSuppressedByTheIdempotencyCheck() {
        DeadLetterQueue dlq = new DeadLetterQueue(GROUP);
        CountingPublisher publisher = new CountingPublisher(true);
        dlq.setPublisher(publisher);

        MessageExt msg = message("M-PUBLISHED-1");
        for (int i = 1; i <= 3; i++) {
            assertTrue(dlq.putMessage(msg, 16, REASON),
                    "第 " + i + " 次转入的外投是成功的, 返回值必须是 true");
        }

        assertEquals(3, publisher.calls(),
                "★ 判重不许拦住了死信 Topic 那一侧：发成功过一次就不许再发, 是「以为发过了」的那种错法");
        assertEquals(3, dlq.getTotalDeadLetters(), "真发出去的每一次都要有一笔记账");
        assertEquals(0, dlq.size(), "已经落到死信 Topic 上的那条不该同时被进程内当第二真相留着");
    }

    @Test
    @DisplayName("缓存被取走之后再重拉：不许把同一条塞回缓存（取走不等于没记过账）")
    public void drainingTheCacheDoesNotForgetThatThisCopyWasAlreadyBooked() {
        DeadLetterQueue dlq = new DeadLetterQueue(GROUP);
        CountingPublisher publisher = new CountingPublisher(false);
        dlq.setPublisher(publisher);

        MessageExt msg = message("M-DRAINED-1");
        assertFalse(dlq.putMessage(msg, 16, REASON));
        assertEquals(1, dlq.pollAll().size(), "先让取走侧把这一条拿走");
        assertEquals(0, dlq.size());

        assertFalse(dlq.putMessage(msg, 16, REASON), "再重拉一次, 事实仍然是「它没进死信 Topic」");
        assertEquals(2, publisher.calls(), "外投这一路照旧要真试一次");
        assertEquals(0, dlq.size(), "★ 取走之后同一条被重拉不许再塞回缓存：那正是同一个死信的两份分身");
        assertEquals(1, dlq.getTotalDeadLetters(), "记账也不许因为缓存被掏空而再涨一笔");
    }

    @Test
    @DisplayName("clear() 是整个兜底容器的重置：重置之后同一条要能重新被兜住")
    public void clearResetsTheWholeFallbackContainer() {
        DeadLetterQueue dlq = new DeadLetterQueue(GROUP);
        CountingPublisher publisher = new CountingPublisher(false);
        dlq.setPublisher(publisher);

        MessageExt msg = message("M-CLEARED-1");
        assertFalse(dlq.putMessage(msg, 16, REASON));
        dlq.clear();
        assertEquals(0, dlq.size());

        assertFalse(dlq.putMessage(msg, 16, REASON));
        assertEquals(1, dlq.size(),
                "clear() 说「这一份兜底容器整个重来」, 那么重来之后同一条被重拉要能重新被兜住");
        assertEquals(2, dlq.getTotalDeadLetters());
        assertEquals(2, publisher.calls());
    }

    @Test
    @DisplayName("判重用的记忆有界：写满容量上限之后, 最老的键必须已经被忘掉（阳性对照就是它会被重新记账）")
    public void theIdempotencyMemoryIsBounded() {
        DeadLetterQueue dlq = new DeadLetterQueue(GROUP);
        CountingPublisher publisher = new CountingPublisher(false);
        dlq.setPublisher(publisher);

        int cap = DeadLetterQueue.MAX_CACHED_DEAD_LETTERS;
        for (int i = 0; i < cap + 1; i++) {
            dlq.putMessage(message("BOUNDED-" + i), 16, REASON);
        }
        assertEquals(cap, dlq.size(), "缓存封顶在 MAX_CACHED_DEAD_LETTERS");
        assertEquals(1, dlq.getTotalExpired(), "溢出只裁掉一条最老的");
        assertEquals(cap + 1L, dlq.getTotalDeadLetters(), "各不相同的消息各记一笔, 判重不许把它们并成一条");

        // 记忆如果有界, 最早那个键必然已经被淘汰 ⇒ 再投它会当成新的一笔记账；
        // 换成一个只长不消的结构（比如普通 HashMap）, 这两条读数就都不成立。
        dlq.putMessage(message("BOUNDED-0"), 16, REASON);
        assertEquals(cap + 2L, dlq.getTotalDeadLetters(),
                "★ 判重的结构必须有界：最老那个键被遗忘之后, 同一条要重新被当成一笔新账");
        assertEquals(2, dlq.getTotalExpired(), "被遗忘的那一条重新挤进缓存, 就会把又一条裁出去");
        assertEquals(cap, dlq.size(), "缓存仍然封顶");
        assertEquals(cap + 2, publisher.calls(), "外投一次都没被幂等挡掉");
    }

    @Test
    @DisplayName("认不出是谁的一条（msgId 或 Topic 为空）一律照旧兜住：宁可重复记账, 不许吞掉消息")
    public void messagesWithoutAnIdentifiableKeyAlwaysFallBackToCaching() {
        DeadLetterQueue dlq = new DeadLetterQueue(GROUP);
        CountingPublisher publisher = new CountingPublisher(false);
        dlq.setPublisher(publisher);

        MessageExt noId = messageIn(TOPIC, null);
        assertFalse(dlq.putMessage(noId, 16, REASON));
        assertFalse(dlq.putMessage(noId, 16, REASON));
        assertEquals(2, dlq.size(),
                "没有 msgId 就判不了重, 只能退回旧行为 —— 吞掉一条真没进 Topic 的消息比多记一笔贵");
        assertEquals(2, dlq.getTotalDeadLetters());

        MessageExt noTopic = message("M-M");
        noTopic.setTopic(null);
        assertFalse(dlq.putMessage(noTopic, 16, REASON));
        assertFalse(dlq.putMessage(noTopic, 16, REASON));
        assertEquals(4, dlq.size(), "同理, Topic 为空也判不了重");
        assertEquals(4, dlq.getTotalDeadLetters());
    }
}
