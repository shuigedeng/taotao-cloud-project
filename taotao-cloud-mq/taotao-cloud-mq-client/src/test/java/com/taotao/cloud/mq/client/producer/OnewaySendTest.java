package com.taotao.cloud.mq.client.producer;

import com.taotao.cloud.mq.common.BrokerData;
import com.taotao.cloud.mq.common.MessageQueue;
import com.taotao.cloud.mq.common.QueueData;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.remoting.netty.*;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * sendOneway 真走 invokeOneway（W3 §4 的验收尺）。
 * <p>
 * 旧实现是 {@code request.markOnewayRPC()} 之后紧接着 {@code invokeSync(...)}：broker 端对
 * oneway 请求根本不写响应（{@code NettyRemotingAbstract.processRequestCommand} 的
 * {@code if (!cmd.isOnewayRPC())} 分支），于是每一次 oneway 发送都必然阻塞满
 * sendMsgTimeoutMillis 再抛 {@code RemotingTimeoutException}。
 * <p>
 * 形状：起一个真 in-process server，发 N 条 oneway，断言
 * <ol>
 *   <li>broker 侧 processor 真被调到 N 次（计数器 latch，不是 sleep）；</li>
 *   <li>每条请求到达时都带着 oneway 标记（标记必须在帧里活过了编解码）；</li>
 *   <li>发送本身正常返回、不抛超时 —— {@link #SEND_TIMEOUT_MILLIS} 故意配得远大于测试预算，
 *       一旦有人改回 invokeSync，这条用例会卡满超时并抛错，而不是靠"耗时刚好小于阈值"侥幸通过。</li>
 * </ol>
 * 附带一条对照：同一个 server 上同步 send 仍然拿得到响应，证明上面的"没有响应"是 oneway 造成的，
 * 不是 server 坏了。
 */
public class OnewaySendTest {

    private static final String TOPIC = "OnewaySendTopic";
    private static final String BROKER_NAME = "broker-oneway";
    private static final int MESSAGES = 5;

    /** 故意远大于一条测试该花的时间：回归成 invokeSync 就会挂满它. */
    private static final long SEND_TIMEOUT_MILLIS = 60000L;

    private NettyRemotingServer server;
    private String serverAddr;
    private CountingProcessor processor;
    private DefaultMQProducer producer;

    @BeforeEach
    public void setUp() throws Exception {
        NettyServerConfig serverConfig = new NettyServerConfig();
        // listenPort = 0 ⇒ 内核分配，随后回读；绝不写死 32768-60999 之间的端口
        serverConfig.setListenPort(0);
        server = new NettyRemotingServer(serverConfig);
        processor = new CountingProcessor();
        server.registerProcessor(RequestCode.SEND_MESSAGE, processor, null);
        server.start();
        serverAddr = "127.0.0.1:" + server.localListenPort();

        producer = new DefaultMQProducer("OnewaySendGroup", new NettyClientConfig());
        producer.setNamesrvAddr(serverAddr);
        producer.setSendMsgTimeoutMillis(SEND_TIMEOUT_MILLIS);
        producer.start();

        // 把路由直接钉到这台 in-process server，绕开 nameserver 解析
        producer.mqClientInstance.updateTopicRouteData(TOPIC, buildRoute());
    }

    @AfterEach
    public void tearDown() {
        if (producer != null) {
            producer.shutdown();
        }
        if (server != null) {
            server.shutdown();
        }
    }

    private TopicRouteData buildRoute() {
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
        addrs.put(BrokerData.MASTER_ID, serverAddr);
        brokerDatas.add(new BrokerData("cluster-oneway", BROKER_NAME, addrs));
        route.setBrokerDatas(brokerDatas);

        return route;
    }

    /** 数请求条数的处理器，并记下每条请求到站时的 oneway 标记. */
    private static class CountingProcessor implements NettyRemotingAbstract.NettyRequestProcessor {
        private final AtomicInteger handled = new AtomicInteger(0);
        private final List<Boolean> onewayFlags = new CopyOnWriteArrayList<Boolean>();
        private volatile CountDownLatch latch = new CountDownLatch(0);

        void arm(int expected) {
            this.latch = new CountDownLatch(expected);
        }

        @Override
        public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
            handled.incrementAndGet();
            onewayFlags.add(Boolean.valueOf(request.isOnewayRPC()));
            latch.countDown();

            RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
            response.setBody("ok".getBytes(StandardCharsets.UTF_8));
            return response;
        }

        @Override
        public boolean rejectRequest() {
            return false;
        }
    }

    private Message message(int seq) {
        Message message = new Message();
        message.setTopic(TOPIC);
        message.setBody(("oneway-" + seq).getBytes(StandardCharsets.UTF_8));
        return message;
    }

    /**
     * 主尺：N 条 oneway 全部到站、全部带 oneway 标记、发送端不抛超时。
     */
    @Test
    public void sendOnewayDoesNotWaitForResponse() throws Exception {
        processor.arm(MESSAGES);

        for (int i = 0; i < MESSAGES; i++) {
            producer.sendOneway(message(i));
        }

        assertTrue(processor.latch.await(30, TimeUnit.SECONDS),
                "broker processor must see all " + MESSAGES + " oneway requests; saw "
                        + processor.handled.get());

        assertEquals(MESSAGES, processor.handled.get(), "each oneway send must reach the processor once");
        assertEquals(MESSAGES, processor.onewayFlags.size());
        for (int i = 0; i < processor.onewayFlags.size(); i++) {
            assertTrue(processor.onewayFlags.get(i).booleanValue(),
                    "request #" + i + " must arrive flagged as onewayRPC");
        }
    }

    /**
     * 对照尺：同一台 server 上同步 send 必须仍然拿得到响应。
     * 没有这条，上面"收不到响应"就可能只是 server 配坏了。
     */
    @Test
    public void syncSendOnTheSameServerStillGetsAResponse() throws Exception {
        processor.arm(1);

        SendResult result = producer.send(message(0));

        assertNotNull(result, "sync send must return a result");
        assertTrue(processor.latch.await(30, TimeUnit.SECONDS), "processor must see the sync request");
        assertEquals(1, processor.handled.get());
        assertEquals(1, processor.onewayFlags.size());
        assertFalse(processor.onewayFlags.get(0).booleanValue(),
                "a sync send must NOT be flagged as oneway");
    }

    /**
     * 按指定队列的 send(msg, mq) 不许被带上 oneway 标记。
     */
    @Test
    public void sendToChosenQueueIsNotOneway() throws Exception {
        processor.arm(1);
        MessageQueue mq = new MessageQueue(TOPIC, BROKER_NAME, 0);

        producer.send(message(0), mq);

        assertTrue(processor.latch.await(30, TimeUnit.SECONDS), "processor must see the request");
        assertEquals(1, processor.onewayFlags.size());
        assertFalse(processor.onewayFlags.get(0).booleanValue(),
                "send(msg, mq) is a sync call and must not be flagged oneway");
    }
}
