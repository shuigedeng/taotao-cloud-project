package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

import com.taotao.cloud.mq.consistency.ttc.remoting.common.Pair;
import com.taotao.cloud.mq.consistency.ttc.remoting.common.RemotingHelper;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingTimeoutException;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingTooMuchRequestException;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * Netty 远程通信抽象基类
 * 提供请求-响应模式的基础设施
 */
public abstract class NettyRemotingAbstract {

    protected static final Logger log = LoggerFactory.getLogger(NettyRemotingAbstract.class);
    // 异步发送默认超时时间（毫秒）
    protected static final long DEFAULT_ASYNC_TIMEOUT_MILLIS = 3000;
    // 信号量用于流控（异步发送）
    protected final Semaphore semaphoreAsync;
    // 信号量用于流控（单向发送）
    protected final Semaphore semaphoreOneway;
    // 请求响应映射表（key: opaque, value: ResponseFuture）
    protected final ConcurrentHashMap<Integer, ResponseFuture> responseTable =
            new ConcurrentHashMap<>(256);

    // 处理器表（key: request code, value: Pair<processor, executor>）
    protected final HashMap<Integer, Pair<NettyRequestProcessor, ExecutorService>> processorTable =
            new HashMap<>(64);

    // 默认处理器
    protected Pair<NettyRequestProcessor, ExecutorService> defaultRequestProcessor;

    public NettyRemotingAbstract(final int permitsAsync, final int permitsOneway) {
        this.semaphoreAsync = new Semaphore(permitsAsync, true);
        this.semaphoreOneway = new Semaphore(permitsOneway, true);
    }

    /**
     * 处理接收到的消息
     */
    public void processMessageReceived(ChannelHandlerContext ctx, RemotingCommand msg) {
        if (msg != null) {
            switch (msg.getType()) {
                case REQUEST_COMMAND:
                    processRequestCommand(ctx, msg);
                    break;
                case RESPONSE_COMMAND:
                    processResponseCommand(ctx, msg);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 处理请求命令
     */
    protected void processRequestCommand(final ChannelHandlerContext ctx, final RemotingCommand cmd) {
        // 查找处理器
        final Pair<NettyRequestProcessor, ExecutorService> matched = this.processorTable.get(cmd.getCode());
        final Pair<NettyRequestProcessor, ExecutorService> pair = matched != null ? matched : this.defaultRequestProcessor;

        if (pair == null) {
            // 没有处理器，返回错误
            String error = " request type " + cmd.getCode() + " not supported";
            final RemotingCommand response = RemotingCommand.createResponseCommand(
                    RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED, error);
            response.setOpaque(cmd.getOpaque());
            ctx.writeAndFlush(response);
            log.warn(RemotingHelper.parseChannelRemoteAddr(ctx.channel()) + error);
            return;
        }

        // 构建Runnable任务
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    // 回调RPC钩子
                    doBeforeRpcHooks(RemotingHelper.parseChannelRemoteAddr(ctx.channel()), cmd);

                    // 处理请求
                    final RemotingCommand response = pair.getObject1().processRequest(ctx, cmd);

                    // 回调RPC钩子
                    doAfterRpcHooks(RemotingHelper.parseChannelRemoteAddr(ctx.channel()), cmd, response);

                    // 如果不是单向调用，返回响应
                    if (!cmd.isOnewayRPC()) {
                        if (response != null) {
                            response.setOpaque(cmd.getOpaque());
                            response.markResponseType();
                            try {
                                ctx.writeAndFlush(response);
                            } catch (Throwable e) {
                                log.error("process request over, but response failed", e);
                                log.error(cmd.toString());
                                log.error(response.toString());
                            }
                        } else {
                            // 处理器没有返回响应，返回空响应
                            RemotingCommand emptyResponse = RemotingCommand.createResponseCommand(
                                    RemotingSysResponseCode.SUCCESS, null);
                            emptyResponse.setOpaque(cmd.getOpaque());
                            ctx.writeAndFlush(emptyResponse);
                        }
                    }
                } catch (Throwable e) {
                    log.error("process request exception", e);
                    log.error(cmd.toString());

                    if (!cmd.isOnewayRPC()) {
                        final RemotingCommand response = RemotingCommand.createResponseCommand(
                                RemotingSysResponseCode.SYSTEM_ERROR,
                                RemotingHelper.exceptionSimpleDesc(e));
                        response.setOpaque(cmd.getOpaque());
                        ctx.writeAndFlush(response);
                    }
                }
            }
        };

        // 提交到线程池执行
        try {
            pair.getObject2().submit(run);
        } catch (Exception e) {
            // 线程池已满，返回系统繁忙
            if (!cmd.isOnewayRPC()) {
                final RemotingCommand response = RemotingCommand.createResponseCommand(
                        RemotingSysResponseCode.SYSTEM_BUSY,
                        "[OVERLOAD]system busy, start flow control for a while");
                response.setOpaque(cmd.getOpaque());
                ctx.writeAndFlush(response);
            }
        }
    }

    /**
     * 处理响应命令
     */
    protected void processResponseCommand(ChannelHandlerContext ctx, RemotingCommand cmd) {
        final int opaque = cmd.getOpaque();

        // 查找对应的ResponseFuture
        final ResponseFuture responseFuture = responseTable.get(opaque);
        if (responseFuture != null) {
            responseFuture.setResponseCommand(cmd);

            // 移除映射表
            responseTable.remove(opaque);

            // 如果有回调，执行回调
            if (responseFuture.getInvokeCallback() != null) {
                executeInvokeCallback(responseFuture);
            } else {
                // 唤醒等待的线程
                responseFuture.putResponse(cmd);
            }
        } else {
            log.warn("receive response, but not matched any request, {} {} {}",
                    RemotingHelper.parseChannelRemoteAddr(ctx.channel()),
                    cmd.toString(),
                    opaque);
        }
    }

    /**
     * 执行回调
     * <p>
     * 语义（对齐 RocketMQ）：回调默认投递到 callbackExecutor；只有当线程池不可用
     * （未配置 / 已关闭 / 拒绝任务）时才退化到当前线程执行。
     * 无论走哪条路、也无论回调自身是否抛出，都必须在结束时归还异步许可
     * （{@link ResponseFuture#release()} 是幂等的）。
     */
    private void executeInvokeCallback(final ResponseFuture responseFuture) {
        final ExecutorService callbackExecutor = getCallbackExecutor();
        boolean runInThisThread = callbackExecutor == null || callbackExecutor.isShutdown();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    responseFuture.executeInvokeCallback();
                } catch (Throwable e) {
                    log.warn("executeInvokeCallback Exception", e);
                } finally {
                    // 许可归还与"回调是否跑过"是两件事，各自一个 CAS；这里保证每条路径恰好还一次
                    responseFuture.release();
                }
            }
        };

        if (runInThisThread) {
            task.run();
            return;
        }
        try {
            callbackExecutor.execute(task);
        } catch (RejectedExecutionException e) {
            log.warn("callbackExecutor rejected callback task, fallback to this thread", e);
            task.run();
        }
    }

    /**
     * 同步调用
     */
    protected RemotingCommand invokeSyncImpl(final Channel channel, final RemotingCommand request,
                                             final long timeoutMillis) throws Exception {
        final int opaque = request.getOpaque();

        try {
            final ResponseFuture responseFuture = new ResponseFuture(
                    channel, opaque, timeoutMillis, null, null);
            this.responseTable.put(opaque, responseFuture);

            final String addr = RemotingHelper.parseChannelRemoteAddr(channel);
            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        responseFuture.setSendRequestOK(true);
                        return;
                    } else {
                        responseFuture.setSendRequestOK(false);
                    }

                    responseTable.remove(opaque);
                    responseFuture.setCause(f.cause());
                    responseFuture.putResponse(null);
                    log.warn("send a request command to channel <" + addr + "> failed.");
                }
            });

            RemotingCommand responseCommand = responseFuture.waitResponse(timeoutMillis);
            if (responseCommand == null) {
                if (responseFuture.isSendRequestOK()) {
                    throw new RemotingTimeoutException(
                            RemotingTimeoutException.newTimeoutException(addr, timeoutMillis));
                } else {
                    throw new RemotingSendRequestException(
                            RemotingSendRequestException.newSendRequestException(addr, responseFuture.getCause()));
                }
            }

            return responseCommand;
        } finally {
            this.responseTable.remove(opaque);
        }
    }

    /**
     * 异步调用实现
     * <p>
     * 许可 accounting：{@code semaphoreAsync} 的许可在这里借出，之后必须且只能由一条路径归还——
     * <ol>
     *   <li>成功：响应到达 → processResponseCommand → 回调 → {@link ResponseFuture#release()}</li>
     *   <li>写失败：channel listener → requestFail → 回调 + release()</li>
     *   <li>超时：scanResponseTable → 回调 + release()</li>
     *   <li>投递阶段就抛：本方法就地 release()（不直接 release 信号量，避免与上面三条双还）</li>
     * </ol>
     * {@code ResponseFuture.release()} 自带幂等 CAS，所以"就地还 + 别处也调"不会多还。
     */
    protected void invokeAsyncImpl(final Channel channel, final RemotingCommand request,
                                   final long timeoutMillis, final InvokeCallback invokeCallback) throws Exception {
        // 获取信号量
        boolean acquired = this.semaphoreAsync.tryAcquire(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
        if (!acquired) {
            throw new RemotingTooMuchRequestException("invokeAsyncImpl tryAcquire semaphore timeout, "
                    + timeoutMillis + "ms, waiting thread numbers: "
                    + this.semaphoreAsync.getQueueLength());
        }

        final int opaque = request.getOpaque();
        final String addr = RemotingHelper.parseChannelRemoteAddr(channel);

        final ResponseFuture responseFuture = new ResponseFuture(
                channel, opaque, timeoutMillis, invokeCallback, this.semaphoreAsync);
        try {
            ResponseFuture stale = this.responseTable.put(opaque, responseFuture);
            if (stale != null) {
                // 同 opaque 撞上（理论上不会发生）：老 future 再没人处理，就地还它的许可
                stale.release();
            }

            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        responseFuture.setSendRequestOK(true);
                        return;
                    }

                    requestFail(opaque);
                    log.warn("send a request command to channel <" + addr + "> failed.");
                }
            });
            // 从这一刻起，许可的归属交给 responseFuture（由回调/超时/写失败三条路之一归还）
        } catch (Exception e) {
            // 投递没成功：这个 future 不会再被任何路径看到，就地归还（幂等，不会双还）
            this.responseTable.remove(opaque, responseFuture);
            responseFuture.release();
            throw new RemotingSendRequestException(
                    RemotingSendRequestException.newSendRequestException(addr, e));
        }
    }

    /**
     * 请求失败处理
     */
    private void requestFail(final int opaque) {
        ResponseFuture responseFuture = responseTable.remove(opaque);
        if (responseFuture != null) {
            responseFuture.setSendRequestOK(false);
            responseFuture.putResponse(null);
            try {
                executeInvokeCallback(responseFuture);
            } catch (Throwable e) {
                log.warn("executeInvokeCallback Exception", e);
            } finally {
                responseFuture.release();
            }
        }
    }

    /**
     * 单向调用实现
     */
    protected void invokeOnewayImpl(final Channel channel, final RemotingCommand request,
                                    final long timeoutMillis) throws Exception {
        // 获取信号量
        boolean acquired = this.semaphoreOneway.tryAcquire(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
        if (!acquired) {
            throw new RemotingTooMuchRequestException("invokeOnewayImpl tryAcquire semaphore timeout, "
                    + timeoutMillis + "ms, waiting thread numbers: "
                    + this.semaphoreOneway.getQueueLength());
        }

        try {
            channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (!f.isSuccess()) {
                        log.warn("send a request command to channel <" + channel.remoteAddress() + "> failed.");
                    }
                }
            });
        } finally {
            this.semaphoreOneway.release();
        }
    }

    /**
     * 注册处理器
     */
    public void registerProcessor(int requestCode, NettyRequestProcessor processor, ExecutorService executor) {
        ExecutorService executorThis = executor;
        if (null == executorThis) {
            executorThis = this.getPublicExecutor();
        }
        Pair<NettyRequestProcessor, ExecutorService> pair = new Pair<>(processor, executorThis);
        this.processorTable.put(requestCode, pair);
    }

    /**
     * 注册默认处理器
     */
    public void registerDefaultProcessor(NettyRequestProcessor processor, ExecutorService executor) {
        this.defaultRequestProcessor = new Pair<>(processor, executor);
    }

    /**
     * 扫描并清理超时的ResponseFuture
     */
    public void scanResponseTable() {
        final long currentTime = System.currentTimeMillis();
        final Iterator<Map.Entry<Integer, ResponseFuture>> it = this.responseTable.entrySet().iterator();

        while (it.hasNext()) {
            final Map.Entry<Integer, ResponseFuture> entry = it.next();
            final ResponseFuture rep = entry.getValue();

            if ((rep.getBeginTimestamp() + rep.getTimeoutMillis() + 1000) <= currentTime) {
                // 超时，移除
                it.remove();
                rep.setCause(new RemotingTimeoutException(
                        "wait response on the channel <" + rep.getRemoteAddr() + "> timeout, "
                                + rep.getTimeoutMillis() + "(ms)"));
                rep.putResponse(null);

                log.warn("remove timeout request, {} {}", rep.getOpaque(), rep.getRemoteAddr());

                try {
                    executeInvokeCallback(rep);
                } catch (Throwable e) {
                    log.warn("scanResponseTable, executeInvokeCallback Exception", e);
                } finally {
                    rep.release();
                }
            }
        }
    }

    /**
     * RPC前置钩子（子类可覆盖）
     */
    protected void doBeforeRpcHooks(String remoteAddr, RemotingCommand request) {
        // 子类实现
    }

    /**
     * RPC后置钩子（子类可覆盖）
     */
    protected void doAfterRpcHooks(String remoteAddr, RemotingCommand request, RemotingCommand response) {
        // 子类实现
    }

    /**
     * 获取回调执行线程池
     */
    public abstract ExecutorService getCallbackExecutor();

    /**
     * 获取公共执行线程池
     */
    public abstract ExecutorService getPublicExecutor();

    // ==================== 回调接口 ====================

    /**
     * 调用回调接口
     */
    public interface InvokeCallback {
        /**
         * 操作完成回调
         */
        void operationComplete(final ResponseFuture responseFuture);
    }

    /**
     * 请求处理器接口
     */
    public interface NettyRequestProcessor {
        /**
         * 处理请求
         */
        RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request)
                throws Exception;

        /**
         * 是否拒绝请求
         */
        boolean rejectRequest();
    }
}
