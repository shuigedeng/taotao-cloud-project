package com.taotao.cloud.rpc.core.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FailoverCluster}：重试次数怎么来、失败时到底重试几次、选中哪个 invoker。
 * <p>
 * invoker 全部用本地替身，不碰 socket —— 要量的是集群层的决策，不是网络。
 */
class FailoverClusterTest {

    static final class Call {
        final String invoker;
        Call(String invoker) {
            this.invoker = invoker;
        }
    }

    /** 可编排结果的替身：按脚本返回 Result 或抛异常，并记录每次被调用。 */
    static class Scripted implements Invoker<String> {
        final String id;
        final List<Call> trace;
        int hits;
        boolean throwOnInvoke = true;
        boolean available = true;

        Scripted(String id, List<Call> trace) {
            this.id = id;
            this.trace = trace;
        }

        @Override
        public Class<String> getInterface() {
            return String.class;
        }

        @Override
        public Result invoke(Invocation invocation) throws Throwable {
            hits++;
            trace.add(new Call(id));
            if (throwOnInvoke) {
                throw new IllegalStateException("fail-" + id + "-" + hits);
            }
            return Result.success(id);
        }

        @Override
        public URL getUrl() {
            return new URL("ttc-rpc", "127.0.0.1", 20880);
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        @Override
        public void destroy() {
            available = false;
        }
    }

    /** 返回 error Result 而不抛异常的 invoker：框架自己就有两个这样的（RpcClient、DubboInvoker）。 */
    static final class ErrorResult extends Scripted {
        ErrorResult(String id, List<Call> trace) {
            super(id, trace);
            throwOnInvoke = false;
        }

        @Override
        public Result invoke(Invocation invocation) {
            hits++;
            trace.add(new Call(id));
            return Result.error(new IllegalStateException("soft-" + id));
        }
    }

    static class ListDirectory implements Directory<String> {
        final URL url;
        volatile boolean destroyed;
        List<Invoker<String>> invokers = new ArrayList<>();
        int listCalls;

        ListDirectory(String retriesParam) {
            url = new URL("consumer", "127.0.0.1", 0);
            url.setServiceInterface("com.shuigedeng.demo.Demo");
            if (retriesParam != null) {
                url.addParameter("retries", retriesParam);
            }
        }

        @Override
        public Class<String> getInterface() {
            return String.class;
        }

        @Override
        public List<Invoker<String>> list() {
            listCalls++;
            return invokers;
        }

        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public boolean isDestroyed() {
            return destroyed;
        }

        @Override
        public void destroy() {
            destroyed = true;
            for (Invoker<String> i : invokers) {
                i.destroy();
            }
        }
    }

    private static List<Invoker<String>> of(List<Call> trace, String... ids) {
        List<Invoker<String>> out = new ArrayList<>();
        for (String id : ids) {
            out.add(new Scripted(id, trace));
        }
        return out;
    }

    private static Invocation call() {
        return new com.taotao.cloud.rpc.common.invoke.RpcInvocation();
    }

    private static String order(List<Call> trace) {
        StringBuilder sb = new StringBuilder();
        for (Call c : trace) {
            if (sb.length() > 0) {
                sb.append("->");
            }
            sb.append(c.invoker);
        }
        return sb.toString();
    }

    @Test
    @DisplayName("join() 出来的集群 invoker 把接口与 URL 代理给目录")
    void clusterInvokerDelegatesIdentity() {
        ListDirectory dir = new ListDirectory(null);
        Invoker<String> cluster = new FailoverCluster().join(dir);
        assertSame(String.class, cluster.getInterface());
        assertSame(dir.getUrl(), cluster.getUrl());
        assertEquals("failover", new FailoverCluster().getName());
    }

    @Test
    @DisplayName("isAvailable 只看目录有没有条目、有没有销毁")
    void availabilityFollowsDirectory() throws Throwable {
        ListDirectory dir = new ListDirectory(null);
        Invoker<String> cluster = new FailoverCluster().join(dir);
        assertFalse(cluster.isAvailable());

        Scripted a = new Scripted("a", new CopyOnWriteArrayList<>());
        dir.invokers = Collections.singletonList(a);
        assertTrue(cluster.isAvailable());

        cluster.destroy();
        assertTrue(dir.destroyed, "cluster.destroy() 必须把目录一起销毁");
        assertFalse(cluster.isAvailable());
    }

    @Test
    @DisplayName("bug_isAvailableIgnoresWhetherTheInvokersThemselvesWork：全挂也『可用』")
    void bug_isAvailableIgnoresInvokerHealth() {
        ListDirectory dir = new ListDirectory(null);
        Scripted a = new Scripted("a", new CopyOnWriteArrayList<>());
        a.available = false;
        dir.invokers = Collections.singletonList(a);

        Invoker<String> cluster = new FailoverCluster().join(dir);
        assertFalse(a.isAvailable(), "前提：唯一的 invoker 自己已经不可用");
        assertTrue(cluster.isAvailable(),
                "FailoverCluster.isAvailable() 只判 list().isEmpty()，从不看 invoker 的 isAvailable()");

        // 猎物：换一个把健康算进去的实现，同一个断言立刻为假
        Directory<String> healthAware = new ListDirectory(null) {
            @Override
            public List<Invoker<String>> list() {
                List<Invoker<String>> out = new ArrayList<>();
                for (Invoker<String> i : invokers) {
                    if (i.isAvailable()) {
                        out.add(i);
                    }
                }
                return out;
            }
        };
        assertFalse(new FailoverCluster().join(healthAware).isAvailable());
    }

//    @Test
//    @DisplayName("无 provider 时抛 IllegalStateException，不是框架统一的 RpcException")
//    void bug_noProviderThrowsPlainIllegalState() {
//        ListDirectory dir = new ListDirectory(null);
//        Invoker<String> cluster = new FailoverCluster().join(dir);
//
//        Throwable e = assertThrows(Throwable.class, () -> cluster.invoke(call()));
//        assertEquals(IllegalStateException.class, e.getClass(), "实际: " + e.getClass().getName());
//        assertTrue(e.getMessage().contains("No provider available"), "实际: " + e.getMessage());
//        assertTrue(e.getMessage().contains("com.shuigedeng.demo.Demo"), "实际: " + e.getMessage());
//        assertFalse(e instanceof com.taotao.cloud.rpc.common.RpcException,
//                "消费方按 RpcException 兜底捕获的话接不到这条");
//
//        // 猎物：框架为这件事准备好了编码与工厂方法，同目录下的兄弟集群实现用的就是它
//        com.taotao.cloud.rpc.common.RpcException rpc = com.taotao.cloud.rpc.common.RpcException.noProvider("same case");
//        assertEquals(5, rpc.getCode());
//        assertThrows(com.taotao.cloud.rpc.common.RpcException.class,
//                () -> new FailfastCluster().join(new ListDirectory(null)).invoke(call()));
//    }

    @Test
    @DisplayName("默认 retries=2 ⇒ 一共 3 次尝试，最后一次的原异常抛出")
    void defaultRetriesMeansThreeAttempts() {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory(null);
        dir.invokers = of(trace, "a", "b");
        Invoker<String> cluster = new FailoverCluster().join(dir);

        Throwable thrown = assertThrows(Throwable.class, () -> cluster.invoke(call()));
        assertTrue(thrown.getMessage().startsWith("fail-"), "实际: " + thrown.getMessage());
        assertEquals(3, trace.size(), "实际调用序列: " + order(trace));
    }

    @Test
    @DisplayName("retries=0 就是只打一次")
    void zeroRetriesIsSingleAttempt() {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory("0");
        dir.invokers = of(trace, "a", "b");
        Invoker<String> cluster = new FailoverCluster().join(dir);

        assertThrows(Throwable.class, () -> cluster.invoke(call()));
        assertEquals(1, trace.size(), "实际调用序列: " + order(trace));
    }

    @Test
    @DisplayName("retries 非法值退回默认 2")
    void unparsableRetriesFallsBackToDefault() {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory("abc");
        dir.invokers = of(trace, "a", "b");
        Invoker<String> cluster = new FailoverCluster().join(dir);

        assertThrows(Throwable.class, () -> cluster.invoke(call()));
        assertEquals(3, trace.size(), "实际调用序列: " + order(trace));
    }

    @Test
    @DisplayName("bug_negativeRetriesNeverInvokesAndBlamesAnUnreachableLine")
    void bug_negativeRetriesReachesUnreachableThrow() {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory("-1");
        dir.invokers = of(trace, "a", "b");
        Invoker<String> cluster = new FailoverCluster().join(dir);

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> cluster.invoke(call()));
        assertEquals("Should never reach here", e.getMessage());
        assertTrue(trace.isEmpty(), "一次都没调用，却报『不该到达』: " + order(trace));
    }

    @Test
    @DisplayName("bug_firstAttemptNeverUsesTheFirstProvider：两个 provider 时第一个只在重试时才轮到")
    void bug_firstAttemptSkipsFirstInvoker() throws Throwable {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory("0");
        Scripted a = new Scripted("a", trace);
        Scripted b = new Scripted("b", trace);
        a.throwOnInvoke = false;
        b.throwOnInvoke = false;
        dir.invokers = new ArrayList<>(Arrays.asList(a, b));

        Invoker<String> cluster = new FailoverCluster().join(dir);
        Result r = cluster.invoke(call());
        assertEquals("b", r.getValue(), "首次调用固定落在 index 1");
        assertEquals(0, a.hits, "列表里的第一个 provider 一次都没被选中过");
    }

    @Test
    @DisplayName("单 provider 时不走那段偏移逻辑：缺陷只在多于一个 provider 时显形")
    void singleProviderShortCircuitsSelection() throws Throwable {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory("0");
        Scripted a = new Scripted("a", trace);
        a.throwOnInvoke = false;
        dir.invokers = Collections.singletonList(a);

        assertEquals("a", new FailoverCluster().join(dir).invoke(call()).getValue());
        assertEquals(1, a.hits);
    }

    @Test
    @DisplayName("三次尝试不会连着打同一个 provider")
    void retriesRotateAcrossProviders() {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory("2");
        dir.invokers = of(trace, "a", "b", "c");
        Invoker<String> cluster = new FailoverCluster().join(dir);

        assertThrows(Throwable.class, () -> cluster.invoke(call()));
        assertEquals(3, trace.size());
        assertEquals(Arrays.asList("b", "c", "a"), Arrays.asList(trace.get(0).invoker,
                trace.get(1).invoker, trace.get(2).invoker), "实际: " + order(trace));
    }

    @Test
    @DisplayName("中途成功即返回，不再重试")
    void successStopsTheRetryLoop() throws Throwable {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory("2");
        Scripted a = new Scripted("a", trace);
        Scripted b = new Scripted("b", trace);
        Scripted c = new Scripted("c", trace);
        c.throwOnInvoke = false;
        dir.invokers = new ArrayList<>(Arrays.asList(a, b, c));

        Result r = new FailoverCluster().join(dir).invoke(call());
        assertEquals("c", r.getValue());
        assertEquals(2, trace.size(), "实际: " + order(trace));
        assertEquals("b", trace.get(0).invoker, "首轮跳过列表第一个: " + order(trace));
        assertEquals(0, a.hits, "已经拿到成功了，列表里剩下的 provider 不该再被调");
    }

    @Test
    @DisplayName("bug_errorResultIsNotRetried：invoker 用 Result 携带异常时，一次都不重试")
    void bug_errorResultIsNotRetried() {
        List<Call> trace = new ArrayList<>();
        ListDirectory dir = new ListDirectory("2");
        dir.invokers = new ArrayList<>(Arrays.asList(new ErrorResult("a", trace),
                new ErrorResult("b", trace)));
        Invoker<String> cluster = new FailoverCluster().join(dir);

        assertThrows(Throwable.class, () -> {
            Result r = cluster.invoke(call());
            // 代理层会在这里把 error Result 翻成异常抛出（JdkProxyFactory:88）
            if (r.hasException()) {
                throw r.getException();
            }
        });
        assertEquals(1, trace.size(),
                "FailoverCluster 只对『抛出来』的失败重试；框架自己的 RpcClient.invoke / DubboInvoker 有两套不同行为: "
                        + order(trace));
    }

    @Test
    @DisplayName("每次重试都重新问目录要列表，provider 换血期间能打新来的")
    void retryReReadsTheDirectory() throws Throwable {
        List<Call> trace = new ArrayList<>();
        Scripted a = new Scripted("a", trace);
        Scripted c = new Scripted("c", trace);
        c.throwOnInvoke = false;

        // 第 2 次 list() 才换成 c：只有"重试轮重新取列表"这条路径能让 c 被调到
        ListDirectory dir = new ListDirectory("1") {
            @Override
            public List<Invoker<String>> list() {
                listCalls++;
                if (listCalls >= 2) {
                    invokers = new ArrayList<>(Collections.singletonList(c));
                }
                return invokers;
            }
        };
        dir.invokers = new ArrayList<>(Collections.singletonList(a));

        Result r = new FailoverCluster().join(dir).invoke(call());
        assertEquals("c", r.getValue(), "实际: " + order(trace));
        assertEquals(2, dir.listCalls, "重试轮应当重新 list()");
        assertEquals(2, a.hits + c.hits);
    }

//    @Test
//    @DisplayName("直连模式的 StaticDirectory 吃的是同一条选择逻辑")
//    void staticDirectorySharesTheSameSelection() {
//        List<Call> trace = new ArrayList<>();
//        ListDirectory probe = new ListDirectory("0");
//        StaticDirectory<String> dir = new StaticDirectory<>(String.class, probe.getUrl(), of(trace, "a", "b"));
//
//        Invoker<String> cluster = new FailoverCluster().join(dir);
//        assertSame(probe.getUrl(), cluster.getUrl(), "StaticDirectory 把消费者 URL 交出来当集群 URL");
//        assertThrows(Throwable.class, () -> cluster.invoke(call()));
//        assertEquals(1, trace.size());
//        assertEquals("b", trace.get(0).invoker,
//                "首次就跳过列表第一个 provider，这个偏置在直连模式同样存在: " + order(trace));
//    }

//    @Test
//    @DisplayName("bug_loadBalanceSettingIsNeverResolved：策略名随便写也不报错")
//    void bug_loadBalanceSettingIsNeverConsulted() {
//        List<Call> trace = new ArrayList<>();
//        ListDirectory dir = new ListDirectory("0");
//        dir.invokers = of(trace, "a", "b");
//        dir.url.addParameter("loadbalance", "this-policy-does-not-exist");
//        Invoker<String> cluster = new FailoverCluster().join(dir);
//
//        assertThrows(Throwable.class, () -> cluster.invoke(call()),
//                "调用本身该失败（provider 全抛），但不该因为策略名失败");
//
//        // 猎物：同一个名字交给真正的扩展点，立刻炸
//        assertThrows(IllegalStateException.class,
//                () -> ExtensionLoader.getExtensionLoader(com.taotao.cloud.rpc.loadbalance.LoadBalance.class)
//                        .getExtension("this-policy-does-not-exist"));
//    }
}
