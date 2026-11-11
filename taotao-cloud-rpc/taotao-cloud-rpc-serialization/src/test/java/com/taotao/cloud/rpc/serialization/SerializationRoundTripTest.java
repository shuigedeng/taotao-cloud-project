package com.taotao.cloud.rpc.serialization;

import com.taotao.cloud.rpc.common.ProtocolConstants;
import com.taotao.cloud.rpc.common.RpcException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.Serializable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 5 个 {@link Serialization} 实现 + {@link SerializationFactory} 的实测行为。
 * <p>
 * 断言全部来自一次独立探针运行（{@code /tmp/ttcrpc_probe/SerProbe.java}），不是设想。
 */
class SerializationRoundTripTest {

    public static class Pojo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int count;
        private List<String> tags;
        private Map<String, String> attrs;

        public Pojo() {}

        public Pojo(String name, int count, List<String> tags, Map<String, String> attrs) {
            this.name = name;
            this.count = count;
            this.tags = tags;
            this.attrs = attrs;
        }

        public String getName() { return name; }
        public void setName(String v) { this.name = v; }
        public int getCount() { return count; }
        public void setCount(int v) { this.count = v; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> v) { this.tags = v; }
        public Map<String, String> getAttrs() { return attrs; }
        public void setAttrs(Map<String, String> v) { this.attrs = v; }

        @Override public boolean equals(Object o) {
            if (!(o instanceof Pojo)) return false;
            Pojo p = (Pojo) o;
            return p.count == count && Objects.equals(p.name, name)
                    && Objects.equals(p.tags, tags) && Objects.equals(p.attrs, attrs);
        }

        @Override public int hashCode() { return Objects.hash(name, count, tags, attrs); }
    }

    private static Pojo sample() {
        Map<String, String> attrs = new LinkedHashMap<>();
        attrs.put("k", "v");
        return new Pojo("订单🚀", 7, new ArrayList<>(Arrays.asList("a", "b")), attrs);
    }

    // ====================== 四个可用的实现 ======================

    @ParameterizedTest(name = "{0} 能把 POJO 原样往返")
    @ValueSource(strings = {"java", "json", "hessian2", "kryo"})
    void pojoRoundTrip(String name) {
        Serialization s = SerializationFactory.getByName(name);
        assertNotNull(s, name + " 应当已注册");
        Pojo in = sample();
        byte[] bytes = s.serialize(in);
        assertTrue(bytes.length > 0, name + " 序列化出空数组");
        Pojo back = s.deserialize(bytes, Pojo.class);
        assertEquals(in, back, name + " 往返后字段丢失/变形");
        assertEquals(7, back.getCount());
        assertEquals(Arrays.asList("a", "b"), back.getTags());
        assertEquals("v", back.getAttrs().get("k"));
    }

    @ParameterizedTest(name = "{0} 能把 String 原样往返")
    @ValueSource(strings = {"java", "json", "hessian2", "kryo"})
    void stringRoundTrip(String name) {
        Serialization s = SerializationFactory.getByName(name);
        for (String v : new String[]{"hello", "", "中文😀", "a\tb\nc"}) {
            assertEquals(v, s.deserialize(s.serialize(v), String.class), name + " -> " + v);
        }
    }

    @ParameterizedTest(name = "{0} 能把 Map/List 容器原样往返")
    @ValueSource(strings = {"java", "hessian2", "kryo"})
    void containerRoundTrip(String name) {
        Serialization s = SerializationFactory.getByName(name);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("list", Arrays.asList(1, 2, 3));
        m.put("nested", sample());
        byte[] bytes = s.serialize(m);
        Object back = s.deserialize(bytes, LinkedHashMap.class);
        assertNotNull(back, name + " 反序列化返回 null");
        assertTrue(back instanceof Map, name + " -> " + back.getClass());
        assertEquals(2, ((Map<?, ?>) back).size());
        assertEquals(Arrays.asList(1, 2, 3), ((Map<?, ?>) back).get("list"), name + " 的 List 变形了");
    }

    @Test
    @DisplayName("bug_json 把 Map 序列化后反序列化成空 Map：条目静默丢失")
    void bug_jsonDropsMapEntries() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("list", Arrays.asList(1, 2, 3));
        m.put("nested", sample());
        byte[] bytes = SerializationFactory.getByName("json").serialize(m);
        assertTrue(bytes.length > 0, "至少有字节，否则这条用例就是空跑");
        Object back = SerializationFactory.getByName("json").deserialize(bytes, LinkedHashMap.class);
        assertNotNull(back);
        assertTrue(back instanceof Map, String.valueOf(back.getClass()));
        assertEquals(0, ((Map<?, ?>) back).size(),
                "JSON 走的是「按目标类型 new 实例」的路线，Map 的键值根本没被填回去");
        // 猎物：其余三个实现在同一份字节语义下都能拿回 2 个条目
        for (String other : new String[]{"java", "hessian2", "kryo"}) {
            Serialization s = SerializationFactory.getByName(other);
            Map<?, ?> b = s.deserialize(s.serialize(m), LinkedHashMap.class);
            assertEquals(2, b.size(), other + " 对照失败");
        }
        // POJO 路径反倒是好的 —— 丢数据只发生在容器目标类型上
        assertEquals(sample(), SerializationFactory.getByName("json")
                .deserialize(SerializationFactory.getByName("json").serialize(sample()), Pojo.class));
    }

    @ParameterizedTest(name = "{0} 的 contentTypeId 与 ProtocolConstants 一致")
    @ValueSource(strings = {"java", "json", "hessian2", "kryo", "protobuf"})
    void contentTypeIdsMatchProtocolConstants(String name) {
        Serialization s = SerializationFactory.getByName(name);
        assertNotNull(s);
        if ("java".equals(name)) assertEquals(ProtocolConstants.SERIALIZE_JAVA, s.getContentTypeId());
        if ("json".equals(name)) assertEquals(ProtocolConstants.SERIALIZE_JSON, s.getContentTypeId());
        if ("hessian2".equals(name)) assertEquals(ProtocolConstants.SERIALIZE_HESSIAN2, s.getContentTypeId());
        if ("kryo".equals(name)) assertEquals(ProtocolConstants.SERIALIZE_KRYO, s.getContentTypeId());
        if ("protobuf".equals(name)) assertEquals(ProtocolConstants.SERIALIZE_PROTOBUF, s.getContentTypeId());
    }

    @Test
    @DisplayName("deserialize(bytes, 类名字符串) 的默认方法对四个实现有效")
    void defaultDeserializeByTypeName() {
        for (String name : new String[]{"java", "json", "hessian2", "kryo"}) {
            Serialization s = SerializationFactory.getByName(name);
            Object back = s.deserialize(s.serialize(sample()), Pojo.class.getName());
            assertEquals(sample(), back, name + " 走 String 重载结果不同");
        }
    }

    // ====================== protobuf ======================

    @Test
    @DisplayName("bug_protobuf 只接受 protobuf.MessageLite，任何普通对象都抛 RpcException")
    void bug_protobufRejectsEverythingOrdinary() {
        Serialization s = SerializationFactory.getByName("protobuf");
        assertNotNull(s);
        RpcException e = assertThrows(RpcException.class, () -> s.serialize(sample()));
        assertTrue(e.getMessage().contains("only supports com.google.protobuf.MessageLite"), e.getMessage());
        // 猎物：同一时刻其余四个实现都吃这个对象
        for (String other : new String[]{"java", "json", "hessian2", "kryo"}) {
            assertNotNull(SerializationFactory.getByName(other).serialize(sample()), other);
        }
        assertThrows(RpcException.class, () -> s.serialize("hello"));
        assertThrows(RpcException.class, () -> s.serialize(42));
    }

    @Test
    @DisplayName("bug_protobuf 对 null 却绕过类型检查，返回空数组")
    void bug_protobufNullBypassesTypeCheck() {
        Serialization proto = SerializationFactory.getByName("protobuf");
        assertArrayEquals(new byte[0], proto.serialize(null),
                "null 不进 MessageLite 校验，protobuf 与其余实现同样返回空数组");
        assertThrows(RpcException.class, () -> proto.serialize("x"),
                "而非 null 的普通对象才触发校验 —— 同一个实现对 null/非 null 两套规则");
    }

    @ParameterizedTest(name = "{0} serialize(null) 得到空数组")
    @ValueSource(strings = {"java", "json", "hessian2", "kryo", "protobuf"})
    void serializeNullYieldsEmptyArray(String name) {
        assertEquals(0, SerializationFactory.getByName(name).serialize(null).length);
    }

    // ====================== SerializationFactory ======================

    @Test
    @DisplayName("已注册的 1..5 号各自映射到正确实现")
    void knownIdsMapToTheirImpl() {
        assertEquals(JavaSerialization.class, SerializationFactory.get(ProtocolConstants.SERIALIZE_JAVA).getClass());
        assertEquals(JsonSerialization.class, SerializationFactory.get(ProtocolConstants.SERIALIZE_JSON).getClass());
        assertEquals(Hessian2Serialization.class,
                SerializationFactory.get(ProtocolConstants.SERIALIZE_HESSIAN2).getClass());
        assertEquals(KryoSerialization.class, SerializationFactory.get(ProtocolConstants.SERIALIZE_KRYO).getClass());
        assertEquals(ProtobufSerialization.class,
                SerializationFactory.get(ProtocolConstants.SERIALIZE_PROTOBUF).getClass());
    }

    @Test
    @DisplayName("实例是工厂级单例：同一 id 两次取到同一对象")
    void factoryHandsOutCachedInstances() {
        assertSame(SerializationFactory.get((byte) 3), SerializationFactory.get((byte) 3));
        assertSame(SerializationFactory.getByName("kryo"), SerializationFactory.getByName("kryo"));
        assertSame(SerializationFactory.get((byte) 3), SerializationFactory.getByName("hessian2"));
    }

    @Test
    @DisplayName("未知 id 直接报错，不再静默回落到 Hessian2")
    void unknownIdFailsFastInsteadOfFallingBack() {
        // 猎物：已注册的 id 照旧拿得到
        assertEquals(Hessian2Serialization.class,
                SerializationFactory.get(ProtocolConstants.SERIALIZE_HESSIAN2).getClass());
        for (byte id : new byte[]{0, 6, 99}) {
            RpcException e = assertThrows(RpcException.class, () -> SerializationFactory.get(id),
                    "id 0x" + String.format("%02X", id) + " 没有人注册过，必须炸而不是回落");
            assertTrue(e.getMessage().contains(String.format("0x%02X", id)),
                    "报错里要带上出错的那个 id: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("未知 name / null name 立刻报错，不再返回 null 让调用方炸 NPE")
    void unknownNameFailsFastInsteadOfReturningNull() {
        // 猎物：合法名字都拿得到实例
        assertNotNull(SerializationFactory.getByName("java"));
        assertThrows(RpcException.class, () -> SerializationFactory.getByName("protostuff"));
        assertThrows(RpcException.class, () -> SerializationFactory.getByName(""));
        assertThrows(IllegalArgumentException.class, () -> SerializationFactory.getByName(null));
        // ttc.rpc.consumer.serialization=thrift 这类拼写错误：在取用点就报"名字不认识 + 支持哪些"，
        // 而不是把 NPE 抛在第一次解引用上
        RpcException e = assertThrows(RpcException.class, () -> SerializationFactory.getByName("thrift"));
        assertTrue(e.getMessage().contains("thrift"), e.getMessage());
        assertTrue(e.getMessage().contains("hessian2"), "报错应列出可用名字: " + e.getMessage());
    }

    @Test
    @DisplayName("自定义实现可以被 register 进去，且按 id 覆盖")
    void customSerializationCanBeRegistered() {
        Serialization custom = new Serialization() {
            @Override public byte getContentTypeId() { return 77; }
            @Override public byte[] serialize(Object obj) { return new byte[]{9, 9}; }
            @Override public <T> T deserialize(byte[] bytes, Class<T> type) { return null; }
        };
        SerializationFactory.register(custom);
        assertSame(custom, SerializationFactory.get((byte) 77));
        assertArrayEquals(new byte[]{9, 9}, SerializationFactory.get((byte) 77).serialize("anything"));
    }

    @Test
    @DisplayName("Serialization 已补 @SPI：五个名字都能经 ExtensionLoader 加载，默认 hessian2")
    void serializationIsSpiLoadable() {
        com.taotao.cloud.rpc.common.spi.ExtensionLoader<Serialization> loader =
                com.taotao.cloud.rpc.common.spi.ExtensionLoader.getExtensionLoader(Serialization.class);
        assertTrue(loader.getExtensionNames().containsAll(
                Arrays.asList("java", "json", "hessian2", "kryo", "protobuf")), loader.getExtensionNames().toString());
        assertEquals("hessian2", Serialization.class.getAnnotation(com.taotao.cloud.rpc.common.spi.SPI.class).value());
        assertEquals(Hessian2Serialization.class, loader.getDefaultExtension().getClass());
        assertSame(loader.getExtension("kryo"), SerializationFactory.getByName("kryo"),
                "工厂改走 SPI 装载之后，两边可以断到同一个对象（修复前只能断同类）");
    }

    @Test
    @DisplayName("两张注册表合流：ExtensionLoader 与 SerializationFactory 对每个名字都给同一实例")
    void theTwoRegistriesHandOutTheSameInstances() {
        com.taotao.cloud.rpc.common.spi.ExtensionLoader<Serialization> loader =
                com.taotao.cloud.rpc.common.spi.ExtensionLoader.getExtensionLoader(Serialization.class);
        for (String name : new String[]{"java", "json", "hessian2", "kryo", "protobuf"}) {
            Serialization viaLoader = loader.getExtension(name);
            assertSame(viaLoader, loader.getExtension(name), "prey：loader 自己是单例缓存");
            assertSame(viaLoader, SerializationFactory.getByName(name), name + " 经两条路必须同一实例");
            assertSame(viaLoader, SerializationFactory.get(viaLoader.getContentTypeId()),
                    name + " 按 id 取也必须回到同一个实例");
        }
    }

    @Test
    @DisplayName("getDefaultLoadBalance 式的默认实现：hessian2 是协议默认序列化")
    void hessian2IsTheProtocolDefault() {
        assertEquals("hessian2", ProtocolConstants.DEFAULT_SERIALIZATION);
        Serialization def = SerializationFactory.getByName(ProtocolConstants.DEFAULT_SERIALIZATION);
        assertEquals(Hessian2Serialization.class, def.getClass());
        assertEquals(ProtocolConstants.SERIALIZE_HESSIAN2, def.getContentTypeId());
    }
}
