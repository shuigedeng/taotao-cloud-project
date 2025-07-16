/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.ccsr.core.remote.raft;

import com.alipay.sofa.jraft.*;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.CliServiceImpl;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.error.JRaftException;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;
import com.taotao.cloud.ccsr.api.event.GlobalEventBus;
import com.taotao.cloud.ccsr.common.config.CcsrConfig;
import com.taotao.cloud.ccsr.common.enums.RaftGroup;
import com.taotao.cloud.ccsr.common.exception.CcsrException;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.core.event.LeaderRefreshEvent;
import com.taotao.cloud.ccsr.core.remote.AbstractRpcServer;
import com.taotao.cloud.ccsr.core.remote.raft.helper.OptionsHelper;
import com.taotao.cloud.ccsr.core.remote.raft.helper.RaftHelper;
import com.taotao.cloud.ccsr.core.serializer.SerializeFactory;
import com.taotao.cloud.ccsr.core.serializer.Serializer;
import com.taotao.cloud.ccsr.spi.Join;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author shuigedeng
 */
@Join(order = 2, isSingleton = true)
public class RaftServer extends AbstractRpcServer {

    private RpcServer rpcServer;

    private CliClientServiceImpl cliClientService;

    private CliService cliService;

    private Configuration conf;

    private PeerId localPeerId;

    private NodeOptions nodeOptions;

    private RaftNodeMonitor nodeMonitor;

    private Serializer serializer;

    private final Map<String, RaftGroupTuple> multiRaftGroup = new ConcurrentHashMap<>();

    private final Map<String, Configuration> configurations = new ConcurrentHashMap<>();

    private final Map<String, List<ScheduledFuture<?>>> scheduledTasks = new ConcurrentHashMap<>();

    public record RaftGroupTuple(
            Node node, RaftGroupService raftGroupService, RaftStateMachine machine) {}

    public RaftServer() {
        // 必须要有，用于SPI加载初始化
    }

    public RaftServer(CcsrConfig config) {
        init(config);
    }

    @Override
    public int portOffset() {
        return config.getRaftConfig().getPortOffset();
    }

    @Override
    public void init(CcsrConfig config) {
        try {
            checkConfig(config);

            Log.info("Initializing Raft server with config: {}", config.getRaftConfig());
            this.config = config;
            this.nodeOptions = OptionsHelper.initNodeOptions(config.getRaftConfig());
            this.localPeerId = initPeerId();
            this.conf = initConfiguration();
            this.nodeMonitor =
                    new RaftNodeMonitor(this, config.getRaftConfig().getRpcRequestTimeout());
            this.serializer = SerializeFactory.getDefault();

            initCliServices(config);
        } catch (Exception e) {
            Log.error("Raft server initialization failed", e);
            throw new CcsrException("Raft initialization failure", e);
        }
    }

    @Override
    public void startServer() throws IOException {
        rpcServer = RaftHelper.initServer(this, localPeerId);
        if (!this.rpcServer.init(null)) {
            Log.error("Fail to start jraft server.");
            throw new JRaftException("Fail to start jraft server.");
        }

        startMultiRaftGroup();

        nodeMonitor.start();
    }

    @Override
    public void stopServer() {
        try {
            for (Map.Entry<String, RaftGroupTuple> entry : multiRaftGroup.entrySet()) {
                final RaftGroupTuple tuple = entry.getValue();
                tuple.node.shutdown();
                tuple.raftGroupService.shutdown();
            }
            cliService.shutdown();
            cliClientService.shutdown();
        } catch (Throwable t) {
            Log.error("There was an error in the raft node shutdown, cause: ", t);
        }
    }

    @Override
    public void await() {}

    @Override
    public void stopPreProcessor() {
        Log.info("========= The raft node is closing =========");
        clearScheduledTasks();
        shutdownNodeMonitor();
    }

    @Override
    public void stopPostProcessor() {
        cleanupMultiRaftGroup();
        Log.info("========= Raft node closed =========");
    }

    private void startMultiRaftGroup() {
        Arrays.stream(RaftGroup.values())
                .forEach(
                        groupEnum -> {
                            if (groupEnum.isEnable()) {
                                String groupId = groupEnum.getName();
                                try {
                                    RaftGroupTuple tuple = createRaftGroup(groupId);
                                    multiRaftGroup.put(groupId, tuple);
                                    scheduleLeaderRefresh(groupId);
                                } catch (Exception e) {
                                    Log.error("Failed to start Raft group {}", groupId, e);
                                }
                            }
                        });
    }

    private RaftGroupTuple createRaftGroup(String groupId) {
        CcsrConfig.RaftConfig raftConfig = config.getRaftConfig();

        // 深拷贝配置
        Configuration groupConf = new Configuration(conf);
        NodeOptions groupOptions = nodeOptions.copy();

        // 初始化存储路径
        RaftHelper.initDirectory(raftConfig.getRootPath(), groupId, localPeerId, groupOptions);

        // 初始化状态机
        RaftStateMachine stateMachine =
                new RaftStateMachine(this, serializer, configurations, groupId);
        groupOptions.setFsm(stateMachine);
        groupOptions.setInitialConf(groupConf);

        // 创建并启动服务
        RaftGroupService raftService =
                new RaftGroupService(groupId, localPeerId, groupOptions, rpcServer, true);
        Node node = raftService.start(false);
        stateMachine.setNode(node);

        // 更新路由表
        RouteTable.getInstance().updateConfiguration(groupId, groupConf);
        registerNodeToCluster(groupId, localPeerId, groupConf);

        return new RaftGroupTuple(node, raftService, stateMachine);
    }

    private void scheduleLeaderRefresh(String groupId) {
        // 1. 参数校验
        if (groupId == null || groupId.isEmpty()) {
            Log.error("Invalid groupId for leader refresh scheduling");
            return;
        }

        // 获取基础配置
        final long electionTimeoutMs = nodeOptions.getElectionTimeoutMs();

        // 计算调度参数（带随机抖动） 动态计算抖动范围
        int jitterRange =
                config.getRaftConfig().getRefreshJitterMax()
                        - config.getRaftConfig().getRefreshJitterMin();

        // 最终调度周期 = 2 * 选举超时 + 基础抖动 + 随机抖动
        Random random = new Random(System.currentTimeMillis());
        // long initialDelay = electionTimeoutMs + random.nextInt(4000); // 初始延迟：选举超时+0~5秒随机值
        // 固定为选举周期 * 3
        long initialDelay = electionTimeoutMs * 3;
        long period =
                electionTimeoutMs * 3L
                        + config.getRaftConfig().getRefreshJitterMin()
                        + random.nextInt(jitterRange);

        // 4. 创建定时任务
        ScheduledFuture<?> refreshTask =
                RaftHelper.getScheduleExecutor()
                        .scheduleAtFixedRate(
                                () -> {
                                    if (isShutdown) {
                                        return;
                                    }
                                    try {
                                        Log.print(
                                                ">>>>>>>>>>>>>>触发 leader 刷新【开始】 for group: %s, leader: %s",
                                                groupId, getLeader(groupId));
                                        refreshRouteTable(groupId);
                                        Log.print(
                                                ">>>>>>>>>>>>>>触发 leader 刷新【成功】 for group: %s, leader: %s",
                                                groupId, getLeader(groupId));

                                    } catch (Exception e) {
                                        Log.error(
                                                "Unexpected error during leader refresh for group {}",
                                                groupId,
                                                e);
                                    }
                                },
                                initialDelay,
                                period,
                                TimeUnit.MILLISECONDS);

        // 5. 注册任务以便后续管理
        scheduledTasks.computeIfAbsent(groupId, k -> new CopyOnWriteArrayList<>()).add(refreshTask);
        Log.print(
                "Scheduled leader refresh for group [{}]. Initial delay: {}ms, Period: {} ms",
                groupId,
                initialDelay,
                period);
    }

    void refreshRouteTable(String group) {
        try {
            RouteTable instance = RouteTable.getInstance();
            int rpcRequestTimeout = config.getRaftConfig().getRpcRequestTimeout();
            Status status = instance.refreshLeader(this.cliClientService, group, rpcRequestTimeout);
            if (!status.isOk()) {
                Log.print(
                        "不能去刷新【Leader】for group: %s, status is: %s, errorMsg: ",
                        group, status, status.getErrorMsg());
            }
            status = instance.refreshConfiguration(this.cliClientService, group, rpcRequestTimeout);
            if (!status.isOk()) {
                System.out.println(
                        "不能去刷新【Route Configuration】 for group : "
                                + group
                                + ", status is : "
                                + status
                                + "=====msg="
                                + status.getErrorMsg());
            }

            // 重要通知事件，告知当前Raft集群的leader是谁
            GlobalEventBus.getInstance().post(new LeaderRefreshEvent(getLeader(group)));

        } catch (Exception e) {
            Log.error("Fail to refresh raft metadata info for group : {}, error is : {}", group, e);
        }
    }

    private void registerNodeToCluster(String groupId, PeerId peer, Configuration conf) {
        ExecutorService regExecutor = Executors.newSingleThreadExecutor();
        regExecutor.execute(
                () -> {
                    int maxRetries = 10;
                    for (int i = 0; i < maxRetries && !isShutdown; i++) {
                        try {
                            if (cliService.getPeers(groupId, conf).contains(peer)) {
                                Log.info("Node {} already in cluster", peer);
                                return;
                            }

                            Status status = cliService.addPeer(groupId, conf, peer);
                            if (status.isOk()) {
                                Log.info("Successfully joined cluster group: " + groupId);
                                return;
                            }

                            Thread.sleep(calculateBackoff(i));
                        } catch (Exception e) {
                            Log.error("Registration attempt {} failed for group {}", i, groupId, e);
                        }
                    }
                });
    }

    private long calculateBackoff(int attempt) {
        return (long) Math.min(1000 * Math.pow(2, attempt), 30000);
    }

    private void cleanupMultiRaftGroup() {
        multiRaftGroup.clear();
    }

    private void clearScheduledTasks() {
        scheduledTasks.forEach(
                (group, tasks) -> {
                    tasks.forEach(
                            task -> {
                                // 发送中断
                                task.cancel(true);
                                if (!task.isDone()) {
                                    Log.warn(
                                            "Task {} for group {} not terminated immediately",
                                            task,
                                            group);
                                }
                            });
                });
        scheduledTasks.clear();
    }

    private void shutdownNodeMonitor() {
        nodeMonitor.shutdown();
    }

    private void checkConfig(CcsrConfig config) {
        if (config.getRaftConfig() == null) {
            throw new CcsrException("RaftConfig cannot be null");
        }
        if (CollectionUtils.isEmpty(config.getClusterAddress())) {
            throw new CcsrException("Cluster address must be configured");
        }
    }

    private PeerId initPeerId() throws SocketException {
        if (config.isLocalMode()) {
            return new PeerId("127.0.0.1", port());
        }
        return new PeerId(getRealIP(), port());
    }

    public static String getRealIP() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                // 跳过环回接口和未启用的接口
                continue;
            }
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                    // 返回 IPv4 地址
                    return addr.getHostAddress();
                }
            }
        }
        return "127.0.0.1";
    }

    private Configuration initConfiguration() {
        conf = new Configuration();
        if (CollectionUtils.isEmpty(config.getClusterAddress())) {
            throw new CcsrException("The cluster address cannot be empty");
        }

        // init raft group node
        NodeManager nodeManager = NodeManager.getInstance();
        for (String address : config.getClusterAddress()) {
            PeerId peerId = PeerId.parsePeer(address);
            conf.addPeer(peerId);
            nodeManager.addAddress(peerId.getEndpoint());
        }
        nodeOptions.setInitialConf(conf);
        return conf;
    }

    private void initCliServices(CcsrConfig config) {
        CliOptions cliOptions = OptionsHelper.initCliOptions(config.getRaftConfig());
        this.cliService = RaftServiceFactory.createAndInitCliService(cliOptions);
        this.cliClientService =
                (CliClientServiceImpl) ((CliServiceImpl) this.cliService).getCliClientService();

        // 增加关闭钩子确保资源释放
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    cliService.shutdown();
                                    cliClientService.shutdown();
                                }));
    }

    public Map<String, RaftGroupTuple> getMultiRaftGroup() {
        return multiRaftGroup;
    }

    public RaftGroupTuple findTupleByGroup(final String group) {
        return multiRaftGroup.get(group);
    }

    public PeerId getLeader(String group) {
        RaftGroupTuple tuple = multiRaftGroup.get(group);
        return tuple != null ? tuple.node().getLeaderId() : null;
    }

    public boolean isLeader(String group) {
        RaftGroupTuple tuple = multiRaftGroup.get(group);
        return tuple != null && tuple.node().isLeader();
    }

    public Configuration getCurrentConf(String groupId) {
        return configurations.getOrDefault(groupId, new Configuration());
    }
}
