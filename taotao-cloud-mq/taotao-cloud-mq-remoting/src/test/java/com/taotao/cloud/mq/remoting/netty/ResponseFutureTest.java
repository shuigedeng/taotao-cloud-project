package com.taotao.cloud.mq.remoting.netty;

import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ResponseFuture 单元测试 — 覆盖响应投递 / 等待 / 释放 / 回调 行为。
 */
public class ResponseFutureTest {

    @Test
    public void testWaitResponseTimesOut() throws Exception {
        ResponseFuture f = new ResponseFuture(null, 1, 100L, null, null);
        RemotingCommand resp = f.waitResponse(50L);
        // timeout 后 latch 仍未 countDown, response 为 null
        assertNull(resp);
    }

    @Test
    public void testPutResponseReleasesWaiter() throws Exception {
        ResponseFuture f = new ResponseFuture(null, 2, 1000L, null, null);
        RemotingCommand payload = RemotingCommand.createResponseCommand(0, "ok");
        new Thread(() -> {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            f.putResponse(payload);
        }).start();
        RemotingCommand got = f.waitResponse(2000L);
        assertNotNull(got);
        assertEquals(0, got.getCode());
    }

    @Test
    public void testExecuteInvokeCallbackFires() throws Exception {
        AtomicReference<ResponseFuture> captured = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        NettyRemotingAbstract.InvokeCallback cb = new NettyRemotingAbstract.InvokeCallback() {
            @Override
            public void operationComplete(ResponseFuture responseFuture) {
                captured.set(responseFuture);
                latch.countDown();
            }
        };
        ResponseFuture f = new ResponseFuture(null, 3, 1000L, cb, null);
        f.putResponse(RemotingCommand.createResponseCommand(0));
        f.executeInvokeCallback();
        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertNotNull(captured.get());
        assertEquals(3, captured.get().getOpaque());
    }

    @Test
    public void testExecuteInvokeCallbackIdempotent() {
        AtomicInteger counter = new AtomicInteger(0);
        NettyRemotingAbstract.InvokeCallback cb = new NettyRemotingAbstract.InvokeCallback() {
            @Override
            public void operationComplete(ResponseFuture responseFuture) {
                counter.incrementAndGet();
            }
        };
        ResponseFuture f = new ResponseFuture(null, 4, 1000L, cb, null);
        f.putResponse(RemotingCommand.createResponseCommand(0));
        f.executeInvokeCallback();
        f.executeInvokeCallback();
        f.executeInvokeCallback();
        assertEquals(1, counter.get());
    }

    @Test
    public void testExecuteInvokeCallbackSwallowsException() {
        NettyRemotingAbstract.InvokeCallback cb = new NettyRemotingAbstract.InvokeCallback() {
            @Override
            public void operationComplete(ResponseFuture responseFuture) {
                throw new RuntimeException("intentional");
            }
        };
        ResponseFuture f = new ResponseFuture(null, 5, 1000L, cb, null);
        f.putResponse(RemotingCommand.createResponseCommand(0));
        // 不应抛
        f.executeInvokeCallback();
    }

    @Test
    public void testReleaseWithSemaphore() {
        java.util.concurrent.Semaphore sem = new java.util.concurrent.Semaphore(0);
        ResponseFuture f = new ResponseFuture(null, 6, 1000L, null, sem);
        f.release();
        assertEquals(1, sem.availablePermits());
    }

    @Test
    public void testReleaseIdempotent() {
        java.util.concurrent.Semaphore sem = new java.util.concurrent.Semaphore(0);
        ResponseFuture f = new ResponseFuture(null, 7, 1000L, null, sem);
        f.release();
        f.release();
        f.release();
        // release 用 CAS, 只生效一次
        assertEquals(1, sem.availablePermits());
    }

    @Test
    public void testReleaseNullSemaphoreSafe() {
        ResponseFuture f = new ResponseFuture(null, 8, 1000L, null, null);
        // null semaphore 不抛
        f.release();
    }

    @Test
    public void testGetOpaqueAndTimeout() {
        ResponseFuture f = new ResponseFuture(null, 42, 3000L, null, null);
        assertEquals(42, f.getOpaque());
        assertEquals(3000L, f.getTimeoutMillis());
    }

    @Test
    public void testSendRequestOKDefault() {
        ResponseFuture f = new ResponseFuture(null, 1, 1000L, null, null);
        assertTrue(f.isSendRequestOK());
        f.setSendRequestOK(false);
        assertFalse(f.isSendRequestOK());
    }

    @Test
    public void testCauseAccessors() {
        ResponseFuture f = new ResponseFuture(null, 1, 1000L, null, null);
        assertNull(f.getCause());
        RuntimeException ex = new RuntimeException("boom");
        f.setCause(ex);
        assertEquals(ex, f.getCause());
    }

    @Test
    public void testToStringContainsKeyFields() {
        ResponseFuture f = new ResponseFuture(null, 123, 1000L, null, null);
        String s = f.toString();
        assertTrue(s.contains("123"));
        assertTrue(s.contains("sendRequestOK"));
    }
}
