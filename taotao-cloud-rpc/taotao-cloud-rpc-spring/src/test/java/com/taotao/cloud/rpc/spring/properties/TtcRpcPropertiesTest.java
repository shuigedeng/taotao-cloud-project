package com.taotao.cloud.rpc.spring.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link TtcRpcProperties} 默认值与宽松绑定。
 */
class TtcRpcPropertiesTest {

    @Test
    @DisplayName("prefix 必须是 ttc.rpc")
    void prefixIsTtcRpc() {
        ConfigurationProperties ann = AnnotatedElementUtils.findMergedAnnotation(
                TtcRpcProperties.class, ConfigurationProperties.class);
        assertNotNull(ann, "TtcRpcProperties 必须带 @ConfigurationProperties");
        assertEquals("ttc.rpc", ann.prefix());
        // Spring Boot 2 里 prefix 与 value 是 @AliasFor 关系，两者必须同值，
        // 否则用户写 value="ttc.rpc" 会静默失效。
        assertEquals(ann.prefix(), ann.value(), "prefix 与 value 必须是同一个键");
        assertEquals("ttc.rpc", ann.value());
    }

    @Test
    @DisplayName("顶层默认值")
    void topLevelDefaults() {
        TtcRpcProperties p = new TtcRpcProperties();
        assertTrue(p.isEnabled());
        assertNotNull(p.getApplication());
        assertNotNull(p.getRegistry());
        assertNotNull(p.getServer());
        assertNotNull(p.getConsumer());
        assertNotNull(p.getProvider());
        assertNotNull(p.getProtocol());
    }

    @Test
    @DisplayName("Application 默认值")
    void applicationDefaults() {
        TtcRpcProperties.Application a = new TtcRpcProperties().getApplication();
        assertEquals("ttc-rpc-app", a.getName());
        assertEquals("1.0.0", a.getVersion());
        assertEquals("shuigedeng", a.getOrganization());
    }

    @Test
    @DisplayName("Registry 默认值")
    void registryDefaults() {
        TtcRpcProperties.Registry r = new TtcRpcProperties().getRegistry();
        assertEquals("127.0.0.1:8084", r.getAddress());
        assertEquals("public", r.getNamespace());
        assertEquals("ttc-config", r.getType());
        assertTrue(r.isEnabled(), "注册中心默认开启");
    }

    @Test
    @DisplayName("Server 默认值")
    void serverDefaults() {
        TtcRpcProperties.Server s = new TtcRpcProperties().getServer();
        assertTrue(s.isEnabled());
        assertEquals("0.0.0.0", s.getHost());
        assertEquals(20880, s.getPort());
        assertEquals(200, s.getThreads());
        assertEquals(8, s.getIoThreads());
        assertEquals(8 * 1024 * 1024, s.getPayload());
    }

    @Test
    @DisplayName("Consumer 默认值")
    void consumerDefaults() {
        TtcRpcProperties.Consumer c = new TtcRpcProperties().getConsumer();
        assertEquals(3000, c.getTimeout());
        assertEquals(2, c.getRetries());
        assertEquals("random", c.getLoadbalance());
        assertEquals("failover", c.getCluster());
        assertEquals(1, c.getConnections());
        assertFalse(c.isCheck());
        assertFalse(c.isAsync());
        assertEquals("hessian2", c.getSerialization());
    }

    @Test
    @DisplayName("Provider 默认值")
    void providerDefaults() {
        TtcRpcProperties.Provider pr = new TtcRpcProperties().getProvider();
        assertEquals(5000, pr.getTimeout(), "provider 超时与 consumer 不同，非对称是有意还是笔误需确认");
        assertEquals(200, pr.getThreads());
        assertEquals(0, pr.getDelay());
        assertEquals(100, pr.getWeight());
        assertFalse(pr.isAsync());
        assertFalse(pr.isToken());
        assertEquals("hessian2", pr.getSerialization());
    }

    @Test
    @DisplayName("Protocol 默认值")
    void protocolDefaults() {
        TtcRpcProperties.Protocol pr = new TtcRpcProperties().getProtocol();
        assertEquals("ttc-rpc", pr.getName());
        assertEquals(20880, pr.getPort());
        assertEquals("hessian2", pr.getSerialization());
        assertEquals(1024, pr.getCompressThreshold());
    }

    @Test
    @DisplayName("宽松绑定：kebab-case 属性写入嵌套对象")
    void bindsKebabCaseNestedKeys() {
        Map<String, Object> props = new LinkedHashMap<String, Object>();
        props.put("ttc.rpc.enabled", "false");
        props.put("ttc.rpc.application.name", "order-service");
        props.put("ttc.rpc.registry.address", "10.0.0.9:8848");
        props.put("ttc.rpc.registry.type", "in-memory");
        props.put("ttc.rpc.server.host", "127.0.0.1");
        props.put("ttc.rpc.server.port", "29999");
        props.put("ttc.rpc.server.io-threads", "3");
        props.put("ttc.rpc.consumer.timeout", "777");
        props.put("ttc.rpc.consumer.loadbalance", "leastactive");
        props.put("ttc.rpc.provider.weight", "55");
        props.put("ttc.rpc.protocol.name", "tri");
        props.put("ttc.rpc.protocol.compress-threshold", "2048");

        Binder binder = new Binder(new MapConfigurationPropertySource(props));
        TtcRpcProperties p = binder.bind("ttc.rpc", Bindable.of(TtcRpcProperties.class)).get();

        assertFalse(p.isEnabled());
        assertEquals("order-service", p.getApplication().getName());
        assertEquals("10.0.0.9:8848", p.getRegistry().getAddress());
        assertEquals("in-memory", p.getRegistry().getType());
        assertEquals("127.0.0.1", p.getServer().getHost());
        assertEquals(29999, p.getServer().getPort());
        assertEquals(3, p.getServer().getIoThreads());
        assertEquals(777, p.getConsumer().getTimeout());
        assertEquals("leastactive", p.getConsumer().getLoadbalance());
        assertEquals(55, p.getProvider().getWeight());
        assertEquals("tri", p.getProtocol().getName());
        assertEquals(2048, p.getProtocol().getCompressThreshold());
    }

    @Test
    @DisplayName("ttc.rpc.server.port 与 ttc.rpc.protocol.port 是两个互不相干的端口")
    void serverAndProtocolPortsAreIndependent() {
        Map<String, Object> props = new LinkedHashMap<String, Object>();
        props.put("ttc.rpc.server.port", "11111");
        Binder binder = new Binder(new MapConfigurationPropertySource(props));
        TtcRpcProperties p = binder.bind("ttc.rpc", Bindable.of(TtcRpcProperties.class)).get();

        assertEquals(11111, p.getServer().getPort());
        assertEquals(20880, p.getProtocol().getPort(),
                "改了 server.port 而 protocol.port 仍是默认值；两者谁生效没有任何文档说明");
    }

    @Test
    @DisplayName("provider.compress-threshold 不存在，只有 protocol.compress-threshold")
    void unknownKeyIsIgnoredSilently() {
        Map<String, Object> props = new LinkedHashMap<String, Object>();
        props.put("ttc.rpc.provider.threads", "42");
        Binder binder = new Binder(new MapConfigurationPropertySource(props));
        TtcRpcProperties p = binder.bind("ttc.rpc", Bindable.of(TtcRpcProperties.class)).get();
        assertEquals(42, p.getProvider().getThreads());
    }

    @Test
    @DisplayName("setter 全部可用且 getServer 等可整体替换")
    void settersReplaceWholeSections() {
        TtcRpcProperties p = new TtcRpcProperties();
        TtcRpcProperties.Server s = new TtcRpcProperties.Server();
        s.setPort(1);
        p.setServer(s);
        assertEquals(1, p.getServer().getPort());

        TtcRpcProperties.Application a = new TtcRpcProperties.Application();
        a.setName("x");
        p.setApplication(a);
        assertEquals("x", p.getApplication().getName());

        TtcRpcProperties.Registry r = new TtcRpcProperties.Registry();
        r.setEnabled(false);
        p.setRegistry(r);
        assertFalse(p.getRegistry().isEnabled());

        TtcRpcProperties.Consumer c = new TtcRpcProperties.Consumer();
        c.setCheck(true);
        p.setConsumer(c);
        assertTrue(p.getConsumer().isCheck());

        TtcRpcProperties.Provider pr = new TtcRpcProperties.Provider();
        pr.setToken(true);
        p.setProvider(pr);
        assertTrue(p.getProvider().isToken());

        TtcRpcProperties.Protocol pt = new TtcRpcProperties.Protocol();
        pt.setName("ttc-rpc");
        p.setProtocol(pt);
        assertEquals("ttc-rpc", p.getProtocol().getName());
    }
}
