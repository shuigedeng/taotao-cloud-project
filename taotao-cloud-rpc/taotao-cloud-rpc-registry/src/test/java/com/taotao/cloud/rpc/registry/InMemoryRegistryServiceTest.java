package com.taotao.cloud.rpc.registry;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import com.taotao.cloud.rpc.common.spi.SPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link InMemoryRegistryService} 的行为契约。
 * <p>
 * 这个类是本次修复新增的：在此之前 {@code RegistryService} 带着
 * {@code META-INF/ttc-rpc/...RegistryService} 元文件却<b>零实现</b>
 * （元文件点名的 InMemoryRpcRegistry 实现的是另一套 RpcRegistry 接口），
 * 而 RegistryDirectory / ServiceConfig / ReferenceConfig 全都依赖它。
 */
class InMemoryRegistryServiceTest {

    private static final String SVC = "com.shuigedeng.demo.UserService";

    private static URL provider(String host, int port) {
        return new URL("ttc-rpc", host, port, SVC);
    }

    private static URL consumer() {
        return new URL("consumer", "127.0.0.1", 0, SVC);
    }

    /** 收集 NotifyListener 的每次推送。 */
    private static final class Recorder implements NotifyListener {
        final List<List<URL>> pushes = new ArrayList<List<URL>>();
        final AtomicInteger count = new AtomicInteger();

        @Override
        public void notify(List<URL> urls) {
            pushes.add(new ArrayList<URL>(urls));
            count.incrementAndGet();
        }
    }

    @Test
    @DisplayName("SPI 已接通：默认名 in-memory 能解析出真正的 RegistryService 实现")
    void defaultExtensionIsAssignable() {
        SPI spi = RegistryService.class.getAnnotation(SPI.class);
        assertNotNull(spi, "prey：RegistryService 必须带 @SPI");
        assertEquals("in-memory", spi.value());

        ExtensionLoader<RegistryService> loader =
                ExtensionLoader.getExtensionLoader(RegistryService.class);
        RegistryService def = loader.getDefaultExtension();
        assertNotNull(def, "默认扩展解析为 null => @SPI 的名字在元文件里没落点");
        assertTrue(def instanceof InMemoryRegistryService, "实际: " + def.getClass());
        assertSame(def, loader.getExtension("in-memory"), "同一名字必须同一实例");
    }

    @Test
    @DisplayName("register 后 lookup 能读回，且按地址幂等去重")
    void registerIsIdempotentByAddress() {
        InMemoryRegistryService reg = new InMemoryRegistryService();
        assertEquals(0, reg.lookup(consumer()).size(), "prey：初始为空");

        reg.register(provider("10.0.0.1", 20880));
        List<URL> after = reg.lookup(consumer());
        assertEquals(1, after.size(), "注册后必须读回 1 条");
        assertEquals("10.0.0.1:20880", after.get(0).getAddress());

        reg.register(provider("10.0.0.1", 20880));
        assertEquals(1, reg.lookup(consumer()).size(), "同地址重复注册不应膨胀");

        reg.register(provider("10.0.0.2", 20880));
        assertEquals(2, reg.lookup(consumer()).size(), "不同地址必须各占一条");
    }

    @Test
    @DisplayName("register(null) 直接抛 IllegalArgumentException")
    void registerRejectsNull() {
        final InMemoryRegistryService reg = new InMemoryRegistryService();
        assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                reg.register(null);
            }
        });
    }

    @Test
    @DisplayName("无 serviceInterface 的 URL 被拒 —— 而不是悄悄落到 \"null\" 键下")
    void urlWithoutServiceInterfaceIsRejected() {
        final InMemoryRegistryService reg = new InMemoryRegistryService();
        URL anonymous = new URL("ttc-rpc", "10.0.0.1", 20880);
        assertNullish(anonymous.getServiceInterface());
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() {
                        reg.register(anonymous);
                    }
                });
        assertTrue(thrown.getMessage().contains("serviceInterface"), "实际消息: " + thrown.getMessage());

        // prey：带接口名的同一个地址照常注册成功，说明被拒的原因确实是缺接口
        reg.register(provider("10.0.0.1", 20880));
        assertEquals(1, reg.lookup(consumer()).size());
    }

    private static void assertNullish(Object o) {
        org.junit.jupiter.api.Assertions.assertNull(o);
    }

    @Test
    @DisplayName("unregister 精确摘除该地址，不误伤同名服务的其他 provider")
    void unregisterIsPerAddress() {
        InMemoryRegistryService reg = new InMemoryRegistryService();
        reg.register(provider("10.0.0.1", 20880));
        reg.register(provider("10.0.0.2", 20880));
        reg.register(provider("10.0.0.3", 20880));
        assertEquals(3, reg.lookup(consumer()).size(), "prey：先塞进 3 条");

        reg.unregister(provider("10.0.0.2", 20880));
        List<URL> left = reg.lookup(consumer());
        assertEquals(2, left.size());
        assertFalse(containsAddress(left, "10.0.0.2:20880"), "实际剩下: " + left);
        assertTrue(containsAddress(left, "10.0.0.1:20880") && containsAddress(left, "10.0.0.3:20880"));

        reg.unregister(provider("10.0.0.9", 20880));
        assertEquals(2, reg.lookup(consumer()).size(), "注销不存在的地址不应影响他人");
    }

    private static boolean containsAddress(List<URL> urls, String address) {
        for (URL u : urls) {
            if (address.equals(u.getAddress())) {
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("订阅立刻收到当前快照，之后每次增删各推一次；取消订阅后不再推送")
    void subscribePushesOnEveryChange() {
        InMemoryRegistryService reg = new InMemoryRegistryService();
        reg.register(provider("10.0.0.1", 20880));

        Recorder r = new Recorder();
        reg.subscribe(consumer(), r);
        assertEquals(1, r.count.get(), "subscribe 必须立刻回放当前提供者");
        assertEquals(1, r.pushes.get(0).size());

        reg.register(provider("10.0.0.2", 20880));
        assertEquals(2, r.count.get());
        assertEquals(2, r.pushes.get(1).size(), "第二次推送应含 2 条: " + r.pushes.get(1));

        reg.unregister(provider("10.0.0.1", 20880));
        assertEquals(3, r.count.get());
        assertEquals(1, r.pushes.get(2).size());

        reg.unsubscribe(consumer(), r);
        reg.register(provider("10.0.0.9", 20880));
        assertEquals(3, r.count.get(), "取消订阅后不应再收到推送");
    }

    @Test
    @DisplayName("重复 register 同一地址不触发推送（幂等）")
    void duplicateRegisterDoesNotNotify() {
        InMemoryRegistryService reg = new InMemoryRegistryService();
        reg.register(provider("10.0.0.1", 20880));
        Recorder r = new Recorder();
        reg.subscribe(consumer(), r);
        int before = r.count.get();
        reg.register(provider("10.0.0.1", 20880));
        assertEquals(before, r.count.get(), "prey：订阅者在场，重复注册不应再推");
    }

    @Test
    @DisplayName("不同服务名互不可见：lookup 只按 serviceInterface 过滤")
    void servicesAreIsolated() {
        InMemoryRegistryService reg = new InMemoryRegistryService();
        reg.register(new URL("ttc-rpc", "10.0.0.1", 20880, SVC));
        reg.register(new URL("ttc-rpc", "10.0.0.2", 20880, "com.shuigedeng.demo.OrderService"));

        assertEquals(1, reg.lookup(consumer()).size());
        List<URL> orders = reg.lookup(new URL("consumer", "127.0.0.1", 0, "com.shuigedeng.demo.OrderService"));
        assertEquals(1, orders.size());
        assertEquals("10.0.0.2:20880", orders.get(0).getAddress());
        assertEquals(0, reg.lookup(new URL("consumer", "127.0.0.1", 0, "com.shuigedeng.demo.Unknown")).size());
    }

    @Test
    @DisplayName("lookup 返回的是不可变快照，改不动注册表")
    void lookupReturnsImmutableSnapshot() {
        InMemoryRegistryService reg = new InMemoryRegistryService();
        reg.register(provider("10.0.0.1", 20880));
        final List<URL> snapshot = reg.lookup(consumer());
        assertThrows(UnsupportedOperationException.class,
                new org.junit.jupiter.api.function.Executable() {
                    @Override
                    public void execute() {
                        snapshot.clear();
                    }
                });
        assertEquals(1, reg.lookup(consumer()).size(), "prey：注册表本身不受影响");
    }

    @Test
    @DisplayName("destroy 清空注册表与订阅者")
    void destroyClearsEverything() {
        InMemoryRegistryService reg = new InMemoryRegistryService();
        reg.register(provider("10.0.0.1", 20880));
        Recorder r = new Recorder();
        reg.subscribe(consumer(), r);
        int before = r.count.get();

        reg.destroy();
        assertEquals(0, reg.lookup(consumer()).size());
        reg.register(provider("10.0.0.1", 20880));
        assertEquals(before, r.count.get(), "prey：destroy 后旧订阅者不应再被通知");
    }
}
