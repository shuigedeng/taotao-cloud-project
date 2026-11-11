package com.taotao.cloud.rpc.common.invoke;

import com.taotao.cloud.rpc.common.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link RpcInvocation} 与 {@link Invocation} 的默认实现契约：
 * 构造器的拷贝语义、attachment 驱动的 version/group、以及"Invocation extends Serializable"
 * 在 Java 序列化这条真实链路上兑现到什么程度。
 */
class RpcInvocationTest {

    private static byte[] write(Object o) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.close();
        return bos.toByteArray();
    }

    private static Object read(byte[] bytes) throws Exception {
        return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
    }

    private static RpcInvocation call() {
        return new RpcInvocation("com.Foo", "bar",
                new Class<?>[]{String.class, int.class}, new Object[]{"a", 7});
    }

    // ==================== 构造器 ====================

    @Test
    @DisplayName("四参构造把方法定位信息原样交出")
    void fourArgCtorKeepsTheCallShape() {
        RpcInvocation inv = call();
        assertEquals("com.Foo", inv.getServiceInterface());
        assertEquals("bar", inv.getMethodName());
        assertArrayEquals(new Class<?>[]{String.class, int.class}, inv.getParameterTypes());
        assertArrayEquals(new Object[]{"a", 7}, inv.getArguments());
        assertEquals("1.0.0", inv.getVersion(), "version 从 attachment 取，缺省 1.0.0");
        assertEquals("", inv.getGroup());
        assertNull(inv.getInvokerUrl());
    }

    @Test
    @DisplayName("五参构造拷贝 attachment 而不是别名整张表")
    void fiveArgCtorCopiesAttachments() {
        Map<String, String> src = new HashMap<String, String>();
        src.put("traceId", "t-1");
        RpcInvocation inv = new RpcInvocation("com.Foo", "bar", new Class<?>[0], new Object[0], src);
        src.put("late", "yes");
        assertEquals("t-1", inv.getAttachment("traceId"));
        assertNull(inv.getAttachment("late"), "外部表的后续修改不得漏进已发出的调用");
        assertNotSame(src, inv.getAttachments());
    }

    @Test
    @DisplayName("五参构造传 null 附件表等价于不传")
    void fiveArgCtorToleratesNullAttachments() {
        RpcInvocation inv = new RpcInvocation("com.Foo", "bar",
                new Class<?>[0], new Object[0], null);
        assertNotNull(inv.getAttachments());
        inv.setAttachment("k", "v");
        assertEquals("v", inv.getAttachment("k"));
    }

    @Test
    @DisplayName("bug_无参构造留下的 parameterTypes/arguments 是 null，不是空数组")
    void bug_defaultCtorLeavesNullArrays() {
        RpcInvocation inv = new RpcInvocation();
        assertNull(inv.getParameterTypes());
        assertNull(inv.getArguments());
        // 猎物：JdkProxyFactory 走的是 setter 路线，所以它构造出来的调用是安全的
        inv.setParameterTypes(new Class<?>[0]);
        inv.setArguments(new Object[0]);
        assertEquals(0, inv.getArguments().length);
        // 而任何直接 new RpcInvocation() 再读长度的代码，拿到 length 之前就炸了
        assertThrows(NullPointerException.class, () -> lengthOf(new RpcInvocation().getArguments()));
        assertThrows(NullPointerException.class,
                () -> lengthOf(new RpcInvocation().getParameterTypes()));
    }

    private static int lengthOf(Object[] arr) {
        return arr.length;
    }

    // ==================== attachment 与 version/group ====================

    @Test
    @DisplayName("version/group 就是两个 attachment 键，setAttachment 立刻改口")
    void versionAndGroupAreAttachmentKeys() {
        RpcInvocation inv = call();
        inv.setAttachment("version", "2.3.4");
        inv.setAttachment("group", "gray");
        assertEquals("2.3.4", inv.getVersion());
        assertEquals("gray", inv.getGroup());
    }

    @Test
    @DisplayName("RpcInvocation 覆写的 getVersion/getGroup 与接口默认实现同义")
    void overridesMatchTheInterfaceDefaults() {
        Invocation anon = new Invocation() {
            private final Map<String, String> att = new HashMap<String, String>();
            @Override public String getServiceInterface() { return "com.Foo"; }
            @Override public String getMethodName() { return "bar"; }
            @Override public Class<?>[] getParameterTypes() { return new Class<?>[0]; }
            @Override public Object[] getArguments() { return new Object[0]; }
            @Override public Map<String, String> getAttachments() { return att; }
            @Override public String getAttachment(String key) { return att.get(key); }
            @Override public String getAttachment(String key, String defaultValue) {
                return att.containsKey(key) ? att.get(key) : defaultValue;
            }
            @Override public void setAttachment(String key, String value) { att.put(key, value); }
            @Override public URL getInvokerUrl() { return null; }
        };
        assertEquals("1.0.0", anon.getVersion(), "接口 default 给的缺省");
        assertEquals("", anon.getGroup());
        anon.setAttachment("version", "9.9.9");
        assertEquals("9.9.9", anon.getVersion());

        RpcInvocation same = call();
        same.setAttachment("version", "9.9.9");
        assertEquals(anon.getVersion(), same.getVersion(), "两条实现必须给同一个答案");
    }

    @Test
    @DisplayName("bug_invokerUrl 上声明的版本不会进 invocation，getVersion 恒为缺省")
    void bug_urlVersionNeverReachesTheInvocation() {
        URL url = new URL("ttc-rpc", "127.0.0.1", 20880, "com.Foo");
        url.setVersion("5.0.0");
        url.setGroup("gray");
        RpcInvocation inv = new RpcInvocation("com.Foo", "bar", new Class<?>[0], new Object[0]);
        inv.setInvokerUrl(url);
        assertEquals("5.0.0", inv.getInvokerUrl().getVersion(), "URL 自己带着版本");
        assertEquals("1.0.0", inv.getVersion(),
                "prey：Invocation.getVersion() 只看 attachment，与它携带的 URL 脱钩");
        assertEquals("", inv.getGroup());
    }

    @Test
    @DisplayName("bug_setAttachments(null) 之后这张调用再也读不出也写不进附件")
    void bug_setAttachmentsNullBricksTheInvocation() {
        RpcInvocation inv = call();
        inv.setAttachments(null);
        assertNull(inv.getAttachments());
        assertThrows(NullPointerException.class, () -> inv.getAttachment("traceId"));
        assertThrows(NullPointerException.class, () -> inv.setAttachment("traceId", "t-2"));
        // 猎物：正常对象读写无碍
        RpcInvocation healthy = call();
        healthy.setAttachment("traceId", "t-1");
        assertEquals("t-1", healthy.getAttachment("traceId"));
        assertEquals("d", healthy.getAttachment("absent", "d"));
    }

    @Test
    @DisplayName("getAttachments 返回活表：filter 依赖它写回，因此就地修改会生效")
    void attachmentsAreTheLiveMap() {
        RpcInvocation inv = call();
        inv.getAttachments().put("tenant", "cn-south");
        assertEquals("cn-south", inv.getAttachment("tenant"));
    }

    // ==================== Serializable ====================

    @Test
    @DisplayName("Java 序列化往返保住定位信息、参数类型、附件与调用者 URL")
    void javaRoundTripKeepsEverything() throws Exception {
        RpcInvocation inv = call();
        inv.setAttachment("traceId", "t-9");
        inv.setInvokerUrl(new URL("ttc-rpc", "10.0.0.1", 20880, "com.Foo"));
        RpcInvocation back = (RpcInvocation) read(write(inv));
        assertEquals("com.Foo", back.getServiceInterface());
        assertEquals("bar", back.getMethodName());
        assertArrayEquals(new Class<?>[]{String.class, int.class}, back.getParameterTypes());
        assertArrayEquals(new Object[]{"a", 7}, back.getArguments());
        assertEquals("t-9", back.getAttachment("traceId"));
        assertEquals("10.0.0.1", back.getInvokerUrl().getHost());
    }

    @Test
    @DisplayName("序列化过 null 附件表的对象，反序列化后附件表仍是 null（字段初始化器不重跑）")
    void deserializationDoesNotRerunFieldInitializers() throws Exception {
        RpcInvocation broken = call();
        broken.setAttachments(null);
        RpcInvocation back = (RpcInvocation) read(write(broken));
        assertNull(back.getAttachments(),
                "反序列化绕过字段初始化器，attachments 保持 null");
        assertThrows(NullPointerException.class, () -> back.getAttachment("k"));

        // 猎物：只要没人显式塞 null，往返后的附件表就是可用的
        RpcInvocation healthy = (RpcInvocation) read(write(call()));
        healthy.setAttachment("k", "v");
        assertEquals("v", healthy.getAttachment("k"));
    }

    @Test
    @DisplayName("bug_业务参数不可序列化时整条调用在写线上才炸，签名层面毫无提示")
    void bug_nonSerializableArgumentBreaksTheContractAtWriteTime() {
        RpcInvocation inv = new RpcInvocation("com.Foo", "bar",
                new Class<?>[]{Object.class}, new Object[]{new Object() {
                }});
        assertThrows(NotSerializableException.class, () -> write(inv));

        // 猎物：Serializable 的参数走得通
        RpcInvocation ok = new RpcInvocation("com.Foo", "bar",
                new Class<?>[]{String.class}, new Object[]{"payload"});
        try {
            assertEquals("payload", ((RpcInvocation) read(write(ok))).getArguments()[0]);
        } catch (Exception e) {
            throw new AssertionError("String 参数应当可序列化", e);
        }
    }
}
