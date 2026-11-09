package com.taotao.cloud.mq.consistency.ttc.client.producer;

import com.taotao.cloud.mq.consistency.ttc.client.MQClientInstance;
import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;
import com.taotao.cloud.mq.consistency.ttc.common.QueueData;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.common.message.Message;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendStatus;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingConnectException;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingTimeoutException;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.ResponseFuture;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * W2a：client 在"根本没拿到 broker 响应"时的行为尺（null 响应不许被洗成任何 SendStatus）.
 * <p>
 * 这张照的入参是 {@code invokeSync} 返回 {@code null} —— 也就是 remoting 契约被违反的那一刻。
 * 生产栈里 null 到不了 producer（证据见工单 §2 的复跑：{@code NettyRemotingAbstract.invokeSyncImpl}
 * 拿到 null 响应一律抛 {@code RemotingTimeoutException}/{@code RemotingSendRequestException}，
 * {@code MQClientInstance.invokeSync} 拿不到 channel 一律抛 {@code RemotingConnectException}），
 * 所以这里只能靠一个覆写 RPC 出口的 {@link RecordingInstance} 把 null 喂进去 ——
 * 这正是"契约被违反时可达"的意思，不是"生产可达"。
 */
public class NullResponseSendTest {

    private static final String TOPIC = "NullResponseTopic";
    private static final String BROKER_NAME = "broker-nullresp";
    /** 假地址：{@link RecordingInstance} 覆写了 RPC 出口，测试全程不建真连接. */
    private static final String BROKER_ADDR = "127.0.0.1:10911";

    private RecordingInstance instance;
    private DefaultMQProducer producer;

    @BeforeEach
    public void setUp() {
        instance = new RecordingInstance();
        instance.updateTopicRouteData(TOPIC, buildRoute());

        producer = new DefaultMQProducer("NullResponseGroup", new NettyClientConfig());
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setClientId("null-response-tester");
        // 同包可直接写 protected 字段：把 producer 的 RPC 出口换成假实例
        producer.mqClientInstance = instance;
    }

    @AfterEach
    public void tearDown() {
        if (producer != null) {
            producer.shutdown();
        }
    }

    private static TopicRouteData buildRoute() {
        TopicRouteData route = new TopicRouteData();

        List<QueueData> queueDatas = new ArrayList<QueueData>();
        QueueData qd = new QueueData();
        qd.setBrokerName(BROKER_NAME);
        qd.setReadQueueNums(1);
        qd.setWriteQueueNums(1);
        qd.setPerm(6);
        queueDatas.add(qd);
        route.setQueueDatas(queueDatas);

        List<BrokerData> brokerDatas = new ArrayList<BrokerData>();
        HashMap<Long, String> addrs = new HashMap<Long, String>();
        addrs.put(BrokerData.MASTER_ID, BROKER_ADDR);
        brokerDatas.add(new BrokerData("cluster-nullresp", BROKER_NAME, addrs));
        route.setBrokerDatas(brokerDatas);

        return route;
    }

    private static Message message() {
        Message message = new Message();
        message.setTopic(TOPIC);
        message.setBody("null-response-probe".getBytes(StandardCharsets.UTF_8));
        return message;
    }

    /**
     * 主尺 1：{@code send(Message)}（走 toSendResult 那一条）拿到 null 响应时必须抛，
     * 且异常里要带足以定位这条请求的信息（topic / brokerAddr / opaque）。
     * <p>
     * <b>改之前的现状</b>（W2a §3.1 留的照，§3.3 已把判据翻成"必须抛"）：不抛，
     * 返回一个 {@code sendStatus == FLUSH_DISK_TIMEOUT} 的 SendResult —— 那是 client 凭空造的码。
     */
    @Test
    public void syncSendWithNoResponseMustThrowInsteadOfFabricatingAFlushStatus() throws Exception {
        instance.returnNull = true;

        RemotingSendRequestException thrown = assertThrows(RemotingSendRequestException.class, () -> {
            SendResult result = producer.send(message());
            throw new AssertionError("null response must not produce a SendResult, got: " + describe(result));
        });

        assertEquals(1, instance.syncCalls, "the fake RPC出口 must actually be the thing that returned null");
        assertNotNull(instance.lastRequest, "the request that got no response must be recorded");
        String msg = thrown.getMessage();
        assertTrue(msg.contains(TOPIC), "message must carry the topic, was: " + msg);
        assertTrue(msg.contains(BROKER_ADDR), "message must carry the broker addr, was: " + msg);
        assertTrue(msg.contains("opaque=" + instance.lastRequest.getOpaque()),
                "message must carry the request opaque, was: " + msg);
        // 造出来的码一个都不许出现在这条异常里：没响应就是"不知道结果"，不是任何一种刷盘结论
        assertFalse(msg.contains("FLUSH"), "no flush verdict may be reported without a response: " + msg);
    }

    /**
     * 主尺 2：{@code send(Message, MessageQueue)}（就地翻译那一条）同上。
     */
    @Test
    public void sendToChosenQueueWithNoResponseMustThrowInsteadOfFabricatingAFlushStatus() throws Exception {
        instance.returnNull = true;
        MessageQueue mq = new MessageQueue(TOPIC, BROKER_NAME, 0);

        RemotingSendRequestException thrown = assertThrows(RemotingSendRequestException.class, () -> {
            SendResult result = producer.send(message(), mq);
            throw new AssertionError("null response must not produce a SendResult, got: " + describe(result));
        });

        assertEquals(1, instance.syncCalls, "the fake RPC出口 must actually be the thing that returned null");
        assertNotNull(instance.lastRequest, "the request that got no response must be recorded");
        String msg = thrown.getMessage();
        assertTrue(msg.contains(TOPIC), "message must carry the topic, was: " + msg);
        assertTrue(msg.contains(BROKER_ADDR), "message must carry the broker addr, was: " + msg);
        assertTrue(msg.contains("opaque=" + instance.lastRequest.getOpaque()),
                "message must carry the request opaque, was: " + msg);
        assertFalse(msg.contains("FLUSH"), "no flush verdict may be reported without a response: " + msg);
    }

    /**
     * 对照尺（异步侧）：remoting 交回一个"没有响应也没有异常"的 ResponseFuture 时，
     * <ol>
     *   <li>{@code send()} 当场不许回调（失败必须在别的线程上交付 —— 这条钉的是
     *       {@code DefaultMQProducer} 已有的 {@code cause == null && getResponseCommand() == null}
     *       分支，W3 写的，本支不重复实现，只在它塌掉时报警）；</li>
     *   <li>回调交付的是异常，绝不是一个凭空造的 {@code FLUSH_DISK_TIMEOUT} 结果。</li>
     * </ol>
     */
    @Test
    public void asyncSendWithNoResponseDeliversAnExceptionLaterNeverAFabricatedFlushStatus() throws Exception {
        final AtomicInteger onSuccessCalls = new AtomicInteger(0);
        final AtomicInteger callbackCallsDuringSend = new AtomicInteger(0);
        final AtomicReference<SendResult> successResult = new AtomicReference<SendResult>();
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        final AtomicInteger onExceptionCalls = new AtomicInteger(0);

        SendCallback callback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (callbackCallsDuringSend.get() > 0) {
                    callbackCallsDuringSend.incrementAndGet();
                }
                successResult.set(sendResult);
                onSuccessCalls.incrementAndGet();
            }

            @Override
            public void onException(Throwable e) {
                if (callbackCallsDuringSend.get() > 0) {
                    callbackCallsDuringSend.incrementAndGet();
                }
                failure.set(e);
                onExceptionCalls.incrementAndGet();
            }
        };

        producer.send(message(), callback);

        // (1) send() 返回的那一刻，回调一次都没跑过
        assertEquals(0, onSuccessCalls.get(), "no success may be delivered inside send()");
        assertEquals(0, onExceptionCalls.get(), "no failure may be delivered on the calling thread inside send()");
        assertEquals(1, instance.syncCalls, "the async RPC entry must have been reached exactly once");
        assertNotNull(instance.capturedCallback, "the InvokeCallback handed to remoting must be captured");

        // 之后才允许异步交付：把哨兵置 1，任何在 send() 之后再跑的回调都算"迟到交付"，不是当场
        callbackCallsDuringSend.set(1);

        // (2) 由 remoting 侧触发"无响应、无异常"的完成事件
        ResponseFuture future = new ResponseFuture(null, instance.lastRequest.getOpaque(), 3000L, null, null);
        assertNull(future.getResponseCommand(), "precondition: no response command in this future");
        assertNull(future.getCause(), "precondition: no cause either");
        instance.capturedCallback.operationComplete(future);

        assertEquals(0, onSuccessCalls.get(),
                "an unanswered async send must never report success, let alone with a fabricated status: "
                        + describe(successResult.get()));
        assertEquals(1, onExceptionCalls.get(), "the unanswered send must be delivered as a failure");
        assertTrue(failure.get() instanceof RemotingTimeoutException,
                "no-response-no-cause must surface as a timeout, got: " + failure.get());
        assertTrue(callbackCallsDuringSend.get() >= 1, "the delivery happened after send() returned");
    }

    private static String describe(SendResult result) {
        if (result == null) {
            return "<no SendResult>";
        }
        SendStatus status = result.getSendStatus();
        return "SendResult[status=" + status + ", msgId=" + result.getMsgId() + "]";
    }

    /** 只换掉 RPC 出口：不建真连接，按开关返回 null / 捕获异步回调. */
    private static class RecordingInstance extends MQClientInstance {

        final NettyRemotingClient remotingMock = mock(NettyRemotingClient.class);
        volatile boolean returnNull;
        volatile int syncCalls;
        volatile String lastAddr;
        volatile RemotingCommand lastRequest;
        volatile NettyRemotingAbstract.InvokeCallback capturedCallback;

        RecordingInstance() {
            super("null-response-instance", "127.0.0.1:9876", new NettyClientConfig());
            // 不调用 start()：getRemotingClient() 已被覆写成返回 mock，producer 只需它非空
        }

        @Override
        public NettyRemotingClient getRemotingClient() {
            return remotingMock;
        }

        @Override
        public RemotingCommand invokeSync(String addr, RemotingCommand request, long timeoutMillis)
                throws RemotingConnectException, RemotingSendRequestException,
                RemotingTimeoutException, InterruptedException {
            record(addr, request);
            // returnNull=true 就是在模拟"remoting 契约被违反"：真实现这里要么抛要么非空
            return returnNull ? null : newSuccessResponse();
        }

        @Override
        public void invokeAsync(String addr, RemotingCommand request, long timeoutMillis,
                                NettyRemotingAbstract.InvokeCallback callback)
                throws RemotingConnectException, InterruptedException, Exception {
            record(addr, request);
            this.capturedCallback = callback;
        }

        private void record(String addr, RemotingCommand request) {
            this.lastAddr = addr;
            this.lastRequest = request;
            this.syncCalls = this.syncCalls + 1;
        }

        private static RemotingCommand newSuccessResponse() {
            RemotingCommand response = RemotingCommand.createResponseCommand(
                    com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode.SUCCESS);
            response.setBody("unused".getBytes(StandardCharsets.UTF_8));
            return response;
        }
    }
}
