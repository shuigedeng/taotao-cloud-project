package com.taotao.cloud.mq.consistency.ttc.nameserver.routeinfo;

import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.QueueData;
import com.taotao.cloud.mq.consistency.ttc.common.TopicConfig;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.common.testsupport.BenchResult;
import com.taotao.cloud.mq.consistency.ttc.nameserver.routeinfo.RouteInfoManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RouteInfoManager 查询性能测试 — 测量路由表在不同规模下的查询吞吐量。
 */
public class RouteInfoManagerBenchmarkTest {

    @TempDir
    Path tempDir;

    private RouteInfoManager rim;

    @BeforeEach
    public void setUp() {
        rim = new RouteInfoManager();
    }

    @Test
    void testRouteLookup100Topics() {
        populateRoutes(100, 1);
        BenchResult bench = BenchResult.runBest3("RouteInfoManager", "pickup-100-topics", 50_000, 3_000,
                () -> rim.pickupTopicRouteData("Topic_50"));
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
        bench.assertBelowThreshold(50.0, "pickup route 100 topics");
    }

    @Test
    void testRouteLookup1000Topics() {
        populateRoutes(1_000, 2);
        BenchResult bench = BenchResult.runBest3("RouteInfoManager", "pickup-1000-topics", 50_000, 3_000,
                () -> rim.pickupTopicRouteData("Topic_500"));
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
        bench.assertBelowThreshold(100.0, "pickup route 1000 topics");
    }

    @Test
    void testRouteLookup10000Topics() {
        populateRoutes(10_000, 4);
        BenchResult bench = BenchResult.runBest3("RouteInfoManager", "pickup-10000-topics", 20_000, 2_000,
                () -> rim.pickupTopicRouteData("Topic_5000"));
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
        bench.assertBelowThreshold(200.0, "pickup route 10000 topics");
    }

    @Test
    void testRegisterBrokerConcurrent() {
        int count = 1_000;
        BenchResult bench = BenchResult.runBest3("RouteInfoManager", "registerBroker-" + count, count, 100,
                () -> rim.registerBroker(
                        "Cluster", "10.0.0.1:10911", "BrokerX", 0L, "", null));
        bench.printToConsole();
        bench.writeToFile(tempDir.resolve("benchmark.txt"));
        bench.assertBelowThreshold(500.0, "registerBroker");
    }

    private void populateRoutes(int topicCount, int brokerCount) {
        for (int i = 0; i < brokerCount; i++) {
            rim.registerBroker("Cluster_" + i, "10.0.0." + i + ":10911", "Broker_" + i, 0L, "", null);
        }
        for (int i = 0; i < topicCount; i++) {
            for (int b = 0; b < brokerCount; b++) {
                TopicConfig tc = new TopicConfig("Topic_" + i, 4, 4, TopicConfig.PERM_READ_WRITE);
                rim.registerTopic("Broker_" + b, tc);
            }
        }
    }
}
