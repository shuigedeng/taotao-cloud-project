package com.taotao.cloud.rpc.common.api;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JdkProxyFactory}：消费端代理与服务端反射 Invoker 两条路都只靠 JDK 反射，
 * 所以这一层每一次"顺手拦截"都直接决定业务方法能不能被调到。
 */
class JdkProxyFactoryTest {

    interface Greeter {
        String hello(String who);

        String shout();

        void ping();

        int plus(int a, int b);

        /** 与 Object.equals(Object) 同名的重载：这是业务方法，不是 Object 方法。 */
        boolean equals(String other);

        String alwaysFails() throws IOException;
    }

    /** 记录收到的调用，并按 next 决定返回什么。 */
    static final class Recording implements Invoker<Greeter> {
        final List<Invocation> seen = new ArrayList<Invocation>();
        final URL url;
        Result next = Result.success("ok");
        Throwable throwsDirectly;

        Recording(URL url) {
            this.url = url;
        }

        @Override public Class<Greeter> getInterface() { return Greeter.class; }

        @Override public Result invoke(Invocation invocation) throws Throwable {
            seen.add(invocation);
            if (throwsDirectly != null) throw throwsDirectly;
            return next;
        }

        @Override public URL getUrl() { return url; }

        @Override public boolean isAvailable() { return true; }

        @Override public void destroy() { }

        @Override public String toString() { return "recording-invoker"; }
    }

    /** 只填方法定位信息的 Invocation，用于绕开代理直接驱动反射 Invoker。 */
    static final class SimpleInvocation implements Invocation {
        private final String name;
        private final Class<?>[] types;
        private final Object[] args;
        private final Map<String, String> attachments = new HashMap<String, String>();

        SimpleInvocation(String name, Class<?>[] types, Object[] args) {
            this.name = name;
            this.types = types;
            this.args = args;
        }

        @Override public String getServiceInterface() { return Greeter.class.getName(); }
        @Override public String getMethodName() { return name; }
        @Override public Class<?>[] getParameterTypes() { return types; }
        @Override public Object[] getArguments() { return args; }
        @Override public Map<String, String> getAttachments() { return attachments; }
        @Override public String getAttachment(String key) { return attachments.get(key); }
        @Override public String getAttachment(String key, String defaultValue) {
            return attachments.containsKey(key) ? attachments.get(key) : defaultValue;
        }
        @Override public void setAttachment(String key, String value) { attachments.put(key, value); }
        @Override public URL getInvokerUrl() { return null; }
    }

    private static URL url() {
        URL u = new URL("ttc-rpc", "127.0.0.1", 20880, Greeter.class.getName());
        u.setVersion("5.0.0");
        return u;
    }

    /** 一个真实的实现类，用于反射 Invoker。 */
    public static class GreeterImpl implements Greeter {
        int pingCount;

        @Override public String hello(String who) { return "hi " + who; }

        @Override public String shout() { return "HEY"; }

        @Override public void ping() { pingCount++; }

        @Override public int plus(int a, int b) { return a + b; }

        @Override public boolean equals(String other) { return "yes".equals(other); }

        @Override public String alwaysFails() throws IOException { throw new IOException("impl-boom"); }
    }

    /** JDK 只认识注册过 handler 的协议，所以服务端入口只能喂 http 这类 URL —— 见下面那条 bug_。 */
    @SuppressWarnings("deprecation")
    private static java.net.URL jdkUrl(String protocol, String path) throws Exception {
        return new java.net.URL(protocol, "127.0.0.1", 20880, path);
    }

    private final JdkProxyFactory factory = new JdkProxyFactory();

    // ==================== getProxy：调用参数如何组装 ====================

    @Test
    @DisplayName("代理把方法名、参数类型、实参、接口名、URL 一并交给 Invoker")
    void proxyAssemblesTheInvocation() throws Throwable {
        Recording rec = new Recording(url());
        rec.next = Result.success("hi bob");
        Greeter proxy = factory.getProxy(rec);

        assertEquals("hi bob", proxy.hello("bob"));
        assertEquals(1, rec.seen.size());
        Invocation inv = rec.seen.get(0);
        assertEquals("hello", inv.getMethodName());
        assertArrayEquals(new Class<?>[]{String.class}, inv.getParameterTypes());
        assertArrayEquals(new Object[]{"bob"}, inv.getArguments());
        assertEquals(Greeter.class.getName(), inv.getServiceInterface());
        assertSame(rec.getUrl(), inv.getInvokerUrl(), "invoker 的 URL 必须原样带过去");
    }

    @Test
    @DisplayName("无参方法到达 invoker 时 arguments 是空数组而不是 null")
    void noArgMethodGetsEmptyArgumentArray() throws Throwable {
        Recording rec = new Recording(url());
        rec.next = Result.success("HEY");
        Greeter proxy = factory.getProxy(rec);

        assertEquals("HEY", proxy.shout());
        // JDK 对无参方法传下来的 args 是 null，代理层必须抹平成空数组，否则下游 .length 就炸
        assertEquals(0, rec.seen.get(0).getArguments().length);
        assertEquals(0, rec.seen.get(0).getParameterTypes().length);
    }

    @Test
    @DisplayName("基本类型参数按 Class 原样传递，装箱不影响返回值")
    void primitiveSignaturesSurviveTheProxy() throws Throwable {
        Recording rec = new Recording(url());
        rec.next = Result.success(Integer.valueOf(12));
        Greeter proxy = factory.getProxy(rec);

        assertEquals(12, proxy.plus(5, 7));
        assertArrayEquals(new Class<?>[]{int.class, int.class},
                rec.seen.get(0).getParameterTypes(), "必须是 int.class 而不是 Integer.class");
    }

    @Test
    @DisplayName("void 方法：结果为空值时代理返回 null，不抛错")
    void voidMethodToleratesNullValue() throws Throwable {
        Recording rec = new Recording(url());
        rec.next = new Result.RpcResult((Object) null);
        Greeter proxy = factory.getProxy(rec);

        proxy.ping();
        assertEquals(1, rec.seen.size());
    }

    @Test
    @DisplayName("Result 带异常时代理原样抛出，调用方看到的仍是业务异常类型")
    void resultExceptionIsRethrownUnwrapped() {
        Recording rec = new Recording(url());
        rec.next = Result.error(new IOException("net-down"));
        final Greeter proxy = factory.getProxy(rec);

        IOException thrown = assertThrows(IOException.class, () -> proxy.alwaysFails());
        assertEquals("net-down", thrown.getMessage());
    }

    @Test
    @DisplayName("invoker 自己抛异常时代理不吞：抛出的是同一个对象")
    void invokerThrowablePropagates() {
        Recording rec = new Recording(url());
        rec.throwsDirectly = new IllegalStateException("from invoker");
        final Greeter proxy = factory.getProxy(rec);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> proxy.hello("x"));
        assertSame(rec.throwsDirectly, thrown);
    }

    @Test
    @DisplayName("异常与值同时存在时代理选择抛异常")
    void exceptionWinsOverValue() {
        Recording rec = new Recording(url());
        Result.RpcResult both = new Result.RpcResult("ignored");
        both.setException(new IllegalStateException("wins"));
        rec.next = both;
        final Greeter proxy = factory.getProxy(rec);

        assertThrows(IllegalStateException.class, () -> proxy.shout());
    }

    // ==================== getProxy：Object 方法拦截 ====================

    @Test
    @DisplayName("toString/hashCode/equals(Object) 被拦下，不产生远程调用")
    void objectMethodsAreHandledLocally() throws Throwable {
        Recording rec = new Recording(url());
        Greeter proxy = factory.getProxy(rec);
        Greeter other = factory.getProxy(new Recording(url()));

        assertEquals("recording-invoker", proxy.toString());
        assertEquals(rec.hashCode(), proxy.hashCode());
        assertTrue(proxy.equals(proxy));
        assertFalse(proxy.equals(other));
        assertTrue(other.equals(other), "prey：另一个代理也是只认自身身份");
        assertEquals(0, rec.seen.size(), "三个 Object 方法都不该走网络");
    }

    @Test
    @DisplayName("接口自己声明的 equals(String) 是业务方法，必须到达 Invoker")
    void businessEqualsOverloadReachesTheInvoker() throws Throwable {
        Recording rec = new Recording(url());
        rec.next = Result.success(Boolean.TRUE);
        Greeter proxy = factory.getProxy(rec);

        assertTrue(proxy.equals("yes"),
                "拦截条件只看方法名和参数个数时，equals(String) 会被当成 Object.equals 吞掉，"
                        + "远程方法静默返回 false");
        assertEquals(1, rec.seen.size(), "prey：这条调用必须真的发出去");
        assertEquals("equals", rec.seen.get(0).getMethodName());
        assertArrayEquals(new Class<?>[]{String.class}, rec.seen.get(0).getParameterTypes());
    }

    // ==================== getProxy：入参校验 ====================

    @Test
    @DisplayName("invoker 为 null 直接拒绝")
    void nullInvokerIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.getProxy((Invoker<Greeter>) null));
        assertThrows(IllegalArgumentException.class,
                () -> factory.getProxy((Invoker<Greeter>) null, true));
    }

    @Test
    @DisplayName("getInterface() 给出非接口类型时拒绝创建代理")
    void nonInterfaceTypeIsRejected() {
        Invoker<String> bad = newBadTypeInvoker();
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> factory.getProxy(bad));
        assertTrue(thrown.getMessage().contains("not an interface"), thrown.getMessage());
        // 猎物：接口类型走同一条路是成功的
        assertNotNull(factory.getProxy(new Recording(url())));
    }

    @SuppressWarnings("unchecked")
    private static Invoker<String> newBadTypeInvoker() {
        return (Invoker<String>) (Invoker<?>) new Invoker() {
            @Override public Class getInterface() { return String.class; }
            @Override public Result invoke(Invocation invocation) { return Result.success(null); }
            @Override public URL getUrl() { return null; }
            @Override public boolean isAvailable() { return true; }
            @Override public void destroy() { }
        };
    }

    @Test
    @DisplayName("URL 为 null 的 invoker 可以建代理，调用只是不带 invokerUrl")
    void nullUrlInvokerIsStillProxyable() throws Throwable {
        Recording rec = new Recording(null);
        Greeter proxy = factory.getProxy(rec);
        proxy.shout();
        assertNull(rec.seen.get(0).getInvokerUrl());
        assertEquals("1.0.0", rec.seen.get(0).getVersion(),
                "prey：代理不看 URL 的 version/group，见 RpcInvocationTest 里那条 bug_");
    }

    // ==================== 泛化入口 ====================

    @Test
    @DisplayName("bug_generic=true 与 false 行为完全一致：泛化代理没有实现")
    @SuppressWarnings("unchecked")
    void bug_genericFlagIsIgnored() throws Throwable {
        Recording typed = new Recording(url());
        Recording generic = new Recording(url());
        Object p1 = factory.getProxy(typed, false);
        Object p2 = factory.getProxy(generic, true);

        assertTrue(p2 instanceof Greeter,
                "prey：声称『不依赖接口类的代理』，实际仍然要求并实现 Greeter");
        assertEquals(p1.getClass(), p2.getClass());

        // 泛化入口在坏输入下给出的仍是同一个异常：说明 generic 根本没被读过
        assertThrows(IllegalStateException.class, () -> factory.getProxy(newBadTypeInvoker(), true));

        ((Greeter) p2).shout();
        assertEquals(1, generic.seen.size(), "generic=true 只是换了个名字的同一条代理链路");
    }

    // ==================== getInvoker：服务端反射 ====================

    @Test
    @DisplayName("getInvoker 反射调用真实实现，值与副作用都落地")
    void reflectiveInvokerCallsThrough() throws Throwable {
        GreeterImpl impl = new GreeterImpl();
        Invoker<Greeter> invoker = factory.getInvoker(impl, Greeter.class, jdkUrl("http", "/"));
        assertEquals(Greeter.class, invoker.getInterface());

        Result r = invoker.invoke(new SimpleInvocation("hello",
                new Class<?>[]{String.class}, new Object[]{"bob"}));
        assertFalse(r.hasException());
        assertEquals("hi bob", r.getValue());

        invoker.invoke(new SimpleInvocation("ping", new Class<?>[0], new Object[0]));
        assertEquals(1, impl.pingCount, "prey：反射真的落到了实现上");
        assertEquals(12, ((Integer) invoker.invoke(new SimpleInvocation("plus",
                new Class<?>[]{int.class, int.class}, new Object[]{5, 7})).getValue()).intValue());
        assertTrue(invoker.isAvailable());
        invoker.destroy();
    }

    @Test
    @DisplayName("实现抛出的业务异常被剥掉 InvocationTargetException 后放进 Result")
    void reflectiveInvokerUnwrapsTargetException() throws Throwable {
        Invoker<Greeter> invoker = factory.getInvoker(new GreeterImpl(), Greeter.class, jdkUrl("http", "/"));
        Result r = invoker.invoke(new SimpleInvocation("alwaysFails", new Class<?>[0], new Object[0]));
        assertTrue(r.hasException());
        assertEquals(IOException.class, r.getException().getClass(),
                "必须是业务异常本身，不是反射包装: " + r.getException());
        assertEquals("impl-boom", r.getException().getMessage());
    }

    @Test
    @DisplayName("方法名或签名对不上时 Result 带 NoSuchMethodException，不抛穿")
    void reflectiveInvokerReportsMissingMethodAsResult() throws Throwable {
        Invoker<Greeter> invoker = factory.getInvoker(new GreeterImpl(), Greeter.class, jdkUrl("http", "/"));
        Result r = invoker.invoke(new SimpleInvocation("noSuch", new Class<?>[0], new Object[0]));
        assertTrue(r.hasException());
        assertEquals(NoSuchMethodException.class, r.getException().getClass());

        Result wrongSignature = invoker.invoke(new SimpleInvocation("hello",
                new Class<?>[]{int.class}, new Object[]{1}));
        assertTrue(wrongSignature.hasException());
        assertEquals(NoSuchMethodException.class, wrongSignature.getException().getClass());
    }

    @Test
    @DisplayName("bug_getInvoker 收的是 java.net.URL：本框架自己的 ttc-rpc 地址根本构造不出来")
    void bug_getInvokerCannotTakeAnRpcUrl() {
        MalformedURLException thrown = assertThrows(MalformedURLException.class,
                () -> factory.getInvoker(new GreeterImpl(), Greeter.class,
                        jdkUrl("ttc-rpc", "/")));
        assertTrue(thrown.getMessage().contains("unknown protocol"), thrown.getMessage());

        // 猎物：换一个 JDK 认识的协议就通了，说明缺的只是签名里的类型
        try {
            assertNotNull(factory.getInvoker(new GreeterImpl(), Greeter.class, jdkUrl("http", "/")));
        } catch (Exception e) {
            throw new AssertionError("http 协议应当可构造", e);
        }
    }

    @Test
    @DisplayName("bug_转换后的 URL 只剩协议/主机/端口：接口名、版本、参数全丢")
    void bug_getInvokerDropsEverythingButHostAndPort() throws Throwable {
        Invoker<Greeter> invoker = factory.getInvoker(new GreeterImpl(), Greeter.class,
                jdkUrl("http", "/com.Foo?timeout=5000&version=2.0.0"));
        URL converted = invoker.getUrl();
        assertEquals("http", converted.getProtocol(), "协议被 java.net.URL 限定，改不出 ttc-rpc");
        assertEquals("127.0.0.1", converted.getHost());
        assertEquals(20880, converted.getPort());
        assertNull(converted.getServiceInterface(),
                "prey：type 明明传进来了，却没被写进 URL —— 这个 Invoker 无法用于路由或注册");
        assertTrue(converted.getParameters().isEmpty(),
                "query 上的 timeout/version 一个都没进 parameters: " + converted.getParameters());
        assertNull(converted.getVersion());
    }

    @Test
    @DisplayName("getInvoker 传 null URL 不炸，URL 就是 null")
    void nullJdkUrlYieldsNullUrl() {
        Invoker<Greeter> invoker = factory.getInvoker(new GreeterImpl(), Greeter.class, null);
        assertNull(invoker.getUrl());
    }

    @Test
    @DisplayName("getProxy 与 getInvoker 可以拼成一条完整本地链路")
    void proxyAndReflectiveInvokerCompose() throws Throwable {
        Invoker<Greeter> invoker = factory.getInvoker(new GreeterImpl(), Greeter.class, jdkUrl("http", "/"));
        Greeter proxy = factory.getProxy(invoker);
        assertEquals("hi local", proxy.hello("local"));
        assertEquals("HEY", proxy.shout());
        assertTrue(proxy.equals("yes"), "equals(String) 走业务实现，返回 true");
        assertFalse(proxy.equals("no"));
    }

    // ==================== SPI 声明 ====================

    @Test
    @DisplayName("ProxyFactory 的默认扩展名 jdk 解析到 JdkProxyFactory 且是单例")
    void jdkIsTheDefaultProxyFactory() {
        com.taotao.cloud.rpc.common.spi.ExtensionLoader<ProxyFactory> loader =
                com.taotao.cloud.rpc.common.spi.ExtensionLoader.getExtensionLoader(ProxyFactory.class);
        assertEquals("jdk", ProxyFactory.class.getAnnotation(com.taotao.cloud.rpc.common.spi.SPI.class).value());
        assertSame(loader.getDefaultExtension(), loader.getExtension("jdk"));
        assertEquals(JdkProxyFactory.class, loader.getDefaultExtension().getClass());
    }
}
