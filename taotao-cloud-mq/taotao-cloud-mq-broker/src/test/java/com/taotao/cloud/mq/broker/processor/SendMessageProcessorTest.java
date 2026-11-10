package com.taotao.cloud.mq.broker.processor;

import com.taotao.cloud.mq.broker.BrokerConfig;
import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import com.taotao.cloud.mq.plugin.store.log.CommitLog;
import com.taotao.cloud.mq.plugin.store.log.FlushDiskType;
import com.taotao.cloud.mq.plugin.store.log.MappedFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SendMessageProcessor 单元测试 — 驱动 SendMessageProcessor.processRequest 各分支。
 */
public class SendMessageProcessorTest {

    @TempDir
    Path tempDir;

    private BrokerController controller;
    private SendMessageProcessor processor;

    @BeforeEach
    public void setUp() throws Exception {
        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName("TestBroker");
        bc.setBrokerClusterName("TestCluster");
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(tempDir.resolve("store").toString());
        msc.setStorePathCommitLog(tempDir.resolve("store/commitlog").toString());
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        NettyServerConfig nsc = new NettyServerConfig();
        controller = new BrokerController(bc, msc, nsc);
        assertTrue(controller.initialize());
        // 不调用 start(), 只用 initialize 后的 commitLog
        processor = new SendMessageProcessor(controller);
    }

    @Test
    public void testRejectRequestReturnsFalse() {
        assertEquals(false, processor.rejectRequest());
    }

    @Test
    public void testMissingTopicReturnsError() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("queueId", "0");
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
        assertTrue(resp.getRemark().contains("topic or queueId"));
    }

    @Test
    public void testMissingQueueIdReturnsError() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", "T");
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testMissingBodyReturnsError() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", "T");
        req.addExtField("queueId", "0");
        req.addExtField("brokerName", "TestBroker");
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
        assertTrue(resp.getRemark().contains("decode body failed"));
    }

    @Test
    public void testInvalidJsonBodyReturnsError() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", "T");
        req.addExtField("queueId", "0");
        req.addExtField("brokerName", "TestBroker");
        req.setBody("not-a-json".getBytes());
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testSendSuccess() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", "TestTopic");
        req.addExtField("queueId", "0");
        req.addExtField("brokerName", "TestBroker");
        MessageExt ext = new MessageExt();
        ext.setTopic("TestTopic");
        ext.setBody("hello".getBytes());
        ext.setTags("tagA");
        ext.setKeys("k1");
        req.setBody(JsonCodec.encode(ext));

        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(0, resp.getCode());
        SendResult sr = JsonCodec.decode(resp.getBody(), SendResult.class);
        assertNotNull(sr);
        assertEquals(SendStatus.SEND_OK, sr.getSendStatus());
    }

    @Test
    public void testOnewayReturnsNull() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.markOnewayRPC();
        req.addExtField("topic", "TestTopic");
        req.addExtField("queueId", "0");
        req.addExtField("brokerName", "TestBroker");
        MessageExt ext = new MessageExt();
        ext.setTopic("TestTopic");
        ext.setBody("hello".getBytes());
        req.setBody(JsonCodec.encode(ext));

        RemotingCommand resp = processor.processRequest(null, req);
        assertNull(resp);
    }

    @Test
    public void testResponsePreservesOpaque() throws Exception {
        int opaque = 123456;
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.setOpaque(opaque);
        req.addExtField("topic", "T");
        req.addExtField("queueId", "0");
        req.addExtField("brokerName", "TestBroker");
        MessageExt ext = new MessageExt();
        ext.setTopic("T");
        ext.setBody("hello".getBytes());
        req.setBody(JsonCodec.encode(ext));
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(opaque, resp.getOpaque());
    }

    @Test
    public void testCountersIncrementedOnSuccess() throws Exception {
        long before = SendMessageProcessor.TopicLogHelper.getSendCount();
        long beforeOk = SendMessageProcessor.TopicLogHelper.getSendSuccess();
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", "CT");
        req.addExtField("queueId", "0");
        req.addExtField("brokerName", "TestBroker");
        MessageExt ext = new MessageExt();
        ext.setTopic("CT");
        ext.setBody("hi".getBytes());
        req.setBody(JsonCodec.encode(ext));
        processor.processRequest(null, req);
        assertTrue(SendMessageProcessor.TopicLogHelper.getSendCount() > before);
        assertTrue(SendMessageProcessor.TopicLogHelper.getSendSuccess() > beforeOk);
    }

    @Test
    public void testCommitLogAvailable() {
        // 验证 setUp 中 controller.initialize 后 commitLog 已存在
        assertNotNull(controller.getCommitLog());
    }

    // ==================== T2：同步刷盘超时必须在"线上"传成 FLUSH_DISK_TIMEOUT ====================

    /**
     * 这条用例量的是<b>响应体</b>，不是状态码枚举存不存在。
     * <p>
     * 前置：{@code SYNC_FLUSH} + {@code syncFlushTimeout} 用默认 5000ms（不调大也不调小）。
     * 毁掉刷盘能力（关掉 mapped 区的 FileChannel ⇒ force 这条路永远走不通，
     * {@code GroupCommitService} 永不 {@code wakeupCustomer}），
     * 然后走一遍真实的 {@code processRequest}：
     * <ol>
     *   <li>健康时响应体是 {@code SEND_OK}（否则这条用例等于把所有发送都判成超时）；</li>
     *   <li>刷盘坏掉后 {@code flushedPosition} 一步不动（因果自证：盘真的没落住）；</li>
     *   <li>传输层仍是 {@code SUCCESS}——这正是客户端 {@code DefaultMQProducer.toSendResult}
     *       在 {@code code == SUCCESS} 时<b>原样采用响应体 SendStatus</b> 的条件，
     *       所以 {@code FLUSH_DISK_TIMEOUT} 就这样传到生产者，不需要动 ttc-mq-client。</li>
     * </ol>
     */
    @Test
    public void testSyncFlushTimeoutIsCarriedToProducerAsFlushDiskTimeout() throws Exception {
        MessageStoreConfig syncCfg = new MessageStoreConfig();
        syncCfg.setStorePathRootDir(tempDir.resolve("wire-sync").toString());
        syncCfg.setStorePathCommitLog(tempDir.resolve("wire-sync/commitlog").toString());
        syncCfg.setMappedFileSizeCommitLog(1024 * 1024);
        syncCfg.setFlushDiskType(FlushDiskType.SYNC_FLUSH);
        assertEquals(5000, syncCfg.getSyncFlushTimeout(), "不许调 syncFlushTimeout 过关");

        BrokerConfig wireBc = new BrokerConfig();
        wireBc.setBrokerName("WireBroker");
        wireBc.setBrokerClusterName("WireCluster");
        BrokerController syncController = new BrokerController(wireBc, syncCfg, new NettyServerConfig());
        assertTrue(syncController.initialize());
        syncController.getCommitLog().start();
        try {
            SendMessageProcessor syncProcessor = new SendMessageProcessor(syncController);

            // 1) 健康的同步刷盘发送：SEND_OK
            RemotingCommand healthy = syncProcessor.processRequest(null,
                    sendRequest("WireTopic", "healthy-body"));
            assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SUCCESS, healthy.getCode());
            SendResult healthyResult = JsonCodec.decode(healthy.getBody(), SendResult.class);
            assertNotNull(healthyResult);
            assertEquals(SendStatus.SEND_OK, healthyResult.getSendStatus(),
                    "刷盘成功时必须是 SEND_OK —— 否则下面的断言没有对照意义");

            // 2) 毁掉刷盘：关掉 FileChannel ⇒ force 永远失败，没人会唤醒 group-commit 请求
            CommitLog log = syncController.getCommitLog();
            MappedFile victim = log.getMappedFileQueue().peekLastMappedFile();
            final int flushedBefore = victim.getFlushedPosition();
            victim.getFileChannel().close();
            assertFalse(victim.getFileChannel().isOpen(), "前置条件：通道必须真的关掉，否则这条用例什么都没测");

            // 3) 线上响应必须是 FLUSH_DISK_TIMEOUT，而不是 SEND_OK
            RemotingCommand resp = syncProcessor.processRequest(null,
                    sendRequest("WireTopic", "doomed-body"));
            assertEquals(flushedBefore, victim.getFlushedPosition(),
                    "前置条件：这条消息确实没落住盘，FLUSH_DISK_TIMEOUT 不是凭空报的");
            assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SUCCESS, resp.getCode(),
                    "传输层保持 SUCCESS：客户端 code==SUCCESS 时直接采用响应体的 SendStatus（DefaultMQProducer.toSendResult）");
            SendResult sr = JsonCodec.decode(resp.getBody(), SendResult.class);
            assertNotNull(sr);
            assertEquals(SendStatus.FLUSH_DISK_TIMEOUT, sr.getSendStatus(),
                    "盘没落住却回 SEND_OK = 工单 T2 要修的洞");
            assertEquals("WireTopic", sr.getTopic(), "超时响应仍要带全定位信息，客户端才知道是哪条");
            assertEquals(0, sr.getQueueId());
            assertTrue(sr.getQueueOffset() > 0, "记录确实进了存储，响应里要带队列位点");

            // 4) 记录本身在存储里可读（失败的只是落盘，不是写入）
            List<MessageExt> back = log.pullMessage("WireTopic", 0, 0, 10);
            assertEquals(2, back.size(), "两条都进了存储，只有第二条没落住盘");
        } finally {
            syncController.getCommitLog().shutdown();
        }
    }

    private RemotingCommand sendRequest(String topic, String body) {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", topic);
        req.addExtField("queueId", "0");
        req.addExtField("brokerName", "TestBroker");
        MessageExt ext = new MessageExt();
        ext.setTopic(topic);
        ext.setBody(body.getBytes(StandardCharsets.UTF_8));
        req.setBody(JsonCodec.encode(ext));
        return req;
    }
}
