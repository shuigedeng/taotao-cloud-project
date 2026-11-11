package com.taotao.cloud.rpc.common.async;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RpcContext 首批测试。
 * <p>
 * 定位问题：这个上下文自称携带 traceId / spanId / token / 附件，实测只有线程本地的 traceId
 * 是真的；spanId 恒为 "0" 且没有 setter，token 根本不存在，attachment 表底层是
 * ConcurrentHashMap（塞 null 值直接 NPE）。全库唯一的读取方是 ConsumerTraceFilter。
 */
class RpcContextTest {

    @BeforeEach
    void normalizeThreadLocal() {
        RpcContext.clear();
    }

    @Test
    @DisplayName("getContext 是线程本地、按需创建")
    void contextIsThreadLocal() throws Exception {
        RpcContext a = RpcContext.getContext();
        assertSame(a, RpcContext.getContext(), "同一线程拿到同一个对象");

        final AtomicReference<RpcContext> other = new AtomicReference<>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                other.set(RpcContext.getContext());
            }
        }, "rpc-context-probe");
        t.start();
        t.join(5000);
        assertNotSame(a, other.get(), "别的线程应当有自己的上下文");
    }

    @Test
    @DisplayName("bug_ traceId 绑的是线程而不是调用：同线程连续两次调用共用一个 id")
    void bug_traceIdIsPerThreadNotPerCall() throws Exception {
        RpcContext.clear();
        String first = RpcContext.getContext().getTraceId();
        String second = RpcContext.getContext().getTraceId();
        // 猎物：线程确实换了 id（不是常量）
        assertNotNull(first);
        assertNotEquals("0", first);
        // 实测行为：同一线程内无论调多少次都是同一个 traceId
        assertEquals(first, second);
        for (int i = 0; i < 5; i++) {
            assertEquals(first, RpcContext.getContext().getTraceId());
        }

        final AtomicReference<String> onOtherThread = new AtomicReference<>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                onOtherThread.set(RpcContext.getContext().getTraceId());
            }
        }, "rpc-context-trace-probe");
        t.start();
        t.join(5000);
        assertNotEquals(first, onOtherThread.get(), "换了线程才是另一个 id —— 说明粒度是线程");
    }

    @Test
    @DisplayName("traceId 是自增数字串，newTraceId 严格比当前的大")
    void traceIdIsMonotonicNumeric() {
        long current = Long.parseLong(RpcContext.getContext().getTraceId());
        long generated = Long.parseLong(RpcContext.newTraceId());
        assertTrue(generated > current, current + " -> " + generated);
        // 猎物：普通构造出来的也是同一个计数器上的数字
        long plain = Long.parseLong(new RpcContext().getTraceId());
        assertTrue(plain >= current, plain + " >= " + current);
    }

    @Test
    @DisplayName("bug_ spanId 恒为『0』且没有任何写入口，文档里的层级是装饰")
    void bug_spanIdIsFrozenAtZero() throws Exception {
        // 猎物：默认构造与显式 traceId 构造都给了 spanId，读得到
        assertEquals("0", new RpcContext().getSpanId());
        assertEquals("0", new RpcContext("custom-trace").getSpanId());
        assertNotNull(RpcContext.class.getDeclaredMethod("getSpanId"));
        assertNotNull(RpcContext.class.getDeclaredMethod("setAttachment", String.class, String.class));

        assertThrows(NoSuchMethodException.class,
                () -> RpcContext.class.getDeclaredMethod("setSpanId", String.class));
        assertThrows(NoSuchMethodException.class,
                () -> RpcContext.class.getDeclaredMethod("nextSpanId"));
        Field f = RpcContext.class.getDeclaredField("spanId");
        assertTrue(java.lang.reflect.Modifier.isFinal(f.getModifiers()), "spanId 是 final，永远只能是 0");
    }

    @Test
    @DisplayName("带 traceId 的构造函数只认传入值，不写回 ThreadLocal")
    void explicitTraceIdConstructor() {
        RpcContext before = RpcContext.getContext();
        RpcContext manual = new RpcContext("manual-id");
        assertEquals("manual-id", manual.getTraceId());
        // 猎物：这个构造函数唯一改变的就是 traceId
        assertEquals("0", manual.getSpanId());
        assertSame(before, RpcContext.getContext(), "手工 new 出来的对象不会顶掉线程本地");
    }

    @Test
    @DisplayName("setContext(null) 被静默忽略，非 null 才真的换")
    void setContextIgnoresNull() {
        RpcContext a = RpcContext.getContext();
        RpcContext b = new RpcContext("b");
        RpcContext.setContext(null);
        assertSame(a, RpcContext.getContext(), "传 null 不报错也不清");
        RpcContext.setContext(b);
        assertSame(b, RpcContext.getContext());
        // 猎物：clear 之后又回到按需创建
        RpcContext.clear();
        assertNotSame(b, RpcContext.getContext());
    }

    @Test
    @DisplayName("bug_ attachment 底层是 ConcurrentHashMap：塞 null 值直接 NPE，且没有 remove")
    void bug_attachmentsRejectNullValues() throws Exception {
        RpcContext ctx = RpcContext.getContext();
        ctx.setAttachment("k", "v");
        assertEquals("v", ctx.getAttachment("k"));
        assertNullValueThrows(ctx);

        assertThrows(NoSuchMethodException.class,
                () -> RpcContext.class.getDeclaredMethod("removeAttachment", String.class));
        // 猎物：接口上唯一的清除手段是整个上下文 clear
        assertNotNull(RpcContext.class.getDeclaredMethod("clear"));
        Field f = RpcContext.class.getDeclaredField("attachments");
        // 声明类型是 ConcurrentMap 接口，运行时才是 ConcurrentHashMap（不接受 null 值的那一位）
        assertEquals(java.util.concurrent.ConcurrentMap.class, f.getType());
        f.setAccessible(true);
        assertEquals(java.util.concurrent.ConcurrentHashMap.class, f.get(ctx).getClass());
    }

    private void assertNullValueThrows(RpcContext ctx) {
        assertThrows(NullPointerException.class, () -> ctx.setAttachment("n", null));
        assertEquals("v", ctx.getAttachment("k"), "抛异常并不会把之前的值弄丢");
    }

    @Test
    @DisplayName("getAttachments 返回的是活表，直接往里塞也能被 getAttachment 读到")
    void getAttachmentsReturnsTheLiveMap() {
        RpcContext ctx = RpcContext.getContext();
        ctx.getAttachments().put("direct", "yes");
        assertEquals("yes", ctx.getAttachment("direct"));
        assertSame(ctx.getAttachments(), ctx.getAttachments(), "每次都是同一个 map 对象");
        assertEquals(1, ctx.getAttachments().size());
    }

    @Test
    @DisplayName("getAttachment 缺失时返回 null，没有默认值重载")
    void missingAttachmentIsNull() throws Exception {
        RpcContext ctx = RpcContext.getContext();
        // 猎物：存在的键读得到
        ctx.setAttachment("present", "1");
        assertEquals("1", ctx.getAttachment("present"));
        assertNullValueForAbsentKey(ctx);
        assertThrows(NoSuchMethodException.class,
                () -> RpcContext.class.getDeclaredMethod("getAttachment", String.class, String.class));
    }

    private void assertNullValueForAbsentKey(RpcContext ctx) {
        assertEquals(null, ctx.getAttachment("absent"));
    }

    @Test
    @DisplayName("bug_ internal 这张表没有任何公开入口，是死存储")
    void bug_internalMapIsUnreachable() throws Exception {
        Field internal = RpcContext.class.getDeclaredField("internal");
        Field attachments = RpcContext.class.getDeclaredField("attachments");
        assertNotNull(attachments);
        // 两个字段都声明成 ConcurrentMap 接口，运行时才是 ConcurrentHashMap
        assertEquals(java.util.concurrent.ConcurrentMap.class, internal.getType());
        RpcContext ctx = RpcContext.getContext();
        internal.setAccessible(true);
        attachments.setAccessible(true);
        assertEquals(java.util.concurrent.ConcurrentHashMap.class, internal.get(ctx).getClass());
        assertEquals(java.util.concurrent.ConcurrentHashMap.class, attachments.get(ctx).getClass());
        for (Method m : RpcContext.class.getDeclaredMethods()) {
            assertFalse(m.getName().toLowerCase().contains("internal"),
                    "不该出现访问器，实测却有：" + m.getName());
        }
        // 猎物：字段确实存在且是私有的
        assertTrue(java.lang.reflect.Modifier.isPrivate(internal.getModifiers()));
    }

    @Test
    @DisplayName("bug_ javadoc 承诺的 token 不存在，想带 token 只能自己塞 attachment")
    void bug_advertisedTokenDoesNotExist() throws Exception {
        for (Method m : RpcContext.class.getDeclaredMethods()) {
            assertFalse(m.getName().toLowerCase().contains("token"), "实测有 token 访问器：" + m.getName());
        }
        // 猎物：attachment 通道是通的，所以「带 token」这件事只能靠约定
        RpcContext ctx = RpcContext.getContext();
        ctx.setAttachment("token", "t-1");
        assertEquals("t-1", ctx.getAttachment("token"));
        assertThrows(NoSuchMethodException.class, () -> RpcContext.class.getDeclaredMethod("getToken"));
    }

    @Test
    @DisplayName("子线程不会继承父线程的上下文（不是 InheritableThreadLocal）")
    void childThreadsDoNotInherit() throws Exception {
        RpcContext parent = RpcContext.getContext();
        parent.setAttachment("tenant", "T1");
        final AtomicReference<String> tenant = new AtomicReference<>();
        final AtomicReference<RpcContext> child = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                child.set(RpcContext.getContext());
                tenant.set(RpcContext.getContext().getAttachment("tenant"));
                latch.countDown();
            }
        }, "rpc-context-inherit-probe");
        t.start();
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        t.join(5000);
        // 猎物：父线程自己读得到
        assertEquals("T1", parent.getAttachment("tenant"));
        assertEquals(null, tenant.get(), "子线程拿不到父线程的 attachment");
        assertNotSame(parent, child.get());
        assertNotEquals(parent.getTraceId(), child.get().getTraceId());
    }
}
