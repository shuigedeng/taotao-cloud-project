package com.taotao.cloud.mq.consistency.ttc.client;

import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingConnectException;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingTimeoutException;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingClient;
import io.netty.channel.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * MQClientInstance 单元测试 — 覆盖 channel 缓存 / NPE 防御 / 路由拉取 路径。
 * <p>
 * 大部分场景需要 Netty 真实连接, 这里用 Mockito 隔离 remotingClient
 * 来覆盖关键分支 (返回 null / 抛异常 / 缓存命中)。
 */
public class MQClientInstanceTest {

    private MQClientInstance instance;
    private NettyRemotingClient mockClient;

    @BeforeEach
    public void setUp() throws Exception {
        instance = new MQClientInstance("TestClient", "localhost:9876", new NettyClientConfig());
        mockClient = mock(NettyRemotingClient.class);
        // 反射注入 mock remotingClient
        Field f = MQClientInstance.class.getDeclaredField("remotingClient");
        f.setAccessible(true);
        f.set(instance, mockClient);
    }

    @Test
    public void testGetClientId() {
        assertEquals("TestClient", instance.getClientId());
    }

    @Test
    public void testGetNamesrvAddr() {
        assertEquals("localhost:9876", instance.getNamesrvAddr());
    }

    @Test
    public void testGetRemotingClient() {
        assertSame(mockClient, instance.getRemotingClient());
    }

    @Test
    public void testGetOrCreateBrokerChannelNullResultNoNpe() throws Exception {
        // 模拟: broker 已关闭, getOrCreateChannel 返回 null (重试都失败)
        when(mockClient.getOrCreateChannel("brokerA:10911")).thenReturn(null);
        Channel ch = instance.getOrCreateBrokerChannel("brokerA:10911");
        assertNull(ch, "broker 不可用时, 应返回 null 而非 NPE");
    }

    @Test
    public void testGetOrCreateBrokerChannelThrowsIsCaught() throws Exception {
        // 模拟: remotingClient 抛 InterruptedException
        when(mockClient.getOrCreateChannel("brokerB:10911"))
                .thenThrow(new InterruptedException("interrupted"));
        // 不抛, 转 null
        Channel ch = instance.getOrCreateBrokerChannel("brokerB:10911");
        assertNull(ch);
    }

    @Test
    public void testGetOrCreateBrokerChannelCachesResult() throws Exception {
        Channel mockChannel = mock(Channel.class);
        when(mockChannel.isActive()).thenReturn(true);
        when(mockClient.getOrCreateChannel("brokerC:10911")).thenReturn(mockChannel);

        // 第一次调用应走 remotingClient
        Channel first = instance.getOrCreateBrokerChannel("brokerC:10911");
        assertSame(mockChannel, first);
        // 第二次应走缓存, 不再调用 remotingClient
        Channel second = instance.getOrCreateBrokerChannel("brokerC:10911");
        assertSame(mockChannel, second);

        // 验证缓存表里只有 1 个 entry
        Field t = MQClientInstance.class.getDeclaredField("brokerChannelTable");
        t.setAccessible(true);
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<String, Channel> map =
                (ConcurrentHashMap<String, Channel>) t.get(instance);
        assertEquals(1, map.size());
    }

    @Test
    public void testGetOrCreateBrokerChannelEvictsInactiveAndReconnects() throws Exception {
        Channel oldChannel = mock(Channel.class);
        Channel newChannel = mock(Channel.class);
        when(oldChannel.isActive()).thenReturn(false);
        when(newChannel.isActive()).thenReturn(true);
        when(mockClient.getOrCreateChannel("brokerD:10911"))
                .thenReturn(oldChannel)   // 第一次返回旧 channel
                .thenReturn(newChannel);  // 第二次返回新 channel

        Channel c1 = instance.getOrCreateBrokerChannel("brokerD:10911");
        assertSame(oldChannel, c1);
        // 缓存里存了 oldChannel
        Channel c2 = instance.getOrCreateBrokerChannel("brokerD:10911");
        // oldChannel.isActive() = false, 应重新连接 → newChannel
        assertSame(newChannel, c2);
    }

    @Test
    public void testCloseBrokerChannel() throws Exception {
        Channel mockChannel = mock(Channel.class);
        when(mockClient.getOrCreateChannel("brokerE:10911")).thenReturn(mockChannel);
        when(mockChannel.isActive()).thenReturn(true);
        instance.getOrCreateBrokerChannel("brokerE:10911");

        instance.closeBrokerChannel("brokerE:10911");
        // 缓存表里不应再有此 key
        Channel cached = instance.getBrokerChannel("brokerE:10911");
        assertNull(cached, "close 后缓存应清空");
    }

    @Test
    public void testGetBrokerChannelUnknownReturnsNull() {
        assertNull(instance.getBrokerChannel("UNKNOWN:10911"));
    }

    @Test
    public void testClearRouteCache() {
        // 不启动, 直接清空应 noop
        instance.clearRouteCache();
        // verify no exception
    }

    @Test
    public void testUpdateTopicRouteDataNullRemoves() {
        // 未填入前缓存为空
        instance.updateTopicRouteData("T1", null);
        // verify still empty
        assertTrue(instance.getTopicRouteTable().isEmpty());
    }

    @Test
    public void testUpdateTopicRouteDataStores() {
        com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData r = new com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData();
        r.setOrder(true);
        instance.updateTopicRouteData("T2", r);
        assertEquals(1, instance.getTopicRouteTable().size());
        assertSame(r, instance.getTopicRouteTable().get("T2"));
    }

    @Test
    public void testGetTopicRouteDataNullNamesrvReturnsNull() {
        // namesrvAddr 非空但未启动 client — 不抛
        MQClientInstance inst = new MQClientInstance("C2", "", new NettyClientConfig());
        assertNull(inst.getTopicRouteData("Unknown"));
    }

    @Test
    public void testGetAllWritableQueuesEmptyRouteReturnsEmpty() {
        assertEquals(0, instance.getAllWritableQueues("T", null).size());
    }

    @Test
    public void testSelectOneMessageQueueEmptyRouteReturnsNull() {
        assertNull(instance.selectOneMessageQueue("T", null));
    }

    @Test
    public void testShutdownNoOpWhenNeverStarted() {
        // 没 start() 的 instance shutdown 不应抛
        instance.shutdown();
    }

    @Test
    public void testGetTopicRouteDataUsesCacheIfPresent() {
        // 首次 put 到 cache, 第二次 getTopicRouteData 应直接命中
        com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData cached = new com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData();
        cached.setOrder(false);
        instance.updateTopicRouteData("CachedTopic", cached);
        com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData got = instance.getTopicRouteData("CachedTopic");
        assertSame(cached, got);
    }

    @Test
    public void testGetTopicRouteDataFetchesWhenMissAndNoAddr() {
        // namesrvAddr 为空时, getTopicRouteData 返回 null 而不是抛
        MQClientInstance noAddr = new MQClientInstance("C3", null, new NettyClientConfig());
        assertNull(noAddr.getTopicRouteData("AnyTopic"));
    }

    @Test
    public void testInvokeSyncChannelNullThrowsConnectException() throws Exception {
        // 拿不到 channel 时, 应抛 RemotingConnectException
        when(mockClient.getOrCreateChannel("nowhere:10911")).thenReturn(null);
        assertThrows(RemotingConnectException.class, () -> {
            instance.invokeSync("nowhere:10911", null, 100L);
        });
    }

    @Test
    public void testInvokeSyncTranslatesException() throws Exception {
        Channel mockChannel = mock(Channel.class);
        when(mockClient.getOrCreateChannel("brokerF:10911")).thenReturn(mockChannel);
        when(mockClient.invokeSync(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyLong()))
                .thenThrow(RemotingTimeoutException.newTimeoutException("brokerF", 1000L));

        assertThrows(RemotingTimeoutException.class, () -> {
            instance.invokeSync("brokerF:10911", null, 100L);
        });
    }

    @Test
    public void testInvokeSyncGenericExceptionWrapped() throws Exception {
        Channel mockChannel = mock(Channel.class);
        when(mockClient.getOrCreateChannel("brokerG:10911")).thenReturn(mockChannel);
        when(mockClient.invokeSync(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyLong()))
                .thenThrow(new RuntimeException("boom"));

        assertThrows(com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingSendRequestException.class, () -> {
            instance.invokeSync("brokerG:10911", null, 100L);
        });
    }

    @Test
    public void testSelectOneMessageQueueDistributesAcrossQueues() {
        com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData route = new com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData();
        com.taotao.cloud.mq.consistency.ttc.common.BrokerData bd = new com.taotao.cloud.mq.consistency.ttc.common.BrokerData();
        bd.setBrokerName("brokerA");
        bd.putBrokerAddr(0L, "brokerA:10911");
        java.util.List<com.taotao.cloud.mq.consistency.ttc.common.BrokerData> bdList = new java.util.ArrayList<>();
        bdList.add(bd);
        route.setBrokerDatas(bdList);

        com.taotao.cloud.mq.consistency.ttc.common.QueueData qd = new com.taotao.cloud.mq.consistency.ttc.common.QueueData();
        qd.setBrokerName("brokerA");
        qd.setWriteQueueNums(4);
        java.util.List<com.taotao.cloud.mq.consistency.ttc.common.QueueData> qdList = new java.util.ArrayList<>();
        qdList.add(qd);
        route.setQueueDatas(qdList);

        // 多次调用, queueId 应在 [0,4) 范围内
        for (int i = 0; i < 20; i++) {
            com.taotao.cloud.mq.consistency.ttc.common.MessageQueue mq = instance.selectOneMessageQueue("T", route);
            assertNotNull(mq);
            assertTrue(mq.getQueueId() >= 0 && mq.getQueueId() < 4);
            assertEquals("brokerA", mq.getBrokerName());
        }
    }

    @Test
    public void testGetAllWritableQueuesAllBrokers() {
        com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData route = new com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData();
        com.taotao.cloud.mq.consistency.ttc.common.BrokerData bd = new com.taotao.cloud.mq.consistency.ttc.common.BrokerData();
        bd.setBrokerName("brokerA");
        bd.putBrokerAddr(0L, "brokerA:10911");
        route.setBrokerDatas(java.util.Collections.singletonList(bd));
        com.taotao.cloud.mq.consistency.ttc.common.QueueData qd = new com.taotao.cloud.mq.consistency.ttc.common.QueueData();
        qd.setBrokerName("brokerA");
        qd.setWriteQueueNums(3);
        route.setQueueDatas(java.util.Collections.singletonList(qd));
        assertEquals(3, instance.getAllWritableQueues("T", route).size());
    }

    @Test
    public void testGetAllWritableQueuesSkipsBrokerWithoutMasterAddr() {
        com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData route = new com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData();
        com.taotao.cloud.mq.consistency.ttc.common.BrokerData bd = new com.taotao.cloud.mq.consistency.ttc.common.BrokerData();
        bd.setBrokerName("noMaster");
        // 不放任何 brokerAddr, selectBrokerAddr() = null
        route.setBrokerDatas(java.util.Collections.singletonList(bd));
        com.taotao.cloud.mq.consistency.ttc.common.QueueData qd = new com.taotao.cloud.mq.consistency.ttc.common.QueueData();
        qd.setBrokerName("noMaster");
        qd.setWriteQueueNums(3);
        route.setQueueDatas(java.util.Collections.singletonList(qd));
        assertEquals(0, instance.getAllWritableQueues("T", route).size());
    }
}
