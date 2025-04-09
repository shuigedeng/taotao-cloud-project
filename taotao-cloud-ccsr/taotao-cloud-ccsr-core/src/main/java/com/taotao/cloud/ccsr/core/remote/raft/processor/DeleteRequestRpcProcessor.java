package com.taotao.cloud.ccsr.core.remote.raft.processor;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataDeleteRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.core.remote.raft.RaftServer;
import com.taotao.cloud.ccsr.core.serializer.Serializer;
import org.ohara.msc.common.enums.RaftGroup;

public class DeleteRequestRpcProcessor extends AbstractRpcProcessor<MetadataDeleteRequest> {

    public DeleteRequestRpcProcessor(RaftServer server, Serializer serializer) {
        super(server, serializer, true);
    }

    @Override
    public void handleRequest(RpcContext ctx, MetadataDeleteRequest request) {
        super.handleRequest(ctx, request);
    }

    @Override
    protected String extractRaftGroup(MetadataDeleteRequest request) {
        return request.getRaftGroup();
    }

    @Override
    public String interest() {
        return MetadataDeleteRequest.class.getName();
    }
}
