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

package com.taotao.cloud.mq.consistency.raft1.server.support.vote;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.mq.consistency.raft1.common.constant.RpcRequestCmdConst;
import com.taotao.cloud.mq.consistency.raft1.common.constant.enums.NodeStatusEnum;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.VoteRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.dto.LogEntry;
import com.taotao.cloud.mq.consistency.raft1.common.entity.resp.VoteResponse;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcClient;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcRequest;
import com.taotao.cloud.mq.consistency.raft1.server.core.LogManager;
import com.taotao.cloud.mq.consistency.raft1.server.core.StateMachine;
import com.taotao.cloud.mq.consistency.raft1.server.dto.NodeInfoContext;
import com.taotao.cloud.mq.consistency.raft1.server.dto.PeerInfoDto;
import com.taotao.cloud.mq.consistency.raft1.server.support.peer.PeerManager;
import com.taotao.cloud.mq.consistency.raft1.server.support.replication.IRaftReplication;
import com.taotao.cloud.mq.consistency.raft1.server.util.InnerFutureList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 投定时调度
 *
 * 1. 在转变成候选人后就立即开始选举过程
 * 自增当前的任期号（currentTerm）
 * 给自己投票
 * 重置选举超时计时器
 * 发送请求投票的 RPC 给其他所有服务器
 * 2. 如果接收到大多数服务器的选票，那么就变成领导人
 * 3. 如果接收到来自新的领导人的附加日志 RPC，转变成跟随者
 * 4. 如果选举过程超时，再次发起一轮选举
 *
 * @since 1.0.0
 */
public class VoteTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(VoteTask.class);

    private final NodeInfoContext nodeInfoContext;

    public VoteTask(NodeInfoContext nodeInfoContext) {
        this.nodeInfoContext = nodeInfoContext;
    }

    @Override
    public void run() {
        try {
            // 1. leader 不参与选举
            if (NodeStatusEnum.LEADER.equals(nodeInfoContext.getStatus())) {
                log.warn("[Raft] >>>>>>>>>>>>>>> current status is leader, ignore vote.");
                return;
            }

            // 2. 判断两次的时间间隔
            boolean isFitElectionTime = isFitElectionTime();
            if (!isFitElectionTime) {
                return;
            }

            // 3. 开始准备选举
            // 3.1 状态候选
            nodeInfoContext.setStatus(NodeStatusEnum.CANDIDATE);
            log.info(
                    "Node will become CANDIDATE and start election leader, info={}",
                    nodeInfoContext.getPeerManager().getSelf());
            // 3.2 上一次的选票时间
            nodeInfoContext.setPreElectionTime(getPreElectionTime());
            // 3.3 term 自增
            nodeInfoContext.setCurrentTerm(nodeInfoContext.getCurrentTerm() + 1);
            // 3.4 给自己投票
            final PeerManager peerManager = nodeInfoContext.getPeerManager();
            final String selfAddress = peerManager.getSelf().getAddress();
            nodeInfoContext.setVotedFor(selfAddress);

            // 通知其他除了自己的节点（暂时使用同步，后续应该优化为异步线程池，这里为了简化流程）
            // TODO: 需要考虑超时的情况
            final List<PeerInfoDto> allPeerList = peerManager.getList();
            List<VoteResponse> voteResponseList = new ArrayList<>();
            for (PeerInfoDto remotePeer : allPeerList) {
                // 跳过自己
                if (remotePeer.getAddress().equals(selfAddress)) {
                    continue;
                }

                // 远程投票
                try {
                    VoteResponse response =
                            voteSelfToRemote(remotePeer, selfAddress, nodeInfoContext);
                    voteResponseList.add(response);
                } catch (Exception e) {
                    log.error("voteSelfToRemote meet ex, remotePeer={}", remotePeer, e);
                }
            }

            // 3.5 判断选举结果
            int voteSuccessTotal = calcVoteSuccessVote(voteResponseList, nodeInfoContext);
            // 如果投票期间,有其他服务器发送 appendEntry , 就可能变成 follower ,这时,应该停止.
            if (NodeStatusEnum.FOLLOWER.equals(nodeInfoContext.getStatus())) {
                log.info("[Raft] 如果投票期间,有其他服务器发送 appendEntry, 就可能变成 follower, 这时,应该停止.");
                return;
            }

            // 是否超过一半？加上自己，等于也行。自己此时没算
            if (voteSuccessTotal >= peerManager.getList().size() / 2) {
                log.warn(
                        "[Raft] >>>>>>>>>>>>>>>>>> leader node vote success become leader {}, voteSuccessTotal={}, total={}",
                        selfAddress,
                        voteSuccessTotal,
                        peerManager.getList().size());
                nodeInfoContext.setStatus(NodeStatusEnum.LEADER);
                peerManager.setLeader(peerManager.getSelf());
                // 投票人信息清空
                nodeInfoContext.setVotedFor("");
                // 成主之后做的一些事情
                afterBeingLeader(nodeInfoContext);
            } else {
                // 投票人信息清空 重新选举
                nodeInfoContext.setVotedFor("");
                log.warn("vote failed, wait next vote");
            }

            // 再次更新选举时间 为什么？？？？
            nodeInfoContext.setPreElectionTime(getPreElectionTime());
        } catch (Exception e) {
            log.error("Vote meet ex", e);
        }
    }

    /**
     * 随机  获取上一次的选举时间
     * @return 时间
     */
    private long getPreElectionTime() {
        return System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150;
    }

    /**
     * 初始化所有的 nextIndex 值为自己的最后一条日志的 index + 1.
     * 如果下次 RPC 时, 跟随者和leader 不一致,就会失败.
     * 那么 leader 尝试递减 nextIndex 并进行重试.最终将达成一致.
     *
     * todo: 这一段感觉和处理的基本逻辑有很多重复的地方，后续可以考虑简化
     *
     * @param nodeInfoContext 上下文
     */
    private void afterBeingLeader(NodeInfoContext nodeInfoContext) {
        nodeInfoContext.setNextIndexes(new ConcurrentHashMap<>());
        nodeInfoContext.setMatchIndexes(new ConcurrentHashMap<>());

        final PeerManager peerManager = nodeInfoContext.getPeerManager();
        final LogManager logManager = nodeInfoContext.getLogManager();

        for (PeerInfoDto peer : peerManager.getPeersWithOutSelf()) {
            nodeInfoContext.getNextIndexes().put(peer, logManager.getLastIndex() + 1);
            nodeInfoContext.getMatchIndexes().put(peer, 0L);
        }

        // 创建[空日志]并提交，用于处理前任领导者未提交的日志
        LogEntry logEntry = new LogEntry(null, nodeInfoContext.getCurrentTerm(), null);

        // 预提交到本地日志, TODO 预提交
        logManager.write(logEntry);
        log.info(
                "write logModule success, logEntry info : {}, log index : {}",
                logEntry,
                logEntry.getIndex());

        final AtomicInteger success = new AtomicInteger(0);

        List<Future<Boolean>> futureList = new ArrayList<>();

        int count = 0;
        //  复制到其他机器
        final IRaftReplication raftReplication = nodeInfoContext.getRaftReplication();
        for (PeerInfoDto peer : peerManager.getPeersWithOutSelf()) {
            // TODO check self and RaftThreadPool
            count++;
            // 并行发起 RPC 复制.
            futureList.add(raftReplication.replication(nodeInfoContext, peer, logEntry));
        }

        CountDownLatch latch = new CountDownLatch(futureList.size());
        List<Boolean> resultList = InnerFutureList.getRPCAppendResult(futureList, latch);

        try {
            latch.await(4000, MILLISECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        for (Boolean aBoolean : resultList) {
            if (aBoolean) {
                success.incrementAndGet();
            }
        }

        // 如果存在一个满足N > commitIndex的 N，并且大多数的matchIndex[i] ≥ N成立，
        // 并且log[N].term == currentTerm成立，那么令 commitIndex 等于这个 N （5.3 和 5.4 节）
        List<Long> matchIndexList = new ArrayList<>(nodeInfoContext.getMatchIndexes().values());
        // 小于 2, 没有意义
        int median = 0;
        if (matchIndexList.size() >= 2) {
            Collections.sort(matchIndexList);
            median = matchIndexList.size() / 2;
        }
        Long N = matchIndexList.get(median);
        if (N > nodeInfoContext.getCommitIndex()) {
            LogEntry entry = logManager.read(N);
            if (entry != null && entry.getTerm() == nodeInfoContext.getCurrentTerm()) {
                nodeInfoContext.setCommitIndex(N);
            }
        }

        //  响应客户端(成功一半)
        final StateMachine stateMachine = nodeInfoContext.getStateMachine();
        if (success.get() >= (count / 2)) {
            // 更新
            nodeInfoContext.setCommitIndex(logEntry.getIndex());
            //  应用到状态机
            stateMachine.apply(logEntry);
            nodeInfoContext.setLastApplied(nodeInfoContext.getCommitIndex());

            log.info("success apply local state machine,  logEntry info : {}", logEntry);
        } else {
            // 回滚已经提交的日志
            logManager.removeOnStartIndex(logEntry.getIndex());
            log.warn("fail apply local state  machine,  logEntry info : {}", logEntry);

            // 无法提交空日志，让出领导者位置
            log.warn("node {} becomeLeaderToDoThing fail ", peerManager.getSelf());
            nodeInfoContext.setStatus(NodeStatusEnum.FOLLOWER);
            peerManager.setLeader(null);
            nodeInfoContext.setVotedFor("");
        }
    }

    /**
     * 计算投票给自己的数量
     * 1. 同时需要更新自己的任期
     * @param voteResponseList 结果列表
     * @param nodeInfoContext 基本信息
     * @return 结果
     */
    private int calcVoteSuccessVote(
            List<VoteResponse> voteResponseList, final NodeInfoContext nodeInfoContext) {
        int sum = 0;

        for (VoteResponse response : voteResponseList) {
            if (response == null) {
                log.error("response is null");
                continue;
            }

            // 投票给自己
            boolean isVoteGranted = response.isVoteGranted();
            if (isVoteGranted) {
                sum++;
            } else {
                // 更新自己的任期.
                long resTerm = response.getTerm();
                if (resTerm >= nodeInfoContext.getCurrentTerm()) {
                    nodeInfoContext.setCurrentTerm(resTerm);
                    log.info(
                            "[Raft] update current term from vote res={}",
                            JSON.toJSONString(response));
                }
            }
        }

        log.info("calcVoteSuccessVote sum={}", sum);
        return sum;
    }

    private VoteResponse voteSelfToRemote(
            PeerInfoDto remotePeer,
            final String selfAddress,
            final NodeInfoContext nodeInfoContext) {
        final LogManager logManager = nodeInfoContext.getLogManager();
        final PeerManager peerManager = nodeInfoContext.getPeerManager();

        // 当前最后的 term
        long lastTerm = 0L;
        LogEntry last = logManager.getLast();
        if (last != null) {
            lastTerm = last.getTerm();
        }

        VoteRequest param = new VoteRequest();
        param.setTerm(nodeInfoContext.getCurrentTerm());
        param.setVotedFor(peerManager.getSelf().getAddress());
        param.setCandidateId(peerManager.getSelf().getAddress());
        long logIndex = logManager.getLastIndex() == null ? 0 : logManager.getLastIndex();
        param.setLastLogIndex(logIndex);
        param.setLastLogTerm(lastTerm);

        RpcRequest request = new RpcRequest();
        request.setCmd(RpcRequestCmdConst.R_VOTE);
        request.setObj(param);
        request.setUrl(remotePeer.getAddress());

        // 发送
        final RpcClient rpcClient = nodeInfoContext.getRpcClient();
        // 请求超时时间，后续可以考虑配置化
        VoteResponse voteResponse = rpcClient.send(request, 30);
        return voteResponse;
    }

    /**
     * 是否满足选举的时间
     *
     * @return 结果
     */
    private boolean isFitElectionTime() {
        long electionTime = nodeInfoContext.getElectionTime();
        long preElectionTime = nodeInfoContext.getPreElectionTime();

        // 基于 RAFT 的随机时间,解决冲突.
        // 这里不会导致这个值越来越大吗？？？
        long randomElectionTime = electionTime + ThreadLocalRandom.current().nextInt(50);
        nodeInfoContext.setElectionTime(randomElectionTime);

        long current = System.currentTimeMillis();
        if (current - preElectionTime < randomElectionTime) {
            log.warn("[Raft] current electionTime is not fit, ignore handle");

            return false;
        }
        return true;
    }
}
