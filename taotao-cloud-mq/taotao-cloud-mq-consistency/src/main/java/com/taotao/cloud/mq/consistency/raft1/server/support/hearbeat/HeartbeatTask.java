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

package com.taotao.cloud.mq.consistency.raft1.server.support.hearbeat;

import com.taotao.cloud.mq.consistency.raft1.common.constant.RpcRequestCmdConst;
import com.taotao.cloud.mq.consistency.raft1.common.constant.enums.NodeStatusEnum;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.AppendLogRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.resp.AppendLogResponse;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcClient;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcRequest;
import com.taotao.cloud.mq.consistency.raft1.server.dto.NodeInfoContext;
import com.taotao.cloud.mq.consistency.raft1.server.dto.PeerInfoDto;
import com.taotao.cloud.mq.consistency.raft1.server.support.peer.PeerManager;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳调度任务
 * @since 1.0.0
 */
public class HeartbeatTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(HeartbeatTask.class);

    private final NodeInfoContext nodeInfoContext;

    public HeartbeatTask(NodeInfoContext nodeInfoContext) {
        this.nodeInfoContext = nodeInfoContext;
    }

    /**
     * - 必须满足 5 秒的时间间隔。
     * （其实这个应该调度间隔控制，方法判断感觉比较奇怪，如何二次刚好没达到，会导致下一次时间间隔过长）
     *
     * - 并发的向其他 follower 节点发送心跳。
     * - 心跳参数包括自身的 ID，自身的 term，以便让对方检查 term，防止网络分区导致的脑裂。
     * - 如果任意 follower 的返回值的 term 大于自身，说明自己分区了，那么需要变成 follower，并更新自己的 term。然后重新发起选举。
     */
    @Override
    public void run() {
        try {
            final PeerManager peerManager = nodeInfoContext.getPeerManager();
            log.info("HEARTBEAT >>>>>>>>>>>>>>>>>>>>>>>> START leader={}", peerManager.getLeader());
            final NodeStatusEnum nodeStatus = nodeInfoContext.getStatus();
            if (!NodeStatusEnum.LEADER.equals(nodeStatus)) {
                // log.info("HEARTBEAT >>>>>>>>>>>>>>>>>>>>>>>> Only leader node need heartbeat,
                // currentStatus={}", nodeStatus);
                return;
            }

            // 时间间隔控制，个人觉得没必要
            // 通知 follower
            List<PeerInfoDto> peerInfoList = peerManager.getList();
            final PeerInfoDto selfInfo = peerManager.getSelf();

            final RpcClient rpcClient = nodeInfoContext.getRpcClient();
            final long currentTerm = nodeInfoContext.getCurrentTerm();
            for (PeerInfoDto remotePeer : peerInfoList) {
                // 跳过自己
                if (remotePeer.getAddress().equals(selfInfo.getAddress())) {
                    continue;
                }

                AppendLogRequest appendLogRequest = new AppendLogRequest();
                appendLogRequest.setLeaderId(selfInfo.getAddress());
                // 这有什么用？ 通知到对方，为什么要设置对方的标识？
                appendLogRequest.setServerId(remotePeer.getAddress());
                appendLogRequest.setTerm(nodeInfoContext.getCurrentTerm());
                appendLogRequest.setLeaderCommit(currentTerm);
                appendLogRequest.setLeaderCommit(nodeInfoContext.getCommitIndex());

                RpcRequest request = new RpcRequest();
                request.setCmd(RpcRequestCmdConst.R_VOTE);
                request.setObj(appendLogRequest);
                request.setUrl(remotePeer.getAddress());

                AppendLogResponse appendLogResponse = rpcClient.send(request);

                // 结果的处理
                final long term = appendLogResponse.getTerm();
                if (term > currentTerm) {
                    log.error(
                            "self will become follower, he's term : {}, my term : {}",
                            term,
                            currentTerm);
                    nodeInfoContext.setCurrentTerm(term);
                    nodeInfoContext.setVotedFor("");
                    nodeInfoContext.setStatus(NodeStatusEnum.FOLLOWER);
                }
            }
        } catch (Exception e) {
            log.error("HEARTBEAT meet ex", e);
        }

        //        log.info(">>>>>>>>>>>>>>> [Heartbeat] task end");
    }
}
