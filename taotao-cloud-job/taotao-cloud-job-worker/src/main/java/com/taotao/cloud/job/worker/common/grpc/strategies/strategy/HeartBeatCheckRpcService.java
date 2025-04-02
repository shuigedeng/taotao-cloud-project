package com.taotao.cloud.job.worker.common.grpc.strategies.strategy;

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.common.exception.KJobException;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.job.worker.common.grpc.strategies.GrpcStrategy;
import com.taotao.cloud.remote.api.ServerDiscoverGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

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
		ServerDiscoverCausa.HeartbeatCheck heartbeatCheck = (ServerDiscoverCausa.HeartbeatCheck) params;
		ServerDiscoverGrpc.ServerDiscoverBlockingStub stub = ip2serverDiscoverStubs.get(heartbeatCheck.getCurrentServer());

		try {
			CommonCausa.Response response = stub.heartbeatCheck(heartbeatCheck);
			if (response.getCode() == RemoteConstant.SUCCESS) {
				return response.getAvailableServer();
			} else {
				log.error("[KJobWorker] heartbeat error");
				throw new KJobException(response.getMessage());
			}
		} catch (Exception e) {
			log.error("[KJobWorker] grpc error");

		}
		return null;

	}

	@Override
	public TransportTypeEnum getTypeEnumFromStrategyClass() {
		return TransportTypeEnum.HEARTBEAT_CHECK;
	}
}
