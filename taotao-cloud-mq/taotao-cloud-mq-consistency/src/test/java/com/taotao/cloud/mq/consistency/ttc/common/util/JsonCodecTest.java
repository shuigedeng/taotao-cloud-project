package com.taotao.cloud.mq.consistency.ttc.common.util;

import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JsonCodec 单元测试 — 覆盖基础 POJO 与 List 的序列化往返。
 */
public class JsonCodecTest {

    @Test
    public void testEncodeDecodePojo() {
        SendResult src = SendResult.ok("msg-1", "TopicA", 1, 10L);
        byte[] bytes = JsonCodec.encode(src);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);

        SendResult decoded = JsonCodec.decode(bytes, SendResult.class);
        assertEquals("msg-1", decoded.getMsgId());
        assertEquals("TopicA", decoded.getTopic());
        assertEquals(1, decoded.getQueueId());
        assertEquals(10L, decoded.getQueueOffset());
        assertEquals(SendStatus.SEND_OK, decoded.getSendStatus());
    }

    @Test
    public void testEncodeDecodeList() {
        List<SendResult> list = new ArrayList<>();
        list.add(SendResult.ok("a", "T1", 0, 0L));
        list.add(SendResult.ok("b", "T2", 1, 1L));
        list.add(SendResult.ok("c", "T3", 2, 2L));

        byte[] bytes = JsonCodec.encode(list);
        List<SendResult> decoded = JsonCodec.decodeList(bytes, SendResult.class);
        assertEquals(3, decoded.size());
        assertEquals("a", decoded.get(0).getMsgId());
        assertEquals("c", decoded.get(2).getMsgId());
    }

    @Test
    public void testToJsonAndFromJson() {
        SendResult src = SendResult.ok("id", "Topic", 0, 0L);
        String json = JsonCodec.toJson(src);
        assertNotNull(json);
        assertTrue(json.contains("id"));

        SendResult decoded = JsonCodec.fromJson(json, SendResult.class);
        assertEquals("id", decoded.getMsgId());
        assertEquals("Topic", decoded.getTopic());
    }

    @Test
    public void testDecodeInvalidBytesThrows() {
        byte[] garbage = new byte[]{0x00, 0x01, 0x02, 0x03};
        assertThrows(RuntimeException.class, () -> JsonCodec.decode(garbage, SendResult.class));
    }

    @Test
    public void testDecodeInvalidJsonStringThrows() {
        assertThrows(RuntimeException.class, () -> JsonCodec.fromJson("{not-json", SendResult.class));
    }

    @Test
    public void testEncodeIgnoresUnknownProperties() {
        // JsonCodec 配置了 FAIL_ON_UNKNOWN_PROPERTIES=false, 含未知字段的 JSON 也能解码
        String json = "{\"msgId\":\"m\",\"unknownField\":\"x\",\"topic\":\"T\"}";
        SendResult r = JsonCodec.fromJson(json, SendResult.class);
        assertEquals("m", r.getMsgId());
        assertEquals("T", r.getTopic());
    }

    @Test
    public void testRoundTripPreservesNullMessageQueue() {
        SendResult src = new SendResult(SendStatus.NO_ROUTE);
        src.setErrorMsg("oops");
        byte[] bytes = JsonCodec.encode(src);
        SendResult out = JsonCodec.decode(bytes, SendResult.class);
        assertEquals(SendStatus.NO_ROUTE, out.getSendStatus());
        assertEquals("oops", out.getErrorMsg());
        assertNull(out.getMessageQueue());
    }

    @Test
    public void testEncodeEmptyList() {
        List<SendResult> empty = new ArrayList<>();
        byte[] bytes = JsonCodec.encode(empty);
        List<SendResult> out = JsonCodec.decodeList(bytes, SendResult.class);
        assertTrue(out.isEmpty());
    }
}
