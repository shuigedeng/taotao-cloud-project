package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 帧格式合一的双向交叉解码用例（非计时）。
 * <p>
 * 本模块历史上存在两套互不兼容的帧：
 * <ul>
 *   <li>线上：{@code NettyEncoder}/{@code NettyDecoder} —— 头长用 int</li>
 *   <li>死代码：{@code RemotingCommand.encode()/decode(ByteBuffer)} —— 头长用 short</li>
 * </ul>
 * 现在只允许一种格式，因此这里做真·双向对拍：
 * <ol>
 *   <li>{@code encode()} 的产物必须能被 <b>真的</b> {@code NettyDecoder}（EmbeddedChannel 里的 handler，
 *       不是手抄一份读法）解开；</li>
 *   <li>{@code NettyEncoder} 的产物必须能被 {@code decode()} 解开；</li>
 *   <li>两者的字节布局逐字段相同（帧长语义 = 不含长度字段自己）。</li>
 * </ol>
 * 所有用例都用<b>非零 header + 非零 body</b>，并对"header 非空/ body 非空"本身立守卫：
 * 头长为 0、体长为 0 的往返什么都证明不了。
 */
public class FrameCrossCodecTest {

    private static final byte[] BODY = new byte[2048];

    static {
        // 非退化 body：既非全 0，也含 0x00 之外的字节
        for (int i = 0; i < BODY.length; i++) {
            BODY[i] = (byte) (i * 31 + 7);
        }
    }

    /** 造一条 header、body 都非空的请求。 */
    private static RemotingCommand newPrey() {
        RemotingCommand cmd = RemotingCommand.createRequestCommand(310);
        cmd.setOpaque(987654321);
        cmd.setVersion(7);
        cmd.setRemark("cross-codec prey");
        cmd.addExtField("topic", "T_CROSS_CODEC");
        cmd.addExtField("queueId", "3");
        cmd.addExtField("brokerName", "broker-a");
        cmd.addExtField("msgId", "AC12000000001234567890ABCDEF00");
        cmd.setBody(BODY.clone());
        return cmd;
    }

    private static void assertSameCommand(RemotingCommand expected, RemotingCommand actual) {
        assertNotNull(actual, "解码返回 null：两套帧格式仍未合一");
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getOpaque(), actual.getOpaque());
        assertEquals(expected.getVersion(), actual.getVersion());
        assertEquals(expected.getRemark(), actual.getRemark());
        assertEquals(expected.getExtFields(), actual.getExtFields());
        assertEquals(expected.isOnewayRPC(), actual.isOnewayRPC());
        assertEquals(expected.isResponseType(), actual.isResponseType());
        assertArrayEquals(expected.getBody(), actual.getBody(), "消息体字节必须逐字节相同");
    }

    // ==================== 方向一：encode() -> NettyDecoder ====================

    @Test
    public void commandEncodeOutputIsReadableByNettyDecoder() {
        RemotingCommand prey = newPrey();
        ByteBuffer encoded = prey.encode();
        assertNotNull(encoded);

        // 守卫：这条 prey 的 header 与 body 都必须非零，否则本用例不携带信息
        byte[] headerData = prey.encodeHeader();
        assertNotNull(headerData);
        assertTrue(headerData.length > 0, "prey 的 header 长度不能为 0");
        assertTrue(prey.getBody().length > 0, "prey 的 body 长度不能为 0");

        EmbeddedChannel channel = new EmbeddedChannel(new NettyDecoder());
        assertTrue(channel.writeInbound(Unpooled.wrappedBuffer(encoded)),
                "NettyDecoder 没能从 encode() 的产物里解出一帧：帧格式仍不兼容");

        RemotingCommand decoded = channel.readInbound();
        assertSameCommand(prey, decoded);
        channel.finishAndReleaseAll();
    }

    // ==================== 方向二：NettyEncoder -> decode() ====================

    @Test
    public void nettyEncoderOutputIsReadableByCommandDecode() {
        RemotingCommand prey = newPrey();

        EmbeddedChannel channel = new EmbeddedChannel(new NettyEncoder());
        channel.writeOutbound(prey);
        ByteBuf out = channel.readOutbound();
        assertNotNull(out, "NettyEncoder 没有产出任何字节");

        byte[] wire = new byte[out.readableBytes()];
        out.readBytes(wire);
        out.release();
        channel.finishAndReleaseAll();

        RemotingCommand decoded = RemotingCommand.decode(ByteBuffer.wrap(wire));
        assertSameCommand(prey, decoded);
    }

    // ==================== 字节布局：证明"同一种格式"而非"碰巧对称" ====================

    @Test
    public void bothProducersEmitIdenticalBytes() {
        RemotingCommand prey = newPrey();

        ByteBuffer encoded = prey.encode();
        assertNotNull(encoded);
        byte[] fromEncode = new byte[encoded.remaining()];
        encoded.get(fromEncode);

        EmbeddedChannel channel = new EmbeddedChannel(new NettyEncoder());
        channel.writeOutbound(prey);
        ByteBuf out = channel.readOutbound();
        byte[] fromEncoder = new byte[out.readableBytes()];
        out.readBytes(fromEncoder);
        out.release();
        channel.finishAndReleaseAll();

        assertArrayEquals(fromEncoder, fromEncode,
                "encode() 与 NettyEncoder 必须产出完全相同的帧字节（只允许一种格式）");

        // 逐字段核一遍布局：[int 帧长][byte 类型][int 头长][头][int 体长][体]
        ByteBuffer probe = ByteBuffer.wrap(fromEncode);
        int frameLength = probe.getInt();
        int headerLength;
        byte serializeType = probe.get();
        headerLength = probe.getInt();

        assertEquals(fromEncode.length - 4, frameLength,
                "帧长必须是不含自身 4 字节的后续字节数（LengthFieldBasedFrameDecoder 语义）");
        assertEquals(RemotingCommand.SERIALIZE_TYPE_JSON, (int) serializeType);
        assertEquals(prey.encodeHeader().length, headerLength);
        assertEquals(1 + 4 + headerLength + 4 + BODY.length, frameLength);
        assertFalse(headerLength <= 0, "头长必须非零，否则本断言组退化为空帧");
    }

    /**
     * 老格式用 short 写头长：超过 32767 字节就会被截断成负数/错值。
     * 合一后 int 头长必须能原样往返，这是"改成 int"而不是"只换个写法"的实质证据。
     */
    @Test
    public void oversizedHeaderSurvivesRoundTrip() {
        StringBuilder big = new StringBuilder(40000);
        while (big.length() < 40000) {
            big.append('x');
        }

        RemotingCommand prey = RemotingCommand.createRequestCommand(310);
        prey.setOpaque(42424242);
        prey.addExtField("blob", big.toString());
        assertTrue(prey.encodeHeader().length > 32767,
                "本用例要求 header 越过 short 上限，否则测不到截断问题");

        EmbeddedChannel channel = new EmbeddedChannel(new NettyDecoder());
        assertTrue(channel.writeInbound(Unpooled.wrappedBuffer(prey.encode())),
                "大 header 帧没能被 NettyDecoder 解出");
        RemotingCommand viaWire = channel.readInbound();
        assertSameCommand(prey, viaWire);
        channel.finishAndReleaseAll();

        assertSameCommand(prey, RemotingCommand.decode(ByteBuffer.wrap(toBytes(prey.encode()))));
    }

    // ==================== 跨向：oneway 标记也必须过帧 ====================

    @Test
    public void onewayFlagSurvivesBothDirections() {
        RemotingCommand prey = newPrey();
        prey.markOnewayRPC();
        assertTrue(prey.isOnewayRPC());

        EmbeddedChannel channel = new EmbeddedChannel(new NettyDecoder());
        assertTrue(channel.writeInbound(Unpooled.wrappedBuffer(prey.encode())));
        RemotingCommand decoded = channel.readInbound();
        assertNotNull(decoded);
        assertTrue(decoded.isOnewayRPC(), "oneway 标记在帧里丢了：broker 端会照常回响应");
        assertArrayEquals(prey.getBody(), decoded.getBody());
        channel.finishAndReleaseAll();

        EmbeddedChannel enc = new EmbeddedChannel(new NettyEncoder());
        enc.writeOutbound(prey);
        ByteBuf out = enc.readOutbound();
        byte[] wire = new byte[out.readableBytes()];
        out.readBytes(wire);
        out.release();
        enc.finishAndReleaseAll();

        RemotingCommand back = RemotingCommand.decode(ByteBuffer.wrap(wire));
        assertNotNull(back);
        assertTrue(back.isOnewayRPC());
        assertTrue(Arrays.equals(prey.getBody(), back.getBody()));
    }

    // ==================== 非法帧：不许拿半截 buffer 猜 ====================

    @Test
    public void truncatedOrBogusFramesAreRejectedNotNullCrash() {
        RemotingCommand prey = newPrey();
        byte[] full = toBytes(prey.encode());

        // 帧长字段撒谎（比实际可读字节多 4）⇒ 必须判非法
        ByteBuffer lying = ByteBuffer.wrap(full.clone());
        lying.putInt(0, full.length);
        assertTrue(lying.getInt(0) > lying.capacity() - 4);
        assertEquals(null, RemotingCommand.decode(lying), "帧长超出可读范围必须返回 null");
        // 基线：同一帧不撒谎时确实能解开，免得上一条因为"根本解不开"而假绿
        assertNotNull(RemotingCommand.decode(ByteBuffer.wrap(full.clone())));

        byte[] truncated = Arrays.copyOf(full, full.length - 100);
        assertEquals(null, RemotingCommand.decode(ByteBuffer.wrap(truncated)),
                "半截帧必须返回 null，不能读出越界或返回残缺 body");

        byte[] bogus = "not-a-frame-at-all-please-reject-me".getBytes(StandardCharsets.UTF_8);
        assertEquals(null, RemotingCommand.decode(ByteBuffer.wrap(bogus)));

        assertEquals(null, RemotingCommand.decode(ByteBuffer.allocate(0)));
        assertEquals(null, RemotingCommand.decode(null));
    }

    private static byte[] toBytes(ByteBuffer buffer) {
        ByteBuffer dup = buffer.duplicate();
        byte[] bytes = new byte[dup.remaining()];
        dup.get(bytes);
        return bytes;
    }
}
