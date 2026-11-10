package com.taotao.cloud.mq.common.message;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Message 单元测试
 */
public class MessageTest {

    @Test
    public void testDefaultConstructor() {
        Message message = new Message();
        assertNull(message.getTopic());
        assertNull(message.getBody());
        assertNull(message.getTags());
        assertNull(message.getKeys());
        assertNotNull(message.getProperties());
        assertTrue(message.getProperties().isEmpty());
        assertEquals(0, message.getFlag());
        assertEquals(0, message.getDelayTimeLevel());
    }

    @Test
    public void testConstructorWithTopicAndBody() {
        String topic = "TestTopic";
        byte[] body = "TestBody".getBytes(StandardCharsets.UTF_8);


        Message message = new Message(topic, body);


        assertEquals(topic, message.getTopic());
        assertArrayEquals(body, message.getBody());
        assertEquals(body.length, message.getBodyLength());
    }

    @Test
    public void testConstructorWithTopicTagsAndBody() {
        String topic = "TestTopic";
        String tags = "TagA||TagB";
        byte[] body = "TestBody".getBytes(StandardCharsets.UTF_8);


        Message message = new Message(topic, tags, body);


        assertEquals(topic, message.getTopic());
        assertEquals(tags, message.getTags());
        assertArrayEquals(body, message.getBody());
    }

    @Test
    public void testConstructorWithTopicTagsKeysAndBody() {
        String topic = "TestTopic";
        String tags = "TagA";
        String keys = "Key001";
        byte[] body = "TestBody".getBytes(StandardCharsets.UTF_8);


        Message message = new Message(topic, tags, keys, body);


        assertEquals(topic, message.getTopic());
        assertEquals(tags, message.getTags());
        assertEquals(keys, message.getKeys());
        assertArrayEquals(body, message.getBody());
    }

    @Test
    public void testPropertiesOperations() {
        Message message = new Message();

        // 测试putProperty和getProperty
        message.putProperty("key1", "value1");
        assertEquals("value1", message.getProperty("key1"));

        // 测试不存在的属性
        assertNull(message.getProperty("nonexistent"));

        // 测试覆盖
        message.putProperty("key1", "newValue");
        assertEquals("newValue", message.getProperty("key1"));

        // 测试getProperties
        Map<String, String> props = message.getProperties();
        assertEquals(1, props.size());
        assertEquals("newValue", props.get("key1"));
    }

    @Test
    public void testSetProperties() {
        Message message = new Message();
        Map<String, String> newProps = new HashMap<>();
        newProps.put("a", "1");
        newProps.put("b", "2");

        message.setProperties(newProps);

        assertEquals("1", message.getProperty("a"));
        assertEquals("2", message.getProperty("b"));
    }

    @Test
    public void testDelayTimeLevel() {
        Message message = new Message();


        // 默认延迟级别为0
        assertEquals(0, message.getDelayTimeLevel());


        // 设置延迟级别
        message.setDelayTimeLevel(3);
        assertEquals(3, message.getDelayTimeLevel());

        // 验证属性中是否包含DELAY
        assertEquals("3", message.getProperty("DELAY"));
    }

    @Test
    public void testGettersAndSetters() {
        Message message = new Message();

        // Topic
        message.setTopic("TestTopic");
        assertEquals("TestTopic", message.getTopic());

        // Body
        byte[] body = "TestBody".getBytes();
        message.setBody(body);
        assertArrayEquals(body, message.getBody());

        // Tags
        message.setTags("TagA");
        assertEquals("TagA", message.getTags());

        // Keys
        message.setKeys("Key001");
        assertEquals("Key001", message.getKeys());

        // Flag
        message.setFlag(123);
        assertEquals(123, message.getFlag());

        // TransactionId
        message.setTransactionId("TX123");
        assertEquals("TX123", message.getTransactionId());
    }

    @Test
    public void testBodyLength() {
        Message message = new Message();

        // 空body
        assertEquals(0, message.getBodyLength());

        // 非空body
        byte[] body = "Hello World".getBytes();
        message.setBody(body);
        assertEquals(body.length, message.getBodyLength());
    }

    @Test
    public void testToString() {
        Message message = new Message();
        message.setTopic("TestTopic");
        message.setTags("TagA");
        message.setKeys("Key001");
        message.setBody("TestBody".getBytes());

        String str = message.toString();

        assertNotNull(str);
        assertTrue(str.contains("TestTopic"));
        assertTrue(str.contains("TagA"));
        assertTrue(str.contains("Key001"));
        assertTrue(str.contains("bodyLength=8")); // TestBody length (8 chars == 8 bytes ASCII)
    }

    @Test
    public void testNullBodyInToString() {
        Message message = new Message();
        message.setTopic("TestTopic");
        // body is null

        String str = message.toString();
        assertNotNull(str);
        assertTrue(str.contains("null"));
    }
}
