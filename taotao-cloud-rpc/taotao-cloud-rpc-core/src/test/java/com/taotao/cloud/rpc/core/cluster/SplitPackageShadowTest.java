package com.taotao.cloud.rpc.core.cluster;

import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import com.taotao.cloud.rpc.common.spi.SPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 同一个 FQCN 在两个模块里各有一份源码时的实际后果。
 * <p>
 * {@code com.taotao.cloud.rpc.cluster} 这个包被 {@code ttc-rpc-core} 和 {@code ttc-rpc-cluster}
 * 各自编译了一份，5 个类型同名。谁生效取决于 classpath 顺序 —— 这里不猜，直接把
 * {@code getResources} 和已加载类的 {@code codeSource} 量出来。
 */
class SplitPackageShadowTest {

    private static final String CLASS_DIR = "com/zifang/z/rpc/cluster/";

    /** 两个模块里同名的 5 个类型（实测：全仓重复 FQCN 恰好只有这 5 个）。 */
    private static final String[] DUPLICATED = {
            "Cluster", "Router", "Directory", "AbstractDirectory", "FailoverCluster"
    };

    private static List<URL> copiesOf(String binaryName) throws Exception {
        ClassLoader cl = SplitPackageShadowTest.class.getClassLoader();
        List<URL> found = new ArrayList<>();
        java.util.Enumeration<URL> e = cl.getResources(CLASS_DIR + binaryName + ".class");
        while (e.hasMoreElements()) {
            found.add(e.nextElement());
        }
        return found;
    }

    private static String locationOf(Class<?> c) {
        try {
            java.security.CodeSource cs = c.getProtectionDomain().getCodeSource();
            return cs == null || cs.getLocation() == null ? "<undefined>" : cs.getLocation().toString();
        } catch (RuntimeException e) {
            return "<unreadable:" + e.getClass().getSimpleName() + ">";
        }
    }

    @Test
    @DisplayName("classloader 里 com.taotao.cloud.rpc.cluster.Cluster.class 有多份字节码")
    void clusterClassIsShippedTwice() throws Exception {
        List<URL> copies = copiesOf("Cluster");
        assertTrue(copies.size() >= 2, "同名 class 只有一份就不该有 shadowing 讨论: " + copies);
        // 两份必须来自不同的输出目录，否则是同一份被数了两次
        assertNotSameLocation(copies.get(0).toString(), copies.get(1).toString(), copies);
    }

    private static void assertNotSameLocation(String a, String b, List<URL> all) {
        assertFalse(a.equals(b), "两个 URL 指向同一处: " + all);
    }

    @Test
    @DisplayName("对照：只属于 ttc-rpc-cluster 的类型各有一份，量具不是恒大于 1")
    void singletonClassHasExactlyOneCopy() throws Exception {
        assertEquals(1, copiesOf("StaticDirectory").size(),
                "StaticDirectory 只在 ttc-rpc-cluster 里有源码，若这里也 >1 说明上面那条是量具假阳");
    }

    @Test
    @DisplayName("5 个类型全部被 core 与 cluster 重复编译")
    void everyDuplicatedTypeHasTwoCopies() throws Exception {
        for (String name : DUPLICATED) {
            assertTrue(copiesOf(name).size() >= 2, name + " 只有一份: " + copiesOf(name));
        }
    }

    @Test
    @DisplayName("测试自己编译到的 Cluster，就是 classloader 实际加载的那一份")
    void compiledReferenceResolvesToLoadedClass() throws Exception {
        assertSame(Cluster.class, Class.forName("com.taotao.cloud.rpc.cluster.Cluster", false,
                SplitPackageShadowTest.class.getClassLoader()));
        assertNotNull(locationOf(Cluster.class));
    }

    @Test
    @DisplayName("被加载的那一份 Cluster 没有 @SPI —— C2 的注解修复落在另一份源码上")
    void loadedClusterCopyLacksSpi() {
        String where = locationOf(Cluster.class);
        assertFalse(Cluster.class.isAnnotationPresent(SPI.class),
                "已加载的 Cluster 来自 " + where + "，若它带上了 @SPI 说明重复源码已被消除，本条应翻正");
    }

//    @Test
//    @DisplayName("bug_clusterExtensionIsUnusableThroughTheLoadedCopy：@SPI 缺席让 5 行扩展表全成死表")
//    void bug_clusterExtensionIsUnusableThroughTheLoadedCopy() {
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
//                () -> ExtensionLoader.getExtensionLoader(Cluster.class));
//        assertTrue(e.getMessage().contains("not annotated with @SPI"), "实际: " + e.getMessage());
//
//        // 猎物 1：同一张扩展表文件确实能被发现，名字也在
//        URL meta = SplitPackageShadowTest.class.getClassLoader()
//                .getResource("META-INF/ttc-rpc/com.taotao.cloud.rpc.cluster.Cluster");
//        assertNotNull(meta, "扩展表资源本身必须在 classpath 上，否则上面那条红说明的是别的问题");
//
//        // 猎物 2：注解齐全的接口用同一个加载器一句话就能取到扩展
//        Object lb = ExtensionLoader.getExtensionLoader(com.taotao.cloud.rpc.loadbalance.LoadBalance.class)
//                .getExtension("random");
//        assertNotNull(lb);
//    }

    @Test
    @DisplayName("bug_ttcrpcCoreIsFirstOnItsOwnClasspath：赢的是 core 那份陈旧副本")
    void bug_coreCopyIsTheOneThatWins() throws Exception {
        List<URL> copies = copiesOf("Cluster");
        String winner = locationOf(Cluster.class);
        boolean winnerIsCore = winner.contains("ttc-rpc-core");
        assertTrue(winnerIsCore,
                "已加载 Cluster 来自 " + winner + "，classpath 上可见的副本: " + copies
                        + " —— 若不是 core 那份，C2 的修复就在核心链路上生效了，本条应翻正");
    }

    @Test
    @DisplayName("URLClassLoader 按另一份源码的目录顺序加载，能拿到带 @SPI 的那一份")
    void otherCopyIsAnnotated() throws Exception {
        List<URL> copies = copiesOf("Cluster");
        URL clusterSide = null;
        for (URL u : copies) {
            if (u.toString().contains("ttc-rpc-cluster")) {
                clusterSide = u;
            }
        }
        assertNotNull(clusterSide, "没找到 ttc-rpc-cluster 那份: " + copies);

        // 把 cluster 那份所在的 classes 根目录单独做成一个 loader，绕开 core 的遮蔽
        String root = clusterSide.toString().replace("com/zifang/z/rpc/cluster/Cluster.class", "");
        // 注解类必须一起给：解析不出注解类型时 getAnnotations() 会把整条丢掉；
        // 且跨 loader 的 Class 对象比对无效，只能按名字判（见下面那段循环）
        String spiMarker = SplitPackageShadowTest.class.getClassLoader()
                .getResource("com/zifang/z/rpc/spi/SPI.class").toString();
        URL spiRoot = new URL(spiMarker.replace("com/zifang/z/rpc/spi/SPI.class", ""));
        try (URLClassLoader only = new URLClassLoader(new URL[]{new URL(root), spiRoot},
                SplitPackageShadowTest.class.getClassLoader().getParent())) {
            assertNotNull(only.findResource("com/zifang/z/rpc/spi/SPI.class"),
                    "量具自检：这个 loader 看不见 SPI 注解类的话，下面那条断言永远只会返回 false");
            Class<?> c = only.loadClass("com.taotao.cloud.rpc.cluster.Cluster");
            assertTrue(locationOf(c).contains("ttc-rpc-cluster"),
                    "遮蔽没绕开，加载到的还是这份: " + locationOf(c));
            // isAnnotationPresent(SPI.class) 在这里不可用：那个 Class 对象来自 app loader，
            // 与 only 解析出来的同名注解不是同一个类型。只能按名字读。
            String value = null;
            boolean annotated = false;
            for (java.lang.annotation.Annotation a : c.getAnnotations()) {
                if ("com.taotao.cloud.rpc.common.spi.SPI".equals(a.annotationType().getName())) {
                    annotated = true;
                    value = String.valueOf(a.annotationType().getMethod("value").invoke(a));
                }
            }
            assertTrue(annotated, "ttc-rpc-cluster 那份带 @SPI 是 C2 修复轮的实测结论"
                    + "（javap 里 RuntimeVisibleAnnotations 明确在），这份的注解是: "
                    + Arrays.toString(c.getAnnotations()));
            assertEquals("failover", value);
        }
    }
}
