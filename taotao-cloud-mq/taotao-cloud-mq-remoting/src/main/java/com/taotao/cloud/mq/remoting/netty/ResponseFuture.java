package com.taotao.cloud.mq.remoting.netty;

import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import io.netty.channel.Channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 响应Future
 * 用于同步等待响应或异步回调
 */
public class ResponseFuture {

    // 请求唯一标识
    private final int opaque;
    // 超时时间（毫秒）
    private final long timeoutMillis;
    // 等待锁
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    // "回调已经跑过"标志：只表达回调投递，不参与许可 accounting
    private final AtomicBoolean callbackExecuted = new AtomicBoolean(false);
    // "异步许可已经归还"标志：只表达流控资源归还，可独立于回调发生
    private final AtomicBoolean semaphoreReleased = new AtomicBoolean(false);
    // 回调函数
    private final NettyRemotingAbstract.InvokeCallback invokeCallback;
    // 信号量（用于流控）
    private final java.util.concurrent.Semaphore releaseSemaphore;
    // 远程地址
    private final String remoteAddr;
    // 通道
    private final Channel processChannel;
    // 开始时间戳
    private final long beginTimestamp = System.currentTimeMillis();
    // 响应命令
    private volatile RemotingCommand responseCommand;
    // 请求是否发送成功
    private volatile boolean sendRequestOK = true;
    // 异常原因
    private volatile Throwable cause;

    public ResponseFuture(Channel channel, int opaque, long timeoutMillis,
                          NettyRemotingAbstract.InvokeCallback invokeCallback,
                          java.util.concurrent.Semaphore releaseSemaphore) {
        this.opaque = opaque;
        this.timeoutMillis = timeoutMillis;
        this.invokeCallback = invokeCallback;
        this.releaseSemaphore = releaseSemaphore;
        this.remoteAddr = channel != null ? RemotingHelper.parseChannelRemoteAddr(channel) : "";
        this.processChannel = channel;
    }

    /**
     * 执行回调
     * <p>
     * 用 {@code callbackExecuted} 这个 CAS 保证回调最多跑一次；结束时顺带归还许可。
     * 注意许可归还是另一个独立标志位（见 {@link #release()}），两者不能互相顶替：
     * 老实现里回调路径与 release 抢同一个 {@code released}，第二次 CAS 必然失败，
     * 于是 semaphoreAsync 的许可每成功一次异步调用就泄漏一个。
     */
    public void executeInvokeCallback() {
        if (invokeCallback != null && callbackExecuted.compareAndSet(false, true)) {
            try {
                invokeCallback.operationComplete(this);
            } catch (Throwable e) {
                // 记录回调异常
            } finally {
                release();
            }
        }
    }

    /**
     * 释放信号量
     * <p>
     * 幂等：无论被调用多少次（回调路径、requestFail、超时扫描），许可最多归还一次。
     */
    public void release() {
        if (semaphoreReleased.compareAndSet(false, true) && releaseSemaphore != null) {
            releaseSemaphore.release();
        }
    }

    /**
     * 回调是否已执行（用于测试与诊断）。
     */
    public boolean isCallbackExecuted() {
        return callbackExecuted.get();
    }

    /**
     * 许可是否已归还（用于测试与诊断）。
     */
    public boolean isSemaphoreReleased() {
        return semaphoreReleased.get();
    }

    /**
     * 等待响应
     */
    public RemotingCommand waitResponse(final long timeoutMillis) throws InterruptedException {
        this.countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return this.responseCommand;
    }

    /**
     * 放入响应
     */
    public void putResponse(final RemotingCommand responseCommand) {
        this.responseCommand = responseCommand;
        this.countDownLatch.countDown();
    }

    // ==================== Getters and Setters ====================

    public RemotingCommand getResponseCommand() {
        return responseCommand;
    }

    public void setResponseCommand(RemotingCommand responseCommand) {
        this.responseCommand = responseCommand;
    }

    public boolean isSendRequestOK() {
        return sendRequestOK;
    }

    public void setSendRequestOK(boolean sendRequestOK) {
        this.sendRequestOK = sendRequestOK;
    }

    public int getOpaque() {
        return opaque;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public NettyRemotingAbstract.InvokeCallback getInvokeCallback() {
        return invokeCallback;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public Channel getProcessChannel() {
        return processChannel;
    }

    public long getBeginTimestamp() {
        return beginTimestamp;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "ResponseFuture{" +
                "opaque=" + opaque +
                ", timeoutMillis=" + timeoutMillis +
                ", sendRequestOK=" + sendRequestOK +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", beginTimestamp=" + beginTimestamp +
                '}';
    }
}
