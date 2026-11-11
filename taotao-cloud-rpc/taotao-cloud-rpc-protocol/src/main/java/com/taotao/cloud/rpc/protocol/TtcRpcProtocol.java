package com.taotao.cloud.rpc.protocol;

import com.taotao.cloud.rpc.common.ProtocolConstants;
import com.taotao.cloud.rpc.common.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ttc-RPC 协议入口（端到端最小化实现）
 * <p>
 * 该类同时作为 server 启动器和 client 启动器，用于演示完整的协议交互：
 * <ul>
 *   <li>魔数/版本/长度 解析</li>
 *   <li>请求 ID 自增与匹配</li>
 *   <li>心跳机制（基于 IdleStateHandler）</li>
 * </ul>
 * <p>
 * 真实生产使用请使用 {@link com.taotao.cloud.rpc.remoting.NettyServer} 和
 * {@link com.taotao.cloud.rpc.remoting.NettyClient}，它们与 ConnectionManager、HeartbeatReconnector 集成。
 */
public class TtcRpcProtocol {

    private static final Logger log = LogManager.getLogger(TtcRpcProtocol.class);

    /** 全局请求 ID 自增器 */
    private static final AtomicLong REQUEST_ID_GEN = new AtomicLong(1);

    /**
     * 生成下一个请求 ID
     */
    public static long nextRequestId() {
        return REQUEST_ID_GEN.incrementAndGet();
    }

    // ====================== 测试用 Server ======================

    public static class TestServer {
        private final int port;
        private EventLoopGroup bossGroup;
        private EventLoopGroup workerGroup;
        private Channel channel;
        private volatile boolean started = false;
        private final java.util.function.Function<TtcRpcMessage, TtcRpcMessage> handler;

        public TestServer(int port, java.util.function.Function<TtcRpcMessage, TtcRpcMessage> handler) {
            this.port = port;
            this.handler = handler;
        }

        public void start() throws InterruptedException {
            if (started) return;
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(
                                        ProtocolConstants.DEFAULT_PAYLOAD, 0, 4, 0, 4));
                                ch.pipeline().addLast(new TtcRpcMessageDecoder());
                                ch.pipeline().addLast(new TtcRpcMessageEncoder());
                                ch.pipeline().addLast(new IdleStateHandler(0, 0,
                                        ProtocolConstants.DEFAULT_HEARTBEAT_INTERVAL_MS / 1000 * 3,
                                        TimeUnit.MILLISECONDS));
                                ch.pipeline().addLast(new ServerHandler(handler));
                            }
                        });
                ChannelFuture f = b.bind(port).sync();
                channel = f.channel();
                started = true;
                log.info("ttc-RPC TestServer started on port {}", port);
            } catch (Exception e) {
                throw new RuntimeException("Failed to start TestServer on port " + port, e);
            }
        }

        public void stop() {
            if (channel != null) channel.close();
            if (bossGroup != null) bossGroup.shutdownGracefully();
            if (workerGroup != null) workerGroup.shutdownGracefully();
            started = false;
        }

        public boolean isStarted() {
            return started;
        }
    }

    // ====================== 测试用 Client ======================

    public static class TestClient {
        private final String host;
        private final int port;
        private Channel channel;
        private EventLoopGroup group;
        private volatile boolean connected = false;

        public TestClient(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public void connect() {
            group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(
                                        ProtocolConstants.DEFAULT_PAYLOAD, 0, 4, 0, 4));
                                ch.pipeline().addLast(new TtcRpcMessageDecoder());
                                ch.pipeline().addLast(new TtcRpcMessageEncoder());
                            }
                        });
                ChannelFuture f = b.connect(host, port).sync();
                channel = f.channel();
                connected = true;
            } catch (Exception e) {
                throw RpcException.network("Failed to connect to " + host + ":" + port, e);
            }
        }

        public void send(TtcRpcMessage message) {
            if (!connected || !channel.isActive()) {
                throw RpcException.network("Channel not active: " + host + ":" + port);
            }
            channel.writeAndFlush(message);
        }

        public void close() {
            if (channel != null) channel.close();
            if (group != null) group.shutdownGracefully();
            connected = false;
        }

        public boolean isConnected() {
            return connected && channel != null && channel.isActive();
        }
    }

    // ====================== Server Handler ======================

    private static class ServerHandler extends ChannelInboundHandlerAdapter {
        private final java.util.function.Function<TtcRpcMessage, TtcRpcMessage> handler;

        ServerHandler(java.util.function.Function<TtcRpcMessage, TtcRpcMessage> handler) {
            this.handler = handler;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (!(msg instanceof TtcRpcMessage)) return;
            TtcRpcMessage request = (TtcRpcMessage) msg;

            // 心跳请求 → 心跳响应
            if (request.getMessageType() == ProtocolConstants.MSG_TYPE_HEARTBEAT_REQ) {
                TtcRpcMessage response = new TtcRpcMessage();
                response.setMessageType(ProtocolConstants.MSG_TYPE_HEARTBEAT_RES);
                response.setRequestId(request.getRequestId());
                ctx.writeAndFlush(response);
                return;
            }

            if (handler != null) {
                try {
                    TtcRpcMessage response = handler.apply(request);
                    if (response != null) {
                        ctx.writeAndFlush(response);
                    }
                } catch (Exception e) {
                    log.error("Handler error: {}", e.getMessage(), e);
                }
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.ALL_IDLE) {
                    log.debug("Server: channel idle, send heartbeat ping to {}", ctx.channel().remoteAddress());
                    TtcRpcMessage ping = new TtcRpcMessage();
                    ping.setMessageType(ProtocolConstants.MSG_TYPE_HEARTBEAT_REQ);
                    ping.setRequestId(nextRequestId());
                    ctx.writeAndFlush(ping);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.error("Server channel error: {}", cause.getMessage(), cause);
            ctx.close();
        }
    }
}
