package com.taotao.cloud.rpc.spring.config;

import com.taotao.cloud.rpc.annotation.TtcRpcReference;
import com.taotao.cloud.rpc.config.ReferenceConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TtcRpcReferenceInjector} 测试。
 * <p>
 * 注入器内部会 new ReferenceConfig().get() 去连远端，所以这里分两类打法：
 * 一是反射直接调 buildReferenceConfig（纯映射逻辑，无网络）；
 * 二是走完整 postProcessPropertyValues，但把远端指向必然拒绝连接的端口，
 * 并用有界线程池兜底，验证"失败被静默吞掉"这条行为。
 */
class TtcRpcReferenceInjectorTest {

    interface Alpha {
        String hello();
    }

    interface Beta {
        String hi();
    }

    /** 属性写满的引用字段，用来证明哪些属性真的被搬进 ReferenceConfig。 */
    static class FullConsumer {
        @TtcRpcReference(interfaceClass = Alpha.class, interfaceName = "custom.Name",
                version = "3.2.1", group = "grpA", timeout = 4321, retries = 5,
                loadbalance = "roundrobin", cluster = "failfast",
                registry = "10.1.2.3:9000", url = "ttcrpc://127.0.0.1:1",
                async = true, oneway = true, check = false, lazy = true,
                connections = 7, client = "nio", serialization = "kryo")
        Alpha alpha;
    }

    static class PlainConsumer {
        @TtcRpcReference(interfaceClass = Beta.class)
        Beta beta;

        String notAnnotated;
    }

    /** 字段声明类型与注解点名的接口**不一致**，用来判接口到底取自哪一边。 */
    static class MismatchedConsumer {
        @TtcRpcReference(interfaceClass = Beta.class, url = "ttcrpc://127.0.0.1:1")
        Alpha alpha;
    }

    /** 与 {@link FullConsumer} 只差在"没有落点"的那五个属性全取默认值 —— 用来做整份 getter 对拍。 */
    static class FiveDefaultsConsumer {
        @TtcRpcReference(interfaceName = "custom.Name",
                version = "3.2.1", group = "grpA", timeout = 4321, retries = 5,
                loadbalance = "roundrobin", cluster = "failfast",
                registry = "10.1.2.3:9000", url = "ttcrpc://127.0.0.1:1",
                async = true, oneway = true)
        Alpha alpha;
    }

    // ---------------- buildReferenceConfig：纯映射 ----------------

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ReferenceConfig build(TtcRpcReference ann, Class iface) throws Exception {
        TtcRpcReferenceInjector injector = new TtcRpcReferenceInjector();
        Method m = TtcRpcReferenceInjector.class.getDeclaredMethod(
                "buildReferenceConfig", TtcRpcReference.class, Class.class);
        m.setAccessible(true);
        return (ReferenceConfig) m.invoke(injector, ann, iface);
    }

    private static TtcRpcReference annOf(Class<?> holder, String fieldName) throws Exception {
        Field f = holder.getDeclaredField(fieldName);
        TtcRpcReference ann = f.getAnnotation(TtcRpcReference.class);
        assertNotNull(ann, holder + "#" + fieldName + " 上应有 @TtcRpcReference");
        return ann;
    }

    @Test
    @DisplayName("buildReferenceConfig 搬运了它认识的那 10 个属性")
    void mappedAttributes() throws Exception {
        TtcRpcReference ann = annOf(FullConsumer.class, "alpha");
        ReferenceConfig cfg = build(ann, Alpha.class);

        assertSame(Alpha.class, cfg.getInterfaceClass());
        assertEquals("custom.Name", cfg.getInterfaceName());
        assertEquals("3.2.1", cfg.getVersion());
        assertEquals("grpA", cfg.getGroup());
        assertEquals(4321, cfg.getTimeout());
        assertEquals(5, cfg.getRetries());
        assertEquals("roundrobin", cfg.getLoadbalance());
        assertEquals("failfast", cfg.getCluster());
        assertEquals("10.1.2.3:9000", cfg.getRegistry());
        assertEquals("ttcrpc://127.0.0.1:1", cfg.getUrl());
    }

    @Test
    @DisplayName("async/oneway 必须随注解属性传到 ReferenceConfig（曾被子静默丢弃）")
    void asyncAndOnewayArePropagated() throws Exception {
        TtcRpcReference ann = annOf(FullConsumer.class, "alpha");
        assertTrue(ann.async(), "prey：注解上确实写了 async = true");
        assertTrue(ann.oneway(), "prey：注解上确实写了 oneway = true");

        ReferenceConfig cfg = build(ann, Alpha.class);
        assertTrue(cfg.isAsync(), "async=true 应搬进 ReferenceConfig");
        assertTrue(cfg.isOneway(), "oneway=true 应搬进 ReferenceConfig");

        // 反向 prey：未声明 async/oneway 的字段不能被误置为 true
        ReferenceConfig plain = build(annOf(PlainConsumer.class, "beta"), Beta.class);
        assertFalse(plain.isAsync(), "prey：默认 async=false 必须仍然是 false");
        assertFalse(plain.isOneway(), "prey：默认 oneway=false 必须仍然是 false");
    }

    @Test
    @DisplayName("interfaceName 缺省时回退到接口的全限定名")
    void interfaceNameFallsBackToClassName() throws Exception {
        TtcRpcReference ann = annOf(PlainConsumer.class, "beta");
        assertEquals("", ann.interfaceName(), "prey：注解没写 interfaceName");
        ReferenceConfig cfg = build(ann, Beta.class);
        assertEquals(Beta.class.getName(), cfg.getInterfaceName());
    }

    @Test
    @DisplayName("url 为空时 ReferenceConfig.getUrl() 保持 null（走注册中心分支）")
    void emptyUrlStaysNull() throws Exception {
        TtcRpcReference ann = annOf(PlainConsumer.class, "beta");
        ReferenceConfig cfg = build(ann, Beta.class);
        assertNull(cfg.getUrl(),
                "url 属性默认 \"\"，注入器用 isEmpty 判空后不 set => 下游只能看到 null");
        assertEquals("127.0.0.1:8084", cfg.getRegistry(), "registry 默认值会被无条件搬下去");
    }

    /**
     * ReferenceConfig 上所有"叶子型"无参 getter 的整份读数。
     * 只收 String/int/long/boolean/Class —— Logger/AtomicBoolean/Invoker/RegistryService/T
     * 这些非叶子返回值不在对拍范围内（它们要么是资源句柄，要么是运行时产物）。
     */
    private static Map<String, String> leafState(ReferenceConfig<?> cfg) throws Exception {
        Map<String, String> out = new TreeMap<String, String>();
        for (Method m : ReferenceConfig.class.getMethods()) {
            if (m.getDeclaringClass() != ReferenceConfig.class) {
                continue;
            }
            if (m.getParameterTypes().length != 0) {
                continue;
            }
            String n = m.getName();
            if (!n.startsWith("get") && !n.startsWith("is")) {
                continue;
            }
            Class<?> r = m.getReturnType();
            if (r != String.class && r != int.class && r != long.class
                    && r != boolean.class && r != Class.class) {
                continue;
            }
            out.put(n, String.valueOf(m.invoke(cfg)));
        }
        return out;
    }

    private static Map<String, String> diffOf(Map<String, String> a, Map<String, String> b) {
        Map<String, String> out = new TreeMap<String, String>();
        assertEquals(a.keySet(), b.keySet(), "两次对拍的 getter 集合不一致，尺子不稳定");
        for (String k : a.keySet()) {
            if (!Objects.equals(a.get(k), b.get(k))) {
                out.put(k, a.get(k) + " != " + b.get(k));
            }
        }
        return out;
    }

    private static final String[] UNLANDED = {"check", "lazy", "connections", "client", "serialization"};

    private static Map<String, String> attrValues(TtcRpcReference ann, String... names) throws Exception {
        Map<String, String> out = new TreeMap<String, String>();
        for (String name : names) {
            out.put(name, String.valueOf(TtcRpcReference.class.getMethod(name).invoke(ann)));
        }
        return out;
    }

    @Test
    @DisplayName("check/lazy/connections/client/serialization 五个属性在整份 ReferenceConfig 里不留任何痕迹")
    void fiveUnlandedAttributesLeaveNoTrace() throws Exception {
        final TtcRpcReference full = annOf(FullConsumer.class, "alpha");
        final TtcRpcReference five = annOf(FiveDefaultsConsumer.class, "alpha");

        // 阳性对照 ①：两份夹具在这 5 个属性上逐个不同，否则"没有痕迹"是空跑。
        Map<String, String> fullAttrs = attrValues(full, UNLANDED);
        Map<String, String> fiveAttrs = attrValues(five, UNLANDED);
        assertEquals(5, fullAttrs.size());
        assertEquals(5, fiveAttrs.size());
        for (String name : UNLANDED) {
            assertNotEquals(fiveAttrs.get(name), fullAttrs.get(name),
                    "夹具 " + name + " 必须与默认值不同，否则对拍检不出东西");
        }

        // 除了这 5 个属性之外，两份注解的其余属性逐字相同（否则下面的零差异说明不了问题）。
        assertEquals(
                attrValues(full, "interfaceName", "version", "group", "timeout", "retries",
                        "loadbalance", "cluster", "registry", "url", "async", "oneway"),
                attrValues(five, "interfaceName", "version", "group", "timeout", "retries",
                        "loadbalance", "cluster", "registry", "url", "async", "oneway"),
                "夹具应当只差那 5 个属性");

        Map<String, String> a = leafState(build(full, Alpha.class));
        Map<String, String> b = leafState(build(five, Alpha.class));
        assertFalse(a.isEmpty(), "prey：必须真的读到了 ReferenceConfig 的叶子状态，实际 0 个 getter");
        assertTrue(a.size() >= 12, "只数出 " + a.size() + " 个叶子 getter，尺子疑似被收窄：" + a.keySet());
        assertEquals(0, diffOf(a, b).size(),
                "5 个无落点属性中有一个渗进了 ReferenceConfig（整份对拍应当零差异）");

        // 阳性对照 ②：换成"有落点"的属性也不同的一份注解，同一把尺子必须数得出差异。
        Map<String, String> landed = diffOf(a, leafState(build(annOf(PlainConsumer.class, "beta"), Alpha.class)));
        assertFalse(landed.isEmpty(),
                "有落点的属性变了却对拍不出差异 => leafState 是常量函数，上面的零差异不可信");
    }

    // ---------------- 完整注入路径：连通性从不校验 ----------------

    /** 在一个独立线程里跑，避免任何一处真挂住把整个 surefire 拖死。 */
    private static Object callWithDeadline(java.util.concurrent.Callable<Object> body)
            throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            Future<Object> f = pool.submit(body);
            try {
                return f.get(20, TimeUnit.SECONDS);
            } catch (java.util.concurrent.ExecutionException e) {
                return e.getCause() != null ? e.getCause() : e;
            } catch (TimeoutException e) {
                f.cancel(true);
                throw new AssertionError("20s 内没返回，疑似死等", e);
            }
        } finally {
            pool.shutdownNow();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Test
    @DisplayName("bug_死地址照样返回代理：check=true 形同虚设，故障推迟到首次调用")
    void bug_deadUrlStillYieldsProxy() throws Exception {
        final TtcRpcReferenceInjector injector = new TtcRpcReferenceInjector();
        injector.setApplicationContext(new GenericApplicationContext());
        final FullConsumer failing = new FullConsumer(); // url = ttcrpc://127.0.0.1:1，必然连不上

        Object injected = callWithDeadline(() -> {
            injector.postProcessProperties(new MutablePropertyValues(), failing, "fullConsumer");
            return null;
        });
        assertNull(injected, "注入过程本身不应返回异常对象: " + injected);

        // 实际行为：字段被塞进了一个看起来可用的代理，而不是 null。
        assertNotNull(failing.alpha,
                "get() 对不可达 Provider 不做任何校验就返回代理 => @TtcRpcReference(check=true) 完全无效");
        assertTrue(java.lang.reflect.Proxy.isProxyClass(failing.alpha.getClass()),
                "应当是 JdkProxyFactory 产出的动态代理，实际: " + failing.alpha.getClass());

        // 故障只在第一次调用时才爆出来 —— 这正是 check 缺失的代价。
        final Alpha proxy = failing.alpha;
        Object outcome = callWithDeadline(() -> {
            try {
                return proxy.hello();
            } catch (Throwable t) {
                return t;
            }
        });
        assertTrue(outcome instanceof Throwable,
                "对死地址的调用本应失败，实际返回: " + outcome);
    }

    @Test
    @DisplayName("bug_@TtcRpcReference.interfaceClass 从不被读：接口只取自字段声明类型")
    void bug_annotationInterfaceClassIsNeverRead() throws Exception {
        final TtcRpcReference ann = annOf(MismatchedConsumer.class, "alpha");
        assertSame(Beta.class, ann.interfaceClass(), "prey：注解点名 Beta");
        assertSame(Alpha.class, MismatchedConsumer.class.getDeclaredField("alpha").getType(),
                "prey：字段声明的却是 Alpha —— 两边不一致才能判接口取自哪一边");

        // 映射层：接口完全由第二个入参决定，注解里那个 interfaceClass 没有参与。
        assertSame(Alpha.class, build(ann, Alpha.class).getInterfaceClass());
        assertSame(Beta.class, build(ann, Beta.class).getInterfaceClass(),
                "换个入参就换个接口 => buildReferenceConfig 没碰 annotation.interfaceClass()");
        // 连派生的 interfaceName 也跟着入参走（注解没写 interfaceName 时）。
        assertEquals(Beta.class.getName(), build(ann, Beta.class).getInterfaceName(),
                "interfaceName 回退用的是入参类型，不是注解点名的接口");

        // 真注入路径：代理只实现字段类型，注解点名的 Beta 在整个产物里不留痕迹。
        TtcRpcReferenceInjector injector = new TtcRpcReferenceInjector();
        GenericApplicationContext ctx = new GenericApplicationContext();
        ctx.refresh();
        try {
            injector.setApplicationContext(ctx);
            final MismatchedConsumer bean = new MismatchedConsumer();
            Object outcome = callWithDeadline(() -> {
                injector.postProcessProperties(new MutablePropertyValues(), bean, "mismatchedConsumer");
                return null;
            });
            assertNull(outcome, "注入过程本身不应返回异常对象: " + outcome);
            assertNotNull(bean.alpha, "字段应当被注入代理");

            Class<?> proxyClass = bean.alpha.getClass();
            assertTrue(java.lang.reflect.Proxy.isProxyClass(proxyClass),
                    "应当是 JdkProxyFactory 产出的动态代理，实际: " + proxyClass);
            assertTrue(Alpha.class.isAssignableFrom(proxyClass),
                    "代理实现的接口来自字段类型");
            assertFalse(Beta.class.isAssignableFrom(proxyClass),
                    "@TtcRpcReference(interfaceClass = Beta.class) 被静默忽略 => 代理不实现 Beta");

            // 登记名同样跟着字段类型走：按 Beta 去容器里取是取不到的。
            assertNotNull(ctx.getBeanFactory().getSingleton(Alpha.class.getName()),
                    "prey：代理按字段类型名登记");
            assertNull(ctx.getBeanFactory().getSingleton(Beta.class.getName()),
                    "注解点名的接口不会成为登记名，谁按 Beta @Autowired 谁失败");
        } finally {
            ctx.close();
        }
    }

    @Test
    @DisplayName("无 @TtcRpcReference 的字段不被触碰")
    void nonAnnotatedFieldsUntouched() throws Exception {
        TtcRpcReferenceInjector injector = new TtcRpcReferenceInjector();
        injector.setApplicationContext(new GenericApplicationContext());

        class Holder {
            String keepMe = "original";
        }
        Holder holder = new Holder();
        PropertyValues out = injector.postProcessProperties(
                new MutablePropertyValues(), holder, "holder");
        assertNotNull(out);
        assertEquals("original", holder.keepMe);
    }

    @Test
    @DisplayName("setBeanFactory 是空实现：不保存容器，因此无法用 BeanFactory 做类型解析")
    void beanFactoryAwareIsNoop() throws Exception {
        TtcRpcReferenceInjector injector = new TtcRpcReferenceInjector();
        injector.setBeanFactory(null);
        // 空实现的证据：类里除了 logger 与 applicationContext 没有别的实例字段
        java.util.List<String> instanceFields = new java.util.ArrayList<String>();
        for (Field f : TtcRpcReferenceInjector.class.getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                instanceFields.add(f.getName());
            }
        }
        assertEquals(java.util.Arrays.asList("applicationContext", "registeredBeanNames"),
                instanceFields, "注入器未持有 BeanFactory，setBeanFactory 形参被丢弃");
        assertNotNull(injector.getClass().getAnnotation(org.springframework.stereotype.Component.class),
                "prey：@Component 在类上，才能被组件扫描登记");
    }

    @Test
    @DisplayName("registerAsBean：Generic 容器登记单例且去重；非 Generic 容器静默不登记")
    void registerAsBeanOnlyOnGenericContext() throws Exception {
        TtcRpcReferenceInjector injector = new TtcRpcReferenceInjector();
        GenericApplicationContext ctx = new GenericApplicationContext();
        ctx.refresh();
        try {
            injector.setApplicationContext(ctx);
            Method m = TtcRpcReferenceInjector.class.getDeclaredMethod(
                    "registerAsBean", Class.class, Object.class);
            m.setAccessible(true);

            Object proxy1 = new Object();
            m.invoke(injector, Alpha.class, proxy1);
            assertSame(proxy1, ctx.getBeanFactory().getSingleton(Alpha.class.getName()));

            // 幂等：第二次同类型必须被 registeredBeanNames 挡掉，不覆盖已有单例
            Object proxy2 = new Object();
            m.invoke(injector, Alpha.class, proxy2);
            assertSame(proxy1, ctx.getBeanFactory().getSingleton(Alpha.class.getName()),
                    "重复注册应被去重挡住，不得覆盖");
        } finally {
            ctx.close();
        }

        // 非 GenericApplicationContext（JDK 动态代理出一个纯 ApplicationContext 视图）
        // => instanceof 分支不成立，代理被静默丢弃，不报错也不登记。
        org.springframework.context.ApplicationContext nonGeneric =
                (org.springframework.context.ApplicationContext) java.lang.reflect.Proxy
                        .newProxyInstance(getClass().getClassLoader(),
                                new Class<?>[]{org.springframework.context.ApplicationContext.class},
                                (proxy, method, args) -> {
                                    if ("toString".equals(method.getName())) {
                                        return "nonGenericCtx";
                                    }
                                    return null;
                                });
        assertFalse(nonGeneric instanceof GenericApplicationContext,
                "prey：这个容器不是 GenericApplicationContext");
        TtcRpcReferenceInjector other = new TtcRpcReferenceInjector();
        other.setApplicationContext(nonGeneric);
        Method m2 = TtcRpcReferenceInjector.class.getDeclaredMethod(
                "registerAsBean", Class.class, Object.class);
        m2.setAccessible(true);
        m2.invoke(other, Beta.class, new Object()); // 不得抛异常
        // 结论：@TtcRpcReference 的代理在非常规容器下不会成为 Spring Bean，
        // 但 injectField 里的 field.set 仍然成功 => 字段有值、容器里查不到，
        // 其它 Bean 想 @Autowired 同一个接口就会失败。
    }

    @Test
    @DisplayName("TtcRpcReferenceInjector 字节码不得引用 Java9 的 Field#canAccess（本模块 source/target=8）")
    void injectorBytecodeStaysWithinJava8Api() throws Exception {
        // 本模块以 maven.compiler.source/target = 8 编译，但 injectField 用了
        // java.lang.reflect.Field#canAccess(Object)（JDK 9 才引入）。
        // javac -source 8 -target 8 会拿 JDK 25 的类库去解析，因此编译通过；
        // 一旦用 --release 8 编译（真正的 JDK 8 API 集），同一个文件直接报 cannot find symbol。
        // 这里用常量池扫描取证，结论与"哪个编译器产出的 class"无关。
        String resource = TtcRpcReferenceInjector.class.getName().replace('.', '/') + ".class";
        java.io.InputStream in = getClass().getClassLoader().getResourceAsStream(resource);
        assertNotNull(in, "找不到 " + resource + "，本测试退化为空跑");
        byte[] bytes;
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = in.read(buf)) > 0) {
                bos.write(buf, 0, n);
            }
            bytes = bos.toByteArray();
        } finally {
            in.close();
        }
        String asLatin1 = new String(bytes, "ISO-8859-1");
        assertFalse(asLatin1.contains("canAccess"),
                "Field#canAccess 是 JDK 9 API，在 source/target=8 的产物里出现即意味着 JDK 8 运行时 NoSuchMethodError");
        assertTrue(asLatin1.contains("setAccessible"),
                "prey：字节码必须确实被读到（setAccessible 是替代写法，同时也是本测试非空跑的证明）");
        assertTrue(asLatin1.contains("java/lang/reflect/Field"),
                "prey：宿主类 Field 必须出现在常量池里");
    }

    @Test
    @DisplayName("注入器实现了 InstantiationAwareBeanPostProcessor（Spring 5.3 已废弃该扩展点）")
    void usesDeprecatedSpringExtensionPoint() {
        assertTrue(org.springframework.beans.factory.config
                        .InstantiationAwareBeanPostProcessor.class
                        .isAssignableFrom(TtcRpcReferenceInjector.class),
                "prey：接口确实被实现");
        // 该接口的 postProcessPropertyValues 在 Spring 5.2 起 @Deprecated、Spring 6 移除，
        // 意味着本注入器无法随 Boot 3 升级 —— 记录为迁移风险。
        Method[] methods = TtcRpcReferenceInjector.class.getDeclaredMethods();
        boolean overridesDeprecated = false;
        for (Method m : methods) {
            if ("postProcessProperties".equals(m.getName())) {
                overridesDeprecated = true;
            }
        }
        assertTrue(overridesDeprecated, "postProcessProperties 未被覆写 => 注入根本不会发生");
    }

    // ================= 注入失败那一支：catch 里只有一行 log.error（第 22 轮，M6 族） =================

    /** 字段声明类型是**类**而不是接口 —— {@code ReferenceConfig.checkConfig()} 必在这里抛。 */
    static class NotAnInterface {
        public String hello() {
            return "local";
        }
    }

    /**
     * 三种字段装在同一种 bean 里：一支必坏（非接口）、一支必坏（坏端口）、一支应当成功。
     * "应当成功"那一支放在同一个 bean 里而不是另开一个类，是为了顺手量另一件事：
     * 字段遍历顺序由 JVM 决定，所以只要它恒为注入成功，就证明**前一次的失败没有把注入器弄成半死状态**。
     */
    static class MixedConsumer {
        @TtcRpcReference(url = "ttcrpc://127.0.0.1:1")
        Alpha good;

        @TtcRpcReference(url = "ttcrpc://127.0.0.1:1")
        NotAnInterface notInterface;

        @TtcRpcReference(url = "ttcrpc://127.0.0.1:notaport")
        Beta badUrl;
    }

    /** 同一个接口两种字段 —— 量的是 {@code registeredBeanNames} 去重在真注入路径上的后果。 */
    static class TwinConsumer {
        @TtcRpcReference(url = "ttcrpc://127.0.0.1:1")
        Alpha first;

        @TtcRpcReference(url = "ttcrpc://127.0.0.1:2")
        Alpha second;
    }

    private static AnnotationConfigApplicationContext startedWith(Class<?>... components) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        Class<?>[] all = new Class<?>[components.length + 1];
        all[0] = TtcRpcReferenceInjector.class;
        System.arraycopy(components, 0, all, 1, components.length);
        ctx.register(all);
        ctx.refresh();
        return ctx;
    }

    @Test
    @DisplayName("bug_注入失败被吞成 null：非接口字段与坏端口字段都静默失败，容器照样 refresh 成功")
    void bug_injectionFailureIsSwallowedIntoANullField() throws Exception {
        final AnnotationConfigApplicationContext[] holder = new AnnotationConfigApplicationContext[1];
        Object refreshOutcome = callWithDeadline(() -> {
            try {
                holder[0] = startedWith(MixedConsumer.class);
                return null;
            } catch (Throwable t) {
                return t;
            }
        });
        assertNull(refreshOutcome, "注入失败不该打断容器启动，实际抛出: " + refreshOutcome);
        AnnotationConfigApplicationContext ctx = holder[0];
        assertNotNull(ctx, "上面那条断言失败时这里没有容器可收尾");
        try {
            MixedConsumer bean = ctx.getBean(MixedConsumer.class);
            // 猎物在场：同一台容器、同一个 bean、同一次字段遍历，好字段必须拿得到代理
            assertNotNull(bean.good, "正向对照不成立 => 注入器压根没跑，下面的 null 判据全是空的");
            assertTrue(java.lang.reflect.Proxy.isProxyClass(bean.good.getClass()),
                    "good 应当是动态代理，实际: " + bean.good.getClass());

            // 而两支必坏的字段：字段是 null，bean 照常交付给业务代码
            assertNull(bean.notInterface,
                    "@TtcRpcReference 打在类（非接口）字段上，注入失败被 catch 吞掉 => 业务代码拿到 null");
            assertNull(bean.badUrl,
                    "url 里的端口写成一个词，注入失败同样被吞掉 => 只有日志里一行 error");
        } finally {
            ctx.close();
        }

        // 那两支吞掉的到底是什么错，得单独量一次：不量就只能猜"大概是 NPE"。
        IllegalStateException notInterface = assertThrows(IllegalStateException.class, () ->
                build(annOf(MixedConsumer.class, "notInterface"), NotAnInterface.class).get());
        assertTrue(notInterface.getMessage().contains("must be an interface"),
                "非接口字段的错因与预期不同，实际: " + notInterface.getMessage());
        RuntimeException badUrl = assertThrows(RuntimeException.class, () ->
                build(annOf(MixedConsumer.class, "badUrl"), Beta.class).get());
        assertTrue(badUrl.getMessage().contains("notaport") || badUrl.getMessage().contains("ttcrpc://"),
                "坏端口的错消息应当带上写坏的那一段或整条 url，实际: " + badUrl.getMessage());
    }

    @Test
    @DisplayName("bug_被吞掉的失败对调用方零可编程痕迹：没有记录入口，pvs 原样返回")
    void bug_swallowedFailureLeavesNoProgrammaticTrace() throws Exception {
        // 结构性守卫：注入器上除 log / applicationContext / registeredBeanNames 之外没有任何字段，
        // 也没有任何 failure / error / last 形状的方法 —— 一旦有，调用方就还能问"刚才有没有没注入成功的字段"。
        Map<String, String> instanceFields = new TreeMap<>();
        for (Field f : TtcRpcReferenceInjector.class.getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                instanceFields.put(f.getName(), f.getType().getSimpleName());
            }
        }
        assertEquals(2, instanceFields.size(), "注入器的实例字段多了：得同时确认新增的那个是不是失败记录 -> " + instanceFields);
        assertTrue(instanceFields.containsKey("applicationContext") && instanceFields.containsKey("registeredBeanNames"),
                "两个实例字段的身份变了，本用例的判据要跟着改 -> " + instanceFields);
        java.util.List<String> reporters = new java.util.ArrayList<>();
        for (Method m : TtcRpcReferenceInjector.class.getDeclaredMethods()) {
            String n = m.getName().toLowerCase();
            if (n.contains("fail") || n.contains("error") || n.contains("last") || n.contains("pending")) {
                reporters.add(m.getName());
            }
        }
        assertEquals(java.util.Collections.emptyList(), reporters,
                "出现了一个能把失败说给调用方听的方法，本用例的结论要翻");

        // 行为面：真的让一次失败发生，然后看它有没有在返回值上留任何痕迹
        TtcRpcReferenceInjector injector = new TtcRpcReferenceInjector();
        injector.setApplicationContext(new GenericApplicationContext());
        final MutablePropertyValues pvs = new MutablePropertyValues();
        final MixedConsumer bean = new MixedConsumer();
        Object outcome = callWithDeadline(() -> {
            try {
                return injector.postProcessProperties(pvs, bean, "mixedConsumer");
            } catch (Throwable t) {
                return t;
            }
        });
        assertTrue(outcome instanceof PropertyValues,
                "postProcessProperties 应当正常返回，实际: " + outcome);
        assertSame(pvs, outcome, "返回值必须是传进去的那一份 pvs：它既不改属性也不带失败标记");
        assertNull(bean.notInterface, "prey：这一次注入确实失败了（否则上面那两句是空跑）");
        assertNotNull(bean.good, "prey：同一个 bean 里有字段注入成功");
    }

    @Test
    @DisplayName("bug_同接口两个字段：字段里是两个不同代理，容器里只有第一个")
    void bug_secondFieldOfSameTypeKeepsAProxyTheContainerNeverSees() throws Exception {
        final AnnotationConfigApplicationContext[] holder = new AnnotationConfigApplicationContext[1];
        Object refreshOutcome = callWithDeadline(() -> {
            try {
                holder[0] = startedWith(TwinConsumer.class);
                return null;
            } catch (Throwable t) {
                return t;
            }
        });
        assertNull(refreshOutcome, "容器不该因为两个同类型字段而启动失败: " + refreshOutcome);
        AnnotationConfigApplicationContext ctx = holder[0];
        try {
            TwinConsumer bean = ctx.getBean(TwinConsumer.class);
            assertNotNull(bean.first, "prey：第一支注入成功");
            assertNotNull(bean.second, "prey：第二支也注入成功（两支各 new 了一个 ReferenceConfig）");
            assertNotSame(bean.first, bean.second,
                    "两个字段是同一次遍历里两次独立 get() 的产物，本应就是两个对象 —— 这条不成立则下面全部退化");

            // registeredBeanNames 按接口全名去重：第二个代理被 add 失败挡在门外
            assertSame(bean.first, ctx.getBeanFactory().getSingleton(Alpha.class.getName()),
                    "登记进容器的只有第一个 => 第二个字段与容器里那个不是同一个对象");
            assertEquals(1, ctx.getBeanNamesForType(Alpha.class).length,
                    "按类型能数出来的 Alpha bean 应当只有一个");
            assertSame(bean.first, ctx.getBean(Alpha.class),
                    "业务代码 @Autowired Alpha 拿到第一个，而它旁边的字段装着第二个");
        } finally {
            ctx.close();
        }
    }
}
