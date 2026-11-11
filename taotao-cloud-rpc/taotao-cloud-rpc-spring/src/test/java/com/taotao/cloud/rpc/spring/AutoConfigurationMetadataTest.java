package com.taotao.cloud.rpc.spring;

import com.taotao.cloud.rpc.starter.annotation.EnableTtcRpc;
import com.taotao.cloud.rpc.starter.config.*;
import com.taotao.cloud.rpc.starter.properties.TtcRpcProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 自动装配注册元数据的一致性检查。
 * <p>
 * 这一组测试刻意不启动 Spring 容器，只读资源文件本身：
 * starter 的价值全在"被 Boot 发现"，所以发现链路必须钉住。
 */
class AutoConfigurationMetadataTest {

    private static List<String> readClasspathLines(String path) throws Exception {
        ClassPathResource res = new ClassPathResource(path);
        List<String> lines = new ArrayList<String>();
        if (!res.exists()) {
            return lines;
        }
        InputStream in = res.getInputStream();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                    lines.add(trimmed);
                }
            }
        } finally {
            in.close();
        }
        return lines;
    }

    @Test
    @DisplayName("AutoConfiguration.imports 必须列出 TtcRpcAutoConfiguration")
    void autoConfigurationImportsFile() throws Exception {
        List<String> lines = readClasspathLines(
                "META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports");
        assertFalse(lines.isEmpty(), "AutoConfiguration.imports 为空 => Boot 2.7+ 发现不了自动装配");
        assertTrue(lines.contains(TtcRpcAutoConfiguration.class.getName()),
                "imports 文件内容: " + lines);
    }

    @Test
    @DisplayName("spring.factories 不得再携带 EnableAutoConfiguration 键（2026-10-30 SB3 火车：注册只走 AutoConfiguration.imports）")
    void springFactoriesCarriesTheAutoConfigurationKey() throws Exception {
        List<String> lines;
        try {
            lines = readClasspathLines("META-INF/spring.factories");
        } catch (Exception e) {
            return; // 文件不存在 = 最好：注册已全量迁到 imports
        }
        for (String line : lines) {
            assertFalse(line.startsWith("org.springframework.boot.autoconfigure.EnableAutoConfiguration"),
                    "spring.factories 里不应再有 EnableAutoConfiguration 键（SB3 不认），内容 = " + lines);
        }
    }

    @Test
    @DisplayName("@EnableTtcRpc 必须 @Import 装配类，否则它是空壳标记")
    void enableTtcRpcImportsTheAutoConfiguration() {
        Import imp = EnableTtcRpc.class.getAnnotation(Import.class);
        assertNotNull(imp,
                "@EnableTtcRpc 的 javadoc 声称\"激活 ttc-RPC 自动装配\"，类上必须有 @Import/@ImportSelector/@ImportBeanDefinitionRegistrar");
        Set<Class<?>> imported = new LinkedHashSet<Class<?>>(Arrays.asList(imp.value()));
        assertTrue(imported.contains(TtcRpcAutoConfiguration.class),
                "@EnableTtcRpc 应导入 TtcRpcAutoConfiguration: " + imported);
        assertTrue(imported.contains(com.taotao.cloud.rpc.starter.config.TtcRpcReferenceInjector.class),
                "prey：@EnableTtcRpc 还应带上 ReferenceInjector，让 @TtcRpcReference 有处理者: " + imported);
    }

    @Test
    @DisplayName("EnableTtcRpc 只声明 scanBasePackages，且框架内没有任何代码读它")
    void enableTtcRpcAttributeSurface() {
        assertEquals(1, EnableTtcRpc.class.getDeclaredMethods().length,
                "EnableTtcRpc 属性: " + Arrays.toString(EnableTtcRpc.class.getDeclaredMethods()));
        assertEquals("scanBasePackages", EnableTtcRpc.class.getDeclaredMethods()[0].getName(),
                "javadoc 承诺扫描 @TtcRpcService/@TtcRpcReference，但该属性无人消费");
    }

    @Test
    @DisplayName("TtcRpcAutoConfiguration 上的 @Import 覆盖三个子装配 + Exporter")
    void autoConfigurationImportsSiblings() {
        Import imp = TtcRpcAutoConfiguration.class.getAnnotation(Import.class);
        assertNotNull(imp);
        Set<Class<?>> imported = new LinkedHashSet<Class<?>>(Arrays.asList(imp.value()));
        assertTrue(imported.contains(TtcRpcServerAutoConfiguration.class), "缺 Server 装配: " + imported);
        assertTrue(imported.contains(TtcRpcConsumerAutoConfiguration.class), "缺 Consumer 装配: " + imported);
        assertTrue(imported.contains(TtcRpcRegistryAutoConfiguration.class), "缺 Registry 装配: " + imported);
        assertTrue(imported.contains(TtcRpcServiceExporter.class), "缺 ServiceExporter: " + imported);
        assertTrue(imported.contains(com.taotao.cloud.rpc.starter.config.TtcRpcReferenceInjector.class),
                "ReferenceInjector 必须被 @Import，否则 @TtcRpcReference 永远不被注入: " + imported);
    }

    @Test
    @DisplayName("ttc.rpc.enabled=false 应关闭整套自动装配（matchIfMissing=true）")
    void conditionalOnPropertyContract() {
        ConditionalOnProperty cond =
                TtcRpcAutoConfiguration.class.getAnnotation(ConditionalOnProperty.class);
        assertNotNull(cond);
        assertEquals("ttc.rpc", cond.prefix());
        assertEquals(1, cond.name().length);
        assertEquals("enabled", cond.name()[0]);
        assertTrue(cond.matchIfMissing(), "缺省必须开启，否则 starter 用起来要显式配 enabled=true");
    }

    @Test
    @DisplayName("Server/Registry 子装配上没有任何 @ConditionalOnProperty —— server.enabled=false 只是运行期判断")
    void siblingConfigsAreUnconditional() {
        assertNullish(TtcRpcServerAutoConfiguration.class.getAnnotation(ConditionalOnProperty.class));
        assertNullish(TtcRpcRegistryAutoConfiguration.class.getAnnotation(ConditionalOnProperty.class));
        // 结论：RpcServer bean 总会被创建（会占端口），enabled=false 只阻止 start()。
        assertNotNull(new TtcRpcProperties().getServer());
    }

    private static void assertNullish(Object o) {
        org.junit.jupiter.api.Assertions.assertNull(o);
    }

    @Test
    @DisplayName("TtcRpcFrameworkInitializer 只有日志字段、零方法 => 纯打印构造器，无副作用")
    void frameworkInitializerIsLogOnly() {
        assertEquals(0, TtcRpcFrameworkInitializer.class.getDeclaredMethods().length,
                "Initializer 不应有任何可调用行为: "
                        + Arrays.toString(TtcRpcFrameworkInitializer.class.getDeclaredMethods()));
        for (java.lang.reflect.Field f : TtcRpcFrameworkInitializer.class.getDeclaredFields()) {
            assertTrue(java.lang.reflect.Modifier.isStatic(f.getModifiers()),
                    "Initializer 唯一允许的非方法成员是静态 logger，发现实例字段: " + f);
            assertEquals("log", f.getName());
        }
        assertEquals(1, TtcRpcFrameworkInitializer.class.getDeclaredConstructors().length);
    }

    @Test
    @DisplayName("元数据可读：SimpleMetadataReader 能解析出被 @Configuration 标注的装配类")
    void configurationClassesAreReadable() throws Exception {
        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory();
        Class<?>[] types = new Class<?>[]{
                TtcRpcAutoConfiguration.class,
                TtcRpcServerAutoConfiguration.class,
                TtcRpcConsumerAutoConfiguration.class,
                TtcRpcRegistryAutoConfiguration.class,
                TtcRpcServiceExporter.class,
                com.taotao.cloud.rpc.starter.config.TtcRpcReferenceInjector.class
        };
        for (Class<?> type : types) {
            AnnotationMetadata md = factory.getMetadataReader(type.getName()).getAnnotationMetadata();
            assertTrue(md.isAnnotated("org.springframework.context.annotation.Configuration")
                            || md.isAnnotated("org.springframework.stereotype.Component"),
                    type + " 既不是 @Configuration 也不是 @Component，Spring 不会处理它");
        }
    }
}
