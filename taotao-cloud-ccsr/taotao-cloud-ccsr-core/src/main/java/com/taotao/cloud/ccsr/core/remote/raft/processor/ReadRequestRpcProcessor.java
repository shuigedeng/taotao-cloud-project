package com.taotao.cloud.ccsr.core.remote.raft.processor;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataReadRequest;
import com.taotao.cloud.ccsr.api.grpc.auto.MetadataWriteRequest;
import com.taotao.cloud.ccsr.core.remote.raft.RaftServer;
import com.taotao.cloud.ccsr.core.serializer.Serializer;
import org.ohara.msc.common.enums.RaftGroup;

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
