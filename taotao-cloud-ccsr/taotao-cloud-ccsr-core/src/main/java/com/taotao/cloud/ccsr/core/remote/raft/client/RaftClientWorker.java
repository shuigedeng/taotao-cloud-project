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

package com.taotao.cloud.ccsr.core.remote.raft.client;

import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.alipay.sofa.jraft.util.Endpoint;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.common.log.Log;

/**
 * @author shuigedeng
 * @date 2025-03-26 09:35
 */
public class RaftClientWorker {

    private final RaftClientFactory factory;

    private final RpcClient rpcClient;

    public RaftClientWorker(RaftClientFactory factory) {
        if (!factory.initialize) {
            factory.init();
        }
        this.factory = factory;
        this.rpcClient = factory.getRpcClient();
    }

    // TODO 失败重试退避序列
    public Response invoke(Message request) throws RemotingException, InterruptedException {
        return invoke(request, 3000L);
    }

    public Response invoke(Message request, long timeout)
            throws RemotingException, InterruptedException {
        Log.print("===RaftClientWorker invoke to leader===>: %s", factory.getLeaderId());
        PeerId leaderId = factory.getLeaderId();
        Endpoint endpoint = new Endpoint(leaderId.getIp(), leaderId.getPort());
        return (Response) rpcClient.invokeSync(endpoint, request, timeout);
    }
}
