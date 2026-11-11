package com.taotao.cloud.rpc.core.context;

import com.taotao.cloud.rpc.async.RpcContext;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.filter.ConsumerTraceFilter;
import com.taotao.cloud.rpc.filter.ProviderContextFilter;
import com.taotao.cloud.rpc.filter.ProviderMonitorFilter;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.invoke.RpcInvocation;
import com.taotao.cloud.rpc.metrics.MetricsCollector;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 上下文传递的跨进程端到端：真实的 socket、真实的 {@link RpcMessageEncoder}/{@link RpcMessageDecoder}、
 * 真实的 {@link RpcServer} + {@link RpcServerHandler}；消费端走
 * {@code ConsumerTraceFilter -> RpcClientHolder}，与 ReferenceConfig 直连模式里那个匿名 Invoker 同一条路。
 * <p>
 * 这批用例钉住的是一个二元结论：attachment <b>能</b>跨过网络（第一组证明），
 * 但到了 Provider 侧就被丢在地上（第二组证明）—— 因为 RpcServerHandler 直接反射调 POJO，
 * 中间没有任何 Invoker 可供 provider 组过滤器插进去。
 */
class ContextPropagationE2ETest {

    public interface TraceProbe {
        String whoAmI();
    }

    /** Provider 侧探针：把"我在哪个线程、我看见了什么"随返回值带回来。 */
    public static class ProbeImpl implements TraceProbe {
        volatile String seenThread;
        volatile String seenTraceInMdc;

        @Override
        public String whoAmI() {
            seenThread = Thread.currentThread().getName();
            seenTraceInMdc = ThreadContext.get("traceId");
            return String.valueOf(seenTraceInMdc);
        }
    }

    private static final class CaptureServer {
        final BlockingQueue<RpcRequest> inbound = new LinkedBlockingQueue<RpcRequest>();
        int port;
        private NioEventLoopGroup boss;
        private NioEventLoopGroup worker;
        private Channel channel;

        CaptureServer(int port) {
            this.port = port;
        }

        void start() throws Exception {
            boss = new NioEventLoopGroup(1);
            worker = new NioEventLoopGroup();
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 编解码用生产实现，只把最后一个 handler 换成"记下来并回一句"
                            ch.pipeline().addLast(new RpcMessageDecoder());
                            ch.pipeline().addLast(new RpcMessageEncoder());
                            ch.pipeline().addLast(new CaptureHandler(inbound));
                        }
                    });
            channel = b.bind("127.0.0.1", port).sync().channel();
            // 绑 0 时端口只有 localAddress 知道，回填给读侧（srv.port 就是本类里所有 URL 的取号处）
            port = ((java.net.InetSocketAddress) channel.localAddress()).getPort();
        }

        RpcRequest take() throws InterruptedException {
            RpcRequest r = inbound.poll(5, TimeUnit.SECONDS);
            assertNotNull(r, "5 秒内没收到任何请求 —— 链路根本没通，后面的断言都是空跑");
            return r;
        }

        void stop() {
            if (channel != null) {
                channel.close();
            }
            closeQuietly(boss);
            closeQuietly(worker);
        }
    }

    private static final class CaptureHandler extends SimpleChannelInboundHandler<RpcRequest> {
        private final BlockingQueue<RpcRequest> inbound;

        CaptureHandler(BlockingQueue<RpcRequest> inbound) {
            this.inbound = inbound;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) {
            inbound.add(msg);
            ctx.writeAndFlush(RpcResponse.success(msg.getRequestId(), "acked:" + msg.getMethodName()));
        }
    }

    private final List<CaptureServer> captureServers = new ArrayList<CaptureServer>();
    private RpcServer providerServer;

    private static void closeQuietly(NioEventLoopGroup group) {
        if (group != null) {
            // 有上限且不被中断打断：收尾卡住会把整个 fork 拖死。
            // quietPeriod 给 0，否则 Netty 默认 2s 静默期会把每个用例垫高 4s。
            group.shutdownGracefully(0, 2, TimeUnit.SECONDS).awaitUninterruptibly(5, TimeUnit.SECONDS);
        }
    }

    /** 绑 0 让内核选端口，再从 channel 的 localAddress 读回真实端口 —— 探针式取号在"关掉探测 socket"与"真正 bind"之间有抢端口竞态。 */
    private CaptureServer startCaptureServer() throws Exception {
        CaptureServer s = new CaptureServer(0);
        s.start();
        captureServers.add(s);
        return s;
    }

    /** 真实 RpcServer；端口由内核分配，取号一律走 {@code RpcServer.getPort()}。 */
    private int startProviderServer(RegistrationConfigurer cfg) throws Exception {
        RpcServer s = new RpcServer("127.0.0.1", 0);
        cfg.apply(s);
        s.start(true);
        for (int i = 0; i < 50 && !s.isStarted(); i++) {
            Thread.sleep(10);
        }
        providerServer = s;
        assertTrue(s.isStarted(), "服务端没起来，后面的断言全是空跑");
        return s.getPort();
    }

    private interface RegistrationConfigurer {
        void apply(RpcServer server);
    }

    /** 与 ReferenceConfig 直连模式里的匿名 Invoker 同一条路：RpcClientHolder -> RpcClient.invoke。 */
    private static Invoker<Object> directInvoker(final URL url) {
        return new Invoker<Object>() {
            @Override
            public Class<Object> getInterface() {
                return Object.class;
            }

            @Override
            public Result invoke(Invocation invocation) {
                return RpcClientHolder.invoke(invocation, url);
            }

            @Override
            public URL getUrl() {
                return url;
            }

            @Override
            public boolean isAvailable() {
                return true;
            }

            @Override
            public void destroy() {
            }
        };
    }

    private static URL urlFor(int port, String serviceInterface, String version, String group) {
        URL url = new URL("ttc-rpc", "127.0.0.1", port, serviceInterface);
        url.setVersion(version);
        url.setGroup(group);
        return url;
    }

    private static Invocation probeInvocation(String serviceInterface) {
        return new RpcInvocation(serviceInterface, "whoAmI", new Class<?>[0], new Object[0]);
    }

    @BeforeEach
    void isolate() {
        RpcContext.clear();
        ThreadContext.clearAll();
        MetricsCollector.getInstance().reset();
    }

    @AfterEach
    void tearDown() throws Exception {
        for (CaptureServer s : captureServers) {
            s.stop();
        }
        captureServers.clear();
        if (providerServer != null) {
            providerServer.stop();
            providerServer = null;
        }
        // RpcClient 用的是非守护 EventLoop，留着会拖住整个 fork
        for (RpcClient c : drainHolderCache()) {
            c.close();
        }
    }

    @SuppressWarnings("unchecked")
    private static List<RpcClient> drainHolderCache() throws Exception {
        Field f = RpcClientHolder.class.getDeclaredField("CLIENTS");
        f.setAccessible(true);
        Map<String, RpcClient> clients = (Map<String, RpcClient>) f.get(null);
        List<RpcClient> copy = new ArrayList<RpcClient>(clients.values());
        clients.clear();
        return copy;
    }

    // ==================== attachment 确实跨过了网络 ====================

    @Test
    @DisplayName("E2E：ConsumerTraceFilter 写的 trace-id 真的出现在对端收到的请求里")
    void traceAttachmentCrossesTheRealSocket() throws Throwable {
        CaptureServer srv = startCaptureServer();
        String traceId = RpcContext.getContext().getTraceId();
        assertNotNull(traceId);

        Invocation inv = probeInvocation(TraceProbe.class.getName());
        inv.setAttachment("keep-me", "caller-side");
        URL url = urlFor(srv.port, TraceProbe.class.getName(), "2.3.4", "grp-a");

        Result r = new ConsumerTraceFilter().invoke(directInvoker(url), inv);
        assertFalse(r.hasException(), "实际异常: " + r.getException());

        RpcRequest seen = srv.take();
        // 猎物：不止 trace-id —— 调用方自己塞的键也完整过了线，说明这个 map 真被序列化过去了
        assertEquals("caller-side", seen.getAttachments().get("keep-me"),
                "attachments 没跨线的话这条最先红");
        assertEquals(traceId, seen.getAttachments().get("trace-id"),
                "对端应看到 ConsumerTraceFilter 写入的 trace-id，实收 " + seen.getAttachments());
        // 回答来自对端而不是本地代码
        assertEquals("acked:whoAmI", r.getValue());
        assertEquals("whoAmI", seen.getMethodName());
        assertArrayEquals(new Class<?>[0], seen.getParameterTypes());
    }

    @Test
    @DisplayName("E2E：RpcClient 用 URL 的 version/group 覆写调用方同名 attachment")
    void urlAttachmentsClobberCallerOnTheWire() throws Throwable {
        CaptureServer srv = startCaptureServer();
        Invocation inv = probeInvocation(TraceProbe.class.getName());
        inv.setAttachment("version", "0.0.0-caller");
        inv.setAttachment("group", "caller-group");
        URL url = urlFor(srv.port, TraceProbe.class.getName(), "9.9.9", "url-group");

        new ConsumerTraceFilter().invoke(directInvoker(url), inv);
        RpcRequest seen = srv.take();
        assertEquals("9.9.9", seen.getAttachments().get("version"));
        assertEquals("url-group", seen.getAttachments().get("group"));
        // 覆写只发生在线上副本，没碰本地 invocation 对象
        assertEquals("0.0.0-caller", inv.getAttachment("version"), "prey：调用方对象未被改写");
    }

    @Test
    @DisplayName("bug_ URL 未设 version 时，调用方显式给的 version attachment 被覆成 null")
    void bug_nullUrlVersionErasesCallerVersion() throws Throwable {
        CaptureServer srv = startCaptureServer();
        Invocation inv = probeInvocation(TraceProbe.class.getName());
        inv.setAttachment("version", "1.2.3");
        URL url = urlFor(srv.port, TraceProbe.class.getName(), null, null);

        new ConsumerTraceFilter().invoke(directInvoker(url), inv);
        RpcRequest seen = srv.take();
        Map<String, String> att = seen.getAttachments();
        assertTrue(att.containsKey("version"), "prey：键确实被写进去了");
        assertNull(att.get("version"), "实收 " + att);
        // 后果：两个 getter 的"默认值"都插手不了 —— Map.getOrDefault 对"键在、值为 null"返回 null
        assertNull(seen.getVersion());
        assertNull(seen.getGroup(), "group 也一样被覆成 null，getOrDefault 的默认值形同虚设");
    }

    @Test
    @DisplayName("bug_ 同线程连续两次调用共用一个 trace-id，对端只能靠 requestId 区分")
    void bug_oneThreadCannotDistinguishItsOwnCalls() throws Throwable {
        CaptureServer srv = startCaptureServer();
        final URL url = urlFor(srv.port, TraceProbe.class.getName(), "1.0.0", null);
        Invoker<Object> withTrace = new Invoker<Object>() {
            private final Invoker<Object> target = directInvoker(url);

            @Override
            public Class<Object> getInterface() {
                return Object.class;
            }

            @Override
            public Result invoke(Invocation invocation) throws Throwable {
                return new ConsumerTraceFilter().invoke(target, invocation);
            }

            @Override
            public URL getUrl() {
                return url;
            }

            @Override
            public boolean isAvailable() {
                return true;
            }

            @Override
            public void destroy() {
            }
        };
        withTrace.invoke(probeInvocation(TraceProbe.class.getName()));
        withTrace.invoke(probeInvocation(TraceProbe.class.getName()));

        RpcRequest first = srv.take();
        RpcRequest second = srv.take();
        assertEquals(first.getAttachments().get("trace-id"), second.getAttachments().get("trace-id"),
                "链路标识是线程级的：同一线程的两次调用带同一个 trace-id");
        assertNotEquals(first.getRequestId(), second.getRequestId(),
                "prey：这确实是两次不同的请求");
    }

    @Test
    @DisplayName("bug_ 换线程就换 trace-id：同一次业务在不同线程上算两条链路")
    void bug_traceIdChangesWithThreadAcrossTheWire() throws Throwable {
        final CaptureServer srv = startCaptureServer();
        final URL url = urlFor(srv.port, TraceProbe.class.getName(), "1.0.0", null);
        final String mainTrace = RpcContext.getContext().getTraceId();
        final Throwable[] failure = new Throwable[1];

        Thread child = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new ConsumerTraceFilter().invoke(directInvoker(url),
                            probeInvocation(TraceProbe.class.getName()));
                } catch (Throwable t) {
                    failure[0] = t;
                }
            }
        }, "ctx-e2e-child");
        child.start();
        child.join(10_000);
        assertNull(failure[0], "子线程调用失败: " + failure[0]);

        RpcRequest seen = srv.take();
        String childTrace = seen.getAttachments().get("trace-id");
        assertNotNull(childTrace);
        assertNotEquals(mainTrace, childTrace, "线程本地 trace-id 各自为政");
        // 猎物：主线程自己的 context 一行没动
        assertEquals(mainTrace, RpcContext.getContext().getTraceId());
    }

    // ==================== 到了 Provider 侧就被丢掉 ====================

    @Test
    @DisplayName("bug_ 真实服务端不把 trace-id 还原进 MDC，业务代码看见 null")
    void bug_providerBusinessCodeSeesNoTraceId() throws Throwable {
        final ProbeImpl impl = new ProbeImpl();
        int port = startProviderServer(new RegistrationConfigurer() {
            @Override
            public void apply(RpcServer s) {
                s.registerService(TraceProbe.class, impl);
            }
        });
        String traceId = RpcContext.getContext().getTraceId();
        Invocation inv = probeInvocation(TraceProbe.class.getName());
        URL url = urlFor(port, TraceProbe.class.getName(), null, null);

        Result r = new ConsumerTraceFilter().invoke(directInvoker(url), inv);
        assertFalse(r.hasException(), "实际异常: " + r.getException());
        // 前置：请求侧确实带上了 trace-id
        assertEquals(traceId, inv.getAttachment("trace-id"));

        assertEquals("null", String.valueOf(r.getValue()),
                "业务方法在 MDC 里应当什么也拿不到");
        assertNull(impl.seenTraceInMdc);
        // 猎物 1：这确实是服务端 IO 线程执行的，不是本地退化调用
        assertNotNull(impl.seenThread);
        assertTrue(impl.seenThread.contains("nioEventLoopGroup"),
                "Provider 线程名应来自服务端的 Netty 组，实测 " + impl.seenThread);
        assertNotEquals(Thread.currentThread().getName(), impl.seenThread, "必须是另一个线程");
        // 猎物 2：能力本身是有的 —— 只要有人把 provider 组过滤器接上去
        ProviderContextFilter filter = new ProviderContextFilter();
        final List<String> insideMdc = new ArrayList<String>();
        filter.invoke(providerInvoker(TraceProbe.class, new Body() {
            @Override
            public Result run(Invocation invocation) {
                insideMdc.add(ThreadContext.get("traceId"));
                return Result.success("x");
            }
        }), inv);
        assertEquals(Arrays.asList(traceId), insideMdc, "prey：手工挂上过滤器就能还原 trace-id");
        assertNull(ThreadContext.get("traceId"), "过滤器收尾要清干净");
    }

    @Test
    @DisplayName("bug_ 真实服务端一个指标都不产：provider 侧监控在链路上不可达")
    void bug_providerMetricsAreNeverRecordedOnTheRealPath() throws Throwable {
        MetricsCollector collector = MetricsCollector.getInstance();
        assertTrue(collector.getAll().isEmpty(), "前置：起始无桶");
        int port = startProviderServer(new RegistrationConfigurer() {
            @Override
            public void apply(RpcServer s) {
                s.registerService(TraceProbe.class, new ProbeImpl() {
                    @Override
                    public String whoAmI() {
                        throw new IllegalStateException("provider-boom");
                    }
                });
            }
        });
        URL url = urlFor(port, TraceProbe.class.getName(), null, null);
        Result r = RpcClientHolder.invoke(probeInvocation(TraceProbe.class.getName()), url);
        assertTrue(r.hasException(), "业务异常应回传，实收 " + r.getValue());

        assertTrue(collector.getAll().isEmpty(),
                "真实服务端调用之后仍无桶，说明 ProviderMonitorFilter 从未被装配: "
                        + collector.getAll().keySet());

        // 猎物：同一个过滤器手工挂上去就会记到桶
        Invoker<Object> boom = providerInvoker(TraceProbe.class, new Body() {
            @Override
            public Result run(Invocation invocation) throws Throwable {
                throw new IllegalStateException("local-boom");
            }
        });
        Throwable thrown = null;
        try {
            new ProviderMonitorFilter().invoke(boom, probeInvocation(TraceProbe.class.getName()));
        } catch (Throwable t) {
            thrown = t;
        }
        assertNotNull(thrown, "prey：手工挂载时异常会从过滤器穿出");
        assertEquals(IllegalStateException.class, thrown.getClass());
        assertEquals("local-boom", thrown.getMessage());
        Map<String, Map<String, Object>> after = collector.getAll();
        assertEquals(1, after.size(), after.keySet().toString());
        Map<String, Object> bucket = after.values().iterator().next();
        assertEquals(TraceProbe.class.getName() + ":whoAmI",
                bucket.get("service") + ":" + bucket.get("method"));
        assertEquals(Long.valueOf(1L), bucket.get("error"));
        assertEquals(Long.valueOf(0L), bucket.get("success"));
    }

    @Test
    @DisplayName("bug_ 服务端拼键只看 version attachment，group 注册永远命中不了")
    void bug_groupScopedRegistrationIsUnreachable() throws Exception {
        final String iface = TraceProbe.class.getName();
        // 按消费端 URL.getServiceKey() 的格式注册一条带 group 的服务
        URL consumerUrl = urlFor(0, iface, "1.0.0", "gray");
        Map<String, Object> services = new ConcurrentHashMap<String, Object>();
        services.put(consumerUrl.getServiceKey(), versioned("in-gray-group"));
        EmbeddedChannel channel = new EmbeddedChannel(new RpcServerHandler(services));
        try {
            assertEquals("gray/" + iface + ":1.0.0", consumerUrl.getServiceKey(),
                    "prey：消费端确实会拼出这种键");
            RpcRequest req = plainRequest(iface, "1.0.0");
            req.getAttachments().put("group", "gray");
            channel.writeInbound(req);
            RpcResponse resp = channel.readOutbound();
            assertNotNull(resp.getException(),
                    "带 group 的注册条目谁也别想命中，实收 result=" + resp.getResult());
            assertTrue(String.valueOf(resp.getErrorMessage()).contains("Service not found"),
                    String.valueOf(resp.getErrorMessage()));

            // 猎物：同一个请求，只要把注册换成不含 group 的键就通了 —— 差别只在服务端不读 group
            Map<String, Object> flat = new ConcurrentHashMap<String, Object>();
            flat.put(RpcServer.serviceKey(iface, "1.0.0"), versioned("flat"));
            EmbeddedChannel second = new EmbeddedChannel(new RpcServerHandler(flat));
            try {
                RpcRequest again = plainRequest(iface, "1.0.0");
                again.getAttachments().put("group", "gray");
                second.writeInbound(again);
                RpcResponse resp2 = second.readOutbound();
                assertEquals("flat", resp2.getResult(), "group 换成什么都不影响命中");
            } finally {
                second.finishAndReleaseAll();
            }
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    @Test
    @DisplayName("bug_ 未带 version 的请求被默认成 1.0.0，会抢走同接口的裸键注册")
    void bug_defaultVersionStealsTheBareKeyRegistration() throws Exception {
        final String iface = TraceProbe.class.getName();
        Map<String, Object> services = new ConcurrentHashMap<String, Object>();
        services.put(iface, versioned("bare"));
        services.put(RpcServer.serviceKey(iface, "1.0.0"), versioned("versioned"));
        EmbeddedChannel channel = new EmbeddedChannel(new RpcServerHandler(services));
        try {
            RpcRequest naked = plainRequest(iface, null);
            assertFalse(naked.getAttachments().containsKey("version"), "prey：请求里根本没有 version");
            channel.writeInbound(naked);
            RpcResponse resp = channel.readOutbound();
            assertEquals("versioned", resp.getResult(),
                    "请求没带 version，却命中了 :1.0.0 那条注册");

            // 猎物：表里没有 :1.0.0 条目时，同一个请求改走 fallback 命中裸键
            Map<String, Object> onlyBare = new ConcurrentHashMap<String, Object>();
            onlyBare.put(iface, services.get(iface));
            EmbeddedChannel second = new EmbeddedChannel(new RpcServerHandler(onlyBare));
            try {
                second.writeInbound(plainRequest(iface, null));
                RpcResponse resp2 = second.readOutbound();
                assertEquals("bare", resp2.getResult());
            } finally {
                second.finishAndReleaseAll();
            }
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    private static ProbeImpl versioned(final String answer) {
        return new ProbeImpl() {
            @Override
            public String whoAmI() {
                return answer;
            }
        };
    }

    private static RpcRequest plainRequest(String iface, String version) {
        RpcRequest req = new RpcRequest();
        req.setRequestId("req-" + System.nanoTime());
        req.setInterfaceName(iface);
        req.setMethodName("whoAmI");
        req.setParameterTypes(new Class<?>[0]);
        req.setArguments(new Object[0]);
        if (version != null) {
            req.getAttachments().put("version", version);
        }
        return req;
    }

    // ==================== 连接缓存 ====================

    @Test
    @DisplayName("bug_ 关掉的 RpcClient 会永久留在 holder 缓存里，之后每次都失败")
    void bug_closedClientIsPoisonForever() throws Throwable {
        CaptureServer srv = startCaptureServer();
        URL url = urlFor(srv.port, TraceProbe.class.getName(), "1.0.0", null);

        Result ok = RpcClientHolder.invoke(probeInvocation(TraceProbe.class.getName()), url);
        assertFalse(ok.hasException(), "前置：连接好的时候调得通，实际 " + ok.getException());
        assertEquals("acked:whoAmI", ok.getValue());

        RpcClient cached = RpcClientHolder.get("127.0.0.1", srv.port);
        cached.close();
        assertFalse(cached.isConnected(), "prey：close 之后确实断了");
        assertSame(cached, RpcClientHolder.get("127.0.0.1", srv.port),
                "prey：holder 把同一个已关闭的实例再交出去");

        Result poisoned = RpcClientHolder.invoke(probeInvocation(TraceProbe.class.getName()), url);
        assertTrue(poisoned.hasException(),
                "服务端还在监听，但 holder 只会把断掉的连接再递出来: " + poisoned.getValue());

        // 结构面：holder 只有 get/invoke 两个非合成方法，没有任何失效入口
        Set<String> holderMethods = new LinkedHashSet<String>();
        for (Method m : RpcClientHolder.class.getDeclaredMethods()) {
            if (!m.isSynthetic()) {
                holderMethods.add(m.getName());
            }
        }
        assertEquals(new LinkedHashSet<String>(Arrays.asList("get", "invoke")), holderMethods);
        // 被缓存的对象自己是有出口能力的，缺的只是 holder 侧那条路
        Set<String> clientMethods = new LinkedHashSet<String>();
        for (Method m : RpcClient.class.getDeclaredMethods()) {
            clientMethods.add(m.getName());
        }
        assertTrue(clientMethods.contains("close"), clientMethods.toString());
        assertTrue(clientMethods.contains("setTimeout"), clientMethods.toString());
    }

    @Test
    @DisplayName("bug_ holder 缓存以 host:port 为键，改 timeout 会传染给别的调用方")
    void bug_holderSharesOneClientPerAddress() throws Exception {
        CaptureServer srv = startCaptureServer();
        RpcClient a = RpcClientHolder.get("127.0.0.1", srv.port);
        RpcClient b = RpcClientHolder.get("127.0.0.1", srv.port);
        assertSame(a, b);
        a.setTimeout(1L);
        // 猎物：b 从没调过 setTimeout，它自己字段上的值却已经变了
        Field t = RpcClient.class.getDeclaredField("timeout");
        t.setAccessible(true);
        assertEquals(1L, ((Long) t.get(b)).longValue(), "同一个连接对象，配置被别的调用方改过了");

        Field f = RpcClientHolder.class.getDeclaredField("CLIENTS");
        f.setAccessible(true);
        assertEquals(ConcurrentHashMap.class, f.get(null).getClass());
        assertTrue(((Map<?, ?>) f.get(null)).containsKey("127.0.0.1:" + srv.port),
                "prey：键里没有任何调用方身份");
    }

    // ==================== 辅助 ====================

    /** Invoker 有四个方法要实现，被测的只是 invoke 那一个。 */
    private interface Body {
        Result run(Invocation invocation) throws Throwable;
    }

    private static Invoker<Object> providerInvoker(final Class<?> iface, final Body body) {
        return new Invoker<Object>() {
            @Override
            @SuppressWarnings("unchecked")
            public Class<Object> getInterface() {
                return (Class<Object>) iface;
            }

            @Override
            public Result invoke(Invocation invocation) throws Throwable {
                return body.run(invocation);
            }

            @Override
            public URL getUrl() {
                return urlFor(0, iface.getName(), null, null);
            }

            @Override
            public boolean isAvailable() {
                return true;
            }

            @Override
            public void destroy() {
            }
        };
    }
}
