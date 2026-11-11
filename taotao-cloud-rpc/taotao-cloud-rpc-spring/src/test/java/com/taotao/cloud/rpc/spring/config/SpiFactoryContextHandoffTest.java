package com.taotao.cloud.rpc.spring.config;

import com.taotao.cloud.rpc.common.spi.SpiExtensionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 真容器起起来之后，{@link SpiExtensionFactory} 那一层到底收没收到这个容器（N54 缺的那一环）。
 * <p>
 * §14.1 的 grep 只证明"生产代码里调用点为 0"，它证明不了"如果真起一个容器会怎样"。
 * 这一族一律用真 {@code AnnotationConfigApplicationContext} + 真 {@code ApplicationContextAware}
 * 回调把那句话量成事实：容器就在注入器手里，而 SPI 层始终看不见它。
 */
class SpiFactoryContextHandoffTest {

    /** 只当作"容器里有一个可按名字与类型取到的 bean"用，不参与任何 SPI 注册。 */
    interface HandoffSPI {
        String whoAmI();
    }

    static class HandoffImpl implements HandoffSPI {
        @Override
        public String whoAmI() {
            return "in-the-container";
        }
    }

    @Configuration
    static class HandoffConfig {
        @Bean
        HandoffSPI handoff() {
            return new HandoffImpl();
        }
    }

    private static Map<?, ?> contextCache() throws Exception {
        Field f = SpiExtensionFactory.class.getDeclaredField("CONTEXT_CACHE");
        f.setAccessible(true);
        return (Map<?, ?>) f.get(null);
    }

    private static int cacheSize() throws Exception {
        return contextCache().size();
    }

    /** CONTEXT_CACHE 没有 remove API（N54 的同族小缺陷），现场只能反射还原。 */
    private static void clearContextCache() throws Exception {
        contextCache().clear();
    }

    private AnnotationConfigApplicationContext startRealContext() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(HandoffConfig.class, TtcRpcReferenceInjector.class);
        ctx.refresh();
        return ctx;
    }

    /** 注入器是 ApplicationContextAware：读它私有字段，证 Spring 真的回调过它。 */
    private static Object contextHeldBy(TtcRpcReferenceInjector injector) throws Exception {
        Field f = TtcRpcReferenceInjector.class.getDeclaredField("applicationContext");
        f.setAccessible(true);
        return f.get(injector);
    }

    @Test
    @DisplayName("bug_ 真容器 refresh 之后 SPI 层仍然看不见它：容器在注入器手里，静态缓存却一直是空的")
    void bug_realRefreshedContextNeverReachesTheSpiFactory() throws Exception {
        assertEquals(0, cacheSize(), "前提：这份静态缓存进来时是空的，本用例看见的一切都是它自己造的");

        AnnotationConfigApplicationContext ctx = startRealContext();
        try {
            // 猎物在场：容器里确实有一个既能按名取、也满足那个接口类型的 bean
            Object bean = ctx.getBean("handoff");
            assertNotNull(bean, "前提不成立的话下面那句 assertNull 就没有意义");
            assertTrue(bean instanceof HandoffSPI, "bean 类型不匹配的话 null 是显然的，不构成缺陷");
            assertSame(HandoffImpl.class, bean.getClass());

            // 而且 Spring 真的把容器交到了 starter 这一层（ApplicationContextAware 回调确实发生）
            TtcRpcReferenceInjector injector = ctx.getBean(TtcRpcReferenceInjector.class);
            ApplicationContext handedToInjector = (ApplicationContext) contextHeldBy(injector);
            assertSame(ctx, handedToInjector,
                    "前提：starter 里那个 Aware 回调收到了这台容器 —— 值就在这里，只是没人往 SPI 层递");

            // 而 SPI 工厂这一层从头到尾没被递过任何东西
            assertNull(new SpiExtensionFactory().getExtension(HandoffSPI.class, "handoff"),
                    "容器里那个同名同类型的 bean 应当取不到 —— 因为没有任何生产代码把容器交给这一层");
            assertEquals(0, cacheSize(),
                    "整条装配跑完，CONTEXT_CACHE 还是空的 ⇒ setApplicationContext 的生产调用点确实是 0");
        } finally {
            ctx.close();
        }
    }

    @Test
    @DisplayName("正向对照：同一个真容器只要被人递进去，反射 getBean 这条路立刻取得到那个 bean")
    void reflectionPathWorksOnceSomeoneHandsTheContextOver() throws Exception {
        assertEquals(0, cacheSize(), "前提：缓存是空的");

        AnnotationConfigApplicationContext ctx = startRealContext();
        try {
            Object bean = ctx.getBean("handoff");
            SpiExtensionFactory.setApplicationContext(ctx);
            assertEquals(1, cacheSize(), "递进去之后缓存应当有一格");
            assertSame(bean, new SpiExtensionFactory().getExtension(HandoffSPI.class, "handoff"),
                    "反射 getBean(name) + 类型判定这条路本身是通的 —— 上一条用例里的 null 是接线缺失，不是反射失败");
        } finally {
            ctx.close();
            clearContextCache();
        }
        assertEquals(0, cacheSize(), "收尾自证：缓存真的空了，不是\"以为清了\"");
        assertNull(new SpiExtensionFactory().getExtension(HandoffSPI.class, "handoff"));
    }
}
