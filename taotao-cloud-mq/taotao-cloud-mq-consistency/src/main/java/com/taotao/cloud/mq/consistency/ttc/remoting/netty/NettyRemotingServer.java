package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

import com.taotao.cloud.mq.consistency.ttc.remoting.common.Pair;
import com.taotao.cloud.mq.consistency.ttc.remoting.common.RemotingHelper;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyConnectManageHandler;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingAbstract;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Netty 远程通信服务端
 */
public class NettyRemotingServer extends NettyRemotingAbstract {

    private static final Logger log = LoggerFactory.getLogger(NettyRemotingServer.class);

    // 服务器配置
    private final NettyServerConfig nettyServerConfig;

    // Boss线程组
    private EventLoopGroup eventLoopGroupBoss;

    // Worker线程组
    private EventLoopGroup eventLoopGroupSelector;

    // 业务线程池
    private DefaultEventExecutorGroup defaultEventExecutorGroup;

    // Netty ServerBootstrap
    private ServerBootstrap serverBootstrap;

    // Channel
    private Channel serverChannel;

    // 端口
    private int port = 0;

    // 公共业务线程池
    private ExecutorService publicExecutor;

    // 定时任务调度器
    private ScheduledExecutorService scheduledExecutorService;

    // 通道事件监听器
    private ChannelEventListener channelEventListener;

    public NettyRemotingServer(final NettyServerConfig nettyServerConfig) {
        this(nettyServerConfig, null);
    }

    public NettyRemotingServer(final NettyServerConfig nettyServerConfig,
                               final ChannelEventListener channelEventListener) {
        super(nettyServerConfig.getServerAsyncSemaphoreValue(),
                nettyServerConfig.getServerOnewaySemaphoreValue());
        this.nettyServerConfig = nettyServerConfig;
        this.channelEventListener = channelEventListener;

        // 初始化公共线程池
        this.publicExecutor = Executors.newFixedThreadPool(
                nettyServerConfig.getServerCallbackExecutorThreads(),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyServerPublicExecutor_" + threadIndex.incrementAndGet());
                    }
                });

        // 初始化定时任务调度器
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyServerScheduledThread");
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
     * 启动服务器
     */
    public void start() {
        // 创建Boss线程组
        this.eventLoopGroupBoss = createEventLoopGroup(1, "NettyBoss");

        // 创建Worker线程组
        this.eventLoopGroupSelector = createEventLoopGroup(
                nettyServerConfig.getServerSelectorThreads(), "NettyServerSelector");

        // 创建业务线程组
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                nettyServerConfig.getServerWorkerThreads(),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyServerWorker_" + threadIndex.incrementAndGet());
                    }
                });

        // 创建ServerBootstrap
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupSelector)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, nettyServerConfig.getServerSocketBacklog())
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSndBufSize())
                .childOption(ChannelOption.SO_RCVBUF, nettyServerConfig.getServerSocketRcvBufSize())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(defaultEventExecutorGroup,
                                        new IdleStateHandler(0, 0,
                                                nettyServerConfig.getServerChannelMaxIdleTimeSeconds()),
                                        new NettyDecoder(),
                                        new NettyEncoder(),
                                        new NettyConnectManageHandler(),
                                        new NettyServerHandler(NettyRemotingServer.this));
                    }
                });

        // 绑定端口并启动
        try {
            ChannelFuture sync = this.serverBootstrap.bind(nettyServerConfig.getListenPort()).sync();
            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
            this.port = addr.getPort();
            this.serverChannel = sync.channel();
            log.info("NettyRemotingServer started on port {}", this.port);
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to start NettyRemotingServer", e);
        }

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
    }

    /**
     * 关闭服务器
     */
    public void shutdown() {
        // 停止定时任务
        if (this.scheduledExecutorService != null) {
            this.scheduledExecutorService.shutdown();
        }

        // 关闭业务线程组
        if (this.defaultEventExecutorGroup != null) {
            this.defaultEventExecutorGroup.shutdownGracefully();
        }

        // 关闭Worker线程组
        if (this.eventLoopGroupSelector != null) {
            this.eventLoopGroupSelector.shutdownGracefully();
        }

        // 关闭Boss线程组
        if (this.eventLoopGroupBoss != null) {
            this.eventLoopGroupBoss.shutdownGracefully();
        }

        // 关闭公共线程池
        if (this.publicExecutor != null) {
            this.publicExecutor.shutdown();
        }

        // 关闭服务器通道
        if (this.serverChannel != null) {
            this.serverChannel.close();
        }

        log.info("NettyRemotingServer shutdown successfully");
    }

    /**
     * 注册处理器
     */
    public void registerProcessor(int requestCode, NettyRequestProcessor processor, ExecutorService executor) {
        ExecutorService executorThis = executor;
        if (null == executorThis) {
            executorThis = this.publicExecutor;
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
     * 获取本地监听端口
     */
    public int localListenPort() {
        return this.port;
    }

    /**
     * 创建EventLoopGroup
     */
    private EventLoopGroup createEventLoopGroup(int nThreads, String threadNamePrefix) {
        ThreadFactory threadFactory = r -> {
            AtomicInteger threadIndex = new AtomicInteger(0);
            return new Thread(r, threadNamePrefix + "_" + threadIndex.incrementAndGet());
        };

        if (useEpoll()) {
            return new EpollEventLoopGroup(nThreads, threadFactory);
        } else {
            return new NioEventLoopGroup(nThreads, threadFactory);
        }
    }

    /**
     * 是否使用Epoll
     */
    private boolean useEpoll() {
        return RemotingHelper.isLinuxPlatform() && Epoll.isAvailable();
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

    // ThreadFactory removed - using java.util.concurrent.ThreadFactory instead
}
