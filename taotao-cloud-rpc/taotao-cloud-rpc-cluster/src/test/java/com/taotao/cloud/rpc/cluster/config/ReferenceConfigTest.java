package com.taotao.cloud.rpc.cluster.config;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import com.taotao.cloud.rpc.registry.InMemoryRegistryService;
import com.taotao.cloud.rpc.registry.RegistryService;
import com.taotao.cloud.rpc.remoting.RpcServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 消费者主干：{@link ReferenceConfig} 从配置到代理，中间每一段是否真的接上了。
 * <p>
 * 两条链路各测一次真 socket：直连模式、以及手工把 {@link InMemoryRegistryService} 接上之后的
 * 注册中心模式 —— 后者用来定位"注册中心模式到底断在哪一段"。
 */
class ReferenceConfigTest {

    interface Greeter {
        String greet(String who);
    }

    static final class GreeterImpl implements Greeter {
        @Override
        public String greet(String who) {
            return "hello " + who;
        }
    }

    /** 只记账不真连的 RegistryService 替身，用来证明"有没有人把实现交进来"。 */
    static final class SpyRegistry implements RegistryService {
        final AtomicInteger registerCalls = new AtomicInteger();
        final AtomicInteger subscribeCalls = new AtomicInteger();
        final AtomicInteger destroyCalls = new AtomicInteger();

        @Override
        public void register(URL url) {
            registerCalls.incrementAndGet();
        }

        @Override
        public void unregister(URL url) {
        }

        @Override
        public void subscribe(URL url, com.taotao.cloud.rpc.registry.NotifyListener listener) {
            subscribeCalls.incrementAndGet();
        }

        @Override
        public void unsubscribe(URL url, com.taotao.cloud.rpc.registry.NotifyListener listener) {
        }

        @Override
        public List<URL> lookup(URL url) {
            return java.util.Collections.emptyList();
        }

        @Override
        public void destroy() {
            destroyCalls.incrementAndGet();
        }
    }

    // ---------- 配置校验 ----------

    @Test
    @DisplayName("两个接口名都没给：init 之前就得报错")
    void missingInterfaceIsRejected() {
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setRegistryService(new InMemoryRegistryService());
        IllegalStateException e = assertThrows(IllegalStateException.class, ref::get);
        assertTrue(e.getMessage().contains("interfaceClass or interfaceName is required"), "实际: " + e.getMessage());
        assertFalse(ref.isInitialized());
    }

    @Test
    @DisplayName("只给接口名也能反查出 Class；不存在的类与不是接口的类各自报错")
    void interfaceNameIsResolvedOrRejected() {
        ReferenceConfig<Greeter> a = new ReferenceConfig<>();
        a.setInterfaceName(Greeter.class.getName());
        a.setRegistryService(new InMemoryRegistryService());
        // 没有 provider，取代理本身应当成功（目录是空的）
        assertNotNull(a.get());
        assertEquals(Greeter.class, a.getInterfaceClass());

        ReferenceConfig<Greeter> b = new ReferenceConfig<>();
        b.setInterfaceName("com.zifang.nope.Missing");
        IllegalStateException e = assertThrows(IllegalStateException.class, b::get);
        assertTrue(e.getMessage().contains("Interface class not found"), "实际: " + e.getMessage());

        ReferenceConfig<Greeter> c = new ReferenceConfig<>();
        c.setInterfaceName(String.class.getName());
        IllegalStateException e2 = assertThrows(IllegalStateException.class, c::get);
        assertTrue(e2.getMessage().contains("must be an interface"), "实际: " + e2.getMessage());
    }

    // ---------- 注册中心模式：断在哪一段 ----------

    @Test
    @DisplayName("bug_registryModeNeverGetsARegistryService：默认路径上字段恒为 null")
    void bug_registryModeNeverAssignsARegistry() {
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        ref.setRegistry("127.0.0.1:8084");

        Object proxy = ref.get();
        assertNotNull(proxy, "取代理这一步是成功的 —— 没有任何异常告诉调用方发现能力是空的");
        assertTrue(ref.isInitialized());
        assertNull(ref.getRegistryService(),
                "connectRegistry() 里唯一一行赋值被注释掉了（ZConfigRegistry 未发布），字段只能是 null");

        // 猎物 1：默认注册中心实现确实可加载，差的只是"谁来 new 它"
        RegistryService loaded = ExtensionLoader.getExtensionLoader(RegistryService.class).getDefaultExtension();
        assertNotNull(loaded);
        assertTrue(loaded instanceof InMemoryRegistryService, "实际: " + loaded.getClass());

        // 猎物 2：把实现交进去，同一条链路立刻走通到订阅
        SpyRegistry spy = new SpyRegistry();
        ReferenceConfig<Greeter> wired = new ReferenceConfig<>();
        wired.setInterfaceClass(Greeter.class);
        wired.setRegistryService(spy);
        wired.get();
        assertEquals(1, spy.subscribeCalls.get(), "接上之后 RegistryDirectory 会订阅");
    }

    @Test
    @DisplayName("bug_registryModeCallFailsWithNoProviderInsteadOfAWiringError：断链被伪装成『没有 provider』")
    void bug_brokenWiringLooksLikeMissingProvider() {
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        Greeter proxy = ref.get();

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> proxy.greet("world"));
        assertTrue(e.getMessage().contains("No provider available"),
                "实际报的是发现结果为空，而不是『注册中心根本没接上』: " + e.getMessage());
    }

    @Test
    @DisplayName("直连模式绕开注册中心：不建 socket、不订阅")
    void directModeBypassesRegistry() {
        SpyRegistry spy = new SpyRegistry();
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        ref.setRegistryService(spy);
        ref.setUrl("ttc-rpc://127.0.0.1:1;ttcrpc://127.0.0.1:2");

        Object proxy = ref.get();
        assertNotNull(proxy);
        assertEquals(0, spy.subscribeCalls.get(), "直连模式不该去订阅注册中心");
        assertNotNull(ref.getClusterInvoker());
    }

    @Test
    @DisplayName("注册中心模式：手工接上进程内注册表后，真实 socket 往返拿到结果")
    void registryModeWorksWhenHandWired() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        int port = server.getPort();
        try {
            InMemoryRegistryService registry = new InMemoryRegistryService();
            URL provider = new URL("ttc-rpc", "127.0.0.1", port);
            provider.setServiceInterface(Greeter.class.getName());
            registry.register(provider);

            ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
            ref.setInterfaceClass(Greeter.class);
            ref.setRegistryService(registry);
            Greeter proxy = ref.get();

            assertEquals("hello world", proxy.greet("world"),
                    "主干（代理→集群→目录→RpcClient→RpcServer）在注册中心模式下必须能跑通一次");
        } finally {
            server.stop();
        }
    }

    @Test
    @DisplayName("直连模式：一次真 socket 往返")
    void directModeRoundTrip() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        int port = server.getPort();
        try {
            ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
            ref.setInterfaceClass(Greeter.class);
            ref.setUrl("ttc-rpc://127.0.0.1:" + port);
            Greeter proxy = ref.get();
            assertEquals("hello direct", proxy.greet("direct"));
        } finally {
            server.stop();
        }
    }

    @Test
    @DisplayName("直连地址漏写协议名（\"://host:port\"）也要真打通：URL 解析修复的链路面")
    void directUrlWithoutProtocolNameStillRoundTrips() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        int port = server.getPort();
        try {
            ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
            ref.setInterfaceClass(Greeter.class);
            ref.setUrl("://127.0.0.1:" + port);
            Greeter proxy = ref.get();

            // 修复前 valueOf 把这条解析成 host=":"、serviceInterface="/127.0.0.1:<port>"，
            // 于是这里去连的是 ":":0 —— 报错发生在 socket 层，看不出是地址写错了
            assertEquals("hello noprotocol", proxy.greet("noprotocol"),
                    "地址里的 host:port 必须落在该落的位置上");
        } finally {
            server.stop();
        }
    }

    @Test
    @DisplayName("直连地址只有 query 没有 path：端口不再被 query 污染")
    void directUrlWithQueryAndNoPathStillRoundTrips() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        int port = server.getPort();
        try {
            ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
            ref.setInterfaceClass(Greeter.class);
            ref.setUrl("ttc-rpc://127.0.0.1:" + port + "?timeout=5000");
            Greeter proxy = ref.get();

            assertEquals("hello queried", proxy.greet("queried"),
                    "修复前 Integer.parseInt(\"<port>?timeout=5000\") 直接抛 NumberFormatException");
        } finally {
            server.stop();
        }
    }

    // ---------- 配置项有没有人读 ----------

    @Test
    @DisplayName("消费者 URL 上写满了参数，只有 retries 有人读")
    void consumerUrlCarriesEverySetting() {
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        ref.setVersion("7.7.7");
        ref.setGroup("grp");
        ref.setTimeout(1234);
        ref.setRetries(5);
        ref.setLoadbalance("leastactive");
        ref.setCluster("broadcast");
        ref.setUrl("ttc-rpc://127.0.0.1:1");

        ref.get();
        URL u = ref.getClusterInvoker().getUrl();
        assertEquals("7.7.7", u.getVersion());
        assertEquals("grp", u.getGroup());
        assertEquals("1234", u.getParameter("timeout"));
        assertEquals("5", u.getParameter("retries"));
        assertEquals("leastactive", u.getParameter("loadbalance"));
        assertEquals("broadcast", u.getParameter("cluster"));
        assertEquals("consumer", u.getParameter("side"));
        assertEquals("consumer", u.getProtocol());
    }

    @Test
    @DisplayName("bug_clusterSettingNeverSelectsAnImplementation：换策略名，拿到的还是 failover")
    void bug_clusterSettingIsIgnored() {
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        ref.setCluster("broadcast");
        ref.setUrl("ttc-rpc://127.0.0.1:1");
        ref.get();

        String impl = ref.getClusterInvoker().getClass().getName();
        assertTrue(impl.contains("FailoverCluster"), "实际实现: " + impl);

        // 猎物：扩展表里 broadcast 是一个真实存在的名字，只是没人拿它去查
        java.net.URL meta = ReferenceConfigTest.class.getClassLoader()
                .getResource("META-INF/ttc-rpc/com.taotao.cloud.rpc.cluster.Cluster");
        assertNotNull(meta);
        assertTrue(readAll(meta).contains("broadcast=com.taotao.cloud.rpc.cluster.BroadcastCluster"),
                "表里有这个名字，配置也写了这个名字，但创建处是 new FailoverCluster()");
    }

    private static String readAll(java.net.URL u) {
        // InputStream.readAllBytes() 是 JDK 9 API；这仓在 -source 8 下构建，JDK 8 机器上编译不过
        try (java.io.InputStream in = u.openStream()) {
            java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
            byte[] chunk = new byte[4096];
            int n;
            while ((n = in.read(chunk)) != -1) {
                buf.write(chunk, 0, n);
            }
            return new String(buf.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @DisplayName("bug_timeoutSettingNeverReachesTheSocket：50ms 的配置挡不住 3 秒的默认超时")
    void bug_timeoutSettingIsWriteOnly() throws Exception {
        // 只接受连接、永不回应的假服务端：真实超时时间只能从耗时上量出来
        try (ServerSocket blocker = new ServerSocket(0)) {
            int port = blocker.getLocalPort();
            Thread acceptor = new Thread(() -> {
                List<java.net.Socket> held = new java.util.ArrayList<>();
                while (!blocker.isClosed()) {
                    try {
                        held.add(blocker.accept());
                    } catch (IOException e) {
                        return;
                    }
                }
                for (java.net.Socket s : held) {
                    try {
                        s.close();
                    } catch (IOException ignored) {
                    }
                }
            }, "blocker-acceptor");
            acceptor.setDaemon(true);
            acceptor.start();

            ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
            ref.setInterfaceClass(Greeter.class);
            ref.setTimeout(50);
            ref.setRetries(0);
            ref.setUrl("ttc-rpc://127.0.0.1:" + port);
            Greeter proxy = ref.get();

            long start = System.nanoTime();
            assertThrows(Throwable.class, () -> proxy.greet("slow"));
            long elapsedMs = (System.nanoTime() - start) / 1_000_000L;
            assertTrue(elapsedMs >= 1000,
                    "配置的是 50ms，实测等了 " + elapsedMs + "ms —— RpcClient.timeout 恒为默认 3000ms");
        }
    }

    // ---------- 生命周期 ----------

    @Test
    @DisplayName("destroy() 之后 get() 拒绝重建，且把注册中心一起销毁")
    void destroyIsOneWay() {
        SpyRegistry spy = new SpyRegistry();
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        ref.setRegistryService(spy);
        ref.get();
        assertNotNull(ref.getRef());

        ref.destroy();
        assertTrue(ref.getDestroyed().get());
        assertNull(ref.getRef());
        assertFalse(ref.isInitialized());
        assertEquals(1, spy.destroyCalls.get());
        assertThrows(IllegalStateException.class, ref::get);
        assertThrows(IllegalStateException.class, ref::get);
    }

    @Test
    @DisplayName("bug_destroyedFlagIsHandedOutMutable：getDestroyed() 交出的是活对象")
    void bug_destroyedFlagIsLive() {
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        ref.setRegistryService(new InMemoryRegistryService());
        ref.get();
        assertFalse(ref.getDestroyed().get());

        ref.getDestroyed().set(true);
        assertThrows(IllegalStateException.class, ref::get, "外部一行就把配置判死了，没有任何校验拦住它");
        ref.getDestroyed().set(false);
        assertNotNull(ref.get(), "同一个对象能把门闸掰回来");

        // 对照：另一组 public setter 从反方向救不回 destroyed —— 门闸只有这一个入口能改
        ReferenceConfig<Greeter> other = new ReferenceConfig<>();
        other.setInterfaceClass(Greeter.class);
        other.setRegistryService(new InMemoryRegistryService());
        other.get();
        other.destroy();
        other.setRef(who -> "resurrected");
        other.setInitialized(true);
        assertThrows(IllegalStateException.class, other::get);
        other.getDestroyed().set(false);
        assertEquals("resurrected", other.get().greet("x"),
                "关掉门闸后，一个从没走过 init() 的 ref 被原样交出去");
    }

    @Test
    @DisplayName("bug_destroyUsesTheSharedRegistryInstance：ExtensionLoader 给的是单例")
    void bug_destroyTakesDownSharedRegistry() {
        RegistryService one = ExtensionLoader.getExtensionLoader(RegistryService.class).getDefaultExtension();
        RegistryService two = ExtensionLoader.getExtensionLoader(RegistryService.class).getDefaultExtension();
        assertSame(one, two, "两个 ReferenceConfig 拿到的是同一个注册表实例");

        InMemoryRegistryService reg = (InMemoryRegistryService) one;
        URL provider = new URL("ttc-rpc", "127.0.0.1", 9);
        provider.setServiceInterface("com.zifang.demo.Shared");
        reg.register(provider);
        assertEquals(1, reg.lookup(provider).size());

        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        ref.setRegistryService(one);
        ref.destroy();

        assertTrue(reg.lookup(provider).isEmpty(),
                "一个引用的 destroy() 清掉了全进程注册表 —— 别的消费者从此看不见任何 provider");
    }

    @Test
    @DisplayName("get() 幂等：同一个代理对象复用")
    void getIsIdempotent() {
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Greeter.class);
        ref.setRegistryService(new InMemoryRegistryService());
        assertSame(ref.get(), ref.get());
    }
}
