package com.taotao.cloud.rpc.remoting;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@code ttc-rpc-remoting} 那套 10 字节帧的编解码器：{@link RpcMessageEncoder} 与
 * {@link RpcMessageDecoder}。
 * <p>
 * 这是线上真正在跑的格式（{@code RpcClient} 与 {@code RpcServer} 的 pipeline 里就是这两个
 * 类），此前一条直接用例都没有 —— 已有的覆盖全从真 socket 往返穿过来，而 socket 那一层永远
 * 只喂"对端刚编出去的那一帧"，所以半包、坏魔数、声明长度、类型字节这些只有恶意/损坏输入才
 * 走得到的分支从未被执行过。
 * <p>
 * 后半段顺着这条帧格式走到两端"编不出去时会发生什么"：服务端的编码失败、客户端等待方被谁叫醒、
 * 以及 {@link RpcClientHandler} 这个广告出去却没接线(public)的类。
 */
class RpcMessageCodecTest {

    static final byte[] MAGIC = "ttcrpc".getBytes(StandardCharsets.US_ASCII);
    static final int HEADER_SIZE = 10;
    static final int MAX_FRAME = 10 * 1024 * 1024;

    interface Greeter {
        String hello();

        Object unserializable();
    }

    static final class GreeterImpl implements Greeter {
        @Override
        public String hello() {
            return "hi";
        }

        @Override
        public Object unserializable() {
            return new Object();
        }
    }

    /** 反序列化时会留痕的类：证明"线上字节被真的构造过"。 */
    static final class Trigger implements Serializable {
        static final AtomicInteger HITS = new AtomicInteger();
        private static final long serialVersionUID = 1L;

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            HITS.incrementAndGet();
            in.defaultReadObject();
        }
    }

    /**
     * 把 protected 的 decode 暴露出来直接驱动。可以传 null 的 ctx：这个实现的 decode 一次都
     * 没有用到 ctx（这本身也是"它没有走 ByteToMessageDecoder 那条路"的一部分，见下面那条）。
     */
    static final class Probe extends RpcMessageDecoder {
        Object decodeFrame(ByteBuf in) throws Exception {
            return super.decode(null, in);
        }
    }

    static RpcRequest request(String id) {
        // parameterTypes 故意与 Greeter#hello() 不一致：这份模型只用来过编解码，
        // 要拿它去真调用的地方一律用 {@link #invocable(String)}。
        RpcRequest req = new RpcRequest();
        req.setRequestId(id);
        req.setInterfaceName(Greeter.class.getName());
        req.setMethodName("hello");
        req.setParameterTypes(new Class<?>[]{String.class});
        req.setArguments(new Object[]{"x"});
        req.getAttachments().put("version", "9.9.9");
        return req;
    }

    static RpcRequest invocable(String id) {
        RpcRequest req = new RpcRequest();
        req.setRequestId(id);
        req.setInterfaceName(Greeter.class.getName());
        req.setMethodName("hello");
        req.setParameterTypes(new Class<?>[0]);
        req.setArguments(new Object[0]);
        return req;
    }

    static byte[] serialize(Object o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
        }
        return baos.toByteArray();
    }

    /** 手工拼一帧：编解码器的两个方向都靠它来交叉验证。 */
    static ByteBuf frame(byte version, byte msgType, int declaredLength, byte[] body) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(MAGIC);
        buf.writeByte(version);
        buf.writeByte(msgType);
        buf.writeInt(declaredLength);
        if (body != null) {
            buf.writeBytes(body);
        }
        return buf;
    }

    static byte[] bytesOf(ByteBuf buf) {
        try {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            return bytes;
        } finally {
            buf.release();
        }
    }

    static byte[] encode(Object msg) {
        EmbeddedChannel channel = new EmbeddedChannel(new RpcMessageEncoder());
        try {
            assertTrue(channel.writeOutbound(msg), "编码器没有接受这条出站消息");
            ByteBuf out = channel.<ByteBuf>readOutbound();
            assertNotNull(out, "编码器没有写出任何字节");
            return bytesOf(out);
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    static Object decodeOne(byte[] frameBytes) {
        EmbeddedChannel channel = new EmbeddedChannel(new RpcMessageDecoder());
        try {
            channel.writeInbound(Unpooled.copiedBuffer(frameBytes));
            return channel.readInbound();
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    static void awaitNothing(Probe probe, ByteBuf buf) throws Exception {
        assertNull(probe.decodeFrame(buf), "这一帧不该被解出来");
    }

    private static int readInt(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 24) | ((bytes[offset + 1] & 0xFF) << 16)
                | ((bytes[offset + 2] & 0xFF) << 8) | (bytes[offset + 3] & 0xFF);
    }

    // ---------- 帧的形状 ----------

    @Test
    @DisplayName("请求帧 = ttcrpc + 版本 1 + 类型 1 + 大端 int 长度 + Java 原生序列化体")
    void requestFrameHeaderIsMagicVersionTypeThenBigEndianLength() throws Exception {
        byte[] body = serialize(request("req-1"));
        byte[] onWire = encode(request("req-1"));

        assertEquals(HEADER_SIZE + body.length, onWire.length, "整帧长度 = 10 字节头 + body");
        assertEquals("ttcrpc", new String(onWire, 0, 4, StandardCharsets.US_ASCII));
        assertEquals(1, onWire[4], "版本字节");
        assertEquals(1, onWire[5], "请求的类型字节");
        assertEquals(body.length, readInt(onWire, 6), "声明长度就是 body 的实际长度");
        // Java 原生序列化的流头：AC ED 00 05
        assertEquals(0xACED, ((onWire[HEADER_SIZE] & 0xFF) << 8) | (onWire[HEADER_SIZE + 1] & 0xFF),
                "body 不是 Java 序列化流");

        // 猎物：同一份字节确实能被解码器认回来，形状不是单方面宣称的
        Object back = decodeOne(onWire);
        assertInstanceOf(RpcRequest.class, back);
        assertEquals("req-1", ((RpcRequest) back).getRequestId());
    }

    @Test
    @DisplayName("响应帧只有第 6 个字节不同，其余布局与请求一致")
    void responseFramesDifferFromRequestsOnlyInTheTypeByte() throws Exception {
        RpcResponse response = RpcResponse.success("req-2", "hi");
        byte[] reqBytes = encode(request("req-2"));
        byte[] respBytes = encode(response);

        assertEquals(1, reqBytes[5], "请求 = 1");
        assertEquals(2, respBytes[5], "响应 = 2");
        // 头部 10 个字节里只有第 6 个是"类型"，其余 9 个各自描述自己的帧 —— 长度域跟着
        // body 走，所以它两份本来就该不同（这是首版把"只有一字节不同"写成逐字节相等的原因）。
        for (int i = 0; i < 5; i++) {
            assertEquals(reqBytes[i], respBytes[i], "魔数与版本号该完全一致，第 " + i + " 字节不该不同");
        }
        assertEquals(reqBytes.length - HEADER_SIZE, readInt(reqBytes, 6), "请求的长度域=自己的 body");
        assertEquals(respBytes.length - HEADER_SIZE, readInt(respBytes, 6), "响应的长度域=自己的 body");
        // 猎物：类型字节是解码器唯一的分流依据，改一个字节就换一种结局
        Object decoded = decodeOne(respBytes);
        assertInstanceOf(RpcResponse.class, decoded);
        assertEquals("hi", ((RpcResponse) decoded).getResult());
    }

    @Test
    @DisplayName("往返保住整个请求模型：参数类型数组、参数值、附件一个都不掉")
    void roundTripRestoresTheWholeRequestModel() throws Exception {
        RpcRequest original = request("req-3");
        RpcRequest back = (RpcRequest) decodeOne(encode(original));

        assertEquals(Greeter.class.getName(), back.getInterfaceName());
        assertEquals("hello", back.getMethodName());
        assertNotNull(back.getParameterTypes(), "parameterTypes 丢了");
        assertEquals(1, back.getParameterTypes().length);
        assertEquals(String.class, back.getParameterTypes()[0]);
        assertArrayEqualsOne(new Object[]{"x"}, back.getArguments());
        assertEquals("9.9.9", back.getAttachments().get("version"));
        // 版本与分组是从附件里读的，所以往返保住附件就是保住路由键
        assertEquals("9.9.9", back.getVersion());
        assertEquals("", back.getGroup());
    }

    static void assertArrayEqualsOne(Object[] expected, Object[] actual) {
        assertNotNull(actual, "arguments 丢了");
        assertEquals(1, actual.length);
        assertEquals(expected[0], actual[0]);
    }

    // ---------- 半包 / 粘包 ----------

    @Test
    @DisplayName("头部不足 10 字节时一律只缓冲：0..9 逐字节喂，一个例外都不许有")
    void partialHeaderIsBufferedUntilTheTenthByteArrives() throws Exception {
        byte[] onWire = encode(request("req-4"));
        Probe probe = new Probe();

        // 猎物：整帧一次喂进去立刻就有消息
        assertNotNull(decodeOne(onWire), "前提：完整一帧应当立刻解出");

        ByteBuf accumulated = Unpooled.buffer();
        try {
            for (int i = 0; i < HEADER_SIZE - 1; i++) {
                accumulated.writeByte(onWire[i]);
                awaitNothing(probe, accumulated);
                assertEquals(0, accumulated.readerIndex(),
                        "第 " + i + " 次喂完之后读位置应当还留在帧首");
            }
            accumulated.writeBytes(onWire, HEADER_SIZE - 1, onWire.length - (HEADER_SIZE - 1));
            Object decoded = probe.decodeFrame(accumulated);
            assertInstanceOf(RpcRequest.class, decoded);
            assertEquals("req-4", ((RpcRequest) decoded).getRequestId());
        } finally {
            accumulated.release();
        }
    }

    @Test
    @DisplayName("头部齐了但 body 只到一半：退回帧首等剩下的字节，不猜长度也不报错")
    void splitBodyWaitsAndThenDeliversTheMessage() throws Exception {
        byte[] onWire = encode(request("req-5"));
        int bodyLength = onWire.length - HEADER_SIZE;
        assertTrue(bodyLength > 16, "这帧的 body 太短，切不开");

        Probe probe = new Probe();
        ByteBuf part = Unpooled.buffer();
        try {
            part.writeBytes(onWire, 0, HEADER_SIZE + bodyLength / 2);
            awaitNothing(probe, part);
            assertEquals(0, part.readerIndex(), "等剩下的 body 时必须把读位置还回去");

            part.writeBytes(onWire, HEADER_SIZE + bodyLength / 2, bodyLength - bodyLength / 2);
            assertInstanceOf(RpcRequest.class, probe.decodeFrame(part));
        } finally {
            part.release();
        }
    }

    @Test
    @DisplayName("两帧连着到一个缓冲里：按写入顺序各解一次，第二帧不会被当成第一帧的 body")
    void twoCompleteFramesDecodeInWriteOrder() throws Exception {
        byte[] first = encode(request("first"));
        byte[] second = encode(RpcResponse.success("second", "hi"));
        EmbeddedChannel channel = new EmbeddedChannel(new RpcMessageDecoder());
        try {
            ByteBuf both = Unpooled.wrappedBuffer(first, second);
            channel.writeInbound(both);

            Object a = channel.readInbound();
            Object b = channel.readInbound();
            assertInstanceOf(RpcRequest.class, a);
            assertInstanceOf(RpcResponse.class, b);
            assertEquals("first", ((RpcRequest) a).getRequestId());
            assertEquals("second", ((RpcResponse) b).getRequestId());
            assertNull(channel.readInbound(), "不该凭空多出第三条消息");
        } finally {
            channel.finishAndReleaseAll();
        }
    }

    // ---------- 坏输入：长度字段 ----------

    @Test
    @DisplayName("bug_frameDecoderConstructorConfigurationIsDecorative：继承 LengthFieldBasedFrameDecoder 的六个参数一律不起作用")
    void bug_frameDecoderConstructorConfigurationIsDecorative() throws Exception {
        Probe probe = new Probe();
        Map<String, Object> inherited = new HashMap<String, Object>();
        for (String name : new String[]{"maxFrameLength", "lengthFieldOffset", "lengthFieldLength",
                "lengthAdjustment", "initialBytesToStrip", "failFast"}) {
            Field field = findField(probe.getClass(), name);
            assertNotNull(field, "父类字段 " + name + " 不在了，这条断言的前提变了");
            field.setAccessible(true);
            inherited.put(name, field.get(probe));
        }
        // 构造函数确实把 10MB / 偏移 6 / 4 字节长度域 / failFast 都交给了父类
        assertEquals(10 * 1024 * 1024, inherited.get("maxFrameLength"));
        assertEquals(6, inherited.get("lengthFieldOffset"));
        assertEquals(4, inherited.get("lengthFieldLength"));
        assertEquals(Boolean.TRUE, inherited.get("failFast"));

        // 但父类那套算帧长度的逻辑一次都没跑过：自己覆写的 decode 从不 call super.decode。
        // 证据是"超限帧"报的是这个类自己那句 IllegalArgumentException，而不是
        // LengthFieldBasedFrameDecoder 的 TooLongFrameException。
        ByteBuf oversize = frame((byte) 1, (byte) 1, MAX_FRAME + 1, null);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> probe.decodeFrame(oversize));
        assertEquals("Invalid data length: " + (MAX_FRAME + 1), e.getMessage());
        oversize.release();

        // 猎物：边界是 >，所以恰好 10MB 的声明会走到"等 body"这一支而不是被拒
        ByteBuf exact = frame((byte) 1, (byte) 1, MAX_FRAME, null);
        try {
            assertNull(probe.decodeFrame(exact), "恰好等于上限时应当当作半包");
            assertEquals(0, exact.readerIndex(), "半包退回时声明长度那 4 个字节也要还回去");
        } finally {
            exact.release();
        }
    }

    static Field findField(Class<?> type, String name) {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            try {
                return c.getDeclaredField(name);
            } catch (NoSuchFieldException ignore) {
                // 继续往上找
            }
        }
        return null;
    }

    @Test
    @DisplayName("负数声明长度在分配任何数组之前就被挡掉")
    void negativeDeclaredLengthFailsFastBeforeAnyAllocation() throws Exception {
        Probe probe = new Probe();
        ByteBuf negative = frame((byte) 1, (byte) 1, -1, null);
        try {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> probe.decodeFrame(negative));
            assertEquals("Invalid data length: -1", e.getMessage());
        } finally {
            negative.release();
        }
        // 猎物：0 不在这一道被挡（`< 0` 判不到它），它由后面那道"流头得是 AC ED"兜住
        ByteBuf zero = frame((byte) 1, (byte) 1, 0, new byte[0]);
        try {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> probe.decodeFrame(zero));
            assertTrue(e.getMessage().contains("declaredLength=0"), e.getMessage());
        } finally {
            zero.release();
        }
    }

    @Test
    @DisplayName("帧对不上内容时，抛出去的那一条要说清是哪一帧（修复前是一条没有消息的 EOFException）")
    void framingFailuresNameTheFrameTheyFailedOn() throws Exception {
        Probe probe = new Probe();

        // 修复前：空 body 一路走到 ObjectInputStream 手里，交回来的 EOFException 连
        // getMessage() 都是 null，落到日志里只剩一个类名，谁都认不出是哪一帧。
        ByteBuf emptyBody = frame((byte) 1, (byte) 1, 0, new byte[0]);
        try {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> probe.decodeFrame(emptyBody));
            assertTrue(e.getMessage().contains("not a Java serialization stream"), e.getMessage());
            assertTrue(e.getMessage().contains("declaredLength=0"), e.getMessage());
        } finally {
            emptyBody.release();
        }

        // 流头齐、内容被截短：这一支仍然要过 ObjectInputStream，钉的是"包装之后带上下文"。
        byte[] whole = serialize(request("req-trunc"));
        byte[] cut = new byte[8];
        System.arraycopy(whole, 0, cut, 0, 8);
        ByteBuf truncated = frame((byte) 1, (byte) 1, cut.length, cut);
        try {
            IOException e = assertThrows(IOException.class, () -> probe.decodeFrame(truncated));
            assertNotNull(e.getMessage(), "包装后的那条必须有消息");
            assertTrue(e.getMessage().contains("Failed to deserialize an RPC frame"), e.getMessage());
            assertTrue(e.getMessage().contains("declaredLength=8"), e.getMessage());
            assertNotNull(e.getCause(), "原始那条要留在 cause 链里");
        } finally {
            truncated.release();
        }

        // 猎物：同一套代码，长度与内容对上就正常应答 —— 缺的从来不是能力，是错误说明
        assertInstanceOf(RpcRequest.class, decodeOne(encode(request("req-len"))));
    }

    // ---------- 坏输入：魔数 / 版本 / 类型 ----------

    @Test
    @DisplayName("坏头部一律把读位置还回帧首：魔数那一支本来就写了，版本那一支补上")
    void badHeaderRestoresTheReaderIndex() throws Exception {
        Probe probe = new Probe();
        byte[] body = serialize(request("req-reset"));
        ByteBuf badMagic = frame((byte) 1, (byte) 1, body.length, body);
        badMagic.setByte(1, 'X');
        try {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> probe.decodeFrame(badMagic));
            assertEquals("Invalid magic number", e.getMessage());
            assertEquals(0, badMagic.readerIndex(), "魔数这一支写了 resetReaderIndex");
        } finally {
            badMagic.release();
        }

        // 版本分支修复前没有 reset：抛出去时 5 个字节已经被吃掉，缓冲里剩下的是从帧中间
        // 开始的字节，下一次解码的起点就被这一次失败挪走了。
        ByteBuf badVersion = frame((byte) 2, (byte) 1, body.length, body);
        try {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> probe.decodeFrame(badVersion));
            assertEquals("Unsupported version: 2", e.getMessage());
            assertEquals(0, badVersion.readerIndex(), "版本这一支也要把位置还回帧首");

            // 只断言 readerIndex 太薄：把版本字节改对再喂一次，位置真还回来了这一帧才解得出。
            badVersion.setByte(4, 1);
            assertInstanceOf(RpcRequest.class, probe.decodeFrame(badVersion));
        } finally {
            badVersion.release();
        }
    }

    @Test
    @DisplayName("bug_wireBytesAreDeserializedBeforeAnyTypeCheck：类型字节要等对象建好之后才看，等于没看")
    void bug_wireBytesAreDeserializedBeforeAnyTypeCheck() throws Exception {
        // 猎物：一个合法的请求帧不会碰 Trigger
        Trigger.HITS.set(0);
        decodeOne(encode(request("req-clean")));
        assertEquals(0, Trigger.HITS.get(), "前提：正常链路上不该有任何东西被构造");

        Probe probe = new Probe();
        byte[] body = serialize(new Trigger());
        // 声明成"响应"，装的不是 RpcResponse —— 服务端/客户端都会在这之前就把它 new 出来
        ByteBuf spoof = frame((byte) 1, (byte) 2, body.length, body);
        try {
            assertThrows(IllegalArgumentException.class, () -> probe.decodeFrame(spoof));
        } finally {
            spoof.release();
        }
        assertEquals(1, Trigger.HITS.get(),
                "readObject 已经在解码线程上跑过了，之后才判「Message type mismatch」");
    }

    @Test
    @DisplayName("类型字节只用来分流、不是校验：一个谁都不认的类型也要先把对象建出来")
    void typeByteIsCheckedOnlyAfterTheObjectHasBeenBuilt() throws Exception {
        byte[] body = serialize(request("req-type"));
        Trigger.HITS.set(0);

        Probe probe = new Probe();
        ByteBuf unknownType = frame((byte) 1, (byte) 99, body.length, body);
        try {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> probe.decodeFrame(unknownType));
            assertEquals("Message type mismatch", e.getMessage());
        } finally {
            unknownType.release();
        }
        // 请求体 + 谁都不认的类型：反序列化照样做完了才报错
        ByteBuf wrongDirection = frame((byte) 1, (byte) 2, body.length, body);
        try {
            assertThrows(IllegalArgumentException.class, () -> probe.decodeFrame(wrongDirection));
        } finally {
            wrongDirection.release();
        }
        assertEquals(0, Trigger.HITS.get(), "这条里载荷是 RpcRequest，不该构造 Trigger");

        // 猎物：同一份 body、类型字节改回 1，就正常解出来了
        ByteBuf rightType = frame((byte) 1, (byte) 1, body.length, body);
        try {
            assertInstanceOf(RpcRequest.class, probe.decodeFrame(rightType));
        } finally {
            rightType.release();
        }
    }

    @Test
    @DisplayName("坏帧在真 pipeline 里不会有响应，只有关连接；单独摆一个解码器时连接照旧活着")
    void badFrameClosesTheServerSideConnectionAndAnswersNothing() {
        Map<String, Object> services = new HashMap<String, Object>();
        services.put(Greeter.class.getName(), new GreeterImpl());
        EmbeddedChannel serverSide = new EmbeddedChannel(new RpcMessageDecoder(),
                new RpcServerHandler(services));
        try {
            // 不拿 writeInbound 的返回值当判据：坏帧和"被链尾吃掉的合法帧"给出的都是 false，
            // 这条断言永远不会红。真正有信息量的是下面两条：没有响应、连接被关。
            serverSide.writeInbound(frame((byte) 2, (byte) 1, 0, null));
            assertNull(serverSide.readOutbound(), "解码失败时服务端一条响应都不会回");
            assertFalse(serverSide.isActive(), "exceptionCaught 里的 ctx.close() 是这件事唯一的兜底");
        } finally {
            serverSide.finishAndReleaseAll();
        }

        // 猎物（对照）：同一份字节后面接一个合法帧，服务端正常应答，说明关连接确实是"坏帧"造成的
        EmbeddedChannel ok = new EmbeddedChannel(new RpcMessageDecoder(), new RpcServerHandler(services));
        try {
            ok.writeInbound(Unpooled.copiedBuffer(encode(invocable("req-after-bad"))));
            RpcResponse resp = ok.<RpcResponse>readOutbound();
            assertNotNull(resp, "合法帧必须有响应");
            assertEquals("hi", resp.getResult());
            assertTrue(ok.isActive());
        } finally {
            ok.finishAndReleaseAll();
        }
    }

    // ---------- 广告与接线 ----------

    @Test
    @DisplayName("bug_advertisedClientHandlerIsNotTheOneOnTheWire：public 的那个 RpcClientHandler 不在任何链路上")
    void bug_advertisedClientHandlerIsNotTheOneOnTheWire() throws Exception {
        byte[] clientBytes = classBytes(RpcClient.class);
        String pool = new String(clientBytes, StandardCharsets.ISO_8859_1);

        // RpcClient.connect() 里写的是 `new RpcClientHandler()`，而 RpcClient 自己有个同名
        // 私有内部类 —— 按 Java 的名字解析规则，那一句指的是内部类，不是同包的 public 类。
        assertTrue(pool.contains("remoting/RpcClient$RpcClientHandler"),
                "前提没成立：RpcClient 里找不到内部类的引用了，这条要重写");
        assertFalse(pool.contains("remoting/RpcClientHandler;"),
                "public 的 RpcClientHandler 根本没被 RpcClient 引用");

        // 它的 javadoc 承诺的两个名字在仓里都不存在：
        // "还有顶层包 com.taotao.cloud.rpc.RpcClientHandler，那是简化版本" / "本类是 DiscoveryRpcClient 长连接模式使用的版本"
        assertThrows(ClassNotFoundException.class, () -> Class.forName("com.taotao.cloud.rpc.RpcClientHandler"));
        assertThrows(ClassNotFoundException.class, () -> Class.forName("com.taotao.cloud.rpc.remoting.DiscoveryRpcClient"));

        // 猎物：这个类本身是能加载的 —— 缺的不是它，是接它上链路的人
        Class<?> publicHandler = Class.forName("com.taotao.cloud.rpc.remoting.RpcClientHandler");
        assertNotNull(publicHandler.getMethod("setFuture", String.class,
                CompletableFuture.class));
    }

    static byte[] classBytes(Class<?> type) throws Exception {
        String resource = type.getName().replace('.', '/') + ".class";
        java.io.InputStream in = type.getClassLoader().getResourceAsStream(resource);
        assertNotNull(in, "找不到 " + resource + "，本测试退化为空跑");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = in.read(buf)) > 0) {
                bos.write(buf, 0, n);
            }
            return bos.toByteArray();
        } finally {
            in.close();
        }
    }

    // ---------- 客户端等待方会被谁叫醒 ----------

    @Test
    @DisplayName("写不出去时等待方立刻拿回异常，不会干等到超时；在飞表也一并清掉")
    void writeFailureWakesTheCallerInsteadOfWaitingForTheTimeout() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        RpcClient client = new RpcClient("127.0.0.1", server.getPort());
        try {
            client.setTimeout(3000L);

            // 猎物：同一处把实参换成空的，调用就成功
            assertEquals("hi", client.sendRequest(invocable("ok-1")).getResult());

            RpcRequest bad = invocable("bad-1");
            bad.setArguments(new Object[]{new Object()});   // 客户端编码阶段必炸

            ExecutionException e = assertThrows(ExecutionException.class, () -> client.sendRequest(bad));
            assertTrue(hasCause(e, NotSerializableException.class),
                    "实际链条: " + chain(e));
            assertEquals(0, pendingCount(client),
                    "写失败的清理是那句 listener 里的 remove，缺了它这张表会一直长");
        } finally {
            client.close();
            server.stop();
        }
    }

    @Test
    @DisplayName("服务端编不出响应时也要回一个错：调用方不该拿回寂静（修复前是干等满 600ms 超时）")
    void unserializableResultComesBackAsAnErrorFrame() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        RpcClient client = new RpcClient("127.0.0.1", server.getPort());
        try {
            // 600ms 是判据的一部分：修复前这一条只能靠闹钟醒，那时抛的是 TimeoutException
            client.setTimeout(600L);

            RpcRequest brick = invocable("brick-1");
            brick.setMethodName("unserializable");

            // 业务方法在服务端是**成功返回**的（RpcServerHandler 把结果塞进 response.setResult），
            // 炸的是后面那次 writeAndFlush 里的 Java 序列化。首版在这里断言"服务端会关连接"，
            // 实测证伪：Netty 把这次写失败只交给 promise，连 exceptionCaught 都没有走到，
            // 所以既没有 RST/FIN 也没有任何一帧回去。
            RpcResponse back = client.sendRequest(brick);
            assertTrue(back.hasException(),
                    "必须是一份异常帧：RpcResponse.hasException() 只看 exception，只填 errorMessage 会被上层当成功");
            String msg = String.valueOf(back.getException().getMessage());
            assertTrue(msg.contains("Failed to encode the RPC response"), msg);
            assertTrue(msg.contains("NotSerializableException"), msg);
            assertEquals("brick-1", back.getRequestId(), "降级帧要挂回原来那个请求");
            assertEquals(0, pendingCount(client));

            // 猎物：同一个客户端、同一条连接，紧接着的正常调用照样拿到结果
            assertEquals("hi", client.sendRequest(invocable("after-brick-1")).getResult());
        } finally {
            client.close();
            server.stop();
        }
    }

    @Test
    @DisplayName("bug_unencodableResponseNeverReachesExceptionCaught：编不出去的响应在服务端连一次 exceptionCaught 都触发不了")
    void bug_unencodableResponseNeverReachesExceptionCaught() throws Exception {
        Map<String, Object> services = new HashMap<String, Object>();
        services.put(Greeter.class.getName(), new GreeterImpl());

        // 前提/猎物：同一形状的 pipeline 遇到"解码失败"是报得出来的 —— RpcServerHandler.exceptionCaught
        // 会 log.error + ctx.close()，所以那个通道会关掉。
        EmbeddedChannel decodeFails = new EmbeddedChannel(new RpcMessageDecoder(),
                new RpcMessageEncoder(), new RpcServerHandler(services));
        try {
            ByteBuf badMagic = frame((byte) 1, (byte) 1, 0, null);
            badMagic.setByte(1, 'X');
            decodeFails.writeInbound(badMagic);
            assertFalse(decodeFails.isActive(), "前提：这条 pipeline 报得出错");
        } finally {
            decodeFails.finishAndReleaseAll();
        }

        // 而"响应编不出去"这一支走的是 writeAndFlush 的 promise：补了监听器之后它能把
        // 寂静换成一个错，但异常本身仍然不进 exceptionCaught、也不进 pipeline 的异常记录
        // —— 也就是说"关连接、报错、上层可见的失败"这三件事里，它仍然只做到了发一帧降级响应。
        RpcRequest brick = invocable("brick-emb");
        brick.setMethodName("unserializable");
        EmbeddedChannel encodeFails = new EmbeddedChannel(new RpcMessageDecoder(),
                new RpcMessageEncoder(), new RpcServerHandler(services));
        try {
            encodeFails.writeInbound(Unpooled.copiedBuffer(encode(brick)));
            assertTrue(encodeFails.isActive(), "exceptionCaught 仍然没走到：那是唯一会关连接的地方");
            encodeFails.checkException();

            // 回填给调用方的那份降级响应。注意读出来的是编码器产出的字节（这条 pipeline 里
            // 有 encoder），不是 RpcResponse 对象。
            ByteBuf degraded = encodeFails.readOutbound();
            assertNotNull(degraded, "编不出去的那一帧要有替代品发出去");
            RpcResponse err = (RpcResponse) decodeOne(bytesOf(degraded));
            assertTrue(err.hasException(), "替代品必须是异常帧");
            assertEquals("brick-emb", err.getRequestId());

            // 猎物：这条连接完全健康，紧接着的正常请求照样答
            encodeFails.writeInbound(Unpooled.copiedBuffer(encode(invocable("after-emb"))));
            ByteBuf written = encodeFails.readOutbound();
            assertNotNull(written, "坏掉的只是那一帧，不是这条连接");
            RpcResponse resp = (RpcResponse) decodeOne(bytesOf(written));
            assertNotNull(resp, "解不出响应帧");
            assertEquals("hi", resp.getResult());
        } finally {
            encodeFails.finishAndReleaseAll();
        }
    }

    @Test
    @DisplayName("连接没了要叫醒每一个在飞的等待方：_eof_与_exceptionCaught_两条路都得落地")
    void deadConnectionWakesInFlightWaiters() throws Exception {
        RpcServer server = new RpcServer("127.0.0.1", 0);
        server.registerService(Greeter.class, new GreeterImpl());
        server.start(true);
        RpcClient client = new RpcClient("127.0.0.1", server.getPort());
        try {
            // 前提/猎物：真正接线的那个 handler（RpcClient 的私有内部类，不是同包那个没接线
            // 的 public 同名类），来一条响应是能叫醒等待方的。
            CompletableFuture<RpcResponse> answered = new CompletableFuture<>();
            putPending(client, "wake-ok", answered);
            EmbeddedChannel delivers = new EmbeddedChannel(newLiveClientHandler(client));
            try {
                // 不能拿 writeInbound 的返回值当判据：handler 是链尾且会把消息吃掉，
                // 那时候它返回的永远是 false。判据只能在等待方身上。
                delivers.writeInbound(RpcResponse.success("wake-ok", "hi"));
                assertTrue(answered.isDone(), "前提：正常完成路径是通的");
                assertEquals("hi", answered.get().getResult());
                assertFalse(pending(client).containsKey("wake-ok"), "完成之后要从在飞表摘掉");
            } finally {
                delivers.finishAndReleaseAll();
            }

            CompletableFuture<RpcResponse> atEof = new CompletableFuture<>();
            putPending(client, "eof", atEof);
            EmbeddedChannel dropped = new EmbeddedChannel(newLiveClientHandler(client));
            try {
                dropped.disconnect();
                assertFalse(dropped.isActive(), "前提：channelInactive 确实已经发生");
                assertTrue(atEof.isCompletedExceptionally(),
                        "修复前这里什么都没发生：连接没了传达到不了任何一个等待方");
                assertTrue(hasMessage(atEof, "Connection closed"), "实际: " + failureMessage(atEof));
                assertFalse(pending(client).containsKey("eof"),
                        "在飞条目也要一并摘掉，否则这张表只会随断连次数长");
            } finally {
                dropped.finishAndReleaseAll();
            }

            CompletableFuture<RpcResponse> atError = new CompletableFuture<>();
            putPending(client, "boom", atError);
            EmbeddedChannel errored = new EmbeddedChannel(newLiveClientHandler(client));
            try {
                IOException boom = new IOException("peer reset");
                errored.pipeline().fireExceptionCaught(boom);
                assertFalse(errored.isActive(), "它照旧关连接");
                assertSame(boom, failureCause(atError), "报错的那一条要原样交给等待方");
                assertFalse(pending(client).containsKey("boom"), "同样从表里摘掉");
            } finally {
                errored.finishAndReleaseAll();
            }
        } finally {
            client.close();
            server.stop();
        }
    }

    static Throwable failureCause(CompletableFuture<RpcResponse> future) {
        try {
            future.getNow(null);
            return null;
        } catch (CompletionException e) {
            return e.getCause();
        }
    }

    static String failureMessage(CompletableFuture<RpcResponse> future) {
        Throwable c = failureCause(future);
        return c == null ? "future 没有异常完成" : c.getClass().getName() + ": " + c.getMessage();
    }

    static boolean hasMessage(CompletableFuture<RpcResponse> future, String fragment) {
        Throwable c = failureCause(future);
        return c != null && c.getMessage() != null && c.getMessage().contains(fragment);
    }

    /** RpcClient 真正接进 pipeline 的那个 handler 是它的私有内部类，只能反射拿到。 */
    static ChannelHandler newLiveClientHandler(RpcClient client) throws Exception {
        Class<?> type = Class.forName(RpcClient.class.getName() + "$RpcClientHandler");
        java.lang.reflect.Constructor<?> ctor = type.getDeclaredConstructor(RpcClient.class);
        ctor.setAccessible(true);
        return (ChannelHandler) ctor.newInstance(client);
    }

    @SuppressWarnings("unchecked")
    static void putPending(RpcClient client, String id, CompletableFuture<RpcResponse> future) throws Exception {
        ((Map<String, CompletableFuture<RpcResponse>>) pending(client)).put(id, future);
    }

    static Map<?, ?> pending(RpcClient client) throws Exception {
        Field field = RpcClient.class.getDeclaredField("pendingRequests");
        field.setAccessible(true);
        return (Map<?, ?>) field.get(client);
    }

    static int pendingCount(RpcClient client) throws Exception {
        return pending(client).size();
    }

    static boolean hasCause(Throwable t, Class<?> type) {
        for (Throwable c = t; c != null; c = c.getCause()) {
            if (type.isInstance(c)) {
                return true;
            }
            if (c.getCause() == c) {
                break;
            }
        }
        return false;
    }

    static String chain(Throwable t) {
        StringBuilder sb = new StringBuilder();
        for (Throwable c = t; c != null; c = c.getCause()) {
            sb.append(c.getClass().getName()).append(": ").append(c.getMessage()).append(" <- ");
            if (c.getCause() == c) {
                break;
            }
        }
        return sb.toString();
    }
}
