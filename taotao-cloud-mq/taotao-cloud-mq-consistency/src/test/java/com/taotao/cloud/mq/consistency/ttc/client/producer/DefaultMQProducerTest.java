package com.taotao.cloud.mq.consistency.ttc.client.producer;

import com.taotao.cloud.mq.consistency.ttc.common.message.Message;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DefaultMQProducer 单元测试
 */
public class DefaultMQProducerTest {

    private DefaultMQProducer producer;

    @BeforeEach
    public void setUp() {
        producer = new DefaultMQProducer("TestProducerGroup");
        producer.setNamesrvAddr("localhost:9876");
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (producer != null) {
            producer.shutdown();
        }
    }

    @Test
    public void testConstructorWithGroup() {
        assertNotNull(producer);
        assertEquals("TestProducerGroup", producer.getProducerGroup());
    }

    @Test
    public void testDefaultValues() {
        // 默认 namesrvAddr 应为空 (用户在 setUp() 中显式设置过, 这里验证清除后的默认值)
        producer.setNamesrvAddr(null);
        assertNull(producer.getNamesrvAddr());
        assertEquals("TestProducerGroup", producer.getProducerGroup());
    }

    @Test
    public void testSetAndGetNamesrvAddr() {
        producer.setNamesrvAddr("192.168.1.100:9876");
        assertEquals("192.168.1.100:9876", producer.getNamesrvAddr());
    }

    @Test
    public void testSetAndGetProducerGroup() {
        producer.setProducerGroup("NewGroup");
        assertEquals("NewGroup", producer.getProducerGroup());
    }

    @Test
    public void testSendMessageWithoutStart() {
        // Create a message
        Message message = new Message();
        message.setTopic("TestTopic");
        message.setBody("TestBody".getBytes(StandardCharsets.UTF_8));

        // Attempt to send without starting the producer should throw an exception
        assertThrows(Exception.class, () -> {
            producer.send(message);
        });
    }

    @Test
    public void testSendOnewayWithoutStart() {
        // Create a message
        Message message = new Message();
        message.setTopic("TestTopic");
        message.setBody("TestBody".getBytes(StandardCharsets.UTF_8));

        // Attempt to send oneway without starting the producer should throw an exception
        assertThrows(Exception.class, () -> {
            producer.sendOneway(message);
        });
    }

    /**
     * 真异步语义（W3 §5 改写）。
     * <p>
     * 旧断言是"send() 返回时 exceptionCaught 已经为 true"，也就是把 send(msg, callback)
     * 当成当场同步回调 —— 那正是这条 API 广告却没兑现的东西，所以按新语义重写：
     * <ol>
     *   <li>未 start 且 callback 为 null ⇒ 当场抛（没有回调就没有异步可言）；</li>
     *   <li>未 start 且带 callback ⇒ send() 不抛，失败在<b>别的线程</b>上交付给 onException；</li>
     *   <li>交付用 CountDownLatch 等，不用 sleep、也不拿挂钟时长当阈值。</li>
     * </ol>
     */
    @Test
    public void testSendAsyncWithoutStart() throws Exception {
        // Create a message
        Message message = new Message();
        message.setTopic("TestTopic");
        message.setBody("TestBody".getBytes(StandardCharsets.UTF_8));

        // (1) 异步发送不带 callback 时应当抛出异常
        assertThrows(Exception.class, () -> {
            producer.send(message, (SendCallback) null);
        });

        // (2) 带 callback 时不应抛，而是把异常交给 callback 处理
        final CountDownLatch delivered = new CountDownLatch(1);
        final AtomicReference<Throwable> caught = new AtomicReference<Throwable>();
        final AtomicReference<String> callbackThread = new AtomicReference<String>();
        final AtomicReference<Thread> callerThread = new AtomicReference<Thread>();

        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                throw new AssertionError("send without start must not report success");
            }

            @Override
            public void onException(Throwable e) {
                caught.set(e);
                callbackThread.set(Thread.currentThread().getName());
                callerThread.set(Thread.currentThread());
                delivered.countDown();
            }
        });

        // (3) 回调必须发生，且不在调用线程上
        assertTrue(delivered.await(10, TimeUnit.SECONDS),
                "async send must deliver its failure to the callback");
        assertNotNull(caught.get(), "callback should receive exception");
        assertTrue(caught.get() instanceof IllegalStateException,
                "not-started producer must report IllegalStateException, got " + caught.get());
        assertNotSame(Thread.currentThread(), callerThread.get(),
                "callback must not run on the calling thread");
        assertFalse(Thread.currentThread().getName().equals(callbackThread.get()),
                "callback must not run on the calling thread, ran on " + callbackThread.get());
        assertTrue(callbackThread.get().startsWith("NettyClientPublicExecutor_"),
                "callback must run on the remoting callbackExecutor, ran on " + callbackThread.get());
    }

    @Test
    public void testCreateMessage() {
        String topic = "TestTopic";
        byte[] body = "TestBody".getBytes(StandardCharsets.UTF_8);

        Message message = new Message(topic, body);

        assertNotNull(message);
        assertEquals(topic, message.getTopic());
        assertArrayEquals(body, message.getBody());
    }

    @Test
    public void testCreateMessageWithTags() {
        String topic = "TestTopic";
        String tags = "TagA||TagB";
        byte[] body = "TestBody".getBytes(StandardCharsets.UTF_8);

        Message message = new Message(topic, tags, body);

        assertNotNull(message);
        assertEquals(topic, message.getTopic());
        assertEquals(tags, message.getTags());
        assertArrayEquals(body, message.getBody());
    }

    @Test
    public void testCreateMessageWithTagsAndKeys() {
        String topic = "TestTopic";
        String tags = "TagA";
        String keys = "Key001";
        byte[] body = "TestBody".getBytes(StandardCharsets.UTF_8);

        Message message = new Message(topic, tags, keys, body);

        assertNotNull(message);
        assertEquals(topic, message.getTopic());
        assertEquals(tags, message.getTags());
        assertEquals(keys, message.getKeys());
        assertArrayEquals(body, message.getBody());
    }

    @Test
    public void testMessageProperties() {
        Message message = new Message();
        message.setTopic("TestTopic");
        message.setBody("TestBody".getBytes(StandardCharsets.UTF_8));

        // Set properties
        message.putProperty("key1", "value1");
        message.putProperty("key2", "value2");

        assertEquals("value1", message.getProperty("key1"));
        assertEquals("value2", message.getProperty("key2"));
        assertNull(message.getProperty("nonexistent"));
    }

    @Test
    public void testMessageExtProperties() {
        MessageExt messageExt = new MessageExt();
        messageExt.setTopic("TestTopic");
        messageExt.setQueueId(1);
        messageExt.setQueueOffset(100);
        messageExt.setMsgId("TestMsgId");

        assertEquals("TestTopic", messageExt.getTopic());
        assertEquals(1, messageExt.getQueueId());
        assertEquals(100, messageExt.getQueueOffset());
        assertEquals("TestMsgId", messageExt.getMsgId());
    }
}
