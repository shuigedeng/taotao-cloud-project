package com.taotao.cloud.mq.consistency.ttc.nameserver.routeinfo;

import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.QueueData;
import com.taotao.cloud.mq.consistency.ttc.common.RegisterBrokerResult;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RouteInfoManager 增强单元测试 — 覆盖 registerTopic / 多 broker 路由 / unregister 流程。
 */
public class RouteInfoManagerExtraTest {

    private RouteInfoManager mgr;

    @BeforeEach
    public void setUp() {
        mgr = new RouteInfoManager();
    }

    @Test
    public void testRegisterAndUnregisterMasterBroker() {
        mgr.registerBroker("C1", "10.0.0.1:10911", "brokerA", 0L, "10.0.0.1:10912", null);
        Map<String, BrokerData> brokerTable = mgr.getBrokerAddrTable();
        assertEquals(1, brokerTable.size());
        assertTrue(brokerTable.containsKey("brokerA"));

        mgr.unregisterBroker("C1", "10.0.0.1:10911", "brokerA", 0L);
        assertEquals(0, mgr.getBrokerAddrTable().size());
        assertEquals(0, mgr.getClusterAddrTable().size());
    }

    @Test
    public void testRegisterBrokerIdZeroSetsHaAddr() {
        RegisterBrokerResult r = mgr.registerBroker(
                "C1", "10.0.0.1:10911", "brokerA", 0L, "10.0.0.1:10912", null);
        assertEquals("10.0.0.1:10912", r.getHaServerAddr());
    }

    @Test
    public void testRegisterBrokerNonZeroIdNoHa() {
        // 非 master (brokerId != 0) 不应回填 haServerAddr
        RegisterBrokerResult r = mgr.registerBroker(
                "C1", "10.0.0.2:10911", "brokerA", 1L, "10.0.0.2:10912", null);
        assertNull(r.getHaServerAddr());
    }

    @Test
    public void testRegisterBrokerMasterAddrOnReplace() {
        mgr.registerBroker("C1", "addr-old:10911", "brokerA", 0L, null, null);
        // 重新注册同 broker, masterAddr 应为上一次的值
        RegisterBrokerResult r = mgr.registerBroker("C1", "addr-new:10911", "brokerA", 0L, null, null);
        assertEquals("addr-old:10911", r.getMasterAddr());
        // selectBrokerAddr 返回最新的 master
        BrokerData bd = mgr.getBrokerAddrTable().get("brokerA");
        assertEquals("addr-new:10911", bd.selectBrokerAddr());
    }

    @Test
    public void testRegisterMultipleBrokersInSameCluster() {
        mgr.registerBroker("C1", "10.0.0.1:10911", "brokerA", 0L, null, null);
        mgr.registerBroker("C1", "10.0.0.2:10911", "brokerB", 0L, null, null);
        Set<String> names = mgr.getBrokerNamesByCluster("C1");
        assertNotNull(names);
        assertEquals(2, names.size());
        assertTrue(names.contains("brokerA"));
        assertTrue(names.contains("brokerB"));
    }

    @Test
    public void testRegisterTopicStoresQueueData() {
        mgr.registerBroker("C1", "10.0.0.1:10911", "brokerA", 0L, null, null);
        TopicConfig tc = new TopicConfig("TopicA", 4, 4, TopicConfig.PERM_READ_WRITE);
        mgr.registerTopic("brokerA", tc);

        Map<String, List<QueueData>> topicTable = mgr.getTopicQueueTable();
        assertTrue(topicTable.containsKey("TopicA"));
        List<QueueData> qds = topicTable.get("TopicA");
        assertEquals(1, qds.size());
        assertEquals("brokerA", qds.get(0).getBrokerName());
        assertEquals(4, qds.get(0).getReadQueueNums());
        assertEquals(4, qds.get(0).getWriteQueueNums());
    }

    @Test
    public void testRegisterTopicReplacesSameBroker() {
        mgr.registerBroker("C1", "10.0.0.1:10911", "brokerA", 0L, null, null);
        TopicConfig tc1 = new TopicConfig("TopicA", 4, 4, TopicConfig.PERM_READ_WRITE);
        TopicConfig tc2 = new TopicConfig("TopicA", 8, 8, TopicConfig.PERM_READ);
        mgr.registerTopic("brokerA", tc1);
        mgr.registerTopic("brokerA", tc2);

        List<QueueData> qds = mgr.getTopicQueueTable().get("TopicA");
        // 同 broker 重复注册, 应替换而不是新增
        assertEquals(1, qds.size());
        assertEquals(8, qds.get(0).getReadQueueNums());
        assertEquals(TopicConfig.PERM_READ, qds.get(0).getPerm());
    }

    @Test
    public void testRegisterTopicWithNullInputsIsSafe() {
        mgr.registerTopic(null, new TopicConfig("T"));
        mgr.registerTopic("brokerA", null);
        mgr.registerTopic("brokerA", new TopicConfig());
        // 都应当 noop 不抛
        assertTrue(mgr.getTopicQueueTable().isEmpty());
    }

    @Test
    public void testPickupTopicRouteDataMultiBroker() {
        mgr.registerBroker("C1", "10.0.0.1:10911", "brokerA", 0L, null, null);
        mgr.registerBroker("C1", "10.0.0.2:10911", "brokerB", 0L, null, null);
        mgr.registerTopic("brokerA", new TopicConfig("T", 4, 4, TopicConfig.PERM_READ_WRITE));
        mgr.registerTopic("brokerB", new TopicConfig("T", 4, 4, TopicConfig.PERM_READ_WRITE));

        TopicRouteData route = mgr.pickupTopicRouteData("T");
        assertNotNull(route);
        assertEquals(2, route.getQueueDatas().size());
        assertEquals(2, route.getBrokerDatas().size());
    }

    @Test
    public void testPickupTopicRouteDataMissingReturnsNull() {
        assertNull(mgr.pickupTopicRouteData("NEVER_REGISTERED"));
    }

    @Test
    public void testGetSystemTopicListIncludesClusters() {
        mgr.registerBroker("C1", "10.0.0.1:10911", "brokerA", 0L, null, null);
        Set<String> sys = mgr.getSystemTopicList();
        assertTrue(sys.contains("C1"));
    }

    @Test
    public void testGetTopicQueueTableReturnsCopy() {
        mgr.registerTopic("brokerA", new TopicConfig("T", 4, 4, TopicConfig.PERM_READ_WRITE));
        Map<String, List<QueueData>> snap = mgr.getTopicQueueTable();
        snap.clear();
        // 内部状态不变
        assertEquals(1, mgr.getTopicQueueTable().size());
    }

    @Test
    public void testGetBrokerAddrTableReturnsCopy() {
        mgr.registerBroker("C1", "10.0.0.1:10911", "brokerA", 0L, null, null);
        Map<String, BrokerData> snap = mgr.getBrokerAddrTable();
        snap.clear();
        assertEquals(1, mgr.getBrokerAddrTable().size());
    }

    @Test
    public void testUnregisterBrokerNotExists() {
        // unregister 一个没注册的 broker 不抛
        mgr.unregisterBroker("C1", "10.0.0.99:10911", "brokerZ", 0L);
        assertTrue(mgr.getBrokerAddrTable().isEmpty());
    }

    @Test
    public void testClusterAddrTableCleanup() {
        mgr.registerBroker("C1", "10.0.0.1:10911", "brokerA", 0L, null, null);
        assertTrue(mgr.getClusterAddrTable().containsKey("C1"));
        mgr.unregisterBroker("C1", "10.0.0.1:10911", "brokerA", 0L);
        // 集群下没 broker 后, 整个集群 entry 应当清除
        assertFalse(mgr.getClusterAddrTable().containsKey("C1"));
    }
}
