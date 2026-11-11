package com.taotao.cloud.rpc.filter;

import com.taotao.cloud.rpc.async.RpcContext;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.invoke.RpcInvocation;
import com.taotao.cloud.rpc.metrics.MetricsCollector;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 四个内置过滤器的真实行为。
 * <p>
 * 全部走真对象：真的 {@link RpcInvocation}、真的 {@link MetricsCollector} 单例（每个用例前 reset），
 * Invoker 只能是手写的桩——{@code ttc-rpc-filter} 并不依赖 {@code ttc-rpc-remoting}，
 * 这里没有可用的真网络调用者（这本身也是「链路装不起来」的一部分）。
 */
class BuiltInFilterBehaviorTest {

    /** 被测服务接口：指标 key 用的就是它的全名。 */
    interface Greeter {
        String greet(String name);
    }

    private static final String SVC = Greeter.class.getName();
    private static final String KEY = SVC + ":greet";

    /** 记录 invoker 被调用时能看到的东西，并把结果/异常按配置发出去。 */
    private static class StubInvoker implements Invoker<Object> {
        final AtomicReference<Invocation> seen = new AtomicReference<>();
        final AtomicReference<String> mdcTraceIdDuringInvoke = new AtomicReference<>("NOT_CALLED");
        final AtomicReference<String> mdcTenantDuringInvoke = new AtomicReference<>("NOT_CALLED");
        private Result result;
        private Throwable boom;
        private int calls = 0;

        StubInvoker returns(Result r) {
            this.result = r;
            return this;
        }

        StubInvoker throwsIt(Throwable t) {
            this.boom = t;
            return this;
        }

        @Override
        public Class<Object> getInterface() {
            @SuppressWarnings("unchecked")
            Class<Object> c = (Class<Object>) (Class<?>) Greeter.class;
            return c;
        }

        @Override
        public Result invoke(Invocation invocation) throws Throwable {
            calls++;
            seen.set(invocation);
            // 在链路内部这一刻读 MDC，才能证明 ProviderContextFilter 真的设置过
            mdcTraceIdDuringInvoke.set(ThreadContext.get("traceId"));
            mdcTenantDuringInvoke.set(ThreadContext.get("tenant"));
            if (boom != null) {
                throw boom;
            }
            return result;
        }

        @Override
        public URL getUrl() {
            return new URL("ttc-rpc", "127.0.0.1", 20880, SVC);
        }

        @Override
        public boolean isAvailable() {
            return true;
        }

        @Override
        public void destroy() {
        }
    }

    private static Invocation newInvocation() {
        return new RpcInvocation(SVC, "greet", new Class<?>[]{String.class}, new Object[]{"zifang"});
    }

    private final MetricsCollector metrics = MetricsCollector.getInstance();

    @BeforeEach
    void isolate() {
        metrics.reset();
        RpcContext.clear();
        ThreadContext.clearAll();
    }

    private Map<String, Object> bucket() {
        Map<String, Object> m = metrics.getAll().get(KEY);
        assertNotNull(m, "没建桶，实测 keys=" + metrics.getAll().keySet());
        return m;
    }

    // ---------------------------------------------------------------- 消费端监控

    @Test
    @DisplayName("MonitorFilter 成功调用记 success=1，且原样返回同一个 Result 对象")
    void monitorFilterCountsSuccess() throws Throwable {
        StubInvoker invoker = new StubInvoker().returns(Result.success("hi"));
        Invocation inv = newInvocation();
        Result out = new MonitorFilter().invoke(invoker, inv);
        // 猎物：invoker 真的被调了一次，收到的是同一个 invocation，返回的是同一个 Result
        assertEquals(1, invoker.calls);
        assertSame(inv, invoker.seen.get());
        assertEquals("hi", out.getValue());
        Map<String, Object> m = bucket();
        assertEquals(1L, m.get("total"));
        assertEquals(1L, m.get("success"));
        assertEquals(0L, m.get("error"));
        assertEquals(SVC, m.get("service"));
        assertEquals("greet", m.get("method"));
    }

    @Test
    @DisplayName("MonitorFilter 把带异常的 Result 记成 error，但不改写返回值")
    void monitorFilterCountsErrorResult() throws Throwable {
        IllegalStateException boom = new IllegalStateException("biz");
        Result err = Result.error(boom);
        StubInvoker invoker = new StubInvoker().returns(err);
        Result out = new MonitorFilter().invoke(invoker, newInvocation());
        assertSame(err, out, "异常结果原样透出，没有被包装");
        assertTrue(out.hasException());
        Map<String, Object> m = bucket();
        assertEquals(1L, m.get("total"));
        assertEquals(0L, m.get("success"));
        assertEquals(1L, m.get("error"));
    }

    @Test
    @DisplayName("bug_ invoker 抛出时 MonitorFilter 换成 RuntimeException，原始异常降级成 cause")
    void bug_monitorFilterWrapsTheOriginalThrowable() throws Throwable {
        IllegalStateException boom = new IllegalStateException("transport down");
        StubInvoker invoker = new StubInvoker().throwsIt(boom);
        Throwable thrown = assertThrows(Throwable.class,
                () -> new MonitorFilter().invoke(invoker, newInvocation()));
        // 实测行为：类型变了，上层按 catch (IllegalStateException) 就接不住
        assertEquals(RuntimeException.class, thrown.getClass());
        assertSame(boom, thrown.getCause());
        Map<String, Object> m = bucket();
        assertEquals(1L, m.get("error"));
        assertEquals(0L, m.get("success"));
    }

    @Test
    @DisplayName("bug_ invoker 返回 null 时 MonitorFilter 变成 RuntimeException(NPE)")
    void bug_monitorFilterTurnsNullResultIntoWrappedNpe() throws Throwable {
        StubInvoker invoker = new StubInvoker().returns(null);
        Throwable thrown = assertThrows(Throwable.class,
                () -> new MonitorFilter().invoke(invoker, newInvocation()));
        assertEquals(RuntimeException.class, thrown.getClass());
        assertTrue(thrown.getCause() instanceof NullPointerException,
                "实测 cause=" + thrown.getCause());
        // 猎物：这一笔仍然被计入 error，指标不会漏
        assertEquals(1L, bucket().get("error"));
        assertEquals(1L, bucket().get("total"));
    }

    @Test
    @DisplayName("bug_ ProviderMonitorFilter 原样重抛，与消费端不对称")
    void bug_providerMonitorRethrowsTheOriginal() throws Throwable {
        IllegalStateException boom = new IllegalStateException("provider side");
        StubInvoker invoker = new StubInvoker().throwsIt(boom);
        Throwable thrown = assertThrows(Throwable.class,
                () -> new ProviderMonitorFilter().invoke(invoker, newInvocation()));
        assertSame(boom, thrown, "服务端不包装：同一份代码两侧异常类型不同");
        assertEquals(1L, bucket().get("error"));
    }

    @Test
    @DisplayName("bug_ ProviderMonitorFilter 遇到 null 结果直接抛裸 NPE，不经过 RuntimeException")
    void bug_providerMonitorThrowsRawNpeOnNullResult() throws Throwable {
        StubInvoker invoker = new StubInvoker().returns(null);
        Throwable thrown = assertThrows(Throwable.class,
                () -> new ProviderMonitorFilter().invoke(invoker, newInvocation()));
        assertTrue(thrown instanceof NullPointerException, "实测 " + thrown.getClass());
        // 猎物：消费端同样是 null 结果时抛的是 RuntimeException（见上一例）
        StubInvoker consumerSide = new StubInvoker().returns(null);
        Throwable other = assertThrows(Throwable.class,
                () -> new MonitorFilter().invoke(consumerSide, newInvocation()));
        assertEquals(RuntimeException.class, other.getClass());
        assertNotSame(other.getClass(), thrown.getClass());
    }

    @Test
    @DisplayName("ProviderMonitorFilter 正常与错误结果都记一次账")
    void providerMonitorRecordsBothOutcomes() throws Throwable {
        new ProviderMonitorFilter().invoke(new StubInvoker().returns(Result.success("ok")), newInvocation());
        assertEquals(1L, bucket().get("success"));
        new ProviderMonitorFilter().invoke(
                new StubInvoker().returns(Result.error(new IllegalStateException("x"))), newInvocation());
        Map<String, Object> m = bucket();
        assertEquals(2L, m.get("total"));
        assertEquals(1L, m.get("success"));
        assertEquals(1L, m.get("error"));
        // 猎物：provider 与 consumer 用同一个 key ⇒ 两侧计数混在同一个桶里
        new MonitorFilter().invoke(new StubInvoker().returns(Result.success("ok")), newInvocation());
        assertEquals(3L, bucket().get("total"));
    }

    @Test
    @DisplayName("bug_ 两侧监控过滤器共用 key，服务端耗时和消费端耗时并进同一个桶")
    void bug_consumerAndProviderShareOneKey() throws Throwable {
        new MonitorFilter().invoke(new StubInvoker().returns(Result.success("ok")), newInvocation());
        new ProviderMonitorFilter().invoke(new StubInvoker().returns(Result.success("ok")), newInvocation());
        Map<String, Object> m = bucket();
        assertEquals(2L, m.get("total"));
        // 一次 consumer + 一次 provider，指标表里却只有一个键：分不清是哪一侧的耗时
        assertEquals(1, metrics.getAll().size(), metrics.getAll().keySet().toString());
        assertFalse(m.containsKey("group"), "桶里连侧别信息都没有");
    }

    @Test
    @DisplayName("两个监控过滤器的 invoke 声明的 throws 不对称（编译期就能看出）")
    void bug_declaredThrowsAreAsymmetric() throws Throwable {
        Method[] pair = new Method[2];
        int k = 0;
        for (Class<?> c : new Class<?>[]{MonitorFilter.class, ProviderMonitorFilter.class}) {
            pair[k++] = c.getDeclaredMethod("invoke", Invoker.class, Invocation.class);
        }
        // 猎物：接口自己声明了 throws Throwable
        assertEquals(1, Filter.class.getDeclaredMethod("invoke", Invoker.class, Invocation.class)
                .getExceptionTypes().length);
        assertEquals(0, pair[0].getExceptionTypes().length, "MonitorFilter 收窄成不抛");
        assertEquals(1, pair[1].getExceptionTypes().length, "ProviderMonitorFilter 照抄接口");
    }

    // ---------------------------------------------------------------- 消费端 trace

    @Test
    @DisplayName("ConsumerTraceFilter 把线程本地的 traceId 写进 attachment 再透传")
    void consumerTraceWritesAttachment() throws Throwable {
        RpcContext ctx = RpcContext.getContext();
        StubInvoker invoker = new StubInvoker().returns(Result.success("ok"));
        Invocation inv = newInvocation();
        Result out = new ConsumerTraceFilter().invoke(invoker, inv);
        assertSame(inv, invoker.seen.get(), "invocation 对象原样往下传");
        assertEquals("ok", out.getValue());
        // 猎物：invoker 侧已经能看到这个 attachment（说明是调用前写的）
        assertEquals(ctx.getTraceId(), invoker.seen.get().getAttachment("trace-id"));
        assertEquals(ctx.getTraceId(), inv.getAttachment("trace-id"));
        // 猎物：它不记指标
        assertTrue(metrics.getAll().isEmpty(), metrics.getAll().keySet().toString());
    }

    @Test
    @DisplayName("bug_ 同线程连续两次调用拿到同一个 traceId，链路无法区分调用")
    void bug_traceIdIsStampedOntoEveryCallOfTheThread() throws Throwable {
        StubInvoker i1 = new StubInvoker().returns(Result.success("a"));
        StubInvoker i2 = new StubInvoker().returns(Result.success("b"));
        new ConsumerTraceFilter().invoke(i1, newInvocation());
        new ConsumerTraceFilter().invoke(i2, newInvocation());
        // 猎物：两次调用都确实带上了 trace-id
        assertNotNull(i1.seen.get().getAttachment("trace-id"));
        assertNotNull(i2.seen.get().getAttachment("trace-id"));
        assertEquals(i1.seen.get().getAttachment("trace-id"), i2.seen.get().getAttachment("trace-id"));

        final AtomicReference<String> other = new AtomicReference<>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                StubInvoker inv = new StubInvoker().returns(Result.success("c"));
                try {
                    new ConsumerTraceFilter().invoke(inv, newInvocation());
                } catch (Throwable ignore) {
                    // 本用例只关心 attachment 取到了什么
                }
                other.set(inv.seen.get() == null ? null : inv.seen.get().getAttachment("trace-id"));
            }
        }, "consumer-trace-probe");
        t.start();
        t.join(5000);
        assertNotNull(other.get());
        assertTrue(!other.get().equals(i1.seen.get().getAttachment("trace-id")),
                "换了线程应该换 id");
    }

    @Test
    @DisplayName("bug_ 上游传进来的 trace-id 会被本地线程计数器无条件覆盖")
    void bug_consumerTraceClobbersAnIncomingTraceId() throws Throwable {
        // 猎物：网关/HTTP 入口带进来的 id 本来是读得到的
        Invocation inv = newInvocation();
        inv.setAttachment("trace-id", "incoming-from-gateway-8899");
        assertEquals("incoming-from-gateway-8899", inv.getAttachment("trace-id"));

        StubInvoker invoker = new StubInvoker().returns(Result.success("ok"));
        new ConsumerTraceFilter().invoke(invoker, inv);
        String after = inv.getAttachment("trace-id");
        assertEquals(RpcContext.getContext().getTraceId(), after);
        assertFalse("incoming-from-gateway-8899".equals(after),
                "跨进程的 trace 关联在这里断了：本地自增号顶掉了全局 id");
    }

    // ---------------------------------------------------------------- 服务端上下文

    @Test
    @DisplayName("ProviderContextFilter 在调用期间把 traceId 放进 MDC，返回前清掉")
    void providerContextSetsThenClearsMdc() throws Throwable {
        StubInvoker invoker = new StubInvoker().returns(Result.success("ok"));
        Invocation inv = newInvocation();
        inv.setAttachment("trace-id", "T-77");
        new ProviderContextFilter().invoke(invoker, inv);
        // 猎物：链路内部确实读得到
        assertEquals("T-77", invoker.mdcTraceIdDuringInvoke.get());
        assertEquals(null, ThreadContext.get("traceId"), "finally 里清掉了");
    }

    @Test
    @DisplayName("没有 trace-id 附件时 MDC 保持为空，不影响链路")
    void providerContextWithoutTraceId() throws Throwable {
        StubInvoker invoker = new StubInvoker().returns(Result.success("ok"));
        new ProviderContextFilter().invoke(invoker, newInvocation());
        assertEquals(null, invoker.mdcTraceIdDuringInvoke.get());
        assertEquals(1, invoker.calls);
        // 猎物：带附件时是另一回事
        StubInvoker with = new StubInvoker().returns(Result.success("ok"));
        Invocation inv = newInvocation();
        inv.setAttachment("trace-id", "T-78");
        new ProviderContextFilter().invoke(with, inv);
        assertEquals("T-78", with.mdcTraceIdDuringInvoke.get());
    }

    @Test
    @DisplayName("bug_ ProviderContextFilter 的 clearAll 会顺手抹掉别人的 MDC 键")
    void bug_providerContextFilterWipesUnrelatedMdcKeys() throws Throwable {
        ThreadContext.put("tenant", "ACME");
        assertEquals("ACME", ThreadContext.get("tenant"), "猎物：过滤器还没进场");
        StubInvoker invoker = new StubInvoker().returns(Result.success("ok"));
        new ProviderContextFilter().invoke(invoker, newInvocation());
        assertEquals(null, ThreadContext.get("tenant"),
                "没有 trace-id 也会走 finally 的 clearAll()，同线程其它 MDC 一起没了");
        // 链路内部也读不到（其实这时还在 finally 之前）
        assertEquals("ACME", invoker.mdcTenantDuringInvoke.get());
    }

    @Test
    @DisplayName("ProviderContextFilter 原样透出异常，且 MDC 仍然被清")
    void providerContextPropagatesOriginalThrowable() throws Throwable {
        IllegalStateException boom = new IllegalStateException("provider blew up");
        StubInvoker invoker = new StubInvoker().throwsIt(boom);
        Invocation inv = newInvocation();
        inv.setAttachment("trace-id", "T-79");
        Throwable thrown = assertThrows(Throwable.class,
                () -> new ProviderContextFilter().invoke(invoker, inv));
        assertSame(boom, thrown);
        assertEquals(null, ThreadContext.get("traceId"));
    }
}
