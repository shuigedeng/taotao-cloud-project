package com.taotao.cloud.rpc.core.context;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.context.CrossProcessProvider;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.invoke.RpcInvocation;
import com.taotao.cloud.rpc.remoting.RpcClientHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 真正的跨进程端到端：另一个 JVM（{@link CrossProcessProvider}，用 ProcessBuilder 拉起）里跑生产
 * {@code RpcServer}，本进程用生产 {@code RpcClient} 打过去。此前所有 E2E 都是同 JVM 双端，
 * 这一批补的是"隔了一个进程边界之后还剩什么"。
 */
class CrossProcessE2ETest {

    private static final String SERVICE = "cross.demo.DemoService";

    private Process child;
    private String childPid;
    private int childPort;
    private final BlockingQueue<String> childLines = new LinkedBlockingQueue<String>();
    private final List<String> childNoise = new ArrayList<String>();

    private static String ownPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        int at = name.indexOf('@');
        return at > 0 ? name.substring(0, at) : name;
    }

    /**
     * 起一个子 JVM 直到它打出 READY；不成就把子进程吐出的全部内容写进异常消息。
     * 交给子进程的端口一律是 0 —— 真实端口由内核分配，从 READY 行里读回来。
     * （先前这里是"父进程探针取号、子进程去 bind"，两者之间有抢端口窗口，只能靠三次重试兜。）
     */
    private void startChild() throws Exception {
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        ProcessBuilder pb = new ProcessBuilder(javaBin, "-cp", System.getProperty("java.class.path"),
                CrossProcessProvider.class.getName(), "0", SERVICE);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        drain(p.getInputStream());
        String ready = awaitReady(p, 20);
        if (ready == null) {
            String why = p.isAlive() ? "还在跑却没打出 READY" : "已退出，exit=" + p.exitValue();
            p.destroy();
            throw new IllegalStateException("子 JVM 没就绪（" + why + "），它吐出的全部内容:\n" + dumpChildOutput());
        }
        child = p;
    }

    /** 只在子进程还活着的时候等；从 READY 行取回真实端口与 PID。 */
    private String awaitReady(Process p, int seconds) throws InterruptedException {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds);
        while (System.nanoTime() < deadline && p.isAlive()) {
            String line = childLines.poll(500, TimeUnit.MILLISECONDS);
            if (line == null) {
                continue;
            }
            if (line.startsWith("READY ")) {
                String[] parts = line.split(" ");
                assertEquals(3, parts.length, "READY 行形状不对: " + line);
                int reported = Integer.parseInt(parts[1]);
                assertTrue(reported > 0 && reported < 65536, "READY 报的不是一个已分配的端口: " + line);
                childPort = reported;
                childPid = parts[2];
                assertNotEquals(ownPid(), childPid, "子进程 PID 与本进程相同 —— 那就不算跨进程");
                return line;
            }
            childNoise.add(line);
            if (line.contains("BIND-FAILED")) {
                return null;
            }
        }
        return null;
    }

    private String dumpChildOutput() {
        StringBuilder sb = new StringBuilder();
        for (String line : childNoise) {
            sb.append(line).append('\n');
        }
        String line;
        while ((line = childLines.poll()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }


    private void drain(final InputStream in) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try (BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
                    String line;
                    while ((line = r.readLine()) != null) {
                        childLines.add(line);
                    }
                } catch (IOException ignored) {
                    // 进程被 destroy 之后流关闭是正常收尾
                }
            }
        }, "cross-process-stdout-drain");
        t.setDaemon(true);
        t.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (child != null) {
            RpcClientHolder.get("127.0.0.1", childPort).close();
            child.destroy();
            child.waitFor(10, TimeUnit.SECONDS);
            if (child.isAlive()) {
                child.destroyForcibly().waitFor(5, TimeUnit.SECONDS);
            }
        }
    }

    private static Invocation call(String method, Class<?>[] types, Object[] args) {
        return new RpcInvocation(SERVICE, method, types, args);
    }

    private Result call(String method, Class<?>[] types, Object[] args, String version) {
        URL url = new URL("ttc-rpc", "127.0.0.1", childPort, SERVICE);
        url.setVersion(version);
        return RpcClientHolder.invoke(call(method, types, args), url);
    }

    private static String field(String line, String key) {
        for (String part : line.split(";")) {
            if (part.startsWith(key + "=")) {
                return part.substring(key.length() + 1);
            }
        }
        return null;
    }

    @Test
    @DisplayName("E2E：跨两个 JVM 的往返，取回的是子进程算出来的值")
    void valueComesFromAnotherJvm() throws Exception {
        startChild();
        Result r = call("echo", new Class<?>[]{String.class}, new Object[]{"hi"}, null);
        assertFalse(r.hasException(), "实际异常: " + r.getException());
        assertEquals("child:hi", r.getValue(),
                "值必须由子进程拼出来 —— 本进程里没有任何代码会产出 child: 前缀");
    }

    @Test
    @DisplayName("E2E：响应里的 PID 与子进程自报的 PID 一致，业务代码却看不见 trace 上下文")
    void responseCarriesTheChildPidButNoTraceContext() throws Exception {
        startChild();
        Result r = call("where", new Class<?>[0], new Object[0], null);
        assertFalse(r.hasException(), "实际异常: " + r.getException());
        String where = String.valueOf(r.getValue());

        // 两条独立通道对账：PID 一路走 socket，一路走子进程的 stdout
        assertEquals(childPid, field(where, "pid"), "实测 " + where);
        assertNotEquals(ownPid(), field(where, "pid"), "实测 " + where);
        assertNotNull(field(where, "thread"));
        assertTrue(field(where, "thread").contains("nioEventLoopGroup"),
                "子进程里也应是 Netty IO 线程直接跑业务，实测 " + where);
        // 跨进程版本的同一件事：请求到了、也答回来了，Provider 侧的 MDC 却还是空的
        // （探针方法里是字符串拼接，Java 的 null 会打成 "null"）
        assertEquals("null", field(where, "mdc"), "实测 " + where);
        // 猎物：attachment 确实过了线 —— 否则子进程连这个服务都找不到
        assertEquals("child", field(where, "prefix"), "实测 " + where);
    }

    @Test
    @DisplayName("E2E：version attachment 跨过进程边界并决定子进程命中哪条注册")
    void versionAttachmentDrivesRoutingInTheOtherJvm() throws Exception {
        startChild();
        Result hit = call("echo", new Class<?>[]{String.class}, new Object[]{"v"}, "7.7.7");
        assertFalse(hit.hasException(), "实际异常: " + hit.getException());
        assertEquals("versioned-child:v", hit.getValue());

        Result bare = call("echo", new Class<?>[]{String.class}, new Object[]{"v"}, null);
        assertEquals("child:v", bare.getValue(), "prey：不带版本时命中的是另一条注册");

        Result miss = call("echo", new Class<?>[]{String.class}, new Object[]{"v"}, "8.8.8");
        assertTrue(miss.hasException(), "版本不匹配却调用成功: " + miss.getValue());
        assertTrue(String.valueOf(miss.getException().getMessage()).contains("Service not found"),
                String.valueOf(miss.getException()));
    }

    @Test
    @DisplayName("E2E：业务异常的类型与消息跨过 JVM 边界原样还原")
    void businessExceptionTypeSurvivesTheJvmBoundary() throws Exception {
        startChild();
        Result r = call("boom", new Class<?>[0], new Object[0], null);
        assertTrue(r.hasException(), "子进程抛了异常，本进程却拿到成功: " + r.getValue());
        Throwable t = r.getException();
        assertNotNull(t);
        assertEquals(CrossProcessProvider.ProbeFailure.class, t.getClass(),
                "实测类型 " + t.getClass().getName());
        assertEquals("cross-jvm-8899", t.getMessage());
        // 猎物：它没被降级成裸 RuntimeException
        assertNotEquals(RuntimeException.class, t.getClass());
    }

    @Test
    @DisplayName("E2E：同一连接的第二次调用落在子进程的同一个 IO 线程上")
    void connectionIsReusedAcrossTheProcessBoundary() throws Exception {
        startChild();
        String first = String.valueOf(call("where", new Class<?>[0], new Object[0], "7.7.7").getValue());
        String second = String.valueOf(call("where", new Class<?>[0], new Object[0], "7.7.7").getValue());
        // 猎物：两次都确实由子进程算出来，而且命中的是带版本那条注册
        assertEquals("versioned-child", field(first, "prefix"), first);
        assertEquals("versioned-child", field(second, "prefix"), second);
        String a = field(first, "thread");
        assertNotNull(a, first);
        assertEquals(a, field(second, "thread"),
                "客户端只有一条 channel，子进程侧应当固定在同一个 worker");
    }
}
