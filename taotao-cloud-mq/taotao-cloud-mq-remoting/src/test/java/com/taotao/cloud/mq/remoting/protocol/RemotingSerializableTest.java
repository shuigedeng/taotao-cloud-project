package com.taotao.cloud.mq.remoting.protocol;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RemotingSerializable 单元测试
 */
public class RemotingSerializableTest {

    @Test
    public void testEncodeObject() {
        TestSerializable obj = new TestSerializable();
        obj.setName("test");
        obj.setValue(100);

        byte[] encoded = obj.encode();
        assertNotNull(encoded);
        assertTrue(encoded.length > 0);
    }

    @Test
    public void testToJson() {
        TestSerializable obj = new TestSerializable();
        obj.setName("test");
        obj.setValue(100);

        String json = obj.toJson();
        assertNotNull(json);
        assertTrue(json.contains("test"));
        assertTrue(json.contains("100"));
    }

    @Test
    public void testDecodeWithClass() {
        String json = "{\"name\":\"test\",\"value\":100}";
        byte[] data = json.getBytes(StandardCharsets.UTF_8);

        TestSerializable decoded = RemotingSerializable.decode(data, TestSerializable.class);
        assertNotNull(decoded);
        assertEquals("test", decoded.getName());
        assertEquals(100, decoded.getValue());
    }

    @Test
    public void testDecodeWithTypeReference() {
        String json = "{\"name\":\"test\",\"value\":100}";
        byte[] data = json.getBytes(StandardCharsets.UTF_8);

        TestSerializable decoded = RemotingSerializable.decode(data, new TypeReference<TestSerializable>() {
        });
        assertNotNull(decoded);
        assertEquals("test", decoded.getName());
        assertEquals(100, decoded.getValue());
    }

    @Test
    public void testDecodeWithNullData() {
        TestSerializable decoded = RemotingSerializable.decode(null, TestSerializable.class);
        assertNull(decoded);
    }

    @Test
    public void testDecodeWithEmptyData() {
        TestSerializable decoded = RemotingSerializable.decode(new byte[0], TestSerializable.class);
        assertNull(decoded);
    }

    @Test
    public void testFromJsonWithClass() {
        String json = "{\"name\":\"test\",\"value\":100}";

        TestSerializable decoded = RemotingSerializable.fromJson(json, TestSerializable.class);
        assertNotNull(decoded);
        assertEquals("test", decoded.getName());
        assertEquals(100, decoded.getValue());
    }

    @Test
    public void testFromJsonWithTypeReference() {
        String json = "{\"name\":\"test\",\"value\":100}";

        TestSerializable decoded = RemotingSerializable.fromJson(json, new TypeReference<TestSerializable>() {
        });
        assertNotNull(decoded);
        assertEquals("test", decoded.getName());
        assertEquals(100, decoded.getValue());
    }

    @Test
    public void testFromJsonWithNull() {
        TestSerializable decoded = RemotingSerializable.fromJson(null, TestSerializable.class);
        assertNull(decoded);
    }

    @Test
    public void testFromJsonWithEmpty() {
        TestSerializable decoded = RemotingSerializable.fromJson("", TestSerializable.class);
        assertNull(decoded);
    }

    @Test
    public void testStaticEncode() {
        TestSerializable obj = new TestSerializable();
        obj.setName("test");
        obj.setValue(100);

        byte[] encoded = RemotingSerializable.encode(obj);
        assertNotNull(encoded);
        assertTrue(encoded.length > 0);
    }

    @Test
    public void testStaticEncodeWithNull() {
        byte[] encoded = RemotingSerializable.encode(null);
        assertNull(encoded);
    }

    @Test
    public void testStaticToJson() {
        TestSerializable obj = new TestSerializable();
        obj.setName("test");
        obj.setValue(100);

        String json = RemotingSerializable.toJson(obj);
        assertNotNull(json);
        assertTrue(json.contains("test"));
        assertTrue(json.contains("100"));
    }

    @Test
    public void testStaticToJsonWithNull() {
        String json = RemotingSerializable.toJson(null);
        assertNull(json);
    }

    @Test
    public void testComplexObjectWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", 123);
        map.put("key3", true);

        byte[] encoded = RemotingSerializable.encode(map);
        assertNotNull(encoded);

        Map<String, Object> decoded = RemotingSerializable.decode(encoded, new TypeReference<Map<String, Object>>() {
        });
        assertNotNull(decoded);
        assertEquals("value1", decoded.get("key1"));
        assertEquals(123, decoded.get("key2"));
        assertEquals(true, decoded.get("key3"));
    }

    /**
     * Test serializable class
     */
    public static class TestSerializable extends RemotingSerializable {
        private String name;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
