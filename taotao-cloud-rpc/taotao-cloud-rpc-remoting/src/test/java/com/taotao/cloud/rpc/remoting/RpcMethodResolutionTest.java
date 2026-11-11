package com.taotao.cloud.rpc.remoting;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 服务端怎么把一次请求解析成一个 {@link Method}。
 * <p>
 * {@link RpcServerHandler#findMethod} 是每一次远程调用的必经之路（先按名字加参数类型
 * 精确匹配，不中再按「名字 + 参数个数 + isAssignableFrom」扫一遍），而它此前一条用例都没有：
 * 链路上已有的用例全用无参或单参方法，恰好绕开了所有会产生歧义的路径。
 * <p>
 * 这里的 provider 一律直接写进路由表，写的是裸接口名键（与
 * {@link RpcServer#registerService(Class, Object)} 的写法一致）；带版本的键由
 * {@code RpcServerVersionKeyTest} 覆盖。被测的是「拿到方法名和一组参数类型之后怎么选方法」，
 * 与注册走哪个重载无关。
 */
class RpcMethodResolutionTest {

    interface Echo {
        String say(String what);
    }

    /** 只声明一个方法 —— 用来做「契约面」的对照。 */
    interface OneMethod {
        String ping();
    }

    interface Joiner {
        String join(String... parts);
    }

    interface Counted {
        long count(int n);
    }

    interface Defaults extends OneMethod {
        @Override
        default String ping() {
            return "default-ping";
        }

        default String greet() {
            return "greet:" + ping();
        }
    }

    /** 契约面只有一个 ping()，但身上还挂着别的方法。 */
    static final class OverEagerProvider implements OneMethod {
        @Override
        public String ping() {
            return "ping";
        }

        public String adminOnly() {
            return "secret";
        }

        private String hidden() {
            return "never";
        }

        @Override
        public String toString() {
            return "provider-toString";
        }
    }

    static class BaseProvider {
        public String inherited() {
            return "from-super";
        }
    }

    static final class DerivedProvider extends BaseProvider implements Echo {
        @Override
        public String say(String what) {
            return what == null ? "was-null" : "was:" + what;
        }
    }

    static final class NarrowOnly {
        public String say(String what) {
            return "narrow:" + what;
        }
    }

    /** 两个候选都收得下 String，而 provider 里没有 say(String) —— 精确匹配必不中。 */
    static final class AmbiguousOverloads {
        public String say(Object o) {
            return "object";
        }

        public String say(CharSequence s) {
            return "chars";
        }
    }

    static final class StringAndObject {
        public String say(String what) {
            return "string";
        }

        public String say(Object what) {
            return "object";
        }
    }

    static final class Varargs implements Joiner {
        @Override
        public String join(String... parts) {
            StringBuilder sb = new StringBuilder("join=");
            for (String p : parts) {
                sb.append(p).append(',');
            }
            return sb.toString();
        }
    }

    static final class IntCounter {
        public long count(int n) {
            return n * 2L;
        }
    }

    private static Map<String, Object> table(String iface, Object provider) {
        Map<String, Object> services = new ConcurrentHashMap<String, Object>();
        services.put(iface, provider);
        return services;
    }

    private static RpcResponse call(Map<String, Object> services, String iface, String method,
                                    Class<?>[] paramTypes, Object[] args) {
        RpcRequest req = new RpcRequest();
        req.setRequestId("req-" + method);
        req.setInterfaceName(iface);
        req.setMethodName(method);
        req.setParameterTypes(paramTypes);
        req.setArguments(args);
        EmbeddedChannel channel = new EmbeddedChannel(new RpcServerHandler(services));
        try {
            channel.writeInbound(req);
            return channel.<RpcResponse>readOutbound();
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    private static Set<String> publicMethodNames(Class<?> clazz) {
        Set<String> names = new HashSet<String>();
        for (Method m : clazz.getMethods()) {
            names.add(m.getName());
        }
        return names;
    }

    /**
     * 独立复算：按反射枚举顺序取第一个「名字对得上、参数个数相同且逐位可赋值」的候选，
     * 直接把它跑一遍得到应答。与被测代码不共用选择逻辑，但用的是同一份反射枚举。
     */
    private static String firstCandidateByReflectionOrder(Object provider, String methodName,
                                                          Class<?>[] paramTypes) {
        for (Method m : provider.getClass().getMethods()) {
            if (!m.getName().equals(methodName)) {
                continue;
            }
            Class<?>[] declared = m.getParameterTypes();
            if (declared.length != paramTypes.length) {
                continue;
            }
            boolean ok = true;
            for (int i = 0; i < declared.length; i++) {
                if (!declared[i].isAssignableFrom(paramTypes[i])) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                m.setAccessible(true);
                try {
                    return String.valueOf(m.invoke(provider, "x"));
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        return "<none>";
    }

    @Test
    @DisplayName("精确签名优先：同名的 String 与 Object 重载按请求声明的类型各走各的")
    void exactSignatureWins() {
        Map<String, Object> services = table(Echo.class.getName(), new StringAndObject());

        assertEquals("string", call(services, Echo.class.getName(), "say",
                new Class<?>[]{String.class}, new Object[]{"x"}).getResult());
        // 猎物：换成 Object 的声明类型，命中的就是另一个重载，而不是「随便哪个」
        assertEquals("object", call(services, Echo.class.getName(), "say",
                new Class<?>[]{Object.class}, new Object[]{new Object()}).getResult());
    }

    @Test
    @DisplayName("重载歧义时兜底按反射枚举顺序定胜负，且从不报歧义")
    void bug_overloadAmbiguityResolvedByReflectionOrder() {
        final AmbiguousOverloads provider = new AmbiguousOverloads();
        Map<String, Object> services = table(Echo.class.getName(), provider);

        // 前提：请求声明的 String 在这两个候选面前确实有歧义，两者都收得下
        assertTrue(Object.class.isAssignableFrom(String.class)
                        && CharSequence.class.isAssignableFrom(String.class),
                "本用例的歧义前提不成立");
        // 前提：两个候选给出的答案不同，所以「选了哪个」是可观察的，不是一句空话
        assertEquals("chars", provider.say((CharSequence) "x"));
        assertEquals("object", provider.say((Object) "x"));

        RpcResponse resp = call(services, Echo.class.getName(), "say",
                new Class<?>[]{String.class}, new Object[]{"x"});
        // 缺陷本体：有歧义也照样应答，绝不报「找不到唯一方法」
        assertFalse(resp.hasException(), "歧义请求被拒了，这条用例的量法就变了: " + resp.getErrorMessage());

        // 独立复算「反射枚举顺序里的第一个可赋值候选」，不许与被测代码共用选择逻辑。
        // 本机（JDK 25）实测这一支绑定的是 say(Object)，比 javac 的最小匹配宽一档；
        // 顺序本身不钉死 —— Class.getMethods() 的顺序本来就不由 JVM 规定，
        // 缺陷的表述正是「歧义由它规定胜负」。
        assertEquals(firstCandidateByReflectionOrder(provider, "say", new Class<?>[]{String.class}),
                resp.getResult(),
                "handler 的兜底结果必须等于反射顺序里的第一个候选，否则本用例量的不是这条路径");
    }

    @Test
    @DisplayName("bug_ provider 的参数比请求声明的更窄时直接判找不到，实参运行时类型合适也不行")
    void bug_narrowerProviderParameterIsUnreachable() {
        Map<String, Object> services = table(Echo.class.getName(), new NarrowOnly());

        assertEquals("narrow:x", call(services, Echo.class.getName(), "say",
                new Class<?>[]{String.class}, new Object[]{"x"}).getResult(),
                "prey：方法确实在表里、也确实打得通");

        // 实参的运行时类型就是 String，完全能进 say(String)，但匹配只看请求声明的类型
        RpcResponse resp = call(services, Echo.class.getName(), "say",
                new Class<?>[]{Object.class}, new Object[]{"x"});
        assertTrue(resp.hasException(), "实收 " + resp.getResult());
        assertTrue(String.valueOf(resp.getErrorMessage()).contains("Method not found"),
                "实际: " + resp.getErrorMessage());
    }

    @Test
    @DisplayName("bug_ 请求只要报得出方法名，provider 的任意 public 方法都能被远程调到")
    void bug_anyPublicMethodOfTheProviderIsReachable() {
        Map<String, Object> services = table(OneMethod.class.getName(), new OverEagerProvider());

        // 契约面只有一个 ping()：adminOnly 与 toString 都不在其中
        Set<String> contract = publicMethodNames(OneMethod.class);
        assertEquals(1, contract.size(), "接口契约方法集: " + contract);
        assertFalse(contract.contains("adminOnly"), "接口契约方法集: " + contract);

        assertEquals("ping", call(services, OneMethod.class.getName(), "ping",
                new Class<?>[0], new Object[0]).getResult(), "prey：契约内的方法照常");

        // 缺陷：adminOnly 不在任何接口上，只因为它是 provider 的 public 方法就能被调到
        assertEquals("secret", call(services, OneMethod.class.getName(), "adminOnly",
                new Class<?>[0], new Object[0]).getResult(),
                "接口里没有这个名字的方法，它照样被执行了");

        // 边界在 public 而不在契约：private 方法仍然进不来
        RpcResponse hidden = call(services, OneMethod.class.getName(), "hidden",
                new Class<?>[0], new Object[0]);
        assertTrue(hidden.hasException(), "private 方法被远程调到了？实收 " + hidden.getResult());
        assertTrue(String.valueOf(hidden.getErrorMessage()).contains("Method not found"),
                "实际: " + hidden.getErrorMessage());
    }

    @Test
    @DisplayName("bug_ Object 的方法也在可调用面上：远端能直接问 provider 要 toString 与 hashCode")
    void bug_objectMethodsAreRemotelyCallable() {
        Map<String, Object> services = table(OneMethod.class.getName(), new OverEagerProvider());
        Object provider = services.get(OneMethod.class.getName());
        assertFalse(publicMethodNames(OneMethod.class).contains("toString"),
                "toString 不在契约里");

        assertEquals("provider-toString",
                call(services, OneMethod.class.getName(), "toString",
                        new Class<?>[0], new Object[0]).getResult(),
                "接口上没有 toString，但它跑在了 provider 身上");

        // 猎物：hashCode 拿到的是这个实例自己的身份哈希，证明执行对象就是表里那个 provider
        assertEquals(Integer.valueOf(provider.hashCode()),
                call(services, OneMethod.class.getName(), "hashCode",
                        new Class<?>[0], new Object[0]).getResult());
    }

    @Test
    @DisplayName("接口的 default 方法在服务端执行，并按 provider 的覆写做动态分派")
    void defaultMethodsRunOnTheProvider() {
        Map<String, Object> bare = table(Defaults.class.getName(), new Defaults() {
        });
        assertEquals("default-ping", call(bare, Defaults.class.getName(), "ping",
                new Class<?>[0], new Object[0]).getResult(),
                "provider 什么都没覆写，执行的就是接口里的 default 方法体");
        assertEquals("greet:default-ping", call(bare, Defaults.class.getName(), "greet",
                new Class<?>[0], new Object[0]).getResult(),
                "greet 只存在于接口的 default 体里");

        Map<String, Object> loud = table(Defaults.class.getName(), new Defaults() {
            @Override
            public String ping() {
                return "loud-ping";
            }
        });
        assertEquals("greet:loud-ping", call(loud, Defaults.class.getName(), "greet",
                new Class<?>[0], new Object[0]).getResult(),
                "default 方法体里再调 ping()，用的是这个 provider 的覆写");
    }

    @Test
    @DisplayName("bug_ 基本类型与包装类型不可互换：按实参运行时类填参数类型的调用方必失败")
    void bug_primitiveAndWrapperAreNotInterchangeable() {
        Map<String, Object> services = table(Counted.class.getName(), new IntCounter());

        assertEquals(Long.valueOf(6L), call(services, Counted.class.getName(), "count",
                new Class<?>[]{int.class}, new Object[]{Integer.valueOf(3)}).getResult(),
                "prey：声明成 int 就打得通");

        RpcResponse boxed = call(services, Counted.class.getName(), "count",
                new Class<?>[]{Integer.class}, new Object[]{Integer.valueOf(3)});
        assertTrue(boxed.hasException(), "实收 " + boxed.getResult());
        assertTrue(String.valueOf(boxed.getErrorMessage()).contains("Method not found"),
                "实际: " + boxed.getErrorMessage());
    }

    @Test
    @DisplayName("参数类型整组为 null 时报找不到方法，而不是兜底扫描踩空的 NPE")
    void nullParameterTypesYieldMethodNotFound() {
        Map<String, Object> services = table(Echo.class.getName(), new DerivedProvider());

        assertEquals("from-super", call(services, Echo.class.getName(), "inherited",
                new Class<?>[0], new Object[0]).getResult(),
                "prey：签名齐着的调用照常，父类的 public 方法也在可调用面上");

        // 有同名方法的这一支是原来最容易踩空的：兜底扫描要比较参数个数，直接对 null 数组取长度
        RpcResponse hit = call(services, Echo.class.getName(), "say", null, new Object[]{"x"});
        assertTrue(hit.hasException(), "本该报错，实收 " + hit.getResult());
        assertEquals("Method not found: say", hit.getErrorMessage(),
                "实际: " + hit.getException());

        // 对照：没有同名方法的另一支，修之前后给出的是同一条消息
        RpcResponse miss = call(services, Echo.class.getName(), "noSuchThing", null, new Object[]{"x"});
        assertTrue(miss.hasException(), "本该报错，实收 " + miss.getResult());
        assertEquals("Method not found: noSuchThing", miss.getErrorMessage(),
                "同一个 null 入参，不该因为有没有同名方法就换成一种 NPE: " + miss.getErrorMessage());
    }

    @Test
    @DisplayName("参数类型数组里含 null 元素时报找不到方法，而不是 message 为 null 的 NPE")
    void nullEntryInParameterTypesYieldsMethodNotFound() {
        Map<String, Object> services = table(Echo.class.getName(), new DerivedProvider());

        assertEquals("was:x", call(services, Echo.class.getName(), "say",
                new Class<?>[]{String.class}, new Object[]{"x"}).getResult(), "prey：正常声明照常");

        RpcResponse resp = call(services, Echo.class.getName(), "say",
                new Class<?>[]{null}, new Object[]{"x"});
        assertTrue(resp.hasException(), "本该报错，实收 " + resp.getResult());
        assertEquals("Method not found: say", resp.getErrorMessage(),
                "修之前这条的 message 是 null，消费端什么也认不出: " + resp.getException());
        assertNotNull(resp.getErrorMessage(), "错误说明必须非空");
    }

    @Test
    @DisplayName("可变参数方法只能按数组类型命中，展开成单参就不认")
    void varargsNeedTheArrayType() {
        Map<String, Object> services = table(Joiner.class.getName(), new Varargs());

        assertEquals("join=a,b,", call(services, Joiner.class.getName(), "join",
                new Class<?>[]{String[].class}, new Object[]{new String[]{"a", "b"}}).getResult(),
                "prey：数组类型精确命中");

        RpcResponse spread = call(services, Joiner.class.getName(), "join",
                new Class<?>[]{String.class}, new Object[]{"a"});
        assertTrue(spread.hasException(), "展开成单参也命中的话这条用例就该红: " + spread.getResult());
        assertTrue(String.valueOf(spread.getErrorMessage()).contains("Method not found"),
                "实际: " + spread.getErrorMessage());
    }

    @Test
    @DisplayName("null 实参照样按声明类型解析，provider 里能区分 null 与空串")
    void nullArgumentMatchesDeclaredType() {
        Map<String, Object> services = table(Echo.class.getName(), new DerivedProvider());
        assertEquals("was-null", call(services, Echo.class.getName(), "say",
                new Class<?>[]{String.class}, new Object[]{null}).getResult());
        // 猎物：空串不是 null，两者在 provider 里走的是不同分支
        assertEquals("was:", call(services, Echo.class.getName(), "say",
                new Class<?>[]{String.class}, new Object[]{""}).getResult());
    }

    @Test
    @DisplayName("bug_ 找不到方法时报的只有方法名，签名与服务名都不提")
    void failureCarriesTheMethodNameNotTheSignature() {
        Map<String, Object> services = table(Echo.class.getName(), new DerivedProvider());

        RpcResponse resp = call(services, Echo.class.getName(), "noSuchThing",
                new Class<?>[0], new Object[0]);
        assertTrue(resp.hasException());
        assertNotNull(resp.getException(), "响应里必须带上异常对象");
        assertFalse(resp.getErrorMessage().contains(Echo.class.getName()),
                "错误消息点名的是方法名: " + resp.getErrorMessage());
        assertTrue(resp.getErrorMessage().contains("noSuchThing"),
                "实际: " + resp.getErrorMessage());
        assertNull(resp.getResult(), "出错时 result 必须留空: " + resp.getResult());

        // 猎物：名字对但签名不对，报的是同一条消息 —— 调用方分不出「没这个方法」和「签名不匹配」
        RpcResponse wrongSig = call(services, Echo.class.getName(), "say",
                new Class<?>[]{Integer.class}, new Object[]{Integer.valueOf(1)});
        assertTrue(wrongSig.hasException(), "实收 " + wrongSig.getResult());
        assertEquals("Method not found: say", wrongSig.getErrorMessage(),
                "消息里只有方法名: " + wrongSig.getErrorMessage());
        assertFalse(wrongSig.getErrorMessage().contains("Integer"),
                "签名不出现在回传的消息里: " + wrongSig.getErrorMessage());
    }
}
