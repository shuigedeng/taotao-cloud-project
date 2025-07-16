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

package com.taotao.cloud.mq.consistency.raft;

import com.taotao.cloud.mq.consistency.raft.client.ClientKVAck;
import com.taotao.cloud.mq.consistency.raft.client.ClientKVReq;
import com.taotao.cloud.mq.consistency.raft.common.NodeConfig;
import com.taotao.cloud.mq.consistency.raft.entity.AentryParam;
import com.taotao.cloud.mq.consistency.raft.entity.AentryResult;
import com.taotao.cloud.mq.consistency.raft.entity.RvoteParam;
import com.taotao.cloud.mq.consistency.raft.entity.RvoteResult;

/**
 *
 * @author shuigedeng
 */
public interface Node extends LifeCycle {

    /**
     * 设置配置文件.
     *
     * @param config
     */
    void setConfig(NodeConfig config);

    /**
     * 处理请求投票 RPC.
     *
     * @param param
     * @return
     */
    RvoteResult handlerRequestVote(RvoteParam param);

    /**
     * 处理附加日志请求.
     *
     * @param param
     * @return
     */
    AentryResult handlerAppendEntries(AentryParam param);

    /**
     * 处理客户端请求.
     *
     * @param request
     * @return
     */
    ClientKVAck handlerClientRequest(ClientKVReq request);

    /**
     * 转发给 leader 节点.
     * @param request
     * @return
     */
    ClientKVAck redirect(ClientKVReq request);
}
