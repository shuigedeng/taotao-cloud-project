package com.taotao.cloud.rpc.remoting;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.invoke.RpcInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link RpcClient} 这条生命周期链路：连不上怎么办、关掉之后怎么办、
 * 调用失败时以什么语言报告（异常还是 error Result）。
 * <p>
 * 连接失败与关闭都要看线程：Netty 的 EventLoop 线程是非守护的，构造期留下的组
 * 若没人关，会长驻在这个 JVM 里。
 */
class RpcClientLifecycleTest {

    interface Greeter {
        String hello();
    }

    static final class GreeterImpl implements Greeter {
        @Override
        public String hello() {
            return "hi";
        }
    }

    /**
     * 一个"必然没人听着"的地址：低端口以普通用户 bind 会被拒（本机实测 errno 13），
     * 所以不用先探一个空闲端口再关掉 —— 探针关闭到真正使用之间端口可能被人抢走，
     * 这条 race 在本套用例里实测红过一次。前提由用例自己断言，见 {@code failedConnectThrows}。
     */
    private static final int DEAD_PORT = 1;

    /** 绑一个真在听的口并交还句柄；端口交给内核挑，读回真实端口，不猜。 */
    private static ServerSocket listeningSocket() throws IOException {
        return new ServerSocket(0, 50, java.net.InetAddress.getByName("127.0.0.1"));
    }

    /** Netty 线程名 {@code nioEventLoopGroup-<poolId>-<serial>}；poolId 全局递增，用它认"这次新建的组"。 */
    private static Set<Integer> liveEventLoopPoolIds() {
        Set<Integer> ids = new HashSet<>();
        String prefix = "nioEventLoopGroup-";
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            String n = t.getName();
            if (!n.startsWith(prefix)) {
                continue;
            }
            int dash = n.indexOf('-', prefix.length());
            if (dash < 0) {
                continue;
            }
            try {
                ids.add(Integer.valueOf(n.substring(prefix.length(), dash)));
            } catch (NumberFormatException ignore) {
                // 名字不合式的线程不参与计数
            }
        }
        return ids;
    }

    private static int watermark() {
        int max = -1;
        for (Integer id : liveEventLoopPoolIds()) {
            max = Math.max(max, id);
        }
        return max;
    }

    private static void awaitPoolIdsAtMost(int limit, long timeoutMs) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline && watermark() > limit) {
            Thread.sleep(20);
        }
    }

    // ---------- 连不上 ----------

    @Test
    @DisplayName("构造即连接：连不上抛 RuntimeException，消息带着 host:port")
    void failedConnectThrows() throws Exception {
        // 先自证量具前提：这个端口谁都绑不上，于是"连不上"不是运气
        assertThrows(IOException.class,
                () -> new ServerSocket(DEAD_PORT, 50, java.net.InetAddress.getByName("127.0.0.1")),
                "前提不成立：127.0.0.1:" + DEAD_PORT + " 竟然能绑上，这条用例的失败连接不作数");

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> new RpcClient("127.0.0.1", DEAD_PORT));
        assertTrue(e.getMessage().contains("Failed to connect to server"), "实际: " + e.getMessage());
        assertTrue(e.getMessage().contains("127.0.0.1:" + DEAD_PORT), "实际: " + e.getMessage());

        // 猎物：换一个真在听的端口，同一个构造调用就成功了。绑 0，端口由内核挑、再读回来
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        try {
            assertTrue(server.getPort() > 0,
                    "绑 0 之后 getPort() 必须交回真实端口，实收 " + server.getPort());
            RpcClient ok = new RpcClient("127.0.0.1", server.getPort());
            assertTrue(ok.isConnected());
            ok.close();
        } finally {
            server.stop();
        }
    }

    @Test
    @DisplayName("连接失败不许把 EventLoopGroup 留在场上")
    void failedConnectReleasesItsEventLoopGroup() throws Exception {
        int before = watermark();
        assertThrows(RuntimeException.class, () -> new RpcClient("127.0.0.1", DEAD_PORT));

        // shutdownGracefully 有 2s quiet period，轮询而不是硬等
        awaitPoolIdsAtMost(before, 8000);
        assertTrue(watermark() <= before,
                "poolId > " + before + " 的组是这次失败连接新建的，它的线程还在跑");
    }

    @Test
    @DisplayName("真建过连接的组关掉之后线程会退场（上一条的对照）")
    void closedClientReleasesItsEventLoopGroup() throws Exception {
        // 只借一个能完成 TCP 握手的监听口：不引入 RpcServer 的 EventLoopGroup，
        // 否则"poolId 比水位大"就不只属于被测 client 那一组了。
        try (ServerSocket listener = listeningSocket()) {
            int port = listener.getLocalPort();
            int before = watermark();
            RpcClient client = new RpcClient("127.0.0.1", port);
            assertTrue(client.isConnected(), "前提：连接真的建起来了");
            assertTrue(watermark() > before,
                    "量具自检：真建过连接的 client 必须留下一组活线程（poolId > " + before + "）");

            client.close();
            awaitPoolIdsAtMost(before, 8000);
            assertTrue(watermark() <= before,
                    "close() 之后仍有 poolId > " + before + " 的组活着，实际水位 " + watermark());
        }
    }

    // ---------- 关掉之后 ----------

    @Test
    @DisplayName("close() 之后再发请求：立刻 IllegalStateException，而不是等超时")
    void sendAfterCloseFailsFast() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        RpcClient client = new RpcClient("127.0.0.1", server.getPort());
        try {
            RpcRequest req = new RpcRequest();
            req.setRequestId("req-1");
            req.setInterfaceName(Greeter.class.getName());
            req.setMethodName("hello");
            req.setParameterTypes(new Class<?>[0]);
            req.setArguments(new Object[0]);

            RpcResponse resp = client.sendRequest(req);
            assertEquals("hi", resp.getResult());
            assertFalse(resp.hasException(), "实际: " + resp.getErrorMessage());
            assertTrue(client.isConnected());

            client.close();
            assertFalse(client.isConnected());
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> client.sendRequest(req));
            assertEquals("Not connected to server", e.getMessage());
            client.close();
        } finally {
            server.stop();
        }
    }

    // ---------- 失败报告的语言 ----------

    private static URL url(String service, int port) {
        URL u = new URL("ttc-rpc", "127.0.0.1", port);
        u.setServiceInterface(service);
        return u;
    }

    @Test
    @DisplayName("invoke() 成功时把值放进 Result，失败时放进 error Result —— 从不抛")
    void invokeSpeaksInResultsNotExceptions() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        int port = server.getPort();
        RpcClient client = new RpcClient("127.0.0.1", port);
        assertTrue(port > 0, "端口没读回来，后面的 URL 会指向 0: " + port);
        try {
            Result good = client.invoke(new RpcInvocation(Greeter.class.getName(), "hello",
                    new Class<?>[0], new Object[0]), url(Greeter.class.getName(), port));
            assertFalse(good.hasException(), "实际: " + good.getException());
            assertEquals("hi", good.getValue());

            // 服务端回了异常响应：invoke() 把它包成 error Result，异常不往外抛
            Result bad = client.invoke(new RpcInvocation("com.zifang.nope.Nope", "hello",
                    new Class<?>[0], new Object[0]), url("com.zifang.nope.Nope", port));
            assertTrue(bad.hasException(), "实际值: " + bad.getValue());
            assertNotNull(bad.getException());
            assertTrue(bad.getException().getMessage().contains("Service not found"),
                    "实际: " + bad.getException().getMessage());

            // 客户端自己出事（已关闭）同样只是 error Result —— FailoverCluster 只对抛出来的失败重试
            client.close();
            Result dead = client.invoke(new RpcInvocation(Greeter.class.getName(), "hello",
                    new Class<?>[0], new Object[0]), url(Greeter.class.getName(), port));
            assertTrue(dead.hasException(), "实际值: " + dead.getValue());
        } finally {
            server.stop();
        }
    }
}
