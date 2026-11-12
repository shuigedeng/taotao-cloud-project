package com.taotao.cloud.rpc.cluster.config;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.registry.RegistryService;
import com.taotao.cloud.rpc.remoting.RpcServer;
import io.netty.channel.Channel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 服务提供方主干：{@link ServiceConfig} 的配置项有没有落到 socket 上、有没有落到注册中心。
 * <p>
 * 全部用真端口跑真 socket（端口每次现取），因为这一层的"导出成功"只有落在
 * 监听套接字与路由表上才算数。
 */
class ServiceConfigTest {

    interface Echo {
        String say(String what);
    }

    static final class EchoImpl implements Echo {
        @Override
        public String say(String what) {
            return "echo:" + what;
        }
    }

    static final class SpyRegistry implements RegistryService {
        final AtomicInteger registerCalls = new AtomicInteger();
        volatile URL last;

        @Override
        public void register(URL url) {
            last = url;
            registerCalls.incrementAndGet();
        }

        @Override
        public void unregister(URL url) {
        }

        @Override
        public void subscribe(URL url, com.taotao.cloud.rpc.registry.NotifyListener listener) {
        }

        @Override
        public void unsubscribe(URL url, com.taotao.cloud.rpc.registry.NotifyListener listener) {
        }

        @Override
        public List<URL> lookup(URL url) {
            return Collections.emptyList();
        }

        @Override
        public void destroy() {
        }
    }

    private static int freePort() throws IOException {
        try (ServerSocket s = new ServerSocket(0)) {
            return s.getLocalPort();
        }
    }

    private static ServiceConfig<Echo> config(int port) {
        ServiceConfig<Echo> cfg = new ServiceConfig<>();
        cfg.setInterfaceClass(Echo.class);
        cfg.setRef(new EchoImpl());
        cfg.setHost("127.0.0.1");
        cfg.setPort(port);
        return cfg;
    }

    /** 真实绑定端口从 channel 上独立量：`getPort()` 正是被比对的那一侧，不能拿它当参照。 */
    private static int boundPort(RpcServer server) throws Exception {
        Field f = RpcServer.class.getDeclaredField("channel");
        f.setAccessible(true);
        Channel ch = (Channel) f.get(server);
        assertNotNull(ch, "channel 为 null 说明根本没绑定成功");
        return ((java.net.InetSocketAddress) ch.localAddress()).getPort();
    }

    // ---------- 配置校验 ----------

    @Test
    @DisplayName("接口、实现、两者匹配，三样都缺不了")
    void checkConfigRejectsIncompleteSetups() {
        ServiceConfig<Echo> noInterface = new ServiceConfig<>();
        noInterface.setRef(new EchoImpl());
        assertTrue(assertThrows(IllegalStateException.class, noInterface::export).getMessage()
                .contains("interfaceClass or interfaceName is required"));

        ServiceConfig<Echo> noRef = new ServiceConfig<>();
        noRef.setInterfaceClass(Echo.class);
        assertTrue(assertThrows(IllegalStateException.class, noRef::export).getMessage().contains("ref is required"));

        // 泛型写法下『接口与实现不匹配』根本编译不过（setRef 收不到错类型），只能绕 interfaceName 进来
        ServiceConfig<Echo> bad = new ServiceConfig<>();
        bad.setInterfaceName("java.util.List");
        bad.setRef(new EchoImpl());
        assertTrue(assertThrows(IllegalStateException.class, bad::export).getMessage()
                .contains("ref is not an instance of"));

        // 实现类只是"长得像"也不行：Echo 只有一个方法，lambda 恰好是其实现，因此必须换一个真不匹配的类型
        ServiceConfig<Echo> ok = new ServiceConfig<>();
        ok.setInterfaceClass(Echo.class);
        ok.setRef(what -> "not the impl");
        assertFalse(ok.isExported());
        assertTrue(assertThrows(IllegalStateException.class,
                () -> {
                    ServiceConfig<Echo> missing = new ServiceConfig<>();
                    missing.setInterfaceName("com.shuigedeng.nope.Nope");
                    missing.setRef(new EchoImpl());
                    missing.export();
                }).getMessage().contains("Interface class not found"));
    }

    // ---------- 服务 URL ----------

    @Test
    @DisplayName("服务 URL 收下了全部配置项，但 weight 有两个互不相通的地方")
    void serviceUrlCarriesConfig() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.setVersion("5.0.0");
        cfg.setGroup("grp");
        cfg.setWeight(300);
        cfg.setTimeout(1200);
        cfg.setThreads(32);
        cfg.setLoadbalance("roundrobin");
        cfg.setCluster("broadcast");
        cfg.setProtocol("thrift");
        cfg.export();
        try {
            URL u = cfg.getServiceUrl();
            assertEquals("thrift", u.getProtocol());
            assertEquals("127.0.0.1", u.getHost());
            assertEquals(cfg.getRpcServer().getPort(), u.getPort(),
                    "URL 里的端口必须等于内核真正分配的那个");
            assertEquals(Echo.class.getName(), u.getServiceInterface());
            assertEquals("grp", u.getGroup());
            assertEquals("5.0.0", u.getVersion());
            assertEquals("300", u.getParameter("weight"));
            assertEquals("1200", u.getParameter("timeout"));
            assertEquals("32", u.getParameter("threads"));
            assertEquals("roundrobin", u.getParameter("loadbalance"));
            assertEquals("broadcast", u.getParameter("cluster"));
            assertEquals("provider", u.getParameter("side"));
            assertEquals("grp/" + Echo.class.getName() + ":5.0.0", u.getServiceKey());

            // 参数里写着 300，字段上却是 0：同一个 URL 的两处真值互不相通
            assertEquals(0, u.getWeight(), "URL.weight 这个字段从来没人写");
            assertTrue(u.getParameters().containsKey("weight"));
        } finally {
            cfg.unexport();
        }
    }

    @Test
    @DisplayName("bug_versionNeverReachesTheServerRouteTable：注册地址带版本，路由表只有裸接口名")
    void bug_versionIsDroppedOnRegistration() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.setVersion("5.0.0");
        cfg.export();
        try {
            Map<String, Object> routes = cfg.getRpcServer().getServiceMap();
            assertEquals(Collections.singleton(Echo.class.getName()), routes.keySet(),
                    "注册地址是 " + cfg.getServiceUrl().getServiceKey() + "，路由表却只按裸接口名收");
            assertFalse(routes.containsKey(Echo.class.getName() + ":5.0.0"));
            assertTrue(cfg.getServiceUrl().getServiceKey().endsWith(":5.0.0"));

            // 猎物：同一个 RpcServer 支持带版本注册，缺的只是 ServiceConfig 没往下传
            RpcServer bare = new RpcServer("127.0.0.1", 0);
            bare.register(Echo.class, new EchoImpl(), "5.0.0");
            assertTrue(bare.getServiceMap().containsKey(Echo.class.getName() + ":5.0.0"),
                    "实际: " + bare.getServiceMap().keySet());
        } finally {
            cfg.unexport();
        }
    }

    @Test
    @DisplayName("bug_protocolSettingDoesNotChangeTheWire：写 thrift，跑的仍是自家 10 字节帧")
    void bug_protocolSettingDoesNotChangeTheFrame() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.setProtocol("thrift");
        cfg.export();
        try {
            assertEquals("thrift", cfg.getServiceUrl().getProtocol());
            assertEquals("com.taotao.cloud.rpc.remoting.RpcServer", cfg.getRpcServer().getClass().getName());

            // 消费者按默认协议来，一点不受"provider 声明了 thrift"的影响
            ReferenceConfig<Echo> ref = new ReferenceConfig<>();
            ref.setInterfaceClass(Echo.class);
            ref.setUrl("ttc-rpc://127.0.0.1:" + cfg.getServiceUrl().getPort());
            assertEquals("echo:hi", ref.get().say("hi"));
        } finally {
            cfg.unexport();
        }
    }

    // ---------- 导出与端口 ----------

    @Test
    @DisplayName("绑 0 监听成功之后，发布出去的地址写的就是内核分配的真实端口")
    void portZeroPublishesTheBoundPort() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.export();
        try {
            RpcServer server = cfg.getRpcServer();
            int actual = boundPort(server);
            assertTrue(actual > 0, "内核分配的真实端口应当 >0，实测 " + actual);
            assertEquals(actual, cfg.getServiceUrl().getPort(),
                    "监听在 " + actual + "，要发布/注册的那条 URL 就得写同一个端口，实收 "
                            + cfg.getServiceUrl().getPort());

            // 猎物：消费侧不再需要手工换端口 —— 照发布出去的那条地址就能打通
            ReferenceConfig<Echo> ref = new ReferenceConfig<>();
            ref.setInterfaceClass(Echo.class);
            ref.setUrl("ttc-rpc://127.0.0.1:" + cfg.getServiceUrl().getPort());
            assertEquals("echo:auto", ref.get().say("auto"));

            // getPort() 的另一半（§7 第 27 行）：绑 0 之后它交回真实端口，starter 日志不再打印 :0
            assertEquals(actual, server.getPort(),
                    "getPort() 应当交回内核挑的那个端口，实收 " + server.getPort());
        } finally {
            cfg.unexport();
        }
    }

    @Test
    @DisplayName("setRpcServer() 注入过的那台必须被复用，而不是再 new 一台去抢同一个端口")
    void injectedServerIsReusedInsteadOfANewBind() throws Exception {
        RpcServer injected = new RpcServer("127.0.0.1", 0);
        injected.start(true);
        int port = injected.getPort();
        assertTrue(port > 0, "注入的这台应当已绑定，实收 " + port);

        ServiceConfig<Echo> cfg = config(port);
        cfg.setRpcServer(injected);
        cfg.export();
        try {
            assertSame(injected, cfg.getRpcServer(),
                    "export() 把注入的服务器换掉了：同端口的第二台 bind 必然失败，服务也就没注册上");

            ReferenceConfig<Echo> ref = new ReferenceConfig<>();
            ref.setInterfaceClass(Echo.class);
            ref.setUrl("ttc-rpc://127.0.0.1:" + port);
            assertEquals("echo:reuse", ref.get().say("reuse"),
                    "调用要落到注入的那台服务器上才说明注册没丢");
        } finally {
            cfg.unexport();
        }
    }

    @Test
    @DisplayName("export() 起的那个常驻线程 parked 在 start() 里，也不能把 unexport() 锁死")
    void unexportReturnsWhileTheServerThreadIsParkedInsideStart() throws Exception {
        int port = freePort();
        ServiceConfig<Echo> cfg = config(port);
        cfg.export();

        // export() 只等 isStarted()，那个线程可能还在 await 前一行，所以轮询而不是当场断言
        final Thread[] holder = new Thread[1];
        waitUntil(() -> {
            Thread t = threadNamed("TtcRpcServer-" + port);
            holder[0] = t;
            return t != null && parkedInsideStart(t);
        }, 3000);
        Thread serverThread = holder[0];
        assertNotNull(serverThread, "找不到 ServiceConfig 起的那个常驻线程，猎物没进圈");
        assertTrue(parkedInsideStart(serverThread),
                "这个线程正停在 RpcServer.start() 里等 closeFuture —— 曾经就是它抱着 RpcServer 的监视器");

        long start = System.nanoTime();
        java.util.concurrent.ExecutorService ex = java.util.concurrent.Executors.newSingleThreadExecutor();
        try {
            ex.submit(cfg::unexport).get(10, java.util.concurrent.TimeUnit.SECONDS);
        } catch (java.util.concurrent.TimeoutException e) {
            throw new AssertionError("unexport() 十分钟也不会返回：stop() 等不到被 start() 占着的监视器", e);
        } finally {
            ex.shutdownNow();
        }
        long ms = (System.nanoTime() - start) / 1_000_000L;
        assertFalse(cfg.isExported());
        assertFalse(cfg.getRpcServer().isStarted(), "实际耗时 " + ms + "ms");

        serverThread.join(5000);
        assertFalse(serverThread.isAlive(), "unexport() 只花了 " + ms + "ms，但那个线程还挂着");
    }

    @Test
    @DisplayName("端口被占时绑定失败，不许留下永不退出的 Netty 线程")
    void failedBindShutsTheEventLoopGroupsDown() throws Exception {
        // 占位端口由自己 bind(0) 拿到并一直持有：探针式取号（探完关掉再去 bind）
        // 在"关掉"与"占住"之间有抢端口窗口，别人抢先 bind 就等于这台没占住。
        try (ServerSocket occupied = new ServerSocket(0)) {
            int port = occupied.getLocalPort();
            int watermark = highestEventLoopPoolId();
            ServiceConfig<Echo> cfg = config(port);
            cfg.export();
            assertFalse(cfg.getRpcServer().isStarted(), "前提：这台服务器确实没绑上");

            // shutdownGracefully 有 2s quiet period，轮询而不是硬等
            waitUntil(() -> highestEventLoopPoolId() <= watermark, 8000);
            assertTrue(highestEventLoopPoolId() <= watermark,
                    "poolId > " + watermark + " 的组是这次失败绑定新建的，它的线程还在 —— 绑定失败没关 EventLoopGroup");
        }
    }

    private static boolean parkedInsideStart(Thread t) {
        for (StackTraceElement e : t.getStackTrace()) {
            if ("com.taotao.cloud.rpc.remoting.RpcServer".equals(e.getClassName())
                    && "start".equals(e.getMethodName())) {
                return true;
            }
        }
        return false;
    }

    private static Thread threadNamed(String name) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (name.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    /** Netty 线程名是 {@code nioEventLoopGroup-<poolId>-<serial>}，poolId 全局递增，用它区分"这次新建的组"。 */
    private static int highestEventLoopPoolId() {
        int max = -1;
        String prefix = "nioEventLoopGroup-";
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            String n = t.getName();
            if (!n.startsWith(prefix)) {
                continue;
            }
            int rest = prefix.length();
            int dash = n.indexOf('-', rest);
            if (dash < 0) {
                continue;
            }
            try {
                max = Math.max(max, Integer.parseInt(n.substring(rest, dash)));
            } catch (NumberFormatException ignore) {
                // 名字不合式的线程不参与计数，宁可漏也不要把别的组算进来
            }
        }
        return max;
    }

    @Test
    @DisplayName("bug_exportReportsSuccessEvenWhenTheBindFailed：端口被占也标 exported=true")
    void bug_exportLiesWhenBindFails() throws Exception {
        // 同上：占位端口自己 bind(0) 并持有，"这个端口上还在听的是我"就不再有竞态
        try (ServerSocket occupied = new ServerSocket(0)) {
            int port = occupied.getLocalPort();
            ServiceConfig<Echo> cfg = config(port);
            long start = System.nanoTime();
            cfg.export();
            long elapsedMs = (System.nanoTime() - start) / 1_000_000L;

            assertTrue(cfg.isExported(), "调用方看到的旗子是绿的");
            assertFalse(cfg.getRpcServer().isStarted(),
                    "而服务端其实从未绑定成功（这一条实测让 export() 白等了 " + elapsedMs + "ms）");
            try (Socket probe = new Socket("127.0.0.1", port)) {
                assertTrue(probe.isConnected(), "端口上还在听的是那个占位的 ServerSocket，不是 RPC 服务");
            }
        }
    }

    @Test
    @DisplayName("export() 幂等；unexport() 之后再 export() 才被封死")
    void exportIsIdempotentUntilUnexported() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.export();
        URL first = cfg.getServiceUrl();
        cfg.export();
        org.junit.jupiter.api.Assertions.assertSame(first, cfg.getServiceUrl(), "第二次 export() 应当整个跳过");
        cfg.unexport();

        assertFalse(cfg.isExported());
        assertTrue(cfg.getDestroyed().get());
        assertThrows(IllegalStateException.class, cfg::export);
        cfg.unexport();
    }

    @Test
    @DisplayName("没导出过时 unexport() 是彻底的空操作")
    void unexportBeforeExportDoesNothing() {
        ServiceConfig<Echo> cfg = new ServiceConfig<>();
        cfg.setInterfaceClass(Echo.class);
        cfg.setRef(new EchoImpl());
        cfg.unexport();
        assertFalse(cfg.isExported());
        assertFalse(cfg.getDestroyed().get(), "此时连 destroyed 都没被置上");
        assertNull(cfg.getServiceUrl());
    }

    // ---------- 延迟导出 ----------

    @Test
    @DisplayName("delay>0 时 export() 立刻返回，此时什么都还没建")
    void delayedExportReturnsBeforeAnythingExists() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.setDelay(400);
        cfg.export();
        try {
            assertFalse(cfg.isExported());
            assertNull(cfg.getServiceUrl());
            assertNull(cfg.getRpcServer());

            waitUntil(() -> cfg.isExported(), 5000);
            assertTrue(cfg.isExported());
            assertNotNull(cfg.getRpcServer());
            assertTrue(cfg.getRpcServer().isStarted());
        } finally {
            cfg.unexport();
        }
    }

    @Test
    @DisplayName("bug_unexportDuringDelayWindowIsSwallowed：取消导出没拦住稍后才起来的服务器")
    void bug_unexportDuringDelayIsIgnored() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.setDelay(400);
        cfg.export();
        cfg.unexport();

        assertFalse(cfg.isExported());
        assertFalse(cfg.getDestroyed().get(), "unexport() 在 exported=false 时就 return 了，连门闸都没落下");
        waitUntil(() -> cfg.isExported(), 5000);

        // 调用方已经"取消"了导出，端口却在这之后才绑上，且没人再管它
        assertTrue(cfg.getRpcServer().isStarted(), "延迟导出的线程不看 destroyed");
        try (Socket s = new Socket("127.0.0.1", cfg.getServiceUrl().getPort())) {
            assertTrue(s.isConnected());
        }
        cfg.unexport();
        assertFalse(cfg.getRpcServer().isStarted(), "只有再 unexport 一次才收得回来");
    }

    // ---------- 注册中心 ----------

    @Test
    @DisplayName("bug_registrySettingOnlyProducesLogLines：设了 registry 也从未注册过")
    void bug_registrySettingNeverRegisters() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.setRegistry("127.0.0.1:8084");
        cfg.export();
        try {
            assertTrue(cfg.isExported());
            assertNull(cfg.getRegistryService(),
                    "registerToRegistry() 里唯一那行赋值被注释掉了，紧接着就 registryService.register(..) 空指针");
        } finally {
            cfg.unexport();
        }

        // 猎物：把实现交进去，同一个 export() 就真的会去注册
        SpyRegistry spy = new SpyRegistry();
        ServiceConfig<Echo> wired = config(0);
        wired.setRegistry("127.0.0.1:8084");
        wired.setRegistryService(spy);
        wired.export();
        try {
            assertEquals(1, spy.registerCalls.get());
            assertNotNull(spy.last);
            assertEquals(Echo.class.getName(), spy.last.getServiceInterface());
        } finally {
            wired.unexport();
        }
    }

    @Test
    @DisplayName("没设 registry 时跳过注册，也不报错")
    void emptyRegistryIsSkippedQuietly() throws Exception {
        SpyRegistry spy = new SpyRegistry();
        ServiceConfig<Echo> cfg = config(0);
        cfg.setRegistryService(spy);
        cfg.export();
        try {
            assertEquals(0, spy.registerCalls.get());
        } finally {
            cfg.unexport();
        }
    }

    @Test
    @DisplayName("setExported(true) 会让 export() 整个跳过：真实服务一台都不起")
    void bug_exportedFlagSetterSkipsTheWholeExport() throws Exception {
        ServiceConfig<Echo> cfg = config(0);
        cfg.setExported(true);
        cfg.export();
        assertTrue(cfg.isExported());
        assertNull(cfg.getRpcServer(), "旗子已经绿了，export() 直接 return");
        assertNull(cfg.getServiceUrl());
    }

    // ---------- 小工具 ----------

    private static void waitUntil(BooleanCondition cond, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (cond.holds()) {
                return;
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private interface BooleanCondition {
        boolean holds();
    }
}
