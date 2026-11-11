package com.taotao.cloud.rpc.remoting;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 带版本注册的查找链路：{@link RpcServer#register(Class, Object, String)} 写键，
 * {@link RpcServerHandler} 读键，两侧必须拼出同一个键。
 * <p>
 * 走的是真实的 {@code RpcServerHandler.channelRead0}，不是直接查 map ——
 * 注册成功而查找不到的缺陷只有在跨过这条边界时才看得见。
 */
class RpcServerVersionKeyTest {

    interface Greeter {
        String hello();
    }

    static final class V234 implements Greeter {
        public String hello() {
            return "v234";
        }
    }

    static final class V567 implements Greeter {
        public String hello() {
            return "v567";
        }
    }

    static final class Plain implements Greeter {
        public String hello() {
            return "plain";
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> liveServiceMap(RpcServer server) throws Exception {
        Field f = RpcServer.class.getDeclaredField("serviceMap");
        f.setAccessible(true);
        return (Map<String, Object>) f.get(server);
    }

    private static RpcRequest request(String version) {
        RpcRequest req = new RpcRequest();
        req.setRequestId("req-" + version);
        req.setInterfaceName(Greeter.class.getName());
        req.setMethodName("hello");
        req.setParameterTypes(new Class<?>[0]);
        req.setArguments(new Object[0]);
        if (version != null) {
            req.getAttachments().put("version", version);
        }
        return req;
    }

    /** 用一个共享同一张活表的 handler 完成一次调用。 */
    private static RpcResponse invoke(Map<String, Object> serviceMap, RpcRequest req) {
        EmbeddedChannel channel = new EmbeddedChannel(new RpcServerHandler(serviceMap));
        channel.writeInbound(req);
        RpcResponse resp = channel.readOutbound();
        channel.finishAndReleaseAll();
        return resp;
    }

    @Test
    @DisplayName("serviceKey：有版本拼 name:version，无版本退回裸接口名")
    void serviceKeyFormatting() {
        assertEquals("com.Foo:1.0.0", RpcServer.serviceKey("com.Foo", "1.0.0"));
        assertEquals("com.Foo", RpcServer.serviceKey("com.Foo", null));
        assertEquals("com.Foo", RpcServer.serviceKey("com.Foo", ""));
    }

    @Test
    @DisplayName("同接口两个版本各占一键，按请求版本精确路由")
    void twoVersionsRouteIndependently() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        Map<String, Object> map = liveServiceMap(server);

        V234 a = new V234();
        V567 b = new V567();
        server.register(Greeter.class, a, "2.3.4");
        server.register(Greeter.class, b, "5.6.7");

        assertEquals(2, map.size(), "每个版本各占一键: " + map.keySet());
        assertFalse(map.containsKey(Greeter.class.getName()),
                "带版本注册不应同时占用裸键: " + map.keySet());

        RpcResponse r1 = invoke(map, request("2.3.4"));
        assertFalse(r1.hasException(), "实际异常: " + r1.getErrorMessage());
        assertEquals("v234", r1.getResult());

        RpcResponse r2 = invoke(map, request("5.6.7"));
        assertFalse(r2.hasException(), "实际异常: " + r2.getErrorMessage());
        assertEquals("v567", r2.getResult());

        // 后注册的 5.6.7 不得顶掉先注册的 2.3.4 —— 这正是修复前的行为
        assertSame(a, map.get(Greeter.class.getName() + ":2.3.4"));
    }

    @Test
    @DisplayName("请求不带 version 附件时按默认 1.0.0 查找，命不中带其他版本的 Provider")
    void defaultVersionDoesNotMatchOtherVersions() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        Map<String, Object> map = liveServiceMap(server);
        server.register(Greeter.class, new V234(), "2.3.4");

        RpcRequest req = request(null);
        assertEquals("1.0.0", req.getVersion(),
                "prey：RpcRequest.getVersion() 缺省返回 \"1.0.0\"，不是 null");

        RpcResponse resp = invoke(map, req);
        assertTrue(resp.hasException(), "只有 2.3.4 在线，默认版本请求不应被放行");
        assertTrue(resp.getErrorMessage().contains("Service not found"),
                "实际: " + resp.getErrorMessage());
    }

    @Test
    @DisplayName("不带版本注册的 Provider 可被默认版本请求经裸键退回命中")
    void unversionedProviderIsReachable() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        Map<String, Object> map = liveServiceMap(server);
        server.registerService(Greeter.class, new Plain());
        assertEquals(1, map.size());
        assertTrue(map.containsKey(Greeter.class.getName()));

        RpcResponse resp = invoke(map, request(null));
        assertFalse(resp.hasException(), "实际异常: " + resp.getErrorMessage());
        assertEquals("plain", resp.getResult());
    }

    @Test
    @DisplayName("register 传 null/空版本等价于不带版本注册")
    void nullVersionFallsBackToBareKey() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        Map<String, Object> map = liveServiceMap(server);
        server.register(Greeter.class, new Plain(), null);
        server.register(Greeter.class, new Plain(), "");
        assertEquals(1, map.size(), "实际: " + map.keySet());
        assertNull(map.get(Greeter.class.getName() + ":null"));
        assertEquals("plain", invoke(map, request("1.0.0")).getResult());
    }

    @Test
    @DisplayName("裸键注册只替缺省版本应答，不得替该接口的所有版本冒充应答")
    void bareRegistrationDoesNotImpersonateEveryVersion() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        Map<String, Object> map = liveServiceMap(server);
        server.registerService(Greeter.class, new Plain());
        server.register(Greeter.class, new V234(), "2.3.4");

        assertEquals("v234", invoke(map, request("2.3.4")).getResult(), "prey：精确键这一路照常");

        RpcResponse wrong = invoke(map, request("8.8.8"));
        assertTrue(wrong.hasException(),
                "裸键条目把任意版本都接走了，实收 result=" + wrong.getResult());
        assertTrue(String.valueOf(wrong.getErrorMessage()).contains("Service not found"),
                String.valueOf(wrong.getErrorMessage()));

        // 缺省版本这一路必须留着：不带 version 附件的请求在服务端读出来就是 "1.0.0"，
        // 与"指名要 1.0.0"不可区分；拒掉它 = 裸键 Provider 对所有不设版本的消费端不可达。
        assertEquals("plain", invoke(map, request(null)).getResult(),
                "缺省版本请求仍应命中裸键条目");

        // 该接口没有任何带版本登记时（没有别的候选），退回裸键这条路仍然生效
        RpcServer bareOnly = new RpcServer("127.0.0.1", 0);
        Map<String, Object> bareMap = liveServiceMap(bareOnly);
        bareOnly.registerService(Greeter.class, new Plain());
        assertEquals("plain", invoke(bareMap, request("8.8.8")).getResult(),
                "没有别的候选时不该拦下退回");
    }

    @Test
    @DisplayName("getServiceMap() 返回的是快照副本，往里写不影响真表")
    void serviceMapAccessorReturnsCopy() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        Map<String, Object> live = liveServiceMap(server);
        server.register(Greeter.class, new V234(), "2.3.4");

        Map<String, Object> copy = server.getServiceMap();
        assertEquals(1, copy.size());
        copy.put("injected", new Plain());
        assertEquals(1, live.size(), "对外暴露的必须是副本，否则任何人都能改路由表");
        assertFalse(live.containsKey("injected"));
    }
}
