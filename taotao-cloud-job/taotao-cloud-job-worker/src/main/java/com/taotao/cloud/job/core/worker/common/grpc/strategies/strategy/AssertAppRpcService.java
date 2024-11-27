package com.taotao.cloud.job.core.worker.common.grpc.strategies.strategy;

import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.common.constant.RemoteConstant;
import com.taotao.cloud.common.domain.WorkerAppInfo;
import com.taotao.cloud.common.exception.TtcJobException;
import com.taotao.cloud.common.utils.CommonUtils;
import com.taotao.cloud.remote.api.ServerDiscoverGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.worker.common.grpc.strategies.GrpcStrategy;
import com.taotao.cloud.worker.common.grpc.strategies.StrategyManager;
import com.taotao.cloud.worker.subscribe.WorkerSubscribeManager;
import org.springframework.beans.BeanUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class AssertAppRpcService implements GrpcStrategy<TransportTypeEnum> {

    List<ServerDiscoverGrpc.ServerDiscoverBlockingStub> serverDiscoverStubs = new ArrayList<>();
    @Override
    public void init() {
        HashMap<String, ManagedChannel> ip2ChannelsMap = RpcInitializer.getIp2ChannelsMap();
        for (ManagedChannel channel : ip2ChannelsMap.values()) {
            serverDiscoverStubs.add(ServerDiscoverGrpc.newBlockingStub(channel));
        }
    }

    @Override
    public Object execute(Object params) {
        ServerDiscoverCausa.AppName appNameInfo = (ServerDiscoverCausa.AppName) params;
        for (ServerDiscoverGrpc.ServerDiscoverBlockingStub serverDiscoverStub : serverDiscoverStubs) {
            try {
                if(WorkerSubscribeManager.isSplit()) {
                    // 需要分组，依附于新的server，优先选择最小连接的server
                    appNameInfo = ServerDiscoverCausa.AppName.newBuilder().setAppName(appNameInfo.getAppName())
                            .setSubAppName(WorkerSubscribeManager.getSubAppName())
                            .setTargetServer(WorkerSubscribeManager.getServerIpList().get(0))
                            .build();
                    log.info("change server to ip:{}", appNameInfo.getTargetServer());
                }

                // 重置状态，防止多次分组
                WorkerSubscribeManager.setSplitStatus(false);

                ServerDiscoverCausa.AppName finalAppNameInfo = appNameInfo;
                CommonCausa.Response response = CommonUtils.executeWithRetry0(() -> serverDiscoverStub.assertApp(finalAppNameInfo));
                if(response.getCode() == RemoteConstant.SUCCESS){
                    return response.getWorkInfo();
                } else {
                    log.error("[TtcJobWorker] assert appName failed, this appName is invalid, please register the appName  first.");
                    throw new TtcJobException(response.getMessage());
                }
            }
            catch (Exception e){
                log.error("[TtcJobWorker] grpc error");
            }
        }
        log.error("[TtcJobWorker] no available server");
        throw new TtcJobException("no server available");

    }

    @Override
    public TransportTypeEnum getTypeEnumFromStrategyClass() {
        return TransportTypeEnum.ASSERT_APP;
    }
}
