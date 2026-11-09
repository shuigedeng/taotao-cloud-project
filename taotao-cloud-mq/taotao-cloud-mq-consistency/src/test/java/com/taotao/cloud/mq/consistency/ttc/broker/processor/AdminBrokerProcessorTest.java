package com.taotao.cloud.mq.consistency.ttc.broker.processor;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerConfig;
import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

/**
 * AdminBrokerProcessor 单元测试 — 不启动 Broker 网络端口, 仅验证协议处理。
 * <p>
 * 用 {@code BrokerController} 的本地 TopicConfig 缓存 + ProcessRequest 流程驱动。
 */
public class AdminBrokerProcessorTest {

    /** 每个用例一个独立目录: 这里建过的 topic 会当场落盘, 不能留到下一个用例的"空表"断言里去. */
    @TempDir
    Path tempDir;

    /**
     * 构造一个最小可用的 BrokerController, 让 AdminBrokerProcessor 可访问 brokerConfig。
     * <p>
     * 存储根目录必须显式指进 {@link #tempDir}：{@code MessageStoreConfig} 的缺省值是
     * {@code ~/store}，而 topic 配置的落盘根就是它 —— 不设既会把测试数据写到用户目录，
     * 也会让上一个用例留下的 {@code topic_config.json} 被下一个用例读回来。
     */
    private BrokerController buildController() {
        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName("TestBroker");
        bc.setBrokerClusterName("TestCluster");
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(tempDir.resolve("store").toString());
        msc.setStorePathCommitLog(tempDir.resolve("store").resolve("commitlog").toString());
        NettyServerConfig nsc = new NettyServerConfig();
        return new BrokerController(bc, msc, nsc);
    }

    @Test
    public void testRejectRequestReturnsFalse() {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        assertEquals(false, p.rejectRequest());
    }

    @Test
    public void testCreateTopicSuccess() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
        req.addExtField("topic", "TestTopic");
        req.addExtField("readQueueNums", "4");
        req.addExtField("writeQueueNums", "4");
        RemotingCommand resp = p.processRequest(null, req);
        assertEquals(0, resp.getCode());
        // 缓存里有
        assertNotNull(p.getTopicConfig("TestTopic"));
    }

    @Test
    public void testCreateTopicDefaultsWhenQueuesMissing() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
        req.addExtField("topic", "DefaultQueueTopic");
        RemotingCommand resp = p.processRequest(null, req);
        assertEquals(0, resp.getCode());
        TopicConfig tc = p.getTopicConfig("DefaultQueueTopic");
        assertEquals(TopicConfig.DEFAULT_READ_QUEUE_NUMS, tc.getReadQueueNums());
        assertEquals(TopicConfig.DEFAULT_WRITE_QUEUE_NUMS, tc.getWriteQueueNums());
    }

    @Test
    public void testCreateTopicEmptyTopicReturnsError() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
        req.addExtField("topic", "");
        RemotingCommand resp = p.processRequest(null, req);
        assertEquals(RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testCreateTopicNullTopicReturnsError() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
        RemotingCommand resp = p.processRequest(null, req);
        assertEquals(RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testCreateTopicOverwritesPreviousConfig() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        // 1) 4 写 4 读
        RemotingCommand req1 = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
        req1.addExtField("topic", "T");
        req1.addExtField("readQueueNums", "4");
        req1.addExtField("writeQueueNums", "4");
        p.processRequest(null, req1);
        // 2) 8 写 8 读
        RemotingCommand req2 = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
        req2.addExtField("topic", "T");
        req2.addExtField("readQueueNums", "8");
        req2.addExtField("writeQueueNums", "8");
        p.processRequest(null, req2);
        assertEquals(8, p.getTopicConfig("T").getReadQueueNums());
    }

    @Test
    public void testUpdateAndCreateTopicSameAsCreate() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
        req.addExtField("topic", "AliasTopic");
        RemotingCommand resp = p.processRequest(null, req);
        assertEquals(0, resp.getCode());
        assertNotNull(p.getTopicConfig("AliasTopic"));
    }

    @Test
    public void testGetAllTopicsEmpty() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_TOPIC_LIST);
        RemotingCommand resp = p.processRequest(null, req);
        assertEquals(0, resp.getCode());
        AdminBrokerProcessor.TopicListResult body = JsonCodec.decode(resp.getBody(),
                AdminBrokerProcessor.TopicListResult.class);
        assertNotNull(body);
        assertEquals(0, body.getTopics().size());
    }

    @Test
    public void testGetAllTopicsAfterCreate() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        for (String t : new String[]{"A", "B", "C"}) {
            RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
            req.addExtField("topic", t);
            p.processRequest(null, req);
        }
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_TOPIC_LIST);
        RemotingCommand resp = p.processRequest(null, req);
        AdminBrokerProcessor.TopicListResult body = JsonCodec.decode(resp.getBody(),
                AdminBrokerProcessor.TopicListResult.class);
        assertEquals(3, body.getTopics().size());
        assertTrue(body.getTopics().contains("A"));
        assertTrue(body.getTopics().contains("B"));
        assertTrue(body.getTopics().contains("C"));
    }

    @Test
    public void testUnknownCodeReturnsNotSupported() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        RemotingCommand req = RemotingCommand.createRequestCommand(99999);
        RemotingCommand resp = p.processRequest(null, req);
        assertEquals(RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED, resp.getCode());
    }

    @Test
    public void testCreateTopicResponseContainsQueueData() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
        req.addExtField("topic", "QD");
        req.addExtField("readQueueNums", "2");
        req.addExtField("writeQueueNums", "2");
        RemotingCommand resp = p.processRequest(null, req);
        AdminBrokerProcessor.CreateTopicResult body = JsonCodec.decode(resp.getBody(),
                AdminBrokerProcessor.CreateTopicResult.class);
        assertEquals("QD", body.getTopic());
        assertEquals(1, body.getQueueDataList().size());
        assertEquals("TestBroker", body.getQueueDataList().get(0).getBrokerName());
    }

    @Test
    public void testResponsePreservesOpaque() throws Exception {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        int opaque = 88888;
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.CREATE_TOPIC);
        req.setOpaque(opaque);
        req.addExtField("topic", "X");
        RemotingCommand resp = p.processRequest(null, req);
        assertEquals(opaque, resp.getOpaque());
    }

    @Test
    public void testGetTopicConfigReturnsNullForUnknown() {
        AdminBrokerProcessor p = new AdminBrokerProcessor(buildController());
        assertNull(p.getTopicConfig("NEVER"));
    }
}
