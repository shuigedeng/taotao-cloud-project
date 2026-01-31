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

import static com.taotao.cloud.ccsr.common.enums.ResponseCode.SYSTEM_ERROR;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.closure.ReadIndexClosure;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.alipay.sofa.jraft.util.BytesUtil;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.grpc.auto.ServiceInstance;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.exception.CcsrException;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.core.remote.raft.RaftClosure;
import com.taotao.cloud.ccsr.core.remote.raft.RaftServer;
import com.taotao.cloud.ccsr.core.remote.raft.handler.RequestDispatcher;
import com.taotao.cloud.ccsr.core.serializer.Serializer;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.SerializationException;

/**
 * AbstractRpcProcessor
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public abstract class AbstractRpcProcessor<T extends Message> implements RpcProcessor<T> {

    protected final RaftServer server;

    protected final Serializer serializer;

    private final boolean isWriteMode;

    private final RequestDispatcher dispatcher;

    public AbstractRpcProcessor( RaftServer server, Serializer serializer, boolean isWriteMode ) {
        this.server = server;
        this.serializer = serializer;
        this.isWriteMode = isWriteMode;
        this.dispatcher = RequestDispatcher.getInstance();
    }

    @Override
    public void handleRequest( RpcContext ctx, T request ) {
        try {
            Log.print("====接收到客户端请求====> ctx=%s request=%s", ctx, request);

            // 1. 提取分组信息
            String group = extractRaftGroup(request);
            RaftServer.RaftGroupTuple tuple = server.findTupleByGroup(group);
            if (tuple == null) {
                groupNotFound(ctx, group);
                return;
            }

            Node node = tuple.node();

            // 2. 检查集群是否有Leader
            PeerId leaderId = server.getLeader(group);
            if (leaderId == null) {
                noLeader(ctx);
                return;
            }

            // 3.读操作尝试线性一致读
            if (!isWriteMode && !server.isLeader(group)) {
                // handleReadRequest(node, ctx, request);
                Response response = handleReadRequest(node, request);
                if (response != null) {
                    ctx.sendResponse(response);
                    return;
                }
                // 否则继续执行下面的逻辑
            }

            // 4. 写操作必须由Leader处理
            if (!server.isLeader(group)) {
                redirect(ctx, leaderId);
                return;
            }
            //            if (isWriteMode && !server.isLeader(group)) {
            //                redirect(ctx, leaderId);
            //                return;
            //            }

            // 5. 序列化请求
            byte[] serialized;
            try {
                serialized = serializer.serialize(request);
            } catch (SerializationException e) {
                serializationError(ctx, e);
                return;
            }

            FailoverClosure closure =
                    new FailoverClosure() {
                        Response data;
                        Throwable ex;

                        @Override
                        public void run( Status status ) {
                            if (Objects.nonNull(ex)) {
                                ctx.sendResponse(
                                        ResponseHelper.error(
                                                SYSTEM_ERROR.getCode(), ex.getMessage()));
                            } else {
                                ctx.sendResponse(data);
                            }
                        }

                        @Override
                        public void setResponse( Response response ) {
                            this.data = response;
                        }

                        @Override
                        public void setThrowable( Throwable throwable ) {
                            this.ex = throwable;
                        }
                    };

            this.apply(node, request, serialized, closure);
        } catch (Exception e) {
            systemError(ctx, e);
        }
    }

    @Deprecated
    private void handleReadRequest( Node node, RpcContext ctx, T request ) {
        try {
            // 1. 查询本地状态机
            Log.print("查询模式，由本地状态机器执行，不向leader发起请求, request=%s", request);
            Response response = dispatcher.dispatch(request, request.getClass());
            ctx.sendResponse(response);
        } catch (Exception e) {
            systemError(ctx, e);
        }
    }

    private Response handleReadRequest( Node node, T request ) {
        CompletableFuture<Response> future = readIndex(node, request);
        try {
            return future.get();
        } catch (Exception e) {
            Log.error("handleReadRequest failed, errorMsg={}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 线性一致读
     */
    private CompletableFuture<Response> readIndex( Node node, T request ) {
        CompletableFuture<Response> future = new CompletableFuture<>();
        node.readIndex(
                BytesUtil.EMPTY_BYTES,
                new ReadIndexClosure() {
                    @Override
                    public void run( Status status, long index, byte[] reqCtx ) {
                        if (status.isOk()) {
                            try {
                                Log.print("查询模式，由本地状态机器执行，不向leader发起请求, request=%s", request);
                                Response response =
                                        dispatcher.dispatch(request, request.getClass());
                                future.complete(response);
                            } catch (Throwable t) {
                                future.completeExceptionally(
                                        new CcsrException(
                                                "The conformance protocol is temporarily unavailable for reading",
                                                t));
                            }
                            return;
                        }
                        Log.error(
                                "ReadIndex has error : {}, go to Leader read.",
                                status.getErrorMsg());
                    }
                });
        return future;
    }

    public interface FailoverClosure extends Closure {

        void setResponse( Response response );

        void setThrowable( Throwable throwable );
    }

    private void apply( Node node, Message msg, byte[] serialized, FailoverClosure closure ) {
        // 创建一个新的任务对象，用于封装要处理的数据和回调
        final Task task = new Task();
        task.setDone(
                new RaftClosure(
                        msg,
                        status -> {
                            RaftClosure.RaftStatus jRaftStatus = (RaftClosure.RaftStatus) status;
                            closure.setThrowable(jRaftStatus.getThrowable());
                            closure.setResponse(jRaftStatus.getResponse());
                            closure.run(jRaftStatus);
                        }));

        task.setData(ByteBuffer.wrap(serialized));
        node.apply(task);
    }

    // ---------- 抽象方法 ----------
    protected abstract String extractRaftGroup( T request );

    private void groupNotFound( RpcContext ctx, String group ) {
        Log.print("===handleRequest===> ctx=%s, group=%s", ctx, group);
        ctx.sendResponse(
                ResponseHelper.error(
                        ResponseCode.GROUP_NOT_FOUND.getCode(), "Raft group not found: " + group));
    }

    private void noLeader( RpcContext ctx ) {
        Log.print("===sendNoLeaderError方法执行===> ctx=%s", ctx);
        ctx.sendResponse(
                ResponseHelper.error(
                        ResponseCode.NO_LEADER.getCode(), "【Cluster has no leader currently】"));
    }

    private void redirect( RpcContext ctx, PeerId leaderId ) {
        String leaderAddr = leaderId.getIp() + ":" + leaderId.getPort();

        // TODO 是否需要重定向时返回 namespace/group/tag 等信息?
        Log.print("===Redirect===> ctx=%s leaderId=%s", ctx, leaderId);
        Any pack =
                Any.pack(
                        ServiceInstance.newBuilder()
                                .setHost(leaderId.getIp())
                                .setPort(leaderId.getPort())
                                .setAddress(leaderAddr)
                                .build());
        ctx.sendResponse(
                ResponseHelper.error(
                        ResponseCode.REDIRECT.getCode(), "Request redirect to leader node", pack));
    }

    private void serializationError( RpcContext ctx, SerializationException e ) {
        ctx.sendResponse(
                ResponseHelper.error(
                        ResponseCode.SERIALIZATION_ERROR.getCode(),
                        "Serialization failed: " + e.getMessage()));
    }

    protected void systemError( RpcContext ctx, Exception e ) {
        ctx.sendResponse(
                ResponseHelper.error(
                        SYSTEM_ERROR.getCode(), "Internal server error: " + e.getMessage()));
    }
}
