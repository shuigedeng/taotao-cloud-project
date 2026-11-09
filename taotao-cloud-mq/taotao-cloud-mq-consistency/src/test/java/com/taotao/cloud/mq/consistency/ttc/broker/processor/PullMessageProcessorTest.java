package com.taotao.cloud.mq.consistency.ttc.broker.processor;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerConfig;
import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.PullResultPayload;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.MessageExtBrokerInner;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import com.taotao.cloud.mq.consistency.ttc.store.PutMessageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PullMessageProcessor 单元测试 — 验证通过 InMemoryQueueIndex 查询真实 CommitLog 内容。
 * <p>
 * 测试覆盖:
 * - 请求参数校验
 * - 空 topic 返回空列表
 * - 写入 N 条后拉取能拿到全部
 * - offset 越界行为
 * - maxNum 限制
 * - nextOffset 单调递增
 * - topic/queueId 隔离
 */
public class PullMessageProcessorTest {

    @TempDir
    Path tempDir;

    private PullMessageProcessor processor;
    private BrokerController controller;

    @BeforeEach
    public void setUp() throws Exception {
        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName("TestBroker_" + UUID.randomUUID().toString().substring(0, 6));
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(tempDir.resolve("store").toString());
        msc.setStorePathCommitLog(tempDir.resolve("store/commitlog").toString());
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        NettyServerConfig nsc = new NettyServerConfig();
        controller = new BrokerController(bc, msc, nsc);
        assertTrue(controller.initialize());
        processor = new PullMessageProcessor(controller);
    }

    private RemotingCommand pull(String topic, String queueId, long offset, Integer maxNum) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        req.addExtField("topic", topic);
        req.addExtField("queueId", queueId);
        req.addExtField("offset", String.valueOf(offset));
        if (maxNum != null) { req.addExtField("maxNum", String.valueOf(maxNum)); }

        return processor.processRequest(null, req);
    }

    private void putMessage(String topic, int queueId, String body) {
        putMessage(topic, queueId, body, null);
    }

    private void putMessage(String topic, int queueId, String body, String tags) {
        MessageExtBrokerInner msg = new MessageExtBrokerInner();
        msg.setTopic(topic);
        msg.setQueueId(queueId);
        msg.setBody(body.getBytes(StandardCharsets.UTF_8));
        msg.setBornTimestamp(System.currentTimeMillis());
        msg.setPropertiesString("");
        if (tags != null) {
            msg.setTags(tags);
        }
        PutMessageResult result = controller.getCommitLog().putMessage(msg);
        assertTrue(result.isOk(), "putMessage 应成功");
    }

    private RemotingCommand pullWithFilter(String topic, long offset, int maxNum,
                                           String filterExpression) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        req.addExtField("topic", topic);
        req.addExtField("queueId", "0");
        req.addExtField("offset", String.valueOf(offset));
        req.addExtField("maxNum", String.valueOf(maxNum));
        req.addExtField("filterType", "TAG");
        req.addExtField("filterExpression", filterExpression);
        return processor.processRequest(null, req);
    }

    /**
     * 回归：整窗消息都不匹配订阅 Tag 时，nextOffset 必须推进到"扫到的位点"。
     * <p>
     * 修之前 nextOffset 原样等于请求 offset，而客户端拿到空结果就不提交位点，
     * 于是带过滤的消费者下一轮从同一位点取回同一窗、同样全被过滤 ——
     * 永远卡在这段不匹配的前缀上，无报错、无日志、就是收不到。
     */
    @Test
    public void testNextOffsetAdvancesWhenWholeWindowIsFilteredOut() throws Exception {
        String topic = "FilterStall_" + UUID.randomUUID().toString().substring(0, 6);
        // 10 条都不匹配，maxNum=2 ⇒ 取窗 2*3=6 条，整窗落空
        for (int i = 0; i < 10; i++) {
            putMessage(topic, 0, "m" + i, "OTHER");
        }

        RemotingCommand resp = pullWithFilter(topic, 0L, 2, "WANTED");
        PullResultPayload body = JsonCodec.decode(resp.getBody(), PullResultPayload.class);

        assertTrue(body.getMessages().isEmpty(), "整窗都不匹配，本轮不该有消息");
        assertTrue(body.getNextOffset() > 0L,
                "整窗被过滤掉时 nextOffset 必须推进到扫到的位点，否则消费者原地打转"
                        + "（实际 nextOffset=" + body.getNextOffset() + "）");
        assertEquals(6L, body.getNextOffset(), "取窗 maxNum*3=6，应推进到第 6 条之后");
    }

    /**
     * 推进不能过头：窗口里还有<b>没被投递</b>的匹配消息时，nextOffset 只能停在
     * "最后一条已投递 +1"，不能跟着扫描进度一起往前跳。
     * <p>
     * 这条是本修复里最容易写错的地方：applyFilter 凑够 maxNum 就停扫，
     * 扫过的窗口（maxNum*3）会比交付的更靠后，若用 max(两者) 就会跨过
     * 窗口后半段里那些还没投出去的 WANTED。
     */
    @Test
    public void testNextOffsetDoesNotSkipUndeliveredMatches() throws Exception {
        String topic = "FilterMixed_" + UUID.randomUUID().toString().substring(0, 6);
        putMessage(topic, 0, "o0", "OTHER");
        putMessage(topic, 0, "o1", "OTHER");
        putMessage(topic, 0, "w2", "WANTED");
        putMessage(topic, 0, "w3", "WANTED");
        putMessage(topic, 0, "w4", "WANTED");

        // 取窗 maxNum*3=6 ⇒ 实际只有 5 条，凑满 maxNum=2 就在 offset 3 处停扫
        RemotingCommand first = pullWithFilter(topic, 0L, 2, "WANTED");
        PullResultPayload b1 = JsonCodec.decode(first.getBody(), PullResultPayload.class);
        assertEquals(2, b1.getMessages().size(), "应取满 maxNum 条匹配消息");
        assertEquals(2L, b1.getMessages().get(0).getQueueOffset());
        assertEquals(3L, b1.getMessages().get(1).getQueueOffset());
        assertEquals(4L, b1.getNextOffset(),
                "只能停在最后一条已投递+1；跟着扫描进度(5)走就会跳过 offset 4 那条 WANTED");

        // 紧接着的下一轮必须还能拿到 offset 4 —— 这是"没跳过"的正面证据
        RemotingCommand second = pullWithFilter(topic, b1.getNextOffset(), 2, "WANTED");
        PullResultPayload b2 = JsonCodec.decode(second.getBody(), PullResultPayload.class);
        assertEquals(1, b2.getMessages().size(), "第三条 WANTED 不能被上一轮跳过");
        assertEquals(4L, b2.getMessages().get(0).getQueueOffset());
    }

    /** 无过滤时行为不变：nextOffset 仍是"最后一条 +1"，且队尾不推进（不能跳过未投递消息）。 */
    @Test
    public void testNextOffsetUnchangedWithoutFilter() throws Exception {
        String topic = "NoFilter_" + UUID.randomUUID().toString().substring(0, 6);
        putMessage(topic, 0, "m0", "T");
        putMessage(topic, 0, "m1", "T");

        RemotingCommand resp = pull(topic, "0", 0, 10);
        PullResultPayload body = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        assertEquals(2, body.getMessages().size());
        assertEquals(2L, body.getNextOffset());
        // 顺带把 tag 的落盘往返钉住: 过滤匹配全靠它, 丢了的话带订阅的消费者一条都收不到
        assertEquals("T", body.getMessages().get(0).getTags(), "tag 应随消息落盘并还原");

        // 已经到队尾：一条都没扫到，nextOffset 必须原地不动
        RemotingCommand tail = pull(topic, "0", 2, 10);
        PullResultPayload tailBody = JsonCodec.decode(tail.getBody(), PullResultPayload.class);
        assertTrue(tailBody.getMessages().isEmpty());
        assertEquals(2L, tailBody.getNextOffset(), "队尾不得推进位点，否则会跳过未投递消息");
    }

    @Test
    public void testRejectRequestReturnsFalse() {
        assertEquals(false, processor.rejectRequest());
    }

    @Test
    public void testMissingTopicReturnsError() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testMissingQueueIdReturnsError() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        req.addExtField("topic", "T");
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testEmptyTopicReturnsEmptyList() throws Exception {
        RemotingCommand resp = pull("NoSuchTopic", "0", 0, 10);
        assertEquals(0, resp.getCode());
        PullResultPayload body = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        assertNotNull(body);
        assertTrue(body.getMessages().isEmpty(), "空 topic 应返回空列表");
        assertEquals(0L, body.getMaxOffset());
    }

    @Test
    public void testPullReturnsRealCommittedMessages() throws Exception {
        String topic = "RealTopic_" + UUID.randomUUID().toString().substring(0, 6);
        putMessage(topic, 0, "m1");
        putMessage(topic, 0, "m2");
        putMessage(topic, 0, "m3");

        RemotingCommand resp = pull(topic, "0", 0, 10);
        PullResultPayload body = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        assertEquals(3, body.getMessages().size());
        assertEquals("m1", new String(body.getMessages().get(0).getBody(), StandardCharsets.UTF_8));
        assertEquals("m2", new String(body.getMessages().get(1).getBody(), StandardCharsets.UTF_8));
        assertEquals("m3", new String(body.getMessages().get(2).getBody(), StandardCharsets.UTF_8));
        assertEquals(0, body.getMessages().get(0).getQueueOffset());
        assertEquals(1, body.getMessages().get(1).getQueueOffset());
        assertEquals(2, body.getMessages().get(2).getQueueOffset());
        assertEquals(3L, body.getMaxOffset(), "maxOffset 应等于已分配的最大 offset");
    }

    @Test
    public void testPullRespectsMaxNum() throws Exception {
        String topic = "MaxNumTopic_" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 10; i++) putMessage(topic, 0, "m" + i);

        RemotingCommand resp = pull(topic, "0", 0, 3);
        PullResultPayload body = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        assertEquals(3, body.getMessages().size());
    }

    @Test
    public void testPullFromOffset() throws Exception {
        String topic = "OffsetTopic_" + UUID.randomUUID().toString().substring(0, 6);
        for (int i = 0; i < 5; i++) putMessage(topic, 0, "m" + i);

        RemotingCommand resp = pull(topic, "0", 2, 10);
        PullResultPayload body = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        assertEquals(3, body.getMessages().size());
        assertEquals(2L, body.getMessages().get(0).getQueueOffset());
        assertEquals("m2", new String(body.getMessages().get(0).getBody(), StandardCharsets.UTF_8));
    }

    @Test
    public void testPullOffsetBeyondMaxReturnsEmpty() throws Exception {
        String topic = "BeyondTopic_" + UUID.randomUUID().toString().substring(0, 6);
        putMessage(topic, 0, "m1");
        putMessage(topic, 0, "m2");

        RemotingCommand resp = pull(topic, "0", 100, 10);
        PullResultPayload body = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        assertTrue(body.getMessages().isEmpty(), "超出 maxOffset 的 offset 应返回空");
        assertEquals(2L, body.getMaxOffset());
    }

    @Test
    public void testPullRespectsQueueIsolation() throws Exception {
        String topic = "QITopic_" + UUID.randomUUID().toString().substring(0, 6);
        putMessage(topic, 0, "q0-m1");
        putMessage(topic, 1, "q1-m1");
        putMessage(topic, 1, "q1-m2");

        RemotingCommand resp0 = pull(topic, "0", 0, 10);
        PullResultPayload body0 = JsonCodec.decode(resp0.getBody(), PullResultPayload.class);
        assertEquals(1, body0.getMessages().size(), "queue 0 应只有 1 条");
        assertEquals("q0-m1", new String(body0.getMessages().get(0).getBody(), StandardCharsets.UTF_8));

        RemotingCommand resp1 = pull(topic, "1", 0, 10);
        PullResultPayload body1 = JsonCodec.decode(resp1.getBody(), PullResultPayload.class);
        assertEquals(2, body1.getMessages().size(), "queue 1 应有 2 条");
        assertEquals("q1-m1", new String(body1.getMessages().get(0).getBody(), StandardCharsets.UTF_8));
        assertEquals("q1-m2", new String(body1.getMessages().get(1).getBody(), StandardCharsets.UTF_8));
    }

    @Test
    public void testPullRespectsTopicIsolation() throws Exception {
        String topicA = "A_" + UUID.randomUUID().toString().substring(0, 6);
        String topicB = "B_" + UUID.randomUUID().toString().substring(0, 6);
        putMessage(topicA, 0, "a-m1");
        putMessage(topicB, 0, "b-m1");
        putMessage(topicB, 0, "b-m2");

        RemotingCommand respA = pull(topicA, "0", 0, 10);
        PullResultPayload bodyA = JsonCodec.decode(respA.getBody(), PullResultPayload.class);
        assertEquals(1, bodyA.getMessages().size(), "topicA 应只有 1 条");
        assertEquals("a-m1", new String(bodyA.getMessages().get(0).getBody(), StandardCharsets.UTF_8));
    }

    @Test
    public void testResponsePreservesOpaque() throws Exception {
        int opaque = 9999;
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        req.setOpaque(opaque);
        req.addExtField("topic", "T");
        req.addExtField("queueId", "0");
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(opaque, resp.getOpaque());
    }
}
