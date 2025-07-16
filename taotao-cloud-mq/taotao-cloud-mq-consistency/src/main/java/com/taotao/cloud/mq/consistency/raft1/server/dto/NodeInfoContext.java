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

package com.taotao.cloud.mq.consistency.raft1.server.dto;

import com.taotao.cloud.mq.consistency.raft1.common.constant.enums.NodeStatusEnum;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcClient;
import com.taotao.cloud.mq.consistency.raft1.server.core.Consensus;
import com.taotao.cloud.mq.consistency.raft1.server.core.LogManager;
import com.taotao.cloud.mq.consistency.raft1.server.core.StateMachine;
import com.taotao.cloud.mq.consistency.raft1.server.rpc.RpcServer;
import com.taotao.cloud.mq.consistency.raft1.server.support.peer.PeerManager;
import com.taotao.cloud.mq.consistency.raft1.server.support.replication.IRaftReplication;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 节点信息上下文
 * @since 1.0.0
 */
public class NodeInfoContext {

    /**
     * 一致性实现
     * @since 1.0.0
     */
    private Consensus consensus;

    /**
     * 节点信息管理
     */
    private PeerManager peerManager;

    /** 日志条目集；每一个条目包含一个用户状态机执行的指令，和收到时的任期号 */
    private LogManager logManager;

    /**
     * rpc 服务端
     */
    private RpcServer rpcServer;

    /**
     * rpc 客户端
     */
    private RpcClient rpcClient;

    /**
     * 状态机
     */
    private StateMachine stateMachine;

    /**
     * 运行状态
     */
    private volatile boolean running = false;

    /**
     * 节点状态
     */
    private volatile NodeStatusEnum status = NodeStatusEnum.FOLLOWER;

    /** 选举时间间隔基数 */
    private volatile long electionTime = 15 * 1000;

    /** 上一次选举时间 */
    private volatile long preElectionTime = 0;

    /** 服务器最后一次知道的任期号（初始化为 0，持续递增） */
    private volatile long currentTerm = 0;

    /** 在当前获得选票的候选人的 Id */
    private volatile String votedFor;

    /** 已知的最大的已经被提交的日志条目的索引值 */
    private volatile long commitIndex;

    /** 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增) */
    private volatile long lastApplied = 0;

    /* ========== 在领导人里经常改变的(选举后重新初始化) ================== */

    /** 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一） */
    private Map<PeerInfoDto, Long> nextIndexes = new ConcurrentHashMap<>();

    /** 对于每一个服务器，已经复制给他的日志的最高索引值 */
    private Map<PeerInfoDto, Long> matchIndexes = new ConcurrentHashMap<>();

    /**
     * 备份策略
     */
    private IRaftReplication raftReplication;

    public IRaftReplication getRaftReplication() {
        return raftReplication;
    }

    public void setRaftReplication(IRaftReplication raftReplication) {
        this.raftReplication = raftReplication;
    }

    public Map<PeerInfoDto, Long> getNextIndexes() {
        return nextIndexes;
    }

    public void setNextIndexes(Map<PeerInfoDto, Long> nextIndexes) {
        this.nextIndexes = nextIndexes;
    }

    public Map<PeerInfoDto, Long> getMatchIndexes() {
        return matchIndexes;
    }

    public void setMatchIndexes(Map<PeerInfoDto, Long> matchIndexes) {
        this.matchIndexes = matchIndexes;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public RpcServer getRpcServer() {
        return rpcServer;
    }

    public void setRpcServer(RpcServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    public Consensus getConsensus() {
        return consensus;
    }

    public void setConsensus(Consensus consensus) {
        this.consensus = consensus;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public long getLastApplied() {
        return lastApplied;
    }

    public void setLastApplied(long lastApplied) {
        this.lastApplied = lastApplied;
    }

    public long getCommitIndex() {
        return commitIndex;
    }

    public void setCommitIndex(long commitIndex) {
        this.commitIndex = commitIndex;
    }

    public RpcClient getRpcClient() {
        return rpcClient;
    }

    public void setRpcClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public LogManager getLogManager() {
        return logManager;
    }

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    public PeerManager getPeerManager() {
        return peerManager;
    }

    public void setPeerManager(PeerManager peerManager) {
        this.peerManager = peerManager;
    }

    public long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public String getVotedFor() {
        return votedFor;
    }

    public void setVotedFor(String votedFor) {
        this.votedFor = votedFor;
    }

    public long getElectionTime() {
        return electionTime;
    }

    public void setElectionTime(long electionTime) {
        this.electionTime = electionTime;
    }

    public long getPreElectionTime() {
        return preElectionTime;
    }

    public void setPreElectionTime(long preElectionTime) {
        this.preElectionTime = preElectionTime;
    }

    public NodeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(NodeStatusEnum status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NodeInfoContext{"
                + "consensus="
                + consensus
                + ", peerManager="
                + peerManager
                + ", logManager="
                + logManager
                + ", rpcServer="
                + rpcServer
                + ", rpcClient="
                + rpcClient
                + ", stateMachine="
                + stateMachine
                + ", running="
                + running
                + ", status="
                + status
                + ", electionTime="
                + electionTime
                + ", preElectionTime="
                + preElectionTime
                + ", currentTerm="
                + currentTerm
                + ", votedFor='"
                + votedFor
                + '\''
                + ", commitIndex="
                + commitIndex
                + ", lastApplied="
                + lastApplied
                + ", nextIndexes="
                + nextIndexes
                + ", matchIndexes="
                + matchIndexes
                + ", raftReplication="
                + raftReplication
                + '}';
    }
}
