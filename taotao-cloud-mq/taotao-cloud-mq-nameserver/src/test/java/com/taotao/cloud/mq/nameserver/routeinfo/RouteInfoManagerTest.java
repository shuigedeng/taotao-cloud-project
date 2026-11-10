package com.taotao.cloud.mq.nameserver.routeinfo;

import com.taotao.cloud.mq.common.RegisterBrokerResult;
import com.taotao.cloud.mq.common.TopicRouteData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RouteInfoManager 单元测试
 */
public class RouteInfoManagerTest {

    private RouteInfoManager routeInfoManager;

    @BeforeEach
    public void setUp() {
        routeInfoManager = new RouteInfoManager();
    }

    @Test
    public void testConstructor() {
        assertNotNull(routeInfoManager);
    }

    @Test
    public void testRegisterBroker() {
        String clusterName = "TestCluster";
        String brokerName = "TestBroker";
        String brokerAddr = "192.168.1.100:10911";
        long brokerId = 0;
        String haServerAddr = "192.168.1.100:10912";

        // Register broker with null topicConfig
        RegisterBrokerResult result = routeInfoManager.registerBroker(
                clusterName, brokerName, brokerAddr, brokerId,
                haServerAddr, null);

        assertNotNull(result);
    }

    @Test
    public void testUnregisterBroker() {
        String clusterName = "TestCluster";
        String brokerName = "TestBroker";
        String brokerAddr = "192.168.1.100:10911";

        // First register the broker
        routeInfoManager.registerBroker(clusterName, brokerName, brokerAddr, 0,
                "192.168.1.100:10912", null);

        // Then unregister
        routeInfoManager.unregisterBroker(clusterName, brokerAddr, brokerName, 0);

        // The method should complete without exception
        assertTrue(true);
    }

    @Test
    public void testPickupTopicRouteData() {
        String topicName = "TestTopic";

        // Without any brokers, should return null
        TopicRouteData routeData = routeInfoManager.pickupTopicRouteData(topicName);

        // No data available yet
        assertNull(routeData);
    }

    @Test
    public void testGetClusterInfo() {
        Map<String, Set<String>> clusterInfo = routeInfoManager.getClusterInfo();

        assertNotNull(clusterInfo);
    }

    @Test
    public void testGetSystemTopicList() {
        Set<String> topicList = routeInfoManager.getSystemTopicList();

        assertNotNull(topicList);
    }

    @Test
    public void testGetBrokerNamesByCluster() {
        Set<String> brokerNames = routeInfoManager.getBrokerNamesByCluster("TestCluster");

        // Should return null or empty set for non-existent cluster
        assertTrue(brokerNames == null || brokerNames.isEmpty());
    }
}
