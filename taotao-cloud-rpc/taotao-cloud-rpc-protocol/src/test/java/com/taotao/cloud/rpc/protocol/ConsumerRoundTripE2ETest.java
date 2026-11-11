package com.taotao.cloud.rpc.protocol;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.invoke.RpcInvocation;
import com.taotao.cloud.rpc.remoting.RpcServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 消费链路端到端：真实的 socket、真实的 {@link RpcServer}、真实的
 * {@link TtcRpcProtocolImpl.TtcRpcInvoker#invoke(Invocation)}。
 * <p>
 * 这批用例存在的意义就是 C1：修复之前 invoke() 不发任何网络请求、
 * 直接返回 "ttc-RPC invocation result for ..." 字符串，而这条断言
 * （"返回值必须是 Provider 算出来的"）在假实现下必红。
 */
class ConsumerRoundTripE2ETest {

    public interface Hello {
        String echo(String input);
    }

    public static class HelloImpl implements Hello {
        public String echo(String input) {
            return "echo:" + input;
        }
    }

    public static class HelloV1 implements Hello {
        public String echo(String input) {
            return "v1:" + input;
        }
    }

    private RpcServer server;

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    private static int freePort() throws IOException {
        ServerSocket socket = new ServerSocket(0);
        try {
            return socket.getLocalPort();
        } finally {
            socket.close();
        }
    }

    /** 起一个真实监听的服务端：绑 0 由内核分配，端口从 {@code RpcServer.getPort()} 读回，
     *  这样就没有"探测取号 → 真正 bind"之间的抢端口窗口，也就不需要重试。 */
    private int startServer(RegistrationConfigurer cfg) throws Exception {
        RpcServer s = new RpcServer("127.0.0.1", 0);
        cfg.apply(s);
        s.start(true);
        // 等监听真正就绪：第一次连接失败不代表缺陷，别让它变成抖动源
        for (int i = 0; i < 50 && !s.isStarted(); i++) {
            Thread.sleep(10);
        }
        server = s;
        return s.getPort();
    }

    /** 只为了把"注册方式"延迟到拿到端口之后。 */
    interface RegistrationConfigurer {
        void apply(RpcServer server);
    }

    private static URL providerUrl(String serviceInterface, int port, String version) {
        URL url = new URL("ttc-rpc", "127.0.0.1", port, serviceInterface);
        url.setVersion(version);
        return url;
    }

    private static Invocation invocation(String serviceInterface, String arg) {
        return new RpcInvocation(serviceInterface, "echo",
                new Class<?>[]{String.class}, new Object[]{arg});
    }

    @Test
    @DisplayName("E2E：默认协议的 invoke() 真的跨 socket 拿到 Provider 算出的值")
    void invokerPerformsRealRoundTrip() throws Exception {
        final int port = startServer(new RegistrationConfigurer() {
            @Override
            public void apply(RpcServer s) {
                s.registerService(Hello.class, new HelloImpl());
            }
        });
        assertTrue(server.isStarted(), "服务端没起来，后面的断言全是空跑");

        TtcRpcProtocolImpl protocol = new TtcRpcProtocolImpl();
        TtcRpcProtocolImpl.TtcRpcInvoker<Hello> invoker =
                (TtcRpcProtocolImpl.TtcRpcInvoker<Hello>) protocol.refer(
                        Hello.class, providerUrl(Hello.class.getName(), port, null), null);

        Result r = invoker.invoke(invocation(Hello.class.getName(), "world"));
        assertFalse(r.hasException(), "实际异常: " + r.getException());
        assertEquals("echo:world", r.getValue(),
                "返回值必须由 Provider 计算得出");
        // 直接钉住被修掉的那个假实现
        assertFalse(String.valueOf(r.getValue()).startsWith("ttc-RPC invocation result for"),
                "又返回占位字符串了: " + r.getValue());

        invoker.destroy();
    }

    @Test
    @DisplayName("E2E：同一 Invoker 复用连接，第二次调用照样通")
    void connectionIsReusedAcrossCalls() throws Exception {
        final int port = startServer(new RegistrationConfigurer() {
            @Override
            public void apply(RpcServer s) {
                s.registerService(Hello.class, new HelloImpl());
            }
        });
        TtcRpcProtocolImpl.TtcRpcInvoker<Hello> invoker =
                (TtcRpcProtocolImpl.TtcRpcInvoker<Hello>) new TtcRpcProtocolImpl().refer(
                        Hello.class, providerUrl(Hello.class.getName(), port, null), null);

        assertFalse(invoker.isAvailable(), "prey：首次调用之前没有连接，isAvailable 必须是 false"
                + "（修复前它无条件返回 true）");
        assertEquals("echo:a", invoker.invoke(invocation(Hello.class.getName(), "a")).getValue());
        assertTrue(invoker.isAvailable(), "连接建立后必须报告可用");
        assertEquals("echo:b", invoker.invoke(invocation(Hello.class.getName(), "b")).getValue());
        invoker.destroy();
        assertFalse(invoker.isAvailable(), "destroy 之后必须转为不可用");
    }

    @Test
    @DisplayName("E2E：Provider 抛业务异常时，异常原样回到消费端而不是被吞成成功")
    void providerExceptionReachesConsumer() throws Exception {
        final int port = startServer(new RegistrationConfigurer() {
            @Override
            public void apply(RpcServer s) {
                s.registerService(Hello.class, new Hello() {
                    public String echo(String input) {
                        throw new IllegalStateException("boom-" + input);
                    }
                });
            }
        });
        TtcRpcProtocolImpl.TtcRpcInvoker<Hello> invoker =
                (TtcRpcProtocolImpl.TtcRpcInvoker<Hello>) new TtcRpcProtocolImpl().refer(
                        Hello.class, providerUrl(Hello.class.getName(), port, null), null);

        Result r = invoker.invoke(invocation(Hello.class.getName(), "x"));
        assertTrue(r.hasException(), "业务异常必须回传，实际 value=" + r.getValue());
        assertNotNull(r.getException());
        assertEquals("boom-x", r.getException().getMessage());
        invoker.destroy();
    }

    @Test
    @DisplayName("E2E：地址没人监听时返回 error result，不把异常抛穿 refer/invoke")
    void deadAddressYieldsErrorResult() throws Exception {
        int dead = freePort();
        TtcRpcProtocolImpl.TtcRpcInvoker<Hello> invoker =
                (TtcRpcProtocolImpl.TtcRpcInvoker<Hello>) new TtcRpcProtocolImpl().refer(
                        Hello.class, providerUrl(Hello.class.getName(), dead, null), null);

        Result r = invoker.invoke(invocation(Hello.class.getName(), "hi"));
        assertTrue(r.hasException(), "连不上却返回成功结果");
        assertFalse(invoker.isAvailable());
        invoker.destroy();
    }

    @Test
    @DisplayName("E2E：带版本注册的 Provider 要 URL.version 对得上才命中")
    void versionedProviderNeedsMatchingVersion() throws Exception {
        final int port = startServer(new RegistrationConfigurer() {
            @Override
            public void apply(RpcServer s) {
                s.register(Hello.class, new HelloV1(), "1.0.0");
            }
        });
        TtcRpcProtocolImpl protocol = new TtcRpcProtocolImpl();

        TtcRpcProtocolImpl.TtcRpcInvoker<Hello> matched =
                (TtcRpcProtocolImpl.TtcRpcInvoker<Hello>) protocol.refer(
                        Hello.class, providerUrl(Hello.class.getName(), port, "1.0.0"), null);
        Result ok = matched.invoke(invocation(Hello.class.getName(), "v"));
        assertFalse(ok.hasException(), "带版本注册 + 带版本请求应当命中，实际: " + ok.getException());
        assertEquals("v1:v", ok.getValue());
        matched.destroy();

        TtcRpcProtocolImpl.TtcRpcInvoker<Hello> mismatched =
                (TtcRpcProtocolImpl.TtcRpcInvoker<Hello>) protocol.refer(
                        Hello.class, providerUrl(Hello.class.getName(), port, "9.9.9"), null);
        Result miss = mismatched.invoke(invocation(Hello.class.getName(), "v"));
        assertTrue(miss.hasException(), "版本不该匹配却成功了: " + miss.getValue());
        mismatched.destroy();
    }

    @Test
    @DisplayName("URL.version 为 null 时附件带的是 null，服务端因此落到裸键")
    void nullVersionAttachmentLandsOnBareKey() throws Exception {
        final int port = startServer(new RegistrationConfigurer() {
            @Override
            public void apply(RpcServer s) {
                s.registerService(Hello.class, new HelloImpl());
            }
        });
        TtcRpcProtocolImpl.TtcRpcInvoker<Hello> invoker =
                (TtcRpcProtocolImpl.TtcRpcInvoker<Hello>) new TtcRpcProtocolImpl().refer(
                        Hello.class, providerUrl(Hello.class.getName(), port, null), null);
        Result r = invoker.invoke(invocation(Hello.class.getName(), "n"));
        assertFalse(r.hasException(), "实际: " + r.getException());
        // 这条同时是"HashMap.getOrDefault 对存在但为 null 的键返回 null"的取证：
        // 若它返回 "1.0.0"，命中的将是 name:1.0.0 而不是裸键，本用例照样绿，
        // 所以另附一条直接量具。
        assertEquals("echo:n", r.getValue());
        invoker.destroy();

        java.util.HashMap<String, String> m = new java.util.HashMap<String, String>();
        m.put("version", null);
        assertEquals(null, m.getOrDefault("version", "1.0.0"),
                "HashMap 覆写了 Map.getOrDefault：键存在但值为 null 时返回 null 而非默认值");
    }

    @Test
    @DisplayName("export() 仍然不绑定任何端口（C1 的另一半没修）")
    void exportStillBindsNothing() throws Exception {
        final int port = startServer(new RegistrationConfigurer() {
            @Override
            public void apply(RpcServer s) {
            }
        });
        URL url = providerUrl(Hello.class.getName(), port, null);
        TtcRpcProtocolImpl.TtcRpcExporter<Hello> exporter =
                (TtcRpcProtocolImpl.TtcRpcExporter<Hello>) new TtcRpcProtocolImpl().export(
                        new TtcRpcProtocolImpl.TtcRpcInvoker<Hello>(Hello.class, url, url));

        assertFalse(exporter.isUnexported());
        assertSame(exporter.getInvoker().getUrl(), url);
        // 关键：export() 之后没有任何东西监听新端口 —— 它只是包了一层对象。
        exporter.unexport();
        assertTrue(exporter.isUnexported(), "prey：unexport 只翻标志位");
    }
}
