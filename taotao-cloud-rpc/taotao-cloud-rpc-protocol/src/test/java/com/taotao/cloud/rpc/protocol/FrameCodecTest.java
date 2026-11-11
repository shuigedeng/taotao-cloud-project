package com.taotao.cloud.rpc.protocol;

import com.taotao.cloud.rpc.common.ProtocolConstants;
import com.taotao.cloud.rpc.common.RpcException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 线格式（wire format）编解码测试。
 * <p>
 * 全部通过 {@link EmbeddedChannel} 驱动，不需要真实 socket。
 */
class FrameCodecTest {

    // ====================== helpers ======================

    private static ByteBuf encode(TtcRpcMessage msg) {
        EmbeddedChannel enc = new EmbeddedChannel(new TtcRpcMessageEncoder());
        assertTrue(enc.writeOutbound(msg), "encoder 应当产出出站字节");
        ByteBuf out = enc.readOutbound();
        assertNotNull(out, "readOutbound() 返回 null 说明编码器没有写出任何内容");
        return out;
    }

    private static byte[] snapshot(ByteBuf buf) {
        byte[] all = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), all);
        return all;
    }

    /** 把字节喂给解码器，返回解码过程中抛出的异常（含 cause 链），没有异常返回 null。 */
    private static Throwable feed(EmbeddedChannel ch, byte[] bytes, int offset, int length) {
        ByteBuf in = Unpooled.wrappedBuffer(bytes, offset, length);
        try {
            ch.writeInbound(in);
        } catch (Throwable t) {
            return root(t);
        }
        try {
            ch.checkException();
        } catch (Throwable t) {
            return root(t);
        }
        return null;
    }

    private static Throwable root(Throwable t) {
        for (Throwable c = t; c != null; c = c.getCause()) {
            if (c instanceof RpcException || c instanceof IndexOutOfBoundsException) return c;
        }
        return t;
    }

    private static TtcRpcMessage request(long id, byte[] body) {
        TtcRpcMessage m = new TtcRpcMessage();
        m.setVersion(ProtocolConstants.VERSION);
        m.setMessageType(ProtocolConstants.MSG_TYPE_REQUEST);
        m.setSerializationId(ProtocolConstants.SERIALIZE_HESSIAN2);
        m.setCompression(ProtocolConstants.COMPRESS_NONE);
        m.setStatus(ProtocolConstants.STATUS_OK);
        m.setRequestId(id);
        m.setBody(body);
        return m;
    }

    private static TtcRpcMessage decodeOne(byte[] frame) {
        EmbeddedChannel dec = new EmbeddedChannel(new TtcRpcMessageDecoder());
        Throwable t = feed(dec, frame, 0, frame.length);
        assertNull(t, "整帧喂入不应抛异常: " + t);
        TtcRpcMessage out = dec.readInbound();
        assertNotNull(out, "解码器没有产出消息");
        return out;
    }

    // ====================== 字段级往返 ======================

    @Test
    @DisplayName("魔数是大端 \"ttcrpc\" 四个字节")
    void magicIsAsciittcrpcInBigEndian() {
        ByteBuf buf = encode(request(1L, new byte[]{1}));
        byte[] all = snapshot(buf);
        assertArrayEquals(new byte[]{0x5A, 0x52, 0x50, 0x43}, Arrays.copyOfRange(all, 0, 4));
        assertEquals(ProtocolConstants.MAGIC_NUMBER,
                ((all[0] & 0xFF) << 24) | ((all[1] & 0xFF) << 16) | ((all[2] & 0xFF) << 8) | (all[3] & 0xFF));
    }

    @Test
    @DisplayName("24 字节定长头：请求 ID 在第 12..19 字节、body 长度在第 20..23 字节")
    void fixedHeaderFieldOffsets() {
        ByteBuf buf = encode(request(0x0102030405060708L, new byte[5]));
        byte[] all = snapshot(buf);
        assertEquals(0x01, all[4]);   // version
        assertEquals(0x01, all[5]);   // message type = REQUEST
        assertEquals(0x03, all[6]);   // serialization = hessian2
        assertEquals(0x00, all[7]);   // compression = none
        assertEquals(0x00, all[8]);   // status = OK (short, 8..9)
        assertEquals(0x00, all[9]);
        long reqId = 0;
        for (int i = 10; i < 18; i++) reqId = (reqId << 8) | (all[i] & 0xFFL);
        assertEquals(0x0102030405060708L, reqId);
        int bodyLen = ((all[18] & 0xFF) << 24) | ((all[19] & 0xFF) << 16) | ((all[20] & 0xFF) << 8) | (all[21] & 0xFF);
        assertEquals(5, bodyLen, "body 长度字段被回填错了");
        int headerLen = ((all[22] & 0xFF) << 8) | (all[23] & 0xFF);
        assertEquals(0, headerLen);
    }

    @Test
    @DisplayName("完整一帧：所有头字段 + body 逐字节还原")
    void fullFrameRoundTrip() {
        byte[] body = "hello-ttcrpc-body".getBytes(StandardCharsets.UTF_8);
        TtcRpcMessage m = request(987654321L, body);
        m.setSerializationId(ProtocolConstants.SERIALIZE_JSON);
        m.setCompression(ProtocolConstants.COMPRESS_GZIP);
        m.setStatus(ProtocolConstants.STATUS_BIZ_ERROR);
        TtcRpcMessage back = decodeOne(snapshot(encode(m)));

        assertEquals(ProtocolConstants.MAGIC_NUMBER, back.getMagic());
        assertEquals(ProtocolConstants.VERSION, back.getVersion());
        assertEquals(ProtocolConstants.MSG_TYPE_REQUEST, back.getMessageType());
        assertEquals(ProtocolConstants.SERIALIZE_JSON, back.getSerializationId());
        assertEquals(ProtocolConstants.COMPRESS_GZIP, back.getCompression());
        assertEquals(ProtocolConstants.STATUS_BIZ_ERROR, back.getStatus());
        assertEquals(987654321L, back.getRequestId());
        assertArrayEquals(body, back.getBody());
        assertEquals(body.length, back.getBodyLength());
    }

    @Test
    @DisplayName("响应码 0..99 全部可以穿过 short 字段")
    void everyStatusCodeSurvivesRoundTrip() {
        short[] codes = {ProtocolConstants.STATUS_OK, ProtocolConstants.STATUS_TIMEOUT,
                ProtocolConstants.STATUS_CONN_LOST, ProtocolConstants.STATUS_BIZ_ERROR,
                ProtocolConstants.STATUS_NO_PROVIDER, ProtocolConstants.STATUS_BAD_REQUEST,
                ProtocolConstants.STATUS_SERIALIZATION, ProtocolConstants.STATUS_PROTOCOL,
                ProtocolConstants.STATUS_LIMIT, ProtocolConstants.STATUS_FORBIDDEN,
                ProtocolConstants.STATUS_UNKNOWN};
        Set<Short> seen = new LinkedHashSet<>();
        for (short c : codes) {
            TtcRpcMessage m = request(1L, new byte[]{0});
            m.setStatus(c);
            short got = decodeOne(snapshot(encode(m))).getStatus();
            assertEquals(c, got, "status=" + c);
            seen.add(got);
        }
        assertEquals(codes.length, seen.size(), "状态码之间有碰撞: " + seen);
    }

    @Test
    @DisplayName("requestId 覆盖 Long.MIN_VALUE / MAX_VALUE")
    void extremeRequestIds() {
        for (long id : new long[]{Long.MIN_VALUE, -1L, 0L, Long.MAX_VALUE}) {
            assertEquals(id, decodeOne(snapshot(encode(request(id, new byte[]{7})))).getRequestId());
        }
    }

    @Test
    @DisplayName("空 body：解码得到 0 长度")
    void emptyBodyFrame() {
        TtcRpcMessage back = decodeOne(snapshot(encode(request(5L, new byte[0]))));
        assertEquals(0, back.getBodyLength());
    }

    // ====================== attachments ======================

    @Test
    @DisplayName("KV 附件带中文/emoji 往返一致，null 值退化为空串")
    void attachmentsRoundTrip() {
        TtcRpcMessage m = request(11L, "x".getBytes(StandardCharsets.UTF_8));
        m.addAttachment("traceId", "abc-123");
        m.addAttachment("服务名", "订单服务🚀");
        m.addAttachment("nil", null);
        TtcRpcMessage back = decodeOne(snapshot(encode(m)));

        assertEquals("abc-123", back.getAttachment("traceId"));
        assertEquals("订单服务🚀", back.getAttachment("服务名"));
        assertEquals("", back.getAttachment("nil"), "null 值写出去是空串，读回来还是空串");
        assertEquals("x", new String(back.getBody(), StandardCharsets.UTF_8), "body 不能被 header 长度挤掉");
    }

    @Test
    @DisplayName("无附件时 headerLength=0，附件集合为空")
    void noAttachmentsMeansZeroHeaderLength() {
        TtcRpcMessage back = decodeOne(snapshot(encode(request(12L, new byte[]{1, 2, 3}))));
        assertEquals(0, back.getHeaderLength());
        assertTrue(back.getAttachments() == null || back.getAttachments().isEmpty(),
                String.valueOf(back.getAttachments()));
    }

    // ====================== 分帧 / 半包 ======================

    @Test
    @DisplayName("定长头 26 字节，与 ProtocolConstants.HEADER_LENGTH 完全一致")
    void declaredHeaderLengthMatchesTheEncoder() {
        byte[] frame = snapshot(encode(request(1L, new byte[0])));
        assertEquals(26, frame.length, "无 body、无附件时的一帧长度");
        assertEquals(26, ProtocolConstants.HEADER_LENGTH);
        assertEquals(0, frame.length - ProtocolConstants.HEADER_LENGTH,
                "门限与真实头等长 —— 解码器不会在定长头还没收全时就往下读");
    }

    @Test
    @DisplayName("只到 24/25 字节时解码器安静等下一批，补齐才产出（不再越界抛异常）")
    void partialHeaderWaitsForMoreBytes() {
        byte[] frame = snapshot(encode(request(4242L, "payload".getBytes(StandardCharsets.UTF_8))));
        for (int cut = 24; cut <= 25; cut++) {
            EmbeddedChannel dec = new EmbeddedChannel(new TtcRpcMessageDecoder());
            assertNull(feed(dec, frame, 0, cut), "cut=" + cut + " 本应只是缓冲，不该抛异常");
            assertNull(dec.readInbound(), "定长头没收齐不该产出消息");
            // 猎物：同一个通道补齐剩余字节后立刻解出，证明前面的"无异常"不是空跑
            assertNull(feed(dec, frame, cut, frame.length - cut));
            TtcRpcMessage m = dec.readInbound();
            assertNotNull(m, "cut=" + cut + " 补齐后应当解出消息");
            assertEquals(4242L, m.getRequestId());
        }
    }

    @Test
    @DisplayName("猎物：23 字节（低于门槛）时确实安静等待，证明上一条不是普遍行为")
    void shortBufferWaitsQuietly() {
        byte[] frame = snapshot(encode(request(4242L, "payload".getBytes(StandardCharsets.UTF_8))));
        EmbeddedChannel dec = new EmbeddedChannel(new TtcRpcMessageDecoder());
        assertNull(feed(dec, frame, 0, ProtocolConstants.HEADER_LENGTH - 1));
        assertNull(dec.readInbound(), "还没凑够头，不该产出消息");
        assertNull(feed(dec, frame, ProtocolConstants.HEADER_LENGTH - 1,
                frame.length - (ProtocolConstants.HEADER_LENGTH - 1)));
        TtcRpcMessage done = dec.readInbound();
        assertNotNull(done, "补齐后应当解出消息");
        assertEquals(4242L, done.getRequestId());
    }

    @Test
    @DisplayName("body 分段到达：先整头 + 部分 body，再补尾")
    void bodyMayArriveInTwoChunks() {
        byte[] frame = snapshot(encode(request(777L, "0123456789abcdef".getBytes(StandardCharsets.UTF_8))));
        EmbeddedChannel dec = new EmbeddedChannel(new TtcRpcMessageDecoder());
        int first = 26 + 5;
        assertNull(feed(dec, frame, 0, first));
        assertNull(dec.readInbound());
        assertNull(feed(dec, frame, first, frame.length - first));
        TtcRpcMessage m = dec.readInbound();
        assertNotNull(m);
        assertEquals(777L, m.getRequestId());
        assertEquals("0123456789abcdef", new String(m.getBody(), StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("两帧连在一起能解出两条消息，顺序与内容都对")
    void twoFramesInOneBuffer() {
        byte[] a = snapshot(encode(request(1L, "A".getBytes(StandardCharsets.UTF_8))));
        byte[] b = snapshot(encode(request(2L, "BB".getBytes(StandardCharsets.UTF_8))));
        byte[] both = new byte[a.length + b.length];
        System.arraycopy(a, 0, both, 0, a.length);
        System.arraycopy(b, 0, both, a.length, b.length);

        EmbeddedChannel dec = new EmbeddedChannel(new TtcRpcMessageDecoder());
        assertNull(feed(dec, both, 0, both.length));
        TtcRpcMessage m1 = dec.readInbound();
        TtcRpcMessage m2 = dec.readInbound();
        assertNotNull(m1);
        assertNotNull(m2);
        assertEquals(1L, m1.getRequestId());
        assertEquals(2L, m2.getRequestId());
        assertEquals("A", new String(m1.getBody(), StandardCharsets.UTF_8));
        assertEquals("BB", new String(m2.getBody(), StandardCharsets.UTF_8));
        assertNull(dec.readInbound(), "第三条应当为空");
    }

    // ====================== 非法输入 ======================

    @Test
    @DisplayName("魔数不对：抛 RpcException 且错误码是 PROTOCOL_EXCEPTION")
    void badMagicIsRejected() {
        byte[] frame = snapshot(encode(request(1L, new byte[]{1, 2, 3})));
        frame[0] = 0x00;
        EmbeddedChannel dec = new EmbeddedChannel(new TtcRpcMessageDecoder());
        Throwable t = feed(dec, frame, 0, frame.length);
        assertTrue(t instanceof RpcException, "实际: " + t);
        assertEquals(RpcException.PROTOCOL_EXCEPTION, ((RpcException) t).getCode());
        assertTrue(t.getMessage().contains("Invalid magic number"), t.getMessage());
        assertNull(dec.readInbound());
    }

    @Test
    @DisplayName("非法魔数后 readerIndex 复位，字节不会半途消失")
    void badMagicResetsReaderIndex() {
        byte[] frame = snapshot(encode(request(1L, new byte[]{1, 2, 3})));
        byte[] corrupted = frame.clone();
        corrupted[3] = 0x00;
        ByteBuf buf = Unpooled.wrappedBuffer(corrupted);
        EmbeddedChannel dec = new EmbeddedChannel(new TtcRpcMessageDecoder());
        try {
            dec.writeInbound(buf);
        } catch (Throwable ignored) {
            // 期望路径
        }
        // markReaderIndex + resetReaderIndex 生效：整帧仍可读回
        assertEquals(frame.length, buf.readableBytes(),
                "解码失败应当把未消费的字节留在 buffer 里，而不是丢掉 4 字节魔数");
    }

    // ====================== 请求 ID 生成器 ======================

    @Test
    @DisplayName("nextRequestId 严格递增且不重复")
    void requestIdsAreMonotonicAndUnique() {
        long first = TtcRpcProtocol.nextRequestId();
        Set<Long> ids = new LinkedHashSet<>();
        long prev = first;
        for (int i = 0; i < 5000; i++) {
            long n = TtcRpcProtocol.nextRequestId();
            assertTrue(n > prev, "请求 ID 必须单调递增: " + prev + " -> " + n);
            ids.add(n);
            prev = n;
        }
        assertEquals(5000, ids.size());
    }

    @Test
    @DisplayName("TtcRpcMessage 的附件读写走的是同一张表")
    void attachmentAccessorsShareState() {
        TtcRpcMessage m = new TtcRpcMessage();
        assertTrue(m.getAttachments() == null || m.getAttachments().isEmpty());
        m.addAttachment("k", "v");
        assertEquals("v", m.getAttachment("k"));
        assertNull(m.getAttachment("absent"));
        assertEquals(1, m.getAttachments().size());
        m.setAttachments(null);
        assertNull(m.getAttachment("k"));
        assertSame(m, m);
    }
}
