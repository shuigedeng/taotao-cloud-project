package com.taotao.cloud.job.core.worker.common.grpc.strategies.strategy;

import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.common.constant.RemoteConstant;
import com.taotao.cloud.common.exception.TtcJobException;
import com.taotao.cloud.remote.api.ServerDiscoverGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.worker.common.grpc.strategies.GrpcStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Slf4j
public class HeartBeatCheckRpcService implements GrpcStrategy<TransportTypeEnum> {
    HashMap<String, ServerDiscoverGrpc.ServerDiscoverBlockingStub> ip2serverDiscoverStubs = new HashMap<>();

    @Override
    public void init() {
        HashMap<String, ManagedChannel> ip2ChannelsMap = RpcInitializer.getIp2ChannelsMap();
        for (String ip : ip2ChannelsMap.keySet()) {
            ip2serverDiscoverStubs.put(ip, ServerDiscoverGrpc.newBlockingStub(ip2ChannelsMap.get(ip)));
        }
    }

    @Override
    public Object execute(Object params) {
        ServerDiscoverCausa.HeartbeatCheck heartbeatCheck = (ServerDiscoverCausa.HeartbeatCheck)params;
        ServerDiscoverGrpc.ServerDiscoverBlockingStub stub= ip2serverDiscoverStubs.get(heartbeatCheck.getCurrentServer());

        try {
            CommonCausa.Response response = stub.heartbeatCheck(heartbeatCheck);
            if(response.getCode() == RemoteConstant.SUCCESS){
                return response.getAvailableServer();
            } else {
                log.error("[TtcJobWorker] heartbeat error");
                throw new TtcJobException(response.getMessage());
            }
        }
        catch (Exception e){
            log.error("[TtcJobWorker] grpc error");

        }
        return null;

    }

    @Override
    public TransportTypeEnum getTypeEnumFromStrategyClass() {
        return TransportTypeEnum.HEARTBEAT_CHECK;
    }
}
