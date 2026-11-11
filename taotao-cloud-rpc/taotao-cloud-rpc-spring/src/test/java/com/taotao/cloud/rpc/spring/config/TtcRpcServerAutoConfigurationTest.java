package com.taotao.cloud.rpc.spring.config;

import com.taotao.cloud.rpc.remoting.RpcServer;
import com.taotao.cloud.rpc.starter.properties.TtcRpcProperties;
import io.netty.channel.EventLoopGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TtcRpcServerAutoConfiguration} 的装配测试。
 * <p>
 * 一律走真实容器 + 真实 socket：这一族错一个字符，日志里的
 * "RPC Server started" 照样打，但客户端连不进去。
 */
class TtcRpcServerAutoConfigurationTest {

    private TtcRpcServerAutoConfiguration newConfig(TtcRpcProperties properties) throws Exception {
        TtcRpcServerAutoConfiguration cfg = new TtcRpcServerAutoConfiguration();
        Field f = TtcRpcServerAutoConfiguration.class.getDeclaredField("properties");
        f.setAccessible(true);
        f.set(cfg, properties);
        return cfg;
    }

    /** 起一个只装 properties + 自动装配类的容器；refresh() 会触发 ContextRefreshedEvent。 */
    private AnnotationConfigApplicationContext context(TtcRpcProperties properties) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.addBeanFactoryPostProcessor(bf -> bf.registerSingleton("ttcrpcProperties", properties));
        ctx.register(TtcRpcServerAutoConfiguration.class);
        ctx.refresh();
        return ctx;
    }

    @Test
    @DisplayName("host=0.0.0.0（默认值）必须原样绑定，不能被换成本机 hostname 解析出的单地址")
    void wildcardHostIsBoundAsConfigured() throws Exception {
        TtcRpcProperties props = new TtcRpcProperties();
        props.getServer().setHost("0.0.0.0");
        props.getServer().setPort(0);

        RpcServer server = newConfig(props).rpcServer();
        try {
            assertEquals("0.0.0.0", server.getHost(),
                    "配置写的是所有网卡，装配出来的 bind 地址却是别的东西");
        } finally {
            server.stop();
        }
    }

    @Test
    @DisplayName("显式 host 也原样透传，装配层不做二次解析")
    void explicitHostIsPassedThrough() throws Exception {
        TtcRpcProperties props = new TtcRpcProperties();
        props.getServer().setHost("127.0.0.1");
        props.getServer().setPort(0);

        RpcServer server = newConfig(props).rpcServer();
        try {
            assertEquals("127.0.0.1", server.getHost());
        } finally {
            server.stop();
        }
    }

    @Test
    @DisplayName("绑 0.0.0.0 之后，127.0.0.1 上的客户端必须连得进来")
    void wildcardBindAcceptsLoopbackConnections() throws Exception {
        TtcRpcProperties props = new TtcRpcProperties();
        props.getServer().setHost("0.0.0.0");
        props.getServer().setPort(0);

        RpcServer server = newConfig(props).rpcServer();
        try {
            server.start(true);
            assertTrue(server.isStarted(), "start(true) 返回时 bind 应当已经同步完成");
            int port = server.getPort();
            assertTrue(port > 0, "绑定端口应当回读得到，实际: " + port);

            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress("127.0.0.1", port), 3000);
                assertTrue(socket.isConnected(),
                        "服务端声称 started，但 127.0.0.1:" + port + " 连不进去");
            }
        } finally {
            server.stop();
        }
    }

    @Test
    @DisplayName("容器里 RpcServer 是单例：事件监听器启动的就是 getBean 拿到的那一台")
    void serverStartedByTheListenerIsTheSingleton() {
        TtcRpcProperties props = new TtcRpcProperties();
        props.getServer().setHost("0.0.0.0");
        props.getServer().setPort(0);

        AnnotationConfigApplicationContext ctx = context(props);
        try {
            RpcServer bean = ctx.getBean(RpcServer.class);
            assertTrue(bean.isStarted(), "server.enabled 默认真，refresh 之后这台不该是停的");
            int port = bean.getPort();
            assertTrue(port > 0, "真实绑定端口应当回读得到，实际: " + port);
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress("127.0.0.1", port), 3000);
                assertTrue(socket.isConnected());
            } catch (java.io.IOException e) {
                throw new AssertionError("单例已 started 但 127.0.0.1:" + port + " 连不进", e);
            }
        } finally {
            ctx.close();
        }
    }

    @Test
    @DisplayName("server.enabled=false 时监听器不该启动这台 bean")
    void disabledFlagKeepsTheBeanUnstarted() {
        TtcRpcProperties props = new TtcRpcProperties();
        props.getServer().setEnabled(false);
        props.getServer().setHost("0.0.0.0");
        props.getServer().setPort(0);

        AnnotationConfigApplicationContext ctx = context(props);
        try {
            RpcServer bean = ctx.getBean(RpcServer.class);
            assertFalse(bean.isStarted(), "server.enabled=false 却把端口开了");
        } finally {
            ctx.close();
        }
    }

    // ------------------------------------------------ 第 23 轮：启动被中断的那一支 catch

    private static Object readServerField(RpcServer server, String name) throws Exception {
        Field f = RpcServer.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(server);
    }

    /**
     * 收尾：关容器，并把 start() 半途可能留下的两组 EventLoopGroup 收掉（当前字节下 RpcServer 的
     * bind 失败清理已经把它们关掉并置 null，所以这里是空转；留着是因为"哪天那两行置空被改回去"
     * 时，非守护线程会留给同一个 fork 里后面所有用例）。
     */
    private static void reap(AnnotationConfigApplicationContext ctx) {
        RpcServer bean = ctx.getBean(RpcServer.class);
        ctx.close();
        try {
            for (String name : new String[]{"bossGroup", "workerGroup"}) {
                Object group = readServerField(bean, name);
                if (group instanceof EventLoopGroup) {
                    ((EventLoopGroup) group).shutdownGracefully(0, 2, TimeUnit.SECONDS)
                            .awaitUninterruptibly(5, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            throw new AssertionError("收尾掏不到那两组 EventLoopGroup，本轮读数不可信: " + e, e);
        }
    }

    /** 只用来挑一个大概率没人用的端口号，不参与任何判据（这一支从不会真的绑成功）。 */
    private static int probeFreePort() {
        try (ServerSocket probe = new ServerSocket(0)) {
            return probe.getLocalPort();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("拿不到端口号", e);
        }
    }

    /** 让 ContextRefreshedEvent 上的 {@code onContextRefreshed()} 走进 InterruptedException 那一支。 */
    private static final class InterruptedBoot {
        final AnnotationConfigApplicationContext ctx;
        final Throwable refreshFailure;
        final boolean interruptFlagLeftSet;

        InterruptedBoot(AnnotationConfigApplicationContext ctx, Throwable refreshFailure,
                        boolean interruptFlagLeftSet) {
            this.ctx = ctx;
            this.refreshFailure = refreshFailure;
            this.interruptFlagLeftSet = interruptFlagLeftSet;
        }
    }

    /**
     * 引导线程带着中断标志进入 refresh()：Netty 的 {@code bind(...).sync()} 在
     * {@code AbstractFuture.await()} 里看到标志就抛 InterruptedException —— 而 bind 请求
     * 本身已经发出去了。这就是 {@code catch (InterruptedException)} 那一支的入场券。
     */
    private InterruptedBoot interruptedBoot(TtcRpcProperties props) {
        AnnotationConfigApplicationContext ctx = null;
        Throwable failure = null;
        boolean stillSet = false;
        try {
            Thread.currentThread().interrupt();
            ctx = context(props);
            stillSet = Thread.currentThread().isInterrupted();
        } catch (Throwable t) {
            failure = t;
        } finally {
            Thread.interrupted(); // 中断标志绝不带出这条用例
        }
        return new InterruptedBoot(ctx, failure, stillSet);
    }

    private TtcRpcProperties serverOnEphemeralPort() {
        return serverProps("0.0.0.0", 0);
    }

    private TtcRpcProperties serverProps(String host, int port) {
        TtcRpcProperties props = new TtcRpcProperties();
        props.getServer().setHost(host);
        props.getServer().setPort(port);
        return props;
    }

    @Test
    @DisplayName("bug_服务端启动被中断 = 只有一行 error：refresh 成功、bean 是停的，而 getPort() 照旧报得出号码")
    void bug_interruptedServerStartBootsWithNoServerAndOnlyAnErrorLog() {
        int port = probeFreePort();
        InterruptedBoot boot = interruptedBoot(serverProps("0.0.0.0", port));
        try {
            assertNull(boot.refreshFailure,
                    "前提变了：refresh() 现在会被这次中断打断（那这一支就不再是静默失败）-> "
                            + boot.refreshFailure);
            RpcServer bean = boot.ctx.getBean(RpcServer.class);
            assertFalse(bean.isStarted(),
                    "start(true) 被中断打断后 onContextRefreshed 只 log.error ⇒ 服务端一条都没监听，"
                            + "而容器判定自己启动成功");
            assertEquals(port, bean.getPort(),
                    "boundPort 的回填在 bind 之前就被打断了，所以 getPort() 吐回的是配置值 —— "
                            + "这个读数区分不开'起住了'和'根本没起'，唯一有信号的是 isStarted()");
            assertTrue(boot.interruptFlagLeftSet,
                    "catch 里 Thread.currentThread().interrupt() 之后没人清 ⇒ 刷容器那条线程带着中断标志"
                            + "回去继续跑业务，它遇到的下一个阻塞调用会当场抛 InterruptedException");
        } finally {
            if (boot.ctx != null) {
                reap(boot.ctx);
            }
        }
    }

    @Test
    @DisplayName("bug_被中断放弃的那台服务端上不留任何句柄：两组 EventLoopGroup 与 channel 全是 null")
    void bug_interruptedStartLeavesNoHandleOnTheServerItAbandoned() throws Exception {
        InterruptedBoot boot = interruptedBoot(serverOnEphemeralPort());
        assertNotNull(boot.ctx, "上一条已经量过这次中断不打断 refresh；这里为空说明形状又变了");
        RpcServer bean = boot.ctx.getBean(RpcServer.class);
        try {
            assertNull(readServerField(bean, "bossGroup"),
                    "start() 的 bind 失败清理会把两组关掉并置 null —— 本轮「没漏端口」靠的就是这两行");
            assertNull(readServerField(bean, "workerGroup"), "同上");
            assertNull(readServerField(bean, "channel"),
                    "channel 从来没被赋出去：bind 是异步发出的，sync() 因为中断标志直接抛了。"
                            + "于是如果那条 bind 在其后才完成，这台对象上没有任何句柄关得了它");
            bean.stop();
            assertFalse(bean.isStarted(),
                    "stop() 第一句 if (!started) return ⇒ 在这台上它是空操作，公开 API 到此为止");
        } finally {
            reap(boot.ctx);
        }
    }

    @Test
    @DisplayName("正向对照：端口真的被占住时 refresh 是响亮失败的（那一支 catch 只接 InterruptedException）")
    void portConflictFailsTheRefreshLoudly() throws Exception {
        try (ServerSocket hold = new ServerSocket(0, 128, InetAddress.getByName("127.0.0.1"))) {
            TtcRpcProperties props = new TtcRpcProperties();
            props.getServer().setHost("127.0.0.1");
            props.getServer().setPort(hold.getLocalPort());

            AnnotationConfigApplicationContext ctx = null;
            Throwable failure = null;
            try {
                ctx = context(props);
            } catch (Throwable t) {
                failure = t;
            }
            try {
                assertNotNull(failure,
                        "端口被占住时 refresh() 竟然成功了 ⇒ 装配层把它也吞了，那 §17 的对照方向要翻");
                boolean sawBind = false;
                StringBuilder chain = new StringBuilder();
                for (Throwable t = failure; t != null && chain.length() < 4000; t = t.getCause()) {
                    chain.append(t.getClass().getName()).append(" <- ");
                    if (t instanceof java.net.BindException) {
                        sawBind = true;
                    }
                    if (t.getCause() == t) {
                        break;
                    }
                }
                assertTrue(sawBind, "失败原因里应当有 BindException，实际链路: " + chain);
            } finally {
                if (ctx != null) {
                    try {
                        ctx.close();
                    } catch (Throwable ignored) {
                        // refresh 没走完，close 只做能做的清理
                    }
                }
            }
        }
    }
}
