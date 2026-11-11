package com.taotao.cloud.rpc.cluster;

import com.taotao.cloud.rpc.cluster.loadbalance.LeastActiveLoadBalance;
import com.taotao.cloud.rpc.cluster.loadbalance.LoadBalance;
import com.taotao.cloud.rpc.cluster.loadbalance.RandomLoadBalance;
import com.taotao.cloud.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import com.taotao.cloud.rpc.common.spi.SPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SPI 登记表与 @SPI 注解是否对得上。
 * <p>
 * 结论（实测）：{@code META-INF/ttc-rpc/} 下给 Cluster / LoadBalance 各登记了一份实现清单，
 * 但这两个接口都没有打 {@code @SPI}，ExtensionLoader 在第一步就把它们拒了 —— 登记表是死文件。
 */
class SpiRegistrationLivenessTest {

    @SPI("probe")
    interface ProbeSPI {
        String mark();
    }

    public static class ProbeImpl implements ProbeSPI {
        @Override public String mark() { return "probe"; }
    }

    @Test
    @DisplayName("正向对照：打了 @SPI 的接口 + 同名资源文件，ExtensionLoader 能加载")
    void annotatedInterfaceWithResourceFileLoads() {
        assertNotNull(ProbeSPI.class.getAnnotation(SPI.class));
        ExtensionLoader<ProbeSPI> loader = ExtensionLoader.getExtensionLoader(ProbeSPI.class);
        assertEquals(Arrays.asList("probe"), loader.getExtensionNames());
        assertEquals("probe", loader.getExtension("probe").mark());
    }

    @Test
    @DisplayName("Cluster 已补 @SPI：5 个实现全部可经 ExtensionLoader 取到，默认 failover")
    void clusterIsSpiLoadable() {
        assertNotNull(Cluster.class.getAnnotation(SPI.class), "Cluster 应当带 @SPI");
        assertEquals("failover", Cluster.class.getAnnotation(SPI.class).value());
        assertNotNull(ClassLoader.getSystemResource("META-INF/ttc-rpc/" + Cluster.class.getName()));

        ExtensionLoader<Cluster> loader = ExtensionLoader.getExtensionLoader(Cluster.class);
        assertTrue(loader.getExtensionNames().containsAll(
                Arrays.asList("failover", "failfast", "failsafe", "failback", "broadcast")),
                loader.getExtensionNames().toString());
        assertEquals("failover", loader.getDefaultExtension().getName());
        assertTrue(loader.getExtension("broadcast") instanceof BroadcastCluster);
    }

    @Test
    @DisplayName("LoadBalance 已补 @SPI：3 个实现可加载，默认 random；RegistryService 同修")
    void loadBalanceAndRegistryAreSpiLoadable() {
        assertNotNull(LoadBalance.class.getAnnotation(SPI.class), "LoadBalance 应当带 @SPI");
        assertEquals("random", LoadBalance.class.getAnnotation(SPI.class).value());

        ExtensionLoader<LoadBalance> loader = ExtensionLoader.getExtensionLoader(LoadBalance.class);
        assertTrue(loader.getExtensionNames().containsAll(
                Arrays.asList("random", "roundrobin", "leastactive")), loader.getExtensionNames().toString());
        assertEquals("random", loader.getDefaultExtension().getName());

        // 同一条修复的另一半：登记表里指向 .disabled 源码的两行已注释掉，
        // 否则 ExtensionLoader 一次性加载全部行会让每个名字都抛 IllegalStateException。
        assertNotNull(com.taotao.cloud.rpc.registry.RegistryService.class.getAnnotation(SPI.class));
        ExtensionLoader<com.taotao.cloud.rpc.registry.RegistryService> reg =
                ExtensionLoader.getExtensionLoader(com.taotao.cloud.rpc.registry.RegistryService.class);
        assertEquals(Arrays.asList("in-memory"), reg.getExtensionNames());
        // 原登记表点名的 InMemoryRpcRegistry 实现的其实是另一套 RpcRegistry 接口，
        // 与 RegistryService 没有继承关系 —— 所以真正可加载的是新补的 URL 契约实现。
        assertTrue(reg.getDefaultExtension()
                instanceof com.taotao.cloud.rpc.registry.InMemoryRegistryService);
        assertFalse(com.taotao.cloud.rpc.registry.RegistryService.class
                        .isAssignableFrom(com.taotao.cloud.rpc.registry.InMemoryRpcRegistry.class),
                "prey：InMemoryRpcRegistry 不属于 RegistryService 类型体系，旧登记表必然加载失败");
    }

    @Test
    @DisplayName("登记表里点名的 5 个 Cluster / 3 个 LoadBalance 类都真实存在且实现接口")
    void registeredClassesAllExistAndImplementTheirInterface() throws Exception {
        List<String> clusters = Arrays.asList("FailoverCluster", "FailfastCluster", "FailsafeCluster",
                "FailbackCluster", "BroadcastCluster");
        for (String simple : clusters) {
            Class<?> c = Class.forName("com.taotao.cloud.rpc.cluster." + simple);
            assertTrue(Cluster.class.isAssignableFrom(c), simple);
        }
        assertEquals(5, clusters.size());
        List<String> lbs = Arrays.asList("RandomLoadBalance", "RoundRobinLoadBalance", "LeastActiveLoadBalance");
        for (String simple : lbs) {
            Class<?> c = Class.forName("" + simple);
            assertTrue(LoadBalance.class.isAssignableFrom(c), simple);
        }
        assertEquals(3, lbs.size());
        // 也就是说：只差一个注解，登记表立刻可用
    }

    @Test
    @DisplayName("Cluster 的 5 个实现各自报告自己的名字，且 NAME 常量与登记 key 一致")
    void clusterNamesMatchRegistryKeys() {
        assertEquals("failover", new FailoverCluster().getName());
        assertEquals("failfast", new FailfastCluster().getName());
        assertEquals("failsafe", new FailsafeCluster().getName());
        assertEquals("failback", new FailbackCluster().getName());
        assertEquals("broadcast", new BroadcastCluster().getName());
        assertEquals(FailoverCluster.NAME, new FailoverCluster().getName());
        assertEquals(BroadcastCluster.NAME, new BroadcastCluster().getName());
    }

    @Test
    @DisplayName("LoadBalance 的 3 个实现名字与登记 key 一致")
    void loadBalanceNamesMatchRegistryKeys() {
        assertEquals("random", new RandomLoadBalance().getName());
        assertEquals("roundrobin", new RoundRobinLoadBalance().getName());
        assertEquals("leastactive", new LeastActiveLoadBalance().getName());
        assertEquals(RandomLoadBalance.NAME,
                new RandomLoadBalance().getName());
    }
}
