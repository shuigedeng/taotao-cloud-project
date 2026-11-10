package com.taotao.cloud.mq.broker;

import com.taotao.cloud.mq.broker.delay.ScheduleMessageService;
import com.taotao.cloud.mq.broker.ha.DefaultHAService;
import com.taotao.cloud.mq.broker.ha.HAProcessor;
import com.taotao.cloud.mq.broker.longpoll.PullRequestHoldService;
import com.taotao.cloud.mq.broker.outapi.BrokerOutAPI;
import com.taotao.cloud.mq.broker.processor.*;
import com.taotao.cloud.mq.broker.slave.SlaveSynchronize;
import com.taotao.cloud.mq.broker.transaction.TransactionCheckService;
import com.taotao.cloud.mq.broker.transaction.TransactionStateRecovery;
import com.taotao.cloud.mq.common.RegisterBrokerResult;
import com.taotao.cloud.mq.common.TopicConfig;
import com.taotao.cloud.mq.common.enums.TransactionState;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingServer;
import com.taotao.cloud.mq.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import com.taotao.cloud.mq.plugin.store.config.ConsumerOffsetManager;
import com.taotao.cloud.mq.plugin.store.config.TopicConfigManager;
import com.taotao.cloud.mq.plugin.store.log.CommitLog;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Broker 控制器.
 * <p>
 * API 基础路径: 无 (非 REST, 由 Netty 远程服务端承载客户端请求)
 * 所属模块: ttc-mq-broker
 * 鉴权: 由 ttc-mq 集群内部协议保障
 *
 * <p>Broker 是消息存储和转发的核心节点, 负责:
 * <ol>
 *   <li>消息存储 — 将消息持久化到 CommitLog</li>
 *   <li>消息转发 — 向 Consumer 推送消息</li>
 *   <li>消息查询 — 根据条件查询消息</li>
 *   <li>心跳维护 — 向 NameServer 注册并维持心跳</li>
 * </ol>
 */
public class BrokerController {

    private static final Logger log = LoggerFactory.getLogger(BrokerController.class);

    // Broker配置
    private final BrokerConfig brokerConfig;

    // 消息存储配置
    private final MessageStoreConfig messageStoreConfig;

    // Netty服务端配置
    private final NettyServerConfig nettyServerConfig;

    // 运行标志
    private final AtomicBoolean running = new AtomicBoolean(false);

    // Netty服务端（处理客户端请求）
    private NettyRemotingServer remotingServer;

    // 定时任务调度器
    private ScheduledExecutorService scheduledExecutorService;

    // 业务线程池
    private ExecutorService sendMessageExecutor;
    private ExecutorService pullMessageExecutor;
    private ExecutorService adminBrokerExecutor;

    // 注册到 NameServer 的客户端 (独立 NettyRemotingClient)
    private NettyRemotingClient namesrvRemotingClient;
    private long brokerHeartbeatIntervalMillis = 5000;

    // CommitLog
    private CommitLog commitLog;

    /** 管理命令处理器（topic 配置的创建与查询入口）. */
    private AdminBrokerProcessor adminBrokerProcessor;

    private SendMessageProcessor sendMessageProcessor;
    private PullMessageProcessor pullMessageProcessor;
    private ConsumerOffsetProcessor consumerOffsetProcessor;
    /**
     * 事务消息处理器（半消息 / 二次确认 / 回查三个码都挂在它身上，见 registerProcessor）。
     */
    private TransactionMessageProcessor transactionMessageProcessor;
    /** 事务回查调度（broker 进程内驱动"超时未确认 ⇒ 自动回滚"）。 */
    private TransactionCheckService transactionCheckService;

    /**
     * v2 增强组件:
     * <ul>
     *   <li>consumerOffsetManager: 消费位点持久化, 跨重启恢复。两条边都在 src/main 里:
     *       提交边 = UPDATE_CONSUMER_OFFSET(220) 挂到 ConsumerOffsetProcessor.handleUpdateConsumerOffset,
     *       读取边 = PullMessageProcessor.resolveStartOffset 在请求不带 offset 时按 consumerGroup 回读
     *       queryOffset; 端到端验收见 ConsumerOffsetRestartE2ETest</li>
     *   <li>topicConfigManager: Topic 配置持久化, 跨重启恢复。写边 = AdminBrokerProcessor 的建/改
     *       与全量替换都走 putTopicConfig / replaceAllTopicConfigs（改表与落盘在同一次写锁里, 不存在
     *       "内存改了文件没改"的中间态）; 读取边 = initialize() 里 start() 把盘上那份读回来, 位置早于
     *       remotingServer.start(); 端到端验收见 TopicConfigRestartE2ETest</li>
     *   <li>pullRequestHoldService: Pull 长轮询, 新消息毫秒级响应</li>
     *   <li>scheduleMessageService: 18 级内置延迟消息</li>
     * </ul>
     */
    private ConsumerOffsetManager consumerOffsetManager;
    private TopicConfigManager topicConfigManager;
    private PullRequestHoldService pullRequestHoldService;
    private ScheduleMessageService scheduleMessageService;

    /** v3 分布式组件: Broker OutAPI (Broker 互查接口). */
    private BrokerOutAPI brokerOutAPI;

    /** v3 分布式组件: SlaveSynchronize (仅 Slave 启动). */
    private SlaveSynchronize slaveSynchronize;

    /** v3 HA: 主从同步服务 (HAService 接口). */
    private com.taotao.cloud.mq.plugin.store.ha.HAService haService;

    /**
     * ConsumerOffset / DelayOffset 全局版本号 (每次 commit 自增).
     * 由 BrokerController 持有以便 BrokerOutAPI 暴露给 Slave.
     */
    private final com.taotao.cloud.mq.common.ha.DataVersion consumerOffsetDataVersion = new com.taotao.cloud.mq.common.ha.DataVersion();
    private final com.taotao.cloud.mq.common.ha.DataVersion delayOffsetDataVersion = new com.taotao.cloud.mq.common.ha.DataVersion();

    /**
     * 构造一个 Broker 控制器, 仅注入配置 (不会立即初始化/启动).
     *
     * @param brokerConfig       Broker 自身配置
     * @param messageStoreConfig 消息存储配置
     * @param nettyServerConfig  Netty 服务端配置
     */
    public BrokerController(
            BrokerConfig brokerConfig,
            MessageStoreConfig messageStoreConfig,
            NettyServerConfig nettyServerConfig) {
        this.brokerConfig = brokerConfig;
        this.messageStoreConfig = messageStoreConfig;
        this.nettyServerConfig = nettyServerConfig;
    }

    /**
     * 初始化 Broker: 加载 CommitLog, 创建 Netty 服务端, 注册处理器, 初始化定时任务与业务线程池.
     *
     * @return 初始化成功返回 true; 加载 CommitLog 失败或抛异常时返回 false
     */
    public boolean initialize() {
        try {
            // 上报地址里的端口取自真正要 bind 的那个配置。只在 BrokerStartup 里回填是不够的：
            // 测试与嵌入式用法都直接 new BrokerController，listenPort 会停在 0，
            // 注册给 NameServer 的就成了 "localhost:0"，客户端一律连不上。
            this.brokerConfig.setListenPort(this.nettyServerConfig.getListenPort());

            // 加载CommitLog
            this.commitLog = new CommitLog(this.messageStoreConfig);
            if (!this.commitLog.load()) {
                log.error("load commitlog failed");
                return false;
            }

            // 初始化 v2 增强组件
            this.consumerOffsetManager = new ConsumerOffsetManager(this.messageStoreConfig.getStorePathRootDir());
            this.consumerOffsetManager.start();

            // topic 配置后端: start() 里就把盘上那份读回来。
            // 位置承重 —— initialize() 全程不绑端口, remotingServer.start() 在 start() 里,
            // 所以这张表在第一条请求进来之前就已经是完整的; 处理器 (registerProcessor 在本方法后面)
            // 拿到的也是这个已经加载完的实例。
            this.topicConfigManager = new TopicConfigManager(this.messageStoreConfig.getStorePathRootDir());
            this.topicConfigManager.start();

            this.pullRequestHoldService = new PullRequestHoldService();
            // pullRequestHoldService 在 start() 时才启动 (后续)

            this.scheduleMessageService = new ScheduleMessageService();
            // scheduleMessageService 在 start() 时才启动 (后续)

            // v3 HA: 创建 HAService (Master / Slave 通用, 内部根据 brokerConfig.isMaster() 切换)
            this.haService = new DefaultHAService(this);
            // 把 HA 回调注入 CommitLog, putMessage 成功后通知 HA 服务
            this.commitLog.setHaCallback((offset, body) -> {
                if (this.haService != null) {
                    this.haService.notifyMessageArrived(offset, body);
                }
            });

            // 初始化Netty服务端
            this.remotingServer = new NettyRemotingServer(this.nettyServerConfig);

            // 初始化定时任务 (先于 processor, 用于心跳等场景)
            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "BrokerScheduledThread");
                t.setDaemon(true);
                return t;
            });

            // 初始化业务线程池 (必须先于 registerProcessor, 否则处理器绑定到 null 线程池)
            this.sendMessageExecutor = Executors.newFixedThreadPool(
                    this.brokerConfig.getSendMessageThreadPoolNums(),
                    r -> new Thread(r, "SendMessageThread_"));

            this.pullMessageExecutor = Executors.newFixedThreadPool(
                    this.brokerConfig.getPullMessageThreadPoolNums(),
                    r -> new Thread(r, "PullMessageThread_"));

            this.adminBrokerExecutor = Executors.newFixedThreadPool(
                    this.brokerConfig.getAdminBrokerThreadPoolNums(),
                    r -> new Thread(r, "AdminBrokerThread_"));

            // 注册处理器 (在线程池就绪后)
            this.registerProcessor();

            log.info("Broker initialize successfully");
            return true;
        } catch (Exception e) {
            log.error("Broker initialize failed", e);
            return false;
        }
    }

    /**
     * 向 Netty 远程服务端注册业务处理器 (发送消息 / 拉取消息 / 管理命令 / BrokerOutAPI).
     * <p>
     * 把四个处理器绑定到相应的 NettyRequestCode 上, 并各自使用独立的业务线程池
     * 隔离 IO 与业务执行, 避免慢调用拖垮 IO 线程.
     */
    private void registerProcessor() {
        // 发送消息处理器
        this.sendMessageProcessor = new SendMessageProcessor(this);
        this.remotingServer.registerProcessor(
                RequestCode.SEND_MESSAGE, this.sendMessageProcessor, this.sendMessageExecutor);

        // 拉取消息处理器
        this.pullMessageProcessor = new PullMessageProcessor(this);
        this.remotingServer.registerProcessor(
                RequestCode.PULL_MESSAGE, this.pullMessageProcessor, this.pullMessageExecutor);

        // 消费位点提交处理器 (UPDATE_CONSUMER_OFFSET → 下面已经 start 着的那个 consumerOffsetManager)
        //
        // 为什么挂 adminBrokerExecutor 而不是 pullMessageExecutor: 位点提交是消费组的【控制面】,
        // 而 pull 池 (brokerConfig.pullMessageThreadPoolNums=16) 会被长轮询整批占住
        // —— 一次挂起最长 MAX_SUSPEND_BUDGET_MILLIS=30s。把提交塞进那个队列, 就等于"消费位点要等
        // 16 条挂住的 pull 让位才写得进去", 那是把控制面挂在数据面最坏的一段延迟后面。
        // adminBrokerExecutor 是本仓既有的低频控制面池 (4 线程), 提交本身是一次内存写, 不需要新机制。
        this.consumerOffsetProcessor = new ConsumerOffsetProcessor(this);
        this.remotingServer.registerProcessor(
                RequestCode.UPDATE_CONSUMER_OFFSET, this.consumerOffsetProcessor, this.adminBrokerExecutor);

        // 管理命令处理器（创建/查询 Topic）
        this.adminBrokerProcessor = new AdminBrokerProcessor(this);
        this.remotingServer.registerProcessor(
                RequestCode.UPDATE_AND_CREATE_TOPIC, this.adminBrokerProcessor, this.adminBrokerExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.GET_ALL_TOPIC_LIST, this.adminBrokerProcessor, this.adminBrokerExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.DELETE_TOPIC, this.adminBrokerProcessor, this.adminBrokerExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.VIEW_MESSAGE_BY_KEY, this.adminBrokerProcessor, this.adminBrokerExecutor);

        // Broker OutAPI (Broker 互查接口, SlaveSynchronize 通过它拉取元数据)
        this.brokerOutAPI = new BrokerOutAPI(this);
        this.remotingServer.registerProcessor(
                RequestCode.GET_ALL_TOPIC_CONFIG, this.brokerOutAPI, this.adminBrokerExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.GET_ALL_CONSUMER_OFFSET, this.brokerOutAPI, this.adminBrokerExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.GET_ALL_DELAY_OFFSET, this.brokerOutAPI, this.adminBrokerExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.GET_ALL_SUBSCRIPTION_GROUP, this.brokerOutAPI, this.adminBrokerExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.QUERY_DATA_VERSION, this.brokerOutAPI, this.adminBrokerExecutor);

        // v3 HA: HA Processor (处理 Slave 上报 offset 等)
        HAProcessor haProcessor = new HAProcessor(this);
        this.remotingServer.registerProcessor(
                RequestCode.HA_REPORT_OFFSET, haProcessor, this.adminBrokerExecutor);

        // 事务消息：半消息 (SEND_MESSAGE_V2=201) / 二次确认 (END_TRANSACTION=251) / 回查 (CHECK_TRANSACTION_STATE=250)
        //
        // 为什么半消息绑 201 而不是复用 200：200 已经绑给 SendMessageProcessor，那条路径把消息按业务
        // topic 直接写进存储、对消费者立刻可见。让"提交前不该可见"的半消息也走 200，就等于要求两个
        // 处理器在同一条码上分诊 —— 分错一次的后果是同一条消息既以半消息又以业务 topic 落进存储两次。
        // 201 在 src/main 里的发送方只有事务 producer 的 prepare 阶段（TransactionMQProducer 把半消息
        // 显式发成 SEND_MESSAGE_V2）；普通发送一律 200，所以绑上 201 不会改道任何既有发送 ——
        // 这条"不改道"由 TransactionMessageE2ETest#ordinarySendIsNotDivertedByTheTransactionRegistration
        // 钉住，同一支用例里真发一条 201 作正对照（半消息计数必须涨）。
        this.transactionMessageProcessor = new TransactionMessageProcessor(this);
        this.remotingServer.registerProcessor(
                RequestCode.SEND_MESSAGE_V2, this.transactionMessageProcessor, this.sendMessageExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.END_TRANSACTION, this.transactionMessageProcessor, this.sendMessageExecutor);
        this.remotingServer.registerProcessor(
                RequestCode.CHECK_TRANSACTION_STATE, this.transactionMessageProcessor, this.adminBrokerExecutor);

        // 事务表跨重启：CommitLog.load() 已经在本方法之前跑完（initialize() 里），盘上的半消息与
        // 定论记录此刻读得回来，所以恢复点落在这里而不是 start()。
        int recovered = TransactionStateRecovery.recover(
                this.commitLog, this.transactionMessageProcessor.getTransactionStateManager());
        if (recovered > 0) {
            log.info("Transaction pending set rebuilt from commitlog: {} half message(s)", recovered);
        }

        // 回查调度：broker 侧唯一能自己走的那一半是"悬着不答的事务到次数自动回滚"。
        // 回查拿不到结论时（broker 进程内没有本地事务这个概念）一律按 UNKNOWN 计一次数，
        // 真正的结论由 producer 侧通过 END_TRANSACTION 送进来，或由 producer 主动来取（见 client）。
        this.transactionCheckService = new TransactionCheckService(
                this.transactionMessageProcessor.getTransactionStateManager(),
                new TransactionCheckService.TransactionCheckCallback() {
                    @Override
                    public TransactionState checkTransaction(
                            String transactionId, com.taotao.cloud.mq.common.message.Message halfMessage) {
                        return TransactionState.UNKNOWN;
                    }
                });
        // 回查得出的结论要兑现成"回投 + 定论记录"，用处理器那一份实现，不在这里另抄一遍
        this.transactionCheckService.setTransactionOutcomeApplier(
                new TransactionCheckService.TransactionOutcomeApplier() {
                    @Override
                    public void apply(String transactionId,
                                      TransactionState state) {
                        transactionMessageProcessor.resolveTransaction(transactionId, state);
                    }
                });
    }

    /**
     * 启动 Broker: 幂等启动, 实际启动动作仅在首次调用时执行.
     * <p>
     * 流程: 启动 CommitLog → 启动 Netty → 若配置了 NameServer 地址则启动注册客户端并注册.
     *
     * @throws Exception 启动过程中发生的异常
     */
    public void start() throws Exception {
        if (this.running.compareAndSet(false, true)) {
            // 启动CommitLog
            this.commitLog.start();

            // 启动 v2 增强组件
            this.pullRequestHoldService.start();
            this.scheduleMessageService.start();

            // 事务回查调度：扫"悬着没答"的集合，到 maxCheckTimes 判回滚。
            // 扫描节拍是运维参数，不是任何一条用例的判据 —— 用例一律直接驱动 checkOnce()，
            // 不在测试里等这个间隔。
            if (this.transactionCheckService != null) {
                this.transactionCheckService.start(
                        this.transactionMessageProcessor.getTransactionStateManager().getCheckIntervalMillis());
            }

            // v3 HA: 启动 HAService (Master 模式启动 push 线程, Slave 模式启动 sync 线程)
            this.haService.start();

            // 启动Netty服务端
            this.remotingServer.start();

            // 注册Broker到NameServer（如果配置了NameServer地址）
            if (this.brokerConfig.getNamesrvAddr() != null && !this.brokerConfig.getNamesrvAddr().isEmpty()) {
                this.startRegisterToNameServer();
            }

            // 启动周期 flush 任务 (5s 一次持久化 consumer offset)
            this.scheduledExecutorService.scheduleAtFixedRate(() -> {
                try {
                    this.consumerOffsetManager.flushIfNecessary();
                } catch (Exception e) {
                    log.warn("flush consumer offsets failed", e);
                }
            }, 5_000L, 5_000L, TimeUnit.MILLISECONDS);

            // v3 分布式: Slave 启动周期性同步任务 (默认 30s 一次)
            if (!this.brokerConfig.isMaster() && this.brokerConfig.getBrokerMasterAddr() != null) {
                this.slaveSynchronize = new SlaveSynchronize(this);
                this.slaveSynchronize.setMasterAddr(this.brokerConfig.getBrokerMasterAddr());
                this.slaveSynchronize.start();
                // 启动时立即同步一次, 然后周期同步
                this.slaveSynchronize.syncAll();
                final long interval = this.brokerConfig.getSyncAllIntervalMillis();
                this.scheduledExecutorService.scheduleAtFixedRate(() -> {
                    try {
                        this.slaveSynchronize.syncAll();
                    } catch (Exception e) {
                        log.warn("slave syncAll failed", e);
                    }
                }, interval, interval, TimeUnit.MILLISECONDS);
                log.info("SlaveSynchronize started, master={} interval={}ms",
                        this.brokerConfig.getBrokerMasterAddr(), interval);
            }

            log.info("Broker started successfully");
        }
    }

    /**
     * 启动一个独立的 Netty 客户端并向 NameServer 发起首次注册, 同时开启心跳周期.
     */
    private void startRegisterToNameServer() throws Exception {
        this.namesrvRemotingClient = new NettyRemotingClient(new com.taotao.cloud.mq.remoting.netty.NettyClientConfig());
        this.namesrvRemotingClient.start();

        // 立即注册一次
        registerBrokerAll(false, false);

        // 心跳周期
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                registerBrokerAll(false, false);
            } catch (Exception e) {
                log.warn("Broker heartbeat failed", e);
            }
        }, brokerHeartbeatIntervalMillis, brokerHeartbeatIntervalMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 立即重新注册到 NameServer (Topic 创建后调用, 使新 Topic 尽快可见).
     */
    public void reRegisterToNameServer() {
        try {
            registerBrokerAll(false, false);
        } catch (Exception e) {
            log.warn("reRegisterToNameServer failed", e);
        }
    }

    /**
     * 向所有配置的 NameServer 注册本 Broker, 维持心跳.
     *
     * @param checkOrderConfig 是否需要核对顺序消息配置 (MVP 忽略)
     * @param oneway           是否为 oneway 请求 (不等待响应, MVP 不支持)
     */
    private void registerBrokerAll(boolean checkOrderConfig, boolean oneway) {
        if (namesrvRemotingClient == null) {
            log.warn("namesrvRemotingClient not initialized, skip register");
            return;
        }
        String namesrvAddr = this.brokerConfig.getNamesrvAddr();
        String[] addrs = namesrvAddr.split(";");
        for (String addr : addrs) {
            String trimmed = addr.trim();
            if (trimmed.isEmpty()) { continue; }

            try {
                Channel channel = namesrvRemotingClient.getOrCreateChannel(trimmed);
                RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.REGISTER_BROKER);
                request.addExtField("clusterName", this.brokerConfig.getBrokerClusterName());
                request.addExtField("brokerName", this.brokerConfig.getBrokerName());
                request.addExtField("brokerAddr", this.brokerConfig.getAdvertisedAddr());
                request.addExtField("brokerId", String.valueOf(com.taotao.cloud.mq.common.BrokerData.MASTER_ID));
                request.addExtField("haServerAddr", "");

                // 上报本地 TopicConfig
                List<TopicConfig> topicConfigs = new java.util.ArrayList<>(
                        this.adminBrokerProcessor.getAllTopicConfigs().values());
                request.setBody(JsonCodec.encode(topicConfigs));

                RemotingCommand response = namesrvRemotingClient.invokeSync(channel, request, 3000);
                if (response != null && response.getCode() == com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode.SUCCESS
                        && response.getBody() != null && response.getBody().length > 0) {
                    try {
                        RegisterBrokerResult result = JsonCodec.decode(response.getBody(), RegisterBrokerResult.class);
                        log.info("Registered to namesrv={} masterAddr={} haAddr={}",
                                trimmed, result == null ? "?" : result.getMasterAddr(),
                                result == null ? "?" : result.getHaServerAddr());
                    } catch (Exception ignore) {
                        log.info("Registered to namesrv={} (decode body failed)", trimmed);
                    }
                } else {
                    log.warn("Register to namesrv={} returned code={}", trimmed,
                            response == null ? "null" : response.getCode());
                }
            } catch (Exception e) {
                log.warn("Register to namesrv={} failed: {}", trimmed, e.getMessage());
            }
        }
    }

    /**
     * 停止 Broker: 幂等停止, 停止 Netty, 关闭 CommitLog 与所有业务线程池.
     */
    public void shutdown() {
        if (this.running.compareAndSet(true, false)) {
            // 关闭 namesrv 客户端
            if (this.namesrvRemotingClient != null) {
                this.namesrvRemotingClient.shutdown();
            }

            // 停止Netty服务端
            this.remotingServer.shutdown();

            // 停止CommitLog
            this.commitLog.shutdown();

            // 停止 v2 增强组件
            if (this.pullRequestHoldService != null) {
                this.pullRequestHoldService.shutdown();
            }

            if (this.scheduleMessageService != null) {
                this.scheduleMessageService.shutdown();
            }

            if (this.transactionCheckService != null) {
                this.transactionCheckService.shutdown();
            }
            if (this.consumerOffsetManager != null) {
                this.consumerOffsetManager.shutdown();
            }

            if (this.topicConfigManager != null) {
                this.topicConfigManager.shutdown();
            }

            // v3 分布式: 关闭 SlaveSynchronize
            if (this.slaveSynchronize != null) {
                this.slaveSynchronize.shutdown();
            }

            // v3 HA: 关闭 HAService
            if (this.haService != null) {
                this.haService.shutdown();
            }

            // 关闭线程池
            this.scheduledExecutorService.shutdown();
            this.sendMessageExecutor.shutdown();
            this.pullMessageExecutor.shutdown();
            this.adminBrokerExecutor.shutdown();

            log.info("Broker shutdown successfully");
        }
    }

    // Getters

    /**
     * @return Broker 自身配置
     */
    public BrokerConfig getBrokerConfig() {
        return brokerConfig;
    }

    /**
     * @return 消息存储配置
     */
    public MessageStoreConfig getMessageStoreConfig() {
        return messageStoreConfig;
    }

    /**
     * @return CommitLog 实例
     */
    public CommitLog getCommitLog() {
        return commitLog;
    }

    /**
     * @return Consumer 位点管理器
     */
    public ConsumerOffsetManager getConsumerOffsetManager() {
        return consumerOffsetManager;
    }

    /**
     * @return 消费位点提交处理器（{@code UPDATE_CONSUMER_OFFSET} 的落点）
     */
    public ConsumerOffsetProcessor getConsumerOffsetProcessor() {
        return consumerOffsetProcessor;
    }

    /**
     * @return Pull 长轮询服务
     */
    /**
     * @return Pull 长轮询服务
     */
    public PullRequestHoldService getPullRequestHoldService() {
        return pullRequestHoldService;
    }

    /**
     * @return 事务消息处理器（半消息 / 二次确认 / 回查三个码的处理器，事务状态表挂在它身上）
     */
    public TransactionMessageProcessor getTransactionMessageProcessor() {
        return transactionMessageProcessor;
    }

    /**
     * @return 事务回查调度服务（测试用它的 checkOnce() 因果驱动一轮扫描）
     */
    public TransactionCheckService getTransactionCheckService() {
        return transactionCheckService;
    }

    /**
     * @return 延迟消息服务
     */
    public ScheduleMessageService getScheduleMessageService() {
        return scheduleMessageService;
    }

    /**
     * @return Broker OutAPI (Broker 互查接口处理器)
     */
    public BrokerOutAPI getBrokerOutAPI() {
        return brokerOutAPI;
    }

    /**
     * topic 配置后端（内存表 + 它的盘上镜像）, 由本控制器在 initialize() 里建好并加载.
     */
    public TopicConfigManager getTopicConfigManager() {
        return topicConfigManager;
    }

    /**
     * @return AdminBrokerProcessor
     */
    public AdminBrokerProcessor getAdminBrokerProcessor() {
        return adminBrokerProcessor;
    }

    /**
     * @return ConsumerOffset 版本号 (暴露给 BrokerOutAPI)
     */
    public com.taotao.cloud.mq.common.ha.DataVersion getConsumerOffsetDataVersion() {
        return consumerOffsetDataVersion;
    }

    /**
     * @return DelayOffset 版本号
     */
    public com.taotao.cloud.mq.common.ha.DataVersion getDelayOffsetDataVersion() {
        return delayOffsetDataVersion;
    }

    /**
     * @return SlaveSynchronize (Slave 模式下非 null).
     */
    public SlaveSynchronize getSlaveSynchronize() {
        return slaveSynchronize;
    }

    /**
     * @return HAService (主从同步服务).
     */
    public com.taotao.cloud.mq.plugin.store.ha.HAService getHaService() {
        return haService;
    }
}
