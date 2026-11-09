package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

import com.taotao.cloud.mq.consistency.ttc.remoting.common.RemotingHelper;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingSendRequestException;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingTimeoutException;
import com.taotao.cloud.mq.consistency.ttc.remoting.exception.RemotingTooMuchRequestException;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyConnectManageHandler;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Netty 远程通信客户端
 */
public class NettyRemotingClient extends NettyRemotingAbstract {

    private static final Logger log = LoggerFactory.getLogger(NettyRemotingClient.class);

    // 客户端配置
    private final NettyClientConfig nettyClientConfig;
    // 通道表（key: address, value: channel）
    private final ConcurrentHashMap<String, ChannelWrapper> channelTables = new ConcurrentHashMap<>();
    // 锁
    private final Lock lockChannelTables = new ReentrantLock();
    // 定时任务调度器
    private final ScheduledExecutorService scheduledExecutorService;
    // 公共业务线程池
    private final ExecutorService publicExecutor;
    // 通道事件监听器
    private final ChannelEventListener channelEventListener;
    // Worker线程组
    private EventLoopGroup eventLoopGroupWorker;
    // 业务线程组
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    // Netty Bootstrap
    private Bootstrap bootstrap;

    public NettyRemotingClient(final NettyClientConfig nettyClientConfig) {
        this(nettyClientConfig, null);
    }

    public NettyRemotingClient(final NettyClientConfig nettyClientConfig,
                               final ChannelEventListener channelEventListener) {
        super(nettyClientConfig.getClientAsyncSemaphoreValue(),
                nettyClientConfig.getClientOnewaySemaphoreValue());
        this.nettyClientConfig = nettyClientConfig;
        this.channelEventListener = channelEventListener;

        // 初始化公共线程池
        int publicThreadNums = nettyClientConfig.getClientCallbackExecutorThreads();
        if (publicThreadNums <= 0) {
            publicThreadNums = 4;
        }
        this.publicExecutor = Executors.newFixedThreadPool(
                publicThreadNums,
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyClientPublicExecutor_" + threadIndex.incrementAndGet());
                    }
                });

        // 初始化定时任务调度器
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyClientScheduledThread");
                    }
                });
    }

    @Override
    public ExecutorService getCallbackExecutor() {
        return this.publicExecutor;
    }

    @Override
    public ExecutorService getPublicExecutor() {
        return this.publicExecutor;
    }

    /**
     * 启动客户端
     */
    public void start() {
        // 创建Worker线程组
        this.eventLoopGroupWorker = new NioEventLoopGroup(
                nettyClientConfig.getClientWorkerThreads(),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyClientWorker_" + threadIndex.incrementAndGet());
                    }
                });

        // 创建业务线程组
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                nettyClientConfig.getClientWorkerThreads(),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyClientWorker_" + threadIndex.incrementAndGet());
                    }
                });

        // 创建Bootstrap
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.eventLoopGroupWorker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis())
                .option(ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize())
                .option(ChannelOption.SO_RCVBUF, nettyClientConfig.getClientSocketRcvBufSize())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                defaultEventExecutorGroup,
                                new NettyEncoder(),
                                new NettyDecoder(),
                                new IdleStateHandler(0, 0,
                                        nettyClientConfig.getClientChannelMaxIdleTimeSeconds()),
                                new NettyConnectManageHandler(),
                                new NettyClientHandler());
                    }
                });

        // 启动定时扫描任务
        this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    scanResponseTable();
                } catch (Throwable e) {
                    log.error("scanResponseTable exception", e);
                }
            }
        }, 3000, 1000, TimeUnit.MILLISECONDS);

        log.info("NettyRemotingClient started");
    }

    /**
     * 关闭客户端
     */
    public void shutdown() {
        // 停止定时任务
        if (this.scheduledExecutorService != null) {
            this.scheduledExecutorService.shutdown();
        }

        // 关闭所有连接
        for (ChannelWrapper cw : this.channelTables.values()) {
            RemotingHelper.closeChannel(cw.getChannel());
        }
        this.channelTables.clear();

        // 关闭业务线程组
        if (this.defaultEventExecutorGroup != null) {
            this.defaultEventExecutorGroup.shutdownGracefully();
        }

        // 关闭Worker线程组
        if (this.eventLoopGroupWorker != null) {
            this.eventLoopGroupWorker.shutdownGracefully();
        }

        // 关闭公共线程池
        if (this.publicExecutor != null) {
            this.publicExecutor.shutdown();
        }

        log.info("NettyRemotingClient shutdown successfully");
    }

    /**
     * 创建通道
     */
    private Channel createChannel(final String addr) throws InterruptedException {
        ChannelWrapper cw = this.channelTables.get(addr);
        if (cw != null && cw.isActive()) {
            return cw.getChannel();
        }

        if (this.lockChannelTables.tryLock(3000, TimeUnit.MILLISECONDS)) {
            try {
                boolean createNewConnection;
                cw = this.channelTables.get(addr);
                if (cw != null) {
                    if (cw.isActive()) {
                        return cw.getChannel();
                    } else if (!cw.getChannelFuture().isDone()) {
                        createNewConnection = false;
                    } else {
                        this.channelTables.remove(addr);
                        createNewConnection = true;
                    }
                } else {
                    createNewConnection = true;
                }

                if (createNewConnection) {
                    ChannelFuture channelFuture = this.bootstrap.connect(RemotingHelper.string2SocketAddress(addr));
                    log.info("createChannel: begin to connect remote host[{}] asynchronously", addr);
                    cw = new ChannelWrapper(channelFuture);
                    this.channelTables.put(addr, cw);
                }
            } catch (Exception e) {
                log.error("createChannel: create channel exception", e);
            } finally {
                this.lockChannelTables.unlock();
            }
        } else {
            log.warn("createChannel: try to lock channel table, but timeout, {}ms", 3000);
        }

        if (cw != null) {
            ChannelFuture channelFuture = cw.getChannelFuture();
            if (channelFuture.awaitUninterruptibly(this.nettyClientConfig.getConnectTimeoutMillis())) {
                if (cw.isActive()) {
                    log.info("createChannel: connect remote host[{}] success, {}", addr, channelFuture.toString());
                    return cw.getChannel();
                } else {
                    log.warn("createChannel: connect remote host[{}] failed, {}", addr, channelFuture.toString());
                }
            } else {
                log.warn("createChannel: connect remote host[{}] timeout {}ms, {}", addr,
                        this.nettyClientConfig.getConnectTimeoutMillis(), channelFuture.toString());
            }
        }

        return null;
    }

    /**
     * 获取或创建通道
     */
    public Channel getOrCreateChannel(final String addr) throws InterruptedException {
        ChannelWrapper cw = this.channelTables.get(addr);
        if (cw != null && cw.isActive()) {
            return cw.getChannel();
        }
        return createChannel(addr);
    }

    /**
     * 公开 connect：按目标地址建立连接（被 MQClientInstance 等高层组件调用）.
     */
    public Channel connect(String addr) throws InterruptedException {
        return getOrCreateChannel(addr);
    }

    /**
     * 公开 invokeSync：在已建好的 Channel 上发起同步 RPC.
     */
    public RemotingCommand invokeSync(Channel channel, RemotingCommand request, long timeoutMillis)
            throws InterruptedException, RemotingTimeoutException, RemotingSendRequestException, Exception {
        return invokeSyncImpl(channel, request, timeoutMillis);
    }

    /**
     * 公开 invokeAsync：在已建好的 Channel 上发起异步 RPC.
     */
    public void invokeAsync(Channel channel, RemotingCommand request, long timeoutMillis,
                            InvokeCallback callback)
            throws Exception {
        invokeAsyncImpl(channel, request, timeoutMillis, callback);
    }

    /**
     * 公开 invokeOneway：不等待响应.
     */
    public void invokeOneway(Channel channel, RemotingCommand request, long timeoutMillis)
            throws Exception {
        invokeOnewayImpl(channel, request, timeoutMillis);
    }

    // ==================== 内部类 ====================

    /**
     * 通道事件监听器
     */
    public interface ChannelEventListener {
        void onChannelConnect(final String remoteAddr, final Channel channel);

        void onChannelClose(final String remoteAddr, final Channel channel);

        void onChannelException(final String remoteAddr, final Channel channel);

        void onChannelIdle(final String remoteAddr, final Channel channel);
    }

    /**
     * Channel包装类
     */
    static class ChannelWrapper {
        private final ChannelFuture channelFuture;

        public ChannelWrapper(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        public boolean isActive() {
            return this.channelFuture.channel() != null && this.channelFuture.channel().isActive();
        }

        public ChannelFuture getChannelFuture() {
            return channelFuture;
        }

        public Channel getChannel() {
            return this.channelFuture.channel();
        }
    }

    /**
     * Netty客户端Handler
     */
    class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
            processMessageReceived(ctx, msg);
        }
    }
}
