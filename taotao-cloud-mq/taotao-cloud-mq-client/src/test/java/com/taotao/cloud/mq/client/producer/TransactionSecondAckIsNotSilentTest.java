package com.taotao.cloud.mq.client.producer;

import com.taotao.cloud.mq.client.MQClientInstance;
import com.taotao.cloud.mq.common.BrokerData;
import com.taotao.cloud.mq.common.QueueData;
import com.taotao.cloud.mq.common.TopicRouteData;
import com.taotao.cloud.mq.common.enums.TransactionState;
import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.exception.RemotingConnectException;
import com.taotao.cloud.mq.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.remoting.exception.RemotingTimeoutException;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * 事务生产者这一侧的两句话：
 * <ol>
 *   <li><b>二次确认失败不许静默。</b>确认送不出去 = 这条事务最终会被 broker 判回滚，
 *       调用方必须在当场知道，而不是在日志里翻。判据是"抛，而且异常里认得出是哪条事务"。</li>
 *   <li><b>业务 topic 不许在发送前被就地覆盖。</b>半消息靠请求码分流，不靠改 topic ——
 *       改掉 topic 的那一刻，broker 侧就再也不知道这条消息原本属于谁。</li>
 * </ol>
 * 这里用一个覆写 RPC 出口的 {@link FakeInstance} 把 broker 的答复钉成可控的两条分支
 * （接受 / 拒绝）：真实现里 broker 拒绝是普通事件，测试要的只是"拒绝这一支到底传没传给调用方"。
 */
public class TransactionSecondAckIsNotSilentTest {

    private static final String TOPIC = "TxSecondAckTopic";
    private static final String BROKER_NAME = "broker-txsecondack";
    private static final String BROKER_ADDR = "127.0.0.1:10911";

    private FakeInstance instance;
    private TransactionMQProducer producer;

    @BeforeEach
    public void setUp() {
        instance = new FakeInstance();
        instance.updateTopicRouteData(TOPIC, buildRoute());
        producer = new TransactionMQProducer("TxSecondAckGroup", new NettyClientConfig());
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setClientId("tx-second-ack-tester");
        // 同包可直接写 protected 字段：把 producer 的 RPC 出口换成假实例
        producer.mqClientInstance = instance;
    }

    @AfterEach
    public void tearDown() {
        if (producer != null) {
            try {
                producer.shutdown();
            } catch (Exception ignore) {
                // 假实例没有真连接，收尸时忽略
            }
        }
    }

    @Test
    @DisplayName("broker 拒绝二次确认时调用方必须收到异常，且异常里认得出这条事务")
    public void rejectedSecondPhaseIsSurfacedToTheCaller() throws Exception {
        instance.rejectEndTransaction = true;
        producer.setTransactionListener(listener(TransactionState.COMMIT));

        Message message = message();
        Exception thrown = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                producer.sendMessageInTransaction(message, null);
            }
        }, "二次确认被 broker 拒绝时必须抛，不许只留一行 warn");
        assertTrue(thrown.getMessage() != null
                        && thrown.getMessage().contains(message.getProperty("TRANSACTION_ID")),
                "异常里必须认得出是哪条事务没确认成功, 实际: " + thrown.getMessage());

        // 阳性对照：同一个假实例只是不再拒绝，这条路径就必须安静地走完
        instance.rejectEndTransaction = false;
        Message control = message();
        SendResult ok = producer.sendMessageInTransaction(control, null);
        assertEquals(SendStatus.SEND_OK, ok.getSendStatus(), "阳性对照：确认被接受时调用方不该收到任何异常");
        assertEquals(2, countCode(instance.allRequests, RequestCode.END_TRANSACTION),
                "两次调用就该有两次二次确认（阳性对照的计数也得对得上）");
    }

    @Test
    @DisplayName("未启动的生产者调用补确认也必须抛，不许静默返回")
    public void endTransactionWithoutClientInstanceThrows() throws Exception {
        producer.mqClientInstance = null;
        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                producer.endTransaction("tx-never-sent", TOPIC, TransactionState.COMMIT);
            }
        }, "确认根本没发出去却安静返回，等于把\"提交失败了\"这件事从调用方眼前抹掉");
    }

    @Test
    @DisplayName("半消息走 201，且业务 topic 全程保持原样")
    public void halfMessageKeepsItsBusinessTopicAndTravelsOnItsOwnRequestCode() throws Exception {
        producer.setTransactionListener(listener(TransactionState.UNKNOWN));
        Message message = message();
        SendResult sent = producer.sendMessageInTransaction(message, null);
        assertEquals(SendStatus.SEND_OK, sent.getSendStatus());

        assertEquals(TOPIC, message.getTopic(),
                "业务 topic 不许被就地改掉（改掉了 broker 就不知道这条属于谁）");
        RemotingCommand half = firstOf(instance.allRequests, RequestCode.SEND_MESSAGE_V2);
        assertNotNull(half, "半消息必须走 SEND_MESSAGE_V2 —— 走 200 就会被普通发送路径直接投给消费者");
        assertEquals(TOPIC, half.getExtField("topic"), "请求里的 topic 扩展字段仍是业务 topic");
        MessageExt body = JsonCodec.decode(half.getBody(), MessageExt.class);
        assertNotNull(body, "半消息的请求体必须是那条消息");
        assertEquals(TOPIC, body.getTopic(), "消息体里的 topic 也必须是业务 topic（broker 靠它认原始 topic）");
        assertEquals(0, countCode(instance.allRequests, RequestCode.SEND_MESSAGE),
                "同一次事务发送不许再走一遍普通发送通道（那就是双投的另一半）");
    }

    // ==================== fixture ====================

    private static TransactionListener listener(final TransactionState localState) {
        return new TransactionListener() {
            @Override
            public TransactionState executeLocalTransaction(Message half, Object arg) {
                return localState;
            }

            @Override
            public TransactionState checkLocalTransaction(Message half) {
                return localState;
            }
        };
    }

    private static Message message() {
        Message message = new Message();
        message.setTopic(TOPIC);
        message.setBody("tx-second-ack".getBytes(StandardCharsets.UTF_8));
        return message;
    }

    private static int countCode(List<RemotingCommand> requests, int code) {
        int n = 0;
        for (RemotingCommand r : requests) {
            if (r.getCode() == code) {
                n++;
            }
        }
        return n;
    }

    private static RemotingCommand firstOf(List<RemotingCommand> requests, int code) {
        for (RemotingCommand r : requests) {
            if (r.getCode() == code) {
                return r;
            }
        }
        return null;
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
        brokerDatas.add(new BrokerData("cluster-txsecondack", BROKER_NAME, addrs));
        route.setBrokerDatas(brokerDatas);
        return route;
    }

    /** 只覆写 RPC 出口的假实例：不建真连接，答复按码可控。 */
    private static class FakeInstance extends MQClientInstance {

        final NettyRemotingClient remotingMock = mock(NettyRemotingClient.class);
        final List<RemotingCommand> allRequests = new ArrayList<RemotingCommand>();
        volatile boolean rejectEndTransaction;

        FakeInstance() {
            super("tx-second-ack-instance", "127.0.0.1:9876", new NettyClientConfig());
        }

        @Override
        public NettyRemotingClient getRemotingClient() {
            return remotingMock;
        }

        @Override
        public RemotingCommand invokeSync(String addr, RemotingCommand request, long timeoutMillis)
                throws RemotingConnectException, RemotingSendRequestException,
                RemotingTimeoutException, InterruptedException {
            synchronized (allRequests) {
                allRequests.add(request);
            }
            if (request.getCode() == RequestCode.END_TRANSACTION && rejectEndTransaction) {
                RemotingCommand rejected = RemotingCommand.createResponseCommand(
                        RemotingSysResponseCode.SYSTEM_ERROR);
                rejected.setRemark("transaction not found: simulated rejection");
                return rejected;
            }
            return RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
        }

        @Override
        public void invokeAsync(String addr, RemotingCommand request, long timeoutMillis,
                                NettyRemotingAbstract.InvokeCallback callback)
                throws RemotingConnectException, InterruptedException, Exception {
            synchronized (allRequests) {
                allRequests.add(request);
            }
        }
    }
}
