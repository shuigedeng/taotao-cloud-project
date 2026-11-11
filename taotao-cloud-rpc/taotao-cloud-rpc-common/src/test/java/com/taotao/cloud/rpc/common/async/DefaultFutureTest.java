package com.taotao.cloud.rpc.common.async;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DefaultFuture 首批测试。
 * <p>
 * 它自称「基于 requestId -&gt; Future 映射的请求-响应匹配」，实测三处对不上：
 * 一是 {@code get()} 是 10ms 轮询而不是等待通知，且没有 cancel / 带超时的 get 重载；
 * 二是映射表以 requestId 为唯一键，重复 id 会让前一个调用方永远等不到自己的响应；
 * 三是被中断时 {@code get()} 既不抛异常也不清理映射表，直接 return null。
 * 全库没有任何生产代码 new 过它（{@code ReferenceConfig.isAsync()} 那几个开关无人读）。
 */
class DefaultFutureTest {

    /** 不与其它用例撞车的自增号段。 */
    private static final AtomicLong ID_GEN = new AtomicLong(100_000L);

    @SuppressWarnings("unchecked")
    private static Map<Long, DefaultFuture> futures() throws Exception {
        Field f = DefaultFuture.class.getDeclaredField("FUTURES");
        f.setAccessible(true);
        return (Map<Long, DefaultFuture>) f.get(null);
    }

    private static DefaultFuture registered(long id, long timeoutMs) throws Exception {
        DefaultFuture future = DefaultFuture.newFuture(id, timeoutMs);
        assertTrue(futures().containsKey(id), "newFuture 之后应当进全局映射表");
        return future;
    }

    private static long nextId() {
        return ID_GEN.incrementAndGet();
    }

    @Test
    @DisplayName("newFuture 注册进 FUTURES，received 完成并摘掉")
    void receivedCompletesAndEvicts() throws Throwable {
        long id = nextId();
        DefaultFuture f = registered(id, 5000);
        assertFalse(f.isDone());
        // 猎物：表里存的就是这个对象本身
        assertSame(f, futures().get(id));

        DefaultFuture.received(id, "hello");
        assertTrue(f.isDone());
        assertFalse(futures().containsKey(id), "完成后必须从全局表摘掉，否则表只涨不消");
        assertEquals("hello", f.get());
    }

    @Test
    @DisplayName("receivedException 把原异常对象原样抛出")
    void receivedExceptionRethrowsTheSameInstance() throws Exception {
        long id = nextId();
        DefaultFuture f = registered(id, 5000);
        IllegalStateException boom = new IllegalStateException("boom");
        DefaultFuture.receivedException(id, boom);
        assertTrue(f.isDone());
        Throwable thrown = assertThrows(Throwable.class, () -> f.get());
        assertSame(boom, thrown);
    }

    @Test
    @DisplayName("timeoutMs=0 的 get() 立刻 TimeoutException，并且把自己从表里摘掉")
    void zeroTimeoutThrowsImmediatelyAndEvicts() throws Exception {
        long id = nextId();
        DefaultFuture f = registered(id, 0);
        long startNs = System.nanoTime();
        TimeoutException te = assertThrows(TimeoutException.class, () -> f.get());
        long elapsedMs = (System.nanoTime() - startNs) / 1_000_000L;
        assertTrue(elapsedMs < 100, "timeout=0 不该睡 10ms，实测 " + elapsedMs + "ms");
        assertTrue(te.getMessage().contains(String.valueOf(id)), te.getMessage());
        assertFalse(futures().containsKey(id));
        assertFalse(f.isDone(), "超时不算完成，调用方拿不到任何标记");
    }

    @Test
    @DisplayName("bug_ 超时后到达的响应被静默丢弃，没有任何回调或指标")
    void bug_lateResponseIsSilentlyDropped() throws Throwable {
        long id = nextId();
        DefaultFuture f = registered(id, 0);
        assertThrows(TimeoutException.class, () -> f.get());

        // 猎物：响应「到了」——received 对已知 id 是有作用的，这里只因表里已空而无作用
        long known = nextId();
        DefaultFuture other = registered(known, 5000);
        DefaultFuture.received(known, "landed");
        assertEquals("landed", other.get());

        DefaultFuture.received(id, "too late");
        assertFalse(f.isDone(), "超时那张票已经废了，没人告诉调用方");
        // 实测行为：响应确实到了，但调用方再也读不到它
        assertThrows(TimeoutException.class, () -> f.get());
    }

    @Test
    @DisplayName("bug_ requestId 撞号时，前一个调用方永远拿不到自己的响应")
    void bug_duplicateRequestIdBricksTheFirstCaller() throws Throwable {
        long id = nextId();
        // 表里同一个 key 只能挂一个 future；第二次 put 直接覆盖第一次
        DefaultFuture first = DefaultFuture.newFuture(id, 0);
        DefaultFuture second = DefaultFuture.newFuture(id, 5000);
        assertSame(second, futures().get(id), "映射表按 id 覆盖，第一个对象从表里消失了");

        DefaultFuture.received(id, "for-second");
        assertTrue(second.isDone());
        assertFalse(first.isDone(), "响应被记到别人头上");
        assertThrows(TimeoutException.class, () -> first.get());
        assertEquals("for-second", second.get());
    }

    @Test
    @DisplayName("bug_ 被中断时 get() 既不抛也不清理，直接 return null")
    void bug_getReturnsNullWhenInterrupted() throws Exception {
        long id = nextId();
        final DefaultFuture f = registered(id, 60_000L);
        final AtomicReference<Object> returned = new AtomicReference<>("NOT_RETURNED");
        final AtomicReference<Throwable> threw = new AtomicReference<>();
        final CountDownLatch started = new CountDownLatch(1);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                started.countDown();
                try {
                    returned.set(f.get());
                } catch (Throwable e) {
                    threw.set(e);
                }
            }
        }, "future-interrupt-probe");
        t.setDaemon(true);
        t.start();
        assertTrue(started.await(5, TimeUnit.SECONDS));
        // 等它进到 10ms 轮询的 sleep 里再打断
        Thread.sleep(50);
        t.interrupt();
        t.join(5000);
        assertFalse(t.isAlive(), "被打断的 get() 应该已经返回");

        // 猎物：既没有异常，也没有超时
        assertEquals(null, threw.get(), "中断路径不该抛东西：" + threw.get());
        // 实测行为：返回值和「服务端真的返回 null」完全长得一样
        assertEquals(null, returned.get());
        assertFalse(f.isDone());
        assertTrue(futures().containsKey(id), "中断路径不做 FUTURES.remove —— 表里留下永久僵尸");
        futures().remove(id);
    }

    @Test
    @DisplayName("bug_ 没人 take 的 future 永远躺在 FUTURES 里，且没有任何清理入口")
    void bug_abandonedFuturesLeakForever() throws Exception {
        long a = nextId();
        long b = nextId();
        registered(a, 60_000L);
        registered(b, 60_000L);
        assertTrue(futures().containsKey(a));
        assertTrue(futures().containsKey(b));
        // 猎物：received 是唯一的摘除路径，走一次就少一个
        DefaultFuture.received(a, null);
        assertFalse(futures().containsKey(a));

        assertFalse(hasAnyMethodNamed("cancel"), "没有 cancel");
        assertFalse(hasAnyMethodNamed("clear"), "没有批量清理");
        assertFalse(hasAnyMethodNamed("expire"), "没有过期回收");
        assertFalse(hasAnyMethodNamed("remove"), "连按 id 摘除的公开方法都没有");
        futures().remove(b);
    }

    private static boolean hasAnyMethodNamed(String name) {
        for (Method m : DefaultFuture.class.getDeclaredMethods()) {
            if (m.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("bug_ 只有无参 get()：既没有 cancel，也没有带超时的 get(long, TimeUnit)")
    void bug_noBoundedWaitApi() throws Exception {
        // 猎物：现存的三个入口都能反射拿到
        assertNotNull(DefaultFuture.class.getDeclaredMethod("get"));
        assertNotNull(DefaultFuture.class.getDeclaredMethod("isDone"));
        assertNotNull(DefaultFuture.class.getDeclaredMethod("newFuture", long.class, long.class));

        assertThrows(NoSuchMethodException.class,
                () -> DefaultFuture.class.getDeclaredMethod("cancel", boolean.class));
        assertThrows(NoSuchMethodException.class,
                () -> DefaultFuture.class.getDeclaredMethod("get", long.class, TimeUnit.class));
        // 超时值只能在 newFuture 时钉死，之后改不了
        assertThrows(NoSuchMethodException.class,
                () -> DefaultFuture.class.getDeclaredMethod("setTimeoutMs", long.class));
    }

    @Test
    @DisplayName("get() 是 10ms 轮询：真实等待时长比 timeout 多出最多一个轮询片")
    void getIsPollingNotSignalled() throws Exception {
        long id = nextId();
        DefaultFuture f = registered(id, 30);
        long start = System.currentTimeMillis();
        assertThrows(TimeoutException.class, () -> f.get());
        long elapsed = System.currentTimeMillis() - start;
        // 猎物：确实睡了，而不是立刻返回（timeout=0 那一例已经证明 0 会秒回）
        assertTrue(elapsed >= 20, "30ms 超时应真的等过，实测 " + elapsed + "ms");
        assertTrue(elapsed < 200, "不该远超 timeout：" + elapsed + "ms");
    }

    @Test
    @DisplayName("getRequestId 回填调用方给的号，负数照收")
    void requestIdIsEchoedBack() throws Throwable {
        long id = nextId();
        DefaultFuture f = registered(id, 5000);
        assertEquals(id, f.getRequestId());
        long neg = -42L;
        DefaultFuture f2 = registered(neg, 5000);
        assertEquals(neg, f2.getRequestId());
        // 猎物：负号照样能匹配上响应
        DefaultFuture.received(neg, "ok");
        assertEquals("ok", f2.get());
        futures().remove(id);
        assertFalse(f.isDone());
    }

    @Test
    @DisplayName("同一个 id 只会被 completed 一次，重复 received 是无操作")
    void secondReceivedIsANoOp() throws Throwable {
        long id = nextId();
        DefaultFuture f = registered(id, 5000);
        DefaultFuture.received(id, "first");
        DefaultFuture.received(id, "second");
        // 猎物：第一次的值留下了
        assertEquals("first", f.get());
        // 重复 received 不抛异常（表里已经没有这个 key）
        assertFalse(futures().containsKey(id));
    }

    @Test
    @DisplayName("received / receivedException 对未知 id 都是静默无操作")
    void unknownIdIsSilentlyIgnored() throws Throwable {
        long id = nextId();
        DefaultFuture.received(id, "ghost");
        DefaultFuture.receivedException(id, new IllegalStateException("ghost"));
        assertFalse(futures().containsKey(id));
        // 猎物：已知 id 是有作用的
        long known = nextId();
        DefaultFuture f = registered(known, 5000);
        DefaultFuture.received(known, "real");
        assertEquals("real", f.get());
    }
}
