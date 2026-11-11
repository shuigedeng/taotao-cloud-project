package com.taotao.cloud.rpc.spring.config;

import com.taotao.cloud.rpc.annotation.TtcRpcService;
import com.taotao.cloud.rpc.remoting.RpcServer;
import com.taotao.cloud.rpc.starter.AnnotationContract;
import com.taotao.cloud.rpc.starter.properties.TtcRpcProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TtcRpcServiceExporter} 行为测试。
 * <p>
 * 用真实但未 start() 的 {@link RpcServer} 当探针：register 只写 serviceMap，
 * 不占端口，因此可以机械读出导出器到底把什么注册进去了。
 */
class TtcRpcServiceExporterTest {

    private RpcServer server;

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    private TtcRpcServiceExporter newExporter(RpcServer rpcServer) throws Exception {
        TtcRpcServiceExporter exporter = new TtcRpcServiceExporter();
        Field f = TtcRpcServiceExporter.class.getDeclaredField("rpcServer");
        f.setAccessible(true);
        f.set(exporter, rpcServer);
        server = rpcServer;
        return exporter;
    }

    private static Map<String, Object> serviceMapOf(RpcServer rpcServer) {
        return rpcServer.getServiceMap();
    }

    // ---------------- fixtures ----------------

    interface Alpha {
        String hello();
    }

    interface Beta {
        String hi();
    }

    @TtcRpcService(version = "2.3.4")
    static class AlphaImpl implements Alpha {
        public String hello() {
            return "alpha";
        }
    }

    /** 故意一次实现两个接口，检验导出器只取 getInterfaces()[0]。 */
    @TtcRpcService
    static class TwoFaceImpl implements Alpha, Beta {
        public String hello() {
            return "hello";
        }

        public String hi() {
            return "hi";
        }
    }

    /** 没有任何接口的 @TtcRpcService 实现。 */
    @TtcRpcService(version = "9.9.9")
    static class NoInterfaceImpl {
        public String ping() {
            return "pong";
        }
    }

    /** 未标注解的普通 Bean。 */
    static class PlainImpl implements Alpha {
        public String hello() {
            return "plain";
        }
    }

    /** 富属性注解，用来证明除 version 外全部被丢弃。 */
    @TtcRpcService(interfaceClass = Alpha.class, version = "7.0.0", group = "g1",
            weight = 42, delay = 1500, timeout = 8888, retries = 7,
            loadbalance = "leastactive", cluster = "failfast", async = true)
    static class RichImpl implements Alpha, Beta {
        public String hello() {
            return "rich";
        }

        public String hi() {
            return "rich-hi";
        }
    }

    // ---------------- tests ----------------

    @Test
    @DisplayName("未标注 @TtcRpcService 的 Bean 原样返回且不注册")
    void nonAnnotatedBeanIsUntouched() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);

        PlainImpl bean = new PlainImpl();
        Object out = exporter.postProcessAfterInitialization(bean, "plainImpl");

        assertSame(bean, out);
        assertTrue(serviceMapOf(rpcServer).isEmpty(),
                "无注解却注册了: " + serviceMapOf(rpcServer).keySet());
    }

    @Test
    @DisplayName("标注 @TtcRpcService 后按 interfaceClass + version 注册，返回同一个 Bean 引用")
    void explicitInterfaceClassRegisters() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);

        AlphaImpl bean = new AlphaImpl();
        Object out = exporter.postProcessAfterInitialization(bean, "alphaImpl");

        assertSame(bean, out, "BeanPostProcessor 必须原样返回 bean");
        Map<String, Object> map = serviceMapOf(rpcServer);
        assertEquals(1, map.size(), "注册表: " + map.keySet());
        // AlphaImpl 声明 version="2.3.4"，@TtcRpcService.version() 默认值是 "1.0.0"，
        // 因此键必须带上版本号，而不是裸接口名。
        assertTrue(map.containsKey(Alpha.class.getName() + ":2.3.4"),
                "key 必须是 {接口全限定名}:{version}，实际: " + map.keySet());
        assertSame(bean, map.get(Alpha.class.getName() + ":2.3.4"));
    }

    @Test
    @DisplayName("同接口不同版本的 Provider 各占一个键，互不覆盖")
    void versionedProvidersAreKeptApart() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);

        // prey：先证明 version 不同、接口相同
        AlphaImpl v1 = new AlphaImpl();          // @TtcRpcService(version = "2.3.4")
        OtherVersionImpl v2 = new OtherVersionImpl(); // @TtcRpcService(version = "5.6.7")，同一 Alpha 接口

        exporter.postProcessAfterInitialization(v1, "alphaV1");
        exporter.postProcessAfterInitialization(v2, "alphaV2");

        Map<String, Object> map = serviceMapOf(rpcServer);
        assertEquals(2, map.size(),
                "两个精确版本 key，不多不少: " + map.keySet());
        assertSame(v1, map.get(Alpha.class.getName() + ":2.3.4"), "v1 被覆盖");
        assertSame(v2, map.get(Alpha.class.getName() + ":5.6.7"), "v2 被覆盖");
        // 关键负向断言（prey 是上面两条同接口注册）：不再写裸接口名别名，
        // 否则"谁先到"决定默认实现，等于把缺陷换了个名字留下。
        assertFalse(map.containsKey(Alpha.class.getName()),
                "带版本注册不得同时占用裸键: " + map.keySet());
    }

    @TtcRpcService(interfaceClass = Alpha.class, version = "5.6.7")
    static class OtherVersionImpl implements Alpha {
        public String hello() {
            return "other-version";
        }
    }

    @Test
    @DisplayName("interfaceClass 缺省时导出全部接口：第二个接口不再被静默丢弃")
    void allImplementedInterfacesAreExported() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);

        exporter.postProcessAfterInitialization(new TwoFaceImpl(), "twoFaceImpl");
        Map<String, Object> map = serviceMapOf(rpcServer);

        // TwoFaceImpl 未写 version，@TtcRpcService.version() 默认 "1.0.0" => 键带默认版本后缀
        assertEquals(2, map.size(),
                "实现两个接口就该导出两个，消费方调 Beta 也要能找到: " + map.keySet());
        assertSameTwoFace(map.get(Alpha.class.getName() + ":1.0.0"), Alpha.class);
        assertSameTwoFace(map.get(Beta.class.getName() + ":1.0.0"), Beta.class);
    }

    private static void assertSameTwoFace(Object exported, Class<?> iface) {
        assertNotNull(exported, "接口 " + iface + " 没被导出");
        assertTrue(iface.isInstance(exported), iface + " 实例校验失败: " + exported.getClass());
    }

    @Test
    @DisplayName("无任何接口的 @TtcRpcService 被跳过（只 warn，不报错）")
    void beanWithoutInterfacesIsSkipped() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);

        NoInterfaceImpl bean = new NoInterfaceImpl();
        Object out = exporter.postProcessAfterInitialization(bean, "noInterfaceImpl");

        assertSame(bean, out);
        assertTrue(serviceMapOf(rpcServer).isEmpty(),
                "无接口的服务被静默跳过，用户得不到任何启动期失败 => " + serviceMapOf(rpcServer));
    }

    @Test
    @DisplayName("CGLIB 代理过的 @TtcRpcService Bean 照常导出，且导出的是代理本身")
    void cglibProxiedBeanIsExported() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);

        ProxyFactory pf = new ProxyFactory(new AlphaImpl());
        pf.setProxyTargetClass(true);
        Object proxy = pf.getProxy();

        assertNull(proxy.getClass().getAnnotation(TtcRpcService.class),
                "prey：CGLIB 子类的裸 getAnnotation 确实读不到 —— 所以导出器必须走 getUserClass");
        exporter.postProcessAfterInitialization(proxy, "alphaProxy");

        Map<String, Object> map = serviceMapOf(rpcServer);
        assertEquals(1, map.size(), "代理 Bean 也必须导出: " + map.keySet());
        assertSame(proxy, map.get(Alpha.class.getName() + ":2.3.4"),
                "注册的必须是代理（这样 @Transactional/@Async 才继续生效），而不是目标对象");
    }

    @Test
    @DisplayName("bug_除 version 外的注解属性全部无落点：weight/timeout/retries/loadbalance/cluster/delay/async/group/interfaceName")
    void bug_mostAnnotationAttributesHaveNoEffect() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);

        RichImpl bean = new RichImpl();
        exporter.postProcessAfterInitialization(bean, "richImpl");
        assertEquals(1, serviceMapOf(rpcServer).size(), "有 prey：注册确实发生了");

        // 机械证明：RpcServer 上所有 register* 入口的参数类型里没有任何数值位，
        // 所以 weight / timeout / retries / delay / threads 在类型层面就无处可去。
        int registerOverloads = 0;
        for (java.lang.reflect.Method m : RpcServer.class.getDeclaredMethods()) {
            if (!m.getName().startsWith("register")) {
                continue;
            }
            registerOverloads++;
            for (Class<?> pt : m.getParameterTypes()) {
                assertFalse(pt == int.class || pt == long.class || Number.class.isAssignableFrom(pt),
                        "register* 出现了数值参数，属性落点结论需要重估: " + m);
            }
        }
        assertEquals(3, registerOverloads,
                "RpcServer 的 register* 重载数量发生变化，请重新核对属性落点: "
                        + java.util.Arrays.toString(RpcServer.class.getDeclaredMethods()));
    }

    @Test
    @DisplayName("rpcServer 尚未注入时先入队，ContextRefreshedEvent 时补注册")
    void pendingRegistrationsFlushOnContextRefreshed() throws Exception {
        TtcRpcServiceExporter pre = new TtcRpcServiceExporter();
        AlphaImpl bean = new AlphaImpl();
        pre.postProcessAfterInitialization(bean, "alphaImpl");

        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);
        // 同一个 bean 走"rpcServer 为 null -> 入队 -> 事件回调"的完整路径
        Field f = TtcRpcServiceExporter.class.getDeclaredField("rpcServer");
        f.setAccessible(true);
        f.set(exporter, null);
        exporter.postProcessAfterInitialization(bean, "alphaImpl");
        assertTrue(serviceMapOf(rpcServer).isEmpty(), "rpcServer 为 null 时不应注册");

        f.set(exporter, rpcServer);
        exporter.onContextRefreshed();
        assertEquals(1, serviceMapOf(rpcServer).size(), "事件回调后应补注册");

        exporter.onContextRefreshed();
        assertEquals(1, serviceMapOf(rpcServer).size(), "队列必须清空，重复事件不得重复注册");
        assertNotNull(pre);
    }

    @Test
    @DisplayName("rpcServer 始终为 null 时：队列必须被排空，且不会因后来出现的服务端复活")
    void pendingQueueIsDrainedWhenNoServer() throws Exception {
        TtcRpcServiceExporter exporter = new TtcRpcServiceExporter();
        exporter.postProcessAfterInitialization(new AlphaImpl(), "alphaImpl");

        List<?> armed = pendingListOf(exporter);
        assertFalse(armed.isEmpty(),
                "猎物不在场 => 这条测试是空跑，请先确认 postProcess 在 rpcServer 为 null 时真的入队了");
        assertEquals(1, armed.size(), "Alpha 只有一个接口，应当只有一条待注册任务");

        exporter.onContextRefreshed();
        assertTrue(pendingListOf(exporter).isEmpty(),
                "上下文刷新完之后队列还留着任务：那些 Runnable 各自强引用一个业务 bean，而它们已经不可能执行");

        // 排空是一次性的：事后补一台服务端，也不该把已经判定丢弃的注册复活
        RpcServer late = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        try {
            Field f = TtcRpcServiceExporter.class.getDeclaredField("rpcServer");
            f.setAccessible(true);
            f.set(exporter, late);
            exporter.onContextRefreshed();
            assertTrue(serviceMapOf(late).isEmpty(),
                    "没有服务端时做出的『丢弃』判定不得被第二次事件悄悄撤销: " + serviceMapOf(late).keySet());
        } finally {
            late.stop();
        }
    }

    private List<?> pendingListOf(TtcRpcServiceExporter exporter) throws Exception {
        Field f = TtcRpcServiceExporter.class.getDeclaredField("pendingRegistrations");
        f.setAccessible(true);
        return (List<?>) f.get(exporter);
    }

    private static Object serverFieldOf(TtcRpcServiceExporter exporter) throws Exception {
        Field f = TtcRpcServiceExporter.class.getDeclaredField("rpcServer");
        f.setAccessible(true);
        return f.get(exporter);
    }

    @Test
    @DisplayName("真容器里只装导出器、不带服务端装配：null 分支确实到得了，且刷完不复存在")
    void serverlessContainerTakesTheQueueBranch() throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(TtcRpcServiceExporter.class, AlphaImpl.class);
        ctx.refresh();
        try {
            assertEquals(0, ctx.getBeanNamesForType(RpcServer.class).length,
                    "这一支的前提是容器里真的没有 RpcServer bean");
            TtcRpcServiceExporter exporter = ctx.getBean(TtcRpcServiceExporter.class);
            assertNull(serverFieldOf(exporter),
                    "容器里没有服务端时 @Autowired(required = false) 给出 null —— 这就是那条分支可达的证据");

            // 阳性对照：容器自己那趟 refresh 已经把 AlphaImpl 过了一遍导出器（队列入队后排空），
            // 但"排空后为空"这条断言单独看分不清"没进来过"和"进来又被丢掉"，所以这里再手工喂一次，
            // 证明同一个容器里的 bean 在同一条 null 状态下确实会入队。
            exporter.postProcessAfterInitialization(ctx.getBean(AlphaImpl.class), "alphaImpl");
            assertFalse(pendingListOf(exporter).isEmpty(),
                    "真容器的 bean 在这条分支上应当入队；没入队就说明这条测试在空跑");
            exporter.onContextRefreshed();
            assertTrue(pendingListOf(exporter).isEmpty(),
                    "排空断言：没有服务端时队列不能留着东西");
        } finally {
            ctx.close();
        }
    }

    @Test
    @DisplayName("server.enabled=false：BeanPostProcessor 仍把服务注册进一台从不监听的 server，日志写着 exported")
    void exportIntoAnUnstartedServerLooksLikeSuccess() {
        TtcRpcProperties props = new TtcRpcProperties();
        props.getServer().setEnabled(false);
        props.getServer().setPort(0);

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.addBeanFactoryPostProcessor(bf -> bf.registerSingleton("ttcrpcProperties", props));
        ctx.register(TtcRpcServerAutoConfiguration.class, TtcRpcServiceExporter.class, AlphaImpl.class);
        ctx.refresh();
        try {
            RpcServer bean = ctx.getBean(RpcServer.class);
            assertFalse(bean.isStarted(), "server.enabled=false 却不该监听");
            Map<String, Object> exported = serviceMapOf(bean);
            // 这条不是"预期如此"的断言，而是把实测形状钉住：修复它的与否是 §8 的决策项（N48）。
            assertEquals(1, exported.size(),
                    "实测形状：导出器把 AlphaImpl 注册进了一台 isStarted()=false 的服务端，"
                            + "键 " + exported.keySet() + "，而它打的日志是 exported（静默失败）");
        } finally {
            ctx.close();
        }
    }

    @Test
    @DisplayName("register 抛异常时只 log.error，Bean 照常返回（导出失败对上层完全静默）")
    void registerFailureIsSwallowed() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);
        // 用 null 接口触发 register 内部 NPE/IllegalArgumentException
        Object out = null;
        try {
            out = exporter.postProcessAfterInitialization(new NoInterfaceHolder(), "boom");
        } catch (RuntimeException e) {
            throw new AssertionError("导出异常不得冒泡到容器", e);
        }
        assertNotNull(out);
        assertFalse(rpcServer.isStarted(), "导出器不该顺带把 server 启动起来");
    }

    /**
     * 注释原先写"会让 register 失败的 fixture"——第 24 轮实测证伪：{@code register(Runnable.class, bean, "1")}
     * 走的是 {@code serviceMap.put("java.lang.Runnable:1", bean)}，成功返回。
     * 保留它是因为 {@link #deadGuardShowingTheOldSwallowTestNeverEntersTheCatch} 要拿它当猎物。
     */
    @TtcRpcService(interfaceClass = Runnable.class, version = "1")
    static class NoInterfaceHolder implements Runnable {
        public void run() {
        }
    }

    // -------------------------------------------------- 第 24 轮：那支 catch 的入场券

    /**
     * 一台"每次注册都当场失败"的服务端。导出器只调 {@code register(Class, Object, String)} 这一个重载
     * （三个 register* 入口的数量由 {@link #bug_mostAnnotationAttributesHaveNoEffect} 钉着），
     * 打掉它就足以让 {@code export()} 里那支 {@code catch (Exception)} 真的收到东西。
     */
    static class RejectingRpcServer extends RpcServer {
        private final java.util.concurrent.atomic.AtomicInteger attempts =
                new java.util.concurrent.atomic.AtomicInteger();

        RejectingRpcServer() {
            super("127.0.0.1", 0);
        }

        @Override
        public void register(Class<?> serviceInterface, Object serviceImpl, String version) {
            attempts.incrementAndGet();
            throw new IllegalStateException("r24 猎物：服务端拒收这次注册");
        }

        int attempts() {
            return attempts.get();
        }
    }

    @Test
    @DisplayName("bug_真容器里注册失败 = 容器全绿、业务 bean 照在、serviceMap 一条没有")
    void bug_registrationFailureKeepsTheContainerGreenAndExportsNothing() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(RejectingRpcServer.class, TtcRpcServiceExporter.class, AlphaImpl.class);
        Throwable failure = null;
        try {
            ctx.refresh();
        } catch (Throwable t) {
            failure = t;
        }
        try {
            assertNull(failure,
                    "导出失败被 :71 那支 catch 吞掉 ⇒ refresh 不该因为它停下；现在它停了，形状变了");
            RejectingRpcServer bean = ctx.getBean(RejectingRpcServer.class);
            assertEquals(1, bean.attempts(),
                    "前提腿：AlphaImpl 只有一个接口，容器这趟 refresh 必须真的一次次调到 register —— "
                            + "一次都没调等于这条在空跑");
            assertTrue(bean.getServiceMap().isEmpty(),
                    "register 当场抛了，serviceMap 里却什么都没留下 —— 服务端一侧没有任何『导出失败』的记账: "
                            + bean.getServiceMap().keySet());
            assertNotNull(ctx.getBean(AlphaImpl.class),
                    "业务 bean 照常存活：它只是个普通 Spring bean，没人告诉过它自己不是 RPC 服务");
            assertFalse(bean.isStarted(), "导出器不负责启动服务端");
        } finally {
            try {
                ctx.close();
            } catch (Throwable ignored) {
                // refresh 没走完时 close 只做能做的清理
            }
        }
    }

    @Test
    @DisplayName("bug_队列排空时注册失败 = 事件回调不冒泡，队列照排空，失败不重试也不记账")
    void bug_pendingFlushFailureNeitherEscapesNorKeepsTheQueue() throws Exception {
        TtcRpcServiceExporter exporter = new TtcRpcServiceExporter();
        exporter.postProcessAfterInitialization(new AlphaImpl(), "alphaImpl");
        exporter.postProcessAfterInitialization(new TwoFaceImpl(), "twoFaceImpl");
        assertEquals(3, pendingListOf(exporter).size(),
                "前提腿：Alpha 1 个接口 + TwoFace 2 个接口，此刻应当排着 3 条待注册任务");

        RejectingRpcServer server = new RejectingRpcServer();
        Field f = TtcRpcServiceExporter.class.getDeclaredField("rpcServer");
        f.setAccessible(true);
        f.set(exporter, server);
        try {
            exporter.onContextRefreshed();
        } catch (RuntimeException e) {
            throw new AssertionError(
                    "ContextRefreshedEvent 上的异常会当场刷死整个容器，:71 那支 catch 正是拦它的: " + e, e);
        }
        assertEquals(3, server.attempts(), "三条任务都到了 register，却没有一条成功");
        assertTrue(server.getServiceMap().isEmpty(),
                "服务端一条服务都没得: " + server.getServiceMap().keySet());
        assertTrue(pendingListOf(exporter).isEmpty(),
                "失败的注册既不重试也不留在队列里 —— 排空发生在 clear() 而不是成功之后");
    }

    @Test
    @DisplayName("旧那条 registerFailureIsSwallowed 从不进 catch：NoInterfaceHolder 其实注册成功")
    void deadGuardShowingTheOldSwallowTestNeverEntersTheCatch() throws Exception {
        RpcServer rpcServer = new RpcServer("127.0.0.1", AnnotationContract.freePort());
        TtcRpcServiceExporter exporter = newExporter(rpcServer);

        exporter.postProcessAfterInitialization(new NoInterfaceHolder(), "boom");

        Map<String, Object> map = serviceMapOf(rpcServer);
        assertEquals(1, map.size(),
                "实测：这个 fixture 让 register 走通了 ⇒ 名为 registerFailureIsSwallowed 的那条用例"
                        + "从来没进过 :71 的 catch（它的断言只证明了一次正常导出）: " + map.keySet());
        assertTrue(map.containsKey(Runnable.class.getName() + ":1"),
                "键就是普通的 {接口}:{版本}，没有任何异常发生: " + map.keySet());
    }

    /** 一台"数得清被调了几次"的服务端：register 先记账再照常走 super，所以 serviceMap 是真的。 */
    static class CountingRpcServer extends RpcServer {
        private final java.util.concurrent.atomic.AtomicInteger registers =
                new java.util.concurrent.atomic.AtomicInteger();

        CountingRpcServer() {
            super("127.0.0.1", 0);
        }

        @Override
        public void register(Class<?> serviceInterface, Object serviceImpl, String version) {
            registers.incrementAndGet();
            super.register(serviceInterface, serviceImpl, version);
        }

        int registers() {
            return registers.get();
        }
    }

    @Test
    @DisplayName("待注册任务每条只跑一次：第二次事件不得重跑（老用例判的是 map size，同键同值重复 put 它看不见）")
    void flushedTasksRunExactlyOnceAcrossRepeatedEvents() throws Exception {
        TtcRpcServiceExporter exporter = new TtcRpcServiceExporter();
        exporter.postProcessAfterInitialization(new AlphaImpl(), "alphaImpl");
        exporter.postProcessAfterInitialization(new TwoFaceImpl(), "twoFaceImpl");

        CountingRpcServer server = new CountingRpcServer();
        Field f = TtcRpcServiceExporter.class.getDeclaredField("rpcServer");
        f.setAccessible(true);
        f.set(exporter, server);

        exporter.onContextRefreshed();
        assertEquals(3, server.registers(), "第一次事件应当把三条待注册任务各跑一次");
        assertEquals(3, server.getServiceMap().size(),
                "prey：三条任务各占一个键，所以 map size 此刻确实是 3: " + server.getServiceMap().keySet());

        exporter.onContextRefreshed();
        assertEquals(3, server.registers(),
                "第二次事件把老任务又跑了一遍 —— pendingRegistrationsFlushOnContextRefreshed 判的是 map size，"
                        + "而同键同值重复 put 不改变 size，所以它结构上看不到这件事（第 24 轮注入 m24-02 实测：它不红）");
        assertTrue(pendingListOf(exporter).isEmpty(), "队列此刻应当是空的");
    }
}
