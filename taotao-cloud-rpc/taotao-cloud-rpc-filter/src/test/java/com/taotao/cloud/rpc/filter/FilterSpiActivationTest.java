package com.taotao.cloud.rpc.filter;

import com.taotao.cloud.rpc.common.spi.Activate;
import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Filter 扩展点是否真的能按 group 装配出来。
 * <p>
 * H6 修复后 {@code getActivateExtension} 冷加载不再空表，这一组把四个内置过滤器按
 * consumer / provider 两侧的激活顺序钉住；同时钉住另一件事：责任链只有『返回一个 List』，
 * 链本身（FilterChain / next）在库里并不存在。
 */
class FilterSpiActivationTest {

    private static final List<String> ALL = Arrays.asList(
            "consumer-trace", "monitor", "provider-context", "provider-monitor");

    private static ExtensionLoader<Filter> loader() {
        return ExtensionLoader.getExtensionLoader(Filter.class);
    }

    private static String simpleName(Filter f) {
        return f.getClass().getSimpleName();
    }

    @Test
    @DisplayName("登记表里的 4 个名字全部可加载")
    void allFourNamesAreLoadable() {
        ExtensionLoader<Filter> l = loader();
        List<String> names = l.getExtensionNames();
        assertTrue(names.containsAll(ALL), names.toString());
        assertEquals(4, names.size());
        for (String n : ALL) {
            assertTrue(l.hasExtension(n), n);
            assertNotNull(l.getExtension(n), n);
        }
    }

    @Test
    @DisplayName("consumer 侧激活顺序是 ConsumerTraceFilter(10) -> MonitorFilter(100)")
    void consumerGroupActivationIsOrdered() {
        List<Filter> filters = loader().getActivateExtension("consumer");
        assertEquals(Arrays.asList("ConsumerTraceFilter", "MonitorFilter"), names(filters));
    }

    @Test
    @DisplayName("provider 侧激活顺序是 ProviderContextFilter(10) -> ProviderMonitorFilter(100)")
    void providerGroupActivationIsOrdered() {
        List<Filter> filters = loader().getActivateExtension("provider");
        assertEquals(Arrays.asList("ProviderContextFilter", "ProviderMonitorFilter"), names(filters));
    }

    private static List<String> names(List<Filter> filters) {
        List<String> out = new ArrayList<>();
        for (Filter f : filters) {
            out.add(simpleName(f));
        }
        return out;
    }

    @Test
    @DisplayName("同时给两个 group：4 个都出来且按 order 升序")
    void bothGroupsMergeAndSort() {
        List<Filter> filters = loader().getActivateExtension("consumer", "provider");
        assertEquals(4, filters.size(), names(filters).toString());
        List<String> got = names(filters);
        // 注意：这里比的是实现类的简单名，不是登记表里的扩展名（那是 ALL）
        for (String n : Arrays.asList("ConsumerTraceFilter", "MonitorFilter",
                "ProviderContextFilter", "ProviderMonitorFilter")) {
            assertTrue(got.contains(n), n + " 缺席：" + got);
        }
        List<Integer> orders = new ArrayList<>();
        for (Filter f : filters) {
            orders.add(f.getClass().getAnnotation(Activate.class).order());
        }
        // 同 group 内的相对次序来自 HashMap.keySet()，不作断言；只钉住排序不变量
        assertEquals(Arrays.asList(10, 10, 100, 100), orders, got.toString());
        // 猎物：单给一个 group 时只会出 2 个
        assertEquals(2, loader().getActivateExtension("consumer").size());
    }

    @Test
    @DisplayName("未知 group 返回空表，而不是抛异常")
    void unknownGroupIsEmptyList() {
        // 猎物：已知 group 非空
        assertFalse(loader().getActivateExtension("consumer").isEmpty());
        assertTrue(loader().getActivateExtension("no-such-group").isEmpty());
        assertTrue(loader().getActivateExtension().isEmpty());
    }

    @Test
    @DisplayName("实例是按名字缓存的单例：同一个名字两次拿到的是同一个对象")
    void extensionInstancesAreCachedPerName() {
        ExtensionLoader<Filter> l = loader();
        assertSame(l.getExtension("monitor"), l.getExtension("monitor"));
        // 猎物：不同名字确实是不同实例
        assertFalse(l.getExtension("monitor") == l.getExtension("consumer-trace"));
    }

    @Test
    @DisplayName("@Activate 的 group / order 在运行时读得到，且两侧不串")
    void activateAnnotationIsReadable() throws Exception {
        assertEquals("consumer", new ConsumerTraceFilter().getClass()
                .getAnnotation(Activate.class).group()[0]);
        assertEquals(10, ProviderContextFilter.class.getAnnotation(Activate.class).order());
        assertEquals(100, MonitorFilter.class.getAnnotation(Activate.class).order());
        assertEquals(100, ProviderMonitorFilter.class.getAnnotation(Activate.class).order());
        // 猎物：group() 返回的是数组，读第 0 位才有意义
        assertEquals(1, ConsumerTraceFilter.class.getAnnotation(Activate.class).group().length);
        assertEquals(10, ConsumerTraceFilter.class.getAnnotation(Activate.class).order());
    }

    @Test
    @DisplayName("bug_ 所谓『责任链』只有一个返回 List 的加载器，库里没有链对象")
    void bug_thereIsNoChainObject() throws Exception {
        // 猎物：Filter 本身在，invoke 也在
        assertNotNull(Class.forName("com.taotao.cloud.rpc.filter.Filter"));
        assertNotNull(Filter.class.getDeclaredMethod("invoke",
                com.taotao.cloud.rpc.common.invoke.Invoker.class, com.taotao.cloud.rpc.common.invoke.Invocation.class));

        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("com.taotao.cloud.rpc.filter.FilterChain"));
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("com.taotao.cloud.rpc.filter.FilterChainBuilder"));
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("com.taotao.cloud.rpc.protocol.ProtocolFilterWrapper"));

        // Filter 接口只有一个方法，没有任何 next / setNext 的挂点
        Method[] ms = Filter.class.getDeclaredMethods();
        assertEquals(1, ms.length);
        assertEquals("invoke", ms[0].getName());

        // 也没有 default 值：@SPI 没写默认实现名，getDefaultExtension() 直接给 null
        assertEquals("", Filter.class.getAnnotation(com.taotao.cloud.rpc.common.spi.SPI.class).value());
        assertNullDefaultExtension();
    }

    private void assertNullDefaultExtension() {
        assertNullSafe(loader().getDefaultExtension());
    }

    private static void assertNullSafe(Filter f) {
        assertEquals(null, f, "Filter 的 @SPI 没有默认名，getDefaultExtension 只能给 null");
    }
}
