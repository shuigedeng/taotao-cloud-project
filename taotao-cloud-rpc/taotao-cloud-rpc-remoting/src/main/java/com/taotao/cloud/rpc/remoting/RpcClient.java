package com.taotao.cloud.rpc.remoting;

import com.taotao.cloud.rpc.remoting.RpcMessageDecoder;
import com.taotao.cloud.rpc.remoting.RpcMessageEncoder;
import com.taotao.cloud.rpc.remoting.RpcRequest;
import com.taotao.cloud.rpc.remoting.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * RPC 客户端
 * 基于 Netty 实现
 */
public class RpcClient {

    private final Logger log = LogManager.getLogger(this.getClass());


    private final String host;
    private final int port;
    private final Map<String, CompletableFuture<RpcResponse>> pendingRequests = new ConcurrentHashMap<>();
    private Channel channel;
    private EventLoopGroup group;
    private volatile boolean connected = false;
    // 超时时间（毫秒）
    private long timeout = 3000;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
        connect();
    }

    /**
     * 连接服务器
     */
    private void connect() {
        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(new RpcClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();
            connected = true;
            log.info("Connected to RPC server: {}:{}", host, port);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            releaseGroupAfterFailedConnect();
            throw new RuntimeException("Failed to connect to server", e);
        } catch (Exception e) {
            releaseGroupAfterFailedConnect();
            throw new RuntimeException("Failed to connect to server: " + host + ":" + port, e);
        }
    }

    /**
     * 构造期连接失败意味着这个对象永远不会被使用，也就没人会调 {@link #close()}：
     * 不主动关掉，留下的就是一组永不退出的非守护 EventLoop 线程。
     */
    private void releaseGroupAfterFailedConnect() {
        group.shutdownGracefully();
        group = null;
    }

    /**
     * 发送请求
     */
    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        if (!connected || !channel.isActive()) {
            throw new IllegalStateException("Not connected to server");
        }

        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        pendingRequests.put(request.getRequestId(), future);

        try {
            channel.writeAndFlush(request).addListener((ChannelFutureListener) f -> {
                if (!f.isSuccess()) {
                    future.completeExceptionally(f.cause());
                    pendingRequests.remove(request.getRequestId());
                }
            });

            // 等待响应，带超时
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            pendingRequests.remove(request.getRequestId());
            throw e;
        }
    }

    /**
     * 高层调用接口：将 Invocation + URL 适配为一次 RPC 调用并返回 Result
     */
    public com.taotao.cloud.rpc.common.invoke.Result invoke(com.taotao.cloud.rpc.common.invoke.Invocation invocation, com.taotao.cloud.rpc.common.URL url) {
        try {
            RpcRequest request = new RpcRequest();
            request.setRequestId(java.util.UUID.randomUUID().toString());
            request.setInterfaceName(url.getServiceInterface() != null ? url.getServiceInterface() : invocation.getServiceInterface());
            request.setMethodName(invocation.getMethodName());
            request.setParameterTypes(invocation.getParameterTypes());
            request.setArguments(invocation.getArguments());
            if (invocation.getAttachments() != null) {
                request.getAttachments().putAll(invocation.getAttachments());
            }
            request.getAttachments().put("version", url.getVersion());
            request.getAttachments().put("group", url.getGroup());

            RpcResponse response = sendRequest(request);
            com.taotao.cloud.rpc.common.invoke.Result result = com.taotao.cloud.rpc.common.invoke.Result.success(response.getResult());
            if (response.hasException()) {
                result = com.taotao.cloud.rpc.common.invoke.Result.error(
                        response.getException() != null
                                ? response.getException()
                                : new RuntimeException(response.getErrorMessage()));
            }
            return result;
        } catch (Exception e) {
            log.error("RPC invoke failed", e);
            return com.taotao.cloud.rpc.common.invoke.Result.error(e);
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        connected = false;
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
        // 清理未完成的请求
        for (CompletableFuture<RpcResponse> future : pendingRequests.values()) {
            future.completeExceptionally(new RuntimeException("Client closed"));
        }
        pendingRequests.clear();
    }

    /**
     * 是否已连接
     */
    public boolean isConnected() {
        return connected && channel != null && channel.isActive();
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * RPC 客户端处理器
     */
    @ChannelHandler.Sharable
    private class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
        private final Logger log = LogManager.getLogger(this.getClass());

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) {
            CompletableFuture<RpcResponse> future = pendingRequests.remove(response.getRequestId());
            if (future != null) {
                future.complete(response);
            } else {
                log.warn("Received unknown response: {}", response.getRequestId());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.error("Client exception: {}", cause.getMessage(), cause);
            failAllPending(cause);
            ctx.close();
        }

        /**
         * 连接没了这件事必须传达到每一个在飞的等待方：这个 handler 是唯一有机会做这件事的人，
         * 缺了它，一条被对端关掉的连接会把上面所有调用都退化成"干等满 timeout"。
         */
        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            log.warn("Connection to RPC server {}:{} lost", host, port);
            failAllPending(new RuntimeException("Connection closed"));
            ctx.fireChannelInactive();
        }
    }

    /**
     * 逐条摘掉并叫醒：先 remove 再 complete，避免 {@link #sendRequest} catch 里那次
     * remove 与这里的 complete 互相抢，留下一条永远不会有人再读的 future。
     */
    private void failAllPending(Throwable cause) {
        for (String id : new java.util.ArrayList<>(pendingRequests.keySet())) {
            CompletableFuture<RpcResponse> future = pendingRequests.remove(id);
            if (future != null) {
                future.completeExceptionally(cause);
            }
        }
    }
}
