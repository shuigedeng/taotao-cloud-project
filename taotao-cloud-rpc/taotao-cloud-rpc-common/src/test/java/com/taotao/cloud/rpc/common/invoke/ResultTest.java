package com.taotao.cloud.rpc.common.invoke;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link Result} 契约：值/异常二选一、attachment 读写、异步 API 面、以及
 * "接口声明 Serializable" 这句话到底兑现到什么程度。
 */
class ResultTest {

    private static byte[] write(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.close();
        return bos.toByteArray();
    }

    private static Object read(byte[] bytes) throws Exception {
        return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
    }

    // ==================== success / error ====================

    @Test
    @DisplayName("success 只带值：hasException 为假，getException 为 null")
    void successCarriesOnlyValue() {
        Result r = Result.success("payload");
        assertEquals("payload", r.getValue());
        assertFalse(r.hasException());
        assertNull(r.getException());
    }

    @Test
    @DisplayName("error 只带异常：value 保持 null，异常按对象身份保留")
    void errorCarriesOnlyException() {
        IllegalStateException boom = new IllegalStateException("boom");
        Result r = Result.error(boom);
        assertTrue(r.hasException());
        assertSame(boom, r.getException());
        assertNull(r.getValue(), "错误结果的 value 必须是 null，不能让调用方拿到半成品");
    }

    @Test
    @DisplayName("两个工厂都产出 RpcResult，且就是 Serializable 的那个实现")
    void factoriesHandOutTheSameImplementationClass() {
        assertEquals(Result.RpcResult.class, Result.success("x").getClass());
        assertEquals(Result.RpcResult.class, Result.error(new RuntimeException()).getClass());
        assertTrue(Result.success("x") instanceof java.io.Serializable);
    }

    @Test
    @DisplayName("value 与 exception 可以同时存在，此时 hasException 优先")
    void exceptionWinsWhenBothAreSet() {
        Result.RpcResult r = new Result.RpcResult("value");
        r.setException(new IllegalStateException("boom"));
        assertEquals("value", r.getValue(), "setValue 侧 unaffected");
        assertTrue(r.hasException(), "代理层只看 hasException，异常必须优先");
    }

    @Test
    @DisplayName("bug_构造器重载陷阱：服务真的返回一个 Throwable 时，new RpcResult(thr) 把它变成异常结果")
    void bug_throwableValueBecomesErrorResult() {
        Throwable returned = new IllegalStateException("this-is-a-return-value");
        Result viaInference = new Result.RpcResult(returned);
        assertTrue(viaInference.hasException(),
                "prey：静态类型是 Throwable 时选中 RpcResult(Throwable) 重载");
        assertNull(viaInference.getValue());

        Result viaCast = new Result.RpcResult((Object) returned);
        assertFalse(viaCast.hasException());
        assertSame(returned, viaCast.getValue(), "强转成 Object 才是『当值返回』");

        Result viaFactory = Result.success(returned);
        assertFalse(viaFactory.hasException(), "工厂方法形参是 Object，所以没有这个歧义");
        assertSame(returned, viaFactory.getValue());
    }

    // ==================== attachments ====================

    @Test
    @DisplayName("attachment：默认值只在键缺失时生效")
    void attachmentDefaultsApplyToMissingKeys() {
        Result.RpcResult r = new Result.RpcResult("v");
        assertNull(r.getAttachment("missing"));
        assertEquals("d", r.getAttachment("missing", "d"));
        r.getAttachments().put("k", "1");
        assertEquals("1", r.getAttachment("k"));
        assertEquals("1", r.getAttachment("k", "d"), "已有值时默认值不得插手");
    }

    @Test
    @DisplayName("bug_attachment 显式置 null 后，带默认值的读取拿不到默认值")
    void bug_explicitNullAttachmentDefeatsTheDefault() {
        Result.RpcResult r = new Result.RpcResult("v");
        r.getAttachments().put("traceId", null);
        assertNull(r.getAttachment("traceId", "generated"),
                "Map.getOrDefault 对『键存在但值为 null』返回 null，不是默认值");
        // 猎物：真正缺失的键走默认值
        assertEquals("generated", r.getAttachment("absent", "generated"));
    }

    @Test
    @DisplayName("getAttachments 返回活表：调用方能改写结果内部状态")
    void attachmentsAreTheLiveMap() {
        Result.RpcResult r = new Result.RpcResult("v");
        Map<String, String> seen = r.getAttachments();
        seen.put("injected", "yes");
        assertEquals("yes", r.getAttachment("injected"),
                "这是当前契约（filter 侧依赖它写回），本用例钉住而不是主张修改");
    }

    @Test
    @DisplayName("RpcResult 无参构造也是可用的空结果")
    void noArgCtorYieldsEmptyResult() {
        Result.RpcResult r = new Result.RpcResult();
        assertFalse(r.hasException());
        assertNull(r.getValue());
        assertTrue(r.getAttachments().isEmpty());
    }

    // ==================== 异步 API 面 ====================

    @Test
    @DisplayName("getRecursionResult 返回自身，getResultFuture 已完成且就是自身")
    void recursionAndFutureAreTheSameObject() {
        Result.RpcResult r = new Result.RpcResult("v");
        assertSame(r, r.getRecursionResult());
        CompletableFuture<Result> f = r.getResultFuture();
        assertTrue(f.isDone());
        assertSame(r, f.join());
    }

    @Test
    @DisplayName("bug_isAsync 在整个 api 层恒为 false：异步契约只有声明没有实现")
    void bug_asyncContractHasNoImplementation() {
        assertFalse(Result.success("v").isAsync());
        assertFalse(new Result.RpcResult(new RuntimeException()).isAsync());

        // 猎物：接口本身允许异步实现 —— 缺的不是能力而是这个实现类。
        Result handRolled = new Result() {
            @Override public Object getValue() { return null; }
            @Override public Throwable getException() { return null; }
            @Override public boolean hasException() { return false; }
            @Override public Map<String, String> getAttachments() { return new HashMap<String, String>(); }
            @Override public String getAttachment(String key) { return null; }
            @Override public String getAttachment(String key, String defaultValue) { return defaultValue; }
            @Override public Result getRecursionResult() { return this; }
            @Override public CompletableFuture<Result> getResultFuture() { return new CompletableFuture<Result>(); }
            @Override public boolean isAsync() { return true; }
        };
        assertTrue(handRolled.isAsync(), "prey：isAsync 是可以为真的");
        assertFalse(handRolled.getResultFuture().isDone(), "prey：未完成的 future 也表达得出来");
        // 而生产代码里唯一的实现 RpcResult 永远回不到这条路上。
        assertFalse(Result.success("v").isAsync());
    }

    // ==================== Serializable ====================

    @Test
    @DisplayName("Java 序列化往返保住值与 attachment")
    void javaRoundTripKeepsValueAndAttachments() throws Exception {
        Result.RpcResult r = new Result.RpcResult("payload");
        r.getAttachments().put("traceId", "t-1");
        Result back = (Result) read(write(r));
        assertEquals("payload", back.getValue());
        assertEquals("t-1", back.getAttachment("traceId"));
        assertFalse(back.hasException());
    }

    @Test
    @DisplayName("异常结果序列化后仍是同一异常类型与消息")
    void javaRoundTripKeepsException() throws Exception {
        Result r = Result.error(new IOException("net-down"));
        Result back = (Result) read(write(r));
        assertTrue(back.hasException());
        assertEquals(IOException.class, back.getException().getClass());
        assertEquals("net-down", back.getException().getMessage());
    }
}
