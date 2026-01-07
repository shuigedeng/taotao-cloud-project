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

package com.taotao.cloud.mq.consistency.raft1.server.core;

import com.taotao.cloud.mq.consistency.raft1.common.core.LifeCycle;
import com.taotao.cloud.mq.consistency.raft1.common.entity.dto.NodeConfig;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.AppendLogRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.ClientKeyValueRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.VoteRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.resp.AppendLogResponse;
import com.taotao.cloud.mq.consistency.raft1.common.entity.resp.ClientKeyValueResponse;
import com.taotao.cloud.mq.consistency.raft1.common.entity.resp.VoteResponse;
import com.taotao.cloud.mq.consistency.raft1.server.support.peer.ClusterPeerManager;

/**
 * 节点
 *
 * 首先，一个 Node 肯定需要配置文件，所以有一个 setConfig 接口，
 *
 * 然后，肯定需要处理“请求投票”和“附加日志”，
 *
 * 同时，还需要接收用户，也就是客户端的请求（不然数据从哪来？），
 *
 * 所以有 handlerClientRequest 接口，最后，考虑到灵活性，
 *
 * 我们让每个节点都可以接收客户端的请求，但 follower 节点并不能处理请求，所以需要重定向到 leader 节点，因此，我们需要一个重定向接口。
 *
 */
public interface Node extends LifeCycle, ClusterPeerManager {

    /**
     * 设置配置文件.
     *
     * @param config 配置
     */
    void setConfig(NodeConfig config);

    /**
     * 处理请求投票 RPC.
     *
     * @param param 请求
     * @return 结果
     */
    VoteResponse handlerRequestVote(VoteRequest param);

    /**
     * 处理附加日志请求.
     *
     * @param param 请求
     * @return v结果
     */
    AppendLogResponse handlerAppendEntries(AppendLogRequest param);

    /**
     * 处理客户端请求.
     *
     * @param request 请求
     * @return 结果
     */
    ClientKeyValueResponse handlerClientRequest(ClientKeyValueRequest request);

    /**
     * 转发给 leader 节点.
     *
     * 如果当前节点是从，当时接收到了写请求，那么应该转发处理
     *
     * @param request 请求
     * @return 结果
     */
    ClientKeyValueResponse redirect(ClientKeyValueRequest request);
}
