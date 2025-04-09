package com.taotao.cloud.ccsr.core.remote.raft.processor;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.core.remote.raft.RaftServer;
import com.taotao.cloud.ccsr.core.serializer.Serializer;
import com.taotao.cloud.ccsr.common.enums.RaftGroup;


public class WriteRequestRpcProcessor extends AbstractRpcProcessor<MetadataWriteRequest> {

    public WriteRequestRpcProcessor(RaftServer server, Serializer serializer) {
        super(server, serializer, true);
    }

    @Override
    public void handleRequest(RpcContext ctx, MetadataWriteRequest request) {
        super.handleRequest(ctx, request);
    }

    @Override
    protected String extractRaftGroup(MetadataWriteRequest request) {
        return request.getRaftGroup();
    }

    @Override
    public String interest() {
        return MetadataWriteRequest.class.getName();
    }
}
