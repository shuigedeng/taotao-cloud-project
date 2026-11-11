package com.taotao.cloud.rpc.common;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link RpcException} 的错误码空间与 13 个工厂方法。
 */
class RpcExceptionTest {

    // ====================== 错误码常量 ======================

    @Test
    void errorCodesHaveTheirDocumentedValues() {
        assertEquals(0, RpcException.UNKNOWN_EXCEPTION);
        assertEquals(1, RpcException.NETWORK_EXCEPTION);
        assertEquals(2, RpcException.TIMEOUT_EXCEPTION);
        assertEquals(3, RpcException.BIZ_EXCEPTION);
        assertEquals(4, RpcException.FORBIDDEN_EXCEPTION);
        assertEquals(5, RpcException.NO_PROVIDER_EXCEPTION);
        assertEquals(6, RpcException.SERIALIZATION_EXCEPTION);
        assertEquals(7, RpcException.PROTOCOL_EXCEPTION);
        assertEquals(8, RpcException.RECONNECT_EXCEPTION);
        assertEquals(9, RpcException.LIMIT_EXCEPTION);
        assertEquals(10, RpcException.CONFIG_EXCEPTION);
    }

    @Test
    void allElevenCodesAreDistinct() {
        Set<Integer> codes = new LinkedHashSet<>(Arrays.asList(
                RpcException.UNKNOWN_EXCEPTION, RpcException.NETWORK_EXCEPTION,
                RpcException.TIMEOUT_EXCEPTION, RpcException.BIZ_EXCEPTION,
                RpcException.FORBIDDEN_EXCEPTION, RpcException.NO_PROVIDER_EXCEPTION,
                RpcException.SERIALIZATION_EXCEPTION, RpcException.PROTOCOL_EXCEPTION,
                RpcException.RECONNECT_EXCEPTION, RpcException.LIMIT_EXCEPTION,
                RpcException.CONFIG_EXCEPTION));
        assertEquals(11, codes.size(), "错误码空间出现重复值: " + codes);
    }

    @Test
    void errorCodesArePublicStaticFinalInts() throws Exception {
        for (String name : new String[]{"UNKNOWN_EXCEPTION", "NETWORK_EXCEPTION", "TIMEOUT_EXCEPTION",
                "BIZ_EXCEPTION", "FORBIDDEN_EXCEPTION", "NO_PROVIDER_EXCEPTION", "SERIALIZATION_EXCEPTION",
                "PROTOCOL_EXCEPTION", "RECONNECT_EXCEPTION", "LIMIT_EXCEPTION", "CONFIG_EXCEPTION"}) {
            java.lang.reflect.Field f = RpcException.class.getField(name);
            int mod = f.getModifiers();
            assertTrue(Modifier.isPublic(mod) && Modifier.isStatic(mod) && Modifier.isFinal(mod),
                    name + " 应当是 public static final");
            assertEquals(int.class, f.getType(), name + " 的类型应当是 int");
        }
    }

    // ====================== 构造器 ======================

    @Test
    void isAnUncheckedException() {
        assertInstanceOf(RuntimeException.class, new RpcException(RpcException.BIZ_EXCEPTION, "boom"));
    }

    @Test
    void codeAndMessageAreRetained() {
        RpcException e = new RpcException(RpcException.TIMEOUT_EXCEPTION, "invoke timeout");
        assertEquals(2, e.getCode());
        assertEquals("invoke timeout", e.getMessage());
        assertNull(e.getCause(), "(int, String) 构造器不应凭空造 cause");
    }

    @Test
    void messageAndCauseAreRetained() {
        IllegalStateException cause = new IllegalStateException("root");
        RpcException e = new RpcException(RpcException.NETWORK_EXCEPTION, "connect refused", cause);
        assertEquals(1, e.getCode());
        assertEquals("connect refused", e.getMessage());
        assertSame(cause, e.getCause());
    }

    @Test
    void causeOnlyConstructorDerivesMessageFromCause() {
        IllegalStateException cause = new IllegalStateException("no message given");
        RpcException e = new RpcException(RpcException.SERIALIZATION_EXCEPTION, cause);
        assertEquals(6, e.getCode());
        assertSame(cause, e.getCause());
        // RuntimeException(cause) 把 cause.toString() 当作 message
        assertEquals("java.lang.IllegalStateException: no message given", e.getMessage());
    }

    @Test
    void codeIsImmutableAfterConstruction() throws Exception {
        // 反射确认 RpcException 自己没有 setter —— code 只能在构造时选定。
        // 只扫 getDeclaredMethods()：getMethods() 会把 Throwable.printStackTrace() 这类继承来的方法也算进来。
        for (Method m : RpcException.class.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers())) continue;
            assertTrue(m.getName().startsWith("get") || m.getName().startsWith("is"),
                    "实例侧只允许读方法，出现写方法: " + m);
        }
        assertThrows(NoSuchMethodException.class, () -> RpcException.class.getMethod("setCode", int.class));
        java.lang.reflect.Field code = RpcException.class.getDeclaredField("code");
        assertTrue(Modifier.isPrivate(code.getModifiers()), "code 字段应当是 private");
        assertTrue(Modifier.isFinal(code.getModifiers()), "code 字段应当是 final");
    }

    private static java.lang.reflect.Field getCodeField() {
        try {
            return RpcException.class.getDeclaredField("code");
        } catch (NoSuchFieldException e) {
            throw new AssertionError("RpcException 不再有 code 字段", e);
        }
    }

    @Test
    void bug_nullAsSecondArgumentIsACompileTimeAmbiguity() throws Exception {
        // `new RpcException(0, null)` 无法编译：两个重载都可匹配。
        List<String> twoArg = new ArrayList<>();
        for (java.lang.reflect.Constructor<?> c : RpcException.class.getConstructors()) {
            Class<?>[] p = c.getParameterTypes();
            if (p.length == 2 && p[0] == int.class) twoArg.add(p[1].getSimpleName());
        }
        assertEquals(new java.util.HashSet<>(Arrays.asList("String", "Throwable")),
                new java.util.HashSet<>(twoArg),
                "(int,String) 与 (int,Throwable) 同时存在，才构成 null 歧义");

        // 猎物：显式转型后两种写法都合法，语义完全不同
        RpcException asMessage = new RpcException(0, (String) null);
        assertNull(asMessage.getMessage());
        assertNull(asMessage.getCause());
        RpcException asCause = new RpcException(0, (Throwable) null);
        assertNull(asCause.getCause());
        assertEquals(asMessage.getCode(), asCause.getCode());
    }

    // ====================== 13 个工厂方法 ======================

    @Test
    void everySingleArgFactoryCarriesItsOwnCode() {
        assertEquals(2, RpcException.timeout("t").getCode());
        assertEquals(1, RpcException.network("n").getCode());
        assertEquals(5, RpcException.noProvider("np").getCode());
        assertEquals(6, RpcException.serialization("s").getCode());
        assertEquals(7, RpcException.protocol("p").getCode());
        assertEquals(3, RpcException.biz("b").getCode());
        assertEquals(4, RpcException.forbidden("f").getCode());
        assertEquals(9, RpcException.limit("l").getCode());
        assertEquals(10, RpcException.config("c").getCode());
    }

    @Test
    void everyTwoArgFactoryKeepsBothMessageAndCause() {
        RuntimeException cause = new RuntimeException("why");
        for (RpcException e : Arrays.asList(
                RpcException.timeout("t", cause), RpcException.network("n", cause),
                RpcException.serialization("s", cause), RpcException.biz("b", cause))) {
            assertEquals("t".isEmpty() ? e.getMessage() : e.getMessage(), e.getMessage());
            assertSame(cause, e.getCause(), e.getClass().getName());
            assertTrue(e.getMessage().length() > 0);
        }
        assertEquals(Arrays.asList(2, 1, 6, 3), Arrays.asList(
                RpcException.timeout("t", cause).getCode(),
                RpcException.network("n", cause).getCode(),
                RpcException.serialization("s", cause).getCode(),
                RpcException.biz("b", cause).getCode()));
    }

    @Test
    void protocolFactoryIsWhatTheDecoderThrows() {
        RpcException e = RpcException.protocol("Invalid magic number: 0x0");
        assertEquals(RpcException.PROTOCOL_EXCEPTION, e.getCode());
        assertEquals("Invalid magic number: 0x0", e.getMessage());
    }

    @Test
    void bug_reconnectCodeHasNoFactoryWhileEveryOtherCodeDoes() {
        // 反向断言的猎物：先证明 (String) 工厂确实覆盖 9 个码，再指出 8 无人生产。
        Set<Integer> produced = new LinkedHashSet<>();
        for (Method m : RpcException.class.getDeclaredMethods()) {
            if (!Modifier.isStatic(m.getModifiers()) || m.getReturnType() != RpcException.class) continue;
            Class<?>[] p = m.getParameterTypes();
            if (p.length == 1 && p[0] == String.class) {
                produced.add(((RpcException) invoke(m, "x")).getCode());
            }
        }
        Set<Integer> all = new LinkedHashSet<>(Arrays.asList(
                RpcException.UNKNOWN_EXCEPTION, RpcException.NETWORK_EXCEPTION, RpcException.TIMEOUT_EXCEPTION,
                RpcException.BIZ_EXCEPTION, RpcException.FORBIDDEN_EXCEPTION, RpcException.NO_PROVIDER_EXCEPTION,
                RpcException.SERIALIZATION_EXCEPTION, RpcException.PROTOCOL_EXCEPTION,
                RpcException.RECONNECT_EXCEPTION, RpcException.LIMIT_EXCEPTION, RpcException.CONFIG_EXCEPTION));
        assertEquals(new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 9, 10)), produced,
                "单参工厂覆盖的码集合发生变化，本用例需要同步");
        assertTrue(all.removeAll(produced), "若 0/8 也已有工厂，本用例的前提就消失了");
        assertEquals(new LinkedHashSet<>(Arrays.asList(0, 8)), all,
                "UNKNOWN_EXCEPTION(0) 与 RECONNECT_EXCEPTION(8) 都定义了常量，却没有任何工厂方法能生产它们"
                        + "——重连路径只能裸 new RpcException(8, ...)，调用方无法用统一入口表达");
    }

    @Test
    void noFactoryProducesTheUnknownCode() {
        assertThrows(NoSuchMethodException.class, () -> RpcException.class.getMethod("unknown", String.class));
        assertThrows(NoSuchMethodException.class, () -> RpcException.class.getMethod("reconnect", String.class));
        // 猎物：其它 9 个码都有具名工厂
        assertEquals(1, RpcException.network("x").getCode());
        assertEquals(0, new RpcException(RpcException.UNKNOWN_EXCEPTION, "raw").getCode());
        assertEquals(8, new RpcException(RpcException.RECONNECT_EXCEPTION, "raw").getCode());
    }

    private static Object invoke(Method m, Object arg) {
        try {
            return m.invoke(null, arg);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }
}
