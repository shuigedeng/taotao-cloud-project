package com.taotao.cloud.rpc.remoting;

import com.taotao.cloud.rpc.remoting.RpcMessageDecoder;
import com.taotao.cloud.rpc.remoting.RpcMessageEncoder;
import com.taotao.cloud.rpc.remoting.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC 服务器
 * 基于 Netty 实现
 */
public class RpcServer {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final String host;
    private final int port;
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;
    /**
     * 真实绑到的端口，0 表示还没绑过。绑完之后 {@link #getPort()} 以它为准：
     * 传 0 让内核挑端口时配置值永远是 0，而调用方（含 starter 的启动日志）读的就是
     * {@code getPort()}。
     */
    private volatile int boundPort;
    private volatile boolean started = false;
    private Thread serverThread;

    public RpcServer(int port) {
        this("0.0.0.0", port);
    }

    public RpcServer(String host, int port) {
        this.host = host == null || host.isEmpty() ? "0.0.0.0" : host;
        this.port = port;
    }

    /**
     * 注册服务（按接口全名）
     */
    public void registerService(Class<?> serviceInterface, Object serviceImpl) {
        registerService(serviceInterface.getName(), serviceImpl);
    }

    /**
     * 注册服务（按 serviceName）
     */
    public void registerService(String serviceName, Object serviceImpl) {
        serviceMap.put(serviceName, serviceImpl);
        log.info("Registered service: {}", serviceName);
    }

    /**
     * 服务在 {@code serviceMap} 里的唯一键：带版本时是 {@code {interface}:{version}}，
     * 不带版本时退化为裸接口名。注册与查找必须共用这一个函数，否则两边拼不出同一个键。
     */
    public static String serviceKey(String interfaceName, String version) {
        if (version == null || version.isEmpty()) {
            return interfaceName;
        }
        return interfaceName + ":" + version;
    }

    /**
     * 注册服务（带版本）：按 {@code {serviceInterface}:{version}} 精确登记。
     * <p>
     * 不再额外写一份裸接口名别名：那会让"同接口多版本"退化成"第一个版本冒充默认实现"，
     * 正是本方法修掉的那个缺陷。不带版本的 Provider 请走
     * {@link #registerService(Class, Object)}。
     */
    public void register(Class<?> serviceInterface, Object serviceImpl, String version) {
        if (version == null || version.isEmpty()) {
            registerService(serviceInterface, serviceImpl);
            return;
        }
        serviceMap.put(serviceKey(serviceInterface.getName(), version), serviceImpl);
        log.info("Registered service: {}:{}", serviceInterface.getName(), version);
    }

    /**
     * 启动服务器（阻塞）
     */
    public void start() throws InterruptedException {
        start(false);
    }

    /**
     * 启动服务器
     * @param daemon true 表示以守护线程异步启动（不阻塞当前线程）
     */
    public void start(boolean daemon) throws InterruptedException {
        Channel bound;
        synchronized (this) {
            if (started) {
                return;
            }

            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(new RpcServerHandler(serviceMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);

            ChannelFuture future;
            try {
                future = bootstrap.bind(host, port).sync();
            } catch (Throwable t) {
                // 两组 EventLoopGroup 已经建好；绑定失败不关，就是永不退出的非守护线程
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                bossGroup = null;
                workerGroup = null;
                throw t;
            }
            bound = future.channel();
            channel = bound;
            java.net.SocketAddress local = bound.localAddress();
            if (local instanceof java.net.InetSocketAddress) {
                boundPort = ((java.net.InetSocketAddress) local).getPort();
            }
            started = true;
            log.info("RPC Server started on {}:{}", host, getPort());
        }

        if (daemon) {
            // 守护线程：把 closeFuture 移到独立线程，避免阻塞调用方
            serverThread = new Thread(() -> {
                try {
                    bound.closeFuture().await();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                } finally {
                    stop();
                }
            }, "ttc-rpc-server-shutdown-hook");
            serverThread.setDaemon(true);
            serverThread.start();
        } else {
            // 阻塞模式：await 必须在监视器之外，否则 stop() 永远拿不到锁，
            // 而 ServiceConfig 正是用一个常驻线程走这条分支的。
            bound.closeFuture().await();
            stop();
        }
    }

    /**
     * 停止服务器
     */
    public synchronized void stop() {
        if (!started) {
            return;
        }
        started = false;

        if (channel != null) {
            channel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }

        log.info("RPC Server stopped");
    }

    /**
     * 是否已启动
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * 获取服务映射
     */
    public Map<String, Object> getServiceMap() {
        return new HashMap<>(serviceMap);
    }

    /**
     * 获取端口
     */
    public int getPort() {
        return boundPort != 0 ? boundPort : port;
    }

    public String getHost() {
        return host;
    }
}
