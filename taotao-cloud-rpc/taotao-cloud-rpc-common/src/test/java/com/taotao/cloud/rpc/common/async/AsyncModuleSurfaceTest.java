package com.taotao.cloud.rpc.common.async;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ttc-rpc-async 模块对外承诺的能力盘点。
 * <p>
 * pom 的 description 写着「Async invocation support (DefaultFuture, AsyncRpcResult, RpcContext)」，
 * 三个名字里只有一个半是真的：AsyncRpcResult 不存在，而 {@code Result} 上那组异步契约
 * （{@code isAsync()} / {@code getResultFuture()}）在本模块没有任何实现可以对接 ——
 * DefaultFuture 既不是 CompletableFuture，也没有被任何 Result 持有。
 */
class AsyncModuleSurfaceTest {

    @Test
    @DisplayName("bug_ pom 承诺的 AsyncRpcResult 这个类根本不存在")
    void bug_advertisedAsyncRpcResultIsMissing() {
        // 猎物：同包的两个类是真的在
        assertNotNull(DefaultFuture.class.getName());
        assertNotNull(RpcContext.class.getName());
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("com.taotao.cloud.rpc.async.AsyncRpcResult"));
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("com.taotao.cloud.rpc.async.AsyncContext"));
    }

    @Test
    @DisplayName("bug_ DefaultFuture 不实现任何 Future 接口，也无法交给 Result")
    void bug_defaultFutureIsNotAFuture() throws Exception {
        // 猎物：类是有的，方法名也对得上
        assertNotNull(DefaultFuture.class.getDeclaredMethod("get"));
        assertNotNull(DefaultFuture.class.getDeclaredMethod("isDone"));

        assertEquals(0, DefaultFuture.class.getInterfaces().length,
                "不实现 java.util.concurrent.Future，也不实现 Result 那一侧的异步契约");
        List<String> ifaces = new ArrayList<>();
        for (Class<?> c : DefaultFuture.class.getInterfaces()) {
            ifaces.add(c.getName());
        }
        assertEquals("[]", ifaces.toString());
        assertFalseImplementsFuture();
    }

    private void assertFalseImplementsFuture() {
        assertTrue(java.util.concurrent.Future.class.isAssignableFrom(DefaultFuture.class) == false);
        assertTrue(java.util.concurrent.CompletableFuture.class.isAssignableFrom(DefaultFuture.class) == false);
    }

    @Test
    @DisplayName("bug_ DefaultFuture 没有带 Future 的构造函数：无法把响应接到任何 Result 上")
    void bug_futureCannotBeHandedToAResult() {
        Constructor<?>[] cs = DefaultFuture.class.getDeclaredConstructors();
        assertEquals(1, cs.length, "唯一的构造函数是 (long, long)");
        assertEquals(2, cs[0].getParameterTypes().length);
        assertThrows(NoSuchMethodException.class,
                () -> DefaultFuture.class.getDeclaredConstructor(long.class, long.class, Object.class));
        // 私有构造函数：连测试也只能走 newFuture
        assertTrue(java.lang.reflect.Modifier.isPrivate(cs[0].getModifiers()));
    }
}
