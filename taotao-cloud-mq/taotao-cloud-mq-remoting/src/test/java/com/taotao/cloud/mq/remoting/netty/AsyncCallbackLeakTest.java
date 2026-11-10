package com.taotao.cloud.mq.remoting.netty;

import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 异步回调与流控许可的闭环测试（W3 §2 / §3 的验收尺）。
 * <p>
 * 覆盖四条归还路径里的三条真路径 + 一条超时路径：
 * <ol>
 *   <li><b>回调真的被投递</b>：每次 invokeAsync 的 InvokeCallback 恰好执行一次，
 *       且跑在 callbackExecutor（{@code NettyClientPublicExecutor_*}）上，
 *       不在调用线程、也不在 netty worker 线程上。</li>
 *   <li><b>许可不泄漏</b>：跑满 semaphoreAsync 全部许可后再跑第二批，
 *       每一批末尾都必须能把全部许可<b>取回来</b>（见 {@link #awaitAllPermitsBack}）。
 *       旧实现每成功一次漏一个 ⇒ 本用例天然会红。</li>
 *   <li><b>超时路径</b>：scanResponseTable 判定过期后同样要执行回调并归还许可。</li>
 *   <li><b>两个标志位分职</b>：回调 CAS 与许可 CAS 各自独立，多条路径竞速时最多跑一次、最多还一次。</li>
 * </ol>
 * 许可数刻意配成 {@link #ASYNC_PERMITS} 这么小：默认 {@code clientAsyncSemaphoreValue = 65535}，
 * 拿它当分母要么慢要么假绿。全部等待都是事件驱动的：回调用 latch，
 * 许可归还用 {@link Semaphore#tryAcquire(int, long, TimeUnit)} 做<b>因果等待</b>
 * （许可是在用户回调返回之后才由 {@code ResponseFuture.executeInvokeCallback()} 的
 * {@code finally { release(); }} 归还的 ⇒ latch 一开并不代表许可已经回来，
 * 所以"等回调落地再读一次 availablePermits()"是"等 A 却断言 B"的顺序 bug，
 * 在 JDK 8 上会稳定假红；而 tryAcquire 全部许可只有在真归还时才成功，
 * 不需要 sleep、也不设挂钟阈值），最后原样还回去，不改变被测对象的记账。
 */
public class AsyncCallbackLeakTest {

    /** 配小的异步许可数（默认 65535，不可测）. */
    private static final int ASYNC_PERMITS = 6;

    /** 第一批刚好借满许可，第二批只有许可真被归还才可能成功. */
    private static final int FIRST_BATCH = ASYNC_PERMITS;
    private static final int SECOND_BATCH = 3;

    private static final long RPC_TIMEOUT_MILLIS = 5000L;

    private NettyRemotingServer server;
    private NettyRemotingClient client;
    private String serverAddr;
    private EchoProcessor processor;

    @BeforeEach
    public void setUp() {
        NettyServerConfig serverConfig = new NettyServerConfig();
        // listenPort = 0 ⇒ 内核分配端口，随后 localListenPort() 回读。
        // 不在测试里写死 32768-60999 之间的端口（Linux 上会被临时端口段抢走 ⇒ bind Address already in use）
        serverConfig.setListenPort(0);
        server = new NettyRemotingServer(serverConfig);
        processor = new EchoProcessor();
        server.registerProcessor(RequestCode.SEND_MESSAGE, processor, null);
        server.start();
        serverAddr = "127.0.0.1:" + server.localListenPort();

        NettyClientConfig clientConfig = new NettyClientConfig();
        clientConfig.setClientAsyncSemaphoreValue(ASYNC_PERMITS);
        client = new NettyRemotingClient(clientConfig);
        client.start();
    }

    @AfterEach
    public void tearDown() {
        if (client != null) {
            client.shutdown();
        }
        if (server != null) {
            server.shutdown();
        }
    }

    /** echo 处理器：每条请求回一个带头字段与非空 body 的响应. */
    private static class EchoProcessor implements NettyRemotingAbstract.NettyRequestProcessor {
        private final AtomicInteger handled = new AtomicInteger(0);

        @Override
        public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
            handled.incrementAndGet();
            RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SUCCESS);
            response.addExtField("echoOpaque", String.valueOf(request.getOpaque()));
            response.setBody(("echo-" + request.getOpaque()).getBytes(StandardCharsets.UTF_8));
            return response;
        }

        @Override
        public boolean rejectRequest() {
            return false;
        }
    }

    /**
     * 因果地等"许可全部归还"这件事真的发生，而不是事后核对一个数字。
     * <p>
     * 归还发生在用户回调返回之后（{@code ResponseFuture.executeInvokeCallback()} 的
     * {@code finally { release(); }}），所以任何"等回调落地的 latch 一开就断言
     * availablePermits()"的写法都只是在读一个还没定的数（JDK 8 上实测假红：
     * {@code expected: <6> but was: <5>}）。这里改成真的把那 {@code expected} 个许可取回来：
     * <ul>
     *   <li>取回来了 ⇒ 每一条借用路径都确实归还过（总数只有 expected，取满即证明没有 outstanding），
     *       随即 {@code release(expected)} 原样还回去，被测信号量的记账不变；</li>
     *   <li>取不回来 ⇒ 就是泄漏，失败消息点名 "leaked"，并带上取满超时那一刻的读数。</li>
     * </ul>
     * 还回去之后再核一次总数，多还（double release）同样会当场红。
     */
    private static void awaitAllPermitsBack(Semaphore sem, int expected, String who)
            throws InterruptedException {
        boolean allBack = sem.tryAcquire(expected, RPC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(allBack, who + " leaked: " + expected + " async permits were the whole quota, "
                + "and they never all came back within the RPC budget (availablePermits="
                + sem.availablePermits() + " when the wait gave up). The callback fired but the "
                + "permit it borrows was not returned.");
        sem.release(expected);
        assertEquals(expected, sem.availablePermits(), who
                + " permit accounting wrong after all permits were re-acquired and handed back"
                + " (too many back means a double release)");
    }

    /**
     * 主尺：第一批跑满许可，第二批只有许可真被归还过才可能成功。
     * <p>
     * 许可是异步归还的（响应到达 → 回调 → release），所以"发完一条就断言许可回到初值"
     * 本身是竞态而不是泄漏尺。正确的尺是分两批，每批的回调全部落地后再<b>因果地</b>把许可取回来
     * （见 {@link #awaitAllPermitsBack}）：
     * 旧实现这里必然红 —— 第一批就把 6 个许可漏光，第二批的 tryAcquire 等满超时后抛
     * {@code RemotingTooMuchRequestException}；即使侥幸不抛，批末的许可核对也会报 0。
     */
    @Test
    public void asyncCallbacksFireAndSemaphorePermitsDoNotLeak() throws Exception {
        Channel channel = client.getOrCreateChannel(serverAddr);
        assertNotNull(channel, "should connect to in-process server");

        final AtomicInteger callbackCount = new AtomicInteger(0);
        final AtomicInteger responseCount = new AtomicInteger(0);
        final List<String> callbackThreads = new CopyOnWriteArrayList<String>();
        NettyRemotingAbstract.InvokeCallback counter = new NettyRemotingAbstract.InvokeCallback() {
            @Override
            public void operationComplete(ResponseFuture responseFuture) {
                callbackCount.incrementAndGet();
                callbackThreads.add(Thread.currentThread().getName());
                if (responseFuture.getResponseCommand() != null) {
                    responseCount.incrementAndGet();
                }
            }
        };

        // ---- 第一批：刚好借满 ASYNC_PERMITS 个许可 ----
        CountDownLatch firstBatch = fireBatch(channel, FIRST_BATCH, counter);
        assertTrue(firstBatch.await(RPC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
                "first batch (a full permit quota) must all call back; got "
                        + callbackCount.get() + "/" + FIRST_BATCH);
        assertEquals(FIRST_BATCH, callbackCount.get(), "each callback must run exactly once");
        assertEquals(FIRST_BATCH, responseCount.get(), "each callback must see its response");
        awaitAllPermitsBack(client.semaphoreAsync, ASYNC_PERMITS,
                "semaphoreAsync after first batch (a full permit quota)");
        assertEquals(0, client.responseTable.size(), "responseTable must drain");

        // ---- 第二批：只有许可真被归还过才可能成功 ----
        int total = FIRST_BATCH + SECOND_BATCH;
        CountDownLatch secondBatch = fireBatch(channel, SECOND_BATCH, counter);
        assertTrue(secondBatch.await(RPC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
                "second batch must also call back (permits must be reusable); got "
                        + callbackCount.get() + "/" + total);

        assertEquals(total, callbackCount.get(), "each callback must run exactly once");
        assertEquals(total, responseCount.get(), "each callback must see its response");
        assertEquals(total, processor.handled.get(), "server processor must be invoked once per call");

        awaitAllPermitsBack(client.semaphoreAsync, ASYNC_PERMITS,
                "semaphoreAsync after two batches (permits must be reusable, none leaked)");
        assertEquals(0, client.responseTable.size(), "responseTable must drain");

        assertEquals(total, callbackThreads.size());
        for (String threadName : callbackThreads) {
            assertTrue(threadName.startsWith("NettyClientPublicExecutor_"),
                    "callback must run on the callbackExecutor, but ran on " + threadName);
        }
    }

    /** 连发 n 条异步请求，返回等待这批回调全部落地的 latch. */
    private CountDownLatch fireBatch(Channel channel, int n,
                                     NettyRemotingAbstract.InvokeCallback delegate) throws Exception {
        CountDownLatch latch = new CountDownLatch(n);
        for (int i = 0; i < n; i++) {
            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
            request.addExtField("seq", String.valueOf(i));
            request.setBody(("body-" + i).getBytes(StandardCharsets.UTF_8));
            client.invokeAsync(channel, request, RPC_TIMEOUT_MILLIS, new CountingCallback(delegate, latch));
        }
        return latch;
    }

    /** 把用户回调和批次 latch 串起来，保证 latch 一定减到 0. */
    private static class CountingCallback implements NettyRemotingAbstract.InvokeCallback {
        private final NettyRemotingAbstract.InvokeCallback delegate;
        private final CountDownLatch latch;

        CountingCallback(NettyRemotingAbstract.InvokeCallback delegate, CountDownLatch latch) {
            this.delegate = delegate;
            this.latch = latch;
        }

        @Override
        public void operationComplete(ResponseFuture responseFuture) {
            try {
                delegate.operationComplete(responseFuture);
            } finally {
                latch.countDown();
            }
        }
    }

    /**
     * 超时路径：scanResponseTable 判定过期后，回调要跑、许可要还、表项要清。
     * <p>
     * 不靠"真的等满超时"（那是挂钟阈值）：构造一个 timeoutMillis 为负的 future，
     * 让扫描判定式 {@code beginTimestamp + timeoutMillis + 1000 <= now} 当场成立。
     */
    @Test
    public void scanResponseTableFiresCallbackAndReturnsPermit() throws Exception {
        final AtomicInteger fired = new AtomicInteger(0);
        final CountDownLatch callbackDone = new CountDownLatch(1);
        final Semaphore sem = new Semaphore(ASYNC_PERMITS, true);

        // 模拟 invokeAsyncImpl 已经借走一个许可
        sem.acquire();
        assertEquals(ASYNC_PERMITS - 1, sem.availablePermits());

        ResponseFuture future = new ResponseFuture(null, Integer.MAX_VALUE - 7, -10_000L,
                new NettyRemotingAbstract.InvokeCallback() {
                    @Override
                    public void operationComplete(ResponseFuture responseFuture) {
                        fired.incrementAndGet();
                        callbackDone.countDown();
                    }
                }, sem);
        client.responseTable.put(future.getOpaque(), future);

        client.scanResponseTable();

        assertTrue(callbackDone.await(RPC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS),
                "timeout scan must deliver the callback");
        assertEquals(1, fired.get(), "callback must run exactly once on the timeout path");
        // 工单只点了三处，这是第四处"核对许可数字"的断言，一并换成因果式。
        // 说明（实测所得，别当成同类竞态）：本例由测试线程自己调 scanResponseTable()，
        // 而 NettyRemotingAbstract.scanResponseTable 的 finally { rep.release(); } 就在调用线程上跑，
        // 所以这条路径的归还不经过"等回调的 latch"，本来就不会假红；
        // 换成语义等价的因果式是为了和其它三处同尺，并且一旦扫描改由定时线程驱动也不会退化成一个还没定的数。
        awaitAllPermitsBack(sem, ASYNC_PERMITS,
                "the borrowed permit on the timeout path (scanResponseTable)");
        assertNull(client.responseTable.get(future.getOpaque()), "expired entry must be removed");

        // 再扫一次不许双还（到这里许可已确认全部回位，两次调用都在测试线程上同步发生）
        future.release();
        future.executeInvokeCallback();
        assertEquals(ASYNC_PERMITS, sem.availablePermits(), "release must stay idempotent");
        assertEquals(1, fired.get(), "callback must stay single-shot");
    }

    /**
     * 两个标志位分职：回调 CAS 与许可 CAS 独立，竞速时最多跑一次、最多还一次。
     */
    @Test
    public void racingReleasePathsRunOnceAndReleaseOnce() throws Exception {
        final Semaphore sem = new Semaphore(ASYNC_PERMITS, true);
        sem.acquire();

        final AtomicInteger fired = new AtomicInteger(0);
        final ResponseFuture future = new ResponseFuture(null, 1000, 1000L,
                new NettyRemotingAbstract.InvokeCallback() {
                    @Override
                    public void operationComplete(ResponseFuture responseFuture) {
                        fired.incrementAndGet();
                    }
                }, sem);

        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(4);
        Runnable[] racers = new Runnable[]{
                new Runnable() {
                    @Override
                    public void run() {
                        future.executeInvokeCallback();
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        future.release();
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        future.executeInvokeCallback();
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        future.release();
                    }
                }
        };
        for (final Runnable racer : racers) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        start.await(RPC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    racer.run();
                    done.countDown();
                }
            });
            t.start();
        }
        start.countDown();
        assertTrue(done.await(RPC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS), "racers must finish");

        assertEquals(1, fired.get(), "callback runs once even under racing paths");
        assertEquals(ASYNC_PERMITS, sem.availablePermits(),
                "one borrowed permit must come back exactly once, never twice");
        assertTrue(future.isCallbackExecuted());
        assertTrue(future.isSemaphoreReleased());
    }

    /**
     * 回调看到的就是本条请求的响应：opaque 与 body 必须对得上，不许串号。
     * <p>
     * 判定一律放在测试线程上：main 侧 {@code ResponseFuture.executeInvokeCallback()} 外面有
     * {@code catch (Throwable)}（{@code NettyRemotingAbstract} 侧还有一层 {@code log.warn}），
     * 回调线程里抛出的 AssertionError 会被整个吞掉 —— 老写法在回调里 assertEquals，
     * 一旦串号，失败只会变成 5 s 后一条误导性的 "callback must fire"。
     * 现在回调只记录观察值（opaque / response / 跑了几次）再 countDown，断言全部在测试线程做。
     */
    @Test
    public void callbackSeesItsOwnResponse() throws Exception {
        Channel channel = client.getOrCreateChannel(serverAddr);
        final RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        request.setBody("ping-body".getBytes(StandardCharsets.UTF_8));

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicInteger callbackRuns = new AtomicInteger(0);
        final AtomicReference<ResponseFuture> seenFuture = new AtomicReference<ResponseFuture>();
        final AtomicReference<RemotingCommand> seenResponse = new AtomicReference<RemotingCommand>();
        client.invokeAsync(channel, request, RPC_TIMEOUT_MILLIS,
                new NettyRemotingAbstract.InvokeCallback() {
                    @Override
                    public void operationComplete(ResponseFuture responseFuture) {
                        seenFuture.set(responseFuture);
                        seenResponse.set(responseFuture.getResponseCommand());
                        callbackRuns.incrementAndGet();
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(RPC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS), "callback must fire");
        assertEquals(1, callbackRuns.get(), "callback must run exactly once");
        ResponseFuture future = seenFuture.get();
        assertNotNull(future, "callback must be handed a response future");
        assertEquals(request.getOpaque(), future.getOpaque(),
                "callback must see its own request: the future handed over belongs to a different"
                        + " opaque (串号 / response table mix-up)");
        RemotingCommand response = seenResponse.get();
        assertNotNull(response, "response must be attached");
        assertEquals(RemotingSysResponseCode.SUCCESS, response.getCode());
        assertEquals(String.valueOf(request.getOpaque()), response.getExtField("echoOpaque"));
        assertEquals("echo-" + request.getOpaque(), new String(response.getBody(), StandardCharsets.UTF_8));
        awaitAllPermitsBack(client.semaphoreAsync, ASYNC_PERMITS,
                "semaphoreAsync after a single successful async call");
    }
}
