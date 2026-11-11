package com.taotao.cloud.rpc.remoting;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC 客户端处理器（remoting 包 — 用 remoting.RpcResponse）.
 *
 * <p>注意：还有顶层包 {@code com.taotao.cloud.rpc.RpcClientHandler}，那是简化版本，
 * 用于旧 RpcClient.sendRequest 短连接调用场景。本类是 DiscoveryRpcClient
 * 长连接模式使用的版本.
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private final Logger log = LogManager.getLogger(this.getClass());

    /** 每个 handler 实例独立维护自己的 requestId→Future 映射，避免与顶层包类的类型冲突 */
    private final ConcurrentHashMap<String, CompletableFuture<RpcResponse>> futures = new ConcurrentHashMap<>();

    /** 注册 requestId → Future */
    public void setFuture(String requestId, CompletableFuture<RpcResponse> future) {
        this.futures.put(requestId, future);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) {
        CompletableFuture<RpcResponse> future = futures.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
            log.debug("Received response for request: {}", response.getRequestId());
        } else {
            log.warn("Received unknown response: {}", response.getRequestId());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("RPC client exception: {}", cause.getMessage(), cause);
        for (CompletableFuture<RpcResponse> future : futures.values()) {
            future.completeExceptionally(cause);
        }
        futures.clear();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.warn("Channel inactive, connection closed");
        for (CompletableFuture<RpcResponse> future : futures.values()) {
            future.completeExceptionally(new RuntimeException("Connection closed"));
        }
        futures.clear();
    }
}
