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

package com.taotao.cloud.ccsr.core.remote.raft.processor;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.core.remote.raft.RaftServer;
import com.taotao.cloud.ccsr.core.serializer.Serializer;

public class ReadRequestRpcProcessor extends AbstractRpcProcessor<MetadataReadRequest> {

    public ReadRequestRpcProcessor(RaftServer server, Serializer serializer) {
        super(server, serializer, false);
    }

    @Override
    public void handleRequest(RpcContext ctx, MetadataReadRequest request) {
        super.handleRequest(ctx, request);
    }

    @Override
    protected String extractRaftGroup(MetadataReadRequest request) {
        return request.getRaftGroup();
    }

    @Override
    public String interest() {
        return MetadataReadRequest.class.getName();
    }
}
