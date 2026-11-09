package com.taotao.cloud.mq.consistency.ttc.nameserver;

import com.taotao.cloud.mq.consistency.ttc.nameserver.kvconfig.KVConfigManager;
import com.taotao.cloud.mq.consistency.ttc.nameserver.processor.DefaultRequestProcessor;
import com.taotao.cloud.mq.consistency.ttc.nameserver.routeinfo.RouteInfoManager;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyRemotingServer;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * NameServer 控制器.
 * <p>
 * API 基础路径: 无 (非 REST, 由 Netty 远程服务端承载集群内部请求)
 * 所属模块: z-mq-nameserver
 * 鉴权: 由 z-mq 集群内部协议保障
 *
 * <p>NameServer 是整个 MQ 集群的服务注册与发现中心, 负责:
 * <ol>
 *   <li>Broker 注册管理 — 接收 Broker 心跳, 维护 Broker 列表</li>
 *   <li>路由信息管理 — 维护 Topic 到 Broker 的路由关系</li>
 *   <li>服务发现 — 为 Producer 和 Consumer 提供路由查询</li>
 *   <li>KV 配置管理 — 管理全局配置项</li>
 * </ol>
 */
public class NameServerController {

    private static final Logger log = LoggerFactory.getLogger(NameServerController.class);

    // NameServer配置
    private final NamesrvConfig namesrvConfig;

    // Netty服务端配置
    private final NettyServerConfig nettyServerConfig;
    // 运行标志
    private final AtomicBoolean running = new AtomicBoolean(false);
    // 路由信息管理器
    private RouteInfoManager routeInfoManager;
    // KV配置管理器
    private KVConfigManager kvConfigManager;
    // Netty服务端
    private NettyRemotingServer remotingServer;
    // 网络事件执行器
    private ExecutorService remotingExecutor;
    // 定时任务调度器 - Broker通道扫描（每10秒）
    private ScheduledExecutorService scheduledExecutorService;
    // 定时任务调度器 - 打印KV配置（每10分钟）
    private ScheduledExecutorService scanExecutorService;

    /**
     * 构造一个 NameServer 控制器, 仅注入配置 (不会立即初始化/启动).
     *
     * @param namesrvConfig     NameServer 配置
     * @param nettyServerConfig Netty 服务端配置
     */
    public NameServerController(NamesrvConfig namesrvConfig, NettyServerConfig nettyServerConfig) {
        this.namesrvConfig = namesrvConfig;
        this.nettyServerConfig = nettyServerConfig;
    }

    /**
     * 初始化 NameServer: 加载 KV 配置, 创建路由信息管理器, 创建 Netty 服务端,
     * 并注册两个定时任务 — Broker 通道扫描 (10s) 与 KV 配置打印 (10min).
     *
     * @return 始终返回 true
     */
    public boolean initialize() {
        // 初始化KV配置管理器
        this.kvConfigManager = new KVConfigManager(namesrvConfig.getKvConfigPath());
        this.kvConfigManager.load();

        // 初始化路由信息管理器
        this.routeInfoManager = new RouteInfoManager();

        // 初始化Netty服务端
        this.remotingServer = new NettyRemotingServer(this.nettyServerConfig);

        // 初始化网络事件执行器
        this.remotingExecutor = Executors.newFixedThreadPool(
                nettyServerConfig.getServerWorkerThreads(),
                r -> new Thread(r, "NameServerExecutorThread_" + ThreadLocalRandom.current().nextInt(1000)));

        // 注册默认请求处理器 (单例)
        this.remotingServer.registerDefaultProcessor(
                new DefaultRequestProcessor(this),
                this.remotingExecutor);

        // 初始化定时任务调度器 - Broker通道扫描
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
                r -> new Thread(r, "NameServerScheduledThread"));

        // 初始化定时任务调度器 - 打印KV配置
        this.scanExecutorService = Executors.newSingleThreadScheduledExecutor(
                r -> new Thread(r, "NameServerScanThread"));

        // 注册定时任务 - 每10秒扫描不活跃的Broker
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                NameServerController.this.routeInfoManager.scanNotActiveBroker();
            } catch (Exception e) {
                log.error("scanNotActiveBroker exception", e);
            }
        }, 5, 10, TimeUnit.SECONDS);

        // 注册定时任务 - 每10分钟打印KV配置
        this.scanExecutorService.scheduleAtFixedRate(() -> {
            try {
                NameServerController.this.kvConfigManager.printAllPeriodically();
            } catch (Exception e) {
                log.error("printAllPeriodically exception", e);
            }
        }, 1, 10, TimeUnit.MINUTES);

        return true;
    }

    /**
     * 启动 NameServer, 幂等启动, 实际启动动作仅在首次调用时执行.
     *
     * @throws Exception 启动过程中发生的异常
     */
    public void start() throws Exception {
        if (this.running.compareAndSet(false, true)) {
            this.remotingServer.start();
            log.info("NameServer started successfully");
        }
    }

    /**
     * 关闭 NameServer, 幂等关闭: 关闭定时任务, 关闭 Netty, 关闭执行器, 持久化 KV 配置.
     */
    public void shutdown() {
        if (this.running.compareAndSet(true, false)) {
            // 关闭定时任务
            this.scheduledExecutorService.shutdown();
            this.scanExecutorService.shutdown();

            // 关闭Netty服务端
            this.remotingServer.shutdown();

            // 关闭执行器
            this.remotingExecutor.shutdown();

            // 保存KV配置
            this.kvConfigManager.persist();

            log.info("NameServer shutdown successfully");
        }
    }

    /**
     * @return NameServer 配置
     */
    public NamesrvConfig getNamesrvConfig() {
        return namesrvConfig;
    }

    /**
     * @return Netty 服务端配置
     */
    public NettyServerConfig getNettyServerConfig() {
        return nettyServerConfig;
    }

    /**
     * @return 路由信息管理器
     */
    public RouteInfoManager getRouteInfoManager() {
        return routeInfoManager;
    }

    /**
     * @return KV 配置管理器
     */
    public KVConfigManager getKvConfigManager() {
        return kvConfigManager;
    }
}
