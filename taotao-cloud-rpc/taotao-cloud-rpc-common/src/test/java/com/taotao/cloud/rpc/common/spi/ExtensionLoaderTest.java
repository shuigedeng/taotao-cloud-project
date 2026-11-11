package com.taotao.cloud.rpc.common.spi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ExtensionLoader} —— SPI 微内核的契约面。
 * <p>
 * 夹具接口与资源文件都在本模块 test 目录内，不依赖任何其它 ttc-rpc 模块。
 */
class ExtensionLoaderTest {

    // ====================== getExtensionLoader 的三道前置校验 ======================

    @Test
    @DisplayName("null 接口被拒")
    void nullTypeRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> ExtensionLoader.getExtensionLoader(null));
        assertTrue(e.getMessage().contains("Extension type == null"), e.getMessage());
    }

    @Test
    @DisplayName("非接口被拒：是 class 也不行")
    void nonInterfaceRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> ExtensionLoader.getExtensionLoader(DemoAlpha.class));
        assertTrue(e.getMessage().contains("is not an interface"), e.getMessage());
    }

    @Test
    @DisplayName("接口存在但没打 @SPI 被拒 —— 这正是 Cluster/LoadBalance/Serialization/RegistryService 的死因")
    void missingSpiAnnotationRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> ExtensionLoader.getExtensionLoader(PlainInterface.class));
        assertTrue(e.getMessage().contains("is not annotated with @SPI"), e.getMessage());
        // 猎物：同一个包里打了 @SPI 的接口就能拿到 loader
        assertNotNull(ExtensionLoader.getExtensionLoader(DemoSPI.class));
    }

    @Test
    @DisplayName("loader 按接口全局单例")
    void loaderIsCachedPerType() {
        assertSame(ExtensionLoader.getExtensionLoader(DemoSPI.class),
                ExtensionLoader.getExtensionLoader(DemoSPI.class));
        assertSame(ExtensionLoader.getExtensionLoader(ColdDemoSPI.class),
                ExtensionLoader.getExtensionLoader(ColdDemoSPI.class));
    }

    // ====================== 资源文件解析 ======================

    @Test
    @DisplayName("META-INF/ttc-rpc/<接口全名> 的三个 key 全部加载，注释与空行被跳过")
    void loadsEveryNameFromResourceFile() {
        ExtensionLoader<DemoSPI> loader = ExtensionLoader.getExtensionLoader(DemoSPI.class);
        List<String> names = loader.getExtensionNames();
        names.sort(java.util.Collections.reverseOrder());
        assertEquals(Arrays.asList("gamma", "beta", "alpha"), names, names.toString());
    }

    @Test
    @DisplayName("按 name 取实例，且同名实例被缓存")
    void getExtensionReturnsCachedInstance() {
        ExtensionLoader<DemoSPI> loader = ExtensionLoader.getExtensionLoader(DemoSPI.class);
        DemoSPI alpha = loader.getExtension("alpha");
        assertEquals("alpha", alpha.mark());
        assertSame(alpha, loader.getExtension("alpha"), "同名扩展必须复用实例");
        assertSame("beta", loader.getExtension("beta").mark().intern());
        assertEquals("gamma", loader.getExtension("gamma").mark());
    }

    @Test
    @DisplayName("@SPI 的 value 就是默认扩展名；\"true\" 是解析到默认的别名")
    void defaultNameAndTrueAlias() {
        ExtensionLoader<DemoSPI> loader = ExtensionLoader.getExtensionLoader(DemoSPI.class);
        assertEquals("alpha", loader.getDefaultExtensionName());
        assertEquals("alpha", loader.getDefaultExtension().mark());
        assertSame(loader.getDefaultExtension(), loader.getExtension("true"),
                "\"true\" 必须走 getDefaultExtension()");
    }

    @Test
    @DisplayName("hasExtension：命中为真、未命中为空串一律为假")
    void hasExtensionContract() {
        ExtensionLoader<DemoSPI> loader = ExtensionLoader.getExtensionLoader(DemoSPI.class);
        assertTrue(loader.hasExtension("beta"));
        assertFalseName(loader);
    }

    private static void assertFalseName(ExtensionLoader<DemoSPI> loader) {
        assertTrue(!loader.hasExtension("nope"));
        assertTrue(!loader.hasExtension(""));
        assertTrue(!loader.hasExtension(null));
    }

    // ====================== 失败路径 ======================

    @Test
    @DisplayName("未知 name 抛 IllegalStateException，而不是返回 null")
    void unknownNameThrows() {
        ExtensionLoader<DemoSPI> loader = ExtensionLoader.getExtensionLoader(DemoSPI.class);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> loader.getExtension("nope"));
        assertTrue(e.getMessage().contains("No such extension: nope"), e.getMessage());
    }

    @Test
    @DisplayName("空 name / null name 直接抛 IllegalArgumentException")
    void blankNameThrows() {
        ExtensionLoader<DemoSPI> loader = ExtensionLoader.getExtensionLoader(DemoSPI.class);
        assertThrows(IllegalArgumentException.class, () -> loader.getExtension(""));
        assertThrows(IllegalArgumentException.class, () -> loader.getExtension(null));
    }

    @Test
    @DisplayName("资源文件指向非实现类：IllegalStateException 直接冒泡")
    void notAssignableFailsLoudly() {
        ExtensionLoader<BadAssignSPI> loader = ExtensionLoader.getExtensionLoader(BadAssignSPI.class);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> loader.getExtensionNames());
        assertTrue(e.getMessage().contains("is not assignable to"), e.getMessage());
    }

    @Test
    @DisplayName("资源文件指向不存在的类：包装成 Failed to load extension class")
    void missingClassFailsLoudly() {
        ExtensionLoader<MissingClassSPI> loader = ExtensionLoader.getExtensionLoader(MissingClassSPI.class);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> loader.getExtensionNames());
        assertTrue(e.getMessage().contains("Failed to load extension class"), e.getMessage());
        assertTrue(e.getCause() instanceof ClassNotFoundException, String.valueOf(e.getCause()));
    }

    @Test
    @DisplayName("有 @SPI 但没有资源文件：只有 WARN，names 为空、默认为 null、取任何扩展都会抛")
    void spiWithoutResourceFileIsSilentlyEmpty() {
        ExtensionLoader<NoResourceSPI> loader = ExtensionLoader.getExtensionLoader(NoResourceSPI.class);
        assertTrue(loader.getExtensionNames().isEmpty(), "没有资源文件应当得到空集合");
        assertEquals("", loader.getDefaultExtensionName(),
                "@SPI 不带 value 时默认名是空串而不是 null，getDefaultExtension() 才因此返回 null");
        assertNull(loader.getDefaultExtension());
        assertTrue(!loader.hasExtension("anything"));
        assertThrows(IllegalStateException.class, () -> loader.getExtension("anything"));
    }

    @Test
    @DisplayName("对外暴露的 classes map 是不可修改视图")
    void classesMapIsUnmodifiable() {
        Map<String, Class<?>> map = ExtensionLoader.getExtensionLoader(DemoSPI.class).getExtensionClassesMap();
        assertEquals(3, map.size());
        assertEquals(DemoBeta.class, map.get("beta"));
        assertThrows(UnsupportedOperationException.class, () -> map.put("x", DemoAlpha.class));
    }

    // ====================== @Activate ======================

    @Test
    @DisplayName("冷加载器上 getActivateExtension 就会触发加载并按 order 升序返回")
    void activateExtensionLoadsOnColdLoader() {
        ExtensionLoader<ColdDemoSPI> loader = ExtensionLoader.getExtensionLoader(ColdDemoSPI.class);

        // 此前这里必须先调 getExtensionNames() 才拿得到东西；修好后冷 loader 直接可用
        List<ColdDemoSPI> consumer = loader.getActivateExtension("consumer");
        assertEquals(2, consumer.size(),
                "consumer 组应当直接看见 ColdA + ColdB —— getActivateExtension() 现在自己触发加载");
        assertEquals(Arrays.asList("coldB", "coldA"), marks(consumer),
                "@Activate.order 10 < 20，排序必须按 order 升序");

        List<ColdDemoSPI> provider = loader.getActivateExtension("provider");
        assertEquals(1, provider.size(), "ColdB 声明了 provider 组");
        assertEquals("coldB", provider.get(0).mark());

        assertTrue(loader.getActivateExtension("nobody").isEmpty());
    }

    private static List<String> marks(List<ColdDemoSPI> exts) {
        List<String> out = new java.util.ArrayList<>();
        for (ColdDemoSPI e : exts) out.add(e.mark());
        return out;
    }

    // ====================== 工厂/注解本身 ======================

    @Test
    @DisplayName("@SPI 的 value 默认是空串，且只能贴在类型上、RUNTIME 保留")
    void spiAnnotationShape() throws Exception {
        assertEquals("", SPI.class.getMethod("value").getDefaultValue());
        java.lang.annotation.Target t = SPI.class.getAnnotation(java.lang.annotation.Target.class);
        assertEquals(1, t.value().length);
        assertEquals(java.lang.annotation.ElementType.TYPE, t.value()[0]);
        assertEquals(java.lang.annotation.RetentionPolicy.RUNTIME,
                SPI.class.getAnnotation(java.lang.annotation.Retention.class).value());
        // 猎物：DemoSPI 用自己的 value 覆盖了默认
        assertEquals("alpha", DemoSPI.class.getAnnotation(SPI.class).value());
    }

    @Test
    @DisplayName("@Activate 的三个属性默认值")
    void activateAnnotationShape() throws Exception {
        assertEquals(0, ((String[]) Activate.class.getMethod("group").getDefaultValue()).length);
        assertEquals(0, ((String[]) Activate.class.getMethod("value").getDefaultValue()).length);
        assertEquals(0, (int) Activate.class.getMethod("order").getDefaultValue());
        assertEquals(20, ColdA.class.getAnnotation(Activate.class).order());
        assertEquals(Arrays.asList("consumer", "provider"),
                Arrays.asList(ColdB.class.getAnnotation(Activate.class).group()));
    }

    @Test
    @DisplayName("bug_AdaptiveExtensionFactory 对普通扩展名一律返回 null：连已登记的 alpha 也取不到")
    void bug_adaptiveFactoryCannotResolvePlainSpiNames() {
        ExtensionFactory factory = new AdaptiveExtensionFactory();
        // 猎物：ExtensionLoader 自己完全能解析这个名字
        assertEquals(DemoAlpha.class, ExtensionLoader.getExtensionLoader(DemoSPI.class)
                .getExtension("alpha").getClass());
        // 而工厂包装层只会返回 null —— @Adaptive 类一个都没有，工厂等于空转
        assertNull(factory.getExtension(DemoSPI.class, "alpha"),
                "AdaptiveExtensionFactory 把解析失败降级成 null，调用方无从分辨");
        assertNull(factory.getExtension(PlainInterface.class, "whatever"));
    }

    // ====================== ExtensionFactory 那一层到底有没有被问过（N54） ======================

    @Test
    @DisplayName("bug_ 加载器手里攥着工厂却从不问它：直接问工厂要得到，经过 loader 就要不到")
    void bug_extensionLoaderNeverConsultsItsObjectFactory() throws Exception {
        FakeSpiContext context = new FakeSpiContext().put("factory", new FactoryImpl());
        assertEquals(0, cacheSize(), "前提：这份静态缓存进来时是空的，本用例看见的一切都是它自己造的");
        SpiExtensionFactory.setApplicationContext(context);

        Object fromFactory;
        try {
            // 猎物在场：工厂自己这条路完全能给出"容器里那一个"实例
            fromFactory = new SpiExtensionFactory().getExtension(FactoryDemoSPI.class, "factory");
            assertSame(context.getBean("factory"), fromFactory,
                    "前提不成立的话下面的断言就没有意义 —— 反射 getBean(name) 这条路是通的");

            // 而 ExtensionLoader 的构造函数里就 new 了一个 AdaptiveExtensionFactory（:79），
            // 取扩展时（:120-126）走的是 getExtensionClass + clazz.newInstance()，一个字没问过那个工厂
            FactoryDemoSPI fromLoader = ExtensionLoader.getExtensionLoader(FactoryDemoSPI.class)
                    .getExtension("factory");
            assertSame(FactoryImpl.class, fromLoader.getClass());
            assertNotSame(fromFactory, fromLoader,
                    "同一个名字、同一个容器：loader 给出来的仍是 newInstance 出来的另一个对象 ⇒ objectFactory 从没被读过");
        } finally {
            clearContextCache();
        }
        // 收尾自证：缓存真的空了，不是"以为清了"
        assertEquals(0, cacheSize());
        assertNull(new SpiExtensionFactory().getExtension(FactoryDemoSPI.class, "factory"));
    }

    @Test
    @DisplayName("bug_ 容器缓存按类名做键：同类的第二个容器静默顶掉第一个，且没有任何移除 API")
    void bug_springContextCacheIsKeyedByClassAndCannotBeEvicted() throws Exception {
        FactoryImpl first = new FactoryImpl();
        FactoryImpl second = new FactoryImpl();
        try {
            SpiExtensionFactory.setApplicationContext(new FakeSpiContext().put("factory", first));
            assertEquals(1, cacheSize());
            assertEquals(java.util.Collections.singletonList(FakeSpiContext.class.getName()),
                    new java.util.ArrayList<>(contextCache().keySet()),
                    "键取的是 applicationContext.getClass().getName()，不是实例");

            SpiExtensionFactory.setApplicationContext(new FakeSpiContext().put("factory", second));
            assertEquals(1, cacheSize(), "两个不同的容器实例在这份缓存里是同一个槽");
            assertSame(second, new SpiExtensionFactory().getExtension(FactoryDemoSPI.class, "factory"),
                    "第二个把第一个顶掉了：没有日志、没有异常，第一个容器的 bean 就此不可见");
        } finally {
            clearContextCache();
        }
        // 能往这份静态缓存里写的公开方法只有一个 put 型的 setter，移除侧一个都没有
        List<String> mutators = new java.util.ArrayList<>();
        for (java.lang.reflect.Method m : SpiExtensionFactory.class.getDeclaredMethods()) {
            String n = m.getName().toLowerCase();
            if (n.contains("remove") || n.contains("clear") || n.contains("unset") || n.contains("reset")) {
                mutators.add(m.getName());
            }
        }
        assertEquals(java.util.Collections.emptyList(), mutators,
                "注册进去的容器在这个 JVM 里退不出去（本用例只能反射掏私有字段来还原现场）");
        assertEquals(0, cacheSize());
    }

    private static Map<String, Object> contextCache() throws Exception {
        java.lang.reflect.Field f = SpiExtensionFactory.class.getDeclaredField("CONTEXT_CACHE");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> cache = (Map<String, Object>) f.get(null);
        return cache;
    }

    private static int cacheSize() throws Exception {
        return contextCache().size();
    }

    private static void clearContextCache() throws Exception {
        contextCache().clear();
    }
}
