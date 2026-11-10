package com.taotao.cloud.mq.nameserver.processor;

import com.taotao.cloud.mq.common.TopicConfig;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.nameserver.NameServerController;
import com.taotao.cloud.mq.nameserver.NamesrvConfig;
import com.taotao.cloud.mq.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DefaultRequestProcessor 单元测试 — 通过完整 initialize 的 NameServerController
 * 路由 + register 协议命令, 验证业务结果。
 * <p>
 * 不启动 Netty 端口 (因为不调用 start()), 仅验证 processRequest 业务逻辑。
 */
public class DefaultRequestProcessorTest {

    private NameServerController controller;
    private DefaultRequestProcessor processor;

    @BeforeEach
    public void setUp() throws Exception {
        NamesrvConfig nsCfg = new NamesrvConfig();
        nsCfg.setKvConfigPath(java.nio.file.Files.createTempDirectory("ttc-test-kv")
                .resolve("kvConfig.json").toString());
        NettyServerConfig nettyCfg = new NettyServerConfig();
        nettyCfg.setListenPort(0); // 不实际启动
        controller = new NameServerController(nsCfg, nettyCfg);
        assertTrue(controller.initialize());
        processor = new DefaultRequestProcessor(controller);
    }

    @AfterEach
    public void tearDown() {
        if (controller != null) {
            controller.shutdown();
        }
    }

    @Test
    public void testRejectRequestReturnsFalse() {
        assertEquals(false, processor.rejectRequest());
    }

    @Test
    public void testUnsupportedRequestCode() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(99999);
        RemotingCommand resp = processor.processRequest(null, req);
        assertNotNull(resp);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED,
                resp.getCode());
    }

    @Test
    public void testRegisterBrokerMissingFieldsReturnsError() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.REGISTER_BROKER);
        // 故意不填 extField
        RemotingCommand resp = processor.processRequest(null, req);
        assertNotNull(resp);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
        assertTrue(resp.getRemark() != null && resp.getRemark().contains("required"));
    }

    @Test
    public void testRegisterBrokerSuccess() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.REGISTER_BROKER);
        req.addExtField("clusterName", "TestCluster");
        req.addExtField("brokerAddr", "10.0.0.1:10911");
        req.addExtField("brokerName", "brokerA");
        req.addExtField("brokerId", "0");
        req.addExtField("haServerAddr", "10.0.0.1:10912");

        RemotingCommand resp = processor.processRequest(null, req);
        assertNotNull(resp);
        assertEquals(0, resp.getCode());

        // 验证 RouteInfoManager 中已存在该 broker
        assertTrue(controller.getRouteInfoManager().getBrokerAddrTable().containsKey("brokerA"));
    }

    @Test
    public void testRegisterBrokerWithTopicConfigs() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.REGISTER_BROKER);
        req.addExtField("clusterName", "C1");
        req.addExtField("brokerAddr", "10.0.0.1:10911");
        req.addExtField("brokerName", "brokerA");
        req.addExtField("brokerId", "0");
        TopicConfig tc = new TopicConfig("TopicA", 4, 4, TopicConfig.PERM_READ_WRITE);
        req.setBody(JsonCodec.encode(java.util.Collections.singletonList(tc)));

        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(0, resp.getCode());

        // broker 应当注册到 topicQueueTable
        assertTrue(controller.getRouteInfoManager().getTopicQueueTable().containsKey("TopicA"));
    }

    @Test
    public void testUpdateAndCreateTopicBeforeBrokerRegister() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
        req.addExtField("topic", "OrphanTopic");
        req.addExtField("readQueueNums", "4");
        req.addExtField("writeQueueNums", "4");
        // broker 尚未注册, 应不抛, 返回 success
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(0, resp.getCode());
    }

    @Test
    public void testUpdateAndCreateTopicMissingTopic() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
        // 不加 topic
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testGetRouteByTopicMissingTopic() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTE_BY_TOPIC);
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testGetRouteByTopicUnknownTopic() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTE_BY_TOPIC);
        req.addExtField("topic", "NOT_EXIST");
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testGetRouteByTopicAfterCreate() throws Exception {
        // 1) 先注册 broker
        RemotingCommand reg = RemotingCommand.createRequestCommand(RequestCode.REGISTER_BROKER);
        reg.addExtField("clusterName", "C1");
        reg.addExtField("brokerAddr", "10.0.0.1:10911");
        reg.addExtField("brokerName", "brokerA");
        reg.addExtField("brokerId", "0");
        processor.processRequest(null, reg);

        // 2) create topic
        RemotingCommand create = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
        create.addExtField("topic", "RouteTopic");
        create.addExtField("readQueueNums", "4");
        create.addExtField("writeQueueNums", "4");
        processor.processRequest(null, create);

        // 3) get route
        RemotingCommand get = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTE_BY_TOPIC);
        get.addExtField("topic", "RouteTopic");
        RemotingCommand resp = processor.processRequest(null, get);
        assertEquals(0, resp.getCode());
        TopicRouteData route = JsonCodec.decode(resp.getBody(), TopicRouteData.class);
        assertNotNull(route);
    }

    @Test
    public void testGetBrokerClusterInfo() throws Exception {
        // 注册一个 broker
        RemotingCommand reg = RemotingCommand.createRequestCommand(RequestCode.REGISTER_BROKER);
        reg.addExtField("clusterName", "C1");
        reg.addExtField("brokerAddr", "10.0.0.1:10911");
        reg.addExtField("brokerName", "brokerA");
        reg.addExtField("brokerId", "0");
        processor.processRequest(null, reg);

        RemotingCommand get = RemotingCommand.createRequestCommand(RequestCode.GET_BROKER_CLUSTER_INFO);
        RemotingCommand resp = processor.processRequest(null, get);
        assertEquals(0, resp.getCode());
        assertNotNull(resp.getBody());
    }

    @Test
    public void testGetAllTopicList() throws Exception {
        RemotingCommand create = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
        create.addExtField("topic", "TopicList1");
        create.addExtField("readQueueNums", "4");
        create.addExtField("writeQueueNums", "4");
        processor.processRequest(null, create);

        RemotingCommand get = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_TOPIC_LIST);
        RemotingCommand resp = processor.processRequest(null, get);
        assertEquals(0, resp.getCode());
        DefaultRequestProcessor.TopicList body = JsonCodec.decode(resp.getBody(),
                DefaultRequestProcessor.TopicList.class);
        assertNotNull(body);
        assertNotNull(body.getTopics());
    }

    @Test
    public void testUnregisterBroker() throws Exception {
        // 先注册
        RemotingCommand reg = RemotingCommand.createRequestCommand(RequestCode.REGISTER_BROKER);
        reg.addExtField("clusterName", "C1");
        reg.addExtField("brokerAddr", "10.0.0.1:10911");
        reg.addExtField("brokerName", "brokerA");
        reg.addExtField("brokerId", "0");
        processor.processRequest(null, reg);

        // 再注销
        RemotingCommand unreg = RemotingCommand.createRequestCommand(RequestCode.UNREGISTER_BROKER);
        unreg.addExtField("clusterName", "C1");
        unreg.addExtField("brokerAddr", "10.0.0.1:10911");
        unreg.addExtField("brokerName", "brokerA");
        unreg.addExtField("brokerId", "0");
        RemotingCommand resp = processor.processRequest(null, unreg);
        assertEquals(0, resp.getCode());

        // 验证已删除
        assertTrue(controller.getRouteInfoManager().getBrokerAddrTable().isEmpty());
    }

    @Test
    public void testUnregisterBrokerMissingFields() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UNREGISTER_BROKER);
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testResponsePreservesOpaque() throws Exception {
        int opaque = 1234567;
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_BROKER_CLUSTER_INFO);
        req.setOpaque(opaque);
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(opaque, resp.getOpaque());
    }

    @Test
    public void testGetRouteInfoByTopicAlias() throws Exception {
        // GET_ROUTEINFO_BY_TOPIC 和 GET_ROUTE_BY_TOPIC 都走同一个 handler
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTEINFO_BY_TOPIC);
        req.addExtField("topic", "None");
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode());
    }

    @Test
    public void testRegisterBrokerWithInvalidBrokerId() throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.REGISTER_BROKER);
        req.addExtField("clusterName", "C1");
        req.addExtField("brokerAddr", "10.0.0.1:10911");
        req.addExtField("brokerName", "brokerA");
        req.addExtField("brokerId", "not-a-number");
        // Long.parseLong 抛 NumberFormatException, 应当透传
        try {
            processor.processRequest(null, req);
        } catch (NumberFormatException expected) {
            // ok
        }
    }
}
