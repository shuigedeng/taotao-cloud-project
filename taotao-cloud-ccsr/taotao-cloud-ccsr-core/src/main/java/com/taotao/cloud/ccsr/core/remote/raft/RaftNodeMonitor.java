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

import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.taotao.cloud.ccsr.common.log.Log;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shuigedeng
 */
public class RaftNodeMonitor {

    private final RaftServer server;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final long checkInterval;
    private final long maxSilentMs;
    private final int minClusterSize;

    private final Map<String, ReentrantLock> groupLock = new ConcurrentHashMap<>();

    public RaftNodeMonitor(RaftServer server, long electionTimeout) {
        this.server = Objects.requireNonNull(server);
        this.checkInterval = electionTimeout * 3; // 检查间隔为3倍选举超时
        this.maxSilentMs = electionTimeout * 5; // 静默时间为3倍选举超时
        this.minClusterSize = 2; // 最小集群大小为2
    }

    public void start() {
        scheduler.scheduleAtFixedRate(
                this::checkClusterHealth, checkInterval / 2, checkInterval, TimeUnit.MILLISECONDS);
        Log.print("[Node监控器] 已启动 (检查间隔={%s}ms, 最大静默时间={%s}ms)", checkInterval, maxSilentMs);
    }

    private void checkClusterHealth() {
        server.getMultiRaftGroup()
                .forEach(
                        (groupId, tuple) -> {
                            Node node = tuple.node();
                            // 新增Leader状态校验
                            if (!checkLeaderStatus(groupId, node)) {
                                return;
                            }

                            ReentrantLock lock =
                                    groupLock.computeIfAbsent(groupId, k -> new ReentrantLock());
                            if (!lock.tryLock()) {
                                Log.print("[%s] 前一次检查流程未完成，跳过本次检查", groupId);
                                return;
                            }

                            try {
                                Log.print(
                                        "[%s] 开始执行Raft组健康检查, node is: %s, leader is: %s",
                                        groupId, node, node.getLeaderId());
                                processGroupHealth(groupId, node);
                            } finally {
                                lock.unlock();
                            }
                        });
    }

    private void processGroupHealth(String groupId, Node node) {
        try {
            // 获取当前生效配置
            Configuration currentConf = server.getCurrentConf(groupId);
            List<PeerId> alivePeers = node.listAlivePeers();

            Log.print("[%s] 当前存活节点列表: %s", groupId, alivePeers);

            currentConf.getPeers().stream()
                    .filter(
                            peer -> {
                                // 排除自身
                                boolean isSelf = peer.equals(node.getNodeId().getPeerId());
                                if (isSelf) {
                                    Log.print("[%s] 跳过自身节点: %s", groupId, peer);
                                }
                                return !isSelf;
                            })
                    .filter(
                            peer -> {
                                // 筛选失效节点
                                boolean isAlive = alivePeers.contains(peer);
                                if (!isAlive) {
                                    Log.print("[%s] 检测到可能失效的节点: %s", groupId, peer);
                                }
                                return !isAlive;
                            })
                    .forEach(peer -> handleDeadNode(groupId, node, peer, currentConf));
        } catch (Exception e) {
            Log.error("[{}] 健康检查执行失败: errorMsg:{}", groupId, e.getMessage(), e);
        }
    }

    private void handleDeadNode(String groupId, Node node, PeerId peer, Configuration currentConf) {
        Log.print("[%s] 正在验证节点 %s 的失效状态", groupId, peer);
        if (node.listAlivePeers().contains(peer)) {
            Log.print("[%s] 节点 %s 已恢复", groupId, peer);
            return;
        }

        removeDeadNode(groupId, node, peer, currentConf);
    }

    private void removeDeadNode(
            String groupId, Node node, PeerId deadPeer, Configuration currentConf) {
        try {
            // 二次确认配置有效性:安全校验
            if (!currentConf.contains(deadPeer)) {
                Log.print("[%s] 节点 %s 已移除", groupId, deadPeer);
                return;
            }

            final int originalSize = currentConf.getPeers().size();
            final Configuration newConf = new Configuration(currentConf);

            if (!newConf.removePeer(deadPeer)) {
                Log.print("[%s] 节点 %s 不存在于配置中，无法移除", groupId, deadPeer);
                return;
            }

            final int newSize = newConf.getPeers().size();
            if (newSize < minClusterSize) {
                Log.print("[%s] 集群最小节点数限制（当前%s），取消移除", groupId, newConf.getPeers().size());
                return;
            }

            Log.print("[%s] 正在提交配置变更: 移除节点 %s", groupId, deadPeer);
            node.changePeers(
                    newConf,
                    status -> {
                        if (status.isOk()) {
                            Log.print("[%s] 配置变更成功", groupId);
                        } else {
                            Log.print("[%s] 变更失败: %s", groupId, status.getErrorMsg());
                        }
                    });

            Log.print("[%s] 原集群大小: %s，移除后大小: %s", groupId, originalSize, newSize);

        } catch (Exception e) {
            Log.error("[%s] 移除节点时发生异常: errorMsg: %s", groupId, e.getMessage(), e);
        }
    }

    // Leader状态检查
    private boolean checkLeaderStatus(String groupId, Node node) {
        if (node.isLeader()) {
            return true;
        }

        // 诊断无Leader的原因
        PeerId leaderId = node.getLeaderId();
        if (leaderId == null) {
            Log.print("[%s] 集群无Leader! 可能原因:", groupId);
            Log.print("  1. 节点未完成启动 (检查启动日志)");
            Log.print("  2. 网络分区 (检查节点间连通性)");
            Log.print("  3. 初始配置错误 (当前配置: %s)", node.getOptions().getInitialConf());
            Log.print(
                    "[%s] 当前节点不是Leader，跳过健康检查, node=%s, leader=%s",
                    groupId, node.getNodeId(), null);
        }
        return false;
    }

    public void shutdown() {
        this.shutdownAndAwaitTermination(5, TimeUnit.SECONDS);
        Log.info("[Node监控器] 节点监控已停止");
    }

    public void shutdownAndAwaitTermination(long timeout, TimeUnit unit) {
        // 1. 禁止新任务提交
        scheduler.shutdown();

        try {
            // 2. 等待现有任务完成（最多等待timeout时间）
            if (!scheduler.awaitTermination(timeout, unit)) {
                // 3. 强制终止未完成任务
                scheduler.shutdownNow();
                Log.warn("线程池未完全关闭，已强制终止");
            }
        } catch (InterruptedException e) {
            // 4. 处理中断：再次尝试终止
            scheduler.shutdownNow();
            Log.warn("关闭过程被中断，已强制终止线程池");
            // 保持中断状态
            Thread.currentThread().interrupt();
        }
    }
}
