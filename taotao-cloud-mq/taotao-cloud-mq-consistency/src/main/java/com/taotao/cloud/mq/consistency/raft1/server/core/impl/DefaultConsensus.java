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

package com.taotao.cloud.mq.consistency.raft1.server.core.impl;

import com.taotao.boot.common.utils.collection.ArrayUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.mq.consistency.raft1.common.constant.enums.NodeStatusEnum;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.AppendLogRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.VoteRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.resp.AppendLogResponse;
import com.taotao.cloud.mq.consistency.raft1.common.entity.resp.VoteResponse;
import com.taotao.cloud.mq.consistency.raft1.server.core.Consensus;
import com.taotao.cloud.mq.consistency.raft1.server.core.LogManager;
import com.taotao.cloud.mq.consistency.raft1.server.core.StateMachine;
import com.taotao.cloud.mq.consistency.raft1.server.dto.NodeInfoContext;
import com.taotao.cloud.mq.consistency.raft1.server.dto.PeerInfoDto;
import com.taotao.cloud.mq.consistency.raft1.server.support.peer.PeerManager;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认一致性实现
 * @since 1.0.0
 */
public class DefaultConsensus implements Consensus {

    private static final Logger log = LoggerFactory.getLogger(DefaultConsensus.class);

    /**
     * 选举锁
     */
    private final ReentrantLock voteLock = new ReentrantLock();

    /**
     * 附加日志锁
     */
    private final ReentrantLock appendLogLock = new ReentrantLock();

    /**
     * node 信息上下文
     */
    private final NodeInfoContext nodeInfoContext;

    public DefaultConsensus(NodeInfoContext nodeInfoContext) {
        this.nodeInfoContext = nodeInfoContext;
    }

    /**
     * 接收者实现：
     *      主要时先做一个抢占锁的动作，失败，则直接返回。
     *
     *      如果term 小于 currentTerm返回 false （5.2 节）
     *      如果 votedFor 为空或者就是 candidateId，并且候选人的日志至少和自己一样新，那么就投票给他（5.2 节，5.4 节）
     *
     * @param request 请求
     * @return 结果
     */
    @Override
    public VoteResponse vote(VoteRequest request) {
        try {
            VoteResponse response = new VoteResponse();
            if (!voteLock.tryLock()) {
                response.setTerm(nodeInfoContext.getCurrentTerm());
                response.setVoteGranted(false);
                return response;
            }

            // 对方任期没有自己新
            if (request.getTerm() < nodeInfoContext.getCurrentTerm()) {
                response.setTerm(nodeInfoContext.getCurrentTerm());
                response.setVoteGranted(false);
                return response;
            }

            final PeerManager peerManager = nodeInfoContext.getPeerManager();
            // (当前节点并没有投票 或者 已经投票过了且是对方节点) && 对方日志和自己一样新
            log.info(
                    "node {} current vote for [{}], param candidateId : {}",
                    peerManager.getSelf(),
                    nodeInfoContext.getVotedFor(),
                    request.getCandidateId());
            log.info(
                    "node {} current term {}, peer term : {}",
                    peerManager.getSelf(),
                    nodeInfoContext.getCurrentTerm(),
                    request.getTerm());

            final LogManager logManager = nodeInfoContext.getLogManager();
            if ((StringUtils.isEmpty(nodeInfoContext.getVotedFor())
                    || nodeInfoContext.getVotedFor().equals(request.getCandidateId()))) {
                if (logManager.getLast() != null) {
                    // 对方没有自己新
                    if (logManager.getLast().getTerm() > request.getLastLogTerm()) {
                        return VoteResponse.fail();
                    }
                    // 对方没有自己新
                    if (logManager.getLastIndex() > request.getLastLogIndex()) {
                        return VoteResponse.fail();
                    }
                }

                // 切换状态
                nodeInfoContext.setStatus(NodeStatusEnum.FOLLOWER);
                // 更新
                peerManager.setLeader(new PeerInfoDto(request.getCandidateId()));
                nodeInfoContext.setCurrentTerm(request.getTerm());
                nodeInfoContext.setVotedFor(request.getVotedFor());

                // 返回成功
                return new VoteResponse(nodeInfoContext.getCurrentTerm(), true);
            }

            return new VoteResponse(nodeInfoContext.getCurrentTerm(), false);
        } finally {
            voteLock.unlock();
        }
    }

    /**
     * 添加日志
     *
     * 接收者实现：
     *    如果 term 小于 currentTerm 就返回 false （5.1 节）
     *    如果日志在 prevLogIndex 位置处的日志条目的任期号和 prevLogTerm 不匹配，则返回 false （5.3 节）
     *    如果已经存在的日志条目和新的产生冲突（索引值相同但是任期号不同），删除这一条和之后所有的 （5.3 节）
     *    附加任何在已有的日志中不存在的条目
     *    如果 leaderCommit 大于 commitIndex，令 commitIndex 等于 leaderCommit 和 新日志条目索引值中较小的一个
     *
     * @param request 请求
     * @return 结果
     */
    @Override
    public AppendLogResponse appendLog(AppendLogRequest request) {
        AppendLogResponse appendLogResponse = new AppendLogResponse();
        final long currentTerm = nodeInfoContext.getCurrentTerm();
        appendLogResponse.setTerm(currentTerm);
        appendLogResponse.setSuccess(false);

        final long reqTerm = request.getTerm();
        try {
            // 1.1 抢占锁
            boolean tryLockFlag = appendLogLock.tryLock();
            if (!tryLockFlag) {
                log.warn("[AppendLog] tryLog false");
                return appendLogResponse;
            }
            // 1.2 是否够格？
            if (currentTerm > request.getTerm()) {
                log.warn("[AppendLog] currentTerm={} > reqTerm={}", currentTerm, reqTerm);
                return appendLogResponse;
            }

            // 2.1 基本信息更新 为什么这样设置？
            final PeerManager peerManager = nodeInfoContext.getPeerManager();
            final long nowMills = System.currentTimeMillis();
            nodeInfoContext.setElectionTime(nowMills);
            nodeInfoContext.setPreElectionTime(nowMills);
            nodeInfoContext.setStatus(NodeStatusEnum.FOLLOWER);
            nodeInfoContext.setCurrentTerm(reqTerm);
            peerManager.setLeader(new PeerInfoDto(request.getLeaderId()));
            // log.info("[AppendLog] update electionTime={}, status=Follower, term={}, leader={}",
            // nowMills, reqTerm, request.getLeaderId());

            // 3.1 处理心跳
            if (ArrayUtils.isEmpty(request.getEntries())) {
                handleHeartbeat(request);

                // 3.2 返回响应
                appendLogResponse.setTerm(reqTerm);
                appendLogResponse.setSuccess(true);
                return appendLogResponse;
            }

            return appendLogResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            appendLogLock.unlock();
        }
    }

    private void handleHeartbeat(AppendLogRequest request) {
        final long startTime = System.currentTimeMillis();
        // log.info("handleHeartbeat start req={}", request);

        final LogManager logManager = nodeInfoContext.getLogManager();

        // 处理 leader 已提交但未应用到状态机的日志

        // 下一个需要提交的日志的索引（如有）
        long nextCommit = nodeInfoContext.getCommitIndex() + 1;

        // 如果 leaderCommit > commitIndex，令 commitIndex 等于 leaderCommit 和 新日志条目索引值中较小的一个
        // 为什么？为了方便把缺失的日志，全部加上
        if (request.getLeaderCommit() > nodeInfoContext.getCommitIndex()) {
            int commitIndex = (int) Math.min(request.getLeaderCommit(), logManager.getLastIndex());
            nodeInfoContext.setCommitIndex(commitIndex);
            nodeInfoContext.setLastApplied(commitIndex);
        }

        final StateMachine stateMachine = nodeInfoContext.getStateMachine();
        while (nextCommit <= nodeInfoContext.getCommitIndex()) {
            // 提交之前的日志
            // todo: 状态机需要基于 kv 实现
            stateMachine.apply(logManager.read(nextCommit));

            nextCommit++;
        }

        long costTime = System.currentTimeMillis() - startTime;
        // log.info("handleHeartbeat start end, costTime={}", costTime);
    }
}
