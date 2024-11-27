package com.taotao.cloud.media.biz.streammediaservices;

/**
 * https://juejin.cn/post/7376595796672004105#heading-3
 *
 * http://localhost:9998/live?url=播放地址
 *
 * ws://localhost:9998/live?url=播放地址
 * 各品牌海康、大华播放地址如何拼接自行百度
 * 海康播放地址： rtsp://admin:fangjia301@192.168.88.22:554/Streaming/Channels/1
 * 本地视频：F:/otherGitProject/video/file.mp4
 */
@Slf4j
@Component
public class NettyServer implements CommandLineRunner, DisposableBean {
    @Resource
    private MediaHandler mediaHandler;

    @Value("${media.port}")
    private Integer mediaPort;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;

    @Override
    public void destroy() throws Exception {
        log.info("websocket server shutdown...");
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().sync();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully().sync();
        }
        if (channelFuture != null) {
            channelFuture.channel().closeFuture().syncUninterruptibly();
        }
        log.info("websocket server shutdown");
    }

    @Override
    public void run(String... args) throws Exception {
        // bossGroup连接线程组，主要负责接受客户端连接，一般一个线程足矣
        bossGroup = new NioEventLoopGroup(1);
        // workerGroup工作线程组，主要负责网络IO读写
        workerGroup = new NioEventLoopGroup(4);
        try {
            // 服务端启动引导类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 服务端启动引导类绑定两个线程组
            serverBootstrap.group(bossGroup, workerGroup);
            // 设置通道为NioChannel
            serverBootstrap.channel(NioServerSocketChannel.class);
            // 可以对入站\出站事件进行日志记录，从而方便我们进行问题排查。
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            // 首选直接内存
            serverBootstrap.option(ChannelOption.ALLOCATOR, PreferredDirectByteBufAllocator.DEFAULT);
            // 服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            // 设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 将小的数据包包装成更大的帧进行传送，提高网络的负载,即TCP延迟传输 设置为不延时
            serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            // 设置自定义的通道初始化器 入站、出站处理
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();

                    // 基于http协议，使用http的编码和解码器
                    pipeline.addLast(new HttpServerCodec());
                    // 以块方式写，添加ChunkedWriteHandler处理器
                    pipeline.addLast(new ChunkedWriteHandler());
                    /**
                     *  说明
                     *  1. http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
                     *  2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
                     */
                    pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 1024));
                    /**
                     *说明
                     * 1. 对应websocket ，它的数据是以 帧(frame) 形式传递
                     * 2. 可以看到WebSocketFrame 下面有六个子类
                     * 3. 浏览器请求时 ws://localhost:8082/v1 表示请求的uri
                     * 4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
                     * 5. 是通过一个 状态码 101
                     *
                     * 这里我们自己进行握手
                     */
//                    pipeline.addLast(new WebSocketServerProtocolHandler("/v1", null, true, 65535));
                    // 跨域handler
                    pipeline.addLast(new CorsHandler(corsConfig));
                    // 添加自定义的handler
                    pipeline.addLast(mediaHandler);
                }
            });

            // 异步IO的结果
            channelFuture = serverBootstrap.bind(new InetSocketAddress("0.0.0.0", mediaPort)).sync();
            // 服务端启动监听事件
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("websocket start success");
                } else {
                    log.info("websocket start failed");
                }
            });
        } catch (Exception e) {
            log.info("websocket start error");
        }
    }
}
