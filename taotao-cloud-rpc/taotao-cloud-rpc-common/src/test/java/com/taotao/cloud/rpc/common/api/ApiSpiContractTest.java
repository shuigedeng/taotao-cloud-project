package com.taotao.cloud.rpc.common.api;

import com.taotao.cloud.rpc.common.invoke.Listener;
import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import com.taotao.cloud.rpc.common.spi.SPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * api 模块的扩展点契约：哪三个接口是 SPI、默认名与元文件是否对得上、
 * 哪些"看起来能挂东西"的接口其实没接线。
 * <p>
 * 这一层看不到 cluster/filter/protocol 模块的实现（它们不在 ttc-rpc-api 的测试
 * classpath 上），所以"某个扩展名在本模块解析不到"是刻意的证据，不是遗漏。
 */
class ApiSpiContractTest {

    private static final String META = "/META-INF/ttc-rpc/";

    @Test
    @DisplayName("三个扩展点的 @SPI 默认名与 javadoc 一致")
    void spiAnnotationsDeclareTheirDefaults() {
        assertEquals("jdk", ProxyFactory.class.getAnnotation(SPI.class).value());
        assertEquals("ttc-rpc", Protocol.class.getAnnotation(SPI.class).value());
        assertEquals("", com.taotao.cloud.rpc.common.filter.Filter.class.getAnnotation(SPI.class).value(),
                "Filter 是列表型扩展点，没有默认实现");
        // 猎物：三者都能被 ExtensionLoader 接受（C2 修的就是这个）
        assertNotNull(ExtensionLoader.getExtensionLoader(ProxyFactory.class));
        assertNotNull(ExtensionLoader.getExtensionLoader(Protocol.class));
        assertNotNull(ExtensionLoader.getExtensionLoader(com.taotao.cloud.rpc.common.filter.Filter.class));
    }

    @Test
    @DisplayName("核心数据接口不该是扩展点：@SPI 缺席是设计而非疏漏")
    void dataInterfacesAreNotExtensionPoints() {
        assertNull(com.taotao.cloud.rpc.common.invoke.Invocation.class.getAnnotation(SPI.class));
        assertNull(com.taotao.cloud.rpc.common.invoke.Invoker.class.getAnnotation(SPI.class));
        assertNull(com.taotao.cloud.rpc.common.invoke.Result.class.getAnnotation(SPI.class));
        assertNull(Exporter.class.getAnnotation(SPI.class));
        for (final Class<?> type : new Class<?>[]{com.taotao.cloud.rpc.common.invoke.Invocation.class,
                com.taotao.cloud.rpc.common.invoke.Invoker.class, com.taotao.cloud.rpc.common.invoke.Result.class,
                Exporter.class}) {
            assertThrows(IllegalArgumentException.class,
                    () -> ExtensionLoader.getExtensionLoader(type),
                    type + " 加了 @SPI 就会让上面的断言和这条一起变红");
        }
    }

    @Test
    @DisplayName("ProxyFactory 的元文件就在本模块：jdk 这个名字解析得到且是同一个实例")
    void proxyFactoryIsFullySelfContained() {
        assertNotNull(ApiSpiContractTest.class.getResource(META + ProxyFactory.class.getName()),
                "元文件缺失会让 jdk 这个名字解析不到");
        ExtensionLoader<ProxyFactory> loader = ExtensionLoader.getExtensionLoader(ProxyFactory.class);
        assertTrue(loader.hasExtension("jdk"), loader.getExtensionNames().toString());
        assertSame(loader.getDefaultExtension(), loader.getExtension("jdk"));
        assertFalse(loader.hasExtension("cglib"), "本模块只登记了 jdk: " + loader.getExtensionNames());
    }

    @Test
    @DisplayName("Protocol 的实现在别的模块：本模块 classpath 上 ttc-rpc 这个名字解析不到")
    void protocolImplementationLivesElsewhere() {
        assertNull(ApiSpiContractTest.class.getResource(META + Protocol.class.getName()),
                "prey：Protocol 的元文件不在 ttc-rpc-api 里");
        ExtensionLoader<Protocol> loader = ExtensionLoader.getExtensionLoader(Protocol.class);
        assertFalse(loader.hasExtension("ttc-rpc"));
        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> loader.getExtension("ttc-rpc"));
        assertTrue(thrown.getMessage().contains("No such extension"), thrown.getMessage());
        // 默认名指向一个本模块无法解析的扩展，所以 getDefaultExtension 同样炸
        assertThrows(IllegalStateException.class, () -> loader.getDefaultExtension());
    }

    @Test
    @DisplayName("bug_Listener 是彻底的空接口：既不是扩展点，也没有任何调用方")
    void bug_listenerIsNotWiredAnywhere() {
        assertNull(Listener.class.getAnnotation(SPI.class));
        assertNull(ApiSpiContractTest.class.getResource(META + Listener.class.getName()));
        assertThrows(IllegalArgumentException.class,
                () -> ExtensionLoader.getExtensionLoader(Listener.class));
        // 猎物：同一个包里声明的 Filter 是接线完整的扩展点
        assertNotNull(com.taotao.cloud.rpc.common.filter.Filter.class.getAnnotation(SPI.class));
        assertNotNull(ExtensionLoader.getExtensionLoader(com.taotao.cloud.rpc.common.filter.Filter.class));
        // Listener.DEFAULT 存在，但没有任何 api 之外的代码能把它交给谁
        assertNotNull(Listener.DEFAULT);
        assertEquals(3, Listener.class.getDeclaredMethods().length,
                "exported/referred/destroyed 三个钩子，一个都没人调用: "
                        + java.util.Arrays.toString(Listener.class.getDeclaredMethods()));
    }

    @Test
    @DisplayName("Filter 没有默认实现，冷加载在本模块拿不到任何扩展名")
    void filterHasNoDefaultAndNoNamesHere() {
        ExtensionLoader<com.taotao.cloud.rpc.common.filter.Filter> loader =
                ExtensionLoader.getExtensionLoader(com.taotao.cloud.rpc.common.filter.Filter.class);
        assertEquals("", loader.getDefaultExtensionName());
        assertNull(loader.getDefaultExtension(), "默认名为空时 getDefaultExtension 返回 null 而不是抛错");
        assertTrue(loader.getExtensionNames().isEmpty(),
                "prey：Filter 的元文件在 ttc-rpc-filter 模块，本模块看不到 monitor/consumer-trace: "
                        + loader.getExtensionNames());
        assertThrows(IllegalArgumentException.class, () -> loader.getExtension(""));
    }

    @Test
    @DisplayName("扩展名大小写敏感：JDK 不会替你把 jdk 认成 JDK")
    void extensionNamesAreCaseSensitive() {
        ExtensionLoader<ProxyFactory> loader = ExtensionLoader.getExtensionLoader(ProxyFactory.class);
        assertFalse(loader.hasExtension("JDK"));
        assertThrows(IllegalStateException.class, () -> loader.getExtension("JDK"));
        // 猎物：小写命中
        assertNotNull(loader.getExtension("jdk"));
    }
}
