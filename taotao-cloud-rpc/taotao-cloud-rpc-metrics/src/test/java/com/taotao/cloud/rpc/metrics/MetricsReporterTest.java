package com.taotao.cloud.rpc.metrics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MetricsReporter 首批测试。
 * <p>
 * 这个类自称「周期性地把指标推送到 ttc-rpc-admin」，实测两头都对不上：
 * 一是 scheduler 是 final 字段，而 stop() 无条件 shutdownNow() —— 于是 stop 是终局操作，
 * 连「从没 start 过就 stop 一次」都会把实例永久废掉；
 * 二是它压根没有发送通道，{@code report()} 是私有的，只打日志。
 * 「全库没有任何地方 new 过 MetricsReporter」这条只能在报告里以 grep 佐证（本模块测试看不见别的模块）。
 */
class MetricsReporterTest {

    private static final String THREAD_NAME = "ttc-rpc-metrics-reporter";

    private static boolean readStarted(MetricsReporter r) throws Exception {
        Field f = MetricsReporter.class.getDeclaredField("started");
        f.setAccessible(true);
        return f.getBoolean(r);
    }

    private static ScheduledExecutorService readScheduler(MetricsReporter r) throws Exception {
        Field f = MetricsReporter.class.getDeclaredField("scheduler");
        f.setAccessible(true);
        return (ScheduledExecutorService) f.get(r);
    }

    /**
     * 注入一个可数的池子。
     * <p>
     * JDK 25 的模块系统不允许反射进 {@code java.util.concurrent}
     * （{@code Executors$DelegatedScheduledExecutorService.e} 会抛 InaccessibleObjectException），
     * 而 {@code Executors.newSingleThreadScheduledExecutor} 返回的正是这个包装器，数不到队列。
     * 凡是要数「排了几个周期任务」的用例，先把同类型的 {@link ScheduledThreadPoolExecutor}
     * 注进 MetricsReporter <b>自己</b>的 scheduler 字段（unnamed module 的实例 final 字段可写），
     * 并当场回读证明注入生效。
     */
    private static ScheduledThreadPoolExecutor injectPool(MetricsReporter r) throws Exception {
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable run) {
                Thread t = new Thread(run, "reporter-probe-pool");
                t.setDaemon(true);
                return t;
            }
        });
        Field f = MetricsReporter.class.getDeclaredField("scheduler");
        f.setAccessible(true);
        f.set(r, pool);
        assertSame(pool, readScheduler(r), "注入没生效，后面数的就不是这个池");
        return pool;
    }

    private static int queuedTasks(MetricsReporter r) throws Exception {
        Object s = readScheduler(r);
        assertTrue(s instanceof ThreadPoolExecutor,
                "必须先 injectPool 才数得了队列，实测 " + s.getClass().getName());
        return ((ThreadPoolExecutor) s).getQueue().size();
    }

    private static Set<Thread> namedThreads() {
        Set<Thread> out = new HashSet<>();
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (THREAD_NAME.equals(t.getName()) && t.isAlive()) {
                out.add(t);
            }
        }
        return out;
    }

    /** 只等『新出现的同名线程』，不靠 sleep 猜，也不受别的用例残留线程影响。 */
    private static Thread awaitNewThread(Set<Thread> before) throws InterruptedException {
        long deadline = System.currentTimeMillis() + 2000;
        while (System.currentTimeMillis() < deadline) {
            for (Thread t : namedThreads()) {
                if (!before.contains(t)) {
                    return t;
                }
            }
            Thread.sleep(10);
        }
        return null;
    }

    @Test
    @DisplayName("start 起一条名为 ttc-rpc-metrics-reporter 的守护线程，stop 后它退出")
    void startCreatesOneNamedDaemonThread() throws Exception {
        MetricsReporter r = new MetricsReporter();
        Set<Thread> before = namedThreads();
        try {
            r.start(3600);
            Thread t = awaitNewThread(before);
            assertNotNull(t, "start 之后没有新线程冒出来");
            assertTrue(t.isDaemon(), "不是守护线程就会挂住 JVM 退出");
            assertTrue(readStarted(r));
            // 猎物：生产用的确实是 JDK 的单线程包装器，不是裸的 ScheduledThreadPoolExecutor
            assertNotSame(ScheduledThreadPoolExecutor.class, readScheduler(r).getClass());
            r.stop();
            t.join(2000);
            assertFalse(t.isAlive(), "stop 之后工作线程应退出");
        } finally {
            r.stop();
        }
    }

    @Test
    @DisplayName("重复 start 被 started 挡板挡住，队列里仍只有一个任务")
    void secondStartIsANoOp() throws Exception {
        MetricsReporter r = new MetricsReporter();
        injectPool(r);
        try {
            r.start(3600);
            r.start(3600);
            r.start(3600);
            assertEquals(1, queuedTasks(r));
            assertFalse(readScheduler(r).isShutdown());
        } finally {
            r.stop();
        }
    }

    @Test
    @DisplayName("bug_ 挡板是 check-then-act：并发 start 可能排进两个周期任务")
    void bug_startGuardIsNotAtomic() throws Exception {
        int trials = 20;
        int duplicated = 0;
        for (int i = 0; i < trials; i++) {
            final MetricsReporter r = new MetricsReporter();
            injectPool(r);
            final CountDownLatch both = new CountDownLatch(2);
            Runnable body = new Runnable() {
                @Override
                public void run() {
                    both.countDown();
                    try {
                        both.await(5, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    r.start(3600);
                }
            };
            Thread a = new Thread(body, "reporter-race-a");
            Thread b = new Thread(body, "reporter-race-b");
            a.setDaemon(true);
            b.setDaemon(true);
            a.start();
            b.start();
            a.join(5000);
            b.join(5000);
            int q = queuedTasks(r);
            assertTrue(q >= 1 && q <= 2, "两个线程最多排两个任务，实测 " + q);
            assertTrue(readStarted(r));
            if (q > 1) {
                duplicated++;
            }
            r.stop();
        }
        // 不断言「一定撞上」（撞不撞看调度），只把观测率打出来：单跑一次不构成证据
        System.out.println("[ttc-rpc-metrics] 并发 start 排进 >1 个任务的轮数：" + duplicated + "/" + trials);
    }

    @Test
    @DisplayName("bug_ stop 是终局操作：再 start 抛 RejectedExecutionException，且 started 卡在 true")
    void bug_restartIsPermanentlyBricked() throws Exception {
        MetricsReporter r = new MetricsReporter();
        ScheduledThreadPoolExecutor pool = injectPool(r);
        r.start(3600);
        assertEquals(1, queuedTasks(r));
        r.stop();
        assertFalse(readStarted(r), "stop 把挡板放回了 false");
        assertTrue(pool.isShutdown());
        assertEquals(0, queuedTasks(r), "shutdownNow 已经把周期任务从队列里清掉");

        // 挡板是 false ⇒ 真的走到 scheduleAtFixedRate，而线程池已经 shutdown
        assertThrows(RejectedExecutionException.class, () -> r.start(3600));
        // 致命的是赋值顺序：started = true 发生在 schedule 之前，异常之后没人回滚
        assertTrue(readStarted(r), "异常后 started 仍是 true");
        r.start(3600);
        assertTrue(pool.isShutdown(), "第三次 start 静默无操作");
        assertEquals(0, queuedTasks(r), "也确实没有新任务进队列");
    }

    @Test
    @DisplayName("bug_ 连『没 start 过就 stop』也会废掉实例；stop 本身不抛")
    void bug_stopWithoutStartIsAlsoTerminal() throws Exception {
        MetricsReporter r = new MetricsReporter();
        ScheduledThreadPoolExecutor pool = injectPool(r);
        r.stop();
        assertFalse(readStarted(r));
        assertTrue(pool.isShutdown(), "scheduler 是 final 字段，stop 无条件关掉它");
        assertThrows(RejectedExecutionException.class, () -> r.start(3600));
        assertTrue(readStarted(r), "同样把 started 留在了 true");

        // 猎物：不碰 stop 的实例可以正常 start
        MetricsReporter fresh = new MetricsReporter();
        ScheduledThreadPoolExecutor p2 = injectPool(fresh);
        fresh.start(3600);
        assertFalse(p2.isShutdown());
        assertEquals(1, queuedTasks(fresh));
        assertTrue(readStarted(fresh));
        fresh.stop();
    }

    @Test
    @DisplayName("bug_ 非法 interval 抛异常，但同样把 started 留在 true")
    void bug_badIntervalPoisonsTheFlag() throws Exception {
        // 猎物：合法值能用
        MetricsReporter ok = new MetricsReporter();
        injectPool(ok);
        ok.start(3600);
        assertTrue(readStarted(ok));
        assertEquals(1, queuedTasks(ok));
        ok.stop();

        MetricsReporter r = new MetricsReporter();
        injectPool(r);
        assertThrows(IllegalArgumentException.class, () -> r.start(0));
        assertTrue(readStarted(r), "started 在 scheduleAtFixedRate 之前就被置 true，异常后没人回滚");
        r.start(3600);
        assertEquals(0, queuedTasks(r), "于是这个实例再也不会真的启动");
    }

    @Test
    @DisplayName("bug_ 所谓『上报』只有日志：类字节里没有任何 java/net 痕迹")
    void bug_reportingHasNoTransport() throws Exception {
        String pool = readClassBytes(MetricsReporter.class);
        // 猎物：字节扫描真的读到了内容（不是空串导致全都『不存在』）
        assertTrue(pool.contains("MetricsCollector"), pool.length() + " bytes");
        assertTrue(pool.contains("snapshot"));
        assertTrue(pool.contains("report"));
        // 实测：没有任何网络 / HTTP / 管理端出口
        assertFalse(pool.contains("java/net"), "常量池里不该出现 java/net");
        assertFalse(pool.contains("Socket"));
        assertFalse(pool.contains("Http"));
        assertFalse(pool.contains("admin"));

        // 结构上也没地方填地址：字段只有 scheduler + started（+ synthetic），且没有网络类型
        Field[] fields = MetricsReporter.class.getDeclaredFields();
        List<String> names = new ArrayList<>();
        for (Field f : fields) {
            names.add(f.getName());
            assertFalse(f.getType().getName().startsWith("java.net"), f.getName() + " 是网络类型");
        }
        assertTrue(names.contains("scheduler"), names.toString());
        assertTrue(names.contains("started"), names.toString());
        // 猎物：start 确实带一个 long 形参，说明唯一的可配项只有周期，没有目标地址
        assertEquals(1, MetricsReporter.class.getDeclaredMethod("start", long.class)
                .getParameterTypes().length);
    }

    @Test
    @DisplayName("bug_ 对外只有 start / stop：快照只能落日志，调用方拿不到")
    void bug_reportIsPrivateAndLogOnly() throws Exception {
        Method report = MetricsReporter.class.getDeclaredMethod("report");
        assertTrue(Modifier.isPrivate(report.getModifiers()));
        assertEquals(void.class, report.getReturnType());
        assertNotNull(MetricsReporter.class.getDeclaredMethod("start", long.class));
        assertNotNull(MetricsReporter.class.getDeclaredMethod("stop"));
        int publicMethods = 0;
        for (Method m : MetricsReporter.class.getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers())) {
                publicMethods++;
            }
        }
        // 猎物就在这一行：public 面确实只有 start / stop 两个
        assertEquals(2, publicMethods);
    }

    private static String readClassBytes(Class<?> c) throws Exception {
        String path = c.getName().replace('.', '/') + ".class";
        InputStream in = c.getClassLoader().getResourceAsStream(path);
        assertNotNull(in, "找不到 " + path);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = in.read(buf)) > 0) {
                bos.write(buf, 0, n);
            }
            return new String(bos.toByteArray(), StandardCharsets.ISO_8859_1);
        } finally {
            in.close();
        }
    }
}
