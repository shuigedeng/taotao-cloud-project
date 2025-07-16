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

package com.taotao.cloud.mq.consistency.raft1.server.rpc;

import com.alipay.remoting.BizContext;
import com.taotao.cloud.mq.consistency.raft1.common.constant.RpcRequestCmdConst;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.AppendLogRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.ClientKeyValueRequest;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.VoteRequest;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcRequest;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcResponse;
import com.taotao.cloud.mq.consistency.raft1.server.core.Node;
import com.taotao.cloud.mq.consistency.raft1.server.dto.PeerInfoDto;

/**
 * 默认实现
 */
public class DefaultRpcServer implements RpcServer {

    /**
     * 节点信息
     */
    private final Node node;

    /**
     * 服务启动的端口
     */
    private final int port;

    /**
     * rpc 服务实现
     */
    private com.alipay.remoting.rpc.RpcServer rpcServer;

    public DefaultRpcServer(Node node, int port) {
        this.node = node;
        this.port = port;
    }

    @Override
    public RpcResponse<?> handlerRequest(RpcRequest request) {
        // 根据客户端的请求，做具体的实现的路由

        final int cmd = request.getCmd();
        final Object reqObj = request.getObj();

        if (cmd == RpcRequestCmdConst.R_VOTE) {
            return new RpcResponse<>(node.handlerRequestVote((VoteRequest) reqObj));
        } else if (request.getCmd() == RpcRequestCmdConst.A_ENTRIES) {
            return new RpcResponse<>(node.handlerAppendEntries((AppendLogRequest) reqObj));
        } else if (request.getCmd() == RpcRequestCmdConst.CLIENT_REQ) {
            return new RpcResponse<>(node.handlerClientRequest((ClientKeyValueRequest) reqObj));
        } else if (request.getCmd() == RpcRequestCmdConst.CHANGE_CONFIG_REMOVE) {
            PeerInfoDto peerInfoDto = (PeerInfoDto) request.getObj();
            return new RpcResponse<>(node.removePeer(peerInfoDto));
        } else if (request.getCmd() == RpcRequestCmdConst.CHANGE_CONFIG_ADD) {
            PeerInfoDto peerInfoDto = (PeerInfoDto) request.getObj();
            return new RpcResponse<>(node.addPeer(peerInfoDto));
        }
        // todo 其他的支持

        return null;
    }

    @Override
    public void init() throws Throwable {
        rpcServer = new com.alipay.remoting.rpc.RpcServer(port, false, false);
        rpcServer.registerUserProcessor(
                new RaftUserProcessor<RpcRequest>() {
                    @Override
                    public Object handleRequest(BizContext bizContext, RpcRequest rpcRequest)
                            throws Exception {
                        return handlerRequest(rpcRequest);
                    }
                });

        // 服务启动
        rpcServer.startup();
    }

    @Override
    public void destroy() throws Throwable {
        rpcServer.shutdown();
    }
}
